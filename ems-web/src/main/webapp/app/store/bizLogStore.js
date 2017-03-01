/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/8/12
 * Time: 10:22 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.bizLogStore', {
    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.bizlogstore',

    listName: 'bizLogList',

    model: 'Ems.model.BizModel',

    actionName: 'bizLogList',

    id: 'bizLogList',

    baseUrl: 'extJsController/bizlog'
});

