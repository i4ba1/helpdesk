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
                        controller: function($rootScope, $state, RequestFactory) {
                            RequestFactory.isAlreadyAuthenticated();
                        }
                    }
                }
            })
            .state('administrator.sn', {
                url: "/sn",
                views: {
                    'content@administrator': {
                        templateUrl: "sections/administrator/project/project-apps-sn.html",
                        controller: "ProjectSerialNumberController as snCtrl"
                    }
                }
            })
            
            .state('administrator.sn.reg-actv', {
                url: "/:state",
                views: {
                    'content@administrator': {
                        templateUrl: "sections/administrator/project/activate-and-register.html",
                        controller: "ActivateAndRegisterController as snCtrl"
                    }
                }
            })
            
            .state('administrator.schoolMgmt', {
                url: "/school-management",
                views: {
                    'content@administrator': {
                        templateUrl: "sections/administrator/school-management/school-list.html",
                        controller: "SchoolController as schoolCtrl"
                    }
                }
            })

            .state('administrator.schoolMgmt.schoolDetail', {
                url: "/school/list",
                views: {
                    'content@administrator': {
                        templateUrl: "sections/administrator/school-management/school-detail.html",
                        controller: "SchoolController as schoolCtrl"
                    }
                }
            });
    }
})();