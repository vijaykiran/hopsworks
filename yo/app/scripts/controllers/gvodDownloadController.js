angular.module('hopsWorksApp').controller('GVodDownloadController', ['$modalInstance', 'datasetName', 'datasetId', 'datasetStructure', 'projectId', 'partners', 'GVodService', 'growl',
    function ($modalInstance, datasetName, datasetId, datasetStructure, projectId, partners, GVodService, growl) {


        var self = this;
        self.datasetName = datasetName;
        self.datasetId = datasetId;
        self.projectId = projectId;
        self.datasetStructure = datasetStructure;

        self.kafkaDownload = false;
        self.hdfsDownload = false;

        self.partners = partners;


        self.topic;
        self.schema;
        self.kafkaCert;
        self.sessionId;

        self.download = function () {

            if (self.kafkaDownload && !self.hdfsDownload) {

                var json = {"topic": self.topic, "schema": self.schema, "cert": self.kafkaCert, "sessionId": self.sessionId, "projectId": self.projectId, "datasetName": self.datasetName, "datasetId": self.datasetId, "datasetStructure": self.datasetStructure, "partners": self.partners};

                GVodService.downloadKafka(json).then(function (success) {
                    growl.success(success, {title: 'Success Kafka Download', ttl: 1000});
                },
                        function (error) {
                            growl.error(error, {title: 'Error', ttl: 1000});
                        });

            } else if (!self.kafkaDownload && self.hdfsDownload) {

                var json = {"projectId": self.projectId, "datasetName": self.datasetName, "datasetId": self.datasetId, "datasetStructure": self.datasetStructure, "partners": self.partners};

                GVodService.downloadHdfs(json).then(function (success) {

                    growl.success(success, {title: 'Success Hdfs Download', ttl: 1000});

                },
                        function (error) {

                            growl.error(error, {title: 'Error', ttl: 1000});

                        });

            } else if (self.kafkaDownload && self.hdfsDownload) {

                var hdfsJson = {"projectId": self.projectId, "datasetName": self.datasetName, "datasetId": self.datasetId, "datasetStructure": self.datasetStructure, "partners": self.partners};

                GVodService.downloadHdfs(hdfsJson).then(function (success) {

                    var kafkaJson = {"topic": self.topic, "schema": self.schema, "cert": self.kafkaCert, "sessionId": self.sessionId, "projectId": self.projectId, "datasetName": self.datasetName, "datasetId": self.datasetId, "datasetStructure": self.datasetStructure, "partners": self.partners};

                    GVodService.downloadKafka(kafkaJson).then(function (success) {

                    },
                            function (error) {

                            });

                },
                        function (error) {

                        });

            }
        };

        self.setHdfs = function () {
            if (self.hdfsDownload) {
                self.hdfsDownload = false;
            } else {
                self.hdfsDownload = true;
            }
        };

        self.setKafka = function () {
            if (self.kafkaDownload) {
                self.kafkaDownload = false;
            } else {
                self.kafkaDownload = true;
            }
        };


        self.close = function () {
            $modalInstance.dismiss('cancel');
        };

    }]);