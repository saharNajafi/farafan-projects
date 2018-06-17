/**
 * Created by IntelliJ IDEA.
 * User: Navid
 * Date: 05/19/2018
 * Time: 01:55 PM
 */

Ext.define('Ems.view.office.Capacity.Grid', {
    extend: 'Gam.grid.Crud',

    alias: 'widget.officecapacitygrid',

    multiSelect: false,
    requires: ['Ems.store.OfficeCapacityStore', 'Ems.view.office.Dialog' ],
    autoHeight: true,
    id: 'grdOfficesCapacityGrid',


    store: {type: 'officecapacitystore'},


    tbar: [
        {
            iconCls: 'add-btn',
            text: 'جدید',
            //action: 'exportExcel',
            handler: function(sender) {
                Ext.create('Ems.view.office.Capacity.Dialog', { enrollmentOfficeID: sender.up('grid').enrollmentOfficeID}).show();
            }
        }
    ],

    doAdd: function(grid, button)
    {

        if(this.isOnValidState() === false)
        {
            /*this.showParentIdError();*/
            return;
        }

        var me = this,
            dialog = me.createView({
                xtype: button.viewType,
                actionType: Gam.GlobalConfiguration.ACTION_TYPES.ADD,
                opener: grid
            });

        if(dialog)
        {
            dialog.setTitle(Gam.Resource.title.add);
            /*dialog.on('savesuccess', this.reloadStore, this);*/
            dialog.setBaseParams();
            me.addNewRecordForDialog(dialog);
            dialog.show();
        }
    },

    actionColumnItems: [
        {
            icon: 'resources/themes/images/default/shared/edit.png',
            tooltip: 'ویرایش',
            action: 'editCapacity',
            stateful: true,
            stateId: this.stateId + 'EditCapacity',
            handler: function (sender,r,c,d,e,f) {
                var grid = sender.up('grid');
                var store = grid.store;
                var form = Ext.create('Ems.view.office.Capacity.Dialog', { enrollmentOfficeID: grid.enrollmentOfficeID});
                var record = store.getAt(r);
                form.editableField = record.get('editable');
                form.action = "edit";
                form.sendID = record.get('id');
                form.down('#capacity').setValue(record.get('capacity'));
                form.down('#workingHoursFrom').hide();
                form.down('#workingHoursTo').hide();
                form.down('#shiftNo').hide();
                form.down('#startDate').hide();
                form.show();
            }
        },
    ],

    initComponent: function () {

        this.columns = [
            {
                text: 'تاریخ شروع',
                width: 120,
                dataIndex: 'startDate',
                xtype: 'gam.datecolumn',
                format: Ext.Date.defaultDateTimeFormat
            },
            {
                text: 'تاریخ پایان',
                width: 120,
                dataIndex: 'endDate',
                xtype: 'gam.datecolumn',
                format: Ext.Date.defaultDateTimeFormat
            },
            {
                xtype: 'gridcolumn',
                text: 'ظرفیت',
                width: 150,
                dataIndex: EmsObjectName.capacity.capacity
            },
            {
                xtype: 'gridcolumn',
                text: 'ساعت شروع کار',
                align: 'center',
                dataIndex: EmsObjectName.capacity.workingHoursFrom,
                id: EmsObjectName.capacity.workingHoursFrom,
                renderer: function(workingHour) {
                    if (workingHour) {
                        workingHour = new String(workingHour);
                        if (workingHour.indexOf(".5") > 0) {
                            //  Change 12.5 to 12:30
                            return workingHour.split(".")[0] + ":30";
                        } else {
                            //  Change 12 to 12:00
                            return workingHour.split(".")[0] + ":00";
                        }
                    }
                }
            },
            {
                xtype: 'gridcolumn',
                text: 'ساعت پایان کار',
                align: 'center',
                dataIndex: EmsObjectName.capacity.workingHoursTo,
                id: EmsObjectName.capacity.workingHoursTo,
                renderer: function(workingHour) {
                    if (workingHour) {
                        workingHour = new String(workingHour);
                        if (workingHour.indexOf(".5") > 0) {
                            //  Change 12.5 to 12:30
                            return workingHour.split(".")[0] + ":30";
                        } else {
                            //  Change 12 to 12:00
                            return workingHour.split(".")[0] + ":00";
                        }
                    }
                }
            },
            {
                xtype: 'gridcolumn',
                text: 'شیفت',
                width: 100,
                dataIndex: EmsObjectName.capacity.shiftNo,
                renderer: function (value) {
                    if (value == "0") {
                        return "صبح";
                    } else if (value == "1") {
                        return "شب";
                    }
                }
            }
        ];
        this.callParent(arguments);
    }


});

