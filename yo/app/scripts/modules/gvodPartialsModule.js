/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module('partialsApplication', []);

angular.module('partialsApplication').controller('Downloader', ['BackendService', function (BackendService) {

        var self = this;
        self.filename;
        self.identifier;
        self.result;
        self.downloading = false;


        self.download = function () {
            var JSONObj = {"name ": self.filename, "identifier": self.identifier};
            BackendService.download(JSONObj).then(function (result) {
                self.result = result;
                self.downloading = true;
            });

        };

    }]);
angular.module('partialsApplication').controller('Uploader', ['BackendService', function (BackendService) {

        var self = this;
        self.filename;
        self.identifier;
        self.result;
        self.uploading = false;


        self.upload = function () {
            var JSONObj = {"name ": self.filename, "identifier": self.identifier};
            BackendService.upload(JSONObj).then(function (result) {
                self.result = result;
                self.uploading = true;
            });

        };

    }]);
angular.module('partialsApplication').controller('Stoper', ['BackendService', function (BackendService) {

        var self = this;
        self.filename;
        self.identifier;
        self.result;
        self.stoped = false;


        self.stop = function () {
            var JSONObj = {"name ": self.filename, "identifier": self.identifier};
            BackendService.stop(JSONObj).then(function (result) {
                self.result = result;
                self.stoped = true;
            });

        };

    }]);

angular.module('partialsApplication').controller('Library',['BackendService',function(BackendService){

        var self = this;
        self.identifier;
        self.filename;
        self.uri;
        self.size;
        self.description;
        self.result;
        
        self.addFile = function(){
            
            var fileinfo = {
                
                name: self.filename,
                uri: self.uri,
                size: self.size,
                description : self.description
                
            };
            
            var JSONObj = {"identifier ": self.identifier, "fileinfo": fileinfo}; 
            
            BackendService.addFile(JSONObj).then(function(result){
                self.result = result;
            });
            
        };
        
        self.getLibraryContents = function(){
            
            BackendService.getLibraryContents().then(function(result){
                self.result = result;
            });
              
        };
        
        self.getLibraryElement = function(){
            var JSONObj = {"identifier ": self.identifier, "name": self.filename}; 
            BackendService.getLibraryElement(JSONObj).then(function(result){
                self.result = result;
            });
        };
        
    }]);

angular.module('partialsApplication').factory('BackendService', ['$http', function ($http) {
        var service = {
            download: function (json) {
                return $http(
                        {
                            method: 'PUT',
                            url: 'http://bbc1.sics.se:18183/torrent/download',
                            data: json
                        });
            },
            upload: function (json) {
                return $http(
                        {
                            method: 'PUT',
                            url: 'http://bbc1.sics.se:18183/torrent/upload',
                            data: json
                        });
            },
            stop: function (json) {
                return $http(
                        {
                            method: 'PUT',
                            url: 'http://bbc1.sics.se:18183/torrent/stop',
                            data: json
                        });
            },
            getLibraryContents: function(){
                return $http(
                        {
                            method: 'GET',
                            url: 'http://bbc1.sics.se:18183/library/contents'
                        });
            }

        };

        return service;
    }]);

