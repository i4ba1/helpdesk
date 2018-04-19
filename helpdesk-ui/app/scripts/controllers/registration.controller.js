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
                    var message = "Berikut Aktivation Key : <b style='color:red;font-size:16px'>" + success.data.activationKey + "</b>";
                    DialogFactory.messageDialog("REGISTRATION", ["REGISTRATION_SUCCESS", message], "sm").then(
                        function(s) {
                            $state.go("administrator.license");
                        }
                    );
                },
                function(failed) {
                    switch (failed.status) {
                        case 403:
                            DialogFactory.messageDialog("REGISTRATION", ["Maaf Serial Number tidak valid !"], "sm");
                            break;
                        default:
                            DialogFactory.messageDialog("REGISTRATION", ["Maaf Passkey tidak valid!"], "sm");
                            break;
                    }
                }
            );

        }
    }
})();