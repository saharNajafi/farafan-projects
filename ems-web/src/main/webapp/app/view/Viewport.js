Ext.define('Ems.view.Viewport', {
    extend: 'Ext.container.Viewport',

    layout: {
        type: 'border'
    },

    initComponent: function () {
        var me = this;

        this.items = [
            {
                region: 'north',
                height: 90,
                layout: 'border',
                bodyStyle: 'background-color: #6F95C4   ',
                defaults: {
                    bodyStyle: 'background-color: #6F95C4',
                    border: false
                },
                items: [
//                    {
//                        width: 100,
//                        region: 'west',
//                        html: '<img style="margin:13px 5px 5px 5px; " src="./resources/themes/images/logo/matiran_small.png"/>'
//                    },
                    {
                        region: 'center',
                        bodyStyle: 'background-color: #6F95C4; text-align: center;',
                        html: '<img style="margin:18px 5px 5px 5px; " src="./resources/themes/images/logo/title_text.png"/>'
//                        html: "<div style='width:100%; height:100%; color: white; font-weight: bolder; font-family: sans-serif;  font-size: 30pt; padding-top: 20px;'>" +
//                            "سامانه پشتیبانی خدمات" +
//                            "</div>"
                    },
                    {
                        width: 100,
                        region: 'east',
                        html: '<img style="margin:5px 5px 5px 5px; width:54px; height: 75px;" src="./resources/themes/images/logo/nocr_small.png"/>'
                    }
                ]
            },
            this.createUserInfoPanel(),
            this.createFeedPanel(),
            {
                layout: 'fit',
                region: 'center',
                xtype: 'container',

                items: [
                    {
                        xtype: 'panel',
                        bodyStyle: 'border:0px; background-color:#B2CFF4;',
                        title: '&nbsp;',
                        items: [
                            {
                                xtype: 'component',
                                border: false,
                                html: '<div style="background-color:#B2CFF4;"><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/></div>' +
                                    '<table id="mainTable" cellpadding="0" cellspacing="0" style="width: 100%; border:0px; background-color:#B2CFF4;">' +
                                    '<tr>' +
                                    '<td style="width: 50%;"> <div style="width: 50%; border:0px;"> </div> </td>' +
                                    '<td style="width: 700px;">' +
                                    '<div style="width: 400px; border:0px;">' +
                                    '<div style="width: 168px; margin-left: auto; margin-right: auto; font-family: Tahoma; font-size: 14px; color:#638FC6;  border:0px;"><b>سامانه خدمات شهروندی</br><br /></div>' +
                                    '<div style="width: 100px; margin-left: auto; margin-right: auto; font-family: Tahoma; font-size: 10px; color:#638FC6; border:0px;"><b>نگارش : </b><b>' + EMS_VERSION + '</b></div>' +
                                    '</div>' +
                                    '</td>' +
                                    '<td style="width: 50%;"> <div style="width: 50%; border:0px;">  </div> </td>' +
                                    '</tr>' +
                                    '</table>'
//                                +
//                                '<img style="position: absolute; bottom:10px; left:10px;" src="./resources/themes/images/logo/nocr_small.png"/>' +
//                                '<img style="position: absolute; bottom:10px; right:10px;" src="./resources/themes/images/logo/matiran_small.png"/>'
                            }
                        ]
                    }
                ],
                id: 'moduleContainer',
                margins: '5 0 5 5',
                flex: 1
                //            }, {
                //                region:'south',
                //                height: 50,
                //                layout: 'hbox',
                //                items:[{
                //                    html: '<img style="width:100; height:50;" src="./resources/themes/images/logo/nocr_small.png"/>'
                //                }]
            }
        ];
        me.callParent(arguments);
    },
    createUserInfoPanel: function () {
        return (
            Ext.create('Ems.view.viewport.UserInfo', {
                hideHeaders: true,
                collapsible: false,
                region: 'north',
                width: '100%',
                style: 'background-color:#B2CFF4;',
                bodyStyle: 'background-color:#B2CFF4;',
                height: 25,
                border: false,
                margins: '0 0 0 0'
            })
            );
    }, createFeedPanel: function () {
        return (
            Ext.create('Ems.view.viewport.MenuPanel', {
                hideHeaders: true,
                collapsible: false,
                region: 'north',
                width: '100%',
                style: 'background-color:#DFE8F6;',
                bodyStyle: 'background-color:#DFE8F6;',
                height: 80,
                border: false,
                margins: '0 0 0 0'
            })
            );
    }
});