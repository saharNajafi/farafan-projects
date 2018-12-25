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

    extractNormalID: null,

    officeSettingID: null,

    extractCCID: null,

    title: 'تنظیمات lds',

    requires: [
        ,'Ems.store.OfficeSettingFeatureExtractNormalStore'
        ,'Ems.store.OfficeSettingFeatureExtractCCStore'
        ,'Ems.view.office.Setting.AutoCompleteFeatureExtractNormal'
        ,'Ems.view.office.Setting.AutoCompleteFeatureExtractCC'
    ],

    initComponent: function () {
        this.callParent(arguments);
       // this.minHeight = 100;
    },

    buildFormItems: function () {
        return [
            {
                xtype: 'autoCompleteFeatureExtractNormal',
                fieldLabel: 'featureExtractNormal',
                id: 'feiN',
                name: 'feiN',
                labelWidth: 135
               // allowBlank: false
            },
            {
                xtype: 'autoCompleteFeatureExtractCC',
                fieldLabel: 'featureExtractCC',
                labelWidth: 135,
                id: 'feiCC',
                name: 'feiCC'
                //allowBlank: false
            }
        ]
    }
});