Ext.define('Ems.view.office.Setting.AutoCompleteFeatureExtractorId', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.autoCompleteFeatureExtractorId',

    requires: [
        'Ems.store.OfficeSettingFeatureExtractorIdStore',
        'Ems.model.OfficeSettingFeatureExtractorIdModel'
    ],

    store: {type: 'officesettingfeatureextractoridstore'},

    multiSelect: false,

    valueField: 'acId',

    displayField: 'acName',

    listWidth: 290,

    emptyText: 'انتخاب کنید...'
});