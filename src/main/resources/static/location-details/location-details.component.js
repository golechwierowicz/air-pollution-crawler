'use strict';

angular.module('locationDetails').component('locationDetails', {
    templateUrl: 'location-details/crawler-config.template.html',
    controller: ['$routeParams', 'Measurement',
        function ($routeParams, Measurement) {
            var self = this;
            self.stationData = Measurement.points({'name': $routeParams.name, 'id': $routeParams.id});
        }
    ]
});
