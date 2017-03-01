/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/18/12
 * Time: 3:52 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.ReportRequestStore', {
    extend: 'Ext.data.Store', //'Gam.data.store.grid.Grid',

    alias: 'store.reportrequeststore',

    model: 'Ems.model.ReportRequestModel',

    //baseUrl:'extJsController/user',

    listName: 'ratingInfoList',

    proxy: {
        url: 'data/report/reportRequest/Grid.json',
        type: 'ajax',
        reader: {
            type: 'json',
            root: 'records'
        }
    }
});

