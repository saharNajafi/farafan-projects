/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/8/12
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.bizLog.Entity', {
    extend: 'Ext.form.ComboBox',

    alias: 'widget.entityaction',
    requires: ['Ems.store.BizLogEntityStore'],
    store: {type: 'bizlogentitystore'},
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

