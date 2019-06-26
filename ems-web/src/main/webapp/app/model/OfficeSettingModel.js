Ext.define('Ems.model.OfficeSettingModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        { name: 'featureExtractNormal', type: 'string' },
        { name: 'featureExtractCC', type: 'string' },
        { name: 'MOCEngineEnhancement', type: 'string' },
        { name: 'feiN', type: 'number' },
        { name: 'feiCC', type: 'number' },
        { name: 'feiISOCC', type: 'number' },
        { name: 'id', type: 'number' }
    ]
});