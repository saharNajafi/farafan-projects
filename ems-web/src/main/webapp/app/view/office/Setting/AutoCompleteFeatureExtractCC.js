/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/14/12
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.office.Setting.AutoCompleteFeatureExtractCC', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.autoCompleteFeatureExtractCC',

    requires: [
        'Ems.store.OfficeSettingFeatureExtractCCStore',
        'Ems.model.OfficeSettingFeatureExtractCCModel'
    ],

    store: {type: 'officesettingfeatureextractccstore'},

    multiSelect: false,

    valueField: 'acId',

    displayField: 'acName',

    listWidth: 290,

    emptyText: 'انتخاب کنید...'
});

