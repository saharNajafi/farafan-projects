package com.gam.nocr.ems.config;

import com.gam.commons.core.BaseRuntimeExceptionCode;

/**
 * Collection of constant values representing mapper error codes and their corresponding messages (to log in console)
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class MapperExceptionCode extends BaseRuntimeExceptionCode {

    public static final String GLB_001_MSG = "Problem in converting the date";

    //com.gam.nocr.ems.data.mapper.tomapper.CardRequestMapper
    public static final String CRM_001 = "EMS_M_CRM_001";
    public static final String CRM_002 = "EMS_M_CRM_002";
    public static final String CRM_003 = "EMS_M_CRM_003";
    public static final String CRM_004 = "EMS_M_CRM_004";
    public static final String CRM_005 = "EMS_M_CRM_005";
    public static final String CRM_006 = "EMS_M_CRM_006";
    public static final String CRM_007 = "EMS_M_CRM_007";
    public static final String CRM_008 = "EMS_M_CRM_008";
    public static final String CRM_009 = "EMS_M_CRM_009";
    public static final String CRM_010 = "EMS_M_CRM_010";
    public static final String CRM_011 = "EMS_M_CRM_011";
    public static final String CRM_012 = "EMS_M_CRM_012";
    public static final String CRM_013 = "EMS_M_CRM_013";
    public static final String CRM_014 = "EMS_M_CRM_014";
    public static final String CRM_015 = "EMS_M_CRM_015";
    public static final String CRM_016 = "EMS_M_CRM_016";
    public static final String CRM_017 = "EMS_M_CRM_017";
    public static final String CRM_018 = "EMS_M_CRM_018";
    public static final String CRM_019 = "EMS_M_CRM_019";
    public static final String CRM_020 = "EMS_M_CRM_020";


    public static final String CRM_001_MSG = "CitizenVTO should not be null.";
    public static final String CRM_002_MSG = "Invalid Type. Problem in converting the CitizenVTO type";
    public static final String CRM_003_MSG = "Invalid State. Problem in converting the CitizenVTO state";
    public static final String CRM_005_MSG = "{} is not a valid gender. Gender must be M or F.";
    public static final String CRM_008_MSG = "CitizenWTO should not be null";
    public static final String CRM_009_MSG = "Problem when converting CitizenWTO type. Type is invalid";
    public static final String CRM_010_MSG = "Problem when converting CitizenWTO state. State is invalid";
    public static final String CRM_012_MSG = "Citizen with this request ID cannot be found";
    public static final String CRM_013_MSG = "Problem when generating CitizenWTO. Request reason cannot be null when request type is REPLACE";
    public static final String CRM_014_MSG = "Problem when generating CitizenWTO. Request reason is invalid for request type REPLACE. Reason may be DESTROYED or IDENTITY_CHANGED";
    public static final String CRM_017_MSG = "Living is null or 0";
    public static final String CRM_018_MSG = "getUserCityType is null";
    public static final String CRM_019_MSG = "LivingCityId is null";
    public static final String CRM_020_MSG = "LivingVillageId is null";




    //com.gam.nocr.ems.data.mapper.tomapper.SpouseMapper
    public static final String SPM_001 = "EMS_M_SPM_001";
    public static final String SPM_002 = "EMS_M_SPM_002";
    public static final String SPM_003 = "EMS_M_SPM_003";
    public static final String SPM_004 = "EMS_M_SPM_004";
    public static final String SPM_005 = "EMS_M_SPM_005";
    public static final String SPM_001_MSG = "SpouseVTO should not be null.";
    public static final String SPM_003_MSG = "Cannot convert SpouseWTO to SpouseTO because SpouseWTO is null";
    public static final String SPM_004_MSG = "Cannot convert SpouseTO to SpouseWTO because SpouseTO is null";
    public static final String SPM_005_MSG = "Cannot convert SpouseTO to SpouseVTO because SpouseTO is null";

    //com.gam.nocr.ems.data.mapper.tomapper.ChildMapper
    public static final String CHM_001 = "EMS_M_CHM_001";
    public static final String CHM_002 = "EMS_M_CHM_002";
    public static final String CHM_003 = "EMS_M_CHM_003";
    public static final String CHM_004 = "EMS_M_CHM_004";
    public static final String CHM_005 = "EMS_M_CHM_005";
    public static final String CHM_006 = "EMS_M_CHM_006";
    public static final String CHM_007 = "EMS_M_CHM_007";
    public static final String CHM_008 = "EMS_M_CHM_007";
    public static final String CHM_001_MSG = "ChildVTO should not be null.";
    public static final String CHM_004_MSG = "{} is not a valid gender. Gender must be M or F.";
    public static final String CHM_005_MSG = "Cannot convert ChildrenWTO to ChildTO because ChildrenWTO is null";
    public static final String CHM_007_MSG = "Cannot convert ChildTO to ChildrenWTO because ChildTO is null";
    public static final String CHM_008_MSG = "Cannot convert ChildTO to ChildVTO because ChildTO is null";


    //com.gam.nocr.ems.data.mapper.tomapper.DocumentMapper
    public static final String DOM_001 = "EMS_M_DOM_001";
    public static final String DOM_002 = "EMS_M_DOM_002";
    public static final String DOM_001_MSG = "Cannot convert DocumentWTO to DocumentTO because DocumentWTO is null";
    public static final String DOM_002_MSG = "Cannot convert DocumentTO to DocumentWTO because DocumentTO is null";

    //com.gam.nocr.ems.data.mapper.tomapper.BiometricMapper
    public static final String BIM_001 = "EMS_M_BIM_001";
    public static final String BIM_002 = "EMS_M_BIM_002";
    public static final String BIM_003 = "EMS_M_BIM_003";
    public static final String BIM_004 = "EMS_M_BIM_004";
    public static final String BIM_005 = "EMS_M_BIM_005";
    public static final String BIM_006 = "EMS_M_BIM_006";
//    public static final String BIM_007 = "EMS_M_BIM_007";
    public static final String BIM_001_MSG = "BiometricWTO cannot be null";
    public static final String BIM_002_MSG = "Problem when converting Biometric type. Type is invalid";
    public static final String BIM_003_MSG = "BiometricTO cannot be null";
    public static final String BIM_004_MSG = "Cannot convert BiometricWTO to BiometricTO";
    public static final String BIM_005_MSG = "Cannot convert BiometricTO to BiometricWTO";
    public static final String BIM_006_MSG = "Meta data item cannot be null";
//    public static final String BIM_007_MSG = "Data for photo vip cannot be null";
    
    //com.gam.nocr.ems.data.mapper.tomapper.PersonMapper
    public static final String PRM_001 = "EMS_M_PRM_001";
    public static final String PRM_002 = "EMS_M_PRM_002";
    public static final String PRM_003 = "EMS_M_PRM_003";
    public static final String PRM_004 = "EMS_M_PRM_004";
    public static final String PRM_005 = "EMS_M_PRM_005";
    public static final String PRM_006 = "EMS_M_PRM_006";
    public static final String PRM_007 = "EMS_M_PRM_007";
    public static final String PRM_008 = "EMS_M_PRM_008";
    public static final String PRM_009 = "EMS_M_PRM_009";
    public static final String PRM_010 = "EMS_M_PRM_010";
    public static final String PRM_011 = "EMS_M_PRM_011";
    public static final String PRM_001_MSG = "cannot initialize value list handler";
    public static final String PRM_002_MSG = "loading list encounter problem";
    public static final String PRM_003_MSG = "invalid input entry, department id is null";
    public static final String PRM_004_MSG = "invalid input entry, department id does not have valid type";
    public static final String PRM_005_MSG = "password encoding failed";
    public static final String PRM_006_MSG = "Incorrect password";
    public static final String PRM_007_MSG = "password couldn't be empty";
    public static final String PRM_008_MSG = "roles must be filled";
    public static final String PRM_009_MSG = "invalid roles or permissions entry";
    public static final String PRM_011_MSG = "it is forbidden to change administrator user name";

    //com.gam.nocr.ems.data.mapper.tomapper.ReportMapper
    public static final String RPM_001 = "EMS_M_RPM_001";

    //com.gam.nocr.ems.data.mapper.tomapper.ReportRequestMapper
    public static final String RRM_001 = "EMS_M_RRM_001";
    
    //com.gam.nocr.ems.data.mapper.tomapper.PersonTokenMapper
    public static final String PTM_001 = "EMS_M_PTM_001";
    public static final String PTM_001_MSG = "invalid personToken, id is null";
    //com.gam.nocr.ems.web.ws.CCOSPaymentWS.java
    public static final String CPM_001 = "EMS_CP_001";
    public static final String CPM_002 = "EMS_CPM_002";
    public static final String CPM_003 = "EMS_CPM_003";
    public static final String CPM_004 = "EMS_CPM_004";
    public static final String CPM_005 = "EMS_CPM_005";
    public static final String CPM_006 = "EMS_CPM_006";
    public static final String CPM_007 = "EMS_CPM_007";
    public static final String CPM_008 = "EMS_CPM_008";
    public static final String CPM_009 = "EMS_CPM_009";
    public static final String CPM_010 = "EMS_CPM_010";
    public static final String CPM_011 = "CPM_011_MSG";
    public static final String CPM_012 = "CPM_012_MSG";
    public static final String CPM_014 = "CPM_014_MSG";
    public static final String CPM_015 = "CPM_015_MSG";
    public static final String CPM_016 = "CPM_016_MSG";
    public static final String CPM_017 = "CPM_017_MSG";
    public static final String CPM_018 = "CPM_018_MSG";
    public static final String CPM_019 = "CPM_019_MSG";
    public static final String CPM_020 = "CPM_020_MSG";
    public static final String CPM_021 = "CPM_021_MSG";
    public static final String CPM_022 = "CPM_022_MSG";
    public static final String CPM_023 = "CPM_023_MSG";
    public static final String CPM_024 = "CPM_024_MSG";
    public static final String CPM_025 = "CPM_025_MSG";
    public static final String CPM_001_MSG = "PreRegistration should not be null.";
    public static final String CPM_002_MSG = "Invalid NationalId for SingleStagePreRegistration with TrackingId: {0}";
    public static final String CPM_003_MSG = "Invalid Origin";
    public static final String CPM_004_MSG = "Invalid Cert Serial Number";
    public static final String CPM_005_MSG = "Invalid Cellphone Number";
    public static final String CPM_006_MSG = "Invalid Mother Name";
    public static final String CPM_007_MSG = "Invalid Gregorian BirthDate";
    public static final String CPM_008_MSG = "Invalid Lunar BirthDate";
    public static final String CPM_009_MSG = "Invalid Gender";
    public static final String CPM_010_MSG = "Invalid Religion";
    public static final String CPM_011_MSG = "PreRegistration should not be null.";
    public static final String CPM_012_MSG = "PreRegistration ID should not be empty.";
    public static final String CPM_014_MSG = "PreRegistration CreatedOn should not be null";
    public static final String CPM_015_MSG = "Invalid NationalId for PreRegistration with ID: {0}";
    public static final String CPM_016_MSG = "Invalid Tracking Id";
    public static final String CPM_017_MSG = "Invalid Origin";
    public static final String CPM_018_MSG = "Invalid Gregorian BirthDate";
    public static final String CPM_019_MSG = "Invalid Lunar BirthDate";
    public static final String CPM_020_MSG = "Invalid Gender";
    public static final String CPM_021_MSG = "Invalid Religion";
    public static final String CPM_022_MSG = "Invalid Cellphone Number";
    public static final String CPM_023_MSG = "Invalid Cert Serial Number";
    public static final String CPM_024_MSG = "Invalid Mother Name";
    public static final String CPM_025_MSG = "Invalid Payment";




}
