/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/17/12
 * Time: 1:28 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.bizLog.FieldSet', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.bizlogfieldset',

    id: 'idbizLogviewfieldset',

    // title:'مشاهده رویداد',

    border: false,
    bodyBorde: false,

    autoScroll: true,

    height: 300,

    layout: 'column',
    initComponent: function () {

        var me = this;

        me.defaults = {
            columnWidth: 1 / 2
        };

        me.callParent(arguments);
    },

    getReadOnlyFields: function () {
        return this.getItems();
    },

    getItems: function () {
        return[
            {
                xtype: 'datedisplayfield',
                name: EmsObjectName.bizLog.date,
                format: Ext.Date.defaultDateTimeFormat,
                fieldLabel: 'تاریخ'
            },
            {
                fieldLabel: 'شخص',
                name: EmsObjectName.bizLog.actor
            },
            {
                fieldLabel: 'عملیات',
                name: EmsObjectName.bizLog.actionNameStv
            },
            {
                fieldLabel: 'موجودیت',
                name: EmsObjectName.bizLog.entityNameStv
            },
            {
                fieldLabel: 'شناسه موجودیت',
                name: EmsObjectName.bizLog.entityID
            },
            {
                fieldLabel: 'اطلاعات تکمیلی',
                name: EmsObjectName.bizLog.additionalData,
                bodyStyle: 'text-align:left;',
                style: 'text-align:left;',
                renderer: function (value) {
                    return '<pre dir="ltr">' + value + '</pre>'
                }
            }
        ]
    }
});

