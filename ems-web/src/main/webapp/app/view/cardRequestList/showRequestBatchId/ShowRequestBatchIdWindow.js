Ext.define('Ems.view.cardRequestList.showRequestBatchId.ShowRequestBatchIdWindow', {
    extend: 'Ext.window.Window',
    id: 'idshowRequestBatchId',
    alias: 'showrequesttrackingid',
    requires: ['Ems.view.cardRequestList.showRequestBatchId.Dialog'],
    width: 300,
    resizable: false,

    constructor: function (config) {
        this.dockedItems = [
            {
                xtype: 'toolbar',
                style: 'direction:ltr; border:0px;',
                bodyStyle: 'direction:ltr; border:0px;',
                dock: 'bottom',
                items: [
                    {
                        text: 'بستن', handler: function () {
                            this.up('window').close();
                        }
                    }
                ]

            }
        ];
        this.items = [
            {xtype: 'showRequestBatchIdDialog'}
        ];
        this.callParent(arguments);
    }
});
