import controller from './layout'
import template from './layout.html'
import home from './home/states'
import addnew from './addnew/states'
import myprojects from './myprojects/states'
import status from './status/states'

export default {
    id: 'layout',
    url: '',
    title: 'states.layout.title',
    longTitle: 'states.layout.longTitle',
    abstract: true,
    defaultChild: home,
    visible: false,
    controller,
    template,
    children: [
        home,
        addnew,
        myprojects,
        status
    ]
};
