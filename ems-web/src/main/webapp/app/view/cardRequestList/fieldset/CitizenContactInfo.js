/**
 * Created by moghaddam on 6/22/14.
 *
 * The fieldset object containing contact information of the citizen (like address, phone, etc.)
 */
Ext.define('Ems.view.cardRequestList.fieldset.CitizenContactInfo', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.cardrequestlistfieldsetcitizencontactinfo',
    name: "citizenContactInfo",

    id: 'citizenContactInfoFieldSetID',

    title: 'اطلاعات پستی و تماس',

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
                fieldLabel: 'کد پستی',
                name: EmsObjectName.cardRequestList.postalCode
            },
            {
                fieldLabel: 'استان',
                name: EmsObjectName.cardRequestList.livingProvinceName
            },
            {
                fieldLabel: 'شهر/روستا',
                name: EmsObjectName.cardRequestList.livingCityName
            },
            {
                fieldLabel: 'تلفن',
                name: EmsObjectName.cardRequestList.phone
            },
            {
                fieldLabel: 'تلفن همراه',
                name: EmsObjectName.cardRequestList.mobile
            },
            {
                columnWidth: 1,
                fieldLabel: 'آدرس',
                name: EmsObjectName.cardRequestList.address
            }
        ]
    }
});
