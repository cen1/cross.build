import subModule from '../index'

import template from './ppcard.html'

export default subModule.directive('ppcard', () => {

    return {
        restrict: 'E',
        replace: true,
        scope: {
            project: '='        },
        template,
        controller: /*@ngInject*/ ($scope) => {
            
            var platform = $scope.project.vmSetting.platform;
            
            $scope.name = platform.name+" "+platform.version+" ("+platform.arch+") on "+$scope.project.vmSetting.provider;
            
        }
    }
});
