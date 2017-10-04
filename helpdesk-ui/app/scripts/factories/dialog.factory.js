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
        function activationDialog(title) {
            modalInstance = uibModal.open({
                animation: true,
                templateUrl: 'views/activation-dialog.html',
                controller: ['$uibModalInstance', 'params', activationDialogController],
                controllerAs: '$ctrl',
                size: "md",
                resolve: {
                    params: function() {
                        return {
                            title: title
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
                templateUrl: 'views/message-dialog.html',
                controller: ['$uibModalInstance', 'params', messageDialogController],
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
                templateUrl: 'views/confirmation-dialog.html',
                controller: ['$uibModalInstance', 'params', confirmationDialogController],
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
                templateUrl: 'views/confirmation-with-messages-dialog.html',
                controller: ['$uibModalInstance', 'params', confirmationWithMesssageDialogController],
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
            ctrl.passkey = "";
            ctrl.reason = "";

            ctrl.ok = function() {
                $uibModalInstance.close({ passkey: ctrl.passkey, reason: ctrl.reason });
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