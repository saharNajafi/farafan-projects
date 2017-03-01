/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/11/12
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.office.OfficeStatusCombobox', {
    extend: 'Ext.form.ComboBox',
    alias: 'widget.officestatuscombobox',

    requires: ['Ems.store.OfficeStatusComboboxStore'],

    store: {type: 'officestatuscomboboxstore'},

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
