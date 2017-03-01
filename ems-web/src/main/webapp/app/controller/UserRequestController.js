/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/18/12
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */





Ext.define('Ems.controller.UserRequestController', {
    extend: 'Gam.app.controller.LocalDialogBasedGrid',

    ns: 'extJsController/userRequest',

    views: [
        'userRequest.Grid',
        'user.access.MultiSelect',
        'user.role.MultiSelect',
        'user.NewEdit.Form',
        'user.view.Windows',
        'user.NewEdit.Windows'

    ],

    statics: {
        statefulComponents: [
            'wUserRequestGrid' ,
            'wUserRequestGridNotAccept ' ,
            'wUserRequestGridView' ,
            'wUserRequestGridEdit' ,
            'wUserRequestGridDelete'
        ]
    },

    stores: [
        'UserStore',
        'userAccessStore',
        'userRoleStore',
        'userAccessMultiSelectStore',
        'userOrganizationalStatusStore'
    ],

    refs: [
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
            ref: 'userRequestGrid',
            selector: 'userrequestgrid'
        },
        {
            ref: 'newEditWindows',
            selector: 'neweditwindows'
        }


    ],

    constructor: function (config) {
        this.me = this;
        this.personId = null;
        this.lastValue = null;
        // this.userActive=null;
        this.callParent(arguments);
    },

    init: function () {


        var me = this;

        this.control({

            "[action=butUserAccessChoice]": {  'click': this.getUserAccessChoice, scope: this     },

            "button[itemId=btnRoleRemove]": { "click": this.getBtnRoleRemove, scope: this    },

            "button[itemId=btnRemoveAccess]": {  "click": this.getBtnRemoveAccess, scope: this    },

            "[action=butUserRolesChoice]": {  'click': this.getUserRolesChoice, scope: this    },

            "[action=btnNewEditUserRequest]": { 'click': this.getSaveFromEditUserRequest, scope: this },


            "#roleAutocomplet": {
                select: function (autocomplete, record, value) {

                    var store = Ext.getCmp('roleMultiSelectds').getStore(),
                        autoModel = {  value: null, text: null    },
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
                        autoModel = {  id: null, name: null    },
                        i;

                    for (i = 0; i < record.length; i++) {

                        var reapetData = Tools.checkDataInStore(store, 'id', record[i].get('acId'));
                        if (reapetData == false) {
                            autoModel.id = record[i].get('acId');
                            autoModel.name = record[i].get('acName');

                            store.add(autoModel);
                            this.setAccesses(autoModel.id);
                            autoModel.name = null;
                            autoModel.id = null;
                        }
                    }
                }
            }

        });

        this.callParent(arguments);

    },
    getSaveFromEditUserRequest: function (btn) {


        var form = this.getUserNewEditForm();

        if (!form.getForm().isValid()) {
            return false;
        }
        ;

        var valueForm = form.getValues();
        var departmen = this.getUsercmborganizationalstatus();

        var store = Ext.create('Ems.store.UserStore', {baseUrl: this.ns});
        store.add(valueForm);
        var dataForm = store.getAt(0);
        var grid = this.getUserRequestGrid();
        var me = this;
        if (departmen.value === null)
            departmen.value = this.lastValue;

        dataForm.set(EmsObjectName.userForm.departmentId, departmen.value);
        dataForm.set(EmsObjectName.userForm.departmentName, departmen.rawValue);

        dataForm.set(EmsObjectName.userForm.id, this.personId);


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
                        //Tools.errorMessageServer(obj.messageInfo)
                    }
                },
                failure: function (resp) {
                    Gam.Msg.showWaitMsg();
                    Tools.errorFailure();
                }
            });
        }
    },

    doViewed: function (grid, rowIndex) {
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
    },

    doEditRequest: function (grid, rowIndex) {
        var store = grid.getStore(),
            record = store.getAt(rowIndex),
            id = record.get('id'),
            me = this;

        this.personId = id;
        //this.userActive=record.get(EmsObjectName.userForm.userActive);

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
                        Tools.errorMessageServer(data.messageInfo)
                    }
                }
            }, failure: function (resp) {
                Tools.errorFailure();
            }
        });
    },

    loadFormEditView: function (record, mode) {
        var win = null;

        if (mode === 'Edit') {
            win = Ext.create('Ems.view.userRequest.NewEdit.Windows');
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

    },

    setRoles: function (acId) {
        var objRoles = Ext.getCmp(EmsObjectName.userForm.userRole),
            value = Tools.trim(objRoles.getValue());

        if (value.length > 0) {
            value += ',' + acId;
        } else {
            value += acId;
        }

        objRoles.setValue(value);
    },

    setAccesses: function (acId) {

        var objAccesses = Ext.getCmp(EmsObjectName.userForm.userAccess),
            value = Tools.trim(objAccesses.getValue());

        if (value.length > 0) {
            value += ',' + acId;
        } else {
            value += acId;
        }

        objAccesses.setValue(value);
    },

    getBtnRemoveAccess: function () {
        var multiSelect = Ext.getCmp('accessMultiSelectds');
        this.BtnRemove(multiSelect);
    },

    getUserAccessChoice: function () {

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
    },

    getUserRolesChoice: function () {

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
    },

    BtnRemove: function (obj) {
        var selectCheckBox = obj.items.items[0].selModel.selected;
        var lengthRec = selectCheckBox.length;
        for (var i = 0; i < lengthRec; i++) {
            var records = selectCheckBox.items[0];
            obj.getStore().remove(records);
        }
        this.setValueHiddenFields(obj.id);
    },

    setValueHiddenFields: function (objName) {

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
    },

    getBtnRoleRemove: function () {
        var obj = Ext.getCmp('roleMultiSelectds');
        this.BtnRemove(obj);
    },


    getDoAction: function (grid, rowIndex, action) {


        //var grid=this.getUserRequestGrid();
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
                        }
                        else {
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


    doNotAccept: function (grid, rowIndex) {
        var store = grid.getStore(),
            record = store.getAt(rowIndex),
            id = record.get('id'),
            me = this;

        this.personId = id;
        //this.userActive=record.get(EmsObjectName.userForm.userActive);

        var name = record.get(EmsObjectName.userForm.firstName);
        var family = record.get(EmsObjectName.userForm.lastName);

        var msg = " آیا از عدم تایید  " + name + " " + family + " اطمینان دارید "

        var fn = function () {
            me.NotAcceptUser(store, id);
        }

        Tools.messageBoxConfirm(msg, fn);
    },

    NotAcceptUser: function (store, ids) {
        var me = this;
        Ext.Ajax.request({

            url: me.ns + '/rejectPerson',
            jsonData: {
                ids: ids
            }, success: function (resp) {
                var data = Ext.decode(resp.responseText);
                if (data.success) {
                    store.load();
                }
            }, failure: function (resp) {
                Tools.errorFailure();
            }
        });
    },


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

        if (!(Tools.trim(Tools.getValue(valueForm, EmsObjectName.userForm.password)) === (Tools.trim(Tools.getValue(valueForm, EmsObjectName.userForm.repeatPassword))))) {
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


        /*         var certificateSerial = parseInt(Tools.trim(Tools.getValue(valueForm, EmsObjectName.userForm.certificateSerial)), null),
         minNumber = 100001 ,
         maxNumber = 999999;

         if (!((minNumber < certificateSerial) && (certificateSerial < maxNumber))) {
         Ext.Msg.show({
         title:'هشدار',
         msg:'شماره سریال شناسنامه باید عددی بین' + minNumber + 'و' + maxNumber + 'باشد',
         width:300,
         buttons:Ext.Msg.OK,
         icon:Ext.Msg.ERROR
         });
         return false;
         }*/

        return true;
    }

});



