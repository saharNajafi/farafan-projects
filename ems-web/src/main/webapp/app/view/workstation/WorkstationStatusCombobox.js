/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/11/12
 * Time: 5:50 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.workstation.WorkstationStatusCombobox', {
    extend: 'Ext.form.ComboBox',
    alias: 'widget.workstatuscombobox',

    requires: ['Ems.store.WorkStationStatusStore'],

    store: {type: 'workstationstatusstore'},

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
