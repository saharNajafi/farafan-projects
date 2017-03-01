Ext.define('Ems.controller.DispatchController', {
    extend: 'Gam.app.controller.LocalDialogBasedGrid',

    ns: 'extJsController/dispatch',

    statics: {
        statefulComponents: [
            'wMainDispatchGrid',
            'wDetailDispatchGrid',

            'wDispatchReceived',
            'wDispatchNotReceived',
            'wDispatchBackToInitialState',
            'wDispatchLost',
            'wDispatchFound',
            'wDispatchUndoSent',
            'wDispatchSent',
            'wDispatchSetBoxDetail',
            'wDispatch',
            'wDispatch',
            'wDispatch'
        ]
    },

    models: ['DispatchModel'],

    views: [ 'dispatch.Grid'],

    requires: ['Ems.view.dispatch.statusBatch.Windows'],

    initViewType: 'dispatchgrid',

    refs: [
        {
            ref: 'dispatchStatusBatchWindows',
            selector: 'dispatchstatusbatchwindows'
        }
    ],

    init: function () {
        var eventConfig = {};

        eventConfig['dispatchstatusbatchgrid actioncolumn'] = { actionclick: this.onActionClick };

        this.control(eventConfig, { });

        this.callParent(arguments);
    },

    //requires:[ 'Ems.store.dispatchSendReceiveStore'  ],

    constructor: function (config) {
        /*        this.control({
         "[itemId=idRecipt]":{
         click:function(){alert('okkkk lost');}
         }
         });*/
        this.callParent(arguments);
    }, doReceived: function (grid, rowIndex) {
        this.dispatchGridActionColumn(this, grid, rowIndex, Tools.trim('itemReceived'));
    }, doNotReceived: function (grid, rowIndex) {
        this.dispatchGridActionColumn(this, grid, rowIndex, Tools.trim('itemNotReceived'));
    }, doBackToInitialState: function (grid, rowIndex) {
        this.dispatchGridActionColumn(this, grid, rowIndex, Tools.trim('backToInitialState'));
    }, doSetBoxDetail: function (grid, rowIndex) {
        var record = grid.getStore().getAt(rowIndex);

        //Ext.create('Ems.view.dispatch.statusBatch.Windows', {parId: record.get('id') }).show();
        var win = Ext.create('Ems.view.dispatch.statusBatch.Windows');
        Tools.MaskUnMask(win);
        var detailGrid = win.down('grid');
        if (detailGrid != null) {
            var detailStore = detailGrid.getStore();
            //detailStore.load({params:{parId:record.get('id')}});
            if (detailStore.readParams == null)
                detailStore.readParams = {parId: record.get('id')};
            else
                detailStore.readParams.parId = record.get('id');
            win.show();
        } else {
            Tools.errorMessageClient(Ems.ErrorCode.client.EMS_C_003);
        }
    }, doLost: function (grid, rowIndex) {
        this.dispatchGridActionColumn(this, grid, rowIndex, Tools.trim('itemLost'));
    }, doFound: function (grid, rowIndex) {
        this.dispatchGridActionColumn(this, grid, rowIndex, Tools.trim('itemFound'));
    }, doUndoSend: function (grid, rowIndex) {
        this.dispatchGridActionColumn(this, grid, rowIndex, Tools.trim('undoSend'));
    }, doSend: function (grid, rowIndex) {
        this.dispatchGridActionColumn(this, grid, rowIndex, Tools.trim('itemSent'));
    }

//    ,ChoiceStatus:function(action){
//        var statusBox=null;
//
//        switch(action)
//        {
//            case 'Received':
//                statusBox=1;
//                break;
//
//            case 'NotReceived':
//                statusBox=2;
//                break;
//
//            case 'backToInitialState':
//                statusBox=0;
//                break;
//
//            case 'Found':
//                statusBox=1;
//                break;
//
//
//            case 'undoSend':
//                statusBox=1;
//                break;
//
//            case 'Send':
//                statusBox=3;
//                break;
//
//            case 'Lost':
//                statusBox=4;
//                break;
//
//            default:
//
//        }
//        return statusBox;
//    }

    , dispatchGridActionColumn: function (view, grid, rowIndex, action) {

        var store = grid.getStore(),
            record = store.getAt(rowIndex),
            ids = record.get(EmsObjectName.dispatchGrid.id),
            listName = store.listName,
            win = Ext.getCmp('idDispatchStatusBatchWindows');
        var jd = {
            ids: ids,
            detailIds: ''
        };
        if (listName == 'detailDispatchList') {
            jd = {
                ids: '',
                detailIds: ids
            };
        }

        Gam.Msg.showWaitMsg();
        Ext.Ajax.request({
            url: view.ns + '/' + action,
            jsonData: jd,
            success: function (response) {
                Gam.Msg.hideWaitMsg();
                try {
                    var obj = Ext.decode(response.responseText);
                    if (obj.success) {
                        store.load();
                    }
                    else {
                        Tools.errorMessageServer(obj.messageInfo)
                    }
                } catch (e) {
                    alert(e.message);
                }
                if (listName === Tools.trim('detailDispatchList')) {
                    Ext.getBody().mask();
                    win.on('close', function () {
                        Ext.getBody().unmask();
                    });
                }
            }
        });

    }



});
