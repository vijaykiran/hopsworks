angular.module('partialsApplication').controller('GVodDownloaderController', ['$modalInstance', function (datasetName, datasetId, projectId, partners) {


        var self = this;
        self.datasetName = datasetName;
        self.datasetId = datasetId;
        self.projectId = projectId;
        
        self.kafkaDownload = false;
        self.hdfsDownload = false;
        
        self.myHdfsIp;
        self.myHdfsPort;
        self.myHdfsDestination;
        self.user;
        
        self.partners = partners;



        self.download = function () {

            if (self.kafkaDownload && !self.hdfsDownload) {

            } else if (!self.kafkaDownload && self.hdfsDownload) {


            } else if (self.kafkaDownload && self.hdfsDownload) {

            }




        };

    }]);