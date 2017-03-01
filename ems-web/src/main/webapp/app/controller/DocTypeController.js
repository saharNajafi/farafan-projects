/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/3/12
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.controller.DocTypeController', {
    extend: 'Gam.app.controller.RowEditorBasedGrid',

    ns: 'extJsController/doctype',


    statics: {
        statefulComponents: [
            'wDocTypeGrid',
            'wDocTypeGridEdit',
            'wDocTypeGridAdd',
            'wDocTypeGridDelete'
        ]
    },

    views: [
        'docType.Grid'
    ],

    requires: [
        'Ems.view.docType.Grid',
        'Ems.view.docType.NewEdit.Form'
    ],

    refs: [
        {
            ref: 'doctypegrid',
            selector: 'doctypegrid'
        },
        {
            ref: 'docTypeNewEditForm',
            selector: 'doctypeneweditform'
        }
    ],

    initViewType: 'doctypegrid',

    constructor: function (config) {
        this.containerNewDocTypeEditWindow = 0;
        this.containerEditDocTypeEditWindow = 0;
        this.docTypeId = 0;
        this.callParent(arguments);
        this.init();
    },
    init: function () {
        this.control({
            "#btnOfficeCreate": {
                click: function (btn) {
                    if (Tools.docType.containerNewWindow === 0) {
                        var win = Ext.create('Ems.view.docType.NewEdit.newWindow').show();
                        Tools.MaskUnMask(win);
                        Tools.docType.containerNewWindow++;
                    }

                }
            },

            "#btnOfficeEdit": {
                click: function (btn) {

                    if (Tools.docType.containerEditWindow === 0) {

                        var win = Ext.create('Ems.view.docType.NewEdit.editWindow');

                        this.LoadFromDocType();
                        if (win) {
                            win.show();
                            Tools.MaskUnMask(win);
                        }
                        Tools.docType.containerEditWindow++;
                    }
                }
            },

            "#btnEidtDocTypeEditWindow": {
                click: function (btn) {
                    if (this.containerEditDocTypeEditWindow % 2 === 0) {

                        var form = this.getDocTypeNewEditForm();
                        if (!form.getForm().isValid()) {
                            return false;
                        }

                        var ids = this.docTypeId;
                        if (ids > 0) {
                            var serviceIds = this.getValueCheckBox(Ext.getCmp('idDoctypecheckService').getValue().rb),
                                name = Ext.getCmp('idDocTypeField').getValue(),
                                win = btn.up('window');
                            this.RegisterNewEditDocType(win, serviceIds, name, ids);
                        } else {
                            Tools.errorMessageClient("سطری انتخاب نشده");
                        }
                    }
                    this.containerEditDocTypeEditWindow++;
                }
            },
            "#btnNewDocTypeEditWindow": {
                click: function (btn) {
                    if (this.containerNewDocTypeEditWindow % 2 === 0) {
                        var form = this.getDocTypeNewEditForm();
                        if (!form.getForm().isValid()) {
                            return false;
                        }

                        var serviceIds = this.getValueCheckBox(Ext.getCmp('idDoctypecheckService').getValue().rb),
                            name = Ext.getCmp('idDocTypeField').getValue(),
                            win = btn.up('window');
                        this.RegisterNewEditDocType(win, serviceIds, name, null);
                    }
                    this.containerNewDocTypeEditWindow++;
                }
            }
        });
        this.callParent(arguments);
    },

    doEditing: function (grid, rowIndex) {
        var store = grid.getStore(),
            record = store.getAt(rowIndex),
            id = record.get('id'),
            services = record.get('services'),
            name = record.get('name'),
            me = this;
        this.docTypeId = id;

        var win = Ext.create('Ems.view.docType.NewEdit.editWindow');

        this.LoadFromDocType(services, name);
        if (win) {
            win.show();
            Tools.MaskUnMask(win);
        }


    },

    LoadFromDocType: function (service, name) {
        try {
            var checkgroup = Ext.getCmp('idDoctypecheckService'),
                arrService = service.split(",");

            checkgroup.setValue({rb: arrService});
            Ext.getCmp('idDocTypeField').setValue(name);
            return true;

        } catch (e) {
            return false;
        }

    },
    getValueCheckBox: function (checkBox) {
        var i = 0;
        var result = "";
        if (checkBox != null) {
            while (i < checkBox.length) {
                if (result === "") {
                    if (checkBox.length == 1) {
                        result = checkBox;
                        return result;
                    }
                    result = result + checkBox[i];
                } else {
                    result = result + ',' + checkBox[i];
                }
                i++;
            }
        }
        return result;
    },
    RegisterNewEditDocType: function (win, serviceIds, name, ids) {
        var me = this,
            grid = this.getDoctypegrid();

//        if((serviceIds==="") || (name==="")){
//            return false;
//        }

//        if(serviceIds===""){
//            Tools.errorMessageClient("لطفا سرویس را مشخص کنید");
//            return false;
//        }

        Gam.Msg.showWaitMsg();

        Ext.Ajax.request({
            url: me.ns + '/save',
            jsonData: {
                records: [
                    {
                        services: serviceIds,
                        docTypeName: name,
                        id: ids
                    }
                ]
            },
            success: function (response) {
                {
                    Gam.Msg.hideWaitMsg();
                    try {
                        var obj = Ext.decode(response.responseText);
                        if (obj.success) {
                            grid.getStore().load();
                            win.close();
                        }
                        else {
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


    }

});


