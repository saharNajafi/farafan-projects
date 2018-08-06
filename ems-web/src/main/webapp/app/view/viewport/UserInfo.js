/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/23/12
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.viewport.UserInfo', {
    extend: 'Ext.panel.Panel',
    layout: 'fit',

    // stateId:'wUserInfoMenu',
    //stateful:false,

    alias: 'widget.viewportuserinfo',

    id: 'viewportUserInfo',

    border: false,
    hideHeaders: true,

    requires: ['Ems.view.viewport.BasicInfo', 'Ems.view.viewport.Operation', 'Ems.view.viewport.Report'],

    initComponent: function () {
        var me = this;
        this.items = [
            {
                xtype: 'container',
                layout: {
                    type: 'hbox',
                    pack: 'start',
                    align: 'stretch'
                },
                items: [
                    {
                        xtype: 'buttongroup',
                        columns: 2,
                        defaults: {
                            scale: 'small',
                            iconAlign: 'right'
                        },
                        items: [
                            {
                                id: 'defineUserAndDepartment',
                                text: 'در حال بارگذاری اطلاعات کاربر...'
                            },
                            {

                                rowspan: 3,
                                text: '',
                                paddingLeft: 0,
                                arrowAlign: 'left', menu: [
                                {
                                    action: 'changePassword',
                                    text: 'تغییر گذرواژه',
                                    icon: 'resources/themes/images/default/toolbar/changePass.png',
                                    id: 'UserInfoMenuChangePassword',
                                    handler: function () {
                                        Tools.defineUserAndDepartment.changPassWord = 0;
                                    }
                                },
                                {
                                    action: 'masterExit',
                                    text: 'خروج',
                                    id: 'custlogout',
                                    tooltip: 'خروج' + '   < Ctrl+Shift+Q >',
                                    icon: 'resources/themes/images/default/toolbar/exit.png',
                                    handler: function () {
                                        Tools.defineUserAndDepartment.logout = 0;
                                    }
                                }
                            ]

                            }
                        ]
                    }
                ],
                listeners: {
                    render: function (view) {

                        view.items.items[0].items.items[0].stateId = 'w' + view.items.items[0].items.items[0].id;
                        view.items.items[0].items.items[0].stateful = false;


                        /*
                         if(view.stateful)
                         {
                         var stateProvider = Ext.create('Ems.state.Provider');
                         stateProvider.setDetail(view.items.items[0].items.items[0].stateId ,1);

                         stateProvider.saveing();

                         }*/

                    }
                }
            }
        ];
        this.callParent(arguments);
    }

});
