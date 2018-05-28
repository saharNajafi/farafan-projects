/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/12/12
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.office.Dialog', {
    extend: 'Gam.window.dialog.LocalEntity',
    alias: 'widget.officedialog',

    requires: [
        'Ems.view.user.NewEdit.cmbOrganizationalStatus'
        , 'Ems.view.office.NewEdit.cmbRating'

        , 'Ems.view.office.NewEdit.ManagementInfo'
        , 'Ems.view.office.NewEdit.OfficeInfo'
        , 'Ems.view.office.NewEdit.OfficeAddressInfo'
        , 'Ems.view.office.NewEdit.OfficeFacility'
        , 'Ems.view.office.NewEdit.cmbManagement'
        , 'Ems.view.office.NewEdit.cmbSuperRegisterOffice'

    ],

    autoScroll: true,

    scrollable: true,

    constrain: true,

    width: 860,

    initComponent: function () {
        this.height = 680;
        this.autoScroll = true;
        this.scrollable = true;
        this.callParent(arguments);
    },

    buildFormItems: function () {
        return [
            {
                xtype: 'officeneweditmanagement'
            },
            {
                xtype: 'officeneweditofficeinfo'
            },
            {
                xtype: 'officeneweditofficeaddressinfo'
            },
            {
                xtype: 'officeneweditofficeFacility'
            }
        ]
    }
});