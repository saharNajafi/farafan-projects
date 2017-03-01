/**
 * Created by moghaddam on 6/22/14.
 *
 * The fieldset object containing information of the citizen parents (like name, NID, etc.)
 */
Ext.define('Ems.view.cardRequestList.fieldset.CitizenParentsInfo', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.cardrequestlistfieldsetcitizenparentsinfo',
    name: "citizenParentsInfo",

    id: 'citizenParentsInfoFieldSetID',

    title: 'اطلاعات والدین',

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
                fieldLabel: 'نام پدر',
                name: EmsObjectName.cardRequestList.fatherName
            },
            {
                fieldLabel: 'شماره ملی پدر',
                name: EmsObjectName.cardRequestList.fatherNID
            },
            {
                fieldLabel: 'شماره شناسنامه پدر',
                name: EmsObjectName.cardRequestList.fatherBirthCertID
            },
            {
                fieldLabel: 'نام مادر',
                name: EmsObjectName.cardRequestList.motherName
            },
            {
                fieldLabel: 'شماره ملی مادر',
                name: EmsObjectName.cardRequestList.motherNID
            },
            {
                fieldLabel: 'شماره شناسنامه مادر',
                name: EmsObjectName.cardRequestList.motherBirthCertID
            }
        ]
    }
});
