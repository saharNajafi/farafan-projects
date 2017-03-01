/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/24/12
 * Time: 12:31 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.viewport.changePassWord.Window', {
    extend: 'Ext.window.Window',
    alias: 'widget.changepasswordwindow',
    requires: ['Ems.view.viewport.changePassWord.Form'],
    width: 400,
    height: 170,
    initComponent: function () {
        this.items = [
            {xtype: 'changepasswordform'}
        ];
        this.dockedItems = [
            {
                xtype: 'toolbar',
                style: 'direction:ltr; border:0px;',
                bodyStyle: 'direction:ltr; border:0px;',
                dock: 'bottom',
                width: 160,
                items: [
                    {
                        id: 'btnChangePassWord',
                        text: 'ثبت',
                        xtype: 'button',
                        width: 70,
                        iconCls: 'windows-Save-icon',
                        handler: function () {
                            Tools.defineUserAndDepartment.register = 0;
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

        this.callParent(arguments);
    }

});
