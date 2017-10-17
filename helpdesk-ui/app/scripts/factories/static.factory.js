(function() {
    "use strict";

    angular.module("application")
        .factory("StaticFactory", [staticFactory]);

    function staticFactory() {
        const subModuleOptions = [{
            label: "Pilih Kelas",
            description: ''
        }, {
            label: "Jumlah Pengguna",
            description: ''
        }]

        const staticContainer = {
            subModuleOptions: subModuleOptions
        }

        return staticContainer;

    }

})();