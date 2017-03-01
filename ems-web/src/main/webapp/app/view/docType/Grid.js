/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/7/12
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.docType.Grid', {
    extend: 'Gam.grid.Crud',

    alias: 'widget.doctypegrid',

    stateId: 'wDocTypeGrid',

    multiSelect: false,

    id: 'idDocTypeGrid',

    title: 'مدیریت انواع سند',

    requires: [
        'Ems.store.DocTypeStore'
    ],

    store: {type: 'doctypestore'},

    actions: ['gam.delete' ],
    actionColumnItems: [
        {
            getClass: function (value, metaData, record, rowIndex, colIndex, store) {
                return 'grid-edit-action-icon';
            },
            tooltip: 'ویرایش', action: 'editing'
        }
    ],
    contextMenu: [ 'gam.delete' ],
    tbar: [

        {
            id: 'btnOfficeCreate',
            text: 'جديد',
            iconCls: 'tbar-add',
            handler: function () {
                Tools.docType.containerNewWindow = 0;
            }
        }/*,
         {
         id: 'btnOfficeEdit',
         text: 'ویرایش' ,
         iconCls: 'tbar-edit',
         handler:function(){
         Tools.docType.containerEditWindow=0;
         }
         }*/,
        'gam.delete'
    ],

    initComponent: function () {
        this.columns = this.getItemsDocTypeGrid();
        this.callParent(arguments);
    }, getItemsDocTypeGrid: function () {
        return([
            {
                xtype: 'gridcolumn',
                width: 150,
                text: 'نوع سند',
                dataIndex: EmsObjectName.docType.name,
                id: EmsObjectName.docType.name,
                filterable: true,
                filter: true
            },
            {
                xtype: 'gridcolumn',
                text: 'سرویس',
                dataIndex: EmsObjectName.docType.service,
                id: EmsObjectName.docType.service,
                flex: 1,
                renderer: function (value) {
                    if (value && value.length !== undefined) {
                        var arr = new Array(),
                            resultStr = "";
                        arr = value.split(",");

                        var i = 0;

                        while (i < arr.length) {

                            var rs = eval("EmsResource.DocType.service.s" + arr[i]);

                            if (resultStr.length === 0) {
                                resultStr = resultStr + rs;
                            } else {
                                resultStr = resultStr + " , " + rs;
                            }
                            i++;
                        }
                        return   resultStr;
                    }
                    return null;
                }
            }
        ]);
    }
});
