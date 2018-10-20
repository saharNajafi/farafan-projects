/**
 * Created with Eclipse :D.
 * User: Dr Adldoost :D
 * Date: 19/10/15
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.store.OfficeSettingFeatureExtractIDStore', {
    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.officesettingfeatureextractidstore',

    require: [ 'Ems.model.OfficeSettingFeatureExtractIDModel' ],
    model: 'Ems.model.OfficeSettingFeatureExtractIDModel',

    listName: 'featureExtractIdsAC',
    baseUrl: 'extJsController/officeSetting'

});

