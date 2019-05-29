Ext.define('Ems.view.cardRequestList.printRegistrationReceipt.PrintRegistrationReceiptWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.printRegistrationReceiptWindow',
//	layout: 'fit',
    requires: ['Ems.view.cardRequestList.printRegistrationReceipt.Dialog'],
    width: 700,
    height: 500,
    resizable: false,
    initComponent: function () {
        this.items = [
            {xtype:'printRegistrationReceiptDialog'}
        ];
        this.callParent(arguments);
    }
});
