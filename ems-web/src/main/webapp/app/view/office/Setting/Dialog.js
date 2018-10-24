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

    extractID: null,

    extractVersion: null,

    title: 'تنظیمات 4545lds',

    requires: [
        ,'Ems.store.OfficeSettingFeatureExtractIDStore'
        ,'Ems.store.OfficeSettingFeatureExtractVersionStore'
        ,'Ems.view.office.Setting.AutoCompleteFeatureExtractID'
        ,,'Ems.view.office.Setting.AutoCompleteFeatureExtractVersion'
    ],

    initComponent: function () {
        this.callParent(arguments);
       // this.minHeight = 100;
    },

    buildFormItems: function () {
        return [
            {
                xtype: 'autoCompleteFeatureExtractID',
                fieldLabel: 'featureExtractID',
                id: 'feid',
                name: 'feid',
                labelWidth: 135,
                allowBlank: false
            },
            {
                xtype: 'autoCompleteFeatureExtractVersion',
                fieldLabel: 'featureExtractVersion',
                labelWidth: 135,
                id: 'feversion',
                name: 'feversion',
                allowBlank: false
            }
        ]
    }
});