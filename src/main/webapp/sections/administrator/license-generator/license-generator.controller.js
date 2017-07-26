(function() {

    angular.module("application")
        .controller('GeneratorController', generatorController);

    generatorController.$inject = ["RequestFactory", "DialogFactory", "$scope", "$state"];

    function generatorController(RequestFactory, DialogFactory, $scope, $state) {
        $scope.generator = {
            selectedProduct: null,
            licenseCount: null,
            secondParam: null,
            subProduct: [],
        };
        $scope.schools = [];
        $scope.products = [];
        $scope.generatedLicense = [];
        $scope.switchGeneratorForm = null;
        $scope.submitLicenseGenerator = submitLicenseGenerator;
        $scope.registerGeneratedSN = registerGeneratedSN;
        $scope.listClass = [
            {id:1, name:"SD kelas 1", value:1},
            {id:2, name:"SD kelas 2", value:2},
            {id:3, name:"SD kelas 3", value:3},
            {id:4, name:"SD kelas 4", value:4},
            {id:5, name:"SD kelas 5", value:5},
            {id:6, name:"SD kelas 6", value:6}
        ];


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