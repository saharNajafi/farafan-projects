/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/16/12
 * Time: 8:56 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.user.RequestToken.cmbReasonIssuance', {
    extend: 'Ext.form.ComboBox',

    alias: 'widget.userrequesttokencmbreasonissuance',
    requires: ['Ems.store.userRequestTokenReasonIssuanceStore'],

    fieldLabel: 'دلیل صدور',
    margin: 5,
    labelWidth: 70,
    store: {type: 'userrequesttokenreasonissuancestore'},
    queryMode: 'local',
    displayField: EmsObjectName.comboBox.name,
    valueField: EmsObjectName.comboBox.code,
    listConfig: {
        getInnerTpl: function () {
            var tpl = '<div>{name}</div>';
            return tpl;
        }
    }

});
/*
 Ext.define('Ems.view.user.RequestToken.cmbReasonIssuance',{
 extend: 'Gam.form.field.Autocomplete',

 alias:'widget.userrequesttokencmbreasonissuance',
 requires:['Ems.store.userRequestTokenReasonIssuanceStore','Ems.model.ComboBoxSimpleModel'],

 fieldLabel: 'دلیل صدور',
 margin:5,
 labelWidth:70,
 // store: {type:'userrequesttokenreasonissuancestore'},
 store:{type:'usercmborganizationalstatusstore'}  ,

 hiddenName: EmsObjectName.userForm.departmentId

 });*/
