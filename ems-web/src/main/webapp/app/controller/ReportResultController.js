/**
 * The Controller of report results module
 *
 * User: E.Z.Moghaddam
 * Date: 7/3/12
 * Time: 2:10 PM
 *
 * Main controller of report results module
 */
Ext.define('Ems.controller.ReportResultController', {
    extend: 'Gam.app.controller.LocalDialogBasedGrid',

    ns: 'extJsController/reportresult',

    views: [
        'reportResult.Grid'
    ],

    initViewType: 'reportresultgrid',

    constructor: function (config) {
        this.callParent(arguments);
    },

    init: function () {
        this.callParent(arguments);
    },

    /**
     * Handler of delete report action
     *
     * @param grid      Reference to the report result grid
     * @param rowIndex  Index of the row item which its delete action has been selected
     */
    doDeleteRequest: function (grid, rowIndex) {
        Tools.messageBoxConfirm("آیا از حذف درخواست گزارش <b>" +
            grid.getStore().getAt(rowIndex).get(EmsObjectName.reportResult.reportName) + "</b> اطمینان دارید؟",
            Ext.bind(this.sendDeleteRequest, this, [grid, grid.getStore().getAt(rowIndex).get("id")]));
    },

    /**
     * Due to naming convention used on general delete button, automatic remove while multiple item are select doesn't work
     * @param grid
     * @param rowIndex
     */
    doDelete: function (grid) {
        var selectedIDs = "";
        var selectedItems = grid.getSelectionModel().getSelection();

        for (var i = 0; i < selectedItems.length; i++) {
            selectedIDs += selectedItems[i].get("id") + ",";
        }

        selectedIDs = selectedIDs.substr(0, selectedIDs.length - 1);
        Tools.messageBoxConfirm("آیا از حذف موارد انتخاب شده اطمینان دارید؟",
            Ext.bind(this.sendDeleteRequest, this, [grid, selectedIDs]));
    },

    sendDeleteRequest: function (grid, ids) {
        Gam.Msg.showWaitMsg();
        Ext.Ajax.request({
            url: 'extJsController/reportrequest/remove',
            jsonData: {
                ids: ids
            },
            success: Ext.bind(function (response) {
                Gam.Msg.hideWaitMsg();
                try {
                    var obj = Ext.decode(response.responseText);
                    if (obj.success) {
                        grid.getStore().load();
                    }
                    else {
                        Gam.Msg.hideWaitMsg();
                        Tools.errorMessageServer(obj.messageInfo)
                    }
                } catch (e) {
                    Gam.Msg.hideWaitMsg();
                    alert(e.message);
                }
            }, this),
            failure: function (resop) {
                Gam.Msg.hideWaitMsg();
                Tools.errorFailure();
            }
        });
    },

    /**
     * Handler of download result action
     *
     * @param grid      Reference to the report result grid
     * @param rowIndex  Index of the row item which its download result action has been selected
     */
    doDownloadResult: function (grid, rowIndex) {
        try {
            if (Ext.get('downloadIframe')) {
                Ext.destroy(Ext.get('downloadIframe'));
            }
        }
        catch (e) {
            console.info(e);
        }

        Ext.DomHelper.append(document.body, {
            tag: 'iframe',
            id: 'downloadIframe',
            frameBorder: 0,
            width: 0,
            height: 0,
            css: 'display:none;visibility:hidden;height:0px;',
            src: 'extJsController/reportresult/loadGeneratedReport?reportRequestId=' +
                grid.getStore().getAt(rowIndex).get("id")
        });
    },

    /**
     * Dummy method which will be called by famework if user clicks on output type button
     */
    doDummyOutputType: function(){

    }
});
