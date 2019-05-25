Ext.define('Ems.view.cardRequestList.printRegistrationReceipt.Window', {
    extend: 'Ext.window.Window',

    id: 'idprintRegistrationReceiptWindows',

    alias: 'printRegistrationReceiptwindows',

    requires: ['Ems.view.cardRequestList.printRegistrationReceipt.printRegistrationReceipt'],

    height: 600,
    width: 1000,

    title: 'چاپ رسید',

    //resizable: false,

    closeAction: 'destroy',
    //modal: true,
    layout: {
        type: 'fit'
    },

    constructor: function (config) {
        this.dockedItems = [
            {
                xtype: 'toolbar',
                style: 'direction:ltr; border:0px;',
                bodyStyle: 'direction:ltr; border:0px;',
                dock: 'bottom',
                width: 60,
                items: [
                    {
                        //width:100 ,
                        text: 'بستن', handler: function () {
                            this.up('window').close();
                        }
                    }
                ]

            }
        ];
        //config = config || {};

        this.items = [
            {xtype: 'trackingcodeprint'}
        ];
        this.callParent(arguments);
    }

});
