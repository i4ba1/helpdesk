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
        var isAdminCreated = $cookies.get("isAdminCreated");

        if (!isAdminCreated) {
            var createCookies = false;
            // Create Admin
            RequestFactory.createAdmin().then(function(repsonse) {
                createCookies = true;
            }, function(responseError) {
                if (responseError.status > 0) {
                    createCookies = true;
                }
            }).then(function() {
                if (createCookies) {
                    $cookies.put("isAdminCreated", true, {
                        expires: (new $window.Date(now.getFullYear(), now.getMonth(), now.getDate() + 2))
                    });
                    console.log("Cookies Created");
                }
            });
        }


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
                            expires: (new $window.Date(now.getFullYear(), now.getMonth(), now.getDate() + 2))
                        });
                        $state.go('administrator.dashboard');
                    },
                    function(responseError) {
                        console.error("Error : " + responseError);
                        DialogFactory.errorDialog("ERROR_LOGIN_TITLE", ["LOGIN_ERROR_MESSAGE"], "md");
                    }
                );
            }
        }
    }

})();