import subModule from './index'

export default subModule.filter('capitalizeFirst', () => {

    return (input) => {

        if (input != null)
            input = input.toLowerCase();
        else
            return '';

        return input.substring(0,1).toUpperCase()+input.substring(1);
    };
});