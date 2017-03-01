Ext.define('Ems.store.userRoleStore', {
    extend: 'Gam.data.store.Autocomplete',
    alias: 'store.userrolesotre',

    require: ['Ems.model.AutocompleteSimpleModel'],
    model: 'Ems.model.AutocompleteSimpleModel',

    autocompleteName: 'roles'

});