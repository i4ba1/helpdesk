/*
 * @Author: Marlina_Kreatif 
 * @Date: 2017-07-18 10:07:29 
 * @Last Modified by: Margono Sugeng Purwoko
 * @Last Modified time: 2017-08-09 14:56:03
 * 
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
                    templateUrl: "views/login.html",
                    controller: "LoginController as ctrl"
                }
            }
        })

        .state('administrator', {
            url: "/admin",
            views: {
                '': {
                    templateUrl: "views/administrator-home.html",
                    controller: "AdministratorController as adminCtrl"
                }
            }
        })

        .state('administrator.dashboard', {
            url: "/dashboard",
            views: {
                'content@administrator': {
                    templateUrl: "views/dashboard.html",
                    controller: "DashboardController as dashboardCtrl"
                }
            }
        })

        .state('administrator.about-us', {
            url: "/about",
            views: {
                'content@administrator': {
                    templateUrl: "views/about-us.html",
                    controller: "AboutUsController"
                }
            }
        })

        .state('administrator.license', {
            url: "/license",
            views: {
                'content@administrator': {
                    templateUrl: "views/license.html",
                    controller: "ProjectSerialNumberController as snCtrl"
                }
            }
        })

        .state('administrator.license.license-detail', {
            url: "/detail?:licenseId",
            views: {
                'content@administrator': {
                    templateUrl: "views/license-detail.html",
                    controller: "ProjectSerialNumberController as snCtrl"
                }
            }
        })

        .state('administrator.license.activation', {
            url: "/:state",
            views: {
                'content@administrator': {
                    templateUrl: "views/license-acativation.html",
                    controller: "ActivateAndRegisterController as snCtrl"
                }

            }
        })

        .state('administrator.registration', {
            url: "/registration",
            views: {
                'content@administrator': {
                    templateUrl: "views/registration.html",
                    controller: "RegistrationController as regCtrl"
                }
            }
        })

        .state('administrator.school-management', {
            url: "/school-management",
            views: {
                'content@administrator': {
                    templateUrl: "views/school-list.html",
                    controller: "SchoolController as schoolCtrl"
                }
            }
        })

        .state('administrator.school-management.school-detail', {
            url: "/detail?:schoolId",
            views: {
                'content@administrator': {
                    templateUrl: "views/school-detail.html",
                    controller: "SchoolController as schoolCtrl"
                }
            }
        })

        .state('administrator.user-management', {
            url: "/user-management",
            views: {
                'content@administrator': {
                    templateUrl: "views/user-list.html",
                    controller: "UserManagementController as userMgmtCtrl"
                }
            }
        })

        .state('administrator.user-management.user-detail', {
            url: "/detail?:userId",
            views: {
                'content@administrator': {
                    templateUrl: "views/user-detail.html",
                    controller: "UserManagementController as userMgmtCtrl"
                }
            }
        })

        .state('administrator.product-management', {
            url: "/product-management",
            views: {
                'content@administrator': {
                    templateUrl: "views/product-list.html",
                    controller: "ProductManagementController as productMgmtCtrl"
                }
            }
        })

        .state('administrator.product-management.product-detail', {
            url: "/detail?:productId",
            views: {
                'content@administrator': {
                    templateUrl: "views/product-detail.html",
                    controller: "ProductManagementController as productMgmtCtrl"
                }
            }
        })

        .state('administrator.generator', {
            url: "/license-generator",
            views: {
                'content@administrator': {
                    templateUrl: "views/create-generator.html",
                    controller: "GeneratorController as generatorCtrl"
                }
            }

        })

        .state('administrator.generator.list', {
            url: "/list",
            views: {
                'content@administrator': {
                    templateUrl: "views/list-generator.html",
                    controller: "GeneratorController as generatorCtrl"
                }
            }

        });

    }
})();