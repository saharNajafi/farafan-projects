Ext.define('Ems.view.messages.Grid', {
	extend: 'Gam.grid.Crud',

	alias: 'widget.messagesgrid',

	requires: [
	           'Ems.store.MessagesStore',
	           'Ems.view.messages.Dialog',
	           'Ems.view.messages.View.Dialog',
	           'Ems.view.messages.NewMessageWindow'
	           , 'Ems.view.messages.View.NewMessageWindow'
	           ,'Ems.view.messages.View.MessageInfo'
	           ,'Ems.view.messages.MessagePriority'
	           ,'Ems.store.GridCueMessageStore'
	           ,'Ems.view.messages.NewEdit.GridCueMessage'
	           ],

	           stateId: 'wMessageGrid',

	           id: 'grdMessages',

	           title: 'مدیریت پیام ها',

	           multiSelect: false,


	           store: {type: 'messagesstore'},
	           actions: ['gam.delete'],
	           actionColumnItems: [
	                               {
	                            	   getClass: function (value, metaData, record, rowIndex, colIndex, store) {
	                            		   return 'grid-view-action-icon';
	                            	   },
	                            	   tooltip: 'مشاهده', action: 'viewing',
	                            	   stateful: true,
	                            	   stateId: this.stateId + 'Viewing'
	                               },
	                               {
                                	   getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                                		   return(record.get('preparedState')=== 'NEW' ? 'grid-reject-repeal-request-action-icon' : 'x-hide-display');
                                	   },
                                	   tooltip: 'حذف',
                                	   action: 'deleteMessage',
                                	   stateful: true,
                                	   stateId: this.stateId + 'DeleteMsg',
                                   }, {
                                  	 getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                                		 
                                  		 return (record.get('hasFile') ? 'report-download-icon': 'x-hide-display');
                                	 },
                                	 tooltip: 'دریافت فایل',
                                	 action: 'downloadHelp'
                                  }
	                               ], 
	                               tbar: [{
	                            	   action: 'Messages',
	                            	   id: 'idBtnEditorSave',
	                            	   itemId : 'btnEditorSave',
	                            	   text: 'جدید',
	                            	   xtype: 'button',
	                            	   width: 70,
	                            	   iconCls: 'add-btn',
	                            	   handler: function () {
	                            		   Ext.create('Ems.store.TempMessagesStore');
	                            		   var win = Ext.create('Ems.view.messages.NewMessageWindow');
	                            		   Tools.MaskUnMask(win);
	                            		   selectedProvince = new Array();
	                            		   selectedDepartment = new Array();
	                            		   win.show();
	                            	   },
	                               }//,
	                               // 'gam.delete'
	                               ]
	           //,selType : 'rowmodel'
	           , initComponent: function () {
	        	   this.columns = this.getColumnMessageGrid();
	        	   this.callParent(arguments);
	           }, getColumnMessageGrid: function () {
	        	   return([
	        	           {
	        	        	   xtype: 'gridcolumn',
	        	        	   width: 150,
	        	        	   text: 'عنوان',
	        	        	   dataIndex: EmsObjectName.messages.title,
	        	        	   id: EmsObjectName.messages.title,
	        	        	   filterable: true,
	        	        	   filter: true
	        	           }/*,
	                       {
	                           xtype: 'gridcolumn',
	                           width: 250,
	                           text: 'متن پیام',
	                           dataIndex: EmsObjectName.messages.msgContent,
	                           id: EmsObjectName.messages.msgContent,
	                           filterable: true,
	                           filter: true
	                       }*/,
	                       {
	                    	   xtype: 'gridcolumn',
	                    	   width: 250,
	                    	   text : 'ایجاد کننده',
	                    	   dataIndex: EmsObjectName.messages.senderUsername,
	                    	   id: EmsObjectName.messages.senderUsername,
	                    	   filterable: true,
	                    	   filter: true
	                       },
	                       {
	                    	   xtype: 'gam.datecolumn',
	                    	   width: 250,
	                    	   text: 'تاریخ ثبت',
	                    	   dataIndex: EmsObjectName.messages.createDate,
	                    	   id: EmsObjectName.messages.createDate,
	                    	   filterable: true,
	                    	   filter: {
	                    		   xtype: 'container',
	                    		   layout: {
	                    			   type: 'hbox',
	                    			   align: 'middle'
	                    		   },
	                    		   defaults: {
	                    			   labelWidth: 10,
	                    			   xtype: 'datefield',
	                    			   flex: 1
	                    		   },
	                    		   items: [
	                    		           {
	                    		        	   fieldLabel: 'از',
	                    		        	   name: 'fromDate'
	                    		           },
	                    		           {
	                    		        	   fieldLabel: 'تا',
	                    		        	   name: 'toDate'
	                    		           }
	                    		           ]
	                    	   }

	                       },
	                       {
								//	xtype: 'gridcolumn',
									filterable: true,
								    filter: {
					                    xtype: 'combo',
					                    store: {
					                        fields: ['value', 'label'],
					                        data: [
					                            {
					                                value: 'NEW',
					                                label: "جدید"
					                            },
					                            {
					                                value: 'SENT',
					                                label: "ارسال شده"
					                            }
					                        ]
					                    },
					                    queryMode: 'local',
					                    displayField: 'label',
					                    valueField: 'value'
					                },
									dataIndex : EmsObjectName.messages.preparedState,
									id : EmsObjectName.messages.preparedState,
									text : 'وضعیت',
									renderer: function (value) {
										//'PENDING_FOR_EMS', 'EMS_REJECT', 'SUSPENDED', 'READY_TO_RENEWAL_DELIVER'
					                    var result = "";
					                    if (value == "NEW") {
					                        result = "جدید";
					                    } else if (value == "SENT") {
					                        result =  "ارسال شده";
					                    } 
					                    return result;
					                },
									width: 100
								},
//	                       {
//	                       xtype: 'gridcolumn',
//	                       width: 150,
//	                       text: 'اولویت',
//	                       filterable: true,
//	                       filter: {
//	                       xtype: 'messagepriority'
//	                       },
//	                       dataIndex: EmsObjectName.messages.msgPriority,
//	                       id: EmsObjectName.messages.msgPriority,
//	                       renderer: function (value) {
//	                       if (value && typeof value === 'string') {
//	                       switch (value) {
//	                       case "IMPORTANT":
//	                       return 'مهم';
//	                       case "NOT_IMPORTANT" :
//	                       return 'غیر مهم';
//	                       }
//	                       }
//	                       }

//	                       }, 
//	                       {
//	                       xtype : 'gam.datecolumn',
//	                       dataIndex: EmsObjectName.messages.createDate,
//	                       id: EmsObjectName.messages.createDate,
//	                       text: 'تاریخ ثبت',
//	                       width: 200,
//	                       format : Ext.Date.defaultDateTimeFormat
//	                       }

//	                       {
//	                       xtype: 'gam.datecolumn',
//	                       width: 200,
//	                       text: 'تاریخ ثبت',
//	                       dataIndex: EmsObjectName.messages.createDate,
//	                       id: EmsObjectName.messages.createDate,
//	                       format: Ext.Date.defaultDateTimeFormat,

//	                       filterable: true,
//	                       filter: {
//	                       xtype: 'container',
//	                       layout: {
//	                       type: 'hbox',
//	                       align: 'middle'
//	                       },
//	                       defaults: {
//	                       labelWidth: 10,
//	                       xtype: 'datefield',
//	                       flex: 1
//	                       },
//	                       items: [
//	                       {
//	                       fieldLabel: 'از',
//	                       name: 'fromDate'
//	                       },
//	                       {
//	                       fieldLabel: 'تا',
//	                       name: 'toDate'
//	                       }
//	                       ]
//	                       }
//	                       }


	                       ]);
	           },
});
