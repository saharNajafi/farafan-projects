/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 9/4/12
 * Time: 10:51 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.bizLog.FieldSetSecond', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.bizlogfieldsetsecond',

    id: 'idbizLogviewfieldsetSecond',

    // title:'مشاهده رویداد',

    border: false,
    bodyBorde: false,

    autoScroll: true,

    // height:100,

    layout: 'column',

    defaults: {
        labelWidth: 50
    },

    initComponent: function () {

        var me = this;

        me.defaults = {
            columnWidth: 1 / 3
        };

        me.callParent(arguments);
    },

    getReadOnlyFields: function () {
        return[
            {
                fieldLabel: 'موجودیت',
                labelWidth: 70,
                name: EmsObjectName.bizLog.entityNameStv,
                renderer: function (value) {
                    return eval("EmsResource.Entity." + value);
                }
            },
            {
                fieldLabel: 'شناسه موجودیت',
                name: EmsObjectName.bizLog.entityID
            },
            {
                fieldLabel: 'عملیات',
                labelWidth: 50,
                name: EmsObjectName.bizLog.actionNameStv,
                renderer: function (value) {
                    return eval("EmsResource.Action." + value);
                }
            }
        ];
    }

});
