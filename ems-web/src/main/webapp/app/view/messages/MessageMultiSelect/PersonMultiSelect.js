/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/9/12
 * Time: 10:30 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.messages.MessageMultiSelect.PersonMultiSelect', {
    extend: 'Ext.container.Container',
    alias: 'widget.personmultiselect',

    requires: [
        'Ems.view.ux.form.MultiSelect' ,
        'Ems.store.personMultiSelectStore'
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
                    name: 'personMultiSelectds',
                    id: 'personMultiSelectds',
                    allowBlank: true,
                    store: {type: 'personmultiselectstore'},
                    displayField: 'name',
                    valueField: 'id',
                    ddReorder: true
                }
            ]
        }
    ]


});