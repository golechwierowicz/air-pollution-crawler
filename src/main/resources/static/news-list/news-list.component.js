'use strict';

angular.module('newsList').component('newsList', {
    templateUrl: 'news-list/news-list.template.html',
    controller: ['$scope', '$cacheFactory', '$window' ,'Crawler', 'ModalService',
        function ($scope, $cacheFactory, $window, Crawler, ModalService) {
            this.showModal = function () {
                ModalService.showModal({
                    templateUrl: "news-list/crawling-modal.template.html",
                    controller: "YesNoController",
                    preClose: (modal) => {
                        modal.element.modal('hide');
                    }
                }).then(modal => {
                    modal.element.modal();
                    modal.close.then(function (result) {
                        $scope.yesNoResult = result ? "You said Yes" : "You didn't say Yes";
                    });
                });
            };
            this.cacheName = 'crawl';
            this.cache = $cacheFactory.get(this.cacheName) === undefined
                ? $cacheFactory(this.cacheName) : $cacheFactory.get(this.cacheName);
            this.currentPage = 1;
            this.pageSize = 10;
            this.alerts = [];

            this.id = this.cache.get('id') === undefined ? '' : this.cache.get('id');
            this.result = this.cache.get('result') === undefined ? [] : this.cache.get('result');
            this.timerCalled = 0;

            this.crawl = function () {
                Crawler.crawl.query(Crawler.crawlingRequest).$promise.then(data => {
                    this.id = data.id;
                    this.timer = $window.setInterval(() => {
                        Crawler.result.query({id: this.id}).$promise.then((data) => {
                            this.result = data;
                            this.cache.put('result', data);
                            this.cache.put('id', this.id);
                        });
                        if (++this.timerCalled > 30) {
                            $window.clearInterval(this.timer);
                        }
                    }, 1000);
                });
                this.showModal();
            };

            this.getResult = function () {
                Crawler.result.query({id: this.id}).$promise.then((data) => {
                    this.result = data;
                    this.totalItems = data.length;
                    this.cache.put('result', data);
                    this.cache.put('id', this.id);
                });
            };

            this.fetchAdditional = function() {
                let currentLength = this.result.length;
                Crawler.result.query({id: this.id}).$promise.then((data) => {
                    this.result = data;
                    this.totalItems = data.length;
                    this.cache.put('result', data);
                    this.cache.put('id', this.id);
                    if(data.length === currentLength) {
                        this.addAlert('Nothing to fetch');
                    }
                });
            };

            this.getCrawledContentDisplay = function() {
                this.totalItems = this.result.length;
                return this.result.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
            };

            this.addAlert = function (msg) {
                this.alerts.push({type: 'warning', msg: msg});
            };

            this.closeAlert = function (index) {
                this.alerts.splice(index, 1);
            };
        }
    ]
});
