/**
 * Created by moghaddam on 6/17/14.
 *
 * The dialog window that woulud be used to display detailed information about a card request
 */
Ext.define('Ems.view.cardRequestList.Dialog', {
    extend: 'Gam.window.dialog.RemoteEntity',
    alias: 'widget.cardrequestlistdialog',

    requires: [
        'Ems.view.cardRequestList.fieldset.CitizenInfo',
        'Ems.view.cardRequestList.fieldset.CitizenContactInfo',
        'Ems.view.cardRequestList.fieldset.CitizenParentsInfo',
        'Ems.view.cardRequestList.fieldset.SpousesInfo',
        'Ems.view.cardRequestList.fieldset.ChildrenInfo',
        'Ems.view.cardRequestList.fieldset.CardRequestInfo'
    ],

    width: 1024,
    height: 900,
    autoScroll: true,

    initComponent: function () {
        this.height = 535;

        this.callParent();
    },

    buildFormItems: function () {
        return [
            {
                xtype: 'cardrequestlistfieldsetcardrequestinfo'
            },
            {
                xtype: 'cardrequestlistfieldsetcitizeninfo'
            },
            {
                xtype: 'cardrequestlistfieldsetcitizencontactinfo'
            },
            {
                xtype: 'cardrequestlistfieldsetcitizenparentsinfo'
            },
            {
                xtype: 'cardrequestlistfieldsetspousesinfo'
            },
            {
                xtype: 'cardrequestlistfieldsetchildreninfo'
            }
        ];
    },

    /**
     * Overriding this method to add scrolling feature
     */
    getFormConfig: function () {
        var formConfig = this.callParent();
        Ext.apply(formConfig, {
            overflowX : 'hidden',
            overflowY : 'auto'
        });

        return formConfig;
    }
});
