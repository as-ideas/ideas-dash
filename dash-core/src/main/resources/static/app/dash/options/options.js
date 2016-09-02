angular.module('dashapp')
    .directive('options', function () {

        var directive = {};

        directive.templateUrl = 'app/dash/options/options.html';
        directive.controllerAs = 'optionscontroller';
        directive.bindToController = true;

        return directive;
    });
