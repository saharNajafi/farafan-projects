package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.profile.ProfileException;
import com.gam.commons.profile.ProfileManager;
import com.gam.commons.security.SecurityException;
import com.gam.commons.security.certificate.CertificateUtil;
import com.gam.nocr.ems.biz.service.BusinessLogService;
import com.gam.nocr.ems.biz.service.CMSService;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.*;
import com.gam.nocr.ems.data.dao.BusinessLogDAO;
import com.gam.nocr.ems.data.dao.CardDAO;
import com.gam.nocr.ems.data.dao.CardRequestDAO;
import com.gam.nocr.ems.data.dao.CardRequestHistoryDAO;
import com.gam.nocr.ems.data.domain.BusinessLogTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.vol.CardApplicationInfoVTO;
import com.gam.nocr.ems.data.domain.vol.CardInfoVTO;
import com.gam.nocr.ems.data.enums.AfterDeliveryRequestType;
import com.gam.nocr.ems.data.enums.BusinessLogAction;
import com.gam.nocr.ems.data.enums.BusinessLogActionAttitude;
import com.gam.nocr.ems.data.enums.BusinessLogEntity;
import com.gam.nocr.ems.data.enums.CardRequestHistoryAction;
import com.gam.nocr.ems.data.enums.CardState;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.util.EmsUtil;

import org.slf4j.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gam.nocr.ems.config.EMSLogicalNames.*;

/**
 * <p>
 * TODO -- Explain this class
 * </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 * @author <a href="mailto:haeri@gamelectronics.com">Nooshin Haeri</a>
 */
@Stateless(name = "AfterDeliveryService")
@Local(AfterDeliveryServiceLocal.class)
@Remote(AfterDeliveryServiceRemote.class)
public class AfterDeliveryServiceImpl extends EMSAbstractService implements AfterDeliveryServiceLocal, AfterDeliveryServiceRemote {

    private static final Logger logger = BaseLog.getLogger(AfterDeliveryServiceImpl.class);

