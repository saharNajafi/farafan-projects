/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 5:27 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.cardRequestList.combo.CardStateComboBox', {
    extend: 'Ext.form.ComboBox',

    alias: 'widget.cardstatecombobox',
    requires: ['Ems.store.CardStateComboBoxStore'],

    store: {type: 'cardstatecomboboxstore'},
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

