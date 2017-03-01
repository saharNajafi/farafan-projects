/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/3/12
 * Time: 9:34 AM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.store.OfficeUserListGridStore', {
    extend: 'Gam.data.store.grid.Grid', alias: 'store.officeformgridstore', model: 'Ems.model.OfficeModel',

    listName: 'enrollmentList',

    id: 'idOfficeUserListGridStore',

    baseUrl: 'extJsController/office'

});
