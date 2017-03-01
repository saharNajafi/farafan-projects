/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/3/12
 * Time: 1:01 PM
 */

Ext.define('Ems.view.messages.NewMessageWindow', {
	 extend: 'Ext.window.Window',
    alias: 'widget.newmessagewindow',
//    layout: 'fit',
    requires: ['Ems.view.messages.Dialog'],
    width: 860,
    initComponent: function () {
        this.items = [
                      {xtype:'messagesdialog'},
                  	];
        this.buttons = [{
		    action: 'btnAddMessage',
			    id: 'idBtnAddMessage',
			    itemId : 'btnAddMessage',
			    text: 'ارسال',
			    xtype: 'button',
//			    width: 70,
			    iconCls: 'windows-Save-icon',
			    handler: function () {
			    }
			},
			{
//			    width: 70,
				action: 'cancelMessage',
			    text: 'انصراف',
			    xtype: 'button',
			    iconCls: 'windows-Cancel-icon',
			    handler: function () {
			        this.up('window').close();
			    }
			}]
        
        this.callParent(arguments);
    },
    
    listeners:{
    	 beforeclose: function(element) {
             console.log('closing');
         }
    },
    
    
});
