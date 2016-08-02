angular.module('hopsWorksApp').controller('P2PCtrl', ['GVoDService','$routeParams','$scope','$interval', function (GVoDService, $routeParams, $scope, $interval) {
        var self = this;
      
        self.projectId = $routeParams.projectID;
        
        self.contents;
        
        var getContents = function(){
          
            var json = {};
            json.projectId = self.projectId;
            
            GVoDService.getContents(json).then(function(success){
                self.contents = success.data.contents;
            });
            
        };
        
        getContents();
        
        
        var contentsInterval = $interval(function () {
                     getContents();
                }, 6000);
                
        $scope.$on("$destroy", function () {
                    $interval.cancel(contentsInterval);
                });        
    }]);