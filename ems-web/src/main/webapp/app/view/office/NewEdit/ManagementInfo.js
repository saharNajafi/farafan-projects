/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/13/12
 * Time: 2:27 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.office.NewEdit.ManagementInfo', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.officeneweditmanagement',

    id: 'idOfficeNewEditManagement',

    title: 'اطلاعات مدیر ',

    //margin: 10,
    layout: 'column',
    initComponent: function () {

        var me = this;

        me.defaults = {
            columnWidth: 1 / 2,
            labelWidth: 150
        };

        me.callParent(arguments);
    },
    getReadOnlyFields: function () {
        return[
            {
                fieldLabel: 'نام مدیر',
                id: EmsObjectName.officeNewEdit.mangName
            },
            {
                fieldLabel: 'تلفن همراه',
                id: EmsObjectName.officeNewEdit.mangMobil

            },
            {
                fieldLabel: 'تلفن ثابت و کدپیش شماره',
                id: EmsObjectName.officeNewEdit.mangTel
            }
        ];
    },
    getEditableFields: function () {
        return this.getItemsManagementInfo();
    }, getItemsManagementInfo: function () {
        return[
            {
                xtype: 'cmbmanagementautocomplete',
                fieldLabel: 'نام مدیر',
                id: EmsObjectName.officeNewEdit.mangName,
                name: EmsObjectName.officeNewEdit.mangName
            },
            {
                fieldLabel: 'تلفن همراه',
                allowBlank: false,
                id: EmsObjectName.officeNewEdit.mangMobil,
                name: EmsObjectName.officeNewEdit.mangMobil,
                regex: Tools.regexCellPhone(),
                regexText: 'این فیلد باید یکx شماره 11 رقمی در قالب 09xxxxxxxxx باشد',
                maxLength: 11,
                maskRe: /\d/i,
                enforceMaxLength: 11
            },
            {
                fieldLabel: 'تلفن ثابت و کد پیش شماره',
                allowBlank: false,
                id: EmsObjectName.officeNewEdit.mangTel,
                name: EmsObjectName.officeNewEdit.mangTel,
                maskRe: /\d/i,
                maxLength: 20,
                enforceMaxLength: 20

            }
        ];
    },

    onPersonSelect: function() {

    },

    onPersonClear: function () {

    }
});
