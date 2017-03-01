Ext.define('Ems.view.user.Grid', {
    extend: 'Gam.grid.Crud',

    alias: 'widget.usergrid',

    requires: [
        'Ems.store.UserStore',
        'Ems.view.user.Dialog',
        'Ems.view.user.NewEdit.cmbOrganizationalStatus'
    ],

    stateId: 'wUserGrid',

    id: 'grdUsers',

    title: 'مدیریت کاربران',

    multiSelect: false,


    store: {type: 'userstore'},
    actions: [
        'gam.add->userdialog'
//        'gam.delete'
    ],
    actionColumnItems: [
        {
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                // if(record.get('userName')!==Tools.trim('gaasadmin'))
                return 'grid-edit-action-icon';
            },
            tooltip: 'ویرایش',
            action: 'editing',
            stateful: true,
            stateId: this.stateId + 'Editing'
        },
        {
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                return 'grid-view-action-icon';
            },
            tooltip: 'مشاهده', action: 'viewing',
            stateful: true,
            stateId: this.stateId + 'Viewing'
        }
        //  'edit->userdialog',
        // 'view->userdialog'
        ,
        {
            icon: 'resources/themes/images/TokenIcon/management.gif',
            tooltip: 'مدیریت توکن', action: 'tokenManagement',
            stateful: true,
            stateId: this.stateId + 'TokenManagement'

        }
    ], tbar: [
              'gam.add'
              , {
                  iconCls: 'girdAction-exportExcel-icon',
                  text: 'خروجی اکسل',
                  action: 'exportExcel',
                  stateful: true,
                  stateId: this.stateId + 'ExportExcel'
              }
          ]

    //,selType : 'rowmodel'
    , initComponent: function () {
        this.columns = this.getColumnUserGrid();
        this.callParent(arguments);
    }, getColumnUserGrid: function () {
        return([
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'نام',
                dataIndex: EmsObjectName.userForm.firstName,
                id: EmsObjectName.userForm.g_firstName,
                filterable: true,
                filter: true
            },
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'نام خانوادگی',
                dataIndex: EmsObjectName.userForm.lastName,
                id: EmsObjectName.userForm.g_lastName,
                filterable: true,
                filter: true

            },
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'شماره ملی',
                dataIndex: EmsObjectName.userForm.nationalCode,
                id: EmsObjectName.userForm.g_nationalCode,
                filterable: true,
                filter: true

            },
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'نام کاربری',
                dataIndex: EmsObjectName.userForm.userName,
                id: EmsObjectName.userForm.g_userName,
                filterable: true,
                filter: true
            },
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'جایگاه سازمانی',
                dataIndex: EmsObjectName.userForm.departmentName,
                id: EmsObjectName.userForm.g_departmentName,
                filterable: true,
                name: EmsObjectName.userForm.departmentId,
                hiddenName: EmsObjectName.userForm.departmentId,
                filter: true
            },
//            {
//                xtype: 'gridcolumn',
//                width: 150,
//                text: 'استان',
//                dataIndex: EmsObjectName.userForm.provinceName,
//                id: EmsObjectName.userForm.provinceName,
//                filterable: true,
//                name: EmsObjectName.userForm.provinceName,
//                hiddenName: EmsObjectName.userForm.provinceName,
//                filter: true
//            },
            {
                xtype: 'gam.datecolumn',
                width: 200,
                text: 'آخرین ورود',
                dataIndex: EmsObjectName.userForm.lastLoginDate,
                id: EmsObjectName.userForm.g_lastLoginDate,
                format: Ext.Date.defaultDateTimeFormat,

                filterable: true,
                filter: {
                    xtype: 'container',
                    layout: {
                        type: 'hbox',
                        align: 'middle'
                    },
                    defaults: {
                        labelWidth: 10,
                        xtype: 'datefield',
                        flex: 1
                    },
                    items: [
                        {
                            fieldLabel: 'از',
                            name: 'fromDate'
                        },
                        {
                            fieldLabel: 'تا',
                            name: 'toDate'
                        }
                    ]
                }
            },
            this.OnActiveInActiveUserActionColumn()
        ]);
    },

    OnActiveInActiveUserActionColumn: function () {
        return({
            xtype: 'gam.actioncolumn',
            width: 100,

            text: 'وضعیت کاربر',

            dataIndex: EmsObjectName.userForm.userActive,
            id: EmsObjectName.userForm.userActive,
            items: [
                {
                    //icon:'resources/themes/images/user/Active.png',
                    tooltip: 'فعال',
                    action: 'active',
                    stateful: true,
                    stateId: this.stateId + 'ActiveInactive',
                    getClass: function (value, metadata, record) {
                        var userActive = record.get(EmsObjectName.userForm.userActive)

                        return (userActive === 'T' && (Tools.trim(record.get(EmsObjectName.userForm.userName)) != 'gaasadmin')) ? 'girdAction-userActive-icon' : 'x-hide-display';
                    }
                },
                {
                    // icon:'resources/themes/images/user/InActive.png',
                    tooltip: 'غیر فعال',
                    action: 'inactive',
                    stateful: true,
                    stateId: this.stateId + 'ActiveInactive',
                    getClass: function (value, metadata, record) {
                        var userActive = record.get(EmsObjectName.userForm.userActive);
                        return(userActive === 'F' && (Tools.trim(record.get(EmsObjectName.userForm.userName)) != 'gaasadmin') ) ? 'girdAction-userInActive-icon' : 'x-hide-display';
                    }
                }
            ]
        });
    }


});
