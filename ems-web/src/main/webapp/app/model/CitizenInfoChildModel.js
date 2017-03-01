/**
 * Created by moghaddam on 6/25/14.
 *
 * The model class representing child info (used in request details dialogue)
 */
Ext.define('Ems.model.CitizenInfoChildModel', {
    extend: 'Gam.data.model.Model',
    fields:[
        EmsObjectName.cardRequestList.childFirstName,
        EmsObjectName.cardRequestList.childNID,
        EmsObjectName.cardRequestList.childBirthDate,
        EmsObjectName.cardRequestList.childGender
    ]
});

