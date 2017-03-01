/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/30/12
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.user.RequestToken.deleteToken.Windows', {
    extend: 'Ext.window.Window',

    requires: ['Ems.view.user.RequestToken.deleteToken.Form'],
    id: 'idRequestTokenDeleteTokenWindows',

    height: 180,
    width: 400,

    resizable: false,
    //closeAction: 'destroy',
    layout: { type: 'fit' },
    title: 'دلیل ابطال توکن',

    initComponent: function () {

        var me = this;

        this.bbar = [
            { action: 'btnRegisterDeleteTokenWindows', width: 100, text: 'ثبت'},
            {
                width: 100,
                text: 'انصراف', handler: function () {
                this.up('window').close();
            }
            }
        ];

        this.items = [
            {
                xtype: 'userrequesttokendeletetokenform'
            }
        ]
        me.callParent(arguments);
    }
});
