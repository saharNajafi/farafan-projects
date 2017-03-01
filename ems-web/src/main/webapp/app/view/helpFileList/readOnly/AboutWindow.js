/**
 * User: khodayari
 */

Ext.define('Ems.view.helpFileList.readOnly.AboutWindow', {
	 extend: 'Ext.window.Window',
    alias: 'widget.readOnlyaboutwindow',

    requires: ['Ems.view.helpFileList.readOnly.AboutDialog'],

    initComponent: function () {
        this.items = [
                      {xtype:'readOnlyaboutdialog'},
                      ];
        this.buttons=[{
                          action: 'btnEdit',
                          id: 'idBtnEdit',
                          itemId : 'btnEdit',
                          text: 'ویرایش',
                          xtype: 'button',
                          width: 70,
                          iconCls: 'windows-Edit-icon',
                          handler: function () {
                          }
                  	},
                  	{
                          width: 70,
                          text: 'انصراف',
                          xtype: 'button',
                          iconCls: 'windows-Cancel-icon',
                          handler: function () {
                              this.up('window').close();
                      }
                  }],
        this.callParent(arguments);
    }
});
