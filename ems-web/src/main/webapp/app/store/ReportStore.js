/**
 * List of reports to be used by manage reports module
 * User: E.Z.Moghaddam
 * Date: 7/18/12
 * Time: 3:52 PM
 * Fetches the list of reports to be used on manage reports module
 */
Ext.define('Ems.store.ReportStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.reportstore',

    model: 'Ems.model.ReportModel',

    baseUrl: 'extJsController/managereports',

    listName: 'reportManagementList'
});

