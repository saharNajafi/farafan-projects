/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/14/12
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.office.Setting.AutoCompleteFeatureExtractID', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.officenamefeatureextractid',

    requires: [
        'Ems.store.OfficeSettingFeatureExtractIDStore', 'Ems.model.OfficeSettingFeatureExtractIDModel' ],

    store: {type: 'officesettingfeatureextractidstore'},

    id: 'officeSettingFeatureExtractIDAutocomplet',
    // setValue: Ext.emptyFn  ,
    //valueField:'acId',

    //listWidth: 290   ,
    //width:310,
    emptyText: 'انتخاب کنید...'

});

