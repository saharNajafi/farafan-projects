/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/7/12
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.DocTypeStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.doctypestore',

    listName: 'docTypeList',

    model: 'Ems.model.DocTypeModel',

    id: 'docTypeList',

    baseUrl: 'extJsController/doctype'

});
