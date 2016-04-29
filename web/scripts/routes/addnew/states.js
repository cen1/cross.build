import controller from './addnew';
import template from './addnew.html';

export default {
    id: 'addnew',
    url: '/addnew',
    requiresAuth: true,
    controller,
    template
};
