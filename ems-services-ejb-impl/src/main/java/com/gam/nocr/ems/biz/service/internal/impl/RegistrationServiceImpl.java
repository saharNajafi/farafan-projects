package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactory;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.core.util.SecureString;
import com.gam.commons.profile.ProfileException;
import com.gam.commons.profile.ProfileManager;
import com.gam.commons.security.signature.Signature;
import com.gam.nocr.ems.biz.service.*;
import com.gam.nocr.ems.biz.service.external.client.portal.ItemWTO;
import com.gam.nocr.ems.config.*;
import com.gam.nocr.ems.data.dao.*;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.vol.BirthCertIssPlaceVTO;
import com.gam.nocr.ems.data.domain.vol.PersonEnquiryVTO;
import com.gam.nocr.ems.data.domain.vol.RegistrationVIPVTO;
import com.gam.nocr.ems.data.domain.ws.*;
import com.gam.nocr.ems.data.enums.*;
import com.gam.nocr.ems.data.mapper.tomapper.DocumentTypeMapper;
import com.gam.nocr.ems.util.CcosRequestStateBundle;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.date.DateFormatException;
import gampooya.tools.date.DateUtil;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.util.Store;
import org.nocr.NIN;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.transaction.UserTransaction;
import java.sql.Timestamp;
import java.util.*;

