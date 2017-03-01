/**
 * List of reports to be managed by authorized admin.
 * User: E.Z.Moghaddam
 * Date: 7/18/12
 * Time: 3:37 PM
 * Displays list of report definition to be managed by authorized user
 */

Ext.define('Ems.view.manageReports.Grid', {
    extend: 'Gam.grid.Crud',
    alias: 'widget.managereportsgrid',
    stateId: 'wManageReportGrid',
    title: 'مدیریت گزارشات',
    multiSelect: false,

    requires: [
        'Ems.store.ReportStore'
    ],

    store: {type: 'reportstore'},

//    actionColumnItems: [
//        {
//            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
//                return 'grid-edit-action-icon';
//            },
//            tooltip: record.get("activationFlag") == 'T' ? 'غیر فعال' : 'فعال',
//            action: 'viewReport',
//            stateful: true,
//            stateId: this.stateId + 'ViewReport'
//        }
//    ],

    initComponent: function () {
        this.columns = this.getGridColumns();
        this.callParent(arguments);
    },

    getGridColumns: function () {
        return([
            {
                dataIndex: EmsObjectName.manageReports.name,
                id: EmsObjectName.manageReports.name,
                text: 'عنوان',
                width: 200
            },
            {
                dataIndex: EmsObjectName.manageReports.comment,
                id: EmsObjectName.manageReports.comment,
                text: 'توضیحات',
                width: 300
            },
            {
                dataIndex: EmsObjectName.manageReports.permission,
                id: EmsObjectName.manageReports.permission,
                text: 'دسترسی',
                width: 200
            },
            {
                xtype: 'gam.actioncolumn',
                dataIndex: EmsObjectName.manageReports.activationFlag,
                id: EmsObjectName.manageReports.activationFlag,
                align: 'center',
                text: 'وضعیت',
                width: 100,
                items: [
                    {
                        tooltip: 'غیر فعال - برای فعال سازی کلیک کنید',
                        action: 'activate',
                        getClass: function (value, metadata, record) {
                            var state = record.get(EmsObjectName.manageReports.activationFlag);

                            return (state === 'F') ? 'grid-activate-report-icon' : 'x-hide-display';
                        }
                    },
                    {
                        tooltip: 'فعال - برای غیر فعال کردن کلیک کنید',
                        action: 'deactivate',
                        getClass: function (value, metadata, record) {
                            var state = record.get(EmsObjectName.manageReports.activationFlag);

                            return (state === 'T') ? 'grid-deactivate-report-icon' : 'x-hide-display';
                        }
                    }
                ]
            }
        ]);
    }
});


