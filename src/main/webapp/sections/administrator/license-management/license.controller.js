/**
 * Serial Number Controller
 */
(function() {
    'use strict';
    angular.module('application')
        .controller("ProjectSerialNumberController", ProjectSerialNumberController);

    ProjectSerialNumberController.$inject = ['$scope', 'RequestFactory', '$state', 'DialogFactory'];

    function ProjectSerialNumberController($scope, RequestFactory, $state, DialogFactory) {
        RequestFactory.isAlreadyAuthenticated();

        $scope.rowCollection = [];
        $scope.displayCollection = [];
        $scope.search = "";

        $scope.totalItems = 64;
        $scope.currentPage = 1;
        $scope.maxSize = 5;
        $scope.itemPage = 10;


        $scope.rembemberCurrentPage = getCurrentPage;
        $scope.activateSerialNumber = activateSerialNumber;

        // Call getAllSerialNumber
        getAllSerialNumber();

        /**------------------------------------------------------*/

        function getAllSerialNumber() {
            $scope.rowCollection = [];
            $scope.displayCollection = [];
            RequestFactory.getSerialNumber().then(
                function(response) {
                    $scope.rowCollection = response.data;
                    $scope.displayCollection = response.data;
                },
                function(error) {
                    console.log(error);
                }
            );
        }

        function getCurrentPage(p) {
            $scope.currentPage = p
        }

        function activateSerialNumber(serialNumber) {
            RequestFactory.activate(serialNumber).then(
                function(response) {
                    DialogFactory.activationDialog("ACTIVATION_SUCCESS", "ACTIVATION_SUCCESS_MESSAGES", "md", "Activation Key :" + response.data.activationKey);
                },
                function(errorResponse) {
                    DialogFactory.activationDialog("ACTIVATION_FAILED", "ACTIVATION_FAILED_MESSAGES", "md", null);
                });
        }

        function openDialog() {}
    }

})();