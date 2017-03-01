Ext.define('Ems.controller.WorkstationController', {
    extend: 'Gam.app.controller.RowEditorBasedGrid',

    statics: {
        statefulComponents: [
            'wWorkStationGrid',
            'wWorkStationGridUnAccept',
            'wWorkStationGridAccept',
            'wWorkStationGridEdit',
            'wWorkStationGridAdd',
            'wWorkStationGridDelete'
        ]
    },

    id: 'idworkstationController',

    ns: 'extJsController/workstation',

    views: [
        'workstation.Grid'
    ],

    initViewType: 'workstationgrid',

    requires: [
               'Ems.controller.util.ExcelExporter'
           ],
    
    refs: [
        {
            ref: 'workStationWindowsNewEdit', selector: 'workstationwindowsnewedit'
        },
        {
            ref: 'workstationGrid', selector: 'workstationform'
        }
    ],

    stores: ['WorkstationStore'],

    constructor: function (config) {
        this.callParent(arguments);
        this.excelExporter = new Ems.controller.util.ExcelExporter();
    },

    init: function () { //
        this.control({
            "[action=exportExcel]": {
                click: function (btn) {
                    var grid = btn.up('toolbar').up('grid');
                    var store = grid.getStore();
                    var fields = store.readParams.fields;
                    fields = Ext.decode(fields);
                    var finalFields = [];
                    for (var i = 0; i < fields.length; i++) {
                        if ((fields[i] !== 'id') && (fields[i] !== 'clientId') &&
                            (fields[i] !== 'enrollmentOfficeId')) {
                            finalFields.push(fields[i]);
                        }
                    }

                    this.excelExporter.doExportExcel(btn, {fields: Ext.encode(finalFields)});
                }
            }
        });
        this.callParent(arguments);
    },
    
    doAccept: function (grid, rowIndex) {

        this.AcceptUnAccept(grid, rowIndex, 'approveWorkstation');
    },
    doUnAccept: function (grid, rowIndex) {
        this.AcceptUnAccept(grid, rowIndex, 'rejectWorkstation');
    }, AcceptUnAccept: function (grid, rowIndex, action) {

        var store = grid.getStore(),
            record = store.getAt(rowIndex),
            ids = record.get(EmsObjectName.dispatchGrid.id);

        Gam.Msg.showWaitMsg();
        Ext.Ajax.request({
            url: 'extJsController/workstation/' + action,
            params: {
                ids: ids
            },
            success: function (response) {
                try {
                    var obj = Ext.decode(response.responseText);
                    Gam.Msg.hideWaitMsg();
                    if (obj.success) {
                        store.load();
                        //grid.getView().refresh();
//                           var result=(action ==='approveWorkstation') ? 'A' : 'R';
//                            record.set(EmsObjectName.workstation.checkUnCheck , result);
//                            record.commit();
                    } else {
                        Tools.errorMessageServer(obj.messageInfo)
                    }
                } catch (e) {
                    Gam.Msg.hideWaitMsg();
                    alert(e.message);
                }

            }
        });
    }



});
