/**
 * Login Controller
 */
(function() {
    'use strict';
    angular.module('application')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$scope', '$state', 'RequestFactory', '$cookies', '$window', 'DialogFactory'];

    function LoginController($scope, $state, RequestFactory, $cookies, $window, DialogFactory) {
        var now = new $window.Date();
        if (RequestFactory.isAlreadyAuthenticated()) {
            $state.go('administrator.dashboard');
        } else {
            $scope.login = login;
            $scope.model = { username: null, password: null };
            $scope.fullYear = new Date().getFullYear();
        }

        /**------------------------------------------------------------ */

        function login() {
            var isValid = $scope.loginForm.username.$valid && $scope.loginForm.password.$valid;
            if (isValid) {
                RequestFactory.login($scope.model).then(
                    function(response) {
                        var data = response.data;
                        $cookies.putObject("loggingIn", data, {
                            expires: (new $window.Date(now.getTime() + (30 * 60 * 1000)))
                        });
                        $state.go('administrator.dashboard');
                    },
                    function(responseError) {
                        console.error("Error : " + responseError);
                        if (responseError.status === 404) {
                            DialogFactory.messageDialog("LOGIN_ERROR_TITLE", ["LOGIN_ERROR_MESSAGE"], "md");
                        } else {
                            DialogFactory.messageDialog("LOGIN_ERROR_TITLE", ["LOGIN_UNEXPECTED_MESSAGE"], "md");
                        }
                    }
                );
            }
        }
    }

})();