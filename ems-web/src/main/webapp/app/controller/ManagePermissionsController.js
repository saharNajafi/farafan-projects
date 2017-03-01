/**
 * Date     : 12/8/13
 * Author   : R. Bakhshpour (ramin.bp@gmail.com)
 */
Ext.define('Ems.controller.ManagePermissionsController', {
    extend: 'Gam.app.controller.Controller',

    ns: 'extJsController/managePermissions',

    initViewTypePostfix: 'iframe',

    views: [
        'managePermissions.Iframe'
    ]
});
