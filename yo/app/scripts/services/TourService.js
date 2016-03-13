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
                tourService.alive_TourOne = 15;
                tourService.timeout_TourOne = null;
                tourService.tourServiceProject = null;


                tourService.StopTourOne = function () {
                    this.currentStep_TourOne = -1;
                    this.alive_TourOne = 15;
                };
                tourService.StopTourTwo = function () {
                    this.currentStep_TourTwo = -1;
                };

                tourService.EnterExampleProject = function () {
                    $location.path('/project/' + this.tourServiceProject.id);
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
