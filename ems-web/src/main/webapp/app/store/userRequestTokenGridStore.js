/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/2/12
 * Time: 6:33 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.userRequestTokenGridStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.userrequesttokengridstore',

    require: ['Ems.model.userRequestTokenGridModel'],
    model: 'Ems.model.userRequestTokenGridModel',
    listName: 'personTokenList',
    baseUrl: 'extJsController/userRequestToken'

});

