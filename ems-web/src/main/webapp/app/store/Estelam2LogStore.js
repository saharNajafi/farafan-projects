/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.Estelam2LogStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.estelam2logstore',

    id: 'idEstelam2LogStore',
    
    listName: 'estelam2LogList',

    model: 'Ems.model.Estelam2LogModel',

    baseUrl: 'extJsController/estelam2falselist'

});
