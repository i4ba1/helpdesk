(function() {

    angular.module("application")
        .controller('GeneratorController', generatorController);

    generatorController.$inject = ["RequestFactory", "DialogFactory", "$scope", "$state"];

    function generatorController(RequestFactory, DialogFactory, $scope, $state) {
        $scope.generator = {
            selectedProduct: null,
            licenseCount: null,
            secondParam: null,
        };
        $scope.schools = [];
        $scope.products = [];
        $scope.generatedLicense = [];
        $scope.switchGeneratorForm = null;
        $scope.submitLicenseGenerator = submitLicenseGenerator;
        $scope.registerGeneratedSN = registerGeneratedSN;

        RequestFactory.getSchools().then(
            function(response) {
                $scope.schools = response.data;
            },
            function(error) {
                console.log("cannot fetch school");
            }
        );

        RequestFactory.getProducts().then(
            function(response) {
                $scope.products = response.data;
            },
            function(error) {
                console.log("cannot fetch school");
            }
        );


        function submitLicenseGenerator(generator) {
            RequestFactory.licenseGenerator(generator.selectedProduct.id, generator.licenseCount, generator.secondParam).then(
                function(response) {
                    $scope.generatedLicense = response.data;
                    $scope.switchGeneratorForm = "swictToTable"
                },
                function(error) {
                    console.log("Error" + error);
                }
            );
        }

        function registerGeneratedSN(generatedSN) {
            RequestFactory.registerGeneratedSN(generatedSN).then(
                function(response) {
                    $state.go("administrator.license");
                },
                function(errorResponse) {
                    DialogFactory.messageDialog("SAVE_FAILED", ["SAVE_LICENSE_FAILED"], "sm");
                }
            );
        }



    }

})();