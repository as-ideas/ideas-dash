angular.module('dash')
    .factory('AppConfig', function ($resource) {

            var appConfigService = {};

            var appConfig = $resource('rest/config').get();

            appConfigService.get = function () {
                return appConfig;
            };

            return appConfigService;
        }
    );


