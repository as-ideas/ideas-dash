/**
 * Service for interaction with philips hue lights
 */
angular.module('dashapp')
    .factory('PhilipsHue', function ($resource) {

        var hueService = {};

        hueService.GREEN = {"on": true, "hue": 25500, "sat": 254, "bri": 180};
        hueService.YELLOW = {"on": true, "hue": 12750, "sat": 254, "bri": 254};
        hueService.RED = {"on": true, "hue": 0, "sat": 254, "bri": 254};
        hueService.BLUE = {"on": true, "hue": 46000, "sat": 254, "bri": 254};

        hueService.update = function (hueConfig, data) {

            // check params
            if (!hueConfig['ip'] || !hueConfig['key'] || !hueConfig['light'] || !data) {
                console.error("invalid parameters, cannot update hue");
                return;
            }

            var hueResource = $resource("http://" + hueConfig['ip'] + "/api/" + hueConfig['key'] + "/lights/" + hueConfig['light'] + "/state", null, {
                'update': {
                    method: 'PUT',
                    isArray: true
                }
            });
            hueResource.update(data);
        };

        return hueService;
        }
    );

