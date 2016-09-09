angular.module('dashapp')
    .directive('group', function () {

        var directive = {};

        directive.templateUrl = 'app/dash/group/group.html';
        directive.controllerAs = 'groupcontroller';
        directive.bindToController = true;

        directive.controller = function ($scope, $interval, Comments) {

            var loadComments = function () {
                $scope.comments = Comments.comments();
            };

            // load comments every 5 seconds
            $interval(loadComments, 5 * 1000);

            // and initially
            loadComments();
        };

        return directive;
    });
