/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 4/28/12
 * Time: 9:18 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.LostCardListModel', {
    extend: 'Gam.data.model.Model',

    fields: [
             
        {name: EmsObjectName.lostCardGrid.cardId, type: 'int'},
        {name: EmsObjectName.lostCard.fname, type: 'string'},
        {name: EmsObjectName.lostCard.lname, type: 'string'},
        {name: EmsObjectName.lostCard.nationalId, type: 'string'},
        {name: EmsObjectName.lostCardGrid.crn, type: 'string'},
        {name: EmsObjectName.lostCardGrid.cardLostDate, type: 'date'},
       
        {name: EmsObjectName.lostCardGrid.citizenFirstName, type: 'string'},
        {name: EmsObjectName.lostCardGrid.citizenLastName, type: 'string'},
        
        {name: EmsObjectName.lostCardGrid.batchId, type: 'int'},
        {name: EmsObjectName.lostCardGrid.isConfirm, type: 'string'}
    ]
});

