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

    action: "add",

    title: 'چدید',

    initComponent: function () {
        this.callParent(arguments);
        this.minHeight = 100;
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
                        {id: 0, title: 'صبح'},
                        {id: 1, title: 'شب'}
                    ]
                }),
                valueField: 'id',
                displayField: 'title',
                itemId: 'shiftNo',
                forceSelection: true,
                fieldLabel: 'شیفت'
            },
            {
                xtype: 'datefield',
                itemId: 'startDate',
                fieldLabel: 'تاریخ شروع',
                forceSelection: true
            },
            {
                xtype: 'workinghours',
                itemId: 'workingHoursFrom',
                fieldLabel: 'از ساعت',
                forceSelection: true
            },
            {
                xtype: 'workinghours',
                itemId: 'workingHoursTo',
                fieldLabel: 'تا ساعت',
                forceSelection: true
            },
            {
                xtype: 'numberfield',
                itemId: 'capacity',
                minValue: 0,
                fieldLabel: 'ظرفیت'
            }
        ]
    }
});