/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/7/12
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.blackList.Grid', {
    extend: 'Gam.grid.RowEditingCrud',

    alias: 'widget.blacklistgrid',

    stateId: 'wBlackListGrid',

    title: 'مدیریت فهرست ممنوعه',

    requires: [
        'Ems.store.BlackListStore'
    ],

    store: {type: 'blackliststore'},

    actions: ['gam.add', 'gam.delete' ],
    actionColumnItems: [  'edit'  ],
    contextMenu: [ 'gam.delete' ],
    tbar: ['gam.add', 'gam.delete' ],

    initComponent: function () {
        this.columns = this.getItemsBlackListGrid();
        this.callParent(arguments);
    }, getItemsBlackListGrid: function () {
        return([
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'نام',
                dataIndex: EmsObjectName.blackList.firstName,
                id: EmsObjectName.blackList.firstName,
                filterable: true,
                filter: true,
                editor: {
                    allowBlank: false,
                    xtype: 'textfield',
                    regex: Tools.regexFarsiAlpha(),
                    maxLength: 42,
                    enforceMaxLength: 42
                }
            },
            {
                xtype: 'gridcolumn',
                text: 'نام خانوادگی',
                width: 150,
                dataIndex: EmsObjectName.blackList.lastName,
                id: EmsObjectName.blackList.lastName,
                filterable: true,
                filter: true,
                editor: {
                    allowBlank: false,
                    xtype: 'textfield',
                    regex: Tools.regexFarsiAlpha(),
                    maxLength: 42,
                    enforceMaxLength: 42
                }
            },
            {
                xtype: 'gridcolumn',
                text: 'کد ملی',
                width: 150,
                dataIndex: EmsObjectName.blackList.nid,
                id: EmsObjectName.blackList.nid,
                filterable: true,
                filter: true,
                editor: {
                    allowBlank: false,
                    xtype: 'textfield',
                    //regex: /^\d{10}$/i,
                    // maskRe: /\d/i,
                    // regexText: "کد ملی 10 رقمی را وارد کنید",
                    vtype: 'numeric',
                    maxLength: 10,
                    enforceMaxLength: 10

                }
            }
        ]);
    }
});
