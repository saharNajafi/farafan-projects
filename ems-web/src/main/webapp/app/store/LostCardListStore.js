Ext.define('Ems.store.LostCardListStore', {

    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.lostcardsotre',

    model: 'Ems.model.LostCardListModel',

    listName: 'lostCardList',

    id: 'lostCardList',

    baseUrl: 'extJsController/lostCardList'

});