'use strict';

// Register `phoneList` component, along with its associated controller and template
angular.module('locationDetails').component('locationDetails', {
    templateUrl: 'location-details/location-details.template.html',
    controller: ['$routeParams', 'Measurement',
        function ($routeParams, Measurement) {
            var self = this;
            console.log($routeParams);
            self.measurements = Measurement.points({'name': $routeParams.name, 'id': $routeParams.id});
        }
    ]
});
