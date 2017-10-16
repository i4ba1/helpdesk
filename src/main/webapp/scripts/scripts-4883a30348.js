/**
 * Application Module
 */
(function() {
    'use strict';
    angular
        .module('application', [
            'ngAnimate',
            'ngCookies',
            'ngSanitize',
            'smart-table',
            'ui.tinymce',
            'pascalprecht.translate',
            'ui.select',
            'ui.bootstrap',
            'ui.router',
            "checklist-model",
            'LocalStorageModule',
            'ngFileSaver',
            "bsLoadingOverlay",
            'angularSpinner'
        ])
        .config(["localStorageServiceProvider", "usSpinnerConfigProvider", config]);

    function config(localStorageServiceProvider, usSpinnerConfigProvider) {
        localStorageServiceProvider.setPrefix('helpdesk');
        localStorageServiceProvider.setDefaultToCookie(false);
        localStorageServiceProvider.setStorageType('localStorage');

        usSpinnerConfigProvider.setDefaults({ color: 'gray' });
    }

})();
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
    config.$inject = ["$stateProvider", "$urlRouterProvider", "uiSelectConfig", "$qProvider"];
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
/*
 * @Author: Marlina_Kreatif 
 * @Date: 2017-07-18 10:06:11 
 * @Last Modified by: Margono Sugeng Purwoko
 * @Last Modified time: 2017-08-10 14:42:43
 * 
 * Application Translate Configuration
 */

