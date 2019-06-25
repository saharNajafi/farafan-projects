Ext.define('Ems.view.office.Setting.AutoCompleteMOCEngineEnhancement', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.autoCompleteMOCEngineEnhancement',

    requires: [
        'Ems.store.OfficeSettingMOCEngineEnhancementStore',
        'Ems.model.OfficeSettingMOCEngineEnhancementModel'
    ],

    store: {type: 'officesettingmocengineenhancementstore'},

    multiSelect: false,

    valueField: 'acId',

    displayField: 'acName',

    listWidth: 290,

    emptyText: 'انتخاب کنید...'
});