/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
'use strict';

angular.module('hopsWorksApp')
        .factory('TourService', [
    
                function () {
                    
                    var tourService = this;
                    
                tourService.currentStep = -1;
                
                tourService.setCurrentStep = function(val){
                    this.currentStep = val;
                };
                tourService.getCurrentStep = function(){
                    return this.currentStep;
                };
                tourService.startTour = function () {
                    this.currentStep = 0;
                };
                tourService.stopTour = function () {
                    this.currentStep = -1;
                };
                tourService.IncrementCurrentStep = function () {
                    if (this.currentStep === 3) {
                        this.currentStep = -1;
                    }
                    this.currentStep = this.currentStep + 1;
                };
                
                tourService.enterExampleProject = function(){
                };
                return tourService;
    }
]);
