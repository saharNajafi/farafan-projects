/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/6/12
 * Time: 12:39 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.DispatchStatusBatchStore', {
    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.dispatchstatusbatchstore',

    listName: 'detailDispatchList',

    model: 'Ems.model.DispatchGridModel',

    id: 'detailDispatchList',

    baseUrl: 'extJsController/dispatch'

});
