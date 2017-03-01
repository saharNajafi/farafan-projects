/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/17/12
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.bizLog.Dialog', {
    extend: 'Gam.window.dialog.LocalEntity',

    alias: 'widget.bizlogdialog',

    requires: [
        'Ems.view.bizLog.FieldSetFirst',
        'Ems.view.bizLog.FieldSetInfo',
        'Ems.view.bizLog.FieldSetSecond'
    ],

    width: 900,

    initComponent: function () {
        this.height = 350;

        this.callParent();
    },

    buildFormItems: function () {
        return [
            {
                xtype: 'bizlogfieldsetFirst'
            },
            {
                xtype: 'bizlogfieldsetsecond'
            },
            {
                xtype: 'bizlogfieldsetinfo'
            }
        ];
    }
});
