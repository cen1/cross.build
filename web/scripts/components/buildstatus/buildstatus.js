import subModule from '../index'

import template from './buildstatus.html'

export default subModule.directive('buildstatus', () => {

    return {
        restrict: 'E',
        replace: true,
        scope: {
            build: '='
        },
        template,
        controller: /*@ngInject*/ ($scope) => {
            
        }
    }
});