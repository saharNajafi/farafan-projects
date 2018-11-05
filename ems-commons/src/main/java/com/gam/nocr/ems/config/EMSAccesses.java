package com.gam.nocr.ems.config;

import gampooya.tools.security.AbstractAccessList;


/**
 * Collection of constant values representing permissions used in services. These values are all defined in GAAS
 * subsystem access list
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class EMSAccesses extends AbstractAccessList {

    public static final String IS_ADMIN = "ems_admin";
    public static final String EMS_VIEW_USER_LIST = "ems_viewUserList";
    public static final String EMS_VIEW_ENROLLMENT_OFFICE_LIST = "ems_viewEnrollmentOfficeList";
    public static final String EMS_VIEW_WORKSTATION_LIST = "ems_viewWorkstationList";
    public static final String EMS_VIEW_DISPATCH_LIST = "ems_viewDispatchList";
    public static final String EMS_VIEW_PERSON_TOKEN_LIST = "ems_viewPersonTokenList";
    public static final String EMS_VIEW_CARD_REQUEST_LIST = "ems_viewCardRequestList";
    public static final String EMS_VIEW_DEPARTMENT_LIST = "ems_viewDepartmentList";
    public static final String EMS_VIEW_DEPARTMENT_USER_LIST = "ems_viewDepartmentUserList";
    public static final String EMS_VIEW_RATING_LIST = "ems_viewRatingList";
    public static final String EMS_VIEW_BIZ_LOG_LIST = "ems_viewBizLogList";
    public static final String EMS_VIEW_BLACK_LIST = "ems_viewBlackList";
    public static final String EMS_VIEW_DOC_TYPE_LIST = "ems_viewDocTypeList";
    public static final String EMS_VIEW_EMS_CARD_REQUEST_LIST = "ems_viewEmsCardRequestList";
    public static final String EMS_VIEW_JOB_LIST = "ems_viewJobList";
    public static final String EMS_VIEW_SYSTEM_PROFILE_LIST = "ems_viewSystemProfileList";
    public static final String EMS_VIEW_REPORT_RESULT_LIST = "ems_viewReportResultList";
    public static final String EMS_VIEW_PROVINCE_LIST = "ems_addCardRequest";
    public static final String EMS_VIEW_REPORT_MANAGEMENT_LIST = "ems_viewReportManagementList";
    public static final String EMS_VIEW_REPORT_REQUEST_LIST = "ems_viewReportRequestList";
    public static final String EMS_VIEW_CCOS_USER_LIST = "ems_viewCcosUserList";
    public static final String EMS_VIEW_CCOS_WORKSTATION_LIST = "ems_viewCcosWorkstationList";
    public static final String EMS_VIEW_OFFICE_CAPACITY_LIST = "ems_viewOfficeCapacityList";
//    public static final String EMS_VIEW_OFFICE_SETTING_LIST = "ems_viewOfficeSettingList";
    //Anbari
    public static final String EMS_VIEW_CMS_PRODUCTION_ERROR_LIST = "ems_cmsErrorProduction";
    public static final String EMS_VIEW_HOLIDAY_LIST = "ems_holiday";
    public static final String EMS_VIEW_ESTELAM2_FALSE_LIST = "ems_estelam2False";

    public static final String EMS_ADD_RATING = "ems_addRating";
    public static final String EMS_VIEW_RATING = "ems_viewRating";
    public static final String EMS_REMOVE_RATING = "ems_removeRating";

    //message
    public static final String EMS_VIEW_MESSAGE_LIST = "ems_viewMessageList";
    public static final String EMS_CCOS_VIEW_MESSAGE_LIST = "ems_viewCcosMessageList";
    
    //help
    public static final String EMS_VIEW_HELP_LIST = "ems_viewHelpList";
    public static final String EMS_VIEW_CCOS_HELP_LIST = "ems_viewCcosHelpList";
    //lost card
    public static final String EMS_LOST_CARD_LIST = "ems_viewCardLost";
    

    private static final EMSAccesses _instance = new EMSAccesses();

    private EMSAccesses() {
        loadPermissionList();
    }

    protected String getAccessFileAddress() {
        return "com/gam/nocr/ems/config/EMSAccesses.properties";
    }
}