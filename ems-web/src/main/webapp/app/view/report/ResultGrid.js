/**
 * Displays results of a report
 */
Ext.define('Ems.view.report.ResultGrid', {
    extend: 'Gam.grid.Crud',
    alias: 'widget.reportresultgrid',
    stateId: 'wReportResultGrid',
    id: 'grdReportResults',
    multiSelect: false,

    requires: [
        'Ems.store.ReportResultStore'
    ],

    store: {type: 'reportresultstore'},

    initComponent: function () {
        this.columns = this.getColumnUserGrid();
//        alert(this.reportId)
//        this.getStore().readParams = {requestId : this.requestId};
        this.callParent(arguments);
    },

    /**
     * Returns an array of columns to be displayed on grid
     * @return {Array}  Array of columns to be displayed on grid
     */
    getColumnUserGrid: function () {
        return([
            {
                xtype: 'gridcolumn',
                width: 300,
                text: 'عنوان',
                dataIndex: EmsObjectName.reportResponse.title, id: EmsObjectName.reportResponse.g_title,
                renderer: function (value) {
                    if (EmsResource.reportResult[value])
                        return EmsResource.reportResult[value];

                    return value;
                }
            },
            {
                xtype: 'gridcolumn',
                sortable: false,
                width: 50,
                text: 'تعداد',
                dataIndex: EmsObjectName.reportResponse.count, id: EmsObjectName.reportResponse.g_count
            }
        ]);
    }
});
