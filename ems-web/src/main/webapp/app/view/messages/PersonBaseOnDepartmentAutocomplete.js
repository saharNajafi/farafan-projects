/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/4/12
 * Time: 11:20 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.messages.PersonBaseOnDepartmentAutocomplete', {
    extend: 'Gam.form.field.Autocomplete',

    alias: 'widget.personbaseondepartmentautocomplete',

    requires: [
        'Ems.model.AutocompleteSimpleModel',
        'Ems.store.PersonBaseOnDepartmentsStore'
    ],

    store: {type: 'personbaseondepartmentsstore'},

    emptyText: 'انتخاب کنید...',
    multiSelect: false,
    displayField: 'acName',
    valueField: 'acId',
    setValue: Ext.emptyFn,
    allowBlank: true,
    hiddenName: EmsObjectName.messages.personId
});
