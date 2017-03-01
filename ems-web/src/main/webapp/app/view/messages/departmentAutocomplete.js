/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/4/12
 * Time: 11:20 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.messages.departmentAutocomplete', {
    extend: 'Gam.form.field.Autocomplete',

    alias: 'widget.departmentAutocomplete',

    requires: [
        'Ems.model.AutocompleteSimpleModel',
        'Ems.store.DepartmentAutocompleteStore'
    ],

    store: {type: 'departmentautocompletestore'},

    emptyText: 'انتخاب کنید...',
    multiSelect: true,
    setValue: Ext.emptyFn,
    displayField: 'acName',
    valueField: 'acId',
    allowBlank: true,
    hiddenName: EmsObjectName.messages.departmentId
});
