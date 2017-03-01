/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.controller.CmsErrorEvaluateListController', {
    extend: 'Gam.app.controller.LocalDialogBasedGrid',

    ns: 'extJsController/cmserrorevaluatelist',

    statics: {
        statefulComponents: [
            'wCmsErrorEvaluateListGrid',
        ]
    },

    views: ['cmserrorevaluateList.Grid'],

  

	    initViewType : 'cmserrorevaluatelistgrid',

    stores: ['CmsErrorEvaluateListStore'],

    constructor: function (config) {

        this.callParent(arguments);
    },

    
    
    doRepeal: function (grid, rowIndex) {
        //  Displaying a confirmation dialogue to user
        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var message = 'آیا از ابطال درخواست <b>{0}</b> <b>{1}</b> به شماره ملی <b>{2}</b> اطمینان دارید؟';
        message = message.replace('{0}', record.get(EmsObjectName.cardRequestList.citizenFirstName));
        message = message.replace('{1}', record.get(EmsObjectName.cardRequestList.citizenSurname));
        message = message.replace('{2}', record.get(EmsObjectName.cardRequestList.citizenNId));
        Tools.messageBoxConfirm(message, Ext.bind(this.repeal, this, [grid, record]));
    },
    
    
    
    doCMSRetry: function (grid, rowIndex) {
        //  Displaying a confirmation dialogue to user
        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var message = 'آیا از صدور مجدد درخواست <b>{0}</b> <b>{1}</b> به شماره ملی <b>{2}</b> اطمینان دارید؟';
        message = message.replace('{0}', record.get(EmsObjectName.cardRequestList.citizenFirstName));
        message = message.replace('{1}', record.get(EmsObjectName.cardRequestList.citizenSurname));
        message = message.replace('{2}', record.get(EmsObjectName.cardRequestList.citizenNId));
        Tools.messageBoxConfirm(message, Ext.bind(this.retry, this, [grid, record]));
    },
    
    
    doDeleteImage: function (grid, rowIndex) {
        //  Displaying a confirmation dialogue to user
        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var message = 'آیا از حذف تصاویر مربوط به درخواست <b>{0}</b> <b>{1}</b> به شماره ملی <b>{2}</b> اطمینان دارید؟';
        message = message.replace('{0}', record.get(EmsObjectName.cardRequestList.citizenFirstName));
        message = message.replace('{1}', record.get(EmsObjectName.cardRequestList.citizenSurname));
        message = message.replace('{2}', record.get(EmsObjectName.cardRequestList.citizenNId));
        Tools.messageBoxConfirm(message, Ext.bind(this.deleteImageAction, this, [grid, record]));
    },
    
    
    
    repeal: function (grid, record) {
    	
        Gam.Msg.showWaitMsg();
        Ext.Ajax.request({
            url: this.ns + '/doRepealAction',
            jsonData: {
            	record: record.data,
            },
            success: function (response) {
                {
                    Gam.Msg.hideWaitMsg();
                    try {
                        var obj = Ext.decode(response.responseText);
                        if (obj.success) {
                            grid.getStore().load();
                        }
                        else {
                            Tools.errorMessageServer(obj.messageInfo);
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
    },
    
    
    
    retry: function (grid, record) {
    	
        Gam.Msg.showWaitMsg();
        Ext.Ajax.request({
            url: this.ns + '/doCMSRetryAction',
            jsonData: {
            	record: record.data,
            },
            success: function (response) {
                {
                    Gam.Msg.hideWaitMsg();
                    try {
                        var obj = Ext.decode(response.responseText);
                        if (obj.success) {
                            grid.getStore().load();
                        }
                        else {
                            Tools.errorMessageServer(obj.messageInfo);
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
    },
    
    
    
    deleteImageAction: function (grid, record) {
    	
        Gam.Msg.showWaitMsg();
        Ext.Ajax.request({
            url: this.ns + '/doDeleteImageAction',
            jsonData: {
            	record: record.data,
            },
            success: function (response) {
                {
                    Gam.Msg.hideWaitMsg();
                    try {
                        var obj = Ext.decode(response.responseText);
                        if (obj.success) {
                            grid.getStore().load();
                        }
                        else {
                            Tools.errorMessageServer(obj.messageInfo);
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
    },
    
});


