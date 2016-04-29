import subModule from './index'
import {urls} from 'config';

export default subModule.factory('Test', /*@ngInject*/ ($http) => {

    const { api } = urls;
    const source = '/test';

    return {
        gettest() {
            return $http.get(`${api}${source}`);
        }
    }
});