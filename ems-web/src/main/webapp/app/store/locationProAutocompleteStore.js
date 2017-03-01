/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/4/12
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.store.locationProAutocompleteStore', {
    extend: 'Gam.data.store.Autocomplete',
    alias: 'store.locationproautocompletestore',

    require: ['Ems.model.AutocompleteSimpleModel'],

    model: 'Ems.model.AutocompleteSimpleModel',

    autocompleteName: 'locationPro'
});

