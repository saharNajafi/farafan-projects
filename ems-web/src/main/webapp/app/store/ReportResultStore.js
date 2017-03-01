/**
 * Store of report results
 */
Ext.define('Ems.store.ReportResultStore', {
    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.reportresultstore',

    model: 'Ems.model.ReportResultModel',

    baseUrl: 'extJsController/report',

    listName: 'reportResultList',

    storeId: 'reportResultStore',

    readParams: {
        reportId: 1
    }

//    proxy:{
//        extraParams: {
//            reportId: 1
//        }
//    }
});
