angular.module('partialsApplication').factory('gvodService', ['gvodStateService','$http', function (gvodStateService, $http) {

        var service = {

            download: function (json) {
                return $http({
                            method: 'PUT',
                            url: gvodStateService.getURL() + ":" + gvodStateService.getPort() + '/torrent/download',
                            data: json
                        });
            },
            upload: function (json) {
                return $http({
                            method: 'PUT',
                            url: gvodStateService.getURL() + ":" + gvodStateService.getPort() + '/torrent/upload',
                            data: json
                        });
            },
            stop: function (json) {
                return $http({
                            method: 'PUT',
                            url: gvodStateService.getURL() + ":" + gvodStateService.getPort() + '/torrent/stop',
                            data: json
                        });
            },
            getLibraryContents: function () {
                return $http({
                            method: 'GET',
                            url: gvodStateService.getURL() + ":" + gvodStateService.getPort() + '/library/contents'
                        });
            },
            addFile: function(json){
                return $http({
                            method: 'PUT',
                            url: gvodStateService.getURL() + ":" + gvodStateService.getPort() + '/library/add',
                            data: json
                        });
            },
            getLibraryElement: function(json){
                return $http({
                            method: 'PUT',
                            url: gvodStateService.getURL() + ":" + gvodStateService.getPort() + '/library/element',
                            data: json
                        });
            },
            checkStatus:  function () {
                return $http({
                        method: 'GET',
                        url: gvodStateService.getURL() + ":" + gvodStateService.getPort() + '/status'
                    });

            }
            

        };

        return service;
    }]);