/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 12:06 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.cardRequestList.Grid', {

    extend: 'Gam.grid.Crud',

    alias: 'widget.cardrequestlistgrid',

    stateId: 'wCardRequestListGrid',

    title: 'مدیریت درخواستها',

    multiSelect: false,

    storeAutoLoad: false,

    requires: [
        'Ems.store.CardRequestListStore',
        'Ems.view.cardRequestList.combo.CardRequestStateComboBox',
        'Ems.view.cardRequestList.combo.CardStateComboBox',
        'Ems.view.cardRequestList.combo.CardRequestTypeComboBox',
        'Ems.view.cardRequestList.combo.CardRequestFlagComboBox'
    ],

    store: {type: 'cardrequestliststore'},

    actionColumnItems: [
        {
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                return 'grid-info-action-icon';
            },
            tooltip: 'مشاهده جزییات درخواست',
            action: 'view',
            viewType: 'cardrequestlistdialog'
        },
        {
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                return 'grid-view-action-icon';
            },
            tooltip: 'مشاهده تاریخچه',
            action: 'cardRequestHistory'
        },
        {
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                if (EmsObjectName.cardRequestedActionMap.hasAccessToChangePriority) {
                    var cardState = record.get(EmsObjectName.cardRequestList.cardRequestState);

                    if (cardState != 'PENDING_ISSUANCE'
                        && cardState != 'ISSUED'
                        && cardState != 'READY_TO_DELIVER'
                        && cardState != 'PENDING_TO_DELIVER_BY_CMS'
                        && cardState != 'DELIVERED'
                        && cardState != 'NOT_DELIVERED'
                        && cardState != 'STOPPED'
                        && cardState != 'REPEALED'
                        && cardState != 'CMS_ERROR'
                        && cardState != 'CMS_PRODUCTION_ERROR') {

                        return 'grid-change-priority';
                    } else
                        return 'grid-action-hidden';
                } else
                    return 'grid-action-hidden';
            },
            tooltip: 'تغییر اولویت',
            action: 'changePriority'
        },
        {
            tooltip: 'ابطال درخواست',
            action: 'repealCardRequest',
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                //  Display repeal request action only if request has not reached AFIS system
                var state = record.get("cardRequestState");
                var requestedAction = record.get("requestedAction");

                if (((state == "REGISTERED") || (state == "RECEIVED_BY_EMS") || (state == "PENDING_IMS") ||
                    (state == "VERIFIED_IMS") || (state == "NOT_VERIFIED_BY_IMS") || (state == "RESERVED") ||
                    (state == "REFERRED_TO_CCOS") || (state == "DOCUMENT_AUTHENTICATED") || (state == "APPROVED")) &&
                    ((requestedAction != EmsObjectName.cardRequestedActionMap.REPEALING) &&
                        (requestedAction != EmsObjectName.cardRequestedActionMap.REPEAL_ACCEPTED))) {
                    return 'grid-repeal-request-action-icon';
                }

                return 'grid-action-hidden';
            }
        },
        {
            tooltip: ' تایید درخواست ابطال دفتر',
            action: 'acceptRepealCardRequest',
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                //  Display repeal request approve action only if repeal request has been sent from office
                var state = record.get("cardRequestState");
                var requestedAction = record.get("requestedAction");

                if (((state == "REGISTERED") || (state == "RECEIVED_BY_EMS") || (state == "PENDING_IMS") ||
                    (state == "VERIFIED_IMS") || (state == "NOT_VERIFIED_BY_IMS") || (state == "RESERVED") ||
                    (state == "REFERRED_TO_CCOS") || (state == "DOCUMENT_AUTHENTICATED") || (state == "APPROVED")) &&
                    ((requestedAction == EmsObjectName.cardRequestedActionMap.REPEALING) &&
                        (requestedAction != EmsObjectName.cardRequestedActionMap.REPEAL_ACCEPTED))) {
                    return 'grid-Checked-action-icon';
                }

                return 'grid-action-hidden';
            }
        },
        {
            tooltip: 'عدم تایید درخواست ابطال دفتر',
            action: 'rejectRepealCardRequest',
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                //  Display repeal request approve action only if repeal request has been sent from office
                var state = record.get("cardRequestState");
                var requestedAction = record.get("requestedAction");

                if (((state == "REGISTERED") || (state == "RECEIVED_BY_EMS") || (state == "PENDING_IMS") ||
                    (state == "VERIFIED_IMS") || (state == "NOT_VERIFIED_BY_IMS") || (state == "RESERVED") ||
                    (state == "REFERRED_TO_CCOS") || (state == "DOCUMENT_AUTHENTICATED") || (state == "APPROVED")) &&
                    (requestedAction == EmsObjectName.cardRequestedActionMap.REPEALING)) {
                    return 'grid-reject-repeal-request-action-icon';
                }

                return 'grid-action-hidden';
            }
        },
        {
            tooltip: 'بازیابی درخواست ابطال',
            action: 'undoRepealCardRequest',
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                //  Display repeal request undo action only if repeal request has been accepted in 3S
                var state = record.get("requestedAction");
                if ((state == EmsObjectName.cardRequestedActionMap.REPEAL_ACCEPTED)) {
                    return 'grid-undo-repeal-request-action-icon';
                }

                return 'grid-action-hidden';
            }
        },
        {
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {

                if (EmsObjectName.cardRequestedActionMap.hasPrintRegistrationReceipt) {
                    return 'grid-print-registration-receipt';
                    // var cardState = record.get(EmsObjectName.cardRequestList.cardRequestState);
                //
                //     if (cardState != 'IMS_ERROR') {
                //         return 'grid-print-registration-receipt';
                // } else
                //     return 'grid-action-hidden';
                } else
                    return 'grid-action-hidden';
            },
            tooltip: 'چاپ رسید',
            action: 'printRegistrationReceipt'
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
    initComponent: function () {
        this.columns = this.getItemsGridForm();
        this.callParent(arguments);
    },

    getItemsGridForm: function () {
        var name;
        return ([
            {
                dataIndex: EmsObjectName.cardRequestList.citizenFirstName,
                id: EmsObjectName.cardRequestList.citizenFirstName,
                text: 'نام',
                filterable: true,
                filter: true,
                sortable: false,
            },

            {
                dataIndex: EmsObjectName.cardRequestList.citizenSurname,
                id: EmsObjectName.cardRequestList.citizenSurname,
                text: 'نام خانوادگی',
                filterable: true,
                filter: true,
                sortable: false,
            },
            {
                dataIndex: EmsObjectName.cardRequestList.citizenNId,
                id: EmsObjectName.cardRequestList.citizenNId,
                text: 'کد ملی',
                filterable: true,
                sortable: false,
                filter: {
                    xtype: 'textfield',
                    vtype: 'numeric',
                    enforceMaxLength: true,
                    maxLength: 10
                }
            },
            {
                xtype: 'gridcolumn',
                width: 200,
                dataIndex: EmsObjectName.cardRequestList.enrollmentOfficeName,
                id: EmsObjectName.cardRequestList.enrollmentOfficeName,
                text: 'اداره ثبت',
                filterable: true,
                sortable: false,
                filter: true,
                filter: {
                    xtype: 'officeautocomplete'
                    , hiddenName: EmsObjectName.cardRequestList.enrollmentOfficeId
//		                                                    

                }
            },

//	                                    	          {
//	                                    	        	  dataIndex: EmsObjectName.cardRequestList.enrollmentOfficeName,
//	                                    	        	  id: EmsObjectName.cardRequestList.enrollmentOfficeName,
//	                                    	        	  text: 'اداره ثبت',
//	                                    	        	  filterable: true,
//	                                    	        	  filter: true
//	                                    	          },
            {
                xtype: 'gam.datecolumn',
                dataIndex: EmsObjectName.cardRequestList.enrolledDate,
                id: EmsObjectName.cardRequestList.enrolledDate,
                text: 'تاریخ تکمیل ثبت نام',
                width: 200,
                format: Ext.Date.defaultDateTimeFormat,
                filterable: true,
                sortable: false,
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
                    }, items: [
                        {
                            fieldLabel: 'از',
                            name: 'fromEnrolledDate'
                        },
                        {
                            fieldLabel: 'تا',
                            name: 'toEnrolledDate'
                        }
                    ]
                }
            },
            {
                xtype: 'gam.datecolumn',
                dataIndex: EmsObjectName.cardRequestList.portalEnrolledDate,
                id: EmsObjectName.cardRequestList.portalEnrolledDate,
                text: 'تاریخ پیش ثبت نام',
                width: 200,
                format: Ext.Date.defaultDateTimeFormat,
                sortable: false,
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
                    }, items: [
                        {
                            fieldLabel: 'از',
                            name: 'fromPortalEnrolledDate'
                        },
                        {
                            fieldLabel: 'تا',
                            name: 'toPortalEnrolledDate'
                        }
                    ]
                }
            },
            {
                xtype: 'gam.datecolumn',
                dataIndex: EmsObjectName.cardRequestList.deliveredDate,
                id: EmsObjectName.cardRequestList.deliveredDate,
                text: 'تاریخ تحویل کارت',
                sortable: false,
                width: 200,
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
                    }, items: [
                        {
                            fieldLabel: 'از',
                            name: 'fromDeliveredDate'
                        },
                        {
                            fieldLabel: 'تا',
                            name: 'toDeliveredDate'
                        }
                    ]
                }
            },

            {
                xtype: 'gam.datecolumn',
                dataIndex: EmsObjectName.cardRequestList.reservationDate,
                id: EmsObjectName.cardRequestList.reservationDate,
                text: 'تاریخ نوبت گیری',
                sortable: false,
                width: 200,
                //format: Ext.Date.defaultDateTimeFormat,
                format: 'Y/m/d',
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
                    }, items: [
                        {
                            fieldLabel: 'از',
                            name: 'reservationDateFrom'
                        },
                        {
                            fieldLabel: 'تا',
                            name: 'reservationDateTo'
                        }
                    ]
                }
            },
            {
                dataIndex: EmsObjectName.cardRequestList.cardRequestState,
                id: EmsObjectName.cardRequestList.cardRequestState,
                text: 'وضعیت درخواست کارت',
                sortable: false,
                width: 300,
                renderer: function (value) {

                    if (value && typeof value === 'string') {
                        var resualt = eval("EmsResource.cardRequestList.CardRequestState." + value);
                        return resualt != null ? resualt : value
                    }
                },
                filterable: true,
                filter: {
                    xtype: 'cardrequeststatecombobox'
                }
            },
            {
                dataIndex: EmsObjectName.cardRequestList.trackingId,
                id: EmsObjectName.cardRequestList.trackingId,
                text: 'کد پیگیری',
                sortable: false,
                filterable: true,
                filter: true
            },
            {
                dataIndex: EmsObjectName.cardRequestList.cardType,
                id: EmsObjectName.cardRequestList.cardType,
                text: 'نوع کارت',
                renderer: function (value) {
                    if (value && typeof value === 'string') {
                        var resualt = eval("EmsResource.cardRequestList.CardRequestType." + value);
                        return resualt != null ? resualt : value;
                    }
                },
                filterable: true,
                sortable: false,
                filter: {
                    xtype: 'cardrequesttypecombobox'
                }
            },
            {
                dataIndex: EmsObjectName.cardRequestList.cardState,
                id: EmsObjectName.cardRequestList.cardState,
                text: 'وضعیت کارت',
                filterable: true,
                sortable: false,
                filter: {
                    xtype: 'cardstatecombobox'
                },
                renderer: function (value) {
                    if (value && typeof value === 'string') {
                        return eval("EmsResource.cardRequestList.CardState." + value);
                    }

                    return value;
                }
            },
            {
                dataIndex: EmsObjectName.cardRequestList.requestedAction,
                id: EmsObjectName.cardRequestList.requestedAction,
                text: 'وضعیت درخواست ابطال',
                sortable: false,
                filterable: true,
                filter: {
                    xtype: 'combo',
                    store: {
                        fields: ['value', 'label'],
                        data: [
                            {
                                value: 'REPEALING',
                                label: 'منتظر تایید ابطال درخواست'
                            },
                            {
                                value: 'REPEAL_ACCEPTED',
                                label: 'آماده ابطال درخواست'
                            },
                            {
                                value: 'READY_TO_PURGE',
                                label: 'آماده بایگانی اطلاعات درخواست'
                            }
                        ]
                    },
                    queryMode: 'local',
                    displayField: 'label',
                    valueField: 'value'
                },
                hidden: true,
                width: 200,
                renderer: function (value) {
                    var result = value;
                    if (value == "REPEALING") {
                        result = "منتظر تایید ابطال درخواست";
                    } else if (value == "REPEAL_ACCEPTED") {
                        result = "آماده ابطال درخواست";
                    }

                    return result;
                }
            },
            {
                dataIndex: EmsObjectName.cardRequestList.requestOrigin,
                id: EmsObjectName.cardRequestList.requestOrigin,
                text: 'نحوه ثبت نام',
                renderer: function (value) {
                    var result = "";
                    if (value == "P") {
                        result = "پورتال";
                    } else if (value == "C") {
                        result = "دفتر پیشخوان";
                    } else if (value == "M") {
                        result = "ثبت نام سیار";
                    } else if (value == "V") {
                        result = "وی آی پی	";
                    }

                    return result;
                }
            },
            {

                xtype: 'gridcolumn',
                width: 200,
                dataIndex: EmsObjectName.cardRequestList.deliveredOfficeName,
                id: EmsObjectName.cardRequestList.deliveredOfficeName,
                text: 'دفتر پیشخوان تحویل کارت',
                filterable: true,
                sortable: false,
                filter: true,
                filter: {
                    xtype: 'officeautocomplete',
                    listeners: {
                        focus: function (autocomplete, e) {
                            autocomplete.getStore().readParams.additionalParams =
                                Ext.JSON.encode({'type': 'delivery'});
                        }
                    }
                }
            }



