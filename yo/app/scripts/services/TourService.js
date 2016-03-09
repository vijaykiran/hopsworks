/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
'use strict';

angular.module('hopsWorksApp')
        .factory('TourService', ['$timeout','$location',
    
                function ($timeout,$location) {
                    
                    var tourService = this;
                    
                tourService.currentStep = -1;
                tourService.alive = 15;
                tourService.exampleProject = null;
                
                tourService.StopTour = function () {
                    this.currentStep = -1;
                    this.alive = 15;
                };
                
                tourService.isExampleProject = function(id){
                  
                    if(this.exampleProject === null)
                    {
                        return false;
                    }
                    else if(this.exampleProject.id === id){
                        return true;
                    }
                    else{
                        return false;
                    }
                };
                tourService.EnterExampleProject = function(){
                    $location.path('/project/' + this.exampleProject.id+"/"+"true");
                };
                
                tourService.KillTourSoon = function(){
                    $timeout(function(){
                        tourService.alive--;
                    },1000).then(function(){
                        if(tourService.alive === 0){
                            tourService.StopTour();
                        }
                        else
                        {
                            tourService.KillTourSoon();
                        }
                    });
                };
                return tourService;
    }
]);
