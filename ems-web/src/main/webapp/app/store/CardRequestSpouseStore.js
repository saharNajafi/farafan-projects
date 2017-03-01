/**
 * Created by moghaddam on 6/25/14.
 */
Ext.define('Ems.store.CardRequestSpouseStore', {
    extend: 'Ext.data.JsonStore',

    alias: 'store.cardrequestspousestore',

    id: 'idCardRequestSpouseStore',

    listName: 'emsCardRequestSpouse',

    model: 'Ems.model.CitizenInfoSpouseModel',

    storeId: 'spousesStore'
});
