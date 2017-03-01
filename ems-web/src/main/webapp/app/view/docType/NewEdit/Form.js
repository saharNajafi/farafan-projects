/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/14/12
 * Time: 5:18 PM
 * To change this template use File | Settings | File Templates.
 *
 *
 */
Ext.define('Ems.view.docType.NewEdit.Form', {
    extend: 'Ext.form.Panel',
    alias: 'widget.doctypeneweditform',
    //width:590,
    border: false,
    bodyBorde: false,
    //
    id: 'idDocTypeNeweditForm',
    defaults: {
        margin: 10
    },
    initComponent: function () {
        this.items = this.OnItemsDocTypeForm();
        this.callParent(arguments);
    },
    OnItemsDocTypeForm: function () {
        return(
            [
                {
                    xtype: 'textfield',
                    fieldLabel: "نوع سند",
                    id: 'idDocTypeField',
                    name: EmsObjectName.docType.name,
                    anchor: '-10',
                    regex: Tools.regexFarsiAlphaAndNumber(),
                    regexText: 'لطفا از حروف فارسی و اعداد استفاده کنید',
                    allowBlank: false,
                    maxLength: 255,
                    enforceMaxLength: 255
                },
                {
                    xtype: 'checkboxgroup',
                    fieldLabel: 'سرویس',
                    allowBlank: false,
                    id: 'idDoctypecheckService',
                    name: EmsObjectName.docType.service,
                    columns: 1,
                    vertical: true,
                    items: [
                        { boxLabel: 'ثبت نام جدید', name: 'rb', inputValue: '1' },
                        { boxLabel: 'تمدید', name: 'rb', inputValue: '2' },
                        { boxLabel: 'المثنی', name: 'rb', inputValue: '3' },
                        { boxLabel: 'صدور مجدد به دلیل خرابی', name: 'rb', inputValue: '4' },
                        { boxLabel: 'صدور مجدد به دلیل تغییرات هویتی', name: 'rb', inputValue: '5' }
                    ]
                }
            ]
            );
    }

});
