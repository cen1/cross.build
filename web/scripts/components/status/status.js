import subModule from '../index'

import template from './status.html'

export default subModule.directive('status', () => {

    return {
        restrict: 'E',
        replace: true,
        scope: {
            vms: "="
        },
        template,
        controller: /*@ngInject*/ ($scope, $timeout) => {
            
        }
    }
});
