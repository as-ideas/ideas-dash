/**
 * Service for interaction with philips hue lights
 */
angular.module('dashapp')
    .factory('PhilipsHue', function ($resource) {

            var hueService = {};

            hueService.GREEN = {"on": true, "hue": 25500, "sat": 254, "bri": 180};
            hueService.YELLOW = {"on": true, "hue": 9000, "sat": 254, "bri": 254};
            hueService.RED = {"on": true, "hue": 0, "sat": 254, "bri": 254};
            hueService.BLUE = {"on": true, "hue": 46000, "sat": 254, "bri": 254};

            hueService.update = function (hueConfig, data) {

                // check params
                if (!hueConfig.url || !hueConfig.key || !hueConfig.light || !data) {
                    console.error("invalid parameters, cannot update hue");
                    return;
                }

                hueConfig.light.split(',').forEach(light => {
                    var hueResource = $resource(hueConfig['url'] + "/api/" + hueConfig['key'] + "/lights/" + light + "/state", null, {
                        'update': {
                            method: 'PUT',
                            isArray: true
                        }
                    });
                    hueResource.update(data);
                })
            };

            return hueService;
        }
    );

