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
        },
        update(data = {}, config = {}) {
            return $http.put(api + source + "/"+data.id, data, config);
        },
        get() {
            return $http.get(api+source);
        },
        getStatus(projectGroupId, projectId) {
            return $http.get(api+source+"/"+projectGroupId+"/projects/"+projectId+"/status");
        },
        getBuildDetails(projectGroupId, projectId, buildNumber) {
            return $http.get(api+source+"/"+projectGroupId+"/projects/"+projectId+"/build/"+buildNumber);
        },
        getBuildConsole(projectGroupId, projectId, buildNumber) {
            return $http.get(api+source+"/"+projectGroupId+"/projects/"+projectId+"/build/"+buildNumber+"/console", {responseType:"text"});
        }
    }
});