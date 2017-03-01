/**
 * User: E.Z.Moghaddam
 * Date: 05/08/13
 * Time: 18:50 PM
 * This autocomplete component would be used to display all departments which are accessible by a person
 */
Ext.define('Ems.view.office.PersonDepartmentsCombo', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.personDepartmentsCombo',

    requires: [
        'Ems.store.PersonDepartmentsStore',
        'Ems.model.AutocompleteSimpleModel'
    ],

    store: {type: 'persondepartmentautocompletestore'},

    multiSelect: false,

    hiddenName: EmsObjectName.officeNewEdit.oficSuperRegisterOfficeId,

    displayField: 'acName',
    valueField: 'acId',

    listWidth: 290,

    emptyText: 'انتخاب کنید...'

});
