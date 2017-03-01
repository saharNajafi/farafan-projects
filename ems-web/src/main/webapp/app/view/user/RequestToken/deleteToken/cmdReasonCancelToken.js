/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/17/12
 * Time: 5:21 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.user.RequestToken.deleteToken.cmdReasonCancelToken', {
    extend: 'Ext.form.ComboBox',

    alias: 'widget.userrequesttokencmdreasoncanceltoken',
    requires: ['Ems.store.userRequestTokenReasonCancelTokenStore'],

    /*    fieldLabel: 'دلیل ابطال',
     margin:5,
     labelWidth:70,*/
    store: {type: 'userrequesttokenreasoncanceltokenstore'},
    queryMode: 'local',
    displayField: 'name',
    valueField: 'code',
    listConfig: {
        getInnerTpl: function () {
            var tpl = '<div>{name}</div>';
            return tpl;
        }
    }
});
