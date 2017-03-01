/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/18/12
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.report.Grid', {
    extend: 'Gam.grid.Crud',
    alias: 'widget.reportgrid',
    stateId: 'wReportGrid',
    title: 'درخواست گزارش',
    multiSelect: false,

    requires: [
        'Ems.store.ReportRequestStore'
    ],

    store: {type: 'reportrequeststore'},

    actionColumnItems: [
        {
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                return 'grid-edit-action-icon';
            },
            tooltip: 'مشاهده',
            action: 'viewReport',
            stateful: true,
            stateId: this.stateId + 'ViewReport'
        }
    ],

    initComponent: function () {
        this.columns = this.getItemsGridForm();
        this.callParent(arguments);
    },

    getItemsGridForm: function () {
        return([
            {
                dataIndex: EmsObjectName.reportRequest.subject,
                id: EmsObjectName.reportRequest.subject,
                text: 'عنوان',
                width: 200
            }
        ]);
    }
});


