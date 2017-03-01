/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/31/12
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.systemProfileList.Grid', {

    extend: 'Gam.grid.RowEditingCrud',

    alias: 'widget.systemprofilegrid',

    stateId: 'wSystemProfileGrid',


    title: 'تنظیمات سیستم',

    multiSelect: false,

    requires: [ 'Ems.store.SystemProfileListStore'  ],

    store: {type: 'systemprofilestore'},

//    actions: ['gam.add'],

//    contextMenu: [ 'gam.add' ],

    tbar: [
        {
            // icon:'resources/themes/images/default/shared/cacheRefresh.png',
            iconCls: 'girdAction-cacheRefresh-icon',
            text: 'بروزرسانی', action: 'updating',
            stateful: true,
            stateId: this.stateId + 'Update'
        }
    ],

    actionColumnItems: ['edit', {
        getClass: function (value, metaData, record, rowIndex, colIndex, store) {
            var result = record.get(EmsObjectName.systemProfileList.childCount);
            if (result == "0") {
                return 'grid-Delete-action-icon';
            }
        },
        tooltip: 'حذف', action: 'deleteing',
        stateful: true,
        stateId: this.stateId + 'Delete'
    }],
    initComponent: function () {
        this.columns = this.getItemsGridForm();
        this.callParent(arguments);
    }, getItemsGridForm: function () {
        return([
            {
                dataIndex: EmsObjectName.systemProfileList.caption,
                id: EmsObjectName.systemProfileList.caption,
                text: 'عنوان',
                width: 150,
                fieldLabel: true,
                sortable: true,
                filter: true
            },
            {
                dataIndex: EmsObjectName.systemProfileList.keyName,
                id: EmsObjectName.systemProfileList.keyName,
                text: 'کلید',
                width: 300,
                align: 'left',
                sortable: true,
                fieldLabel: true,
                filter: true
            },
            {
                dataIndex: EmsObjectName.systemProfileList.parentKeyName,
                id: EmsObjectName.systemProfileList.parentKeyName,
                text: 'کلید مافوق',
                align: 'left',
                width: 150,
                sortable: true
            },
            {
                dataIndex: EmsObjectName.systemProfileList.value,
                id: EmsObjectName.systemProfileList.value,
                text: 'مقدار',
                align: 'left',
                sortable: false,
                flex: 1,
                editor: {
                    xtype: 'textfield',
                    style: 'direction: ltr;'
                }
            }
        ]);
    }
});

