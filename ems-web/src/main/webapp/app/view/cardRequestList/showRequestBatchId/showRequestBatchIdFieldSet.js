
Ext.define('Ems.view.cardRequestList.showRequestBatchId.showRequestBatchIdFieldSet',
    {
        id: 'ShowRequestBatchIdFieldSet',
        extend: 'ICT.form.FieldSet',
        alias: 'widget.showRequestBatchIdFieldSet',

        contentStyle: function () {
            return (Tools.user.StyleObject);
        },
        layout: 'column',
        border: false,
        initComponent: function () {

            var me = this;
            me.defaults = {
                columnWidth: 1
            };
            me.callParent(arguments);
        },
        getEditableFields: function () {
            return [
                {
                    fieldLabel: 'شماره دسته ',
                    itemId: EmsObjectName.BatchList.cmsID,
                    xtype: 'displayfield'
                }
            ];
        }
    });
