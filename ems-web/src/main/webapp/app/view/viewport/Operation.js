/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/10/12
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.viewport.Operation', {
    extend: 'Ext.panel.Panel',
    layout: 'fit',

    // stateId:'wOperationMenu',
    // stateful:false,

    alias: 'widget.operation',
    initComponent: function () {
        this.items = [this.getItemsOperation()];
        this.callParent(arguments);
    },


    getItemsOperation: function () {
        return({
            xtype: 'buttongroup',
            columns: 2,
            defaults: {
                scale: 'large',
                iconAlign: 'top'
            },
            items: [   
			        {
			        	xtype: 'splitbutton',
			        	action: 'Dispatch',
			        	text: 'توزيع',
			        	tooltip: 'توزيع' + '   < Ctrl+Shift+D >',
			        	iconCls: 'toolbar-sushi',
	                    scale: 'large',
	                    rowspan: 3,
	                    iconAlign: 'top',
	                    arrowAlign: 'bottom',
			        	id: 'OperationMenuDispatch',
			        	width: 90,
			        	menu: [
			        	       {
			        	    	   tooltip: 'مدیریت کارت های گم شده',
			        	    	   text: 'مدیریت کارت های گم شده',
			        	    	   action: 'LostCard',
			        	    	   icon: 'resources/themes/images/default/toolbar/card.png',
			        	    	   id: 'OperationLostCard'
			        	       },
			        	       {
			        	    	   tooltip: 'مدیریت دسته های گم شده',
			        	    	   text: 'مدیریت دسته های گم شده',
			        	    	   action: 'LostBatch',
			        	    	   icon: 'resources/themes/images/default/toolbar/batch.png',
			        	    	   id: 'OperationLostBatch'
			        	       }
			        	       ]
			        },
			        {
			        	action: 'BlackList',
			        	text: 'فهرست ممنوعه',
			        	tooltip: 'فهرست ممنوعه' + '   < Ctrl+Shift+B >',
			        	iconCls: 'toolbar-blacklist',
			        	id: 'OperationMenuBlackList'
			        }
			        ],
            listeners: {
                render: function (view) {

                    view.items.items[0].stateId = 'w' + view.items.items[0].id;
                    view.items.items[0].stateful = false;

                    view.items.items[1].stateId = 'w' + view.items.items[1].id;
                    view.items.items[1].stateful = false;

                    /*
                     if(view.stateful)
                     {
                     var stateProvider = Ext.create('Ems.state.Provider');
                     stateProvider.setDetail(view.items.items[0].stateId ,1);
                     stateProvider.setDetail(view.items.items[1].stateId ,1);

                     stateProvider.saveing();

                     }*/

                }
            }
        });
    }

});
