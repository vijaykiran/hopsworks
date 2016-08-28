angular.module('hopsWorksApp').controller('P2PCtrl', ['GVoDService', '$routeParams', '$scope', '$interval', function (GVoDService, $routeParams, $scope, $interval) {
        var self = this;

        self.projectId = $routeParams.projectID;

        self.contents;

        self.remove = function (torrentId) {
            var json = {};
            json.torrentId = torrentId;
            GVoDService.remove(json).then(function (success) {
                getContents();
            });
        };

        var getContents = function () {

            var json = {};
            json.projectId = self.projectId;

            GVoDService.getContents(json).then(function (success) {
                self.contents = success.data.contents;
                var length = self.contents.length;
                for (var j = 0; j < length; j++) {
                    self.contents[j]['max'] = 100;
                    var ejson = {};
                    ejson.torrentId = self.contents[j].torrentId.val;
                    GVoDService.getExtendedDetails(ejson).then(function (success) {
                        self.contents[j]['dynamic'] = success.percentageCompleted * 100;
                    });
                }
            });

        };

        getContents();


        var contentsInterval = $interval(function () {
            getContents();
        }, 5000);

        $scope.$on("$destroy", function () {
            $interval.cancel(contentsInterval);
        });
    }]);