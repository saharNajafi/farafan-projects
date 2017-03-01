/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/2/12
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.user.RequestToken.windows', {
    extend: 'Ext.window.Window',

    alias: 'widget.requesttokenwindows',
    requires: ['Ems.view.user.RequestToken.form', 'Ems.view.user.RequestToken.grid'],

    id: 'idRequestTokenwindows',

    grid: null,
    rowIndex: null,

//    setParameter: function (grid,rowIndex){
//        this.grid=grid;
//        this.rowIndex=rowIndex;
//    },

    //height: 280,
    width: 500,

    resizable: false,
    //closeAction: 'destroy',
    //modal: true,
//    layout: {
//        type: 'fit'
//    },
    title: 'درخواست توکن جدید',

    initComponent: function () {
        var me = this;

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

        this.items = [
            {
                xtype: 'userrequesttokenform',
                controller: this.controller
            },
            {
                xtype: 'userrequesttoken',
                controller: this.controller
            }
        ];
        me.callParent(arguments);
    }
});