import static com.gam.nocr.ems.config.EMSLogicalNames.*;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "RegistrationService")
@Local(RegistrationServiceLocal.class)
@Remote(RegistrationServiceRemote.class)
public class RegistrationServiceImpl extends EMSAbstractService implements
        RegistrationServiceLocal, RegistrationServiceRemote {

    private static final String DEFAULT_CARD_REQUEST_IDLE_PERIOD = "365";
    private static final String DEFAULT_KEY_FING_CANDIDATE_SIZE_KB = "12";
    private static final String DEFAULT_KEY_FING_NORMAL_1_SIZE_KB = "1.5";
    private static final String DEFAULT_KEY_FING_NORMAL_2_SIZE_KB = "1.5";
    private static final String DEFAULT_KEY_SCANNED_DOCUMENT_SIZE_KB = "400";
    private static final String DEFAULT_KEY_SCANNED_DOCUMENT_MIN_SIZE_KB = "100";
    private static final String DEFAULT_SKIP_CMS_CHECK = "false";
    private static final String DEFAULT_SKIP_ESTELAM_CHECK = "false";
    private static final String VIP_STR = "VIP";
    private static final String DEFAULT_KEY_IMS_ESTELAM_IAMGE_ENABLE = "1";
    private static final Logger vipLogger = BaseLog
            .getLogger("VipLogger");

    @Resource
    SessionContext sessionContext;

    @Resource
    UserTransaction userTransaction;

    private static final Logger logger = BaseLog
            .getLogger(RegistrationServiceImpl.class);
    private static final Logger jobLOGGER = BaseLog
            .getLogger("portalFetchCardRequest");

    private CitizenDAO getCitizenDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_CITIZEN));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_001,
                    BizExceptionCode.GLB_001_MSG, e, DAO_CITIZEN.split(","));
        }
    }

    private CardRequestDAO getCardRequestDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_CARD_REQUEST));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_002,
                    BizExceptionCode.GLB_001_MSG, e,
                    DAO_CARD_REQUEST.split(","));
        }
    }

    private EnrollmentOfficeDAO getEnrollmentOfficeDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_ENROLLMENT_OFFICE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_002,
                    BizExceptionCode.GLB_001_MSG, e,
                    DAO_ENROLLMENT_OFFICE.split(","));
        }
    }

    private CardRequestHistoryDAO getCardRequestHistoryDAO()
            throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_CARD_REQUEST_HISTORY));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_101,
                    BizExceptionCode.GLB_001_MSG, e,
                    DAO_CARD_REQUEST_HISTORY.split(","));
        }
    }

    private CitizenInfoDAO getCitizenInfoDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_CITIZEN_INFO));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_003,
                    BizExceptionCode.GLB_001_MSG, e,
                    DAO_CITIZEN_INFO.split(","));
        }
    }

    private BusinessLogDAO getBusinessLogDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_BUSINESSLOG));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_007,
                    BizExceptionCode.GLB_001_MSG, e, DAO_BUSINESSLOG.split(","));
        }
    }

    private BiometricDAO getBiometricDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_BIOMETRIC));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_004,
                    BizExceptionCode.GLB_001_MSG, e, DAO_BIOMETRIC.split(","));
        }
    }

    private BiometricInfoDAO getBiometricInfoDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_BIOMETRIC_INFO));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_004,
                    BizExceptionCode.GLB_001_MSG, e,
                    DAO_BIOMETRIC_INFO.split(","));
        }
    }

    private PhotoVipDAO getPhotoVipDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_PHOTO_VIP));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_004, BizExceptionCode.GLB_001_MSG, e,
                    DAO_PHOTO_VIP.split(","));
        }
    }

    private BlackListDAO getBlackListDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_BLACK_LIST));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_077, BizExceptionCode.GLB_001_MSG, e,
                    DAO_BLACK_LIST.split(","));
        }
    }

    private PersonTokenDAO getPersonTokenDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_PERSON_TOKEN));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_126,
                    BizExceptionCode.GLB_001_MSG, e,
                    DAO_PERSON_TOKEN.split(","));
        }
    }

    private PersonDAO getPersonDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_PERSON));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.ABS_002,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_ABOUT.split(","));
        }
    }

    private IMSManagementService getIMSManagementService() throws BaseException {
        IMSManagementService imsManagementService = null;
        try {
            imsManagementService = ServiceFactoryProvider.getServiceFactory()
                    .getService(getServiceJNDIName(SRV_IMS_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_068,
                    BizExceptionCode.GLB_002_MSG, e,
                    SRV_IMS_MANAGEMENT.split(","));
        }
        imsManagementService.setUserProfileTO(getUserProfileTO());
        return imsManagementService;
    }

    private DocumentDAO getDocumentDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_DOCUMENT));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_005,
                    BizExceptionCode.GLB_001_MSG, e, DAO_DOCUMENT.split(","));
        }
    }

    private ServiceDocumentTypeDAO getServiceDocumentTypeDAO()
            throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_SERVICE_DOCTYPE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_057,
                    BizExceptionCode.GLB_001_MSG, e,
                    DAO_SERVICE_DOCTYPE.split(","));
        }
    }

    private CardManagementService getCardManagementService() throws BaseException {
        CardManagementService cardManagementService;
        try {
            cardManagementService = ServiceFactoryProvider.getServiceFactory().getService(
                    getServiceJNDIName(SRV_CARD_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_061, BizExceptionCode.GLB_002_MSG, e,
                    SRV_CARD_MANAGEMENT.split(","));
        }
        cardManagementService.setUserProfileTO(getUserProfileTO());
        return cardManagementService;
    }

    private EncryptedCardRequestDAO getEncryptedCardRequestDAO()
            throws BaseException {
        try {
            return DAOFactoryProvider
                    .getDAOFactory()
                    .getDAO(EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_ENCRYPTED_CARD_REQUEST));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_084,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_ENCRYPTED_CARD_REQUEST.split(","));
        }
    }

    private RegistrationPaymentDAO getRegistrationPaymentDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_REGISTRATION_PAYMENT));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RS_001,
                    BizExceptionCode.GLB_001_MSG, e);
        }
    }

    /**
     * getPortalManagementService
     *
     * @return an instance of type {@link PortalManagementService}
     * @throws {@link BaseException}
     */
    private PortalManagementService getPortalManagementService()
            throws BaseException {
        PortalManagementService portalManagementService;
        try {
            portalManagementService = ServiceFactoryProvider
                    .getServiceFactory()
                    .getService(
                            EMSLogicalNames
                                    .getServiceJNDIName(EMSLogicalNames.SRV_PORTAL_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_075,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_PORTAL_MANAGEMENT.split(","));
        }
        portalManagementService.setUserProfileTO(getUserProfileTO());
        return portalManagementService;
    }

    //Anbari : Separate  Insertion of CardRequest Blobs
    private CardRequestBlobsDAO getCardRequestBlobs() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_CARDREQUEST_BLOBS));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_162,
                    BizExceptionCode.GLB_001_MSG, e, DAO_DOCUMENT.split(","));
        }
    }


    private boolean isNullOrEmptyString(String str) throws BaseException {
        return str == null || str.trim().equals("");
    }

    private void checkCardRequestValid(CardRequestTO crq) throws BaseException {
        if (crq == null) {
            throw new ServiceException(BizExceptionCode.RSI_006, BizExceptionCode.RSI_006_MSG);
        }
        if (crq.getState() == null) {
            throw new ServiceException(BizExceptionCode.RSI_012, BizExceptionCode.RSI_012_MSG);
        }
        if (crq.getType() == null) {
            throw new ServiceException(BizExceptionCode.RSI_013, BizExceptionCode.RSI_013_MSG);
        }
        if (!isNullOrEmptyString(crq.getTrackingID()) && (crq.getTrackingID().length() != 10)) {
            throw new ServiceException(BizExceptionCode.RSI_014, BizExceptionCode.RSI_014_MSG);
        }
    }

    @Override
    public void checkPreviousCardRequestNotStopped(CitizenTO citizenTo) throws BaseException {
        List<CardRequestTO> oldRequests = getCardRequestDAO().findByNationalId(citizenTo.getNationalID());
        if (oldRequests != null) {
            for (CardRequestTO oldReq : oldRequests) {
                if (CardRequestState.STOPPED.equals(oldReq.getState())) {
                    throw new ServiceException(BizExceptionCode.RSI_131, BizExceptionCode.RSI_129_MSG);
                }
            }
        }
    }

    private void checkCitizenValid(CitizenTO ctz) throws BaseException {
        if (ctz == null) {
            throw new ServiceException(BizExceptionCode.RSI_011,
                    BizExceptionCode.RSI_011_MSG);
        }
        if (isNullOrEmptyString(ctz.getFirstNamePersian())) {
            throw new ServiceException(BizExceptionCode.RSI_008,
                    BizExceptionCode.RSI_008_MSG);
        }
        if (ctz.getFirstNamePersian().length() > 42) {
            throw new ServiceException(BizExceptionCode.RSI_015,
                    BizExceptionCode.RSI_015_MSG);
        }
        if (isNullOrEmptyString(ctz.getSurnamePersian())) {
            throw new ServiceException(BizExceptionCode.RSI_009,
                    BizExceptionCode.RSI_009_MSG);
        }
        if (ctz.getSurnamePersian().length() > 42) {
            throw new ServiceException(BizExceptionCode.RSI_016,
                    BizExceptionCode.RSI_016_MSG);
        }
        if (ctz.getNationalID() == null) {
            throw new ServiceException(BizExceptionCode.RSI_010,
                    BizExceptionCode.RSI_010_MSG);
        }
        try {
            if (!NIN.validate(Long.valueOf(ctz.getNationalID()))) {
                throw new ServiceException(BizExceptionCode.RSI_017,
                        BizExceptionCode.RSI_017_MSG);
            }
        } catch (NumberFormatException e) {
            throw new ServiceException(BizExceptionCode.RSI_123,
                    BizExceptionCode.RSI_017_MSG);
        }
    }

    private void checkCitizenInBlackList(String nid) throws BaseException {
        List<BlackListTO> blackList = getBlackListDAO().findByNid(nid);
        if (blackList != null && blackList.size() > 0)
            throw new ServiceException(BizExceptionCode.RSI_078,
                    BizExceptionCode.RSI_078_MSG);
    }

    private void checkCitizenInfoValid(CitizenInfoTO czi) throws BaseException { // todo:
        if (czi == null)
            throw new ServiceException(BizExceptionCode.RSI_018,
                    BizExceptionCode.RSI_018_MSG);
        if (czi.getBirthCertificateSeries() == null)
            throw new ServiceException(BizExceptionCode.RSI_019,
                    BizExceptionCode.RSI_019_MSG);
        if (czi.getFatherBirthDateSolar() == null)
            throw new ServiceException(BizExceptionCode.RSI_020,
                    BizExceptionCode.RSI_020_MSG);
        if (isNullOrEmptyString(czi.getFirstNameEnglish()))
            throw new ServiceException(BizExceptionCode.RSI_021,
                    BizExceptionCode.RSI_021_MSG);
        if (czi.getMotherBirthDateSolar() == null)
            throw new ServiceException(BizExceptionCode.RSI_022,
                    BizExceptionCode.RSI_022_MSG);
        if (isNullOrEmptyString(czi.getSurnameEnglish()))
            throw new ServiceException(BizExceptionCode.RSI_023,
                    BizExceptionCode.RSI_023_MSG);
        // Decided to use 'birthCertificateIssuancePlace' instead of
        // 'birthCertificateIssuancePlaceProvince'
        // if (czi.getBirthCertificateIssuancePlaceProvince() == null) {
        // throw new ServiceException(BizExceptionCode.RSI_024,
        // BizExceptionCode.RSI_024_MSG);
        // }
        // if (czi.getLiving() == null)
        // throw new ServiceException(BizExceptionCode.RSI_025,
        // BizExceptionCode.RSI_025_MSG);
        // if (czi.getLivingCity() == null)
        // throw new ServiceException(BizExceptionCode.RSI_026,
        // BizExceptionCode.RSI_026_MSG);
        if (czi.getReligion() == null)
            throw new ServiceException(BizExceptionCode.RSI_027,
                    BizExceptionCode.RSI_027_MSG);
        if (czi.getBirthCertificateId() != null
                && czi.getBirthCertificateId().length() > 10)
            throw new ServiceException(BizExceptionCode.RSI_028,
                    BizExceptionCode.RSI_028_MSG);
        if (!isNullOrEmptyString(czi.getBirthCertificateSeries())
                && czi.getBirthCertificateSeries().length() != 6)
            throw new ServiceException(BizExceptionCode.RSI_029,
                    BizExceptionCode.RSI_029_MSG);
        if (!isNullOrEmptyString(czi.getFatherFirstNameEnglish())
                && czi.getFatherFirstNameEnglish().length() > 25)
            throw new ServiceException(BizExceptionCode.RSI_030,
                    BizExceptionCode.RSI_030_MSG);
        if (!isNullOrEmptyString(czi.getFatherFirstNamePersian())
                && czi.getFatherFirstNamePersian().length() > 42)
            throw new ServiceException(BizExceptionCode.RSI_031,
                    BizExceptionCode.RSI_031_MSG);
        if (isNullOrEmptyString(czi.getFatherNationalID())) {
            throw new ServiceException(BizExceptionCode.RSI_096,
                    BizExceptionCode.RSI_096_MSG);
        }
        try {
            if (!ConstValues.DEFAULT_NID.equals(czi.getFatherNationalID())) {
                if (!NIN.validate(Long.valueOf(czi.getFatherNationalID()))) {
                    throw new ServiceException(BizExceptionCode.RSI_032,
                            BizExceptionCode.RSI_032_MSG);
                }
            }
        } catch (NumberFormatException e) {
            throw new ServiceException(BizExceptionCode.RSI_032,
                    BizExceptionCode.RSI_032_MSG);
        }
        if (czi.getFirstNameEnglish().length() > 25)
            throw new ServiceException(BizExceptionCode.RSI_033,
                    BizExceptionCode.RSI_033_MSG);
        if (!isNullOrEmptyString(czi.getMotherFirstNamePersian())
                && czi.getMotherFirstNamePersian().length() > 42)
            throw new ServiceException(BizExceptionCode.RSI_034,
                    BizExceptionCode.RSI_034_MSG);
        if (isNullOrEmptyString(czi.getMotherNationalID())) {
            throw new ServiceException(BizExceptionCode.RSI_097,
                    BizExceptionCode.RSI_097_MSG);
        }
        try {
            if (!ConstValues.DEFAULT_NID.equals(czi.getMotherNationalID())) {
                if (!NIN.validate(Long.valueOf(czi.getMotherNationalID()))) {
                    throw new ServiceException(BizExceptionCode.RSI_035,
                            BizExceptionCode.RSI_035_MSG);
                }
            }
        } catch (NumberFormatException e) {
            throw new ServiceException(BizExceptionCode.RSI_035,
                    BizExceptionCode.RSI_035_MSG);
        }
        if (!isNullOrEmptyString(czi.getPostcode())
                && (czi.getPostcode().length() != 10))
            throw new ServiceException(BizExceptionCode.RSI_036,
                    BizExceptionCode.RSI_036_MSG);
        if (czi.getSurnameEnglish().length() > 25)
            throw new ServiceException(BizExceptionCode.RSI_037,
                    BizExceptionCode.RSI_037_MSG);
        if (!isNullOrEmptyString(czi.getFatherFatherName())
                && (czi.getFatherFatherName().length() > 42))
            throw new ServiceException(BizExceptionCode.RSI_038,
                    BizExceptionCode.RSI_038_MSG);
        if (!isNullOrEmptyString(czi.getFatherSurname())
                && (czi.getFatherSurname().length() > 42))
            throw new ServiceException(BizExceptionCode.RSI_039,
                    BizExceptionCode.RSI_039_MSG);
        if (!isNullOrEmptyString(czi.getFatherBirthCertificateId())) {
            if (czi.getFatherBirthCertificateId().trim().startsWith("0")) {
                if ((czi.getFatherBirthCertificateId().length() != 10)
                        && (czi.getFatherBirthCertificateId().length() != 1))
                    throw new ServiceException(BizExceptionCode.RSI_124,
                            BizExceptionCode.RSI_124_MSG);
            }
            if (czi.getFatherBirthCertificateId().length() > 10)
                throw new ServiceException(BizExceptionCode.RSI_040,
                        BizExceptionCode.RSI_040_MSG);
        }
        if (!isNullOrEmptyString(czi.getFatherBirthCertificateSeries())
                && (czi.getFatherBirthCertificateSeries().length() != 6))
            throw new ServiceException(BizExceptionCode.RSI_041,
                    BizExceptionCode.RSI_041_MSG);
        if (!isNullOrEmptyString(czi.getMotherSurname())
                && (czi.getMotherSurname().length() > 42))
            throw new ServiceException(BizExceptionCode.RSI_042,
                    BizExceptionCode.RSI_042_MSG);
        if (!isNullOrEmptyString(czi.getMotherFatherName())
                && (czi.getMotherFatherName().length() > 42))
            throw new ServiceException(BizExceptionCode.RSI_043,
                    BizExceptionCode.RSI_043_MSG);
        if (!isNullOrEmptyString(czi.getMotherBirthCertificateId())) {
            if (czi.getMotherBirthCertificateId().trim().startsWith("0")) {
                if ((czi.getMotherBirthCertificateId().length() != 10)
                        && (czi.getMotherBirthCertificateId().length() != 1))
                    throw new ServiceException(BizExceptionCode.RSI_125,
                            BizExceptionCode.RSI_124_MSG);
            }
            if (czi.getMotherBirthCertificateId().length() > 10)
                throw new ServiceException(BizExceptionCode.RSI_044,
                        BizExceptionCode.RSI_044_MSG);
        }
        if (!isNullOrEmptyString(czi.getFatherBirthCertificateSeries())
                && (czi.getFatherBirthCertificateSeries().length() != 6))
            throw new ServiceException(BizExceptionCode.RSI_045,
                    BizExceptionCode.RSI_045_MSG);
        if (!isNullOrEmptyString(czi.getAddress())
                && (czi.getAddress().length() > 300))
            throw new ServiceException(BizExceptionCode.RSI_046,
                    BizExceptionCode.RSI_046_MSG);
        if (!isNullOrEmptyString(czi.getPhone())
                && (czi.getPhone().length() > 20))
            throw new ServiceException(BizExceptionCode.RSI_047,
                    BizExceptionCode.RSI_047_MSG);
        if (EmsUtil.checkString(czi.getMobile())
                && !"NA".equals(czi.getMobile())) {
            if (!EmsUtil.checkRegex(czi.getMobile(), EmsUtil.cellNoConstraint)) {
                throw new ServiceException(BizExceptionCode.RSI_048,
                        BizExceptionCode.RSI_048_MSG);
            }
        }
        // if (!isNullOrEmptyString(czi.getMobile()) &&
        // (czi.getMobile().length() > 11))
        // throw new ServiceException(BizExceptionCode.RSI_048,
        // BizExceptionCode.RSI_048_MSG);
        if (czi.getBirthDateGregorian() == null)
            throw new ServiceException(BizExceptionCode.RSI_070,
                    BizExceptionCode.RSI_070_MSG);
        if (isNullOrEmptyString(czi.getBirthDateLunar()))
            throw new ServiceException(BizExceptionCode.RSI_071,
                    BizExceptionCode.RSI_071_MSG);
        try {
            DateUtil.convert(czi.getBirthDateLunar(), DateUtil.HIJRI);
        } catch (DateFormatException e) {
            throw new ServiceException(BizExceptionCode.RSI_072,
                    BizExceptionCode.RSI_072_MSG, e);
        }
        if (isNullOrEmptyString(czi.getBirthDateSolar())) {
            try {
                czi.setBirthDateSolar(DateUtil.convert(
                        czi.getBirthDateGregorian(), DateUtil.JALALI));
            } catch (Exception e) {
                throw new ServiceException(BizExceptionCode.RSI_073,
                        BizExceptionCode.RSI_073_MSG, e);
            }
        }
        if (isNullOrEmptyString(czi.getBirthDateSolar()))
            throw new ServiceException(BizExceptionCode.RSI_074,
                    BizExceptionCode.RSI_074_MSG);
    }

    private void checkSpouseValid(SpouseTO spouse) throws BaseException {
        if (!EmsUtil.checkString(spouse.getSpouseNationalID()))
            throw new ServiceException(BizExceptionCode.RSI_109,
                    BizExceptionCode.RSI_109_MSG);
        if (spouse.getSpouseBirthDate() == null)
            throw new ServiceException(BizExceptionCode.RSI_110,
                    BizExceptionCode.RSI_110_MSG + spouse.getSpouseNationalID());
        if (spouse.getSpouseMarriageDate() == null)
            throw new ServiceException(BizExceptionCode.RSI_111,
                    BizExceptionCode.RSI_111_MSG + spouse.getSpouseNationalID());
        if (spouse.getMaritalStatus() == null)
            throw new ServiceException(BizExceptionCode.RSI_112,
                    BizExceptionCode.RSI_112_MSG + spouse.getSpouseNationalID());
        // else {
        // if (spouse.getSpouseDeathOrDivorceDate() == null)
        // throw new ServiceException(BizExceptionCode.RSI_113,
        // BizExceptionCode.RSI_113_MSG);
        // }
    }

    private void checkChildValid(ChildTO child) throws BaseException {
        if (!EmsUtil.checkString(child.getChildNationalID()))
            throw new ServiceException(BizExceptionCode.RSI_114,
                    BizExceptionCode.RSI_114_MSG);
        if (child.getChildBirthDateSolar() == null)
            throw new ServiceException(BizExceptionCode.RSI_115,
                    BizExceptionCode.RSI_115_MSG);
    }

    private void checkBiometricValid(BiometricTO bio) throws BaseException {
        if (bio == null) {
            throw new ServiceException(BizExceptionCode.RSI_051,
                    BizExceptionCode.RSI_051_MSG);
        }
        if (bio.getType() == null) {
            throw new ServiceException(BizExceptionCode.RSI_052,
                    BizExceptionCode.RSI_052_MSG);
        }
        if (bio.getData() == null) {
            throw new ServiceException(BizExceptionCode.RSI_053,
                    BizExceptionCode.RSI_053_MSG);
        }
    }

    private void checkDocumentValid(DocumentTO doc) throws BaseException {
        if (doc == null) {
            throw new ServiceException(BizExceptionCode.RSI_054,
                    BizExceptionCode.RSI_054_MSG);
        }
        if (doc.getType() == null) {
            throw new ServiceException(BizExceptionCode.RSI_055,
                    BizExceptionCode.RSI_055_MSG);
        }
        if (doc.getData() == null) {
            throw new ServiceException(BizExceptionCode.RSI_056,
                    BizExceptionCode.RSI_056_MSG);
        }
    }

    private void checkPreviousCardStateValid(CardRequestDAO cardRequestDAO,
                                             List<CitizenTO> citizens) throws BaseException {
        List<CardRequestTO> cardRequestsForCitizen = cardRequestDAO
                .findByCitizen(citizens.get(0));
        CardRequestState state = cardRequestsForCitizen.get(0).getState();
        if ((state != CardRequestState.STOPPED_NOT_FOLLOW_LIMITED_RESERVE)
                && (state != CardRequestState.NOT_DELIVERED)
                && (state != CardRequestState.STOPPED)
                && (state != CardRequestState.DELIVERED)
//				&& (state != CardRequestState.REPEALED)
                ) {
            throw new ServiceException(BizExceptionCode.RSI_062,
                    BizExceptionCode.RSI_062_MSG, new String[]{citizens
                    .get(0).getNationalID()});
        }
    }

    private void secureString(CardRequestTO cardRequestTO) {
        cardRequestTO.getCitizen().setFirstNamePersian(
                SecureString.getSecureString(cardRequestTO.getCitizen()
                        .getFirstNamePersian()));
        cardRequestTO.getCitizen().setSurnamePersian(
                SecureString.getSecureString(cardRequestTO.getCitizen()
                        .getSurnamePersian()));
        cardRequestTO
                .getCitizen()
                .getCitizenInfo()
                .setBirthCertificateIssuancePlace(
                        SecureString.getSecureString(cardRequestTO.getCitizen()
                                .getCitizenInfo()
                                .getBirthCertificateIssuancePlace()));
        cardRequestTO
                .getCitizen()
                .getCitizenInfo()
                .setFatherFirstNamePersian(
                        SecureString.getSecureString(cardRequestTO.getCitizen()
                                .getCitizenInfo().getFatherFirstNamePersian()));
        cardRequestTO
                .getCitizen()
                .getCitizenInfo()
                .setFatherSurname(
                        SecureString.getSecureString(cardRequestTO.getCitizen()
                                .getCitizenInfo().getFatherSurname()));
        cardRequestTO
                .getCitizen()
                .getCitizenInfo()
                .setFatherFatherName(
                        SecureString.getSecureString(cardRequestTO.getCitizen()
                                .getCitizenInfo().getFatherFatherName()));
        cardRequestTO
                .getCitizen()
                .getCitizenInfo()
                .setMotherFirstNamePersian(
                        SecureString.getSecureString(cardRequestTO.getCitizen()
                                .getCitizenInfo().getMotherFirstNamePersian()));
        cardRequestTO
                .getCitizen()
                .getCitizenInfo()
                .setMotherSurname(
                        SecureString.getSecureString(cardRequestTO.getCitizen()
                                .getCitizenInfo().getMotherSurname()));
        cardRequestTO
                .getCitizen()
                .getCitizenInfo()
                .setMotherFatherName(
                        SecureString.getSecureString(cardRequestTO.getCitizen()
                                .getCitizenInfo().getMotherFatherName()));
    }

    private void checkCitizenInfoValidInIMS(CardRequestTO crq)
            throws BaseException {
        CitizenTO ctz = crq.getCitizen();
        CitizenInfoTO czi = ctz.getCitizenInfo();
        List<PersonEnquiryVTO> vtos = new ArrayList<PersonEnquiryVTO>();
        // Enquiry for CITIZEN
        vtos.add(new PersonEnquiryVTO(ctz.getNationalID(), // national id
                ctz.getFirstNamePersian(), // first name
                ctz.getSurnamePersian(), // surname
                czi.getFatherFirstNamePersian(), // father first name
                null, // birth certificate seri
                ConstValues.DEFAULT_CERT_SERIAL, // birth certificate serial
                czi.getBirthCertificateId(), // birth certificate id
                czi.getBirthDateSolar(), czi.getGender())); // birth date

        // Enquiry for citizen FATHER
        if (!ConstValues.DEFAULT_NID.equals(czi.getFatherNationalID())) {
            vtos.add(new PersonEnquiryVTO(czi.getFatherNationalID(), // national
                    // id
                    czi.getFatherFirstNamePersian(), // first name
                    czi.getFatherSurname(), // surname
                    czi.getFatherFatherName(), // father first name
                    null, // birth certificate seri
                    ConstValues.DEFAULT_CERT_SERIAL, // birth certificate
                    // serial
                    czi.getFatherBirthCertificateId(), // birth certificate id
                    DateUtil.convert(czi.getFatherBirthDateSolar(),
                            DateUtil.JALALI), Gender.M)); // birth date
        }
        // Enquiry for citizen MOTHER
        if (!ConstValues.DEFAULT_NID.equals(czi.getMotherNationalID())) {
            vtos.add(new PersonEnquiryVTO(czi.getMotherNationalID(), // national
                    // id
                    czi.getMotherFirstNamePersian(), // first name
                    czi.getMotherSurname(), // surname
                    czi.getMotherFatherName(), // father first name
                    null, // birth certificate seri
                    ConstValues.DEFAULT_CERT_SERIAL, // birth certificate
                    // serial
                    czi.getMotherBirthCertificateId(), // birth certificate id
                    DateUtil.convert(czi.getMotherBirthDateSolar(),
                            DateUtil.JALALI), Gender.F)); // birth date
        }
        for (SpouseTO sps : czi.getSpouses()) {
            // Spouse Gender
            Gender spouseGender = Gender.UNDEFINED;
            switch (czi.getGender()) {
                case M:
                    spouseGender = Gender.F;
                    break;

                case F:
                    spouseGender = Gender.M;
                    break;
            }
            // Enquiry for citizen SPOUSE
            vtos.add(new PersonEnquiryVTO(
                    sps.getSpouseNationalID(), // national
                    // id
                    sps.getSpouseFirstNamePersian(), // first name
                    sps.getSpouseSurnamePersian(), // surname
                    sps.getSpouseFatherName(), // father first name
                    null, // birth certificate seri
                    ConstValues.DEFAULT_CERT_SERIAL, // birth certificate
                    // serial
                    sps.getSpouseBirthCertificateId(), // birth certificate id
                    DateUtil.convert(sps.getSpouseBirthDate(), DateUtil.JALALI), // birth
                    // date
                    spouseGender)); // Gender
        }

        for (ChildTO chi : czi.getChildren()) {
            // Enquiry for citizen CHILD
            vtos.add(new PersonEnquiryVTO(chi.getChildNationalID(), // national
                    // id
                    chi.getChildFirstNamePersian(), // first name
                    null, // surname todo: Child surname
                    // needs to be added later,
                    // currently no surname is
                    // provided for child
                    chi.getChildFatherName(), // father first name
                    null, // birth certificate seri
                    ConstValues.DEFAULT_CERT_SERIAL, // birth certificate
                    // serial
                    chi.getChildBirthCertificateId(), // birth certificate id
                    DateUtil.convert(chi.getChildBirthDateSolar(),
                            DateUtil.JALALI), // birth date
                    chi.getChildGender())); // Gender
        }

        HashMap<String, Boolean> map = getIMSManagementService()
                .getOnlineEnquiry(
                        vtos.toArray(new PersonEnquiryVTO[vtos.size()]));
        List<String> invalidNIds = new ArrayList<String>();
        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
            if (entry.getValue().equals(Boolean.FALSE)) {
                invalidNIds.add(entry.getKey());
            }
        }

        if (!invalidNIds.isEmpty()) {
            StringBuilder str = new StringBuilder();
            for (String invalidNId : invalidNIds) {
                str.append(invalidNId).append(", ");
            }
            logger.error("\n\nInvalid nationalIds in checkCitizenInfoValidInIMS method are : "
                    + str + "\n\n");
            throw new ServiceException(BizExceptionCode.RSI_069,
                    BizExceptionCode.RSI_069_MSG,
                    new String[]{invalidNIds.toString()});

        } else {
            CitizenInfoTO newCitizenInfo = crq.getCitizen().getCitizenInfo();
            logger.info("\n----------------------- Citizen fields before running IMS update -----------------------\n");
            logger.info("\nCitizen Data : " + crq.getCitizen().getNationalID()
                    + " " + crq.getCitizen().getFirstNamePersian() + " "
                    + crq.getCitizen().getSurnamePersian() + "\n");
            logger.info("\nBirthCertificate Serial Number : "
                    + newCitizenInfo.getBirthCertificateSeries() + "\n");
            logger.info("\nBirthCertificate ID : "
                    + newCitizenInfo.getBirthCertificateId() + "\n");
            logger.info("\nSolarBirthDate : "
                    + newCitizenInfo.getBirthDateSolar() + "\n");
            logger.info("\nGregorianBirthDate : "
                    + newCitizenInfo.getBirthDateGregorian() + "\n");
            logger.info("\nLunarBirthDate : "
                    + newCitizenInfo.getBirthDateLunar() + "\n");
            logger.info("\nFather FirstName : "
                    + newCitizenInfo.getFatherFirstNamePersian() + "\n");
            logger.info("\nFather LastName : "
                    + newCitizenInfo.getFatherSurname() + "\n");
            logger.info("\nFather BirthDate : "
                    + newCitizenInfo.getFatherBirthDateSolar() + "\n");
            logger.info("\nFather BirthCertificateID : "
                    + newCitizenInfo.getFatherBirthCertificateId() + "\n");
            logger.info("\nFather BirthCertificateSeries : "
                    + newCitizenInfo.getFatherBirthCertificateSeries() + "\n");
            logger.info("\nMother FirstName : "
                    + newCitizenInfo.getMotherFirstNamePersian() + "\n");
            logger.info("\nMother SurName : "
                    + newCitizenInfo.getMotherSurname() + "\n");
            logger.info("\nMother BirthDate : "
                    + newCitizenInfo.getMotherBirthDateSolar() + "\n");
            logger.info("\nMother BirthCertificateSeries : "
                    + newCitizenInfo.getMotherBirthCertificateSeries() + "\n");
            logger.info("\nMother BirthCertificateID : "
                    + newCitizenInfo.getMotherBirthCertificateId() + "\n");
            logger.info("\n-----------------------------------------------------------------------------------------\n");

            try {
                if (EmsUtil.checkListSize(vtos)) {
                    getIMSManagementService().updatePersonInfoByOnlineEnquiry(
                            crq,
                            vtos.toArray(new PersonEnquiryVTO[vtos.size()]));
                    logger.info("\n\nThe cardRequestTO has been set via IMS sub system.\n");
                }
            } catch (BaseException e) {
                throw e;
            } catch (Exception e) {
                throw new ServiceException(BizExceptionCode.RSI_092,
                        BizExceptionCode.GLB_008_MSG, e);
            }

            logger.info("\n----------------------- Citizen fields after running IMS update -----------------------\n");
            logger.info("\nCitizen Data : " + crq.getCitizen().getNationalID()
                    + " " + crq.getCitizen().getFirstNamePersian() + " "
                    + crq.getCitizen().getSurnamePersian() + "\n");
            logger.info("\nBirthCertificate Serial Number : "
                    + newCitizenInfo.getBirthCertificateSeries() + "\n");
            logger.info("\nBirthCertificate ID : "
                    + newCitizenInfo.getBirthCertificateId() + "\n");
            logger.info("\nSolarBirthDate : "
                    + newCitizenInfo.getBirthDateSolar() + "\n");
            logger.info("\nGregorianBirthDate : "
                    + newCitizenInfo.getBirthDateGregorian() + "\n");
            logger.info("\nLunarBirthDate : "
                    + newCitizenInfo.getBirthDateLunar() + "\n");
            logger.info("\nFather FirstName : "
                    + newCitizenInfo.getFatherFirstNamePersian() + "\n");
            logger.info("\nFather LastName : "
                    + newCitizenInfo.getFatherSurname() + "\n");
            logger.info("\nFather BirthDate : "
                    + newCitizenInfo.getFatherBirthDateSolar() + "\n");
            logger.info("\nFather BirthCertificateID : "
                    + newCitizenInfo.getFatherBirthCertificateId() + "\n");
            logger.info("\nFather BirthCertificateSeries : "
                    + newCitizenInfo.getFatherBirthCertificateSeries() + "\n");
            logger.info("\nMother FirstName : "
                    + newCitizenInfo.getMotherFirstNamePersian() + "\n");
            logger.info("\nMother SurName : "
                    + newCitizenInfo.getMotherSurname() + "\n");
            logger.info("\nMother BirthDate : "
                    + newCitizenInfo.getMotherBirthDateSolar() + "\n");
            logger.info("\nMother BirthCertificateSeries : "
                    + newCitizenInfo.getMotherBirthCertificateSeries() + "\n");
            logger.info("\nMother BirthCertificateID : "
                    + newCitizenInfo.getMotherBirthCertificateId() + "\n");
            logger.info("\n-----------------------------------------------------------------------------------------\n");
        }
        // return vtos;
    }

    private void checkEncryptedCardRequest(EncryptedCardRequestTO to)
            throws BaseException {
        if (to == null)
            throw new ServiceException(BizExceptionCode.RSI_085,
                    BizExceptionCode.RSI_085_MSG);
        if (to.getCitizenInfo() == null)
            throw new ServiceException(BizExceptionCode.RSI_086,
                    BizExceptionCode.RSI_086_MSG);
        if (to.getFaces() == null)
            throw new ServiceException(BizExceptionCode.RSI_087,
                    BizExceptionCode.RSI_087_MSG);
        if (to.getDocuments() == null)
            throw new ServiceException(BizExceptionCode.RSI_088,
                    BizExceptionCode.RSI_088_MSG);
    }

    private Long save(CardRequestTO newCardRequest) throws BaseException {
        CitizenDAO citizenDAO = getCitizenDAO();
        CardRequestDAO cardRequestDAO = getCardRequestDAO();
        CitizenInfoDAO citizenInfoDAO;

        List<CitizenTO> citizens = citizenDAO.findByNID(newCardRequest.getCitizen().getNationalID());

        CardRequestTO cr;

        List<BirthCertIssPlaceVTO> birthCertIssPlaceVTOList = fetchBirthCertIssPlace(newCardRequest.getCitizen().getNationalID());

        if (EmsUtil.checkListSize(birthCertIssPlaceVTOList))
            newCardRequest.getCitizen().getCitizenInfo().setBirthCertificateIssuancePlace(birthCertIssPlaceVTOList.get(0).getDepName());
        else
            throw new ServiceException(BizExceptionCode.RSI_107, BizExceptionCode.RSI_107_MSG + newCardRequest.getCitizen().getNationalID());

        if (citizens.size() == 0) {// then citizen with this nid does not yet exist
            CitizenTO newCitizen = citizenDAO.create(newCardRequest.getCitizen());
            newCardRequest.setCitizen(newCitizen);
        } else {// then citizen with this nid exists
            if (newCardRequest.getId() == null) {// Do this check when saving new request - not when updating
                checkPreviousCardStateValid(cardRequestDAO, citizens);
            }
            CitizenTO citizenLoadedFromDb = citizens.get(0);
            CitizenInfoTO citizenInfoLoadedFromDb = citizenLoadedFromDb.getCitizenInfo();
            citizenInfoDAO = getCitizenInfoDAO();
            if (citizenInfoLoadedFromDb != null) {// Information for this citizen is currently in the db
                citizenLoadedFromDb.setCitizenInfo(null);
                citizenInfoLoadedFromDb.setCitizen(null);
                citizenInfoDAO.delete(citizenInfoLoadedFromDb); // Cascade on remove should be implemented in the database
            }
            CitizenInfoTO newCitizenInfo = newCardRequest.getCitizen().getCitizenInfo();
            newCitizenInfo.setCitizen(citizenLoadedFromDb);
            newCitizenInfo.setId(null);
            citizenInfoDAO.create(newCitizenInfo);
            citizenLoadedFromDb.setCitizenInfo(newCitizenInfo);

            CitizenTO newCitizen = newCardRequest.getCitizen();
            citizenLoadedFromDb.setFirstNamePersian(newCitizen.getFirstNamePersian());
            citizenLoadedFromDb.setNationalID(newCitizen.getNationalID());
            citizenLoadedFromDb.setSurnamePersian(newCitizen.getSurnamePersian());

            //For requests those purged before we should set purgeBio and purgeDate to default value.
            if (citizenLoadedFromDb.getPurgeBio() == null || citizenLoadedFromDb.getPurgeBio().equals(Boolean.TRUE)) {
                citizenLoadedFromDb.setPurgeBio(Boolean.FALSE);
                citizenLoadedFromDb.setPurgeBioDate(null);
            }

            newCardRequest.setCitizen(citizenLoadedFromDb);
        }


        createFakePaymentForCCOSVIPAndReplica(newCardRequest);


        if (newCardRequest.getId() == null) {
            if (newCardRequest.getTrackingID() == null || newCardRequest.getTrackingID().trim().length() == 0 ||
                    newCardRequest.getTrackingID().equals("0000000000")) {
                newCardRequest.setTrackingID(EmsUtil.generateTrackingId(newCardRequest.getCitizen().getNationalID() + new Date()));
            }
            cr = cardRequestDAO.create(newCardRequest);
        } else {
            CardRequestTO to = cardRequestDAO.fetchCardRequest(newCardRequest.getId());
            if (to != null) {
                newCardRequest.setPortalRequestId(to.getPortalRequestId());
                newCardRequest.setLastSyncDate(to.getLastSyncDate());
                newCardRequest.setPortalEnrolledDate(to.getPortalEnrolledDate());
                newCardRequest.setPortalLastModifiedDate(to.getPortalLastModifiedDate());
                newCardRequest.setOrigin(to.getOrigin());
                newCardRequest.setPriority(to.getPriority());
                newCardRequest.setOriginalCardRequestOfficeId(to.getOriginalCardRequestOfficeId());
                newCardRequest.setReservationDate(to.getReservationDate());
                newCardRequest.setLockDate(to.getLockDate());
                newCardRequest.setEstelam2Flag(to.getEstelam2Flag());
                // *************************Anbari:Payment
                if (!newCardRequest.getState()
                        .equals(CardRequestState.RESERVED)) {
                    newCardRequest.setPaid(to.isPaid());
                    newCardRequest.setPaidDate(to.getPaidDate());
                }
                // *****************************
                newCardRequest.setRequestedSmsStatus(to.getRequestedSmsStatus());


            }
            cr = cardRequestDAO.update(newCardRequest);
        }

        return cr.getId();
    }

    private void createFakePaymentForCCOSVIPAndReplica(CardRequestTO newCardRequest) throws BaseException {
        if (newCardRequest.getRegistrationPaymentTO() == null) {
            RegistrationPaymentTO registrationPaymentTO = new RegistrationPaymentTO();
            registrationPaymentTO.setDescription("");
            Long paymentOrderId = EmsUtil.getRandomPaymentOrderId();
            registrationPaymentTO.setOrderId(paymentOrderId);
            registrationPaymentTO.setSucceed(false);
            registrationPaymentTO.setResCode(null);
            registrationPaymentTO.setCitizenTO(newCardRequest.getCitizen());
            registrationPaymentTO.setConfirmed(false);
            registrationPaymentTO.setPaymentDate(new Date());
            registrationPaymentTO.setMatchFlag((short) 1);
            registrationPaymentTO.setPaidBank(IPGProviderEnum.UNDEFIGNED);
            String nationalId = newCardRequest.getCitizen().getNationalID();
            Map<String, String> registrationPaymentResult =
                    getRegistrationPaymentService().getPaymentAmountAndPaymentCode(newCardRequest.getType(), nationalId);
            registrationPaymentTO.setAmountPaid(Integer.valueOf(registrationPaymentResult.get("paymentAmount")));
            registrationPaymentTO.setPaymentCode(registrationPaymentResult.get("paymentCode"));
            getRegistrationPaymentDAO().create(registrationPaymentTO);
            newCardRequest.setPaid(false);
            newCardRequest.setRegistrationPaymentTO(registrationPaymentTO);
        }
    }

    @Override
    @Permissions(value = "ems_addCardRequest")
    @BizLoggable(logAction = "INSERT", logEntityName = "REQUEST")
    public Long saveFromCcos(CardRequestTO newCardRequest) throws BaseException {
        try {
            // Check validation of new request
            checkCardRequestValid(newCardRequest);
            checkCitizenValid(newCardRequest.getCitizen());
            checkCitizenInBlackList(newCardRequest.getCitizen().getNationalID());
            checkCitizenInfoValid(newCardRequest.getCitizen().getCitizenInfo());
            if (EmsUtil.checkListSize(newCardRequest.getCitizen().getCitizenInfo().getSpouses())) {
                for (SpouseTO spouseTO : newCardRequest.getCitizen().getCitizenInfo().getSpouses())
                    checkSpouseValid(spouseTO);
            }
            if (EmsUtil.checkListSize(newCardRequest.getCitizen().getCitizenInfo().getChildren())) {
                for (ChildTO childTO : newCardRequest.getCitizen().getCitizenInfo().getChildren())
                    checkChildValid(childTO);
            }
            secureString(newCardRequest);

            /**
             * The method checkCitizenInfoValidInIMS is only needed to call for those requests, which have a request type of 'FIRST_CARD'.
             * For other types of requests, the action of checking with IMS is taken place whenever the request is fetched.
             */
            if (CardRequestType.FIRST_CARD.equals(newCardRequest.getType())) {
                //checkCitizenInfoValidInIMS(newCardRequest);
                getIMSManagementService().doEstelam3(newCardRequest.getId(), true);
            }

//	            checkPreviousCardRequest(newCardRequest);

            //Anbari:
            // checkCardLostState(newCardRequest);
            if (!Boolean.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_SKIP_CMS_CHECK, DEFAULT_SKIP_CMS_CHECK)))
                getCardManagementService().checkRequestValidation(newCardRequest.getCitizen().getNationalID(), newCardRequest.getType());


            newCardRequest.setOrigin(CardRequestOrigin.C); // Origin is CCOS
            Long id = save(newCardRequest);

            if (newCardRequest.getCitizen().getId() != null) {
                //biometric_info record should delete
                getBiometricInfoDAO().removeBiometricInfo(newCardRequest.getCitizen().getId());
            }

            getCardRequestHistoryDAO().create(new CardRequestTO(id), "Sodoore Mojaddad with type : " + newCardRequest.getType(), SystemId.CCOS, null,
                    CardRequestHistoryAction.COMPLETE_REGISTRATION, getUserProfileTO().getUserName());
            return id;
        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    //Anbari
