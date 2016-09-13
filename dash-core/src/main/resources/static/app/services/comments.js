/**
 * Service for interaction with philips hue lights
 */
angular.module('dashapp')
    .factory('Comments', function ($resource, Persistence) {

            // backend-communication
            var commentResource = $resource('rest/comments');

            var syncComments = function () {

                // load comments from local storage/persistence
                var savedComments = Persistence.load('comments', []);

                // post them to the server
                commentResource.save(savedComments).$promise.then(function () {
                    // load from server
                    commentResource.query().$promise.then(function (serverComments) {
                        // save to storage
                        Persistence.save('comments', serverComments);
                    });
                });
            };

            // public API
            var commentService = {};

            commentService.comments = function () {
                syncComments();
                return Persistence.load('comments', []);
            };

            commentService.comment = function (comment) {
                commentResource.save([comment]);
                syncComments();
            };

            return commentService;
        }
    );
