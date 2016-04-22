/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module('partialsApplication',[]);

angular.module('partialsApplication').controller('Downloader', ['BackendService',function(BackendService){
        
        var self = this;
        self.filename;
        self.identifier;
        self.result;

        var JSONObj = { "bookname ":self.filename, "price":self.identifier };
        
        self.download = function(){
          
            BackendService.download(JSONObj).then(function(result){
                self.result = result;
            });
            
        };
        
}]);
angular.module('partialsApplication').controller('Uploader', ['BackendService',function(BackendService){
        
        var self = this;
        self.filename;
        self.identifier;
        self.result;

        var JSONObj = { "bookname ":self.filename, "price":self.identifier };
        
        self.upload = function(){
          
            BackendService.upload(JSONObj).then(function(result){
                self.result = result;
            });
            
        };
        
}]);
angular.module('partialsApplication').controller('Stoper', ['BackendService',function(BackendService){
        
        var self = this;
        self.filename;
        self.identifier;
        self.result;

        var JSONObj = { "bookname ":self.filename, "price":self.identifier };
        
        self.stop = function(){
          
            BackendService.stop(JSONObj).then(function(result){
                self.result = result;
            });
            
        };
        
}]);
angular.module('partialsApplication').factory('BackendService',['$http',function($http){
        var service = {
            
            download: function (json) {
                  return $http.put('localhost:18180/torrent/download',json);
                },
            upload: function (json) {
                  return $http.put('localhost:18180/torrent/upload',json);
                },
            stop: function (json) {
                  return $http.put('localhost:18180/torrent/stop',json);
                }    
            
        };
        
        return service;
}]);

