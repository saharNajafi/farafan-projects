/**
 * List of report requests made by current user
 * User: E.Z.Moghaddam
 * Date: 7/18/12
 * Time: 3:37 PM
 * Displays list of report requests made by current user
 */

Ext.define('Ems.view.reportRequest.Grid', {
    extend: 'Gam.grid.Crud',
    alias: 'widget.reportrequestgrid',
    stateId: 'wReportRequestGrid',
    title: 'درخواست گزارش',
    multiSelect: false,

    requires: [
        'Ems.store.report.ReportRequestStore'
    ],

    store: {type: 'reportreqstore'},

    actionColumnItems: [
        {
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                return 'grid-run-report-icon';
            },
            tooltip: 'ثبت درخواست گزارش',
            action: 'runReport'
        }
    ],

    initComponent: function () {
        this.columns = this.getGridColumns();
        this.callParent(arguments);
    },

    /**
     * Returns an array of grid columns
     *
     * @returns {Array}
     */
    getGridColumns: function () {
        return([
            {
                dataIndex: EmsObjectName.reportRequest.name,
                id: EmsObjectName.reportRequest.name,
                text: 'عنوان',
                width: 200
            },
            {
                dataIndex: EmsObjectName.reportRequest.comment,
                id: EmsObjectName.reportRequest.comment,
                text: 'توضیحات',
                width: 400
            }
        ]);
    }
});


