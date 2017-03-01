/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/1/12
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.OfficeSuperRegisterOfficeStore', {
    extend: 'Gam.data.store.Autocomplete',
    alias: 'store.officesuperregisterofficesotre',

    require: ['Ems.model.AutocompleteSimpleModel'],
    model: 'Ems.model.AutocompleteSimpleModel',

    autocompleteName: 'nonEnrollmentDepartment'
});
