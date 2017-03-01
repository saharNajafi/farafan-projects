/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/4/12
 * Time: 11:20 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.department.parentDNAutocomplete', {
    extend: 'Gam.form.field.Autocomplete',

    alias: 'widget.patentdnautocomplete',

    requires: [
        'Ems.model.AutocompleteSimpleModel',
        'Ems.store.parentDNAutocompleteStore'
    ],

    store: {type: 'parentdnautocompletestore'},

    emptyText: 'انتخاب کنید...',
    multiSelect: false,
    displayField: 'acName',
    valueField: 'acId',
    allowBlank: true,
    hiddenName: EmsObjectName.Department.parentDNId
});
