'use strict';

angular.module('airPollutionApp').config(['$locationProvider', '$routeProvider',
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
            .when('/crawler', {
                template: '<news-list></news-list>'
            })
            .when('/config', {
                template: '<crawler-config></crawler-config>'
            })
            .when('/export', {
                template: '<export></export>'
            })
            .otherwise('/map');
    }
]);
