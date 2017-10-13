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
            'ngFileSaver'
        ])
        .config(config);

    function config(localStorageServiceProvider) {
        localStorageServiceProvider.setPrefix('helpdesk');
        localStorageServiceProvider.setDefaultToCookie(false);
        localStorageServiceProvider.setStorageType('localStorage');
    }

})();