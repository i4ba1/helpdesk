(function() {
    'use strict';
    angular.module('application')
        .directive('footerDirective', footerDirective);

    function footerDirective() {


        var directive = {
            restrict: 'E',
            templateUrl: "views/footer.html",
            replace: true
        };

        return directive;
    }
})();