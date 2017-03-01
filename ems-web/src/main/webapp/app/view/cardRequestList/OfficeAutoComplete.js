/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/14/12
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.cardRequestList.OfficeAutoComplete', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.officeautocomplete',

    requires: [
        'Ems.store.OfficeAutoCompleteStore', 'Ems.model.AutocompleteSimpleModel' ],

    store: {type: 'officeautocompletestore'},

  
    emptyText: 'انتخاب کنید...',
    displayField: 'acName',
    valueField: 'acId',
    acType : 'sdfsdf',
    allowBlank: true
	
});

