package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.EnrollmentOfficeDelegator;
import com.gam.nocr.ems.biz.delegator.PersonDelegator;
import com.gam.nocr.ems.biz.delegator.WorkstationDelegator;
import com.gam.nocr.ems.biz.service.GAASService;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.DepartmentTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.WorkstationTO;
import com.gam.nocr.ems.data.domain.ws.SecurityContextWTO;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeDeliverStatus;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeType;
import com.gam.nocr.ems.util.EmsUtil;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.gam.nocr.ems.config.EMSLogicalNames.SRV_GAAS;
import static com.gam.nocr.ems.config.EMSLogicalNames.getExternalServiceJNDIName;

/**
 * Base class for all CCOS web services. It has some base methods (e.g. authorization) that may be used in child classes
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class EMSWS {

    private static final Logger logger = BaseLog.getLogger(EMSWS.class);
    private static final String DEFAULT_CCOS_EXACT_VERSION = "2.0.2.23";
    private static final String DEFAULT_ENABLE_CCOS_CHECK = "1";


    public UserProfileTO validateCCOSUser(SecurityContextWTO securityContextWTO, Logger logger) throws InternalException {
        UserProfileTO userProfileTO;
        try {
            userProfileTO = validateRequest(securityContextWTO);
        } catch (InternalException internalException) {
            logger.error(internalException.getMessage(), internalException.getFaultInfo()
                    .getCode(), internalException);
            throw internalException;
        }
        return userProfileTO;
    }

    /**
     * Validates the username, personID and the workstation that the request is coming from. The workstation should
     * belongs to the same enrollment office that current user is member of
     *
     * @param securityContextWTO The login and session information of the user
     * @return The {@link com.gam.commons.core.data.domain.UserProfileTO} object containing information of current user
     * that can be passed by to any delegator. In case of any error or invalid data, an exception would be
     * thrown
     * @throws InternalException
     */
    public UserProfileTO validateRequest(SecurityContextWTO securityContextWTO) throws InternalException {
        try {
            if (securityContextWTO == null || securityContextWTO.getUsername() == null || securityContextWTO.getUsername().trim().length() == 0) {
                throw new InternalException(WebExceptionCode.EMW_002_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_002));
            }

            if (securityContextWTO.getTicket() == null || securityContextWTO.getTicket().trim().length() == 0) {
                throw new InternalException(WebExceptionCode.EMW_003_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_003));
            }

            if (securityContextWTO.getWorkstationID() == null || securityContextWTO.getWorkstationID().trim().length() == 0) {
                throw new InternalException(WebExceptionCode.EMW_004_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_004));
            }

            if (securityContextWTO.getCcosVersion() == null || !validateCCOSMinVersion(securityContextWTO.getCcosVersion())) {
                throw new InternalException(WebExceptionCode.EMW_012_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_013));
            }

            String username = securityContextWTO.getUsername();
            UserProfileTO userProfileTO = new UserProfileTO();
            userProfileTO.setUserName(username);

            // **************** Anbari : caching personID
            Long personID = getPersonService().findPersonIdByUsername(username);
            //Long personID = getGaasService().getPersonID(userProfileTO);
            if (personID == null || personID == 0) {
                throw new InternalException(WebExceptionCode.EMW_005_MSG + securityContextWTO.getUsername(), new EMSWebServiceFault(WebExceptionCode.EMW_005));
            }
            userProfileTO.setPersonID(personID);

            //*******************

            PersonDelegator delegator = new PersonDelegator();
            DepartmentTO departmentTO = delegator.loadDepartmentByPersonId(userProfileTO, personID);
            if (departmentTO == null || departmentTO.getId() == null || departmentTO.getId() == 0) {
                throw new InternalException(WebExceptionCode.EMW_006_MSG + securityContextWTO.getUsername(), new EMSWebServiceFault(WebExceptionCode.EMW_006));
            }
            userProfileTO.setDepID(departmentTO.getId());
            userProfileTO.setDepName(departmentTO.getName());

            //Workstation must be related to User enrollment office.
            WorkstationTO workstation = new WorkstationDelegator().findByDepartmentIdAndActivationCode(userProfileTO, departmentTO.getId(), securityContextWTO.getWorkstationID());

            if (workstation == null) {
                throw new InternalException(WebExceptionCode.EMW_007_MSG + securityContextWTO.getWorkstationID(),
                        new EMSWebServiceFault(WebExceptionCode.EMW_007));
            }
            if (!departmentTO.getId().equals(workstation.getEnrollmentOffice().getId())) {
                throw new InternalException(WebExceptionCode.EMW_008_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_008));
            }

            if (!getGaasService().validateTicket(securityContextWTO.getUsername(), securityContextWTO.getTicket())) {
                throw new InternalException(WebExceptionCode.EMW_010_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_010));
            }

            return userProfileTO;
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()), e);
        } catch (InternalException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.EMW_001_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_001), e);
        }
    }


    //Anbari
    private Boolean validateCCOSMinVersion(String currentCcosVersion) {
        Integer isCcosVersionCheck = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_ENABLE_CCOS_VERSION_CHECK, DEFAULT_ENABLE_CCOS_CHECK));
        if (isCcosVersionCheck == 0 || "vip".equals(currentCcosVersion)) { // not checking ccos version
            return true;
        } else {
            List<String> ccosVersions = getCompatibleClientVerList();
            return versionsCompare(currentCcosVersion, ccosVersions);
        }
    }

    public List<String> getCompatibleClientVerList() {
        List<String> newVerCode = new ArrayList();
        try {
            String ccosExactVersions = String.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_CCOS_EXACT_VERSION, DEFAULT_CCOS_EXACT_VERSION));
            ccosExactVersions = removeInvalidItems(ccosExactVersions);
            ccosExactVersions.replaceAll("\\s+", "");
            String[] verCode = ccosExactVersions.split(",");
            String temp;
            for (int m = 0; m < verCode.length; m++) {
                temp = verCode[m].trim();
                if (!temp.isEmpty()) {
                    newVerCode.add(temp);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception occured in get compatible client version list:", ex);
        }
        return newVerCode;
    }

    private String removeInvalidItems(String record) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] verCode = record.split(",");
        String temp;
        for (int i = 0; i < verCode.length; i++) {
            temp = verCode[i].trim();
            if (Pattern.compile("^\\d+((\\.\\d+))*$").matcher(temp).matches()
                    ||
                    Pattern.compile("^\\d+((\\.\\d))*+(.\\*)$").matcher(temp).matches()) {
                stringBuilder.append(temp).append(",");
            }
        }

        String validItems = stringBuilder.toString().trim();
        if (validItems.isEmpty())
            return "";

        return validItems.substring(0, validItems.length() - 1);
    }
