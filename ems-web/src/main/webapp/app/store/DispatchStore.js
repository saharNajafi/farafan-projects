Ext.define('Ems.store.DispatchStore', {

    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.dispatchgridsotre',

    model: 'Ems.model.DispatchGridModel',


    listName: 'mainDispatchList',

    id: 'mainDispatchList',

    baseUrl: 'extJsController/dispatch'

});