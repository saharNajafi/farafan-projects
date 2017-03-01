/**
 * Created by Gam Electronics Co.
 * User: Haeri
 * Date: 3/11/12
 * Time: 10:28 AM
 */
Ext.define('Portal.view.util.Captcha', {
    extend: 'Ext.panel.Panel',

    height: 200,
    width: 315,
    layout: {
        type: 'anchor'
    },
    bodyCls: ['captcha-panel'],

    initComponent: function () {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'image',
                    margin: 5,
                    src: 'generateCaptcha.png?rnd=' + Math.random(),
                    cls: 'captcha-image'
                },
                {
                    xtype: 'container',
                    margin: 5,
                    width: 300,
                    layout: {
                        type: 'hbox'
                    },
                    items: [
                        {
                            xtype: 'panel',
                            height: 70,
                            width: 250,
                            layout: {
                                type: 'anchor'
                            },
                            bodyCls: ['captcha-inner-panel'],
                            items: [
                                {
                                    xtype: 'label',
                                    margin: 5,
                                    text: 'متن بالا را وارد کنيد:'
                                },
                                {
                                    xtype: 'textfield',
                                    id: 'fldCaptchaAnswer',
                                    name: 'fldCaptchaAnswer',
                                    margin: 10,
                                    anchor: '100%'
                                }
                            ]
                        },
                        {
                            xtype: 'button',
                            id: 'captchaRefreshBtn',
                            margin: 5,
                            iconCls: 'captcha-refresh-btn'
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }
});