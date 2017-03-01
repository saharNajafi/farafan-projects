/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/3/12
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.DepartmentStore', {
    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.departmentstore',

    listName: 'departmentList',

    model: 'Ems.model.DepartmentModel',

    id: 'DepartmentList',

    baseUrl: 'extJsController/department'

});
