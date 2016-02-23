/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

'use strict';

angular.module('hopsWorksApp')
        .controller('ExampleProjectCtrl', ['ProjectService', 'growl',
            function (ProjectService, growl) {

                var self = this;

                self.createExampleProject = function () {
                    ProjectService.example().$promise.then(
                            function (success) {
                                growl.success(success.successMessage, {title: 'Success', ttl: 2000});
                                
                                if (success.errorMsg) {
                                    growl.warning(success.errorMsg, {title: 'Error', ttl: 10000});
                                }
                                
                            },
                            function (error) {

                            }

                    );
                };
            }]);            