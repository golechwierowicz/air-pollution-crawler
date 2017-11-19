'use strict';

angular.module('newsList').component('newsList', {
    templateUrl: 'news-list/news-list.template.html',
    controller: ['$scope', '$cacheFactory', '$window', '$cookies', 'Crawler', 'ModalService', 'ngClipboard',
        function ($scope, $cacheFactory, $window, $cookies, Crawler, ModalService, ngClipboard) {
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
                        console.log(result)
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
            this.result = Crawler.crawlingResult === undefined ? [] : Crawler.crawlingResult;
            this.timerCalled = 0;

            this.crawl = function () {
                Crawler.crawl.query(Crawler.crawlingRequest).$promise.then(data => {
                    this.id = data.id;
                    this.timer = $window.setInterval(() => {
                        Crawler.result.query({id: this.id}).$promise.then((data) => {
                            this.result = data;
                            $cookies.putObject(Crawler.cookieCrawlerResultKey, this.result.slice(0, 5)); // save 5 results to cookie
                            this.cache.put('id', this.id);
                        });
                        if (++this.timerCalled > 10) {
                            $window.clearInterval(this.timer);
                        }
                    }, 1000);
                });
                this.showModal();
            };

            this.fetchAdditional = function() {
                let currentLength = this.result.length;
                Crawler.result.query({id: this.id}).$promise.then((data) => {
                    this.result = data;
                    this.totalItems = data.length;
                    this.cache.put('result', data);
                    this.cache.put('id', this.id);
                    $cookies.putObject(Crawler.cookieCrawlerResultKey, data);
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

            this.copy = function(webContent) {
                ngClipboard.toClipboard(webContent);
            }
        }
    ]
});
