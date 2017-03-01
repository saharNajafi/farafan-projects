/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.GridCueMessageStore', {
    extend: 'Gam.data.store.grid.Grid',

    alias: 'store.GridCueMessageStore',

    id: 'idGridCueMessageStore',

    listName: 'CueMessageList',

    model: 'Ems.model.CueMessageListModel',

    baseUrl: 'extJsController/CueMessageList'

});

