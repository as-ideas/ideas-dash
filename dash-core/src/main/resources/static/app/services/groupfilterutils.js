/**
 * Utilities that help transforming group data for the view
 */
angular.module('dashapp')
    .factory('GroupFilterUtils', function () {

            /**
             * Helper that determines the existence of a check in a collection of checks
             */
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
             * used to group duplicated checks (better overview on monitor)
             */
            var aggregateDuplicated = function (group) {

                var filteredGroup = angular.copy(group);

                // new array for aggregated checks
                var groupedChecks = [];

                // iterate over checks, for each check look for already existing check in groupedChecks
                for (var i = 0; i < filteredGroup.checks.length; i++) {
                    var checkOne = filteredGroup.checks[i];
                    if (!containsCheck(checkOne, groupedChecks)) {
                        groupedChecks.push(checkOne);
                    }
                }

                // replace original checks with aggregated checks
                filteredGroup.checks = groupedChecks;

                return filteredGroup;
            };

            /**
             * Used to filter a groups check by enabled teams
             *
             * @param group that shall be filtered
             * @param enabledTeams collections of teams that are enabled in the ui
             */
            var filterByTeam = function (group, enabledTeams) {

                var filteredGroup = angular.copy(group);

                // new array for filtered checks
                var filteredChecks = [];

                // iterate over checks and add to filtered teams if team selected or no team selected
                for (var i = 0; i < filteredGroup.checks.length; i++) {

                    var check = filteredGroup.checks[i];

                    // teams that the checkresult is mapped to (can be more than one)
                    var teams = check.teams;

                    // team can be null or empty, because a check might not contain a team.
                    // in this case, the check should be displayed (for everyone)
                    if (!teams || teams.length == 0) {
                        filteredChecks.push(check);
                        continue;
                    }

                    // only add check if one of the teams the checkresult is mapped to is enabled in the ui
                    for (var j = 0; j < teams.length; j++) {

                        if (enabledTeams[teams[j]]) {
                            filteredChecks.push(check);
                            break;
                        }
                    }
                }

                // replace original checks with aggregated checks
                filteredGroup.checks = filteredChecks;

                return filteredGroup;
            };

            /**
             * used to aggregate green checks in a group
             */
            var aggregateGreen = function (group) {

                var filteredGroup = angular.copy(group);

                // new array for aggregated checks
                var aggregatedChecks = [];

                // count green checks
                var greenCount = 0;

                // iterate over checks, put non-green in aggregated list, count green
                for (var i = 0; i < filteredGroup.checks.length; i++) {
                    var check = filteredGroup.checks[i];
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
                filteredGroup.checks = aggregatedChecks;

                return filteredGroup;
            };

            /**
             * Puts a "fine" - check result in the group if there are no real results
             */
            var fillEmptyWithGreen = function (group) {

                var filteredGroup = angular.copy(group);

                if (filteredGroup.checks.length < 1) {
                    filteredGroup.checks.push({
                        name: 'F-I-N-E',
                        info: 'F-I-N-E',
                        state: 'GREEN'
                    });
                }

                return filteredGroup;
            };

        var groupFilterUtils = {};

        groupFilterUtils.filter = function (groups, config) {

            var filteredGroups = [];
            if (!groups) {
                return filteredGroups;
            }

            // aggregate/filter
            for (var i = 0; i < groups.length; i++) {

                var group = groups[i];

                if (config.aggregateAll || (config[group.name] && config[group.name].aggregate)) {
                    group = aggregateGreen(group);
                }

                group = filterByTeam(group, config.selectedTeams);

                if (config.showEmptyGroups) {
                    group = fillEmptyWithGreen(group);
                }

                group = aggregateDuplicated(group);

                filteredGroups.push(group);
            }

            return filteredGroups;
            };

            return groupFilterUtils;
        }
    );
