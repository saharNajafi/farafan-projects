/**
 * Created by Gam Electronics Co.
 * User: Haeri
 * Date: 3/12/12
 * Time: 3:53 PM
 */

Ext.define('Ems.controller.OfficeController', {
    extend: 'Gam.app.controller.LocalDialogBasedGrid',


    ns: 'extJsController/office',


    statics: {
        statefulComponents: [
            'wOfficeGrid',
            'wOfficeGridExtensionTokens',
            'wOfficeGridCancelToken',
            'wOfficeGridSendToken',
            'wOfficeGridNewToken',
            'wOfficeGridUserList',
            'wOfficeGridAdd',
            'wOfficeGridDelete',
            'wOfficeGridEdit',
            'wOfficeGridView'
        ]
    },

    models: ['OfficeModel'],

    initViewType: 'officegrid',

    requires: [
        'Ems.view.department.locationAutocomplete',
        'Ems.view.office.NewEdit.WorkingHoursCombo',
        'Ems.view.office.NewEdit.OfficeTypeRadioGroup',
        'Ems.view.office.CalenderType',
        'Ems.view.office.OfficeName.AutoComplete',
        'Ems.view.office.OfficeName.NOCRAutoComplete',
        'Ems.view.office.NewEdit.OfficeDeliverRadioGroup',
        'Ems.controller.util.ExcelExporter'
        // 'Ems.view.office.Setting.AutoCompleteFeatureExtractID',
        // 'Ems.view.office.Setting.AutoCompleteFeatureExtractVersion',
        // 'Ems.view.office.Setting.Dialog'
    ],

    views: [
        'office.Grid'
    ],

    refs: [
        {
            ref: 'officegrid',
            selector: 'officegrid'
        }
    ],

    constructor: function (config) {
        this.selectRowId = 0;
        this.callParent(arguments);
        this.init();
        this.excelExporter = new Ems.controller.util.ExcelExporter();
    },

    /**
     * if data is valid , it will be sent to server and if not , one alert will be shown to user.
     * this method has overwritten the ict-all-debug.js
     * @author namjoofar
     * @param button
     */
    doSave: function (button) {
        var me = this,
            dialog = button.up('entitydialog'),
            formPanel = dialog.down('form');

        /*if(me.beforeSave(button) === false) {
            Ext.Msg.alert({
                title: 'توجه!',
                msg: 'لطفا تمامی اطلاعات را بدرستی وارد کنید.',
                icon: 'windows-Cancel-icon'
                //icon: '../../resources/themes/images/default/shared/notVerified.png'
            });
            return;
        }*/


        if (!Gam.util.Form.isValid(formPanel.form)) {
            Ext.Msg.alert({
                title: 'توجه!',
                msg: 'لطفا تمامی اطلاعات را بدرستی وارد کنید.',
                icon: 'windows-Cancel-icon'
                //icon: '../../resources/themes/images/default/shared/notVerified.png'
            });
            return false;
        }


        //form don't have any change!
        if (!formPanel.isDirty()) {
            Ext.Msg.alert({
                title: 'توجه!',
                //msg:Gam.Msg.showInfoMsg(Gam.Resource.message.failure.notEditedForm),
                msg: 'هیچ تغییری در اطلاعات فرم مشاهده نشده است!',
                icon: 'windows-Cancel-icon'
                //icon: '../../resources/themes/images/default/shared/notVerified.png'
            });
            return false;
        }


        me.syncStore(
            formPanel,
            Gam.GlobalConfiguration.dialogBasedGrid.continuousDataEntry,
            Gam.GlobalConfiguration.dialogBasedGrid.clearDataEntry);
    },

    init: function () {
        this.control({
//            // office.Form
//            '#grdOffices': {
//                eventDeleteOffice: function(view, grid, rowIndex, colIndex) {
//                    this.deleteOffice(view, grid, rowIndex, colIndex);
//                },
//                eventViewOffice: function(view, grid, rowIndex, colIndex) {
//                    this.viewOffice(view, grid, rowIndex, colIndex);
//                },
//                eventEditOffice: function(view, grid, rowIndex, colIndex) {
//                    this.editOffice(view, grid, rowIndex, colIndex);
//                },
//                eventDeliveryExtensionTokens: function(view, grid, rowIndex, colIndex,mode) {
//                    this.DeliveryExtensionToken(view, grid, rowIndex, colIndex,mode);
//                },
//                eventRevokeOffice: function(view, grid, rowIndex, colIndex) {
//                    this.revokeOffice(view, grid, rowIndex, colIndex);
//                }
//                ,eventIssueSSLToken:function(view, grid, rowIndex, colIndex) {
//                    this.IssueSSLToken(view, grid, rowIndex, colIndex);
//                }
//                ,eventUserListOffice:function(view, grid, rowIndex, colIndex){
//                    this.onUserListOffice(view, grid, rowIndex, colIndex);
//                }
//            },
//            '#btnCreateOffice': {
//                click: function(btn) {
//                    this.getCreateOffice(btn);
//                }
//            },
//
//            '#btnCancelWindows': {
//                click: function(btn) {
//                    btn.up('window').destroy();
//                }
//            },


            '[action=btnOfficeRegisterDeliver]': {
                click: function (btn) {
                    this.RegisterRevokeOffice(btn);
                }
            }
            ,

            'officecapacitydialog [action=edit]': {
                click: function (grid, rowIndex) {
                    var record = grid.store.getAt(rowIndex);
                    var form = Ext.create('Ems.view.office.Capacity.Dialog', {
                        enrollmentOfficeID: grid.enrollmentOfficeID
                    });
                    Ext.each(form.query('field'), function (field) {
                        field.setValue(record.get(field.getItemId()));
                    });
                    form.show();
                }
            },

            'officecapacitydialog [action=remove]': {
                click: function (grid, rowIndex) {
                    Ext.msg.confirm('', 'آیا از حذف ظرفیت اطمینان دارید؟', function (btn) {
                        if (btn == "yes") {
                            Gam.Msg.showWaitMsg();
                            Ext.Ajax.request({
                                url: 'test',
                                jsonData: {id: grid.store.getAt(rowIndex)},
                                success: function () {
                                    Gam.Msg.hideWaitMsg();
                                },
                                failure: function () {
                                    Gam.Msg.hideWaitMsg();
                                }
                            });
                        }
                    });
                }
            },

            'officesettingdialog cancelbtn': {
                click: function (sender) {
                    sender.up('dialog').close();
                }
            },

            'officecapacitydialog cancelbtn': {
                click: function (sender) {
                    sender.up('dialog').close();
                }
            },
            'officecapacitydialog savebtn': {
                click: function (sender) {
                    var form = sender.up('dialog');
                    var list = [];
                    var flag = true;
                    var obj = {id: form.action == "edit" ? form.sendID : null};
                    if (form.action == "add") {
                        Ext.apply(obj, {enrollmentOfficeId: form.enrollmentOfficeID});
                    }
                    var me = this;
                    if (form.action == "add") {
                        Ext.each(form.query('field'),
                            function (field) {
                                if (field.itemId != "id") {
                                    if (field.allowBlank == false && field.getValue() == null) {
                                        flag = false;
                                        field.markInvalid("پر کردن این فیلد الزامی است");
                                    }
                                }
                                if (field.isHidden() == false) {
                                    obj[field.getItemId()] = field.getValue();
                                }
                            }
                        );
                    } else {
                        obj['capacity'] = form.down('#capacity').getValue();
                        obj['workingHoursFrom'] = form.down('#workingHoursFrom').getValue();
                        obj['workingHoursTo'] = form.down('#workingHoursTo').getValue();
                        obj['startDate'] = form.down('#startDate').getValue();
                        obj['shiftNo'] = form.down('#shiftNo').getValue();
                        obj['enrollmentOfficeId'] = form.enrollmentOfficeID;
                        obj['editable'] = form.editableField;
                    }
                    list.push(obj);
                    if (flag) {
                        Gam.Msg.showWaitMsg();
                        Ext.Ajax.request({
                            url: 'extJsController/officeCapacity' + '/save',
                            jsonData: {records: list},
                            success: function (response) {
                                Gam.Msg.hideWaitMsg();
                                var obj = Ext.decode(response.responseText);
                                if (obj.messageInfo.message != "Ems.ErrorCode.security.EMS_S_AUT_004") {
                                    if (Ext.JSON.decode(response.responseText).success) {
                                        Ext.Msg.alert('ثبت موفق', 'عملیات با موفقیت انجام شد');
                                        Ext.StoreManager.get('idOfficeCapacityStore').load();
                                        form.close();
                                    } else {
                                        var msg = Ext.JSON.decode(response.responseText).messageInfo.message;
                                        var showMsg = "";
                                        if (msg[msg.length - 1] == "6") {
                                            showMsg = "تاریخ شروع باید از تاریخ امروز بزرگتر باشد";
                                        } else if (msg[msg.length - 1] == "7") {
                                            showMsg = "این سطر در سیستم وجود دارد.";
                                        }
                                        Ext.Msg.alert('خطا', showMsg);
                                    }
                                }
                            },
                            failure: function () {
                                // Ext.Msg.alert('خطا', 'خطایی رخ داده است');
                                Gam.Msg.hideWaitMsg();
                                Tools.errorFailure();
                            }
                        });
                    }
                }
            },

            'officesettingdialog savebtn': {
                click: function (sender) {
                    var form = sender.up('dialog');
                    var me = this;
                    var obj = {};
                    if (form.sendID != null) {
                        obj = {id: form.sendID};
                    }
                    Ext.Ajax.request({
                        url: 'extJsController/officeSetting' + '/save',
                        jsonData: {
                            records: [
                                {
                                    feiN: form.down('#feiN').getValue(),
                                    feiCC: form.down('#feiCC').getValue(),
                                    feiISOCC: form.down('#feiISOCC').getValue(),
                                    id: form.officeSettingID
                                }
                            ]
                        },
                        success: function (response) {
                            if (Ext.JSON.decode(response.responseText).success) {
                                Ext.Msg.alert('ثبت موفق', 'عملیات با موفقیت انجام شد');
                                Ext.StoreManager.get('officeList').load();
                                form.close();
                            } else {
                                // var msg = Ext.JSON.decode(response.responseText).messageInfo.message;
                                // var showMsg = "";
                                // if (msg[msg.length - 1] == "6") {
                                //     showMsg = "تاریخ شروع باید از تاریخ امروز بزرگتر باشد";
                                // }
                                // else if (msg[msg.length - 1] == "7") {
                                //     showMsg = "این سطر در سیستم وجود دارد. لطفا آن را ویرایش کنید";
                                // }
                                Ext.Msg.alert('خطا', showMsg);
                            }
                        },
                        failure: function () {
                            Ext.Msg.alert('خطا', 'خطایی رخ داده است');
                        }
                    });
                }
            },

            "[action=exportExcel]": {
                click: function (btn) {
                    var grid = btn.up('toolbar').up('grid');
                    var store = grid.getStore();
                    var fields = store.readParams.fields;
                    fields = Ext.decode(fields);
                    var finalFields = [];
                    for (var i = 0; i < fields.length; i++) {
                        if ((fields[i] !== 'area') && (fields[i] !== 'ssl') &&
                            (fields[i] !== 'parentId') && (fields[i] !== 'zip') &&
                            (fields[i] !== 'rate') && (fields[i] !== 'rateId') &&
                            (fields[i] !== 'managerPhone') && (fields[i] !== 'managerId') &&
                            (fields[i] !== 'managerMobile') && (fields[i] !== 'locId') &&
                            (fields[i] !== 'tokenId') && (fields[i] !== 'superiorOfficeId') &&
                            (fields[i] !== 'id') && (fields[i] !== 'tokenState')) {
                            finalFields.push(fields[i]);
                        }
                    }

                    this.excelExporter.doExportExcel(btn, {fields: Ext.encode(finalFields)});
                }
            }
        });
        this.callParent(arguments);
    },

    /*
        Author: Navid
        Description: write doCapacityOffice method for first test
    */
    doCapacityOffice: function (grid, rowIndex) {
        var win = Ext.create('Ems.view.office.Capacity.Window', {controller: this});
        var form = win.down('officeneweditcapacityofficeinfo');
        var record = grid.store.getAt(rowIndex);
        var capacityGrid = win.down('grid');
        capacityGrid.store.extraParams = {enrollmentOfficeId: record.get('id')};
        var capacityStore = capacityGrid.getStore();
        capacityGrid.enrollmentOfficeID = record.get('id');
        if (capacityStore.readParams == null) {
            capacityStore.readParams({enrollmentOfficeID: record.get('id')});
        } else {
            capacityStore.readParams.enrollmentOfficeID = record.get('id');
        }
        Ext.each(form.query('field'),
            function (field) {
                field.setValue(record.get(field.getItemId()));
            }
        );
        Tools.MaskUnMask(win);
        var storeOffice = grid.getStore(),
            record = storeOffice.getAt(rowIndex),
            capacityGrid = win.down('grid');
        if (capacityGrid != null) {
            win.show();
        }
    },

    /*
     Author: Navid
     Description: write doSettingOffice method for first test
     */
    doSettingOffice: function (grid, rowIndex) {
        var win = Ext.create('Ems.view.office.Setting.Dialog', {height: 130});
        var record = grid.store.getAt(rowIndex);
        var extractN = win.down('#feiN');
        var extractCC = win.down('#feiCC');
        var extractISOCC = win.down('#feiISOCC');
        win.officeSettingID = record.get('ostId');
        Ext.Ajax.request({
            method: 'POST',
            url: 'extJsController/featureExtract/load',
            jsonData: {enrollmentOfficeId: record.get('id')},
            success: function (response) {
                var data = Ext.JSON.decode(response.responseText).records;
                win.show();
                for (var i = 0; i < data.length; i++) {
                    if (data[i].featureExtractType == "2") {
                        var rec = data[i].feiId;
                        setTimeout(function () {
                            extractCC.onTriggerClick();
                        }, 500);
                        setTimeout(function () {
                            extractCC.select(extractCC.store.getNodeById(rec));
                            extractCC.onTriggerClick();
                        }, 1500);
                    } else if (data[i].featureExtractType == "1") {
                        var rec2 = data[i].feiId;
                        setTimeout(function () {
                            extractN.onTriggerClick();
                        }, 500);
                        setTimeout(function () {
                            extractN.select(extractN.store.getNodeById(rec2));
                            extractN.onTriggerClick();
                        }, 1500);
                    } else if (data[i].featureExtractType == "3") {
                        var rec3 = data[i].feiId;
                        setTimeout(function () {
                            extractISOCC.onTriggerClick();
                        }, 500);
                        setTimeout(function () {
                            extractISOCC.select(extractISOCC.store.getNodeById(rec3));
                            extractISOCC.onTriggerClick();
                        }, 1500);
                    }
                }

            },
            failure: function () {
                alert('fail');
            }
        });
    },

    doUserListOffice: function (grid, rowIndex) {

        var win = Ext.create('Ems.view.office.userList.windows', {controller: this});
        Tools.MaskUnMask(win);
        var storeOffice = grid.getStore(),
            record = storeOffice.getAt(rowIndex),
            userListGrid = win.down('grid');
        if (userListGrid != null) {
            var userListStore = userListGrid.getStore();
            if (userListStore.readParams == null) {
                userListStore.readParams({depId: record.get('id')});
            } else {
                userListStore.readParams.depId = record.get('id');
            }
            win.show();
        }
    },

    doIssueSSLToken: function (grid, rowIndex) {
        var store = grid.getStore(),
            record = store.getAt(rowIndex),
            id = record.get('id'),
            me = this;

        var fn = function () {
            me.RegisterOfficeToken(me, grid, id, 'issueFirstNetworkToken');
        };

        Tools.messageBoxConfirm('آیا از صدور توکن اطمینان دارید؟', fn);
    },

    doDeliverTokens: function (grid, rowIndex) {
        var store = grid.getStore(),
            record = store.getAt(rowIndex),
            id = record.get('id'),
            me = this;

        var fn = function () {
            me.RegisterOfficeToken(me, grid, id, 'deliverNetworkToken')
        };

        Tools.messageBoxConfirm('آیا از تحویل توکن اطمینان دارید؟', fn);
    },

    doExtensionTokens: function (grid, rowIndex) {

        var store = grid.getStore(),
            record = store.getAt(rowIndex),
            id = record.get('id'),
            me = this;

        var fn = function () {
            me.RegisterOfficeToken(me, grid, id, 'reissueNetworkToken');
        };

        Tools.messageBoxConfirm('آیا از تمدید مجدد اطمینان دارید؟', fn);
    },

    doDamagedTokens: function (grid, rowIndex) {

        var store = grid.getStore(),
            record = store.getAt(rowIndex),
            id = record.get('id'),
            me = this;

        var fn = function () {
            me.RegisterOfficeToken(me, grid, id, 'issueNetworkTokenForDamagedToken');
        };

        Tools.messageBoxConfirm('آیا از صدور مجدد توکن اطمینان دارید؟', fn);
    },

    doDuplicateTokens: function (grid, rowIndex) {

        var store = grid.getStore(),
            record = store.getAt(rowIndex),
            id = record.get('id'),
            me = this;

        var fn = function () {
            me.RegisterOfficeToken(me, grid, id, 'issueReplicaNetworkToken');
        };

        Tools.messageBoxConfirm('آیا از صدور توکن المثنی  اطمینان دارید؟', fn);
    },

    /**
     * Handler of revoke office action
     *
     * @param grid      Reference to the enrollment office grid
     * @param rowIndex  Index of the row which user has selected its revoke button
     */
    doRevokeOffice: function (grid, rowIndex) {
        var store = grid.getStore();
        var record = store.getAt(rowIndex);
        var id = record.get('id'),
            parentId = record.get(EmsObjectName.Department.parentDNId),
            officeType = record.get(EmsObjectName.officeNewEdit.officeType);

        this.selectRowId = id;

//        Tools.messageBoxConfirm('آيا از ابطال حق امتياز دفتر<br />انتخابی اطمينان داريد؟'
        Tools.messageBoxConfirm('آيا از حذف اطمينان داريد؟'//khodayari
            , Ext.bind(this.checkInProgressRequests, this, [id, parentId, officeType]));
    },

    /**
     * Given an office identifier and tries to check its in progress requests. If there is any in progress request, the
     * user has to select a substitution office for them
     *
     * @param officeID  Identifier of the office to check its in progress requests
     */
    checkInProgressRequests: function (officeID, parentId, officeType) {

        Gam.Msg.showWaitMsg();
        Ext.Ajax.request({
            url: this.ns + '/checkInProgressRequests',
            jsonData: {
                enrollmentId: officeID
            },
            success: Ext.bind(function (response) {
                Gam.Msg.hideWaitMsg();
                try {
                    var obj = Ext.decode(response.responseText);
                    if (obj.success) {

                        console.log(obj.inProgressRequestsFlag);
                        if (obj.inProgressRequestsFlag === 'REGULAR_REQUESTS') {//khodayari
                            var offWin = Ext.create('Ems.view.office.choice.Windows', {
                                controller: this,
                                inProgressRequests: obj.inProgressRequestsFlag,
                                parentId: parentId,
                                officeID: officeID,
                                officeType: officeType
                            });
                            Tools.MaskUnMask(offWin);
                            offWin.show();
                        } else if (obj.inProgressRequestsFlag === 'CRITICAL_REQUESTS') {
                            Tools.errorMessageClient("امکان حذف این دفتر بعلت نوع درخواستهای موجود در آن وجود ندارد.");
                        } else if (obj.inProgressRequestsFlag === 'NOCR_OFFICE') {
                            Tools.errorMessageClient("امکان حذف ادارات وجود ندارد.");
                        } else {
                            this.doSubstituteAndDelete(officeID, null, null, false);
                        }
                    } else {
                        Tools.errorMessageServer(obj.messageInfo)
                    }
                } catch (e) {
                    Tools.errorMessageServer(e.message);
//                    alert(e.message);
                }
            }, this),
            failure: function (resop) {
                Gam.Msg.hideWaitMsg();
                Tools.errorFailure();
            }
        });
    },

    /**
     * Handler of delete token action
     * @param grid      Tokens grid
     * @param rowIndex  Index of the item which its delete action has been called
     */
    doDeleteToken: function (grid, rowIndex) {
        //  Displaying a confirmation dialogue to user
        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var message = 'آیا از حذف درخواست صدور توکن <b>{0}</b>  اطمینان دارید؟';
        message = message.replace('{0}', record.get(EmsObjectName.officeNewEdit.oficName));
        Tools.messageBoxConfirm(message, Ext.bind(this.deleteNetworkTokenRequest,
            this,
            [grid, record.get("id")]));
    },
    doChangeUploadPhoto: function (grid, rowIndex) {
        //  Displaying a confirmation dialogue to user
        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var officeSettingType;

        if (record.raw != undefined) {
            officeSettingType = record.raw.uploadPhoto;
        } else {
            officeSettingType = record.data.uploadPhoto;
        }

        var message;
        if (officeSettingType != null && officeSettingType != "" && officeSettingType == "1") {
            message = ' آیا از غیرفعالسازی آپلود عکس  در <b>{0}</b>  اطمینان دارید؟';

        } else {
            message = ' آیا از  فعالسازی آپلود عکس  در <b>{0}</b>  اطمینان دارید؟';
        }

        message = message.replace('{0}', record.get(EmsObjectName.officeNewEdit.oficName));
        Tools.messageBoxConfirm(message, Ext.bind(this.changeOfficeSetting,
            this,
            [grid, record.get("id"), 'UPLOAD_PHOTO']));

    },
    changeOfficeSetting: function (grid, enrollmentOfficeID, officeSettingType) {
        Gam.Msg.showWaitMsg();
        Ext.Ajax.request({
            url: this.ns + '/chageOfficeSetting',
            jsonData: {
                enrollmentId: enrollmentOfficeID,
                officeSettingType: officeSettingType
            },
            success: Ext.bind(function (response) {
                Gam.Msg.hideWaitMsg();
                try {
                    var obj = Ext.decode(response.responseText);
                    if (obj.success) {
                        grid.getStore().load();
                    } else {
                        Tools.errorMessageServer(obj.messageInfo)
                    }
                } catch (e) {
                    alert(e.message);
                }
            }, this),
            failure: function (resop) {
                Gam.Msg.hideWaitMsg();
                Tools.errorFailure();
            }
        });
    },

    doChangeFingerScanSingleMode: function (grid, rowIndex) {


        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var officeSettingType;
        if (record.raw != undefined) {
            officeSettingType = record.raw.fingerScanSingleMode;
        } else {
            officeSettingType = record.data.fingerScanSingleMode;
        }

        var message;
        if (officeSettingType != null && officeSettingType != "" && officeSettingType == "1") {
            message = ' آیا از غیر فعالسازی اخذ اثر انگشت در مد تک انگشتی  در <b>{0}</b>  اطمینان دارید؟';

        } else {
            message = 'آیا از  فعالسازی  اخذ اثر انگشت در مد تک انگشتی  در <b>{0}</b>  اطمینان دارید؟';
        }
        message = message.replace('{0}', record.get(EmsObjectName.officeNewEdit.oficName));
        Tools.messageBoxConfirm(message, Ext.bind(this.changeOfficeSetting,
            this,
            [grid, record.get("id"), 'FINGER_SCAN_SINGLE_MODE']));

    },

    doChangeDisabilityMode: function (grid, rowIndex) {


        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var officeSettingType;
        if (record.raw != undefined) {
            officeSettingType = record.raw.disabilityMode;
        } else {
            officeSettingType = record.data.disabilityMode;
        }
        var message;
        if (officeSettingType != null && officeSettingType != "" && officeSettingType == "1") {
            message = ' آیا از غیر فعالسازی قابلیت اخذ تصویر چهره از معلولین  <b>{0}</b>  اطمینان دارید؟';

        } else {
            message = 'آیا از  فعالسازی قابلیت اخذ تصویر چهره از معلولین <b>{0}</b>  اطمینان دارید؟';
        }
        message = message.replace('{0}', record.get(EmsObjectName.officeNewEdit.oficName));
        Tools.messageBoxConfirm(message, Ext.bind(this.changeOfficeSetting,
            this,
            [grid, record.get("id"), 'DISABILITY_MODE']));

    },

    doChangeReIssueRequest: function (grid, rowIndex) {


        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var officeSettingType;
        if (record.raw != undefined) {
            officeSettingType = record.raw.reIssueRequest;
        } else {
            officeSettingType = record.data.reIssueRequest;
        }
        var message;
//    	  debugger;
        if (officeSettingType != null && officeSettingType != "" && officeSettingType == "1") {
            message = 'آیا از غیرفعالسازی امکان ثبت درخواست صدور مجدد برای دفتر<b>{0}</b>  اطمینان دارید؟';
        } else {
            message = ' آیا از فعالسازی امکان ثبت درخواست صدور مجدد برای دفتر<b>{0}</b>  اطمینان دارید؟';
        }
        message = message.replace('{0}', record.get(EmsObjectName.officeNewEdit.oficName));
        Tools.messageBoxConfirm(message, Ext.bind(this.changeOfficeSetting,
            this,
            [grid, record.get("id"), 'REISSUE_REQUEST']));

    },

    doChangeElderyMode: function (grid, rowIndex) {


        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var officeSettingType;
        if (record.raw != undefined) {
            officeSettingType = record.raw.elderlyMode;
        } else {
            officeSettingType = record.data.elderlyMode;
        }
        var message;
        if (officeSettingType != null && officeSettingType != "" && officeSettingType == "1") {
            message = 'آیا از غیرفعالسازی  مد سالمندی <b>{0}</b>  اطمینان دارید؟';
        } else {
            message = 'آیا از فعالسازی مد سالمندی<b>{0}</b>  اطمینان دارید؟';
        }
        message = message.replace('{0}', record.get(EmsObjectName.officeNewEdit.oficName));
        Tools.messageBoxConfirm(message, Ext.bind(this.changeOfficeSetting,
            this,
            [grid, record.get("id"), 'ELDERLY_MODE']));

    },

    doChangeNMocGeneration: function (grid, rowIndex) {


        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var officeSettingType;
        if (record.raw != undefined) {
            officeSettingType = record.raw.nMocGeneration;
        } else {
            officeSettingType = record.data.nMocGeneration;
        }
        var message;
        if (officeSettingType != null && officeSettingType != "" && officeSettingType == "1") {
            message = 'آیا از غیرفعالسازی پین جایگزین <b>{0}</b>  اطمینان دارید؟';
        } else {
            message = 'آیا از فعالسازی  پین جایگزین <b>{0}</b>  اطمینان دارید؟';
        }
        message = message.replace('{0}', record.get(EmsObjectName.officeNewEdit.oficName));
        Tools.messageBoxConfirm(message, Ext.bind(this.changeOfficeSetting,
            this,
            [grid, record.get("id"), 'NMOC_GENERATION']));

    },

    doChangeAmputationAnnouncment: function (grid, rowIndex) {


        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var officeSettingType;
        if (record.raw != undefined) {
            officeSettingType = record.raw.amputationAnnouncment;
        } else {
            officeSettingType = record.data.amputationAnnouncment;
        }
        var message;
        if (officeSettingType != null && officeSettingType != "" && officeSettingType == "1") {
            message = 'آیا از غیرفعالسازی تغییر وضعیت انگشتان به قطع در شروع اخذ و بی کیفیت در حین اخذ <b>{0}</b>  اطمینان دارید؟';
        } else {
            message = 'آیا از فعالسازی تغییر وضعیت انگشتان به قطع در شروع اخذ و بی کیفیت در حین اخذ <b>{0}</b>  اطمینان دارید؟';
        }
        message = message.replace('{0}', record.get(EmsObjectName.officeNewEdit.oficName));
        Tools.messageBoxConfirm(message, Ext.bind(this.changeOfficeSetting,
            this,
            [grid, record.get("id"), 'AMPUTATION_ANNOUNCMENT']));

    },
    doChangeAllowEditBackground: function (grid, rowIndex) {


        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var officeSettingType;
        if (record.raw != undefined) {
            officeSettingType = record.raw.allowEditBackground;
        } else {
            officeSettingType = record.data.allowEditBackground;
        }
        var message;
        if (officeSettingType != null && officeSettingType != "" && officeSettingType == "1") {
            message = 'آیا از غیرفعالسازی امکان ویرایش پس زمینه <b>{0}</b>  اطمینان دارید؟';
        } else {
            message = 'آیا از فعالسازی  امکان ویرایش پس زمینه <b>{0}</b>  اطمینان دارید؟';
        }
        message = message.replace('{0}', record.get(EmsObjectName.officeNewEdit.oficName));
        Tools.messageBoxConfirm(message, Ext.bind(this.changeOfficeSetting,
            this,
            [grid, record.get("id"), 'ALLOW_EDIT_BACKGROUND']));

    },
    doChangeAllowAmputatedFinger: function (grid, rowIndex) {
        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var officeSettingType;
        if (record.raw != undefined) {
            officeSettingType = record.raw.allowAmputatedFinger;
        } else {
            officeSettingType = record.data.allowAmputatedFinger;
        }
        var message;
        if (officeSettingType != null && officeSettingType != "" && officeSettingType == "1") {
            message = 'آیا از غیرفعالسازی مکان انتخاب وضعیت قطع برای انگشتان برای سالمندان <b>{0}</b>  اطمینان دارید؟';
        } else {
            message = 'آیا از فعالسازی مکان انتخاب وضعیت قطع برای انگشتان برای سالمندان <b>{0}</b>  اطمینان دارید؟';
        }
        message = message.replace('{0}', record.get(EmsObjectName.officeNewEdit.oficName));
        Tools.messageBoxConfirm(message, Ext.bind(this.changeOfficeSetting,
            this,
            [grid, record.get("id"), 'ALLOW_AMPUTATED_FINGER']));

    },
    doChangeAllowChangeFinger: function (grid, rowIndex) {
        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var officeSettingType;
        if (record.raw != undefined) {
            officeSettingType = record.raw.allowChangeFinger;
        } else {
            officeSettingType = record.data.allowChangeFinger;
        }
        var message;
        if (officeSettingType != null && officeSettingType != "" && officeSettingType == "1") {
            message = 'آیا از غیرفعالسازی امکان تغییر وضعیت انگشتان در حین اخذ انگشت حتی پس از اخذ نخستین تصویر برای سالمندان <b>{0}</b>  اطمینان دارید؟';
        } else {
            message = 'آیا از فعالسازی امکان تغییر وضعیت انگشتان در حین اخذ انگشت حتی پس از اخذ نخستین تصویر برای سالمندان <b>{0}</b>  اطمینان دارید؟';
        }
        message = message.replace('{0}', record.get(EmsObjectName.officeNewEdit.oficName));
        Tools.messageBoxConfirm(message, Ext.bind(this.changeOfficeSetting,
            this,
            [grid, record.get("id"), 'ALLOW_CHANGE_FINGER']));

    },
    doChangeUseScannerUI: function (grid, rowIndex) {


        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var officeSettingType;
        if (record.raw != undefined) {
            officeSettingType = record.raw.useScannerUI;
        } else {
            officeSettingType = record.data.useScannerUI;
        }
        var message;
        if (officeSettingType != null && officeSettingType != "" && officeSettingType == "1") {
            message = 'آیا از غیرفعالسازی امکان استفاده از اسکنرهای قدیمی <b>{0}</b>  اطمینان دارید؟';
        } else {
            message = 'آیا از فعالسازی  امکان استفاده از اسکنرهای قدیمی <b>{0}</b>  اطمینان دارید؟';
        }
        message = message.replace('{0}', record.get(EmsObjectName.officeNewEdit.oficName));
        Tools.messageBoxConfirm(message, Ext.bind(this.changeOfficeSetting,
            this,
            [grid, record.get("id"), 'USE_SCANNER_UI']));

    },


    /**
     * Calls a service to remove given token from the list of token issuance requests
     *
     * @param grid                  Tokens grid
     * @param enrollmentOfficeID    Identifier of the enrollment office which its token request have to be deleted
     */
    deleteNetworkTokenRequest: function (grid, enrollmentOfficeID) {
        Gam.Msg.showWaitMsg();
        Ext.Ajax.request({
            url: this.ns + '/deleteToken',
            jsonData: {
                enrollmentId: enrollmentOfficeID
            },
            success: Ext.bind(function (response) {
                Gam.Msg.hideWaitMsg();
                try {
                    var obj = Ext.decode(response.responseText);
                    if (obj.success) {
                        grid.getStore().load();
                    } else {
                        Tools.errorMessageServer(obj.messageInfo)
                    }
                } catch (e) {
                    alert(e.message);
                }
            }, this),
            failure: function (resop) {
                Gam.Msg.hideWaitMsg();
                Tools.errorFailure();
            }
        });
    },

    RegisterOfficeToken: function (view, grid, id, url) {
        Gam.Msg.showWaitMsg();

        Ext.Ajax.request({
            url: view.ns + '/' + url,
            jsonData: {
                enrollmentId: id
            },
            success: function (response) {
                {
                    Gam.Msg.hideWaitMsg();
                    try {
                        var obj = Ext.decode(response.responseText);
                        if (obj.success) {
                            grid.getStore().load();
                        } else {
                            Tools.errorMessageServer(obj.messageInfo)
                        }
                    } catch (e) {
                        alert(e.message);
                    }
                }
            },
            failure: function (resop) {
                Gam.Msg.hideWaitMsg();
                Tools.errorFailure();
            }
        });
    },

    RegisterRevokeOffice: function (btn) {
        var win = btn.up("toolbar").up('window');

        var grid = this.getOfficegrid();
        ///////////////////////////////////
//        sm = grid.getSelectionModel();
//        var item =  sm.getSelection();
//        if (item.length == 0) {
//            Tools.errorMessageClient("هیچ سطری از لیست مشخص نشده")
//            return false;
//        }
//            var itemID = (item[0].data).id;
        ///////////////////////////////////
//         
//        if (this.selectRowId <= 0) {
//            Tools.errorMessageClient("هیچ سطری از لیست مشخص نشده")
//            return false;
//        }

        if (!win.down('form').getForm().isValid()) {
            return false;
        }
//        var reason = Ext.getCmp(EmsObjectName.userRequestTokenDeleteTokenForm.reasonDelete).getValue();
//        var description = Ext.getCmp(EmsObjectName.userRequestTokenDeleteTokenForm.descriptionDeleteToken).getValue();

        var replacedEnrollmentOfficeId = Ext.getCmp(EmsObjectName.userRequestTokenDeleteTokenForm.replacedEnrollmentOfficeId).getValue();

        Gam.Msg.showWaitMsg();

        this.doSubstituteAndDelete(this.selectRowId, replacedEnrollmentOfficeId, win, true);//khodayari
    },
    doSubstituteAndDelete: function (selectRowId, replacedEnrollmentOfficeId, win, inProgressFlag) {//khodayari
        var me = this;
        var grid = this.getOfficegrid();
        Ext.Ajax.request({
//          url: me.ns + '/revokeNetworkToken',
            url: me.ns + '/substituteAndDelete',
            jsonData: {
                enrollmentId: selectRowId,
//              reason: reason + '-' + description,
                substitutionOfficeId: replacedEnrollmentOfficeId
            },
            success: function (response) {
                {
                    Gam.Msg.hideWaitMsg();
//                  try {
                    var obj = Ext.decode(response.responseText);
                    if (obj.success) {
                        grid.getStore().load();
                        console.log("in progress flag is : " + inProgressFlag);
                        if (inProgressFlag) {
                            //todo
                            Tools.successMessage("درخواست های دفتر مورد نظر به دفتر انتخاب شده منتقل شد");
                        }
                        if (win)
                            win.close();
                    } else {
                        Tools.errorMessageServer(obj.messageInfo);
                    }
//                  } catch (e) {
//                      alert(e.message);
//                  }
                }
            },
            failure: function (resop) {
                Gam.Msg.hideWaitMsg();
                Tools.errorFailure();
            }
        });
    }
});
