(function() {
    'use strict';
    angular.module("application").controller("SchoolController", SchoolController);
    SchoolController.inject = ['$scope', '$state', '$stateParams'];

    function SchoolController($scope, $state, $stateParams) {
        $scope.rowCollection = [
            { schoolName: 'SDN 03 Riau', createdDate: new Date('1988-05-21') },
            { schoolName: 'SDN 06 Riau', createdDate: new Date('1988-05-17') },
        ];
    }
})();