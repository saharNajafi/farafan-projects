/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/18/12
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.controller.ReportController', {
    extend: 'Gam.app.controller.LocalDialogBasedGrid',
    ns: 'extJsController/report',

    statics: {
        statefulComponents: [
            'wReportGrid',
            'wReportGridAdd',
            'wReportGridDelete'
        ]
    },

    views: [
        'report.Grid'
    ],

    stores: [
//        'ReportResultStore'
    ],

    initViewType: 'reportgrid',

    /**
     * Reference to report result dialog instance
     */
    resultDialog: null,

    init: function () {

        /**
         * Attaching event handlers to elements
         */
        this.control({
            /**
             * Attach a handler to close button of the dialog
             */
            'dialog button[action=close]': {
                click: Ext.bind(this.doClose, this)
            }
        });

        this.callParent(arguments);
    },

    /**
     * Displays report result on a new dialog
     * @param grid      The grid containing list of reports
     * @param rowIndex  Index of the report object that has been clicked by user
     */
    doViewReport: function (grid, rowIndex) {

        this.resultDialog = Ext.create("Ems.view.report.ResultDialog");
        this.resultDialog.title = "گزارش " + grid.getStore().getAt(rowIndex).get("subject");

        //  Setting the reportId parameter on report result grid store
        this.resultDialog.setReportId(grid.getStore().getAt(rowIndex).get("id"));

        this.resultDialog.show();
    },

    /**
     * Handler of close button on report result dialog
     */
    doClose: function () {
        this.resultDialog.hide();
    },

    constructor: function () {
        //this.control({}),
        this.callParent(arguments);
    }
});
