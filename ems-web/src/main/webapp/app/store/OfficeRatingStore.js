/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/1/12
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.OfficeRatingStore', {

    extend: 'Gam.data.store.Autocomplete',
    alias: 'store.officeratingstore',

    require: ['Ems.model.AutocompleteSimpleModel'],
    model: 'Ems.model.AutocompleteSimpleModel',

    autocompleteName: 'rating'
});

