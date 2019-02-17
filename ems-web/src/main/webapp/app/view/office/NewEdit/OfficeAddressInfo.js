Ext.define('Ems.view.office.NewEdit.OfficeAddressInfo', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.officeneweditofficeaddressinfo',

    id: 'idOfficeNewEditOfficeAddressInfo',

    title: 'آدرس دفتر',

    margin: 10,
    layout: 'column',
    initComponent: function () {

        var me = this;

        me.defaults = {
            columnWidth: 1,
            labelWidth: 150
        };

        me.callParent(arguments);
    },
    getReadOnlyFields: function () {
        return [
            {
                fieldLabel: 'کد پستی',
                anchor: '-10',
                id: EmsObjectName.officeNewEdit.oficPostCode


            },
            {
                fieldLabel: 'آدرس کامل دفتر',
                anchor: '-10',
                id: EmsObjectName.officeNewEdit.oficAddress
            }
        ];
    },
    getEditableFields: function () {
        return [
            {
                xtype: 'textfield',
                allowBlank: false,
                fieldLabel: 'کد پستی',
                anchor: '-10', id: EmsObjectName.officeNewEdit.oficPostCode,
                name: EmsObjectName.officeNewEdit.oficPostCode,
                // regex: /^[0-9]+$/,
                maskRe: /\d/i,
                maxLength: 10,
                minLength: 10,
                enforceMaxLength: 5
                //,regexText: "کد پستی 10 رقمی را وارد کنید"


            },
            {
                xtype: 'textarea',
                fieldLabel: 'آدرس کامل دفتر',
                allowBlank: false,
                // anchor: '-10',
                id: EmsObjectName.officeNewEdit.oficAddress,
                name: EmsObjectName.officeNewEdit.oficAddress,
                maxLength: 200,
                enforceMaxLength: 200,
                regexText: 'طول رشته بیشتر از حد مجاز می باشد',
                enableKeyEvents:true,
                htmlSelected: false,
                listeners:{
                    keyup: function(field, e) {
                        if((field.maxLength || field.enforceMaxLength)
                           && e.keyCode == 8) {
                            var oldValue = field.getValue();
                            field.setValue(oldValue.slice(0,oldValue.length-1));
                            field.htmlSelected = false;
                        }
                        else if(e.keyCode == 65 && e.ctrlKey) {
                            field.inputEl.dom.select();
                            field.htmlSelected = true;
                        }
                        if(field.htmlSelected == true && e.keyCode == 46) {
                            field.setValue("");
                            field.htmlSelected = false;
                        }
                    }
                }
            }
        ];
    }
});
