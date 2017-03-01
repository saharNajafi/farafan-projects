/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/14/12
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.office.OfficeName.NOCRAutoComplete', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.nocrautocomplete',

    requires: [
        'Ems.store.NOCRAutoCompleteStore', 'Ems.model.AutocompleteSimpleModel' ],

    store: {type: 'nocrautocompletestore'},

    id: 'nocrAutocomplet',
    // setValue: Ext.emptyFn  ,
    //valueField:'acId',

    //listWidth: 290   ,
    //width:310,
    emptyText: 'انتخاب کنید...'

});

