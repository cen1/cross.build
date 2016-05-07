import subModule from '../index'

import template from './buildhistory.html'

export default subModule.directive('buildhistory', () => {

    return {
        restrict: 'E',
        replace: true,
        scope: {
            selectedProjectGroup: '=',
            status: '=',
            pauseRefresh: '='
        },
        template,
        controller: /*@ngInject*/ ($scope, ProjectGroups, $interval) => {
            $scope.buildJobs = new Map();
            $scope.selectedBuildJobs = new Map();
            $scope.latestBuildNumber = 0;
            $scope.latestBuild=null;
            
            $scope.buildConsole = new Map();
            $scope.viewLog = new Map();
            
            $scope.openBuild = (buildNumber, select) => {
                
                if (select) {
                    if (typeof $scope.selectedBuildJobs.get(buildNumber)==='undefined') {
                        $scope.selectedBuildJobs.set(buildNumber, false);
                    }
                    var current = $scope.selectedBuildJobs.get(buildNumber);
                    $scope.selectedBuildJobs.set(buildNumber, !current);
                }
                
                ProjectGroups.getBuildDetails($scope.selectedProjectGroup.id, $scope.status.name, buildNumber).success((data) => {
                    $scope.buildJobs.set(buildNumber, data);
                    if (buildNumber>$scope.latestBuildNumber) {
                        $scope.latestBuildNumber = buildNumber;
                        $scope.latestBuild=data;
                    }
                });
            }
            
            $scope.viewConsole = (build) => {
                if (typeof $scope.viewLog.get(build.number)==='undefined') {
                        $scope.viewLog.set(build.number, false);
                }
                var current = $scope.viewLog.get(build.number);
                $scope.viewLog.set(build.number, !current);
                console.log(!current);
                console.log($scope.selectedBuildJobs.get(build.number));
                
                ProjectGroups.getBuildConsole($scope.selectedProjectGroup.id, $scope.status.name, build.number).success((data) => {
                    $scope.buildConsole.set(parseInt(build.number), data);
                });
            }
            
            $scope.initLast3 = () => {
                for (var k = 0; k < $scope.status.builds.length && k<3; k++) {
                    $scope.openBuild($scope.status.builds[k].number, false);
                }
            }
            
            $scope.initLast3();
            
            //live update
            var numBuilds = $scope.status.builds.length;
            
            var offset = 0;
            var moreData = false;
            $scope.refreshInterval = $interval(() => {
                if (!$scope.pauseRefresh) {
                    ProjectGroups.getStatus($scope.selectedProjectGroup.id, $scope.status.name).success((data) => {
                        
                        if (data.builds.length > numBuilds || ($scope.latestBuild!=null && $scope.latestBuild.building)) {
                            var buildNum = 0;
                            if (data.builds.length > numBuilds) {
                                numBuilds=data.builds.length;
                                $scope.status.builds.splice(0, 0, data.builds[0]);
                                $scope.openBuild(data.builds[0].number, true);
                                
                                buildNum = parseInt(data.builds[0].number);
                            } else {
                                buildNum=parseInt($scope.latestBuild.id);
                            }
                            
                            $scope.viewLog.set(buildNum, true);
                            if (!$scope.buildConsole.has(buildNum)) {
                                $scope.buildConsole.set(buildNum, "");
                            }
                            
                            ProjectGroups.getProgressiveConsole($scope.selectedProjectGroup.id, $scope.status.name, buildNum, offset)
                                .success((data, status, headers) => {
                                
                                var textSize = parseInt(headers("X-Text-Size"));
                                moreData = headers("X-More-Data");
                                var existingText = $scope.buildConsole.get(buildNum);
                                $scope.buildConsole.set(buildNum, existingText+data);
                                
                                if (textSize>offset) {
                                    offset=textSize;
                                }
                                
                                if (!moreData || moreData==null || $scope.pauseRefresh) {
                                    offset=0;
                                    $scope.latestBuild.building=false;
                                    $scope.openBuild(buildNum, false);
                                }
                                
                                //console.log($scope.buildJobs);
                                //console.log(buildNum);
                                var buildJob = $scope.buildJobs.get(buildNum);
                                if (typeof buildJob!=='undefined') {
                                    var txtId=buildJob.fullDisplayName.replace(' ', '');
                                    var txt = document.getElementById(txtId);
                                    var wraptxt = angular.element(txt);
                                    wraptxt.scrollTop(wraptxt[0].scrollHeight);
                                }
                                
                            }).error((data, status) => {
                                offset=0;
                                $scope.latestBuild.building=false;
                            });
                            
                        }
                    });
                }
            }, 5000);
            
            $scope.$on("$destroy",function(){
                if (angular.isDefined($scope.refreshInterval)) {
                    $interval.cancel($scope.refreshInterval);
                }
            });       
        }
    }
});