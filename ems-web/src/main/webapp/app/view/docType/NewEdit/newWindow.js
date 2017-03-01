/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/14/12
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.docType.NewEdit.newWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.doctypeneweditnewwindow',

    //requires:['Ems.view.docType.NewEdit.Form'],

    id: 'idDocTypeNeweditNewwindow',
    height: 250,
    width: 350,

    resizable: false,
    // closeAction: 'destroy',
    //modal: true,
    layout: {
        type: 'fit'
    },
    title: 'افزودن سند',

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
                        id: 'btnNewDocTypeEditWindow',
                        text: 'ثبت',
                        xtype: 'button',
                        width: 70,
                        iconCls: 'windows-Save-icon'/* ,
                     margins: '5 0 0 0'*/
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
                xtype: 'doctypeneweditform'
            }
        ]
        me.callParent(arguments);
    }
});
