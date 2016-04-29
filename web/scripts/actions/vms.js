import subModule from './index'
import {urls} from 'config';
import {headers} from 'config';

export default subModule.factory('Vms', /*@ngInject*/ ($http) => {

    const { api } = urls;
    const source = '/public/vms';

    return {
        getVms() {
            return $http.get(api + source);
        },
        getLoad(vmId, loadType) {
            return $http.get(api + source+"/"+vmId+"/load?loadType="+loadType);
        }
        
    }
});