/**
 * Activation And Registration Controller 
 */
(function() {
	'use strict';

	angular.module("application")
		.controller("ActivateAndRegisterController", ActivateAndRegisterController);

	ActivateAndRegisterController.$inject = [ '$scope', '$state', '$stateParams' ];

	function ActivateAndRegisterController(scope, state, stateParams) {
		scope.state = stateParams.state;
		scope.title = scope.state == "activate" ? "ACTIVATE" : "REGISTER";

	}
})();