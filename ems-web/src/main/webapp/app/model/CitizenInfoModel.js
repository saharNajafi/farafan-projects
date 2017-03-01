/**
 * Created by moghaddam on 6/22/14.
 */
Ext.define('Ems.model.CitizenInfoModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.cardRequestList.citizenFirstName
        , EmsObjectName.cardRequestList.citizenSurname
        , EmsObjectName.cardRequestList.citizenNId
        , EmsObjectName.cardRequestList.citizenBirthDate
        , EmsObjectName.cardRequestList.citizenBirthDateSOL
        , EmsObjectName.cardRequestList.citizenBirthDateLUN
        , EmsObjectName.cardRequestList.gender
        , EmsObjectName.cardRequestList.religion
        , EmsObjectName.cardRequestList.birthCertiIssuancePlace
        , EmsObjectName.cardRequestList.postalCode
        , EmsObjectName.cardRequestList.livingProvinceName
        , EmsObjectName.cardRequestList.livingCityName
        , EmsObjectName.cardRequestList.phone
        , EmsObjectName.cardRequestList.mobile
        , EmsObjectName.cardRequestList.address
        , EmsObjectName.cardRequestList.fatherBirthCertID
        , EmsObjectName.cardRequestList.fatherName
        , EmsObjectName.cardRequestList.fatherNID
        , EmsObjectName.cardRequestList.motherBirthCertID
        , EmsObjectName.cardRequestList.motherName
        , EmsObjectName.cardRequestList.motherNID
    ]
});
