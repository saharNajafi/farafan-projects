/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/9/12
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.department.locationAutocomplete', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.locationautocomplete',

    requires: [
        'Ems.model.AutocompleteSimpleModel',
        'Ems.store.locationAutocompleteStore'
    ],

    initComponent: function () {
        this.store = {
            type: 'locationautocompletestore',
            storeId: 'eofLocationStore'
        };
        this.callParent(arguments);
    },

    multiSelect: false,

    emptyText: 'انتخاب کنید...',

    displayField: 'acName',
    valueField: 'acId',

    hiddenName: EmsObjectName.Department.locationId
});

