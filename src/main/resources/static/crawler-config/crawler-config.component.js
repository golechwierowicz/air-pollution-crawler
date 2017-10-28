'use strict';

angular.module('crawlerConfig').component('crawlerConfig', {
    templateUrl: 'crawler-config/crawler-config.template.html',
    controller: ['$cookies', 'Crawler',
        function ($cookies, Crawler) {
            this.xPath = '';
            this.filterWord = '';

            this.getCrawlingRequest = function () {
                return Crawler.crawlingRequest;
            };

            this.newCrawlingRequest = this.getCrawlingRequest() === undefined ? {
                xPaths: [],
                filterWords: [],
                filterByKeywordOnly: false
            } : this.getCrawlingRequest();

            this.saveConfig = function () {
                console.info('setting new crawling req', this.newCrawlingRequest);
                console.info('old', Crawler.crawlingRequest);
                Crawler.crawlingRequest = this.newCrawlingRequest;
                $cookies.putObject(Crawler.cookieKey, Crawler.crawlingRequest);
            };

            this.showXPath = function () {
                if (this.newCrawlingRequest === undefined)
                    return true;
                if (this.newCrawlingRequest.filterByKeywordOnly === undefined) {
                    this.newCrawlingRequest.filterByKeywordOnly = false;
                }
                return !this.newCrawlingRequest.filterByKeywordOnly;
            };

            this.removeXPath = function (index) {
                this.newCrawlingRequest.xPaths.splice(index, 1);
            };

            this.addXPath = function () {
                this.newCrawlingRequest.xPaths.push(this.xPath);
                this.xPath = '';
            };

            this.addFilterWord = function () {
                this.newCrawlingRequest.filterWords.push(this.filterWord);
                this.filterWord = '';
            };

            this.removeWord = function (index) {
                this.newCrawlingRequest.filterWords.splice(index, 1);
            }
        }
    ]
});
