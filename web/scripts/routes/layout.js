import {auth} from 'config'

export default /*@ngInject*/ ($rootScope, $scope, $state) => {

    $scope.login = () => auth.keycloak.login();
    $scope.logout = () => auth.keycloak.logout({redirectUri: $state.href('layout.home', {}, {absolute: true})});

};