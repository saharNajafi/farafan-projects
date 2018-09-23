package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.profile.ProfileManager;
import com.gam.commons.stateprovider.StateProviderTO;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.biz.service.UserManagementService;
import com.gam.nocr.ems.config.*;
import com.gam.nocr.ems.data.dao.EnrollmentOfficeDAO;
import com.gam.nocr.ems.data.dao.OfficeSettingDAO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.OfficeSettingTO;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeDeliverStatus;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeType;
import gampooya.tools.security.SecurityContextService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
@Stateless(name = "StateProviderService")
@Local(StateProviderServiceLocal.class)
@Remote(StateProviderServiceRemote.class)
public class StateProviderServiceImpl extends EMSAbstractService implements StateProviderServiceLocal, StateProviderServiceRemote {

    private static final Logger logger = BaseLog.getLogger(StateProviderServiceImpl.class);

    protected Class[] paramsType = new Class[]{StateProviderTO.class};

    public List<StateProviderTO> getState(String moduleName, List<String> stateIds) {
        if (stateIds == null || stateIds.size() == 0) {
            return null;
        }
        List<StateProviderTO> result = new ArrayList<StateProviderTO>(stateIds.size());
        for (String stateId : stateIds) {
            if (stateId != null && stateId.trim().length() != 0) {
                StateProviderTO stateProviderTO = null;
                try {
                    stateProviderTO = fetchState(stateId);
                } catch (Exception ex) {
                    EMSLog.getLogger(this.getClass()).error("Exception happened while trying to find stateId:" + stateId + ".", ex);
                    stateProviderTO.setStateId(stateId);
                    stateProviderTO.setValue("{\"hidden\":false}");
                }
                result.add(stateProviderTO);
            }
        }

        return result;
    }

    public void setState(String moduleName, List<StateProviderTO> records) throws BaseException {
        if (records == null || records.size() == 0) {
            return;
        }

        for (StateProviderTO stateProviderTO : records) {
            try {
                ProfileManager manager = ProfileHelper.getProfileManager();
                Long personID = getPersonService().findPersonIdByUsername(userProfileTO.getUserName());
                manager.setProfile(ProfileKeyName.STATE_ROOT + "." + stateProviderTO.getStateId(),
                        ProfileKeyName.STATE_ROOT, stateProviderTO.getValue(), false, null, Integer.parseInt("" + personID));
            } catch (Exception ex) {
                throw new ServiceException("", "", ex);
            }
        }
    }

