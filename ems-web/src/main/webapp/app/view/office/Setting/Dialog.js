/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/12/12
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.office.Setting.Dialog', {
    extend: 'Gam.window.dialog.LocalEntity',
    alias: 'widget.officesettingdialog',


    autoScroll: true,

    width: 400,

    height: 200,

    action: "add",

    title: 'تنظیمات',

    requires: [
        ,'Ems.store.OfficeSettingFeatureExtractIDStore'
        ,'Ems.store.OfficeSettingFeatureExtractVersionStore'
    ],

    initComponent: function () {
        this.callParent(arguments);
       // this.minHeight = 100;
    },

    buildFormItems: function () {
        return [
            {
                xtype: 'numberfield',
                itemId: 'officeID',
                fieldLabel: 'dsfsdf'
            },
            {
                xtype: 'combobox',
                store: {type: 'officesettingfeatureextractidstore'},
                valueField: 'id',
                displayField: 'title',
                itemId: 'extractID',
                forceSelection: true,
                fieldLabel: 'تست۱'
            },
            {
                xtype: 'combobox',
                store: {type: 'officesettingfeatureextractversionstore'},
                valueField: 'id',
                displayField: 'title',
                itemId: 'extractVersion',
                forceSelection: true,
                fieldLabel: 'تست۲'
            }
        ]
    }
});