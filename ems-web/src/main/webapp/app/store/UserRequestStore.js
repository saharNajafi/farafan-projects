/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/19/12
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.UserRequestStore', {
    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.userrequeststore',

    model: 'Ems.model.UserModel',

    baseUrl: 'extJsController/user',

    listName: 'requestedUserList'

});

