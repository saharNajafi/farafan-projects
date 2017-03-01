Ext.define('Ems.view.viewport.MenuPanel', {
    extend: 'Ext.panel.Panel',
    layout: 'fit',

    alias: 'widget.menupanel',

    border: false,
    hideHeaders: true,

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
                    {xtype: 'basicInfo'},
                    {xtype: 'operation'},
                    {xtype: 'report'}
                ]
            }
        ];
        this.callParent(arguments);
    }
});
