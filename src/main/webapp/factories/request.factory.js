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
            createProduct: createProduct,
            viewProductDetail: viewProductDetail,
            updateProduct: updateProduct,
            deleteProduct: deleteProduct,
            getProducts: getProducts,
            getUnreadLicenses: getUnreadLicenses,
            getSchools: getSchools,
            createSchool: createSchool,
            schoolDetail: schoolDetail,
            updateSchool: updateSchool,
            deleteSchool: deleteSchool,
            getUsers: getUsers,
            licenseGenerator: licenseGenerator,
            registerGeneratedSN: registerGeneratedSN,
            getNotifications: getNotifications,
            viewDetailUnreadLicense: viewDetailUnreadLicense
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
         * Fetch all unread license
         */
        function getUnreadLicenses() {
            return $http.get(baseURL + "/snManagement/findUnreadLicenses/");
        }

        /**
         * View detail of unread license
         */
        function viewDetailUnreadLicense(licenseId, historyId) {
            return $http.get(baseURL + "/snManagement/viewDetailUnreadLicense/" + licenseId + "/" + historyId);
        }

        /**
         * get all user data
         */
        function getUsers() {
            return $http.get("assets/dummy/user.dummy.json");
        }

        /**
         * get all products
         */
        function getProducts() {
            return $http.get(baseURL + "/productManagement/");
        }

        /**
         * View detail of product
         * @param {*} productId 
         */
        function viewProductDetail(productId) {
            return $http.get(baseURL + "/productManagement/productDetail/" + productId);
        }

        /**
         * Create new of product
         * @param {*} product 
         */
        function createProduct(product) {
            var formData = {
                id: null,
                productName: product.productName,
                productCode: product.productCode,
                createdDate: null,
                deleted: false
            };

            return $http.post(baseURL + "/productManagement/createProduct/", formData);
        }

        /**
         * update current product
         * @param {*} product 
         */
        function updateProduct(product) {
            var formData = {
                id: product.id,
                productName: product.productName,
                productCode: product.productCode,
                createdDate: product.createdDate,
                deleted: false
            };

            return $http.put(baseURL + "/productManagement/updateProduct/", formData);
        }

        /**
         * 
         * @param {*} productId 
         */
        function deleteProduct(productId) {
            return $http.deleted(baseURL + "/productManagement/deleteProduct/" + productId);
        }

        /**
         * get all school data
         * requestType is GET
         */
        function getSchools() {
            return $http.get(baseURL + "/schoolManagement/");
        }

        function schoolDetail(schoolId) {
            return $http.get(baseURL + "/schoolManagement/schoolDetail/" + schoolId);
        }

        function createSchool(school) {
            var formData = {
                id: null,
                schoolName: school.schoolName,
                schoolAddress: school.schoolAddress,
                createdDate: null,
                deleted: false
            }
            return $http.post(baseURL + "/schoolManagement/createSchool/", formData);
        }

        function updateSchool(school) {
            var formData = {
                id: school.id,
                schoolName: school.schoolName,
                schoolAddress: school.schoolAddress,
                createdDate: school.createdDate,
                deleted: school.deleted
            }
            return $http.put(baseURL + "/schoolManagement/updateSchool/", formData);
        }

        function deleteSchool(schoolId) {
            return $http.delete(baseURL + "/schoolManagement/deleteSchool/" + schoolId);
        }

        /**
         * @param  generator { productId, licenseCount, secondParam}
         * requestType  is GET 
         */
        function licenseGenerator(productId, licenseCount, secondParam) {
            return $http.get(baseURL + "/snManagement/snGenerator/" + productId + "/" + licenseCount + "/" + secondParam);
        }

        function registerGeneratedSN(snList) {
            return $http.post(baseURL + "/snManagement/registerGeneratedSN/", snList);
        }

        function getNotifications() {
            return $http.get("assets/dummy/dashboard.dummy.json");
        }




    }

})();