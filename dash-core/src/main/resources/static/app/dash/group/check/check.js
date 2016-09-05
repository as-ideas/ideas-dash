angular.module('dashapp')
    .directive('check', function () {

        var directive = {};

        directive.templateUrl = 'app/dash/group/check/check.html';
        directive.controllerAs = 'checkcontroller';
        directive.bindToController = true;

        directive.controller = function ($scope, Comments) {

            $scope.addComment = function (comment, checkResultIdentifier) {
                commentObject = {};
                commentObject.comment = comment;
                commentObject.checkResultIdentifier = checkResultIdentifier;
                Comments.comment(commentObject);
            };

            $scope.byCheck = function () {
                return function (check, foo, comments) {
                    var filteredComments = [];
                    angular.forEach(comments, function (comment) {
                        if (check['checkResultIdentifier'] == comment['checkResultIdentifier']) {
                            console.log("match");
                            console.log(check['checkResultIdentifier']);
                            console.log(comment['checkResultIdentifier']);
                            console.log("...");
                            filteredComments.push(comment);
                        }
                    });

                    return filteredComments;
                }
            }
        };

        return directive;
    });
