(function() {

    angular.module("application")
        .controller('GeneratorController', generatorController);

    generatorController.$inject = ["RequestFactory", "DialogFactory", "$scope", "$state", "$cookies"];

    function generatorController(RequestFactory, DialogFactory, $scope, $state, $cookies) {
        $scope.licenseGeneratorDTO = {
            product: null,
            subProducts: [],
            licenseCount: 0
        };
        //$scope.schools = [];
        $scope.products = [];
        $scope.generatedLicense = [];
        $scope.switchListGenerator = null;
        $scope.submitLicenseGenerator = submitLicenseGenerator;
        $scope.registerGeneratedSN = registerGeneratedSN;
        $scope.listClass = [
            {id:1, name:"SD subProduct 1", value:1},
            {id:2, name:"SD subProduct 2", value:2},
            {id:3, name:"SD subProduct 3", value:3},
            {id:4, name:"SD subProduct 4", value:4},
            {id:5, name:"SD subProduct 5", value:5},
            {id:6, name:"SD subProduct 6", value:6}
        ];
        $scope.switchToSubProduct = null;
        $scope.subProducts = [];

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
            $scope.switchListGenerator = $cookies.get("type");
            if($scope.switchListGenerator === "EL"){
                $scope.generatedLicense = $cookies.getObject("listGenerated").EL;
            }else{
                var listGenerated = $cookies.getObject("listGenerated");
                $scope.generatedLicense = [];
                for(var i=0; i<$cookies.get("licenseCount"); i++){
                    $scope.generatedLicense.push(listGenerated["Paket"+(i+1)]);
                }
                //console.log("The Object is=============> ",Object.keys($scope.generatedLicense).length);
            }
        }

        function submitLicenseGenerator(licenseGeneratorDTO) {
            RequestFactory.licenseGenerator(licenseGeneratorDTO).then(
                function(response) {
                    $scope.generatedLicense = response.data;
                    $cookies.putObject("listGenerated", $scope.generatedLicense);
                    $cookies.put("licenseCount", licenseGeneratorDTO.licenseCount);
                    $cookies.put("type", $scope.licenseGeneratorDTO.product.subModuleType);
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
            RequestFactory.fetchSubProductByProductId(productId).then(
                function (response) {
                    $scope.subProducts = response.data;
                    $scope.switchToSubProduct = "switchToSubProduct";
                },
                function (error) {
                    console.log("getSubProduct "+ error)
                }
            );
        }

        $scope.onSelected = function (selectedProduct) {
            //do selectedProduct.PropertyName like selectedProduct.Name or selectedProduct.Key
            //whatever property your list has.
            console.log("selectedProduct=========> ", selectedProduct);
            if(selectedProduct.subModuleType === "EP"){
                getSubProduct(selectedProduct.id);
            }else{
                $scope.licenseGeneratorDTO.subProducts = [{id:null, label:null, value:null}]
            }
        }
    }

})();