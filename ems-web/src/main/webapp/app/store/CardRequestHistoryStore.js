/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.CardRequestHistoryStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.cardrequesthistorystore',

    listName: 'emsCardRequestHistoryList',

    model: 'Ems.model.CardRequestHistoryModel',

    baseUrl: 'extJsController/cardrequestlist'

});
