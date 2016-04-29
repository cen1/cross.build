import subModule from '../index'

import template from './addnew.html'

export default subModule.directive('addnew', () => {

    return {
        restrict: 'E',
        replace: true,
        scope: {
            projectgroup: '=',
            addnew: '=',
            vmset: '='
        },
        template,
        controller: /*@ngInject*/ ($scope) => {
            
            $scope.projectgroup.projects = [];
            $scope.selectedPlatform = { };
            
            $scope.addPlatform = () => {
                var platformCopy = angular.copy($scope.selectedPlatform);
                platformCopy = JSON.parse(platformCopy);

                var ppcard = {};
                ppcard.id = platformCopy.id;
                ppcard.vmSetting = platformCopy;
                ppcard.vmSetting.provider = platformCopy.provider;
                ppcard.vmSetting.platform = platformCopy.platform;
                
                for (var i = 0; i < $scope.vmset.length; i++) {
                    if ($scope.vmset[i].id==platformCopy.id) {
                        $scope.vmset.splice(i, 1);
                    }
                }
                $scope.selectedPlatform = { };
                
                $scope.projectgroup.projects.push(ppcard);
            }
            
            $scope.removePlatform = (platform) => {
                for (var i = 0; i < $scope.projectgroup.projects.length; i++) {
                    if ($scope.projectgroup.projects[i].id==platform.id) {
                        var ppcard = $scope.projectgroup.projects[i];
                        $scope.vmset.push(ppcard.vmSetting);
                        $scope.projectgroup.projects.splice(i, 1);
                        
                        $scope.selectedPlatform = { };
                    }
                }
            }
        }
    }
});
