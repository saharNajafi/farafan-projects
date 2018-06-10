/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/10/12
 * Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */


Ext.define('Ems.view.viewport.BasicInfo', {
    extend: 'Gam.form.Panel', //'Ext.panel.Panel',//

    layout: 'fit',

    alias: 'widget.basicInfo',

//    stateId:'wBaseInfoMenu',

    constructor: function (config) {
        config = config || {};
        this.callParent(arguments);
    },

    initComponent: function () {
        this.items = [this.getItemsConterPanel()];
        this.callParent(arguments);
    },


    getItemsConterPanel: function () {
        return( {
            xtype: 'image',
            src: 'resources/themes/images/default/mainMenu/header.jpg'
        }, {
            xtype: 'buttongroup',
            columns: 6,
            // stateId:'wBaseInfoMenu',
            //  stateful:false,
            defaults: {
                scale: 'large',
                iconAlign: 'top',
                width: 75
            },
            items: [
                {
                    xtype: 'splitbutton',
                    action: 'User',
                    text: 'کاربران',
                    //hidden: globalAccessAllow,
                    //text: 'Menu Button',
                    scale: 'large',
                    rowspan: 3,
                    iconCls: 'toolbar-user',
                    iconAlign: 'top',
                    id: 'BaseInfoMenuUserManagement',
                    arrowAlign: 'bottom',
                    menu: [
                        {
                            action: 'User',
                            id: 'BaseInfoMenuUser',
                            tooltip: 'کاربران' + '   < Ctrl+Shift+U >',
                            text: 'مدیریت کاربران',
                            icon: 'resources/themes/images/default/toolbar/userList.png'

                        },
                        {
                            action: 'UserRequest',
                            id: 'BaseInfoMenuUserRequest',
                            tooltip: 'کاربران درخواستی' + '   < Ctrl+Alt+U >',
                            text: 'مدیریت کاربران درخواستی',
                            icon: 'resources/themes/images/default/toolbar/userRequest.png'
                        }/*,
                         {
                         action: 'ManageRoles',
                         text: 'نقش ها',
                         icon: 'resources/themes/images/default/toolbar/role.png'
                         },
                         {
                         action: 'ManagePermissions',
                         text: 'دسترسی ها',
                         icon: 'resources/themes/images/default/toolbar/permission.png'
                         }*/
                    ]
                },
                {
                    action: 'Department',
                    id: 'BaseInfoMenuDepartment',
                    tooltip: 'جایگاه سازمانی' + '   < Ctrl+Shift+O >',
                    text: 'جایگاه سازمانی',
                   // hidden: globalAccessAllow,
                    iconCls: 'toolbar-department',
                    width: 85
                },
                {
                    action: 'Office',
                    id: 'BaseInfoMenuOffice',
                    tooltip: 'دفاتر پیشخوان' + '   < Ctrl+Shift+S >',
                    text: 'دفاتر پیشخوان',
                   // hidden: globalAccessAllow,
                    iconCls: 'toolbar-office'
                },
                {
                    action: 'Workstation',
                    id: 'BaseInfoMenuWorkstation',
                    text: 'ايستگاه کاری',
                    //hidden: globalAccessAllow,
                    tooltip: 'ايستگاه کاری' + '   < Ctrl+Shift+T >',
                    iconCls: 'toolbar-workstation'
                },
                {
                    action: 'Rating',
                    id: 'BaseInfoMenuRating',
                    text: 'رتبه',
                  //  hidden: globalAccessAllow,
                    tooltip: 'رتبه' + '   < Ctrl+Shift+I >',
                    iconCls: 'toolbar-rating',
                    width: 60
                },
                {
                    action: 'DocType',
                    id: 'BaseInfoMenuDocType',
                    text: 'انواع سند',
                   // hidden: globalAccessAllow,
                    tooltip: 'انواع سند' + ' < Ctrl+Shift+T >',
                    iconCls: 'toolbar-docType'
                }
            ],
            listeners: {
                render: function (view) {

                    view.items.items[0].stateId = 'w' + view.items.items[0].id;
                    view.items.items[0].stateful = false;

                    view.items.items[0].menu.items.items[0].stateId = 'w' + view.items.items[0].menu.items.items[0].id;
                    view.items.items[0].menu.items.items[0].stateful = false;

                    view.items.items[0].menu.items.items[1].stateId = 'w' + view.items.items[0].menu.items.items[1].id;
                    view.items.items[0].menu.items.items[1].stateful = false;

                    view.items.items[1].stateId = 'w' + view.items.items[1].id;
                    view.items.items[1].stateful = false;

                    view.items.items[2].stateId = 'w' + view.items.items[2].id;
                    view.items.items[2].stateful = false;

                    view.items.items[3].stateId = 'w' + view.items.items[3].id;
                    view.items.items[3].stateful = false;

                    view.items.items[4].stateId = 'w' + view.items.items[4].id;
                    view.items.items[4].stateful = false;

                    view.items.items[5].stateId = 'w' + view.items.items[5].id;
                    view.items.items[5].stateful = false;


                    /*
                     if(1==1)//(view.stateful)
                     {
                     var stateProvider = Ext.create('Ems.state.Provider');
                     stateProvider.setDetail(view.items.items[0].stateId ,1);
                     stateProvider.setDetail(view.items.items[1].stateId ,1);
                     stateProvider.setDetail(view.items.items[2].stateId ,1);
                     stateProvider.setDetail(view.items.items[3].stateId ,1);
                     stateProvider.setDetail(view.items.items[4].stateId ,1);
                     stateProvider.setDetail(view.items.items[5].stateId ,1);

                     stateProvider.saveing();

                     }*/

                }
            }
        });
    }



});
