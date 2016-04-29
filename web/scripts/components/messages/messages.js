import subModule from '../index'

import template from './messages.html'

export default subModule.directive('validatorMessages', () => {

    return {
        restrict: 'E',
        replace: true,
        template,
        scope: {
            form: '=',
            options: '='
        }
    }
});
