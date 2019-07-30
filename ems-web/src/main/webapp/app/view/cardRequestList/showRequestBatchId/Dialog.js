Ext.define('Ems.view.cardRequestList.showRequestBatchId.Dialog', {
    extend: 'ICT.Panel',
    id: 'idShowRequestBatchIdDialog',
    requires :  ['Ems.view.cardRequestList.showRequestBatchId.showRequestBatchIdFieldSet'],
    alias: 'widget.showRequestBatchIdDialog',
    bodyBorder: false,
    border: false,
    initComponent: function () {
        this.getItems();
        this.callParent(arguments);
    },

    getItems: function(){
        return [
            {
                xtype: 'showRequestBatchIdFieldSet'
            }
        ];
    }
});