    private CardDAO getCardDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CARD));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.CMS_013, BizExceptionCode.GLB_001_MSG, e, EMSLogicalNames.DAO_CARD.split(","));
        }
    }

    private CardRequestDAO getCardRequestDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_CARD_REQUEST));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.KSI_001, BizExceptionCode.GLB_001_MSG, e, DAO_CARD_REQUEST.split(","));
        }
    }

    private CMSService getCMSService() throws BaseException {
        CMSService cmsService = null;
        try {
            cmsService = ServiceFactoryProvider.getServiceFactory().getService(getExternalServiceJNDIName(SRV_CMS), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.KSI_002, BizExceptionCode.GLB_001_MSG, e, SRV_CMS.split(","));
        }
        cmsService.setUserProfileTO(getUserProfileTO());
        return cmsService;
    }

    private BusinessLogDAO getBusinessLogDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_BUSINESSLOG));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.KSI_003, BizExceptionCode.GLB_001_MSG, e, DAO_BUSINESSLOG.split(","));
        }
    }
    
    private CardRequestHistoryDAO getCardRequestHistoryDAO()
			throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					getDaoJNDIName(DAO_CARD_REQUEST_HISTORY));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.CRE_003,
					BizExceptionCode.GLB_001_MSG, e,
					DAO_CARD_REQUEST_HISTORY.split(","));
		}
	}
    
    //Anbari
    private BusinessLogService getBusinessLogService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider.getServiceFactory();
        BusinessLogService businessLogService;
        try {
            businessLogService = serviceFactory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_BUSINESS_LOG), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.CRI_014,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_BUSINESS_LOG.split(","));
        }
        return businessLogService;
    }

    /**
     * @param certificate
     * @throws ServiceException
     * @author Sina Golesorkhi
     */
    @Override
    public void doOCSPVerification(byte[] certificate) throws ServiceException {
        CertificateFactory certFactory;
        try {
            certFactory = CertificateFactory.getInstance("X.509");
            InputStream in = new ByteArrayInputStream(certificate);
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);
            ProfileManager profileManager = ProfileHelper.getProfileManager();
            NOCRPKEParamProviderImpl nocrpkeParamProvider = new NOCRPKEParamProviderImpl(profileManager);
            nocrpkeParamProvider.setValidationMethod("OCSP");
            CertificateUtil.verifyCerificate(cert, nocrpkeParamProvider, nocrpkeParamProvider.getValidationMethod());
        } catch (CertificateException e) {
            throw new ServiceException(BizExceptionCode.DOV_001, e);
        } catch (ProfileException e) {
            throw new ServiceException(BizExceptionCode.DOV_002, e);
        } catch (SecurityException e) {
            throw new ServiceException(BizExceptionCode.DOV_003, e);
        }
    }

    @Override
    public void doAfterDelivery(long requestId, AfterDeliveryRequestType requestType, String complementaryData) throws BaseException {
        switch (requestType) {
            case CHANGE_PIN:
                changePin(requestId, requestType, complementaryData);
                return;

            case REPEAL_SIGNATURE:
                repealSignature(requestId, requestType, complementaryData);
                return;

            case RESUME:
                resume(requestId, requestType, complementaryData);
                return;

            case REVOKE:
                revoke(requestId, requestType, complementaryData);
                return;

            case SUSPEND:
                suspend(requestId, requestType, complementaryData);
                return;

            case UNBLOCK:
                unblock(requestId, requestType, complementaryData);
                return;

            case UPDATE_DYNAMIC_DATA:
                updateDynamicData(requestId, requestType, complementaryData);
                return;
        }
    }

    @Permissions(value = "ems_changePin")
    private void changePin(long requestId, AfterDeliveryRequestType requestType, String complementaryData) throws BaseException {
        // Just for logging purposes    	
        getBusinessLogService().insertLog(
                new BusinessLogTO(new Timestamp(new Date().getTime()), getUserProfileTO().getUserName(), BusinessLogAction.AFTER_DELIVERY_CHANGE_PIN, BusinessLogEntity.REQUEST, Long
                        .toString(requestId), complementaryData));

      //Anbari : createLog for after delivery changepin
        getCardRequestHistoryDAO().create(new CardRequestTO(requestId),	"AfterDelivery : CHANGE PIN",  SystemId.CCOS, null,CardRequestHistoryAction.CHANGE_PIN,getUserProfileTO().getUserName());
    }

    @Permissions(value = "ems_repealSignature")
    private void repealSignature(long requestId, AfterDeliveryRequestType requestType, String complementaryData) throws BaseException {
        /**
         * 1 : ACTIVE (Changed to 3) 2 : SUSPEND (Changed to 4)
         */
        int CARD_APPLICATION_STATUS = 4;
        String signatureApp = null;
        String productId = null;
        String SIGNATURE_APP_DEFAULT_VALUE = "AppSig01";
        String PRODUCT_ID_DEFAULT_VALUE = "200";
        CardRequestTO cr = getCardRequestDAO().find(CardRequestTO.class, requestId);
        if (cr == null) {
            throw new ServiceException(BizExceptionCode.KSI_017, BizExceptionCode.KSI_017_MSG, new Long[]{requestId});
        }

        CitizenTO citizenTO = cr.getCitizen();
        if (citizenTO == null) {
            throw new ServiceException(BizExceptionCode.KSI_018, BizExceptionCode.KSI_018_MSG, new Long[]{requestId});
        }

        String nationalId = citizenTO.getNationalID();
        if (nationalId == null) {
            throw new ServiceException(BizExceptionCode.KSI_019, BizExceptionCode.KSI_019_MSG, new Long[]{citizenTO.getId()});
        }

        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            signatureApp = (String) pm.getProfile(ProfileKeyName.KEY_CARD_APPLICATION_SIGNATURE_ID, true, null, null);
            productId = (String) pm.getProfile(ProfileKeyName.KEY_CMS_ISSUE_CARD_PRODUCT_ID, true, null, null);
        } catch (Exception e) {
            logger.warn(BizExceptionCode.KSI_015, BizExceptionCode.KSI_015_MSG, e);
        }
        if (signatureApp == null) {
            signatureApp = SIGNATURE_APP_DEFAULT_VALUE;
        }
        if (productId == null) {
            productId = PRODUCT_ID_DEFAULT_VALUE;
        }
        CardInfoVTO cardInfoVTO = getCMSService().getCurrentCitizenCardByProduct(nationalId, productId);
        if (cardInfoVTO == null) {
            throw new ServiceException(BizExceptionCode.KSI_020, BizExceptionCode.KSI_020_MSG, new Object[]{nationalId, productId});
        }
        String crn = cardInfoVTO.getCrn();
        List<CardApplicationInfoVTO> cardApplicationInfoVTOList = getCMSService().getCardApplications(crn);
        boolean signatureFlag = false;
        for (CardApplicationInfoVTO cardApplicationInfoVTO : cardApplicationInfoVTOList) {
            if (signatureApp.equals(cardApplicationInfoVTO.getId())) {
                cardApplicationInfoVTO.setStatus(CARD_APPLICATION_STATUS);
                cardApplicationInfoVTO.setReason("UNKNOWN");
                getCMSService().updateCardApplicationStatus(crn, cardApplicationInfoVTO);
                signatureFlag = true;
                break;
            }
        }

        if (!signatureFlag) {
            logger.info("===================================================================================");
            logger.info("============================= SIGNATURE NOT FOUND =================================");
            logger.info(BizExceptionCode.KSI_016_MSG);
            logger.info("===================================================================================");
            logger.info("===================================================================================");
            throw new ServiceException(BizExceptionCode.KSI_016, BizExceptionCode.KSI_016_MSG);
        }

        getBusinessLogService().insertLog(
                new BusinessLogTO(new Timestamp(new Date().getTime()), getUserProfileTO().getUserName(), BusinessLogAction.AFTER_DELIVERY_REPEAL_SIGNATURE, BusinessLogEntity.REQUEST, Long
                        .toString(requestId), complementaryData));
    }

    @Permissions(value = "ems_resumeCard")
    private void resume(long requestId, AfterDeliveryRequestType requestType, String complementaryData) throws BaseException {
        CardRequestTO cr = getCardRequestDAO().find(CardRequestTO.class, requestId);
        if (cr == null) {
            throw new ServiceException(BizExceptionCode.KSI_004, BizExceptionCode.KSI_004_MSG, new Long[]{requestId});
        }
        if (cr.getCard() == null) {
            throw new ServiceException(BizExceptionCode.KSI_005, BizExceptionCode.KSI_005_MSG, new Long[]{requestId});
        }
        if (cr.getCard().getCrn() == null || cr.getCard().getCrn().trim().equals("")) {
            throw new ServiceException(BizExceptionCode.KSI_006, BizExceptionCode.KSI_006_MSG, new Long[]{requestId});
        }

        try {
            getCMSService().resumeCard(cr.getCard().getCrn(), complementaryData);
        } catch (BaseException e) {
            // Handles already resumed card
            if (BizExceptionCode.CSI_064.equals(e.getExceptionCode())) {
                logger.info(e.getExceptionCode(), e.getMessage(), e);
            } else {
                throw e;
            }
        }
        List<Long> ids = new ArrayList<Long>();
        ids.add(cr.getCard().getId());
        getCardDAO().updateCardsState(ids, CardState.DELIVERED);
        getBusinessLogService().insertLog(
                new BusinessLogTO(new Timestamp(new Date().getTime()), getUserProfileTO().getUserName(), BusinessLogAction.AFTER_DELIVERY_RESUME, BusinessLogEntity.REQUEST, Long.toString(requestId),
                        complementaryData));
    }

    @Permissions(value = "ems_revokeCard")
    private void revoke(long requestId, AfterDeliveryRequestType requestType, String complementaryData) throws BaseException {
        CardRequestTO cr = getCardRequestDAO().find(CardRequestTO.class, requestId);
        if (cr == null) {
            throw new ServiceException(BizExceptionCode.KSI_007, BizExceptionCode.KSI_007_MSG, new Long[]{requestId});
        }
        if (cr.getCard() == null) {
            throw new ServiceException(BizExceptionCode.KSI_008, BizExceptionCode.KSI_008_MSG, new Long[]{requestId});
        }
        if (cr.getCard().getCrn() == null || cr.getCard().getCrn().trim().equals("")) {
            throw new ServiceException(BizExceptionCode.KSI_009, BizExceptionCode.KSI_009_MSG, new Long[]{requestId});
        }
        try {
            getCMSService().revokeCard(cr.getCard().getCrn(), complementaryData);
        } catch (BaseException e) {
            // Handles already revoked card
            if (BizExceptionCode.CSI_071.equals(e.getExceptionCode())) {
                logger.info(e.getExceptionCode(), e.getMessage(), e);
            } else {
                throw e;
            }
        }
        List<Long> ids = new ArrayList<Long>();
        ids.add(cr.getCard().getId());
        getCardDAO().updateCardsState(ids, CardState.REVOKED);
        getBusinessLogService().insertLog(
                new BusinessLogTO(new Timestamp(new Date().getTime()), getUserProfileTO().getUserName(), BusinessLogAction.AFTER_DELIVERY_REVOKE, BusinessLogEntity.REQUEST, Long.toString(requestId),
                        complementaryData));
    }

    @Permissions(value = "ems_suspendCard")
    private void suspend(long requestId, AfterDeliveryRequestType requestType, String complementaryData) throws BaseException {
        CardRequestTO cr = getCardRequestDAO().find(CardRequestTO.class, requestId);
        if (cr == null) {
            throw new ServiceException(BizExceptionCode.KSI_010, BizExceptionCode.KSI_010_MSG, new Long[]{requestId});
        }
        if (cr.getCard() == null) {
            throw new ServiceException(BizExceptionCode.KSI_011, BizExceptionCode.KSI_011_MSG, new Long[]{requestId});
        }
        if (cr.getCard().getCrn() == null || cr.getCard().getCrn().trim().equals("")) {
            throw new ServiceException(BizExceptionCode.KSI_012, BizExceptionCode.KSI_012_MSG, new Long[]{requestId});
        }
        try {
            getCMSService().suspendCard(cr.getCard().getCrn(), complementaryData);
        } catch (BaseException e) {
            // Handles already suspended card
            if (BizExceptionCode.CSI_057.equals(e.getExceptionCode())) {
                logger.info(e.getExceptionCode(), e.getMessage(), e);
            } else {
                throw e;
            }
        }
        List<Long> ids = new ArrayList<Long>();
        ids.add(cr.getCard().getId());
        getCardDAO().updateCardsState(ids, CardState.LOST);
        getBusinessLogService().insertLog(
                new BusinessLogTO(new Timestamp(new Date().getTime()), getUserProfileTO().getUserName(), BusinessLogAction.AFTER_DELIVERY_SUSPEND, BusinessLogEntity.REQUEST, Long.toString(requestId),
                        complementaryData));
    }

    @Permissions(value = "ems_unblockCard")
    private void unblock(long requestId, AfterDeliveryRequestType requestType, String complementaryData) throws BaseException {
        // Just for logging purposes
    	getBusinessLogService().insertLog(
                new BusinessLogTO(new Timestamp(new Date().getTime()), getUserProfileTO().getUserName(), BusinessLogAction.AFTER_DELIVERY_UNBLOCK, BusinessLogEntity.REQUEST, Long.toString(requestId),
                        complementaryData));
        
        //Anbari : createLog for after delivery unblock
        getCardRequestHistoryDAO().create(new CardRequestTO(requestId),	"AfterDelivery : UNBLOCK",  SystemId.CCOS, null,CardRequestHistoryAction.AFTER_DELIVERY_UNBLOCK,getUserProfileTO().getUserName());
        
    }

    @Permissions(value = "ems_repealSignature")
    private void updateDynamicData(long requestId, AfterDeliveryRequestType requestType, String complementaryData) throws BaseException {
        // Just for logging purposes
    	getBusinessLogService().insertLog(
                new BusinessLogTO(new Timestamp(new Date().getTime()), getUserProfileTO().getUserName(), BusinessLogAction.AFTER_DELIVERY_UPDATE_DYNAMIC_DATA, BusinessLogEntity.REQUEST, Long
                        .toString(requestId), complementaryData));
        
        
    }    


}
