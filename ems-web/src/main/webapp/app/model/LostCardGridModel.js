/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 4/28/12
 * Time: 9:18 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.LostCardGridModel', {
    extend: 'Gam.data.model.Model',

    fields: [
        {name: EmsObjectName.lostCard.cardId, type: 'int'},
        {name: EmsObjectName.lostCard.crn, type: 'string'},
        {name: EmsObjectName.lostCard.fname, type: 'string'},
        {name: EmsObjectName.lostCard.lname, type: 'string'},
        {name: EmsObjectName.lostCard.nationalId, type: 'string'},
        {name: EmsObjectName.lostCard.cardLostDate, type: 'date'},
       
        {name: EmsObjectName.lostCard.citizenFirstName, type: 'string'},
        {name: EmsObjectName.lostCard.citizenLastName, type: 'string'},

        
        {name: EmsObjectName.lostCard.numberBox, type: 'string'},
        {name: EmsObjectName.lostCard.batchId, type: 'string'},
        {name: EmsObjectName.lostCard.isConfirm, type: 'string'}
    ]
});

