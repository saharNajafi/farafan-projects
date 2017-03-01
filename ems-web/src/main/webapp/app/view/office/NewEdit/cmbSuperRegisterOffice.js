/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/1/12
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.office.NewEdit.cmbSuperRegisterOffice', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.cmbSuperregisterofficeautocomplete',

    requires: [
        'Ems.store.OfficeSuperRegisterOfficeStore',
        'Ems.model.AutocompleteSimpleModel'
    ],

    store: {type: 'officesuperregisterofficesotre'},

    multiSelect: false,

    hiddenName: EmsObjectName.officeNewEdit.oficSuperRegisterOfficeId,

    displayField: 'acName',
    valueField: 'acId',

    listWidth: 290,

    emptyText: 'انتخاب کنید...'

});
