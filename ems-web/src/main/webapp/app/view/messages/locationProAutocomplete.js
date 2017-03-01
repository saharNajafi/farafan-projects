/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/9/12
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.messages.locationProAutocomplete', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.locationproautocomplete',

    requires: [
        'Ems.model.AutocompleteSimpleModel',
        'Ems.store.locationProAutocompleteStore'
    ],

    initComponent: function () {
        this.store = {
            type: 'locationproautocompletestore',
//            storeId: 'eofLocationStore'
        };
        this.callParent(arguments);
    },

    
//    store: {type: 'locationProAutocompleteStore'},

    multiSelect: true,
    id: 'locationproautocomplete',
    setValue: Ext.emptyFn,
    valueField: 'acId',
    fieldLabel: ' استان',
    emptyText: 'انتخاب کنید...',
    hiddenName:EmsObjectName.messages.provinceId,
});

