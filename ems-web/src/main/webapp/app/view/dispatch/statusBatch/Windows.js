/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/6/12
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.dispatch.statusBatch.Windows', {
    extend: 'Ext.window.Window',

    id: 'idDispatchStatusBatchWindows',

    alias: 'dispatchstatusbatchwindows',

    requires: ['Ems.view.dispatch.statusBatch.Grid'],

    height: 400,
    width: 1100,

    resizable: false,

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

        //this.items=[{xtype:'dispatchstatusbatchgrid', parId: config.parId }];
        //delete config.parId;
        this.items = [
            {xtype: 'dispatchstatusbatchgrid'}
        ];
        this.callParent(arguments);
    }

});