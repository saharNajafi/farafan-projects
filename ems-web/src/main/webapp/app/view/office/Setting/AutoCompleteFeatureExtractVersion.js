/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/14/12
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.office.OfficeName.AutoCompleteFeatureExtractVersion', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.officenamefeatureextractversion',

    requires: [
        'Ems.store.OfficeSettingFeatureExtractVersionStore', 'Ems.model.OfficeSettingFeatureExtractVersionModel' ],

    store: {type: 'officesettingfeatureextractversionstore'},

    id: 'officeSettingFeatureExtractVersionAutocomplet',

    emptyText: 'انتخاب کنید...'

});

