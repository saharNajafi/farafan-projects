/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 6:11 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.estelam2falseList.estelam2Log.Grid', {
    extend: 'Gam.grid.Crud',
    alias: 'widget.estelam2loggrid',
    requires: ['Ems.store.Estelam2LogStore'],
    store: {type: 'estelam2logstore'},
    border: false,
    columns: [
      
        {
            xtype: 'gam.datecolumn',
            dataIndex: EmsObjectName.estelam2FalseList.createDate,
            format: Ext.Date.defaultDateTimeFormat,
            text: 'تاریخ'
        },
        {
            dataIndex: EmsObjectName.estelam2FalseList.perName,
            text: 'نوع',
            renderer: function (value) {
                if (value && typeof value === 'string') {
                    var resualt = eval("EmsResource.estelamFailureType." + value);
                    return resualt != null ? resualt : value;
                }
            },
            	
        },
        {
            dataIndex: EmsObjectName.estelam2FalseList.action,
            width: 600,
            text: 'توضیحات'
        }
    ],
    initComponent: function () {
        this.callParent(arguments);
    }

});
