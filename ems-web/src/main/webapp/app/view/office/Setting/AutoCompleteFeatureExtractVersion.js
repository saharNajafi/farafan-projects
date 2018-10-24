/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/14/12
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.office.Setting.AutoCompleteFeatureExtractVersion', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.autoCompleteFeatureExtractVersion',

    requires: [
        'Ems.store.OfficeSettingFeatureExtractVersionStore',
        'Ems.model.OfficeSettingFeatureExtractVersionModel'
    ],

    store: {type: 'officesettingfeatureextractversionstore'},

    multiSelect: false,

    valueField: 'acId',

    displayField: 'acName',

    forceSelection: false,

    listWidth: 290,

    emptyText: 'انتخاب کنید...'
});

