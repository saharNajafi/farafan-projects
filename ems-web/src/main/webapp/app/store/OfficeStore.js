/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/3/12
 * Time: 1:11 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.OfficeStore', {
    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.officeformgridstore',
    model: 'Ems.model.OfficeModel',

    listName: 'enrollmentList',

    id: 'officeList',

    baseUrl: 'extJsController/office'
});


