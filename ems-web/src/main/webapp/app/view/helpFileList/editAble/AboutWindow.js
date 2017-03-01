/**
 * User: khodayari
 */

Ext.define('Ems.view.helpFileList.editAble.AboutWindow', {
	 extend: 'Ext.window.Window',
    alias: 'widget.editAbleaboutwindow',

    requires: ['Ems.view.helpFileList.editAble.AboutDialog'],

    initComponent: function () {
        this.items = [
                      {xtype:'editAbleaboutdialog'},
                      ];
        this.buttons=[{
                          action: 'btnAdd',
                          id: 'idBtnAdd',
                          itemId : 'btnAdd',
                          text: 'ارسال',
                          xtype: 'button',
                          width: 70,
                          iconCls: 'windows-Save-icon',
                          handler: function () {
                          }
                  	},
                  	{
                  		  action: 'btnCancelEdit',
                  		  id:'idBtnCancelEdit',
                  		  itemId : 'btnCancelEdit',
                          width: 70,
                          text: 'انصراف',
                          xtype: 'button',
                          iconCls: 'windows-Cancel-icon',
                          handler: function () {
//                              this.up('window').close();
                      }
                  }]
        this.callParent(arguments);
    }
});
