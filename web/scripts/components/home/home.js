import subModule from '../index'
import {auth} from 'config'

import template from './home.html'

export default subModule.directive('home', () => {

    return {
        restrict: 'E',
        replace: true,
        template,
        listVms: "=",
        createVmUbutnu: "=",
        createVmfreebsd: "=",
        controller: /*@ngInject*/ ($scope) => {
            $scope.auth = auth.keycloak;
        }
    }
});
