/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/31/12
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.SystemProfileListStore', {
    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.systemprofilestore',

    model: 'Ems.model.SystemProfileListModel',

    baseUrl: 'extJsController/systemprofile',

    listName: 'systemProfileList'


});
