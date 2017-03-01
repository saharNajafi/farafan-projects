/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/7/12
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.BlackListStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.blackliststore',

    listName: 'blackList',

    model: 'Ems.model.BlackListModel',

    baseUrl: 'extJsController/blackList'

});

