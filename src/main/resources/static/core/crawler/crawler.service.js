'use strict';

angular.module('core.crawler').factory('Crawler', ['$resource', '$cookies',
    function ($resource, $cookies) {
        return {
            cookieKey: 'crawlingRequest',
            crawl: $resource(
                'api/crawl', {}, {
                    query: {
                        method: 'POST'
                    }
                }),
            result: $resource(
                'api/crawl/:id', {}, {
                    query: {
                        params: {id: 'id'},
                        method: 'GET',
                        isArray: true
                    }
                }),
            crawlingRequest: $cookies.getObject('crawlingRequest')
        }
    }
]);