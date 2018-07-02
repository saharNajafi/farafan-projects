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
                Ext.create('Ems.view.office.Capacity.Dialog', { title: 'چدید', height: 210, enrollmentOfficeID: sender.up('grid').enrollmentOfficeID}).show();
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
           // icon: 'resources/themes/images/default/shared/edit.png',
            tooltip: 'ویرایش',
            action: 'editCapacity',
            stateful: true,
            stateId: this.stateId + 'EditCapacity',
            getClass: function (value, metadata, record) {
                if(record.get('editable')) {
                    return 'girdAction-OfficeCapacity-edit-icon';
                }
                else {
                    return 'x-hide-display';
                }
           },
            handler: function (sender,r,c,d,e,f) {
                var grid = sender.up('grid');
                var store = grid.store;
                var form = Ext.create('Ems.view.office.Capacity.Dialog', {title: 'ویرایش', height: 100, enrollmentOfficeID: grid.enrollmentOfficeID});
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
                sortable: false,
                dataIndex: 'startDate',
                renderer: function (val) {
                    var dd = val.split('T')[0];
                   var newVal =  new Date(dd);
                   var month = (Ext.JalaliDate.getMonth(newVal)+1).toString();
                   var day = Ext.JalaliDate.getDate(newVal).toString();
                   if(month.length == 1) {
                       month = 0 + month;
                   }
                   if(day.length == 1) {
                       day = 0 + day;
                   }
                   return Ext.JalaliDate.getFullYear(newVal).toString() + '/' + month + '/' + day;
                }
                //xtype: 'gam.datecolumn',
                //format: Ext.Date.defaultDateTimeFormat
            },
            {
                text: 'تاریخ پایان',
                width: 120,
                sortable: false,
                dataIndex: 'endDate',
                renderer: function (val) {
                    var dd = val.split('T')[0];
                    var newVal =  new Date(dd);
                    var month = (Ext.JalaliDate.getMonth(newVal)+1).toString();
                    var day = Ext.JalaliDate.getDate(newVal).toString();
                    if(month.length == 1) {
                        month = 0 + month;
                    }
                    if(day.length == 1) {
                        day = 0 + day;
                    }
                    return Ext.JalaliDate.getFullYear(newVal).toString() + '/' + month + '/' + day;
                }
                //xtype: 'gam.datecolumn'
                //format: Ext.Date.defaultDateTimeFormat
            },
            {
                xtype: 'gridcolumn',
                text: 'ظرفیت',
                width: 150,
                sortable: false,
                dataIndex: EmsObjectName.capacity.capacity
            },
            {
                xtype: 'gridcolumn',
                text: 'ساعت شروع کار',
                align: 'center',
                sortable: false,
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
                sortable: false,
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
                sortable: false,
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

