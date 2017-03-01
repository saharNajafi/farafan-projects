
Ext.define('Ems.store.TokenRequestListStore', {
    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.tokenrequestliststore',
    id: 'idTokenRequestListStore',
    model:  'Ems.model.TokenRequestListModel',
    baseUrl: 'extJsController/token',

    listName: 'tokenRenewalRequestList'
});
