/**
 * Request Factory
 */
(function() {

    'use strict';
    angular.module('application')
        .factory("RequestFactory", RequestFactory);

    RequestFactory.$inject = ["$http", "$state", "$cookies"];

    function RequestFactory($http, $state, $cookies) {
        var baseURL = "/helpdesk/api";
        var service = {
            getBaseUrl: getBaseUrl,
            getSerialNumber: getSerialNumber,
            createAdmin: createAdmin,
            login: login,
            logout: logout,
            isAlreadyAuthenticated: isAlreadyAuthenticated,
            activate: activate,
            getProducts: getProducts,
            getSchools: getSchools,
            createSchool: createSchool,
            getUsers: getUsers,
            licenseGenerator: licenseGenerator,
            getNotifications: getNotifications
        }

        return service;

        /** ------------------------------------------------------ */

        function getBaseUrl() {
            return baseURL;
        }

        function getSerialNumber() {
            return $http.get(baseURL + '/snManagement');
        }

        /**
         * @param  {} model
         */
        function login(model) {
            var formData = {
                id: "",
                name: "",
                createdDate: "",
                userName: model.username,
                password: model.password
            }
            return $http.post(baseURL + "/userManagement/loggingIn/", formData);
        }

        function createAdmin() {
            var formData = {
                id: "",
                name: "",
                createdDate: "",
                userName: "",
                password: ""
            }
            return $http.post(baseURL + "/userManagement/createUser/", formData);
        }

        function logout() {
            var loggingIn = $cookies.getObject("loggingIn");
            return $http.post(baseURL + "/userManagement/loggedOut/", loggingIn);
        }

        function isAlreadyAuthenticated() {
            var user = $cookies.getObject("loggingIn");
            if (user) {
                return true;
            } else {
                $state.go("login");
                return false;
            }
        }

        function activate(serialNumber) {
            return $http.post(baseURL + "/snManagement/activate/", serialNumber);
        }

        /**
         * get all user data
         */
        function getUsers() {
            return $http.get("assets/dummy/user.dummy.json");
        }

        /**
         * get all product data
         * requestType is GET
         */
        function getProducts() {
            return $http.get("assets/dummy/product.dummy.json");
        }

        /**
         * get all school data
         * requestType is GET
         */
        function getSchools() {
            return $http.get(baseURL + "/schoolManagement/");
        }

        function createSchool(school) {
            var formData = {
                id: null,
                schoolName: school.schoolName,
                schoolAddress: school.schoolAddress,
                createdDate: null,
                deleted: false
            }
            return $http.post(baseURL + "/createSchool/", formData)
        }

        /**
         * @param  generator { selectedProduct,licenseCount,secondParam}
         * requestType  is POST 
         */
        function licenseGenerator(generator) {
            return $http.post("assets/dummy/generated-license.dummy.json");
        }

        function getNotifications() {
            return $http.get("assets/dummy/dashboard.dummy.json");
        }


    }

})();