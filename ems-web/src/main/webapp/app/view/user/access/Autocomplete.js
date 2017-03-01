/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/6/12
 * Time: 8:29 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.user.access.Autocomplete', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.useraccessautocomplete',

    requires: [
        'Ems.store.userAccessStore'
    ],

    store: {type: 'useraccesssotre'},

    multiSelect: true,
    id: 'accessAutocomplet',
    setValue: Ext.emptyFn,
    valueField: 'acId',
    fieldLabel: ' دسترسی',
    listWidth: 290,
    width: 310,
    emptyText: 'انتخاب کنید...'
    //, hiddenName: 'heid1'
});
