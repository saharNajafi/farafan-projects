Ext.define('Ems.store.OfficeSettingFeatureExtractorIdStore', {

    extend: 'Gam.data.store.Autocomplete',
    alias: 'store.officesettingfeatureextractoridstore',

    require: ['Ems.model.AutocompleteSimpleModel'],
    model: 'Ems.model.AutocompleteSimpleModel',

    autocompleteName: 'featureExtractorId'
});