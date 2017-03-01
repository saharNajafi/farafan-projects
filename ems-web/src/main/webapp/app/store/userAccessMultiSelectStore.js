/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/9/12
 * Time: 10:36 AM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.store.userAccessMultiSelectStore', {
    extend: 'Ext.data.ArrayStore',
    model: 'Ems.model.userMultiSelectModel',

    alias: 'store.useraccessmultiselectstore'
});
