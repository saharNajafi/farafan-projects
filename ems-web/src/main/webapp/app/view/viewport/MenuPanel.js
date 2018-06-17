Ext.define('Ems.view.viewport.MenuPanel', {
    extend: 'Ext.panel.Panel',
    layout: 'fit',

    alias: 'widget.menupanel',

    border: false,
    hideHeaders: true,

    // listeners: {
    //     beforerender: function() {
    //         var me = this;
    //         Ext.Ajax.request({
    //             url: 'extJsController/currentUser/fetchJobVariable',
    //             method: 'POST',
    //             success: function(response, request) {
    //                 globalAccessAllow = Boolean(Ext.JSON.decode(response.responseText).fetchJobVariable);
    //                 if(globalAccessAllow) {
    //                     me.down('basicInfo').show();
    //                     me.down('operation').show();
    //                     var reportPanel = me.down('report');
    //                     var bizLog = reportPanel.down('button[action=BizLog] menu');
    //                     var btns = reportPanel.query('button');
    //                     Ext.each(btns, function(panel) {
    //                         panel.show();
    //                     });
    //                     Ext.each(bizLog.query('component'), function(item) {
    //                         item.show();
    //                     });
    //                     Ext.ComponentQuery.query('viewport')[0].update();
    //                     //reportPanel.down('button[action=BizLog]').show();
    //                 }
    //             }
    //         });
    //     }
    // },

    requires: [
        'Ems.view.viewport.BasicInfo',
        'Ems.view.viewport.Operation',
        'Ems.view.viewport.Report'
    ],

    initComponent: function () {
        this.items = [
            {
                xtype: 'container',
                layout: {
                    type: 'hbox',
                    pack: 'start',
                    align: 'stretch'
                },
                items: [
                    {xtype: 'basicInfo' },
                    {xtype: 'operation' },
                    {xtype: 'report' }
                ]
            }
        ];
        this.callParent(arguments);
    }
});
