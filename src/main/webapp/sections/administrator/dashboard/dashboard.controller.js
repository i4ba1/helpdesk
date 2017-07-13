/**
 * Dashboard Controller
 */
(function() {
	'use strict';
	angular.module('application')
		.controller('DashboardController', DashboardController);

	DashboardController.$inject = [ '$scope', '$state', '$stateParams', 'RequestFactory' ];

	function DashboardController($scope, $state, $stateParams, RequestFactory) {
		$scope.notifications = [];
		$scope.rowCollections = [];

		//Get all new notification
		RequestFactory.getNotifications().then(
			function(response) {
				$scope.notifications = response.data;
				$scope.rowCollections = [].concat($scope.notifications);
			},
			function(error) {
				console.log("notifications not found");
			}
		);
		console.log("");
	}

})();