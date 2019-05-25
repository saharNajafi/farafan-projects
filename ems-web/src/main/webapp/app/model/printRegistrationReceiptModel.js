Ext.define('Ems.model.printRegistrationReceiptModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.cardRequestList.citizenFirstName
        , EmsObjectName.cardRequestList.citizenSurname
        , EmsObjectName.cardRequestList.fatherName
        , EmsObjectName.cardRequestList.citizenNId
        , EmsObjectName.cardRequestList.birthCertId
        , EmsObjectName.cardRequestList.citizenBirthDate
        , EmsObjectName.cardRequestList.reservationDate
        , EmsObjectName.cardRequestList.trackingId
        , EmsObjectName.cardRequestList.userFirstName
        , EmsObjectName.cardRequestList.userLastName
        , EmsObjectName.cardRequestList.receiptDate
    ]
});