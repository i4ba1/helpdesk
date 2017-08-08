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
                templateUrl: 'sections/components/activation-dialog.html',
                controller: activationDialogController,
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

            return modalInstance.result;

        }

        /**
         * @param {*} title 
         * @param {*} messsages 
         * @param {*} size 
         */
        function messageDialog(title, messsages, size) {
            modalInstance = uibModal.open({
                animation: true,
                templateUrl: 'sections/components/message-dialog.html',
                controller: messageDialogController,
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

            return modalInstance.result;
        }

        /**
         * @param {*} title 
         * @param {*} messsage 
         * @param {*} size 
         */
        function confirmationDialog(title, messsage, size) {
            modalInstance = uibModal.open({
                animation: true,
                templateUrl: 'sections/components/confirmation-dialog.html',
                controller: confirmationDialogController,
                controllerAs: '$ctrl',
                size: size,
                backdrop: 'static',
                resolve: {
                    params: function() {
                        return {
                            title: title,
                            messsage: messsage
                        };
                    }
                }
            });

            return modalInstance.result;
        }

        /**
         * @param {*} title 
         * @param {*} messsage 
         * @param {*} size 
         */
        function confirmationWithMessageDialog(title, messsage, placeholder) {
            modalInstance = uibModal.open({
                animation: true,
                templateUrl: 'sections/components/confirmation-with-messages-dialog.html',
                controller: confirmationWithMesssageDialogController,
                controllerAs: '$ctrl',
                size: "md",
                backdrop: 'static',
                resolve: {
                    params: function() {
                        return {
                            title: title,
                            messsage: messsage,
                            placeholder: placeholder
                        };
                    }
                }
            });

            return modalInstance.result;
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

        function messageDialogController($uibModalInstance, params) {
            var ctrl = this;
            ctrl.title = params.title;
            ctrl.messages = params.messsages;

            ctrl.ok = function() {
                $uibModalInstance.close("close");
            };


        }

        function confirmationDialogController($uibModalInstance, params) {
            var ctrl = this;
            ctrl.title = params.title;
            ctrl.message = params.messsage;

            ctrl.ok = function() {
                $uibModalInstance.close("yes");
            };

            ctrl.dismiss = function() {
                $uibModalInstance.dismiss("no");
            }
        }

        function confirmationWithMesssageDialogController($uibModalInstance, params) {
            var ctrl = this;
            ctrl.title = params.title;
            ctrl.message = params.messsage;
            ctrl.placeholder = params.placeholder;
            ctrl.reason = "";

            ctrl.ok = function() {
                $uibModalInstance.close(ctrl.reason);
            };

            ctrl.dismiss = function() {
                $uibModalInstance.dismiss("no");
            }
        }
        // =======================[END Message Dialog Controller]===========

        var dialogContainer = {
            activationDialog: activationDialog,
            messageDialog: messageDialog,
            confirmationDialog: confirmationDialog,
            confirmationWithMessageDialog: confirmationWithMessageDialog
        };

        return dialogContainer;


    }
})();