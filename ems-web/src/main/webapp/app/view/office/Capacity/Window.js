/**
 * Created by IntelliJ IDEA.
 * User: Navid
 * Date: 05/19/2018
 * Time: 2:03 PM
 */

Ext.define('Ems.view.office.Capacity.Window', {
    extend: 'Ext.window.Window',

    requires: [
        'Ems.view.office.Capacity.Grid'
        ,'Ems.view.user.NewEdit.cmbOrganizationalStatus'
        , 'Ems.view.office.NewEdit.cmbRating'

        , 'Ems.view.office.NewEdit.ManagementInfo'
        , 'Ems.view.office.NewEdit.OfficeInfo'
        , 'Ems.view.office.NewEdit.CapacityOfficeInfo'
        , 'Ems.view.office.NewEdit.OfficeAddressInfo'
        , 'Ems.view.office.NewEdit.OfficeFacility'
        , 'Ems.view.office.NewEdit.cmbManagement'
        , 'Ems.view.office.NewEdit.cmbSuperRegisterOffice'
    ],


    height: 550,
    width: 750,

    resizable: false,
    //closeAction: 'destroy',
    //modal: true,
    layout: {
        type: 'border'
    },
    title: 'فهرست ظرفیت',

    initComponent: function () {
        var me = this;

        this.dockedItems = [
            {
                xtype: 'toolbar',
                style: 'direction:ltr; border:0px;',
                bodyStyle: 'direction:ltr; border:0px;',
                dock: 'bottom',
                width: 90,
                items: [
                    {
                        width: 70,
                        text: 'بستن',
                        iconCls: 'windows-Cancel-icon',
                        // style:'margin-right:5px; ',
                        handler: function () {
                            this.up('window').close();
                        }
                    }
                ]

            }
        ];

        this.items = [
            {
                xtype: 'officeneweditcapacityofficeinfo',
                region: 'north',
                height: 200
            },
            {
                xtype: 'officecapacitygrid',
                region: 'center',
                flex: 1
            }
        ]
        me.callParent(arguments);
    }
});
