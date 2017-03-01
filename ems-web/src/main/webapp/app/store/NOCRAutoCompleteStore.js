/**
 * Created with Eclipse :D.
 * User: Dr Adldoost :D
 * Date: 19/10/15
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.NOCRAutoCompleteStore', {
    extend: 'Gam.data.store.Autocomplete',
    alias: 'store.nocrautocompletestore',

    require: [ 'Ems.model.AutocompleteSimpleModel' ],
    model: 'Ems.model.AutocompleteSimpleModel',

    autocompleteName: 'nocr'

});

