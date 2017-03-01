Ext.define('Ems.store.LostBatchStore', {

    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.lostBatchgridsotre',

    model: 'Ems.model.LostBatchGridModel',

    listName: 'lostBatchList',

    id: 'lostBatchgrid',

    baseUrl: 'extJsController/lostBatchList'

});