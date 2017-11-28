'use strict';

angular.module('export').component('export', {
    templateUrl: 'export/export.template.html',
    controller: ['$window', 'Measurement',
        function ($window, Measurement) {
            this.alerts = [];
            this.download = function() {
                function goTo(target) {
                    var protocol = $window.location.protocol;
                    var host = $window.location.host;
                    return protocol + '//' + host + target;
                }
                this.addAlert('Download will begin shortly...');
                $window.location.href = goTo(Measurement.export);
            };

            this.addAlert = function (msg) {
                this.alerts.push({type: 'info', msg: msg});
            };

        }
    ]
});
