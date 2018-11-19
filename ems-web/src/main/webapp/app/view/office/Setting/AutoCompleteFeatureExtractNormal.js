/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/14/12
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.office.Setting.AutoCompleteFeatureExtractNormal', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.autoCompleteFeatureExtractNormal',

    requires: [
        'Ems.store.OfficeSettingFeatureExtractNormalStore',
        'Ems.model.OfficeSettingFeatureExtractNormalModel'
    ],

    store: {type: 'officesettingfeatureextractnormalstore'},

    multiSelect: false,

    // forceSelection: false,

    valueField: 'acId',

    displayField: 'acName',

    listWidth: 290,

    emptyText: 'انتخاب کنید...'
});
