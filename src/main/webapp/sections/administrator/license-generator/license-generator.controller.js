(function() {

    angular.module("application")
        .controller('GeneratorController', generatorController);

    generatorController.$inject = ["RequestFactory", "DialogFactory", "$scope"];

    function generatorController(RequestFactory, DialogFactory, $scope) {
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
            RequestFactory.licenseGenerator(generator).then(
                function(response) {
                    $scope.generatedLicense = response.data;
                    $scope.switchGeneratorForm = "swictToTable"
                },
                function(error) {
                    console.log("Error" + error);
                }
            );
        }



    }

})();