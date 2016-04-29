import subModule from './index'

export default subModule.filter('capitalize', () => {

    return (input) => {

        if (input != null)
            input = input.toLowerCase();
        else
            return '';

        var inputs = input.split(' ').map((i) => i.substring(0, 1).toUpperCase() + i.substring(1));

        return inputs.join(' ');
    };
});