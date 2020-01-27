//khodayari
Ext.define('Ems.view.tokenRequestList.Grid', {
    extend: 'Gam.grid.Crud',
    alias: 'widget.tokengrid',
    //stateId: 'wTokenRequestListGrid',
    title: 'درخواست های توکن',
    multiSelect: false,

    requires: ['Ems.store.TokenRequestListStore'],

    store: {type: 'tokenrequestliststore'},

    actions: ['gam.add', 'gam.delete'],

    initComponent: function () {

        this.columns = this.getItemsGridForm();
        this.callParent(arguments);
    },
    actionColumnItems: [
        {
            icon: 'resources/themes/images/default/shared/forbidden.png',
            tooltip: 'حذف',
            action: 'rejectRequest',
            stateful: true,
            stateId: this.stateId + 'RejectToken',
            getClass: function (value, metadata, record) {

                var tokenStatus = record.get(EmsObjectName.tokenRequest.tokenState);
                return (tokenStatus === 'PENDING_FOR_EMS' ? 'girdAction-reject-ict-icon' : 'x-hide-display');
            }
        },
        {
            // icon:'resources/themes/images/TokenIcon/new.png',
            tooltip: 'تایید  توکن ',
            action: 'approveRequest',
            stateful: true,
            stateId: this.stateId + 'approveToken',
            getClass: function (value, metadata, record) {
                var status = record.get(EmsObjectName.tokenRequest.tokenState);
                return ((status === 'PENDING_FOR_EMS') || (status === 'EMS_REJECT') ? 'girdAction-taeid-ict-icon' : 'x-hide-display');
            }
        },
        {

            //icon:'resources/themes/images/TokenIcon/tahvil.gif',
            tooltip: 'تحویل توکن',
            action: 'deliverToken',
            stateful: true,
            stateId: this.stateId + 'deliverToken',
            getClass: function (value, metadata, record) {
                var status = record.get(EmsObjectName.tokenRequest.tokenState);
                return (status === 'READY_TO_RENEWAL_DELIVER' ? 'girdAction-tahvil-ict-icon' : 'x-hide-display');
            }
        },
        {
            // icon:'resources/themes/images/TokenIcon/new.png',
            tooltip: 'فعال سازی ',
            action: 'activateRequest',
            stateful: true,
            stateId: this.stateId + 'activateToken',
            getClass: function (value, metadata, record) {
                var status = record.get(EmsObjectName.tokenRequest.tokenState);
                return ((status === 'SUSPENDED') ? 'girdAction-faalsazi-ict-icon' : 'x-hide-display');
            }
        }],

    getItemsGridForm: function () {
        return [
            {
                //	xtype: 'gridcolumn',
                filterable: true,
                filter: true,
                dataIndex: EmsObjectName.tokenRequest.adminName,
                id: EmsObjectName.tokenRequest.adminName,
                text: 'نام مدیر',
                width: 100
            },
            {
                //	xtype: 'gridcolumn',
                filterable: true,
                filter: true,
                dataIndex: EmsObjectName.tokenRequest.departmentName,
                id: EmsObjectName.tokenRequest.departmentName,
                text: 'نام دفتر',
                width: 100
            },
            {
                //	xtype: 'gridcolumn',
                filterable: true,
                filter: {
                    xtype: 'combo',
                    store: {
                        fields: ['value', 'label'],
                        data: [
                            {
                                value: 'SUSPENDED',
                                label: "آماده فعالسازی"
                            },
                            {
                                value: 'PENDING_FOR_EMS',
                                label: "منتظر تایید EMS"
                            },
                            {
                                value: 'EMS_REJECT',
                                label: "تایید نشده"
                            },
                            {
                                value: 'READY_TO_RENEWAL_DELIVER',
                                label: "آماده تحویل"
                            }
                        ]
                    },
                    queryMode: 'local',
                    displayField: 'label',
                    valueField: 'value'
                },
                dataIndex: EmsObjectName.tokenRequest.tokenState,
                id: EmsObjectName.tokenRequest.tokenState,
                text: 'وضعیت',
                renderer: function (value) {
                    //'PENDING_FOR_EMS', 'EMS_REJECT', 'SUSPENDED', 'READY_TO_RENEWAL_DELIVER'
                    var result = "";
                    if (value == "SUSPENDED") {
                        result = "آماده فعالسازی";
                    } else if (value == "PENDING_FOR_EMS") {
                        result = "منتظر تایید EMS";
                    } else if (value == "EMS_REJECT") {
                        result = "تایید نشده";
                    }
                    else if (value == "READY_TO_RENEWAL_DELIVER") {
                        result = "آماده تحویل";
                    }
                    return result;
                },
                width: 100
            },
            {
                filterable: true,
                filter: {
                    xtype: 'combo',
                    store: {
                        fields: ['value', 'label'],
                        data: [
                            {
                                value: 'FIRST_TOKEN',
                                label: "صدور اولیه"
                            },
                            {
                                value: 'REPLICA',
                                label: "صدورالمثنی"
                            },
                            {
                                value: 'REPLACED',
                                label: "صدور مجدد به دلیل خرابی"
                            }
                        ]
                    },
                    queryMode: 'local',
                    displayField: 'label',
                    valueField: 'value'
                },
                dataIndex: EmsObjectName.tokenRequest.tokenReason,
                id: EmsObjectName.tokenRequest.tokenReason,
                text: ' دلیل صدور توکن ',
                renderer: function (value) {
                    if (value && typeof value === 'string') {
                        switch (value) {
                            case "FIRST_TOKEN":
                                return 'صدور اولیه';
                                break;
                            case "REPLICA":
                                return 'صدورالمثنی';
                                break;
                            case "REPLACED":
                                return 'صدور مجدد به دلیل خرابی';
                                break;
                            default :
                                return 'نا مشخص';
                                break;
                        }
                    }
                },
                width:150
            },
            {
                //	xtype: 'gridcolumn',
                filterable: true,
                filter: true,
                dataIndex: EmsObjectName.tokenRequest.tokenType,
                id: EmsObjectName.tokenRequest.tokenType,
                text: 'نوع',
                width: 100
            },

            {
                xtype: 'gam.datecolumn',
                dataIndex: EmsObjectName.tokenRequest.issuanceDate,
                id: EmsObjectName.tokenRequest.issuanceDate,
                text: 'تاریخ صدور',
                width: 100,
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
                xtype: 'gam.datecolumn',
                dataIndex: EmsObjectName.tokenRequest.requestDate,
                id: EmsObjectName.tokenRequest.requestDate,
                text: 'تاریخ درخواست',
                width: 100,
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
                xtype: 'gam.datecolumn',
                dataIndex: EmsObjectName.tokenRequest.deliverDate,
                id: EmsObjectName.tokenRequest.deliverDate,
                text: 'تاریخ تحویل',
                width: 100,
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
            }
        ];
    }

});
