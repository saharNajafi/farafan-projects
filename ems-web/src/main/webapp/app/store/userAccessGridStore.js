/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 4/29/12
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.userAccessGridStore', {
    extend: 'Ext.data.Store',
    alias: 'store.useraccessgridsotre',
    require: ['Ems.model.ComboBoxSimpleModel'],
    model: 'Ems.model.ComboBoxSimpleModel'
});
