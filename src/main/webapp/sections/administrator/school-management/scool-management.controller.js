(function() {
    'use strict';

    angular.module('application').controller('SchoolManagementController', schoolManagement);

    schoolManagement.$inject = ['RequestFactory'];

    function schoolManagement(RequestFactory) {
        RequestFactory.isAlreadyAuthenticated()
    }
})();