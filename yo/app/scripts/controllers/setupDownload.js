angular.module('hopsWorksApp')
    .controller('SetupDownloadCtrl', ['$modalInstance', 'DataSetService','KafkaService','GVoDService','growl', 'defaultDatasetName','projectId','datasetId','partners', 
        function ($modalInstance, DataSetService, KafkaService, GVoDService, growl, defaultDatasetName, projectId, datasetId, partners) {

            var self = this;
            self.defaultDatasetName = defaultDatasetName;
            self.projectId = projectId;
            self.datasetId = datasetId;
            self.partners = partners;
            
            self.datasetName = self.defaultDatasetName;
            self.datasetNameOk = true;
            
            
            self.isNameOk = function(){
              
                DataSetService.checkFileExist(self.datasetName).then(function(success){
                   
                    if(success.status === 200){
                        self.datasetNameOk = false;
                    }else{
                        self.datasetNameOk = true;
                    }
                    
                },function(error){
                    
                });
                
            };
            
        }]);