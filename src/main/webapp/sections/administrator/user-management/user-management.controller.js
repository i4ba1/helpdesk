(function() {
    'use strict';

    angular.module('application').controller('UserManagementController', userManagement);

    userManagement.$inject = ['RequestFactory'];

    function userManagement(RequestFactory) {
        RequestFactory.isAlreadyAuthenticated()
    }
})();