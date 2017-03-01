/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/8/12
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.bizLog.Action', {
    extend: 'Ext.form.ComboBox',

    alias: 'widget.bizlogaction',
    requires: ['Ems.store.bizLogActionStore'],

    store: {type: 'bizlogactionstore'},
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
