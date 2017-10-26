'use strict';

// Register `phoneList` component, along with its associated controller and template
angular.module('locations').component('locationList', {
    templateUrl: 'location-list/location-list.template.html',
    controller: ['LocationPoint',
        function (LocationPoint) {
            this.locationPoints = LocationPoint.query();
        }
    ]
});
