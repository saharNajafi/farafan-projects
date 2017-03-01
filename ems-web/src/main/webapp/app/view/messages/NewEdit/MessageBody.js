/**
 * Created with IntelliJ IDEA. User: Mousavi Date: 6/24/12 Time: 5:00 PM To
 * change this template use File | Settings | File Templates.
 **/
Ext.define('Ems.view.messages.NewEdit.MessageBody',
				{
					id:'MessageBody',
					extend : 'ICT.form.FieldSet',
					alias : 'widget.messageneweditmessageBody',
					

					contentStyle : function() {
						return (Tools.user.StyleObject);
					},
					initComponent : function() {

						var me = this;
						
						me.callParent(arguments);
					},
//					layout : 'column',
					border:false,
				
					getEditableFields : function() {
							return [
							        {
										fieldLabel : 'عنوان',
										allowBlank : false,
										xtype:'textfield',
										labelWidth:30,
										id :  EmsObjectName.messages_ID.title,
										itemId:  EmsObjectName.messages.title,
										name : EmsObjectName.messages.title,
										maxLength : 32,
										width:800,
										bodyStyle : this.contentStyle(),
										style : this.contentStyle()
									},
									{
								        xtype: 'htmleditor',
								        enableColors: true,
								        enableAlignments: true,
								        enableLists: false,
								        id :  EmsObjectName.messages_ID.content,
								        itemId:  EmsObjectName.messages.content,
										name : EmsObjectName.messages.content,
								        width:800,
								        height:140
									},
//									{
//
//										xtype:'button',
//										text: 'آماده سازی برای ارسال',
//										scale:'small',
////										margin: 50,
////										dock :'left',
//										handler:function(){
//											if(Ext.getCmp(EmsObjectName.messages_ID.title).getValue() && Ext.getCmp(EmsObjectName.messages_ID.content).getValue()){
//											
////											if(Ext.getCmp(EmsObjectName.messages_ID.provinceName).getValue() || Ext.getCmp(EmsObjectName.messages_ID.msgStatus).lastValue=='ALL')
//											{
//												
//												var tmpStore = Ext.getStore('TempMessageStore');
//												if(!tmpStore)
//													tmpStore = Ext.create('Ems.store.TempMessagesStore');
//												
//												var formData = new Object();
////												var displayAddress = new String();
//												formData.title = Ext.getCmp(EmsObjectName.messages_ID.title).getValue();
//												formData.content = Ext.getCmp(EmsObjectName.messages_ID.content).getValue();
//												formData.isAll = Ext.getCmp(EmsObjectName.messages.isAll).getValue();
//												formData.isManager = Ext.getCmp(EmsObjectName.messages.isManager).getValue();
//												formData.isOffice = Ext.getCmp(EmsObjectName.messages.isOffice).getValue();
//												formData.isNocr = Ext.getCmp(EmsObjectName.messages.isNocr).getValue();
//												formData.isAllOffice = Ext.getCmp(EmsObjectName.messages.isAllOffice).getValue();
//												if(formData.isAllOffice){
//													formData.isOffice = true;
//													formData.isNocr = true;
//												}
//												
//												
////												formData.msgPriority = Ext.getCmp(EmsObjectName.messages_ID.msgPriority).value;
////												formData.messageType = Ext.getCmp(EmsObjectName.messages_ID.msgStatus).rawValue;
//												Ext.getCmp(EmsObjectName.messages_ID.title).setDisabled(true);
//												Ext.getCmp(EmsObjectName.messages_ID.content).setReadOnly(true);
//												Ext.getCmp(EmsObjectName.messages_ID.content).getToolbar().hide(false);
//												
////												switch (Ext.getCmp(EmsObjectName.messages_ID.msgStatus).lastValue) {
////												case 'ALL':{
////													displayAddress = 'تمامی کاربران';
////													break;
////												}
////												case 'PROVINCE':{
////													formData.provinceName = Ext.getCmp(EmsObjectName.messages_ID.provinceName).rawValue;
////													formData.provinceId = Ext.getCmp(EmsObjectName.messages_ID.provinceName).value;
////													displayAddress = formData.provinceName;
////													break;
////												}
////												case 'DEPARTMENT':{
////													if(Ext.getCmp(EmsObjectName.messages_ID.departmentName).rawValue && 
////															Ext.getCmp(EmsObjectName.messages_ID.provinceName).value){
////														formData.provinceName = Ext.getCmp(EmsObjectName.messages_ID.provinceName).rawValue; 
////														formData.departmentName = Ext.getCmp(EmsObjectName.messages_ID.departmentName).rawValue; 
////														formData.provinceId = Ext.getCmp(EmsObjectName.messages_ID.provinceName).value; 
////														formData.departmentId = Ext.getCmp(EmsObjectName.messages_ID.departmentName).value; 
////														
////														displayAddress = formData.provinceName+" - "+formData.departmentName ;
////													}else{
////														Gam.window.MessageManager.showInfoMsg('لطفا آدرس را کامل وارد نمایید ');	
////													}
////													
////													break;
////												}
////												case 'NOCR':{
////													
////													if((Ext.getCmp(EmsObjectName.messages_ID.departmentName).rawValue)
////														&& Ext.getCmp(EmsObjectName.messages_ID.provinceName).value
////														&&(Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).value)){
////															
////															formData.provinceName = Ext.getCmp(EmsObjectName.messages_ID.provinceName).rawValue; 
////															formData.departmentName = Ext.getCmp(EmsObjectName.messages_ID.departmentName).rawValue; 
////															formData.nocrofficName = Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).rawValue; 
////															
////															formData.provinceId = Ext.getCmp(EmsObjectName.messages_ID.provinceName).value; 
////															formData.departmentId = Ext.getCmp(EmsObjectName.messages_ID.departmentName).value; 
////															formData.nocrofficId = Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).value; 
////															displayAddress = formData.provinceName+" - "+formData.departmentName + " - "+formData.nocrofficName;
////														
////														}else{
////															Gam.window.MessageManager.showInfoMsg('لطفا آدرس را کامل وارد نمایید ');
////														}
////													break;
////												}
////												case 'OFFICE':{
////													if((Ext.getCmp(EmsObjectName.messages_ID.departmentName).rawValue)
////															&& (Ext.getCmp(EmsObjectName.messages_ID.provinceName).value)//{
////															&&(Ext.getCmp(EmsObjectName.messages_ID.officeName).value)
////															&&(Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).value)){
////														
////														formData.provinceName = Ext.getCmp(EmsObjectName.messages_ID.provinceName).rawValue; 
////														formData.departmentName = Ext.getCmp(EmsObjectName.messages_ID.departmentName).rawValue; 
////														formData.nocrofficName = Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).rawValue; 
////														formData.officeName = Ext.getCmp(EmsObjectName.messages_ID.officeName).rawValue; 
////														
////														formData.provinceId = Ext.getCmp(EmsObjectName.messages_ID.provinceName).value; 
////														formData.departmentId = Ext.getCmp(EmsObjectName.messages_ID.departmentName).value; 
////														formData.nocrofficId = Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).value; 
////														formData.officeId = Ext.getCmp(EmsObjectName.messages_ID.officeName).value; 
////														displayAddress = formData.provinceName+" - "+formData.departmentName + " - "+formData.nocrofficName+" - "+formData.officeName;
////													}else{
////														Gam.window.MessageManager.showInfoMsg('لطفا آدرس را کامل وارد نمایید ');
////													}
////													break;
////												}
////												case 'PRIVATE':{
////													if((Ext.getCmp(EmsObjectName.messages_ID.departmentName).rawValue)
////															&&(Ext.getCmp(EmsObjectName.messages_ID.provinceName).value)
////															&&(Ext.getCmp(EmsObjectName.messages_ID.personName).value)
////															){
////														
////														formData.provinceName = Ext.getCmp(EmsObjectName.messages_ID.provinceName).rawValue; 
////														formData.departmentName = Ext.getCmp(EmsObjectName.messages_ID.departmentName).rawValue; 
////														formData.nocrofficName = Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).rawValue; 
////														formData.officeName = Ext.getCmp(EmsObjectName.messages_ID.officeName).rawValue; 
////														formData.personName = Ext.getCmp(EmsObjectName.messages_ID.personName).rawValue; 
////														
////														formData.provinceId = Ext.getCmp(EmsObjectName.messages_ID.provinceName).value; 
////														formData.departmentId = Ext.getCmp(EmsObjectName.messages_ID.departmentName).value; 
////														formData.nocrofficId = Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).value; 
////														formData.officeId = Ext.getCmp(EmsObjectName.messages_ID.officeName).value; 
////														formData.personId = Ext.getCmp(EmsObjectName.messages_ID.personName).value; 
////														displayAddress = formData.provinceName+" - "+formData.departmentName + " - "+formData.nocrofficName+" - "+formData.officeName+" - "+formData.personName;
////													}else{
////														Gam.window.MessageManager.showInfoMsg('لطفا آدرس را کامل وارد نمایید ');	
////													}
////													break;
////												}
////												default:
////													break;
////												}
//												
//												if(displayAddress.length > 0){
////													var itemID = Math.floor((Math.random() * displayAddress.length) + 1);
//													var itemID = displayAddress;
//													formData.id = itemID;
//													var btnID = itemID;
////												if(tmpStore.data.indexOf(tmpStore.getById(displayAddress)) == -1){
//													tmpStore.add(formData);
////														Ext.getCmp('CueMessages').add(
////																Ext.create('Ext.form.Panel', {
////																	border: false,
////																	layout : 'hbox',
////																	items: [
////																	        {
////																	        	xtype:'button',
////																	        	tooltip:'حذف',
////																	        	iconCls: 'delete-btn',
////																	        	id : "button-"+btnID,
////																	        	handler: function() {
////																	        		var btn = this;
////																	        		tmpStore.removeAt(tmpStore.data.indexOf(tmpStore.getById(btnID)));
////																	        		btn.up().close();
////																	        	}
////																	        },
////																	        {
////																	        	xtype: 'label',
////																	        	text:displayAddress,
////																	        	cls: 'label-form-field',
////																	        	margin: '0 0 0 10'
////																	        }
////																	        ]
////																})
////														);
////														
////													}
//												}
////												Ext.getCmp('CueMessages').doLayout();
//											
//												
//											}
////											else{
////												Gam.window.MessageManager.showInfoMsg('لطفا آدرس را کامل وارد نمایید ');	
////												// Reset form
////												Ext.getCmp(EmsObjectName.messages_ID.provinceName).setValue("");
////												Ext.getCmp(EmsObjectName.messages_ID.departmentName).setValue("");
////												Ext.getCmp(EmsObjectName.messages_ID.nocrofficName).setValue("");
////												Ext.getCmp(EmsObjectName.messages_ID.officeName).setValue("");
////												Ext.getCmp(EmsObjectName.messages_ID.personName).setValue("");
////											}
//											///
//											
//											}else{
//												Gam.window.MessageManager.showInfoMsg('لطفا عنوان و متن را وارد نمایید');
//											}
//											
//										}
//									}
									];
						}
				});
