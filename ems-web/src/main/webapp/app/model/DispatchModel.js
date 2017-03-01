/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 4/28/12
 * Time: 9:18 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.DispatchModel', {
    extend: 'Gam.data.model.Model',

    fields: [
        {name: EmsObjectName.dispatchGrid.id, type: 'int'},
        {name: EmsObjectName.dispatchGrid.receiverBox, type: 'string'},
        {name: EmsObjectName.dispatchGrid.sendDateBox, type: 'date'},
        {name: EmsObjectName.dispatchGrid.receiveDateBox, type: 'date'},
        {name: EmsObjectName.dispatchGrid.mySendDateBox, type: 'date'},
        {name: EmsObjectName.dispatchGrid.lostDateBox, type: 'date'},
        //grid SendRecieve
        {name: EmsObjectName.dispatchGrid.batchNumber, type: 'string'},
        {name: EmsObjectName.dispatchGrid.cartTotal, type: 'int'},
        {name: EmsObjectName.dispatchGrid.branchCode, type: 'string'},

        //grid form
        {name: EmsObjectName.dispatchGrid.numberBox, type: 'string'},
        {name: EmsObjectName.dispatchGrid.batchTotal, type: 'int'},
        {name: EmsObjectName.dispatchGrid.statusBox, type: 'int'},
        {name: EmsObjectName.dispatchGrid.receiverCodeBox, type: 'string'},
        {name: EmsObjectName.dispatchGrid.typeReceiver, type: 'string'} ,
        {name: EmsObjectName.dispatchGrid.typeSend, type: 'string'}
    ]
});

