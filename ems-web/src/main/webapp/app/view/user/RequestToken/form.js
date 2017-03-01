/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/2/12
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.user.RequestToken.form', {
    extend: 'Ext.form.Panel',

    requires: [ 'Ems.view.user.RequestToken.cmbReasonIssuance'],

    id: 'idUserRequestTokenFrom',

    alias: 'widget.userrequesttokenform',
    bodyBorder: false,
    border: false,
    initComponent: function () {
        this.items = [
            this.OnRegisterFormToken()
        ];
        this.callParent(arguments);
    }, OnRegisterFormToken: function () {
        return ({
            layout: 'column',
            border: false,
            height: 80,
            bodyBorder: false,
            items: [
                this.registerFormToken_c1(),
                this.registerFormToken_c2()
            ]
        });
    },
    registerFormToken_c1: function () {
        return({
            xtype: 'container',
            columnWidth: .50,
            bodyBorder: false,
            border: false,

            items: [
                {
                    xtype: 'container',
                    layout: {
                        type: 'anchor'
                    },

                    items: [
                        {
                            xtype: "radiogroup",
                            layout: 'anchor',
                            name: EmsObjectName.userRequestToken.tokenType,
                            itemId: 'tokenManagementRadioGroup',
                            id: 'tokenManagmentRadio',
                            border: false,
                            bodyBorder: false,
                            fieldLabel: 'نوع توکن',
                            defaults: {
                                anchor: '100%',
                                hideEmptyLabel: false,
                                xtype: "radio",
                                name: "tokenType",
                                labelWidth: 1

                            },
                            items: [
                                {
                                    boxLabel: 'احراز هویت',
                                    inputValue: "A",
                                    disabled: true
                                },
                                {
                                    boxLabel: 'امضا',
                                    inputValue: "S",
                                    disabled: true
                                },
                                {
                                    boxLabel: 'رمزنگاری',
                                    inputValue: "E",
                                    disabled: true
                                }
                            ]
                        }
                    ]
                }
            ]
        });
    },
    registerFormToken_c2: function () {
        return({
            xtype: 'container',
            columnWidth: .50,
            bodyBorder: false,
            border: false,
            items: [
                {
                    xtype: 'userrequesttokencmbreasonissuance',
                    id: EmsObjectName.userRequestTokenForm.reasonDeleteToken,
                    itemId: 'userRequestIssuanceReason',
                    name: EmsObjectName.userRequestTokenForm.reasonDeleteToken
                },
                {
                    html: '<br/>', border: false
                },
                {
                    xtype: 'button', itemId: 'RegisterRequestToken', text: 'ثبت توکن',
                    handler: function () {
                        Tools.UserRequestToken.RegisterRequestToken = 0;
                    },
                    width: 100,
                    border: true
                }
            ]
        });
    }
});

