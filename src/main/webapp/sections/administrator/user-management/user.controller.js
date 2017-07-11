(function() {
    'use strict';

    angular.module('application').controller('UserManagementController', userManagementController);

    userManagementController.$inject = ['$scope', '$state', '$stateParams'];

    function userManagementController($scope, $state, $stateParams) {
        $scope.rowCollection = [
            { firstName: 'Muhammad', lastName: "Uways", userName: "muhammad.uways", createdDate: new Date('2017-05-21'), role: "User" },
            { firstName: 'Muhammad', lastName: "Thoha", userName: "muhammad.thoha", createdDate: new Date('2017-05-21'), role: "User" },
        ];
    }
})();