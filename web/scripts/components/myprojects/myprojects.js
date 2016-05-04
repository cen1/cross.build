import subModule from '../index'

import template from './myprojects.html'

export default subModule.directive('myprojects', () => {

    return {
        restrict: 'E',
        replace: true,
        scope: {
            myprojects: '=',
            updateProject: '=',
            selectedProjectGroup: '='      
        },
        template,
        controller: /*@ngInject*/ ($scope, ProjectGroups, $location) => {
            
            $scope.projectStatus = [];
            
            $scope.select = (pg) => {
                $scope.selectedProjectGroup = pg;
                $scope.projectStatus = [];
                $location.search("pgid", pg.id);
                
                for (var i = 0; i < $scope.selectedProjectGroup.projects.length; i++) {
                    
                    ProjectGroups.getStatus(pg.id, $scope.selectedProjectGroup.projects[i].id).success((data) => {
                        for (var j = 0; j < $scope.selectedProjectGroup.projects.length; j++) {
                            if (data.name==$scope.selectedProjectGroup.projects[j].id) {
                                data.project = $scope.selectedProjectGroup.projects[j];
                                $scope.projectStatus.push(data);
                            }   
                        }
                    });
                }
            }
                                    
            $scope.$watch(() => { return $scope.myprojects.length!=0; }, () => {
                var params = $location.search();
                if (params.pgid != null) {
                    for (var i = 0; i < $scope.myprojects.length; i++) {
                        if ($scope.myprojects[i].id==params.pgid) {
                            $scope.select($scope.myprojects[i]);
                        }
                    }
                }
            });
        }
    }
});
