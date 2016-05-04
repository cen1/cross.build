import subModule from '../index'

import template from './buildhistory.html'

export default subModule.directive('buildhistory', () => {

    return {
        restrict: 'E',
        replace: true,
        scope: {
            selectedProjectGroup: '=',
            status: '='
        },
        template,
        controller: /*@ngInject*/ ($scope, ProjectGroups) => {
            $scope.buildJobs = new Map();
            $scope.selectedBuildJobs = new Map();
            
            $scope.buildConsole = new Map();
            $scope.viewLog = new Map();
            
            $scope.openBuild = (build, select) => {
                
                if (select) {
                    if (typeof $scope.selectedBuildJobs.get(build.number)==='undefined') {
                        $scope.selectedBuildJobs.set(build.number, false);
                    }
                    var current = $scope.selectedBuildJobs.get(build.number);
                    $scope.selectedBuildJobs.set(build.number, !current);
                }
                
                ProjectGroups.getBuildDetails($scope.selectedProjectGroup.id, $scope.status.name, build.number).success((data) => {
                    $scope.buildJobs.set(build.number, data);
                });
            }
            
            $scope.viewConsole = (build) => {
                if (typeof $scope.viewLog.get(build.number)==='undefined') {
                        $scope.viewLog.set(build.number, false);
                }
                    var current = $scope.viewLog.get(build.number);
                    $scope.viewLog.set(build.number, !current);
                ProjectGroups.getBuildConsole($scope.selectedProjectGroup.id, $scope.status.name, build.number).success((data) => {
                    $scope.buildConsole.set(build.number, data);
                });
            }
            
            $scope.initLast3 = () => {
                for (var k = 0; k < $scope.status.builds.length && k<3; k++) {
                    $scope.openBuild($scope.status.builds[k], false);
                }
            }
            
            $scope.initLast3();          
        }
    }
});