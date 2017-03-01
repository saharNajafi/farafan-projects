/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/4/12
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.store.DepartmentAutocompleteStore', {
    extend: 'Gam.data.store.Autocomplete',
    alias: 'store.departmentautocompletestore',

    require: ['Ems.model.AutocompleteSimpleModel'],
    model: 'Ems.model.AutocompleteSimpleModel',

    //
    autocompleteName: 'enrollmentOfficeByProvince'
});
