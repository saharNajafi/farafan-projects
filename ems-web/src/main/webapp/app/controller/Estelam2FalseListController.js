/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.controller.Estelam2FalseListController', {
    extend: 'Gam.app.controller.LocalDialogBasedGrid',

    ns: 'extJsController/estelam2falselist',

    statics: {
        statefulComponents: [
            'wEstelam2FalseListGrid',
        ]
    },

    views: ['estelam2falseList.Grid'],

    refs: [
           {
               ref: 'estelam2LogGrid',
               selectors: 'estelam2loggrid'
           }
       ],

	    initViewType : 'estelam2falselistgrid',

    stores: ['Estelam2FalseListStore'],

    constructor: function (config) {

        this.callParent(arguments);
    },
    
    doEstelam2Log: function (grid, rowIndex) {

        var store = grid.getStore(),
            record = store.getAt(rowIndex),
            id = record.get('id');

        var win = Ext.create('Ems.view.estelam2falseList.estelam2Log.Window',
            {
                autoScroll: true,
            });
        Tools.MaskUnMask(win);
        var logListGrid = win.down('grid');
        var me = this;

        if (id > 0) {
         
        	var logListStore = logListGrid.getStore();
            if (logListStore.readParams == null) {
                st.readParams({estelam2falseId: id});
            } else {
                logListStore.readParams.estelam2falseId = id;
            }
            
            win.show();

        }

    },
    
});


