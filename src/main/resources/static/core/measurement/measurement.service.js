'use strict';

angular.module('core.measurement').factory('Measurement', ['$resource',
    function ($resource) {
        return $resource('api/rest/station/name/:name/id/:id/measurements', {}, {
            points: {
                params: {name: 'name', id: 'id'},
                method: 'GET'
            }
        });
    }
]);