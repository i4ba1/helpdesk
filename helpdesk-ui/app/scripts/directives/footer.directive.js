(function() {
    'use strict';
    angular.module('application')
        .directive('footerDirective', footerDirective);

    function footerDirective() {


        var directive = {
            restrict: 'E',
            templateUrl: "sections/components/footer.html",
            replace: true
        };

        return directive;
    }
})();