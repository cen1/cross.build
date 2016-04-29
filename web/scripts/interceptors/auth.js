import subModule from './index'
import {auth} from 'config'

export default subModule.factory('authInterceptor', /*@ngInject*/ ($q) => {

    return {
        request(config) {
            let deferred = $q.defer();

            //console.log(auth.keycloak.token)
            if (auth.keycloak.token) {
                
                auth.keycloak.updateToken(30).success(() => {

                    config.headers = config.headers || {};
                    config.headers.Authorization = 'Bearer ' + auth.keycloak.token;
                    deferred.resolve(config);
                }).error(() => {
                    deferred.reject({status: 401});
                });
            } else {
                deferred.resolve(config);
            }
            return deferred.promise;
        }
    };
});