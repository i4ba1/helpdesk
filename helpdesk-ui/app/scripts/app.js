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
            'angularSpinner',
            'ui.mask'
        ])
        .config(["localStorageServiceProvider", "usSpinnerConfigProvider", config])
        .run(["$rootScope", "bsLoadingOverlayService", run]);

    function config(localStorageServiceProvider, usSpinnerConfigProvider) {
        localStorageServiceProvider.setPrefix('helpdesk');
        localStorageServiceProvider.setDefaultToCookie(false);
        localStorageServiceProvider.setStorageType('localStorage');

        usSpinnerConfigProvider.setDefaults({ color: 'gray' });
    }

    function run($rootScope, bsLoadingOverlayService) {
        $rootScope.showOverlay = showOverlay;
        $rootScope.hideOverlay = hideOverlay;

        function showOverlay() {
            bsLoadingOverlayService.start();
        }

        function hideOverlay() {
            bsLoadingOverlayService.stop();
        }

    }

})();