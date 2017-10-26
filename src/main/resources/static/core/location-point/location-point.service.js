'use strict';

angular.module('core.locationPoint').factory('LocationPoint', ['$resource',
    function ($resource) {
        return $resource('api/rest/loc_points', {}, {
            query: {
                method: 'GET',
                isArray: true
            }
        });
    }
]);