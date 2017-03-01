/**
 * Created with IntelliJ IDEA. User: Mousavi Date: 6/24/12 Time: 5:00 PM To
 * change this template use File | Settings | File Templates.
 **/
Ext.define('Ems.view.cardRequestList.cardRequestPriority.cardRequestPriorityFieldSet',
		{
	id:'CardRequestPriorityFieldSet',
	extend : 'ICT.form.FieldSet',
	alias : 'widget.cardRequestPriorityFieldSet',

	contentStyle : function() {
		return (Tools.user.StyleObject);
	},
	layout : 'column',
	border:false,
	initComponent : function() {

		var me = this;
		me.defaults = {
				columnWidth : 1 / 2
		};
		me.callParent(arguments);
	},
	getEditableFields : function() {
		return [
		        {
		        	fieldLabel : 'نام',
		        	itemId: EmsObjectName.cardRequestList.citizenFirstName,
		        	xtype:'displayfield'
		        },
		        {
		        	fieldLabel : 'نام خانوادگی',
		        	itemId:EmsObjectName.cardRequestList.citizenSurname,
		        	xtype:'displayfield'
		        },
		        {
		        	fieldLabel : 'کد ملی',
		        	itemId:EmsObjectName.cardRequestList.citizenNId,
		        	xtype:'displayfield'
		        },
		        {
		        	fieldLabel : 'وضعیت',
		        	itemId:EmsObjectName.cardRequestList.cardState,
		        	xtype:'displayfield'
		        },

		        {
		        	itemId:EmsObjectName.cardRequestList.priority,
		        	xtype: 'cardRequestPriorityCombo'
		        },
		        {
		        	xtype: 'container',
		        	columnWidth: .50,
		        	bodyBorder: false,
		        	border: false,
		        	items: [
		        	        {
		        	        	html: '<br/>', border: false
		        	        },
		        	        {
		        	        	html: '<br/>', border: false
		        	        },
		        	        {
		        	        	action: 'btnUpdatePriority',
		        	        	id: 'idBtnAddMessage',
		        	        	itemId : 'btnUpdatePriority',
		        	        	text: 'ارسال',
		        	        	xtype: 'button',
		        	        	width: 100,
		        	        	border: true,
		        	        	iconCls: 'windows-Save-icon',
		        	        	handler: function () {
		        	        	}
		        	        },
		        	        {
		        	        	width: 100,
		        	        	border: true,
		        	        	text: 'انصراف',
		        	        	xtype: 'button',
		        	        	iconCls: 'windows-Cancel-icon',
		        	        	handler: function () {
		        	        		this.up('window').close();
		        	        	}
		        	        }
		        	        ]}
		        ];
	}
		});
