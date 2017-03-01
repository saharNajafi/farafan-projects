var selectedProvince = new Array();
var selectedDepartment = new Array();
var selectedPerson = new Array();
var displayAddress = new String();
Ext.define('Ems.controller.MessagesController', {
	extend : 'Gam.app.controller.LocalDialogBasedGrid',
	ns : 'extJsController/messages',
	nsDownload : 'extJsController/downloadMessageAttached',

	views : [ 'messages.Grid'
	          ],
	          refs: [
	                 {
	                	 ref: 'messagesgrid',
	                	 selector: 'messagesgrid'
	                 }
	                 ],
	                 stores : [ 'MessagesStore' ],
	                 requires: ['Gam.button.Add',
	                            'Ems.view.messages.MessageOfficeTypeRadioGroup',
	                            'Ems.view.messages.View.MessageRadioReadOnlyGroup',
	                            'Ems.view.messages.MessageMultiSelect.ProvinceMultiSelect',
	                            'Ems.view.messages.MessageMultiSelect.PersonMultiSelect',
	                            'Ems.view.messages.MessageMultiSelect.DepartmentMultiSelect'],
	                            constructor : function(config) {

	                            	this.callParent(arguments);
	                            }, init: function () {
	                            	var me = this;
	                            	this.control({
	                            		"[action=btnAddMessage]": { 'click': this.getSaveFrom, scope: this },
	                            		"[action=cancelMessage]": { 'click': this.getCancelMessage, scope: this },
                            		   "button[itemId=btnRemoveProvince]": {  "click": this.getBtnRemoveProvince, scope: this    },
                            		   "button[itemId=btnRemovePerson]": {  "click": this.getBtnRemovePerson, scope: this    },
                            		   "button[itemId=btnRemoveDepartment]": {  "click": this.getBtnRemoveDepartment, scope: this    },
	                            	});

	                            	this.callParent(arguments);
	                            },
	                            getSaveFrom: function (btn) {

	                            	var me = this;
	                            	var grid =  Ext.getCmp('messagesgrid');
	                            	var tmpStore = Ext.getStore('TempMessageStore');
	                            	if(!tmpStore)
	                            		tmpStore = Ext.create('Ems.store.TempMessagesStore');

	                            	var formData = new Object();
	                            	formData.title = Ext.getCmp(EmsObjectName.messages_ID.title).getValue();
	                            	
	                            	if(formData.title == "" || formData.title == undefined){
	                            		Tools.errorMessageClient('لطفا عنوان پیام را وارد نمایید');
	                            		return;
	                            	}
	                            	formData.content = Ext.getCmp(EmsObjectName.messages_ID.content).getValue();
	                            	if(formData.content == "" || formData.content == undefined){
	                            		Tools.errorMessageClient('لطفا متن  پیام را وارد نمایید');
	                            		return;
	                            	}
	                            	formData.isAll = Ext.getCmp(EmsObjectName.messages.isAll).getValue();
	                            	formData.isManager = Ext.getCmp(EmsObjectName.messages.isManager).getValue();
	                            	formData.isOffice = Ext.getCmp(EmsObjectName.messages.isOffice).getValue();
	                            	formData.isNocr = Ext.getCmp(EmsObjectName.messages.isNocr).getValue();
	                            	formData.isAllOffice = Ext.getCmp(EmsObjectName.messages.isAllOffice).getValue();
	                            	if(formData.isAllOffice){
	                            		formData.isOffice = true;
	                            		formData.isNocr = true;
	                            	}

	                            	var provinceArray = [];
	                            	var provinceLenght = Ext.getCmp('provinceMultiSelectds').getStore().data.items.length
	                            	if(provinceLenght>0){
	                            		for ( var storeItem = 0; storeItem < provinceLenght; storeItem++) {
	                            			provinceArray.push(Ext.getCmp('provinceMultiSelectds').getStore().data.items[storeItem].data);
	                            		}
	                            	}
	                            	
	                            	
	                             	var departmentArray = [];
	                            	var departmentLenght = Ext.getCmp('departmentMultiSelectds').getStore().data.items.length
	                            	if(departmentLenght>0){
	                            		for ( var storeItem = 0; storeItem < departmentLenght; storeItem++) {
	                            			departmentArray.push(Ext.getCmp('departmentMultiSelectds').getStore().data.items[storeItem].data);
	                            		}
	                            	}
	                            	
	                            	
	                            	var personArray = [];
	                            	var personLenght = Ext.getCmp('personMultiSelectds').getStore().data.items.length
	                            	if(personLenght>0){
	                            		for ( var storeItem = 0; storeItem < personLenght; storeItem++) {
	                            			personArray.push(Ext.getCmp('personMultiSelectds').getStore().data.items[storeItem].data);
	                            		}
	                            	}
	                            	
	                            	formData.provinces= provinceArray;
	                            	formData.offices = departmentArray;
	                            	formData.persons = personArray;

	                            	tmpStore.add(formData);

	                            	Ext.getCmp(EmsObjectName.messages_ID.title).setDisabled(true);
	                            	Ext.getCmp(EmsObjectName.messages_ID.content).setReadOnly(true);
	                            	Ext.getCmp(EmsObjectName.messages_ID.content).getToolbar().hide(false);
	                            	var store = Ext.getStore('TempMessageStore').data.items;	
	                            	var storeLength = Ext.getStore('TempMessageStore').data.items.length;
	                            	if(storeLength != 0){
	                            		var DataFormArray = [];
	                            		
                            			DataFormArray.push(store[0].data);
                            		

	                            		var dataFormObject = new Object();
	                            		dataFormObject.data = DataFormArray;
	                            		var dataFormArrayJson = JSON.stringify(DataFormArray);
	                            		var dataFormArrayDecoded = Ext.decode(dataFormArrayJson);

	                            		var win = Ext.create('Ems.view.messages.NewMessageWindow');
	                            		Gam.Msg.showWaitMsg();

	                            		
	                            		Ext.Ajax.request({

	                            			url: me.ns + '/save', jsonData: {

	                            				records: dataFormArrayDecoded
	                            			},
	                            			success: function (response) {
	                            				Gam.Msg.hideWaitMsg();
	                            				if(response.status == "200"){
	                            					
//	                            					win.close();
	                            					btn.up('window').close();
	                            					grid.getStore().load();
	                            				} else {
	                            					Gam.Msg.hideWaitMsg();
	                            					Tools.errorMessageClient(response.statusText);
	                            				}
	                            			},
	                            			failure: function (resp) {
	                            				Gam.Msg.showWaitMsg();
	                            				Tools.errorFailure();
	                            			}
	                            		});
	                            		Ext.create('Ems.store.TempMessagesStore');
	                            	}
	                            	else{
	                            		Tools.errorMessageClient('لطفا مقصد را انتخاب نمایید');
	                            		return;
	                            	}

	                            },
	                            doDeleteMessage :  function (grid, rowIndex) {
	                    	        var me = this,
	                	            store = grid.getStore();
	
		                	        var msg = " آیا از حذف پیام  اطمینان دارید ";
	
		                	        var fn = function () {
		                	            me.deleteRecord(store, rowIndex);
		                	        };
	
		                	        Tools.messageBoxConfirm(msg, fn);
		                	    },
		                	    deleteRecord: function (store, rowIndex) {
		                	        var me = this;
		                	      
		                	        record = store.getAt(rowIndex),
		                            rowID = record.get(EmsObjectName.messages.id);
		                	        
		                	        Ext.Ajax.request({
		                	            url: me.ns + '/delete',
		                	            jsonData: {
		                	                ids: rowID
		                	            },
		                	            success: function (response) {
		                	                var rec = Ext.decode(response.responseText);
		                	                if (rec.success) {
		                	                	var grid =  Ext.getCmp('messagesgrid');
		                	                	grid.getStore().load();
		                	                }
		                	            },
		                	            failure: function () {
		                	                Tools.errorFailure();
		                	            }
		                	        });
		                	    },
	                            doViewing: function (grid, rowIndex) {
	                            	var messageID = grid.getStore().getAt(rowIndex).get('id'),
	                            	me = this;
	                            	me.loadForm(messageID,'view');        	

	                            }, doEditing: function (grid, rowIndex) {
	                            	var selectedItem = grid.getStore().getAt(rowIndex),
	                            	me = this;
//	                            	selectedItemID = grid.getStore().getAt(rowIndex).get(EmsObjectName.messages.id);
	                            	me.loadForm(selectedItem.data.id , 'edit');


	                            },
	                            getBtnRemoveProvince: function () {

	            	            	selectedPerson = new Array();
	            	            	selectedDepartment = new Array();
	            	            	selectedProvince= new Array();

	                            	
	                            	var multiSelectDepartment = Ext.getCmp('departmentMultiSelectds');
//	            	                this.BtnRemove(multiSelectDepartment);
	                            	multiSelectDepartment.getStore().removeAll();
	                            	
	                            	var multiSelectPerson = Ext.getCmp('personMultiSelectds');
//	                            	this.BtnRemove(multiSelectPerson);
	                            	multiSelectPerson.getStore().removeAll();

	            	                var multiSelect = Ext.getCmp('provinceMultiSelectds');
//	            	                this.BtnRemove(multiSelect);
	            	                multiSelect.getStore().removeAll();
	            	                
	            	                Ext.getCmp(EmsObjectName.messages_ID.departmentName).setDisabled(false);
    		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(false);
	            	                
	            	            },
	            	            getBtnRemovePerson: function () {
	            	            	selectedPerson = new Array();

	            	            	var multiSelect = Ext.getCmp('personMultiSelectds');
	            	                multiSelect.getStore().removeAll();
	            	                
//	            	                this.BtnRemove(multiSelect);
	            	            },
	            	            getBtnRemoveDepartment: function () {
	            	            	selectedPerson = new Array();
	            	            	selectedDepartment = new Array();

	            	            	var multiSelectPerson = Ext.getCmp('personMultiSelectds');
//	            	            	this.BtnRemove(multiSelectPerson);
	            	            	multiSelectPerson.getStore().removeAll();
	            	            	
	            	                var multiSelect = Ext.getCmp('departmentMultiSelectds');
//	            	                this.BtnRemove(multiSelect);
	            	                multiSelect.getStore().removeAll();
	            	                
    		        				Ext.getCmp(EmsObjectName.messages_ID.personName).setDisabled(false);
	            	            },
	            	            
	            	            //hossein start
	            	            getCancelMessage: function (btn) {
	            	            	
	            	            	Gam.Msg.showWaitMsg();

	            	            	Ext.Ajax.request({
                            			url: this.ns + '/clearAttachment', jsonData: {
                            			},
                            			success: function (response) {
                            				if(response.status == "200"){
//                            					win.close();
                            					Gam.Msg.hideWaitMsg();
//                            					btn.up('window').close();
//                            					grid.getStore().load();s
                            				} else {
                            					Gam.Msg.hideWaitMsg();
                            					Tools.errorMessageClient(response.statusText);
                            				}
                            			},
                            			failure: function (resp) {
                            				Gam.Msg.showWaitMsg();
                            				Tools.errorFailure();
                            			}
                            		});
	            	            },
	            	            //hossein end
	            	            
	            	            BtnRemove: function (obj) {
	            	                var selectCheckBox = obj.items.items[0].selModel.selected;
	            	                var lengthRec = selectCheckBox.length;
	            	                for (var i = 0; i < lengthRec; i++) {
	            	                    var records = selectCheckBox.items[0];
	            	                    obj.getStore().remove(records);
	            	                    
	            	                }
//	            	                this.setValueHiddenFields(obj.id);
	            	            },
	            	            setValueHiddenFields: function (objName) {

	            	                var me = this;
	            	                if (objName === Tools.trim('roleMultiSelectds')) {
	            	                    var userRoles = this.getUserRoleMultiSelect().down('form').down('multiselect'),
	            	                        storeUserRole = userRoles.getStore();
	            	                    var userForm_t = Ext.getCmp(EmsObjectName.userForm.userRole);
	            	                    userForm_t.setValue('');

	            	                    Ext.each(storeUserRole.data.items, function (items) {
	            	                        var r = items.get('id');
	            	                        me.setRoles(r);
	            	                    });

	            	                } else if (objName === Tools.trim('provinceMultiSelectds')) {

	            	                    var userAccess = this.getUserAccessMultiSelect().down('form').down('multiselect'),
	            	                        storeUserAccess = userAccess.getStore();
	            	                    Ext.getCmp(EmsObjectName.userForm.userAccess).setValue('');

	            	                    Ext.each(storeUserAccess.data.items, function (items) {
	            	                        var r = items.get('id');
	            	                        me.setAccesses(r);
	            	                    });
	            	                }
	            	            },
	                            doDownloadHelp : function(grid, rowIndex){
	                    	    	me = this;
	                    	    	
	                    	    	console.log(grid.getStore().getAt(rowIndex).get("id"));
	                    	    	try {
	                    	            if (Ext.get('downloadIframe')) {
	                    	                Ext.destroy(Ext.get('downloadIframe'));
	                    	            }
	                    	        }
	                    	        catch (e) {
	                    	            console.info(e);
	                    	        }
	                    	
	                    	        Ext.DomHelper.append(document.body, {
	                    	            tag: 'iframe',
	                    	            id: 'downloadIframe',
	                    	            frameBorder: 0,
	                    	            width: 0,
	                    	            height: 0,
	                    	            css: 'display:none;visibility:hidden;height:0px;',
	                    	            src: me.nsDownload+'/downloadAttachedFile?id=' +
	                    	                grid.getStore().getAt(rowIndex).get("id")
	                    	        });
	                    	    },
	                    	    loadForm: function (recordID, mode) {
	                            	me = this;
	                            	var win , panel  ,messageInfo , messageAddress, messageBody,displayAddressFields ,destGrid;

	                            	switch (mode) {
	                            	case 'view':
	                            	{			
	                            		win = Ext.create('Ems.view.messages.View.NewMessageWindow');
	                            		panel =  win.down('messagesdialogView');
	                            		messageInfo = panel.down('messageneweditmessageInfoView');
	                            		messageAddress = messageInfo.down('messageneweditmessageAddressView');
	                            		messageBody = messageInfo.down('messageneweditmessageBodyView');
	                            		messageRadioGroup = messageInfo.down('messageRadioReadOnlyGroup');
	                            		displayAddressFields =panel.down('messageneweditCueMessagesView');
	                            		break;

	                            	}
	                            	case 'edit':{
	                            		win = Ext.create('Ems.view.messages.NewMessageWindow');
	                            		panel =  win.down('.messagesdialog');
	                            		messageInfo = panel.down('messageneweditmessageInfo');
	                            		messageAddress = messageInfo.down('messageneweditmessageAddress');
	                            		messageBody = messageInfo.down('messageneweditmessageBody');
	                            		displayAddressFields = panel.down('messageneweditCueMessages');
	                            		break;

	                            	}

	                            	default:
	                            		break;
	                            	}
	                            	Ext.Ajax.request({
	                            		url: me.ns + '/load', jsonData: {
	                            			ids: recordID
	                            		}, success: function (resp) {

	                            			var data = Ext.decode(resp.responseText);
	                            			var msgStatus , destinations;
	                            			if (data.success) {
	                            				
	                            				var rec = data.records;
//	                            				msgStatus = rec[0].destinations[0].messageType;
	                            				destinations = rec[0].destinations;
	                            				if (rec != null) {
	                            					Tools.MaskUnMask(win);
	                            					messageAddress.setData(Ext.create('Ems.model.MessagesModel',{
	                            						id:rec[0].id,
	                            						createDate:Gam.util.Format.date(rec[0].createDate)

	                            					}),messageAddress);

	                            					messageBody.setData(Ext.create('Ems.model.MessagesModel',
	                            							{
	                            						id:rec[0].id,
	                            						title : rec[0].title,
	                            						content  : unescape(rec[0].content)
	                            							}),messageBody);

	                            					messageRadioGroup.setData(Ext.create('Ems.model.MessagesModel',
	                            							{
	                            						id:rec[0].id,
	                            						isAll : rec[0].isAll,
	                            						isNocr  :rec[0].isNocr,
	                            						isOffice : rec[0].isOffice,
	                            						createDate:Gam.util.Format.date(rec[0].createDate),
	                            						isManager  :rec[0].isManager,
	                            						
	                            							}),messageRadioGroup);

	                            					
	                            					var destIDArray = [] ,   distID ,tmpArray = [];
	                            				
	                            					var provinceArr = rec[0].provinces;
	                            					var officesArr = rec[0].offices;
	                            					var personsArr = rec[0].persons;
	                            					var displayAddress = new String();
	                            					
	                            					for ( var iProv = 0; iProv < provinceArr.length; iProv++){
	                            						displayAddress += provinceArr[iProv].name+ " ";
	                            					}
	                            					for ( var iOffi = 0; iOffi < officesArr.length; iOffi++){
	                            						displayAddress += officesArr[iOffi].name+ " ";
	                            					}
	                            					for ( var iPers = 0; iPers < personsArr.length; iPers++){
	                            						displayAddress += personsArr[iPers].firstName+' '+personsArr[iPers].lastName+ " ";
	                            					}
                        							Ext.getCmp('CueMessagesView').add(
                									Ext.create('Ext.form.Panel', {
                										border: false,
                										items: [
                										        {
                										        	xtype: 'label',
                										        	text:displayAddress,
                										        	cls: 'label-form-field',
                										        	margin: '0 0 0 10'
                										        }
                										        ]
                									})
                							);
	                            					win.show();
//	                            					var destIDArray = [] ,   distID ,tmpArray = [];
//	                            					for ( var iDest = 0; iDest < destinations.length; iDest++) {
//	                            						var displayAddress = new String();
//	                            						distID = destinations[iDest].id;
//	                            						tmpArray.push(distID);
//	                            						switch (destinations[iDest].messageType) {
//	                            						case 'PROVINCE':{
//	                            							displayAddress = destinations[iDest].provinceName;
//	                            							break;
//	                            						}
//	                            						case 'ALL':{
//	                            							displayAddress = 'تمامی کاربران';
//	                            							break;
//	                            						}
//	                            						case 'DEPARTMENT':{
//	                            							displayAddress = destinations[iDest].provinceName+
//	                            							" - "+destinations[iDest].departmentName ;
//	                            							break;
//	                            						}
//	                            						case 'NOCR':{
//	                            							displayAddress = destinations[iDest].provinceName+
//	                            							" - "+destinations[iDest].departmentName +
//	                            							" - "+destinations[iDest].nocrofficName;
//	                            							break;
//	                            						}
//	                            						case 'OFFICE':{
//	                            							displayAddress = destinations[iDest].provinceName+
//	                            							" - "+destinations[iDest].departmentName+ 
//	                            							" - "+destinations[iDest].nocrofficName+
//	                            							" - "+destinations[iDest].officeName;
//	                            							break;
//	                            						}
//	                            						case 'PRIVATE':{
//	                            							displayAddress = destinations[iDest].provinceName+
//	                            							" - "+destinations[iDest].departmentName +
//	                            							" - "+destinations[iDest].nocrofficName+
//	                            							" - "+destinations[iDest].officeName+
//	                            							" - "+destinations[iDest].personName;
//	                            							break;
//	                            						}
//	                            						default:
//	                            							break;
//	                            						} 
//	                            						win.show();
//
//	                            						switch (mode) {
//	                            						case 'view':
//	                            						{
//	                            							Ext.getCmp('CueMessagesView').add(
//	                            									Ext.create('Ext.form.Panel', {
//	                            										border: false,
//	                            										items: [
//	                            										        {
//	                            										        	xtype: 'label',
//	                            										        	text:displayAddress,
//	                            										        	cls: 'label-form-field',
//	                            										        	margin: '0 0 0 10'
//	                            										        }
//	                            										        ]
//	                            									})
//	                            							);
//
////	                            							displayAddressFields.add(
////	                            							Ext.create('Ext.form.Panel', {
////	                            							border: false,
////	                            							items: [
////	                            							{
////	                            							xtype: 'label',
////	                            							text:displayAddress,
////	                            							cls: 'label-form-field',
////	                            							margin: '0 0 0 10'
////	                            							}
////	                            							],
////	                            							})
////	                            							);
//	                            							break;
//	                            						}
//	                            						case 'edit':
//	                            						{
//	                            							displayAddressFields.add(
//	                            									Ext.create('Ext.form.Panel', {
//	                            										border: false,
//	                            										items: [
//	                            										        {
//	                            										        	xtype: 'label',
//	                            										        	text:displayAddress,
//	                            										        	cls: 'label-form-field',
//	                            										        	margin: '0 0 0 10'
//	                            										        }
//	                            										        ],
//	                            									})
//
////	                            									Ext.create('Ext.form.Label', {
////	                            									text:displayAddress,
////	                            									id:distID,
////	                            									cls: 'label-form-field'
////	                            									}),
////	                            									{ xtype: 'tbspacer', width: 50 }
//	                            							);
//	                            							break;
//	                            						}
//
//	                            						default:
//	                            							break;
//	                            						}
//	                            					}
	                            				}else {
	                            					Tools.errorMessageClient(Ems.ErrorCode.client.EMS_C_004);
	                            				}
	                            			} else {
	                            				Tools.errorMessageServer(data.messageInfo);
	                            			}

	                            		}, failure: function (resp) {
	                            			Tools.errorFailure();
	                            		}
	                            	});
	                            }

});
