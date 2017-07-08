/**
 * Application Router Configuration
 */
(function() {
    'use strict';
    angular
        .module('application')
        .config(config);

    function config($stateProvider, $urlRouterProvider, uiSelectConfig, $qProvider) {
        $qProvider.errorOnUnhandledRejections(false);
        uiSelectConfig.theme = 'bootstrap';
        $urlRouterProvider.otherwise("/");

        $stateProvider.state('login', {
            url: "/",
            views: {
                '': {
                    templateUrl: "sections/login/login.html",
                    controller: "LoginController as loginCtrl"
                }
            }
        })

        .state('administrator', {
            url: "/admin",
            views: {
                '': {
                    templateUrl: "sections/administrator/administrator-home.html",
                    controller: "AdministratorController as adminCtrl"
                }
            }
        })

        .state('administrator.about-us', {
            url: "/about",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/about-us/about-us.html",
                    controller: "AboutUsController"
                }
            }
        })

        .state('administrator.license', {
            url: "/license",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/license-manangement/license.html",
                    controller: "ProjectSerialNumberController as snCtrl"
                }
            }
        })

        .state('administrator.license.activation', {
            url: "/:state",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/license-management/license-acativation.html",
                    controller: "ActivateAndRegisterController as snCtrl"
                }
            }
        });
    }
})();