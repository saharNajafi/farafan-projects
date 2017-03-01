/**
 * Created with IntelliJ IDEA.
 * User: Moghaddam
 * Date: 4/13/13
 * Time: 2:35 PM
 * Working hours selection Combobox
 */
Ext.define('Ems.view.office.NewEdit.WorkingHoursCombo', {
    extend: 'Gam.form.field.comboBox.ComboBox',
    alias: 'widget.workinghours',

    requires: [
        'Ems.store.WorkingHoursStore'
    ],

    store: {type: 'workinghoursstore'},

    multiSelect: false,

    displayField: 'name',

    valueField: 'code',

    forceSelection: true
});

