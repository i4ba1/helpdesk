(function() {
    "use strict";

    angular.module("application")
        .controller("RegistrationController", registration);
    registration.$inject = ["$scope", "RequestFactory", "DialogFactory", "$state"];

    function registration($scope, RequestFactory, DialogFactory, $state) {
        $scope.license = new License();
        $scope.products = [];
        $scope.submit = registration;

        function registration(license) {
            RequestFactory.register(license).then(
                function(success) {
                    DialogFactory.messageDialog("REGISTRATION", ["REGISTRATION_SUCCESS"], "sm").then(
                        function(s) {
                            $state.go("administrator.license");
                        }
                    );
                },
                function(failed) {
                    DialogFactory.messageDialog("REGISTRATION", ["REGISTRATION_FAILED"], "sm");
                }
            );

        }
    }
})();