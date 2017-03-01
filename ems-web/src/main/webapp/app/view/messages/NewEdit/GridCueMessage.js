/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 12:06 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.messages.NewEdit.GridCueMessage', {

    extend: 'Gam.grid.Crud',
    id:'cuemessages',
    alias: 'widget.cuemessages',
//    multiSelect: false,

    requires: ['Ems.store.GridCueMessageStore'],

    store: {type: 'GridCueMessageStore'},

    initComponent: function () {
    	me = this;
    	
        me.columns = this.getItemsGridForm();
        me.callParent(arguments);
    },

    getItemsGridForm: function () {
        return([{
                xtype: 'gridcolumn',
                width: 150,
                text: 'مقصد',
                dataIndex: EmsObjectName.cueMessageList.messageDestination,
                id:  EmsObjectName.cueMessageList.messageDestination,
                filterable: true,
                filter: true
            }]);
    }
});

