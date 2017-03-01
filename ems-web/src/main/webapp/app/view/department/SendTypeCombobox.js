/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/9/12
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.department.SendTypeCombobox', {
    extend: 'Ext.form.ComboBox',
    alias: 'widget.sendtypecombobox',

    requires: ['Ems.store.DispatchSendTypeStore'],

    store: {type: 'dispatchsendtypestore'},

    initComponent: function () {
        // this.getValueDefault();
        this.callParent(arguments);
    },

    queryMode: 'local',

    displayField: EmsObjectName.comboBox.name,

    valueField: EmsObjectName.comboBox.code,

    hiddenName: EmsObjectName.Department.dispatchSendType,

    autoSelect: true,


    listConfig: {
        getInnerTpl: function () {
            var tpl = '<div>{name}</div>';
            return tpl;
        }
    },

    getValueDefault: function (view) {

        var comboSendType = Ext.getCmp(EmsObjectName.Department.dispatchSendType);

        comboSendType.setValue(store.getAt(0).get('code'));


//          var  comboStore = comboSendType.getStore();
//
//        var me = this;
//        comboStore.on('load',function(store) {
//            comboSendType.setValue(store.getAt(0).get('code'));
//        })


    }
});
