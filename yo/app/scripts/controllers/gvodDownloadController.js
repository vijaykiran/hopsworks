angular.module('hopsWorksApp').controller('GVodDownloadController', ['$modalInstance', 'datasetName', 'datasetId', 'projectId', 'partners','$scope',
    function ($modalInstance, datasetName, datasetId, projectId, partners,$scope) {


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

        $scope.setHdfs = function () {
            if(self.hdfsDownload){
                self.hdfsDownload = false;
            }else{
                self.hdfsDownload = true;
            }
        };

        $scope.setHdfs = function () {
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