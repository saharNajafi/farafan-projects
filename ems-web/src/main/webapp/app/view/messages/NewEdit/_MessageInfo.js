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
//	width: 840,
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
	            ,'Ems.view.messages.NewEdit.MessageAddress'],
	            //	 margin: 10,
//	            layout : 'column',
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
	            	        	layout: 'column',
	            	        	columnWidth: 1/2,
	            	        	xtype: 'panel',
	            	        	id: 'gridContainer',
	            	        	border: false,
	            	        	bodyBorder: false,
	            	        	items: [
	            	        	        //   this.getMessageAddress_2(),
	            	        	        this.getMessageAddress_3(),
	            	        	        this.getMessageAddress_5(),
	            	        	        this.getMessageAddress_4(),
	            	        	        this.getMessageAddress_7(),
	            	        	        this.getMessageAddress_6(),
	            	        	       // this.getMessageAddress(),
	            	        	        ]
	            	        },
	            	        {xtype:'messageneweditmessageBody'}
	            	        ];
	            },
	            getMessageAddress: function () {
	            	return({
	            		xtype: 'container',
	            		id: 'idMessageAddressFrom',
	            		border: false,
	            		bodyBorder: false,
	            		items: [
	            		        {
	            		        	fieldLabel : 'اولویت',
	            		        	xtype:'messagepriority',
	            		        	allowBlank : true,
	            		        	value:"1",
	            		        	labelWidth:'5',
	            		        	id : EmsObjectName.messages_ID.msgPriority,
	            		        	itemId:  EmsObjectName.messages.msgPriority,
	            		        	name : "مهم"
	            		        },
	            		        ]

	            	});
	            },
//	            getMessageAddress_2: function () {
//	            return({
//	            xtype: 'container',
//	            id: 'idMessageAddressFrom_2',
//	            border: false,
//	            bodyBorder: false,
////	            items: [
////	            {
////	            fieldLabel : 'گیرنده پیام',
////	            allowBlank : true,
//////          labelWidth:'80',
////	            xtype : 'messagetypecombo',
////	            id : EmsObjectName.messages_ID.msgStatus,
////	            itemId:  EmsObjectName.messages.msgStatus,
////	            name : EmsObjectName.messages.msgStatus,
////	            listeners : {
////	            select : function(combo, value) {

////	            Ext.getCmp(EmsObjectName.messages_ID.provinceName).setValue("");
////	            Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
////	            Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setValue("");
////	            Ext.getCmp(EmsObjectName.messages_ID.officeName).setValue("");
////	            Ext.getCmp(EmsObjectName.messages_ID.personName).setValue("");

////	            switch (value[0].data.value) {
////	            case 'PROVINCE':{
////	            Ext.getCmp(EmsObjectName.messages_ID.provinceName).setDisabled(false);
////	            Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(true);
////	            Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setDisabled(true);
////	            Ext.getCmp(EmsObjectName.messages_ID.officeName).setDisabled(true);
////	            Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
////	            break;
////	            }
////	            case 'DEPARTMENT':{
////	            Ext.getCmp(EmsObjectName.messages_ID.provinceName).setDisabled(false);
////	            Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(false);
////	            Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setDisabled(true);
////	            Ext.getCmp(EmsObjectName.messages_ID.officeName).setDisabled(true);
////	            Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
////	            break;
////	            }
////	            case 'NOCR':{
////	            Ext.getCmp(EmsObjectName.messages_ID.provinceName).setDisabled(false);
////	            Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(false);
////	            Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setDisabled(false);
////	            Ext.getCmp(EmsObjectName.messages_ID.officeName).setDisabled(true);
////	            Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
////	            break;
////	            }
////	            case 'OFFICE':{
////	            Ext.getCmp(EmsObjectName.messages_ID.provinceName).setDisabled(false);
////	            Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(false);
////	            Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setDisabled(false);
////	            Ext.getCmp(EmsObjectName.messages_ID.officeName).setDisabled(false);
////	            Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
////	            break;
////	            }
////	            case 'PRIVATE':{
////	            Ext.getCmp(EmsObjectName.messages_ID.provinceName).setDisabled(false);
////	            Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(false);
////	            Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setDisabled(false);
////	            Ext.getCmp(EmsObjectName.messages_ID.officeName).setDisabled(false);
////	            Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(false);
////	            break;
////	            }
////	            case 'ALL':{
////	            Ext.getCmp(EmsObjectName.messages_ID.provinceName).setDisabled(true);
////	            Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(true);
////	            Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setDisabled(true);
////	            Ext.getCmp(EmsObjectName.messages_ID.officeName).setDisabled(true);
////	            Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
////	            break;
////	            }
////	            default:
////	            break;
////	            }
////	            },
////	            },
////	            },

////	            ]

