angular.module('dashapp')
    .directive('check', function () {

        var directive = {};

        directive.templateUrl = 'app/dash/group/check/check.html';
        directive.controllerAs = 'checkcontroller';
        directive.bindToController = true;

        directive.controller = function ($scope, Comments) {

            $scope.addComment = function (comment, checkResultIdentifier) {
                var commentObject = {};
                commentObject.comment = comment;
                commentObject.checkResultIdentifier = checkResultIdentifier;
                Comments.comment(commentObject);
            };

            $scope.deleteComment = function (commentIdentifier) {
                var commentObject = {};
                commentObject.commentIdentifier = commentIdentifier;
                Comments.remove(commentObject);
            };
        };

        return directive;
    });

angular.module('dashapp')
    .filter('byCheckResultIdentifier', function () {
            return function (comments, checkResultIdentifier) {
                var filteredComments = [];
                angular.forEach(comments, function (comment) {
                    if (checkResultIdentifier == comment['checkResultIdentifier'] && !comment.deleted) {
                        filteredComments.push(comment);
                    }
                });

                return filteredComments;
            }
        }
    );