//	private void checkCardLostState(CardRequestTO newCardRequest)
//			throws BaseException {
//		CardRequestTO cardRequestTO = getCardRequestDAO().find(
//				CardRequestTO.class, newCardRequest.getId());
//		if(cardRequestTO != null)
//		{
//			CardTO card = cardRequestTO.getCard();
//			if(card.getLostDate() != null && (CardState.MISSED != card.getState() || CardState.LOST != card.getState()))
//					 throw new ServiceException(BizExceptionCode.CMS_083, BizExceptionCode.CMS_074_MSG);	
//				
//				
//		}
//
//	}

    @Override
    @Permissions(value = "ems_addCardRequest")
    public Long saveFromMES(CardRequestTO newCardRequest) throws BaseException {
        try {
            // Check validation of new request
            checkCardRequestValid(newCardRequest);
            checkCitizenValid(newCardRequest.getCitizen());
            checkCitizenInBlackList(newCardRequest.getCitizen().getNationalID());
            checkCitizenInfoValid(newCardRequest.getCitizen().getCitizenInfo());
            if (EmsUtil.checkListSize(newCardRequest.getCitizen()
                    .getCitizenInfo().getSpouses())) {
                for (SpouseTO spouseTO : newCardRequest.getCitizen()
                        .getCitizenInfo().getSpouses())
                    checkSpouseValid(spouseTO);
            }
            if (EmsUtil.checkListSize(newCardRequest.getCitizen()
                    .getCitizenInfo().getChildren())) {
                for (ChildTO childTO : newCardRequest.getCitizen()
                        .getCitizenInfo().getChildren())
                    checkChildValid(childTO);
            }

            // Check the validity of the non portal request with CMS
            // if (newCardRequest.getPortalRequestId() == null ||
            // newCardRequest.getPortalRequestId().compareTo(0L) <= 0) {
            // checkCitizenInfoValidInIMS(newCardRequest);
            // }
            if (!Boolean.valueOf(EmsUtil.getProfileValue(
                    ProfileKeyName.KEY_SKIP_CMS_CHECK, DEFAULT_SKIP_CMS_CHECK)))
                getCardManagementService().checkRequestValidation(
                        newCardRequest.getCitizen().getNationalID(),
                        newCardRequest.getType());

            newCardRequest.setOrigin(CardRequestOrigin.M); // Origin is MES
            return save(newCardRequest);
        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    private Long saveFromJob(CardRequestTO newCardRequest) throws BaseException {
        try {
            jobLOGGER.info("Validating card request data for portal request identifier {} before save ", newCardRequest.getPortalRequestId());
            // Check validation of new request
            checkCardRequestValid(newCardRequest);
            checkCitizenValid(newCardRequest.getCitizen());
            checkCitizenInBlackList(newCardRequest.getCitizen().getNationalID());
            checkCitizenInfoValid(newCardRequest.getCitizen().getCitizenInfo());
            if (EmsUtil.checkListSize(newCardRequest.getCitizen().getCitizenInfo().getSpouses())) {
                for (SpouseTO spouseTO : newCardRequest.getCitizen().getCitizenInfo().getSpouses())
                    checkSpouseValid(spouseTO);
            }
            if (EmsUtil.checkListSize(newCardRequest.getCitizen().getCitizenInfo().getChildren())) {
                for (ChildTO childTO : newCardRequest.getCitizen().getCitizenInfo().getChildren())
                    checkChildValid(childTO);
            }

            if (!EmsUtil.checkString(newCardRequest.getCitizen().getCitizenInfo().getFatherNationalID()))
                newCardRequest.getCitizen().getCitizenInfo().setFatherNationalID("0000000000");
            if (!EmsUtil.checkString(newCardRequest.getCitizen().getCitizenInfo().getMotherNationalID()))
                newCardRequest.getCitizen().getCitizenInfo().setMotherNationalID("0000000000");

            userTransaction.begin();

            if (newCardRequest.getOrigin() == null)
                newCardRequest.setOrigin(CardRequestOrigin.P); // Origin is Portal

            newCardRequest.setOriginalCardRequestOfficeId(null);
            Long cardRequestId = save(newCardRequest);

            getCardRequestHistoryDAO().create(newCardRequest, "Portal Request Id: " + newCardRequest.getPortalRequestId().toString(),
                    SystemId.PORTAL, null, CardRequestHistoryAction.TRANSFER_FROM_PORTAL, null);

            userTransaction.commit();

            return cardRequestId;
        } catch (Exception e) {
            try {
                userTransaction.rollback();
            } catch (Exception e1) {
                logger.error(e.getMessage(), e);
            }
            if (e instanceof BaseException)
                throw (BaseException) e;
            else
                throw new ServiceException(BizExceptionCode.RSI_079, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private Long newSaveFromJob(CardRequestTO newCardRequest) throws BaseException {
        try {
            jobLOGGER.info("Validating card request data for portal request identifier {} before save ", newCardRequest.getPortalRequestId());
//	    		// Check validation of new request
            checkCardRequestValid(newCardRequest);
            checkCitizenValid(newCardRequest.getCitizen());
            checkCitizenInBlackList(newCardRequest.getCitizen().getNationalID());
            checkCitizenInfoValid(newCardRequest.getCitizen().getCitizenInfo());
            if (EmsUtil.checkListSize(newCardRequest.getCitizen().getCitizenInfo().getSpouses())) {
                for (SpouseTO spouseTO : newCardRequest.getCitizen().getCitizenInfo().getSpouses())
                    checkSpouseValid(spouseTO);
            }
            if (EmsUtil.checkListSize(newCardRequest.getCitizen().getCitizenInfo().getChildren())) {
                for (ChildTO childTO : newCardRequest.getCitizen().getCitizenInfo().getChildren())
                    checkChildValid(childTO);
            }

            if (!EmsUtil.checkString(newCardRequest.getCitizen().getCitizenInfo().getFatherNationalID()))
                newCardRequest.getCitizen().getCitizenInfo().setFatherNationalID("0000000000");
            if (!EmsUtil.checkString(newCardRequest.getCitizen().getCitizenInfo().getMotherNationalID()))
                newCardRequest.getCitizen().getCitizenInfo().setMotherNationalID("0000000000");

            Long cardRequestId = save(newCardRequest);

            getCardRequestHistoryDAO().create(newCardRequest, "Portal Request Id: " + newCardRequest.getPortalRequestId().toString(),
                    SystemId.PORTAL, null, CardRequestHistoryAction.TRANSFER_FROM_PORTAL, null);

            return cardRequestId;
        } catch (Exception e) {
            if (e instanceof BaseException)
                throw (BaseException) e;
            else
                throw new ServiceException(BizExceptionCode.RSI_079, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_editCardRequest")
    @BizLoggable(logAction = "UPDATE", logEntityName = "REQUEST")
    public Long updateFromCcos(CardRequestTO newCardRequest)
            throws BaseException {
        // Check validation of new request
        checkCardRequestValid(newCardRequest);
        checkCitizenValid(newCardRequest.getCitizen());
        checkCitizenInBlackList(newCardRequest.getCitizen().getNationalID());
        checkCitizenInfoValid(newCardRequest.getCitizen().getCitizenInfo());
        if (EmsUtil.checkListSize(newCardRequest.getCitizen().getCitizenInfo()
                .getSpouses())) {
            for (SpouseTO spouseTO : newCardRequest.getCitizen()
                    .getCitizenInfo().getSpouses())
                checkSpouseValid(spouseTO);
        }
        if (EmsUtil.checkListSize(newCardRequest.getCitizen().getCitizenInfo()
                .getChildren())) {
            for (ChildTO childTO : newCardRequest.getCitizen().getCitizenInfo()
                    .getChildren())
                checkChildValid(childTO);
        }
        // TODO: This part has been modified in order to solve the problem of
        // issuing card for those requests which
        // TODO: have been production error via CMS and then repealed by CCOS
        // operators.
        // This conditional expression is responsible for switching on/off
        // evaluation of request through CMS.
        if (!Boolean.valueOf(EmsUtil.getProfileValue(
                ProfileKeyName.KEY_SKIP_CMS_CHECK, "true"))) {
            getCardManagementService().checkRequestValidation(
                    newCardRequest.getCitizen().getNationalID(),
                    newCardRequest.getType());
        }
        try {
            RegistrationPaymentTO registrationPaymentTO = getCardRequestDAO().find(CardRequestTO.class, newCardRequest.getId())
                    .getRegistrationPaymentTO();
            newCardRequest.setRegistrationPaymentTO(registrationPaymentTO);
        }catch (Exception e){
            newCardRequest.setRegistrationPaymentTO(null);
        }

        Long requestId = save(newCardRequest);

        getCardRequestHistoryDAO().create(newCardRequest, null, SystemId.CCOS,
                null, CardRequestHistoryAction.COMPLETE_REGISTRATION,
                getUserProfileTO().getUserName());

        return requestId;
    }

    @Override
    @Permissions(value = "ems_viewCardRequest")
    @BizLoggable(logAction = "LOAD", logEntityName = "REQUEST")
    public CardRequestTO load(Long requestId) throws BaseException {
        return getCardRequestDAO().find(CardRequestTO.class, requestId);
    }

    @Override
    @Permissions(value = "ems_findCitizenInfo")
    @BizLoggable(logAction = "LOAD", logEntityName = "CITIZEN")
    public CitizenTO loadCitizen(String firstName, String lastName,
                                 String nationalId) throws BaseException {
//		if (isNullOrEmptyString(firstName)) {
//			throw new ServiceException(BizExceptionCode.RSI_065,
//					BizExceptionCode.RSI_065_MSG);
//		}
//		if (isNullOrEmptyString(lastName)) {
//			throw new ServiceException(BizExceptionCode.RSI_066,
//					BizExceptionCode.RSI_066_MSG);
//		}
        if (isNullOrEmptyString(nationalId)) {
            throw new ServiceException(BizExceptionCode.RSI_067,
                    BizExceptionCode.RSI_067_MSG);
        }
        List<CitizenTO> citizens = getCitizenDAO().findByAttributes(firstName,
                lastName, nationalId);
        if (citizens.size() == 0) {
            return null;
        }


        return getIMSManagementService().fetchCitizenInfo(nationalId);
    }

    @Override
    @Permissions(value = "ems_viewCardRequest")
    @BizLoggable(logAction = "LOAD", logEntityName = "CITIZEN")
    public CitizenTO loadCitizen(long requestId) throws BaseException {
        return getCitizenDAO().findByRequestId(requestId);
    }

    @Override
    @Permissions(value = "ems_removeCardRequest")
    @BizLoggable(logAction = "DELETE", logEntityName = "REQUEST")
    public boolean remove(long requestId) throws BaseException {
        boolean removed = getCardRequestDAO().deleteByID(requestId);
        if (!removed) {
            throw new ServiceException(BizExceptionCode.RSI_081,
                    BizExceptionCode.RSI_081_MSG, new Long[]{requestId});
        }
        return removed;
    }

    private void addBiometricData(long requestId, ArrayList<BiometricTO> biometricDatas)
            throws BaseException {
        CardRequestTO cr = getCardRequestDAO().find(CardRequestTO.class, requestId);
        if (cr == null) {
            throw new ServiceException(BizExceptionCode.RSI_049, BizExceptionCode.RSI_049_MSG,
                    new Long[]{requestId});
        }
        CitizenInfoTO citizenInfoInDb = cr.getCitizen().getCitizenInfo();
        if (citizenInfoInDb == null) {
            throw new ServiceException(BizExceptionCode.RSI_050, BizExceptionCode.RSI_050_MSG,
                    new Long[]{requestId});
        }

        BiometricDAO biometricDAO = getBiometricDAO();

        try {
            for (BiometricTO bio : biometricDatas) {
                if (BiometricType.FING_CANDIDATE.equals(bio.getType())) {

                    Integer fingCandidateSize;

                    try {
                        fingCandidateSize = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_FING_CANDIDATE_SIZE_KB
                                , DEFAULT_KEY_FING_CANDIDATE_SIZE_KB));
                    } catch (NumberFormatException e) {
                        logger.error(e.getMessage(), e);
                        fingCandidateSize = Integer.valueOf(DEFAULT_KEY_FING_CANDIDATE_SIZE_KB);
                    }

                    if (bio.getData().length > (fingCandidateSize * 1024))
                        throw new ServiceException(BizExceptionCode.RSI_094, BizExceptionCode.RSI_094_MSG);
                } else if (BiometricType.FING_NORMAL_1.equals(bio.getType())) {

                    Float fingCandidateSize;

                    try {
                        fingCandidateSize = Float.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_FING_CANDIDATE_NORMAL_1_SIZE_KB
                                , DEFAULT_KEY_FING_NORMAL_1_SIZE_KB));
                    } catch (NumberFormatException e) {
                        logger.error(e.getMessage(), e);
                        fingCandidateSize = Float.valueOf(DEFAULT_KEY_FING_NORMAL_1_SIZE_KB);
                    }

                    if (bio.getData().length > ((int)(fingCandidateSize * 1024)))
                        throw new ServiceException(BizExceptionCode.RSI_165, BizExceptionCode.RSI_165_MSG);
                } else if (BiometricType.FING_NORMAL_2.equals(bio.getType())) {

                    Float fingCandidateSize;

                    try {
                        fingCandidateSize = Float.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_FING_CANDIDATE_NORMAL_2_SIZE_KB
                                , DEFAULT_KEY_FING_NORMAL_2_SIZE_KB));
                    } catch (NumberFormatException e) {
                        logger.error(e.getMessage(), e);
                        fingCandidateSize = Float.valueOf(DEFAULT_KEY_FING_NORMAL_2_SIZE_KB);
                    }

                    if (bio.getData().length > ((int)(fingCandidateSize * 1024)))
                        throw new ServiceException(BizExceptionCode.RSI_166, BizExceptionCode.RSI_166_MSG);
                }
                addBiometric(biometricDAO, citizenInfoInDb, bio);
            }
        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }


    // haghshenas
    private void addBiometricInfoData(long requestId, String featureExtractorIdNormal,String featureExtractorIdCC) throws BaseException {
        BiometricInfoDAO biometricInfoDAO = getBiometricInfoDAO();

        try {
            BiometricInfoTO biometricInfoTO = new BiometricInfoTO();
            CardRequestTO cardRequestTO = getCardRequestDAO().find(
                    CardRequestTO.class, requestId);
            biometricInfoTO.setNationalID(cardRequestTO.getCitizen()
                    .getNationalID());
            biometricInfoTO.setCitizen(cardRequestTO.getCitizen());
            if (featureExtractorIdNormal == null)
                throw new ServiceException(BizExceptionCode.RSI_164,
                        BizExceptionCode.RSI_164_MSG);
            biometricInfoTO.setFeatureExtractorIdNormal(featureExtractorIdNormal);
            if (featureExtractorIdCC == null)
                throw new ServiceException(BizExceptionCode.RSI_167,
                        BizExceptionCode.RSI_167_MSG);
            biometricInfoTO.setFeatureExtractorIdCC(featureExtractorIdCC);
            Long countBios = biometricInfoDAO.checkBiometricInfo(requestId);
            if (countBios == null)
                throw new ServiceException(BizExceptionCode.RSI_160,
                        BizExceptionCode.RSI_160_MSG);
            if (countBios == 2) {
                biometricInfoTO.setType(BiometricInfoType.MOC);
                biometricInfoTO.setMinType(MinutiaType.MIN1);

            } else if (countBios > 2) {
                biometricInfoTO.setType(BiometricInfoType.MOC);
                biometricInfoTO.setMinType(MinutiaType.BOTH);

            } else if (countBios == 1 || countBios == 0) {
                biometricInfoTO.setType(BiometricInfoType.PIN);
                biometricInfoTO.setMinType(MinutiaType.NULL);
            }
            biometricInfoDAO.create(biometricInfoTO);

        } catch (BaseException e) {
            sessionContext.getRollbackOnly();
            throw e;
        } catch (Exception e) {
            sessionContext.getRollbackOnly();
            throw new ServiceException(BizExceptionCode.RSI_161,
                    BizExceptionCode.RSI_160_MSG, e);
        }
    }

    // haghshenas
    private boolean removeBiometricInfoData(long requestId)
            throws BaseException {
        BiometricInfoDAO biometricInfoDAO = getBiometricInfoDAO();
        CardRequestTO cardRequestTO = getCardRequestDAO().find(
                CardRequestTO.class, requestId);
        // String nationalID = cardRequestTO.getCitizen().getNationalID();
        boolean removed = biometricInfoDAO.removeBiometricInfo(cardRequestTO.getCitizen().getId()) > 0;
        if (!removed) {
            throw new ServiceException(BizExceptionCode.RSI_147,
                    BizExceptionCode.RSI_147_MSG, new Long[]{requestId});
        }
        return removed;
    }


    @Override
    @Permissions(value = "ems_addFingerInfo")
    @BizLoggable(logAction = "INSERT", logEntityName = "BIOMETRIC")
    public void addFingerData(long requestId, ArrayList<BiometricTO> biometricDatas, String featureExtractorIdNormal,String featureExtractorIdCC ) throws BaseException {
        addBiometricData(requestId, biometricDatas);

        // addBiometricInfoData(requestId);
        addBiometricInfoData(requestId, featureExtractorIdNormal,featureExtractorIdCC);

        getCardRequestHistoryDAO().create(new CardRequestTO(requestId), null, SystemId.CCOS, null,
                CardRequestHistoryAction.FINGER_SCAN, getUserProfileTO().getUserName());
    }

    @Override
    public void addFingerDataFromMES(long requestId, ArrayList<BiometricTO> biometricDatas, String featureExtractorIdNormal,String featureExtractorIdCC) throws BaseException {
        addBiometricData(requestId, biometricDatas);
//        addBiometricInfoData(requestId);
        addBiometricInfoData(requestId, featureExtractorIdNormal,featureExtractorIdCC);
    }

    private void addFingerDataFromVip(long requestId, ArrayList<BiometricTO> biometricDatas, String featureExtractorIdNormal, String featureExtractorIdCC) throws BaseException {
        addBiometricData(requestId, biometricDatas);
//        addBiometricInfoData(requestId);
        addBiometricInfoData(requestId, featureExtractorIdNormal,featureExtractorIdCC);

        getCardRequestHistoryDAO().create(new CardRequestTO(requestId), VIP_STR, SystemId.CCOS, null,
                CardRequestHistoryAction.FINGER_SCAN, getUserProfileTO().getUserName());
    }

    @Override
    @Permissions(value = "ems_addFaceInfo")
    @BizLoggable(logAction = "INSERT", logEntityName = "BIOMETRIC")
    public void addFaceData(long requestId, ArrayList<BiometricTO> biometricDatas) throws BaseException {
        addBiometricData(requestId, biometricDatas);

        getCardRequestHistoryDAO().create(new CardRequestTO(requestId), null, SystemId.CCOS, null,
                CardRequestHistoryAction.FACE_SCAN, getUserProfileTO().getUserName());
    }

    @Override
    public void addFaceDataFromMES(long requestId, ArrayList<BiometricTO> biometricDatas) throws BaseException {
        addBiometricData(requestId, biometricDatas);
    }

    private void addBiometric(BiometricDAO biometricDAO, CitizenInfoTO czi, BiometricTO bio) throws BaseException {
        checkBiometricValid(bio);
        bio.setCitizenInfo(czi);
        biometricDAO.create(bio);
    }

    @Override
    @Permissions(value = "ems_addDocument")
    @BizLoggable(logAction = "INSERT", logEntityName = "DOCUMENT")
    public void addDocument(long requestId, ArrayList<DocumentTO> documents) throws BaseException {
        this.addDocuments(requestId, documents);
    }

    @Override
    public void addDocumentFromMES(long requestId, ArrayList<DocumentTO> documents) throws BaseException {
        this.addDocuments(requestId, documents);
    }

    private void addDocuments(long requestId, ArrayList<DocumentTO> documents) throws BaseException {
        CardRequestDAO cardRequestDAO = getCardRequestDAO();
        CardRequestTO cr = cardRequestDAO.find(CardRequestTO.class, requestId);
        if (cr == null) {
            throw new ServiceException(BizExceptionCode.RSI_058, BizExceptionCode.RSI_058_MSG,
                    new Long[]{requestId});
        }
        CitizenTO citizenInDb = cr.getCitizen();
        CitizenInfoTO citizenInfoInDb = citizenInDb.getCitizenInfo();

        DocumentDAO documentDAO = getDocumentDAO();

        try {
            Integer maxDocumentSize;
            Integer minDocumentSize;
            try {
                maxDocumentSize = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_SCANNED_DOCUMENT_SIZE_KB
                        , DEFAULT_KEY_SCANNED_DOCUMENT_SIZE_KB));
            } catch (NumberFormatException e) {
                logger.error(e.getMessage(), e);
                maxDocumentSize = Integer.valueOf(DEFAULT_KEY_SCANNED_DOCUMENT_SIZE_KB);
            }

            try {
                minDocumentSize = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_SCANNED_DOCUMENT_MIN_SIZE_KB
                        , DEFAULT_KEY_SCANNED_DOCUMENT_MIN_SIZE_KB));
            } catch (NumberFormatException e) {
                logger.error(e.getMessage(), e);
                minDocumentSize = Integer.valueOf(DEFAULT_KEY_SCANNED_DOCUMENT_MIN_SIZE_KB);
            }

            StringBuilder result = new StringBuilder().append("Document Type ids are: ");
            for (DocumentTO doc : documents) {
                if (doc.getData().length > (maxDocumentSize * 1024))
                    throw new ServiceException(BizExceptionCode.RSI_095, BizExceptionCode.RSI_095_MSG);
                else if (doc.getData().length < (minDocumentSize * 1024))
                    throw new ServiceException(BizExceptionCode.RSI_128, BizExceptionCode.RSI_128_MSG);

                addDoc(documentDAO, citizenInfoInDb, doc);

                result.append(doc.getType().getId()).append(", ");
            }

            if (getUserProfileTO() != null) {
                getCardRequestHistoryDAO().create(new CardRequestTO(requestId), result.substring(0, result.length() - 2),
                        SystemId.CCOS, null, CardRequestHistoryAction.DOCUMENT_SCAN, getUserProfileTO().getUserName());
            } else {
                getCardRequestHistoryDAO().create(new CardRequestTO(requestId), result.substring(0, result.length() - 2),
                        SystemId.CCOS, null, CardRequestHistoryAction.DOCUMENT_SCAN, null);
            }
        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * @deprecated {@link RegistrationServiceImpl#removeFingerAllTypeData(Long)}
     */
    @Override
    @Permissions(value = "ems_removeFingerInfo")
    public boolean removeFingerData(long requestId, BiometricType bioType) throws BaseException {
        boolean result = removeBiometricData(requestId, bioType);
        updateBiometricInfoData(requestId, bioType);
        return result;
    }


    // haghshenas
    private Boolean updateBiometricInfoData(long requestId, BiometricType bioType)
            throws BaseException {
        try {
            if (BiometricType.FING_MIN_1.equals(bioType)
                    || BiometricType.FING_MIN_2.equals(bioType)) {

                BiometricInfoDAO biometricInfoDAO = getBiometricInfoDAO();
                Long countBios = biometricInfoDAO.checkBiometricInfo(requestId);
                if (countBios == null)
                    throw new ServiceException(BizExceptionCode.RSI_160,
                            BizExceptionCode.RSI_160_MSG);

                CardRequestTO cardRequestTO = getCardRequestDAO().find(
                        CardRequestTO.class, requestId);
                String nationalID = cardRequestTO.getCitizen().getNationalID();
                BiometricInfoTO biometricInfoTO = getBiometricInfoDAO()
                        .findByNid(nationalID);

                if (countBios == 1 || countBios == 0) {
                    biometricInfoTO.setType(BiometricInfoType.PIN);
                    biometricInfoTO.setMinType(MinutiaType.NULL);
                } else if (countBios == 2) {

                    biometricInfoTO.setMinType(MinutiaType.MIN1);

                }
                biometricInfoDAO.update(biometricInfoTO);
            }
            return true;
        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (Exception e) {
            sessionContext.setRollbackOnly();
            throw new ServiceException(BizExceptionCode.RSI_159, BizExceptionCode.RSI_159_MSG, e);
        }
    }

    //Adldoost
    @Override
    @Permissions(value = "ems_removeFingerInfo")
    public boolean removeFingerAllTypeData(long requestId) throws BaseException {
        for (BiometricType type : BiometricType.values()) {
            if (type == BiometricType.FING_ALL || type == BiometricType.FING_CANDIDATE || type == BiometricType.FING_MIN_1 ||
                    type == BiometricType.FING_MIN_2 || type == BiometricType.FING_NORMAL_1 ||
                    type == BiometricType.FING_NORMAL_2)
                removeBiometricData(requestId, type);
        }
        removeBiometricInfoData(requestId);

        return true;
    }

    @Override
    @BizLoggable(logAction = "DELETE", logEntityName = "BIOMETRIC")
    public void removeFingerDataBySystem(Long citizenId) throws BaseException {
        getBiometricDAO().removeFingersInfoByCitizenId(citizenId);
        // CitizenTO citizenTO = getCitizenDAO().find(CitizenTO.class,
        // citizenId);
        // String nationalID = citizenTO.getNationalID();
        getBiometricInfoDAO().removeBiometricInfo(citizenId);
    }

    /**
     * @deprecated {@link RegistrationServiceImpl#removeFaceAllTypeData(Long)}
     */
    @Override
    @Permissions(value = "ems_removeFaceInfo")
    public boolean removeFaceData(long requestId, BiometricType bioType) throws BaseException {
        return removeBiometricData(requestId, bioType);
    }

    //Adldoost
    @Override
    @Permissions(value = "ems_removeFaceInfo")
    public boolean removeFaceAllTypeData(long requestId) throws BaseException {
//    	for(BiometricType type : BiometricType.values())
//    	{
//    		if (type == BiometricType.FACE_IMS || type == BiometricType.FACE_CHIP || type == BiometricType.FACE_MLI || type == BiometricType.FACE_LASER) 
//    			removeBiometricData(requestId, type);
//    	}
        boolean removed = getBiometricDAO().removeFaceInfoByRequestId(requestId) > 0;
        if (!removed) {
            throw new ServiceException(BizExceptionCode.RSI_141, BizExceptionCode.RSI_082_MSG, new Long[]{requestId});
        }

        getCardRequestHistoryDAO().create(new CardRequestTO(requestId), null,
                SystemId.CCOS, null,
                CardRequestHistoryAction.REJECT_FACE_INFO_BY_MANAGER,
                getUserProfileTO().getUserName());


        return removed;
    }

    @Override
    @BizLoggable(logAction = "DELETE", logEntityName = "BIOMETRIC")
    public void removeFaceDataBySystem(Long citizenId) throws BaseException {
        getBiometricDAO().removeFaceInfoByCitizenId(citizenId);
    }

    private void addDoc(DocumentDAO documentDAO, CitizenInfoTO czi,
                        DocumentTO doc) throws BaseException {
        checkDocumentValid(doc);
        doc.setCitizenInfo(czi);
        documentDAO.create(doc);
    }

    @BizLoggable(logAction = "DELETE", logEntityName = "BIOMETRIC")
    private boolean removeBiometricData(long requestId, BiometricType bioType)
            throws BaseException {
        BiometricDAO biometricDAO = getBiometricDAO();
        boolean removed = biometricDAO.removeByRequestIdAndType(requestId,
                bioType) > 0;
        if (!removed) {
            throw new ServiceException(BizExceptionCode.RSI_082,
                    BizExceptionCode.RSI_082_MSG, new Long[]{requestId});
        }
        return removed;
    }

    @Override
    @Permissions(value = "ems_removeDocument")
    public boolean removeDocument(Long requestId,
                                  List<DocumentTypeWTO> documentTypeWTOs) throws BaseException {
        List<DocumentTypeTO> documentTypeTOs = new ArrayList<DocumentTypeTO>();

        for (DocumentTypeWTO documentTypeWTO : documentTypeWTOs) {
            documentTypeTOs.add(DocumentTypeMapper.convert(documentTypeWTO));
        }

        boolean doRemoveDocument = doRemoveDocument(requestId, documentTypeTOs);
        getCardRequestHistoryDAO().create(new CardRequestTO(requestId), null,
                SystemId.CCOS, null,
                CardRequestHistoryAction.REJECT_DOC_INFO_BY_MANAGER,
                getUserProfileTO().getUserName());

        return doRemoveDocument;
    }

    @Override
    public boolean removeDocumentBySystem(Long requestId,
                                          List<DocumentTypeTO> documentTypeTOs) throws BaseException {
        return doRemoveDocument(requestId, documentTypeTOs);
    }

    @BizLoggable(logAction = "DELETE", logEntityName = "DOCUMENT")
    private boolean doRemoveDocument(Long requestId,
                                     List<DocumentTypeTO> documentTypeTOs) throws BaseException {
        DocumentDAO documentDAO = getDocumentDAO();
        Boolean removed = documentDAO.removeByRequestIdAndType(requestId,
                documentTypeTOs) > 0;
        if (!removed) {
            throw new ServiceException(BizExceptionCode.RSI_083,
                    BizExceptionCode.RSI_083_MSG, new Long[]{requestId});
        }
        return removed;
    }

    @Override
    @Permissions(value = "ems_viewFingerInfo")
    public List<BiometricTO> getFingerData(long requestId, BiometricType bioType)
            throws BaseException {
        return getBiometricData(requestId, bioType);
    }

    @Override
    @Permissions(value = "ems_viewFaceInfo")
    public List<BiometricTO> getFaceData(long requestId, BiometricType bioType)
            throws BaseException {
        return getBiometricData(requestId, bioType);
    }

    @BizLoggable(logAction = "LOAD", logEntityName = "BIOMETRIC")
    public List<BiometricTO> getBiometricData(long requestId,
                                              BiometricType bioType) throws BaseException {
        if (!getCardRequestDAO().existsCardRequest(requestId)) {
            throw new ServiceException(BizExceptionCode.RSI_063,
                    BizExceptionCode.RSI_063_MSG, new Long[]{requestId});
        }
        BiometricDAO biometricDAO = getBiometricDAO();
        return biometricDAO.findByRequestIdAndType(requestId, bioType);
    }

    @Override
    @Permissions(value = "ems_viewDocument")
    @BizLoggable(logAction = "LOAD", logEntityName = "DOCUMENT")
    public List<DocumentTO> getDocument(long requestId, DocumentTypeTO docType)
            throws BaseException {
        if (!getCardRequestDAO().existsCardRequest(requestId)) {
            throw new ServiceException(BizExceptionCode.RSI_064,
                    BizExceptionCode.RSI_064_MSG, new Long[]{requestId});
        }
        return getDocumentDAO().findByRequestIdAndType(requestId, docType);
    }

    @Override
    @Permissions(value = "ems_addDocument")
    @BizLoggable(logAction = "LOAD", logEntityName = "SERVICE_DOC_TYPE")
    public List<DocumentTypeTO> getServiceDocuments(Integer serviceType,
                                                    Long cardRequestID) throws BaseException {
        List<DocumentTO> documentTOs = getDocumentDAO().findByRequestIdAndType(
                cardRequestID, null);

        List<ServiceDocumentTypeTO> serviceDocTypes = getServiceDocumentTypeDAO()
                .findByServiceId(serviceType);
        List<DocumentTypeTO> docTypes = new ArrayList<DocumentTypeTO>();

        if (EmsUtil.checkListSize(serviceDocTypes)) {
            if (EmsUtil.checkListSize(documentTOs)) {
                for (ServiceDocumentTypeTO sdType : serviceDocTypes) {
                    boolean documentExists = false;
                    for (DocumentTO document : documentTOs) {
                        if (sdType.getDocumentType().getId()
                                .equals(document.getType().getId())) {
                            documentExists = true;
                            break;
                        }
                    }

                    if (!documentExists)
                        docTypes.add(sdType.getDocumentType());
                }
            } else {
                for (ServiceDocumentTypeTO sdType : serviceDocTypes) {
                    docTypes.add(sdType.getDocumentType());
                }
            }
        } else {
            throw new ServiceException(BizExceptionCode.RSI_122,
                    BizExceptionCode.RSI_122_MSG);
        }

        return docTypes;
    }

    @Override
    @Permissions(value = "ems_authenticateDocument")
    @BizLoggable(logAction = "AUTHENTICATE", logEntityName = "REQUEST")
    public void authenticateDocument(long requestId) throws BaseException {
        authenticateDocumentImpl(requestId);

        getCardRequestHistoryDAO().create(new CardRequestTO(requestId), null,
                SystemId.CCOS, null,
                CardRequestHistoryAction.AUTHENTICATE_DOCUMENT,
                getUserProfileTO().getUserName());
    }

    @Override
    public void authenticateDocumentFromMES(long requestId)
            throws BaseException {
        try {
            authenticateDocumentImpl(requestId);
        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    private void authenticateDocumentImpl(long requestId) throws BaseException {
        CardRequestDAO cardRequestDAO = getCardRequestDAO();
        CardRequestTO cardRequest = cardRequestDAO.find(CardRequestTO.class,
                requestId);
        if (cardRequest == null) {
            throw new ServiceException(BizExceptionCode.RSI_059,
                    BizExceptionCode.RSI_059_MSG, new Long[]{requestId});
        }
        cardRequest.setAuthenticity(CardRequestAuthenticity.AUTHENTIC);
        cardRequest.setState(CardRequestState.DOCUMENT_AUTHENTICATED);
        cardRequestDAO.update(cardRequest);
    }

    @Override
    @Permissions(value = "ems_approveCardRequest")
    @BizLoggable(logAction = "APPROVE", logEntityName = "REQUEST")
    public void approveRequest(long requestId, byte[] digest, UserProfileTO userProfileTO)
            throws BaseException {
        approveRequestImpl(requestId, digest, CardRequestState.APPROVED, userProfileTO);

        getCardRequestHistoryDAO().create(new CardRequestTO(requestId),
                CardRequestState.APPROVED.name(), SystemId.CCOS, null,
                CardRequestHistoryAction.MANAGER_APPROVAL,
                getUserProfileTO().getUserName());
    }

    @Override
    public void approveRequestFromMES(long requestId, byte[] digest, UserProfileTO userProfileTO)
            throws BaseException {
        try {
            approveRequestImpl(requestId, digest,
                    CardRequestState.APPROVED_BY_MES, userProfileTO);
        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    private void approveRequestImpl(long requestId, byte[] envelope,
                                    CardRequestState finalState, UserProfileTO userProfileTO) throws BaseException {
        /**
         * Begin
         *
         * @author Sina Golesorkhi
         */
        /**
         * Begin
         *
         * @author Sina Golesorkhi
         */
        ProfileManager profileManager;
        NOCRPKEParamProviderImpl nocrpkeParamProvider;
        try {
            profileManager = ProfileHelper.getProfileManager();
            nocrpkeParamProvider = new NOCRPKEParamProviderImpl(profileManager);
        } catch (ProfileException e) {
            throw new BaseException(BizExceptionCode.RSI_091,
                    BizExceptionCode.RSI_090_MSG, e, new Long[]{requestId});
        }
        try {
            // Verifying the envelope
            // madanipour
            // nocrpkeParamProvider.setValidationMethod("CRL");
            //
            Signature.verifySignature(envelope, nocrpkeParamProvider);
        } catch (Exception e) {
            throw new BaseException(BizExceptionCode.RSI_090,
                    BizExceptionCode.RSI_090_MSG, e, new Long[]{requestId});
        }
        /**
         * End
         *
         * @author Sina Golesorkhi
         */

        CardRequestDAO cardRequestDAO = getCardRequestDAO();
        CardRequestTO cardRequest = cardRequestDAO.find(CardRequestTO.class,
                requestId);
        if (cardRequest == null) {
            throw new BaseException(BizExceptionCode.RSI_060,
                    BizExceptionCode.RSI_060_MSG, new Long[]{requestId});
        }

        /**
         * Compare token owner against enrollment office manager. Token owner
         * has to be the manager of the enrollment office which has sent the
         * manager approval request
         */

        long depID = userProfileTO.getDepID();
        Long officeId = cardRequest.getEnrollmentOffice().getId();
        Long personID = getPersonDAO().findPersonIdByUsername(userProfileTO.getUserName());
        Integer currentUserId = getPersonDAO().findUserIdByPersonId(personID);

        List<TokenState> states = new ArrayList<TokenState>();
        states.add(TokenState.DELIVERED);
        List<PersonTokenTO> personTokenTOs = getPersonTokenDAO().findByPersonIdAndTypeAndState(personID, TokenType.SIGNATURE, states);

        Integer signerUserID = getSingerUserIDFromSignedData(envelope);

        if (officeId.intValue() != depID) {
            throw new BaseException(BizExceptionCode.RSI_116,
                    BizExceptionCode.RSI_116_MSG, new Long[]{
                    depID,
                    officeId.longValue(), requestId});
        }

        if (!currentUserId.equals(signerUserID)) {
            throw new BaseException(BizExceptionCode.RSI_116,
                    BizExceptionCode.RSI_116_MSG, new Long[]{
                    currentUserId.longValue(),
                    signerUserID.longValue(), requestId});
        }

//		if (signerUserID != null) {
//			if (!signerUserID.equals(managerUserID)) {
//				throw new BaseException(BizExceptionCode.RSI_116,
//						BizExceptionCode.RSI_116_MSG, new Long[] {
//								managerUserID.longValue(),
//								signerUserID.longValue(), requestId });
//			}
        if (personTokenTOs.size() != 1) {
            throw new BaseException(BizExceptionCode.RSI_147,
                    BizExceptionCode.RSI_147_MSG, new Long[]{
                    personID,
                    signerUserID.longValue(), requestId});
        }
//			// As username/password control on login process is case
//			// insensitive,
//			// checking manager's username against current username has to be
//			// case insensitive too
//			if (!getUserProfileTO().getUserName().toLowerCase()
//					.equals(managerUserName.toLowerCase())) {
//				throw new BaseException(BizExceptionCode.RSI_120,
//						BizExceptionCode.RSI_120_MSG, new String[] {
//								managerUserName,
//								getUserProfileTO().getUserName(),
//								requestId + "" });
//			}
//		}

        // PersonTokenTO personTokenTO =
        // getPersonTokenDAO().findDeliveredByPersonIdAndType(managerPersonId,
        // TokenType.SIGNATURE);
        //
        // if (personTokenTO == null)
        // throw new BaseException(BizExceptionCode.RSI_127,
        // BizExceptionCode.RSI_127_MSG);

        // ****************** Anbari: Separate insertion of CardRequest Blobs

        //CardRequestBlobsTO findcardRequestBlobsTO = getCardRequestBlobs().findByCardRequestId(requestId);
//		CardRequestBlobsTO findcardRequestBlobsTO = getCardRequestBlobs().find(CardRequestBlobsTO.class, requestId);
//		if (findcardRequestBlobsTO != null) {
//			findcardRequestBlobsTO.setOfficerSign(envelope);
//			getCardRequestBlobs().update(findcardRequestBlobsTO);
//		} else {
//			CardRequestBlobsTO cardRequestBlobsTO = new CardRequestBlobsTO();
//			cardRequestBlobsTO.setCardRequest(cardRequest);
//			cardRequestBlobsTO.setOfficerSign(envelope);
//			getCardRequestBlobs().create(cardRequestBlobsTO);
//
//		}	

        cardRequest.setOfficerSign(envelope);

        // ******************

        cardRequest.setState(finalState);
        cardRequestDAO.update(cardRequest);
    }

    /**
     * Given a signed data (request data signed by enrollment office manager
     * using his/her token) and extracts userID (named SERIALNUMBER in subjectDN
     * of the certificate) from it
     *
     * @param envelope Signed request
     * @return Identifier of enrollment office manager stored in his/her
     * certificate subject
     */
    private Integer getSingerUserIDFromSignedData(byte[] envelope)
            throws BaseException {
        Integer result = -1;

        CMSSignedData cmsSignedData = null;
        try {
            cmsSignedData = new CMSSignedData(envelope);
        } catch (CMSException e) {
            throw new BaseException(BizExceptionCode.RSI_117,
                    BizExceptionCode.RSI_117_MSG, e);
        }
        Store certStore = cmsSignedData.getCertificates();
        SignerInformationStore signers = cmsSignedData.getSignerInfos();
        Collection c = signers.getSigners();
        Iterator it = c.iterator();

        // There is only a single certificate in request approve scenario. So
        // there is no need to iterate over all of them
        if (it.hasNext()) {
            SignerInformation signer = (SignerInformation) it.next();
            Collection certCollection = certStore.getMatches(signer.getSID());
            Iterator certIt = certCollection.iterator();
            X509CertificateHolder cert = (X509CertificateHolder) certIt.next();
            String certificateSubjectDN = cert.getSubject().toString();
            String[] tokenizedSubjectDN = certificateSubjectDN.split(",");
            for (String subjectToken : tokenizedSubjectDN) {

                // User ID of enrollment office manager has been stored in
                // certificate subject as SERIALNUMBER attribute
                if (subjectToken.trim().startsWith("SERIALNUMBER=")) {
                    result = Integer.valueOf(subjectToken.trim().split("=")[1]);
                    break;
                }
            }
        } else {
            throw new BaseException(BizExceptionCode.RSI_118,
                    BizExceptionCode.RSI_118_MSG);
        }

        if (result == -1) {
            throw new BaseException(BizExceptionCode.RSI_119,
                    BizExceptionCode.RSI_119_MSG);
        }

        return result;
    }

    /**
     * The method fetchPortalCardRequestsToSave is used to fetch the requests
     * from the sub system 'Portal' and save them
     *
     * @throws com.gam.commons.core.BaseException
     */
    @Override
//    @BizLoggable(logAction = "LOAD_FROM_PORTAL", logEntityName = "PORTAL", logActor = "System")
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public Boolean fetchPortalCardRequestsToSave(List<Long> portalCardRequestIds) throws BaseException {
        Boolean loopFlag = true;

        List<Long> successfulSavedCardRequestIds = new ArrayList<Long>();
        List<Long> successfulSavedPortalCardRequestIds = new ArrayList<Long>();
        List<Long> unsuccessfulSavedPortalCardRequestIds = new ArrayList<Long>();

        List<CardRequestTO> cardRequestTOList = getPortalManagementService().transferCardRequests(portalCardRequestIds);

        if (cardRequestTOList != null && !cardRequestTOList.isEmpty()) {
            jobLOGGER.info("Starting to iterated over portal card requests {} to insert them in EMS", portalCardRequestIds);
            for (CardRequestTO cardRequestTO : cardRequestTOList) {
                try {
                    successfulSavedCardRequestIds.add(savePortalRequest(cardRequestTO));
                    successfulSavedPortalCardRequestIds.add(cardRequestTO.getPortalRequestId());

                    SyncCardRequestWTO syncCardRequestWTO = new SyncCardRequestWTO();
                    syncCardRequestWTO.setId(cardRequestTO.getPortalRequestId());
                    syncCardRequestWTO.setCardRequestState(CardRequestState.RECEIVED_BY_EMS.name());

                    List<SyncCardRequestWTO> syncCardRequestWTOList = new ArrayList<SyncCardRequestWTO>();
                    syncCardRequestWTOList.add(syncCardRequestWTO);

                    jobLOGGER.info("Updating status of portal card request {} in portal to RECEIVED_BY_EMS", syncCardRequestWTO.getId());
                    List<ItemWTO> itemWTOList = getPortalRegistrationService().updateCardRequestsState(syncCardRequestWTOList);
                    List<Long> updatedRequests = new ArrayList<Long>();

                    if (EmsUtil.checkListSize(itemWTOList)) {
                        for (ItemWTO itemWTO : itemWTOList) {
                            if ("updated".equals(itemWTO.getValue())) {
                                updatedRequests.add(Long.valueOf(itemWTO.getKey()));
                            }
                        }
                    }
                    if (!updatedRequests.isEmpty()) {
                        jobLOGGER.info("Updating sync date of portal card request {} in EMS", syncCardRequestWTO.getId());
                        getCardRequestDAO().updateSyncDatesByCurrentDate(updatedRequests);
                    }
                } catch (BaseException e) {
                    logger.error(e.getExceptionCode() + " : " + e.getMessage(), e);
                    unsuccessfulSavedPortalCardRequestIds.add(cardRequestTO.getPortalRequestId());
                    jobLOGGER.error("An error happened while trying to fetch portal card request " + cardRequestTO.getPortalRequestId() + ", persisting it in EMS and updating its status in portal", e);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    unsuccessfulSavedPortalCardRequestIds.add(cardRequestTO.getPortalRequestId());
                    jobLOGGER.error("An error happened while trying to fetch portal card request " + cardRequestTO.getPortalRequestId() + ", persisting it in EMS and updating its status in portal", e);
                }
            }

            if (EmsUtil.checkListSize(successfulSavedCardRequestIds)) {
                jobLOGGER.info("List of portal card requests identifiers successfully inserted in EMS database is {} and their corresponding identifier in EMS is {}", successfulSavedPortalCardRequestIds, successfulSavedCardRequestIds);
                createBusinessLog(BusinessLogAction.LOAD_FROM_PORTAL, BusinessLogEntity.PORTAL, "System",
                        EmsUtil.toJSON("cardRequestIds:" + successfulSavedCardRequestIds + ", portalCardRequestIds:" +
                                successfulSavedPortalCardRequestIds), BusinessLogActionAttitude.T
                );
            }

            if (EmsUtil.checkListSize(unsuccessfulSavedPortalCardRequestIds))
                jobLOGGER.info("List of portal card requests identifiers NOT inserted in EMS database is {}", unsuccessfulSavedPortalCardRequestIds);
            createBusinessLog(BusinessLogAction.LOAD_FROM_PORTAL, BusinessLogEntity.PORTAL, "System",
                    EmsUtil.toJSON("portalCardRequestIds:" + unsuccessfulSavedPortalCardRequestIds), BusinessLogActionAttitude.F);

        } else {
            loopFlag = false;
        }

        return loopFlag;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private Long savePortalRequest(CardRequestTO cardRequestTO) throws BaseException {
        if (!CardRequestState.PENDING_FOR_EMS.equals(cardRequestTO.getState())) {
            throw new ServiceException(BizExceptionCode.RSI_076,
                    BizExceptionCode.RSI_076_MSG, new Object[]{
                    cardRequestTO.getPortalRequestId(),
                    CardRequestState.PENDING_FOR_EMS, cardRequestTO.getState()}
            );
        }

        try {
            CardRequestTO portalRequest = getCardRequestDAO().findByPortalRequestId(cardRequestTO.getPortalRequestId());

            /**
             * If the request was registered on EMS in advance, then because of difference between portal and ems fields,
             * the fields bellow should be set
             */
            if (portalRequest != null) {
                jobLOGGER.info("There is already a card request in EMS having portal request id {}. So trying to update it instead of inserting a new record", cardRequestTO.getPortalRequestId());
                cardRequestTO.setId(portalRequest.getId());
                cardRequestTO.setType(portalRequest.getType());
                cardRequestTO.setMetadata(null);
                CitizenTO citizenTO = getCitizenDAO().findAllAttributesByRequestId(portalRequest.getId());
                cardRequestTO.getCitizen().getCitizenInfo().setLiving(citizenTO.getCitizenInfo().getLiving());
                cardRequestTO.getCitizen().getCitizenInfo().setLivingCity(citizenTO.getCitizenInfo().getLivingCity());
            } else {
                cardRequestTO.setType(CardRequestType.FIRST_CARD);
            }

            cardRequestTO.setState(CardRequestState.RECEIVED_BY_EMS);

            return saveFromJob(cardRequestTO);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            jobLOGGER.error("Unable to save portal card request identifier " + cardRequestTO.getPortalRequestId(), e);
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            jobLOGGER.error("Unable to save portal card request identifier " + cardRequestTO.getPortalRequestId(), e);
            throw new ServiceException(BizExceptionCode.RSI_089, BizExceptionCode.RSI_089_MSG, new Object[]{cardRequestTO.getId()});
        }
    }

    @Override
//    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public Long saveReservationCardRequest(
            CardRequestTO portalCardRequest) throws BaseException {
        try {
            CardRequestTO emsRequest = getCardRequestDAO().findByPortalRequestId(portalCardRequest.getPortalRequestId());

            /**
             * If the request was registered on EMS in advance, then because of difference between portal and ems fields,
             * the fields bellow should be set
             */
            if (emsRequest != null) {
                jobLOGGER.info("There is already a card request in EMS having portal request id {}. So trying to update it instead of inserting a new record", portalCardRequest.getPortalRequestId());
                portalCardRequest.setId(emsRequest.getId());
                portalCardRequest.setType(emsRequest.getType());
                portalCardRequest.setMetadata(null);
                CitizenTO citizenTO = getCitizenDAO().findAllAttributesByRequestId(emsRequest.getId());
                portalCardRequest.getCitizen().getCitizenInfo().setLiving(citizenTO.getCitizenInfo().getLiving());
                portalCardRequest.getCitizen().getCitizenInfo().setLivingCity(citizenTO.getCitizenInfo().getLivingCity());
                //Milad & Alireza for fix null enrollmet office id in saving reservation when you have original eof
                portalCardRequest.setOriginalCardRequestOfficeId(emsRequest.getOriginalCardRequestOfficeId());
                portalCardRequest.setOrigin(emsRequest.getOrigin());
                portalCardRequest.setEnrollmentOffice(emsRequest.getEnrollmentOffice());
            } else {
                portalCardRequest.setType(CardRequestType.FIRST_CARD);
                portalCardRequest.setOrigin(CardRequestOrigin.P);
                portalCardRequest.setOriginalCardRequestOfficeId(null);
            }

//            portalCardRequest.setState(CardRequestState.RECEIVED_BY_EMS);


            return newSaveFromJob(portalCardRequest);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            jobLOGGER.error("Unable to save portal card request identifier " + portalCardRequest.getPortalRequestId(), e);
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            jobLOGGER.error("Unable to save portal card request identifier " + portalCardRequest.getPortalRequestId(), e);
            throw new ServiceException(BizExceptionCode.RSI_089, BizExceptionCode.RSI_089_MSG, new Object[]{portalCardRequest.getId()});
        }
    }

    @Override
    public List<Long> fetchPortalCardRequestIdsForTransfer() throws BaseException {
        return getPortalManagementService().fetchPortalCardRequestIdsForTransfer();
    }

    /**
     * @throws BaseException
     * @throws ServiceException
     * @author Sina Golesorkhi
     */
    @Override
    public CertificateTO getCertificateByUsage(CertificateUsage certificateUsage)
            throws BaseException {
        return findCertificateByUsage(certificateUsage);
    }

    // Methods for EncryptedCardRequest
    @Override
    @BizLoggable(logAction = "INSERT", logEntityName = "ENCRYPTED_CARD_REQUEST")
    public Long saveEncryptedCardRequest(EncryptedCardRequestTO to)
            throws BaseException {
        checkEncryptedCardRequest(to);
        getEncryptedCardRequestDAO().create(to);
        return to.getId();
    }

    private CertificateTO findCertificateByUsage(
            CertificateUsage certificateUsage) throws BaseException {
        return getCertificateDAO().findCertificateByUsage(certificateUsage);
    }

    private CertificateDAO getCertificateDAO() throws ServiceException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        CertificateDAO certificateDAO = null;
        try {
            certificateDAO = factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_CERTIFICATE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_080,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_CERTIFICATE.split(","));
        }

        return certificateDAO;
    }

    /**
     * @throws BaseException
     * @author Sina Golesorkhi
     */
    @Override
    @BizLoggable(logAction = "LOAD", logEntityName = "ENCRYPTED_CARD_REQUEST")
    public EncryptedCardRequestTO getEncryptedCardRequestTO(String id)
            throws BaseException {
        return getEncryptedCardRequestDAO().find(EncryptedCardRequestTO.class,
                id);

    }

    @Override
    public HashMap<Enum<CardRequestState>, List<CardRequestState>> registrationStatePolicyForUpdate(
            CardRequestState cardRequestState) throws BaseException {
        HashMap<Enum<CardRequestState>, List<CardRequestState>> map = new HashMap<Enum<CardRequestState>, List<CardRequestState>>();

        if (CardRequestState.REGISTERED.equals(cardRequestState)) {
            List<CardRequestState> validCardRequestStateList = new ArrayList<CardRequestState>();
            validCardRequestStateList.add(CardRequestState.PENDING_FOR_EMS);

            map.put(CardRequestState.REGISTERED, validCardRequestStateList);
        } else if (CardRequestState.PENDING_FOR_EMS.equals(cardRequestState)) {
            List<CardRequestState> validCardRequestStateList = new ArrayList<CardRequestState>();
            validCardRequestStateList.add(CardRequestState.RECEIVED_BY_EMS);

            map.put(CardRequestState.PENDING_FOR_EMS, validCardRequestStateList);
        }

        return map;
    }

    @Override
    public void idleR2DCardRequest() throws BaseException {
        CardRequestDAO cardRequestDAO = getCardRequestDAO();

        String idlePeriod = EmsUtil.getProfileValue(
                ProfileKeyName.KEY_CARD_REQUEST_IDLE_PERIOD,
                DEFAULT_CARD_REQUEST_IDLE_PERIOD);
        cardRequestDAO.idleR2DCardRequest(idlePeriod);
    }

    @Override
    @Permissions(value = "ems_saveVipRequest")
    @BizLoggable(logAction = "INSERT_VIP", logEntityName = "REQUEST")
    public Boolean saveFromVip(CardRequestTO requestTO,
                               ArrayList<BiometricTO> fingers, ArrayList<BiometricTO> faces,
                               ArrayList<DocumentTO> documents, String featureExtractorIdNormal,String featureExtractorIdCC) throws BaseException {
        try {

            // do validation
            checkCardRequestValid(requestTO);
            checkCitizenValid(requestTO.getCitizen());
            checkCitizenInfoValid(requestTO.getCitizen().getCitizenInfo());
            vipLogger.info("===================================================================================================================");
            vipLogger.info("========================== vip citizen nid is:" + requestTO.getCitizen().getNationalID() + " ==========================");
            vipLogger.info("===================================================================================================================");
            logger.info("======================================================================================================================");
            logger.info("========================== vip citizen nid is:" + requestTO.getCitizen().getNationalID() + " ==========================");
            logger.info("===================================================================================================================");
            if (EmsUtil.checkListSize(requestTO.getCitizen().getCitizenInfo().getSpouses())) {
                for (SpouseTO spouseTO : requestTO.getCitizen().getCitizenInfo().getSpouses())
                    checkSpouseValid(spouseTO);
            }
            if (EmsUtil.checkListSize(requestTO.getCitizen().getCitizenInfo().getChildren())) {
                for (ChildTO childTO : requestTO.getCitizen().getCitizenInfo().getChildren())
                    checkChildValid(childTO);
            }
            // do estelam2
//		IMSManagementService imsManagementService = getIMSManagementService();
//		
//		PersonEnquiryVTO personalInfo = imsManagementService.getPersonalInfoByGetEstelam2(requestTO.getCitizen().getNationalID());
//			if(personalInfo ==null ||  EmsUtil.checkString(personalInfo.getMetadata()))	
//				throw new ServiceException(BizExceptionCode.RSI_132, BizExceptionCode.RSI_132_MSG);


            //TODO:change person info
//			requestTO.getCitizen().setFirstNamePersian(personalInfo.getFirstName());
//			requestTO.getCitizen().setSurnamePersian(personalInfo.getLastName());
//			requestTO.getCitizen().getCitizenInfo().setBirthCertificateId(personalInfo
//					.getBirthCertificateId());
//			requestTO.getCitizen().getCitizenInfo().setBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
//			requestTO.getCitizen().getCitizenInfo().setFatherFirstNamePersian(personalInfo
//					.getFatherName());
//			requestTO.getCitizen().getCitizenInfo().setGender(personalInfo.getGender());		


//		requestTO.getCitizen().setFirstNamePersian("حمید");
//		requestTO.getCitizen().setSurnamePersian("رضایی");
//		requestTO.getCitizen().getCitizenInfo().setBirthCertificateId("35388");
//		requestTO.getCitizen().getCitizenInfo().setBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
//		requestTO.getCitizen().getCitizenInfo().setFatherFirstNamePersian("پدر حمید");
//		requestTO.getCitizen().getCitizenInfo().setGender(Gender.M);	


            //
            // find this request exists on db
            List<CardRequestTO> cardRequestTOs = getCardRequestDAO().findByNationalId(requestTO.getCitizen().getNationalID());
            if (EmsUtil.checkListSize(cardRequestTOs))
                throw new ServiceException(BizExceptionCode.RSI_133, BizExceptionCode.RSI_133_MSG);
            // check to be in correct vip workstation
            long depID = userProfileTO.getDepID();
            EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class, depID);
            String enrollmentOfficeCode = enrollmentOfficeTO.getCode();
            ProfileManager pm = ProfileHelper.getProfileManager();
            String codeVip = (String) pm.getProfile(ProfileKeyName.KEY_VIP_ENROLLMENT_OFFICE, true, null, null);

            if (enrollmentOfficeCode.equals(codeVip))
                requestTO.setEnrollmentOffice(enrollmentOfficeTO);
            else
                throw new ServiceException(BizExceptionCode.RSI_134, BizExceptionCode.RSI_134_MSG);
            requestTO.setOrigin(CardRequestOrigin.V); // Origin is VIP
            requestTO.setState(CardRequestState.DOCUMENT_AUTHENTICATED);
            requestTO.setEstelam2Flag(Estelam2FlagType.N);
            requestTO.setReservationDate(new Date());
            requestTO.setRequestedSmsStatus(1);

            long requestId = save(requestTO);
            CardRequestTO cardRequestAfterInsert = getCardRequestDAO().find(CardRequestTO.class, requestId);
            // set priority to 99
            CardRequestTO cardRequestdb = getCardRequestDAO().find(CardRequestTO.class, requestId);
            cardRequestdb.setPriority(99);
            //
            cardRequestAfterInsert.setPriority(99);
//            addFingerDataFromVip(requestId, fingers);
            addFingerDataFromVip(requestId, fingers, featureExtractorIdNormal,featureExtractorIdCC);
            if (EmsUtil.checkListSize(faces)) {
                ArrayList<BiometricTO> faceList = new ArrayList<BiometricTO>();
                for (BiometricTO faceBiometric : faces) {

                    if (faceBiometric.getType() == BiometricType.VIP_IMAGE)

                    {

                        PhotoVipTO photoVipTO = new PhotoVipTO();
                        photoVipTO.setData(faceBiometric.getData());
                        photoVipTO.setMetaData(faceBiometric.getMetaData());
                        addFaceDataFromVip(requestId, photoVipTO);

                    } else
                        faceList.add(faceBiometric);
                }

                addBiometricData(requestId, faceList);
                getCardRequestHistoryDAO().create(new CardRequestTO(requestId), SystemId.VIP.toString(), SystemId.CCOS, null,
                        CardRequestHistoryAction.FACE_SCAN, getUserProfileTO().getUserName());

            }
            if (EmsUtil.checkListSize(documents))
                addDocumentsVip(requestId, documents);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RSI_138, BizExceptionCode.RSI_138_MSG, e);
        }

        return true;
    }


    @Override
    public TrackingNumberWTO retrieveTrackingNumber(String nationalId,
                                                    Date birthDate) throws BaseException {
        TrackingNumberWTO trackingNumberWTO = null;

        if (!EmsUtil.checkString(nationalId))
            throw new ServiceException(BizExceptionCode.RSI_109,
                    BizExceptionCode.RSI_109_MSG);
        if (birthDate == null)
            throw new ServiceException(BizExceptionCode.RSI_110,
                    BizExceptionCode.RSI_110_MSG);

        String birthDateSolar = DateUtil.convert(birthDate, DateUtil.JALALI);

        CardRequestTO cardRequestTO = getCardRequestDAO()
                .findByNIdBirthDateSolar(nationalId, birthDateSolar);

        if (cardRequestTO != null) {
            if (cardRequestTO.getTrackingID() != null)
                trackingNumberWTO = new TrackingNumberWTO(
                        cardRequestTO.getTrackingID(),
                        CcosRequestStateBundle.getMessage(cardRequestTO
                                .getState().toString()));
            else

                throw new ServiceException(BizExceptionCode.RSI_103,
                        BizExceptionCode.RSI_103_MSG);
        } else
            throw new ServiceException(BizExceptionCode.RSI_102,
                    BizExceptionCode.RSI_102_MSG);

        return trackingNumberWTO;

    }

    /**
     * The method fetchBirthCertIssPlace is used to fetch the birth city that
     * associated to a specified nationalId
     *
     * @param nationalId is an instance of type {@link String}, which represents the
     *                   nationalId
     * @return
     * @throws BaseException
     */
    @Override
    public List<BirthCertIssPlaceVTO> fetchBirthCertIssPlace(String nationalId)
            throws BaseException {
        //before change
//		HashMap param = new HashMap();
//		param.put("nationalID", nationalId.substring(0, 3));		
//		try {
//			ValueListHandler vlh = EMSValueListProvider.getProvider().loadList(
//					"birthCertIssPlaceList", ("main").split(","),
//					("count").split(","), param, null, null);
//			return vlh.getList(true);
//		} catch (ListException e) {
//			throw new ServiceException(BizExceptionCode.RSI_104,
//					BizExceptionCode.RSI_104_MSG, e);
//		} catch (ListHandlerException e) {
//			throw new ServiceException(BizExceptionCode.RSI_105,
//					BizExceptionCode.RSI_105_MSG, e);
//		} catch (Exception e) {
//			throw new ServiceException(BizExceptionCode.RSI_106,
//					BizExceptionCode.RSI_106_MSG, e);
//		}

        BirthCertIssPlaceVTO birthCertIssPlaceVTO = new BirthCertIssPlaceVTO();
        List<BirthCertIssPlaceVTO> birthCertIssPlaceVTOs = new ArrayList<BirthCertIssPlaceVTO>();

        try {
            String birthCertIssuePlaceName = getCitizenInfoDAO().getBirthCertIssuePlaceByNid(nationalId);


            birthCertIssPlaceVTO.setDepName(birthCertIssuePlaceName);
            birthCertIssPlaceVTOs.add(birthCertIssPlaceVTO);
            return birthCertIssPlaceVTOs;
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.RSI_104, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RSI_106, BizExceptionCode.RSI_106_MSG, e);
        }
    }

    private PortalRegistrationService getPortalRegistrationService()
            throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        PortalRegistrationService portalRegistrationService;
        try {
            portalRegistrationService = serviceFactory
                    .getService(EMSLogicalNames
                            .getExternalServiceJNDIName(EMSLogicalNames.SRV_PORTAL_REGISTRATION), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_093,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
        }
        portalRegistrationService.setUserProfileTO(getUserProfileTO());
        return portalRegistrationService;
    }

    /**
     * @param logAction
     * @param logEntityName
     * @param logActor
     * @param additionalData
     * @throws BaseException
     */
    private void createBusinessLog(BusinessLogAction logAction,
                                   BusinessLogEntity logEntityName, String logActor,
                                   String additionalData, BusinessLogActionAttitude actionAttitude)
            throws BaseException {
        BusinessLogTO businessLogTO = new BusinessLogTO();
        businessLogTO.setEntityID(" ");
        businessLogTO.setAction(logAction);
        businessLogTO.setEntityName(logEntityName);
        businessLogTO.setActor(logActor);
        businessLogTO.setAdditionalData(additionalData);
        businessLogTO.setDate(new Timestamp(new Date().getTime()));
        businessLogTO.setActionAttitude(actionAttitude);
        getBusinessLogService().insertLog(businessLogTO);
    }

    /**
     * getBusinessLogService
     *
     * @return an instance of type {@link BusinessLogService}
     * @throws BaseException
     */
    private BusinessLogService getBusinessLogService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        BusinessLogService businessLogService;
        try {
            businessLogService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_BUSINESS_LOG), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_108,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_BUSINESS_LOG.split(","));
        }
        return businessLogService;
    }

    @Override
    @Permissions(value = "ems_addDocument")
    @BizLoggable(logAction = "LOAD", logEntityName = "SERVICE_DOC_TYPE")
    public List<DocumentTypeTO> getServiceVipDocuments(Integer serviceType)
            throws BaseException {
        List<ServiceDocumentTypeTO> serviceDocTypes = getServiceDocumentTypeDAO().findByServiceId(serviceType);
        List<DocumentTypeTO> docTypes = new ArrayList<DocumentTypeTO>();
        if (EmsUtil.checkListSize(serviceDocTypes)) {
            for (ServiceDocumentTypeTO sdType : serviceDocTypes) {
                docTypes.add(sdType.getDocumentType());
            }

        } else

            throw new ServiceException(BizExceptionCode.RSI_137, BizExceptionCode.RSI_137_MSG);

        return docTypes;
    }

    private void addFaceDataFromVip(long requestId, PhotoVipTO photoVipTO)
            throws BaseException {
        if (photoVipTO != null) {
            PhotoVipDAO photoVipDAO = getPhotoVipDAO();
            CardRequestTO cardRequestTO = new CardRequestTO(requestId);
            try {

                if (photoVipTO.getData() == null) {
                    throw new ServiceException(BizExceptionCode.RSI_136,
                            BizExceptionCode.RSI_136_MSG);
                }
                photoVipTO.setCardRequest(cardRequestTO);
                photoVipDAO.create(photoVipTO);
            } catch (BaseException e) {
                throw e;
            } catch (Exception e) {
                throw new ServiceException(BizExceptionCode.RSI_145, BizExceptionCode.RSI_145_MSG, e);
            }
        }
    }

    @Override
    public PhotoVipWTO getPhotoVip(Long cardRquestId) throws BaseException {
        try {

            PhotoVipTO photoVipTO = getPhotoVipDAO().getPhotoVip(cardRquestId);
            if (photoVipTO == null)
                return null;
            PhotoVipWTO photoVipWTO = new PhotoVipWTO();
            photoVipWTO.setData(photoVipTO.getData());
            photoVipWTO.setMetaData(photoVipTO.getMetaData());
            photoVipWTO.setId(photoVipTO.getId());
            return photoVipWTO;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.RSI_140, BizExceptionCode.RSI_140_MSG, e);
        }


    }

    private void addDocumentsVip(long requestId, ArrayList<DocumentTO> documents) throws BaseException {
        CardRequestDAO cardRequestDAO = getCardRequestDAO();
        CardRequestTO cr = cardRequestDAO.find(CardRequestTO.class, requestId);
        if (cr == null) {
            throw new ServiceException(BizExceptionCode.RSI_142, BizExceptionCode.RSI_058_MSG,
                    new Long[]{requestId});
        }
        CitizenTO citizenInDb = cr.getCitizen();
        CitizenInfoTO citizenInfoInDb = citizenInDb.getCitizenInfo();

        DocumentDAO documentDAO = getDocumentDAO();

        try {
            Integer maxDocumentSize;
            Integer minDocumentSize;
            try {
                maxDocumentSize = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_SCANNED_DOCUMENT_SIZE_KB
                        , DEFAULT_KEY_SCANNED_DOCUMENT_SIZE_KB));
            } catch (NumberFormatException e) {
                logger.error(e.getMessage(), e);
                maxDocumentSize = Integer.valueOf(DEFAULT_KEY_SCANNED_DOCUMENT_SIZE_KB);
            }

            try {
                minDocumentSize = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_SCANNED_DOCUMENT_MIN_SIZE_KB
                        , DEFAULT_KEY_SCANNED_DOCUMENT_MIN_SIZE_KB));
            } catch (NumberFormatException e) {
                logger.error(e.getMessage(), e);
                minDocumentSize = Integer.valueOf(DEFAULT_KEY_SCANNED_DOCUMENT_MIN_SIZE_KB);
            }

            StringBuilder result = new StringBuilder().append(SystemId.VIP + " " + "Document Type ids are: ");
            for (DocumentTO doc : documents) {
                if (doc.getData().length > (maxDocumentSize * 1024))
                    throw new ServiceException(BizExceptionCode.RSI_143, BizExceptionCode.RSI_095_MSG);
                else if (doc.getData().length < (minDocumentSize * 1024))
                    throw new ServiceException(BizExceptionCode.RSI_144, BizExceptionCode.RSI_128_MSG);

                addDoc(documentDAO, citizenInfoInDb, doc);

                result.append(doc.getType().getId()).append(", ");
            }

            if (getUserProfileTO() != null) {
                getCardRequestHistoryDAO().create(new CardRequestTO(requestId), result.substring(0, result.length() - 2),
                        SystemId.CCOS, null, CardRequestHistoryAction.DOCUMENT_SCAN, getUserProfileTO().getUserName());
            } else {
                getCardRequestHistoryDAO().create(new CardRequestTO(requestId), result.substring(0, result.length() - 2),
                        SystemId.CCOS, null, CardRequestHistoryAction.DOCUMENT_SCAN, null);
            }
        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    @Override
    @Permissions(value = "ems_editCardRequest")
    @BizLoggable(logAction = "UPDATE", logEntityName = "REQUEST")
    public Long updateVipRegistrationRequestFromCccos(
            CardRequestTO cardRequestTO) throws BaseException {
        try {
            long depID = userProfileTO.getDepID();
            EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeDAO()
                    .find(EnrollmentOfficeTO.class, depID);
            String enrollmentOfficeCode = enrollmentOfficeTO.getCode();
            ProfileManager pm = ProfileHelper.getProfileManager();
            String codeVip = (String) pm.getProfile(
                    ProfileKeyName.KEY_VIP_ENROLLMENT_OFFICE, true, null, null);
            if (!enrollmentOfficeCode.equals(codeVip))

                throw new ServiceException(BizExceptionCode.RSI_146,
                        BizExceptionCode.RSI_146_MSG);
            CitizenTO citizenTO = getCitizenDAO().findByRequestId(
                    cardRequestTO.getId());
            CitizenInfoTO citizenInfo = citizenTO.getCitizenInfo();
            CitizenInfoTO citizenVipInfo = cardRequestTO.getCitizen()
                    .getCitizenInfo();

            citizenInfo.setPostcode(citizenVipInfo.getPostcode());
            citizenInfo.setAddress(citizenVipInfo.getAddress());
            citizenInfo.setPhone(citizenVipInfo.getPhone());
            if (citizenVipInfo.getLivingCity() != null && !"0".equals(citizenVipInfo.getLivingCity()))
                citizenInfo.setLivingCity(citizenVipInfo.getLivingCity());
            else
                throw new ServiceException(BizExceptionCode.RSI_162,
                        BizExceptionCode.RSI_162_MSG);
            if (citizenVipInfo.getLiving() != null && !"0".equals(citizenVipInfo.getLiving()))
                citizenInfo.setLiving(citizenVipInfo.getLiving());
            else
                throw new ServiceException(BizExceptionCode.RSI_163,
                        BizExceptionCode.RSI_163_MSG);
            citizenInfo.setMobile(citizenVipInfo.getMobile());
            citizenInfo.setMotherFirstNamePersian(citizenVipInfo.getMotherFirstNamePersian());

            CitizenInfoTO update = getCitizenInfoDAO().update(citizenInfo);

            getCardRequestHistoryDAO().create(cardRequestTO,
                    SystemId.VIP.toString(), SystemId.CCOS, null,
                    CardRequestHistoryAction.COMPLETE_REGISTRATION,
                    getUserProfileTO().getUserName());

            return update.getId();

        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (Exception e) {
            sessionContext.setRollbackOnly();
            throw new ServiceException(BizExceptionCode.RSI_145,
                    BizExceptionCode.RSI_145_MSG, e);
        }
    }


    //Anbari
    @Override
    @Permissions(value = "ems_viewPreRegistrationVIP")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String saveVIPpreRegistrationAndDoEstelam(CardRequestTO cardRequestTO) throws BaseException {

        RegistrationVIPVTO savePreRegistrationVIP = sessionContext.getBusinessObject(RegistrationServiceLocal.class).savePreRegistrationVIP(cardRequestTO, getUserProfileTO());
        if (savePreRegistrationVIP == null)
            throw new ServiceException(BizExceptionCode.RSI_148, BizExceptionCode.RSI_148_MSG);

        Boolean doEstelamForVIP = Boolean.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_SKIP_ESTELAM_CHECK, DEFAULT_SKIP_ESTELAM_CHECK));
        if (savePreRegistrationVIP.getCardRequestId() != null && !doEstelamForVIP) {
            getIMSManagementService().doEstelam3(
                    savePreRegistrationVIP.getCardRequestId(), true);
        }

        return savePreRegistrationVIP.getTrackingId();
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public RegistrationVIPVTO savePreRegistrationVIP(CardRequestTO cardRequestTO, UserProfileTO userProfile) throws BaseException {
        try {
            // Check validation of new request
            checkCitizenInfoValidForOfficeVIPRequest(cardRequestTO.getCitizen().getCitizenInfo());
            if (EmsUtil.checkListSize(cardRequestTO.getCitizen().getCitizenInfo().getSpouses())) {
                for (SpouseTO spouseTO : cardRequestTO.getCitizen().getCitizenInfo().getSpouses())
                    checkSpouseValid(spouseTO);
            }
            if (EmsUtil.checkListSize(cardRequestTO.getCitizen().getCitizenInfo().getChildren())) {
                for (ChildTO childTO : cardRequestTO.getCitizen().getCitizenInfo().getChildren())
                    checkChildValid(childTO);
            }
            secureString(cardRequestTO);

            //Get Birth place For citizen
            List<BirthCertIssPlaceVTO> birthCertIssPlaceVTOList = fetchBirthCertIssPlace(cardRequestTO.getCitizen().getNationalID());
            if (EmsUtil.checkListSize(birthCertIssPlaceVTOList))
                cardRequestTO.getCitizen().getCitizenInfo().setBirthCertificateIssuancePlace(birthCertIssPlaceVTOList.get(0).getDepName());
            else
                throw new ServiceException(BizExceptionCode.RSI_150, BizExceptionCode.RSI_107_MSG + cardRequestTO.getCitizen().getNationalID());
            cardRequestTO.setType(CardRequestType.FIRST_CARD);
            String generateTrackingId = EmsUtil.generateTrackingId(cardRequestTO.getCitizen().getNationalID() + new Date());
            cardRequestTO.setTrackingID(generateTrackingId);
            cardRequestTO.setOrigin(CardRequestOrigin.C);
            cardRequestTO.setReservationDate(new Date());
            cardRequestTO.setState(CardRequestState.REFERRED_TO_CCOS);
            cardRequestTO.setEnrollmentOffice(new EnrollmentOfficeTO(userProfile.getDepID()));
            cardRequestTO.setEnrolledDate(new Date());
            cardRequestTO.setEstelam2Flag(Estelam2FlagType.R);
            //cardRequestTO.setPriority(99);

            Boolean checkCMSforLatestCard = Boolean.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_SKIP_CMS_CHECK, DEFAULT_SKIP_CMS_CHECK));
            //Check CMS for latestCard if profile key for checking CMS is false(skip cms)
            if (!checkCMSforLatestCard)
                getCardManagementService().checkRequestValidation(cardRequestTO.getCitizen().getNationalID(), cardRequestTO.getType());
            CitizenTO newCitizen = getCitizenDAO().create(cardRequestTO.getCitizen());
            cardRequestTO.setCitizen(newCitizen);
            /*Create Fake Success Payment*/
            createFakePaymentForCCOSVIPAndReplica(cardRequestTO);
            /*Create Fake Success Payment*/
            CardRequestTO newCardRequest = getCardRequestDAO().create(cardRequestTO);
            getCardRequestHistoryDAO().create(new CardRequestTO(newCardRequest.getId()), "Pish SabteName VIP : " + cardRequestTO.getType(), SystemId.CCOS, null,
                    CardRequestHistoryAction.COMPLETE_REGISTRATION, userProfile.getUserName());

            RegistrationVIPVTO registrationVIPVTO = new RegistrationVIPVTO();
            registrationVIPVTO.setCardRequestId(newCardRequest.getId());
            registrationVIPVTO.setTrackingId(generateTrackingId);
            return registrationVIPVTO;
        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    //Madanipour
    private void checkCitizenInfoValidForOfficeVIPRequest(CitizenInfoTO czi) throws BaseException {

        //Validate citizen info
        if (czi.getReligion() == null)
            throw new ServiceException(BizExceptionCode.RSI_151,
                    BizExceptionCode.RSI_027_MSG);

        if (isNullOrEmptyString(czi.getBirthDateLunar())) {
            try {
                DateUtil.convert(czi.getBirthDateLunar(), DateUtil.HIJRI);
            } catch (DateFormatException e) {
                throw new ServiceException(BizExceptionCode.RSI_152,
                        BizExceptionCode.RSI_072_MSG, e);
            }
        }

        if (isNullOrEmptyString(czi.getBirthDateSolar())) {
            try {
                czi.setBirthDateSolar(DateUtil.convert(
                        czi.getBirthDateGregorian(), DateUtil.JALALI));
            } catch (Exception e) {
                throw new ServiceException(BizExceptionCode.RSI_153,
                        BizExceptionCode.RSI_073_MSG, e);
            }
        }

        // Validate address info
        if (isNullOrEmptyString(czi.getAddress())
                || (czi.getAddress().length() > 300))
            throw new ServiceException(BizExceptionCode.RSI_154,
                    BizExceptionCode.RSI_046_MSG);
//
//				if (!isNullOrEmptyString(czi.getPhone())
//						&& (czi.getPhone().length() > 20))
//					throw new ServiceException(BizExceptionCode.RSI_047,
//							BizExceptionCode.RSI_047_MSG);

        if (EmsUtil.checkString(czi.getMobile())) {
            if (!EmsUtil.checkRegex(czi.getMobile(), EmsUtil.cellNoConstraint)) {
                throw new ServiceException(BizExceptionCode.RSI_155,
                        BizExceptionCode.RSI_048_MSG);
            }
        } else {
            throw new ServiceException(BizExceptionCode.RSI_156,
                    BizExceptionCode.RSI_048_MSG);
        }

        if (isNullOrEmptyString(czi.getPostcode())
                || (czi.getPostcode().length() != 10))
            throw new ServiceException(BizExceptionCode.RSI_157,
                    BizExceptionCode.RSI_036_MSG);

    }


    @Override
    public List<CitizenTO> findByNID(String nid) throws BaseException {
        return getCitizenDAO().findByNID(nid);
    }

    //Anbari:Estelam
