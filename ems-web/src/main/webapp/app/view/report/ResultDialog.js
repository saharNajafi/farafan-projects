/**
 * The dialog window that would be used to display report results
 */
Ext.define('Ems.view.report.ResultDialog', {
    extend: 'Gam.window.dialog.Dialog',
    alias: 'widget.reportresultdialog',
    layout: 'fit',
    width: 500,
    height: 500,

    /**
     * Identifier of the report that its result should be displayed in grid
     */
    reportId: 0,

    requires: [
        'Ems.view.report.ResultGrid'
    ],

    initComponent: function () {
        this.callParent(arguments);
    },

    /**
     * Returns contents of report window. It's a grid containing report result
     * @return {Array}
     */
    buildItems: function () {
        return [
            {
                xtype: 'reportresultgrid',
                id: 'reportResultGrid',
                reportId: this.reportId
            }
        ];
    },

    setReportId: function (reportId) {
        this.reportId = reportId;
        var reportResultStore = Ext.data.StoreManager.lookup('reportResultStore');
        Ext.apply(reportResultStore.readParams, {reportId: reportId});
        reportResultStore.load();
    }
});