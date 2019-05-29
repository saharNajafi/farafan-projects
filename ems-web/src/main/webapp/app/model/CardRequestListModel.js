/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.CardRequestListModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.cardRequestList.citizenFirstName
        , EmsObjectName.cardRequestList.citizenSurname
        , EmsObjectName.cardRequestList.citizenNId
        ,EmsObjectName.cardRequestList.enrollmentOfficeName
        , EmsObjectName.cardRequestList.enrollmentOfficeId
        ,EmsObjectName.cardRequestList.priority
        , {name: EmsObjectName.cardRequestList.enrolledDate, type: 'date'}
        , {name: EmsObjectName.cardRequestList.portalEnrolledDate, type: 'date'}
        , EmsObjectName.cardRequestList.cardRequestState
        , EmsObjectName.cardRequestList.trackingId
        , EmsObjectName.cardRequestList.cardType
        , EmsObjectName.cardRequestList.cardState
        , EmsObjectName.cardRequestList.requestedAction
        , EmsObjectName.cardRequestList.requestOrigin
        , EmsObjectName.cardRequestList.deliveredOfficeName
        , {name: EmsObjectName.cardRequestList.deliveredDate, type: 'date'}
        
        //hossein 8 feature start
        , EmsObjectName.cardRequestList.documentFlag
        , EmsObjectName.cardRequestList.fingerFlag
        , EmsObjectName.cardRequestList.faceFlag
        , {name: EmsObjectName.cardRequestList.reservationDate, type: 'date'},
        , EmsObjectName.cardRequestList.fatherName
        , EmsObjectName.cardRequestList.birthCertId
        , EmsObjectName.cardRequestList.citizenBirthDate
        , EmsObjectName.cardRequestList.userFirstName
        , EmsObjectName.cardRequestList.userLastName
        , EmsObjectName.cardRequestList.receiptDate
        //hossein 8 feature start
    ]
});
