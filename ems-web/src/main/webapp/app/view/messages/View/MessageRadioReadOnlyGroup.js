/**
 * Created with IntelliJ IDEA. User: Mousavi Date: 6/24/12 Time: 5:00 PM To
 * change this template use File | Settings | File Templates.
 **/
Ext.define('Ems.view.messages.View.MessageRadioReadOnlyGroup',
				{
					id:'MessageRadioReadOnlyGroupView',
					extend : 'ICT.form.FieldSet',
					alias : 'widget.messageRadioReadOnlyGroup',
					 layout:'column',

					contentStyle : function() {
						return (Tools.user.StyleObject);
					},
					initComponent : function() {

						var me = this;
						me.defaults = {
				    			columnWidth : 1 / 6
				    	};
						me.callParent(arguments);
					},
//					layout : 'column',
					border:false,
					isReadOnly : function(){
						return true;
					},
				 getReadOnlyFields : function() {
						return [
				                {
				                	xtype: 'checkboxfield',
				    				boxLabel: 'كل سازمانها',
				    				boxLabelAlign: 'after',
//				    				labelWidth:'10',
				    				id: EmsObjectName.messages.isAll,
				    				itemId: EmsObjectName.messages.isAll,
				    				name: EmsObjectName.messages.isAll,
				    				readOnly: true,
				    				listeners: {}
				                },
				                {
				                	xtype: 'checkboxfield',
				    				boxLabel: 'دفاتر دولتی',
				    				boxLabelAlign: 'after',
//				    				labelWidth:'10',
				    				id: EmsObjectName.messages.isNocr,
				    				itemId: EmsObjectName.messages.isNocr,
				    				name: EmsObjectName.messages.isNocr,
				    				readOnly: true,
				    				listeners: {}
				                },
				                {
				                	xtype: 'checkboxfield',
				    				boxLabel: 'دفاتر خصوصی',
				    				boxLabelAlign: 'after',
//				    				labelWidth:'10',
				    				id: EmsObjectName.messages.isOffice,
				    				itemId: EmsObjectName.messages.isOffice,
				    				name: EmsObjectName.messages.isOffice,
				    				readOnly: true,
				    				listeners: {}
				                },
//				                {
//				                	xtype: 'radiogroup',
//				            		id: '_idMessageProvinceForm',
//				            		border: false,
//				            		bodyBorder: false,
//				            		fieldLabel: 'نوع دفتر',
//				            		columns : 1,
//				            		items:[
//				            		       {boxLabel: 'همه',	readOnly: true, name: 'officeType', inputValue: 'ALLOFFICE', id: EmsObjectName.messages.isAllOffice,itemId: EmsObjectName.messages.isAllOffice,labelWidth:'5',checked:true},
//				            		       {boxLabel: 'خصوصی',readOnly: true, name: 'officeType', inputValue: 'NOCR', id: EmsObjectName.messages.isOffice,itemId: EmsObjectName.messages.isOffice,labelWidth:'5',},
//				            		       {boxLabel: 'دولتی',	readOnly: true, name: 'officeType', inputValue: 'OFFICE', id: EmsObjectName.messages.isNocr,itemId: EmsObjectName.messages.isNocr,labelWidth:'5',}
//				            		       ]
//				                	
//				                },
				                {
						        	xtype: 'checkboxfield',
						        	boxLabel: '  مدیر',
//						        	labelWidth:'5',
						        	boxLabelAlign: 'after',
						        	id: EmsObjectName.messages.isManager,
						        	itemId: EmsObjectName.messages.isManager,
						        	name:EmsObjectName.messages.isManager,
						        	readOnly: true
						        },
				        
						        {
									fieldLabel : 'تاریخ',
									xtype:'displayfield',
//									labelWidth:35,
									id : EmsObjectName.messages_ID.createDate,
									itemId:  EmsObjectName.messages.createDate,
									name : EmsObjectName.messages.createDate
									
								},
						        
				    	        ];
					}
				});
