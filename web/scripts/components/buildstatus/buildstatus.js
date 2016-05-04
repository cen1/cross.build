import subModule from '../index'

import template from './buildstatus.html'

export default subModule.directive('buildstatus', () => {

    return {
        restrict: 'E',
        replace: true,
        scope: {
            result: '='        
        },
        template,
        controller: /*@ngInject*/ ($scope) => {
        
        }
    }
});