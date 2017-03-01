/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/31/12
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.controller.SystemProfileListController', {

    extend: 'Gam.app.controller.RowEditorBasedGrid',

    ns: 'extJsController/systemProfile',

    statics: {
        statefulComponents: [
            'wSystemProfileGrid',
            'wSystemProfileGridDelete',
            'wSystemProfileGridUpdate',
            'wSystemProfileGridAdd',
            'wSystemProfileGridEdit'
        ]
    },

    views: ['systemProfileList.Grid'],
    initViewType: 'systemprofilegrid',

    refs: [
        {
            ref: 'systemprofilegrid',
            selector: 'systemprofilegrid'
        }
    ],

    constructor: function () {
        this.callParent(arguments);
    },


    init: function () { //
        this.control({
            "[action=updating]": {
                click: function (btn) {
                    this.doUpdating();
                }
            }
        });
        this.callParent(arguments);
    },


    doDeleteing: function (grid, rowIndex) {
        var me = this,
            store = grid.getStore(),
            record = store.getAt(rowIndex),
            keyName = record.get(EmsObjectName.systemProfileList.keyName);

        var msg = " آیا از حذف کلید " + keyName + " اطمینان دارید "

        var fn = function () {
            me.deleteRecord(store, keyName);
        }

        Tools.messageBoxConfirm(msg, fn);

    },

    deleteRecord: function (store, keyName) {
        var me = this;
        Ext.Ajax.request({
            url: me.ns + '/delete',
            jsonData: {
                ids: keyName
            },
            success: function (response) {
                var rec = Ext.decode(response.responseText);
                if (rec.success) {
                    store.load();
                }
            },
            failure: function () {
                Tools.errorFailure();
            }
        });
    },

    doUpdating: function () {
        var me = this,
            grid = this.getSystemprofilegrid(),
            store = grid.getStore();


        Ext.Ajax.request({
            url: me.ns + '/reload',
            success: function (response) {
                var rec = Ext.decode(response.responseText);
                if (rec.success) {
                    store.load();
                }
            },
            failure: function () {
                Tools.errorFailure();
            }
        });

    }

});
