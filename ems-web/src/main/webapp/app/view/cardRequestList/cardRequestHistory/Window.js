/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.cardRequestList.cardRequestHistory.Window', {
    extend: 'Ext.window.Window',

    id: 'idcardRequestHistoryWindows',

    alias: 'cardrequesthistorywindows',

    requires: ['Ems.view.cardRequestList.cardRequestHistory.Grid'],

    height: 600,
    width: 1000,

    title: 'تاریخچه درخواست ',

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
            {xtype: 'cardrequesthistorygrid'}
        ];
        this.callParent(arguments);
    }

});
