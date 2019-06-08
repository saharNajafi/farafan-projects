
Ext.define('Ems.view.cardRequestList.printRegistrationReceipt.printRegistrationReceiptFieldSet', {
    id: 'idprintregistrationreceipt',
    extend : 'ICT.form.FieldSet',
    alias: 'widget.printRegistrationReceiptFieldSet',

    contentStyle : function() {
        return (Tools.user.StyleObject);
    },
    layout: 'column',
    border:false,
    initComponent : function() {

        var me = this;
        me.defaults = {
            columnWidth : 1 / 3
        };
        me.callParent(arguments);
    },
    isReadOnly : function(){
        return true;
    },
    getReadOnlyFields: function () {
        return[
            {
                fieldLabel: 'نام',
                itemId: EmsObjectName.cardRequestList.citizenFirstName
            },
            {
                fieldLabel: 'نام خانوادگی',
                itemId: EmsObjectName.cardRequestList.citizenSurname
            },
            {
                fieldLabel: 'نام پدر',
                itemId: EmsObjectName.cardRequestList.fatherName
            },
            {
                fieldLabel: 'شماره ملی',
                itemId: EmsObjectName.cardRequestList.citizenNId
            },
            {
                fieldLabel: 'شماره شناسنامه',
                itemId: EmsObjectName.cardRequestList.birthCertId
            },
            {
                fieldLabel: 'تاریخ تولد',
                itemId: EmsObjectName.cardRequestList.citizenBirthDate
            },

            {
                fieldLabel: 'تاریخ مراجعه',
                itemId: EmsObjectName.cardRequestList.reservationDate,
                renderer: Gam.util.Format.dateRenderer('Y/m/d')
            },
            {
                fieldLabel: 'کد رهگیری',
                itemId: EmsObjectName.cardRequestList.trackingId
            },
            {
                fieldLabel: 'تاریخ چاپ رسید',
                itemId: EmsObjectName.cardRequestList.receiptDate,
                renderer: Gam.util.Format.dateRenderer('Y/m/d')
            } ,
            {
                fieldLabel: 'نام کاربر',
                itemId: EmsObjectName.cardRequestList.userFirstName
            },
            {
                fieldLabel: 'نام خانوادگی کاربر',
                itemId: EmsObjectName.cardRequestList.userLastName
            },
            {
                fieldLabel: 'امضای متصدی',
                itemId: EmsObjectName.cardRequestList.userSign
            },
            {
                action: 'btnNewEditUserRequest',
                id: 'idBtnNewEditUserRequest',
                text: 'پرینت',
                xtype: 'button',
                width: 70,
                iconCls: 'windows-Save-icon'/* ,
                     margins: '5 0 0 0'*/


                //style:'margin-right:10px; '
            }
        ]
    }
});
