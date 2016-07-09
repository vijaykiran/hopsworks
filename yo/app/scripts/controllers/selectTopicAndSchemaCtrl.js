angular.module('hopsWorksApp').controller('SelectTopicAndSchemaCtrl', ['$modalInstance','KafkaService','growl','projectId',
    function ($modalInstance,KafkaService,growl,projectId) {


        var self = this;
        
        self.topics = [];
        
        self.projectId = projectId;
        
        self.getTopics = function(){
            
            KafkaService.getTopics(self.projectId).then(function(success){
                self.topics = success.data;
                if(self.topics.length === 0){
                    growl.error("You need to create a Topic before proceeding with a kafka download", {title: 'Error', ttl: 1000});
                    self.close();
                }
            },
            function(error){
                
            });
            
        };
        
        self.getTopics();
        
        self.done = function(index){
            $modalInstance.close(self.topics[index].name);
        };
        
        self.close = function () {
            $modalInstance.dismiss('cancel');
        };

    }]);