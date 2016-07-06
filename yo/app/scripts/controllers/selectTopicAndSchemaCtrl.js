angular.module('hopsWorksApp').controller('SelectTopicAndSchemaCtrl', ['$modalInstance','KafkaService','growl',
    function ($modalInstance,KafkaService,growl) {


        var self = this;
        
        self.topics;
        
        self.choosenTopic;

        self.getTopics = function(){
            
            KafkaService.getTopics.then(function(success){
                self.topics = success;
                if(self.topics.length === 0){
                    growl.error("You need to create a Topic before proceeding with a kafka download", {title: 'Error', ttl: 1000});
                    self.close();
                }
            },
            function(error){
                
            });
            
        };
        
        self.getTopics();
        
        self.chooseTopic = function(){
          
            var TopicAndSchema = {"topicName": self.choosenTopic};
            
        };
        
        self.close = function () {
            $modalInstance.dismiss('cancel');
        };

    }]);