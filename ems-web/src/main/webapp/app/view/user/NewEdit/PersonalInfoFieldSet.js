/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/4/12
 * Time: 7:25 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.user.NewEdit.PersonalInfoFieldSet', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.personalinfofieldset',

    requires: ['Ems.view.user.NewEdit.cmbOrganizationalStatus' ],

    layout: 'column',

    initComponent: function () {
        var me = this;

        me.defaults = {
            columnWidth: 1 / 2
        };

        me.callParent();
    },

    getReadOnlyFields: function () {
        return [];
    },

    getEditableFields: function () {
        return[
            {
                fieldLabel: 'نام', id: EmsObjectName.userForm.firstName, name: EmsObjectName.userForm.firstName
            },
            {
                fieldLabel: 'نام خانوادگی', id: EmsObjectName.userForm.lastName, name: EmsObjectName.userForm.lastName
            },
            {
                fieldLabel: 'شماره شناسنامه', id: EmsObjectName.userForm.certificateId, name: EmsObjectName.userForm.certificateId
            },
            {
                fieldLabel: 'شماره ملی', id: EmsObjectName.userForm.nationalCode, name: EmsObjectName.userForm.nationalCode
            },
            {
                fieldLabel: 'شماره سریال شناسنامه', id: EmsObjectName.userForm.certificateSerial, name: EmsObjectName.userForm.certificateSerial
            },
            {
                fieldLabel: 'نام پدر', id: EmsObjectName.userForm.fatherName, name: EmsObjectName.userForm.fatherName
            }
        ];

    },

    onReceiverSelect: function (autocomplete, record) {
        var fieldSet = autocomplete.up('fieldset');

        fieldSet.down('pathnumberautocomplete').setValue({ pathId: record.get('pathId'), pathNumber: record.get('pathNumber') });
        fieldSet.down('textareafield[name=receiverAddress]').setValue(record.get('address'));
    }

});
