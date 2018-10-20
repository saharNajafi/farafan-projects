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

    width: 350,

    sendID: null,

    height: 150,

    action: "add",

    title: 'تنظیمات lds',

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
                xtype: 'combobox',
                store: {type: 'officesettingfeatureextractidstore'},
                valueField: 'feiId',
                displayField: 'featureExtractorNAME',
                itemId: 'extractID',
                forceSelection: true,
                labelWidth: 140,
                fieldLabel: 'featureExtractID'
            },
            {
                xtype: 'combobox',
                store: {type: 'officesettingfeatureextractversionstore'},
                valueField: 'fevId',
                displayField: 'featureExtractorVersion',
                itemId: 'extractVersion',
                labelWidth: 140,
                forceSelection: true,
                fieldLabel: 'featureExtractVersion'
            }
        ]
    }
});