(function() {
    'use strict';
    translateConfiguration.$inject = ["$translateProvider"];
    angular
        .module('application')
        .config(translateConfiguration);

    function translateConfiguration($translateProvider) {

        $translateProvider.translations('en', {
            WELCOME: "Welcome",
            USERNAME_PLH: "Username",
            PASSWORD_PLH: "Password",
            REMEMBER_ME: "remember me",
            LOGIN: "Login",
            LOGOUT: "Logout",
            EXECUTIVE_PRODUCER: "Executive Producer",
            PROJECT_MANAGER: "Project Manager",
            SYSTEM_ARCHITECT: "System Architect",
            UI_UX_DESIGNER: "UI/UX Designer",
            PROGRAMMER: "Programmer",
            PHONE: "Phone",
            FAX: "Fax",
            EMAIL: "Email",
            COMPANY: "Company",
            WEBSITE: "Website",
            SCHOOL: "School"
        });
        $translateProvider.translations('id', {
            WELCOME: "Selamat Datang",
            REMEMBER_ME: "ingat saya",
            LOGIN: "Masuk",
            LOGOUT: "Keluar",
            EXECUTIVE_PRODUCER: "Executive Producer",
            PROJECT_MANAGER: "Project Manager",
            SYSTEM_ARCHITECT: "System Architect",
            UI_UX_DESIGNER: "UI/UX Designer",
            PROGRAMMER: "Programmer",
            PHONE: "Nomor Telepon",
            FAX: "Fax",
            EMAIL: "Email",
            COMPANY: "Perusahaan",
            WEBSITE: "Website",
            DASHBOARD: "Beranda",
            ABOUT_US: "Tentang Kami",
            KNT_APPS: "KNT Apps",
            PROJECT_APPS: "Project Apps",
            SERIAL_NUMBER: "Serial Number",
            PASS_KEY: "Pass Key",
            ACTIVATION_KEY: "Activation Key",
            REGISTRATION_DATE: "Tanggal Registrasi",
            APP_TYPE: "Tipe App",
            SERIAL_NUMBER_SEARCH: "Pencarian Serial Number...",
            FILTER_SERIAL_NUMBER: "Saring Serial Number...",
            FILTER_PASS_KEY: "Saring Pass Key...",
            FILTER_ACTIVATION_KEY: "Saring Activation Key...",
            FILTER_REGISTRATION_DATE: "Saring Tanggal..",
            FILTER_TYPE_APP: "Saring Tipe App",
            MAC_ADDR: "MAC",
            XLOCK: "XLock",
            SN_STATUS: "SN Status",
            ACTIVATE: 'Aktivasi',
            REACTIVATE: 'Activasi Ulang',
            ACTIVATED: "Teraktivasi",
            UNACTIVATED: "Belum Teraktivasi",
            ACTIVATION_SUCCESS: "Aktifasi Berhasil",
            ACTIVATION_FAILED: "Aktifasi Gagal",
            ACTIVATION_SUCCESS_MESSAGES: "Aktifasi Serial Number Berhasil",
            ACTIVATION_FAILED_MESSAGES: "Aktifasi Serial Number Gagal",
            SCHOOL: "Sekolah",
            SCHOOL_MANAGEMENT: "Manajement Sekolah",
            SCHOOL_LIST: "Daftar Sekolah",
            SCHOOL_DETAIL: "Detail Sekolah",
            SCHOOL_ADD: "Tambah Sekolah",
            LOGIN_ERROR_TITLE: "Login Gagal",
            LOGIN_ERROR_MESSAGE: "Maaf Nama Pengguna atau Kata Sandi Salah!",
            SCHOOL_ADDRESS: "Alamat Sekolah",
            SCHOOL_NAME: "Nama Sekolah",
            SCHOOL_ADDRESS_PLH: "Masukan alamat sekolah",
            SCHOOL_NAME_PLH: "Masukan nama sekolah",
            REGISTER_DATE: "Tanggal Daftar",
            USER_MANAGEMENT: "Manajemen Pengguna",
            FIRST_NAME: "Nama Depan",
            FIRST_NAME_PLH: "Masukkan nama belakang",
            LAST_NAME: "Nama Belakang",
            LAST_NAME_PLH: "Masukkan nama belakang",
            USERNAME: "Nama Pengguna",
            USERNAME_PLH: "Masukkan nama pengguna",
            UPDATE: "Ubah",
            SAVE: "Simpan",
            CANCEL: "Batal",
            PASSWORD: "Kata Sandi",
            PASSWORD_PLH: "Masukkan kata sandi",
            CREATED_DATE: "Tanggal dibuat",
            ROLE: "Role",
            PRODUCT: "Produk",
            PRODUCT_NAME: "Nama Produk",
            PRODUCT_NAME_PLH: "Masukan nama produk",
            PRODUCT_CODE: "Kode Produk",
            PRODUCT_CODE_PLH: "Masukan kode produk",
            CODE: "Kode",
            PRODUCT_MANAGEMENT: "Manajemen Produk",
            GENERATOR: "Generator",
            LICENSE_COUNT: "Jumlah Lisensi",
            LICENSE_COUNT_PLH: "Masukan jumlah lisensi",
            SECOND_PARAM: "Parameter",
            SECOND_PARAM_PLH: "Masukan jumlah client",
            SELECT_PRODUCT: "Pilih Produk",
            SELECT_SCHOOL: "Pilih Sekolah",
            NO: "No.",
            NOTIFICATION: "Pemberitahuan",
            SAVE_SUCCESS: "Simpan Data Berhasil",
            SAVE_FAILED: "Simpan Data Gagal",
            SAVE_SCHOOL_SUCCESS: "Data sekolah berhasil disimpan",
            SAVE_SCHOOL_FAILED: "Data Sekolah gagal disimpan",
            SAVE_PRODUCT_SUCCESS: "Data product berhasil disimpan",
            SAVE_PRODUCT_FAILED: "Data product gagal disimpan",
            DATA_NOT_FOUND: "Data tidak ditemukan!",
            YES: "Ya",
            NOT: "Tidak",
            DELETE_MESSAGE: "Anda yakin hapus data ini ?",
            CONFIRMATION: "Konfirmasi",
            DELETE_SUCCESS: "Berhasil Hapus",
            DELETE_FAILED: "Gagal Hapus",
            DELETE_DATA_SUCCESS: "Berhasil menghapus data ini !",
            DELETE_DATA_FAILED: "Gagal menghapus data ini !",
            DATA_ALREADY_EXIST: "Data sudah pernah disimpan !",
            SAVE_LICENSE_FAILED: "Simpan data serial number gagal",
            SAVE_LICENSE_SUCCESS: "Simpan data serial number berhasil",
            LICENSE: "Serial number",
            LICENSE_STATUS: "Status",
            GENERATE: "Generate",
            LABEL_TXT: "Label Sub Modul",
            LABEL_TXT_PLH: "Masukan label sub module",
            DESCRIPTION: "Deskripsi",
            DESCRIPTION_PLH: "Masukan deskripsi pengisian data",
            DESCRIPTION_DIRECT_ENTRY: "Pengisian sub modul akan dilakukan saat melakukan pembuatan serial number baru pada menu '<b><i>GENERATOR</i></b>'",
            SUB_MODULE_TYPE: "Jenis Sub Modul",
            DIRECT_ENTRY: "Entri Langsung",
            CHOICE_ENTRY: "Entri Pilihan",
            CHOICE_TEXT: "Data Pilihan",
            ADD_CHOICE: "Tambah Pilihan",
            LABEL: 'Label',
            VALUE: 'Value',
            NUMBER_CLIENT: "Jml. Pengguna",
            GENERATE_FAILED: "Generate SN Gagal",
            PRODUCT_MUST_FILLED: "Produk harus dipilih",
            LICENSE_COUNT_GREATER_THAN_ZERO: "Jumlah lisensi minimal 1",
            SELECT_AT_LEAST_ONE: "Pilihlah minimal 1 dari pilihan sub produk",
            CLASS: "Kelas",
            ACTIVATION_COUNT: "Jml. Aktivasi",
            LICENSE_HISTORY: "Riwayat Serial Number",
            LICENSE_HISTORY_NOT_FOUND: "data riwayat serial number tidak ditemukan",
            STATUS: "Status",
            DATE: "Tanggal",
            BLOCK: "Blokir",
            ADD_KUOTA: "Tambah Kuota",
            BLOCK_CONFIRMATION: "Apakah yakin untuk melakukan blokir serial number ?",
            BLOCK_REASON_TEXT: "masukanlah alasan pemblokiran serial number...!",
            SUCCESS_SCHOOL_UPDATE_NOTIFICATION: "Nama sekolah telah di update",
            FAILED_SCHOOL_UPDATE_NOTIFICATION: "Nama sekolah gagal di update",
            SUCCESS_OVERRIDE_NOTIFICATION: "Limit aktivasi berhasil ditambahkan",
            FAILED_OVERRIDE_NOTIFICATION: "Limit aktivasi gagal ditambahkan",
            OVERRIDE_REASON_TEXT: "Masukkanlah alasan untuk menambah limit aktivasi",
            OVERRIDE_CONFIRMATION: "Apakah yakin untuk melakukan menambah limit aktivasi ?",
            PASSKEY_PLH: "Masukkan passkey",
            PASKEY_LABEL: "Passkey",
            SAVE_TO_XLSX_CONFIRMATION: "Simpan data berhasil, apakah data akan diekspor ke file XLSX ?",
            RELOAD_NOTIFICATION: "Data belum tersimpan, apakah yakin keluar dari halaman ini?",
            PASSKEY_LABEL: "Passkey",
            FAILED_ACTIVATE_NOTIFICATION: "Aktivasi gagal",
            SUCCESS_ACTIVATE_NOTIFICATION: "Aktivasi berhasil",
            REASON_ACTIVATE: "Masukkan alasan aktivasi",
            OVERRIDE_LIMIT_TEXT: "Apakah yakin untuk menambah limit aktivasi ?",
            OVERRIDE_LIMIT_REASON_TEXT: "Masukan alasan menambah aktivasi limit !",
            BLOCK_SUCCESS: "Blokir Berhasil",
            BLOCK_FAILED: "Blokir Gagal",
            BLOCK_SUCCESS_MESSAGE: "Blokir serial number berhasil",
            BLOCK_FAILED_MESSAGE: "Blokir serial number gagal",
            EXPORT_CURRENT_PAGE: "Ekspor Lisensei 1 Halaman",
            EXPORT_ALL_PAGE: "Ekspor Semua Lisense ",
            UPLOAD_FILE: "Unggah tanda bukti permintaan!",
            DOWNLOAD_ATTACHMENT: "Unduh Lampiran",
            BLOCKED_ST: "Lisensi diblokir",
            GENERATED_ST: "Lisensi dibuat",
            ACTIVATED_ST: "Lisensi diaktivasi",
            OVERRIDED_ST: "Lisensi tambah limit",
            REGISTERED_ST: "Lisensi didaftarkan"

        });
        $translateProvider.useSanitizeValueStrategy('sanitize');
        $translateProvider.preferredLanguage('id');
    }

})();
(function() {
    'use strict';
    angular.module('application')
        .directive('footerDirective', footerDirective);

    function footerDirective() {


        var directive = {
            restrict: 'E',
            templateUrl: "views/footer.html",
            replace: true
        };

        return directive;
    }
})();
(function() {
    "use strict";
    angular.module('application')
        .directive('fileModel', ['$parse', function($parse) {
            return {
                restrict: 'A',
                link: function(scope, element, attrs) {
                    var model = $parse(attrs.fileModel);
                    var modelSetter = model.assign;

                    element.bind('change', function() {
                        scope.$apply(function() {
                            modelSetter(scope, element[0].files[0]);
                        });
                    });
                }
            };
        }]);
})();
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

        function overrideActivationLimit(licenseId, message, file) {
            var formdata = new FormData();
            formdata.append("licenseId", licenseId);
            formdata.append("message", message);
            formdata.append("file", file)

            return $http.post(baseURL + "/snManagement/overrideActivationLimit/", formdata, {
                transformRequest: angular.identity,
                headers: {
                    'Content-Type': undefined
                }
            });
        }

        function blockLicense(licenseId, message) {
            return $http.put(baseURL + "/snManagement/blocked/" + licenseId + "/" + message);
        }

        function exportData(dataExport) {
            alasql('SELECT * INTO XLSX("licenses.xlsx",{headers:true}) FROM ?', [dataExport, dataExport]);
        }
    }

})();
(function() {
    'use strict';

    angular.module('application')
        .factory("DialogFactory", DialogFactory);

    DialogFactory.$inject = ['$uibModal'];

    function DialogFactory(uibModal) {
        var modalInstance = null;

        // ====================[START Message Dialog Creator]=================
        /**
         * @param {*} title 
         * @param {*} content 
         * @param {*} size 
         * @param {*} contentExtra 
         */
        function activationDialog(title) {
            modalInstance = uibModal.open({
                animation: true,
                templateUrl: 'views/activation-dialog.html',
                controller: ['$uibModalInstance', 'params', activationDialogController],
                controllerAs: '$ctrl',
                size: "md",
                resolve: {
                    params: function() {
                        return {
                            title: title
                        };
                    }
                }
            });

            return modalInstance.result;

        }

        /**
         * @param {*} title 
         * @param {*} messsages 
         * @param {*} size 
         */
        function messageDialog(title, messsages, size) {
            modalInstance = uibModal.open({
                animation: true,
                templateUrl: 'views/message-dialog.html',
                controller: ['$uibModalInstance', 'params', messageDialogController],
                controllerAs: '$ctrl',
                size: size,
                resolve: {
                    params: function() {
                        return {
                            title: title,
                            messsages: messsages
                        };
                    }
                }
            });

            return modalInstance.result;
        }

        /**
         * @param {*} title 
         * @param {*} messsage 
         * @param {*} size 
         */
        function confirmationDialog(title, messsage, size) {
            modalInstance = uibModal.open({
                animation: true,
                templateUrl: 'views/confirmation-dialog.html',
                controller: ['$uibModalInstance', 'params', confirmationDialogController],
                controllerAs: '$ctrl',
                size: size,
                backdrop: 'static',
                resolve: {
                    params: function() {
                        return {
                            title: title,
                            messsage: messsage
                        };
                    }
                }
            });

            return modalInstance.result;
        }

        /**
         * @param {*} title 
         * @param {*} messsage 
         * @param {*} size 
         */
        function confirmationWithMessageDialog(title, messsage, placeholder, extra) {
            var template = 'views/confirmation-with-messages-dialog.html';
            if (extra) {
                template = 'views/override-dialog.html'
            }
            modalInstance = uibModal.open({
                animation: true,
                templateUrl: template,
                controller: ['$uibModalInstance', 'params', confirmationWithMesssageDialogController],
                controllerAs: '$ctrl',
                size: "md",
                backdrop: 'static',
                resolve: {
                    params: function() {
                        return {
                            title: title,
                            messsage: messsage,
                            placeholder: placeholder,
                            extra: extra
                        };
                    }
                }
            });

            return modalInstance.result;
        }

        // =======================[END Message Dialog Creator]==============

        // =====================[START Message Dialog Controller]===========
        function activationDialogController($uibModalInstance, params) {
            var ctrl = this;
            ctrl.title = params.title;
            ctrl.passkey = "";
            ctrl.reason = "";

            ctrl.ok = function() {
                $uibModalInstance.close({ passkey: ctrl.passkey, reason: ctrl.reason });
            };


        }

        function messageDialogController($uibModalInstance, params) {
            var ctrl = this;
            ctrl.title = params.title;
            ctrl.messages = params.messsages;

            ctrl.ok = function() {
                $uibModalInstance.close("close");
            };


        }

        function confirmationDialogController($uibModalInstance, params) {
            var ctrl = this;
            ctrl.title = params.title;
            ctrl.message = params.messsage;

            ctrl.ok = function() {
                $uibModalInstance.close("yes");
            };

            ctrl.dismiss = function() {
                $uibModalInstance.dismiss("no");
            }
        }

        function confirmationWithMesssageDialogController($uibModalInstance, params) {
            var ctrl = this;
            ctrl.title = params.title;
            ctrl.message = params.messsage;
            ctrl.placeholder = params.placeholder;
            ctrl.reason = "";
            ctrl.file;

            ctrl.ok = function() {
                if (params.extra) {
                    $uibModalInstance.close({ file: ctrl.file, reason: ctrl.reason });
                } else {
                    $uibModalInstance.close(ctrl.reason);
                }
            };

            ctrl.dismiss = function() {
                $uibModalInstance.dismiss("no");
            }
        }
        // =======================[END Message Dialog Controller]===========

        var dialogContainer = {
            activationDialog: activationDialog,
            messageDialog: messageDialog,
            confirmationDialog: confirmationDialog,
            confirmationWithMessageDialog: confirmationWithMessageDialog
        };

        return dialogContainer;


    }
})();
/**
 * Propfilter this filter i'am forgoten for usage
 */
