/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/24/12
 * Time: 5:00 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.user.NewEdit.OrganizationInfo', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.userneweditorganizationinfo',

    requires: [
        'Ems.view.user.role.roles',
        'Ems.view.user.access.access',
        'Ems.view.user.NewEdit.cmbOrganizationalStatus'
    ],

    id: 'iduserneweditorganizationinfo',

    title: 'اطلاعات سازمانی',

    contentStyle: null,

    //margin: 10,
    layout: 'column',
    initComponent: function () {

        var me = this;

        me.defaults = {
            columnWidth: 2
        };

        me.callParent(arguments);
    },
//    getReadOnlyFields:function(){
//        return this.getItems();
//    },
    getEditableFields: function () {
        return this.getItems();
    }, getItems: function () {
        return[
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
            },
            {
                labelWidth: 110,
                width: 370,
                xtype: 'usercmborganizationalstatus',
                id: EmsObjectName.userForm.departmentName,
                name: EmsObjectName.userForm.departmentName,
                fieldLabel: 'جایگاه سازمانی',
                allowBlank: false,
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
                    this.getOrganizationFrmNewEdit_c1(),
                    this.getOrganizationFrmNewEdit_c2()
                ]
            }
        ];
    }, getOrganizationFrmNewEdit_c1: function () {
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
                    width: 375
                }
            ]

        });
    }, getOrganizationFrmNewEdit_c2: function () {
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
                    width: 375
                }
            ]
        });
    }, onRolesClear: function () {

    }, onPermissionsClear: function () {

    }
});

