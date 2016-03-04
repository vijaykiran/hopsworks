'use strict';

angular.module('hopsWorksApp')
        .controller('HomeCtrl', ['ProjectService', 'ModalService', 'growl', 'ActivityService', 'UtilsService', '$q',
            function (ProjectService, ModalService, growl, ActivityService, UtilsService, $q) {

                var self = this;

                self.histories = [];
                self.projects = [];
                self.currentPage = 1;
                // Load all projects

                var loadProjects = function (success) {
                    self.projects = success;
                    self.pageSizeProjects = 10;
                    self.totalPagesProjects = Math.ceil(self.projects.length / self.pageSizeProjects);
                    self.totalItemsProjects = self.projects.length;
                    self.currentPageProjects = 1;
                };

                var loadActivity = function (success) {
                    var i = 0;
                    var histories = success.data;
                    var today = new Date();
                    var day = today.getDate();
                    var yesterday = new Date(new Date().setDate(day - 1));
                    var lastWeek = new Date(new Date().setDate(day - 7));
                    var older = new Date(new Date().setDate(day - 14));

                    var firstToday = true;
                    var firstYesterday = true;
                    var firstThisWeek = true;
                    var firstLastWeek = true;
                    var firstOlder = true;

                    today.setHours(0, 0, 0, 0);
                    yesterday.setHours(0, 0, 0, 0);
                    lastWeek.setHours(0, 0, 0, 0);
                    older.setHours(0, 0, 0, 0);

                    if (histories.length === 0) {
                        self.histories = [];
                    } else {
                        histories.slice().forEach(function (history) {
                            var historyDate = new Date(history.timestamp);
                            historyDate.setHours(0, 0, 0, 0);

                            if (+historyDate === +today) {
                                if (firstToday) {
                                    firstToday = false;
                                    history.flag = 'today';
                                } else if (i % 10 === 0) {
                                    history.flag = 'today';
                                } else {
                                    history.flag = '';
                                }
                            } else if (+historyDate === +yesterday) {
                                if (firstYesterday) {
                                    firstYesterday = false;
                                    history.flag = 'yesterday';
                                } else if (i % 10 === 0) {
                                    history.flag = 'yesterday';
                                } else {
                                    history.flag = '';
                                }
                            } else if (+historyDate > +lastWeek) {
                                if (firstThisWeek) {
                                    firstThisWeek = false;
                                    history.flag = 'thisweek';
                                } else if (i % 10 === 0) {
                                    history.flag = 'thisweek';
                                } else {
                                    history.flag = '';
                                }
                            } else if (+historyDate <= +lastWeek && +historyDate >= +older) {
                                if (firstLastWeek) {
                                    firstLastWeek = false;
                                    history.flag = 'lastweek';
                                } else if (i % 10 === 0) {
                                    history.flag = 'lastweek';
                                } else {
                                    history.flag = '';
                                }
                            } else {
                                if (firstOlder) {
                                    firstOlder = false;
                                    history.flag = 'older';
                                } else if (i % 10 === 0) {
                                    history.flag = 'older';
                                } else {
                                    history.flag = '';
                                }
                            }

                            self.histories.push(history);
                            i++;

                        });
                    }


                    self.pageSize = 10;
                    self.totalPages = Math.floor(self.histories.length / self.pageSize);
                    self.totalItems = self.histories.length;

                };

                var updateUIAfterChange = function () {

                    ActivityService.getByUser().then(function (result1) {
                        ProjectService.query().$promise.then(function (result2) {
                            loadActivity(result1);
                            loadProjects(result2);
                        });
                    });

                    $q.all({
                        'first': ActivityService.getByUser(),
                        'second': ProjectService.query().$promise
                    }
                    ).then(function (result) {
                        loadActivity(result.first);
                        loadProjects(result.second);
                    },
                    function(error){
                        growl.info(error, {title: 'Info', ttl: 5000});
                    });


                };


                updateUIAfterChange();

                // Create a new project
                self.newProject = function () {
                    ModalService.createProject('lg').then(
                            function (success) {
                                updateUIAfterChange();
                            }, function (error) {
                        growl.info("Closed project without saving.", {title: 'Info', ttl: 5000});
                    });
                };
                self.createExampleProject = function () {
                    ProjectService.example().$promise.then(
                            function (success) {
                                growl.success(success.successMessage, {title: 'Success', ttl: 2000});
                                updateUIAfterChange();
                                if (success.errorMsg) {
                                    growl.warning(success.errorMsg, {title: 'Error', ttl: 10000});
                                }

                            },
                            function (error) {
                                growl.info("problem", {title: 'Info', ttl: 5000});
                            }

                    );
                };

                self.deleteOnlyProject = function (projectId) {
                    ProjectService.remove({id: projectId}).$promise.then(
                            function (success) {
                                growl.success(success.successMessage, {title: 'Success', ttl: 15000});
                                updateUIAfterChange();
                            },
                            function (error) {
                                growl.error(error.data.errorMsg, {title: 'Error', ttl: 15000});
                            }
                    );
                };
                self.deleteProjectAndDatasets = function (projectId) {

                    ProjectService.delete({id: projectId}).$promise.then(
                            function (success) {
                                growl.success(success.successMessage, {title: 'Success', ttl: 15000});
                                updateUIAfterChange();
                            },
                            function (error) {
                                growl.error(error.data.errorMsg, {title: 'Error', ttl: 15000});
                            }
                    );
                };
            }]);


