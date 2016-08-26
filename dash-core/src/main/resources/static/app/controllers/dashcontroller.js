var dashApp = angular.module('dash');

var dashController = function ($scope, $resource, $interval, AppConfig, PhilipsHue, GroupFilterUtils, StateUtils) {

    AppConfig.injectWhenLoaded($scope, 'title');

    var storage = window['localStorage'];

    // config object from/in localStorage
    $scope.config = storage && storage['config'] ? JSON.parse(storage['config']) : {};
    $scope.config = angular.extend({}, {
        teams: {},
        aggregate: true,
        aggregateDuplicated: true,
        showEmptyGroups: true,
        hue: {}
    }, $scope.config);

    // watch changes and write to storage
    $scope.$watch("config", function (config) {
        if (storage) {
            storage['config'] = JSON.stringify(config);
        }
    }, true);

    // infos rest resource
    var infosResource = $resource('rest/infos');

    // teams rest resource
    var teamsResource = $resource('rest/teams');

    teamsResource.get().$promise.then(function (teams) {
        $scope.teams = teams['teams'];

        // select all available teams if the user did not make a choice yet
        // (this is only the case when the team checkboxes have never been touched)
        if (isEmptyObject($scope.config.teams)) {
            selectAllTeams();
        }
    });

    // last update time. caused monitor to die if problems occur.
    $scope.lastUpdate = {};

    $scope.loadInfos = function () {
        // Do not store the result of query() into the $scope directly.
        // The rest call may take some time and query() returns an empty resource immediately and updates it later.
        // This leads to flickering
        var promise = infosResource.get().$promise;
        promise.then(function (infos) {

            $scope.lastUpdate.date = new Date();

            // be-app-start-id
            var applicationStartId = infos.applicationStartId;
            // if start id has changed reload the window
            if ($scope.applicationStartId && $scope.applicationStartId != applicationStartId) {
                window.location.reload(true);
            }
            // save start id
            $scope.applicationStartId = applicationStartId;

            // aggregate/filter
            for (var i = 0; i < infos.groups.length; i++) {

                var group = infos.groups[i];

                if ($scope.config[group.name] && $scope.config[group.name].aggregate) {
                    GroupFilterUtils.aggregateGreen(group);
                }

                GroupFilterUtils.filterByTeam(group, $scope.config.teams);

                if ($scope.config.showEmptyGroups) {
                    GroupFilterUtils.fillEmptyWithGreen(group);
                }

                if ($scope.config.aggregateDuplicated) {
                    GroupFilterUtils.aggregateDuplicated(group);
                }
            }

            $scope.overallState = StateUtils.overallState(infos.groups);

            if ($scope.config.hue['enabled']) {
                switchHueLights($scope.overallState);
            }

            $scope.infos = infos;

            if (infos == undefined || infos.groups == undefined) {
                $scope.lastUpdate.state = 'red';
            }

            $scope.lastUpdate.state = 'green';
        });
        promise.catch(function () {
            $scope.lastUpdate.state = 'red';
        });
    };

    // execute loadGroups every 30 seconds
    $interval($scope.loadInfos, 30 * 1000);

    // load groups initially
    $scope.loadInfos();

    $scope.checkClass = StateUtils.classForState;

    $scope.groupClass = function (group) {
        return StateUtils.classForState(StateUtils.groupState(group));
    };

    $scope.checkOrder = function (check) {

        if (check.order != undefined) {
            return check.order;
        }

        var state = check.state;
        return 4 - StateUtils.scoreForState(state);
    };

    function selectAllTeams() {
        angular.forEach($scope.teams, function (teamName) {
            $scope.config.teams[teamName] = true;
        });
    }

    function isEmptyObject(obj) {
        return Object.getOwnPropertyNames(obj).length == 0;
    }

    function switchHueLights(state) {
        if (state == "GREEN") {
            PhilipsHue.update($scope.config.hue, PhilipsHue.GREEN);
        } else if (state == "YELLOW") {
            PhilipsHue.update($scope.config.hue, PhilipsHue.YELLOW);
        } else if (state == "RED") {
            PhilipsHue.update($scope.config.hue, PhilipsHue.RED);
        } else {
            PhilipsHue.update($scope.config.hue, PhilipsHue.BLUE);
        }
    }
};

dashApp.controller('dashcontroller', dashController);
