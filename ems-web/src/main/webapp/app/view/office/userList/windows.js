/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/13/12
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.office.userList.windows', {
    extend: 'Ext.window.Window',

    requires: ['Ems.view.office.userList.grid'],


    height: 500,
    width: 650,

    autoScroll: true,
    resizable: false,
    //closeAction: 'destroy',
    //modal: true,
    layout: {
        type: 'fit'
    },
    title: 'فهرست کاربران',

    initComponent: function () {
        var me = this;

        this.dockedItems = [
            {
                xtype: 'toolbar',
                style: 'direction:ltr; border:0px;',
                bodyStyle: 'direction:ltr; border:0px;',
                dock: 'bottom',
                width: 90,
                items: [
                    {
                        width: 70,
                        text: 'بستن',
                        iconCls: 'windows-Cancel-icon',
                        // style:'margin-right:5px; ',
                        handler: function () {
                            this.up('window').close();
                        }
                    }
                ]

            }
        ];

        this.items = [
            {
                xtype: 'officeuserlistgrid'
            }
        ]
        me.callParent(arguments);
    }
});
