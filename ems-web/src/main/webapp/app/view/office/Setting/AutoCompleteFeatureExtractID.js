/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/14/12
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.office.Setting.AutoCompleteFeatureExtractID', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.autoCompleteFeatureExtractID',

    requires: [
        'Ems.store.OfficeSettingFeatureExtractIDStore',
        'Ems.model.OfficeSettingFeatureExtractIDModel'
    ],

    store: {type: 'officesettingfeatureextractidstore'},

    multiSelect: false,

    forceSelection: false,

    valueField: 'acId',

    displayField: 'acName',

    listWidth: 290,

    hiddenName: EmsObjectName.officeNewEdit.oficRatingId,

    emptyText: 'انتخاب کنید...'
});