//	            });
//	            },
	            getMessageAddress_3: function () {
	            	return({
	            		xtype: 'container',
	            		id: 'idMessageAddressFrom_3',
	            		border: false,
	            		bodyBorder: false,
	            		items: [
								{
									fieldLabel : 'اولویت',
									xtype:'messagepriority',
									allowBlank : true,
									value:"1",
									labelWidth:'5',
									id : EmsObjectName.messages_ID.msgPriority,
									itemId:  EmsObjectName.messages.msgPriority,
									name : "مهم"
								},
	            		        {
	            		        	xtype: 'checkboxfield',
	            		        	boxLabel: 'كل ',
	            		        	boxLabelAlign: 'after',
	            		        	id: EmsObjectName.messages_ID.kolProvine,
	            		        	name: EmsObjectName.messages_ID.kolProvine,
	            		        	disabled: false,
	            		        	listeners: {
	            		        		change: Ext.bind(function ($event) {
	            		        			if($event.checked){
	            		        				Ext.getCmp(EmsObjectName.messages_ID.provinceName).setDisabled(true);
	            		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(true);
	            		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
	            		        				Ext.getCmp(EmsObjectName.messages_ID.khosousiOffice).setValue(true);
	            		        				Ext.getCmp(EmsObjectName.messages_ID.doloatiOffice).setValue(true);
	            		        				Ext.getCmp(EmsObjectName.messages_ID.modir).setValue(true);
//	            		        				Ext.getCmp(EmsObjectName.messages_ID.doloatiOffice,

	            		        			}
	            		        			else{ 
	            		        				Ext.getCmp(EmsObjectName.messages_ID.provinceName).setDisabled(false);
	            		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(false);
	            		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(false);
	            		        				Ext.getCmp(EmsObjectName.messages_ID.khosousiOffice).setValue(false);
	            		        				Ext.getCmp(EmsObjectName.messages_ID.doloatiOffice).setValue(false);
	            		        				Ext.getCmp(EmsObjectName.messages_ID.modir).setValue(false);
	            		        			}
	            		        		}, this)
	            		        	}
	            		        },
	            		        {
	            		        	xtype:'hidden',
	            		        	height :20,
	            		        	width:20,
	            		        },
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
//	            		        			if (Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue()) 
	            		        			{
	            		        				autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
	            		        					'locationId' : Ext.getCmp(selectedProvince[0]),
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

//	            		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
	            		        			}

            		        			if(newValue != oldValue){

	            		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
	            		        				Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setValue("");
	            		        				Ext.getCmp(EmsObjectName.messages_ID.officeName).setValue("");
	            		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setValue("");
	            		        			}
	            		        		},
	            		        		select : function(combo, records, eOpts){
            		        			
	            		        			Ext.each(records, function (items) {
	            		        				var rec = items.data;
	            		        				if(selectedProvince.indexOf(rec) == -1)
	            		        					selectedProvince.push(rec);
	            		        			});

	            		        			if(selectedProvince.length > 1){
	            		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(true);
	            		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
	            		        			}
	            		        		},
	            		        		beforedeselect: function(combo, record, index, eOpts) {
	            		        			selectedProvince.splice(selectedProvince.indexOf(record.data));

	            		        			if(selectedProvince.length <= 1){
	            		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(false);
	            		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(false);
	            		        			}
	            		        		}
	            		        	}
	            		        },

	            		        ]

	            	});
	            },getMessageAddress_4: function () {
	            	return({
	            		xtype: 'container',
	            		id: 'idMessageAddressFrom_4',
	            		border: false,
	            		bodyBorder: false,
//	            		labelWidth:'5',
	            		items: [
	            		        {
	            		        	xtype: 'messageofficetyperadiogroup',
            		                fieldLabel: 'نوع دفتر',
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
	            		        	width:100,
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
	            		        			
	            		        			if(selectedProvince.length == 1)
//	            		        			if (Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue()) 
	            		        			{
	            		        				autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
	            		        					'locationId' : Ext.getCmp(selectedProvince.data),
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

//	            		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
	            		        			}
	            		        			if(newValue != oldValue){

	            		        				Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setValue("");
	            		        				Ext.getCmp(EmsObjectName.messages_ID.officeName).setValue("");
	            		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setValue("");
	            		        			}
	            		        		},
	            		        		select : function(combo, records, eOpts){

	            		        			Ext.each(records, function (items) {
	            		        				var rec = items.data;
	            		        				if(selectedDepartment.indexOf(rec) == -1)
	            		        					selectedDepartment.push(rec);
	            		        			});

	            		        			if(selectedDepartment.length > 1){
	            		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
	            		        			}
	            		        		},
	            		        		beforedeselect: function(combo, record, index, eOpts) {
	            		        			selectedDepartment.splice(selectedDepartment.indexOf(record.data));

	            		        			if(selectedDepartment.length <= 1){
	            		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(false);
	            		        			}
	            		        		},
	            		        	}
	            		        },
	            		        ]

	            	});
	            },
	            getMessageAddress_5: function () {
	            return({
	            xtype: 'container',
	            id: 'idMessageAddressFrom_5',
	            border: false,
	            labelWidth:'5',
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
//		        			if (Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue()) 
		        			{
		        				autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
		        					'locationId' : Ext.getCmp(selectedProvince[0]),
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

//		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
		        			}

		        			if(newValue != oldValue){

		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
		        				Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setValue("");
		        				Ext.getCmp(EmsObjectName.messages_ID.officeName).setValue("");
		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setValue("");
		        			}
		        		},
		        		select : function(combo, records, eOpts){
		        			
		        			Ext.each(records, function (items) {
		        				var rec = items.data;
		        				if(selectedProvince.indexOf(rec) == -1)
		        					selectedProvince.push(rec);
		        			});

		        			if(selectedProvince.length > 1){
		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(true);
		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(true);
		        			}
		        		},
		        		beforedeselect: function(combo, record, index, eOpts) {
		        			selectedProvince.splice(selectedProvince.indexOf(record.data));

		        			if(selectedProvince.length <= 1){
		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(false);
		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(false);
		        			}
		        		}
		        	}
		        },

	            ]

	            });
	            },
	            getMessageAddress_6: function () {
	            return({
	            xtype: 'container',
	            id: 'idMessageAddressFrom_6',
	            border: false,
	            labelWidth:'5',
	            bodyBorder: false,
	            items: [
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

		        			if (Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue()) {
		        				var officeId= Ext.getCmp(EmsObjectName.messages_ID.officeName).getValue().trim();
		        				var nocrId=Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).getValue().trim();
		        				var depId=Ext.getCmp(EmsObjectName.messages_ID.departmentName).getValue().trim();

		        				if( officeId != undefined &&  officeId != null && officeId.length !=0){
		        					autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
		        						'departmentId' : Ext.getCmp(EmsObjectName.messages_ID.officeName).getValue()
		        					});
		        				}else if( nocrId != undefined &&  nocrId != null && nocrId.length !=0)

		        				{

		        					autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
		        						'departmentId' : Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).getValue()
		        					});

		        				}
		        				else if( depId != undefined &&  depId != null && depId.length !=0)
		        				{
		        					autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
		        						'departmentId' : Ext.getCmp(EmsObjectName.messages_ID.departmentName).getValue()
		        					});
		        				}
		        				else {
		        					autocomplete.getStore().readParams.additionalParams = '';
		        					Gam.window.MessageManager.showInfoMsg('لطفا جایگاه سازمانی مورد نظر را انتخاب نمایید');
		        				}
		        			} else {
		        				autocomplete.getStore().readParams.additionalParams = '';
		        				Gam.window.MessageManager.showInfoMsg('لطفا ابتدا استان را انتخاب نمایید');
		        			}
		        		},

		        		focus : function(autocomplete, e) {
		        			if (Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue()) {

		        				var officeId= Ext.getCmp(EmsObjectName.messages_ID.officeName).getValue().trim();
		        				var nocrId=Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).getValue().trim();
		        				var depId=Ext.getCmp(EmsObjectName.messages_ID.departmentName).getValue().trim();

		        				if( officeId != undefined &&  officeId != null && officeId.length !=0){
		        					autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
		        						'departmentId' : Ext.getCmp(EmsObjectName.messages_ID.officeName).getValue()
		        					});
		        				}else if( nocrId != undefined &&  nocrId != null && nocrId.length !=0)

		        				{

		        					autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
		        						'departmentId' : Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).getValue()
		        					});

		        				}
		        				else if( depId != undefined &&  depId != null && depId.length !=0)
		        				{
		        					autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
		        						'departmentId' : Ext.getCmp(EmsObjectName.messages_ID.departmentName).getValue()
		        					});
		        				}
		        				else {
		        					autocomplete.getStore().readParams.additionalParams = '';
		        					Gam.window.MessageManager.showInfoMsg('لطفا جایگاه سازمانی مورد نظر را انتخاب نمایید');
		        				}
		        			} else {
		        				autocomplete.getStore().readParams.additionalParams = '';
		        				Gam.window.MessageManager.showInfoMsg('لطفا ابتدا استان را انتخاب نمایید');
		        			}
		        		},
		        		change : function(autocomplete,
		        				newValue, oldValue) {

		        			if (oldValue == undefined) {

//		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
		        			}
		        			if(newValue != oldValue){
		        			}
		        		}
		        	}
		        },

	            ]

	            });
	            },
	            getMessageAddress_7: function () {
	            	return({
	            		xtype: 'container',
	            		id: 'idMessageAddressFrom_7',
	            		border: false,
	            		labelWidth:'5',
	            		bodyBorder: false,
	            		items: [
	            		        {
	            		        	xtype: 'checkboxfield',
	            		        	boxLabel: 'مدیر',
	            		        	boxLabelAlign: 'after',
	            		        	id: EmsObjectName.messages_ID.modir,
	            		        	name:EmsObjectName.messages_ID.modir,
	            		        	disabled: false
	            		        },
//	            		        {
//	            		        	xtype:'hidden',
//	            		        	height :20,
//	            		        	width:20,
//	            		        },
//	            		        {
//	            		        	fieldLabel : 'کاربر',
//	            		        	// allowBlank : false,
//	            		        	disabled: false,
//	            		        	allowBlank : true,
//	            		        	labelWidth:'5',
//	            		        	xtype : 'personbaseondepartmentautocomplete',
//	            		        	id : EmsObjectName.messages_ID.personName,
//	            		        	itemId :EmsObjectName.messages.person,
//	            		        	dataIndex : EmsObjectName.messages.personName,
//	            		        	listeners : {
//
//	            		        		autocompleteselect : function(
//	            		        				autocomplete, e) {
//
//	            		        			if (Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue()) {
//	            		        				var officeId= Ext.getCmp(EmsObjectName.messages_ID.officeName).getValue().trim();
//	            		        				var nocrId=Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).getValue().trim();
//	            		        				var depId=Ext.getCmp(EmsObjectName.messages_ID.departmentName).getValue().trim();
//
//	            		        				if( officeId != undefined &&  officeId != null && officeId.length !=0){
//	            		        					autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
//	            		        						'departmentId' : Ext.getCmp(EmsObjectName.messages_ID.officeName).getValue()
//	            		        					});
//	            		        				}else if( nocrId != undefined &&  nocrId != null && nocrId.length !=0)
//
//	            		        				{
//
//	            		        					autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
//	            		        						'departmentId' : Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).getValue()
//	            		        					});
//
//	            		        				}
//	            		        				else if( depId != undefined &&  depId != null && depId.length !=0)
//	            		        				{
//	            		        					autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
//	            		        						'departmentId' : Ext.getCmp(EmsObjectName.messages_ID.departmentName).getValue()
//	            		        					});
//	            		        				}
//	            		        				else {
//	            		        					autocomplete.getStore().readParams.additionalParams = '';
//	            		        					Gam.window.MessageManager.showInfoMsg('لطفا جایگاه سازمانی مورد نظر را انتخاب نمایید');
//	            		        				}
//	            		        			} else {
//	            		        				autocomplete.getStore().readParams.additionalParams = '';
//	            		        				Gam.window.MessageManager.showInfoMsg('لطفا ابتدا استان را انتخاب نمایید');
//	            		        			}
//	            		        		},
//
//	            		        		focus : function(autocomplete, e) {
//	            		        			if (Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue()) {
//
//	            		        				var officeId= Ext.getCmp(EmsObjectName.messages_ID.officeName).getValue().trim();
//	            		        				var nocrId=Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).getValue().trim();
//	            		        				var depId=Ext.getCmp(EmsObjectName.messages_ID.departmentName).getValue().trim();
//
//	            		        				if( officeId != undefined &&  officeId != null && officeId.length !=0){
//	            		        					autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
//	            		        						'departmentId' : Ext.getCmp(EmsObjectName.messages_ID.officeName).getValue()
//	            		        					});
//	            		        				}else if( nocrId != undefined &&  nocrId != null && nocrId.length !=0)
//
//	            		        				{
//
//	            		        					autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
//	            		        						'departmentId' : Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).getValue()
//	            		        					});
//
//	            		        				}
//	            		        				else if( depId != undefined &&  depId != null && depId.length !=0)
//	            		        				{
//	            		        					autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({
//	            		        						'departmentId' : Ext.getCmp(EmsObjectName.messages_ID.departmentName).getValue()
//	            		        					});
//	            		        				}
//	            		        				else {
//	            		        					autocomplete.getStore().readParams.additionalParams = '';
//	            		        					Gam.window.MessageManager.showInfoMsg('لطفا جایگاه سازمانی مورد نظر را انتخاب نمایید');
//	            		        				}
//	            		        			} else {
//	            		        				autocomplete.getStore().readParams.additionalParams = '';
//	            		        				Gam.window.MessageManager.showInfoMsg('لطفا ابتدا استان را انتخاب نمایید');
//	            		        			}
//	            		        		},
//	            		        		change : function(autocomplete,
//	            		        				newValue, oldValue) {
//
//	            		        			if (oldValue == undefined) {
//
////	            		        				Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
//	            		        			}
//	            		        			if(newValue != oldValue){
//	            		        			}
//	            		        		}
//	            		        	}
//	            		        },

	            		        ]

	            	});
	            }
		});