(function() {
    'use strict';
    angular.module('application')
        .filter('propsFilter', propsFilter);

    function propsFilter() {

        return filter;

        /**--------------------------------------------- */

        function filter(items, props) {
            var out = [];
            if (angular.isArray(items)) {
                items.forEach(function(item) {
                    var itemMatches = false;

                    var keys = Object.keys(props);
                    for (var i = 0; i < keys.length; i++) {
                        var prop = keys[i];
                        var text = props[prop].toLowerCase();
                        if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                            itemMatches = true;
                            break;
                        }
                    }

                    if (itemMatches) {
                        out.push(item);
                    }
                });
            } else {
                // Let the output be the input untouched
                out = items;
            }
            return out;
        }
    }

})();
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
                        DialogFactory.messageDialog("LOGIN_ERROR_TITLE", ["LOGIN_ERROR_MESSAGE"], "md");
                    }
                );
            }
        }
    }

})();
/**
 * Administrator Controller
 */

(function() {
    'use strict';
    angular.module('application')
        .controller('AdministratorController', AdministratorController);

    AdministratorController.$inject = ['$scope', '$state', '$cookies', 'RequestFactory'];

    function AdministratorController($scope, $state, $cookies, RequestFactory) {
        RequestFactory.isAlreadyAuthenticated();

        $(document).ready(function() {

            if (typeof jQuery === "undefined") {
                throw new Error("AdminLTE requires jQuery");
            }

            $.AdminLTE = {};

            $.AdminLTE.options = {
                navbarMenuSlimscroll: true,
                navbarMenuSlimscrollWidth: "3px",
                navbarMenuHeight: "200px",
                animationSpeed: 500,
                sidebarToggleSelector: "[data-toggle='offcanvas']",
                sidebarPushMenu: true,
                sidebarSlimScroll: true,
                sidebarExpandOnHover: false,
                enableBoxRefresh: true,
                enableBSToppltip: true,
                BSTooltipSelector: "[data-toggle='tooltip']",
                enableFastclick: false,
                enableControlSidebar: true,
                controlSidebarOptions: {
                    toggleBtnSelector: "[data-toggle='control-sidebar']",
                    selector: ".control-sidebar",
                    slide: true
                },
                colors: {
                    lightBlue: "#3c8dbc",
                    red: "#f56954",
                    green: "#00a65a",
                    aqua: "#00c0ef",
                    yellow: "#f39c12",
                    blue: "#0073b7",
                    navy: "#001F3F",
                    teal: "#39CCCC",
                    olive: "#3D9970",
                    lime: "#01FF70",
                    orange: "#FF851B",
                    fuchsia: "#F012BE",
                    purple: "#8E24AA",
                    maroon: "#D81B60",
                    black: "#222222",
                    gray: "#d2d6de"
                },
                screenSizes: {
                    xs: 480,
                    sm: 768,
                    md: 992,
                    lg: 1200
                }
            };

            /* ------------------
             * - Implementation -
             * ------------------
             * The next block of code implements AdminLTE's
             * functions and plugins as specified by the
             * options above.
             */
            $(function() {
                "use strict";

                //Fix for IE page transitions
                $("body").removeClass("hold-transition");

                //Extend options if external options exist
                if (typeof AdminLTEOptions !== "undefined") {
                    $.extend(true,
                        $.AdminLTE.options,
                        AdminLTEOptions);
                }

                //Easy access to options
                var o = $.AdminLTE.options;

                //Set up the object
                _init();

                //Activate the layout maker
                $.AdminLTE.layout.activate();

                //Enable sidebar tree view controls
                $.AdminLTE.tree('.sidebar');

                //Add slimscroll to navbar dropdown
                if (o.navbarMenuSlimscroll && typeof $.fn.slimscroll != 'undefined') {
                    $(".navbar .menu").slimscroll({
                        height: o.navbarMenuHeight,
                        alwaysVisible: false,
                        size: o.navbarMenuSlimscrollWidth
                    }).css("width", "100%");
                }

                //Activate fast click
                if (o.enableFastclick && typeof FastClick != 'undefined') {
                    FastClick.attach(document.body);
                }

                /*
                 * INITIALIZE BUTTON TOGGLE
                 * ------------------------
                 */
                $('.btn-group[data-toggle="btn-toggle"]').each(function() {
                    var group = $(this);
                    $(this).find(".btn").on('click', function(e) {
                        group.find(".btn.active").removeClass("active");
                        $(this).addClass("active");
                        e.preventDefault();
                    });

                });
            });

            /* ----------------------------------
             * - Initialize the AdminLTE Object -
             * ----------------------------------
             * All AdminLTE functions are implemented below.
             */
            function _init() {
                'use strict';
                /* Layout
                 * ======
                 * Fixes the layout height in case min-height fails.
                 *
                 * @type Object
                 * @usage $.AdminLTE.layout.activate()
                 *        $.AdminLTE.layout.fix()
                 *        $.AdminLTE.layout.fixSidebar()
                 */
                $.AdminLTE.layout = {
                    activate: function() {
                        var _this = this;
                        _this.fix();
                        _this.fixSidebar();
                        $(window, ".wrapper").resize(function() {
                            _this.fix();
                            _this.fixSidebar();
                        });
                    },
                    fix: function() {
                        //Get window height and the wrapper height
                        var neg = $('.main-header').outerHeight() + $('.main-footer').outerHeight();
                        var window_height = $(window).height();
                        var sidebar_height = $(".sidebar").height();
                        //Set the min-height of the content and sidebar based on the
                        //the height of the document.
                        if ($("body").hasClass("fixed")) {
                            $(".content-wrapper, .right-side").css('min-height', window_height - $('.main-footer').outerHeight());
                        } else {
                            var postSetWidth;
                            if (window_height >= sidebar_height) {
                                $(".content-wrapper, .right-side").css('min-height', window_height - neg);
                                postSetWidth = window_height - neg;
                            } else {
                                $(".content-wrapper, .right-side").css('min-height', sidebar_height);
                                $(".admin-dash").css("height", sidebar_height);
                                postSetWidth = sidebar_height;
                            }

                            //Fix for the control sidebar height
                            var controlSidebar = $($.AdminLTE.options.controlSidebarOptions.selector);
                            if (typeof controlSidebar !== "undefined") {
                                if (controlSidebar.height() > postSetWidth)
                                    $(".content-wrapper, .right-side").css('min-height', controlSidebar.height());
                            }

                        }
                    },
                    fixSidebar: function() {
                        //Make sure the body tag has the .fixed class
                        if (!$("body").hasClass("fixed")) {
                            if (typeof $.fn.slimScroll != 'undefined') {
                                $(".sidebar").slimScroll({ destroy: true }).height("auto");
                            }
                            return;
                        } else if (typeof $.fn.slimScroll == 'undefined' && window.console) {
                            window.console.error("Error: the fixed layout requires the slimscroll plugin!");
                        }
                        //Enable slimscroll for fixed layout
                        if ($.AdminLTE.options.sidebarSlimScroll) {
                            if (typeof $.fn.slimScroll != 'undefined') {
                                //Destroy if it exists
                                $(".sidebar").slimScroll({ destroy: true }).height("auto");
                                //Add slimscroll
                                $(".sidebar").slimscroll({
                                    height: ($(window).height() - $(".main-header").height()) + "px",
                                    color: "rgba(0,0,0,0.2)",
                                    size: "3px"
                                });
                            }
                        }
                    }
                };

                /* Tree()
                 * ======
                 * Converts the sidebar into a multilevel
                 * tree view menu.
                 *
                 * @type Function
                 * @Usage: $.AdminLTE.tree('.sidebar')
                 */
                $.AdminLTE.tree = function(menu) {
                    var _this = this;
                    var animationSpeed = $.AdminLTE.options.animationSpeed;
                    $(document).off('click', menu + ' li a')
                        .on('click', menu + ' li a', function(e) {
                            //Get the clicked link and the next element
                            var $this = $(this);
                            var checkElement = $this.next();

                            //Check if the next element is a menu and is visible
                            if ((checkElement.is('.treeview-menu')) && (checkElement.is(':visible')) && (!$('body').hasClass('sidebar-collapse'))) {
                                //Close the menu
                                checkElement.slideUp(animationSpeed, function() {
                                    checkElement.removeClass('menu-open');
                                    //Fix the layout in case the sidebar stretches over the height of the window
                                    //_this.layout.fix();
                                });
                                checkElement.parent("li").removeClass("active");
                                $(".admin-dash").css("height", "100vh");
                                $(".content-wrapper, .right-side").css("min-height", "92.5vh");
                            }
                            //If the menu is not visible
                            else if ((checkElement.is('.treeview-menu')) && (!checkElement.is(':visible'))) {
                                //Get the parent menu
                                var parent = $this.parents('ul').first();
                                //Close all open menus within the parent
                                var ul = parent.find('ul:visible').slideUp(animationSpeed);
                                //Remove the menu-open class from the parent
                                ul.removeClass('menu-open');
                                //Get the parent li
                                var parent_li = $this.parent("li");

                                //Open the target menu and add the menu-open class
                                checkElement.slideDown(animationSpeed, function() {
                                    //Add the class active to the parent li
                                    checkElement.addClass('menu-open');
                                    parent.find('li.active').removeClass('active');
                                    parent_li.addClass('active');
                                    //Fix the layout in case the sidebar stretches over the height of the window
                                    _this.layout.fix();
                                });
                            }
                            //if this isn't a link, prevent the page from being redirected
                            if (checkElement.is('.treeview-menu')) {
                                e.preventDefault();
                            }
                        });
                };

            }
        });

        $scope.pushMenu = pushMenu;
        $scope.logout = logout;

        // call push menu for first time when page reload
        // pushMenu();

        /**--------------------------------------- */

        function pushMenu() {
            if ($("body").hasClass('sidebar-collapse')) {
                $("body").removeClass('sidebar-collapse').trigger('expanded.pushMenu');
            } else {
                $("body").addClass('sidebar-collapse').trigger('collapsed.pushMenu');
                $(".content-wrapper").css("min-height", "92.5vh");
                $(".admin-dash").css("height", "100vh");
            }
        }

        function logout() {
            RequestFactory.logout().then(
                function(response) {
                    $cookies.remove("loggingIn");
                    $state.go("login");
                },
                function(error) {
                    console.log("Error : " + error);
                });
        }

    };
})();
/**
 * Serial Number Controller
 */
