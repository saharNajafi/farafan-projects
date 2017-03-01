/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 4/29/12
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.userAccessStore', {
    extend: 'Gam.data.store.Autocomplete',
    alias: 'store.useraccesssotre',

    require: [ 'Ems.model.AutocompleteSimpleModel' ],
    model: 'Ems.model.AutocompleteSimpleModel',

    autocompleteName: 'permissions'

});
