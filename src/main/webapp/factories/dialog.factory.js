(function() {
    'use strict';

    angular.module('application')
        .factory("DialogFactory", DialogFactory);

    DialogFactory.$inject = ['$uibModal'];

    function DialogFactory(uibModal) {
        var service = {
            showDialogMsg: showDialogMsg
        };

        var modalInstance = null;


        return service;

        function showDialogMsg(title, content, size, contentExtra) {
            modalInstance = uibModal.open({
                animation: true,
                templateUrl: 'sections/components/dialog.html',
                controller: function($uibModalInstance, params) {
                    var ctrl = this;
                    ctrl.title = params.title;
                    ctrl.content = params.content;
                    ctrl.contentExtra = params.contentExtra;

                    ctrl.ok = function() {
                        $uibModalInstance.close("close");
                    };

                },
                controllerAs: '$ctrl',
                size: size,
                resolve: {
                    params: function() {
                        return {
                            title: title,
                            content: content,
                            contentExtra: contentExtra
                        };
                    }
                }
            });

        }

    }
})();