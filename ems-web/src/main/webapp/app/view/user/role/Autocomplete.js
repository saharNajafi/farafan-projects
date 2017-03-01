/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/6/12
 * Time: 9:36 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.user.role.Autocomplete', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.userroleautocomplete',

    requires: [
        'Ems.store.userRoleStore',
        'Ems.model.AutocompleteSimpleModel'
    ],

    store: {type: 'userrolesotre'},

    multiSelect: true,
    id: 'roleAutocomplet',
    setValue: Ext.emptyFn,
    valueField: 'acId',
    displayField: 'acName',
    fieldLabel: 'نقش',
    listWidth: 290,
    width: 310,
    emptyText: 'انتخاب کنید...',
    hiddenName: 'roleMultiSelectds'
});

