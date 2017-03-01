/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.CmsErrorEvaluateListStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.cmserrorevaluateliststore',

    id: 'idCmsErrorEvaluateListStore',

    listName: 'cmserrorevaluateList',

    model: 'Ems.model.CmsErrorEvaluateListModel',

    baseUrl: 'extJsController/cmserrorevaluatelist'

});

