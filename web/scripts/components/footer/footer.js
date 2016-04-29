import subModule from '../index'

import template from './footer.html'

export default subModule.directive('layoutFooter', () => {

    return {
        restrict: 'E',
        replace: true,
        template,
        controller: /*@ngInject*/ ($scope) => {
            $scope.year = new Date().getFullYear();
        }
    }
});
