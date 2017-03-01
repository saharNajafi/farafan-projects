Ext.define('Ems.view.workstation.Grid', {
    extend: 'Gam.grid.RowEditingCrud',

    alias: 'widget.workstationgrid',

    stateId: 'wWorkStationGrid',

    id: 'idWorkstationsGrid',

    title: 'مدیریت ایستگاه کاری',

    requires: [
        'Ems.store.WorkstationStore',
        'Ems.view.office.OfficeName.AutoComplete',
        'Ems.view.workstation.WorkstationStatusCombobox',
        'Ems.view.office.PersonOfficeCombo'
    ],

    store: {type: 'workstationstore'},

    actions: ['gam.add', 'gam.delete' ],

    actionColumnItems: [
        'edit'
        //'view->workstationdialog'
        , {
            tooltip: 'تایید',
            action: 'accept',
            stateful: true,
            stateId: this.stateId + 'Accept',
            getClass: function (value, metadata, record) {
                var Checked = record.get(EmsObjectName.workstation.checkUnCheck);
                return 'grid-' + (Checked === 'N' ? 'Checked' : null) + '-action-icon';
            }
        }
        , {
            tooltip: 'عدم تایید',
            action: 'unAccept',
            stateful: true,
            stateId: this.stateId + 'UnAccept', getClass: function (value, metadata, record) {
                var UnChecked = record.get(EmsObjectName.workstation.checkUnCheck);
                return 'grid-' + (UnChecked === 'N' ? 'UnChecked' : null) + '-action-icon';
            }
        }
    ],

    tbar: [
        'gam.add', 'gam.delete'
        , {
            iconCls: 'girdAction-exportExcel-icon',
            text: 'خروجی اکسل',
            action: 'exportExcel',
            stateful: true,
            stateId: this.stateId + 'ExportExcel'
        }
    ],

    initComponent: function () {
        this.columns = this.getItemsWorkstationForm();
        this.callParent(arguments);
    }, getItemsWorkstationForm: function () {
        return([
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'استان',
                dataIndex: EmsObjectName.workstation.provinceName,
                id: EmsObjectName.workstation.provinceName,
                filterable: true,
                filter: true
            },
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'نام دفتر',
                dataIndex: EmsObjectName.workstation.officeName,
                id: EmsObjectName.workstation.officeName,
                filterable: true,
                filter: true,
                editor: {
                    allowBlank: false,
                    xtype: 'personOfficeCombo',
                    hiddenName: EmsObjectName.workstation.officeId
                    /*,
                     listeners: {
                     'autocompleteselect': function(autocomplete, record)
                     {
                     alert('select22');
                     var formRecord1=autocomplete.up()
                     var formRecord2=  formRecord1.form
                     var formRecord=formRecord2._records;
                     //                            var valueForm=autocomplete.up('form').getValues();
                     var autoId=record.get('acId');
                     formRecord.set(EmsObjectName.workstation.officeId,autoId);

                     //  var editingPlugin = autocomplete.up('form').editingPlugin,
                     //      editingRecord = editingPlugin.context.record,
                     //      rowEditor = editingPlugin.getEditor();

                     //editingRecord.set('pathAddress', record.get('address'));

                     //rowEditor.reRenderDisplayFields(editingRecord);
                     }
                     }*/
                }
            },
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'کد دفتر',
                dataIndex: EmsObjectName.workstation.officeCode,
                id: EmsObjectName.workstation.officeCode,
                filterable: true,
                filter: true
            },
            {
                xtype: 'gridcolumn',
                width: 200,
                text: 'کد ایستگاه کاری',
                dataIndex: EmsObjectName.workstation.worksationID,
                id: EmsObjectName.workstation.worksationID,
                filterable: true,
                filter: true,
                editor: {
                    allowBlank: false,
                    xtype: 'textfield',
                    maxLength: 100,
                    enforceMaxLength: 100
                }
            } ,
            {
                xtype: 'gridcolumn',
                width: 200,
                text: 'کد فعالسازی',
                dataIndex: EmsObjectName.workstation.activationCode,
                id: EmsObjectName.workstation.activationCode,
                filterable: true,
                filter: true,
                editor: {
                    allowBlank: false,
                    xtype: 'textfield',
                    regex: /^[a-z,A-Z,0-9]+$/,
                    regexText: 'استفاده از کاراکتر غیر مجاز',
                    maxLength: 255,
                    enforceMaxLength: 255
                }
            } ,
            {
                xtype: 'gridcolumn',
                width: 200,
                text: 'وضعیت',
                filterable: true,
                filter: {
                    xtype: 'workstatuscombobox'
                },
                dataIndex: EmsObjectName.workstation.checkUnCheck,
                id: EmsObjectName.workstation.checkUnCheck,
                renderer: function (value) {
                    if (value && typeof value === 'string') {
                        switch (value) {
                            case 'N':
                                return 'جدید';
                            case 'A':
                                return 'تایید شد';
                            case 'R':
                                return 'عدم تایید'
                        }
                    }
                }
            }
        ]);
    }, getCheckedActionColumn: function () {
        return({
            // style:'text-align: center; ',
            dataIndex: EmsObjectName.workstation.checkUnCheck,
            tooltip: 'تایید', getClass: function (value, metadata, record) {
                var Checked = record.get(EmsObjectName.workstation.checkUnCheck);
                return 'grid-' + ((Checked === 1) ? 'Checked' : null) + '-action-icon';
            }, scope: this
        });
    }, getUnCheckedActionColumn: function () {
        return({
            //style:'text-align: center; ',
            dataIndex: EmsObjectName.workstation.checkUnCheck,
            tooltip: 'عدم تایید', getClass: function (value, metadata, record) {
                var UnChecked = record.get(EmsObjectName.workstation.checkUnCheck);
                return 'grid-' + ((UnChecked === 1) ? 'UnChecked' : null) + '-action-icon';
            }, scope: this
        });
    }


});
