//khodayar
	Ext.define('Ems.controller.TokenRequestListController', {
		   extend: 'Gam.app.controller.LocalDialogBasedGrid',
	    ns:'extJsController/token',
	    statics: {
	        statefulComponents: [
	            'wTokenRequestListGrid',
	        ]
	    },
	    views: ['tokenRequestList.Grid'],
	    initViewType : 'tokengrid',
	    
	    stores: ['TokenRequestListStore'],
	
	    refs: [
	           {
	               ref: 'tokengrid',
	               selector: 'tokengrid'
	           }
	       ],
	    constructor: function (config) {
	       this.callParent(arguments);
	   }, 
	    init: function () {
	    	var me = this;
	        me.callParent(arguments);
	    },
	    doRejectRequest: function (grid, rowIndex) {
	        var me = this,
	            store = grid.getStore();

	        var msg = " آیا از عدم تایید کلید  اطمینان دارید ";

	        var fn = function () {
	            me.deleteRecord(store, rowIndex);
	        };

	        Tools.messageBoxConfirm(msg, fn);
	    },
	    deleteRecord: function (store, rowIndex) {
	        var me = this;
	        record = store.getAt(rowIndex),
            rowID = record.get(EmsObjectName.tokenRequest.id);
	        
	        Ext.Ajax.request({
	            url: me.ns + '/reject',
	            jsonData: {
	                ids: rowID
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
	    doApproveRequest: function (grid, rowIndex) {
	        var me = this;
	        store = grid.getStore(),
	        record = store.getAt(rowIndex),
            rowID = record.get(EmsObjectName.tokenRequest.id);
	        
	        Ext.Ajax.request({
	            url: me.ns + '/approve',
	            jsonData: {
	                ids: rowID
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
	    doDeliverToken: function (grid, rowIndex) {
	        var me = this;
	        store = grid.getStore(),
	        record = store.getAt(rowIndex),
            rowID = record.get(EmsObjectName.tokenRequest.id);
	        
	        Ext.Ajax.request({
	            url: me.ns + '/deliver',
	            jsonData: {
	                ids: rowID
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
	    doActivateRequest: function (grid, rowIndex) {
	        var me = this;
	        store = grid.getStore(),
	        record = store.getAt(rowIndex),
            rowID = record.get(EmsObjectName.tokenRequest.id);
	        
	        Ext.Ajax.request({
	            url: me.ns + '/activate',
	            jsonData: {
	                ids: rowID
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
	    }
	   });
	
	
