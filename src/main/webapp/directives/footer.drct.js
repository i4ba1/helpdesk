(function() {
    'use strict';
    angular.module('application')
        .directive('footerDirective', footerDirective);

    function footerDirective() {
        var template = '<!-- Main Footer -->' +
            '<footer class="footer">' +
            '<strong>Copyright &copy; 2016 - ' + (new Date().getFullYear()) + ', <a ui-sref="administrator.about-us">Kharisma Nusantara Teknologi Company</a></strong>' +
            '</footer>';

        var directive = {
            restrict: 'E',
            template: template,
            replace: true
        };

        return directive;
    }
})();