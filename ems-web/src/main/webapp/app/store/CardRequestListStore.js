/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.CardRequestListStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.cardrequestliststore',

    id: 'idCardRequestListStore',

    listName: 'emsCardRequestList',

    model: 'Ems.model.CardRequestListModel',

    baseUrl: 'extJsController/cardrequestlist'

});

