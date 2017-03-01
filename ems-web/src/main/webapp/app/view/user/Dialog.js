/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/3/12
 * Time: 1:01 PM
 */

Ext.define('Ems.view.user.Dialog', {
    extend: 'Gam.window.dialog.LocalEntity',
    alias: 'widget.userdialog',

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
