/**
 * Date     : 12/8/13
 * Author   : R. Bakhshpour (ramin.bp@gmail.com)
 */
Ext.define('Ems.controller.ManageRolesController', {
    extend: 'Gam.app.controller.Controller',

    ns: 'extJsController/manageRoles',

    initViewTypePostfix: 'iframe',

    views: [
        'manageRoles.Iframe'
    ]
});
