/**
 * Created with IntelliJ IDEA. User: Mousavi Date: 7/10/12 Time: 2:14 PM To
 * change this template use File | Settings | File Templates.
 */
Ext
		.define(
				'Ems.view.viewport.Report',
				{
					extend : 'Ext.panel.Panel',
					layout : 'fit',

					// stateId:'wReportMenu',
					// stateful:false,

					alias : 'widget.report',
					initComponent : function() {
						this.items = [ this.getItemsReport() ];
						this.callParent(arguments);
					},

					getItemsReport : function() {
						return ({
							xtype : 'buttongroup',
							columns : 7,
							defaults : {
								scale : 'large',
								iconAlign : 'top',
								width : 75
							},
							items : [
									{
										xtype : 'splitbutton',
										action : 'BizLog',
										text : 'مديريت سيستم',
										iconCls : 'toolbar-bizLog',
										scale : 'large',
										rowspan : 3,
										iconAlign : 'top',
										arrowAlign : 'bottom',
										id : 'ReportMenuBizLogManager',
										width : 90,
										menu : [
												{
													tooltip : 'رویدادها'
															+ '   < Ctrl+Shift+E >',
													text : 'رویدادها',
													action : 'BizLog',
													icon : 'resources/themes/images/default/toolbar/bizLogItem.png',
													id : 'ReportMenuBizLogManagerBizLog'
												},
												{
													tooltip : 'کارها'
															+ '   < Ctrl+Shift+J >',
													text : 'کارها',
													action : 'JobList',
													icon : 'resources/themes/images/default/toolbar/jobConfig.png',
													stateful : false,
													id : 'ReportMenuBizLogManagerJobList'
												},
												{
													text : 'تنظیمات سیستم',
													tooltip : 'تنظیمات سیستم'
															+ '   < Ctrl+Shift+C >',
													action : 'SystemProfileList',
													icon : 'resources/themes/images/default/toolbar/setting.png',
													id : 'wReportMenuBizLogManagerSystemProfileList'
												},
												{
													tooltip : 'راهنمای کاربران CCOS',
													text : 'راهنما',
													action : 'HelpFileList',
													icon : 'resources/themes/images/default/toolbar/iconRibonHelp.png',
													id : 'ReportMenuBizLogManagerHelpFileList'
												},
												{
													text : 'درخواست های توکن',
													tooltip : 'درخواست های توکن',
													action : 'TokenRequestList',
													icon : 'resources/themes/images/default/toolbar/setting.png',
													id : 'wReportMenuBizLogManagerTokenRequest'
												} ]
									},
									{
										tooltip : 'درخواستها'
												+ '   < Ctrl+Shift+R >',
										text : 'درخواستها',
										action : 'CardRequestList',
										icon : 'resources/themes/images/default/toolbar/index.png',
										id : 'ReportMenuBizLogManagerCardRequestList'
									},
									{
										tooltip : 'مدیریت درخواست هایی که در صدور با خطا مواجه شدند',
										text : 'خطاهای صدور',
										action : 'CmsErrorEvaluateList',
										icon : 'resources/themes/images/default/toolbar/cms_error.png',
										id : 'ReportMenuBizLogManagerCmsErrorEvaluateList',

									},
									{
										tooltip : 'مدیریت درخواست هایی که استعلام آن ها منفی شده است',
										icon : 'resources/themes/images/default/toolbar/estelam2_error.png',
										id : 'ReportMenuBizLogManagerEstelam2FalseList',
										text : 'استعلام های منفی',
										width : 100,
										action : 'Estelam2FalseList',

									},

									{
										tooltip : 'تعطیلات',
										text : 'تعطیلات',
										action : 'HolidayList',
										icon : 'resources/themes/images/default/toolbar/tatil.png',
										id : 'ReportMenuBizLogManagerHolidayList'
									},
									{
										xtype : 'splitbutton',
										tooltip : 'درخواست گزارشات'
												+ '   < Ctrl+Alt+R >',
										text : 'گزارشات',
										action : 'ReportRequest',
										icon : 'resources/themes/images/default/toolbar/reports-request.png',
										id : 'reportsMenu',
										iconAlign : 'top',
										arrowAlign : 'bottom',
										menu : [
												{
													tooltip : 'مدیریت گزارشات'
															+ '   < Ctrl+Alt+I >',
													text : 'مدیریت گزارشات',
													action : 'ManageReports',
													icon : 'resources/themes/images/default/toolbar/reports.png',
													id : 'manageReports'
												},
												{
													tooltip : 'درخواست گزارش'
															+ '   < Ctrl+Alt+R >',
													text : 'درخواست گزارش',
													action : 'ReportRequest',
													icon : 'resources/themes/images/default/toolbar/reports-request.png',
													id : 'reportRequest'
												},
												{
													tooltip : 'دمشاهده نتیجه گزارش'
															+ '   < Ctrl+Alt+P >',
													text : 'مشاهده نتیجه گزارش',
													action : 'ReportResult',
													icon : 'resources/themes/images/default/toolbar/reports-result.png',
													id : 'reportResult'
												} ]
									},
									{
										tooltip : 'مدیریت پیام ها',
										text : 'مدیریت پیام ها',
										action : 'Messages',
										icon : 'resources/themes/images/default/toolbar/iconRibonNotification.png',
										id : 'ReportMenuBizLogManagerMessages'
									}

							],
							listeners : {
								render : function(view) {

									// view.items.items[0].stateId = 'w' +
									// view.items.items[0].id;
									// view.items.items[0].stateful = false;

									// view.items.items[0].menu.items.items[0].stateId
									// = 'w' +
									// view.items.items[0].menu.items.items[0].id;
									// view.items.items[0].menu.items.items[0].stateful
									// = false;

									// view.items.items[0].menu.items.items[1].stateId
									// = 'w' +
									// view.items.items[0].menu.items.items[1].id;
									// view.items.items[0].menu.items.items[1].stateful
									// = false;

									// view.items.items[0].menu.items.items[2].stateId
									// = 'w' +
									// view.items.items[0].menu.items.items[2].id;
									// view.items.items[0].menu.items.items[2].stateful
									// = false;

									// view.items.items[0].menu.items.items[3].stateId
									// = 'w' +
									// view.items.items[0].menu.items.items[3].id;
									// view.items.items[0].menu.items.items[3].stateful
									// = false;

									/*
									 * view.items.items[1].stateId =
									 * 'w'+view.items.items[1].id;
									 * view.items.items[1].stateful = false;
									 * 
									 * view.items.items[1].menu.items.items[0].stateId=
									 * 'w'+view.items.items[1].menu.items.items[0].id;
									 * view.items.items[1].menu.items.items[0].stateful=
									 * false;
									 * 
									 * view.items.items[1].menu.items.items[1].stateId=
									 * 'w'+view.items.items[1].menu.items.items[1].id;
									 * view.items.items[1].menu.items.items[1].stateful=
									 * false;
									 */

									/*
									 * if(view.stateful) { var stateProvider =
									 * Ext.create('Ems.state.Provider');
									 * stateProvider.setDetail(view.items.items[0].stateId
									 * ,1);
									 * stateProvider.setDetail(view.items.items[1].stateId
									 * ,1);
									 * 
									 * stateProvider.saveing();
									 *  }
									 */

								}
							}
						});
					}

				});
