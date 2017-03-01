Ext.define('Ems.view.user.access.access', {
    extend: 'Ext.form.Panel',
    alias: 'widget.useraccessaccess',

    requires: [
        'Ems.store.userAccessStore',
        //'Ems.view.user.access.Grid',
        'Ems.view.user.access.Autocomplete',
        'Ems.view.user.access.MultiSelect'
    ],


    border: false,
    bodyBorder: false,

    initComponent: function () {
        this.items = this.getRoleItems();

        this.callParent(arguments);
    },

    getRoleItems: function () {
        return({
            xtype: 'container',
            margin: 5,
            border: false,
            items: [
                {
                    xtype: 'container',
                    layout: {type: 'hbox'}, items: [
                    {
                        xtype: 'useraccessautocomplete', allowBlank: true
                    },
                    {
                        xtype: 'button',
                        text: 'حذف',
                        itemId: 'btnRemoveAccess'
                        //iconCls: 'i-delete'
                    }
                ]
                } ,
                {
                    xtype: 'useraccessmultiselect',
                    height: 100,
                    style: 'margin:5px; height:100px;',
                    hiddenName: 'userAccessHiddenName'
                }
            ]
        });
    }

});