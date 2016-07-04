angular.module('hopsWorksApp').controller('P2PCtrl', ['GVodService', function (GVodService) {
        var self = this;
        self.identifier = GVodService.getIdentifier();
        self.filename = GVodService.getFilename();

        self.uri;
        self.size;
        self.description;
        self.result;
        self.viewToLoad;

        self.showView = new Array(0);
        self.status = false;
        self.addFile = function () {

            var fileInfo = {
                name: self.filename,
                uri: self.uri,
                size: self.size,
                description: self.description

            };

            var JSONObj = {"identifier": self.identifier, "fileInfo": fileInfo};

            GVodService.addFile(JSONObj).then(function (result) {
                self.result = result;
            });

        };

        self.getLibraryContents = function () {

            GVodService.getLibraryContents().then(function (result) {
                self.result = result;
            });

        };

        self.getLibraryElement = function () {
            var JSONObj = {"identifier": self.identifier, "name": self.filename};
            GVodService.getLibraryElement(JSONObj).then(function (result) {
                self.result = result;
            });
            self.status = true;
        };
        
        self.closeLibraryElement = function(){
            self.status = false;
        };

        self.enlarge = function (status, name, identifier, index) {

            switch (status) {
                case "NONE":
                    self.viewToLoad = "upload";
                    break;
                case "UPLOADING":
                    self.viewToLoad = "stop";
                    break;
                case "DOWNLOADING" :
                    self.viewToLoad = "stop";
                    break;
            }

            GVodService.setFilename(name);
            GVodService.setIdentifier(identifier);
            
            if(self.showView.length === 0){
                self.showView = new Array(self.result.data.contents.length);
                self.showView[index] = true;
            }else{
                self.showView[index] = true;
            }
            
            

        };
        
        self.minimize = function(index){
          
          self.showView[index]= false;  
            
        };

    }]);