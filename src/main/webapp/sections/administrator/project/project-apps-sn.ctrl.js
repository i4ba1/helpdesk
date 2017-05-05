/**
 * Serial Number Controller
 */
(function() {
    'use strict';
    angular.module('application')
        .controller("ProjectSerialNumberController", ProjectSerialNumberController);

    ProjectSerialNumberController.$inject = ['$scope', 'RequestFactory', '$state'];

    function ProjectSerialNumberController($scope, RequestFactory, $state) {

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
        
        function activateSerialNumber(serialNumber){
        	
        }
    }

})();