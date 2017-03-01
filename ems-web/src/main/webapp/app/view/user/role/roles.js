Ext.define('Ems.view.user.role.roles', {
    extend: 'Ext.form.Panel',
    alias: 'widget.userRoles',

    requires: [
        'Ems.store.userRoleStore',
        //'Ems.view.user.role.Grid',
        'Ems.view.user.role.Autocomplete',
        'Ems.view.user.role.MultiSelect'

        // ,'Ems.view.user.multiSelect.MultiSelect'
        //,'Ems.store.userRoleGridStore'
    ],

    border: false,
    bodyBorder: false,

    initComponent: function () {
        this.items = this.getRolesItems();
        this.callParent(arguments);
    },

    getRolesItems: function () {
        return(
        {
            xtype: 'container',
            margin: 5,
            border: false,
            items: [
                {
                    xtype: 'container',
                    layout: {type: 'hbox'}, items: [
                    {
                        xtype: 'userroleautocomplete', allowBlank: true
                    },
                    {
                        xtype: 'button',
                        text: 'حذف',
                        itemId: 'btnRoleRemove'
                        //iconCls: 'i-delete'
                    }
                ]
                } ,
                {
                    xtype: 'userrolemultiselect',
                    height: 100,
                    style: ' margin:5px; height:100px;'
                }
            ]
        }
            );
    }

});