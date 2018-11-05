/**
 * Created with IntelliJ IDEA.
 * User: Navid
 * Date: 05/19/2018
 * Time: 2:05 PM
 */
Ext.define('Ems.store.OfficeSettingTest', {
    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.officesettingtest',
    id: 'officesettingtest',
    model: 'Ems.model.OfficeSettingTest',
    listName: 'officeSettingList',
    baseUrl: 'extJsController/officeSetting'
});
