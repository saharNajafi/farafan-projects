Ext.define('Ems.store.LostCardStore', {

    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.lostcardgridsotre',

    model: 'Ems.model.LostCardGridModel',

    listName: 'lostCardList',

    id: 'lostcardgrid',

    baseUrl: 'extJsController/lostCardList'

});