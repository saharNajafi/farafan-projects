/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/8/12
 * Time: 11:10 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.controller.BizLogController', {
    extend: 'Gam.app.controller.LocalDialogBasedGrid',

    statics: {
        statefulComponents: [
            'wBizlogGrid'
        ]
    },
    id: 'IDBizLogController',

    ns: 'extJsController/bizlog',

    views: [ 'bizLog.Grid' ],

    initViewType: 'bizloggrid',

    constructor: function (config) {
        this.callParent(arguments);
    },
    init: function () { //
        this.control({
            "[action=exportExcel]": {
                click: function (btn) {
                    this.doExportExcel(btn);
                }
            }
        });
        this.callParent(arguments);
    },
    doExportExcel: function (btn) {
        var grid = btn.up('toolbar').up('grid');
        var store = grid.getStore();

        var listName = store.readParams.listName;
        var fields = store.readParams.fields;
        var filters = '{}';
        try {
            filters = store.proxy.encodeFilters(store.filters.items);
        } catch (e) {
        }
        var sorter = '{}';
        try {
            sorter = store.proxy.encodeSorters(store.sorters.items);
        } catch (e) {
        }

        var config = {
            url: 'extJsController/exportList/query',
            params: {
                listName: listName,
                fields: fields,
                filter: filters,
                page: 1,
                start: 0,
                limit: 1000,
                sort: sorter
            }
        }
        Gam.util.PopupWindow.open(config);
    },
    doVerify: function (grid, rowIndex, colIndex) {

        var store = grid.getStore();
        var record = store.getAt(rowIndex);
        var me = this;

        Ext.Ajax.request({
            url: me.ns + '/checkBusinessLogStatus',
            jsonData: {ids: record.get('id')},
            success: function (response) {
                var rec = Ext.decode(response.responseText);
                if (rec.success) {
                    if (rec.verified === true) {
                        record.set('verify', true);
                    } else if (rec.verified === false) {
                        record.set('verify', false);
                    }
                    record.commit();
                }
            },
            failure: function () {
                Tools.errorFailure();
            }
        });
    }

});
