/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/13/12
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.office.NewEdit.OfficeInfo', {
	extend: 'Gam.form.FieldSet',
	alias: 'widget.officeneweditofficeinfo',

	id: 'idOfficeNewEditOfficeInfo',

	title: 'اطلاعات  دفتر',
	height: 280,
	margin: 10,
	layout: 'column',
	initComponent: function () {

		var me = this;

		me.defaults = {
				columnWidth: 1 / 2,
				labelWidth: 150
		};

		me.callParent(arguments);
	},
	getReadOnlyFields: function () {
		return [
		        {
		        	fieldLabel: 'کد دفتر', id: EmsObjectName.officeNewEdit.oficCode
		        },
		        {
		        	fieldLabel: 'نام دفتر', id: EmsObjectName.officeNewEdit.oficName
		        },
		        {
		        	fieldLabel: 'فکس',
		        	id: EmsObjectName.officeNewEdit.oficFax
		        },
		        {
		        	fieldLabel: 'تلفن ثابت',
		        	id: EmsObjectName.officeNewEdit.oficTel
		        },
		        {
		        	fieldLabel: 'جایگاه سازمانی مافوق',
		        	id: EmsObjectName.officeNewEdit.oficSuperRegisterOffice
		        },
		        {
		        	fieldLabel: 'شهر/روستا',
		        	id: EmsObjectName.officeNewEdit.location
		        },
		        {
		        	fieldLabel: 'متراژ',
		        	id: EmsObjectName.officeNewEdit.oficMeter
		        },
		        // {
		        // 	//xtype:'officeneweditcmbrating',
		        // 	fieldLabel: 'رتبه',
		        // 	id: EmsObjectName.officeNewEdit.oficRating
		        // },
		        {
		        	fieldLabel: 'شروع ساعت کاری',
		        	id: EmsObjectName.officeNewEdit.workingHoursStartId,
		        	renderer: Ext.bind(this.formatTimeValue, this)
		        },
		        {
		        	fieldLabel: 'پایان ساعت کاری',
		        	id: EmsObjectName.officeNewEdit.workingHoursFinishId,
		        	renderer: Ext.bind(this.formatTimeValue, this)
		        },
		        {
		        	fieldLabel: 'نوع دفتر',
		        	id: EmsObjectName.officeNewEdit.khosusiType,
		        	renderer: function (value) {

		        		if (value == "1") {
		        			return "دفتر پیشخوان";
		        		} else if (value == "0") {
		        			return "اداره ثبت احوال";
		        		} else if (value == "2") {
		        			return "دفتر پست";
		        		}
		        	}
		        },

		        {
		        	fieldLabel: 'وضعیت تحویل کارت دفتر پیش خوان',
		        	id: EmsObjectName.officeNewEdit.officeDeliver,
		        	renderer: function (value) {
		        		if (value == "1") {
		        			return "قابلیت تحویل دارد";
		        		} else if (value == "0") {
		        			return "قابلیت تحویل ندارد";
		        		}
		        	}
		        },
		        // {
		        // 	fieldLabel: 'نوع تقویم',
		        // 	id: EmsObjectName.officeNewEdit.calenderType,
		        // 	renderer: function (value) {
                //
		        // 		if (value == "1") {
		        // 			return 'پنجشنبه باز و جمعه تعطیل';
		        // 		} else if (value == "0") {
		        // 			return "جمعه و پنجشنبه تعطیل";
		        // 		}else if (value == "2") {
		        // 			return "جمعه و پنجشنبه باز";
		        // 		}else if (value == "3") {
		        // 			return "جمعه باز و پنجشنبه تعطیل";
		        // 		}
		        // 	}
		        // },
		        {
		        	fieldLabel: 'دفتر معین',
		        	id: EmsObjectName.officeNewEdit.superiorOfficeName,
		        	renderer: function (value) {
		        		if (!value) {
		        			return "ندارد";
		        		} else {
		        			return value;
		        		}
		        	}
		        },

			{
				xtype: 'checkbox',
				readOnly: true,
				boxLabel: 'فعال',
				id: EmsObjectName.officeNewEdit.active
			},
			{
				xtype: 'checkbox',
				readOnly: true,
				boxLabel: 'وضعیت ارسال بسته',
				id: EmsObjectName.officeNewEdit.postNeeded
			},
			{
				fieldLabel: 'کد پستی شهر مقصد',
				id: EmsObjectName.officeNewEdit.postDestinationCode
			}


//		        {
//		        xtype: 'checkboxfield',
//		        disabled: true,
//		        boxLabel: 'صدور توکن SSL',
//		        boxLabelAlign: 'before',
//		        id: EmsObjectName.officeNewEdit.oficTokenExport
//		        }
		        ];
	},

	formatTimeValue: function (value) {
		var result = value;
		var pointPosition = value.split(".");
		if (pointPosition.length > 1) {
			result = pointPosition[0] + ":30";
		} else {
			result = pointPosition[0] + ":00";
		}
		return result;
	},

	getEditableFields: function () {

		this.EditableField = [
		                      {
		                    	  fieldLabel: 'کد دفتر', id: EmsObjectName.officeNewEdit.oficCode, name: EmsObjectName.officeNewEdit.oficCode, maskRe: /\d/i,
		                    	  allowBlank: false,
		                    	  maxLength: 10,
		                    	  enforceMaxLength: 10
		                      },
		                      {
		                    	  fieldLabel: 'نام دفتر',
		                    	  regex: Tools.regexFarsiAlphaAndNumber(),
		                    	  regexText: 'لطفا از حروف فارسی استفاده کنید',
		                    	  //,xtype:'officeneweditcmbofficename'
		                    	  id: EmsObjectName.officeNewEdit.oficName,
		                    	  name: EmsObjectName.officeNewEdit.oficName,
		                    	  allowBlank: false,
		                    	  maxLength: 200,
		                    	  enforceMaxLength: 200
		                      },
		                      {
		                    	  fieldLabel: 'فکس', id: EmsObjectName.officeNewEdit.oficFax, name: EmsObjectName.officeNewEdit.oficFax, maskRe: /\d/i,
		                    	  allowBlank: false,
		                    	  maxLength: 20,
		                    	  enforceMaxLength: 20
		                      },
		                      {
		                    	  fieldLabel: 'تلفن ثابت', id: EmsObjectName.officeNewEdit.oficTel, name: EmsObjectName.officeNewEdit.oficTel, maskRe: /\d/i,
		                    	  allowBlank: false,
		                    	  maxLength: 20,
		                    	  enforceMaxLength: 20
		                      },
		                      {
		                    	  xtype: 'cmbSuperregisterofficeautocomplete',
		                    	  fieldLabel: 'جایگاه سازمانی مافوق',
		                    	  id: EmsObjectName.officeNewEdit.oficSuperRegisterOffice,
		                    	  name: EmsObjectName.officeNewEdit.oficSuperRegisterOffice,
		                    	  allowBlank: false,
		                    	  listeners: {
		                    		  autocompleteselect: function (autocomplete, record) {
		                    			  // Resetting location of enrollment office. Location field should be reselected by the user in
		                    			  // order to select a new value base on its super registration location
		                    			  if (autocomplete.getValue() != autocomplete.originalValue) {
		                    				  Ext.getCmp(EmsObjectName.officeNewEdit.location).setValue("");
		                    				  Ext.getCmp(EmsObjectName.officeNewEdit.location).enable();

		                    				  // Resetting superior office of enrollment office if its type is OFFICE. Superior field should
		                    				  // be reselected by the user in order to select a new value base on its super registration
		                    				  Ext.getCmp(EmsObjectName.officeNewEdit.superiorOfficeName).setValue("");
		                    			  }
		                    		  },
		                    		  change: function (autocomplete, newValue, oldValue) {
		                    			  if (oldValue == undefined) {
		                    				  //  The user has selected an enrollment office for edit. So enable the location field. It's
		                    				  //  disabled by default. In case of New enrollment office scenario, this event would not be
		                    				  //  raised, so the location autocomplete would be disabled until the user selects something
		                    				  //  on super registration office field
		                    				  Ext.getCmp(EmsObjectName.officeNewEdit.location).enable();
		                    			  }
		                    		  }
		                    	  }
		                      },
		                      {
		                    	  xtype: 'locationautocomplete',
		                    	  fieldLabel: 'شهر/روستا',
		                    	  id: EmsObjectName.officeNewEdit.location,
		                    	  name: EmsObjectName.officeNewEdit.location,
		                    	  disabled: true,
		                    	  listeners: {
		                    		  focus: function (autocomplete, e) {
		                    			  if (Ext.getCmp(EmsObjectName.officeNewEdit.oficSuperRegisterOffice).getValue()) {
		                    				  autocomplete.getStore().readParams.additionalParams =
		                    					  Ext.JSON.encode({'superDepartmentID': Ext.getCmp(EmsObjectName.officeNewEdit.oficSuperRegisterOffice).getValue()});
		                    			  } else {
		                    				  autocomplete.getStore().readParams.additionalParams = '';
		                    				  Gam.window.MessageManager.showInfoMsg('لطفا ابتدا جایگاه سازمانی مافوق دفتر را تعیین نمایید');
		                    			  }
		                    		  }
		                    	  }
		                      },
		                      {
		                    	  fieldLabel: 'متراژ',
		                    	  id: EmsObjectName.officeNewEdit.oficMeter,
		                    	  name: EmsObjectName.officeNewEdit.oficMeter,
		                    	  maskRe: /\d/i,
		                    	  maxLength: 4,
		                    	  enforceMaxLength: 4,
		                    	  allowBlank: false
		                      },
		                      // {
		                    	//   xtype: 'cmbrating',
		                    	//   fieldLabel: 'رتبه',
		                    	//   id: EmsObjectName.officeNewEdit.oficRating,
		                    	//   name: EmsObjectName.officeNewEdit.oficRating,
		                    	//   allowBlank: false
		                      // },
		                      {
		                    	  xtype: 'workinghours',
		                    	  fieldLabel: 'شروع ساعت کاری',
		                    	  id: EmsObjectName.officeNewEdit.workingHoursStartId,
		                    	  name: EmsObjectName.officeNewEdit.workingHoursStartId,
//		                    	  hiddenName: "workingHoursStart",
		                    	  allowBlank: false,
		                    	  listeners: {
		                    		  select: function (combo, records, eOpts) {
		                    			  //  Start working hour must be less than finish hour
		                    			  var finishHour = Ext.getCmp(EmsObjectName.officeNewEdit.workingHoursFinishId).getValue();
		                    			  if (finishHour) {
		                    				  if (new Number(finishHour) <= new Number(combo.getValue())) {
		                    					  Tools.errorMessageClient("ساعت شروع کار دفتر می بایست قبل از ساعت پایان کار آن باشد");
		                    					  combo.setValue("");
		                    				  }
		                    			  }
		                    		  }
		                    	  }
		                      },
		                      {
		                    	  xtype: 'workinghours',
		                    	  fieldLabel: 'پایان ساعت کاری',
		                    	  id: EmsObjectName.officeNewEdit.workingHoursFinishId,
		                    	  name: EmsObjectName.officeNewEdit.workingHoursFinishId,
//		                    	  hiddenName: EmsObjectName.officeNewEdit.workingHoursFinishId,
		                    	  allowBlank: false,
		                    	  listeners: {
		                    		  select: function (combo, records, eOpts) {
		                    			  //  Start working hour must be less than finish hour
		                    			  var startHour = Ext.getCmp(EmsObjectName.officeNewEdit.workingHoursStartId).getValue();
		                    			  if (startHour) {
		                    				  if (new Number(startHour) >= new Number(combo.getValue())) {
		                    					  Tools.errorMessageClient("ساعت شروع کار دفتر می بایست قبل از ساعت پایان کار آن باشد");
		                    					  combo.setValue("");
		                    				  }
		                    			  }
		                    		  }
		                    	  }
		                      },
		                      {
		                    	  xtype: 'officetyperadiogroup',
		                    	  fieldLabel: 'نوع دفتر',
		                    	  id: EmsObjectName.officeNewEdit.khosusiType,
		                    	  name: EmsObjectName.officeNewEdit.khosusiType,
		                    	  allowBlank: false,
		                    	  listeners: {
		                    		  change: Ext.bind(function (radioGroup, newValue, oldValue) {
		                    			  //change by khodayari
		                    			  var value  = EmsObjectName._oldOfficeValues.setKhosusiType;
		                    			  var khosusiType = oldValue;
		                    			  /**************************************/
		                    			  if (value == "0") {
		                    				  khosusiType = "NOCR";
		                    			  } else if (value == "1") {
		                    				  khosusiType = "OFFICE";
		                    			  } else if (value == "2") {
		                    				  khosusiType = "POST";
		                    			  }
		                    			  else if (value == "NOCR") {
		                    				  khosusiType = "NOCR";
		                    			  } else if (value == "OFFICE") {
		                    				  khosusiType = "OFFICE";
		                    			  } else if (value == "POST") {
		                    				  khosusiType = "POST";
		                    			  }
		                    			  /***************************************/
		                    			  if (khosusiType) {
		                    				  var officeID = Ext.getCmp(EmsObjectName.officeNewEdit.officeID).getValue();
		                    				  if (officeID) {
		                    					  //  Changing NOCR to OFFICE is prohibited
		                    					  if ((khosusiType == "NOCR") && (newValue.khosusiType == "OFFICE") &&
		                    							  (radioGroup.originalValue.khosusiType != "OFFICE")) {
		                    						  radioGroup.setValue("NOCR");
		                    						  Gam.window.MessageManager.showInfoMsg('تغییر نوع دفتر از <b>«اداره ثبت احوال»</b> به <b>«دفتر پیشخوان»</b> امکان پذیر نمی باشد');
		                    					  }else if ((khosusiType == "NOCR") && (newValue.khosusiType == "POST") &&
		                    							  (radioGroup.originalValue.khosusiType != "POST")) {
		                    						  radioGroup.setValue("NOCR");
		                    						  Gam.window.MessageManager.showInfoMsg('تغییر نوع دفتر از <b>«اداره ثبت احوال»</b> به <b>«دفتر پست»</b> امکان پذیر نمی باشد');
		                    					  } else {
		                    						  this.changeSuperiorOfficeStateByOfficeType(newValue.khosusiType);
		                    					  }

		                    				  } else {
		                    					  //  The user is creating a new office. So any office type could be selected
		                    					  this.changeSuperiorOfficeStateByOfficeType(newValue.khosusiType);
		                    				  }
		                    			  }
		                    			  else {
		                    				  this.changeSuperiorOfficeStateByOfficeType(newValue.khosusiType);
		                    			  }
		                    		  }, this)
		                    	  }
		                      },
		                      
		                      
		                      
		                      // {
		                    	//   xtype: 'calendertype',
		                    	//   fieldLabel: 'نوع تقویم',
		                    	//   id: EmsObjectName.officeNewEdit.calenderType,
		                    	//   name: EmsObjectName.officeNewEdit.calenderType,
		                    	//   allowBlank: false,
		                    	//   listeners: {
		                    	// 	  change: function (autocomplete, newValue, oldValue) {
		                    	//
		                    	// 		  if(oldValue == undefined){
		                    	// 			  EmsObjectName.officeNewEdit.calenderTypeTmp = newValue;
		                    	// 		  }
		                    	// 		  else  if(EmsObjectName.officeNewEdit.calenderTypeTmp ==  "0")
		                    	// 		  {
                              //
		                    	// 		  }
		                    	// 		  else  if(EmsObjectName.officeNewEdit.calenderTypeTmp ==  "1")
		                    	// 		  {
		                    	//
		                    	// 			  switch (newValue) {
		                    	// 			  case "0":
		                    	// 				  Gam.window.MessageManager.showInfoMsg('این دفتر باید تا انجام آخرین رزرو باز باشد');
		                    	// 				  break;
                              //
		                    	// 			  default:
		                    	// 				  break;
		                    	// 			  }
		                    	// 		  }
		                    	// 		  else if(EmsObjectName.officeNewEdit.calenderTypeTmp ==  "2")
		                    	// 		  {
		                    	// 			  switch (newValue) {
		                    	// 			  case "1":
		                    	// 			  case "0":
		                    	// 				  Gam.window.MessageManager.showInfoMsg('این دفتر باید تا انجام آخرین رزرو باز باشد');
		                    	// 				  break;
                              //
		                    	// 			  default:
		                    	// 				  break;
		                    	// 			  }
		                    	// 		  }
		                    	// 	  }
		                    	//   }
                              //
		                      // },
		                      
		                      
		                      {
		                    	  xtype: 'officenameautocomplete',
		                    	  fieldLabel: 'دفتر معین',
		                    	  id: EmsObjectName.officeNewEdit.superiorOfficeName,
		                    	  name: EmsObjectName.officeNewEdit.superiorOfficeName,
		                    	  hiddenName: EmsObjectName.officeNewEdit.superiorOfficeId,
		                    	  disabled: true,
		                    	  listeners: {
		                    		  focus: function (autocomplete, e) {
		                    			  if (Ext.getCmp(EmsObjectName.officeNewEdit.oficSuperRegisterOffice).getValue()) {
		                    				  autocomplete.getStore().readParams.additionalParams =
		                    					  Ext.JSON.encode({'superDepartmentID': Ext.getCmp(EmsObjectName.officeNewEdit.oficSuperRegisterOffice).getValue(),
		                    						  'officeType': 'NOCR'});
		                    			  } else {
		                    				  autocomplete.getStore().readParams.additionalParams = Ext.JSON.encode({'officeType': 'NOCR', 'superDepartmentID': -1});
		                    				  Gam.window.MessageManager.showInfoMsg('لطفا ابتدا جایگاه سازمانی مافوق دفتر را تعیین نمایید');
		                    			  }
		                    		  }
		                    	  }
		                      },
		                      {
		                    	  xtype: 'officedeliverradiogroup',
		                    	  fieldLabel: 'وضعیت تحویل در دفتر پیش خوان',
		                    	  id: EmsObjectName.officeNewEdit.officeDeliver,
		                    	  name: EmsObjectName.officeNewEdit.officeDeliver,
		                    	  allowBlank: false 

		                      },
        //     private Boolean isActive;
        // private Boolean isPostNeede;
        // private String postDestinationCode;
								{
									xtype: 'checkbox',
									fieldLabel: 'فعال',
									id: EmsObjectName.officeNewEdit.active,
									name: EmsObjectName.officeNewEdit.active
								},
								{
									xtype: 'checkbox',
									fieldLabel: 'وضعیت ارسال بسته',
									id: EmsObjectName.officeNewEdit.postNeeded,
									name: EmsObjectName.officeNewEdit.postNeeded
								},
								{
									xtype: 'textfield',
									fieldLabel: 'کد پستی شهر مقصد',
                                    enforceMaxLength: true,
									maxLength: 10,
									maskRe: /\d/i,
									id: EmsObjectName.officeNewEdit.postDestinationCode,
									name: EmsObjectName.officeNewEdit.postDestinationCode
								},
		                      {
		                    	  xtype: 'hiddenfield',
		                    	  fieldLabel: 'شناسه دفتر',
		                    	  style: {
		                    		  display: 'none'
		                    	  },
		                    	  id: EmsObjectName.officeNewEdit.officeID,
		                    	  disabled: true
		                      }

		                      ];

//		if (this.isAddAction()) {
//		this.EditableField.push({
//		xtype: 'checkboxfield',
//		boxLabel: 'صدور توکن SSL',
//		boxLabelAlign: 'before',
//		id: EmsObjectName.officeNewEdit.oficTokenExport,
//		name: EmsObjectName.officeNewEdit.oficTokenExport,
//		disabled: true
//		});
//		}
		return this.EditableField;
	},

	onLocationClear: function () {

	},

	onEnrollmentSelect: function() {

	},

	onEnrollmentClear: function () {

	},

	onLocationSelect: function() {

	},

	onRatingSelect: function() {

	},

	onRatingClear: function() {

	},

	onNonEnrollmentDepartmentSelect: function() {

	},

	/**
	 * Handler of clear selection event of super registration office field. The location field should be disabled when
	 * the super registration office has no value
	 */
	onNonEnrollmentDepartmentClear: function () {
		//  Resetting location of enrollment office. Location field should be reselected by the user in order
		//  to select a new location base on its super registration location
		Ext.getCmp(EmsObjectName.officeNewEdit.location).setValue("");
		Ext.getCmp(EmsObjectName.officeNewEdit.location).disable();

		// Resetting superior office of enrollment office if its type is OFFICE. Superior field should
		// be reselected by the user in order to select a new value base on its super registration
		Ext.getCmp(EmsObjectName.officeNewEdit.superiorOfficeName).setValue("");
	},

	/**
	 * Disable superior office field if user has selected the NOCR type. Because NOCR offices have no superior office
	 * @param newOfficeType Newly selected office type
	 */
	changeSuperiorOfficeStateByOfficeType: function (newOfficeType) {

		var ssl = Ext.getCmp(EmsObjectName.officeNewEdit.oficTokenExport);
		var superiorOffice = Ext.getCmp(EmsObjectName.officeNewEdit.superiorOfficeName);
		
		var officeDeliver =  Ext.getCmp(EmsObjectName.officeNewEdit.officeDeliver);
		if (newOfficeType == "NOCR") {
			
//			if(superiorOffice.rawValue != ''){
//				EmsObjectName._oldOfficeValues.setSuperiorOffice = superiorOffice.rawValue;
//				EmsObjectName._oldOfficeValues.setSuperiorOfficeID = superiorOffice.getValue();
//			}
			
			superiorOffice.clearValue();
			superiorOffice.disable();

			officeDeliver.disable();            

			if (ssl) {
				ssl.disable();
				ssl.setValue(false);
			}
		} else if (newOfficeType == "OFFICE") {
			superiorOffice.enable();
//			if(EmsObjectName._oldOfficeValues.setSuperiorOffice != ''){
//				superiorOffice.setValue(EmsObjectName._oldOfficeValues.setSuperiorOfficeID);			
//				superiorOffice.setRawValue(EmsObjectName._oldOfficeValues.setSuperiorOffice);
//			}
			officeDeliver.enable();
			if (officeDeliver.originalValue == undefined || officeDeliver.originalValue.officeDeliver == undefined)
				officeDeliver.setValue("0");
			else
				officeDeliver.setValue(officeDeliver.originalValue.officeDeliver);
			if (ssl) {
				ssl.enable();
			}
		} else if (newOfficeType == "POST") {
			superiorOffice.enable();
//			if(EmsObjectName._oldOfficeValues.setSuperiorOffice != ''){
//				superiorOffice.setValue(EmsObjectName._oldOfficeValues.setSuperiorOfficeID);
//				superiorOffice.setRawValue(EmsObjectName._oldOfficeValues.setSuperiorOffice);
//			}
		
			officeDeliver.enable();
			if (officeDeliver.originalValue == undefined || officeDeliver.originalValue.officeDeliver == undefined)
				officeDeliver.setValue("0");
			else
				officeDeliver.setValue(officeDeliver.originalValue.officeDeliver);            
			if (ssl) {
				ssl.enable();
			}
		}
	}
});

