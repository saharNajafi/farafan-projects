/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/9/12
 * Time: 10:30 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.user.access.MultiSelect', {
    extend: 'Ext.container.Container',
    alias: 'widget.useraccessmultiselect',

    requires: [
        'Ems.view.ux.form.MultiSelect' ,
        'Ems.store.userAccessMultiSelectStore'
    ],

    //title: 'MultiSelect Test',
    bodyPadding: 10,

    items: [
        {
            xtype: 'form',
            height: 100,
            //width:352,
            autoScroll: true,
            items: [
                {
                    border: false,
                    bodyBorder: false,
                    width: '100%',
                    xtype: 'multiselect',
                    msgTarget: 'side',
                    name: 'accessMultiSelectds',
                    id: 'accessMultiSelectds',
                    allowBlank: true,
                    store: {type: 'useraccessmultiselectstore'},
                    displayField: 'name',
                    valueField: 'id',
                    ddReorder: true
                }
            ]
        }
    ]


})