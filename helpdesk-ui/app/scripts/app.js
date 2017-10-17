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
        .config(["localStorageServiceProvider", "usSpinnerConfigProvider", config])
        .run(["$rootScope", "bsLoadingOverlayService", "$sce", run]);

    function config(localStorageServiceProvider, usSpinnerConfigProvider) {
        localStorageServiceProvider.setPrefix('helpdesk');
        localStorageServiceProvider.setDefaultToCookie(false);
        localStorageServiceProvider.setStorageType('localStorage');

        usSpinnerConfigProvider.setDefaults({ color: 'gray' });
    }

    function run($rootScope, bsLoadingOverlayService, $sce) {
        $rootScope.showOverlay = showOverlay;
        $rootScope.hideOverlay = hideOverlay;
        $rootScope.rootTrustAsHtml = trustAsHtml;

        $rootScope.tinyMceOption = {
            height: 400,
            selector: 'textarea',
            theme: 'modern',
            plugins: [
                'advlist autolink lists link image charmap print preview anchor textcolor',
                'searchreplace visualblocks code fullscreen',
                'insertdatetime media table contextmenu paste code'
            ],
            toolbar: 'formatselect | bold italic backcolor | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | removeformat',
            inline: false,
            menubar: ""
        };

        function showOverlay() {
            bsLoadingOverlayService.start();
        }

        function hideOverlay() {
            bsLoadingOverlayService.stop();
        }

        function trustAsHtml(htmlContent) {
            if (!htmlContent) {
                htmlContent = "<center>" +
                    "<h2>Produk belum di pilih.</h2>" +
                    "<p>Silahkan pilih salah satu produk yang tersedia untuk melihat deskripsi dari sebuah produk.</p>" +
                    "</center>"
            }
            return $sce.trustAsHtml(htmlContent);
        }



    }

})();