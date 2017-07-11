(function() {
    'use strict';
    angular.module("application").controller("SchoolController", schoolManagementController);
    schoolManagementController.inject = ['$scope', '$state', '$stateParams'];

    function schoolManagementController($scope, $state, $stateParams) {
        $scope.rowCollection = [
            { schoolName: 'SDN 03 Riau', address: "Jl Kartini IV", createdDate: new Date('1988-05-21') },
            { schoolName: 'SDN 06 Riau', address: "Jl Kartini VI", createdDate: new Date('1988-05-17') },
        ];
    }
})();