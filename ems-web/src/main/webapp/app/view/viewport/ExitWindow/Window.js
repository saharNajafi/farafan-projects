/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/29/12
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.viewport.ExitWindow.Window', {
    extend: 'Ext.window.Window',
    alias: 'widget.exitwindow',

    initComponent: function () {
        this.items = [
            {
                xtype: "component",
                width: 1,
                height: 1,
                hidden: true,
                autoRender: false,
                resizable: false,
                closable: false,
                autoEl: {
                    tag: "iframe",
//                src : "http://ems.gamelectronics.com:7001/cas/logout"
                    src: "/cas/logout"
                }
            }
        ];

        this.callParent(arguments);
    }

});
