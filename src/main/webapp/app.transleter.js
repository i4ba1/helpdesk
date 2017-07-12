/**
 * Application Translate Configuration
 */
(function() {
    'use strict';
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
            ACTIVATE: 'Activasi',
            REACTIVATE: 'Activasi Ulang',
            ACTIVATED: "Teraktivasi",
            UNACTIVATED: "Belum Teraktivasi",
            ACTIVATION_SUCCESS: "Aktifasi Berhasil",
            ACTIVATION_FAILED: "Aktifasi Gagal",
            ACTIVATION_SUCCESS_MESSAGES: "Aktifasi Serial Number Berhasil",
            ACTIVATION_FAILED_MESSAGES: "Aktifasi Serial Number Gagal",
            SCHOOL: "Sekolah",
            SCHOOL_MANAGEMENT: "Manajement Sekolah",
            ERROR_LOGIN_TITLE: "Login Gagal",
            LOGIN_ERROR_MESSAGE: "Maaf Nama Pengguna atau Kata Sandi Salah!",
            SCHOOL_ADDRESS: "Alamat Sekolah",
            SCHOOL_NAME: "Nama Sekolah",
            REGISTER_DATE: "Tanggal Daftar",
            USER_MANAGEMENT: "User Management",
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
            KNT_PRODUCT: "Product",
            PRODUCT_NAME:"Nama Produk",
            PRODUCT_NAME_PLH:"Masukan nama produk",
            PRODUCT_CODE:"Kode Produk",
            PRODUCT_CODE_PLH:"Kode Produk",
            CODE:"Kode",
        });
        $translateProvider.useSanitizeValueStrategy('sanitize');
        $translateProvider.preferredLanguage('id');
    }

})();