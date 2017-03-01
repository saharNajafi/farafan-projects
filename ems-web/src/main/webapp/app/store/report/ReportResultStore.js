/**
 * The store for list of report requests which have been saved by current user
 *
 * User: E.Z.Moghaddam
 * Date: 7/18/12
 * Time: 3:52 PM
 *
 * The store for list of report requests which have been saved by current user
 */
Ext.define('Ems.store.report.ReportResultStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.reportresstore',

    model: 'Ems.model.report.ReportResultModel',

    baseUrl: 'extJsController/reportrequests',

    listName: 'reportResultsList'
});

