/**
 * Created with IntelliJ IDEA. User: Mousavi Date: 6/24/12 Time: 5:00 PM To
 * change this template use File | Settings | File Templates.
 **/
Ext.define('Ems.view.messages.View.MessageAddress',
				{
					id:'MessageAdderssView',
					extend : 'ICT.form.FieldSet',
					alias : 'widget.messageneweditmessageAddressView',
					//title : 'مشخصات پیام',

					contentStyle : function() {
						return (Tools.user.StyleObject);
					},
					layout : 'column',
					border:false,
					initComponent : function() {

						var me = this;
						me.defaults = {
							columnWidth : 1 / 1
						};
						me.callParent(arguments);
					},
					isReadOnly : function(){
						return true;
					},
					getReadOnlyFields : function() {
						return [
								
//						        {
//									fieldLabel : 'اولویت',
//									xtype:'displayfield',
////									labelWidth:35,
//									id : EmsObjectName.messages_ID.msgPriority,
//									itemId:  EmsObjectName.messages.msgPriority,
//									name : EmsObjectName.messages.msgPriority,
//								     renderer: function (value) {
//						                    if (value && typeof value === 'string') {
//						                        switch (value) {
//						                        case "IMPORTANT":
//						                        	return 'مهم';
//						                        case "NOT_IMPORTANT" :
//						                        	return 'غیر مهم';
//						                        }
//						                    }
//						                }
//
//								},
//								{
//									fieldLabel : 'تاریخ',
//									xtype:'displayfield',
////									labelWidth:35,
//									id : EmsObjectName.messages_ID.createDate,
//									itemId:  EmsObjectName.messages.createDate,
//									name : EmsObjectName.messages.createDate
//									
//								}
								
								/*,
						        {
									fieldLabel : 'گیرنده پیام',
									readOnly :true,
									xtype:'textfield',
									id : EmsObjectName.messages_ID.msgStatus,
									itemId:  EmsObjectName.messages.msgStatus,
									name : EmsObjectName.messages.msgStatus
								},
								{
									fieldLabel : 'استان',
									readOnly :true,
									xtype:'textfield',
									text : 'استان',
									id : EmsObjectName.messages_ID.province,
									itemId: EmsObjectName.messages.province
								},
								{
									fieldLabel : 'اداره',
									readOnly :true,
									xtype:'textfield',
									id : EmsObjectName.messages_ID.department,
									itemId: EmsObjectName.messages.department
								},
								{
									fieldLabel : 'دفتر معین',
									readOnly :true,
									xtype:'textfield',
									id : EmsObjectName.messages_ID.nocroffic,
									itemId: EmsObjectName.messages.nocroffic
								},
								{
									fieldLabel : 'دفتر خصوصی',
									readOnly :true,
									xtype:'textfield',
									id : EmsObjectName.messages_ID.office,
									itemId: EmsObjectName.messages.office
								},
								{
									fieldLabel : 'کاربر',
									readOnly :true,
									xtype:'textfield',
									id : EmsObjectName.messages_ID.person,
									itemId :EmsObjectName.messages.person,
								}*/
								];
					}
				});
