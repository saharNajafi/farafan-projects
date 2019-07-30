/**
 * Created with IntelliJ IDEA.
 * User: a.amiri
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.BatchListStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.batchliststore',

    id: 'idBatchListStore',

    listName: 'emsBatchList',

    model: 'Ems.model.BatchListModel',

    baseUrl: 'extJsController/findbatch'

});

