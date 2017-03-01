/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/9/12
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.user.role.MultiSelect', {
    extend: 'Ext.container.Container',
    alias: 'widget.userrolemultiselect',

    requires: [
        'Ems.view.ux.form.MultiSelect'
        , 'Ems.store.userRoleMultiSelectStore'
    ],

    title: 'MultiSelect Test',
    bodyPadding: 10,

    items: [
        {
            xtype: 'form',
            height: 100,
            //width:352,
            autoScroll: true,
            items: [
                {
                    //  height:100,
                    border: false,
                    bodyBorder: false,
                    width: '100%',
                    xtype: 'multiselect',
                    msgTarget: 'side',
                    name: 'roleMultiSelectds',
                    id: 'roleMultiSelectds',
                    allowBlank: true,
                    store: {type: 'userrolemultiselectstore'},
                    displayField: 'name',
                    valueField: 'id',
                    ddReorder: true
                }
            ]
        }
    ]
})
