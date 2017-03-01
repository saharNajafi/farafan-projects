package com.gam.nocr.ems.config;

/**
 * Collection of constant values used in different layers and classes
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class ConstValues {

    /**
     * ****************************************************************************************************************
     * ****************************************************************************************************************
     * Pattern:
     * 		public static final String EJB_CapitalizedServiceInterfaceWithoutService = "Service interface name"
     *
     * Sample: If the name of the service is "BusinessRulesService", the constant value should be created like below
     * 		public static final String EJB_BUSINESSRULES_SERVICE = "BusinessRulesService";
     * ****************************************************************************************************************
     * ****************************************************************************************************************
     */

    /**
     * Name of BusinessRulesService to call
     */
//	public static final String EJB_BUSINESSRULES_SERVICE = "BusinessRulesService";
//	public static final String EJB_MAIL_SERVICE = "MailService";
//	public static final String EJB_REGISTRATION_SERVICE = "RegistrationService";
//	public static final String EJB_TRACKING_SERVICE = "TrackingService";

    private static final String EMS_SERVICES_EJB_IMPL = "ems-services-ejb-impl/";
    private static final String EMS_DAO_JPA_IMPL = "ems-dao-jpa-impl/";

    public static final String EJB_BUSINESSRULES_SERVICE = "BusinessRulesService";
    public static final String EJB_MAIL_SERVICE = "MailService";
    public static final String EJB_REGISTRATION_SERVICE = EMS_SERVICES_EJB_IMPL + "RegistrationService!com.gam.nocr.ems.biz.service.internal.impl.RegistrationService";
    public static final String EJB_TRACKING_SERVICE = EMS_SERVICES_EJB_IMPL + "TrackingService!com.gam.nocr.ems.biz.service.internal.impl.TrackingService";
    public static final String EJB_USER_SERVICE = "UserService";
    public static final String EJB_WORKSTATION_SERVICE = "WorkstationService";


    public static final String EJB_CITIZEN_DAO = EMS_DAO_JPA_IMPL + "CitizenDAO!com.gam.nocr.ems.data.dao.impl.CitizenDAO";

    public static final String GAM_ERROR_WITH_LIMITED_RETRY = "GAM_EWLR:";
    public static final String GAM_ERROR_WITH_UNLIMITED_RETRY = "GAM_EWUR:";
    public static final String GAM_ERROR_WITH_NO_RETRY = "GAM_EWNR:";

    public static String DEFAULT_NAMES_FA = "\u0646\u062F\u0627\u0631\u062F";
    public static String DEFAULT_NAMES_EN = "NA";
    public static String DEFAULT_GENDER = "UNDEFINED";
    public static String DEFAULT_NUMBER = "0";
    public static String DEFAULT_CERT_SERIAL = "000000";
    public static String DEFAULT_NID = "0000000000";
    public static String DEFAULT_DATE = "1211/01/01";

}
