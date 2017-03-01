/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/2/12
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.user.NewEdit.cmbOrganizationalStatus', {
    extend: 'Gam.form.field.Autocomplete',
    alias: 'widget.usercmborganizationalstatus',

    requires: [
        'Ems.store.userOrganizationalStatusStore',
        'Ems.model.AutocompleteSimpleModel'
    ],


    store: {type: 'usercmborganizationalstatusstore'},

    emptyText: 'انتخاب کنید...',
    multiSelect: false,
    displayField: 'acName',
    valueField: 'acId'/* ,

     hiddenName: EmsObjectName.userForm.departmentId*/

});
