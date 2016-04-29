let angular = require('angular');

let states = require('./routes/states');

var StateTree = function (_states) {
    if (!_states) throw new Error('_states is null or undefined in stateTree');
    this.defaultPath = _states.defaultPath;
    this.root = _states;
};

var StateTreeNode = function () {
};

StateTreeNode.prototype.getParentSegment = function () {
    var node = this;
    while (node.parent && !node.isSegment) {
        node = node.parent;
    }
    return node;
};

var addStateRecursive = function (root, stateProvider, currentId, currentUrl, currentSegment, depth) {
    if (!depth) depth = 0;
    var id = currentId ? currentId + '.' + root.id : root.id;

    root.depth = depth;
    root.absoluteId = id;
    root.absoluteUrl = currentUrl ? currentUrl + root.url : '!' + root.url;

    currentUrl = root.absoluteUrl;

    if (!root.isSubPage) root.isSubPage = false;
    else root.isSubPage = true;

    if (!root.hideSubPages) root.hideSubPages = false;
    else root.hideSubPages = true;

    if (root.visible !== false) root.visible = true;

    if (root.isSegment !== true) root.isSegment = false;

    if (root.isSegment) {
        currentSegment = root;
    }
    root.segment = currentSegment;

    stateProvider.state(id, root);

    if (root.children) {

        root.children.forEach(function (state, i) {

            state.parentState = root;
            state.menuParentState = findFirstMenuParent(root);
            if (state.menuParentState === state.parentState) {
                state.menuIndex = i;
            } else {
                state.menuIndex = state.parentState.menuIndex;
            }

            // set child authentication requirements
            if ((typeof state.requiresAuth === 'undefined' &&
                typeof root.requiresAuth !== 'undefined') || root.requiresAuth) {
                state.requiresAuth = root.requiresAuth;
            }

            // set child roles requirements
            if (typeof state.requiredRoles === 'undefined' &&
                typeof root.requiredRoles !== 'undefined') {
                state.requiredRoles = angular.extend(root.requiredRoles);
            } else if (state.requiredRoles && root.requiredRoles) {
                state.requiredRoles = state.requiredRoles.concat(root.requiredRoles).filter(function(el, index, arr) {
                    return index === arr.indexOf(el);
                });
            }

            addStateRecursive(state, stateProvider, id, currentUrl, currentSegment, depth + 1);
        });
    }
};

var findFirstMenuParent = function (root) {
    if (root.isMenuParent !== false) {
        return root;
    } else if (root.parentState) {
        return findFirstMenuParent(root.parentState);
    }
    return null;
};

StateTree.prototype.addStates = function (stateProvider) {
    if (!this.root) return;
    addStateRecursive(this.root, stateProvider);
};

module.exports = new StateTree(states);