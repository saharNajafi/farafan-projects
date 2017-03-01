/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/24/12
 * Time: 5:00 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.user.NewEdit.PersonInfo', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.userneweditpersonInfo',

    id: 'iduserneweditpersonInfo',

    title: 'اطلاعات شخصی',

    contentStyle: function () {
        return(Tools.user.StyleObject);
    },

    //margin: 10,
    layout: 'column',
    initComponent: function () {

        var me = this;

        me.defaults = {
            columnWidth: 1 / 2
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
                fieldLabel: 'نام', allowBlank: false, id: EmsObjectName.userForm.firstName, name: EmsObjectName.userForm.firstName, regex: Tools.regexFarsiAlpha(), regexText: "از حروف الفبای فارسی استفاده کنید", maxLength: 32, bodyStyle: this.contentStyle(), style: this.contentStyle()
            },
            {
                fieldLabel: 'نام خانوادگی', allowBlank: false, id: EmsObjectName.userForm.lastName, name: EmsObjectName.userForm.lastName, regex: Tools.regexFarsiAlpha(), regexText: "از حروف الفبای فارسی استفاده کنید", maxLength: 32, bodyStyle: this.contentStyle(), style: this.contentStyle()
            },
            {
                fieldLabel: 'شماره ملی', allowBlank: false, id: EmsObjectName.userForm.nationalCode, name: EmsObjectName.userForm.nationalCode, maskRe: /\d/i, vtype: 'numeric'
                //,regexText: "کد ملی 10 رقمی را وارد کنید"
                , enforceMaxLength: true, maxLength: 10, minLength: 10, bodyStyle: this.contentStyle(), style: this.contentStyle(), listeners: {
                'render': function (c) {
                    Tools.setElementDir(c, 'ltr');
                }
            }
            },
            {
                fieldLabel: 'نام پدر', allowBlank: false, id: EmsObjectName.userForm.fatherName, name: EmsObjectName.userForm.fatherName, regex: Tools.regexFarsiAlpha(), regexText: "از حروف الفبای فارسی استفاده کنید", bodyStyle: this.contentStyle(), style: this.contentStyle(), maxLength: 32
            },
            {
                fieldLabel: 'شماره شناسنامه', allowBlank: false, id: EmsObjectName.userForm.certificateId, name: EmsObjectName.userForm.certificateId
                //,regex: /^\d{10}$/i
                , vtype: 'numeric', maxLength: 10, enforceMaxLength: true, maskRe: /\d/i, bodyStyle: this.contentStyle(), style: this.contentStyle(), regexText: "حداکثر طول عدد 10 رقم می باشد", listeners: {
                'render': function (c) {
                    Tools.setElementDir(c, 'ltr');
                }
            }
            },
            {
                fieldLabel: 'شماره سریال شناسنامه', allowBlank: false, id: EmsObjectName.userForm.certificateSerial, name: EmsObjectName.userForm.certificateSerial, vtype: 'serialCertificate', maxLength: 6, enforceMaxLength: true, maskRe: /\d/i, bodyStyle: this.contentStyle(), style: this.contentStyle()
                // ,regexText: "شماره سریال 6 رقمی را وارد کنید"
                , listeners: {
                'render': function (c) {
                    Tools.setElementDir(c, 'ltr');
                }
            }
            },
            {
                xtype: 'textfield',
                allowBlank: false,
                fieldLabel: 'پست الکترونیکی',
                id: EmsObjectName.userForm.email,
                name: EmsObjectName.userForm.email,
                vtype: 'email',
                bodyStyle: this.contentStyle(),
                style: this.contentStyle(),
                listeners: {
                    'render': function (c) {
                        Tools.setElementDir(c, 'ltr');
                    }
                }
            }
        ]
    }
});
