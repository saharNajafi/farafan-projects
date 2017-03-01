/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/9/12
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */


Ext.define('Ems.view.user.NewEdit.Windows', {
    extend: 'Ext.window.Window',
    alias: 'widget.neweditwindows',

    requires: ['Ems.view.user.NewEdit.Form'],

    id: 'idNewEditWindow',
    height: 520,
    width: 810,

    resizable: false,
    closeAction: 'destroy',
    //modal: true,
    layout: {
        type: 'fit'
    },
    title: 'ویرایش مشخصات کاربران',

    initComponent: function () {
        var me = this;
        this.dockedItems = [
            {
                xtype: 'toolbar',
                style: 'direction:ltr; border:0px;',
                bodyStyle: 'direction:ltr; border:0px;',
                dock: 'bottom',
                width: 160,
                items: [
                    {
                        action: 'btnNewEditUser',
                        id: 'idBtnNewEditUser',
                        text: 'ثبت',
                        xtype: 'button',
                        width: 70,
                        iconCls: 'windows-Save-icon',
                        handler: function () {
                            Tools.user.Editing = 0;
                        }


                        //style:'margin-right:10px; '
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
        ];

        this.items = [
            {
                xtype: 'userneweditform'
            }
        ]
        me.callParent(arguments);
    }
});




