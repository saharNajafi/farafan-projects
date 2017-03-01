/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/10/12
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.user.view.Windows', {
    extend: 'Ext.window.Window',

    requires: ['Ems.view.user.view.Form'],

    height: 400,
    width: 850,

    resizable: false,
    closeAction: 'destroy',
    //modal: true,
    layout: {
        type: 'fit'
    },
    title: 'مشاهده اطلاعات کامل کاربر',

    initComponent: function () {
        var me = this;

        this.dockedItems = [
            {
                xtype: 'toolbar',
                style: 'direction:ltr; border:0px;',
                bodyStyle: 'direction:ltr; border:0px;',
                dock: 'bottom',
                width: 55,
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
                xtype: 'userviewform'
            }
        ]
        me.callParent(arguments);
    }
});
