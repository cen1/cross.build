import subModule from '../index'

import template from './myprojects.html'

export default subModule.directive('myprojects', () => {

    return {
        restrict: 'E',
        replace: true,
        scope: {
            myprojects: '='        
        },
        template,
        controller: /*@ngInject*/ ($scope, ProjectGroups, $location) => {
            
            $scope.projectgroup = null;
            $scope.projectStatus = [];
            
            $scope.select = (pg) => {
                $scope.projectStatus = [];
                $scope.projectgroup = pg;
                $location.search("pgid", pg.id);
                
                for (var i = 0; i < $scope.projectgroup.projects.length; i++) {
                    
                    ProjectGroups.getStatus(pg.id, $scope.projectgroup.projects[i].id).success((data) => {
                        for (var j = 0; j < $scope.projectgroup.projects.length; j++) {
                            if (data.name==$scope.projectgroup.projects[j].id) {
                                data.project = $scope.projectgroup.projects[j];
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
