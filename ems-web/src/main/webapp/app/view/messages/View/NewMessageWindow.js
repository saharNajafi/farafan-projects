/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/3/12
 * Time: 1:01 PM
 */

Ext.define('Ems.view.messages.View.NewMessageWindow', {
	 extend: 'Ext.window.Window',
    alias: 'widget.newmessagewindowView',

    requires: ['Ems.view.messages.View.Dialog'],
    width: 860,
    initComponent: function () {
//        this.height = 535;
        this.items = [
                      {xtype:'messagesdialogView'},
                  	];
        this.callParent(arguments);
    }
});
