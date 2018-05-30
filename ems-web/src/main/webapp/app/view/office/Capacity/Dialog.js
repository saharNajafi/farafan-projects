/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/12/12
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.office.Capacity.Dialog', {
    extend: 'Gam.window.dialog.LocalEntity',
    alias: 'widget.officecapacitydialog',


    autoScroll: true,

    width: 400,

    initComponent: function () {
        this.height = 250;
        this.callParent(arguments);
    },

    buildFormItems: function () {
        return [
            {
                xtype: 'numberfield',
                hidden: true,
                itemId: 'id'
            },
            {
                xtype: 'combobox',
                store: Ext.create('Ext.data.Store', {
                    fields: [
                        { name: 'id', type: 'number'},
                        { name: 'title', type: 'string'}
                    ],
                    data: [
                        {id: 1, title: 'صبح'},
                        {id: 2, title: 'شب'}
                    ]
                }),
                valueField: 'id',
                displayField: 'title',
                itemId: 'shiftNo',
                fieldLabel: 'شیفت'
            },
            {
                xtype: 'datefield',
                itemId: 'startDate',
                fieldLabel: 'تاریخ شروع'
            },
            {
                xtype: 'workinghours',
                itemId: 'workingHoursFrom',
                fieldLabel: 'از ساعت'
            },
            {
                xtype: 'workinghours',
                itemId: 'workingHoursTo',
                fieldLabel: 'تا ساعت'
            },
            {
                xtype: 'numberfield',
                itemId: 'capacity',
                fieldLabel: 'ظرفیت'
            }
        ]
    }
});