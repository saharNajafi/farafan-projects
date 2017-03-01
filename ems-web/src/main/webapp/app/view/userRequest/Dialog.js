/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/19/12
 * Time: 11:10 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.userRequest.Dialog', {
    extend: 'Gam.window.dialog.LocalEntity',
    alias: 'widget.userrequestdialog',

    requires: [
        'Ems.view.user.NewEdit.PersonInfo'
        , 'Ems.view.user.NewEdit.SystemInfo'
        , 'Ems.view.user.NewEdit.OrganizationInfo'
    ],

    width: 860,

    initComponent: function () {
        this.height = 535;

        this.callParent();
    },

    buildFormItems: function () {
        return [
            {
                xtype: 'userneweditpersonInfo'
            },
            {
                xtype: 'userneweditsysteminfo'
            },
            {
                xtype: 'userneweditorganizationinfo'
            }
        ];
    }
});
