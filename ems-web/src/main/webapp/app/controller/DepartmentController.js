/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/3/12
 * Time: 2:22 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.controller.DepartmentController', {
    extend: 'Gam.app.controller.RowEditorBasedGrid',

    statics: {
        statefulComponents: [
            'wDepartmentGrid',
            'wDepartmentGridAdd',
            'wDepartmentGridDelete',
            'wDepartmentGridEdit'
        ]
    },

    id: 'iDepartmentController',

    requires: [
        'Ems.store.DepartmentStore',
        'Ems.view.department.parentDNAutocomplete',
        'Ems.view.department.locationAutocomplete',
        'Ems.view.department.SendTypeCombobox',
        'Ems.controller.util.ExcelExporter'
        
    ],


    ns: 'extJsController/department',

    views: [
        'department.Grid'
    ],

    refs: [
        {
            ref: 'sendtypecombobox',
            selector: 'sendtypecombobox'
        }
    ],

    initViewType: 'departmentgrid',

    constructor: function (config) {
        this.callParent(arguments);
        this.excelExporter = new Ems.controller.util.ExcelExporter();
    },

    init: function () {
        this.callParent(arguments);
        this.control({
            "[action=exportExcel]": {
                click: function (btn) {
                    var grid = btn.up('toolbar').up('grid');
                    var store = grid.getStore();
                    var fields = store.readParams.fields;
                    fields = Ext.decode(fields);
                    var finalFields = [];
                    for (var i = 0; i < fields.length; i++) {
                        if ((fields[i] !== 'parentId') && (fields[i] !== 'sendType') &&
                            (fields[i] !== 'sendTypeId') && (fields[i] !== 'locId') &&
                            (fields[i] !== 'clientId')) {
                            finalFields.push(fields[i]);
                        }
                    }

                    this.excelExporter.doExportExcel(btn, {fields: Ext.encode(finalFields)});

                }, scope:this
            }
        });

    }

});
