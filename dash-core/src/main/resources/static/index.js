angular.module('dashapp', ['ngResource', 'ngSanitize'])
    .controller('indexcontroller', function ($scope, AppConfig) {

        // inject page title
        AppConfig.get().$promise.then(function (config) {
            $scope['title'] = config['title'];
        });
    })
;