
Ext.define('Ems.view.cardRequestList.printRegistrationReceipt', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.printRegistrationReceipt',
    id: 'idprintRegistrationReceipt',

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

    getItems: function () {
        return[
            {
                fieldLabel: 'نام',
                name: EmsObjectName.cardRequestList.citizenFirstName
            },
            {
                fieldLabel: 'نام خانوادگی',
                name: EmsObjectName.cardRequestList.citizenSurname
            },
            {
                fieldLabel: 'نام پدر',
                name: EmsObjectName.cardRequestList.fatherName
            },
            {
                fieldLabel: 'شماره ملی',
                name: EmsObjectName.cardRequestList.citizenNId
            },
            {
                fieldLabel: 'شماره شناسنامه',
                name: EmsObjectName.cardRequestList.birthCertId
            },
            {
                fieldLabel: 'تاریخ تولد',
                name: EmsObjectName.cardRequestList.citizenBirthDate
            },

            {
                fieldLabel: 'تاریخ مراجعه',
                name: EmsObjectName.cardRequestList.reservationDate,
                renderer: Gam.util.Format.dateRenderer('Y/m/d')
            },
            {
                fieldLabel: 'کد رهگیری',
                name: EmsObjectName.cardRequestList.trackingId
            },
            {
                fieldLabel: 'تاریخ چاپ رسید',
                name: EmsObjectName.cardRequestList.receiptDate
            } ,
            {
                fieldLabel: 'نام کاربر',
                name: EmsObjectName.cardRequestList.userFirstName
            },
            {
                fieldLabel: 'نام خانوادگی کاربر',
                name: EmsObjectName.cardRequestList.userLastName
            },
            {
                fieldLabel: 'امضای متصدی',
                name: EmsObjectName.cardRequestList.userSign
            }
        ]
    }
});
