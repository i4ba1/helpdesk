/**
 * Serial Number Controller
 */
(function() {
    'use strict';
    angular.module('application')
        .controller("ProjectSerialNumberController", ProjectSerialNumberController);

    ProjectSerialNumberController.$inject = ['$scope', 'RequestFactory', '$state', "$stateParams", 'DialogFactory'];

    function ProjectSerialNumberController($scope, RequestFactory, $state, $stateParams, DialogFactory) {
        RequestFactory.isAlreadyAuthenticated();

        $scope.rowCollection = [];
        $scope.displayCollection = [];
        $scope.search = "";

        $scope.totalItems = 64;
        $scope.currentPage = 1;
        $scope.maxSize = 5;
        $scope.itemPage = 10;

        $scope.license = null;
        $scope.rembemberCurrentPage = getCurrentPage;
        $scope.activateSerialNumber = activateSerialNumber;

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
                    DialogFactory.activationDialog("ACTIVATION_SUCCESS", "ACTIVATION_SUCCESS_MESSAGES", "md", "Activation Key :" + response.data.activationKey).then(
                        function(response) {
                            getAllSerialNumber();
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

            RequestFactory.viewLicenseDetail(licenseId).then(
                function(response) {
                    $scope.license = response.data;
                },
                function(error) {
                    console.log("data not found" + error);
                }
            );
        } else {
            /**
             *  Call getAllSerialNumber
             */
            getAllSerialNumber();
        }


        function licenseActivation(licenseId) {

        }

        function licenseBlock(licenseId) {
            DialogFactory.confirmationDialog("CONFIRMATION", "BLOCK_CONFIRMATION", "sm").then(
                function(yes) {

                },
                function(no) {}
            );
        }

        function updateSchool(licenseId, schoolName) {

        }
    }

})();