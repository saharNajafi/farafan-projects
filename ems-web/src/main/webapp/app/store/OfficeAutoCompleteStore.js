/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/14/12
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.OfficeAutoCompleteStore', {
    extend: 'Gam.data.store.Autocomplete',
    alias: 'store.officeautocompletestore',

    require: [ 'Ems.model.AutocompleteSimpleModel' ],
    model: 'Ems.model.AutocompleteSimpleModel',

    autocompleteName: 'office'

});

