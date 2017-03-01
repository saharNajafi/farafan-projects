/**
 * The Controller of reports management module
 * User: E.Z.Moghaddam
 * Date: 7/3/12
 * Time: 2:10 PM
 * Main controller of reports management module
 */
Ext.define('Ems.controller.ManageReportsController', {
    extend: 'Gam.app.controller.LocalDialogBasedGrid',

//    statics: {
//        statefulComponents: [
//            'wRatingGrid',
//            'wRatingGridAdd',
//            'wRatingGridDelete',
//            'wRatingGridEdit'
//
//        ]
//    },
    ns: 'extJsController/managereports',

    views: ['manageReports.Grid'],

    initViewType: 'managereportsgrid',

    constructor: function (config) {
        this.callParent(arguments);
    },

    doActivate: function (grid, rowIndex) {
        var recordId = grid.getStore().getAt(rowIndex).get("id");
        this.changeReportState(grid, recordId, 'T');
    },

    /**
     * Deactivates given report
     *
     * @param grid      Reference to the manage reports grid
     * @param rowIndex  Index of the report which has to be deactivated
     */
    doDeactivate: function (grid, rowIndex) {
        var recordId = grid.getStore().getAt(rowIndex).get("id");
        this.changeReportState(grid, recordId, 'F');
    },

    /**
     * Given a report identifier and calls an appropriate service on server side to change its state (enable/disable)
     *
     * @param grid      Reference to the report grid
     * @param reportId  Identifier of the report to change its state
     * @param newState  New state of the given report
     */
    changeReportState: function (grid, reportId, newState) {
        Gam.Msg.showWaitMsg();
        Ext.Ajax.request({
            url: this.ns + '/changeReportState',
            jsonData: {
                reportId: reportId,
                newReportState: newState
            },
            success: Ext.bind(function (response) {
                Gam.Msg.hideWaitMsg();
                try {
                    var obj = Ext.decode(response.responseText);
                    if (obj.success) {
                        grid.getStore().load();
                    }
                    else {
                        Tools.errorMessageServer(obj.messageInfo)
                    }
                } catch (e) {
                    alert(e.message);
                }
            }, this),
            failure: function (resop) {
                Gam.Msg.hideWaitMsg();
                Tools.errorFailure();
            }
        });
    }
});
