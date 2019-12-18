/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.controller.CardRequestListController', {
    extend: 'Gam.app.controller.RemoteDialogBasedGrid',

    ns: 'extJsController/cardrequestlist',
    bUrl: 'extJsController/findbatch',

    statics: {
        statefulComponents: [
            'wCardRequestListGrid',
            'wCardRequestListGridEdit'
        ]
    },
    requires: [
        'Ems.view.cardRequestList.OfficeAutoComplete'
    ],

    views: ['cardRequestList.Grid', 'cardRequestList.Dialog'],

    refs: [
        {
            ref: 'cardRequestHistoryGrid',
            selectors: 'cardrequesthistorygrid'
        },
        {
            ref: 'cardRequestPriorityCombo', selector: 'cardRequestPriorityCombo'
        }
    ],

    initViewType: 'cardrequestlistgrid',

    stores: ['CardRequestListStore', 'CardRequestSpouseStore', 'CardRequestChildStore'],

    //hossein 8 feature start
    spousesStore: Ext.create("Ems.store.CardRequestSpouseStore"),
    childrenStore: Ext.create("Ems.store.CardRequestChildStore"),
    //hossein 8 feature end

    constructor: function (config) {
        this.callParent(arguments);
    },

    init: function () { //
        sessionStorage.setItem('cardRequestId', null);
        this.control({
            "[action=exportExcel]": {
                click: function (btn) {
                    this.doExportExcel(btn);
                }
            },
            "[action=btnUpdatePriority]": {'click': this.getUpdatePriorityFrom, scope: this}

        });
        this.callParent(arguments);
    },

    doCardRequestHistory: function (grid, rowIndex) {

        var store = grid.getStore(),
            record = store.getAt(rowIndex),
            id = record.get('id');

        var win = Ext.create('Ems.view.cardRequestList.cardRequestHistory.Window',
            {
                width: 1000,
                autoScroll: true,
                title: "تاریخچه درخواست «" + record.get("citizenFirstName") + " " +
                    record.get("citizenSurname") + "» به شماره ملی " +
                    record.get("citizenNId")
            });
        Tools.MaskUnMask(win);
        var historyListGrid = win.down('grid');
        var me = this;

        if (id > 0) {
            var historyListStore = historyListGrid.getStore();
            if (historyListStore.readParams == null) {
                st.readParams({cardRequestId: id});
            } else {
                historyListStore.readParams.cardRequestId = id;
            }
            win.show();

        }

    },
    doShowRequestBatchId: function (grid, rowIndex) {
        sessionStorage.setItem('cardRequestId', null);
        var store = grid.getStore(),
            me = this;
        record = store.getAt(rowIndex),
            id = record.get('id');
        sessionStorage.setItem('cardRequestId', id);
        Ext.Ajax.request({
            url: me.bUrl + '/findBatchIdByCardRequestId',
            jsonData: {
                cardRequestId: id
            },
            success: function (resp) {
                var data = Ext.decode(resp.responseText);
                if (data.success) {
                    var rec = data.records;
                    if (rec != null) {
                        me.loadFormBatchIDView(rec[0], 'View');
                    } else {
                        Tools.errorMessageClient(Ems.ErrorCode.client.EMS_C_004);
                    }
                } else {
                    Tools.errorMessageServer(obj.messageInfo);
                }
            }, failure: function (resp) {
                Tools.errorFailure();
            }
        });
    },
    doChangePriority: function (grid, rowIndex) {	// khodayari
        sessionStorage.setItem('cardRequestId', null);
        var store = grid.getStore(),
            me = this;
        record = store.getAt(rowIndex),
            id = record.get('id');
        sessionStorage.setItem('cardRequestId', id);
        Ext.Ajax.request({
            url: me.ns + '/findCardRequestById',
            jsonData: {
                cardRequestId: id
            },
            success: function (resp) {
                var data = Ext.decode(resp.responseText);
                if (data.success) {
                    var rec = data.records;
                    if (rec != null) {
                        me.loadFormEditView(rec[0], 'View');
                    } else {
                        Tools.errorMessageClient(Ems.ErrorCode.client.EMS_C_004);
                    }
                } else {
                    Tools.errorMessageServer(obj.messageInfo);
                }
            }, failure: function (resp) {
                Tools.errorFailure();
            }
        });


    },
    doPrintRegistrationReceipt: function (grid, rowIndex) {
        var store = grid.getStore(),
            record = store.getAt(rowIndex);
        var reqId = record.get(EmsObjectName.userForm.id);
        Ext.Ajax.request({
            url: this.ns + '/canPrintRegistration', params: {
                cardRequestId: reqId
            }, success: function (resp) {
                var data = Ext.decode(resp.responseText);
                if (data.success) {
                    sessionStorage.setItem('cardRequestId', reqId);
                    var win = Ext.create('Ems.view.cardRequestList.printRegistrationReceipt.PrintRegistrationReceiptWindow');
                    printReceipt = win.down('.printRegistrationReceiptDialog');
                    printReceiptInfo = printReceipt.down('printRegistrationReceiptFieldSet');
                    Tools.MaskUnMask(win);
                    win.show();
                } else {
                    Tools.errorMessageServer(data.messageInfo)
                }
            }, failure: function () {
                Tools.errorFailure();
            }
        });

    },
    // doPrintRegistrationReceipt: function (grid, rowIndex) {
    //     var store = grid.getStore(),
    //         record = store.getAt(rowIndex);
    //     var reqId = record.get('id');
    //     Ext.Ajax.request({
    //         url: this.ns + '/canPrintRegistration',
    //         params: {
    //             cardRequestId: reqId
    //         },
    //         success: function (resp) {
    //             var data = Ext.decode(resp.responseText);
    //             if (data.success) {
    //                 sessionStorage.setItem('cardRequestId', reqId);
    //                 var win = Ext.create('Ems.view.cardRequestList.printRegistrationReceipt.PrintRegistrationReceiptWindow');
    //                 printReceipt = win.down('.printRegistrationReceiptDialog');
    //                 printReceiptInfo = printReceipt.down('printRegistrationReceiptFieldSet');
    //                 Tools.MaskUnMask(win);
    //                 win.show();
    //             } else {
    //                 Tools.errorMessageClient("چاپ رسید برای این شهروند مقدور نمی باشد");
    //                 //
    //             }
    //         }, failure: function (resp) {
    //             Tools.errorFailure();
    //         }
    //     });


    // sessionStorage.setItem('cardRequestId', null);
    // var store = grid.getStore(),
    //     me = this;
    // record = store.getAt(rowIndex),
    //     id = record.get('id');
    // Ext.Ajax.request({
    //     url: me.ns + '/printRegistrationReceipt',
    //     jsonData: {
    //         cardRequestId: id
    //     },
    //     success: function (resp) {
    //         var data = Ext.decode(resp.responseText);
    //         if (data.success) {
    //             var rec = data.records;
    //             if (rec != null) {
    //                 me.loadPrintView(rec[0], 'Print');
    //             } else {
    //                 Tools.errorMessageClient(Ems.ErrorCode.client.EMS_C_004);
    //             }
    //         } else {
    //             Tools.errorMessageServer(obj.messageInfo);
    //         }
    //     }, failure: function (resp) {
    //         Tools.errorFailure();
    //     }
    // });


 loadFormEditView: function (record, mode) {
        var win = null;

        if (mode === 'View') {
            win = Ext.create('Ems.view.cardRequestList.cardRequestPriority.CardRequestPriorityWindow');
            panel = win.down('.cardRequestPriorityDialog');
            panelInfo = panel.down('cardRequestPriorityFieldSet');
            Tools.MaskUnMask(win);
        }
        var store = Ext.create('Ems.store.CardRequestListStore', {baseUrl: this.ns});
        store.add(record);
        var rec = store.getAt(0);
        var cardPriority;

        if (rec.get(EmsObjectName.cardRequestList.priority)) {
            cardPriority = rec.get(EmsObjectName.cardRequestList.priority).toString();
        } else {
            cardPriority = '0';
        }

        var status = '';
        if (rec.get(EmsObjectName.cardRequestList.cardState) && typeof rec.get(EmsObjectName.cardRequestList.cardState) === 'string') {
            var resualt = eval("EmsResource.cardRequestList.CardRequestState." + rec.get(EmsObjectName.cardRequestList.cardState));
            resualt != null ? status = resualt : status = value;
        }


        panelInfo.setData(Ext.create('Ems.model.CardRequestListModel', {
            citizenFirstName: rec.get(EmsObjectName.cardRequestList.citizenFirstName),
            citizenSurname: rec.get(EmsObjectName.cardRequestList.citizenSurname),
            cardState: status,
            priority: cardPriority,
            citizenNId: rec.get(EmsObjectName.cardRequestList.citizenNId)

        }), panelInfo);
        win.show();
    },
    loadFormBatchIDView: function (record, mode) {
        var win = null;

        if (mode === 'View') {
            win = Ext.create('Ems.view.cardRequestList.showRequestBatchId.ShowRequestBatchIdWindow');
            panel = win.down('.showRequestBatchIdDialog');
            panelInfo = panel.down('showRequestBatchIdFieldSet');
            Tools.MaskUnMask(win);
        }
        var store = Ext.create('Ems.store.BatchListStore', {baseUrl: this.ns});
        store.add(record);
        var rec = store.getAt(0);
        var batchCmsId;

        if (rec.get(EmsObjectName.BatchList.cmsID)) {
            batchCmsId = rec.get(EmsObjectName.BatchList.cmsID).toString();
        } else {
            batchCmsId = Ems.ErrorCode.client.EMS_C_004;
        }
        panelInfo.setData(Ext.create('Ems.model.BatchListModel', {
            cmsID: batchCmsId
        }), panelInfo);
        win.show();
    }
    ,
    loadPrintView: function (record, mode) {
        var win = null;
        if (mode === 'Print') {
            win = Ext.create('Ems.view.cardRequestList.printRegistrationReceipt.PrintRegistrationReceiptWindow');
            printReceipt = win.down('.printRegistrationReceiptDialog');
            printReceiptInfo = printReceipt.down('printRegistrationReceiptFieldSet');
            Tools.MaskUnMask(win);
        }
        var store = Ext.create('Ems.store.CardRequestListStore', {baseUrl: this.ns});
        store.add(record);
        var rec = store.getAt(0);
        // printReceiptInfo.setData(Ext.create('Ems.model.CardRequestListModel', {
        //     citizenFirstName: rec.get(EmsObjectName.cardRequestList.citizenFirstName),
        //     citizenSurname: rec.get(EmsObjectName.cardRequestList.citizenSurname),
        //     fatherName: rec.get(EmsObjectName.cardRequestList.fatherName),
        //     citizenNId: rec.get(EmsObjectName.cardRequestList.citizenNId),
        //     birthCertId: rec.get(EmsObjectName.cardRequestList.birthCertId),
        //     citizenBirthDate: rec.get(EmsObjectName.cardRequestList.citizenBirthDate),
        //     enrolledDate: rec.get(EmsObjectName.cardRequestList.enrolledDate),
        //     trackingId: rec.get(EmsObjectName.cardRequestList.trackingId),
        //     userFirstName: rec.get(EmsObjectName.cardRequestList.userFirstName),
        //     userLastName: rec.get(EmsObjectName.cardRequestList.userLastName),
        //     receiptDate: rec.get(EmsObjectName.cardRequestList.receiptDate)
        // }), printReceiptInfo);
        win.show();
    }
    ,
    getUpdatePriorityFrom: function (btn) {

        var me = this;
        var grid = Ext.getCmp('cardrequestlistgrid');
        Gam.Msg.showWaitMsg();

        Ext.Ajax.request({

            url: me.ns + '/updateCardRequestPriority', jsonData: {

                priority: parseInt(Ext.getCmp('cardRequestPriorityCombo').rawValue),
                cardRequestId: sessionStorage.getItem('cardRequestId'),
                oldPriority: record.get('priority')
            },
            success: function (response) {
                Gam.Msg.hideWaitMsg();
                if (response.status == "200") {
//	    				   win.close();
                    btn.up('window').close();
                    grid.getStore().load();
                    sessionStorage.setItem('cardRequestId', null);
                } else {
                    Gam.Msg.hideWaitMsg();
                    Tools.errorMessageClient(response.statusText);
                }
            },
            failure: function (resp) {
                Gam.Msg.showWaitMsg();
                Tools.errorFailure();
            }
        });
    }
    ,
    /**
     * Handler of repeal request action
     * @param grid      Card requests grid
     * @param rowIndex  Index of the item which its repeal action has been selected
     */
    doRepealCardRequest: function (grid, rowIndex) {
        this.repealCardRequest(grid, rowIndex);
    }
    ,

    /**
     * Handler of reject repeal request action. It would be called when the 3S user rejects EOF request for repealing
     * a request
     *
     * @param grid      Card requests grid
     * @param rowIndex  Index of the item which its reject repeal action has been selected
     */
    doRejectRepealCardRequest: function (grid, rowIndex) {

        //  This is same as undoRepealCardRequest, so call the same service
        this.doUndoRepealCardRequest(grid, rowIndex);
    }
    ,

    /**
     * Handler of accept repeal request action. It would be called when the 3S user accepts EOF request for repealing
     * a request
     *
     * @param grid      Card requests grid
     * @param rowIndex  Index of the item which its repeal action has been selected
     */
    doAcceptRepealCardRequest: function (grid, rowIndex) {
        this.repealCardRequest(grid, rowIndex);
    }
    ,

    /**
     * Handler of undo repeal request action.
     *
     * @param grid      Card requests grid
     * @param rowIndex  Index of the item which its repeal action has been selected
     */
    doUndoRepealCardRequest: function (grid, rowIndex) {
        //  Displaying a confirmation dialogue to user
        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var message = 'آیا از بازیابی درخواست ابطال <b>{0}</b> <b>{1}</b> به شماره ملی <b>{2}</b> اطمینان دارید؟';
        message = message.replace('{0}', record.get(EmsObjectName.cardRequestList.citizenFirstName));
        message = message.replace('{1}', record.get(EmsObjectName.cardRequestList.citizenSurname));
        message = message.replace('{2}', record.get(EmsObjectName.cardRequestList.citizenNId));
        Tools.messageBoxConfirm(message, Ext.bind(this.sendRepealCardRequest, this, [grid, record.get("id"), 'REPEAL_UNDO']));
    }
    ,

    /**
     * General handler for 'accept repeal request' and 'repeal request' actions.
     *
     * @param grid          Card requests grid
     * @param rowIndex  Index of the item which its repeal action has been selected
     */
    repealCardRequest: function (grid, rowIndex) {
        //  Displaying a confirmation dialogue to user
        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var message = 'آیا از ابطال درخواست <b>{0}</b> <b>{1}</b> به شماره ملی <b>{2}</b> اطمینان دارید؟';
        message = message.replace('{0}', record.get(EmsObjectName.cardRequestList.citizenFirstName));
        message = message.replace('{1}', record.get(EmsObjectName.cardRequestList.citizenSurname));
        message = message.replace('{2}', record.get(EmsObjectName.cardRequestList.citizenNId));
        Tools.messageBoxConfirm(message, Ext.bind(this.sendRepealCardRequest, this, [grid, record.get("id"), 'REPEAL_ACCEPTED']));
    }
    ,

    /**
     * Callback method to be called after user confirmation for repealing a card request
     * @param grid              Card requests grid
     * @param cardRequestID     Identifier of the card request to be repealed
     * @param repealAction      The action parameter to be sent to repeal service
     */
    sendRepealCardRequest: function (grid, cardRequestID, repealAction) {
        Gam.Msg.showWaitMsg();
        Ext.Ajax.request({
            url: this.ns + '/repealCardRequest',
            jsonData: {
                cardRequestId: cardRequestID,
                cardRequestAction: repealAction
            },
            success: function (response) {
                {
                    Gam.Msg.hideWaitMsg();
                    try {
                        var obj = Ext.decode(response.responseText);
                        if (obj.success) {
                            grid.getStore().load();
                        } else {
                            Tools.errorMessageServer(obj.messageInfo)
                        }
                    } catch (e) {
                        alert(e.message);
                    }
                }
            },
            failure: function (resop) {
                Gam.Msg.hideWaitMsg();
                Tools.errorFailure();
            }
        });
    }
    ,

    doExportExcel: function (btn) {
        var grid = btn.up('toolbar').up('grid');
        var store = grid.getStore();

        var listName = store.readParams.listName;
        var fields = store.readParams.fields;
        var filters = '{}';
        try {
            filters = store.proxy.encodeFilters(store.filters.items);
        } catch (e) {
        }
        var sorter = '{}';
        try {
            sorter = store.proxy.encodeSorters(store.sorters.items);
        } catch (e) {
        }

        var config = {
            url: 'extJsController/exportList/query',
            params: {
                listName: listName,
                fields: fields,
                filter: filters,
                page: 1,
                start: 0,
                limit: 10000,
                sort: sorter
            }
        }
        Gam.util.PopupWindow.open(config);
    }
    ,


    /**
     * Fetches detailed information about selected card request from server to be displayed in a popup dialog
     */
    loadFormViaServer: function (view, record, baseUrl, params) {
        var me = this,
            formPanel = view.down('form');

        params = params || {};
        Ext.apply(params, {
            readOnly: view.isReadOnly(),
            cardRequestId: record.get('id')
        });

        formPanel.load({
            url: (baseUrl || me.ns) + '/' + Gam.GlobalConfiguration.CONTROLLER_ACTIONS.LOAD_BY_ID,
            params: params,
            success: function (form, action) {

                //  Overriding the value of delivery office from the grid record
                if (action.result.data.deliveredOfficeName) {
                    var cardRequestInfo = Ext.create('Ems.model.CardRequestListModel', action.result.data);
                    cardRequestInfo.set('deliveredOfficeName', record.get('deliveredOfficeName'));
                    form.loadRecord(cardRequestInfo);
                }

                var citizenInfo = Ext.create('Ems.model.CitizenInfoModel', action.result.data.citizenInfo);
                form.loadRecord(citizenInfo);

                this.spousesStore.loadData(action.result.data.spouses);
                this.childrenStore.loadData(action.result.data.children);
            },
            failure: function (formPanel, formBasic) {
                //  TODO: This failure handler is an overridden version of gam-all version to process result. this
                //  should be refactored to move such logic to a parent class
                var buildMessage = function (messageInfo, success) {
                    var message,
                        messageKey = messageInfo.message,
                        messageKeyParts;

                    if (!messageKey) {
                        return success ? Gam.Resource.message.info.operation : Gam.Resource.message.failure.operation;
                    }

                    messageKeyParts = messageKey.split('.');
                    if (!Ext.isArray(messageKeyParts) || messageKeyParts.length <= 2) {
                        return messageKey;
                    }

                    message = window[messageKeyParts[0]];
                    if (Ext.isDefined(message)) {
                        messageKeyParts = Ext.Array.erase(messageKeyParts, 0, 1);
                    }
                    Ext.each(messageKeyParts, function (subKey) {
                        message = message[subKey];

                        return Ext.isDefined(message);
                    });

                    return message;
                };

                var res = formBasic.response.responseJSON,
                    messageInfo = res.messageInfo,
                    message;

                if (!messageInfo) {
                    return;
                }

                message = buildMessage(messageInfo, res.success);
                if (messageInfo.arguments) {
                    message = Ext.String.format.apply(this, Ext.Array.merge(message, messageInfo.arguments));
                }

                messageInfo.message = message;
                messageInfo.icon = messageInfo.icon.toUpperCase();
                if (!messageInfo.autoShow) {
                    return;
                }

                if (messageInfo.manner == Gam.GlobalConfiguration.MESSAGE.MANNERS.NOTIFICATION) {
                    Gam.Msg.autoHideNotify(message, Gam.window.Notification[messageInfo.icon]);
                } else {
                    Gam.Msg['show' + messageInfo.msgMethodPart + 'Msg'](message);
                }

                this.getDialog().close();
            },
            waitMsg: Gam.Resource.message.info.formLoading,
            scope: me
        });
    }


})
;

