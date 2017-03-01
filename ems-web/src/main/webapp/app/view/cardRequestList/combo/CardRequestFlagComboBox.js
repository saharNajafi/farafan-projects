/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
Ext.define('Ems.view.cardRequestList.combo.CardRequestFlagComboBox', {
    extend: 'Ext.form.ComboBox',

    alias: 'widget.cardrequestflagcombobox',
    requires: ['Ems.store.CardRequestFlagComboBoxStore'],

    store: {type: 'cardrequestflagcomboboxstore'},
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
