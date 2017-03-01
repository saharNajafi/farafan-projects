/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/30/12
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.user.RequestToken.deleteToken.Form', {
    extend: 'Ext.form.Panel',

    alias: 'widget.userrequesttokendeletetokenform',
    requires: ['Ems.view.user.RequestToken.deleteToken.cmdReasonCancelToken'],

    bodyBorder: false,
    border: false,
    initComponent: function () {
        this.items = [  this.OnRegisterFormToken()  ];
        this.callParent(arguments);
    }, OnRegisterFormToken: function () {
        return ({
            layout: 'column',
            border: false,
            bodyBorder: false,
            items: [this.OnRegisterDeleteFormToken_c1()]
        });
    }, OnRegisterDeleteFormToken_c1: function () {
        return({
            xtype: 'form',
            columnWidth: 1,
            bodyBorder: false,
            border: false,
            items: [
                {
                    xtype: 'userrequesttokencmdreasoncanceltoken',
                    id: EmsObjectName.userRequestTokenDeleteTokenForm.reasonDelete,
                    name: EmsObjectName.userRequestTokenDeleteTokenForm.reasonDelete,
                    fieldLabel: 'دلیل ابطال',
                    margin: 5,
                    labelWidth: 70
                },
                {
                    xtype: 'textarea',
                    labelWidth: 75,
                    fieldLabel: 'توضیحات',
                    anchor: '-5',
                    name: EmsObjectName.userRequestTokenDeleteTokenForm.descriptionDeleteToken,
                    id: EmsObjectName.userRequestTokenDeleteTokenForm.descriptionDeleteToken

                }
            ]
        });
    }
});
