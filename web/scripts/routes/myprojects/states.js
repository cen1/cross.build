import controller from './myprojects';
import template from './myprojects.html';

export default {
    id: 'myprojects',
    url: '/myprojects',
    requiresAuth: true,
    controller,
    template
};
