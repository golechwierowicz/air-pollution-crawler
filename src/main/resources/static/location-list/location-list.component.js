'use strict';

angular.module('locations').component('locationList', {
    templateUrl: 'location-list/location-list.template.html',
    controller: ['LocationPoint',
        function (LocationPoint) {
            this.locationPointsAll = [];
            this.maxSize = 5;
            this.pageSize = 10;
            this.currentPage = 1;
            this.totalItems = 1;

            this.getLocationPointDisplay = function () {
                return this.locationPointsAll.slice(this.currentPage * this.pageSize, this.currentPage * this.pageSize + this.pageSize);
            };

            LocationPoint.query().$promise.then((data) => {
                data.sort((a, b) => {
                    let x = a.name.toLowerCase();
                    let y = b.name.toLowerCase();
                    return x < y ? -1 : x > y ? 1 : 0;
                });
                this.locationPointsAll = data;
                this.totalItems = this.locationPointsAll.length;
            });
        }
    ]
});
