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
        var httpHeader = {
            transformRequest: angular.identity,
            headers: {
                'Content-Type': undefined
            }
        };

        var formData;

        var service = {
            getBaseUrl: getBaseUrl,
            getSerialNumber: getSerialNumber,
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
            exportData: exportData,
            register: register
        };

        return service;

        /** ------------------------------------------------------ */

        function getBaseUrl() {
            return baseURL;
        }

        function getSerialNumber(searchModel) {
            if (isAlreadyAuthenticated()) {
                formData = new FormData();
                formData.append("category", searchModel.category.toUpperCase());
                formData.append("searchText", searchModel.searchText);
                formData.append("page", searchModel.page - 1);
                formData.append("startDate", searchModel.startDate);
                formData.append("endDate", searchModel.endDate);
                return $http.post(baseURL + '/snManagement/serialNumbers/', formData, httpHeader);
            }
            return null;

        }

        /**
         * @param  {} model
         */
        function login(model) {
            formData = new FormData();
            formData.append("userName", model.username);
            formData.append("password", model.password);

            return $http.post(baseURL + "/userManagement/loggingIn/", formData, httpHeader);
        }

        function logout() {
            var loggingIn = $cookies.getObject("loggingIn");
            var formData = new FormData();
            formData.append("token", loggingIn.token);
            return $http.post(baseURL + "/userManagement/loggedOut/", formData, httpHeader);
        }

        function isAlreadyAuthenticated() {
            var user = $cookies.getObject("loggingIn");
            var currentMillis = new Date().getTime();
            if (currentMillis > user.expiredTime) {
                $cookies.remove("loggingIn");
                $state.go("login");
                return false;
            }

            return true;
        }

        function activate(licenseId, passkey, reason) {
            if (isAlreadyAuthenticated()) {
                var fd = new FormData();
                fd.append("licenseId", licenseId);
                fd.append("passkey", passkey);
                fd.append("reason", reason);

                return $http.post(baseURL + "/snManagement/activate/", fd, httpHeader);
            }

            return null;
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
            if (isAlreadyAuthenticated()) {
                return $http.get(baseURL + "/productManagement/");
            }
            return null;
        }

        /**
         * View detail of product
         * @param {*} productId
         */
        function viewProductDetail(productId) {
            if (isAlreadyAuthenticated()) {
                return $http.get(baseURL + "/productManagement/productDetail/" + productId);
            }
            return null;
        }

        /**
         * Create new of product
         * @param {*} product
         */
        function createProduct(productDto) {
            if (isAlreadyAuthenticated()) {
                return $http.post(baseURL + "/productManagement/createProduct/", productDto);
            }
            return null;
        }

        /**
         * update current product
         * @param {*} product
         */
        function updateProduct(productDto) {
            if (isAlreadyAuthenticated()) {
                return $http.post(baseURL + "/productManagement/updateProduct/", productDto);
            }
            return null;
        }

        /**
         *
         * @param {*} productId
         */
        function deleteProduct(productId) {
            if (isAlreadyAuthenticated()) {
                return $http.delete(baseURL + "/productManagement/deleteProduct/" + productId);
            }
            return null;
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
            var fd = new FormData();
            fd.append("licenseId", licenseId);
            fd.append("schoolName", schoolName);

            return $http.post(baseURL + "/snManagement/updateSchool/", fd, httpHeader);
        }

        function deleteSchool(schoolId) {
            return $http.delete(baseURL + "/schoolManagement/deleteSchool/" + schoolId);
        }

        /**
         * @param  generator { productId, licenseCount, secondParam}
         * requestType  is GET
         */
        function licenseGenerator(licenseProductDTO) {
            if (isAlreadyAuthenticated()) {
                return $http.post(baseURL + "/snManagement/snGenerator/", licenseProductDTO);
            }
            return null;
        }

        function registerGeneratedSN(snList) {
            if (isAlreadyAuthenticated()) {
                return $http.post(baseURL + "/snManagement/registerGeneratedSN/", snList);
            }
            return null;
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
            if (isAlreadyAuthenticated()) {
                return $http.get(baseURL + "/snManagement/viewDetailSN/" + licenseId);
            }
            return null;
        }

        function overrideActivationLimit(licenseId, message, file) {
            if (isAlreadyAuthenticated()) {
                var formdata = new FormData();
                formdata.append("licenseId", licenseId);
                formdata.append("message", message);
                formdata.append("file", file)

                return $http.post(baseURL + "/snManagement/overrideActivationLimit/", formdata, httpHeader);
            }
            return null;
        }

        function blockLicense(licenseId, message) {
            if (isAlreadyAuthenticated()) {
                formData = new FormData();
                formData.append("licenseId", licenseId);
                formData.append("message", message);
                return $http.post(baseURL + "/snManagement/blocked/", formData, httpHeader);
            }
            return null;
        }

        function exportData(dataExport) {
            var name = "Serial Number " + new Date().toLocaleString().split("/").join("-");
            alasql('SELECT * INTO XLSX("' + name + '.xlsx",{headers:true}) FROM ?', [dataExport, dataExport]);
        }

        function register(license) {
            if (isAlreadyAuthenticated()) {
                return $http.post(baseURL + "/snManagement/registerInHelpdesk/", license);
            }
            return null;
        }
    }

})();