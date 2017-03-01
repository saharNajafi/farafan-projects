/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/18/12
 * Time: 5:21 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.user.parent.Grid', {
    extend: 'Gam.grid.Crud',

    requires: [
        /*'Ems.store.UserStore',*/'Ems.view.user.Dialog',
        'Ems.view.user.NewEdit.cmbOrganizationalStatus'
    ],

    multiSelect: false

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
                filterable: true,
                filter: true
            },
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'نام خانوادگی',
                dataIndex: EmsObjectName.userForm.lastName,
                filterable: true,
                filter: true

            },
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'شماره ملی',
                dataIndex: EmsObjectName.userForm.nationalCode,
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

    getActiveInActiveUserActionColumn: function () {/**/
    },

    getUserName: function () {/**/
    },

    getLastInput: function () {/**/
    }

});
