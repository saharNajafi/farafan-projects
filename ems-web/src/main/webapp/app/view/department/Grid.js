/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/3/12
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.department.Grid', {
    extend: 'Gam.grid.RowEditingCrud',

    alias: 'widget.departmentgrid',

    stateId: 'wDepartmentGrid',

    title: 'مدیریت جایگاه سازمانی',


    store: {type: 'departmentstore'},

    actions: ['gam.add', 'gam.delete' ],
    actionColumnItems: [
        {
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                if (parseInt(record.data.id, 10) !== 1)
                    return 'grid-edit-action-icon';
            },
            tooltip: 'ویرایش',
            action: 'edit',
            stateful: true,
            stateId: this.stateId + 'Edit'
        }

    ],
    contextMenu: [ 'gam.delete' ],
    tbar: [
           'gam.add',
           'gam.delete',
           {
               iconCls: 'girdAction-exportExcel-icon',
               text: 'خروجی اکسل',
               action: 'exportExcel',
               stateful: true,
               stateId: this.stateId + 'ExportExcel'
           }
       ],

    listeners: {
        /**
         * Enable location autocomplete if user is trying to edit one current records
         * @param evt
         */
        beforeedit: function (editor, context) {
            if (context.record.get("id").length == undefined) {
                //  This is an edit record (not a new one), so enable the locaiton autocomplete
                Ext.getCmp(EmsObjectName.Department.locationEditor).enable();
            }
        }
    },

    initComponent: function () {
        this.columns = this.getItemsDepartmentGridForm();
        this.callParent(arguments);
    }, getItemsDepartmentGridForm: function () {
        return([
                {
                    xtype: 'gridcolumn',
                    width: 150,
                    text: 'استان',
                    dataIndex: EmsObjectName.Department.provinceName,
                    id: EmsObjectName.Department.provinceName,
                    filterable: true,
                    filter: true
                },
            {
                xtype: 'gridcolumn',
                width: 110,
                text: 'نام',
                dataIndex: EmsObjectName.Department.name,
                id: EmsObjectName.Department.name,

                filterable: true,
                filter: true,
                editor: {
                    allowBlank: false,
                    xtype: 'textfield',
                    regex: Tools.regexFarsiAlpha(),
                    regexText: "از حروف الفبای فارسی استفاده کنید",
                    maxLength: 200,
                    enforceMaxLength: 200
                }
            },
            {
                xtype: 'gridcolumn',
                text: 'کد',
                align: 'center',
                dataIndex: EmsObjectName.Department.code,
                id: EmsObjectName.Department.code,
                filterable: true,
                filter: true,
                editor: {
                    allowBlank: false,
                    xtype: 'textfield',
                    maxLength: 10,
                    enforceMaxLength: 10
                }
            },
            {
                xtype: 'gridcolumn',
                text: 'جایگاه سازمانی مافوق',
                dataIndex: EmsObjectName.Department.parentDN,
                id: EmsObjectName.Department.parentDN,
                filterable: true,
                filter: true,
                width: 150,
                editor: {
                    xtype: 'patentdnautocomplete',
                    id: EmsObjectName.Department.parentDNEditor,
                    listeners: {
                        autocompleteselect: function (autocomplete, record) {
                            // Resetting location of department. Location field should be reselected by the user in
                            // order to select a new value base on its parent department location
                            if (autocomplete.getValue() != autocomplete.originalValue) {
                                Ext.getCmp(EmsObjectName.Department.locationEditor).setValue("");
                                Ext.getCmp(EmsObjectName.Department.locationEditor).enable();
                            }
                        },
                        change: function (autocomplete, newValue, oldValue) {
                            if (oldValue == undefined) {
                                //  The user has selected a department for edit. So enable the location field. It's
                                //  disabled by default. In case of New department scenario, this event would not be
                                //  raised, so the location autocomplete would be disabled until the user selects
                                //  something on parent department field
                                Ext.getCmp(EmsObjectName.Department.locationEditor).enable();
                            }
                        },
                        autocompleteclear: function (autocomplete) {
                            // Resetting location of department. Location field should be reselected by the user in
                            // order to select a new value base on its parent department location
                            Ext.getCmp(EmsObjectName.Department.locationEditor).setValue("");
                            Ext.getCmp(EmsObjectName.Department.locationEditor).disable();
                        }
                    }
                }
            },
            {
                xtype: 'gridcolumn',
                text: 'کدپستی',
                dataIndex: EmsObjectName.Department.postalCode,
                id: EmsObjectName.Department.postalCode,
                filterable: true,
                filter: true,
                editor: {
                    xtype: 'textfield',
                    allowBlank: false,
                    regex: /^[0-9]+$/,
                    minLength: 10,
                    maxLength: 10,
                    enforceMaxLength: 10

                }
            },
            {
                xtype: 'gridcolumn',
                text: 'دامنه',
                dataIndex: EmsObjectName.Department.dn,
                id: EmsObjectName.Department.dn,
                filterable: true,
                filter: true,
                editor: {
                    xtype: 'textfield',
                    allowBlank: false,
                    regex: /^[a-zA-Z\u002E\s]+$/,
                    regexText: 'از حروف لاتین و علامت نقطه استفاده کنید'
                }
            }/*,{
             xtype:'gridcolumn',
             text:'نوع ارسال',
             dataIndex: EmsObjectName.Department.dispatchSendType,
             id: EmsObjectName.Department.dispatchSendType,
             filterable: true,
             filter:{
             xtype:'sendtypecombobox'
             },
             renderer:function(value){
             if(value && typeof value === 'string')
             {
             var val=Tools.trim(value);
             if(val==='BOX'){
             return 'جعبه';
             }else if(val==='BATCH'){
             return 'دسته';
             }
             }
             }
             }*/,
            {
                xtype: 'gridcolumn',
                text: 'محل',
                dataIndex: EmsObjectName.Department.location,
                id: EmsObjectName.Department.location,
                filterable: true,
                filter: true,
                editor: {
                    id: EmsObjectName.Department.locationEditor,
                    allowBlank: false,
                    disabled: true,
                    xtype: 'locationautocomplete',
                    listeners: {
                        focus: function (autocomplete, e) {
                            if (Ext.getCmp(EmsObjectName.Department.parentDNEditor).getValue()) {
                                if (Ext.getCmp(EmsObjectName.Department.parentDNEditor).getValue() == 1) {
                                    //  User has selected the central department (MARKAZ) as parent of this department,
                                    //  so it could be a province department and we have to display the list of all possible
                                    //  values (Specifically provinces list)
                                    autocomplete.getStore().readParams.additionalParams = '';
                                } else {
                                    autocomplete.getStore().readParams.additionalParams =
                                        Ext.JSON.encode({'superDepartmentID': Ext.getCmp(EmsObjectName.Department.parentDNEditor).getValue()});
                                }
                            } else {
                                autocomplete.getStore().readParams.additionalParams = '';
                                Gam.window.MessageManager.showInfoMsg('لطفا ابتدا جایگاه سازمانی مافوق دفتر را تعیین نمایید');
                            }
                        }
                    }
                }
            },
            {
                xtype: 'gridcolumn',
                text: 'آدرس',
                dataIndex: EmsObjectName.Department.address,
                id: EmsObjectName.Department.address,
                filterable: true,
                filter: true,
                flex: 1,
                editor: {
                    xtype: 'textfield',
                    maxLength: 255,
                    enforceMaxLength: 255,
                    allowBlank: false
                }
            }
        ]);
    }
});


