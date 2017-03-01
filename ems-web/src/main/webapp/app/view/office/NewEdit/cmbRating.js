/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/3/12
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.office.NewEdit.cmbRating', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.cmbrating',

    requires: [
        'Ems.store.OfficeRatingStore',
        'Ems.model.AutocompleteSimpleModel'
    ],

    store: {type: 'officeratingstore'},

    multiSelect: false,

    displayField: 'acName',

    valueField: 'acId',

    listWidth: 290,

    hiddenName: EmsObjectName.officeNewEdit.oficRatingId,

    emptyText: 'انتخاب کنید...'
});

