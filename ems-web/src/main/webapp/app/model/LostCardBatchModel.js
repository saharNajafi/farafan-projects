/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 4/28/12
 * Time: 9:18 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.LostCardBatchModel', {
    extend: 'Gam.data.model.Model',

    fields: [
             
        {name: EmsObjectName.lostCardBatchGrid.cardId, type: 'int'},
        {name: EmsObjectName.lostCardBatchGrid.crn, type: 'string'},
        {name: EmsObjectName.lostCardBatchGrid.cardLostDate, type: 'date'},
       
        {name: EmsObjectName.lostCardBatchGrid.citizenFirstName, type: 'string'},
        {name: EmsObjectName.lostCardBatchGrid.citizenLastName, type: 'string'},
        
        {name: EmsObjectName.lostCardBatchGrid.batchId, type: 'int'},
        {name: EmsObjectName.lostCardBatchGrid.isConfirm, type: 'string'}
    ]
});

