angular.module('hopsWorksApp').controller('SelectDownloadTypeCtrl', ['$modalInstance',
    function ($modalInstance) {


        var self = this;

        self.kafkaDownload = false;
        self.hdfsDownload = false;

        self.selectDownloadType = function () {

            if (!self.kafkaDownload && self.hdfsDownload) {
                
                $modalInstance.close(0);
                
            }else if(self.kafkaDownload && !self.hdfsDownload){
                
                $modalInstance.close(1);
                
            }else if(self.kafkaDownload && self.hdfsDownload){
                
                $modalInstance.close(2);
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