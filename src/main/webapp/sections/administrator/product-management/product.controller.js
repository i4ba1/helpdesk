(function() {


    /**
     * application Module
     *
     * Description
     */
    angular.module('application')
        .controller('ProductManagementController', productManagementController);

    productManagementController.$inject = ["$scope", "$state", "$stateParams", "RequestFactory", "DialogFactory"];

    function productManagementController($scope, $state, $stateParams, RequestFactory, DialogFactory) {

        $scope.products = [];
        $scope.rowCollections = [];
        $scope.productId = $stateParams.productId;
        $scope.product = null;
        $scope.submitProductForm = submitProductForm;
        $scope.deleteProduct = deleteProduct;

        if ($state.is("administrator.product-management")) {
            // get all products
            RequestFactory.getProducts().then(
                function(response) {
                    $scope.products = response.data;
                    $scope.rowCollections = [].concat($scope.products);
                },
                function(error) {
                    DialogFactory.messageDialog("NOTIFICATION", ["DATA_NOT_FOUND"], "sm");
                }
            );
        }

        if ($scope.productId) {
            RequestFactory.productDetail($scope.productId).then(
                function(response) {
                    $scope.product = response.data;
                },
                function(error) {
                    DialogFactory.messageDialog("NOTIFICATION", ["DATA_NOT_FOUND"], "sm").then(
                        function(dialogReturn) {
                            $state.go("administrator.product-management");
                        }
                    );
                }
            );
        }

        function submitProductForm(product) {
            var result = null;

            if ($scope.productId) {
                result = RequestFactory.updateProduct(product);
            } else {
                result = RequestFactory.createProduct(product);
            }

            result.then(
                function(response) {
                    DialogFactory.messageDialog("SAVE_SUCCESS", ["SAVE_SCHOOL_SUCCESS"], "sm").then(
                        function(dialogReturn) {
                            $state.go("administrator.product-management");
                        }
                    );
                },
                function(error) {
                    DialogFactory.messageDialog("SAVE_FAILED", ["SAVE_SCHOOL_FAILED"], "sm");
                });
        }

        function deleteProduct(productId) {
            DialogFactory.confirmationDialog("CONFIRMATION", "DELETE_MESSAGE", "sm").then(
                function(dialogReturn) {
                    RequestFactory.deleteProduct(productId).then(
                        function(response) {
                            DialogFactory.messageDialog("DELETE_SUCCESS", ["DELETE_DATA_SUCCESS"], "sm").then(
                                function(response) {
                                    $state.go("administrator.product-management");
                                },
                                function(dismiss) {}
                            );
                        },
                        function(errorResponse) {
                            DialogFactory.messageDialog("DELETE_FAILED", ["DELETE_DATA_FAILED"], "sm");
                        }
                    );
                },
                function(dismiss) {

                }
            )
        }
    }

})();