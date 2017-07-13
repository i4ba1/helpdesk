(function() {


    /**
     * application Module
     *
     * Description
     */
    angular.module('application')
        .controller('ProductManagementController', productManagementController);

    productManagementController.$inject = ["$scope", "RequestFactory", "DialogFactory"];

    function productManagementController($scope, RequestFactory, DialogFactory) {

        $scope.products = [];
        $scope.rowCollections = [];

        // get all products
        RequestFactory.getProducts().then(
            function(response) {
                $scope.products = response.data;
                $scope.rowCollections = [].concat($scope.products);
            },
            function(error) {
                console.log("products not found");
            }
        );
    }

})();