/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/9/12
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.user.NewEdit.Form', {
    extend: 'Ext.form.Panel', alias: 'widget.userneweditform', requires: [
        , 'Ems.view.user.NewEdit.PersonInfo'
        , 'Ems.view.user.NewEdit.SystemInfo'
        , 'Ems.view.user.NewEdit.OrganizationInfo'
    ]
    /*,layout:'column' */, id: 'idUserNewEditForm'
    //, msgTarget: 'under'
    , bodyBorder: false, border: false
    //, autoScroll:true
    , initComponent: function () {

        this.items = [
            {
                xtype: 'hiddenfield',
                allowBlank: true,
                id: EmsObjectName.userForm.userRole,
                name: EmsObjectName.userForm.userRole
            },
            {
                xtype: 'hiddenfield',
                allowBlank: true,
                id: EmsObjectName.userForm.userAccess,
                name: EmsObjectName.userForm.userAccess
            }
            ,
            {xtype: 'userneweditpersonInfo'}
            ,
            {xtype: 'userneweditsysteminfo'}
            //,{xtype:'organizationfieldset'}
            ,
            this.OnOrganizationFrmNewEdit()

        ];
        this.callParent(arguments);
    }

    ////////////////////////////////////////////////
    , OnOrganizationFrmNewEdit: function () {
        return ({
            xtype: 'fieldset', title: 'اطلاعات سازمانی', id: 'jaigaheSazmani', items: [
                {
                    labelWidth: 110,
                    width: 370,
                    xtype: 'usercmborganizationalstatus',
                    id: EmsObjectName.userForm.departmentId,
                    name: EmsObjectName.userForm.departmentId,
                    fieldLabel: 'جایگاه سازمانی',
                    allowBlank: true,
                    hiddenName: EmsObjectName.userForm.departmentId,
                    listeners: {
                        specialkey: function (field, e) {
                            if (e.getKey() == e.TAB) {
                                if (field.value === "" || field.lastValue === "") {
                                    field.allowBlank = false;
                                }
                            }
                        }
                    }


                },
                {
                    layout: 'column',
                    xtype: 'panel',
                    id: 'gridContainer',
                    border: false,
                    bodyBorder: false,
                    items: [
                        this.OnOrganizationFrmNewEdit_c1(),
                        this.OnOrganizationFrmNewEdit_c2()
                    ]
                }
            ]
        });
    }, OnOrganizationFrmNewEdit_c1: function () {
        return({
            xtype: 'container',
            id: 'iduserRolesFrom',
            columnWidth: .5,
            border: false,
            bodyBorder: false,
            defaults: {
                anchor: '-2'
            },
            items: [
                {
                    xtype: 'userRoles',
                    width: 360
                }
            ]

        });
    }, OnOrganizationFrmNewEdit_c2: function () {
        return({
            xtype: 'container',
            columnWidth: .5,
            border: false,
            bodyBorder: false,
            defaults: {
                anchor: '-2'
            }, items: [
                {
                    xtype: 'useraccessaccess',
                    width: 360
                }
            ]
        });
    }
});
