/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/18/12
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.controller.ReportViewController', {
    extend: 'Gam.app.controller.LocalDialogBasedGrid',

    ns: 'extJsController/reportview',


    statics: {
        statefulComponents: [
            'wReportViewGrid',
            'wReportViewGridDelete',
            'wReportViewGridAdd'
        ]
    },

    views: ['reportView.Grid'],
    initViewType: 'reportgridview',

    /*
     init:function(){
     //this.control({}),
     this.callParent(arguments);
     },*/

    constructor: function () {
        //this.control({}),
        this.callParent(arguments);
    }

});

