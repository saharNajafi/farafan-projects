/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/13/12
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.office.userList.grid', {
    extend: 'Gam.grid.Crud',

    alias: 'widget.officeuserlistgrid',

    multiSelect: false,
    requires: ['Ems.store.OfficeUserListStore' ],
    autoHeight: true,
    id: 'grdOfficesUserListGrid',

    store: {type: 'officeuserliststore'},

    initComponent: function () {

        this.columns = [
            {
                xtype: 'gridcolumn',
                text: 'نام',
                width: 150,
                dataIndex: EmsObjectName.suerList.firstName
            },
            {
                xtype: 'gridcolumn',
                text: 'نام خانوادگی ',
                width: 150,
                dataIndex: EmsObjectName.suerList.lastName
            },
            {
                xtype: 'gridcolumn',
                text: 'کد ملی',
                width: 150,
                dataIndex: EmsObjectName.suerList.nid
            },
            {
                xtype: 'gridcolumn',
                text: 'شناسه کاربری',
                flex: 1,
                dataIndex: EmsObjectName.suerList.username
            }
        ];

        this.callParent(arguments);
    }


});

