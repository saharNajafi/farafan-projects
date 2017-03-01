/**
 * List of report requests made by current user
 * User: E.Z.Moghaddam
 * Date: 7/18/12
 * Time: 3:37 PM
 * Displays list of report requests made by current user
 */

Ext.define('Ems.view.reportResult.Grid', {
    extend: 'Gam.grid.Crud',
    alias: 'widget.reportresultgrid',
    stateId: 'wReportResultGrid',
    title: 'درخواست های ثبت شده برای تولید گزارش',
    multiSelect: true,

    requires: [
        'Ems.store.report.ReportResultStore'
    ],

    store: {type: 'reportresstore'},

    actions: [
        'gam.delete'
    ],

    tbar: ['gam.delete'],

    actionColumnItems: [
        {
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                if (record.get("requestState") == "IN_PROGRESSED") {
                    return 'x-hide-display';
                } else {
                    return 'grid-delete-icon';
                }
            },
            tooltip: 'حذف درخواست گزارش',
            action: 'deleteRequest'
        },
        {
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                if (record.get("requestState") == "PROCESSED") {
                    return 'report-download-icon';
                } else {
                    return 'x-hide-display';
                }
            },
            tooltip: 'دریافت فایل نتیجه گزارش',
            action: 'downloadResult'
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
        return [
            {
                dataIndex: EmsObjectName.reportResult.reportName,
                id: EmsObjectName.reportResult.reportName,
                text: 'عنوان گزارش',
                width: 400
            },
            {
                xtype: 'gam.datecolumn',
                dataIndex: EmsObjectName.reportResult.requestDate,
                id: EmsObjectName.reportResult.requestDate,
                format: Ext.Date.defaultDateTimeFormat,
                text: 'زمان درخواست',
                width: 150
            },
            {
                xtype: 'gam.datecolumn',
                dataIndex: EmsObjectName.reportResult.generateDate,
                id: EmsObjectName.reportResult.generateDate,
                format: Ext.Date.defaultDateTimeFormat,
                text: 'زمان تولید گزارش',
                width: 150
            },
            {
                xtype: 'gam.datecolumn',
                dataIndex: EmsObjectName.reportResult.jobScheduleDate,
                id: EmsObjectName.reportResult.jobScheduleDate,
                format: Ext.Date.defaultDateTimeFormat,
                text: 'زمان درخواستی تولید گزارش',
                width: 150
            },
            {
                xtype: 'gam.actioncolumn',
                width: 80,
                text: 'نوع خروجی',
                dataIndex: EmsObjectName.reportResult.requestOutputType,
                id:  EmsObjectName.reportResult.requestOutputType,
                align: 'center',
                items: [
                    {
                        action: 'dummyOutputType',
                        getClass: function (value, metadata, record) {
                            var outputType = record.get(EmsObjectName.reportResult.requestOutputType);

                            return (outputType === 'PDF') ? 'report-exportPDF-icon' : 'report-exportExcel-icon';
                        }
                    }
                ]
            },
            {
                dataIndex: EmsObjectName.reportResult.requestState,
                id: EmsObjectName.reportResult.requestState,
                text: 'وضعیت',
                width: 120,
                renderer: function (value) {
                    var result = value;
                    switch (value) {
                        case 'REQUESTED':
                            result = 'منتظر اجرا';
                            break;
                        case 'IN_PROGRESSED':
                            result = 'در دست پردازش';
                            break;
                        case 'ERROR':
                            result = 'خطا در پردازش';
                            break;
                        case 'PROCESSED':
                            result = 'آماده دریافت فایل نتیجه';
                            break;
                    }
                    return result;
                }
            }
        ];
    }
});


