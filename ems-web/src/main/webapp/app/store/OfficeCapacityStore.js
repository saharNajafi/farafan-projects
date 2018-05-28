/**
 * Created with IntelliJ IDEA.
 * User: Navid
 * Date: 05/19/2018
 * Time: 2:05 PM
 */
Ext.define('Ems.store.OfficeCapacityStore', {
    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.officecapacitystore',

    model: 'Ems.model.OfficeCapacityModel',
    baseUrl: 'extJsController/dispatch',
    listName: 'departmentUserList'
});
