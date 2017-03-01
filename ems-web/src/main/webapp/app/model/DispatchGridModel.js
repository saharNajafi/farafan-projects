/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/16/12
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.DispatchGridModel', {
    extend: 'Gam.data.model.Model',

    fields: [
        {name: EmsObjectName.dispatchGrid.mySendDateBox, type: 'date'},
        {name: EmsObjectName.dispatchGrid.batchNumber, type: 'string'},
        {name: EmsObjectName.dispatchGrid.numberBox, type: 'string'},
        {name: EmsObjectName.dispatchGrid.receiverBox, type: 'string'},
        {name: EmsObjectName.dispatchGrid.sendDateBox, type: 'date'},
        {name: EmsObjectName.dispatchGrid.receiveDateBox, type: 'date'},
        {name: EmsObjectName.dispatchGrid.lostDateBox, type: 'date'},

        {name: EmsObjectName.dispatchGrid.typeSend, type: 'string'},
        {name: EmsObjectName.dispatchGrid.typeReceiver, type: 'string'},

        {name: EmsObjectName.dispatchGrid.statusBox, type: 'int'}
    ]
});
