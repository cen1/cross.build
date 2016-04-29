import subModule from '../index'
import {auth} from 'config'

import template from './header.html'

export default subModule.directive('layoutHeader', () => {

    return {
        restrict: 'E',
        replace: true,
        scope: {
            onLogin: '=',
            onLogout: '='
        },
        template,
        controller: /*@ngInject*/ ($scope) => {
            
            $scope.auth = auth.keycloak;
            $scope.username = "";
            
            if ($scope.auth.authenticated) {
                $scope.auth.loadUserInfo().success((data) => {
                    $scope.username = data.preferred_username;
                    $scope.$apply();
                });
            }
            
            $scope.login = () => $scope.onLogin();
            $scope.logout = () => $scope.onLogout();
        }
    }
});
