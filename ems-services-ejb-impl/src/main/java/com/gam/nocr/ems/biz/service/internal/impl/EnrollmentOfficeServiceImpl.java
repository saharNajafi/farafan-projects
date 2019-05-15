package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.*;
import com.gam.nocr.ems.config.*;
import com.gam.nocr.ems.data.dao.*;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.vol.EnrollmentOfficeVTO;
import com.gam.nocr.ems.data.domain.ws.HealthStatusWTO;
import com.gam.nocr.ems.data.domain.ws.OfficeAppointmentWTO;
import com.gam.nocr.ems.data.enums.*;
import com.gam.nocr.ems.util.*;
import gampooya.tools.security.SecurityContextService;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import org.displaytag.exception.ListHandlerException;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;

import static com.gam.nocr.ems.config.EMSLogicalNames.SRV_GAAS;
import static com.gam.nocr.ems.config.EMSLogicalNames.getExternalServiceJNDIName;

/**
 * <p>
 * TODO -- Explain this class
 * </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 * @author Noushin Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "EnrollmentOfficeService")
@Local(EnrollmentOfficeServiceLocal.class)
@Remote(EnrollmentOfficeServiceRemote.class)
public class EnrollmentOfficeServiceImpl extends EMSAbstractService implements
        EnrollmentOfficeServiceLocal, EnrollmentOfficeServiceRemote {

    @Resource
    SessionContext sessionContext;

    private static final Logger logger = BaseLog
            .getLogger(EnrollmentOfficeServiceImpl.class);

    private static final Logger jobLogger = BaseLog.getLogger("CreateActiveShifts");

    private static final String DEFAULT_IS_DISABLE_USER_IN_CHANGE_MANAGER_ACTION = "true";


    /**
     * =============== Private methods ===============
     */
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().equals("");
    }

    private boolean isValidLength(String str, int len) {
        return str.length() <= len;
    }

    private void checkEnrollmentOfficeVTO(
            EnrollmentOfficeVTO enrollmentOfficeVTO) throws BaseException {
        if (enrollmentOfficeVTO == null)
            throw new ServiceException(BizExceptionCode.EOS_004,
                    BizExceptionCode.EOS_004_MSG);
        if (isNullOrEmpty(enrollmentOfficeVTO.getName()))
            throw new ServiceException(BizExceptionCode.EOS_005,
                    BizExceptionCode.EOS_005_MSG);
        if (!isValidLength(enrollmentOfficeVTO.getName(), 255))
            throw new ServiceException(BizExceptionCode.EOS_006,
                    BizExceptionCode.EOS_006_MSG);
        if (isNullOrEmpty(enrollmentOfficeVTO.getAddress()))
            throw new ServiceException(BizExceptionCode.EOS_007,
                    BizExceptionCode.EOS_007_MSG);
        if (!isValidLength(enrollmentOfficeVTO.getAddress(), 255))
            throw new ServiceException(BizExceptionCode.EOS_008,
                    BizExceptionCode.EOS_008_MSG);
        if (isNullOrEmpty(enrollmentOfficeVTO.getZip()))
            throw new ServiceException(BizExceptionCode.EOS_009,
                    BizExceptionCode.EOS_009_MSG);
        if (enrollmentOfficeVTO.getZip().length() > 10
                || enrollmentOfficeVTO.getZip().length() < 5)
            throw new ServiceException(BizExceptionCode.EOS_010,
                    BizExceptionCode.EOS_010_MSG);
        if (!isNullOrEmpty(enrollmentOfficeVTO.getPhone())
                && !isValidLength(enrollmentOfficeVTO.getPhone(), 20))
            throw new ServiceException(BizExceptionCode.EOS_011,
                    BizExceptionCode.EOS_011_MSG);
        if (!isNullOrEmpty(enrollmentOfficeVTO.getFax())
                && !isValidLength(enrollmentOfficeVTO.getFax(), 20))
            throw new ServiceException(BizExceptionCode.EOS_012,
                    BizExceptionCode.EOS_012_MSG);
        if (isNullOrEmpty(enrollmentOfficeVTO.getCode()))
            throw new ServiceException(BizExceptionCode.EOS_013,
                    BizExceptionCode.EOS_013_MSG);
        if (!isValidLength(enrollmentOfficeVTO.getCode(), 255))
            throw new ServiceException(BizExceptionCode.EOS_014,
                    BizExceptionCode.EOS_014_MSG);
        if (enrollmentOfficeVTO.getParentId() == null)
            throw new ServiceException(BizExceptionCode.EOS_015,
                    BizExceptionCode.EOS_015_MSG);
//        if (enrollmentOfficeVTO.getRateId() == null)
//            throw new ServiceException(BizExceptionCode.EOS_016,
//                    BizExceptionCode.EOS_016_MSG);
        if (enrollmentOfficeVTO.getManagerId() == null)
            throw new ServiceException(BizExceptionCode.EOS_073,
                    BizExceptionCode.EOS_073_MSG);
        if (isNullOrEmpty(enrollmentOfficeVTO.getManagerPhone()))
            throw new ServiceException(BizExceptionCode.EOS_017,
                    BizExceptionCode.EOS_017_MSG);
        if (!isValidLength(enrollmentOfficeVTO.getManagerPhone(), 20))
            throw new ServiceException(BizExceptionCode.EOS_018,
                    BizExceptionCode.EOS_018_MSG);
        if (isNullOrEmpty(enrollmentOfficeVTO.getManagerMobile()))
            throw new ServiceException(BizExceptionCode.EOS_019,
                    BizExceptionCode.EOS_019_MSG);
        if (!isValidLength(enrollmentOfficeVTO.getManagerMobile(), 11))
            throw new ServiceException(BizExceptionCode.EOS_020,
                    BizExceptionCode.EOS_020_MSG);
        if (enrollmentOfficeVTO.getWorkingHoursStart() == null)
            throw new ServiceException(BizExceptionCode.EOS_059,
                    BizExceptionCode.EOS_059_MSG);
        if (enrollmentOfficeVTO.getWorkingHoursFinish() == null)
            throw new ServiceException(BizExceptionCode.EOS_060,
                    BizExceptionCode.EOS_060_MSG);
        if (enrollmentOfficeVTO.getWorkingHoursStart() >= enrollmentOfficeVTO
                .getWorkingHoursFinish())
            throw new ServiceException(BizExceptionCode.EOS_061,
                    BizExceptionCode.EOS_061_MSG);
        if (isNullOrEmpty(enrollmentOfficeVTO.getKhosusiType()))
            throw new ServiceException(BizExceptionCode.EOS_062,
                    BizExceptionCode.EOS_062_MSG);
        if (EnrollmentOfficeType.OFFICE.name().equals(
                enrollmentOfficeVTO.getOfficeType())) {
            if (enrollmentOfficeVTO.getSuperiorOfficeId() == null)
                throw new ServiceException(BizExceptionCode.EOS_068,
                        BizExceptionCode.EOS_068_MSG);
        }
        if (!isValidLength(String.valueOf(enrollmentOfficeVTO.getDepPhoneNumber()), 8))
            throw new ServiceException(BizExceptionCode.EOS_084,
                    BizExceptionCode.EOS_084_MSG);
        if (isNullOrEmpty(String.valueOf(enrollmentOfficeVTO.getDepPhoneNumber())))
            throw new ServiceException(BizExceptionCode.EOS_085,
                    BizExceptionCode.EOS_085_MSG);
    }

    /**
     * =============== Getter for DAOs ===============
     */

    /**
     * getEnrollmentOfficeDAO
     *
     * @return an instance of type EnrollmentOfficeDAO
     */
    private EnrollmentOfficeDAO getEnrollmentOfficeDAO() throws BaseException {
        try {
            return DAOFactoryProvider
                    .getDAOFactory()
                    .getDAO(EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_ENROLLMENT_OFFICE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.EOS_001,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_ENROLLMENT_OFFICE.split(","));
        }
    }

    private DepartmentDAO getDepartmentDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_DEPARTMENT));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.EOS_003,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_DEPARTMENT.split(","));
        }
    }

    private OfficeActiveShiftDAO getActiveShiftDAO() throws BaseException {
        try {
            return DAOFactoryProvider.
                    getDAOFactory().
                    getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_OFFICE_ACTIVE_SHIFT));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.EOS_101,
                    BizExceptionCode.GLB_029_MSG,
                    e,
                    EMSLogicalNames.DAO_OFFICE_ACTIVE_SHIFT.split(","));
        }
    }

    private OfficeCapacityDAO getOfficeCapacityDAO() throws BaseException {
        try {
            return DAOFactoryProvider.
                    getDAOFactory().
                    getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_OFFICE_CAPACITY));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.EOS_102,
                    BizExceptionCode.GLB_030_MSG,
                    e,
                    EMSLogicalNames.DAO_OFFICE_CAPACITY.split(","));
        }
    }


    private OfficeSettingDAO getOfficeSettingDAO() throws BaseException {
        try {
            return DAOFactoryProvider
                    .getDAOFactory()
                    .getDAO(EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_OFFICE_SETTING));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.EOS_079,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_OFFICE_SETTING.split(","));
        }

    }

    /**
     * getCardRequestDAO
     *
     * @return an instance of type {@link CardRequestDAO}
     */
    private CardRequestDAO getCardRequestDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.EOS_037,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_CARD_REQUEST.split(","));
        }
    }

    private DispatchDAO getDispatchInfoDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_DISPATCHING));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.EOS_024,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_DISPATCHING.split(","));
        }
    }

    private PersonDAO getPersonDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_PERSON));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.EOS_025,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_PERSON.split(","));
        }
    }

    // madanipour
    private CardRequestHistoryDAO getCardRequestHistoryDAO()
            throws BaseException {
        try {
            return DAOFactoryProvider
                    .getDAOFactory()
                    .getDAO(EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST_HISTORY));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.EOS_025,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_CARD_REQUEST_HISTORY.split(","));
        }
    }

    // Commented by Adldoost
    // private NetworkTokenDAO getNetworkTokenDAO() throws BaseException {
    // try {
    // return
    // DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_NETWORK_TOKEN));
    // } catch (DAOFactoryException e) {
    // throw new ServiceException(BizExceptionCode.EOS_028,
    // BizExceptionCode.GLB_001_MSG, e,
    // EMSLogicalNames.DAO_NETWORK_TOKEN.split(","));
    // }
    // }

    /**
     * =================== Getter for Services ===================
     */

    /**
     * getTokenManagementService
     *
     * @return an instance of type TokenManagementService
     */
    private TokenManagementService getTokenManagementService()
            throws BaseException {
        TokenManagementService tokenManagementService;
        try {
            tokenManagementService = ServiceFactoryProvider
                    .getServiceFactory()
                    .getService(
                            EMSLogicalNames
                                    .getServiceJNDIName(EMSLogicalNames.SRV_TOKEN_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.EOS_002,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_TOKEN_MANAGEMENT.split(","));
        }
        tokenManagementService.setUserProfileTO(getUserProfileTO());
        return tokenManagementService;
    }

    /**
     * getCMSService
     *
     * @return an instance of type {@link CMSService}
     * @throws {@link BaseException}
     */
    private CMSService getCMSService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        CMSService cmsService;
        try {
            cmsService = serviceFactory.getService(EMSLogicalNames
                    .getExternalServiceJNDIName(EMSLogicalNames.SRV_CMS), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.EOS_032,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_CMS.split(","));
        }
        cmsService.setUserProfileTO(getUserProfileTO());
        return cmsService;
    }

    private BusinessLogService getBusinessLogService() throws BaseException {
        BusinessLogService businessLogService;
        try {
            businessLogService = ServiceFactoryProvider
                    .getServiceFactory()
                    .getService(
                            EMSLogicalNames
                                    .getServiceJNDIName(EMSLogicalNames.SRV_BUSINESS_LOG), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.EOS_074,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_BUSINESS_LOG.split(","));
        }
        businessLogService.setUserProfileTO(getUserProfileTO());
        return businessLogService;
    }

    /**
     * getPortalBaseInfoService
     *
     * @return an instance of type {@link PortalBaseInfoService}
     * @throws {@link BaseException}
     */
    private PortalBaseInfoService getPortalBaseInfoService()
            throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        PortalBaseInfoService portalBaseInfoService;
        try {
            portalBaseInfoService = serviceFactory
                    .getService(EMSLogicalNames
                            .getExternalServiceJNDIName(EMSLogicalNames.SRV_PORTAL_BASE_INFO), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.EOS_033,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
        }
        portalBaseInfoService.setUserProfileTO(getUserProfileTO());
        return portalBaseInfoService;
    }


    private GAASService getGaasService() throws BaseException {
        GAASService gaasService = null;
        try {
            gaasService = ServiceFactoryProvider.getServiceFactory().getService(getExternalServiceJNDIName(SRV_GAAS), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.PSI_020,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    new String[]{EMSLogicalNames.SRV_GAAS});
        }
        gaasService.setUserProfileTO(getUserProfileTO());
        return gaasService;
    }

    private List<CardRequestState> fetchInProgressedStatesFromProfile()
            throws BaseException {
        String defaultStates = CardRequestState.RESERVED.name()
                + ","
                + CardRequestState.REFERRED_TO_CCOS.name()
                + ","
                + CardRequestState.DOCUMENT_AUTHENTICATED.name()
                + ","
                + CardRequestState.APPROVED_BY_MES.name()
                + ","
                + CardRequestState.APPROVED.name()
                + ","
                + CardRequestState.SENT_TO_AFIS.name()
                + ","
                + CardRequestState.APPROVED_BY_AFIS.name()
                + ","
                + CardRequestState.PENDING_ISSUANCE.name()
                + ","
                + CardRequestState.ISSUED.name()
                + ","
                + CardRequestState.READY_TO_DELIVER.name()
                + ","
                + CardRequestState.UNSUCCESSFUL_DELIVERY.name()
                + ","
                + CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC
                .name()
                + ","
                + CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE
                .name() + "," + CardRequestState.NOT_DELIVERED.name()
                + "," + CardRequestState.CMS_ERROR.name() + ","
                + CardRequestState.CMS_PRODUCTION_ERROR.name() + ","
                + CardRequestState.IMS_ERROR.name();

        String states;
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            states = (String) pm.getProfile(
                    ProfileKeyName.KEY_INPROGRESSED_CARD_REQUEST_STATES, true,
                    null, null);
            if (!EmsUtil.checkString(states)) {
                states = defaultStates;
            }
        } catch (Exception e) {
            logger.warn(BizExceptionCode.EOS_066, BizExceptionCode.EOS_066_MSG);
            states = defaultStates;
        }

        List<CardRequestState> inProgressedStates = new ArrayList<CardRequestState>();
        String[] statesArray = states.split(",");
        for (String state : statesArray) {
            inProgressedStates.add(CardRequestState.valueOf(state));
        }
        return inProgressedStates;
    }

    private List<Long> findSubOffice(Long enrollmentOfficeId)
            throws BaseException {
        return getEnrollmentOfficeDAO().findSubOffice(enrollmentOfficeId);
    }

    @Override
    @Permissions(value = "ems_editEnrollmentOffice || ems_addEnrollmentOffice")
    @BizLoggable(logAction = "INSERT", logEntityName = "ENROLLMENT_OFFICE")
    public Long save(EnrollmentOfficeVTO enrollmentOfficeVTO)
            throws BaseException {
        try {
            checkEnrollmentOfficeVTO(enrollmentOfficeVTO);

            EnrollmentOfficeTO office = new EnrollmentOfficeTO();
            office.setName(enrollmentOfficeVTO.getName() != null ? enrollmentOfficeVTO.getName().trim().replaceAll("[\\n\\t\\r]", "") : "");
            office.setAddress(enrollmentOfficeVTO.getAddress() != null ? enrollmentOfficeVTO.getAddress().trim().replaceAll("[\\n\\t\\r]", "") : "");
            office.setPostalCode(enrollmentOfficeVTO.getZip());
            office.setPhone(enrollmentOfficeVTO.getPhone());
            office.setFax(enrollmentOfficeVTO.getFax());
            office.setCode(enrollmentOfficeVTO.getCode());
            office.setLocation(new LocationTO(enrollmentOfficeVTO.getLocId()));
            office.setWorkingHoursFrom(enrollmentOfficeVTO.getWorkingHoursStart());
            office.setWorkingHoursTo(enrollmentOfficeVTO.getWorkingHoursFinish());
            office.setKhosusiType(OfficeType.valueOf(enrollmentOfficeVTO.getKhosusiType()));
            int calenderType = getCalenderType(
                    enrollmentOfficeVTO.getThursdayMorningActive()
                    , enrollmentOfficeVTO.getThursdayEveningActive()
                    , enrollmentOfficeVTO.getFridayMorningActive()
                    , enrollmentOfficeVTO.getFridayEveningActive());
            office.setCalenderType(OfficeCalenderType.toCalenderType((long) calenderType));
            office.setHasStair(enrollmentOfficeVTO.getHasStair());
            office.setHasStair(enrollmentOfficeVTO.getHasStair());
            office.setHasElevator(enrollmentOfficeVTO.getHasElevator());
            office.setHasPortabilityEquipment(enrollmentOfficeVTO.getHasPortabilityEquipment());
            office.setThursdayMorningActive(enrollmentOfficeVTO.getThursdayMorningActive());
            office.setThursdayEveningActive(enrollmentOfficeVTO.getThursdayEveningActive());
            office.setFridayMorningActive(enrollmentOfficeVTO.getFridayMorningActive());
            office.setFridayEveningActive(enrollmentOfficeVTO.getFridayEveningActive());
            office.setSingleStageOnly(enrollmentOfficeVTO.getSingleStageOnly());
            office.setActive(enrollmentOfficeVTO.getActive());
            office.setPostNeeded(enrollmentOfficeVTO.getPostNeeded());
            office.setPostDestinationCode(enrollmentOfficeVTO.getPostDestinationCode());

            if (enrollmentOfficeVTO.getKhosusiType().equals(OfficeType.NOCR.name())
                    || enrollmentOfficeVTO.getKhosusiType().equals("0"))
                office.setType(EnrollmentOfficeType.NOCR);
            else
                office.setType(EnrollmentOfficeType.OFFICE);

            //Anbari: set Delivery status in EOF while saving
            if (EnrollmentOfficeType.OFFICE.name().equals(enrollmentOfficeVTO.getOfficeType())) {
                if (EnrollmentOfficeDeliverStatus.ENABLED.equals(EnrollmentOfficeDeliverStatus.toState(Long.valueOf(enrollmentOfficeVTO.getOfficeDeliver()))))
                    office.setDeliver(EnrollmentOfficeDeliverStatus.ENABLED);
                else
                    office.setDeliver(EnrollmentOfficeDeliverStatus.DISABLED);
            } else {
                office.setDeliver(EnrollmentOfficeDeliverStatus.DISABLED);
            }

            if (enrollmentOfficeVTO.getSuperiorOfficeId() != null)
                office.setSuperiorOffice(new EnrollmentOfficeTO(enrollmentOfficeVTO.getSuperiorOfficeId()));
            else
                office.setSuperiorOffice(null);

            DepartmentDAO departmentDAO = getDepartmentDAO();

            DepartmentTO parentTO = departmentDAO
                    .fetchDepartment(enrollmentOfficeVTO.getParentId());

            office.setParentDepartment(new DepartmentTO(parentTO.getId()));
            if (enrollmentOfficeVTO.getArea() != null)
                office.setArea(enrollmentOfficeVTO.getArea().intValue());
            Long rateId;
            if (!enrollmentOfficeVTO.getActive()) {
                rateId = getRatingInfoDAO().findBySize((long) 0);
                /** rating info has been deleted form UI.just set default value when creating a new office*/
                if (rateId != null)
                    office.setRatingInfo(new RatingInfoTO(rateId));
            } else {
                rateId = getRatingInfoDAO().findBySize((long) 1);
                if (rateId != null)
                    office.setRatingInfo(new RatingInfoTO(rateId));
            }
            // if this returns null, then an exception
            // will be thrown when trying to update

            PersonTO manager = getPersonDAO().find(PersonTO.class,
                    enrollmentOfficeVTO.getManagerId());
            if (manager == null)
                throw new ServiceException(BizExceptionCode.EOS_027,
                        BizExceptionCode.EOS_027_MSG,
                        new Long[]{enrollmentOfficeVTO.getManagerId()});
            manager.setPhone(enrollmentOfficeVTO.getManagerPhone());
            manager.setMobilePhone(enrollmentOfficeVTO.getManagerMobile());

            office.setManager(manager);
            office.setDispatchSendType(DepartmentDispatchSendType.CARD);

            DepartmentTO departmentTO = departmentDAO.create(office);

            departmentTO.setDn("EOF" + departmentTO.getId());
            departmentTO.setParentDN(parentTO.getDn() + "."
                    + parentTO.getParentDN());
            departmentTO.setDepPhoneNumber(enrollmentOfficeVTO.getDepPhoneNumber());

            manager.setDepartment(departmentTO);

            // Commented By Adldoost
            // if (enrollmentOfficeVTO.getSsl() &&
            // "OFFICE".equals(enrollmentOfficeVTO.getOfficeType())) {
            // getTokenManagementService().issueFirstNetworkToken(departmentTO.getId(),
            // TokenType.NETWORK);
            // enrollmentOfficeVTO.setTokenState(TokenState.READY_TO_ISSUE.name());
            // }
            enrollmentOfficeVTO.setId(departmentTO.getId());

            //create office setting
            OfficeSettingTO officeSettingTO = new OfficeSettingTO();
            officeSettingTO.setEnrollmentOffice(new EnrollmentOfficeTO(departmentTO.getId()));
            getOfficeSettingDAO().create(officeSettingTO);

            return departmentTO.getId();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.EOS_038,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    private int getCalenderType(Boolean thursdayMorning, Boolean thursdayEvening
            , Boolean fridayMorning, Boolean fridayEvening) {

        int calenderType = 0;

        if (!thursdayMorning && !thursdayEvening && !fridayMorning && !fridayEvening)
            calenderType = 0;

        if ((thursdayMorning || thursdayEvening) && !fridayMorning && !fridayEvening)
            calenderType = 1;

        if (!thursdayMorning && !thursdayEvening && (fridayMorning || fridayEvening))
            calenderType = 3;

        if (!thursdayMorning && thursdayEvening && (fridayMorning || fridayEvening))
            calenderType = 2;

        if (thursdayMorning && !thursdayEvening && (fridayMorning || fridayEvening))
            calenderType = 2;

        if (thursdayMorning && thursdayEvening && (fridayMorning || fridayEvening))
            calenderType = 2;

        return calenderType;
    }

    @Override
    @Permissions(value = "ems_editEnrollmentOffice")
    @BizLoggable(logAction = "UPDATE", logEntityName = "ENROLLMENT_OFFICE")
    public Long update(EnrollmentOfficeVTO enrollmentOfficeVTO) throws BaseException {
        try {
            checkEnrollmentOfficeVTO(enrollmentOfficeVTO);
            EnrollmentOfficeTO office = getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class, enrollmentOfficeVTO.getId());
            if (office == null)
                throw new ServiceException(BizExceptionCode.EOS_023, BizExceptionCode.EOS_023_MSG, new Long[]{enrollmentOfficeVTO.getId()});

            //Commented By Adldoost
            //Long existingNetworkTokenId = getNetworkTokenDAO().findNotRevokedTokenIdByEnrollmentOfficeId(office.getId());

            office.setName(enrollmentOfficeVTO.getName() != null ? enrollmentOfficeVTO.getName().trim().replaceAll("[\\n\\t\\r]", "") : "");

            office.setAddress(enrollmentOfficeVTO.getAddress() != null ? enrollmentOfficeVTO.getAddress().trim().replaceAll("[\\n\\t\\r]", "") : "");

            office.setPostalCode(enrollmentOfficeVTO.getZip());
            office.setPhone(enrollmentOfficeVTO.getPhone());
            office.setFax(enrollmentOfficeVTO.getFax());
            office.setCode(enrollmentOfficeVTO.getCode());
            office.setLocation(new LocationTO(enrollmentOfficeVTO.getLocId()));
            office.setWorkingHoursFrom(enrollmentOfficeVTO.getWorkingHoursStart());
            office.setWorkingHoursTo(enrollmentOfficeVTO.getWorkingHoursFinish());
            int calenderType = getCalenderType(
                    enrollmentOfficeVTO.getThursdayMorningActive()
                    , enrollmentOfficeVTO.getThursdayEveningActive()
                    , enrollmentOfficeVTO.getFridayMorningActive()
                    , enrollmentOfficeVTO.getFridayEveningActive());
            office.setCalenderType(OfficeCalenderType.toCalenderType((long) calenderType));
            office.setHasStair(enrollmentOfficeVTO.getHasStair());
            office.setHasElevator(enrollmentOfficeVTO.getHasElevator());
            office.setHasPortabilityEquipment(enrollmentOfficeVTO.getHasPortabilityEquipment());
            office.setThursdayMorningActive(enrollmentOfficeVTO.getThursdayMorningActive());
            office.setThursdayEveningActive(enrollmentOfficeVTO.getThursdayEveningActive());
            office.setFridayMorningActive(enrollmentOfficeVTO.getFridayMorningActive());
            office.setFridayEveningActive(enrollmentOfficeVTO.getFridayEveningActive());
            office.setSingleStageOnly(enrollmentOfficeVTO.getSingleStageOnly());
            office.setDepPhoneNumber(enrollmentOfficeVTO.getDepPhoneNumber());
            office.setActive(enrollmentOfficeVTO.getActive());
            Long rateId;
            if (!enrollmentOfficeVTO.getActive()) {
                rateId = getRatingInfoDAO().findBySize((long) 0);
                /** rating info has been deleted form UI.just set default value when editing an office*/
                if (rateId != null)
                    office.setRatingInfo(new RatingInfoTO(rateId));
            } else {
                rateId = getRatingInfoDAO().findBySize((long) 1);
                if (rateId != null)
                    office.setRatingInfo(new RatingInfoTO(rateId));
            }
            office.setPostNeeded(enrollmentOfficeVTO.getPostNeeded());
            office.setPostDestinationCode(enrollmentOfficeVTO.getPostDestinationCode());
            if (EnrollmentOfficeType.NOCR.equals(office.getType()) &&
                    EnrollmentOfficeType.OFFICE.name().equals(enrollmentOfficeVTO.getOfficeType()))
                throw new ServiceException(BizExceptionCode.EOS_069, BizExceptionCode.EOS_069_MSG);
            //CommentedByAdldoost
            //            else if (EnrollmentOfficeType.OFFICE.equals(office.getType()) &&
            //                    EnrollmentOfficeType.NOCR.name().equals(enrollmentOfficeVTO.getOfficeType())) {
            //                if (existingNetworkTokenId != null)
            //                    getTokenManagementService().revokeNetworkToken(existingNetworkTokenId, ReplicaReason.UNSPECIFIED, "");
            //            }

            if (enrollmentOfficeVTO.getKhosusiType().equals(OfficeType.NOCR.name())
                    || enrollmentOfficeVTO.getKhosusiType().equals("0"))
                office.setType(EnrollmentOfficeType.NOCR);
            else
                office.setType(EnrollmentOfficeType.OFFICE);


            if (enrollmentOfficeVTO.getKhosusiType().equals(OfficeType.NOCR.name())) {
                office.setKhosusiType(OfficeType.NOCR);
            } else if (enrollmentOfficeVTO.getKhosusiType().equals(
                    OfficeType.POST.name())) {
                office.setKhosusiType(OfficeType.POST);
            } else if (enrollmentOfficeVTO.getKhosusiType().equals(
                    OfficeType.OFFICE.name())) {
                office.setKhosusiType(OfficeType.OFFICE);
            }


            //  Non 'NOCR' Enrollment offices should have a superior office (of type 'NOCR')
            //Anbari: set Delivery status in EOF while updating
            if (EnrollmentOfficeType.OFFICE.name().equals(enrollmentOfficeVTO.getOfficeType())) {
                if (EnrollmentOfficeDeliverStatus.ENABLED.equals(EnrollmentOfficeDeliverStatus.toState(Long.valueOf(enrollmentOfficeVTO.getOfficeDeliver()))))
                    office.setDeliver(EnrollmentOfficeDeliverStatus.ENABLED);
                else
                    office.setDeliver(EnrollmentOfficeDeliverStatus.DISABLED);
                office.setSuperiorOffice(new EnrollmentOfficeTO(enrollmentOfficeVTO.getSuperiorOfficeId()));
            } else {
                office.setSuperiorOffice(null);
                office.setDeliver(EnrollmentOfficeDeliverStatus.DISABLED);
            }

            DepartmentDAO departmentDAO = getDepartmentDAO();
            PersonDAO personDAO = getPersonDAO();

            DepartmentTO parentTO = departmentDAO.fetchDepartment(enrollmentOfficeVTO.getParentId());
            office.setParentDepartment(parentTO);
            if (enrollmentOfficeVTO.getArea() != null)
                office.setArea(enrollmentOfficeVTO.getArea().intValue());
//            office.setRatingInfo(new RatingInfoTO(enrollmentOfficeVTO.getRateId()));
            office.setParentDN(parentTO.getDn() + "." + parentTO.getParentDN());

            PersonTO prevManager = null;
            PersonTO manager = null;

            manager = personDAO.find(PersonTO.class, enrollmentOfficeVTO.getManagerId());
            if (manager == null)
                throw new ServiceException(BizExceptionCode.EOS_026, BizExceptionCode.EOS_026_MSG, new Long[]{enrollmentOfficeVTO.getManagerId()});

            if ((office.getManager() == null && enrollmentOfficeVTO
                    .getManagerId() != null)
                    || (office.getManager() != null && !office.getManager()
                    .getId().equals(enrollmentOfficeVTO.getManagerId()))) {
                // CommentedByAdldoost
                // // a different manager is being assigned to this enrollment
                // office
                // // office must not have an operating network token (which is
                // linked to the current manager)
                // if (existingNetworkTokenId != null
                // &&
                // EnrollmentOfficeType.OFFICE.name().equals(enrollmentOfficeVTO.getOfficeType()))
                // {
                // throw new ServiceException(BizExceptionCode.EOS_056,
                // BizExceptionCode.EOS_056_MSG, new
                // Long[]{existingNetworkTokenId});
                // }

                prevManager = office.getManager();


                //Disable previous manager
                if (Boolean
                        .valueOf(EmsUtil
                                .getProfileValue(
                                        ProfileKeyName.KEY_IS_DISABLE_USER_IN_CHANGE_MANAGER_ACTION,
                                        DEFAULT_IS_DISABLE_USER_IN_CHANGE_MANAGER_ACTION))) {
                    getGaasService().disableUser(prevManager.getUserId());
                    prevManager.setStatus(BooleanType.T);
                    personDAO.changeStatus(prevManager);
                }
                // UserInfoVTO userInfoVTO =
                // getGaasService().getUser(prevManager.getUserId());
                // userInfoVTO.setRoles(new ArrayList<RoleVTO>());
                // userInfoVTO.setPermissions(new ArrayList<PermissionVTO>());
                // userInfoVTO.setEnabled(false);
                // getGaasService().update(userInfoVTO);

                office.setManager(manager);
            }

            // departmentDAO.update(office);
            //
            // if (prevManager != null) {
            // prevManager.setDepartment(null);
            // personDAO.update(prevManager);
            // }

            manager.setPhone(enrollmentOfficeVTO.getManagerPhone());
            manager.setMobilePhone(enrollmentOfficeVTO.getManagerMobile());
            manager.setDepartment(office);

            personDAO.update(manager);

            return office.getId();
        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (Exception e) {
            sessionContext.setRollbackOnly();
            throw new ServiceException(BizExceptionCode.EOS_039, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_viewEnrollmentOffice")
    @BizLoggable(logAction = "LOAD", logEntityName = "ENROLLMENT_OFFICE")
    public EnrollmentOfficeVTO load(Long officeId) throws BaseException {
        try {
            if (officeId == null)
                throw new ServiceException(BizExceptionCode.EOS_029,
                        BizExceptionCode.EOS_029_MSG);
            EnrollmentOfficeTO office = getEnrollmentOfficeDAO().find(
                    EnrollmentOfficeTO.class, officeId);
            if (office == null)
                throw new ServiceException(BizExceptionCode.EOS_030,
                        BizExceptionCode.EOS_030_MSG, new Long[]{officeId});

            EnrollmentOfficeVTO vto = new EnrollmentOfficeVTO();
            vto.setName(office.getName());
            vto.setAddress(office.getAddress());
            vto.setZip(office.getPostalCode());
            vto.setPhone(office.getPhone());
            vto.setFax(office.getFax());
            vto.setCode(office.getCode());
            vto.setParentId(office.getParentDepartment().getId());
            vto.setParentName(office.getParentDepartment().getName());
            if (office.getArea() != null)
                vto.setArea(office.getArea().longValue());
//            vto.setRateId(office.getRatingInfo().getId());
//            vto.setRate(office.getRatingInfo().getClazz());
            vto.setManagerId(office.getManager().getId());
            vto.setManagerName(office.getManager().getFirstName() + " "
                    + office.getManager().getLastName());
            vto.setManagerPhone(office.getManager().getPhone());
            vto.setManagerMobile(office.getManager().getMobilePhone());
            vto.setLocId(office.getLocId());
            vto.setLocName(office.getLocName());
            vto.setWorkingHoursStart(office.getWorkingHoursFrom());
            vto.setWorkingHoursFinish(office.getWorkingHoursTo());
            vto.setKhosusiType(office.getKhosusiType().toString());
//			 vto.setCalenderType(OfficeCalenderType.toLong(office.getCalenderType()).toString());
            vto.setDepPhoneNumber(office.getParentDepartment().getDepPhoneNumber());
            vto.setThursdayMorningActive(office.getThursdayMorningActive());
            vto.setThursdayEveningActive(office.getThursdayMorningActive());
            vto.setFridayMorningActive(office.getFridayMorningActive());
            vto.setFridayEveningActive(office.getFridayEveningActive());
            vto.setSingleStageOnly(office.getSingleStageOnly());
            vto.setHasStair(office.getHasStair());
            vto.setHasElevator(office.getHasElevator());
            vto.setHasPortabilityEquipment(office.getHasPortabilityEquipment());
            vto.setActive(office.getActive());
            vto.setPostNeeded(office.getPostNeeded());
            vto.setPostDestinationCode(office.getPostDestinationCode());
            return vto;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.EOS_040,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    // @Override
    // @Permissions(value = "ems_removeEnrollmentOffice")
    // @BizLoggable(logAction = "DELETE", logEntityName = "ENROLLMENT_OFFICE")
    // public boolean remove(String officeIds) throws BaseException {
    // try {
    // if (officeIds == null || officeIds.trim().length() == 0)
    // throw new ServiceException(BizExceptionCode.EOS_031,
    // BizExceptionCode.EOS_031_MSG);
    //
    // Long depId;
    // for (String id : officeIds.split(",")) {
    // depId = Long.parseLong(id.trim());
    //
    // Long existingNetworkTokenId =
    // getNetworkTokenDAO().findNotRevokedTokenIdByEnrollmentOfficeId(depId);
    // if (existingNetworkTokenId != null) {
    // throw new ServiceException(BizExceptionCode.EOS_057,
    // BizExceptionCode.EOS_057_MSG, new Long[]{existingNetworkTokenId});
    // }
    //
    // List<Long> officeIdList = findSubOffice(depId);
    // if (EmsUtil.checkListSize(officeIdList))
    // throw new ServiceException(BizExceptionCode.EOS_070,
    // BizExceptionCode.EOS_070_MSG);
    //
    // if(getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class,
    // depId).getType() == EnrollmentOfficeType.NOCR)
    // {
    // //Adldoost -- check oef doesn't have card request
    // if(getCardRequestDAO().findEnrollmentOfficeCardRequestCount(depId) >
    // (long)0)
    // {
    // throw new ServiceException(BizExceptionCode.EOS_078,
    // BizExceptionCode.EOS_078_MSG);
    // }
    //
    // getPortalBaseInfoService().checkEnrollmentOfficeDeletePossibilityAndPerform(depId);
    // //Anbari :Call CMS immediately after deleting office : if CMS does not
    // have the specified usersite catch the appropriate exception and continue
    // as a normal situation
    // List<EnrollmentOfficeTO> enrollmentOfficeTOListForSync = new
    // ArrayList<EnrollmentOfficeTO>();
    // enrollmentOfficeTOListForSync.add(getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class,
    // depId));
    // try {
    // getCMSService().updateEnrollmentOffices(enrollmentOfficeTOListForSync,
    // EnrollmentOfficeStatus.toInteger(EnrollmentOfficeStatus.DISABLED));
    // } catch (Exception exception) {
    // if(exception instanceof ServiceException)
    // {
    // ServiceException serviceException = (ServiceException) exception;
    // if(serviceException.getExceptionCode().equals(BizExceptionCode.CSI_110))
    // {
    // logger.info("CMS EOF Does not Exist : " + serviceException.getMessage());
    // }
    // else
    // throw exception;
    // }
    // else
    // throw exception;
    // }
    // }
    //
    // }
    // return getDepartmentDAO().removeDepartments(officeIds);
    // } catch (BaseException e) {
    // throw e;
    // } catch (NumberFormatException e) {
    // throw new ServiceException(BizExceptionCode.EOS_058,
    // BizExceptionCode.EOS_058_MSG, e);
    // } catch (Exception e) {
    // throw new ServiceException(BizExceptionCode.EOS_041,
    // BizExceptionCode.GLB_008_MSG, e);
    // }
    // }

    @Override
    @Permissions(value = "ems_viewEnrollmentOfficeList")
    public SearchResult fetchEnrollments(String searchString, int from, int to,
                                         String orderBy, Map additionalParams) throws BaseException {
        HashMap param = new HashMap();
        StringBuilder parts = new StringBuilder(",enrollment");

        if ((additionalParams != null)
                && (additionalParams.get("superDepartmentID") != null)) {
            parts.append(",parentDepId");
            param.put("superDepartmentID",
                    additionalParams.get("superDepartmentID"));
        }
        if ((additionalParams != null)
                && (additionalParams.get("officeType") != null)) {
            String str = "";
            if ((additionalParams.get("officeType").equals("NOCR")))
                str = EnrollmentOfficeType.NOCR.name();
            else
                str = EnrollmentOfficeType.OFFICE.name();
            parts.append(",officeType");
            param.put("type", str);
        }
//		if ((additionalParams != null)
//				&& (additionalParams.get("officeType") != null)) {
//			parts.append(",officeType");
//			param.put("type", additionalParams.get("officeType"));
//		}
        if ((additionalParams != null)
                && (additionalParams.get("officeId") != null)
                && (additionalParams.get("officeType") != null)) {
            if (OfficeType.NOCR.name().equals(
                    additionalParams.get("officeType")))
                parts.append(",otherNocr");
            else if (OfficeType.OFFICE.name().equals(
                    additionalParams.get("officeType")) || OfficeType.POST.name().equals(
                    additionalParams.get("officeType")))
                parts.append(",superior");

            param.put("officeId", additionalParams.get("officeId"));
            param.put("type", "NOCR");
        }

        param.put("depName", "%" + searchString + "%");
        try {
            ValueListHandler vlh = EMSValueListProvider.getProvider().loadList(
                    "departmentAC", ("main" + parts).split(","),
                    ("count" + parts).split(","), param, orderBy, null);
            List list = vlh.getList(from, to, true);
            return new SearchResult(vlh.size(), list);
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.EOS_014,
                    BizExceptionCode.GLB_006_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.EOS_015,
                    BizExceptionCode.GLB_007_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.EOS_042,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_viewEnrollmentOfficeList")
    public SearchResult fetchPersonEnrollments(String searchString, int from,
                                               int to, String orderBy, Map additionalParams) throws BaseException {
        HashMap param = new HashMap();
        Long personID = getPersonDAO().findPersonIdByUsername(getUserProfileTO().getUserName());
        StringBuilder parts = new StringBuilder(",enrollment");

        param.put("depName", "%" + searchString + "%");
        param.put("perid", personID);

        try {
            ValueListHandler vlh = EMSValueListProvider.getProvider().loadList(
                    "personDepartmentAC", ("main" + parts).split(","),
                    ("count" + parts).split(","), param, orderBy, null);
            List list = vlh.getList(from, to, true);
            return new SearchResult(vlh.size(), list);
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.EOS_075,
                    BizExceptionCode.GLB_006_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.EOS_076,
                    BizExceptionCode.GLB_007_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.EOS_077,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    public boolean checkForActiveRequest(long enrollmentOfficeId) {// todo: no
        // implementation
        return false;
    }

    @Override
    /**
     * The method revokeAndSubstitute is used to replace a specified enrollmentOffice with another one
     *
     * @param enrollmentOfficeId is an instance of type {@link Long} which represents the id of the main enrollmentOffice
     * @param superiorEnrollmentOfficeId is an instance of type {@link Long} which represents the id of the superior enrollmentOffice
     * @param reason
     * @param comment
     * @throws BaseException
     */
    // Commented By Adldoost
    // @Permissions(value = "ems_revokeNetworkToken")
    // public void revokeAndSubstitute(Long enrollmentOfficeId,
    // Long superiorEnrollmentOfficeId
    // /*ReplicaReason reason
    // String comment*/) throws BaseException {
    // try {
    // if (enrollmentOfficeId == null) {
    // throw new ServiceException(BizExceptionCode.EOS_053,
    // BizExceptionCode.EOS_053_MSG);
    // }
    // if (reason == null) {
    // throw new ServiceException(BizExceptionCode.EOS_055,
    // BizExceptionCode.EOS_055_MSG);
    // }
    // Adldoost
    // Long tokenId =
    // getNetworkTokenDAO().findNotRevokedTokenIdByEnrollmentOfficeId(enrollmentOfficeId);
    //
    // if (tokenId == null) {
    // throw new ServiceException(BizExceptionCode.EOS_051,
    // BizExceptionCode.EOS_051_MSG);
    // }
    // List<Long> officeIdList = findSubOffice(enrollmentOfficeId);
    // if (EmsUtil.checkListSize(officeIdList))
    // throw new ServiceException(BizExceptionCode.EOS_071,
    // BizExceptionCode.EOS_070_MSG);
    //
    // EnrollmentOfficeTO superiorEnrollmentOffice = null;
    //
    // if (checkInProgressRequests(enrollmentOfficeId)) {
    // if (superiorEnrollmentOfficeId == null) {
    // throw new ServiceException(BizExceptionCode.EOS_063,
    // BizExceptionCode.EOS_063_MSG);
    // } else {
    // if (enrollmentOfficeId.equals(superiorEnrollmentOfficeId)) {
    // throw new ServiceException(BizExceptionCode.EOS_067,
    // BizExceptionCode.EOS_067_MSG);
    // }
    //
    // superiorEnrollmentOffice =
    // getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class,
    // superiorEnrollmentOfficeId);
    // if (superiorEnrollmentOffice == null) {
    // throw new ServiceException(BizExceptionCode.EOS_064,
    // BizExceptionCode.EOS_064_MSG);
    // }
    // if
    // (!EnrollmentOfficeType.NOCR.equals(superiorEnrollmentOffice.getType())) {
    // throw new ServiceException(BizExceptionCode.EOS_072,
    // BizExceptionCode.EOS_072_MSG);
    // }
    // }
    // }
    //
    // getTokenManagementService().revokeNetworkToken(tokenId, reason, comment);
    // if (superiorEnrollmentOffice != null) {
    // getCardRequestDAO().replaceSuperiorWithOfficeId(enrollmentOfficeId,
    // superiorEnrollmentOffice.getId());
    //
    // getDispatchInfoDAO().replaceDispatchInfoReceiverDepId(enrollmentOfficeId,
    // superiorEnrollmentOffice.getId());
    //
    // // EnrollmentOfficeTO enrollmentOfficeTO =
    // getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class,
    // enrollmentOfficeId);
    // // if (enrollmentOfficeTO == null) {
    // // throw new ServiceException(BizExceptionCode.EOS_065,
    // BizExceptionCode.EOS_065_MSG);
    // // }
    // // enrollmentOfficeTO.setSuperiorOffice(superiorEnrollmentOffice);
    // // getEnrollmentOfficeDAO().update(enrollmentOfficeTO);
    // }
    // } catch (BaseException e) {
    // throw e;
    // } catch (Exception e) {
    // throw new ServiceException(BizExceptionCode.EOS_052,
    // BizExceptionCode.GLB_008_MSG, e);
    // }
    // }
    // Adldoost
    // madanipour
    @Permissions(value = "ems_revokeNetworkToken")
    public boolean substituteAndDelete(Long enrollmentOfficeId,
                                       Long superiorEnrollmentOfficeId) throws BaseException {
        try {
            if (enrollmentOfficeId == null) {
                throw new ServiceException(BizExceptionCode.EOS_053,
                        BizExceptionCode.EOS_053_MSG);
            }

            List<Long> officeIdList = findSubOffice(enrollmentOfficeId);
            if (EmsUtil.checkListSize(officeIdList))
                throw new ServiceException(BizExceptionCode.EOS_071,
                        BizExceptionCode.EOS_070_MSG);

            EnrollmentOfficeTO superiorEnrollmentOffice = null;
            boolean hasCardRequest = checkInProgressRequests(enrollmentOfficeId);
            if (hasCardRequest) {
                if (superiorEnrollmentOfficeId == null
                        && (getEnrollmentOfficeDAO().find(
                        EnrollmentOfficeTO.class, enrollmentOfficeId)
                        .getType() == EnrollmentOfficeType.OFFICE)) {
                    throw new ServiceException(BizExceptionCode.EOS_063,
                            BizExceptionCode.EOS_063_MSG);
                } else {
                    if (enrollmentOfficeId.equals(superiorEnrollmentOfficeId)) {
                        throw new ServiceException(BizExceptionCode.EOS_067,
                                BizExceptionCode.EOS_067_MSG);
                    }
                    if (getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class,
                            enrollmentOfficeId).getType() == EnrollmentOfficeType.OFFICE) {
                        superiorEnrollmentOffice = getEnrollmentOfficeDAO()
                                .find(EnrollmentOfficeTO.class,
                                        superiorEnrollmentOfficeId);
                        if (superiorEnrollmentOffice == null) {
                            throw new ServiceException(
                                    BizExceptionCode.EOS_064,
                                    BizExceptionCode.EOS_064_MSG);
                        }
                        if (!EnrollmentOfficeType.NOCR
                                .equals(superiorEnrollmentOffice.getType())) {
                            throw new ServiceException(
                                    BizExceptionCode.EOS_072,
                                    BizExceptionCode.EOS_072_MSG);
                        }
                    }
                }
            }
            if (superiorEnrollmentOffice != null) {

                List<Long> cardRequestIds = getCardRequestDAO()
                        .getCardRequestForSubstituteAndDeleteEOF(
                                enrollmentOfficeId);
                getCardRequestDAO().replaceSuperiorWithOfficeId(
                        superiorEnrollmentOffice.getId(), enrollmentOfficeId,
                        cardRequestIds);

                // madanipour for logging in transfer scenario
                String lastEofName = getDepartmentDAO().fetchDepartment(
                        enrollmentOfficeId).getName();
                String newEofName = getDepartmentDAO().fetchDepartment(
                        superiorEnrollmentOfficeId).getName();
                for (Long id : cardRequestIds) {
                    getCardRequestHistoryDAO()
                            .create(new CardRequestTO(id),
                                    "حذف " + lastEofName + " و انتقال درخواست ها به "
                                            + newEofName,
                                    SystemId.EMS,
                                    null,
                                    CardRequestHistoryAction.TRANSFER_TO_SUPERIOR_OFFICE,
                                    getUserProfileTO().getUserName());

                }

                getDispatchInfoDAO().replaceDispatchInfoReceiverDepId(
                        enrollmentOfficeId, superiorEnrollmentOffice.getId());
            }

            //todo:@Namjoofar @Shirin_Abbasi this record not should delete physically. We need have it because of historeis.
            //<editor-fold desc="set is not active">
            EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class, enrollmentOfficeId);
            enrollmentOfficeTO.setActive(false);
            getEnrollmentOfficeDAO().update(enrollmentOfficeTO);
            //</editor-fold>

            if (getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class,
                    enrollmentOfficeId).getType() == EnrollmentOfficeType.NOCR) {
                // Adldoost -- check oef doesn't have card request
                if (getCardRequestDAO().findEnrollmentOfficeCardRequestCount(
                        enrollmentOfficeId) > (long) 0) {
                    throw new ServiceException(BizExceptionCode.EOS_078,
                            BizExceptionCode.EOS_078_MSG);
                }


                //<editor-fold desc="EMS don't need to portals verification to delete anymore!">
                /*getPortalBaseInfoService()
                        .checkEnrollmentOfficeDeletePossibilityAndPerform(
                                enrollmentOfficeId);*/
                //</editor-fold>


                // Anbari :Call CMS immediately after deleting office : if CMS
                // does not have the specified usersite catch the appropriate
                // exception and continue as a normal situation
                List<EnrollmentOfficeTO> enrollmentOfficeTOListForSync = new ArrayList<EnrollmentOfficeTO>();
                enrollmentOfficeTOListForSync.add(getEnrollmentOfficeDAO()
                        .find(EnrollmentOfficeTO.class, enrollmentOfficeId));
                try {
                    /*getCMSService()
                            .updateEnrollmentOffices(
                                    enrollmentOfficeTOListForSync,
                                    EnrollmentOfficeStatus
                                            .toInteger(EnrollmentOfficeStatus.DISABLED));*/
                } catch (Exception exception) {
                    if (exception instanceof ServiceException) {
                        ServiceException serviceException = (ServiceException) exception;
                        if (serviceException.getExceptionCode().equals(
                                BizExceptionCode.CSI_110)) {
                            logger.info("CMS EOF Does not Exist : "
                                    + serviceException.getMessage());
                        } else
                            throw exception;
                    } else
                        throw exception;
                }
            }
            if (!hasCardRequest) {
                try {
                    getActiveShiftDAO().removeByEnrollmentOfficeId(enrollmentOfficeId);
                    getOfficeCapacityDAO().removeByEnrollmentOfficeId(enrollmentOfficeId);
                    return getDepartmentDAO().removeDepartments(enrollmentOfficeId.toString());
                } catch (Exception e) {
                    throw new ServiceException(
                            BizExceptionCode.EOS_103,
                            BizExceptionCode.GLB_008_MSG,
                            e);
                }
            } else {
                return true;
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.EOS_052,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    // Commented By Adldoost
    // @Override
    // @Permissions(value = "ems_issueNetworkToken")
    // public Long issueToken(Long entityId, TokenType type) throws
    // BaseException {
    // try {
    // return getTokenManagementService().issueFirstNetworkToken(entityId,
    // type);
    // } catch (BaseException e) {
    // throw e;
    // } catch (Exception e) {
    // throw new ServiceException(BizExceptionCode.EOS_043,
    // BizExceptionCode.GLB_008_MSG, e);
    // }
    // }

    // @Override
    // @Permissions(value = "ems_issueNetworkToken")
    // public Long reissueToken(Long enrollmentOfficeId) throws BaseException {
    // try {
    // Long tokenId =
    // getNetworkTokenDAO().findNotRevokedTokenIdByEnrollmentOfficeId(enrollmentOfficeId);
    //
    // if (tokenId != null)
    // return getTokenManagementService().reIssueNetworkTokenRequest(tokenId);
    // else
    // throw new ServiceException(BizExceptionCode.EOS_035,
    // BizExceptionCode.EOS_034_MSG);
    // } catch (BaseException e) {
    // throw e;
    // } catch (Exception e) {
    // throw new ServiceException(BizExceptionCode.EOS_044,
    // BizExceptionCode.GLB_008_MSG, e);
    // }
    // }

    // @Override
    // @Permissions(value = "ems_issueNetworkToken")
    // public Long replicateToken(Long entityId, TokenType tokenType,
    // ReplicaReason reason) throws BaseException {
    // try {
    // return getTokenManagementService().replicateNetworkToken(entityId,
    // tokenType, reason);
    // } catch (BaseException e) {
    // throw e;
    // } catch (Exception e) {
    // throw new ServiceException(BizExceptionCode.EOS_045,
    // BizExceptionCode.GLB_008_MSG, e);
    // }
    // }

    // @Override
    // @Permissions(value = "ems_revokeNetworkToken")
    // public void revokeToken(Long enrollmentOfficeId, ReplicaReason reason,
    // String comment) throws BaseException {
    // try {
    // Long tokenId =
    // getNetworkTokenDAO().findNotRevokedTokenIdByEnrollmentOfficeId(enrollmentOfficeId);
    //
    // if (tokenId != null)
    // getTokenManagementService().revokeNetworkToken(tokenId, reason, comment);
    // else
    // throw new ServiceException(BizExceptionCode.EOS_034,
    // BizExceptionCode.EOS_034_MSG);
    // } catch (BaseException e) {
    // throw e;
    // } catch (Exception e) {
    // throw new ServiceException(BizExceptionCode.EOS_046,
    // BizExceptionCode.GLB_008_MSG, e);
    // }
    // }

    // @Override
    // @Permissions(value = "ems_deliverNetworkToken")
    // public void deliverToken(Long enrollmentOfficeId) throws BaseException {
    // try {
    // Long tokenId =
    // getNetworkTokenDAO().findNotRevokedTokenIdByEnrollmentOfficeId(enrollmentOfficeId);
    //
    // if (tokenId != null)
    // getTokenManagementService().deliverNetworkToken(tokenId);
    // else
    // throw new ServiceException(BizExceptionCode.EOS_036,
    // BizExceptionCode.EOS_034_MSG);
    // } catch (BaseException e) {
    // throw e;
    // } catch (Exception e) {
    // throw new ServiceException(BizExceptionCode.EOS_047,
    // BizExceptionCode.GLB_008_MSG, e);
    // }
    // }

    @Override
    public Map<String, List<EnrollmentOfficeTO>> fetchModifiedEnrollmentOffices()
            throws BaseException {
        List<EnrollmentOfficeTO> newEnrollmentOfficeTOs;
        List<EnrollmentOfficeTO> modifiedEnrollmentOfficeTOs;
        List<EnrollmentOfficeTO> deletedEnrollmentOfficeTOs;

        newEnrollmentOfficeTOs = getEnrollmentOfficeDAO()
                .findNewActiveEnrollmentOffices();
        modifiedEnrollmentOfficeTOs = getEnrollmentOfficeDAO()
                .findActiveModifiedEnrollmentOffices();
        deletedEnrollmentOfficeTOs = getEnrollmentOfficeDAO()
                .findInActiveModifiedEnrollmentOffices();

        // just to output log
        // begin
        List<Long> ids = new ArrayList<Long>();
        for (EnrollmentOfficeTO enrollmentOfficeTO : newEnrollmentOfficeTOs)
            ids.add(enrollmentOfficeTO.getId());
        logger.info("EnrollmentOfficeServiceImpl().fetchModifiedEnrollmentOffices() method - New Active EnrollmentOffice Ids are : "
                + EmsUtil.toStringList(ids));
        ids.clear();

        for (EnrollmentOfficeTO enrollmentOfficeTO : modifiedEnrollmentOfficeTOs)
            ids.add(enrollmentOfficeTO.getId());
        logger.info("EnrollmentOfficeServiceImpl().fetchModifiedEnrollmentOffices() method - Active Modified EnrollmentOffice Ids are : "
                + EmsUtil.toStringList(ids));
        ids.clear();

        for (EnrollmentOfficeTO enrollmentOfficeTO : deletedEnrollmentOfficeTOs)
            ids.add(enrollmentOfficeTO.getId());
        logger.info("EnrollmentOfficeServiceImpl().fetchModifiedEnrollmentOffices() method - InActive Modified EnrollmentOffice Ids are : "
                + EmsUtil.toStringList(ids));
        // end

        Map<String, List<EnrollmentOfficeTO>> enrollmentOffices = new HashMap<String, List<EnrollmentOfficeTO>>();
        enrollmentOffices.put("newEnrollmentOffice", newEnrollmentOfficeTOs);
        enrollmentOffices.put("modifiedEnrollmentOffice",
                modifiedEnrollmentOfficeTOs);
        enrollmentOffices.put("deletedEnrollmentOffice",
                deletedEnrollmentOfficeTOs);

        return enrollmentOffices;
    }

    /**
     * The method notifySubSystemsAboutNewEnrollmentOffices is used to notify the sub systems 'CMS' and 'Portal' about
     * adding some new enrollment offices.
     *
     * @throws com.gam.commons.core.BaseException
     */
    @Override
//    @BizLoggable(logAction = "NOTIFY_ADDED", logEntityName = "ENROLLMENT_OFFICE", logActor = "System")
    public Long notifySubSystemsAboutEnrollmentOffices(EnrollmentOfficeTO enrollmentOfficeTO, String mode) throws BaseException {
        try {
            List<EnrollmentOfficeTO> enrollmentOfficeTOListForSync = new ArrayList<EnrollmentOfficeTO>();
            if ("NEW".equals(mode)) {
                enrollmentOfficeTO.setStatus(EnrollmentOfficeStatus.ENABLED);

                try {
                    getCMSService().addEnrollmentOffice(enrollmentOfficeTO, EnrollmentOfficeStatus.toInteger(EnrollmentOfficeStatus.ENABLED));
                    enrollmentOfficeTOListForSync.add(enrollmentOfficeTO);
                    logger.debug("successfully updated new enrollment office in CMS with id : " + enrollmentOfficeTO.getId());


                } catch (BaseException e) {
                    if (BizExceptionCode.CSI_098.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_100.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_101.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_102.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_103.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_104.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_105.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_134.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_114.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_147.equals(e.getExceptionCode())) {
                        logger.error(e.getMessage(), e);

                    } else if (BizExceptionCode.CSI_099.equals(e.getExceptionCode())) {
                        enrollmentOfficeTOListForSync.add(enrollmentOfficeTO);
                    } else {
                        enrollmentOfficeTOListForSync.add(enrollmentOfficeTO);

                    }
                }

//                getPortalBaseInfoService().updateEnrollmentOffices(enrollmentOfficeTOListForSync);

                logger.debug("successfully updated new enrollment office in CCOS with id : " + enrollmentOfficeTO.getId());
            } else if ("EDIT".equals(mode)) {
                enrollmentOfficeTO.setStatus(EnrollmentOfficeStatus.ENABLED);
                List<EnrollmentOfficeTO> activeEnrollmentOfficeTOList = new ArrayList<EnrollmentOfficeTO>();
                activeEnrollmentOfficeTOList.add(enrollmentOfficeTO);
                getCMSService().updateEnrollmentOffices(activeEnrollmentOfficeTOList, EnrollmentOfficeStatus.toInteger(EnrollmentOfficeStatus.ENABLED));

                enrollmentOfficeTOListForSync.add(enrollmentOfficeTO);

                logger.debug("successfully updated modified enrollment office in CMS with id : " + enrollmentOfficeTO.getId());

                enrollmentOfficeTOListForSync.add(enrollmentOfficeTO);

//                getPortalBaseInfoService().updateEnrollmentOffices(enrollmentOfficeTOListForSync);

                logger.debug("successfully updated modified enrollment office in CCOS with id : " + enrollmentOfficeTO.getId());
            } else if ("DELETE".equals(mode)) {
                enrollmentOfficeTO.setStatus(EnrollmentOfficeStatus.DISABLED);
                List<EnrollmentOfficeTO> inactiveEnrollmentOfficeTOList = new ArrayList<EnrollmentOfficeTO>();
                inactiveEnrollmentOfficeTOList.add(enrollmentOfficeTO);
                getCMSService().updateEnrollmentOffices(inactiveEnrollmentOfficeTOList, EnrollmentOfficeStatus.toInteger(EnrollmentOfficeStatus.DISABLED));


                logger.debug("successfully updated deleted enrollment office in CMS with id : " + enrollmentOfficeTO.getId());

                enrollmentOfficeTOListForSync.add(enrollmentOfficeTO);

                List<Long> inactiveEnrollmentOfficeIds = new ArrayList<Long>();
                inactiveEnrollmentOfficeIds.add(enrollmentOfficeTO.getId());

//                getPortalBaseInfoService().removeEnrollmentOffices(inactiveEnrollmentOfficeIds);

                logger.debug("successfully updated deleted enrollment office in CCOS with id : " + enrollmentOfficeTO.getId());
            }

//            getDepartmentDAO().updateSyncDateByCurrentDate(enrollmentOfficeIds);

            return enrollmentOfficeTOListForSync.get(0).getId();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.EOS_048, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    public void updateSyncDateByCurrentDate(Map<String, List<Long>> ids)
            throws BaseException {
        BusinessLogAction logAction = null;
        String additionalData = null;
        Boolean exceptionFlag = null;

        for (String keyName : ids.keySet()) {
            List<Long> enrollmentOfficeIds = ids.get(keyName);

            // Update lastSyncDate
            if (!enrollmentOfficeIds.isEmpty()) {
                if (keyName.contains("new") && keyName.contains("Success")) {
                    logger.info("EnrollmentOfficeServiceImpl().updateSyncDateByCurrentDate() method - "
                            + "Successfully updated New Active EnrollmentOffice Ids to update sync date are : "
                            + EmsUtil.toStringList(enrollmentOfficeIds));

                    getDepartmentDAO().updateSyncDateByCurrentDate(
                            enrollmentOfficeIds);

                    logger.info("EnrollmentOfficeServiceImpl().updateSyncDateByCurrentDate() method - "
                            + "Successfully update sync date for New Active EnrollmentOffices with ids : "
                            + EmsUtil.toStringList(enrollmentOfficeIds));

                    logAction = BusinessLogAction.NOTIFY_ADDED;
                    additionalData = EmsUtil
                            .toJSON("Successfully added new enrollment office ids : "
                                    + enrollmentOfficeIds);
                    exceptionFlag = true;
                } else if (keyName.contains("new")
                        && keyName.contains("Failure")) {
                    logger.info("EnrollmentOfficeServiceImpl().updateSyncDateByCurrentDate() method - "
                            + "New enrollment office ids failed to notify : "
                            + EmsUtil.toStringList(enrollmentOfficeIds));

                    logAction = BusinessLogAction.NOTIFY_ADDED;
                    additionalData = EmsUtil
                            .toJSON("New enrollment office ids failed to notify : "
                                    + enrollmentOfficeIds);
                    exceptionFlag = false;
                } else if (keyName.contains("edited")
                        && keyName.contains("Success")) {
                    logger.info("EnrollmentOfficeServiceImpl().updateSyncDateByCurrentDate() method - "
                            + "Successfully updated modified Active EnrollmentOffice Ids to update sync date are : "
                            + EmsUtil.toStringList(enrollmentOfficeIds));

                    getDepartmentDAO().updateSyncDateByCurrentDate(
                            enrollmentOfficeIds);

                    logger.info("EnrollmentOfficeServiceImpl().updateSyncDateByCurrentDate() method - "
                            + "Successfully update sync date for Modified Active EnrollmentOffices with ids : "
                            + EmsUtil.toStringList(enrollmentOfficeIds));

                    logAction = BusinessLogAction.NOTIFY_MODIFIED;
                    additionalData = EmsUtil
                            .toJSON("Successfully notified modify enrollment office ids : "
                                    + enrollmentOfficeIds);
                    exceptionFlag = true;
                } else if (keyName.contains("edited")
                        && keyName.contains("Failure")) {
                    logger.info("EnrollmentOfficeServiceImpl().updateSyncDateByCurrentDate() method - "
                            + "Modify enrollment office ids failed to notify : "
                            + EmsUtil.toStringList(enrollmentOfficeIds));

                    logAction = BusinessLogAction.NOTIFY_MODIFIED;
                    additionalData = EmsUtil
                            .toJSON("Modify enrollment office ids failed to notify : "
                                    + enrollmentOfficeIds);
                    exceptionFlag = false;
                } else if (keyName.contains("deleted")
                        && keyName.contains("Success")) {
                    logger.info("EnrollmentOfficeServiceImpl().updateSyncDateByCurrentDate() method - "
                            + "Successfully updated deleted Active EnrollmentOffice Ids to update sync date are : "
                            + EmsUtil.toStringList(enrollmentOfficeIds));

                    getDepartmentDAO().updateSyncDateByCurrentDate(
                            enrollmentOfficeIds);

                    logger.info("EnrollmentOfficeServiceImpl().updateSyncDateByCurrentDate() method - "
                            + "Successfully update sync date for InActive EnrollmentOffices with ids : "
                            + EmsUtil.toStringList(enrollmentOfficeIds));

                    logAction = BusinessLogAction.NOTIFY_REMOVED;
                    additionalData = EmsUtil
                            .toJSON("Successfully notified delete enrollment office ids : "
                                    + enrollmentOfficeIds);
                    exceptionFlag = true;
                } else if (keyName.contains("deleted")
                        && keyName.contains("Failure")) {
                    logger.info("EnrollmentOfficeServiceImpl().updateSyncDateByCurrentDate() method - "
                            + "Remove enrollment office ids failed to notify : "
                            + EmsUtil.toStringList(enrollmentOfficeIds));

                    logAction = BusinessLogAction.NOTIFY_REMOVED;
                    additionalData = EmsUtil
                            .toJSON("Remove enrollment office ids failed to notify : "
                                    + enrollmentOfficeIds);
                    exceptionFlag = false;
                }

                getBusinessLogService().createBusinessLog(logAction,
                        BusinessLogEntity.ENROLLMENT_OFFICE, "System",
                        additionalData, exceptionFlag);
            }
        }
    }

    /**
     * The method deleteEnrollmentOfficeToken is used to delete the token of a
     * specified EnrollmentOffice
     *
     * @param EnrollmentOfficeId
     *            is an instance of type {@link Long}, which represents a
     *            specified instance of type
     *            {@link com.gam.nocr.ems.data.domain.EnrollmentOfficeTO}
     * @throws com.gam.commons.core.BaseException
     */
    // Commented By Adldoost
    // @Override
    // public void deleteEnrollmentOfficeToken(Long EnrollmentOfficeId) throws
    // BaseException {
    // List<NetworkTokenTO> networkTokenTOList =
    // getNetworkTokenDAO().findByEnrollmentOfficeIdAndState(EnrollmentOfficeId,
    // TokenState.READY_TO_ISSUE);
    // if (!EmsUtil.checkListSize(networkTokenTOList)) {
    // deleteToken(null);
    // } else {
    // NetworkTokenTO networkTokenTO = networkTokenTOList.get(0);
    // deleteToken(networkTokenTO.getId());
    // }
    // }

    // Commented By Adldoost
    // @Override
    // public void deleteToken(Long tokenId) throws BaseException {
    // getTokenManagementService().deleteToken(tokenId, TokenOrigin.NETWORK);
    // }

    /**
     * The method checkInProgressRequests is used to check the requests that are
     * in progress
     *
     * @param enrollmentOfficeId is an instance of type {@link Long}, which represents a
     *                           specified instance of type {@link EnrollmentOfficeTO}
     * @return true or false
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public boolean checkInProgressRequests(Long enrollmentOfficeId)
            throws BaseException {
        List<CardRequestState> inProgressedStates = fetchInProgressedStatesFromProfile();
        List<Long> cardRequestIds = getCardRequestDAO()
                .findByEnrollmentOfficeIdAndStates(enrollmentOfficeId,
                        inProgressedStates);

        if (cardRequestIds.get(0) > 0) {
            List<Long> enrollmentOfficeIds = getEnrollmentOfficeDAO()
                    .fetchOtherNocrOfficeCountWithSameParentById(
                            enrollmentOfficeId);
            return enrollmentOfficeIds.get(0) > 0;
        } else {
            return false;
        }
    }

    @Override
    public List<Long> getEnrollmentOfficeListIds() throws BaseException {
        return getEnrollmentOfficeDAO().getEnrollmentOfficeListIds();
    }

    @Override
    public SearchResult fetchNOCRs(String searchString, int from, int to,
                                   String orderBy, Map additionalParams) throws BaseException {

        HashMap param = new HashMap();
        StringBuilder parts = new StringBuilder(",enrollment");

//        parts.append(",officeType");
//        param.put("type", "NOCR");

        if ((additionalParams != null) && (additionalParams.get("superDepartmentID") != null)) {
            parts.append(",parentDepId");
            param.put("superDepartmentID", additionalParams.get("superDepartmentID"));
        }
        if ((additionalParams != null) && (additionalParams.get("officeId") != null)
                && (additionalParams.get("officeType") != null)) {
            if (EnrollmentOfficeType.NOCR.name().equals(additionalParams.get("officeType")))
                parts.append(",otherNocr");
            else if (EnrollmentOfficeType.OFFICE.name().equals(additionalParams.get("officeType")))
                parts.append(",superior");

            param.put("officeId", additionalParams.get("officeId"));

        }

        param.put("depName", "%" + searchString + "%");
        try {
            ValueListHandler vlh = EMSValueListProvider.getProvider()
                    .loadList("NOCRAC",
                            ("main" + parts).split(","),
                            ("count" + parts).split(","),
                            param, orderBy, null);
            List list = vlh.getList(from, to, true);
            return new SearchResult(vlh.size(), list);
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.EOS_014, BizExceptionCode.GLB_006_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.EOS_015, BizExceptionCode.GLB_007_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.EOS_042, BizExceptionCode.GLB_008_MSG, e);
        }
    }


//Anbari
//	@Override
//	//@Asynchronous
//	public Long disableOfficeDeliveryFeature(Long eofId,Long supperiorOfficeId,String mode) throws BaseException {
//		List<Long> eofNotifiedIds = new ArrayList<Long>();
//		EnrollmentOfficeTO eof = getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class, eofId);
//		Long eofNotifiedId = null;
//		try {
//			eofNotifiedId = notifySubSystemsAboutEnrollmentOffices(eof,mode);
//		} catch (Exception e) {
//			// Call CMS or Portal Failed ... do roll back and set DISABLE
//			eof.setDeliver(EnrollmentOfficeDeliverStatus.ENABLED);
//			getEnrollmentOfficeDAO().update(eof);
//			throw new ServiceException(BizExceptionCode.EOS_079, BizExceptionCode.EOS_079_MSG, e);
//		}
//		if(eofNotifiedId != null)
//		{
//			eofNotifiedIds.add(eofNotifiedId);
//			getDepartmentDAO().updateSyncDateByCurrentDate(eofNotifiedIds);
//			getDispatchInfoDAO().replaceDispatchInfoReceiverIdBySuperriorOfficeId(eof.getId(), supperiorOfficeId);
//		}
//		return eofNotifiedId;
//	}


    //Anbari
    @Override
    @Asynchronous
    public Future disableOfficeDeliveryFeature(Long officeEOFID, Long supperiorOfficeId, String mode) throws BaseException {
        getDispatchInfoDAO().replaceDispatchInfoReceiverIdBySuperriorOfficeId(officeEOFID, supperiorOfficeId);
        getCardRequestDAO().updateCardRequestEOFdeliverId(officeEOFID, supperiorOfficeId);
        return new AsyncResult("");
    }

    // hossein messaging
    @Override
    public SearchResult fetchEnrollmentsByProvince(String searchString, int from,
                                                   int to, String orderBy, Map additionalParams) throws BaseException {
        HashMap param = new HashMap();
        StringBuilder parts = new StringBuilder("");
        Long personID = getPersonDAO().findPersonIdByUsername(getUserProfileTO().getUserName());

        param.put("officeName", "%" + searchString + "%");
        param.put("perid", personID);


        param.put("provinceId", additionalParams.get("provinceId"));

        try {
            ValueListHandler vlh = EMSValueListProvider.getProvider().loadList(
                    "enrollmentOfficeByProvince", ("main" + parts).split(","),
                    ("count" + parts).split(","), param, orderBy, null);
            List list = vlh.getList(from, to, true);
            return new SearchResult(vlh.size(), list);
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.EOS_075,
                    BizExceptionCode.GLB_006_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.EOS_076,
                    BizExceptionCode.GLB_007_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.EOS_077,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    public SearchResult fetchOfficesAutoComplete(UserProfileTO userProfileTO, String searchString, int from, int to,
                                                 String orderBy, Map additionalParams) throws BaseException {
        HashMap param = new HashMap();
        param.put("depName", "%" + searchString + "%");
        try {
            List<EMSAutocompleteTO> fetchOffices = getEnrollmentOfficeDAO().fetchOfficesAutoComplete(userProfileTO, searchString, from, to,
                    orderBy, additionalParams);

            Integer countOfficesAutoComplete = getEnrollmentOfficeDAO().countOfficesAutoComplete(userProfileTO, searchString, from, to,
                    orderBy, additionalParams);


            return new SearchResult(countOfficesAutoComplete, fetchOffices);

        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.EOS_042,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }


//Anbari
//	@Override
//	//@Asynchronous
//	public Long enableOfficeDeliveryFeature(Long eofId, Long superiorOfficeId,String mode)	throws BaseException {
//			List<Long> eofNotifiedIds = new ArrayList<Long>();
//			EnrollmentOfficeTO eof = getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class, eofId);
//			Long eofNotifiedId = null;
//			try {
//				eofNotifiedId = notifySubSystemsAboutEnrollmentOffices(eof,mode);
//			} catch (Exception e) {
//				// Call CMS or Portal Failed ... do roll back and set DISABLE
//				eof.setDeliver(EnrollmentOfficeDeliverStatus.DISABLED);
//				getEnrollmentOfficeDAO().update(eof);
//				throw new ServiceException(BizExceptionCode.EOS_080, BizExceptionCode.EOS_079_MSG, e);
//
//			}
//			if(eofNotifiedId != null)
//			{
//				eofNotifiedIds.add(eofNotifiedId);
//				getDepartmentDAO().updateSyncDateByCurrentDate(eofNotifiedIds);
//			}
//			return eofNotifiedId;
//	}


    /**
     * @author Madanipour
     */
    @Permissions(value = "ems_viewAndChangeOfficeSetting")
    @Override
    public void changeOfficeSetting(long enrollmentId, String officeSettingType)
            throws BaseException {

        OfficeSettingTO officeSettingTO = getOfficeSettingDAO()
                .findByOfficeId(enrollmentId);

        try {

            if (OfficeSettingType.FINGER_SCAN_SINGLE_MODE.toString().equals(
                    officeSettingType)) {
                officeSettingTO.setFingerScanSingleMode(changeSetting(officeSettingTO.getFingerScanSingleMode()));

            } else if (OfficeSettingType.DISABILITY_MODE.toString().equals(
                    officeSettingType)) {
                officeSettingTO.setDisabilityMode(changeSetting(officeSettingTO.getDisabilityMode()));

            } else if (OfficeSettingType.UPLOAD_PHOTO.toString().equals(
                    officeSettingType)) {
                officeSettingTO.setUploadPhoto(changeSetting(officeSettingTO.getUploadPhoto()));

            } else if (OfficeSettingType.REISSUE_REQUEST.toString().equals(
                    officeSettingType)) {
                officeSettingTO.setReissueRequest(changeSetting(officeSettingTO.getReissueRequest()));

            } else if (OfficeSettingType.ELDERLY_MODE.toString().equals(
                    officeSettingType)) {
                officeSettingTO.setElderlyMode(changeSetting(officeSettingTO.getElderlyMode()));

            } else if (OfficeSettingType.AMPUTATION_ANNOUNCMENT.toString()
                    .equals(officeSettingType)) {
                officeSettingTO.setAmputationAnnouncment(changeSetting(officeSettingTO.getAmputationAnnouncment()));

            } else if (OfficeSettingType.NMOC_GENERATION.toString().equals(
                    officeSettingType)) {
                officeSettingTO.setnMocGeneration(changeSetting(officeSettingTO.getnMocGeneration()));

            } else if (OfficeSettingType.ALLOW_EDIT_BACKGROUND.toString().equals(
                    officeSettingType)) {
                officeSettingTO.setAllowEditBackground(changeSetting(officeSettingTO.getAllowEditBackground()));

            } else if (OfficeSettingType.USE_SCANNER_UI.toString().equals(
                    officeSettingType)) {
                officeSettingTO.setUseScannerUI(changeSetting(officeSettingTO.getUseScannerUI()));

            } else if (OfficeSettingType.ALLOW_AMPUTATED_FINGER.toString().equals(
                    officeSettingType)) {
                officeSettingTO.setAllowAmputatedFingerStatusForElderly(changeSetting(officeSettingTO.getAllowAmputatedFingerStatusForElderly()));

            } else if (OfficeSettingType.ALLOW_CHANGE_FINGER.toString().equals(
                    officeSettingType)) {
                officeSettingTO.setAllowChangeFingerStatusDuringCaptureForElderly(changeSetting(officeSettingTO.getAllowChangeFingerStatusDuringCaptureForElderly()));
            }

        } catch (Exception exception) {
            throw new ServiceException(BizExceptionCode.EOS_081, exception);
        }

    }

    private Boolean changeSetting(Boolean settingValue) {
        if (settingValue.equals(Boolean.TRUE)) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    @Override
    public Boolean getAccessViewAndChangeOfficeSetting(UserProfileTO userProfile)
            throws BaseException {
        try {
            SecurityContextService securityContextService = new SecurityContextService();
            if (securityContextService.hasAccess(userProfileTO.getUserName(),
                    "ems_viewAndChangeOfficeSetting")) {
                return true;
            }

            return false;
        } catch (Exception e) {
            logger.error(BizExceptionCode.EOS_083, e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.EOS_083,
                    BizExceptionCode.GLB_008_MSG, e);
        }

    }


    //Anbari - userPerm-commented
    /*@Override
    public Boolean getAccessViewAndChangeOfficeSetting(UserProfileTO userProfile)
			throws BaseException {
		try {
			if (getUserManagementService().hasAccess(userProfileTO.getUserName(),
					"ems_viewAndChangeOfficeSetting")) {
				return true;
			}

			return false;
		} catch (Exception e) {
			logger.error(BizExceptionCode.EOS_083, e.getMessage(), e);
			throw new ServiceException(BizExceptionCode.EOS_083,
					BizExceptionCode.GLB_008_MSG, e);
		}

	}*/

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

    /**
     * استعلام مجاز بودن دفتر برای ثبت نام تک مرحله ای
     *
     * @param nationalId
     * @param healthStatusWTO
     * @param enrollmentOfficeId
     */
    /*public void checkEnrollmentOfficeEligibleForSingleStageEnrollment(
            String nationalId, HealthStatusWTO healthStatusWTO, Long enrollmentOfficeId) throws BaseException {
        if (nationalId == null) {
            throw new ServiceException(BizExceptionCode.EOS_100, BizExceptionCode.PRR_006_MSG);
        }
        if (!hasEnoughCapacityToday(nationalId, enrollmentOfficeId)) {
            throw new ServiceException(BizExceptionCode.EOS_087, BizExceptionCode.EOS_087_MSG);
        }
        if (!hasEnoughAccessibilityForSingleStageEnrollment(
                healthStatusWTO.getHasTwoFingerScanable().getCode()
                , healthStatusWTO.getPupilIsVisible().getCode()
                , enrollmentOfficeId)) {
            throw new ServiceException(BizExceptionCode.EOS_088, BizExceptionCode.EOS_088_MSG);
        }
        if (!hasEnoughInstrumentsForSingleStageEnrollment(
                healthStatusWTO.getAbilityToGo().getCode()
                , healthStatusWTO.getClimbingStairsAbility().getCode(), enrollmentOfficeId)) {
            throw new ServiceException(BizExceptionCode.EOS_089, BizExceptionCode.EOS_089_MSG);
        }
        if (!officeIsActive(enrollmentOfficeId)) {
            throw new ServiceException(BizExceptionCode.EOS_099, BizExceptionCode.EOS_099_MSG);
        }
    }*/
    public void checkEnrollmentOfficeEligibleForSingleStageEnrollment(
            String nationalId, HealthStatusWTO healthStatusWTO, Long enrollmentOfficeId) throws BaseException {
        if (nationalId == null) {
            throw new ServiceException(BizExceptionCode.EOS_100, BizExceptionCode.PRR_006_MSG);
        }

        EnrollmentOfficeSingleStageTO enrollmentOfficeSingleStageTO = findEnrollmentOfficeSingleStageById(enrollmentOfficeId);

        if (!hasEnoughCapacityToday(nationalId, enrollmentOfficeId)) {
            throw new ServiceException(BizExceptionCode.EOS_087, BizExceptionCode.EOS_087_MSG);
        }

        if (!hasEnoughAccessibilityForSingleStageEnrollment(
                healthStatusWTO.getHasTwoFingerScanable().getCode(),
                healthStatusWTO.getPupilIsVisible().getCode(),
                enrollmentOfficeSingleStageTO)) {
            throw new ServiceException(BizExceptionCode.EOS_088, BizExceptionCode.EOS_088_MSG);
        }

        if (!hasEnoughInstrumentsForSingleStageEnrollment(
                healthStatusWTO.getAbilityToGo().getCode(),
                healthStatusWTO.getClimbingStairsAbility().getCode(),
                enrollmentOfficeSingleStageTO)) {
            throw new ServiceException(BizExceptionCode.EOS_089, BizExceptionCode.EOS_089_MSG);
        }

        if (!enrollmentOfficeSingleStageTO.getEOF_IS_ACTIVE()) {
            throw new ServiceException(BizExceptionCode.EOS_099, BizExceptionCode.EOS_099_MSG);
        }
    }


    /**
     * استعلام کفایت ظرفیت
     *
     * @param enrollmentOfficeId
     */
    public Boolean hasEnoughCapacityToday(String nationalId, Long enrollmentOfficeId) throws ServiceException {
        Boolean result = Boolean.FALSE;
        try {
            CardRequestTO cardRequestTO = getCardRequestService().findLastRequestByNationalId(nationalId);
            if (cardRequestTO != null) {
                ReservationTO reservationTO = getReservationService().findReservationByCrqId(cardRequestTO.getId());
                if (reservationTO != null && reservationTO.getEnrollmentOffice() != null) {
                    if (Boolean.FALSE.equals(reservationTO.getEnrollmentOffice().getActive())) {
                        result = Boolean.TRUE;
                    }
                }
            } else {
                OfficeCapacityTO officeCapacity =
                        getOfficeCapacityService().findByEnrollmentOfficeIdAndDateAndWorkingHour(enrollmentOfficeId);
                OfficeActiveShiftTO activeShiftTO =
                        getOfficeActiveShiftService().findActiveShiftByOfficeCapacity(officeCapacity.getId());
                OfficeCapacityTO officeCapacityTO = activeShiftTO.getOfficeCapacity();
                if (activeShiftTO != null && officeCapacityTO != null) {
                    if(activeShiftTO.getRemainCapacity() > 0 && officeCapacityTO.getCapacity() > 0) {
                        if (Math.round(activeShiftTO.getRemainCapacity() * 10 / officeCapacityTO.getCapacity()) != 0) {
                            result = Boolean.TRUE;
                        }
                    }
                }
            }
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.EOS_091, BizExceptionCode.EOS_091_MSG, e, new Object[]{nationalId});
        }
        return result;
    }

    /**
     * استعلام تناسب شرایط محیطی با وضعیت تندرستی(اختیارات)
     *
     * @param pupilIsVisible
     * @param climbingStairsAbility
     * @param enrollmentOfficeId
     */
    public Boolean hasEnoughAccessibilityForSingleStageEnrollment(
            String climbingStairsAbility, String pupilIsVisible, Long enrollmentOfficeId) throws ServiceException {
        try {
            return getEnrollmentOfficeDAO().hasOfficeQueryByAccessibility(
                    climbingStairsAbility, pupilIsVisible, enrollmentOfficeId);
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.EOS_094, BizExceptionCode.EOS_094_MSG, e);
        }
    }


    /**
     * new version:
     * استعلام تناسب شرایط محیطی با وضعیت تندرستی(اختیارات)
     *
     * @param climbingStairsAbility
     * @param pupilIsVisible
     * @param enrollmentOfficeSingleStageTO
     * @return
     * @throws ServiceException
     */
    public Boolean hasEnoughAccessibilityForSingleStageEnrollment(
            String climbingStairsAbility,
            String pupilIsVisible,
            EnrollmentOfficeSingleStageTO enrollmentOfficeSingleStageTO) throws ServiceException {
        try {
            return getEnrollmentOfficeDAO().hasOfficeQueryByAccessibility(
                    climbingStairsAbility, pupilIsVisible, enrollmentOfficeSingleStageTO);
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.EOS_094, BizExceptionCode.EOS_094_MSG, e);
        }
    }

    /**
     * استعلام تناسب شرایط محیطی با وضعیت تندرستی(امکانات)
     *
     * @param abilityToGo
     * @param hasTwoFingersScanable
     * @param enrollmentOfficeId
     */
    public Boolean hasEnoughInstrumentsForSingleStageEnrollment(
            String abilityToGo, String hasTwoFingersScanable, Long enrollmentOfficeId) throws ServiceException {
        try {
            return getEnrollmentOfficeDAO().hasOfficeQueryByInstruments(abilityToGo, hasTwoFingersScanable, enrollmentOfficeId);
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.EOS_095, BizExceptionCode.EOS_095_MSG, e);
        }
    }


    /**
     * new version :
     * استعلام تناسب شرایط محیطی با وضعیت تندرستی(امکانات)
     *
     * @param abilityToGo
     * @param hasTwoFingersScanable
     * @param enrollmentOfficeSingleStageTO
     * @return
     * @throws ServiceException
     */
    public Boolean hasEnoughInstrumentsForSingleStageEnrollment(
            String abilityToGo,
            String hasTwoFingersScanable,
            EnrollmentOfficeSingleStageTO enrollmentOfficeSingleStageTO) throws ServiceException {
        try {
            return getEnrollmentOfficeDAO().hasOfficeQueryByInstruments(abilityToGo, hasTwoFingersScanable, enrollmentOfficeSingleStageTO);
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.EOS_095, BizExceptionCode.EOS_095_MSG, e);
        }
    }

    /**
     * اگر دفتر فعال باشد میتواند ثبت نام تک مرحله ای داشته باشد و در غیر اینصورت نمیتواند
     */
    public Boolean officeIsActive(Long enrollmentOfficeId) throws ServiceException {
        try {
            return getEnrollmentOfficeDAO().officeIsActive(enrollmentOfficeId);
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.EOS_098, BizExceptionCode.EOS_098_MSG, e);
        }
    }


    private CitizenService getCitizenService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        CitizenService citizenService;
        try {
            citizenService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_CITIZEN), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_CITIZEN.split(","));
        }
        citizenService.setUserProfileTO(getUserProfileTO());
        return citizenService;
    }


    private OfficeActiveShiftService getOfficeActiveShiftService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        OfficeActiveShiftService officeActiveShiftService;
        try {
            officeActiveShiftService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_OFFICE_ACTIVE_SHIFT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_OFFICE_ACTIVE_SHIFT.split(","));
        }
        officeActiveShiftService.setUserProfileTO(getUserProfileTO());
        return officeActiveShiftService;
    }

    private OfficeCapacityService getOfficeCapacityService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        OfficeCapacityService officeCapacityService;
        try {
            officeCapacityService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_OFFICE_CAPACITY), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_OFFICE_CAPACITY.split(","));
        }
        officeCapacityService.setUserProfileTO(getUserProfileTO());
        return officeCapacityService;
    }

    private ReservationService getReservationService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        ReservationService reservationService;
        try {
            reservationService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_RESERVATION), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_RESERVATION.split(","));
        }
        reservationService.setUserProfileTO(getUserProfileTO());
        return reservationService;
    }

    public void updateActiveShift(CardRequestTO cardRequest, Long officeId,
                                  Integer activeDate,
                                  ShiftEnum shiftNo) throws BaseException {
        OfficeActiveShiftTO activeShiftTO;
        activeShiftTO = findActiveShiftByOfficeAndDateAndShift(officeId, activeDate, shiftNo);
        if (activeShiftTO == null) {
            throw new ServiceException(BizExceptionCode.EOS_086, BizExceptionCode.EOS_086_MSG, new Object[]{officeId, String.valueOf(activeDate), shiftNo});
        }
        decreaseActiveShiftRemainingCapacity(activeShiftTO);
        if (!cardRequest.getReservations().isEmpty()) {
//			ReservationTO reservationTO = cardRequest.getReservations().stream().max(Comparator.comparing(ReservationTO::getId)).get();
            ReservationTO reservationTO = Collections.max(cardRequest.getReservations(), new Comparator<ReservationTO>() {
                @Override
                public int compare(ReservationTO reservationTO1, ReservationTO reservationTO2) {
                    return reservationTO1.getId().compareTo(reservationTO2.getId());
                }
            });
            officeId = reservationTO.getEnrollmentOffice().getId();
            activeDate = Integer.valueOf(CalendarUtil.getDate(reservationTO.getDate(), LangUtil.LOCALE_FARSI).replace("/",""));
            shiftNo = reservationTO.getShiftNo();
            activeShiftTO = findActiveShiftByOfficeAndDateAndShift(officeId, activeDate, shiftNo);
            if (activeShiftTO != null) {
                increaseActiveShiftRemainingCapacity(activeShiftTO);
            } else {
                logger.error(BizExceptionCode.EOS_097_MSG, new Object[]{String.valueOf(officeId), String.valueOf(activeDate), shiftNo});
                //throw new ServiceException(BizExceptionCode.EMS_REG_011, BizExceptionCode.EMS_REG_011_MSG, new Object[]{officeId, String.valueOf(activeDate), shiftNo});
            }
        }
    }

    private OfficeActiveShiftTO findActiveShiftByOfficeAndDateAndShift(Long eof, Integer date, ShiftEnum shiftNo) throws BaseException {
        return getOfficeActiveShiftService().OfficeActiveShiftByOfficeIdAndRsvDate(eof, shiftNo, date);
    }

    private void decreaseActiveShiftRemainingCapacity(OfficeActiveShiftTO activeShiftTO) throws BaseException {
        if (activeShiftTO.getRemainCapacity() <= 0) {
            throw new ServiceException(BizExceptionCode.EOS_090, BizExceptionCode.EOS_090_MSG, new Object[]{activeShiftTO.getId()});
        }
        getOfficeActiveShiftService().editActiveShiftRemainCapacity(activeShiftTO.getId(), activeShiftTO.getRemainCapacity() - 1);
    }

    private void increaseActiveShiftRemainingCapacity(OfficeActiveShiftTO activeShiftTO) throws BaseException {
        getOfficeActiveShiftService().editActiveShiftRemainCapacity(activeShiftTO.getId(), activeShiftTO.getRemainCapacity() + 1);
    }

    public void editEnrollmentOfficeAppointment(CardRequestTO cardRequest, OfficeAppointmentWTO officeAppointmentWTO) throws BaseException {
        ReservationTO reservationTO;
        Long officeId = officeAppointmentWTO.getRegistrationOffice().getId();
        Integer activeDate = officeAppointmentWTO.getActiveShift().getActiveDate();
        ShiftEnum shiftNo = officeAppointmentWTO.getActiveShift().getShiftNo();
        boolean mustCreateOrUpdate = false;
        if (cardRequest.getReservations().isEmpty()) {
            reservationTO = new ReservationTO();
            reservationTO.setCardRequest(cardRequest);
            reservationTO.setPaid(cardRequest.isPaid());
            reservationTO.setPaidDate(cardRequest.getPaidDate());
            updateActiveShift(cardRequest, officeId, activeDate, shiftNo);
            mustCreateOrUpdate = true;
        } else {
//			reservationTO = cardRequest.getReservations().stream().max(Comparator.comparing(ReservationTO::getId)).get();
            Collections.max(cardRequest.getReservations(), new Comparator<ReservationTO>() {
                @Override
                public int compare(ReservationTO reservationTO1, ReservationTO reservationTO2) {
                    return Double.compare(reservationTO1.getId(), reservationTO2.getId());
                }
            });
            reservationTO = Collections.max(cardRequest.getReservations(), null);
            Integer reserveDate =Integer.valueOf(CalendarUtil.getDate(reservationTO.getDate(), LangUtil.LOCALE_FARSI).replace("/",""));
            if (!reservationTO.getShiftNo().equals(shiftNo)
                    || !officeId.equals(reservationTO.getEnrollmentOffice().getId())
                    || !activeDate.equals(reserveDate)) {
                mustCreateOrUpdate = true;
            }
        }

        if (mustCreateOrUpdate) {
            updateActiveShift(cardRequest, officeId, activeDate, shiftNo);
            reservationTO.setShiftNo(officeAppointmentWTO.getActiveShift().getShiftNo());
            reservationTO.setEnrollmentOffice(new EnrollmentOfficeTO(officeAppointmentWTO.getRegistrationOffice().getId()));
            String gregorianDate = CalendarUtil.convertPersianToGregorian(DateUtil.formatAsDate(String.valueOf(officeAppointmentWTO.getAppointmentDate())));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000000");
            Date reservationDate = null;
            try {
                reservationDate = df.parse(gregorianDate.replaceAll("/", "-") + " " + "00:00:00.000000");
                reservationTO.setDate(reservationDate);
            } catch (ParseException e) {
                logger.error("editEnrollmentOfficeAppointment failed: Exception in parsing reservation date: " + gregorianDate + " for card request id: " + cardRequest.getId(), e);
            }
            reservationTO.setPortalReservationId(officeAppointmentWTO.getId());
            cardRequest.setEnrollmentOffice(reservationTO.getEnrollmentOffice());
            Integer today = Integer.valueOf(CalendarUtil.getDate(new Date(), new Locale("fa")).replace("/",""));
            if (today.equals(officeAppointmentWTO.getAppointmentDate())) {
                fillCardRequestRsvDate(cardRequest, reservationDate, false);
                getCardRequestService().updateCitizenByEstelam(cardRequest, true, false);
            } else {
                fillCardRequestRsvDate(cardRequest, reservationDate, true);
                getCardRequestService().update(cardRequest);
            }
            getCardRequestHistoryService().create(cardRequest, "CRS Request Id: " + cardRequest.getPortalRequestId().toString(),
                    SystemId.PORTAL, null, CardRequestHistoryAction.TRANSFER_FROM_PORTAL, null);
            reservationTO = getReservationService().create(reservationTO);
            getCardRequestHistoryService().create(
                    reservationTO.getCardRequest(),
                    "CRS Reservation Date: "
                            + gampooya.tools.date.DateUtil.convert(reservationTO.getDate(),
                            gampooya.tools.date.DateUtil.JALALI), SystemId.PORTAL, null,
                    CardRequestHistoryAction.TRANSFER_RESERVE, null);
        }
    }

    @Override
    public List<EnrollmentOfficeTO> getEnrollmentOfficeList() throws BaseException {
        return getEnrollmentOfficeDAO().getEnrollmentOfficeList();
    }

    @Override
    public List<OfficeCapacityTO> listOfficeCapacityByDate(int startDate, int endDate) throws BaseException {
        return getOfficeCapacityService().listOfficeCapacityByDate(startDate, endDate);
    }

    @Override
    public void updateActiveShiftForEnrollmentOfficeAndDate(EnrollmentOfficeTO enrollmentOfficeTO, Date fromDate, Map<Long, List<OfficeCapacityTO>> officeCapacityMap) throws BaseException {
        fromDate = EmsUtil.getDateAtMidnight(fromDate);
        int persianDate = Integer.valueOf(CalendarUtil.getDate(fromDate, new Locale("fa")).replace("/",""));
        List<OfficeCapacityTO> todateOfficeCapacity = new ArrayList<OfficeCapacityTO>();

        /*OfficeCapacityTO officeCapacityMorning = officeCapacityService.findbyEnrollmentOfficeIdAndDateAndShift(enrollmentOfficeTO.getId(), ShiftEnum.MORNING, persianDate);
        OfficeCapacityTO officeCapacityEvening = officeCapacityService.findbyEnrollmentOfficeIdAndDateAndShift(enrollmentOfficeTO.getId(), ShiftEnum.EVENING, persianDate);*/
        List<OfficeCapacityTO> officeCapacityTOs = officeCapacityMap.get(enrollmentOfficeTO.getId());

        if (EmsUtil.checkListSize(officeCapacityTOs)) {
            for (OfficeCapacityTO officeCapacityTO : officeCapacityTOs) {
                if (officeCapacityTO.getStartDate() <= persianDate && officeCapacityTO.getEndDate() >= persianDate) {
                    todateOfficeCapacity.add(officeCapacityTO);
                }
            }
        }

        if (EmsUtil.checkListSize(todateOfficeCapacity)) {
            for (OfficeCapacityTO officeCapacityTO : todateOfficeCapacity) {
                try {
                    OfficeActiveShiftTO officeActiveShiftTO = getOfficeActiveShiftService().OfficeActiveShiftByOfficeIdAndRsvDate(enrollmentOfficeTO.getId(), officeCapacityTO.getShiftNo(), persianDate);
                    getOfficeActiveShiftService().activeShiftSaveOrUpdate(officeCapacityTO, officeActiveShiftTO, enrollmentOfficeTO, fromDate);
                } catch (Exception ex) {
                    jobLogger.error("Error occurred on create/update active shift (" + String.valueOf(enrollmentOfficeTO.getId()) + ", "
                            + String.valueOf(persianDate) + "," + officeCapacityTO.getShiftNo().getCode() + ")", ex);
                    logger.error("Error occurred on create/update active shift (" + String.valueOf(enrollmentOfficeTO.getId()) + ", "
                            + String.valueOf(persianDate) + "," + officeCapacityTO.getShiftNo().getCode() + ")", ex);
                }
            }
        }
    }


    public void fillCardRequestRsvDate(CardRequestTO cardRequest, Date reservationDate, boolean changeEstelamFlag) throws ServiceException {
        if (cardRequest.getState().equals(CardRequestState.RESERVED)) {
            if (cardRequest.getAuthenticity() != null) {
                cardRequest.setReEnrolledDate(reservationDate);
            } else {
                cardRequest.setReservationDate(reservationDate);
            }
            if (changeEstelamFlag) {
                changeCardRequestEstelamStatus(cardRequest);
            }
            cardRequest.setRequestedSmsStatus(0);
        } else if (cardRequest.getState().equals(CardRequestState.DOCUMENT_AUTHENTICATED) ||
                cardRequest.getState().equals(CardRequestState.REFERRED_TO_CCOS)) {
            cardRequest.setReEnrolledDate(reservationDate);
            cardRequest.setRequestedSmsStatus(0);
            if (changeEstelamFlag) {
                changeCardRequestEstelamStatus(cardRequest);
            }
        } else {
            throw new ServiceException(BizExceptionCode.EOS_096, BizExceptionCode.EOS_096_MSG);
        }

    }

    private void changeCardRequestEstelamStatus(CardRequestTO cardRequest) {
        cardRequest.setEstelam2Flag(Estelam2FlagType.R);
        String doesNotExist = Configuration.getProperty("dont.exit");
        cardRequest.getCitizen().setFirstNamePersian(doesNotExist);
        cardRequest.getCitizen().setSurnamePersian(doesNotExist);
        cardRequest.getCitizen().getCitizenInfo().setBirthCertificateId(doesNotExist);
    }

    private CardRequestService getCardRequestService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        CardRequestService cardRequestService;
        try {
            cardRequestService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_CARD_REQUEST), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_CARD_REQUEST.split(","));
        }
        cardRequestService.setUserProfileTO(getUserProfileTO());
        return cardRequestService;
    }

    private CardRequestHistoryService getCardRequestHistoryService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        CardRequestHistoryService cardRequestHistoryService;
        try {
            cardRequestHistoryService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_CARD_REQUEST_HISTORY), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_CARD_REQUEST_HISTORY.split(","));
        }
        cardRequestHistoryService.setUserProfileTO(getUserProfileTO());
        return cardRequestHistoryService;
    }

    @Override
    public EnrollmentOfficeSingleStageTO findEnrollmentOfficeSingleStageById(Long enrollmentOfficeId) throws BaseException {
        return getEnrollmentOfficeDAO().findEnrollmentOfficeSingleStageById(enrollmentOfficeId);
    }

    private RatingInfoDAO getRatingInfoDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_RATING_INFO));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.RMS_006, BizExceptionCode.GLB_001_MSG, e, EMSLogicalNames.DAO_RATING_INFO.split(",")
            );
        }
    }
}
