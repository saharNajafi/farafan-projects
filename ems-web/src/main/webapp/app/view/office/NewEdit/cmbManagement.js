/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/1/12
 * Time: 11:35 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.office.NewEdit.cmbManagement', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.cmbmanagementautocomplete',

    requires: [
        'Ems.model.AutocompleteManagementModel' ,
        'Ems.store.OfficeManagementStore'
    ],

    store: {type: 'officemanegementsotre'},

    multiSelect: false,

    displayField: 'acName',
    valueField: 'acId',

    listWidth: 290,

    hiddenName: EmsObjectName.officeNewEdit.mangId,

    emptyText: 'انتخاب کنید...',

    listeners: {
        autocompleteselect: function (autocomplete, record) {
            var phone = record.get('phone');
            var mobile = record.get('mobile');
            Ext.getCmp(EmsObjectName.officeNewEdit.mangMobil).setValue(mobile);
            Ext.getCmp(EmsObjectName.officeNewEdit.mangTel).setValue(phone);
        }
    }
});

