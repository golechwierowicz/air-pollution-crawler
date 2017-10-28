'use strict';

angular.module('phonecatApp').config(['$locationProvider', '$routeProvider',
    function ($locationProvider, $routeProvider) {
        $locationProvider.hashPrefix('!');

        $routeProvider
            .when('/location_points', {
                template: '<location-list></location-list>'
            })
            .when('/location_point/:name/:id', {
                template: '<location-details></location-details>'
            })
            .when('/map', {
                template: '<location-map></location-map>'
            })
            .otherwise('/location_points');
    }
]);
