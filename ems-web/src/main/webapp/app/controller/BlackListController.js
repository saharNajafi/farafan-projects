/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/3/12
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.controller.BlackListController', {
    extend: 'Gam.app.controller.RowEditorBasedGrid',
    id: 'idBlackListController',

    statics: {
        statefulComponents: [
            'wBlackListGrid',
            'wBlackListGridEdit',
            'wBlackListGridAdd',
            'wBlackListGridDelete'
        ]
    },

    ns: 'extJsController/blacklist',

    views: ['blackList.Grid'],

    initViewType: 'blacklistgrid',

    constructor: function () {
        this.callParent(arguments);
    }

});
