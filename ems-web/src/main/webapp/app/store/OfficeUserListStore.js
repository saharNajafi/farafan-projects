/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/7/12
 * Time: 9:48 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.OfficeUserListStore', {
    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.officeuserliststore',

    /*    listName:'departmentList',

     model:'Ems.model.DepartmentModel',

     baseUrl:'extJsController/department'*/

    model: 'Ems.model.OfficeUserListModel',
    baseUrl: 'extJsController/dispatch',
    listName: 'departmentUserList'
});
