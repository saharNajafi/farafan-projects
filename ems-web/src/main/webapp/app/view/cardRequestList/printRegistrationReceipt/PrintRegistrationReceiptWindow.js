Ext.define('Ems.view.cardRequestList.printRegistrationReceipt.PrintRegistrationReceiptWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.printRegistrationReceiptWindow',
//	layout: 'fit',
    requires: ['Ems.view.cardRequestList.printRegistrationReceipt.Dialog'],
    width: 700,
    resizable: false,
    initComponent: function () {
        this.items = [
            {xtype: 'printRegistrationReceiptDialog'}
        ];

        this.dockedItems = [
            {
                xtype: 'toolbar',
                style: 'direction:ltr; border:0px;',
                bodyStyle: 'direction:ltr; border:0px;',
                dock: 'bottom',
                width: 160,

                items: [
                    {
                        action: 'btnNewEditUserRequest',
                        id: 'idBtnNewEditUserRequest',
                        text: 'پرینت',
                        xtype: 'button',
                        type: 'print',
                        width: 70,
                        iconCls: 'windows-Save-icon',

                        handler: function (res) {
                            // var grid = Ext.window("printRegistrationReceiptWindow");
                           // var targetElement = Ext.getCmp('idPrintRegistrationReceiptDialog');
                            // var myWindow = window.open('', '', 'width=600,height=500');
                            // myWindow.document.write('<html><head>');
                            // myWindow.document.write('<title>' + 'Title' + '</title>');
                            // // myWindow.document.write('<script type="text/javascript" src="http://extjs.cachefly.net/ext-4.1.1-gpl/ext-all-debug.js"></script>');
                            // myWindow.document.write('</head><body>');
                            // myWindow.document.write(targetElement.body.dom.innerHTML);
                            // myWindow.document.write('</body></html>');
                            // myWindow.document.close();
                            // myWindow.close();
                            // myWindow.print();
                            // console.log(targetElement.body.dom.innerHTML.getElementById("cardRequestId").valueOf());
                           //console.log( document.getElementById("crqId").valueOf());
                           //console.log(Ext.getDom('crqId').getValue());
                            Ext.Ajax.request({

                                standardSubmit : true,
                                url: 'extJsController/cardrequestlist/print',
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/pdf'
                                },
                                success: function (response) {
                                    window.open("extJsController/cardrequestlist/print", "_blank")
                                },
                                failure: function (resp) {
                                    Tools.errorFailure();
                                }
                            });
                        }
                    },
                    {
                        width: 70,
                        text: 'انصراف',
                        iconCls: 'windows-Cancel-icon',
                        // style:'margin-right:5px; ',
                        handler: function () {
                            this.up('window').close();
                        }
                    }
                ]

            }
        ];
            this.callParent(arguments);
    }
});
