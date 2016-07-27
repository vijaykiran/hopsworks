'use strict';

angular.module('hopsWorksApp')
        .controller('JsonCtrl', ['$modalInstance', '$scope', 'title', 'json',
          function ($modalInstance, $scope, title, json) {

            var self = this;
            self.title = title;
            self.json = json;

            self.ok = function () {
              $modalInstance.close();
            };
          }]);

