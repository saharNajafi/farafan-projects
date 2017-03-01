/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.estelam2falseList.estelam2Log.Window', {
    extend: 'Ext.window.Window',

    id: 'idestelam2LogWindows',

    alias: 'estelam2logwindows',

    requires: ['Ems.view.estelam2falseList.estelam2Log.Grid'],

    height: 600,
    width: 1000,

    title: 'لاگ استعلام منفی',

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
            {xtype: 'estelam2loggrid'}
        ];
        this.callParent(arguments);
    }

});
