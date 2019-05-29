Ext.define('Ems.view.cardRequestList.printRegistrationReceipt.Dialog', {
    extend: 'ICT.Panel',
    id: 'idPrintRegistrationReceiptDialog',
    requires :  ['Ems.view.cardRequestList.printRegistrationReceipt.printRegistrationReceiptFieldSet'],
    alias: 'widget.printRegistrationReceiptDialog',
    bodyBorder: false,
    border: false,
    initComponent: function () {
        this.getItems();
        this.callParent(arguments);
    },

    getItems: function(){
        return [
            {
                xtype: 'printRegistrationReceiptFieldSet'
            }
        ];
    }
});
