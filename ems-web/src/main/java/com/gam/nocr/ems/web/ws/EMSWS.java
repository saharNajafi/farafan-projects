package com.gam.nocr.ems.web.ws;

import static com.gam.nocr.ems.config.EMSLogicalNames.SRV_GAAS;
import static com.gam.nocr.ems.config.EMSLogicalNames.getExternalServiceJNDIName;

import org.slf4j.Logger;

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

/**
 * Base class for all CCOS web services. It has some base methods (e.g. authorization) that may be used in child classes
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class EMSWS {

    private static final Logger logger = BaseLog.getLogger(EMSWS.class);
    private static final String DEFAULT_CCOS_EXACT_VERSION  = "2.0.2.23";
    private static final String DEFAULT_ENABLE_CCOS_CHECK  = "1";

    /**
     * Validates the username, personID and the workstation that the request is coming from. The workstation should
     * belongs to the same enrollment office that current user is member of
     *
     * @param securityContextWTO The login and session information of the user
     * @return  The {@link com.gam.commons.core.data.domain.UserProfileTO} object containing information of current user
     *          that can be passed by to any delegator. In case of any error or invalid data, an exception would be
     *          thrown
     * @throws InternalException
     */
    
    
    
    
   
    
    public UserProfileTO validateRequest(SecurityContextWTO securityContextWTO) throws InternalException {
        try {
        	
        	
            if (securityContextWTO == null || securityContextWTO.getUsername() == null || securityContextWTO.getUsername().trim().length() == 0)
                throw new InternalException(WebExceptionCode.EMW_002_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_002));

            if (securityContextWTO.getTicket() == null || securityContextWTO.getTicket().trim().length() == 0)
                throw new InternalException(WebExceptionCode.EMW_003_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_003));

            if (securityContextWTO.getWorkstationID() == null || securityContextWTO.getWorkstationID().trim().length() == 0)
                throw new InternalException(WebExceptionCode.EMW_004_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_004));            
            
            if (securityContextWTO.getCcosVersion() == null || !validateCCOSMinVersion(securityContextWTO.getCcosVersion()))
                throw new InternalException(WebExceptionCode.EMW_012_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_013));

            String username = securityContextWTO.getUsername();
            UserProfileTO userProfileTO = new UserProfileTO();
            userProfileTO.setUserName(username);
            
            // **************** Anbari : caching personID
            Long personID = getPersonService().findPersonIdByUsername(username);
            //Long personID = getGaasService().getPersonID(userProfileTO);
            if (personID == null || personID == 0)
                throw new InternalException(WebExceptionCode.EMW_005_MSG + securityContextWTO.getUsername(), new EMSWebServiceFault(WebExceptionCode.EMW_005));
            userProfileTO.setPersonID(personID);
                        
           //*******************

            PersonDelegator delegator = new PersonDelegator();
            DepartmentTO departmentTO = delegator.loadDepartmentByPersonId(userProfileTO, personID);
            if (departmentTO == null || departmentTO.getId() == null || departmentTO.getId() == 0)
                throw new InternalException(WebExceptionCode.EMW_006_MSG + securityContextWTO.getUsername(), new EMSWebServiceFault(WebExceptionCode.EMW_006));
            userProfileTO.setDepID(departmentTO.getId());
            userProfileTO.setDepName(departmentTO.getName());

            //Workstation must be related to User enrollment office.
            WorkstationTO workstation = new WorkstationDelegator().findByDepartmentIdAndActivationCode(userProfileTO, departmentTO.getId(), securityContextWTO.getWorkstationID());

            if (workstation == null)
                throw new InternalException(WebExceptionCode.EMW_007_MSG + securityContextWTO.getWorkstationID(),
                        new EMSWebServiceFault(WebExceptionCode.EMW_007));
            if (!departmentTO.getId().equals(workstation.getEnrollmentOffice().getId()))
                throw new InternalException(WebExceptionCode.EMW_008_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_008));

            if (!getGaasService().validateTicket(securityContextWTO.getUsername(), securityContextWTO.getTicket()))
                throw new InternalException(WebExceptionCode.EMW_010_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_010));

            return userProfileTO;
        } catch (BaseException e) {
            throw new InternalException(e.getExceptionCode(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (InternalException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.EMW_001_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_001), e);
        }
    } 
     

    //Anbari
    private Boolean validateCCOSMinVersion(String ccosVersion) {
    	
    	String ccosExactVersion = String.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_CCOS_EXACT_VERSION,DEFAULT_CCOS_EXACT_VERSION));
    	Integer isCcosVersionCheck = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_ENABLE_CCOS_VERSION_CHECK,DEFAULT_ENABLE_CCOS_CHECK));
    	if(isCcosVersionCheck == 0 || "vip".equals(ccosVersion)) // not checking ccos version
    		return true;
    	else if(ccosExactVersion.equals(ccosVersion) || versionCompare(ccosExactVersion,ccosVersion)== -1 )
    		return true;
    	else 
    		return false;
    	
	}

    public static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }





	/**
     * Indicates whether the type of enrollment office of the user is NOCR or not. It would be used by the CCOS in its
     * startup process in order to detect the type of menu item that should be displayed in GUI
     *
     * @param userProfileTO    The {@link com.gam.commons.core.data.domain.UserProfileTO} of the user to check its
     *                         office type
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
            if (!EnrollmentOfficeType.NOCR.equals(enrollmentOfficeTO.getType()))
            {
            	if (EnrollmentOfficeDeliverStatus.DISABLED.name().equals(enrollmentOfficeTO.getDeliver()))
            		throw new InternalException(WebExceptionCode.EMW_011_MSG, new EMSWebServiceFault(WebExceptionCode.EMW_012));
            }
        } catch (BaseException e) {
            throw new InternalException(e.getExceptionCode(), new EMSWebServiceFault(e.getExceptionCode()), e);
        }
    }
    

    /**
     * Returns a reference to the GAAS service in order to call its web services
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
            personManagementService = (PersonManagementService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_PERSON),null);
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.PDL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_PERSON.split(","));
        }
        return personManagementService;
    }
}