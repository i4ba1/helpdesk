(function() {

    angular.module("application")
        .controller('GeneratorController', generatorController);

    generatorController.$inject = ["RequestFactory", "DialogFactory", "$scope", "$state", "$cookies"];

    function generatorController(RequestFactory, DialogFactory, $scope, $state, $cookies) {
        $scope.generator = {
            selectedProduct: null,
            licenseCount: null,
            subProduct: [],
        };
        //$scope.schools = [];
        $scope.products = [];
        $scope.generatedLicense = [];
        $scope.switchListGenerator = null;
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
        $scope.switchToSubProduct = null;
        $scope.subProduct = [];

        /*RequestFactory.getSchools().then(
            function(response) {
                $scope.schools = response.data;
            },
            function(error) {
                console.log("cannot fetch school");
            }
        );*/

        if($state.is("administrator.generator")){
            RequestFactory.getProducts().then(
                function(response) {
                    $scope.products = response.data;
                },
                function(error) {
                    console.log("cannot fetch school");
                }
            );
        }else if($state.is("administrator.generator.list")){
            $scope.generatedLicense = $cookies.getObject("listGenerated");
            $scope.switchListGenerator = $cookies.get("type");
        }

        function submitLicenseGenerator(generator) {
            RequestFactory.licenseGenerator(generator.selectedProduct, generator.licenseCount, generator.subProduct).then(
                function(response) {
                    $scope.generatedLicense = response.data;
                    $cookies.putObject("listGenerated", $scope.generatedLicense);
                    $cookies.put("type", $scope.generator.selectedProduct.subProductType);
                    $state.go("administrator.generator.list");
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

        function getSubProduct(productId){
            RequestFactory.fetchSubProduct(productId).then(
                function (response) {
                    $scope.subProduct = response.data;
                    $scope.switchToSubProduct = "switchToSubProduct";
                },
                function (error) {
                    console.log("getSubProduct "+ error)
                }
            );
        }

        $scope.onSelected = function (selectedItem) {
            //do selectedItem.PropertyName like selectedItem.Name or selectedItem.Key
            //whatever property your list has.
            console.log("selectedItem=========> ", selectedItem);
            getSubProduct(1);
        }
    }

})();