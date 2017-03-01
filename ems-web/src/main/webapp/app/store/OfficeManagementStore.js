/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/1/12
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.OfficeManagementStore', {

    extend: 'Gam.data.store.Autocomplete',
    alias: 'store.officemanegementsotre',

    require: ['Ems.model.AutocompleteManagementModel'],
    model: 'Ems.model.AutocompleteManagementModel',

    autocompleteName: 'person'
});
