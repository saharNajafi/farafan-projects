Ext.define('Ems.view.dispatch.Grid', {
    extend: 'Ems.view.dispatch.parent.Grid',
    alias: 'widget.dispatchgrid',

    stateId: 'wMainDispatchGrid',

    requires: ['Ems.store.DispatchStore' ],

    id: 'dispatchGrid',

    title: 'مدیریت توزیع',

    viewConfig: {
        stripeRows: true
    },

    store: {type: 'dispatchgridsotre'}, getColumns: function () {
        return ([
            {
                xtype: 'gridcolumn',
                text: 'شماره کارتن',
                width: 110,
                dataIndex: EmsObjectName.dispatchGrid.numberBox,
                id: EmsObjectName.dispatchGrid.numberBox,
                filterable: true,
                filter: {
                    style: 'direction: ltr;'
                },
//                style: "direction: ltr;",
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
                id: EmsObjectName.dispatchGrid.receiverBox,
                filterable: true,
                filter: true,
                dataIndex: EmsObjectName.dispatchGrid.receiverBox

            },
            {
                xtype: 'gam.datecolumn',
                width: 200,
                text: 'تاريخ ارسال به این واحد',
                format: Ext.Date.defaultDateTimeFormat,
                id: EmsObjectName.dispatchGrid.sendDateBox,
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
                            name: 'fromSentDate'
                        },
                        {
                            fieldLabel: 'تا',
                            name: 'toSendDate'
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
                id: EmsObjectName.dispatchGrid.receiveDateBox,
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
                            name: 'fromReceiveDate'
                        },
                        {
                            fieldLabel: 'تا',
                            name: 'toReceiveDate'
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
                id: EmsObjectName.dispatchGrid.statusBox,
                filterable: true,
                filter: {
                    xtype: 'dispatchstatuscombobox'
                },
                renderer: function (value) {
                    if (value >= 0 && typeof value != 'undefined')
                        return eval("EmsResource.dispatching.DispatchStatus.s" + value);
                    ;
                }
            },
            {
                xtype: 'gam.actioncolumn',
                dataIndex: EmsObjectName.dispatchGrid.statusBox,
                id: EmsObjectName.dispatchGrid.action,
                text: 'عملیات',
                style: 'padding :1px;',
                flex: 1,
                items: this.ActionColumnDispatch()
            }

        ]);
    }


//    ,getSetBoxDetail:function(){
//        return ({
//            icon:'resources/themes/images/dispath/setBoxDetail.png',//DesignationStatusBatch.png',
//            tooltip:'تعیین وضعیت دسته ها',
//            action:'setBoxDetail',
//            style:'margin-left:5px',
//            getClass: function(value,metadata,record){
//
//                var statusBox = record.get(EmsObjectName.dispatchGrid.statusBox),
//                    typeReceiver= record.get(EmsObjectName.dispatchGrid.typeReceiver),
//                    typeSend= record.get(EmsObjectName.dispatchGrid.typeSend);
//
//                return ( typeReceiver!=typeSend) ? 'x-grid-center-icon' : 'x-hide-display';
//            }
//        });
//    }
    , getMySendDateBox: function () {
        return {hidden: true};
    }, getBatchNumber: function () {
        return({
            xtype: 'gridcolumn',
            width: 100,
            align: 'center',
            text: 'تعداد دسته ها',
            filterable: true,
            filter: true,
            dataIndex: EmsObjectName.dispatchGrid.batchNumber,
            id: EmsObjectName.dispatchGrid.batchNumber
        });
    }
});







