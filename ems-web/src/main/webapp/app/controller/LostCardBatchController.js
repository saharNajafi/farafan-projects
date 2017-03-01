/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/8/12
 * Time: 11:10 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.controller.lostCardBatchController', {
    extend: 'Gam.app.controller.LocalDialogBasedGrid',

    statics: {
//        statefulComponents: [
//            'wBizlogGrid'
//        ]
    },
    id: 'IDLostCardBatchController',

    ns: 'extJsController/lostBatchList',

    views: [ 'lostCardBatch.Grid' ],

    initViewType: 'lostcardbatch',

    constructor: function (config) {
        this.callParent(arguments);
    },
    init: function () {
        this.callParent(arguments);
    }
});
