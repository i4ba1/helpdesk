(function() {
    'use strict';

    angular.module('application')
        .factory("DialogFactory", DialogFactory);

    DialogFactory.$inject = ['$uibModal'];

    function DialogFactory(uibModal) {
        var modalInstance = null;

        // ====================[START Message Dialog Creator]=================
        /**
         * @param {*} title 
         * @param {*} content 
         * @param {*} size 
         * @param {*} contentExtra 
         */
        function activationDialog(title, content, size, contentExtra) {
            modalInstance = uibModal.open({
                animation: true,
                templateUrl: 'sections/components/dialog.html',
                controller: activationDialogControllerfunction,
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

        /**
         * @param {*} title 
         * @param {*} messsages 
         * @param {*} size 
         */
        function errorDialog(title, messsages, size) {
            modalInstance = uibModal.open({
                animation: true,
                templateUrl: 'sections/components/error-dialog.html',
                controller: null,
                controllerAs: '$ctrl',
                size: size,
                resolve: {
                    params: function() {
                        return {
                            title: title,
                            messsages: messsages
                        };
                    }
                }
            });
        }

        // =======================[END Message Dialog Creator]==============

        // =====================[START Message Dialog Controller]===========
        function activationDialogController($uibModalInstance, params) {
            var ctrl = this;
            ctrl.title = params.title;
            ctrl.content = params.content;
            ctrl.contentExtra = params.contentExtra;

            ctrl.ok = function() {
                $uibModalInstance.close("close");
            };


        }

        function errorDialogController($uibModalInstance, params) {
            var ctrl = this;
            ctrl.title = params.title;
            ctrl.messages = params.content;

            ctrl.ok = function() {
                $uibModalInstance.close("close");
            };


        }
        // =======================[END Message Dialog Controller]===========

        var dialogContainer = {
            activationDialog: activationDialog,
            errorDialog: errorDialog
        };

        return dialogContainer;


    }
})();