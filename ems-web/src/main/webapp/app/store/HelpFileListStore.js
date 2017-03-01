//khodayari
Ext.define('Ems.store.HelpFileListStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.helpfileliststore',

    id: 'idHelpFileListStore',
    listName: 'helpList',
    model: 'Ems.model.HelpFileListStoreModel',
    baseUrl: 'extJsController/helpfilelist'
});

