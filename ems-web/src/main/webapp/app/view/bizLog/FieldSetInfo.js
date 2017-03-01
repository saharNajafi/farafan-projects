/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 9/4/12
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.bizLog.FieldSetInfo', {
    extend: 'Gam.form.FieldSet',
    alias: 'widget.bizlogfieldsetinfo',

    //id:'idbizLogviewfieldsetinfo',

    // title:'مشاهده رویداد',

    border: false,
    bodyBorde: false,

    //height:100,

    layout: 'column',
    initComponent: function () {

        var me = this;

        me.defaults = {
            columnWidth: 1
        };

        me.callParent(arguments);
    },

    getReadOnlyFields: function () {
        return[
            {
                xtype: 'textarea',
                height: 170,
                autoScroll: true,
                readOnly: true,
                allowBlank: true,
                fieldLabel: 'اطلاعات تکمیلی',
                name: EmsObjectName.bizLog.additionalData,
                listeners: {
                    'render': function (c) {
                        Tools.setElementDir(c, 'ltr');
                    }
                }
                // bodyStyle:'text-align:left;',
                // style:'text-align:left;' ,
                /* renderer:function(value){
                 return '<pre dir="ltr">'+value+'</pre>'
                 }*/
            }
        ];
    }


});
