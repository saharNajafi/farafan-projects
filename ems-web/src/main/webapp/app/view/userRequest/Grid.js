/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/18/12
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.userRequest.Grid', {
    extend: 'Ems.view.userRequest.parent.Grid',
    alias: 'widget.userrequestgrid',

    stateId: 'wUserRequestGrid',

    requires: [
        'Ems.store.UserRequestStore',
        'Ems.view.user.Dialog',
        'Ems.view.user.NewEdit.cmbOrganizationalStatus'
    ],

    initComponent: function () {
        this.columns = this.getColumnUserGrid();
        this.callParent(arguments);
    },

    multiSelect: false,


    store: {type: 'userrequeststore'},
    id: 'grdUserRequest',

    title: 'مدیریت کاربران درخواستی',

    actions: [ 'gam.delete' ],

    actionColumnItems: [

        {
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                return 'grid-view-action-icon';
            },
            tooltip: 'مشاهده', action: 'viewed',
            stateful: true,
            stateId: this.stateId + 'View'
        } ,
        {
            // icon: 'resources/themes/images/default/shared/forbidden.png',
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {     //REQUESTED
                var status = record.get(EmsObjectName.userForm.requestStatus);
                return (status != "REQUESTED") ? "" : "grid-edit-action-icon";
            },
            tooltip: 'ویرایش', action: 'editRequest',
            stateful: true,
            stateId: this.stateId + 'Edit'
        }
        ,
        //  'edit->userdialog',
        // 'view->userdialog'
        {
            //icon: 'resources/themes/images/default/shared/forbidden.png',
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {     //REQUESTED
                var status = record.get(EmsObjectName.userForm.requestStatus);
                return (status === "REQUESTED") ? "girdAction-forbidden-icon" : "x-grid-centerColumn-hidden";
            },
            tooltip: 'عدم تایید', action: 'notAccept',
            stateful: true,
            stateId: this.stateId + 'NotAccept'

        }
    ],

    contextMenu: [ 'gam.delete' ],

    tbar: ['gam.delete' ],

    getColumnUserGrid: function () {
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
            this.getUserName()
            ,
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'جایگاه سازمانی',
                dataIndex: EmsObjectName.userForm.departmentName,
                id: EmsObjectName.userForm.g_departmentName,
                filterable: true,
                // id: EmsObjectName.userForm.departmentId,
                //name: EmsObjectName.userForm.departmentId,
                filter: true
            }
            ,
            this.getLastInput()
            ,
            this.getActiveInActiveUserActionColumn()
        ]);
    },

    getActiveInActiveUserActionColumn: function () {
        return({
            xtype: 'gridcolumn',
            width: 100,
            text: 'وضعیت کاربر',

            dataIndex: EmsObjectName.userForm.requestStatus,
            id: EmsObjectName.userForm.g_requestStatus,
            renderer: function (value) {
                if (value && typeof value === 'string') {
                    var val = Tools.trim(value);
                    if (val === "REJECTED") {
                        return "رد شد";
                    } else if (val === "REQUESTED") {
                        return "منتظر تایید";
                    }
                    return null;
                }
            }
        });
    },

    getUserName: function () {
        return ({
            xtype: 'gridcolumn',
            hidden: true
        });
    },

    getLastInput: function () {
        return ({
            xtype: 'gridcolumn',
            hidden: true
        });
    }

});
