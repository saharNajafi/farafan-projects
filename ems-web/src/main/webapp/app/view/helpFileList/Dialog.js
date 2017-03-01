//khodayari
Ext.define('Ems.view.helpFileList.Dialog', {
    extend: 'Gam.window.dialog.LocalEntity',
    alias: 'widget.helpfilelistdialog',
    id:'idhelpfilelistdialog',
    requires: ['Ems.view.helpFileList.Uploader'],

    width: 500,
    saveButton:false,
    cancelButton : false,
    initComponent: function () {
        this.height = 150;

        this.callParent();
    },

    buildFormItems: function () {
        return [
                {
            xtype: 'helpFileUploader'
                }
    ];
    }
});
