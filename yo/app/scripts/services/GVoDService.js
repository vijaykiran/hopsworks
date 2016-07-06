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
                            url: '/api/download/downloadhdfs',
                            data: json
                        });
                    },
                    downloadKafka: function (json) {
                        return $http({
                            method: 'PUT',
                            url: '/api/download/downloadKafka',
                            data: json
                        });
                    }
                };
                return service;
            }]);
