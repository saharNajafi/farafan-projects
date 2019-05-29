Ext.define('Ems.store.PrintRegistrationReceiptStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.printregistrationreceiptstore',

    id: 'idPrintRegistrationReceiptStore',

    listName: 'emsCardRequestList',

    model: 'Ems.model.CardRequestListModel',

    baseUrl: 'extJsController/cardrequestlist'

});