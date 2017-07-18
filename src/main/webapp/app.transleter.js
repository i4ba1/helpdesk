/*
 * @Author: Marlina_Kreatif 
 * @Date: 2017-07-18 10:06:11 
 * @Last Modified by: Margono Sugeng Purwoko
 * @Last Modified time: 2017-07-18 15:09:45
 * 
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
            KNT_PRODUCT: "Product",
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
            SECOND_PARAM_PLH: "Masukan kelas atau jumlah client",
            SELECT_PRODUCT: "Pilih Produk",
            SELECT_SCHOOL: "Pilih Sekolah",
            NO: "No.",
            NOTIFICATION: "Pemberitahuan",
            SAVE_SUCCESS: "Simpan Data Berhasil",
            SAVE_FAILED: "Simpan Data Gagal",
            SAVE_SCHOOL_SUCCESS: "Data sekolah berhasil disimpan",
            SAVE_SCHOOL_FAILED: "Data Sekolah gagal disimpan",
            DATA_NOT_FOUND: "Data tidak ditemukan!",
            YES: "Ya",
            NOT: "Tidak",
            DELETE_MESSAGE: "Anda yakin hapus data ini ?",
            CONFIRMATION: "Konfirmasi",
            DELETE_SUCCESS: "Berhasil Hapus",
            DELETE_FAILED: "Gagal Hapus",
            DELETE_DATA_SUCCESS: "Berhasil menghapus data ini !",
            DELETE_DATA_FAILED: "Gagal menghapus data ini !",
            DATA_ALREADY_EXIST: "Data sudah pernah disimpan !"


        });
        $translateProvider.useSanitizeValueStrategy('sanitize');
        $translateProvider.preferredLanguage('id');
    }

})();