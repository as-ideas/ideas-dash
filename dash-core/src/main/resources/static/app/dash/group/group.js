angular.module('dashapp')
    .directive('group', function () {

        var directive = {};

        directive.templateUrl = 'app/dash/group/group.html';
        directive.controllerAs = 'groupcontroller';
        directive.bindToController = true;

        directive.controller = function ($scope) {

        };

        return directive;
    });
