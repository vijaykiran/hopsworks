angular.module('hopsWorksApp')
    .controller('SetupDownloadCtrl', ['$modalInstance', 'DataSetService','KafkaService','GVoDService','growl', 'defaultDatasetName','projectId','datasetId','partners', 
        function ($modalInstance, DataSetService, KafkaService, GVoDService, growl, defaultDatasetName, projectId, datasetId, partners) {

            var self = this;
            self.projectId = projectId;
            self.datasetId = datasetId;
            self.partners = partners;
            
            self.datasetName = defaultDatasetName;
            
            var dataSetService = DataSetService(self.projectId);
            
            
            self.manifestAvailable = false;
            self.manifestKafka = false;
            
            
            self.isNameOk = function(){
              
                dataSetService.getAllDatasets().then(function(success){
                 
                    var data = success.data;
                    for(var i = 0; i<data.length; i++){
                        if(data[i].name === self.datasetName){
                            self.datasetNameOk = false;
                            return;
                        }
                    }
                    
                    self.datasetNameOk = true;
                 
                },function(error){
                    
                });
                
            };
            
            self.datasetNameOk = self.isNameOk();
            
        }]);