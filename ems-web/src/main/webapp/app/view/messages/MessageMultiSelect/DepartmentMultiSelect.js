/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/9/12
 * Time: 10:30 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.messages.MessageMultiSelect.DepartmentMultiSelect', {
    extend: 'Ext.container.Container',
    alias: 'widget.departmentmultiselect',

    requires: [
        'Ems.view.ux.form.MultiSelect' ,
        'Ems.store.departmentMultiSelectStore'
    ],

    //title: 'MultiSelect Test',
    bodyPadding: 10,

    items: [
        {
            xtype: 'form',
            height: 80,
            //width:352,
            autoScroll: true,
            items: [
                {
                    border: false,
                    bodyBorder: false,
                    width: '100%',
                    xtype: 'multiselect',
                    msgTarget: 'side',
                    name: 'departmentMultiSelectds',
                    id: 'departmentMultiSelectds',
                    allowBlank: true,
                    store: {type: 'departmentmultiselectstore'},
                    displayField: 'name',
                    valueField: 'id',
                    ddReorder: true
                }
            ]
        }
    ]


});