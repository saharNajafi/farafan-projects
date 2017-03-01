Ext.define('Ems.store.LostCardBatchStore', {

    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.lostcardbatchsotre',

    model: 'Ems.model.LostCardBatchModel',

    listName: 'lostBatchList',

    id: 'lostCardBatchList',

    baseUrl: 'extJsController/lostBatchList'

});