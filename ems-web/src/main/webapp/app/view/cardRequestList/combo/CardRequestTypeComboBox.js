/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.cardRequestList.combo.CardRequestTypeComboBox', {
    extend: 'Ext.form.ComboBox',

    alias: 'widget.cardrequesttypecombobox',
    requires: ['Ems.store.CardRequestTypeComboBoxStore'],

    store: {type: 'cardrequesttypecomboboxstore'},
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
