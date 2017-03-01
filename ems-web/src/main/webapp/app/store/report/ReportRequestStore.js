/**
 * The store for list of report requests
 * User: E.Z.Moghaddam
 * Date: 7/18/12
 * Time: 3:52 PM
 * The store for list of report requests
 */
Ext.define('Ems.store.report.ReportRequestStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.reportreqstore',

    model: 'Ems.model.ReportRequestModel',

    baseUrl: 'extJsController/reportrequests',

    listName: 'reportRequestList',

    readParams: {
        'scope': 'EMS'
    }
});

