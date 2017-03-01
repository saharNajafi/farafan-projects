/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 4/29/12
 * Time: 10:29 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.UserStore', {
    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.userstore',

    model: 'Ems.model.UserModel',

    baseUrl: 'extJsController/user',

    listName: 'userList'

});