    private StateProviderTO fetchState(String stateId) {
        if (stateId == null)
            return null;
        StateProviderTO stateProviderTO = new StateProviderTO();
        stateProviderTO.setStateId(stateId);
        try {
            SecurityContextService securityContextService = new SecurityContextService();
            ProfileManager profileManager = ProfileHelper.getProfileManager();
            Long personID = getPersonService().findPersonIdByUsername(userProfileTO.getUserName());
            Integer perId = null;
            try {
                perId = Integer.parseInt("" + personID);
            } catch (Exception pex) {
                logger.error(pex.getMessage(), pex);
                //Person Id will be null and it is supposed to be not needed in this case.
            }
            String value;
            if (stateId.endsWith("Grid")) {
                value = (String) profileManager.getProfile(ProfileKeyName.STATE_ROOT + "." + stateId, false, null, perId);
                if (value != null && value.trim().length() > 0)
                    stateProviderTO.setValue(value);
            } else if (stateId.startsWith("ccos.")) {
                OfficeSettingTO officeSettingTO = getOfficeSettingDAO().findByOfficeId(getUserProfileTO().getDepID());
                if (stateId.endsWith("currentDate")) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    String date = format.format(new Date());
                    /*date = DateUtil.convert(new Date(), DateUtil.GREGORIAN);*/
                    stateProviderTO.setValue(date);
                } else if (stateId.endsWith("officeType") || stateId.endsWith("enrollmentOfficeId")) {
                    Long userDepartmentId = getUserProfileTO().getDepID();
                    EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class, userDepartmentId);

                    if (stateId.endsWith("officeType"))
                        stateProviderTO.setValue(enrollmentOfficeTO.getType().name());
                    else if (stateId.endsWith("enrollmentOfficeId"))
                        stateProviderTO.setValue(enrollmentOfficeTO.getId().toString());
                }
                //Anbari
                else if (stateId.endsWith("isDeliver")) {
                    Long userDepartmentId = getUserProfileTO().getDepID();
                    EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class, userDepartmentId);
                    if (EnrollmentOfficeType.OFFICE.equals(enrollmentOfficeTO.getType()) && EnrollmentOfficeDeliverStatus.ENABLED.equals(enrollmentOfficeTO.getDeliver()))
                        stateProviderTO.setValue("1");
                    else
                        stateProviderTO.setValue("0");
                } else if (stateId.endsWith("isVIP")) {
                    try {
                        long depID = userProfileTO.getDepID();
                        EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class, depID);
                        String enrollmentOfficeCode = enrollmentOfficeTO.getCode();
                        ProfileManager pm = ProfileHelper.getProfileManager();
                        String codeVip = (String) pm.getProfile(ProfileKeyName.KEY_VIP_ENROLLMENT_OFFICE, true, null, null);
                        if (enrollmentOfficeCode.equals(codeVip))
                            stateProviderTO.setValue("1");
                        else
                            stateProviderTO.setValue("0");
                    } catch (Exception e) {

                        EMSLog.getLogger(this.getClass()).error("Exception happened while trying to find stateId:" + "isVip" + ".", e);
                        stateProviderTO.setStateId("isVip");
                        stateProviderTO.setValue("0");

                    }

                }
                /**
                 * edited by Madanipour
                 * check some setting that should be set for each office
                 */

                else if (stateId.endsWith("canImportFaceFromFile")) {

                    if (officeSettingTO.getUploadPhoto()) {

                        stateProviderTO.setValue("true");
                    } else {
                        stateProviderTO.setValue("false");
                    }

                } else if (stateId.endsWith("forceTenprintUnsegmentedCapture")) {

                    if (officeSettingTO.getFingerScanSingleMode()) {
                        stateProviderTO.setValue("false");
                    } else {
                        stateProviderTO.setValue("true");
                    }


                } else if (stateId.endsWith("elderlyMode")) {

                    if (officeSettingTO.getElderlyMode()) {

                        stateProviderTO.setValue("true");
                    } else {
                        stateProviderTO.setValue("false");
                    }

                } else if (stateId.endsWith("icaoTestsEnabled")) {

                    if (officeSettingTO.getNonConfirmingIcaoImage()) {

                        stateProviderTO.setValue("false");
                    } else {
                        stateProviderTO.setValue("true");
                    }

                } else if (stateId.endsWith("disabilityMode")) {

                    if (officeSettingTO.getDisabilityMode()) {

                        stateProviderTO.setValue("true");
                    } else {
                        stateProviderTO.setValue("false");
                    }

                } else if (stateId.endsWith("ReIssueRequest")) {

                    if (officeSettingTO.getReissueRequest()) {
                        stateProviderTO.setValue("1");
                    } else {
                        stateProviderTO.setValue("0");
                    }

                } else if (stateId
                        .endsWith("allowChangeFingerStatusDuringCapture")) {

                    if (officeSettingTO.getAmputationAnnouncment())
                        stateProviderTO.setValue("true");
                    else
                        stateProviderTO.setValue("false");

                } else if (stateId
                        .endsWith("allowAmputatedFingerStatus")) {

                    if (officeSettingTO.getAmputationAnnouncment())
                        stateProviderTO.setValue("true");
                    else
                        stateProviderTO.setValue("false");

                } else if (stateId
                        .endsWith("nMocGeneration")) {

                    if (officeSettingTO.getnMocGeneration())
                        stateProviderTO.setValue("1");
                    else
                        stateProviderTO.setValue("0");

                } else if (stateId
                        .endsWith("allowEditBackground")) {

                    if (officeSettingTO.getAllowEditBackground())
                        stateProviderTO.setValue("true");
                    else
                        stateProviderTO.setValue("false");

                } else if (stateId
                        .endsWith("useScannerUI")) {

                    if (officeSettingTO.getUseScannerUI())
                        stateProviderTO.setValue("true");
                    else
                        stateProviderTO.setValue("false");

                } else if (stateId.endsWith("tokenExpire")) {
                    try {
                        ProfileManager pm = ProfileHelper.getProfileManager();
                        String tokenExpireDateValue = (String) pm.getProfile(ProfileKeyName.Signature_Token_Expire_Notification_Days, true, null, null);
                        stateProviderTO.setValue(tokenExpireDateValue);
                    } catch (Exception e) {

                        EMSLog.getLogger(this.getClass()).error("Exception happened while trying to find stateId:" + "ccosTokenExpire" + ".", e);
                        stateProviderTO.setStateId("ccosTokenExpire");
                        stateProviderTO.setValue("21");

                    }

                } /*else if (stateId.endsWith("featureExtractorID")) {
                    if (!StringUtils.isEmpty(officeSettingTO.getFeatureExtractorID()))
                        stateProviderTO.setValue(officeSettingTO.getFeatureExtractorID());
                    else

                        stateProviderTO.setValue("");
                } else if (stateId.endsWith("featureExtractorVersion")) {
                    if (!StringUtils.isEmpty(officeSettingTO.getFeatureExtractorVersion()))
                        stateProviderTO.setValue(officeSettingTO.getFeatureExtractorVersion());
                    else

                        stateProviderTO.setValue("");
                }*/ else {
                    value = (String) profileManager.getProfile(ProfileKeyName.STATE_ROOT + "." + stateId, true, null, null);
                    if (value != null && value.trim().length() > 0)
                        stateProviderTO.setValue(value);
                }

                //************ Anbari change the securityContex to load access from cache
                
                /*
            } else if (UIStateIds.W_RATING_ADD.equalsIgnoreCase(stateId)) {
                value = "{\"hidden\":true}";
                if (getUserManagementService().hasAccess(userProfileTO.getUserName(), "ems_addRating")) {
                    value = "{\"hidden\":false}";
//					value = "{\"disabled\":false,\"tooltip\":\"This is a test while enabled\"}";
                }
                stateProviderTO.setValue(value);
            } else if (UIStateIds.W_RATING_EDIT.equalsIgnoreCase(stateId)) {
                value = "{\"disabled\":true}";
                if (getUserManagementService().hasAccess(userProfileTO.getUserName(), "ems_editRating")) {
                    value = "{\"disabled\":false}";
                }
                stateProviderTO.setValue(value);
            } else if (UIStateIds.W_RATING_DELETE.equalsIgnoreCase(stateId)) {
                value = "{\"hidden\":true}";
                if (getUserManagementService().hasAccess(userProfileTO.getUserName(), "ems_removeRating")) {
                    value = "{\"hidden\":false}";
                }
                stateProviderTO.setValue(value);
            }*/


            } else if (UIStateIds.W_RATING_ADD.equalsIgnoreCase(stateId)) {
                value = "{\"hidden\":true}";
                if (securityContextService.hasAccess(userProfileTO.getUserName(), "ems_addRating")) {
                    value = "{\"hidden\":false}";
//					value = "{\"disabled\":false,\"tooltip\":\"This is a test while enabled\"}";
                }
                stateProviderTO.setValue(value);
            } else if (UIStateIds.W_RATING_EDIT.equalsIgnoreCase(stateId)) {
                value = "{\"disabled\":true}";
                if (securityContextService.hasAccess(userProfileTO.getUserName(), "ems_editRating")) {
                    value = "{\"disabled\":false}";
                }
                stateProviderTO.setValue(value);
            } else if (UIStateIds.W_RATING_DELETE.equalsIgnoreCase(stateId)) {
                value = "{\"hidden\":true}";
                if (securityContextService.hasAccess(userProfileTO.getUserName(), "ems_removeRating")) {
                    value = "{\"hidden\":false}";
                }
                stateProviderTO.setValue(value);
            }


        } catch (Exception e) {
            //TODO: Throw BaseException with appropriate Exception Code
            logger.error(BizExceptionCode.GLB_ERR_MSG, e);
        }
        return stateProviderTO;
    }

    //Anbari
    private UserManagementService getUserManagementService() throws BaseException {
        UserManagementService userManagementService;
        try {
            userManagementService = ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_USER), null);
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.RMG_016, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_USER.split(","));
        }
        return userManagementService;
    }


    private EnrollmentOfficeDAO getEnrollmentOfficeDAO() throws BaseException {
        EnrollmentOfficeDAO enrollmentOfficeDAO = null;
        try {
            enrollmentOfficeDAO = DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_ENROLLMENT_OFFICE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.EOS_001,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_ENROLLMENT_OFFICE.split(","));
        }
        return enrollmentOfficeDAO;
    }

    private OfficeSettingDAO getOfficeSettingDAO() throws ServiceException {

        OfficeSettingDAO officeSettingDAO = null;

        try {
            officeSettingDAO = DAOFactoryProvider
                    .getDAOFactory()
                    .getDAO(EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_OFFICE_SETTING));
        } catch (DAOFactoryException e) {

            throw new ServiceException(BizExceptionCode.SPS_001,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_OFFICE_SETTING.split(","));

        }
        return officeSettingDAO;

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
}

