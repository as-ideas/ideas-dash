angular.module('dashapp')
    .directive('groupinfo', function () {

        var directive = {};

        directive.templateUrl = 'app/dash/group/info/groupinfo.html';
        directive.controllerAs = 'groupinfocontroller';
        directive.bindToController = true;

        directive.controller = function ($scope) {

        };

        return directive;
    });
