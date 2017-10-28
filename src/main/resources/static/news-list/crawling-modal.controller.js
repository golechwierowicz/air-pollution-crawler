'use strict';

angular.module('newsList').controller('YesNoController', ['$scope', '$window', 'close', function($scope, $window, close) {

    $scope.close = function(result) {
        close(result, 500); // close, but give 500ms for bootstrap to animate
    };

    $scope.progressVal = 0;

    $scope.timer = $window.setInterval(function() {
        $scope.$apply(function() {
            $scope.progressVal += 10;
        });
        console.log($scope.progressVal);
        if($scope.progressVal > 100) {
            close(false, 500);
            $window.clearInterval($scope.timer);

        }
    }, 1000);

    $scope.getProgressVal = function() {
        return $scope.progressVal;
    }

}]);