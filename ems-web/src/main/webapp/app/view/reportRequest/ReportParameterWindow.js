Ext
		.define(
				'Ems.view.reportRequest.ReportParameterWindow',
				{
					extend : 'Ext.window.Window',

					alias : 'widget.reportparameterwindow',

					title : 'انتخاب پارامترهای گزارش ',
					reportRecord : null,
					metadata : null,
					minHeight : 100,
					width : 400,

					buttons : [ {
						text : 'ثبت درخواست',
						action : 'btnSaveReportRequest'
					}, {
						text : 'انصراف',
						action : 'btnCancelReportRequest'
					} ],

					constructor : function(config) {
						this.callParent(arguments);
					},

					initComponent : function() {

						// Adding report name as the first item
						var reportParamItems = [ {
							xtype : 'displayfield',
							fieldCls : 'ems-display-field',
							fieldLabel : 'عنوان',
							value : this.reportRecord
									.get(EmsObjectName.reportRequest.name)
						} ];

						// Adding report parameters if any exists
						if ((this.metadata) && (this.metadata.length > 0))
							reportParamItems = reportParamItems.concat(this
									.getReportParameterFileds());

						// Adding report output type selector at the end of the
						// form
						reportParamItems.push({
							xtype : 'reportoutputradiogroup',
							fieldLabel : 'نوع خروجی'
						});

						// Adding a date field to let the user specify a date to
						// schedule the report to be generated on that date
						reportParamItems
								.push({
									xtype : 'fieldset',
									id : 'jobScheduleFieldset',
									title : 'اجرای گزارش در تاریخی مشخص',
									checkboxToggle : true,
									collapsed : true,
									layout : 'form',
									items : [ {
										name : 'jobScheduleDate',
										id : 'jobScheduleDate',
										fieldLabel : 'تاریخ تولید گزارش',
										width : 350,
										allowBlank : true,
										xtype : 'datefield',
										validator : function(value) {
											if (Ext.getCmp(
													'jobScheduleFieldset')
													.getState()
													&& !Ext
															.getCmp(
																	'jobScheduleFieldset')
															.getState().collapsed) {
												if ((value == null)
														|| (value.length == 0)
														|| (this
																.parseDate(value) == null)) {
													return 'لطفا تاریخ تولید گزارش را انتخاب فرمایید. انتخاب تاریخی در گذشته و یا انتخاب روز جاری امکان پذیر نمی باشد';
												}

												var selectedDate = this
														.parseDate(value)
														.getTime();
												var currentDate = new Date();
												// The Ext.Date.clearTime
												// function is not available,
												// due to some unknown problem,
												// so clear time
												// of currentDate manually
												currentDate.setHours(0);
												currentDate.setMinutes(0);
												currentDate.setSeconds(0);
												currentDate.setMilliseconds(0);

												if (selectedDate <= currentDate) {
													return 'لطفا تاریخ تولید گزارش را انتخاب فرمایید. انتخاب تاریخی در گذشته و یا انتخاب روز جاری امکان پذیر نمی باشد';
												}
											}
											return true;
										}
									} ]
								});

						this.items = [ {
							xtype : 'gam.form',
							border : false,
							bodyStyle : {
								padding : '10px'
							},
							items : reportParamItems
						} ];

						this.callParent(arguments);
					},

					/**
					 * Given a parameter config and checks weather it's a hidden
					 * parameter for an autocomplete field or not
					 * 
					 * @param param
					 */
					isAutocompleteHiddenField : function(param) {
						for ( var i = 0; i < this.metadata.length; i++) {
							if (this.metadata[i].name != param.name) {
								if ((this.metadata[i].name + "Name") == param.name) {
									return true;
								}
							}
						}

						return false;
					},

					/**
					 * Generates list of parameter fields using report metadata
					 * to be displayed on form.
					 * 
					 * @returns {Array}
					 */
					getReportParameterFileds : function() {
						var reportParameters = [];
						for ( var i = 0; i < this.metadata.length; i++) {
							if ((this
									.isAutocompleteHiddenField(this.metadata[i]) == false)
									&& (this.metadata[i].name != 'nocrVALIDATOR')) {
								var paramItem = this
										.getParamFormItem(this.metadata[i]);
								if (paramItem != null)
									reportParameters.push(paramItem);
							}
						}

						return reportParameters;
					},

					/**
					 * Given a parameter object and creates a parameter form
					 * item base on its type and properties
					 * 
					 * @param param
					 *            The parameter object to create a form item
					 *            base on its properties
					 */
					getParamFormItem : function(param) {
						var allowBlank = false;

						// Use mandatory property of the parameter if specified
						// or make the field mandatory if no such property has
						// been defined
						if (param.properties.mandatory
								&& param.properties.mandatory == "false") {
							allowBlank = true;
						}

						var paramItem = {
							name : param.name,
							id : param.name,
							fieldLabel : param.properties.label,
							width : 350,
							allowBlank : allowBlank
						};

						var paramProperties = this
								.addParamSpecificProperteis(param);
						if (paramProperties == null) {
							// The given parameter is unknown. So ignore it
							return null;
						}

						paramItem = Ext.apply(paramItem, paramProperties);

						return paramItem;
					},

					/**
					 * Given a parameter object and returns any param specific
					 * properties to be added to the form item corresponding to
					 * that parameter
					 * 
					 * @param param
					 *            The parameter object to be used to determine
					 *            any param specific properties
					 */
					addParamSpecificProperteis : function(param) {
						var result = null;
						if ((param.properties) && (param.properties.ui)) {
							// The UI attribute has been specified by report
							// designer, so use as a basis for param GUI
							switch (param.properties.ui) {
							case 'DATE':
								var value, defaults = param.properties['default'];
								if (defaults) {
									if (defaults === 'CURRENT_DATE') {
										value = new Date();
									} else {
										value = new Date(defaults);
									}
								}
								// if(param.properties.default){
								// if(param.properties.default ===
								// "CURRENT_DATE"){
								// value = new Date();
								// } else {
								// value = new Date(param.properties.default);
								// }
								// }
								result = {
									xtype : 'datefield',
									value : value
								};
								break;
							case 'NID':
								result = {
									xtype : 'textfield',
									value : param.value ? param.value : "",
									vtype : 'numeric',
									enforceMaxLength : true,
									maxLength : 10,
									minLength : 10,
									maskRe : /\d/i
								};
								break;
							// case 'EOF_COMBO':
							// result = {
							// xtype : 'textfield',
							// value: param.value ? param.value : ""
							// };
							// break;
							case 'DEP_COMBO':
								result = {
									xtype : 'personDepartmentsCombo',
									// xtype :
									// 'cmbSuperregisterofficeautocomplete',
									hiddenName : param.name + "Name",
									name : param.name,
									value : param.value ? param.value : ""
								};
								break;
							// case 'USERNAME_COMBO':
							// result = {
							// xtype : 'textfield',
							// value: param.value ? param.value : ""
							// };
							// break;
							// case 'LOCATION_COMBO':
							// result = {
							// xtype : 'textfield',
							// value: param.value ? param.value : ""
							// };
							// break;
							// case 'PERSON_COMBO':
							// result = {
							// xtype : 'textfield',
							// value: param.value ? param.value : ""
							// };
							// break;
								
							case 'PRO_COMBO':
								result = {
									xtype : 'provincesCombo',
									hiddenName : param.name + "Name",
									name : param.name,
									value : param.value ? param.value : ""
								};
								break;

							case 'BIRTH_DATE':
								result = {
									xtype : 'textfield',
									value : param.properties['default'],
									enforceMaxLength : true,
									maxLength : 4,
									minLength : 4,
									maskRe : /\d/i
								};
								break;

							case 'RANGE':
								result = {
									xtype : 'textfield',
									value : param.properties['default'],
									maskRe : /\d/i
								};
								break;

							case 'STATE_COMBO':
								result = {
									xtype : 'cardrequeststatecombobox',
									hiddenName : param.name + "Name",
									name : param.name,
									value : param.value ? param.value : ""
								};
								break;
							}
						} else {
							// Define the xtype base on type attribute of the
							// parameter (java.util.Date, java.lang.String,
							// etc.)
							switch (param.type) {
							case 'java.util.Date':
								result = {
									xtype : 'datefield',
									value : param.value ? param.value : ""
								};
								break;
							case 'java.lang.String':
								result = {
									xtype : 'textfield',
									value : param.value ? param.value : ""
								};
								break;
							default:
								xtype = 'textfield';
							}
						}
						return result;
					},

					/**
					 * Given a parameter object and returns an appropriate xtype
					 * base on its properties
					 * 
					 * @param param
					 *            The parameter to specify its xtype
					 */
					getParameterXType : function(param) {
						var xtype = 'textfield';
						if ((param.properties) && (param.properties.ui)) {
							switch (param.properties.ui) {
							case 'DATE':
								return 'datefield';
								break;
							case 'NID':
								return 'textfield';
								break;
							case 'BIRTH_DATE':
								return 'textfield';
								break;
							case 'RANGE':
								return 'textfield';
								break;
							default:
								xtype = 'textfield';
							}
						} else {
							// Define the xtype base on type attribute of the
							// parameter (java.util.Date, java.lang.String,
							// etc.)
							switch (param.type) {
							case 'java.util.Date':
								xtype = 'datefield';
								break;
							case 'java.lang.String':
								xtype = 'textfield';
								break;
							default:
								xtype = 'textfield';
							}
						}

						return xtype;
					}
				});