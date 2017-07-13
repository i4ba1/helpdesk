(function() {
    'use strict';

    angular.module("application").controller("SchoolController", schoolManagementController);

    schoolManagementController.$inject = ['$scope', '$state', '$stateParams', 'RequestFactory'];

    function schoolManagementController($scope, $state, $stateParams, RequestFactory) {
        $scope.schools = [];
        $scope.rowCollection = [];


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
    }
})();