//	                                    	        	  dataIndex: EmsObjectName.cardRequestList.deliveredOfficeName,
//	                                    	        	  id: EmsObjectName.cardRequestList.deliveredOfficeName,
//	                                    	        	  text: 'دفتر پیشخوان تحویل کارت',
//	                                    	        	  filterable: true,
//	                                    	        	  filter: true
            ,
            {
                dataIndex: EmsObjectName.cardRequestList.documentFlag,
                id: EmsObjectName.cardRequestList.documentFlag,
                text: 'اسکن مدارک',
                sortable: false,
                filterable: true,
                filter: {
                    xtype: 'cardrequestflagcombobox'
                },
                renderer: function (value) {
                    var result = "";
                    if (value == true) {
                        result = "دارد";
                    } else {
                        result = "ندارد";
                    }

                    return result;
                }
            },
            {
                dataIndex: EmsObjectName.cardRequestList.faceFlag,
                id: EmsObjectName.cardRequestList.faceFlag,
                text: 'تصویر چهره',
                sortable: false,
                filterable: true,
                filter: {
                    xtype: 'cardrequestflagcombobox'
                },
                renderer: function (value) {
                    var result = "";
                    if (value == true) {
                        result = "دارد";
                    } else {
                        result = "ندارد";
                    }

                    return result;
                }
            },
            {
                dataIndex: EmsObjectName.cardRequestList.priority,
                id: EmsObjectName.cardRequestList.priority,
                text: 'اولویت',
                sortable: false,
                filterable: false,
                filter: false
            },
            {
                dataIndex: EmsObjectName.cardRequestList.fingerFlag,
                id: EmsObjectName.cardRequestList.fingerFlag,
                text: 'اثر انگشت',
                sortable: false,
                filterable: true,
                filter: {
                    xtype: 'cardrequestflagcombobox'
                },
                renderer: function (value) {
                    var result = "";
                    if (value == true) {
                        result = "دارد";
                    } else {
                        result = "ندارد";
                    }

                    return result;
                }
            }
        ]);
    }
});

