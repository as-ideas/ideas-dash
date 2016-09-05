angular.module('dashapp')
    .component('dash', {
        templateUrl: 'app/dash/dash.html',
        controller: function ($scope, $resource, $interval, PhilipsHue, GroupFilterUtils, StateUtils, Persistence) {

            Persistence.init($scope, "config", {
                selectedTeams: {},
                showEmptyGroups: true,
                hue: {},
                showOptions: true
            });

            // infos rest resource
            var infosResource = $resource('rest/infos');

            // teams rest resource
            var teamsResource = $resource('rest/teams');

            teamsResource.get().$promise.then(function (teams) {

                $scope.teams = teams['teams'];

                // initialize new teams
                angular.forEach($scope.teams, function (team) {
                    if ($scope.config.selectedTeams[team] == undefined) {
                        $scope.config.selectedTeams[team] = true;
                    }
                });
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

                        GroupFilterUtils.filterByTeam(group, $scope.config.selectedTeams);

                        if ($scope.config.showEmptyGroups) {
                            GroupFilterUtils.fillEmptyWithGreen(group);
                        }

                        GroupFilterUtils.aggregateDuplicated(group);
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
        }
    });
