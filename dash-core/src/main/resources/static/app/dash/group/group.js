angular.module('dashapp')
    .directive('group', function () {

        var directive = {};

        directive.templateUrl = 'app/dash/group/group.html';
        directive.controllerAs = 'groupcontroller';
        directive.bindToController = true;

        directive.controller = function ($scope, $interval, Comments) {

            var loadComments = function () {
                Comments.comments().$promise.then(function (comments) {
                    $scope.comments = comments;
                });
            };

            // load comments every 10 seconds
            $interval(loadComments(), 10 * 1000);

            // initially load comments
            loadComments();
        };

        return directive;
    });
