angular.module('dash', ['ngResource', 'ngSanitize'])
    .controller('dashcontroller', function ($scope, $resource, $interval) {

        // team filter
        $scope.team = "All Teams";

        // green aggregation
        $scope.aggregate = undefined;

        // infos rest resource
        var infosResource = $resource('rest/infos');

        // teams rest resource
        var teamsResource = $resource('rest/teams');

        $scope.teams = teamsResource.get().teams;

        // last update time. caused monitor to die if problems occur.
        $scope.lastUpdate = {};

        /**
         * used to aggregate green checks (better overview on monitor)
         *
         * @param group
         * @returns {*}
         */
        var aggregate = function (group) {

            // if no aggregation is requested, do nothing
            if (!$scope.aggregate) {
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

        $scope.loadInfos = function () {
            // Do not store the result of query() into the $scope directly.
            // The rest call may take some time and query() returns an empty resource immediately and updates it later.
            // This leads to flickering
            var promise = infosResource.get().$promise;
            promise.then(function (infos) {

                $scope.lastUpdate.date = new Date();

                // aggregate
                var aggregatedGroups = [];
                for (var i = 0; i < infos.groups.length; i++) {
                    aggregatedGroups[i] = aggregate(infos.groups[i]);
                }

                $scope.infos = infos;

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

                // handle check that has no defined team, belongs to team (or all if All Teams is active)
                if ($scope.team == "All Teams" || !check.team || check.team == "" || $scope.team == check.team) {
                    // aggregate state (worst of two :))
                    state = aggregateState(state, check.state);
                }
            }
            return $scope.classForState(state);
        };

        $scope.groupOrder = function (group) {

            var name = group.name;

            var score = 1;
            var groupScores = {
                'PROD': score++,
                'UAT': score++,
                'CONS': score++,
                'DEV': score++
            };

            var groupScore = groupScores[name];
            return groupScore || score;
        };

        $scope.checkOrder = function (check) {

            var state = check.state;
            return 4 - scoreForState(state);
        };

        $scope.teamFilter = function (check) {

            if ($scope.team == "All Teams") {
                return true;
            }

            if (check.team == undefined || check.team.length < 1) {
                return true;
            }

            if (check.team == $scope.team) {
                return true;
            }

            return false;
        };
    }
);