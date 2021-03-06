angular.module('hopsWorksApp')
        .controller('FilePreviewCtrl', ['$modalInstance', '$showdown','DataSetService', 'growl', 'fileName', 'filePath', 'projectId',
          function ($modalInstance, $showdown, DataSetService, growl, fileName, filePath, projectId) {
            var self = this;

            self.filePath = filePath;
            self.fileName = fileName;
            self.projectId = projectId;
            self.content;
            self.type;
            self.extension;
            self.fileDetails;
            self.init = function () {
              var dataSetService = DataSetService(self.projectId); //The datasetservice for the current project.
              dataSetService.filePreview(filePath).then(
                      function (success) {
//                                .replace(/\\/g, '\\\\')
//                                .replace(/\"/g, '\\"')
//                                .replace(/\//g, '\\/')
//                                .replace(/\b/g, '\\b')
//                                .replace(/\f/g, '\\f')
//                                .replace(/\n/g, '\\n')
//                                .replace(/\r/g, '\\r')
//                                .replace(/\t/g, '\\t')
                        self.fileDetails = JSON.parse(success.data.data);
                        console.log(self.fileDetails.filePreviewDTO[0].content);

                        self.type = self.fileDetails.filePreviewDTO[0].type;
                        self.content = self.fileDetails.filePreviewDTO[0].content;
                        self.extension = self.fileDetails.filePreviewDTO[0].extension;
                      }, function (error) {
                growl.error(error.data.errorMsg, {title: 'Could not get file contents', ttl: 5000, referenceId: 23});
              });
            };
            self.init();

            self.close = function () {
              $modalInstance.dismiss('cancel');
            };
          }]);

