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
        $urlRouterProvider.otherwise("/login");

        $stateProvider.state('login', {
            url: "/login",
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

        .state('administrator.dashboard', {
            url: "/dashboard",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/dashboard/dashboard.html",
                    controller: "DashboardController as dashboardCtrl"
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
                    templateUrl: "sections/administrator/license-management/license.html",
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
        })

        .state('administrator.school-management', {
            url: "/school-management",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/school-management/school-list.html",
                    controller: "SchoolController as schoolCtrl"
                }
            }
        })

        .state('administrator.school-management.school-detail', {
            url: "/detail?:schoolId",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/school-management/school-detail.html",
                    controller: "SchoolController as schoolCtrl"
                }
            }
        })

        .state('administrator.user-management', {
            url: "/user-management",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/user-management/user-list.html",
                    controller: "UserManagementController as userMgmtCtrl"
                }
            }
        })

        .state('administrator.user-management.user-detail', {
            url: "/detail?:userId",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/user-management/user-detail.html",
                    controller: "UserManagementController as userMgmtCtrl"
                }
            }
        });

    }
})();