angular.module('hopsWorksApp')
        .controller('SetupDownloadCtrl', ['$modalInstance', 'DataSetService', 'KafkaService', 'GVoDService', 'growl', 'defaultDatasetName', 'projectId', 'datasetId', 'partners', 'ModalService',
            function ($modalInstance, DataSetService, KafkaService, GVoDService, growl, defaultDatasetName, projectId, datasetId, partners, ModalService) {

                var self = this;
                self.projectId = projectId;
                self.datasetId = datasetId;
                self.partners = partners;

                self.datasetName = defaultDatasetName;

                var dataSetService = DataSetService(self.projectId);


                self.manifestAvailable = false;
                self.manifestKafka = false;
                self.manifest;

                self.DownloadType;

                self.topicsRemainingForCreation = 0;
                self.topicsCreated = false;
                
                self.topicsMap = {};

                self.validTopicName = function (id) {
                    var topicName = document.getElementById(id).value;
                    var topics;

                    KafkaService.getTopics(self.projectId).then(function (success) {
                        topics = success.data;
                    });

                    for (var i = 0; i < topics.length; i++) {
                        if (topicName === topics[i].name) {
                            return false;
                        }
                    }
                    return true;
                };

                self.createTopic = function (file) {
                    
                    var fileName = file.name;
                    var topicName = document.getElementById(fileName).value;

                    var schemaDetail = {};
                    schemaDetail.name = topicName;
                    schemaDetail.contents = file.schema;
                    schemaDetail.version = 1;

                    KafkaService.createSchema(self.projectId, schemaDetail).then(
                            function (success) {

                            }, function (error) {
                        growl.error(error.data.errorMsg, {title: 'Failed to create schema', ttl: 5000});
                    });

                    var topicDetails = {};
                    topicDetails.name = topicName;
                    topicDetails.numOfPartitions = 2;
                    topicDetails.numOfReplicas = 1;
                    topicDetails.schemaName = file.schema;
                    topicDetails.schemaVersion = 1;

                    KafkaService.createTopic(self.projectId, topicDetails).then(
                            function (success) {
                                self.topicsRemainingForCreation--;
                                self.topicsMap.fileName = topicName;
                                if(self.topicsRemainingForCreation === 0){
                                    self.topicsCreated = true;
                                }
                            }, function (error) {
                        growl.error(error.data.errorMsg, {title: 'Failed to create topic', ttl: 5000});
                        
                    });

                };

                self.setTopicsRemainingForCreation = function (files) {
                    for (var i = 0; i < files.length; i++) {
                        if (files[i].schema !== '') {
                            self.topicsRemainingForCreation++;
                        }
                    }
                };

                self.myFilter = function (item) {

                    return item.schema !== '';

                };

                self.isNameOk = function () {

                    dataSetService.getAllDatasets().then(function (success) {

                        var data = success.data;
                        for (var i = 0; i < data.length; i++) {
                            if (data[i].name === self.datasetName) {
                                self.datasetNameOk = false;
                                return;
                            }
                        }

                        self.datasetNameOk = true;

                    }, function (error) {
                        
                    });

                };

                self.datasetNameOk = self.isNameOk();


                self.DownloadRequest = function () {

                    var json = {"projectId": self.projectId, "datasetId": self.datasetId, "datasetName": self.datasetName};
                    GVoDService.DownloadRequest(json).then(function (success) {
                        self.manifest = success;
                        self.manifestAvailable = true;
                        if (self.manifest.supportKafka) {
                            self.manifestKafka = true;
                            self.setTopicsRemainingForCreation(self.manifest.fileInfos);
                        } else {
                            self.manifestKafka = false;
                        }
                    },
                            function (error) {
                                
                                growl.error(error.data.errorMsg, {title: 'Failed to get manifest', ttl: 5000});

                            });



                };

                self.downloadTypeHdfs = function () {
                    self.DownloadType = 0;
                };

                self.downloadTypeKafkaHdfs = function () {
                    self.DownloadType = 1;
                };

                self.download = function () {

                    if (self.DownloadType === 0) {
                        
                        var json = {};
                        json.datasetName = self.manifest.datasetName;
                        json.projectId = self.projectId;
                        json.datasetId = self.datasetId;
                        json.partners = self.partners;
                        
                        GVoDService.downloadHdfs(json).then(function(success){
                           
                            $modalInstance.close(success);
                            
                        },function(error){
                             growl.error(error.data.errorMsg, {title: 'Failed to start download', ttl: 5000});
                        });

                    } else {
                        
                        var json = {};
                        json.datasetName = self.manifest.datasetName;
                        json.projectId = self.projectId;
                        json.datasetId = self.datasetId;
                        json.partners = self.partners;
                        json.topics = self.topicsMap;
                        
                        GVoDService.downloadKafka(json).then(function(success){
                           
                            $modalInstance.close(success);
                            
                        },function(error){
                             growl.error(error.data.errorMsg, {title: 'Failed to start download', ttl: 5000});
                        });
                    }

                };

                self.showSchema = function (schema) {

                    ModalService.json('md', 'Schema', schema).then(function (success) {

                    });

                };

            }]);