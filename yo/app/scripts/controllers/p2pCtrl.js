angular.module('hopsWorksApp').controller('P2PCtrl', ['GVoDService','$routeParams','$scope','$interval', function (GVoDService, $routeParams, $scope, $interval) {
        var self = this;
      
        self.projectId = $routeParams.projectID;
        
        self.contents;
        
        self.remove = function(torrentId){
            var json = {};
            json.torrentId = torrentId;
            GVoDService.remove(json).then(function(success){
               getContents(); 
            });
        };
        
        var getContents = function(){
          
            var json = {};
            json.projectId = self.projectId;
            
            GVoDService.getContents(json).then(function(success){
                self.contents = success.data.contents;
                for(var i = 0; i<self.contents.length;i++){
                    var json = {};
                    json.torrentId = self.contents[i].torrentId;
                    GVoDService.getExtendedDetails(json).then(function(success){
                        self.contents[i].progress = success.data.percentageCompleted;
                    });
                }
            });
            
        };
        
        getContents();
        
        
        var contentsInterval = $interval(function () {
                     getContents();
                }, 1000);
                
        $scope.$on("$destroy", function () {
                    $interval.cancel(contentsInterval);
                });        
    }]);