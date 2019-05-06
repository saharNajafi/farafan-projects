package com.gam.nocr.ems.config;

import com.gam.commons.core.BaseRuntimeExceptionCode;


/**
 * Collection of constant values representing web layer error codes and their corresponding messages (to log in console)
 *
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class WebExceptionCode extends BaseRuntimeExceptionCode {
    public static final String GLB_001 = "EMS_GLB_001";
    public static final String GLB_005 = "EMS_GLB_005";
    public static final String GLB_001_MSG = "Cannot access security context data.";
    public static final String GLB_002_MSG = "Cannot instantiate service {}";
    public static final String GLB_003_MSG = " Error in operation ";
    public static final String GLB_004_MSG = "Cannot initialize Delegator";
    public static final String GLB_005_MSG = "personalInfoWTO Input argument is null";
    public static final String GLB_ERR_MSG = "\nException was happened in the middle of the process. The stack trace is : ";

    //com.gam.nocr.ems.web.action.StateProviderAction
    public static final String SPA_001 = "EMS_A_SPA_001";
    public static final String SPA_002 = "EMS_A_SPA_002";

    //com.gam.nocr.ems.web.aspects.ExceptionHandler
    public static final String EXH_001 = "EMS_A_EXH_001";
    public static final String EXH_002 = "EMS_A_EXH_002";
    public static final String EXH_003 = "EMS_A_EXH_003";

    //com.gam.nocr.ems.web.action.PersonAction
    public static final String PEA_001 = "EMS_A_PEA_001";
    public static final String PEA_002 = "EMS_A_PEA_002";
    public static final String PEA_003 = "EMS_A_PEA_003";
    public static final String PEA_004 = "EMS_A_PEA_004";
    public static final String PEA_005 = "EMS_A_PEA_005";
    public static final String PEA_006 = "EMS_A_PEA_006";
    public static final String PEA_007 = "EMS_A_PEA_007";
    public static final String PEA_008 = "EMS_A_PEA_008";
    public static final String PEA_009 = "EMS_A_PEA_009";
    public static final String PEA_010 = "EMS_A_PEA_010";
    public static final String PEA_011 = "EMS_A_PEA_011";
    public static final String PEA_012 = "EMS_A_PEA_012";
    public static final String PEA_013 = "EMS_A_PEA_013";
    public static final String PEA_014 = "EMS_A_PEA_014";
    public static final String PEA_015 = "EMS_A_PEA_015";
    public static final String PEA_005_MSG = "invalid input entry, person id is empty";
    public static final String PEA_009_MSG = "invalid input entry, nothing send to process";
    
    //com.gam.nocr.ems.web.action.PersonTokenAction
    public static final String PTA_001 = "EMS_A_PTA_001";
    public static final String PTA_002 = "EMS_A_PTA_002";
    public static final String PTA_003 = "EMS_A_PTA_003";
    public static final String PTA_004 = "EMS_A_PTA_004";
    public static final String PTA_005 = "EMS_A_PTA_005";
    public static final String PTA_006 = "EMS_A_PTA_006";
    public static final String PTA_007 = "EMS_A_PTA_007";
    public static final String PTA_008 = "EMS_A_PTA_008";
    public static final String PTA_009 = "EMS_A_PTA_009";
    public static final String PTA_010 = "EMS_A_PTA_010";
    public static final String PTA_002_MSG = "invalid request, ids are null";
    
    //com.gam.nocr.ems.web.action.DepartmentAction
    public static final String DEA_001 = "EMS_A_DEA_001";
    public static final String DEA_002 = "EMS_A_DEA_002";

    //com.gam.nocr.ems.web.action.DocTypeAction
    public static final String DTA_001 = "EMS_A_DTA_001";
    public static final String DTA_002 = "EMS_A_DTA_002";
    public static final String DTA_003 = "EMS_A_DTA_003";
    public static final String DTA_004 = "EMS_A_DTA_004";
    public static final String DTA_002_MSG = "invalid input entry, document type id is empty";

    //com.gam.nocr.ems.web.action.SystemProfileAction
    public static final String SYA_001 = "EMS_A_SYA_001";
    public static final String SYA_002 = "EMS_A_SYA_002";
    public static final String SYA_003 = "EMS_A_SYA_003";
    public static final String SYA_004 = "EMS_A_SYA_004";
    public static final String SYA_005 = "EMS_A_SYA_005";
    public static final String SYA_006 = "EMS_A_SYA_006";

    //com.gam.nocr.ems.web.action.BlackListAction
    public static final String BLA_001 = "EMS_A_BLA_001";
    public static final String BLA_002 = "EMS_A_BLA_002";
    public static final String BLA_003 = "EMS_A_BLA_003";
    public static final String BLA_004 = "EMS_A_BLA_004";
    public static final String BLA_002_MSG = "invalid input entry, black list id is empty";

    //com.gam.nocr.ems.web.action.JobAction
    public static final String JBA_001 = "EMS_A_JBA_001";
    public static final String JBA_002 = "EMS_A_JBA_002";
    public static final String JBA_003 = "EMS_A_JBA_003";
    public static final String JBA_004 = "EMS_A_JBA_004";
    public static final String JBA_005 = "EMS_A_JBA_005";
    public static final String JBA_006 = "EMS_A_JBA_006";
    public static final String JBA_002_MSG = "HttpRequest is null. Cannot get an instance of SchedulerService.";
    public static final String JBA_003_MSG = "Unable to find scheduler in ServletContext.";

    //com.gam.nocr.ems.web.action.EnrollmentOfficeAction
    public static final String EOA_001 = "EMS_A_EOA_001";
    public static final String EOA_002 = "EMS_A_EOA_002";
    public static final String EOA_003 = "EMS_A_EOA_003";
    public static final String EOA_004 = "EMS_A_EOA_004";
    public static final String EOA_005 = "EMS_A_EOA_005";
    public static final String EOA_006 = "EMS_A_EOA_006";
    public static final String EOA_007 = "EMS_A_EOA_007";
    public static final String EOA_008 = "EMS_A_EOA_008";
    public static final String EOA_009 = "EMS_A_EOA_009";
    public static final String EOA_010 = "EMS_A_EOA_010";
    public static final String EOA_011 = "EMS_A_EOA_011";
    public static final String EOA_012 = "EMS_A_EOA_012";
    public static final String EOA_013 = "EMS_A_EOA_013";
    public static final String EOA_014 = "EMS_A_EOA_014";
    public static final String EOA_015 = "EMS_A_EOA_015";
    public static final String EOA_004_MSG = "invalid input entry, office id is empty";
    public static final String EOA_011_MSG = "The sent value which belongs to the '{}' is wrong.";
    public static final String EOA_015_MSG = "The office {} has sub office";

    //com.gam.nocr.ems.web.action.RatingInfoAction
    public static final String RIA_001 = "EMS_A_RIA_001";
    public static final String RIA_002 = "EMS_A_RIA_002";
    public static final String RIA_003 = "EMS_A_RIA_003";
    public static final String RIA_004 = "EMS_A_RIA_004";
    public static final String RIA_004_MSG = "invalid input entry, rating info id is empty";

    
    //com.gam.nocr.ems.web.action.HolidayListAction
    public static final String HLA_001 = "EMS_A_HLA_001";
    public static final String HLA_002 = "EMS_A_HLA_002";

    
    //com.gam.nocr.ems.web.action.WorkstationAction
    public static final String WSA_001 = "EMS_A_WSA_001";
    public static final String WSA_002 = "EMS_A_WSA_002";
    public static final String WSA_003 = "EMS_A_WSA_003";
    public static final String WSA_004 = "EMS_A_WSA_004";
    public static final String WSA_005 = "EMS_A_WSA_005";
    public static final String WSA_006 = "EMS_A_WSA_006";
    public static final String WSA_007 = "EMS_A_WSA_007";
    public static final String WSA_008 = "EMS_A_WSA_008";
    public static final String WSA_004_MSG = "invalid input entry, workstation id is empty";

    //com.gam.nocr.ems.web.action.DispatchingAction
    public static final String DPA_001 = "EMS_A_DPA_001";
    public static final String DPA_002 = "EMS_A_DPA_002";
    public static final String DPA_003 = "EMS_A_DPA_003";
    public static final String DPA_004 = "EMS_A_DPA_004";
    public static final String DPA_005 = "EMS_A_DPA_005";
    public static final String DPA_006 = "EMS_A_DPA_006";
    public static final String DPA_007 = "EMS_A_DPA_007";

    //com.gam.nocr.ems.web.action.UserAction
    public static final String USA_001 = "EMS_A_USA_001";
    public static final String USA_002 = "EMS_A_USA_002";
    
    // com.gam.nocr.ems.web.action.MessageAction
    public static final String MEA_001 = "EMS_A_MEA_001";
    public static final String MEA_002 = "EMS_A_MEA_002";
    public static final String MEA_003 = "EMS_A_MEA_003";
    public static final String MEA_002_MSG = "message could not find in db";
    
    // com.gam.nocr.ems.web.action.HelpAction
    public static final String HEA_001 = "EMS_A_HEA_001";
    public static final String HEA_002 = "EMS_A_HEA_002";
    public static final String HEA_003 = "EMS_A_HEA_003";
    public static final String HEA_004 = "EMS_A_HEA_004";
    public static final String HEA_005 = "EMS_A_HEA_005";
    public static final String HEA_006 = "EMS_A_HEA_006";
    public static final String HEA_007 = "EMS_A_HEA_007";
    public static final String HEA_008 = "EMS_A_HEA_008";
    public static final String HEA_007_MSG = "content type is unvalid";
    public static final String HEA_008_MSG = "the lenght of file is bigger than 5MB";
    
    // com.gam.nocr.ems.web.action.AboutAction
    public static final String ABA_001 = "EMS_A_ABA_001";
    public static final String ABA_002 = "EMS_A_ABA_002";
    
    //com.gam.nocr.ems.web.ws.EMSWS
    public static final String EMW_001 = "EMS_W_EMW_001";
    public static final String EMW_002 = "EMS_W_EMW_002";
    public static final String EMW_003 = "EMS_W_EMW_003";
    public static final String EMW_004 = "EMS_W_EMW_004";
    public static final String EMW_005 = "EMS_W_EMW_005";
    public static final String EMW_006 = "EMS_W_EMW_006";
    public static final String EMW_007 = "EMS_W_EMW_007";
    public static final String EMW_008 = "EMS_W_EMW_008";
    public static final String EMW_009 = "EMS_W_EMW_009";
    public static final String EMW_010 = "EMS_W_EMW_010";
    public static final String EMW_011 = "EMS_W_EMW_011";
    public static final String EMW_012 = "EMS_W_EMW_012";
    public static final String EMW_013 = "EMS_W_EMW_013";
    public static final String EMW_001_MSG = "Unable to execute validateRequest service.";
    public static final String EMW_002_MSG = "Unable to execute validateRequest service. No username specified.";
    public static final String EMW_003_MSG = "Unable to execute validateRequest service. User is not logged in.";
    public static final String EMW_004_MSG = "Unable to execute validateRequest service. No workstation id is specified.";
    public static final String EMW_005_MSG = "Unable to execute validateRequest service. No person found for specified username:";
    public static final String EMW_006_MSG = "Unable to execute validateRequest service. The specified username is not assigned to any enrollment office.Username:";
    public static final String EMW_007_MSG = "Unable to execute validateRequest service. No workstation found with activation code ";
    public static final String EMW_008_MSG = "Unable to execute validateRequest service. Workstation does not belong to the department that the user belongs to";
    public static final String EMW_010_MSG = "Unable to execute validateRequest service, ticket validation failed. this request either tempered or use session expired";
    public static final String EMW_011_MSG = "Unable to execute validateRequest service, office type validation failed. this request in not allowed from this office";
    public static final String EMW_012_MSG = "Incompatible ccos version";

    //com.gam.nocr.ems.web.ws.WSSecurity
    public static final String SCW_001 = "EMS_W_SCW_001";
    public static final String SCW_002 = "EMS_W_SCW_002";
    public static final String SCW_003 = "EMS_W_SCW_003";
    public static final String SCW_001_MSG = "unAuthorised.";

    //com.gam.nocr.ems.web.ws.CardWS
    public static final String CRW_001 = "EMS_W_CRW_001";
    public static final String CRW_002 = "EMS_W_CRW_002";
    public static final String CRW_003 = "EMS_W_CRW_003";
    public static final String CRW_004 = "EMS_W_CRW_004";
    public static final String CRW_005 = "EMS_W_CRW_005";
    public static final String CRW_006 = "EMS_W_CRW_006";
    public static final String CRW_007 = "EMS_W_CRW_007";
    public static final String CRW_008 = "EMS_W_CRW_008";
    public static final String CRW_009 = "EMS_W_CRW_009";
    public static final String CRW_001_MSG = "Unable to execute delivery service for request with id ";
    public static final String CRW_002_MSG = "Unable to execute notifyUnsuccessfulDelivery service for request with id ";
    public static final String CRW_007_MSG = "Cannot verify the requested certificate using OCSP";
    public static final String CRW_008_MSG = "Unable to execute retrieveDeliverMessage service";
    public static final String CRW_009_MSG = "Unable to doImsVerificationInDelivery in delivery progress";
    
    //Adldoost
    //com.gam.nocr.ems.web.ws.TokenWS
    public static final String TKW_001 = "EMS_W_TKW_001";
    public static final String TKW_002 = "EMS_W_TKW_002";
    public static final String TKW_003 = "EMS_W_TKW_003";
    public static final String TKW_004 = "EMS_W_TKW_004";
    public static final String TKW_005 = "EMS_W_TKW_005";
    public static final String TKW_006 = "EMS_W_TKW_006";
    public static final String TKW_007 = "EMS_W_TKW_007";
    public static final String TKW_001_MSG = "Unable to execute save renewal token request service for person id ";
    public static final String TKW_002_MSG = "Unable to execute remove renewal token request service for person id and request id ";
    public static final String TKW_003_MSG = "you are not allowed to delete token with state , type and person :";
    public static final String TKW_004_MSG = "This Person doesn't have any delivered signature token";
    public static final String TKW_005_MSG = "This Person has more than one signature token request";
    public static final String TKW_006_MSG = "This Person dosent't have renewal token request access";
    public static final String TKW_007_MSG = "The client date is not correct";
    
    //com.gam.nocr.ems.web.ws.DispatchWS
    public static final String DPW_001 = "EMS_W_DPW_001";
    public static final String DPW_002 = "EMS_W_DPW_002";
    public static final String DPW_003 = "EMS_W_DPW_003";
    public static final String DPW_004 = "EMS_W_DPW_004";
    public static final String DPW_005 = "EMS_W_DPW_005";
    public static final String DPW_001_MSG = "Unable to execute itemLost service for card ids ";
    public static final String DPW_002_MSG = "Unable to execute itemFound service for card ids ";
    public static final String DPW_003_MSG = "Unable to execute itemReceived service for card ids ";
    public static final String DPW_004_MSG = "Unable to execute itemNotReceived service for card ids ";
    public static final String DPW_005_MSG = "Unable to execute backToInitialState service for card ids ";

    //com.gam.nocr.ems.web.ws.EMSBasicInfoWS
    public static final String BIW_001 = "EMS_W_BIW_001";
    public static final String BIW_002 = "EMS_W_BIW_002";
    public static final String BIW_003 = "EMS_W_BIW_003";
    public static final String BIW_004 = "EMS_W_BIW_004";
    public static final String BIW_005 = "EMS_W_BIW_005";
    public static final String BIW_006 = "EMS_W_BIW_006";
    public static final String BIW_007 = "EMS_W_BIW_007";
    public static final String BIW_008 = "EMS_W_BIW_008";
    public static final String BIW_009 = "EMS_W_BIW_009";
    public static final String BIW_010 = "EMS_W_BIW_010";
    public static final String BIW_001_MSG = "no request information received to save workstation";
    public static final String BIW_002_MSG = "cannot convert WorkstationWTO to WorkstationTO";
    public static final String BIW_003_MSG = "failed to save new workstation request";
    public static final String BIW_004_MSG = "no request information received to save person";
    public static final String BIW_005_MSG = "cannot convert PersonWTO to PersonVTO";
    public static final String BIW_006_MSG = "failed to save new person request";
    public static final String BIW_007_MSG = "no request information received to change password";
    public static final String BIW_008_MSG = "cannot convert UserWTO to UserVTO";
    public static final String BIW_009_MSG = "failed to change password";
    public static final String BIW_010_MSG = "failed to retrieve user information";

    //com.gam.nocr.ems.web.ws.ListServiceWS
    public static final String LSW_002 = "EMS_W_LSW_002";
    public static final String LSW_003 = "EMS_W_LSW_003";
    public static final String LSW_004 = "EMS_W_LSW_004";
    public static final String LSW_002_MSG = "Can not instantiate ListService object.";
    public static final String LSW_003_MSG = "Exception happened while trying to execute ListService to fetch data.";
    public static final String LSW_004_MSG = "Permission Exception!. The current user does not have enough permission to fetch this list.";

    //com.gam.nocr.ems.web.ws.StateProviderWS
    public static final String SPW_002 = "EMS_W_SPW_002";
    public static final String SPW_003 = "EMS_W_SPW_003";
    public static final String SPW_005 = "EMS_W_SPW_005";
    public static final String SPW_006 = "EMS_W_SPW_006";
    public static final String SPW_002_MSG = "StateProviderParameterWTO parameter can not be null.";
    public static final String SPW_003_MSG = "Can not fetch state info for specified state ids.";
    public static final String SPW_005_MSG = "StateProviderParameterWTO parameter can not be null.";
    public static final String SPW_006_MSG = "Can not save state for specified state ids.";

    //com.gam.nocr.ems.web.ws.MESTransferWS
    public static final String MEW_001 = "EMS_W_MEW_001";
    public static final String MEW_002 = "EMS_W_MEW_002";
    public static final String MEW_001_MSG = "Unable to generate EncryptedCardRequestTO from submitted data";
    public static final String MEW_002_MSG = "Failed to save EncryptedCardRequest";

    //com.gam.nocr.ems.web.ws.UserManagementWS
    public static final String UMW_001 = "EMS_W_UMW_001";
    public static final String UMW_002 = "EMS_W_UMW_002";
    public static final String UMW_003 = "EMS_W_UMW_003";
    public static final String UMW_004 = "EMS_W_UMW_004";
    public static final String UMW_001_MSG = "Unable to find certificate";
    public static final String UMW_002_MSG = "Cannot convert to base 64";
    public static final String UMW_003_MSG = "Unable to change password. Reason unknown";
    public static final String UMW_004_MSG = "Unable to get Permissions. Reason unknown";
    
    //com.gam.nocr.ems.web.ws.EmksWS
    public static final String EKW_001 = "EMS_W_EKW_001";
    public static final String EKW_002 = "EMS_W_EKW_002";
    public static final String EKW_003 = "EMS_W_EKW_003";
    public static final String EKW_004 = "EMS_W_EKW_004";
    public static final String EKW_001_MSG = "Unable to get pins from EMKS. Reason unknown";
    public static final String EKW_002_MSG = "Unable to get Signiture from EMKS. Reason unknown";
    public static final String EKW_003_MSG = "Unable to get Signiture from EMKS. Reason unknown";
    public static final String EKW_004_MSG = "Unable to get MOC Keys from EMKS. Reason unknown";

    // com.gam.nocr.ems.web.ws.HelpWS
    public static final String HEW_001 = "EMS_W_HEW_001";
    public static final String HEW_002 = "EMS_W_HEW_002";
    public static final String HEW_003 = "EMS_W_HEW_003";
    public static final String HEW_004 = "EMS_W_HEW_004";
    public static final String HEW_005 = "EMS_W_HEW_005";
    public static final String HEW_006 = "EMS_W_HEW_006";
    public static final String HEW_001_MSG = "Unable to get about. Reason unknown";
    public static final String HEW_002_MSG = "Unable to download help file. Reason unknown";
    public static final String HEW_003_MSG = "Unable to download message file. Reason unknown";
    public static final String HEW_004_MSG = "Unable to download message detail. Reason unknown";
    public static final String HEW_005_MSG = "Message object is null. Reason unknown";
    public static final String HEW_006_MSG = "Unable to delete message. Reason unknown";

    //com.gam.nocr.ems.web.ws.RegistrationWS
    public static final String RSW_001 = "EMS_W_RSW_001";
    public static final String RSW_002 = "EMS_W_RSW_002";
    public static final String RSW_003 = "EMS_W_RSW_003";
    public static final String RSW_004 = "EMS_W_RSW_004";
    public static final String RSW_005 = "EMS_W_RSW_005";
    public static final String RSW_006 = "EMS_W_RSW_006";
    public static final String RSW_007 = "EMS_W_RSW_007";
    public static final String RSW_008 = "EMS_W_RSW_008";
    public static final String RSW_009 = "EMS_W_RSW_009";
    public static final String RSW_010 = "EMS_W_RSW_010";
    public static final String RSW_011 = "EMS_W_RSW_011";
    public static final String RSW_012 = "EMS_W_RSW_012";
    public static final String RSW_013 = "EMS_W_RSW_013";
    public static final String RSW_014 = "EMS_W_RSW_014";
    public static final String RSW_015 = "EMS_W_RSW_015";
    public static final String RSW_016 = "EMS_W_RSW_016";
    public static final String RSW_017 = "EMS_W_RSW_017";
    public static final String RSW_018 = "EMS_W_RSW_018";
    public static final String RSW_019 = "EMS_W_RSW_019";
    public static final String RSW_020 = "EMS_W_RSW_020";
    public static final String RSW_021 = "EMS_W_RSW_021";
    public static final String RSW_022 = "EMS_W_RSW_022";
    public static final String RSW_023 = "EMS_W_RSW_023";
    public static final String RSW_024 = "EMS_W_RSW_024";
    public static final String RSW_025 = "EMS_W_RSW_025";
    public static final String RSW_026 = "EMS_W_RSW_026";
    public static final String RSW_027 = "EMS_W_RSW_027";
    public static final String RSW_028 = "EMS_W_RSW_028";
    public static final String RSW_029 = "EMS_W_RSW_029";
    public static final String RSW_030 = "EMS_W_RSW_030";
    public static final String RSW_031 = "EMS_W_RSW_031";
    public static final String RSW_032 = "EMS_W_RSW_032";
    public static final String RSW_033 = "EMS_W_RSW_033";
    public static final String RSW_034 = "EMS_W_RSW_034";
    public static final String RSW_035 = "EMS_W_RSW_035";
    public static final String RSW_036 = "EMS_W_RSW_036";
    public static final String RSW_037 = "EMS_W_RSW_037";
    public static final String RSW_038 = "EMS_W_RSW_038";
    public static final String RSW_039 = "EMS_W_RSW_039";
    public static final String RSW_040 = "EMS_W_RSW_040";
    public static final String RSW_041 = "EMS_W_RSW_041";
    public static final String RSW_042 = "EMS_W_RSW_042";
    public static final String RSW_043 = "EMS_W_RSW_043";
    public static final String RSW_044 = "EMS_W_RSW_044";
    public static final String RSW_045 = "EMS_W_RSW_045";
    public static final String RSW_046 = "EMS_W_RSW_046";
    public static final String RSW_047 = "EMS_W_RSW_047";
    public static final String RSW_048 = "EMS_W_RSW_048";
    public static final String RSW_049 = "EMS_W_RSW_049";
    public static final String RSW_050 = "EMS_W_RSW_050";
    public static final String RSW_051 = "EMS_W_RSW_051";
    public static final String RSW_052 = "EMS_W_RSW_052";
    public static final String RSW_053 = "EMS_W_RSW_053";
    public static final String RSW_054 = "EMS_W_RSW_054";
    public static final String RSW_055 = "EMS_W_RSW_055";
    public static final String RSW_056 = "EMS_W_RSW_056";
    public static final String RSW_057 = "EMS_W_RSW_057";
    public static final String RSW_058 = "EMS_W_RSW_058";
    public static final String RSW_059 = "EMS_W_RSW_059";
    public static final String RSW_060 = "EMS_W_RSW_060";
    public static final String RSW_061 = "EMS_W_RSW_061";
    public static final String RSW_062 = "EMS_W_RSW_062";
    public static final String RSW_063 = "EMS_W_RSW_063";
    public static final String RSW_064 = "EMS_W_RSW_064";
    public static final String RSW_065 = "EMS_W_RSW_065";
    public static final String RSW_066 = "EMS_W_RSW_066";
    public static final String RSW_067 = "EMS_W_RSW_067";
    public static final String RSW_068 = "EMS_W_RSW_068";
    public static final String RSW_069 = "EMS_W_RSW_069";
    public static final String RSW_070 = "EMS_W_RSW_070";
    public static final String RSW_071 = "EMS_W_RSW_071";
    public static final String RSW_072 = "EMS_W_RSW_072";
    public static final String RSW_073 = "EMS_W_RSW_073";
    public static final String RSW_074 = "EMS_W_RSW_074";
    public static final String RSW_075 = "EMS_W_RSW_075";
    public static final String RSW_076 = "EMS_W_RSW_076";
    public static final String RSW_077 = "EMS_W_RSW_077";
    public static final String RSW_078 = "EMS_W_RSW_078";
    public static final String RSW_079 = "EMS_W_RSW_079";
    public static final String RSW_080 = "EMS_W_RSW_080";
    public static final String RSW_081 = "EMS_W_RSW_081";
    public static final String RSW_082 = "EMS_W_RSW_082";
    public static final String RSW_083 = "EMS_W_RSW_083";
    public static final String RSW_084 = "EMS_W_RSW_084";
    public static final String RSW_085 = "EMS_W_RSW_085";
    public static final String RSW_086 = "EMS_W_RSW_086";
    public static final String RSW_087 = "EMS_W_RSW_087";
    public static final String RSW_001_MSG = "Unable to convert CitizenWTO to CardRequestTO";
    public static final String RSW_002_MSG = "CitizenWTO should not be null";
    public static final String RSW_003_MSG = "Cannot save CitizenWTO which has request Id value, try update instead";
    public static final String RSW_004_MSG = "Failed to save request, cause unknown";
    public static final String RSW_005_MSG = "Cannot update CitizenWTO which has no request Id value, try save instead";
    public static final String RSW_006_MSG = "Unable to convert CitizenWTO to CardRequestTO";
    public static final String RSW_007_MSG = "{} is not a valid gender. Gender must be M or F.";
    public static final String RSW_008_MSG = "Failed to update registration request, cause unknown";
    public static final String RSW_009_MSG = "Unable to generate CitizenWTO from CardRequestTO and CitizenTO";
    public static final String RSW_010_MSG = "CardRequest cannot be found with given ID: ";
    public static final String RSW_011_MSG = "Citizen with this request ID cannot be found";
    public static final String RSW_012_MSG = "Unable to generate BiometricTO from BiometricWTO";
    public static final String RSW_013_MSG = "Unable to save Finger Info";
    public static final String RSW_014_MSG = "Cannot convert SpouseWTO to SpouseTO because SpouseWTO is null";
    public static final String RSW_015_MSG = "Problem when trying to set set spouseTO birth date from SpouseWTO";
    public static final String RSW_016_MSG = "Cannot convert ChildrenWTO to ChildTO because ChildrenWTO is null";
    public static final String RSW_017_MSG = "Unable to remove finger info ";
    public static final String RSW_018_MSG = "Unable to generate BiometricTO from BiometricWTO ";
    public static final String RSW_019_MSG = "Unable to save face Info ";
    public static final String RSW_020_MSG = "Face info not found for request id: ";
    public static final String RSW_021_MSG = "Multiple face info found for request id: ";
    public static final String RSW_022_MSG = "Unable to generate BiometricWTO from BiometricTO";
    public static final String RSW_023_MSG = "Unable to remove face info ";
    public static final String RSW_024_MSG = "Unable to generate DocumentTO from DocumentWTO";
    public static final String RSW_026_MSG = "Unable to generate DocumentTypeWTO from DocumentTypeTO with id: ";
    public static final String RSW_027_MSG = "Unable to authenticate document for request id ";
    public static final String RSW_028_MSG = "Unable to remove documents for request id ";
    public static final String RSW_029_MSG = "Unable to approve request id ";
    public static final String RSW_030_MSG = "BiometricWTOs cannot be null";
    public static final String RSW_031_MSG = "BiometricTO type cannot be null";
    public static final String RSW_032_MSG = "BiometricWTO must have a type of finger";
    public static final String RSW_033_MSG = "Unable to load card request with id ";
    public static final String RSW_034_MSG = "BiometricWTO must have a type of face";
    public static final String RSW_035_MSG = "BiometricWTOs cannot be empty";
    public static final String RSW_036_MSG = "Unable to generate DocumentWTO from DocumentTO";
    public static final String RSW_037_MSG = "BiometricWTO type cannot be null";
    public static final String RSW_038_MSG = "BiometricWTOs cannot be empty";
    public static final String RSW_039_MSG = "Unable to load citizen for card request with id ";
    public static final String RSW_040_MSG = "BiometricWTOs cannot be null";
    public static final String RSW_041_MSG = "{} is not a valid gender. Gender must be M or F.";
    public static final String RSW_042_MSG = "Cannot convert DocumentWTO to DocumentTO because DocumentWTO is null";
    public static final String RSW_043_MSG = "Citizen with this request ID cannot be found";
    public static final String RSW_044_MSG = "Problem when generating CitizenWTO. Request reason cannot be null when request type is REPLACE";
    public static final String RSW_045_MSG = "Problem when generating CitizenWTO. Request reason is invalid for request type REPLACE. Reason may be DESTROYED or IDENTITY_CHANGED";
    public static final String RSW_046_MSG = "Problem when converting CitizenWTO type. Type is invalid";
    public static final String RSW_047_MSG = "Problem when converting CitizenWTO state. State is invalid";
    public static final String RSW_048_MSG = "Cannot convert SpouseTO to SpouseWTO because SpouseTO is null";
    public static final String RSW_049_MSG = "Cannot convert SpouseTO to SpouseWTO. Problem setting SpouseTO birth date";
    public static final String RSW_050_MSG = "Cannot convert ChildTO to ChildrenWTO because ChildTO is null";
    public static final String RSW_051_MSG = "BiometricWTO cannot be null";
    public static final String RSW_052_MSG = "Problem when converting Biometric type. Type is invalid";
    public static final String RSW_053_MSG = "BiometricTO cannot be null";
    public static final String RSW_054_MSG = "Unable to get document for request id ";
    public static final String RSW_055_MSG = "Cannot convert DocumentTO to DocumentWTO because DocumentTO is null";
    public static final String RSW_056_MSG = "Unable to get face info for request id ";
    public static final String RSW_057_MSG = "Unable to get service requests for service type ";
    public static final String RSW_058_MSG = "Items array contains a null value";
    public static final String RSW_059_MSG = "Items array contains an element with a null key";
    public static final String RSW_060_MSG = "Items array contains an invalid key";
    public static final String RSW_061_MSG = "Unable to search for citizen with national id ";
    public static final String RSW_062_MSG = "Citizen with the following attributes not found: ";
    public static final String RSW_063_MSG = "Unable to generate CitizenWTO";
    public static final String RSW_064_MSG = "Unable to convert certificate to Base64";
    public static final String RSW_065_MSG = "Unable to convert CitizenWTO to CardRequestTO";
    public static final String RSW_066_MSG = "Cannot save CitizenWTO which has request Id value, try update instead";
    public static final String RSW_067_MSG = "Unable to generate BiometricTO from BiometricWTO";
    public static final String RSW_068_MSG = "BiometricTO type cannot be null";
    public static final String RSW_069_MSG = "Fingers must have a 'finger' type";
    public static final String RSW_070_MSG = "Unable to generate BiometricTO from BiometricWTO";
    public static final String RSW_071_MSG = "BiometricWTO type cannot be null";
    public static final String RSW_072_MSG = "BiometricWTO must have a 'face' type";
    public static final String RSW_073_MSG = "Unable to generate DocumentTO from DocumentWTO";
    public static final String RSW_074_MSG = "Failed to save request, cause unknown";
    public static final String RSW_075_MSG = "Unable to get/find the certificate for CCOS";
    public static final String RSW_076_MSG = "Cannot convert to base 64";
    public static final String RSW_077_MSG = "unexpected exception happened while trying to fetch archive id";
    public static final String RSW_080_MSG = "Unable to save face items. Number of face items received from CCOS is less than 4 image";
    public static final String RSW_081_MSG = "Unable to convert CitizenWTO to CardRequestTO in preRegistration VIP";
    public static final String RSW_086_MSG = "the feature extractor id normal is empty";
    public static final String RSW_087_MSG = "the feature extractor id cc is empty";



    // com.gam.nocr.ems.web.ws.RegistrationVipWS
    public static final String RVW_001 = "EMS_W_RVW_001";
    public static final String RVW_002 = "EMS_W_RVW_002";
    public static final String RVW_003 = "EMS_W_RVW_003";
    public static final String RVW_004 = "EMS_W_RVW_004";
    public static final String RVW_005 = "EMS_W_RVW_005";
    public static final String RVW_007 = "EMS_W_RVW_007";
    public static final String RVW_008 = "EMS_W_RVW_008";
    public static final String RVW_009 = "EMS_W_RVW_009";
    public static final String RVW_011 = "EMS_W_RVW_011";
    public static final String RVW_012 = "EMS_W_RVW_012";
    public static final String RVW_013 = "EMS_W_RVW_013";
    public static final String RVW_014 = "EMS_W_RVW_014";
    public static final String RVW_015 = "EMS_W_RVW_015";
    public static final String RVW_016 = "EMS_W_RVW_016";
    public static final String RVW_017 = "EMS_W_RVW_017";
    public static final String RVW_018 = "EMS_W_RVW_018";
    public static final String RVW_019 = "EMS_W_RVW_019";
    public static final String RVW_020 = "EMS_W_RVW_020";
    public static final String RVW_021 = "EMS_W_RVW_021";
    public static final String RVW_022 = "EMS_W_RVW_022";
    public static final String RVW_023 = "EMS_W_RVW_023";
    public static final String RVW_024 = "EMS_W_RVW_024";
    public static final String RVW_001_MSG = "Failed to save vip registration request, cause unknown";
    public static final String RVW_002_MSG = "Failed to get vip document types, cause unknown";
    public static final String RVW_003_MSG = "Failed to convert vip document types, cause unknown";
    public static final String RVW_004_MSG = "Failed in getCredentials, cause unknown";
    public static final String RVW_005_MSG = "Failed to convert citizenTo to cardRequestTo, cause unknown";
    public static final String RVW_007_MSG = "Failed to convert finger info, cause unknown";
    public static final String RVW_008_MSG = "finger biometric type is null";
    public static final String RVW_009_MSG = "finger biometric type is unvalid";
    public static final String RVW_011_MSG = "face biometric type is null";
    public static final String RVW_012_MSG = "face biometric type is unvalid";
    public static final String RVW_013_MSG = "Failed to convert documents, cause unknown";
    public static final String RVW_014_MSG = "Failed to convert father birth date";
    public static final String RVW_015_MSG = "Failed to convert mother birth date";
    public static final String RVW_017_MSG = "Failed to get photo vip, reason is unknown";
    public static final String RVW_018_MSG = "Finger info can not be null or empty";
    public static final String RVW_019_MSG = "personal info has null or empty value";
    public static final String RVW_020_MSG = "Face biometrics can not be null";
    public static final String RVW_021_MSG = "Face biometrics must be equal to 5";    
    public static final String RVW_022_MSG = "Failed to get citizn birthDate";    
    //com.gam.nocr.ems.web.action.EncCardRequestAction
    public static final String ECR_001 = "EMS_W_ECR_001";
    public static final String ECR_002 = "EMS_W_ECR_002";
    public static final String ECR_001_MSG = "Invalid input entry, EncryptedCardRequestTO id is empty";

    //com.gam.nocr.ems.web.action.BusinessLogAction
    public static final String BGA_001 = "EMS_A_BGA_001";

    //com.gam.nocr.ems.web.action.ReportManagementAction
    public static final String RMA_001 = "EMS_A_RMA_001";
    public static final String RMA_002 = "EMS_A_RMA_002";
    public static final String RMA_003 = "EMS_A_RMA_003";
    public static final String RMA_004 = "EMS_A_RMA_004";
    public static final String RMA_005 = "EMS_A_RMA_005";
    public static final String RMA_006 = "EMS_A_RMA_006";
    public static final String RMA_007 = "EMS_A_RMA_007";
    public static final String RMA_008 = "EMS_A_RMA_008";
    public static final String RMA_009 = "EMS_A_RMA_009";
    public static final String RMA_010 = "EMS_A_RMA_010";
    public static final String RMA_011 = "EMS_A_RMA_011";
    public static final String RMA_012 = "EMS_A_RMA_012";
    public static final String RMA_013 = "EMS_A_RMA_013";
    public static final String RMA_014 = "EMS_A_RMA_014";
    public static final String RMA_015 = "EMS_A_RMA_015";
    public static final String RMA_005_MSG = "Invalid input entry. ReportIds are null.";
    public static final String RMA_011_MSG = "Invalid input entry. The value specified for new state of given report is not valid.";
    public static final String RMA_014_MSG = "Invalid input entry. The value specified for report id is not valid.";
    public static final String RMA_015_MSG = "Invalid input entry. No value specified for report id in order to fetch its metadata.";

    //com.gam.nocr.ems.web.action.ReportRequestAction
    public static final String RRA_001 = "EMS_A_RRA_001";
    public static final String RRA_002 = "EMS_A_RRA_002";
    public static final String RRA_003 = "EMS_A_RRA_003";
    public static final String RRA_004 = "EMS_A_RRA_004";
    public static final String RRA_005 = "EMS_A_RRA_005";
    public static final String RRA_006 = "EMS_A_RRA_006";
    public static final String RRA_004_MSG = "Error in date conversion";

    //com.gam.nocr.ems.web.action.CardRequestAction
    public static final String CRA_001 = "EMS_A_CRA_001";
    public static final String CRA_002 = "EMS_A_CRA_002";
    public static final String CRA_003 = "EMS_A_CRA_003";
    public static final String CRA_004 = "EMS_A_CRA_004";
    public static final String CRA_005 = "EMS_A_CRA_005";
    public static final String CRA_006 = "EMS_A_CRA_006";
    public static final String CRA_007 = "EMS_A_CRA_007";
    public static final String CRA_008 = "EMS_A_CRA_008";
    public static final String CRA_009 = "EMS_A_CRA_009";
    public static final String CRA_010 = "EMS_A_CRA_010";
    public static final String CRA_011 = "EMS_A_CRA_011";
    public static final String CRA_012 = "EMS_A_CRA_012";
    public static final String CRA_013 = "EMS_A_CRA_013";
    public static final String CRA_014 = "EMS_A_CRA_014";
    public static final String CRP_001 = "EMS_P_CRP_001";
//    public static final String CRA_009 = "EMS_A_CRA_009";
//    public static final String CRA_010 = "EMS_A_CRA_010";
    public static final String CRA_013_MSG="invalid input entry, request id is empty";
    
    //com.gam.nocr.ems.web.ws.RequestWS
    public static final String RQW_001 = "EMS_W_RQW_001";
    public static final String RQW_002 = "EMS_W_RQW_002";
    public static final String RQW_003 = "EMS_W_RQW_003";
    public static final String RQW_004 = "EMS_W_RQW_004";
    public static final String RQW_001_MSG = "unexpected exception happened while trying to fetch archive id";
    public static final String RQW_002_MSG = "unexpected exception happened while trying to retrieve tracking number";
    public static final String RQW_003_MSG = "unexpected exception happened while trying to perform repeal action";
    public static final String RQW_004_MSG = "unexpected exception happened while trying to perform transfer action";

    //com.gam.nocr.ems.web.ws.ReportWS
    public static final String RPW_001 = "EMS_W_RPW_001";
    public static final String RPW_002 = "EMS_W_RPW_002";
    public static final String RPW_003 = "EMS_W_RPW_003";
    public static final String RPW_004 = "EMS_W_RPW_004";
    public static final String RPW_001_MSG = "unexpected exception happened while trying to fetch report metadata";
    public static final String RPW_002_MSG = "unexpected exception happened while trying to submit report request";
    public static final String RPW_003_MSG = "unexpected exception happened while trying to remove report request";
    public static final String RPW_004_MSG = "unexpected exception happened while trying to load generated report";
    
    //com.gam.nocr.ems.web.action.LostCardAction    
    public static final String LCA_001 = "EMS_A_LCA_001";
    public static final String LCA_002 = "EMS_A_LCA_002";
    
    //com.gam.nocr.ems.web.action.LostBatchAction
    public static final String LBA_001 = "EMS_A_LBA_001";
    public static final String LBA_002 = "EMS_A_LBA_002";
    //com.gam.nocr.ems.web.ws.MessageWS
    public static final String MSW_001 = "EMS_W_MSW_001";
    public static final String MSW_002 = "EMS_W_MSW_002";
    
    
    public static final String MSW_001_MSG = "Unable to get count messages";
    public static final String MSW_002_MSG = "Unable to get content message";

    //com.gam.nocr.ems.web.ws.EmsWorkStationPlatformManagementWS
    public static final String WST_008 = "EMSWorkstationPMService0008";
    public static final String WST_009 = "EMSWorkstationPMService0009";
    public static final String WST_010 = "EMSWorkstationPMService0010";
    public static final String WST_011 = "EMSWorkstationPMService0011";
    public static final String WST_012 = "EMSWorkstationPMService0012";
    public static final String WST_013 = "EMSWorkstationPMService0013";
    public static final String WST_014 = "EMSWorkstationPMService0014";
    public static final String EMSWorkstationPMService0008 = "IP address format is invalid";
    public static final String EMSWorkstationPMService0009 = "MAC address could not be empty";
    public static final String EMSWorkstationPMService0010 = "MAC address is too short";
    public static final String EMSWorkstationPMService0011 = "MAC address is too long";
    public static final String EMSWorkstationPMService0012 = "OS version could not be empty";
    public static final String EMSWorkstationPMService0013 = "Username could not be empty";
    public static final String EMSWorkstationPMService0014 = "Computer name could not be empty";

    //com.gam.nocr.ems.web.ws.WS.CardRequestStateWS
    public static final String CRQW_001 = "EMS_W_CRQW_001";
    public static final String CRQW_002 = "EMS_W_CRQW_002";
    public static final String CRQW_003 = "EMS_W_CRQW_003";

    public static final String CRQW_001_MSG = "unexpected exception happened while trying to perform checkCardRequestState action";
    public static final String CRQW_002_MSG = "unexpected exception happened while trying to perform checkBirthCertificateSerial action";
    public static final String CRQW_003_MSG = "unexpected exception happened while trying to perform checkTrackingId action";

    //com.gam.nocr.ems.web.action.OfficeCapacityAction
    public static final String OFC_001 = "EMS_A_OFC_001";
    public static final String OFC_002 = "EMS_A_OFC_002";
    public static final String OFC_003 = "EMS_A_OFC_003";
    public static final String OFC_001_MSG = "invalid input entry, officeCapacity id is empty";

    //com.gam.nocr.ems.web.ws.CCOSPaymentWS.java
    public  static final String CPW_001 = "EMS_W_CPW_001";
    public  static final String CPW_002 = "EMS_W_CPW_002";
    public  static final String CPW_003 = "EMS_W_CPW_003";
    public  static final String CPW_004 = "EMS_W_CPW_004";
    public  static final String CPW_005 = "EMS_W_CPW_005";
    public  static final String CPW_006 = "EMS_W_CPW_006";
    public  static final String CPW_007 = "EMS_W_CPW_007";
    public  static final String CPW_008 = "EMS_W_CPW_008";
    public  static final String CPW_009 = "EMS_W_CPW_009";
    public  static final String CPW_010 = "EMS_W_CPW_010";
    public  static final String CPW_011 = "EMS_W_CPW_011";
    public  static final String CPW_012 = "EMS_W_CPW_012";
    public  static final String CPW_013 = "EMS_W_CPW_013";
    public  static final String CPW_014 = "EMS_W_CPW_014";
    public  static final String CPW_015 = "EMS_W_CPW_015";
    public  static final String CPW_016 = "EMS_W_CPW_016";
    public  static final String CPW_017 = "EMS_W_CPW_017";
    public  static final String CPW_018 = "EMS_W_CPW_018";
    public  static final String CPW_019 = "EMS_W_CPW_019";
    public  static final String CPW_020 = "EMS_W_CPW_020";
    public  static final String CPW_001_MSG = "Unable to check the possibility of inserting single stag";
    public  static final String CPW_002_MSG = "Unable to check enrollment office eligibility for single stage enrollment";
    public static final String CPW_003_MSG = "Invalid Parameter -> nationalId :";
    public static final String CPW_005_MSG = "not exists card request with citizen nationalId";
    public static final String CPW_006_MSG = "IMS SubSystem Has Exception";
    public static final String CPW_007_MSG = "IMS SubSystem unable/has problem to respond now";
    public static final String CPW_008_MSG = "RegistrationPaymentWTO argument is null";
    public static final String CPW_009_MSG = "Error occurred in transferring Single-Stage-PreRegistration";
    public static final String CPW_010_MSG = "Error occurred in save Payment Info";
    public static final String CPW_011_MSG = "Error occurred in get Payment Amount";
    public static final String CPW_012_MSG = "Error occurred in has Citizen Successful Payment";
    public static final String CPW_013_MSG = "Error occurred in transfer Single Stage PreRegistration To Ems";
    public static final String CPW_014_MSG = "Error occurred in retrieve Payment orderId";
    public static final String CPW_015_MSG = "Error occurred assign Payment To Enrollment";
    public static final String CPW_016_MSG = "an error occurred on convert To registrationPaymentTO TO  RegistrationPaymentTO";
    public static final String CPW_017_MSG = "an error occurred on convert To singlePreRegistrationWTO TO ReservationTO";
    public static final String CPW_018_MSG = "an error occurred in imsInquiry WebService";
    public static final String CPW_019_MSG = "PersonalInfoWTO is null";
    public static final String CPW_020_MSG = "PersonEnquiryWTO is null";

    //com.gam.nocr.ems.web.action.OfficeSettingAction.java
    public static final String OSA_001 = "EMS_A_OSA_001";
    public static final String OSA_002 = "EMS_A_OSA_002";
    public static final String OSA_003 = "EMS_A_OSA_003";
    public static final String OSA_002_MSG = "invalid input entry, enrollment office id is empty";

    //com.gam.nocr.ems.web.action.FeatureExtractIdsAction.java
    public static final String FEI_001 = "EMS_A_FEI_001";
    public static final String FEI_002 = "EMS_A_FEI_002";
    public static final String FEI_001_MSG = "invalid input entry, enrollment office id is empty";
}
