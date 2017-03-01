/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/8/12
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.BizStore', {
    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.bizstore',

    listName: 'bizLogList',

    model: 'Ems.model.BizModel',

    actionName: 'bizLogList',

    id: 'bizLogList',

    baseUrl: 'extJsController/bizlog'
});
