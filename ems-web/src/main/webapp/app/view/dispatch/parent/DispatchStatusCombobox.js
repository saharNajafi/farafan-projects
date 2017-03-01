/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/11/12
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.dispatch.parent.DispatchStatusCombobox', {
    extend: 'Ext.form.ComboBox',
    alias: 'widget.dispatchstatuscombobox',

    requires: ['Ems.store.DispatchStatusStoreComboboxStore'],

    store: {type: 'dispatchstatusstorecomboboxstore'},

    queryMode: 'local',

    displayField: EmsObjectName.comboBox.name,

    valueField: EmsObjectName.comboBox.code,
    listConfig: {
        getInnerTpl: function () {
            var tpl = '<div>{name}</div>';
            return tpl;
        }
    }
});
