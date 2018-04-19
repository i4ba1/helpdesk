(function() {
    'use strict';

    angular.module("application").controller("SchoolController", schoolManagementController);

    schoolManagementController.$inject = ['$scope', '$state', '$stateParams', 'RequestFactory', 'DialogFactory'];

    function schoolManagementController($scope, $state, $stateParams, RequestFactory, DialogFactory) {
        RequestFactory.isAlreadyAuthenticated();
        $scope.schools = [];
        $scope.rowCollection = [];
        $scope.schoolId = $stateParams.schoolId;
        $scope.school = null;
        $scope.submitSchoolForm = submitSchoolForm;
        $scope.deleteSchool = deleteSchool;

        if ($state.is("administrator.school-management")) {

            // get all school
            RequestFactory.getSchools().then(
                function(response) {
                    $scope.schools = response.data;
                    $scope.rowCollections = [].concat($scope.schools);
                },
                function(error) {
                    DialogFactory.messageDialog("NOTIFICATION", ["DATA_NOT_FOUND"], "sm");
                }
            );

        }

        if ($scope.schoolId) {
            RequestFactory.schoolDetail($scope.schoolId).then(
                function(response) {
                    $scope.school = response.data;
                },
                function(error) {
                    DialogFactory.messageDialog("NOTIFICATION", ["DATA_NOT_FOUND"], "sm").then(
                        function(dialogReturn) {
                            $state.go("administrator.school-management");
                        },
                        function(dismiss) {}
                    );
                }
            );
        }

        function submitSchoolForm(school) {
            var result = null;

            if ($scope.schoolId) {
                result = RequestFactory.updateSchool(school);
            } else {
                result = RequestFactory.createSchool(school);
            }

            result.then(
                function(response) {
                    DialogFactory.messageDialog("SAVE_SUCCESS", ["SAVE_SCHOOL_SUCCESS"], "sm").then(
                        function(dialogReturn) {
                            $state.go("administrator.school-management");
                        }
                    );
                },
                function(error) {
                    DialogFactory.messageDialog("SAVE_FAILED", ["DATA_ALREADY_EXIST"], "sm");
                });
        }

        function deleteSchool(schoolId) {
            DialogFactory.confirmationDialog("CONFIRMATION", "DELETE_MESSAGE", "sm").then(
                function(dialogReturn) {
                    RequestFactory.deleteSchool(schoolId).then(
                        function(response) {
                            DialogFactory.messageDialog("DELETE_SUCCESS", ["DELETE_DATA_SUCCESS"], "sm").then(
                                function(response) {
                                    $state.go("administrator.school-management");
                                },
                                function(dismiss) {}
                            );
                        },
                        function(errorResponse) {
                            DialogFactory.messageDialog("DELETE_FAILED", ["DELETE_DATA_FAILED"], "sm");
                        }
                    )
                },
                function(dismiss) {

                }
            )
        }


    }
})();