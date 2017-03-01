/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/3/12
 * Time: 1:01 PM
 */

Ext.define('Ems.view.cardRequestList.cardRequestPriority.CardRequestPriorityWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.cardRequestPriorityWindow',
//	layout: 'fit',
	requires: ['Ems.view.cardRequestList.cardRequestPriority.Dialog'],
	width: 500,
    resizable: false,
	initComponent: function () {
		this.items = [
		              {xtype:'cardRequestPriorityDialog'},
		              ];
//		this.buttons = [{
//			action: 'btnAddMessage',
//			id: 'idBtnAddMessage',
//			itemId : 'btnAddMessage',
//			text: 'ارسال',
//			xtype: 'button',
////			width: 70,
//			iconCls: 'windows-Save-icon',
//			handler: function () {
//			}
//		},
//		{
////			width: 70,
//			text: 'انصراف',
//			xtype: 'button',
//			iconCls: 'windows-Cancel-icon',
//			handler: function () {
//				this.up('window').close();
//			}
//		}]

		this.callParent(arguments);
	},
});
