/**
 * Utilities that are state-related
 */

angular.module('dashapp')
    .factory('StateUtils', function () {

        var stateUtils = {};

        /**
         * Gives you a score for a state (higher is worse)
         */
        stateUtils.scoreForState = function (state) {

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

        /**
         * Gives you the worst of two states
         */
        stateUtils.aggregateState = function (state1, state2) {
            if (stateUtils.scoreForState(state1) > stateUtils.scoreForState(state2)) {
                return state1;
            }
            return state2;
        };

        /**
         * Maps a state to a css-class
         */
        stateUtils.classForState = function (state) {

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

        /**
         * Gives you the worst check state of a group
         */
        stateUtils.groupState = function (group) {
            var state = 'GREEN';
            for (var i = 0; i < group.checks.length; i++) {

                var check = group.checks[i];
                state = stateUtils.aggregateState(state, check.state);
            }
            return state;
        };

        /**
         * Gives you the worst state of all groups
         * @param groups
         */
        stateUtils.overallState = function (groups) {

            var overallState = "GREEN";
            for (var j = 0; j < groups.length; j++) {
                overallState = stateUtils.aggregateState(stateUtils.groupState(groups[j]), overallState);
            }
            return overallState;
        };

        return stateUtils;
        }
    );

