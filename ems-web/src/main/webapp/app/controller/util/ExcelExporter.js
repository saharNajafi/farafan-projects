/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
Ext.define('Ems.controller.util.ExcelExporter', {
    constructor: function (config) {

    },

    doExportExcel: function(btn, config){
        config = config || {};
        var grid = btn.up('toolbar').up('grid');
        var store = grid.getStore();

        var listName = store.readParams.listName;
        var fields = config.fields ? config.fields : store.readParams.fields;
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

        var requestConfig = {
            url: 'extJsController/exportList/query',
            params: {
                listName: listName,
                fields: fields,
                filter: filters,
                page: 1,
                start: 0,
                limit: 10000,
                sort: sorter
            }
        };
        Gam.util.PopupWindow.open(requestConfig);
    }
});