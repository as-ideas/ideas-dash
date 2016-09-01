/**
 * Service that helps interacting with browser local storage
 */
angular.module('dashapp')
    .factory('Persistence', function () {

            // initialize storage
            var storage = window['localStorage'];
            if (!storage) {
                throw "go get a proper browser that supports local storage!";
            }
            var data = storage['data'] ? JSON.parse(storage['data']) : {};

            var persist = function () {
                storage['data'] = JSON.stringify(data);
            };

            // public api
            var persistenceService = {};

            /**
             * Initializes the persistence for your controller (auto-persist via watch)
             *
             * @param scope $scope object of your controller, persisted data will be bound to it under given property name
             * @param persistencePropertyName the name under which you access the persisted data in given scope object
             * @param initialState initial state. will only be set for properties previously undefined
             */
            persistenceService.init = function (scope, persistencePropertyName, initialState) {

                // bind data
                scope[persistencePropertyName] = data;

                // add watch
                scope.$watch(persistencePropertyName, function () {
                    persist();
                }, true);

                // set initial state
                data = angular.extend({}, initialState, data);

                persist();
            };

            return persistenceService;
        }
    );