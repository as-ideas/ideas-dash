/**
 * Service for interaction with philips hue lights
 */
angular.module('dashapp')
    .factory('Comments', function ($resource, $timeout, Persistence) {

            // backend-communication
            var commentResource = $resource('rest/comments');

            // load comments from local storage/persistence
            var comments = Persistence.load('comments');

            // post them to the server
            commentResource.save(comments);

            // public API
            var commentService = {};

        commentService.startPollingComments = function ($scope, fieldName) {
            commentResource.query().$promise.then(function (comments) {
                $scope[fieldName] = comments;
                Persistence.save('comments', comments);
                $timeout(commentService.startPollingComments, 5000, true, $scope, fieldName);
            });
                return comments;
            };

            commentService.comment = function (comment) {
                commentResource.save([comment]);
            };

            return commentService;
        }
    );