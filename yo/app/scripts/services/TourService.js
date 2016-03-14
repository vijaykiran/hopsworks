/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
'use strict';

angular.module('hopsWorksApp')
        .factory('TourService', ['$timeout', '$location',
            function ($timeout, $location) {

                var tourService = this;

                tourService.currentStep_TourOne = -1;
                tourService.currentStep_TourTwo = -1;
                tourService.currentStep_TourThree = -1;
                tourService.currentStep_TourFour = -1;
                tourService.alive_TourOne = 15;
                tourService.timeout_TourOne = null;


                tourService.StopTourOne = function () {
                    this.currentStep_TourOne = -1;
                    this.alive_TourOne = 15;
                };

                tourService.EnterExampleProject = function (id) {
                    $location.path('/project/' + id);
                    this.StopTourOne();
                };

                tourService.KillTourOneSoon = function ()
                {
                    this.timeout_TourOne = $timeout(function () {
                        tourService.alive_TourOne--;
                    }, 1000).then(function () {
                        if (tourService.alive_TourOne === 0)
                        {
                            tourService.StopTourOne();
                        }
                        else
                        {
                            tourService.KillTourOneSoon();
                        }
                    });
                };
                return tourService;
            }
        ]);
