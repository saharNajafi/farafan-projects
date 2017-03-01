/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 9/4/12
 * Time: 10:44 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.bizLog.FieldSetFirst', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.bizlogfieldsetFirst',

    // id:'idbizLogviewfieldsetFirst',

    // title:'مشاهده رویداد',

    border: false,
    bodyBorde: false,


    //height:100,

    layout: 'column',
    initComponent: function () {

        var me = this;

        me.defaults = {
            columnWidth: 1 / 3
        };

        me.callParent(arguments);
    },

    getEditableFields: function () {
        return null;
    },

    getReadOnlyFields: function () {
        return[
            {
                xtype: 'datedisplayfield',
                name: EmsObjectName.bizLog.date,
                format: Ext.Date.defaultDateTimeFormat,
                fieldLabel: 'تاریخ',
                labelWidth: 70
            },
            {
                fieldLabel: 'شخص',
                name: EmsObjectName.bizLog.actor
            }
        ]
    }


});
