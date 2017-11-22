/**
 * Serial Number Controller
 */
(function() {
    'use strict';
    angular.module('application')
        .controller("ProjectSerialNumberController", ProjectSerialNumberController);

    ProjectSerialNumberController.$inject = ['$scope', 'RequestFactory', '$state', "$stateParams", 'DialogFactory', 'FileSaver', 'Blob', '$rootScope'];

    function ProjectSerialNumberController($scope, RequestFactory, $state, $stateParams, DialogFactory, FileSaver, Blob, $rootScope) {
        RequestFactory.isAlreadyAuthenticated();

        $scope.rowCollection = [];
        $scope.displayCollection = [];
        $scope.search = "";

        $scope.maxSize = 4;
        $scope.itemPage = 20;
        $scope.totalItem;

        $scope.license = null;
        $scope.updateSchool = updateSchool;
        $scope.overrideActivationLimit = overrideActivationLimit;
        $scope.rembemberCurrentPage = getCurrentPage;
        $scope.activateSerialNumber = activateSerialNumber;
        $scope.licenseBlock = licenseBlock;
        $scope.exportFile = exportFile;
        $scope.exportAttachment = exportAttachment;
        $scope.searchLicenseByCategory = searchLicenseByCategory;
        $scope.resetSearchModel = resetSearchModel;

        $scope.searchModel = new Search();

        /**------------------------------------------------------*/
        function getAllSerialNumber(searchModel) {
            $rootScope.showOverlay();
            $scope.rowCollection = [];
            $scope.displayCollection = [];
            RequestFactory.getSerialNumber(searchModel).then(
                function(response) {
                    $scope.rowCollection = response.data.data;
                    $scope.displayCollection = response.data.data;
                    $scope.totalItem = response.data.totalPage;
                    $scope.searchModel.page = response.data.currentPage + 1;
                    $rootScope.hideOverlay();
                },
                function(error) {
                    $rootScope.hideOverlay();
                    console.log(error);
                }
            );
        }

        function getCurrentPage(p) {
            $scope.currentPage = p
        }

        /**
         * this function is no longer used, consider for removing
         * @param {*} serialNumber 
         */
        function activateSerialNumber(serialNumber) {
            RequestFactory.activate(serialNumber).then(
                function(response) {
                    DialogFactory.activationDialog("ACTIVATION_SUCCESS", "ACTIVATION_SUCCESS_MESSAGES", "md", "Activation Key :" + response.data.activationKey).then(
                        function(response) {
                            getAllSerialNumber(new Search());
                        },
                        function(dismiss) {}
                    );
                },
                function(errorResponse) {
                    DialogFactory.activationDialog("ACTIVATION_FAILED", "ACTIVATION_FAILED_MESSAGES", "md", null);
                });
        }

        if ($state.is("administrator.license.license-detail")) {
            var licenseId = $stateParams.licenseId;
            $scope.licenseHistories = [];
            $scope.licenseActivation = licenseActivation;
            $scope.licenseBlock = licenseBlock;
            $scope.disableDetail = false;

            RequestFactory.viewLicenseDetail(licenseId).then(
                function(response) {
                    $scope.license = response.data;
                    $scope.disableDetail = $scope.license.licenseStatus === 4 ? true : false;
                },
                function(error) {
                    console.log("data not found" + error);
                }
            );
        } else {
            /**
             *  Call getAllSerialNumber
             */
            getAllSerialNumber(new Search());
        }


        function licenseActivation() {
            DialogFactory.activationDialog("ACTIVATION").then(
                function(yes) {
                    RequestFactory.activate($stateParams.licenseId, yes.passkey, yes.reason).then(
                        function(response) {
                            DialogFactory.messageDialog("NOTIFICATION", ["SUCCESS_ACTIVATE_NOTIFICATION", "Aktivasi key:" + response.data.activationKey], "sm").then(
                                function() {
                                    //$state.reload();
                                    $state.go("administrator.license.license-detail", {}, { reload: 'administrator.license.license-detail' });
                                }
                            );
                        },
                        function(error) {
                            DialogFactory.messageDialog("ACTIVATION_FAILED", ["ACTIVATION_FAILED_MESSAGES"], "md");
                            console.error(error);
                        }
                    );
                },
                function(no) {}
            );
        }

        function licenseBlock(licenseId) {
            DialogFactory.confirmationWithMessageDialog("CONFIRMATION", "BLOCK_CONFIRMATION", "BLOCK_REASON_TEXT").then(
                function(reason) {
                    RequestFactory.blockLicense($stateParams.licenseId, reason).then(
                        function(response) {
                            DialogFactory.messageDialog("BLOCK_SUCCESS", ["BLOCK_SUCCESS_MESSAGE"], "sm").then(
                                function() {
                                    $state.reload();
                                }
                            );
                        },
                        function(errorResponse) {
                            DialogFactory.messageDialog("BLOCK_FAILED", ["BLOCK_FAILED_MESSAGE"], "sm");
                        }
                    )
                },
                function(no) {}
            );
        }

        function updateSchool(schoolName) {
            RequestFactory.updateSchool($stateParams.licenseId, schoolName).then(
                function(response) {
                    DialogFactory.messageDialog("NOTIFICATION", ["SUCCESS_SCHOOL_UPDATE_NOTIFICATION"], "sm");
                },
                function(error) {
                    DialogFactory.messageDialog("NOTIFICATION", ["FAILED_SCHOOL_UPDATE_NOTIFICATION"], "sm");
                }
            );
        }

        function overrideActivationLimit() {

            DialogFactory.confirmationWithMessageDialog("CONFIRMATION", "OVERRIDE_LIMIT_TEXT", "OVERRIDE_LIMIT_REASON_TEXT", true).then(
                function(result) {
                    RequestFactory.overrideActivationLimit($stateParams.licenseId, result.reason, result.file).then(
                        function(response) {
                            DialogFactory.messageDialog("NOTIFICATION", ["SUCCESS_OVERRIDE_NOTIFICATION"], "sm").then(
                                function() {
                                    $state.reload();
                                }
                            );
                            $scope.license.activationLimit = response.data.activationLimit;
                        },
                        function(error) {
                            DialogFactory.messageDialog("NOTIFICATION", ["FAILED_OVERRIDE_NOTIFICATION"], "sm");
                        }
                    );
                },
                function(no) {}
            );
        }

        function exportFile(exportType) {

            var dataExport = [];
            var data = [];
            if (exportType === 'ALL') {
                data = data.concat($scope.rowCollection);
            } else {
                data = data.concat($scope.displayCollection);
            }

            angular.forEach(data, function(row, key) {
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

        function exportAttachment(history) {
            var dataUri = "data:" + history.fileContentType + ";base64," + history.fileData;
            fetch(dataUri)
                .then(function(response) {
                    return response.blob();
                }).then(function(blobData) {
                    FileSaver.saveAs(blobData, history.fileName);
                });
        }

        function searchLicenseByCategory(searchModel) {
            var sm = new Search(searchModel.category, searchModel.searchText, searchModel.page);
            if (searchModel.category === 'date') {
                sm.startDate = getTime(searchModel.startDate.toString());
                sm.endDate = getTime(searchModel.endDate.toString());
            }
            getAllSerialNumber(sm);
        }

        function resetSearchModel() {
            $scope.searchModel.searchText = "";
            $scope.searchModel.startDate = "";
            $scope.searchModel.endDate = "";
        }

        function getTime(dateString) {
            var day = parseInt(dateString.substring(0, 2)),
                month = parseInt(dateString.substring(2, 4)),
                year = parseInt(dateString.substring(4, dateString.length));

            var date = new Date(year, month - 1, day, 0, 0, 0, 0);
            return date.getTime();
        }

        function Search(category, searchText, page, startDate, endDate) {
            this.searchText = searchText ? searchText : "";
            this.category = category ? category : "sn";
            this.page = page ? page : 1;
            this.startDate = startDate ? startDate : 0;
            this.endDate = endDate ? startDate : 0;
        }
    }

})();