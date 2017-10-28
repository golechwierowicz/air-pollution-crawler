'use strict';

angular.module('locationMap').component('locationMap', {
    templateUrl: 'location-map/location-map.template.html',
    controller: ['$window', 'LocationPoint',
        function ($window, LocationPoint) {
            this.locationPoints = LocationPoint.query();
            this.$window = $window;
            this.googleMapsUrl="https://maps.googleapis.com/maps/api/js?key=AIzaSyDIID1G3V8XpGzGPD--pm_6atJhZPWDh90";

            this.goToDetails = function() {
                $window.location.href = '#!/location_point/' + this.data.name + '/' + this.data.id;
            };
        }
    ]
});
