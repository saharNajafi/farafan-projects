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

    title: 'اطلاعات متقاضی',

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
                name: EmsObjectName.cardRequestList.citizenFirstName
            },
            {
                fieldLabel: 'نام خانوادگی',
                name: EmsObjectName.cardRequestList.citizenSurname
            },
            {
                fieldLabel: 'شماره ملی',
                name: EmsObjectName.cardRequestList.citizenNId
            },
            {
                fieldLabel: 'تاریخ تولد میلادی',
                name: EmsObjectName.cardRequestList.citizenBirthDate,
                renderer: Ext.util.Format.dateRenderer('Y/m/d')
            },
            {
                fieldLabel: 'تاریخ تولد شمسی',
                name: EmsObjectName.cardRequestList.citizenBirthDateSOL
            },
            {
                fieldLabel: 'تاریخ تولد قمری',
                name: EmsObjectName.cardRequestList.citizenBirthDateLUN
            },
            {
                fieldLabel: 'محل صدور شناسنامه',
                name: EmsObjectName.cardRequestList.birthCertiIssuancePlace
            },
            {
                fieldLabel: 'جنسیت',
                name: EmsObjectName.cardRequestList.gender,
                renderer: function(value){
                    if(value == 'M'){
                        return "مرد";
                    } else if (value == 'F'){
                        return "زن";
                    }
                }
            },
            {
                fieldLabel: 'دین',
                name: EmsObjectName.cardRequestList.religion
            }
        ]
    }
});
