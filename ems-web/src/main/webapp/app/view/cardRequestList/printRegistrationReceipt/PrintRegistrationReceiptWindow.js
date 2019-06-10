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

                        handler: function () {
                            // var grid = Ext.window("printRegistrationReceiptWindow");
                            var targetElement = Ext.getCmp('idPrintRegistrationReceiptDialog');
                            var myWindow = window.open('', '', 'width=600,height=500');
                            myWindow.document.write('<html><head>');
                            myWindow.document.write('<title>' + 'Title' + '</title>');
                            // myWindow.document.write('<script type="text/javascript" src="/wr/ext/4.1-beta-1/rtl/ext-all-debug.js"></script>');
                            myWindow.document.write('</head><body style="direction: ltr; border: 0px; width: 690px; right: 4px; top: 150px;">');
                            myWindow.document.write(targetElement.body.dom.innerHTML);
                            myWindow.document.write('</body></html>');
                            myWindow.document.close();
                            myWindow.print();
                            myWindow.onafterprint = function (event) {
                                console.log("Printing completed...");
                            };
                            myWindow.addEventListener("afterprint", function (event) {
                                console.log("Printing completed...");
                            });
                            (function() {

                                var beforePrint = function() {
                                    console.log('Functionality to run before printing.');
                                };

                                var afterPrint = function() {
                                    console.log('Functionality to run after printing');
                                };

                                if (window.matchMedia) {
                                    var mediaQueryList = window.matchMedia('print');
                                    mediaQueryList.addListener(function(mql) {
                                        if (mql.matches) {
                                            beforePrint();
                                        } else {
                                            afterPrint();
                                        }
                                    });
                                }

                                window.onbeforeprint = beforePrint;
                                window.onafterprint = afterPrint;

                            }());
                                // Ext.Ajax.request({
                                //     url: 'extJsController/cardrequestlist/print',
                                //     jsonData: {
                                //         cardRequestId: id
                                //     },
                                //     failure: function (resp) {
                                //         Tools.errorFailure();
                                //     }
                                // });
                            myWindow.close();
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
        ],
            this.callParent(arguments);
    }
});
