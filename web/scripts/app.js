import angular from 'angular'
import Keycloak from 'keycloak-js'

import stateTree from './stateTree'
import config from 'config'

import ngRouter from 'angular-ui-router'
import ngBootstrap from 'angular-ui-bootstrap'
import ngSelect from 'angular-ui-select'
import ngSanitize from 'angular-sanitize'
import ngMessages from 'angular-messages'
import ngAnimate from 'angular-animate'
import ngMask from 'angular-ui-mask'
import ngTable from 'ng-table'

import filters from './filters'
import utils from './utils'
import interceptors from './interceptors'
import components from './components'
import actions from './actions'

import c3 from 'c3'
import Notification from 'angular-ui-notification'
//import luegg from 'angularjs-scroll-glue'

export let app = angular.module('cross-build', [
    ngRouter,
    ngBootstrap,
    ngSelect,
    ngSanitize,
    ngMessages,
    ngAnimate,
    ngMask,
    ngTable,
    Notification,
    //luegg.directives,
    
    utils.name,
    filters.name,
    interceptors.name,
    components.name,
    actions.name
]);

app.config(($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider) => {

    $locationProvider.html5Mode(true).hashPrefix('!');
    $httpProvider.interceptors.push('authInterceptor');
    //$httpProvider.interceptors.push('errorInterceptor');

    stateTree.addStates($stateProvider);
});

config.auth = {};

app.run(($rootScope, $state, $stateParams, $location) => {

    $rootScope.$state = $state;

    // auth
    $rootScope.$on('$stateChangeStart', (event, toState) => {
        //console.log("state change start");
        if (toState.requiresAuth) {

            if (config.auth.keycloak.authenticated) {

                if (toState.requiredRoles && toState.requiredRoles
                        .filter((role) => auth.keycloak.realmAccess.roles.indexOf(role) == -1).length > 0) {

                    $rootScope.$broadcast('noAuthorization');
                    event.preventDefault();
                }
            } else {
                $rootScope.$broadcast('noAuthentication', $state.href(toState.name, toState.params, {absolute: true}));
                event.preventDefault();
            }
        }
    });
    
    /*$rootScope.$on('$stateChangeSuccess', (event, toState, toParams, fromState, fromParams) => {
        console.log("state change success");
        console.log(locationSearch);
        $location.search(locationSearch);
    });*/

    $rootScope.$on('noAuthentication',(event, url) => {

        config.auth.keycloak.login({redirectUri: url});
    });

    $rootScope.$on('noAuthorization', () => {
        $state.transitionTo('layout.home');
    });

    // rem loading
    let elem = document.getElementById('splash');
    elem.parentElement.removeChild(elem);
});

config.auth.keycloak = new Keycloak();

config.auth.keycloak.init({onLoad: 'check-sso'})
    .success(() => angular.bootstrap(document, ['cross-build']))
    .error(() => angular.bootstrap(document, ['cross-build']));