'use strict';

angular.module('locations').component('locationList', {
    templateUrl: 'location-list/location-list.template.html',
    controller: ['LocationPoint',
        function (LocationPoint) {
            this.locationPoints = LocationPoint.query();
        }
    ]
});