//			@Override
//			public EstelamImsImageWTO getImsEstelamImage(String nationalID) throws BaseException {
//				Integer imsEstelamImageEnable = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_IMS_ESTELAM_IAMGE_ENABLE
//		                , DEFAULT_KEY_IMS_ESTELAM_IAMGE_ENABLE));
//				EstelamImsImageWTO estelamImsImageWTO = new EstelamImsImageWTO(); 
//				if(imsEstelamImageEnable == 1)
//				{
//					 List<ImsEstelamImageTO> imsEstelamImageTOList = getImsEstelamImageDAO().findImsImageByNationalIdAndType(nationalID,ImsEstelamImageType.IMS_BOTH_IMAGE);
//					 for (ImsEstelamImageTO imsEstelamImageTO : imsEstelamImageTOList) {				 
//						 if(imsEstelamImageTO.getType().equals(ImsEstelamImageType.IMS_NID_IMAGE))
//							 estelamImsImageWTO.setNidImage(imsEstelamImageTO.getData());
//						 if(imsEstelamImageTO.getType().equals(ImsEstelamImageType.IMS_CERT_IMAGE))
//							 estelamImsImageWTO.setCertImage(imsEstelamImageTO.getData());				
//					}			 			 
//				}
//				return estelamImsImageWTO;
//			}


    //Anbari:Estelam
    @Override
    public EstelamImsImageWTO getImsEstelamImage(String nationalID) throws BaseException {
        Integer imsEstelamImageEnable = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_IMS_ESTELAM_IAMGE_ENABLE
                , DEFAULT_KEY_IMS_ESTELAM_IAMGE_ENABLE));
        EstelamImsImageWTO estelamImsImageWTO = new EstelamImsImageWTO();
        if (imsEstelamImageEnable == 1) {
            ImsEstelamImageTO imsEstelamImageTO = getImsEstelamImageDAO().findImsImageByNationalID(nationalID);
            if (imsEstelamImageTO != null)
                estelamImsImageWTO.setNidImage(imsEstelamImageTO.getNationalIdImage());
        }
        return estelamImsImageWTO;
    }


    //Anbari:Estelam
    private ImsEstelamImageDAO getImsEstelamImageDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        ImsEstelamImageDAO imsEstelamImageDAO = null;
        try {
            imsEstelamImageDAO = factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_IMS_ESTELAM_IMAGE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.MMS_065,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_IMS_ESTELAM_IMAGE.split(","));
        }
        return imsEstelamImageDAO;
    }

    //Anbari
    @Override
    public CitizenBirthDateAndGenderWTO getBirthDateAndGenderByRequestId(Long requestId) throws BaseException {
        CitizenTO citizenTO = getCitizenDAO().findRequestId(requestId);
        CitizenBirthDateAndGenderWTO wto = new CitizenBirthDateAndGenderWTO();
        if (citizenTO != null) {
            CitizenInfoTO czi = citizenTO.getCitizenInfo();
            if (czi != null) {
                Date birthDateGregorian = czi.getBirthDateGregorian();
                Gender gender = czi.getGender();
                if (birthDateGregorian != null)
                    wto.setBirthDate(new Timestamp(czi.getBirthDateGregorian().getTime()));
                if (gender != null)
                    wto.setGender(czi.getGender().name());

            }
        }
        return wto;
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

    private RegistrationPaymentService getRegistrationPaymentService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        RegistrationPaymentService registrationPaymentService;
        try {
            registrationPaymentService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_REGISTRATION_PAYMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_REGISTRATION_PAYMENT.split(","));
        }
        registrationPaymentService.setUserProfileTO(getUserProfileTO());
        return registrationPaymentService;
    }
}
