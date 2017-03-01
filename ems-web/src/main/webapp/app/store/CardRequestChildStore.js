/**
 * Created by moghaddam on 6/25/14.
 */
Ext.define('Ems.store.CardRequestChildStore', {
    extend: 'Ext.data.JsonStore',

    alias: 'store.cardrequestchildstore',

    id: 'idCardRequestChildStore',

    listName: 'emsCardRequestChild',

    model: 'Ems.model.CitizenInfoChildModel',

    storeId: 'childrenStore'
});
