/**
 * Created with IntelliJ IDEA. User: Mousavi Date: 6/24/12 Time: 5:00 PM To
 * change this template use File | Settings | File Templates.
 **/
Ext.define('Ems.view.messages.View.MessageInfo',
		{
	id:'MessageInfo',
	extend : 'ICT.form.FieldSet',
	alias : 'widget.messageneweditmessageInfoView',
	title : 'مشخصات پیام',
	width: 840,
	contentStyle : function() {
		return (Tools.user.StyleObject);
	},
	requires : ['Ems.view.messages.MessageTypeCombo'
	            ,'Ems.view.messages.locationProAutocomplete'
	            ,'Ems.view.messages.departmentAutocomplete'
	            ,'Ems.view.messages.PersonBaseOnDepartmentAutocomplete'
	            ,'Ems.store.TempMessagesStore'
	            ,'Ems.view.messages.MessagePriority'
	            ,'Ems.view.messages.View.MessageBody'
	            ,'Ems.view.messages.View.MessageAddress',
	            'Ems.view.messages.View.MessageRadioReadOnlyGroup'],
	            //	 margin: 10,
//	            layout : 'column',
	            initComponent : function() {

	            	var me = this;

	            	me.defaults = {
	            			//	columnWidth : 1 / 2
	            	};

	            	me.callParent(arguments);
	            },
	            getEditableFields : function() {
	            	return [
	            	        {
	            	        	layout: 'column',
//	            	        	columnWidth: 1/2,
	            	        	xtype: 'panel',
	            	        	id: 'gridContainer',
	            	        	border: false,
	            	        	bodyBorder: false,
	            	        	items: [
	            	        	        this.getMessagepriority(),
	            	        	        {xtype:'messageneweditmessageAddressView'},
	            	        	        {xtype:'messageneweditmessageBodyView'},
	            	        	        ]
	            	        }
	            	        ];
	            },
	            getMessagepriority : function() {
	            	return({
	            		xtype: 'container',
	            		id: 'idMessagepriorityForm',
	            		border: false,
	            		bodyBorder: false,
	            		items: [
	            		        {
	            		        	xtype: 'messageRadioReadOnlyGroup',
	            		        	id: EmsObjectName.messages.officeType,
	            		        	name: EmsObjectName.messages.officeType,
	            		        	allowBlank: false,
	            		        	listeners: {}
	            		        },
	            		        {
	            		        	xtype:'hidden',
	            		        	height :20,
	            		        	width:20,
	            		        },
	            		        ]

	            	});
	            },
		});
