/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/24/12
 * Time: 6:15 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.user.NewEdit.SystemInfo', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.userneweditsysteminfo',

    id: 'iduserneweditsysteminfo',

    title: 'اطلاعات سیستمی',

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
                fieldLabel: 'نام کاربری', xtype: 'textfield', id: EmsObjectName.userForm.userName, name: EmsObjectName.userForm.userName, regex: /^[a-zA-Z0-9]+$/, maxLength: 20, allowBlank: false, regexText: "لطفا از حروف لاتین و اعداد استفاده کنید"
                // ,regexText:'فیلد باید از حروف لاتین استفاده شود'+'<br/>'+'حداکثر طول قیلد تا 20 کاراکتر می تواند باشد'
                , bodyStyle: this.contentStyle(), style: this.contentStyle(), listeners: {
                'render': function (c) {
                    Tools.setElementDir(c, 'ltr');
                }
            }

            },
            {
                fieldLabel: 'گذرواژه', xtype: 'textfield', inputType: 'password', id: EmsObjectName.userForm.password, name: EmsObjectName.userForm.password, regex: Tools.regexPassword(), regexText: "لطفا از حروف لاتین و اعداد و برخی کاراکترهای خاص استفاده کنید", maxLength: 20, bodyStyle: this.contentStyle(), style: this.contentStyle(), listeners: {
                'render': function (c) {
                    Tools.setElementDir(c, 'ltr');
                }
            }

            },
            {
                // style:'margin-top:25px'
                fieldLabel: 'تکرار گذرواژه', xtype: 'textfield', inputType: 'password', id: EmsObjectName.userForm.repeatPassword, name: EmsObjectName.userForm.repeatPassword, regex: Tools.regexPassword(), regexText: "لطفا از کلمات لاتین و اعداد و برخی از کاراکتر های خاص استفاده کنید", bodyStyle: this.contentStyle(), style: this.contentStyle(), listeners: {
                'render': function (c) {
                    Tools.setElementDir(c, 'ltr');
                }
            }

            }
        ]
    }
});
