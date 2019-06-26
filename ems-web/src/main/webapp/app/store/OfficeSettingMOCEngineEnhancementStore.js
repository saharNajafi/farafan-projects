Ext.define('Ems.store.OfficeSettingMOCEngineEnhancementStore', {

    extend: 'Gam.data.store.Autocomplete',
    alias: 'store.officesettingmocengineenhancementstore',

    require: ['Ems.model.AutocompleteSimpleModel'],
    model: 'Ems.model.AutocompleteSimpleModel',

    autocompleteName: 'MOCEngineEnhancementAuto'
});