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
        var cardRequestId = sessionStorage.getItem('cardRequestId');
        return [
            {
                //xtype: 'printRegistrationReceiptFieldSet'
                html: '<iframe src="/ems-web/app/pdfjs/web/pdfViewer.jsp?cardRequestId='+ cardRequestId+ '" frameborder="0" style="overflow:hidden;height:450px;width:100%" ></iframe>'
            }
        ];
    }
});
