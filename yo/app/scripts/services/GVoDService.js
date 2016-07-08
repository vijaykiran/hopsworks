'use strict';
/*
 * Service allowing fetching topic history objects by type.
 */
angular.module('hopsWorksApp')

        .factory('GVoDService', ['$http', function ($http) {
                var service = {
                    downloadHdfs: function (json) {
                        return $http({
                            method: 'PUT',
                            url: '/api/gvod/downloadhdfs',
                            data: json
                        });
                    },
                    downloadKafka: function (json) {
                        return $http({
                            method: 'PUT',
                            url: '/api/gvod/downloadKafka',
                            data: json
                        });
                    }
                };
                return service;
            }]);
