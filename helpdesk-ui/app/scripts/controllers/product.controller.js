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
        $scope.productId = null;
        $scope.productDto = {
            product: {
                id: null,
                productName: null,
                productCode: null,
                description: null,
                subModuleType: "EL",
                subModuleLable: null,
                createdDate: null,
                deleted: false
            },
            subProducts: []
        };

        $scope.submitProductForm = submitProductForm;
        $scope.deleteProduct = deleteProduct;
        $scope.entryType = "direct";
        $scope.entryTypeChange = entryTypeChange;
        $scope.addNewSubProduct = addNewSubProduct;
        $scope.deleteSubProduct = deleteSubProduct;

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
        } else {
            $scope.productId = $stateParams.productId;
            $scope.isUpdate = $scope.productId ? true : false;
        }

        if ($scope.productId) {
            RequestFactory.viewProductDetail($scope.productId).then(
                function(response) {
                    $scope.productDto = response.data;
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

        function submitProductForm(productDto) {
            var result = null;

            if ($scope.productId) {
                result = RequestFactory.updateProduct(productDto);
            } else {
                result = RequestFactory.createProduct(productDto);
            }

            result.then(
                function(response) {
                    DialogFactory.messageDialog("SAVE_SUCCESS", ["SAVE_PRODUCT_SUCCESS"], "sm").then(
                        function(dialogReturn) {
                            $state.go("administrator.product-management");
                        }
                    );
                },
                function(error) {
                    DialogFactory.messageDialog("SAVE_FAILED", ["SAVE_PRODUCT_FAILED"], "sm");
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

        function entryTypeChange(type) {
            if (type && type === "EP") {
                $scope.productDto.subProducts = [{
                    id: null,
                    label: null,
                    value: null
                }];
            } else {
                $scope.productDto.subProducts = [];
            }
        }

        function addNewSubProduct() {
            var newSubProduct = {
                id: null,
                label: null,
                value: null
            }
            $scope.productDto.subProducts.push(newSubProduct);
        }

        function deleteSubProduct(id, index) {
            if (id) {
                RequestFactory.deleteSubProduct(id).then(function(response) {
                    DialogFactory.messageDialog("DELETE_SUCCESS", ["DELETE_DATA_SUCCESS"], "sm");
                }, function(error) {
                    DialogFactory.messageDialog("DELETE_FAILED", ["DELETE_DATA_FAILED"], "sm");
                });
            }
            $scope.productDto.subProducts.splice(index, 1);
        }
    }

})();