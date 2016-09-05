/**
 * Service for interaction with philips hue lights
 */
angular.module('dashapp')
    .factory('Comments', function ($resource, Persistence) {

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

            commentService.comments = function () {
                comments = commentResource.query();
                return comments;
            };

            commentService.comment = function (comment) {
                commentResource.save([comment]);
                comments = commentResource.query();
            };

            return commentService;
        }
    );