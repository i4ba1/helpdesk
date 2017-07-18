(function() {
    'use strict';

    angular.module("application").controller("SchoolController", schoolManagementController);

    schoolManagementController.$inject = ['$scope', '$state', '$stateParams', 'RequestFactory', 'DialogFactory'];

    function schoolManagementController($scope, $state, $stateParams, RequestFactory, DialogFactory) {
        $scope.schools = [];
        $scope.rowCollection = [];
        $scope.schoolId = $stateParams.schoolId;
        $scope.school = null;
        $scope.submitSchoolForm = submitSchoolForm;


        // get all school
        RequestFactory.getSchools().then(
            function(response) {
                $scope.schools = response.data;
                $scope.rowCollections = [].concat($scope.schools);
            },
            function(error) {
                console.log("schools not found");
            }
        );

        function submitSchoolForm(school) {
            RequestFactory.createSchool(school).then(
                function(response) {},
                function(error) {
                    console.log("ERROR :: " + error);
                });


        }
    }
})();