Ext.define('Ems.view.user.role.Grid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.userrolegrid',

    requires: ['Ems.store.userRoleGridStore'],
    store: {type: 'userrolegridsotre'},//Ext.data.StoreManager.lookup('simpsonsStore'),
    hideHeaders: true,
    //autoHeight: true,
    autoScroll: true,
    height: 170,
    width: '100%',
    id: 'idUserRoleGrid',
    initComponent: function () {

        this.columns = [Ext.create('Ext.grid.RowNumberer'), { dataIndex: EmsObjectName.comboBox.name, flex: 1}  ];
        this.selModel = Ext.create('Ext.selection.CheckboxModel');
        /*, {
         listeners: {
         selectionChange: function(sm, selections) {
         grid.down('#removeButton').setDisabled(selections.length == 0);
         }
         }
         });*/
        this.callParent(arguments);
    }
});
