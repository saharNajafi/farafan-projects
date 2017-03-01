/**
 * Created with IntelliJ IDEA. User: Mousavi Date: 6/24/12 Time: 5:00 PM To
 * change this template use File | Settings | File Templates.
 **/
Ext.define('Ems.view.messages.NewEdit.MessageAddress',
		{
	id:'MessageAdderss',
	extend : 'ICT.form.FieldSet',
	alias : 'widget.messageneweditmessageAddress',
	//title : 'مشخصات پیام',

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
		        	fieldLabel : 'اولویت',
		        	xtype:'messagepriority',
		        	allowBlank : true,
		        	value:"1",
		        	labelWidth:'70',
		        	id : EmsObjectName.messages_ID.msgPriority,
		        	itemId:  EmsObjectName.messages.msgPriority,
		        	name : "مهم"
		        },
		        {
		        	fieldLabel : 'گیرنده پیام',
		        	allowBlank : true,
		        	labelWidth:'80',
		        	xtype : 'messagetypecombo',
		        	id : EmsObjectName.messages_ID.msgStatus,
		        	itemId:  EmsObjectName.messages.msgStatus,
		        	name : EmsObjectName.messages.msgStatus,
		        	listeners : {
		        		select : function(combo, value) {
		        			Ext.getCmp(EmsObjectName.messages_ID.provinceName).setValue("");
		        			Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
		        			Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setValue("");
		        			Ext.getCmp(EmsObjectName.messages_ID.officeName).setValue("");
		        			Ext.getCmp(EmsObjectName.messages_ID.personName).setValue("");

		        			switch (value[0].data.value) {
		        			case 'PROVINCE':{
		        				Ext.getCmp(EmsObjectName.messages_ID.provinceName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(true);
		        				Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setDisabled(true);
		        				Ext.getCmp(EmsObjectName.messages_ID.officeName).setDisabled(true);
		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
		        				break;
		        			}
		        			case 'DEPARTMENT':{
		        				Ext.getCmp(EmsObjectName.messages_ID.provinceName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setDisabled(true);
		        				Ext.getCmp(EmsObjectName.messages_ID.officeName).setDisabled(true);
		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
		        				break;
		        			}
		        			case 'NOCR':{
		        				Ext.getCmp(EmsObjectName.messages_ID.provinceName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.officeName).setDisabled(true);
		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
		        				break;
		        			}
		        			case 'OFFICE':{
		        				Ext.getCmp(EmsObjectName.messages_ID.provinceName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.officeName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
		        				break;
		        			}
		        			case 'PRIVATE':{
		        				Ext.getCmp(EmsObjectName.messages_ID.provinceName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.officeName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(false);
		        				break;
		        			}
		        			case 'ALL':{
		        				Ext.getCmp(EmsObjectName.messages_ID.provinceName).setDisabled(true);
		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(true);
		        				Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setDisabled(true);
		        				Ext.getCmp(EmsObjectName.messages_ID.officeName).setDisabled(true);
		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
		        				break;
		        			}
		        			default:
		        				break;
		        			}
		        		},
		        	},
		        },
		        {
		        	fieldLabel : 'استان',
		        	xtype : 'locationproautocomplete',
		        	allowBlank : true,
		        	text : 'استان',
		        	labelWidth:'70',
		        	disabled: true,
		        	dataIndex : EmsObjectName.messages.provinceName,
		        	id : EmsObjectName.messages_ID.provinceName,
		        	itemId: EmsObjectName.messages.province,
		        	listeners : {
		        		focus : function(autocomplete, e) {

		        			autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
		        				'allProvinces' : ''
		        			});

		        		},
		        		change : function(autocomplete,
		        				newValue, oldValue) {
		        			if (oldValue == undefined) {
		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
		        			}
		        			if(newValue != oldValue){
//		        				Ext.getCmp(EmsObjectName.messages_ID.provinceName).setValue("");
		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
		        				Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setValue("");
		        				Ext.getCmp(EmsObjectName.messages_ID.officeName).setValue("");
		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setValue("");
		        			}
		        		}
		        	}
		        },
		        {
		        	fieldLabel : 'اداره',
		        	xtype : 'departmentAutocomplete',
		        	allowBlank : true,
		        	labelWidth:'80',
		        	disabled: true,
		        	id : EmsObjectName.messages_ID.departmentName,
		        	itemId: EmsObjectName.messages.department,
		        	dataIndex : EmsObjectName.messages.departmentName,
		        	listeners : {

		        		autocompleteselect : function(
		        				autocomplete, e) {

		        			if (Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue()) {
		        				autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
		        					'locationId' : Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue(),
		        					'nonEnrollment' : ''
		        				});
		        			} else {
		        				autocomplete.getStore().readParams.additionalParams = '';
		        				Gam.window.MessageManager.showInfoMsg('لطفا ابتدا استان را انتخاب نمایید');
		        			}
		        		},

		        		focus : function(autocomplete, e) {

		        			if (Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue()) {
		        				autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
		        					'locationId' : Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue(),
		        					'nonEnrollment' : ''
		        				});
		        			} else {
		        				autocomplete.getStore().readParams.additionalParams = '';
		        				Gam.window.MessageManager.showInfoMsg('لطفا ابتدا استان را انتخاب نمایید');
		        			}
		        		},
		        		change : function(autocomplete,
		        				newValue, oldValue) {
		        			if (oldValue == undefined) {
		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
		        			}
		        			if(newValue != oldValue){
//		        				Ext.getCmp(EmsObjectName.messages_ID.provinceName).setValue("");
//		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
		        				Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setValue("");
		        				Ext.getCmp(EmsObjectName.messages_ID.officeName).setValue("");
		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setValue("");
		        			}
		        		}
		        	}
		        },
		        {
		        	fieldLabel : 'دفتر معین',
		        	allowBlank : true,
		        	labelWidth:'70',
		        	disabled: true,
		        	// allowBlank : false,
		        	xtype : 'departmentAutocomplete',
		        	id : EmsObjectName.messages_ID.nocrofficName,
		        	itemId: EmsObjectName.messages.nocroffic,
		        	dataIndex : EmsObjectName.messages.nocrofficName,
		        	listeners : {

		        		autocompleteselect : function(autocomplete, e) {

		        			if (Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue()) {
		        				autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
		        					'locationId' : Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue(),
		        					'nocroffice' : Ext.getCmp(EmsObjectName.messages_ID.departmentName).getValue()
		        				});
		        			} else {
		        				autocomplete.getStore().readParams.additionalParams = '';
		        				Gam.window.MessageManager
		        				.showInfoMsg('لطفا ابتدا استان را انتخاب نمایید');
		        			}
		        		},

		        		focus : function(autocomplete, e) {

		        			if (Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue()) {
		        				autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
		        					'locationId' : Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue(),
		        					'nocroffice' : Ext.getCmp(EmsObjectName.messages_ID.departmentName).getValue()
		        				});
		        			} else {
		        				autocomplete.getStore().readParams.additionalParams = '';
		        				Gam.window.MessageManager
		        				.showInfoMsg('لطفا ابتدا استان را انتخاب نمایید');
		        			}
		        		},
		        		change : function(autocomplete,
		        				newValue, oldValue) {
		        			if (oldValue == undefined) {
		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
		        			}
		        			if(newValue != oldValue){
//		        				Ext.getCmp(EmsObjectName.messages_ID.provinceName).setValue("");
//		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
//		        				Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setValue("");
		        				Ext.getCmp(EmsObjectName.messages_ID.officeName).setValue("");
		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setValue("");
		        			}
		        		}
		        	}
		        },
		        {
		        	fieldLabel : 'دفتر خصوصی',
		        	// allowBlank : false,
		        	labelWidth:'80',
		        	disabled: true,
		        	allowBlank : true,
		        	xtype : 'departmentAutocomplete',
		        	id : EmsObjectName.messages_ID.officeName,
		        	itemId: EmsObjectName.messages.office,
		        	dataIndex : EmsObjectName.messages.officeName,
		        	listeners : {

		        		autocompleteselect : function(autocomplete, e) {

		        			if (Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue()) {
		        				autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
		        					'locationId' : Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue(),
		        					'nocrId' : Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).getValue(),
		        					'office' : ''
		        				});
		        			} else {
		        				autocomplete.getStore().readParams.additionalParams = '';
		        				Gam.window.MessageManager
		        				.showInfoMsg('لطفا ابتدا استان را انتخاب نمایید');
		        			}
		        		},

		        		focus : function(autocomplete, e) {

		        			if (Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue()) {
		        				autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
		        					'locationId' : Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue(),
		        					'nocrId' : Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).getValue(),
		        					'office' : ''
		        				});
		        			} else {
		        				autocomplete.getStore().readParams.additionalParams = '';
		        				Gam.window.MessageManager.showInfoMsg('لطفا ابتدا استان را انتخاب نمایید');
		        			}
		        		},
		        		change : function(autocomplete,
		        				newValue, oldValue) {
		        			if (oldValue == undefined) {
		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
		        			}
		        			if(newValue != oldValue){
//		        				Ext.getCmp(EmsObjectName.messages_ID.provinceName).setValue("");
//		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
//		        				Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setValue("");
//		        				Ext.getCmp(EmsObjectName.messages_ID.officeName).setValue("");
		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setValue("");
		        			}
		        		}
		        	}
		        },
		        {
		        	fieldLabel : 'کاربر',
		        	// allowBlank : false,
		        	disabled: true,
		        	allowBlank : true,
		        	labelWidth:'70',
		        	xtype : 'personbaseondepartmentautocomplete',
		        	id : EmsObjectName.messages_ID.personName,
		        	itemId :EmsObjectName.messages.person,
		        	dataIndex : EmsObjectName.messages.personName,
		        	listeners : {

		        		autocompleteselect : function(
		        				autocomplete, e) {

		        			if (Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue()) {
		        				autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
		        					'departmentId' : Ext.getCmp(EmsObjectName.messages_ID.officeName).getValue()
		        				});
		        			} else {
		        				autocomplete.getStore().readParams.additionalParams = '';
		        				Gam.window.MessageManager.showInfoMsg('لطفا ابتدا استان را انتخاب نمایید');
		        			}
		        		},

		        		focus : function(autocomplete, e) {

		        			if (Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue()) {
		        				autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
		        					'departmentId' : Ext.getCmp(EmsObjectName.messages_ID.officeName).getValue()
		        				});
		        			} else {
		        				autocomplete.getStore().readParams.additionalParams = '';
		        				Gam.window.MessageManager.showInfoMsg('لطفا ابتدا استان را انتخاب نمایید');
		        			}
		        		},
		        		change : function(autocomplete,
		        				newValue, oldValue) {
		        			if (oldValue == undefined) {
		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
		        			}
		        			if(newValue != oldValue){
//		        				Ext.getCmp(EmsObjectName.messages_ID.provinceName).setValue("");
//		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
//		        				Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setValue("");
//		        				Ext.getCmp(EmsObjectName.messages_ID.officeName).setValue("");
//		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setValue("");
		        			}
		        		}
		        	}
		        }
		        ];
	}
		});
