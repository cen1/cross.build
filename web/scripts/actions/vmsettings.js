import subModule from './index'
import {urls} from 'config';
import {headers} from 'config';

export default subModule.factory('VmSettings', /*@ngInject*/ ($http) => {

    const { api } = urls;
    const source = '/public/vmsettings';

    return {
        getVmSettings() {
            return $http.get(api + source);
        }        
    }
});