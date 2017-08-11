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
        $scope.updateSchool = updateSchool;
        $scope.overrideActivationLimit = overrideActivationLimit;
        $scope.rembemberCurrentPage = getCurrentPage;
        $scope.activateSerialNumber = activateSerialNumber;
        $scope.licenseBlock = licenseBlock;
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
            $scope.disableDetail = false;

            RequestFactory.viewLicenseDetail(licenseId).then(
                function(response) {
                    $scope.license = response.data;
                    $scope.disableDetail = $scope.license.licenseStatus === 4 ? true : false;
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


        function licenseActivation() {
            DialogFactory.activationDialog("ACTIVATION").then(
                function(yes) {
                    RequestFactory.activate($stateParams.licenseId, yes.passkey, yes.reason).then(
                        function(response) {
                            DialogFactory.messageDialog("NOTIFICATION", ["SUCCESS_ACTIVATE_NOTIFICATION", "Aktivasi key:" + response.data.activationKey], "sm").then(
                                function() {
                                    //$state.reload();
                                    $state.go("administrator.license.license-detail", {}, {reload: 'administrator.license.license-detail'});
                                }
                            );
                        },
                        function(error) {

                        }
                    );
                },
                function(no) {}
            );
        }

        function licenseBlock(licenseId) {
            DialogFactory.confirmationWithMessageDialog("CONFIRMATION", "BLOCK_CONFIRMATION", "BLOCK_REASON_TEXT").then(
                function(reason) {
                    RequestFactory.blockLicense($stateParams.licenseId, reason).then(
                        function(response) {
                            DialogFactory.messageDialog("BLOCK_SUCCESS", ["BLOCK_SUCCESS_MESSAGE"], "sm").then(
                                function() {
                                    $state.reload();
                                }
                            );
                        },
                        function(errorResponse) {
                            DialogFactory.messageDialog("BLOCK_FAILED", ["BLOCK_FAILED_MESSAGE"], "sm");
                        }
                    )
                },
                function(no) {}
            );
        }

        function updateSchool(schoolName) {
            RequestFactory.updateSchool($stateParams.licenseId, schoolName).then(
                function(response) {
                    DialogFactory.messageDialog("NOTIFICATION", ["SUCCESS_SCHOOL_UPDATE_NOTIFICATION"], "sm");
                },
                function(error) {
                    DialogFactory.messageDialog("NOTIFICATION", ["FAILED_SCHOOL_UPDATE_NOTIFICATION"], "sm");
                }
            );
        }

        function overrideActivationLimit() {

            DialogFactory.confirmationWithMessageDialog("CONFIRMATION", "OVERRIDE_LIMIT_TEXT", "OVERRIDE_LIMIT_REASON_TEXT").then(
                function(reason) {
                    RequestFactory.overrideActivationLimit($stateParams.licenseId, reason).then(
                        function(response) {
                            DialogFactory.messageDialog("NOTIFICATION", ["SUCCESS_OVERRIDE_NOTIFICATION"], "sm").then(
                                function() {
                                    $state.reload();
                                }
                            );
                            $scope.license.activationLimit = response.data.activationLimit;
                        },
                        function(error) {
                            DialogFactory.messageDialog("NOTIFICATION", ["FAILED_OVERRIDE_NOTIFICATION"], "sm");
                        }
                    );
                },
                function(no) {}
            );


        }
    }

})();