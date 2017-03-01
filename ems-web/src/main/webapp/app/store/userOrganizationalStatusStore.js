/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/2/12
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.userOrganizationalStatusStore', {
    extend: 'Gam.data.store.Autocomplete',
    alias: 'store.usercmborganizationalstatusstore',

    require: ['Ems.model.AutocompleteSimpleModel'],
    model: 'Ems.model.AutocompleteSimpleModel',


    autocompleteName: 'department'

});
