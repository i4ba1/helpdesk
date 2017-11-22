(function() {

    angular.module("application")
        .controller('GeneratorController', generatorController);

    generatorController.$inject = ["RequestFactory", "DialogFactory", "$scope", "$state", "localStorageService", "$rootScope"];

    function generatorController(RequestFactory, DialogFactory, $scope, $state, localStorageService, $rootScope) {
        $scope.licenseGeneratorDTO = {
            product: null,
            subProducts: [],
            licenseCount: 1
        };

        $scope.products = [];
        $scope.generatedLicense = [];
        $scope.switchListGenerator = null;
        $scope.submitLicenseGenerator = submitLicenseGenerator;
        $scope.registerGeneratedSN = registerGeneratedSN;
        $scope.switchToSubProduct = null;
        $scope.subProducts = [];
        $scope.switchPage = "create";
        $scope.checkLimit = checkLimit;
        $scope.maxsize = 1000;

        function submitLicenseGenerator(licenseGeneratorDTO) {
            var isvalid = true;
            var messages = [];
            if (!licenseGeneratorDTO.product) {
                messages.push("PRODUCT_MUST_FILLED");
                isvalid = false;
            }

            if (licenseGeneratorDTO.licenseCount <= 0) {
                messages.push("LICENSE_COUNT_GREATER_THAN_ZERO");
                isvalid = false;
            }

            if (licenseGeneratorDTO.product && licenseGeneratorDTO.subProducts.length === 0) {
                messages.push("SELECT_AT_LEAST_ONE");
                isvalid = false;
            }

            if (isvalid) {
                $rootScope.showOverlay();
                RequestFactory.licenseGenerator(licenseGeneratorDTO).then(
                    function(response) {
                        setTimeout(function() {
                            $rootScope.hideOverlay();
                            $scope.generatedLicense = response.data;
                            // $cookies.putObject("listGenerated", $scope.generatedLicense);
                            localStorageService.set("licenseCount", licenseGeneratorDTO.licenseCount);
                            localStorageService.set("listGenerated", $scope.generatedLicense);
                            localStorageService.set("type", $scope.licenseGeneratorDTO.product.subModuleType);
                            $state.go("administrator.generator.list");
                        }, 2000);
                    },
                    function(error) {
                        $rootScope.hideOverlay();
                        console.log("Error" + error);
                    }
                );
            } else {
                DialogFactory.messageDialog("GENERATE_FAILED", messages, "sm");
            }
        }

        function registerGeneratedSN(generatedSN) {
            var licenses = [];
            $rootScope.showOverlay();
            if ($scope.switchListGenerator === "EP") {
                for (var i = 0; i < localStorageService.get("licenseCount"); i++) {
                    for (var j = 0; j < generatedSN[i].length; j++) {
                        var license = generatedSN[i][j];
                        license.schoolName = $scope.schoolList[i].schoolName;
                        licenses.push(license);
                    }
                }
            } else {
                licenses = generatedSN;
            }

            RequestFactory.registerGeneratedSN(licenses).then(
                function(response) {
                    $rootScope.hideOverlay();
                    exportData(response.data);
                    $state.go("administrator.license");
                },
                function(errorResponse) {
                    $rootScope.hideOverlay();
                    DialogFactory.messageDialog("SAVE_FAILED", ["SAVE_LICENSE_FAILED"], "sm");
                }
            );
        }

        function getSubProduct(productId) {
            RequestFactory.fetchSubProductByProductId(productId).then(
                function(response) {
                    $scope.subProducts = response.data;
                    $scope.switchToSubProduct = "switchToSubProduct";
                    $scope.licenseGeneratorDTO.subProducts = angular.copy($scope.subProducts);
                },
                function(error) {
                    console.log("getSubProduct " + error)
                }
            );
        }

        $scope.onSelected = function(selectedProduct) {
            if (selectedProduct.subModuleType === "EP") {
                getSubProduct(selectedProduct.id);
            } else {
                $scope.licenseGeneratorDTO.subProducts = [{ id: null, label: null, value: null }]
            }
        }

        function exportData(licenseList) {
            var dataExport = [];

            angular.forEach(licenseList, function(row, key) {
                dataExport.push({
                    "No.": key + 1,
                    "Serial Number": row.serialNumber.license,
                    "Produk": row.serialNumber.productName,
                    "Jumlah Pengguna": row.serialNumber.numberOfClient ? row.serialNumber.numberOfClient : 1,
                    "Sekolah": row.serialNumber.schoolName,
                    "Tanggal Dibuat": new Date(row.serialNumber.createdDate).toLocaleDateString()
                })
            });

            RequestFactory.exportData(dataExport);
        }



        function checkLimit() {
            var total = $scope.licenseGeneratorDTO.licenseCount * $scope.licenseGeneratorDTO.subProducts;
            $scope.maxsize = 100;
        }

        if ($state.is("administrator.generator")) {
            RequestFactory.getProducts().then(
                function(response) {
                    $scope.products = response.data;
                },
                function(error) {
                    console.log("cannot fetch product");
                }
            );
        } else if ($state.is("administrator.generator.list")) {
            $scope.switchListGenerator = localStorageService.get("type");
            if ($scope.switchListGenerator === "EL") {
                $scope.generatedLicense = localStorageService.get("listGenerated").EL;
            } else {
                var listGenerated = localStorageService.get("listGenerated");
                $scope.generatedLicense = [];
                $scope.schoolList = [];
                for (var i = 0; i < localStorageService.get("licenseCount"); i++) {
                    $scope.generatedLicense.push(listGenerated["Paket" + (i + 1)]);
                    $scope.schoolList.push({ schoolName: null });
                }
            }
        }
    }

})();