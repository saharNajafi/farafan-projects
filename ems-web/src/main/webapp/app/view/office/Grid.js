Ext.define('Ems.view.office.Grid', {
        extend: 'Gam.grid.Crud',
        requires: [
            'Ems.view.office.Dialog' ,
            'Ems.view.office.Setting.Dialog' ,
            'Ems.view.office.OfficeStatusCombobox' ,
            'Ems.store.OfficeStore'
        ],

        alias: 'widget.officegrid',

        multiSelect: false,

        stateId: 'wOfficeGrid',

        id: 'officeGrid',

        title: 'مدیریت دفاتر پیشخوان ',

        store: {type: 'officeformgridstore'},

        actions: ['gam.add->officedialog'],
//
        actionColumnItems: [
            'edit->officedialog',
            'view->officedialog',
            {
                // icon: 'resources/themes/images/user/UserList.png',
                tooltip: 'ظرفیت',
                action: 'capacityOffice',
                stateful: true,
                stateId: this.stateId + 'Capacity',
                getClass: function (value, metadata, record) {
                    return 'girdAction-OfficeCapacity-icon';
                }
            },
            {
                // icon: 'resources/themes/images/user/UserList.png',
                tooltip: 'تنظیمات',
                action: 'settingOffice',
                stateful: true,
                stateId: this.stateId + 'Setting',
                getClass: function (value, metadata, record) {
                    return 'girdAction-OfficeSetting-icon';
                }
            },
            {
                // icon: 'resources/themes/images/user/UserList.png',
                tooltip: 'فهرست کاربران',
                action: 'userListOffice',
                stateful: true,
                stateId: this.stateId + 'UserList',
                getClass: function (value, metadata, record) {
                    return 'girdAction-UserList-icon';
                }
            },
            {
            	icon: 'resources/themes/images/default/shared/forbidden.png',
                tooltip: 'حذف',
                action: 'revokeOffice',
                stateful: true,
                stateId: this.stateId + 'CancelToken',
//                getClass: function (value, metadata, record) {
//                    var deleteToken = record.get(EmsObjectName.officeNewEdit.tokenStatus);
//                    var officeType = record.get(EmsObjectName.officeNewEdit.officeType);
//                    return (((deleteToken === "DELIVERED") || (deleteToken === "READY_TO_DELIVER")) && (officeType == 'OFFICE')) ? 'girdAction-deleteToken-icon' : 'x-hide-display';
//                }
            },
//            {
//                // icon:'resources/themes/images/TokenIcon/new.png',
//                tooltip: 'صدور توکن ',
//                action: 'issueSSLToken',
//                stateful: true,
//                stateId: this.stateId + 'NewToken',
//                getClass: function (value, metadata, record) {
//                    var status = record.get(EmsObjectName.officeNewEdit.tokenStatus);
//                    var officeType = record.get(EmsObjectName.officeNewEdit.officeType);
//                    return((((status == "REVOKED") || (status == "") || (status == null) || (status == "PKI_ERROR"))
//                        && (officeType == 'OFFICE')) ? 'girdAction-issueSSLToken-icon' : 'x-hide-display');
//                }
//            }, {
//                //icon:'resources/themes/images/TokenIcon/tahvil.gif',
//                tooltip: 'تحویل توکن',
//                action: 'deliverTokens',
//                stateful: true,
//                stateId: this.stateId + 'SendToken',
//                getClass: function (value, metadata, record) {
//                    var status = record.get(EmsObjectName.officeNewEdit.tokenStatus);
//                    var officeType = record.get(EmsObjectName.officeNewEdit.officeType);
//                    return (status === "READY_TO_DELIVER" && (officeType == 'OFFICE')) ? 'girdAction-tahvil-icon' : 'x-hide-display';
//                }
//            }, {
//                // icon:'resources/themes/images/TokenIcon/deleteToken.png',
//                tooltip: 'ابطال توکن',
//                action: 'revokeOffice',
//                stateful: true,
//                stateId: this.stateId + 'CancelToken',
//                getClass: function (value, metadata, record) {
//                    var deleteToken = record.get(EmsObjectName.officeNewEdit.tokenStatus);
//                    var officeType = record.get(EmsObjectName.officeNewEdit.officeType);
//                    return (((deleteToken === "DELIVERED") || (deleteToken === "READY_TO_DELIVER")) && (officeType == 'OFFICE')) ? 'girdAction-deleteToken-icon' : 'x-hide-display';
//                }
//            }, {
//                tooltip: ' صدور مجدد بدلیل خرابی',
//                action: 'damagedTokens',//'extensionTokens',
//                stateful: true,
//                stateId: this.stateId + 'DamagedTokens',//'ExtensionTokens',
//                getClass: function (value, metadata, record) {
//                    var status = record.get(EmsObjectName.officeNewEdit.tokenStatus);
//                    var officeType = record.get(EmsObjectName.officeNewEdit.officeType);
//                    return (status === "DELIVERED" && (officeType == 'OFFICE')) ? 'girdAction-damaged-icon' : 'x-hide-display';
//                }
//            }, {
//                tooltip: 'تمدید توکن',
//                action: 'extensionTokens',
//                stateful: true,
//                stateId: this.stateId + 'ExtensionTokens',
//                getClass: function (value, metadata, record) {
//                    var status = record.get(EmsObjectName.officeNewEdit.tokenStatus);
//                    var officeType = record.get(EmsObjectName.officeNewEdit.officeType);
//                    return (status === "DELIVERED" && (officeType == 'OFFICE')) ? 'grid-tamdid-action-icon' : 'x-hide-display';
//                }
//            }, {
//                tooltip: 'المثنی',
//                action: 'duplicateTokens',
//                stateful: true,
//                stateId: this.stateId + 'DuplicateTokens',
//                getClass: function (value, metadata, record) {
//                    var status = record.get(EmsObjectName.officeNewEdit.tokenStatus);
//                    var officeType = record.get(EmsObjectName.officeNewEdit.officeType);
//                    return (status === "DELIVERED" && (officeType == 'OFFICE')) ? 'girdAction-duplicate-icon' : 'x-hide-display';
//                }
//            },
//            {
//                tooltip: 'حذف درخواست صدور توکن',
//                action: 'deleteToken',
//                getClass: function (value, metadata, record) {
//                    var result = Tools.trim(record.get(EmsObjectName.userRequestToken.status));
//                    var officeType = record.get(EmsObjectName.officeNewEdit.officeType);
//                    return (result == 'READY_TO_ISSUE' && (officeType == 'OFFICE')) ? 'grid-undo-person-token-action-icon' : 'grid-action-hidden';
//                }
//            }
            {
              	 
         	   tooltip: 'آپلود عکس',
         	   action: 'changeUploadPhoto',
         	   getClass: function (value, metadata, record) {
         		   debugger;
         		  if(EmsObjectName.officeNewEdit.accessViewAndChangeOfficeSetting){
	         		   if(record.raw != undefined){	                       
	         			   var officeSettingType = record.raw.uploadPhoto;
	         			   if(officeSettingType != null && officeSettingType != "" && officeSettingType == "1")
	         				   return 'girdAction-LivePhotoActive-icon';
	         			   else
	         				   return 'girdAction-LivePhotoDeactive-icon';
	         		   }else{
	         			   return 'girdAction-LivePhotoDeactive-icon';
	         		   }
         		   }

         	   }
            },
            {
         	   tooltip: 'اخذ اثر انگشت در مد تک انگشتی',
         	   action: 'changeFingerScanSingleMode',
         	   getClass: function (value, metadata, record) {
	         		  if(EmsObjectName.officeNewEdit.accessViewAndChangeOfficeSetting){
	         		   if(record.raw != undefined){
	         			   var officeSettingType = record.raw.fingerScanSingleMode;
	
	         			   if(officeSettingType != null && officeSettingType != "" && officeSettingType == "1")
	         				   return 'girdAction-FourfourtwoActive-icon';
	         			   else
	         				   return 'girdAction-FourfourtwoDeactive-icon';
	         		   }else{
	         			   return 'girdAction-FourfourtwoDeactive-icon';
	         		   }
	         	   }
         	   }
            },
            {
         	   tooltip: 'اخذ تصویر چهره معلولین',
         	   action: 'changeDisabilityMode',
         	   getClass: function (value, metadata, record) {
         		  if(EmsObjectName.officeNewEdit.accessViewAndChangeOfficeSetting){
	         		   if(record.raw != undefined){
	         			   var officeSettingType = record.raw.disabilityMode;
	         			   if(officeSettingType != null && officeSettingType != "" && officeSettingType == "1")
	         				   return 'girdAction-DisablityModeActive-icon';
	         			   else
	         				   return 'girdAction-DisablityModeDeactive-icon'; 
	         		   }else{
	         			   return 'girdAction-DisablityModeDeactive-icon'; 
	         		   }
	         	   }
         	   }
            },
            {
         	   tooltip: 'ثبت درخواست صدور مجدد',
         	   action: 'changeReIssueRequest',
         	   getClass: function (value, metadata, record) {
         		  if(EmsObjectName.officeNewEdit.accessViewAndChangeOfficeSetting){
	         		   if(record.raw != undefined){
	         			   var officeSettingType = record.raw.reIssueRequest;
	         			   if(officeSettingType != null && officeSettingType != "" && officeSettingType == "1")
	         				   return 'girdAction-ReIssueRequestActive-icon';
	         			   else
	         				   return 'girdAction-ReIssueRequestDeactive-icon'; 
	         		   }else{
	         			   return 'girdAction-ReIssueRequestDeactive-icon'; 
	         		   }
	         	   }
         	   }
            }, 
            {
         	   tooltip: 'مد سالمندی',
         	   action: 'changeElderyMode',
         	   getClass: function (value, metadata, record) {
         		  if(EmsObjectName.officeNewEdit.accessViewAndChangeOfficeSetting){
	         		   if(record.raw != undefined){
	         			   var officeSettingType = record.raw.elderlyMode;
	         			   if(officeSettingType != null && officeSettingType != "" && officeSettingType == "1")
	         				   return 'girdAction-ElderyModeActive-icon';
	         			   else
	         				   return 'girdAction-ElderyModeDeactive-icon'; 
	         		   }else{
	         			   return 'girdAction-ElderyModeDeactive-icon'; 
	         		   }
	         	   }
         	   }
            },
            {
          	   tooltip: 'فعالسازی پین جایگزین',
          	   action: 'changeNMocGeneration',
          	   getClass: function (value, metadata, record) {
          		 if(EmsObjectName.officeNewEdit.accessViewAndChangeOfficeSetting){
	          		   if(record.raw != undefined){
	          			   var officeSettingType = record.raw.nMocGeneration;
	          			   if(officeSettingType != null && officeSettingType != "" && officeSettingType == "1")
	          				   return 'girdAction-ReplacePinActive-icon';
	          			   else
	          				   return 'girdAction-ReplacePinDeactive-icon'; 
	          		   }else{
	          			   return 'girdAction-ReplacePinDeactive-icon'; 
	          		   }
	          	   }
          	   }
             },
             {
           	   tooltip: 'تغییر وضعیت انگشتان به قطع در شروع اخذ و بی کیفیت در حین اخذ',
           	   action: 'changeAmputationAnnouncment',
           	   getClass: function (value, metadata, record) {
	           		if(EmsObjectName.officeNewEdit.accessViewAndChangeOfficeSetting){
	           		   if(record.raw != undefined){
	           			   var officeSettingType = record.raw.amputationAnnouncment;
	           			   if(officeSettingType != null && officeSettingType != "" && officeSettingType == "1")
	           				   return 'girdAction-AmputationAnnouncmentActive-icon';
	           			   else
	           				   return 'girdAction-AmputationAnnouncmentDeactive-icon'; 
	           		   }else{
	           			   return 'girdAction-AmputationAnnouncmentDeactive-icon'; 
	           		   }
	           	   }
           	   }
              },
              {
              	   tooltip: 'استفاده از اسکنرهای قدیمی',
              	   action: 'changeUseScannerUI',
              	   getClass: function (value, metadata, record) {
   	           		if(EmsObjectName.officeNewEdit.accessViewAndChangeOfficeSetting){
   	           		   if(record.raw != undefined){
   	           			   var officeSettingType = record.raw.useScannerUI;
   	           			   if(officeSettingType != null && officeSettingType != "" && officeSettingType == "1")
   	           				   return 'girdAction-UseScannerUIActive-icon';
   	           			   else
   	           				   return 'girdAction-UseScannerUIDeactive-icon'; 
   	           		   }else{
   	           			   return 'girdAction-UseScannerUIDeactive-icon'; 
   	           		   }
   	           	   }
              	   }
                 },
                 {
                 	   tooltip: 'ویرایش دستی پس زمینه',
                 	   action: 'changeAllowEditBackground',
                 	   getClass: function (value, metadata, record) {
      	           		if(EmsObjectName.officeNewEdit.accessViewAndChangeOfficeSetting){
      	           		   if(record.raw != undefined){
      	           			   var officeSettingType = record.raw.allowEditBackground;
      	           			   if(officeSettingType != null && officeSettingType != "" && officeSettingType == "1")
      	           				   return 'girdAction-AllowEditBackgroundActive-icon';
      	           			   else
      	           				   return 'girdAction-AllowEditBackgroundDeactive-icon'; 
      	           		   }else{
      	           			   return 'girdAction-AllowEditBackgroundDeactive-icon'; 
      	           		   }
      	           	   }
                 	   }
                    },
            {
                tooltip: 'امکان انتخاب وضعیت قطع برای انگشتان برای سالمندان',
                action: 'changeAllowAmputatedFinger',
                getClass: function (value, metadata, record) {
                    if(EmsObjectName.officeNewEdit.accessViewAndChangeOfficeSetting){
                        if(record.raw != undefined){
                            var officeSettingType = record.raw.allowAmputatedFinger;
                            if(officeSettingType != null && officeSettingType != "" && officeSettingType == "1")
                                return 'girdAction-AllowAmputatedFingerActive-icon';
                            else
                                return 'girdAction-AllowAmputatedFingerInactive-icon';
                        }else{
                            return 'girdAction-AllowAmputatedFingerInactive-icon';
                        }
                    }
                }
            }   ,

            {
                tooltip: 'امکان تغییر وضعیت انگشتان در حین اخذ انگشت حتی پس از اخذ نخستین تصویر برای سالمندان',
                action: 'changeAllowChangeFinger',
                getClass: function (value, metadata, record) {
                    if(EmsObjectName.officeNewEdit.accessViewAndChangeOfficeSetting){
                        if(record.raw != undefined){
                            var officeSettingType = record.raw.allowChangeFinger;
                            if(officeSettingType != null && officeSettingType != "" && officeSettingType == "1")
                                return 'girdAction-AllowChangeFingerActive-icon';
                            else
                                return 'girdAction-AllowChangeFingerInactive-icon';
                        }else{
                            return 'girdAction-AllowChangeFingerInactive-icon';
                        }
                    }
                }
            }

            
        ],

        contextMenu: [ 'gam.add'],

        tbar: ['gam.add',
               {
                   iconCls: 'girdAction-exportExcel-icon',
                   text: 'خروجی اکسل',
                   action: 'exportExcel',
                   stateful: true,
                   stateId: this.stateId + 'ExportExcel'
               }
           ],



        initComponent: function () {
            this.columns = this.getColumnOffice();
            this.callParent(arguments);
        },

        getColumnOffice: function () {
            return ([
                     {
                         xtype: 'gridcolumn',
                         width: 150,
                         text: 'استان',
                         dataIndex: EmsObjectName.officeNewEdit.provinceName,
                         id: EmsObjectName.officeNewEdit.provinceName,
                         filterable: true,
                         filter: true
                     },
                {
                    xtype: 'gridcolumn',
                    text: 'نام دفتر',
                    filterable: true,
                    filter: true,
                    dataIndex: EmsObjectName.officeNewEdit.oficName,
                    id: EmsObjectName.officeNewEdit.g_oficName
                },
                {
                    xtype: 'gridcolumn',
                    filterable: true,
                    filter: true,
                    text: 'نام مدير',
                    dataIndex: EmsObjectName.officeNewEdit.mangName,
                    id: EmsObjectName.officeNewEdit.g_mangName
                },
                {
                    xtype: 'gridcolumn',
                    text: 'جایگاه سازمانی مافوق',
                    width: 120,
                    filterable: true,
                    filter: true,
                    dataIndex: EmsObjectName.officeNewEdit.oficSuperRegisterOffice,
                    id: EmsObjectName.officeNewEdit.g_oficSuperRegisterOffice
                },
                {
                    xtype: 'gridcolumn',
                    text: 'کد دفتر',
                    filterable: true,
                    filter: true,
                    align: 'center',
                    dataIndex: EmsObjectName.officeNewEdit.oficCode,
                    id: EmsObjectName.officeNewEdit.g_oficCode
                },
                {
                    xtype: 'gridcolumn',
                    text: 'شهر/روستا',
                    width: 200,
                    filterable: true,
                    filter: {
                        xtype: 'locationautocomplete'
                    },
                    align: 'center',
                    dataIndex: EmsObjectName.officeNewEdit.location,
                    id: EmsObjectName.officeNewEdit.locationId
                },
                {
                    xtype: 'gridcolumn',
                    width: 250,
                    text: 'آدرس کامل محل دفتر',
                    filterable: true,
                    filter: true,
                    dataIndex: EmsObjectName.officeNewEdit.oficAddress,
                    id: EmsObjectName.officeNewEdit.g_oficAddress
                },
                {
                    xtype: 'gridcolumn',
                    text: 'تلفن دفتر',
                    filterable: true,
                    filter: true,
                    dataIndex: EmsObjectName.officeNewEdit.oficTel,
                    id: EmsObjectName.officeNewEdit.g_oficTel
                },
                {
                    xtype: 'gridcolumn',
                    text: 'نوع دفتر ',
                    width: 100,
                    dataIndex: EmsObjectName.officeNewEdit.khosusiType,
                    id: EmsObjectName.officeNewEdit.g_khosusiType,
                    filterable: true,
                    filter: {
                        xtype: 'combo',
                        store: {
                            fields: ['value', 'label'],
                            data: [
                                 {
                                    value: '0',
                                    label: 'اداره ثبت احوال'
                                },
                                {
                                    value: '1',
                                    label: 'دفتر پیشخوان'
                                },
                                {
                                    value: '2',
                                    label: 'دفتر پست'
                                }/*,
                                {
                                    value: 'NOCR',
                                    label: 'اداره ثبت احوال'
                                },
                                {
                                    value: 'OFFICE',
                                    label: 'دفتر پیشخوان'
                                },
                                {
                                    value: 'POST',
                                    label: 'دفتر پست'
                                }*/
                            ]
                        },
                        queryMode: 'local',
                        displayField: 'label',
                        valueField: 'value'
                    },
                    renderer: function (value) {
                    	 if (value == "0") {
                             return "اداره ثبت احوال";
                         } else if (value == "1") {
                             return "دفتر پیشخوان";
                         }else if (value == "2") {
                             return 'دفتر پست';
                         }
                         else if (value == "NOCR") {
                             return "اداره ثبت احوال";
                         } else if (value == "OFFICE") {
                             return "دفتر پیشخوان";
                         }else if (value == "POST") {
                             return 'دفتر پست';
                         }
                        
                    }
                },
                {
                    xtype: 'gridcolumn',
                    text: 'امکان تحویل ',
                    width: 100,
                    dataIndex: EmsObjectName.officeNewEdit.officeDeliver,
                    id: EmsObjectName.officeNewEdit.g_officeDeliver,
                    filterable: true,
                    filter: {
                        xtype: 'combo',
                        store: {
                            fields: ['value', 'label'],
                            data: [
                                {
                                    value: '1',
                                    label: 'دارد'
                                },
                                {
                                    value: '0',
                                    label: 'ندارد'
                                }
                            ]
                        },
                        queryMode: 'local',
                        displayField: 'label',
                        valueField: 'value'
                    },
                    renderer: function (value) {
                        if (value == "0") {
                            return "ندارد";
                        } else if (value == "1") {
                            return "دارد";
                        }
                    }
                },
                {
                    xtype: 'gridcolumn',
                    text: 'رتبه ',
                    filterable: true,
                    filter: true,
                    dataIndex: EmsObjectName.officeNewEdit.oficRating,
                    id: EmsObjectName.officeNewEdit.g_oficRating
                },
                {
                    xtype: 'gridcolumn',
                    text: 'ساعت شروع کار',
                    align: 'center',
                    dataIndex: EmsObjectName.officeNewEdit.workingHoursStartId,
                    id: EmsObjectName.officeNewEdit.g_workingHoursStartId,
                    filterable: true,
                    filter: {
                        xtype: 'workinghours',
                        editable: false
                    },
                    renderer: Ext.bind(this.workingHoursRenderer, this)
                },
                {
                    xtype: 'gridcolumn',
                    text: 'ساعت پایان کار',
                    align: 'center',
                    dataIndex: EmsObjectName.officeNewEdit.workingHoursFinishId,
                    id: EmsObjectName.officeNewEdit.g_workingHoursFinishId,
                    filterable: true,
                    filter: {
                        xtype: 'workinghours',
                        editable: false
                    },
                    renderer: Ext.bind(this.workingHoursRenderer, this)
                },
                {
                    xtype: 'gridcolumn',
                    text: 'دفتر معین',
                    filterable: true,
                    dataIndex: EmsObjectName.officeNewEdit.superiorOfficeName,
                    id: EmsObjectName.officeNewEdit.g_superiorOfficeName,
                    width: 125,
                    filter: {
                        xtype: 'nocrautocomplete',
                        hiddenName: EmsObjectName.officeNewEdit.superiorOfficeId
                    }
                },
                {
                    xtype: 'gridcolumn',
                    text: 'دامنه',
                    dataIndex: EmsObjectName.officeNewEdit.fqdn,
                    id: EmsObjectName.officeNewEdit.g_fqdn,
                    filterable: true,
                    filter: {
                        style: 'direction: ltr;'
                    },
                    renderer: function (value, metaData) {
                        metaData.tdCls += 'ems-direction-ltr';
                        return value;
                    }
                }//,
//                {
//                    xtype: 'gridcolumn',
//                    text: 'وضعیت توکن',
//                    filterable: true,
//                    dataIndex: EmsObjectName.officeNewEdit.tokenStatus,
//                    id: EmsObjectName.officeNewEdit.g_tokenStatus,
//                    renderer: function (value) {
//                        if (value && typeof value === 'string') {
//                            switch (value) {
//                                case "READY_TO_ISSUE":
//                                    return 'آماده صدور';
//                                    break;
//                                case "PENDING_TO_ISSUE":
//                                    return 'در دست صدور';
//                                    break;
//                                case "READY_TO_DELIVER":
//                                    return 'آماده تحویل';
//                                    break;
//                                case "DELIVERED":
//                                    return 'تحویل شد';
//                                    break;
//                                case "REVOKED":
//                                    return 'ابطال شد';
//                                    break;
//                                default :
//                                    break;
//                            }
//                        }
//                    }
//                }
            ]);
        },

        /*
         * The renderer method for working hours column. It renders a float number in a readable time format. It returns
         * nothing if working hour parameter is null
         */
        workingHoursRenderer: function (workingHour) {
            if (workingHour) {
                workingHour = new String(workingHour);
                if (workingHour.indexOf(".5") > 0) {
                    //  Change 12.5 to 12:30
                    return workingHour.split(".")[0] + ":30";
                } else {
                    //  Change 12 to 12:00
                    return workingHour.split(".")[0] + ":00";
                }
            }
        }
    }
);
