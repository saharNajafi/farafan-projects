/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/8/12
 * Time: 11:10 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.controller.LostCardController', {
    extend: 'Gam.app.controller.LocalDialogBasedGrid',

    statics: {
//        statefulComponents: [
//            'wBizlogGrid'
//        ]
    },
    id: 'IDLostCardController',

    ns: 'extJsController/lostcard',

    views: [ 'lostCard.Grid' ],

    initViewType: 'lostcardgrid',

    constructor: function (config) {
        this.callParent(arguments);
    },
    init: function () {
        this.callParent(arguments);
    },
    doConfirmlostcard: function (grid, rowIndex) {


      var store = grid.getStore(),
          record = store.getAt(rowIndex).data,
          id = record.id,
          me = this;

      Ext.Ajax.request({

          url: me.ns + '/doConfirmLostCard', jsonData: {
        	  cardId: id
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
