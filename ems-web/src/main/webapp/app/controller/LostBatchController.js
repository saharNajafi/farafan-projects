/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/8/12
 * Time: 11:10 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.controller.LostBatchController', {
    extend: 'Gam.app.controller.LocalDialogBasedGrid',

    statics: {
//        statefulComponents: [
//            'wBizlogGrid'
//        ]
    },
    id: 'IDLostBatchController',

    ns: 'extJsController/lostbatch',

    views: [ 'lostBatch.Grid' ],

    initViewType: 'lostbatchgrid',

    constructor: function (config) {
        this.callParent(arguments);
    },
    init: function () {
    	
        this.callParent(arguments);
    },
    doConfirmlostbatch: function (grid, rowIndex) {


    	 var store = grid.getStore(),
         record = store.getAt(rowIndex).data,
         id = record.id,
         me = this;

        Ext.Ajax.request({

            url: me.ns + '/doConfirmLostBatch', jsonData: {
            	batchId: id
            }, success: function (resp) {
                var data = Ext.decode(resp.responseText);
                if (data.success) {
                	 grid.getStore().load();

                }
               
            }, failure: function (resp) {
                Tools.errorFailure();
            }
        });
    },
});
