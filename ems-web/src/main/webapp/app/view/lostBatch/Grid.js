Ext.define('Ems.view.lostBatch.Grid', {
	 extend: 'Gam.grid.Crud',
    alias: 'widget.lostbatchgrid',

    stateId: 'wMainlostbatchGrid',

    requires: ['Ems.store.LostBatchStore' ],

    id: 'lostbatchgrid',

    title: 'مدیریت دسته های گم شده',

    viewConfig: {
        stripeRows: true
    },

    store: {type: 'lostBatchgridsotre'},
    initComponent: function () {
		
		this.columns = this.getColumns();
		this.callParent(arguments);
	},
	 actionColumnItems: [
	                        {
	                            getClass: function (value, metaData, record, rowIndex, colIndex, store) {

	                            var confirmType = record.get( EmsObjectName.lostBatch.isConfirm);
	                            return( confirmType==0 ? 'girdAction-losttaeed-icon' : 'x-hide-display');
	                            },
	                            tooltip: 'تایید',
	                            action: 'confirmlostbatch',
	                            stateful: true,
	                            stateId: this.stateId + 'ConfirmLostbatch'
	                        }
	                        ,{
	                            getClass: function (value, metaData, record, rowIndex, colIndex, store) {

	                            var confirmType = record.get( EmsObjectName.lostBatch.isConfirm);
	                            return( confirmType==0 ? 'girdAction-lost-noconfirm-icon' : 'x-hide-display');
	                            },
	                            tooltip: 'عدم تایید',
	                            action: 'unconfirmlostbatch',
	                            stateful: true,
	                            stateId: this.stateId + 'ConfirmLostbatch'
	                        }
	                    ],
    getColumns: function () {
        return ([
            {
                xtype: 'gridcolumn',
                text: 'شماره دسته',
                width: 110,
                dataIndex: EmsObjectName.lostBatch.cmsID,
                id: EmsObjectName.lostBatch.cmsID,
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
            {
                xtype: 'gam.datecolumn',
                width: 200,
                text: 'تاریخ گم شدن دسته',
                format: Ext.Date.defaultDateTimeFormat,
                id: EmsObjectName.lostBatch.batchLostDate,
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
                dataIndex: EmsObjectName.lostBatch.batchLostDate
            },
//            {
//                xtype: 'gridcolumn',
//                width: 150,
//                text: 'وضعیت  ',
//                id: EmsObjectName.lostBatch.isConfirm,
//                filterable: true,
//                filter: true,
//                dataIndex: EmsObjectName.lostBatch.isConfirm,
//                renderer: function (value, metaData) {
//                	
//                	switch (value) {
//					case false:
//						return 'تایید نشده';
//						break;
//					case true:
//						return 'تایید شده';
//					default:
//						return 'تایید نشده';
//						break;
//					}
////                    metaData.tdCls += 'ems-direction-ltr';
////                    return value;
//                }
//            },
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'وضعیت تایید ',
                id: EmsObjectName.lostBatch.isConfirm,
                filterable: true,
                filter: {
                    xtype: 'combo',
                    store: {
                        fields: ['value', 'label'],
                        data: [
                            {
                                value: '0',
                                label:'تایید نشده'
                            },
                            {
                                value: '1',
                                label: 'تایید شده'
                            }
                        ]
                    },
                    queryMode: 'local',
                    displayField: 'label',
                    valueField: 'value'
                },
                dataIndex: EmsObjectName.lostBatch.isConfirm,
                renderer: function (value, metaData) {
                	
                	switch (value) {
					case '0':
						return 'تایید نشده';
						break;
					case '1':
						return 'تایید شده';
					default:
						return 'تایید نشده';
						break;
					}
//                    metaData.tdCls += 'ems-direction-ltr';
//                    return value;
                }

            }

        ]);
    }

});







