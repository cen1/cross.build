import subModule from './index'
import {urls} from 'config';
import {auth} from 'config'

export default subModule.factory('ProjectGroups', /*@ngInject*/ ($http) => {

    const { api } = urls;
    var userId = auth.keycloak.idTokenParsed.sub;
    const source = '/users/'+userId+'/projectgroups';

    return {
        addnew(data = {}, config = {}) {
            return $http.post(api + source, data, config);
        }
    }
});