(function() {
    'use strict';
    angular.module('application')
        .controller("ProjectSerialNumberController", ProjectSerialNumberController);

    ProjectSerialNumberController.$inject = ['$scope', 'RequestFactory', '$state', "$stateParams", 'DialogFactory', 'FileSaver', 'Blob'];

    function ProjectSerialNumberController($scope, RequestFactory, $state, $stateParams, DialogFactory, FileSaver, Blob) {
        RequestFactory.isAlreadyAuthenticated();

        $scope.rowCollection = [];
        $scope.displayCollection = [];
        $scope.search = "";

        $scope.currentPage = 1;
        $scope.maxSize = 5;
        $scope.itemPage = 20;

        $scope.license = null;
        $scope.updateSchool = updateSchool;
        $scope.overrideActivationLimit = overrideActivationLimit;
        $scope.rembemberCurrentPage = getCurrentPage;
        $scope.activateSerialNumber = activateSerialNumber;
        $scope.licenseBlock = licenseBlock;
        $scope.exportFile = exportFile;
        $scope.exportAttachment = exportAttachment;
        /**------------------------------------------------------*/
        function getAllSerialNumber() {
            $scope.rowCollection = [];
            $scope.displayCollection = [];
            RequestFactory.getSerialNumber().then(
                function(response) {
                    $scope.rowCollection = response.data;
                    $scope.displayCollection = response.data;
                },
                function(error) {
                    console.log(error);
                }
            );
        }

        function getCurrentPage(p) {
            $scope.currentPage = p
        }

        /**
         * this function is no longer used, consider for removing
         * @param {*} serialNumber 
         */
        function activateSerialNumber(serialNumber) {
            RequestFactory.activate(serialNumber).then(
                function(response) {
                    DialogFactory.activationDialog("ACTIVATION_SUCCESS", "ACTIVATION_SUCCESS_MESSAGES", "md", "Activation Key :" + response.data.activationKey).then(
                        function(response) {
                            getAllSerialNumber();
                        },
                        function(dismiss) {}
                    );
                },
                function(errorResponse) {
                    DialogFactory.activationDialog("ACTIVATION_FAILED", "ACTIVATION_FAILED_MESSAGES", "md", null);
                });
        }

        if ($state.is("administrator.license.license-detail")) {
            var licenseId = $stateParams.licenseId;
            $scope.licenseHistories = [];
            $scope.licenseActivation = licenseActivation;
            $scope.licenseBlock = licenseBlock;
            $scope.disableDetail = false;

            RequestFactory.viewLicenseDetail(licenseId).then(
                function(response) {
                    $scope.license = response.data;
                    $scope.disableDetail = $scope.license.licenseStatus === 4 ? true : false;
                },
                function(error) {
                    console.log("data not found" + error);
                }
            );
        } else {
            /**
             *  Call getAllSerialNumber
             */
            getAllSerialNumber();
        }


        function licenseActivation() {
            DialogFactory.activationDialog("ACTIVATION").then(
                function(yes) {
                    RequestFactory.activate($stateParams.licenseId, yes.passkey, yes.reason).then(
                        function(response) {
                            DialogFactory.messageDialog("NOTIFICATION", ["SUCCESS_ACTIVATE_NOTIFICATION", "Aktivasi key:" + response.data.activationKey], "sm").then(
                                function() {
                                    //$state.reload();
                                    $state.go("administrator.license.license-detail", {}, { reload: 'administrator.license.license-detail' });
                                }
                            );
                        },
                        function(error) {
                            DialogFactory.messageDialog("ACTIVATION_FAILED", ["ACTIVATION_FAILED_MESSAGES"], "md");
                            console.error(error);
                        }
                    );
                },
                function(no) {}
            );
        }

        function licenseBlock(licenseId) {
            DialogFactory.confirmationWithMessageDialog("CONFIRMATION", "BLOCK_CONFIRMATION", "BLOCK_REASON_TEXT").then(
                function(reason) {
                    RequestFactory.blockLicense($stateParams.licenseId, reason).then(
                        function(response) {
                            DialogFactory.messageDialog("BLOCK_SUCCESS", ["BLOCK_SUCCESS_MESSAGE"], "sm").then(
                                function() {
                                    $state.reload();
                                }
                            );
                        },
                        function(errorResponse) {
                            DialogFactory.messageDialog("BLOCK_FAILED", ["BLOCK_FAILED_MESSAGE"], "sm");
                        }
                    )
                },
                function(no) {}
            );
        }

        function updateSchool(schoolName) {
            RequestFactory.updateSchool($stateParams.licenseId, schoolName).then(
                function(response) {
                    DialogFactory.messageDialog("NOTIFICATION", ["SUCCESS_SCHOOL_UPDATE_NOTIFICATION"], "sm");
                },
                function(error) {
                    DialogFactory.messageDialog("NOTIFICATION", ["FAILED_SCHOOL_UPDATE_NOTIFICATION"], "sm");
                }
            );
        }

        function overrideActivationLimit() {

            DialogFactory.confirmationWithMessageDialog("CONFIRMATION", "OVERRIDE_LIMIT_TEXT", "OVERRIDE_LIMIT_REASON_TEXT", true).then(
                function(result) {
                    RequestFactory.overrideActivationLimit($stateParams.licenseId, result.reason, result.file).then(
                        function(response) {
                            DialogFactory.messageDialog("NOTIFICATION", ["SUCCESS_OVERRIDE_NOTIFICATION"], "sm").then(
                                function() {
                                    $state.reload();
                                }
                            );
                            $scope.license.activationLimit = response.data.activationLimit;
                        },
                        function(error) {
                            DialogFactory.messageDialog("NOTIFICATION", ["FAILED_OVERRIDE_NOTIFICATION"], "sm");
                        }
                    );
                },
                function(no) {}
            );
        }

        function exportFile(exportType) {

            var dataExport = [];
            var data = [];
            if (exportType === 'ALL') {
                data = data.concat($scope.rowCollection);
            } else {
                data = data.concat($scope.displayCollection);
            }

            angular.forEach(data, function(row, key) {
                dataExport.push({
                    "No.": key + 1,
                    "Serial Number": row.serialNumber.license,
                    "Produk": row.serialNumber.productName,
                    "Jumlah Pengguna": row.serialNumber.numberOfClient ? row.serialNumber.numberOfClient : 1,
                    "Sekolah": row.serialNumber.schoolName,
                    "Tanggal Dibuat": new Date(row.serialNumber.createdDate).toLocaleDateString()
                })
            });

            RequestFactory.exportData(dataExport);
        }

        function exportAttachment(history) {
            var dataUri = "data:" + history.fileContentType + ";base64," + history.fileData;
            fetch(dataUri)
                .then(function(response) {
                    return response.blob();
                }).then(function(blobData) {
                    FileSaver.saveAs(blobData, history.fileName);
                });
        }
    }

})();
(function() {
    'use strict';

    angular.module('application').controller('AboutUsController', aboutUs);

    aboutUs.$inject = ['RequestFactory'];

    function aboutUs(RequestFactory) {
        RequestFactory.isAlreadyAuthenticated()
    }
})();
/**
 * Dashboard Controller
 */
