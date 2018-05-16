/**
 * Service for interaction with philips hue lights
 */
angular.module('dashapp')
    .factory('Comments', function ($resource, $interval, Persistence) {

        var commentsContainer = { comments: [] }

        // backend-communication
        var commentResource = $resource('rest/comments', {}, {
            save: {
                method: "POST",
                isArray: true
            }
        });

        var syncComments = function () {
            // load comments from local storage/persistence
            var savedComments = Persistence.load('comments', []);
            commentsContainer.comments = savedComments

            // post them to the server
            commentResource.save(savedComments).$promise.then(function (serverComments) {
                Persistence.save('comments', serverComments);
                commentsContainer.comments = serverComments
            });
        };

        // public API
        var commentService = {};

        commentService.startSync = function () {
            $interval(syncComments, 5 * 1000)
            syncComments()

            return commentsContainer
        };

        commentService.comment = function (comment) {
            commentsContainer.comments.splice(-1, 0, comment)
            syncComments()
        };

        commentService.remove = function (comment) {
            comment.deleted = true;
            commentResource.save([comment]);
            syncComments()
        };

        return commentService;
    }
);
