var dashApp = angular.module('dash');

var appConfigServiceFactory = function ($resource) {

    var appConfigService = {};

    var appConfig = $resource('rest/config').get();

    appConfigService.injectWhenLoaded = function (scope, key) {
        appConfig.$promise.then(function (config) {
            scope[key] = config[key];
        });
    };

    return appConfigService;
};

dashApp.factory('AppConfig', appConfigServiceFactory);

