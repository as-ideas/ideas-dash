angular.module('dash', ['ngResource', 'ngSanitize'])
    .controller('dashcontroller', function ($scope, $resource, $interval) {

        // teams rest resource
        var configResource = $resource('rest/config');

        configResource.get().$promise.then(function (config) {
            $scope.title = config['title'];
        });

        var storage = window['localStorage'];

        // config object from/in localStorage
        $scope.config = storage && storage['config'] ? JSON.parse(storage['config']) : {};
        $scope.config = angular.extend({}, {
            teams: {},
            aggregate: true,
            aggregateDuplicated: true
        }, $scope.config);

        // migration from old team config to teams object, can be removed once all monitors ran this version
        if ($scope.config.team) {
            $scope.config.teams[$scope.config.team] = true;
            delete $scope.config.team;
        }

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

        /**
         * used to aggregate green checks (better overview on monitor)
         *
         * @param group
         * @returns {*}
         */
        var aggregateGreenChecks = function (group) {

            // if no aggregation is requested, do nothing
            if (!$scope.config.aggregate) {
                return group;
            }

            // new array for aggregated checks
            var aggregatedChecks = [];

            // count green checks
            var greenCount = 0;

            // iterate over checks, put non-green in aggregated list, count green
            for (var i = 0; i < group.checks.length; i++) {
                var check = group.checks[i];
                var state = check.state;
                if (state == 'GREEN') {
                    greenCount++;
                } else {
                    aggregatedChecks.push(check);
                }
            }

            // put "fine"-entry in aggragated checks
            if (greenCount > 0) {
                aggregatedChecks.push({
                    name: 'F-I-N-E',
                    info: greenCount + ' Checks',
                    state: 'GREEN'
                });
            }

            // replace original checks with aggregated checks
            group.checks = aggregatedChecks;
        };

        var containsCheck = function (checkOne, arrayWithChecks) {
            for (var j = 0; j < arrayWithChecks.length; j++) {
                var checkAnother = arrayWithChecks[j];
                if (checkOne.name === checkAnother.name && checkOne.info === checkAnother.info && checkOne.state === checkAnother.state) {
                    return true;
                }
            }
            return false;
        };

        /**
         * used to gruop duplicated checks (better overview on monitor)
         *
         * @param group
         * @returns {*}
         */
        var aggregateDuplicated = function (group) {

            // if no aggregation is requested, do nothing
            if (!$scope.config.aggregateDuplicated) {
                console.info('Skip aggregate duplicated');
                return group;
            }

            // new array for aggregated checks
            var groupedChecks = [];

            // iterate over checks, for each check look for already existing check in groupedChecks
            for (var i = 0; i < group.checks.length; i++) {
                var checkOne = group.checks[i];
                if (!containsCheck(checkOne, groupedChecks)) {
                    groupedChecks.push(checkOne);
                }
            }

            // replace original checks with aggregated checks
            group.checks = groupedChecks;
        };

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

                // aggregate
                for (var i = 0; i < infos.groups.length; i++) {
                    aggregateGreenChecks(infos.groups[i]);
                    aggregateDuplicated(infos.groups[i]);
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

        var aggregateState = function (state1, state2) {
            if (scoreForState(state1) > scoreForState(state2)) {
                return state1;
            }
            return state2;
        };

        var scoreForState = function (state) {
            if (state == 'GREEN' || state == 'green') {
                return 0;
            }
            if (state == 'GREY' || state == 'grey') {
                return 1;
            }
            if (state == 'YELLOW' || state == 'yellow') {
                return 2;
            }
            if (state == 'RED' || state == 'red') {
                return 3;
            }
            return -1;
        };

        $scope.classForState = function (state) {

            if (state == 'GREEN' || state == 'green') {
                return "alert-success";
            }
            if (state == 'GREY' || state == 'grey') {
                return "alert-disabled";
            }
            if (state == 'YELLOW' || state == 'yellow') {
                return "alert-warning";
            }
            return "alert-danger";
        };

        $scope.groupClass = function (group) {

            var state = 'GREEN';
            for (var i = 0; i < group.checks.length; i++) {

                var check = group.checks[i];

                // include the check only if it belongs to the displayed team(s)
                if (isTeamSelected(check.team)) {
                    // aggregate state (worst of two :))
                    state = aggregateState(state, check.state);
                }
            }
            return $scope.classForState(state);
        };

        $scope.checkOrder = function (check) {

            var state = check.state;
            return 4 - scoreForState(state);
        };

        $scope.teamFilter = function (check) {
            return isTeamSelected(check.team);
        };

        function isTeamSelected(team) {
            // team can be null, because a check might not contain a team.
            // in this case, the check should be displayed
            if (!team) {
                return true;
            }

            return $scope.config.teams[team];
        }

        function selectAllTeams() {
            angular.forEach($scope.teams, function (teamName) {
                $scope.config.teams[teamName] = true;
            });
        }

        function isEmptyObject(obj) {
            return Object.getOwnPropertyNames(obj).length == 0;
        }
    }
);