(function() {
    'use strict';
    angular.module('application')
        .controller('DashboardController', DashboardController);

    DashboardController.$inject = ['$scope', '$state', '$stateParams', 'RequestFactory'];

    function DashboardController($scope, $state, $stateParams, RequestFactory) {
        $scope.notifications = [];
        $scope.rowCollections = [];
        $scope.products = [];
        $scope.getBackground = getBackground;
        $scope.itemPage = 5;
        $scope.currentPage = 1;
        $scope.rembemberCurrentPage = getCurrentPage;

        //Get all new notification
        RequestFactory.getUnreadLicenses().then(
            function(response) {
                $scope.notifications = response.data;
                $scope.rowCollections = [].concat($scope.notifications);
            },
            function(error) {
                console.log("notifications not found");
            }
        );

        function getCurrentPage(p) {
            $scope.currentPage = p
        }

        RequestFactory.licenseCountByProduct().then(
            function(response) {
                $scope.products = response.data;
            },
            function(error) {
                console.log(error);
            }
        );

        function getBackground(index) {
            var bg = ["bg-red", "bg-yellow", "bg-aqua", "bg-blue", "bg-light-blue", "bg-green", "bg-navy", "bg-teal", "bg-olive", "bg-lime", "bg-orange", "bg-fuchsia", "bg-purple", "bg-maroon", "bg-black"]
            var result = new Object();
            if (index) {
                result[bg[index]] = true;
            } else {
                result[bg[0]] = true;
            }

            return result;

        }


    }

})();
(function() {
    'use strict';

    angular.module("application").controller("SchoolController", schoolManagementController);

    schoolManagementController.$inject = ['$scope', '$state', '$stateParams', 'RequestFactory', 'DialogFactory'];

    function schoolManagementController($scope, $state, $stateParams, RequestFactory, DialogFactory) {
        $scope.schools = [];
        $scope.rowCollection = [];
        $scope.schoolId = $stateParams.schoolId;
        $scope.school = null;
        $scope.submitSchoolForm = submitSchoolForm;
        $scope.deleteSchool = deleteSchool;

        if ($state.is("administrator.school-management")) {

            // get all school
            RequestFactory.getSchools().then(
                function(response) {
                    $scope.schools = response.data;
                    $scope.rowCollections = [].concat($scope.schools);
                },
                function(error) {
                    DialogFactory.messageDialog("NOTIFICATION", ["DATA_NOT_FOUND"], "sm");
                }
            );

        }

        if ($scope.schoolId) {
            RequestFactory.schoolDetail($scope.schoolId).then(
                function(response) {
                    $scope.school = response.data;
                },
                function(error) {
                    DialogFactory.messageDialog("NOTIFICATION", ["DATA_NOT_FOUND"], "sm").then(
                        function(dialogReturn) {
                            $state.go("administrator.school-management");
                        },
                        function(dismiss) {}
                    );
                }
            );
        }

        function submitSchoolForm(school) {
            var result = null;

            if ($scope.schoolId) {
                result = RequestFactory.updateSchool(school);
            } else {
                result = RequestFactory.createSchool(school);
            }

            result.then(
                function(response) {
                    DialogFactory.messageDialog("SAVE_SUCCESS", ["SAVE_SCHOOL_SUCCESS"], "sm").then(
                        function(dialogReturn) {
                            $state.go("administrator.school-management");
                        }
                    );
                },
                function(error) {
                    DialogFactory.messageDialog("SAVE_FAILED", ["DATA_ALREADY_EXIST"], "sm");
                });
        }

        function deleteSchool(schoolId) {
            DialogFactory.confirmationDialog("CONFIRMATION", "DELETE_MESSAGE", "sm").then(
                function(dialogReturn) {
                    RequestFactory.deleteSchool(schoolId).then(
                        function(response) {
                            DialogFactory.messageDialog("DELETE_SUCCESS", ["DELETE_DATA_SUCCESS"], "sm").then(
                                function(response) {
                                    $state.go("administrator.school-management");
                                },
                                function(dismiss) {}
                            );
                        },
                        function(errorResponse) {
                            DialogFactory.messageDialog("DELETE_FAILED", ["DELETE_DATA_FAILED"], "sm");
                        }
                    )
                },
                function(dismiss) {

                }
            )
        }


    }
})();
(function() {
    'use strict';

    angular.module('application').controller('UserManagementController', userManagementController);

    userManagementController.$inject = ['$scope', '$state', '$stateParams', 'RequestFactory'];

    function userManagementController($scope, $state, $stateParams, RequestFactory) {
        $scope.users = [];
        $scope.rowCollection = [];


        // get all school
        RequestFactory.getUsers().then(
            function(response) {
                $scope.users = response.data;
                $scope.rowCollections = [].concat($scope.users);
            },
            function(error) {
                console.log("users not found");
            }
        );

    }
})();
(function() {


    /**
     * application Module
     *
     * Description
     */
    angular.module('application')
        .controller('ProductManagementController', productManagementController);

    productManagementController.$inject = ["$scope", "$state", "$stateParams", "RequestFactory", "DialogFactory"];

    function productManagementController($scope, $state, $stateParams, RequestFactory, DialogFactory) {

        $scope.products = [];
        $scope.rowCollections = [];
        $scope.productId = null;
        $scope.productDto = {
            product: {
                id: null,
                productName: null,
                productCode: null,
                description: null,
                subModuleType: "EL",
                subModuleLable: null,
                createdDate: null,
                deleted: false
            },
            subProducts: []
        };

        $scope.submitProductForm = submitProductForm;
        $scope.deleteProduct = deleteProduct;
        $scope.entryType = "direct";
        $scope.entryTypeChange = entryTypeChange;
        $scope.addNewSubProduct = addNewSubProduct;
        $scope.deleteSubProduct = deleteSubProduct;

        if ($state.is("administrator.product-management")) {
            // get all products
            RequestFactory.getProducts().then(
                function(response) {
                    $scope.products = response.data;
                    $scope.rowCollections = [].concat($scope.products);
                },
                function(error) {
                    DialogFactory.messageDialog("NOTIFICATION", ["DATA_NOT_FOUND"], "sm");
                }
            );
        } else {
            $scope.productId = $stateParams.productId;
            $scope.isUpdate = $scope.productId ? true : false;
        }

        if ($scope.productId) {
            RequestFactory.viewProductDetail($scope.productId).then(
                function(response) {
                    $scope.productDto = response.data;
                },
                function(error) {
                    DialogFactory.messageDialog("NOTIFICATION", ["DATA_NOT_FOUND"], "sm").then(
                        function(dialogReturn) {
                            $state.go("administrator.product-management");
                        }
                    );
                }
            );
        }

        function submitProductForm(productDto) {
            var result = null;

            if ($scope.productId) {
                result = RequestFactory.updateProduct(productDto);
            } else {
                result = RequestFactory.createProduct(productDto);
            }

            result.then(
                function(response) {
                    DialogFactory.messageDialog("SAVE_SUCCESS", ["SAVE_PRODUCT_SUCCESS"], "sm").then(
                        function(dialogReturn) {
                            $state.go("administrator.product-management");
                        }
                    );
                },
                function(error) {
                    DialogFactory.messageDialog("SAVE_FAILED", ["SAVE_PRODUCT_FAILED"], "sm");
                });
        }

        function deleteProduct(productId) {
            DialogFactory.confirmationDialog("CONFIRMATION", "DELETE_MESSAGE", "sm").then(
                function(dialogReturn) {
                    RequestFactory.deleteProduct(productId).then(
                        function(response) {
                            DialogFactory.messageDialog("DELETE_SUCCESS", ["DELETE_DATA_SUCCESS"], "sm").then(
                                function(response) {
                                    $state.go("administrator.product-management");
                                },
                                function(dismiss) {}
                            );
                        },
                        function(errorResponse) {
                            DialogFactory.messageDialog("DELETE_FAILED", ["DELETE_DATA_FAILED"], "sm");
                        }
                    );
                },
                function(dismiss) {

                }
            )
        }

        function entryTypeChange(type) {
            if (type && type === "EP") {
                $scope.productDto.subProducts = [{
                    id: null,
                    label: null,
                    value: null
                }];
            } else {
                $scope.productDto.subProducts = [];
            }
        }

        function addNewSubProduct() {
            var newSubProduct = {
                id: null,
                label: null,
                value: null
            }
            $scope.productDto.subProducts.push(newSubProduct);
        }

        function deleteSubProduct(id, index) {
            if (id) {
                RequestFactory.deleteSubProduct(id).then(function(response) {
                    DialogFactory.messageDialog("DELETE_SUCCESS", ["DELETE_DATA_SUCCESS"], "sm");
                }, function(error) {
                    DialogFactory.messageDialog("DELETE_FAILED", ["DELETE_DATA_FAILED"], "sm");
                });
            }
            $scope.productDto.subProducts.splice(index, 1);
        }
    }

})();
(function() {

    angular.module("application")
        .controller('GeneratorController', generatorController);

    generatorController.$inject = ["RequestFactory", "DialogFactory", "$scope", "$state", "localStorageService", "bsLoadingOverlayService"];

    function generatorController(RequestFactory, DialogFactory, $scope, $state, localStorageService, bsLoadingOverlayService) {
        $scope.licenseGeneratorDTO = {
            product: null,
            subProducts: [],
            licenseCount: 1
        };

        $scope.products = [];
        $scope.generatedLicense = [];
        $scope.switchListGenerator = null;
        $scope.submitLicenseGenerator = submitLicenseGenerator;
        $scope.registerGeneratedSN = registerGeneratedSN;
        $scope.switchToSubProduct = null;
        $scope.subProducts = [];
        $scope.switchPage = "create";

        function submitLicenseGenerator(licenseGeneratorDTO) {
            var isvalid = true;
            var messages = [];
            if (!licenseGeneratorDTO.product) {
                messages.push("PRODUCT_MUST_FILLED");
                isvalid = false;
            }

            if (licenseGeneratorDTO.licenseCount <= 0) {
                messages.push("LICENSE_COUNT_GREATER_THAN_ZERO");
                isvalid = false;
            }

            if (licenseGeneratorDTO.product && licenseGeneratorDTO.subProducts.length === 0) {
                messages.push("SELECT_AT_LEAST_ONE");
                isvalid = false;
            }

            if (isvalid) {
                showOverlay();
                RequestFactory.licenseGenerator(licenseGeneratorDTO).then(
                    function(response) {
                        setTimeout(function() {
                            hideOverlay();
                            $scope.generatedLicense = response.data;
                            // $cookies.putObject("listGenerated", $scope.generatedLicense);
                            localStorageService.set("licenseCount", licenseGeneratorDTO.licenseCount);
                            localStorageService.set("listGenerated", $scope.generatedLicense);
                            localStorageService.set("type", $scope.licenseGeneratorDTO.product.subModuleType);
                            $state.go("administrator.generator.list");
                        }, 2000);
                    },
                    function(error) {
                        hideOverlay();
                        console.log("Error" + error);
                    }
                );
            } else {
                DialogFactory.messageDialog("GENERATE_FAILED", messages, "sm");
            }
        }

        function registerGeneratedSN(generatedSN) {
            var licenses = [];
            if ($scope.switchListGenerator === "EP") {
                for (var i = 0; i < localStorageService.get("licenseCount"); i++) {
                    for (var j = 0; j < generatedSN[i].length; j++) {
                        var license = generatedSN[i][j];
                        license.schoolName = $scope.schoolList[i].schoolName;
                        licenses.push(license);
                    }
                }
            } else {
                licenses = generatedSN;
            }

            RequestFactory.registerGeneratedSN(licenses).then(
                function(response) {
                    DialogFactory.confirmationDialog("SAVE_SUCCESS", "SAVE_TO_XLSX_CONFIRMATION", "sm").then(
                        function(result) {
                            exportData(response.data);
                            $state.go("administrator.license");
                        },
                        function(dismiss) {
                            $state.go("administrator.license");
                        }
                    );
                },
                function(errorResponse) {
                    DialogFactory.messageDialog("SAVE_FAILED", ["SAVE_LICENSE_FAILED"], "sm");
                }
            );
        }

        function getSubProduct(productId) {
            RequestFactory.fetchSubProductByProductId(productId).then(
                function(response) {
                    $scope.subProducts = response.data;
                    $scope.switchToSubProduct = "switchToSubProduct";
                    $scope.licenseGeneratorDTO.subProducts = angular.copy($scope.subProducts);
                },
                function(error) {
                    console.log("getSubProduct " + error)
                }
            );
        }

        $scope.onSelected = function(selectedProduct) {
            if (selectedProduct.subModuleType === "EP") {
                getSubProduct(selectedProduct.id);
            } else {
                $scope.licenseGeneratorDTO.subProducts = [{ id: null, label: null, value: null }]
            }
        }

        function exportData(licenseList) {
            var dataExport = [];

            angular.forEach(licenseList, function(row, key) {
                dataExport.push({
                    "No.": key + 1,
                    "Serial Number": row.serialNumber.license,
                    "Produk": row.serialNumber.productName,
                    "Jumlah Pengguna": row.serialNumber.numberOfClient ? row.serialNumber.numberOfClient : 1,
                    "Sekolah": row.serialNumber.schoolName,
                    "Tanggal Dibuat": new Date(row.serialNumber.createdDate).toLocaleDateString()
                })
            });

            RequestFactory.exportData(dataExport);
        }

        function showOverlay() {
            bsLoadingOverlayService.start();
        }

        function hideOverlay() {
            bsLoadingOverlayService.stop();
        }

        if ($state.is("administrator.generator")) {
            RequestFactory.getProducts().then(
                function(response) {
                    $scope.products = response.data;
                },
                function(error) {
                    console.log("cannot fetch product");
                }
            );
        } else if ($state.is("administrator.generator.list")) {
            $scope.switchListGenerator = localStorageService.get("type");
            if ($scope.switchListGenerator === "EL") {
                $scope.generatedLicense = localStorageService.get("listGenerated").EL;
            } else {
                var listGenerated = localStorageService.get("listGenerated");
                $scope.generatedLicense = [];
                $scope.schoolList = [];
                for (var i = 0; i < localStorageService.get("licenseCount"); i++) {
                    $scope.generatedLicense.push(listGenerated["Paket" + (i + 1)]);
                    $scope.schoolList.push({ schoolName: null });
                }
            }
        }
    }

})();