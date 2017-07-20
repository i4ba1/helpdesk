/**
 * Dashboard Controller
 */
(function() {
    'use strict';
    angular.module('application')
        .controller('DashboardController', DashboardController);

    DashboardController.$inject = ['$scope', '$state', '$stateParams', 'RequestFactory'];

    function DashboardController($scope, $state, $stateParams, RequestFactory) {
        $scope.notifications = [];
        $scope.rowCollections = [];
        $scope.products = [];
        $scope.getBackground = getBackground;

        //Get all new notification
        RequestFactory.getUnreadLicenses().then(
            function(response) {
                $scope.notifications = response.data;
                $scope.rowCollections = [].concat($scope.notifications);
            },
            function(error) {
                console.log("notifications not found");
            }
        );

        RequestFactory.getProducts().then(
            function(response) {
                $scope.products = response.data;
            },
            function(error) {
                console.log(error);
            }
        );

        function getBackground(index) {
            var bg = ["bg-red", "bg-yellow", "bg-aqua", "bg-blue", "bg-light-blue", "bg-green", "bg-navy", "bg-teal", "bg-olive", "bg-lime", "bg-orange", "bg-fuchsia", "bg-purple", "bg-maroon", "bg-black"]
            var result = new Object();
            if (index) {
                result[bg[index]] = true;
            } else {
                result[bg[0]] = true;
            }

            return result;

        }


    }

})();