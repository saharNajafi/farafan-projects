/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/18/12
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.reportView.Grid', {
    extend: 'Gam.grid.Crud',

    alias: 'widget.reportviewgrid',

    stateId: 'wReportViewGrid',


    title: 'مشاهد گزارش',

    multiSelect: false,

    requires: [
        'Ems.store.ReportRequestStore'
    ],

    store: {type: 'reportrequeststore'},

    /*actions: ['gam.add->bizlogdialog'] ,

     actionColumnItems:['view->bizlogdialog'],*/

    /* contextMenu: [ 'gam.add' ],

     tbar: ['gam.add' ] ,*/

    initComponent: function () {
        this.columns = this.getItemsGridForm();
        this.callParent(arguments);
    }, getItemsGridForm: function () {
        return([
            {
                dataIndex: EmsObjectName.reportRequest.subject,
                id: EmsObjectName.reportRequest.subject,
                text: 'موضوع'
            }
        ]);
    }
});

