/**
 * Created by IntelliJ IDEA.
 * User: Mousavi
 * Date: 5/3/12
 * Time: 1:07 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.OfficeModel', {
    extend: 'Ext.data.Model',
    fields: [

        EmsObjectName.officeNewEdit.oficName
        , EmsObjectName.officeNewEdit.oficCode
        , EmsObjectName.officeNewEdit.oficMeter
        , EmsObjectName.officeNewEdit.oficFax
        , EmsObjectName.officeNewEdit.oficTel
        , EmsObjectName.officeNewEdit.oficTokenExport
        , EmsObjectName.officeNewEdit.oficSuperRegisterOffice
        , EmsObjectName.officeNewEdit.oficSuperRegisterOfficeId
        , EmsObjectName.officeNewEdit.oficPostCode
        , EmsObjectName.officeNewEdit.oficAddress
        , EmsObjectName.officeNewEdit.oficRating
        , EmsObjectName.officeNewEdit.oficRatingId
        
        ,EmsObjectName.officeNewEdit.uploadPhoto
        ,EmsObjectName.officeNewEdit.fingerScanSingleMode
        ,EmsObjectName.officeNewEdit.disabilityMode
        ,EmsObjectName.officeNewEdit.reIssueRequest
        ,EmsObjectName.officeNewEdit.elderlyMode
        ,EmsObjectName.officeNewEdit.nMocGeneration
        ,EmsObjectName.officeNewEdit.amputationAnnouncment
        ,EmsObjectName.officeNewEdit.useScannerUI
        ,EmsObjectName.officeNewEdit.allowEditBackground
        

        , EmsObjectName.officeNewEdit.mangName
        //,EmsObjectName.officeNewEdit.mangNameId
        , EmsObjectName.officeNewEdit.mangTel
        , EmsObjectName.officeNewEdit.mangId
        , EmsObjectName.officeNewEdit.mangMobil

        , EmsObjectName.officeNewEdit.tokenId
        , EmsObjectName.officeNewEdit.tokenStatus
//        , EmsObjectName.officeNewEdit.substitutionOfficeName

        , EmsObjectName.officeNewEdit.location
        , EmsObjectName.officeNewEdit.locationId

        , EmsObjectName.officeNewEdit.workingHoursStartId
        , EmsObjectName.officeNewEdit.workingHoursFinishId

        , EmsObjectName.officeNewEdit.officeType
        , EmsObjectName.officeNewEdit.officeDeliver
        , EmsObjectName.officeNewEdit.khosusiType

        , EmsObjectName.officeNewEdit.superiorOfficeName
        , EmsObjectName.officeNewEdit.superiorOfficeId
        , EmsObjectName.officeNewEdit.fqdn
        , EmsObjectName.officeNewEdit.calenderType
        , EmsObjectName.officeNewEdit.provinceName
        , EmsObjectName.officeNewEdit.officeDepPhoneNumber
        , EmsObjectName.officeNewEdit.thursdayMorningActive
        , EmsObjectName.officeNewEdit.thursdayEveningActive
        , EmsObjectName.officeNewEdit.fridayMorningActive
        , EmsObjectName.officeNewEdit.fridayEveningActive
        , EmsObjectName.officeNewEdit.singleStageOnly
        , EmsObjectName.officeNewEdit.hasStair
        , EmsObjectName.officeNewEdit.hasElevator
        , EmsObjectName.officeNewEdit.hasPortabilityEquipment
        
    ]
});
