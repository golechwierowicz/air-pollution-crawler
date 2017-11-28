'use strict';

angular.module('locationDetails').component('locationDetails', {
    templateUrl: 'location-details/location-details.template.html',
    controller: ['$routeParams', 'Measurement',
        function ($routeParams, Measurement) {
            const self = this;
            this.stationData = Measurement.measurements.points({'name': $routeParams.name, 'id': $routeParams.id});
        }
    ]
});
