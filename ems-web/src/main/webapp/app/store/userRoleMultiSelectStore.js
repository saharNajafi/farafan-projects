/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/9/12
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.store.userRoleMultiSelectStore', {
    extend: 'Ext.data.ArrayStore',
    model: 'Ems.model.userMultiSelectModel',

    alias: 'store.userrolemultiselectstore'
});
