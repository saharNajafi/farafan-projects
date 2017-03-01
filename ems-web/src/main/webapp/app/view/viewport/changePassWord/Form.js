/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/24/12
 * Time: 12:31 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.viewport.changePassWord.Form', {
    extend: 'Ext.form.Panel', alias: 'widget.changepasswordform'

    /*,layout:'column' */, id: 'idChangePassWordForm'
    //, msgTarget: 'under'
    , bodyBorder: false, border: false
    //, autoScroll:true
    , defaults: {
        xtype: 'textfield',
        labelWidth: 100,
        anchor: '-10',
        allowBlank: true
    }, initComponent: function () {

        this.items = [
            {xtype: 'component', html: '<br/>'},
            {
                fieldLabel: 'گذرواژه قبلی',
                inputType: 'password',
                id: EmsObjectName.defineUserAndDepartment.oldPassword,
                name: EmsObjectName.defineUserAndDepartment.oldPassword,
                regex: Tools.regexPassword(),
                regexText: "لطفا از حروف لاتین و اعداد و برخی کاراکترهای خاص استفاده کنید",
                maxLength: 20,
                allowBlank: false,
                listeners: {
                    'render': function (c) {
                        Tools.setElementDir(c, 'ltr');
                    }
                }
            },
            {
                fieldLabel: 'گذر وازه جدید',
                inputType: 'password',
                id: EmsObjectName.defineUserAndDepartment.newPassword,
                name: EmsObjectName.defineUserAndDepartment.newPassword,
                regex: Tools.regexPassword(),
                regexText: "لطفا از حروف لاتین و اعداد و برخی کاراکترهای خاص استفاده کنید",
                maxLength: 20,
                allowBlank: false,
                listeners: {
                    change: function (th) {
                        var confirmField = th.up('form').down('[id=' + EmsObjectName.defineUserAndDepartment.confirmNewPassword + ']');
                        confirmField.validate();
                    },
                    'render': function (c) {
                        Tools.setElementDir(c, 'ltr');
                    }
                }

            } ,
            {
                fieldLabel: 'تکرار گذرواژه جدید',
                inputType: 'password',
                id: EmsObjectName.defineUserAndDepartment.confirmNewPassword,
                name: EmsObjectName.defineUserAndDepartment.confirmNewPassword,
                allowBlank: false,
                initialPassField: EmsObjectName.defineUserAndDepartment.newPassword, listeners: {
                'render': function (c) {
                    Tools.setElementDir(c, 'ltr');
                }
            }

            },
            {xtype: 'component', html: '<br/>'}

        ];
        this.callParent(arguments);
    }

});
