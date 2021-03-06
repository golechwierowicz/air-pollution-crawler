'use strict';

angular.module('locationMap').component('locationMap', {
    templateUrl: 'location-map/location-map.template.html',
    controller: ['$window', 'LocationPoint',
        function ($window, LocationPoint) {
            this.locationPoints = LocationPoint.query();
            this.$window = $window;
            this.googleMapsUrl="https://maps.googleapis.com/maps/api/js?key=;

            this.goToDetails = function() {
                $window.location.href = '#!/location_point/' + this.data.name + '/' + this.data.id;
            };

            this.determineMarkerColor = function(id) {
                const url = 'https://raw.githubusercontent.com/Concept211/Google-Maps-Markers/master/images/marker_';
                const suffix = '.png';
                function combine(color) {
                    return url + color + suffix;
                }
                if(id < 2) {
                    return combine('green');
                } else if(id >= 2 && id < 4) {
                    return combine('yellow');
                } else if(id >= 4) {
                    return combine('black');
                }
            };


        }
    ]
});
