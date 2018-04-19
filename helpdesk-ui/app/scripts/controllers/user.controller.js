(function() {
    'use strict';

    angular.module('application').controller('UserManagementController', userManagementController);

    userManagementController.$inject = ['$scope', '$state', '$stateParams', 'RequestFactory'];

    function userManagementController($scope, $state, $stateParams, RequestFactory) {
        RequestFactory.isAlreadyAuthenticated();
        $scope.users = [];
        $scope.rowCollection = [];


        // get all school
        RequestFactory.getUsers().then(
            function(response) {
                $scope.users = response.data;
                $scope.rowCollections = [].concat($scope.users);
            },
            function(error) {
                console.log("users not found");
            }
        );

    }
})();