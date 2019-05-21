/**
 * Created by moghaddam on 6/17/14.
 *
 * The fieldset object containing personal information of the citizen (like name, family, religion, etc.)
 */
Ext.define('Ems.view.cardRequestList.fieldset.CitizenInfo', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.cardrequestlistfieldsetcitizeninfo',
    name: "citizenInfo",

    id: 'citizenInfoFieldSetID',

    title: 'رسید متقاضی',

    layout: 'column',

    initComponent: function () {
        this.defaults = {
            columnWidth: 1 / 3,
            labelWidth: 150
        };

        this.callParent(arguments);
    },

    getReadOnlyFields: function () {
        return this.getItems();
    },

    getEditableFields: function () {
        return this.getItems();
    },

    getItems: function () {
        return[
            {
                fieldLabel: 'نام',
                name: EmsObjectName.trackingCodePrintList.citizenFirstName
            },
            {
                fieldLabel: 'نام خانوادگی',
                name: EmsObjectName.trackingCodePrintList.citizenSurname
            },
            {
                fieldLabel: 'نام پدر',
                name: EmsObjectName.trackingCodePrintList.fatherName
            },
            {
                fieldLabel: 'شماره ملی',
                name: EmsObjectName.trackingCodePrintList.citizenNId
            },
            {
                fieldLabel: 'شماره شناسنامه',
                name: EmsObjectName.trackingCodePrintList.birthCertId
            },
            {
                fieldLabel: 'تاریخ تولد',
                name: EmsObjectName.trackingCodePrintList.citizenBirthDate
            },

            {
                fieldLabel: 'تاریخ مراجعه',
                name: EmsObjectName.trackingCodePrintList.rsvDate,
                renderer: Gam.util.Format.dateRenderer('Y/m/d')
            },
            {
                fieldLabel: 'کد رهگیری',
                name: EmsObjectName.trackingCodePrintList.trackingId
            },
            {
                fieldLabel: 'تاریخ چاپ رسید',
                name: EmsObjectName.trackingCodePrintList.datePrinted
            } ,
            {
                fieldLabel: 'نام کاربر',
                name: EmsObjectName.trackingCodePrintList.userFirstName
            },
            {
                fieldLabel: 'نام خانوادگی کاربر',
                name: EmsObjectName.trackingCodePrintList.userLastName
            },
            {
                fieldLabel: 'امضای متصدی',
                name: EmsObjectName.trackingCodePrintList.userSign
            }
        ]
    }
});
