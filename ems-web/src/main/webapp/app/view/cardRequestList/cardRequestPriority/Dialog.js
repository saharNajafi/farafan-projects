/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/3/12
 * Time: 1:01 PM
 */


Ext.define('Ems.view.cardRequestList.cardRequestPriority.Dialog', {
	extend: 'ICT.Panel',
	id: 'idCardRequestPriorityDialog',
	requires :  ['Ems.view.cardRequestList.cardRequestPriority.cardRequestPriorityCombo' , 
	             'Ems.view.cardRequestList.cardRequestPriority.cardRequestPriorityFieldSet'],
	alias: 'widget.cardRequestPriorityDialog',
	bodyBorder: false,
	border: false,
	initComponent: function () {
		this.getItems();
		this.callParent(arguments);
	},

	getItems: function(){
		return [
		        {
		        	xtype: 'cardRequestPriorityFieldSet'
		        }
		        ];
	},

});