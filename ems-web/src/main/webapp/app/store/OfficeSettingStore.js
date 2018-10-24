Ext.define('Ems.store.OfficeSettingStore', {
    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.officesettingstore',
    id: 'idOfficeSettingStore',
    model: 'Ems.model.OfficeSettingModel',
    listName: 'officeSettingList',
    baseUrl: 'extJsController/officeSetting'
});