/**
 * User: E.Z.Moghaddam
 * Date: 05/08/13
 * Time: 18:50 PM
 * This store would be used by corresponding autocomplete component to display all departments which are accessible by
 * a person
 */
Ext.define('Ems.store.PersonDepartmentsStore', {
    extend: 'Gam.data.store.Autocomplete',
    alias: 'store.persondepartmentautocompletestore',

    require: ['Ems.model.AutocompleteSimpleModel'],
    model: 'Ems.model.AutocompleteSimpleModel',

    autocompleteName: 'personDepartment'

});
