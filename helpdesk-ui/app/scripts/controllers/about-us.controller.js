(function() {
    'use strict';

    angular.module('application').controller('AboutUsController', aboutUs);

    aboutUs.$inject = ['RequestFactory'];

    function aboutUs(RequestFactory) {
        RequestFactory.isAlreadyAuthenticated();
    }
})();