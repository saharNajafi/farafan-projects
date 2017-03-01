/**
 * Created by moghaddam on 6/25/14.
 *
 * The model class representing spouse info (used in request details dialogue)
 */

Ext.define('Ems.model.CitizenInfoSpouseModel', {
    extend: 'Gam.data.model.Model',
    fields:[
        EmsObjectName.cardRequestList.spouseFirstName,
        EmsObjectName.cardRequestList.spouseSureName,
        EmsObjectName.cardRequestList.spouseNID,
        EmsObjectName.cardRequestList.spouseMaritalStatus,
        EmsObjectName.cardRequestList.spouseMarriageDate,
        EmsObjectName.cardRequestList.spouseDeathDivorceDate
    ]
});
