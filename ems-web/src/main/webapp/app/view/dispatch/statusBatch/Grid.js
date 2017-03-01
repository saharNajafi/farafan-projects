Ext.define('Ems.view.dispatch.statusBatch.Grid', {
    extend: 'Ems.view.dispatch.parent.Grid',
    alias: 'widget.dispatchstatusbatchgrid',

    stateId: 'wDetailDispatchGrid',

    requires: ['Ems.store.DispatchStatusBatchStore' ],

    id: 'dispatchStatusBatchGrid',

    viewConfig: {
        stripeRows: true
    },

    store: {type: 'dispatchstatusbatchstore'}, getSetBoxDetail: function () {
        return false;
    },

    constructor: function (config) {
        //config = config || {};

        //this.store = Ext.apply({readParams: config.parId}, this.store);
        //delete config.parId;

        this.callParent(arguments);
    }, getColumns: function () {
        return ([
            {
                xtype: 'gridcolumn',
                text: 'شماره دسته',
                width: 110,
                dataIndex: EmsObjectName.dispatchGrid.numberBox,
                id: EmsObjectName.dispatchGrid.detail_numberBox,
                filterable: true,
                filter: {
                    style: 'direction: ltr;'
                },
                renderer: function (value, metaData) {
                    metaData.tdCls += 'ems-direction-ltr';
                    return value;
                }
            }
            ,
            this.getBatchNumber()
            ,
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'گیرنده',
                id: EmsObjectName.dispatchGrid.detail_receiverBox,
                filterable: true,
                filter: true,
                dataIndex: EmsObjectName.dispatchGrid.receiverBox

            }/* , {
             xtype: 'gridcolumn',
             text: 'کد گیرنده',
             dataIndex: EmsObjectName.dispatchGrid.receiverCodeBox

             }*/
            ,
            {
                xtype: 'gam.datecolumn',
                width: 200,
                text: 'تاريخ ارسال به این واحد',
                format: Ext.Date.defaultDateTimeFormat,
                id: EmsObjectName.dispatchGrid.detail_sendDateBox,
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
                },
                dataIndex: EmsObjectName.dispatchGrid.sendDateBox

            }
            ,
            {
                xtype: 'gam.datecolumn',
                width: 200,
                text: 'تاریخ دریافت در این واحد',
                id: EmsObjectName.dispatchGrid.detail_receiveDateBox,
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
                },
                dataIndex: EmsObjectName.dispatchGrid.receiveDateBox

            } ,
            this.getMySendDateBox()
            ,
            {
                xtype: 'gridcolumn',
                text: 'وضعیت',
                sortable: false,
                width: 90,
                dataIndex: EmsObjectName.dispatchGrid.statusBox,
                id: EmsObjectName.dispatchGrid.detail_statusBox,
                filterable: true,
                filter: {
                    xtype: 'dispatchstatuscombobox'
                },
                renderer: function (value) {
                    if (value >= 0 && typeof value != 'undefined')
                        return eval("EmsResource.dispatching.DispatchStatus.s" + value);
                }
            },
            {
                xtype: 'gam.actioncolumn',
                dataIndex: EmsObjectName.dispatchGrid.statusBox,
                id: EmsObjectName.dispatchGrid.detail_action,
                text: 'عملیات',
                style: 'padding :1px;',
                flex: 1,
                items: this.ActionColumnDispatch()
            }

        ]);
    }, getMySendDateBox: function () {
        return({
            xtype: 'gam.datecolumn',
            width: 130,
            text: 'تاریخ ارسال از این واحد',
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
            },
            dataIndex: EmsObjectName.dispatchGrid.mySendDateBox,
            id: EmsObjectName.dispatchGrid.detail_mySendDateBox,
            format: Ext.Date.defaultDateTimeFormat
        });
    }, getBatchNumber: function () {
        return({
            xtype: 'gridcolumn',
            width: 100,
            align: 'center',
            filterable: true,
            filter: true,
            text: 'تعداد کارتها',
            dataIndex: EmsObjectName.dispatchGrid.batchNumber,
            id: EmsObjectName.dispatchGrid.detail_batchNumber
        });
    }

});




