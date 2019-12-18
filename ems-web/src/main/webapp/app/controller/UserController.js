Ext.define('Ems.controller.UserController', {
    extend: 'Gam.app.controller.LocalDialogBasedGrid',

    ns: 'extJsController/user',

    statics: {
        statefulComponents: [
            'wUserGrid',
            'wUserGridAdd',
            'wUserGridDelete',
            'wUserGridEditing',
            'wUserGridViewing',
            'wUserGridTokenManagement',
            'wUserGridActiveInactive'
        ]
    },

    requires: ['Gam.button.Add',
        'Ems.controller.util.ExcelExporter'],

    views: [
        'user.Grid',
        'user.access.MultiSelect',
        'user.role.MultiSelect',
        'user.NewEdit.Form',
        'user.view.Windows',
        'user.NewEdit.Windows',

        'user.RequestToken.grid'
        , 'user.RequestToken.cmbReasonIssuance'
        , 'user.RequestToken.deleteToken.Windows'
        , 'user.RequestToken.form'
        , 'user.RequestToken.deleteToken.Form'
    ], stores: [
        'UserStore',
        'userRequestTokenGridStore',
        'userAccessStore',
        'userRoleStore',
        'userAccessMultiSelectStore',
        'userOrganizationalStatusStore'
    ], refs: [
        {
            ref: 'userRoleMultiSelect',
            selector: 'userrolemultiselect'
        },
        {
            ref: 'userAccessMultiSelect', selector: 'useraccessmultiselect'
        },
        {
            ref: 'userNewEditForm',
            selector: 'userneweditform'
        },
        {
            ref: 'usercmborganizationalstatus', selector: 'usercmborganizationalstatus'
        },
        {
            ref: 'userGrid',
            selector: 'usergrid'
        },
        {
            ref: 'newEditWindows',
            selector: 'neweditwindows'
        },
        // begin  userRequestToken
        {
            ref: 'userRequestTokenDeleteTokenForm',
            selector: 'userrequesttokendeletetokenform'
        },
        {
            ref: 'userRequestTokenCmbReasonIssuance',
            selector: 'userrequesttokencmbreasonissuance'
        },
        {
            ref: 'userRequestTokenGrid',
            selector: 'userrequesttoken'
        },
        {
            ref: 'userRequestTokenForm',
            selector: 'userrequesttokenform'
        },
        {
            ref: 'userRequestToken',
            selector: 'userrequesttoken'
        },
        {
            ref: 'userRequestTokenDeleteTokenForm',
            selector: 'userrequesttokendeletetokenform'

        },
        {
            ref: 'usercmborganizationalstatusstore',
            selector: 'usercmborganizationalstatusstore'
        }
        // end userRequestToken

    ], constructor: function (config) {
        this.me = this;
        this.personId = null;
        this.lastValue = null;
        this.userActive = null;
        this.hasActiveToken = null;
        this.excelExporter = new Ems.controller.util.ExcelExporter();
        this.callParent(arguments);
    }, init: function () {

        var me = this;

        /*        this.BirthCertSeriesCheck=function(value,field){
         // var st = this.CheckLength(value,field,6);
         var regexIdentitySerial = /^\d{6}$/i;
         //field.activeError="<ul><li class='last'>مقدار این فیلد معتبر نمی باشد</li></ul>";
         var val=parseInt(value,10);
         var regexVal= regexIdentitySerial.test(value)
         //
         var minNumber = 100001 ,
         maxNumber = 999999 ;

         if(((minNumber<val) && (val<maxNumber)) && regexVal){
         return true;
         } else{
         return false;
         }
         }*/

        /*
         this.CheckLength=function(value,field,lengthVar){
         var val=value+"";
         var arr=val.split("");
         var result="";
         Ext.each(arr,function(item){
         result+=item;
         if(result.length===lengthVar){
         Ext.getCmp(field.id).setValue(result);
         return true;
         }
         });
         }*/

        this.control({
            //"[action=ManagementToken]":{'click':this.getManagementToken},

            "userrequesttoken actioncolumn": {'actionclick': this.onActionClick},

            "[action=butUserAccessChoice]": {'click': this.getUserAccessChoice, scope: this},

            "button[itemId=btnRoleRemove]": {"click": this.getBtnRoleRemove, scope: this},

            "button[itemId=btnRemoveAccess]": {"click": this.getBtnRemoveAccess, scope: this},

            "[action=butUserRolesChoice]": {'click': this.getUserRolesChoice, scope: this},

            "[action=btnNewEditUser]": {'click': this.getSaveFromEdit, scope: this},

            "userrequesttokenform  #tokenManagementRadioGroup": {
                'change': this.onTokenManagementRadioChange,
                scope: this
            },

            "#roleAutocomplet": {
                select: function (autocomplete, record, value) {
                    var store = Ext.getCmp('roleMultiSelectds').getStore(),
                        autoModel = {value: null, text: null},
                        i;

                    for (i = 0; i < record.length; i++) {

                        var reapetData = Tools.checkDataInStore(store, 'id', record[i].get('acId'));
                        if (reapetData == false) {
                            autoModel.id = record[i].get('acId');
                            autoModel.name = record[i].get('acName');

                            if (store.find('acId', autoModel.id, 0, true, true) == -1) {
                                store.add(autoModel);
                                this.setRoles(autoModel.id);
                                autoModel.name = null;
                                autoModel.id = null;
                            }
                        }
                    }
                }
            },

            "#accessAutocomplet": {
                select: function (autocomplete, record, value) {
                    var store = Ext.getCmp('accessMultiSelectds').getStore(),
                        autoModel = {id: null, name: null},
                        i;

                    for (i = 0; i < record.length; i++) {

                        var reapetData = Tools.checkDataInStore(store, 'id', record[i].get('acId'));
                        if (reapetData == false) {
                            autoModel.id = record[i].get('acId');
                            autoModel.name = record[i].get('acName');

                            //if(store.find('acId',autoModel.value,0,true, true)==-1){
                            store.add(autoModel);
                            this.setAccesses(autoModel.id);
                            autoModel.name = null;
                            autoModel.id = null;
                            //}
                        }
                    }
                }
            },
            //begin RequestToken
            "[action=btnRegisterDeleteTokenWindows]": {
                click: this.onBtnRegisterDeleteTokenWindows
                //scope:this
            },
            "[id=grdUsers]": {
                eventManagementToken: function (view, grid, rowIndex, colIndex) {
                    this.managementToken(view, grid, rowIndex, colIndex);
                }
            },
            "[itemId=RegisterRequestToken]": {
                click: this.onRegisterRequestToken
                //scope:this
            },
            "[id=iduserrequesttoken]": {
                eventRequestExtensionToken: function (view, grid, rowIndex, colIndex) {
                    this.RequestExtensionToken(view, grid, rowIndex, colIndex);
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
                        if ((fields[i] !== 'clientId') && (fields[i] !== 'password') &&
                            (fields[i] !== 'cPassword') && (fields[i] !== 'permissions') &&
                            (fields[i] !== 'roles') && (fields[i] !== 'email') &&
                            (fields[i] !== 'requestStatusString') && (fields[i] !== 'id') &&
                            (fields[i] !== 'birthCertNum') && (fields[i] !== 'birthCertSeries') &&
                            (fields[i] !== 'fatherName') && (fields[i] !== 'departmentId')) {
                            finalFields.push(fields[i]);
                        }
                    }

                    this.excelExporter.doExportExcel(btn, {fields: Ext.encode(finalFields)});
                }
            }
            //end RequestToken
            //'userrequestgrid actioncolumn':{ actionclick: this.onActionClick }

        });


        this.callParent(arguments);

        /*

         shortcut.add("Ctrl+D",function() {
         alert("Bold");
         });
         //Remove the shortcut
         shortcut.remove("Ctrl+D");



         shortcut.add("Ctrl+A",function(){

         var g=Ext.getCmp('grdUsers');
         var grid_t=this.getUserGrid();
         if(grid_t){
         alert(grid_t);
         }
         });

         */


    },
    getSaveFromEdit: function (btn) {

        var form = this.getUserNewEditForm();

        if (!form.getForm().isValid()) {
            return false;
        }

        var valueForm = form.getValues();
        var departmen = this.getUsercmborganizationalstatus();

        var store = Ext.create('Ems.store.UserStore', {baseUrl: this.ns});
        store.add(valueForm);
        var dataForm = store.getAt(0);
        var grid = this.getUserGrid();
        var me = this;
        if (departmen.value === null)
            departmen.value = this.lastValue;

        dataForm.set(EmsObjectName.userForm.departmentId, departmen.value);
        dataForm.set(EmsObjectName.userForm.departmentName, departmen.rawValue);

        dataForm.set(EmsObjectName.userForm.id, this.personId);
        dataForm.set(EmsObjectName.userForm.userActive, this.userActive);

//        if(dataForm.get(EmsObjectName.userForm.departmentId)==="");
//            dataForm.set(EmsObjectName.userForm.departmentId,this.lastValue);

        if (this.PermissionUserEditInfo(dataForm)) {
            var win = Ext.getCmp('idNewEditWindow');
            Gam.Msg.showWaitMsg();
            var data = Ext.decode(Tools.toJson(dataForm.data, true));
            Ext.Ajax.request({

                url: me.ns + '/save', jsonData: {
                    records: data
                },
                success: function (response) {
                    var obj = Ext.decode(response.responseText);
                    Gam.Msg.hideWaitMsg();
                    if (obj.success) {
                        win.close();
                        grid.getStore().load();
                    } else {
                        Gam.Msg.hideWaitMsg();
                        Tools.errorMessageServer(obj.messageInfo)
                    }
                },
                failure: function (resp) {
                    Gam.Msg.showWaitMsg();
                    Tools.errorFailure();
                }
            });
        }
    },


    doViewing: function (grid, rowIndex) {
        //
        var personId = grid.getStore().getAt(rowIndex).get(EmsObjectName.userForm.id),
            me = this;
        Ext.Ajax.request({
            url: me.ns + '/load', jsonData: {
                ids: personId
            }, success: function (resp) {
                var data = Ext.decode(resp.responseText);
                if (data.success) {
                    var rec = data.records;
                    if (rec != null) {
                        me.loadFormEditView(rec[0], 'View');
                    } else {
                        Tools.errorMessageClient(Ems.ErrorCode.client.EMS_C_004);
                    }
                } else {
                    Tools.errorMessageServer(obj.messageInfo)
                }
            }, failure: function (resp) {
                Tools.errorFailure();
            }
        });
    }, doEditing: function (grid, rowIndex) {

//        if(Tools.user.Editing != 0){
//            return false;
//        }else{
//            Tools.user.Editing++;
//        }
        var store = grid.getStore(),
            record = store.getAt(rowIndex),
            id = record.get('id'),
            me = this;

        Tools.user.StyleObject = 'margin-left: 25px;';

        this.personId = id;
        this.userActive = record.get(EmsObjectName.userForm.userActive);

        Ext.Ajax.request({

            url: me.ns + '/load', jsonData: {
                ids: id
            }, success: function (resp) {
                var data = Ext.decode(resp.responseText);
                if (data.success) {
                    var rec = data.records;
                    if (rec != null) {

                        me.loadFormEditView(rec[0], 'Edit');
                        //store.load();
                        //win.show();
                    } else {
                        Tools.errorMessageServer(obj.messageInfo)
                    }

                }
                Tools.user.StyleObject = null;
            }, failure: function (resp) {
                Tools.user.StyleObject = null;
                Tools.errorFailure();
            }
        });
    }, loadFormEditView: function (record, mode) {
        var win = null;

        if (mode === 'Edit') {
            win = Ext.create('Ems.view.user.NewEdit.Windows');
            Tools.MaskUnMask(win);

            if (Ext.isIE6 || Ext.isIE7) {
                win.show();
                win.close();
                win = Ext.create('Ems.view.user.NewEdit.Windows');
            }

        } else if (mode === 'View') {
            win = Ext.create('Ems.view.user.view.Windows');
            Tools.MaskUnMask(win);
        }

        var userRole = null;
        var userAccess = null;

        var accessMultiSelectds = Ext.getCmp('accessMultiSelectds');
        var roleMultiSelectds = Ext.getCmp('roleMultiSelectds');

        userRole = (mode === "Edit") ? roleMultiSelectds : userRole;
        userAccess = (mode === 'Edit') ? accessMultiSelectds : userAccess;

        //userRole = (mode==="View") ? (win.down('form').down('userviewrole')) : userRole;
        //userAccess= (mode==='View') ? win.down('form').down('userviewaccess') : userAccess;
        userRole = (mode === "View") ? roleMultiSelectds : userRole;
        userAccess = (mode === 'View') ? accessMultiSelectds : userAccess;


        var store = Ext.create('Ems.store.UserStore', {baseUrl: this.ns});
        store.add(record);
        var rec = store.getAt(0),
            form = win.down('form');

        if (mode === 'Edit') {
            try {
                if (rec.get('userName') === Tools.trim('gaasadmin')) {
                    Ext.getCmp(EmsObjectName.userForm.userName).readOnly = true;
                } else {
                    Ext.getCmp(EmsObjectName.userForm.userName).readOnly = false;
                }
            } catch (e) {
                Ext.MessageBox.alert(e.message);
            }


            form.loadRecord(rec);

        } else {


            form.loadRecord(Ext.create('Ems.model.UserEditViewModel', {
                r_firstName: rec.get(EmsObjectName.userForm.firstName),
                r_lastName: rec.get(EmsObjectName.userForm.lastName),
                r_userName: rec.get(EmsObjectName.userForm.userName),
                r_fatherName: rec.get(EmsObjectName.userForm.fatherName),
                r_nid: rec.get(EmsObjectName.userForm.nationalCode),
                r_birthCertSeries: rec.get(EmsObjectName.userForm.certificateSerial),
                r_birthCertNum: rec.get(EmsObjectName.userForm.certificateId),
                r_email: rec.get(EmsObjectName.userForm.email),
                r_departmentName: rec.get(EmsObjectName.userForm.departmentName)
            }));
        }


        if (mode === 'Edit') {
            var departmen = this.getUsercmborganizationalstatus();
            departmen.rawValue = rec.get(EmsObjectName.userForm.departmentName);
            departmen.value = rec.get(EmsObjectName.userForm.departmentId);
            this.lastValue = rec.get(EmsObjectName.userForm.departmentId);
            //departmen.lastValue = rec.get(EmsObjectName.userForm.departmentId);

        }
        //role gird
        var storeRole = Ext.create('Ems.store.userRoleMultiSelectStore', {baseUrl: this.ns});
        var dataRoleGrid = record.roleList;
        var storeRoleGrid = userRole.getStore();

        Ext.each(dataRoleGrid, function (item) {
            storeRoleGrid.add(item);
        });


        //access grid
        var storeAccess = Ext.create('Ems.store.userAccessMultiSelectStore', {baseUrl: this.ns});
        var dataAccessGrid = record.permissionList;
        var storeAccessGrid = userAccess.getStore();

        Ext.each(dataAccessGrid, function (item) {
            storeAccessGrid.add(item);
        });

        win.show();

    }, setRoles: function (acId) {

        var objRoles = Ext.getCmp(EmsObjectName.userForm.userRole),
            value = objRoles.getValue();
        if (value && typeof value != 'undefined') {
            if (value.length > 0) {
                value += ',' + acId;
            } else {
                value += acId;
            }
        } else {
            value += acId;
        }
        objRoles.setValue(value);
    }, setAccesses: function (acId) {

        var objAccesses = Ext.getCmp(EmsObjectName.userForm.userAccess),
            value = objAccesses.getValue();

        if (value && typeof value != 'undefined') {
            if (value.length > 0) {
                value += ',' + acId;
            } else {
                value += acId;
            }
        } else {
            value += acId;
        }
        objAccesses.setValue(value);

    }, doInactive: function (grid, rowIndex, colIndex) {
        this.getActiveInActiveUserEvent(grid, rowIndex, 'InActiveUser');
    }, doActive: function (grid, rowIndex, colIndex) {
        this.getActiveInActiveUserEvent(grid, rowIndex, 'ActiveUser');
    }, doTokenManagement: function (grid, rowIndex) {
//        Ext.Loader.setConfig({enabled:true});
//        this.application.getController('userRequestTokenController');
//        this.application.fireEvent('managementToken', grid,rowIndex) ;
        var win = Ext.create('Ems.view.user.RequestToken.windows', {controller: this});
        Tools.MaskUnMask(win);
        var userStore = grid.getStore(),
            record = userStore.getAt(rowIndex),
            personId = record.get('id'),
            detailGrid = win.down('grid'),
            me = this;

        Tools.UserRequestToken.userId = personId;


        if (detailGrid != null && personId != null) {
            var detailStore = detailGrid.getStore();

            me.getInformAcceptableTypes(this, personId, null, 'Add');

            if (detailStore.readParams == null)
                detailStore.readParams = {perId: personId};
            else
                detailStore.readParams.perId = personId;
            win.show();
        } else {
            Tools.errorMessageClient(Ems.ErrorCode.client.EMS_C_003);
        }


    }, getActiveInActiveUserEvent: function (grid, rowIndex, mode) {
        var me = this,
            inAct = 'آیا از فعال کردن کاربر اطمینان دارید؟',
            act = 'آیا از غیر فعال کردن کاربر اطمینان دارید؟';

        var fn = function () {
            me.ActiveInActivePerson(me, grid, rowIndex, mode);
        }
        Tools.messageBoxConfirm(((mode === 'ActiveUser') ? act : inAct), fn);

    }, getBtnRemoveAccess: function () {

        var multiSelect = Ext.getCmp('accessMultiSelectds');
        this.BtnRemove(multiSelect);
    }, getUserAccessChoice: function () {

        var cmbAccessList = Ext.getCmp('cmbUserAccessChoice');
        accessValue = cmbAccessList.getValue();
        if (accessValue) {
            var storeCombo = this.getUserAccessStoreStore();
            var accessGrid = this.getUserAccessGrid().getStore();

            storeCombo.clearFilter();
            storeCombo.filter(EmsObjectName.comboBox.code, accessValue);

            var newRec = storeCombo.first();

            var check = Tools.checkDataInStore(accessGrid, EmsObjectName.comboBox.code, newRec.get(EmsObjectName.comboBox.code));
            if (!check)
                accessGrid.add(newRec);

        }
    }, getUserRolesChoice: function () {

        var cmbRoleList = Ext.getCmp('cmbUserRolesChoice');
        RoleValue = cmbRoleList.getValue();

        if (RoleValue) {
            var storeCombo = this.getUserRoleStoreStore();
            var roleGrid = this.getUserRoleGrid();

            storeCombo.clearFilter();
            storeCombo.filter(EmsObjectName.comboBox.code, cmbRoleList.getValue());

            var newRec = storeCombo.first();

            var check = Tools.checkDataInStore(roleGrid.getStore(), EmsObjectName.comboBox.code, newRec.get(EmsObjectName.comboBox.code));
            if (!check)
                roleGrid.getStore().add(newRec);
        }
    }, BtnRemove: function (obj) {
        var selectCheckBox = obj.items.items[0].selModel.selected;
        var lengthRec = selectCheckBox.length;
        for (var i = 0; i < lengthRec; i++) {
            var records = selectCheckBox.items[0];
            obj.getStore().remove(records);
        }
        this.setValueHiddenFields(obj.id);
    }, setValueHiddenFields: function (objName) {

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

        } else if (objName === Tools.trim('accessMultiSelectds')) {

            var userAccess = this.getUserAccessMultiSelect().down('form').down('multiselect'),
                storeUserAccess = userAccess.getStore();
            Ext.getCmp(EmsObjectName.userForm.userAccess).setValue('');

            Ext.each(storeUserAccess.data.items, function (items) {
                var r = items.get('id');
                me.setAccesses(r);
            });
        }
    }, getBtnRoleRemove: function () {
        var obj = Ext.getCmp('roleMultiSelectds');
        this.BtnRemove(obj);
    }, ActiveInActivePerson: function (view, grid, rowIndex, mode) {
        var store = grid.getStore(),
            record = store.getAt(rowIndex);

        var modeId;
        if (mode === 'ActiveUser')
            modeId = 'F';
        else if (mode === 'InActiveUser')
            modeId = 'T';

        if ((record.get(EmsObjectName.userForm.userName) != Tools.trim('gaasadmin'))) {
            if (modeId === 'T' || modeId === 'F') {
                Ext.Ajax.request({
                    url: view.ns + '/changeStatus', params: {
                        ids: record.get(EmsObjectName.userForm.id)
                    }, success: function (resp) {
                        var data = Ext.decode(resp.responseText);
                        if (data.success) {
                            record.set(EmsObjectName.userForm.userActive, modeId);
                            record.commit();
                        } else {
                            Tools.errorMessageServer(data.messageInfo)
                        }
                    }, failure: function () {
                        Tools.errorFailure();
                    }
                });
            } else {
                Tools.errorMessageClient(Ems.ErrorCode.client.EMS_C_005);
            }
        } else {
            Tools.errorMessageClient(Ems.ErrorCode.client.EMS_C_006);
        }
    },

    ///begin userRequestTokenContorller

    onBtnRegisterDeleteTokenWindows: function (but) {

        var win = Ext.getCmp('idRequestTokenDeleteTokenWindows');//but.up(this.initViewType).up('window');
        Tools.MaskUnMask(win);
        var form = this.getUserRequestTokenDeleteTokenForm();//.getForm().getValues();
        var value = form.getValues();
        var me = this;
        var store = this.getUserRequestToken().getStore();

        Gam.Msg.showWaitMsg();

        Ext.Ajax.request({
            url: me.ns + '/revokePersonToken',
            jsonData: {
                description: value.descriptionDeleteToken,
                reason: value.reasonDelete,
                tokenId: store.getAt(win.rowIndex).get('id')
            },
            success: function (response) {
                {

                    Gam.Msg.hideWaitMsg();
                    try {
                        var obj = Ext.decode(response.responseText);
                        if (obj.success) {
                            store.load();
                            win.close();
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
    onRegisterRequestToken: function () {
        if (Tools.UserRequestToken.RegisterRequestToken === 0) {
            var radio = Ext.getCmp('tokenManagmentRadio').getChecked();
            var radioChecked = null;
            Ext.each(radio, function (item) {
                if (item.checked === true)
                    radioChecked = item;
            });

            if (radioChecked == null) {
                Tools.errorMessageClient("لطفا نوع توکن را انتخاب نمایید");
                Tools.UserRequestToken.RegisterRequestToken++;
                return;
            } else {
                var record = this.getUserRequestTokenForm().getValues();

                if (!record.reasonDeleteToken) {
                    Tools.errorMessageClient("لطفا دلیل صدور توکن را انتخاب نمایید");
                    Tools.UserRequestToken.RegisterRequestToken++;
                    return;
                } else if (record.reasonDeleteToken === 'N') {
                    if (Tools.UserRequestToken.hasActiveToken.indexOf(record.tokenType) >= 0) {
                        Tools.errorMessageClient("اولین صدور این توکن برای این کاربر نیست. لطفا دلیل درست را انتخاب نمایید.");
                        Tools.UserRequestToken.RegisterRequestToken++;
                        return;
                    }
                }
            }
            this.onSaveRequestToken(radioChecked);
            Tools.UserRequestToken.RegisterRequestToken++;
        }
    },
    onSaveRequestToken: function (radioChecked) {
        var me = this;
        var record = this.getUserRequestTokenForm().getValues();
        if (record.reasonDeleteToken == "D" && record.tokenType == "S") {
            var msg = " با ثبت درخواست توکن، توکن قبلی شما ابطال خواهد شد. آیا اطمینان دارید؟ ";

            var fn = function () {
                me.saveRequest(record, radioChecked);
            };

            Tools.messageBoxConfirm(msg, fn);
            return;
        } else {
            me.saveRequest(record, radioChecked);
        }


    },

    saveRequest: function (record, radioChecked) {
        var tt = record.tokenType;
        var reas = record.reasonDeleteToken,
            me = this;
        Gam.Msg.showWaitMsg();
        Ext.Ajax.request({
            url: this.ns + '/issuePersonToken',
            jsonData: {
                personId: Tools.UserRequestToken.userId,
                reason: reas,
                tokenType: tt

            }, success: function (resp) {
                Gam.Msg.hideWaitMsg();
                var data = Ext.decode(resp.responseText);
                if (data.success) {
                    var st = Tools.UserRequestToken.userId;
                    var detailGrid = me.getUserRequestTokenGrid();

                    if (detailGrid != null) {
                        var detailStore = detailGrid.getStore();

                        if (detailStore.readParams == null) {
                            detailStore.readParams = {perId: Tools.UserRequestToken.userId};
                        } else {
                            detailStore.readParams.perId = Tools.UserRequestToken.userId;
                        }
                        me.getInformAcceptableTypes(me, Tools.UserRequestToken.userId, radioChecked, 'Add');
                        detailGrid.getStore().load();
                        detailGrid.getView().refresh();

                    }
                } else {
                    Gam.Msg.hideWaitMsg();
                    Tools.errorMessageServer(data.messageInfo)
                }
            }, failure: function (response) {
                Gam.Msg.hideWaitMsg();
                Tools.errorFailure();
            }

        });
    },
    doReadyToDeliver: function (grid, rowIndex) {
        var me = this;
        var fn = function () {
            me.getDoAction(grid, rowIndex, 'deliverPersonToken');
        };
        Tools.messageBoxConfirm('آیا از تحویل توکن اطمینان دارید؟', fn);

    },
    doDelivered: function (grid, rowIndex) {
        var me = this;
        var fn = function () {
            me.getDoAction(grid, rowIndex, 'reissuePersonToken');
        };

        Tools.messageBoxConfirm('آیا از تمدید توکن اطمینان دارید؟', fn);

    },
    doRevoke: function (grid, rowIndex) {  //
        var win_t;
        if (!win_t) {
            win_t = Ext.create('Ems.view.user.RequestToken.deleteToken.Windows', {grid: grid, rowIndex: rowIndex});
            Tools.MaskUnMask(win_t);
            win_t.show();
        }
    },

    /**
     * Handler of delete action which deletes token request
     * @param grid      Tokens grid
     * @param rowIndex  Index of the item which its delete action has been called
     */
    doDelete: function (grid, rowIndex) {
        //  Displaying a confirmation dialogue to user
        var gridStore = grid.getStore();
        var record = gridStore.getAt(rowIndex);
        var message = 'آیا از حذف درخواست صدور توکن <b>{0}</b>  اطمینان دارید؟';
        var code = record.get(EmsObjectName.userRequestToken.code);
        switch (code) {
            case "AUTHENTICATION":
                code = 'احراز هویت';
                break;
            case "SIGNATURE":
                code = 'امضا';
                break;
            case "ENCRYPTION":
                code = 'رمزنگاری';
                break;
        }
        message = message.replace('{0}', code);
        Tools.messageBoxConfirm(message, Ext.bind(this.deleteUserTokenRequest, this, [grid, record.get("id")]));
    },

    /**
     * Calls a service to remove given token from the list of token issuance requests
     *
     * @param grid            Tokens grid
     * @param tokenRequestID  Identifier of the token request to be removed from database
     */
    deleteUserTokenRequest: function (grid, tokenRequestID) {
        Gam.Msg.showWaitMsg();
        Ext.Ajax.request({
            url: this.ns + '/deleteToken',
            jsonData: {
                tokenId: tokenRequestID
            },
            success: Ext.bind(function (response) {
                Gam.Msg.hideWaitMsg();
                try {
                    var obj = Ext.decode(response.responseText);
                    if (obj.success) {
                        grid.getStore().load();
                        this.getInformAcceptableTypes(this, Tools.UserRequestToken.userId, null, 'Add');
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

    getDoAction: function (grid, rowIndex, action) {

        var store = grid.getStore(),
            record = store.getAt(rowIndex);

        var me = this;

        Gam.Msg.showWaitMsg();

        Ext.Ajax.request({
            url: me.ns + '/' + action,
            jsonData: {
                // personId:Tools.UserRequestToken.userId,
                tokenId: record.get('id')
            },
            success: function (response) {
                {
                    Gam.Msg.hideWaitMsg();
                    try {
                        var obj = Ext.decode(response.responseText);
                        if (obj.success) {
                            store.load();
                        } else {
                            Tools.errorMessageServer(obj.messageInfo)
                        }
                    } catch (e) {
                        alert(e.message);
                    }
                    var win = Ext.getCmp('idRequestTokenDeleteTokenWindows');
                    Tools.MaskUnMask(win);
                }
            },
            failure: function (resp) {
                Gam.Msg.showWaitMsg();
                Tools.errorFailure();
            }
        });
    },
    getPositionRadioGroup: function (objRadio, data, mode) {
        if (mode != null) {
            if (!(data === null) || (Tools.trim(data) === "")) {
                var radiogroupToken = Ext.getCmp('tokenManagmentRadio');//(win).down('form').down('radiogroup')
                var CodeId = radiogroupToken.items.items;

                radiogroupToken.reset();

                try {

                    var i = 0;
                    while (i <= 2) {
                        CodeId[i].disable();
                        CodeId[i].checked = false;
                        CodeId[i].rawValue = false;
                        CodeId[i].value = false;
                        i++;
                    }

                    if (data.indexOf('A') >= 0) {
                        (mode === 'Add') ? CodeId[0].enable() : CodeId[0].disable();
                    }
                    if (data.indexOf('S') >= 0) {
                        (mode === 'Add') ? CodeId[1].enable() : CodeId[1].disable();
                    }
                    if (data.indexOf('E') >= 0) {
                        (mode === 'Add') ? CodeId[2].enable() : CodeId[2].disable();
                    }


                } catch (err) {
                    alert(err.message);
                }
            } else if ((Tools.trim(data) === "") && (objRadio != null)) {
                objRadio.checked = false;
                objRadio.disable = true;
                objRadio.value = false;
            }
        } else {
            Tools.errorMessageClient(Ems.ErrorCode.client.EMS_C_007);
        }
    },
    getInformAcceptableTypes: function (view, personId, objRadio, mode) {
        Ext.Ajax.request({
            url: view.ns + '/informAcceptableTypes',
            jsonData: {
                personId: personId
            },
            success: function (response) {
                var data = Ext.decode(response.responseText);
                if (data.success) {
                    Tools.UserRequestToken.hasActiveToken = data.hasActiveToken;
                    view.getPositionRadioGroup(objRadio, data.validTokenTypes, mode);
                } else {
                    Tools.errorMessageServer(obj.messageInfo)
                }
            },
            failure: function (resp) {
                Tools.errorFailure();
            }
        });
    },


    ////end userRequestTokenContorller

    PermissionUserEditInfo: function (valueForm) {
        if ((Tools.trim(Tools.getValue(valueForm, EmsObjectName.userForm.userRole)) === ""))//&& (Tools.trim(Tools.getValue(valueForm,EmsObjectName.userForm.userAccess)) === ""))
        {
            Ext.Msg.show({
                title: 'هشدار',
                msg: 'لطفا نقش کاربر را مشخص کنید',
                width: 300,
                buttons: Ext.Msg.OK,
                icon: Ext.Msg.ERROR
            });
            return false;
        }

        if (Tools.getValue(valueForm, EmsObjectName.userForm.password) !== (Tools.getValue(valueForm, EmsObjectName.userForm.repeatPassword))) {
            Ext.Msg.show({
                title: 'هشدار',
                msg: 'مقدار گذرواژه و مقدار تکرار گذرواژه مساوی نیست',
                width: 300,
                buttons: Ext.Msg.OK,
                icon: Ext.Msg.ERROR
            });


            return false;
        }

        if (Tools.getValue(valueForm, EmsObjectName.userForm.departmentId) === "") {

            Ext.getCmp(EmsObjectName.userForm.departmentId).allowBlank = false;

            // Ext.getCmp('idUserNewEditForm').getForm().reset();
            Ext.Msg.show({
                title: 'هشدار',
                msg: 'جایگاه سازمانی مناسب برای شخص مورد نظر تعریف نشده است',
                width: 300,
                buttons: Ext.Msg.OK,
                icon: Ext.Msg.ERROR
            });


            return false;
        }

        /*        var certificateSerial = parseInt(Tools.trim(Tools.getValue(valueForm,EmsObjectName.userForm.certificateSerial)),null),
         minNumber = 100001 ,
         maxNumber = 999999 ;

         if(!((minNumber<certificateSerial) && (certificateSerial<maxNumber))){
         Ext.Msg.show({
         title: 'هشدار',
         msg: 'شماره سریال شناسنامه باید عددی بین'+minNumber+'و'+maxNumber+'باشد',
         width: 300,
         buttons: Ext.Msg.OK,
         icon: Ext.Msg.ERROR
         });
         return false;
         }*/

        return true;
    },

    leaveCursorDepartment: function () {


    },

    onTokenManagementRadioChange: function (radioGroup, value) {

        var userRequestTokenForm = this.getUserRequestTokenForm(),
            userRequestToken = this.getUserRequestToken(),
            userRequestIssuanceCombo = userRequestTokenForm.down('#userRequestIssuanceReason'),
            userRequestTokenMap = EmsObjectName.userRequestTokenMap,
            tokenType = value['tokenType'];

        userRequestIssuanceCombo.store.clearFilter();

        if (userRequestToken.store.find('tokenType', userRequestTokenMap[tokenType], 0, false, false, true) == -1) {
            userRequestIssuanceCombo.store.filterBy(function (record) {
                var code = record.get('code');
                return code == 'N' ? true : false;
            });

        } else {
            userRequestIssuanceCombo.store.filterBy(function (record) {
                var code = record.get('code');
                return (code == 'D' || code == 'R') ? true : false;
            });
        }

    }

});


