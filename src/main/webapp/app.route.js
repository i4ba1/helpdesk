/*
 * @Author: Marlina_Kreatif 
 * @Date: 2017-07-18 10:07:29 
 * @Last Modified by: Margono Sugeng Purwoko
 * @Last Modified time: 2017-08-02 09:35:42
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
                    templateUrl: "sections/login/login.html",
                    controller: "LoginController as ctrl"
                }
            }
        })

        .state('administrator', {
            url: "/admin",
            views: {
                '': {
                    templateUrl: "sections/administrator/administrator-home.html",
                    controller: "AdministratorController as ctrl"
                }
            }
        })

        .state('administrator.dashboard', {
            url: "/dashboard",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/dashboard/dashboard.html",
                    controller: "DashboardController as ctrl"
                }
            }
        })

        .state('administrator.about-us', {
            url: "/about",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/about-us/about-us.html",
                    controller: "AboutUsController as ctr"
                }
            }
        })

        .state('administrator.license', {
            url: "/license",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/license-management/license.html",
                    controller: "ProjectSerialNumberController as ctrl"
                }
            }
        })

        .state('administrator.license.license-detail', {
            url: "/detail?:licenseId:historyId",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/license-management/license-detail.html",
                    controller: "ProjectSerialNumberController as ctrl"
                }
            }
        })

        .state('administrator.license.activation', {
            url: "/:state",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/license-management/license-acativation.html",
                    controller: "ActivateAndRegisterController as ctrl"
                }

            }
        })

        .state('administrator.school-management', {
            url: "/school-management",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/school-management/school-list.html",
                    controller: "SchoolController as ctrl"
                }
            }
        })

        .state('administrator.school-management.school-detail', {
            url: "/detail?:schoolId",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/school-management/school-detail.html",
                    controller: "SchoolController as ctrl"
                }
            }
        })

        .state('administrator.user-management', {
            url: "/user-management",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/user-management/user-list.html",
                    controller: "UserManagementController as ctrl"
                }
            }
        })

        .state('administrator.user-management.user-detail', {
            url: "/detail?:userId",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/user-management/user-detail.html",
                    controller: "UserManagementController as ctrl"
                }
            }
        })

        .state('administrator.product-management', {
            url: "/product-management",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/product-management/product-list.html",
                    controller: "ProductManagementController as ctrl"
                }
            }
        })

        .state('administrator.product-management.product-detail', {
            url: "/detail?:productId",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/product-management/product-detail.html",
                    controller: "ProductManagementController as ctrl"
                }
            }
        })

        .state('administrator.generator', {
            url: "/generator",
            views: {
                'content@administrator': {
                    templateUrl: "sections/administrator/license-generator/license-generator.html",
                    controller: "GeneratorController as ctrl"
                }
            }

        });

    }
})();