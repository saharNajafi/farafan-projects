Ext.define('Ems.view.lostCard.Grid', {
	 extend: 'Gam.grid.Crud',
    alias: 'widget.lostcardgrid',

    stateId: 'wMainlostcardGrid',

    requires: ['Ems.store.LostCardStore' ],

    id: 'lostcardgrid',

    title: 'مدیریت کارتهای گم شده',

    viewConfig: {
        stripeRows: true
    },

    store: {type: 'lostcardgridsotre'},

    initComponent: function () {
        this.columns = this.getColumns();
        this.callParent(arguments);
    },
    actionColumnItems: [

                    ],
    getColumns: function () {
        return ([
{
    xtype: 'gridcolumn',
    text: 'نام',
    width: 110,
    dataIndex: EmsObjectName.lostCard.fname,
    id: EmsObjectName.lostCard.fname,
    filterable: true,
    filter: {
        style: 'direction: ltr;'
    },
//    style: "direction: ltr;",
    renderer: function (value, metaData) {
        metaData.tdCls += 'ems-direction-ltr';
        return value;
    }
},
{
    xtype: 'gridcolumn',
    text: 'نام خانوادگی',
    width: 110,
    dataIndex: EmsObjectName.lostCard.lname,
    id: EmsObjectName.lostCard.lname,
    filterable: true,
    filter: {
        style: 'direction: ltr;'
    },
//    style: "direction: ltr;",
    renderer: function (value, metaData) {
        metaData.tdCls += 'ems-direction-ltr';
        return value;
    }
},
{
    xtype: 'gridcolumn',
    text: 'شماره ملی',
    width: 110,
    dataIndex: EmsObjectName.lostCard.nationalId,
    id: EmsObjectName.lostCard.nationalId,
    filterable: true,
    filter: {
        style: 'direction: ltr;'
    },
//    style: "direction: ltr;",
    renderer: function (value, metaData) {
        metaData.tdCls += 'ems-direction-ltr';
        return value;
    }
},

            {
                xtype: 'gridcolumn',
                text: 'شماره کارت',
                width: 110,
                dataIndex: EmsObjectName.lostCard.crn,
                id: EmsObjectName.lostCard.crn,
                filterable: true,
                filter: {
                    style: 'direction: ltr;'
                },
//                style: "direction: ltr;",
                renderer: function (value, metaData) {
                    metaData.tdCls += 'ems-direction-ltr';
                    return value;
                }
            },
            
            {
                xtype: 'gam.datecolumn',
                width: 200,
                text: 'تاریخ گم شدن کارت',
                format: Ext.Date.defaultDateTimeFormat,
                id: EmsObjectName.lostCard.cardLostDate,
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
                dataIndex: EmsObjectName.lostCard.cardLostDate
            },
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'وضعیت تایید ',
                id: EmsObjectName.lostCard.isConfirm,
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
                dataIndex: EmsObjectName.lostCard.isConfirm,
                renderer: function (value, metaData) {
                	
                	
                	switch (value) {
					case '0':
						return 'تایید نشده';
						break;
					case '1':
						return 'تایید شده';
					default:
						//return 'تایید نشده';
						break;
					}
//                    metaData.tdCls += 'ems-direction-ltr';
//                    return value;
                }

            },

            this.LostCardActionColumn()
        ]);
    },
    LostCardActionColumn:function () {
        return({
            xtype: 'gam.actioncolumn',
            width: 100,
            text: 'عملیات',
            cls:'x-column-header-text',
            dataIndex: EmsObjectName.lostCard.isConfirm,
            id: EmsObjectName.lostCard.isConfirm,
            items:[
                {
                    getClass: function (value, metaData, record, rowIndex, colIndex, store) {

                        var confirmType = record.get( EmsObjectName.lostCard.isConfirm);
                        return( confirmType==0 ? 'girdAction-losttaeed-icon' : 'x-hide-display');
                    },
                    tooltip: 'تایید گمشدگی کارت',
                    action: 'confirmlostcard',
                    stateful: true,
                    stateId: this.stateId + 'LostCard'
                },
                {
                    getClass: function (value, metaData, record, rowIndex, colIndex, store) {

                        var confirmType = record.get( EmsObjectName.lostCard.isConfirm);
                        return( confirmType==0 ? 'girdAction-unconfirm-icon' : 'x-hide-display');
                    },
                    tooltip: 'عدم تایید گمشدگی کارت',
                    action: 'unconfirmLostCard',
                    stateful: true,
                    stateId: this.stateId + 'UnconfirmLostCard'
                }
            ]
                })
    }

});







