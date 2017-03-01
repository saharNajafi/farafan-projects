/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/8/12
 * Time: 10:27 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.bizLog.Grid', {
    extend: 'Gam.grid.Crud',

    alias: 'widget.bizloggrid',

    stateId: 'wBizlogGrid',

    storeAutoLoad: false,

    title: 'مدیریت رویداد ها',

    multiSelect: false,

    requires: [
        'Ems.store.BizStore',
        'Ems.view.bizLog.Action',
        'Ems.view.bizLog.Entity',

        'Ems.view.office.Dialog' ,
        'Ems.view.bizLog.Dialog'
    ],

    store: {type: 'bizstore'},

    actions: ['gam.add->bizlogdialog'],

    actionColumnItems: [
        'view->bizlogdialog',
        {
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                var result = 'girdAction-';
                var val = record.get('verify');

                if (val === "" || value === null) {
                    result += 'verify';
                } else if (val === true) {
                    result += 'verified';
                } else if (val === false) {
                    result += 'noVerified';
                }
                return result + '-icon';
            },

            tooltip: '',
            action: 'verify',
            stateful: true,
            stateId: this.stateId + 'Editing'
        }

    ],
    tbar: [
        {
            iconCls: 'girdAction-exportExcel-icon',
            text: 'خروجی اکسل',
            action: 'exportExcel',
            stateful: true,
            stateId: this.stateId + 'ExportExcel'
        }
    ],

    /* contextMenu: [ 'gam.add' ],

     tbar: ['gam.add' ] ,*/

    initComponent: function () {
        this.columns = this.getItemsGridForm();
        this.callParent(arguments);
    }, getItemsGridForm: function () {
        return([
            {
                text: 'تاریخ',
                dataIndex: EmsObjectName.bizLog.date,
                id: EmsObjectName.bizLog.date,
                width: 200,
                xtype: 'gam.datecolumn',
                format: Ext.Date.defaultDateTimeFormat,
                filterable: true,
                filter: {
                    xtype: 'container',
                    layout: {
                        type: 'hbox',
                        align: 'middle'
                    },
                    defaults: {
                        labelWidth: 10,
                        xtype: 'datefield',
                        flex: 1
                    },
                    items: [
                        {
                            fieldLabel: 'از',
                            name: 'fromDate'
                        },
                        {
                            fieldLabel: 'تا',
                            name: 'toDate'
                        }
                    ]
                }
            },
            {
                xtype: 'gridcolumn',
                text: 'شخص',
                dataIndex: EmsObjectName.bizLog.actor,
                id: EmsObjectName.bizLog.actor,
                align: 'center',
                filter: true
            },
            {
                xtype: 'gridcolumn',
                text: 'عملیات',
                dataIndex: EmsObjectName.bizLog.actionNameStv,
                id: EmsObjectName.bizLog.actionNameStv,
                filter: {
                    xtype: 'bizlogaction'
                },
                renderer: function (value) {
                    return eval("EmsResource.Action." + value);
                }
            },
            {
                xtype: 'gridcolumn',
                text: 'موجودیت',
                dataIndex: EmsObjectName.bizLog.entityNameStv,
                id: EmsObjectName.bizLog.entityNameStv,
                filter: {
                    xtype: 'entityaction'
                },
                renderer: function (value) {
                    return eval("EmsResource.Entity." + value);
                }
            },
            {
                xtype: 'gridcolumn',
                text: 'شناسه موجودیت',
                dataIndex: EmsObjectName.bizLog.entityID,
                id: EmsObjectName.bizLog.entityID,
                filter: true,
                align: 'center'
            },
            {
                xtype: 'gridcolumn',
                text: 'اطلاعات تکمیلی',
                sortable: false,
                dataIndex: EmsObjectName.bizLog.additionalData,
                id: EmsObjectName.bizLog.additionalData,
                filter: true,
                flex: 1
            }
        ]);
    },
    setVerified: function (grid, rowIndex, value) {
        return({});
    }
});
