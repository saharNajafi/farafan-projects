/**
 * Created with IntelliJ IDEA. User: Mousavi Date: 6/24/12 Time: 5:00 PM To
 * change this template use File | Settings | File Templates.
 **/

Ext.define('Ems.view.messages.NewEdit.MessageInfo',
		{
	id:'MessageInfo',
	extend : 'ICT.form.FieldSet',
	alias : 'widget.messageneweditmessageInfo',
	title : 'مشخصات پیام',
	contentStyle : function() {
		return (Tools.user.StyleObject);
	},
	requires : ['Ems.view.messages.MessageTypeCombo'
	            ,'Ems.view.messages.locationProAutocomplete'
	            ,'Ems.view.messages.departmentAutocomplete'
	            ,'Ems.view.messages.PersonBaseOnDepartmentAutocomplete'
	            ,'Ems.store.TempMessagesStore'
	            ,'Ems.view.messages.MessagePriority'
	            ,'Ems.view.messages.NewEdit.MessageBody'
	            ,'Ems.view.messages.NewEdit.MessageAddress'
	            ,'Ems.view.messages.Uploader'],

	            initComponent : function() {

	            	var me = this;

	            	me.defaults = {
//	            			columnWidth : 1 / 2
	            	};

	            	me.callParent(arguments);
	            },
	            getEditableFields : function() {
	            	return [
	            	        {
//	            	        	layout: 'column',
//	            	        	columnWidth: 1/2,
	            	        	xtype: 'panel',
	            	        	id: 'gridContainer',
	            	        	border: false,
	            	        	bodyBorder: false,
	            	        	items: [

	            	        	        this.getMessagepriority(),
	            	        	        this.getMessageProvince(),
//	            	        	        this.getMessageOffice(),
//	            	        	        this.getMessagePerson(),

	            	        	        ]
	            	        },

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
	            		        	xtype: 'messageofficetyperadiogroup',
	            		        	id: EmsObjectName.messages.officeType,
	            		        	name: EmsObjectName.messages.officeType,
	            		        	allowBlank: false,
	            		        	listeners: {
	            		        		change: Ext.bind(function (radioGroup, newValue, oldValue) {

	            		        		}, this)
	            		        	}
	            		        },
	            		        {
	            		        	xtype:'hidden',
	            		        	height :20,
	            		        	width:20,
	            		        },
	            		        ]

	            	});
	            },
	            getMessageProvince : function() {
	            	return({
	            		layout: 'column',
	            		columnWidth: 1/3,
	            		xtype: 'panel',
	            		id: 'idMessageProvinceForm',
	            		border: false,
	            		bodyBorder: false,
	            		items: [
	            		        {
	            		        	fieldLabel : 'استان',
	            		        	xtype : 'locationproautocomplete',
	            		        	allowBlank : true,
	            		        	text : 'استان',
	            		        	labelWidth:'5',
	            		        	disabled:false,
	            		        	dataIndex : EmsObjectName.messages.provinceName,
	            		        	id : EmsObjectName.messages_ID.provinceName,
	            		        	itemId: EmsObjectName.messages.province,
	            		        	listeners : {
	            		        		focus : function(autocomplete, e) {

	            		        			autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
	            		        				'allProvinces' : ''
	            		        			});

	            		        		},

	            		        		autocompleteselect : function(
	            		        				autocomplete, e) {

	            		        			if(selectedProvince.length == 1)
//	            		        				if (Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue()) 
	            		        			{
	            		        				autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
	            		        					'provinceId' : selectedProvince[0].acId,/* Ext.getCmp(selectedProvince[0]),*/
//	            		        					'nonEnrollment' : ''
	            		        				});
	            		        			} else {

	            		        				autocomplete.getStore().readParams.additionalParams = '';
//	            		        				Gam.window.MessageManager.showInfoMsg('لطفا ابتدا استان را انتخاب نمایید');
	            		        			}
	            		        		},
	            		        		change : function(autocomplete,
	            		        				newValue, oldValue) {

	            		        			if (oldValue == undefined) {
	            		        			}

	            		        			if(newValue != oldValue){

	            		        			}
	            		        		},
	            		        		select : function(combo, records, eOpts){

//	            		        			Ext.each(records, function (items) {
//	            		        			var rec = items.data;
//	            		        			displayAddress = new String();
//	            		        			if(selectedProvince.indexOf(rec) == -1){
//	            		        			selectedProvince.push(rec);
//	            		        			displayAddress += rec.acName;

//	            		        			Ext.getCmp('CueMessages').add(
//	            		        			Ext.create('Ext.form.Panel', {
//	            		        			border: false,
//	            		        			layout : 'hbox',
//	            		        			items: [

//	            		        			{
//	            		        			xtype: 'label',
//	            		        			text:displayAddress,
//	            		        			cls: 'label-form-field',
//	            		        			margin: '0 0 0 10'
//	            		        			}
//	            		        			]
//	            		        			})
//	            		        			);
//	            		        			}
//	            		        			});


	            		        			var store = Ext.getCmp('provinceMultiSelectds').getStore(),
	            		        			autoModel = {  id: null, name: null},
	            		        			i;
	            		        			for (i = 0; i < records.length; i++) {

	            		        				var reapetData = Tools.checkDataInStore(store, 'id', records[i].data.acId);
	            		        				if (reapetData == false) {
	            		        					autoModel.acId = null;
	            		        					autoModel.acName = null;
	            		        					autoModel.id = autoModel.acId = records[i].data.acId;
	            		        					autoModel.name = autoModel.acName = records[i].data.acName;

	            		        					
	            		        					//if(store.find('acId',autoModel.value,0,true, true)==-1){
	            		        					store.add(autoModel);
	            		        					selectedProvince.push(autoModel);
//	            		        					this.setAccesses(autoModel.id);
	            		        					//}
	            		        				}
	            		        			}
	            		        			if(Ext.getCmp('provinceMultiSelectds').getStore().data.items.length > 1){
	            		        				Ext.getCmp('departmentMultiSelectds').getStore().removeAll();
	            		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(true);

	            		        				Ext.getCmp('personMultiSelectds').getStore().removeAll();
	            		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
	            		        			}
	            		        		},
	            		        		beforedeselect: function(combo, record, index, eOpts) {
	            		        			selectedProvince.splice(selectedProvince.indexOf(record.data));

//	            		        			Ext.each(selectedProvince, function (items) {
//	            		        			displayAddress = new String();
//	            		        			var rec = items;

//	            		        			displayAddress += rec.acName;
//	            		        			Ext.getCmp('CueMessages').add(
//	            		        			Ext.create('Ext.form.Panel', {
//	            		        			border: false,
//	            		        			layout : 'hbox',
//	            		        			items: [

//	            		        			{
//	            		        			xtype: 'label',
//	            		        			text:displayAddress,
//	            		        			cls: 'label-form-field',
//	            		        			margin: '0 0 0 10'
//	            		        			}
//	            		        			]
//	            		        			})
//	            		        			);
//	            		        			});

	            		        			if(Ext.getCmp('provinceMultiSelectds').getStore().data.items.length <= 1){
	            		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(false);
	            		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(false);
	            		        			}
	            		        		}
	            		        	}
	            		        },
	            		        {
	            		        	xtype: 'button',
	            		        	text: 'حذف',
	            		        	itemId: 'btnRemoveProvince'
	            		        		//iconCls: 'i-delete'
	            		        },

	            		        {
	            		        	xtype:'hidden',
	            		        	height :20,
	            		        	width:20,
	            		        },
	            		        {
	            		        	fieldLabel : 'دفتر',
	            		        	xtype : 'departmentAutocomplete',
	            		        	allowBlank : true,
	            		        	labelWidth:'5',
	            		        	disabled: false,
	            		        	id : EmsObjectName.messages_ID.departmentName,
	            		        	itemId: EmsObjectName.messages.department,
	            		        	dataIndex : EmsObjectName.messages.departmentName,
	            		        	listeners : {

	            		        		autocompleteselect : function(
	            		        				autocomplete, e) {

	            		        			if(selectedDepartment.length > 0){
	            		        				autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
	            		        					'departmentId' : selectedDepartment[0].id
	            		        				});

	            		        			}
	            		        		},

	            		        		focus : function(autocomplete, e) {
	            		        			
	            		        			if(Ext.getCmp('provinceMultiSelectds').getStore().data.items.length == 1)
	            		        			{
	            		        				autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
	            		        					'provinceId' : Ext.getCmp('provinceMultiSelectds').getStore().data.items[0].data.id,/* Ext.getCmp(selectedProvince[0]),*/
//	            		        					'nonEnrollment' : ''
	            		        				});
	            		        			}
	            		        			else {
	            		        				autocomplete.getStore().readParams.additionalParams = '';
//	            		        				Gam.window.MessageManager.showInfoMsg('لطفا ابتدا استان را انتخاب نمایید');
	            		        			}
	            		        		},
	            		        		change : function(autocomplete,
	            		        				newValue, oldValue) {
	            		        			if (oldValue == undefined) {

	            		        			}
	            		        			if(newValue != oldValue){
	            		        			}
	            		        		},
	            		        		select : function(combo, records, eOpts){
//	            		        			Ext.each(records, function (items) {
//	            		        			var rec = items.data;
//	            		        			displayAddress = new String();
//	            		        			if(selectedDepartment.indexOf(rec) == -1){
//	            		        			selectedDepartment.push(rec);
//	            		        			displayAddress += rec.acName;

//	            		        			Ext.getCmp('CueMessages').add(
//	            		        			Ext.create('Ext.form.Panel', {
//	            		        			border: false,
//	            		        			layout : 'hbox',
//	            		        			items: [
//	            		        			{
//	            		        			xtype: 'label',
//	            		        			text:displayAddress,
//	            		        			cls: 'label-form-field',
//	            		        			margin: '0 0 0 10'
//	            		        			}
//	            		        			]
//	            		        			})
//	            		        			);
//	            		        			}
//	            		        			});

//	            		        			if(selectedDepartment.length > 1){
//	            		        			Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
//	            		        			}



	            		        			var store = Ext.getCmp('departmentMultiSelectds').getStore(),
	            		        			autoModel = {  id: null, name: null},
	            		        			i;
	            		        			for (i = 0; i < records.length; i++) {

	            		        				var reapetData = Tools.checkDataInStore(store, 'id', records[i].data.acId);
	            		        				if (reapetData == false) {
	            		        					autoModel.id = autoModel.acId = records[i].data.acId;
	            		        					autoModel.name = autoModel.acName = records[i].data.acName;

	            		        					//if(store.find('acId',autoModel.value,0,true, true)==-1){
	            		        					store.add(autoModel);
	            		        					selectedDepartment.push(autoModel);
//	            		        					this.setAccesses(autoModel.id);
	            		        					autoModel.acName = null;
	            		        					autoModel.acId = null;
	            		        					//}
	            		        				}
	            		        			}
	            		        			if(Ext.getCmp('departmentMultiSelectds').getStore().data.items.length> 1){
	            		        				Ext.getCmp('personMultiSelectds').getStore().removeAll();
	            		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);

	            		        			}

	            		        		},

	            		        		beforedeselect: function(combo, record, index, eOpts) {
	            		        			selectedProvince.splice(selectedProvince.indexOf(record.data));

//	            		        			Ext.each(selectedProvince, function (items) {
//	            		        			displayAddress = new String();
//	            		        			var rec = items;

//	            		        			displayAddress += rec.acName;
//	            		        			Ext.getCmp('CueMessages').add(
//	            		        			Ext.create('Ext.form.Panel', {
//	            		        			border: false,
//	            		        			layout : 'hbox',
//	            		        			items: [

//	            		        			{
//	            		        			xtype: 'label',
//	            		        			text:displayAddress,
//	            		        			cls: 'label-form-field',
//	            		        			margin: '0 0 0 10'
//	            		        			}
//	            		        			]
//	            		        			})
//	            		        			);
//	            		        			});

	            		        			if(Ext.getCmp('departmentMultiSelectds').getStore().data.items.length <= 1){
	            		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(false);
	            		        			}
	            		        		}
	            		        	}
	            		        },
	            		        {
	            		        	xtype: 'button',
	            		        	text: 'حذف',
	            		        	itemId: 'btnRemoveDepartment'
	            		        		//iconCls: 'i-delete'
	            		        },
	            		        {
	            		        	xtype:'hidden',
	            		        	height :20,
	            		        	width:20,
	            		        },
	            		        {
	            		        	fieldLabel : 'کاربر',
	            		        	// allowBlank : false,
	            		        	disabled: false,
	            		        	allowBlank : true,
	            		        	labelWidth:'5',
	            		        	xtype : 'personbaseondepartmentautocomplete',
	            		        	id : EmsObjectName.messages_ID.personName,
	            		        	itemId :EmsObjectName.messages.person,
	            		        	dataIndex : EmsObjectName.messages.personName,
	            		        	listeners : {
	            		        		autocompleteselect : function(
	            		        				autocomplete, e) {
	            		        			if(selectedPerson.length > 0){
	            		        				autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
	            		        					'departmentId' : Ext.getCmp('departmentMultiSelectds').getStore().data.items[0].data.id
	            		        				});
	            		        			}
	            		        		},

	            		        		focus : function(autocomplete, e) {

	            		        			if(Ext.getCmp('departmentMultiSelectds').getStore().data.items.length == 1){
	            		        				autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
	            		        					'departmentId' : Ext.getCmp('departmentMultiSelectds').getStore().data.items[0].data.id
	            		        				});
	            		        			}

	            		        		},
	            		        		change : function(autocomplete,newValue, oldValue) {},
	            		        		select : function(combo, records, eOpts){
//	            		        			Ext.each(records, function (items) {
//	            		        			var rec = items.data;
//	            		        			displayAddress = new String();
//	            		        			if(selectedPerson.indexOf(rec) == -1){
//	            		        			selectedPerson.push(rec);
//	            		        			displayAddress += rec.acName;

//	            		        			Ext.getCmp('CueMessages').add(
//	            		        			Ext.create('Ext.form.Panel', {
//	            		        			border: false,
//	            		        			layout : 'hbox',
//	            		        			items: [

//	            		        			{
//	            		        			xtype: 'label',
//	            		        			text:displayAddress,
//	            		        			cls: 'label-form-field',
//	            		        			margin: '0 0 0 10'
//	            		        			}
//	            		        			]
//	            		        			})
//	            		        			);
//	            		        			}
//	            		        			});


	            		        			var store = Ext.getCmp('personMultiSelectds').getStore(),
	            		        			autoModel = {  id: null, name: null},
	            		        			i;
	            		        			for (i = 0; i < records.length; i++) {

	            		        				var reapetData = Tools.checkDataInStore(store, 'id', records[i].data.acId);
	            		        				if (reapetData == false) {
	            		        					autoModel.id = autoModel.acId = records[i].data.acId;
	            		        					autoModel.name = autoModel.acName = records[i].data.acName;

	            		        					//if(store.find('acId',autoModel.value,0,true, true)==-1){
	            		        					store.add(autoModel);
	            		        					selectedPerson.push(autoModel);
//	            		        					this.setAccesses(autoModel.id);
	            		        					autoModel.acName = null;
	            		        					autoModel.idacId = null;
	            		        					//}
	            		        				}
	            		        			}

	            		        		},
	            		        	}
	            		        },
	            		        {
	            		        	xtype: 'button',
	            		        	text: 'حذف',
	            		        	itemId: 'btnRemovePerson'
	            		        		//iconCls: 'i-delete'
	            		        },
	            		        ]
	            	});
	            },


		});

