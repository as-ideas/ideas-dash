/**
 * Service for interaction with philips hue lights
 */
angular.module('dashapp')
    .factory('Comments', function ($resource, $interval, Persistence) {

            // backend-communication
            var commentResource = $resource('rest/comments');

            // load comments from local storage/persistence
            var comments = Persistence.load('comments');

            // post them to the server
            commentResource.save(comments);

            // load all comments from server
            comments = commentResource.query();

            Persistence.save('comments', comments);

            // public API
            var commentService = {};

        // TODO: regular update

            commentService.comments = function () {
                return comments;
            };

            commentService.comment = function (comment) {
                comments.push(comment);
                Persistence.save('comments', comments);
            };

            return commentService;
        }
    );