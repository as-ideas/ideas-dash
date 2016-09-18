angular.module('dashapp', ['ngResource', 'ngSanitize'])
    .config(['$httpProvider', function ($httpProvider) {
        $httpProvider.defaults.xsrfCookieName = 'csrftoken';
        $httpProvider.defaults.xsrfHeaderName = 'X-CSRFToken';
    }])
    .controller('indexcontroller', function ($scope, AppConfig) {

        // inject page title
        AppConfig.get().$promise.then(function (config) {
            $scope['title'] = config['title'];
        });
    })
;