import subModule from '../index'

import template from './home.html'

export default subModule.directive('home', () => {

    return {
        restrict: 'E',
        replace: true,
        template,
        controller: /*@ngInject*/ ($scope) => {
            
        }
    }
});
