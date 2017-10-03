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
            viewDetailUnreadLicense: viewDetailUnreadLicense,
            licenseCountByProduct: licenseCountByProduct,
            fetchSubProductByProductId: fetchSubProductByProductId,
            deleteSubProduct: deleteSubProduct,
            viewLicenseDetail: viewLicenseDetail,
            overrideActivationLimit: overrideActivationLimit,
            blockLicense: blockLicense,
            exportData: exportData
        };

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

        function activate(licenseId, passkey, reason) {
            return $http.post(baseURL + "/snManagement/activate/" + licenseId + "/" + passkey + "/" + reason);
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
        function createProduct(productDto) {
            return $http.post(baseURL + "/productManagement/createProduct/", productDto);
        }

        /**
         * update current product
         * @param {*} product 
         */
        function updateProduct(productDto) {
            return $http.put(baseURL + "/productManagement/updateProduct/", productDto);
        }

        /**
         * 
         * @param {*} productId 
         */
        function deleteProduct(productId) {
            return $http.delete(baseURL + "/productManagement/deleteProduct/" + productId);
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

        function updateSchool(licenseId, schoolName) {

            return $http.put(baseURL + "/snManagement/updateSchool/" + licenseId + "/" + schoolName);
        }

        function deleteSchool(schoolId) {
            return $http.delete(baseURL + "/schoolManagement/deleteSchool/" + schoolId);
        }

        /**
         * @param  generator { productId, licenseCount, secondParam}
         * requestType  is GET 
         */
        function licenseGenerator(licenseProductDTO) {
            return $http.post(baseURL + "/snManagement/snGenerator/", licenseProductDTO);
        }

        function registerGeneratedSN(snList) {
            return $http.post(baseURL + "/snManagement/registerGeneratedSN/", snList);
        }

        function licenseCountByProduct() {
            return $http.get(baseURL + "/snManagement/licenseCountByProduct/");
        }

        function fetchSubProductByProductId(productId) {
            return $http.get(baseURL + "/productManagement/fetchSubProduct/" + productId);
        }

        function fetchSubProduct(productId) {
            return $http.get(baseURL + "/productManagement/fetchSubProduct/" + productId);
        }

        function deleteSubProduct(subProductId) {
            return $http.delete(baseURL + "/productManagement/deleteSubProduct/" + subProductId);
        }

        function viewLicenseDetail(licenseId) {
            return $http.get(baseURL + "/snManagement/viewDetailSN/" + licenseId);
        }

        function overrideActivationLimit(licenseId, message) {
            return $http.put(baseURL + "/snManagement/overrideActivationLimit/" + licenseId + "/" + message);
        }

        function blockLicense(licenseId, message) {
            return $http.put(baseURL + "/snManagement/blocked/" + licenseId + "/" + message);
        }

        function exportData(dataExport) {
            alasql('SELECT * INTO XLSX("licenses.xlsx",{headers:true}) FROM ?', [dataExport, dataExport]);
        }
    }

})();