//
//
//    private static int versionCompare(String str1, String str2) {
//        String[] vals1 = str1.split("\\.");
//        String[] vals2 = str2.split("\\.");
//        int i = 0;
//        // set index to first non-equal ordinal or length of shortest version string
//        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
//            i++;
//        }
//        // compare first non-equal ordinal number
//        if (i < vals1.length && i < vals2.length) {
//            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
//            return Integer.signum(diff);
//        }
//        // the strings are equal or one string is a substring of the other
//        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
//        return Integer.signum(vals1.length - vals2.length);
//    }

    private static boolean versionsCompare(String ccosVersion, List<String> validVersions) {
        String[] currentVersion = ccosVersion.split("\\.");
        //check if any of current version is compatible with any of exciting versions
        for (String validVersion : validVersions) {
            String[] version = validVersion.split("\\.");
            int i = 0;
            // check if current number in both strings are either equals or valid versions current character is *
            while (i < version.length && (currentVersion[i].equals(version[i]) || version[i].equals("*"))) {
                i++;
            }
            if (i == version.length) {
                //when we find the first version that current version is compatible with, our search ends and we return true
                return true;
            }
        }
        //if none of the versions compatible with current version we return false
        return false;
    }

    /**
     * Indicates whether the type of enrollment office of the user is NOCR or not. It would be used by the CCOS in its
     * startup process in order to detect the type of menu item that should be displayed in GUI
     *
     * @param userProfileTO The {@link com.gam.commons.core.data.domain.UserProfileTO} of the user to check its
     *                      office type
     * @throws InternalException
     */
    public void isNocrOffice(UserProfileTO userProfileTO) throws InternalException {
        try {
            EnrollmentOfficeTO enrollmentOfficeTO = new EnrollmentOfficeDelegator().find(userProfileTO.getDepID());
            if (!EnrollmentOfficeType.NOCR.equals(enrollmentOfficeTO.getType()))
                throw new InternalException(WebExceptionCode.EMW_011_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_011));
        } catch (BaseException e) {
            throw new InternalException(e.getExceptionCode(), new EMSWebServiceFault(e.getExceptionCode()), e);
        }
    }

    public void isNocrOfficeOrDeliveryOffice(UserProfileTO userProfileTO) throws InternalException {
        try {
            EnrollmentOfficeTO enrollmentOfficeTO = new EnrollmentOfficeDelegator().find(userProfileTO.getDepID());
            if (!EnrollmentOfficeType.NOCR.equals(enrollmentOfficeTO.getType())) {
                if (EnrollmentOfficeDeliverStatus.DISABLED.name().equals(enrollmentOfficeTO.getDeliver()))
                    throw new InternalException(WebExceptionCode.EMW_011_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_012));
            }
        } catch (BaseException e) {
            throw new InternalException(e.getExceptionCode(), new EMSWebServiceFault(e.getExceptionCode()), e);
        }
    }


    /**
     * Returns a reference to the GAAS service in order to call its web services
     *
     * @return reference to the {@link com.gam.nocr.ems.biz.service.GAASService} implementation
     * @throws InternalException
     */
    private GAASService getGaasService() throws InternalException {
        try {
            return ServiceFactoryProvider.getServiceFactory().getService(getExternalServiceJNDIName(SRV_GAAS), null);
        } catch (ServiceFactoryException e) {
            throw new InternalException(WebExceptionCode.GLB_002_MSG,
                    new EMSWebServiceFault(WebExceptionCode.EMW_009), e);
        }
    }

    //Anbari
    private PersonManagementService getPersonService() throws BaseException {
        PersonManagementService personManagementService;
        try {
            personManagementService = (PersonManagementService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_PERSON), null);
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.PDL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_PERSON.split(","));
        }
        return personManagementService;
    }

    public void throwInternalException(String code, String message, Object[] args, Exception e, Logger logger) throws InternalException {
        if (args != null) {
            message = MessageFormat.format(message, args);
        }
        InternalException internalException = new InternalException(message, new EMSWebServiceFault(code));
        logger.error(internalException.getMessage(), internalException.getFaultInfo()
                .getCode(), e);
        throw internalException;
    }

    public void throwInternalException(String code, String message, Exception e, Logger logger) throws InternalException {
        this.throwInternalException(code, message, null, e, logger);
    }

    public void throwInternalException(String code, String message, Logger logger) throws InternalException {
        this.throwInternalException(code, message, null, null, logger);
    }
}