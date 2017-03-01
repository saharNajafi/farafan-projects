/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/27/12
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.controller.JobListController', {
    extend: 'Gam.app.controller.RowEditorBasedGrid',

    ns: 'extJsController/jobs',


    statics: {
        statefulComponents: [
            'wJobsGrid',
            'wJobsGridPlay',
            'wJobsGridResume',
            'wJobsGridPause',
            'wJobsGridEdit'
        ]
    },

    views: ['jobList.Grid'],

    initViewType: 'joblistgrid',

    constructor: function (config) {
        var me = this;
        this.callParent(arguments);
    },

    doPause: function (grid, rowIndex) {
        this.sendDataJob(grid, rowIndex, Tools.trim('pause'))
    },
    doResume: function (grid, rowIndex) {
        this.sendDataJob(grid, rowIndex, Tools.trim('resume'))
    },
    doPlay: function (grid, rowIndex) {
        this.sendDataJob(grid, rowIndex, Tools.trim('run'))
    },
    doInterrupt: function (grid, rowIndex) {
        this.sendDataJob(grid, rowIndex, Tools.trim('interrupt'))
    },

    sendDataJob: function (grid, rowIndex, url) {

        var store = grid.getStore(),
            record = store.getAt(rowIndex),
            name = record.get(EmsObjectName.job.name)
        var me = this;
        var t = me.ns + '/' + url;

        Gam.Msg.showWaitMsg();
        Ext.Ajax.request({

            url: me.ns + '/' + url,
            jsonData: {
                jobKey: name
            },
            success: function (response) {
                var rec = Ext.decode(response.responseText);
                if (rec.success) {
                    store.load();
                    Gam.Msg.hideWaitMsg();
                    //Tools.successMessage("عملیات با موفقیت صورت گرفت");
                }

            },
            failure: function (response) {
                Gam.Msg.hideWaitMsg();
                Tools.errorFailure();
            }
        });

    }
});


