/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.Estelam2FalseListStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.estelam2falseliststore',

    id: 'idEstelam2FalseListStore',

    listName: 'estelam2falseList',

    model: 'Ems.model.Estelam2FalseListModel',

    baseUrl: 'extJsController/estelam2falselist'

});

