package com.gam.nocr.ems.biz.service.external.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.AbstractService;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.factory.DAOFactory;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.BusinessLogService;
import com.gam.nocr.ems.biz.service.CMSService;
import com.gam.nocr.ems.biz.service.IMSManagementService;
import com.gam.nocr.ems.biz.service.IMSService;
import com.gam.nocr.ems.config.*;
import com.gam.nocr.ems.data.dao.CardRequestDAO;
import com.gam.nocr.ems.data.dao.CardRequestHistoryDAO;
import com.gam.nocr.ems.data.dao.CertificateDAO;
import com.gam.nocr.ems.data.dao.CitizenDAO;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.vol.CardInfoVTO;
import com.gam.nocr.ems.data.enums.*;
import com.gam.nocr.ems.util.EmsUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.ejb.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * The class CardIssuanceRequestServiceImpl is used to prepare all the requirements which are needed in calling the CMS
 * service which handles the card issuance request.
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "CardIssuanceRequestService")
@Local(CardIssuanceRequestServiceLocal.class)
@Remote(CardIssuanceRequestServiceRemote.class)
public class CardIssuanceRequestServiceImpl extends AbstractService implements CardIssuanceRequestServiceLocal, CardIssuanceRequestServiceRemote {

    private static final String IMS_EXCEPTION = "imsException";
    private static final String CMS_EXCEPTION = "cmsException";

    private static final String SUCCESS_VALUE = "SUCCESS";
//	private static final String GAM_ERROR_WITH_LIMITED_RETRY = "GAM_EWLR:";
//	private static final String GAM_ERROR_WITH_UNLIMITED_RETRY = "GAM_EWUR:";
//	private static final String GAM_ERROR_WITH_NO_RETRY = "GAM_EWNR:";

    private static final int CMS_CARD_STATE_SUSPEND = 8;
    private static final int CMS_CARD_STATE_REVOKED = 9;
    private static final int CMS_CARD_STATE_DESTROYED = 10;

    private static final Logger logger = BaseLog.getLogger(CardIssuanceRequestServiceImpl.class);
    private static final Logger cmsLogger = BaseLog.getLogger("CmsLogger");

    /**
     * ===============
     * Getter for DAOs
     * ===============
     */

    /**
     * getCardRequestDAO
     */
    private CardRequestDAO getCardRequestDAO() throws BaseException {
        DAOFactory daoFactory = DAOFactoryProvider.getDAOFactory();
        CardRequestDAO cardRequestDAO = null;
        try {
            cardRequestDAO = daoFactory.getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST));
        } catch (DAOFactoryException e) {
            cmsLogger.error(BizExceptionCode.CRI_003 + " : " + BizExceptionCode.GLB_001_MSG, e);
            throw new ServiceException(
                    BizExceptionCode.CRI_003,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_CARD_REQUEST.split(","));
        }
        return cardRequestDAO;
    }

    /**
     * getCardRequestHistoryDAO
     */
    private CardRequestHistoryDAO getCardRequestHistoryDAO() throws BaseException {
        DAOFactory daoFactory = DAOFactoryProvider.getDAOFactory();
        CardRequestHistoryDAO cardRequestHistoryDAO = null;
        try {
            cardRequestHistoryDAO = daoFactory.getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST_HISTORY));
        } catch (DAOFactoryException e) {
            cmsLogger.error(BizExceptionCode.CRI_004 + " : " + BizExceptionCode.GLB_001_MSG, e);
            throw new ServiceException(
                    BizExceptionCode.CRI_004,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_CARD_REQUEST_HISTORY.split(","));
        }
        return cardRequestHistoryDAO;
    }

    private CitizenDAO getCitizenDAO() throws BaseException {
        DAOFactory daoFactory = DAOFactoryProvider.getDAOFactory();
        CitizenDAO citizenDAO = null;
        try {
            citizenDAO = daoFactory.getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CITIZEN));
        } catch (DAOFactoryException e) {
            cmsLogger.error(BizExceptionCode.CRI_006 + " : " + BizExceptionCode.GLB_001_MSG, e);
            throw new ServiceException(
                    BizExceptionCode.CRI_006,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_CITIZEN.split(","));
        }
        return citizenDAO;
    }

    /**
     * getCertificateDAO
     *
     * @return an instance of type CertificateDAO
     */
    private CertificateDAO getCertificateDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        CertificateDAO certificateDAO = null;
        try {
            certificateDAO = (CertificateDAO) factory.getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CERTIFICATE));
        } catch (DAOFactoryException e) {
            cmsLogger.error(BizExceptionCode.CRI_008 + " : " + BizExceptionCode.GLB_001_MSG, e);
            throw new ServiceException(
                    BizExceptionCode.CRI_008,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_CERTIFICATE.split(","));
        }
        return certificateDAO;
    }

    /**
     * ===================
     * Getter for Services
     * ===================
     */

    private BusinessLogService getBusinessLogService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider.getServiceFactory();
        BusinessLogService businessLogService;
        try {
            businessLogService = serviceFactory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_BUSINESS_LOG), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            cmsLogger.error(BizExceptionCode.CRI_014 + " : " + BizExceptionCode.GLB_002_MSG, e);
            throw new ServiceException(
                    BizExceptionCode.CRI_014,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_BUSINESS_LOG.split(","));
        }
        return businessLogService;
    }

    private IMSManagementService getIMSManagementService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider.getServiceFactory();
        IMSManagementService imsManagementService;
        try {
            imsManagementService = serviceFactory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_IMS_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            cmsLogger.error(BizExceptionCode.CRI_015 + " : " + BizExceptionCode.GLB_002_MSG, e);
            throw new ServiceException(
                    BizExceptionCode.CRI_015,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_IMS_MANAGEMENT.split(","));
        }
        return imsManagementService;
    }

    private CMSService getCMSService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider.getServiceFactory();
        CMSService cmsService = null;
        try {
            cmsService = serviceFactory.getService(EMSLogicalNames.getExternalServiceJNDIName(EMSLogicalNames.SRV_CMS), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            cmsLogger.error(BizExceptionCode.CRI_002 + " : " + BizExceptionCode.GLB_002_MSG, e);
            throw new ServiceException(
                    BizExceptionCode.CRI_002,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
        }
        return cmsService;
    }

    /**
     * getIMSService
     *
     * @return an instance of type {@link com.gam.nocr.ems.biz.service.IMSService}
     */
    private IMSService getIMSService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider.getServiceFactory();
        IMSService imsService = null;
        try {
            imsService = serviceFactory.getService(EMSLogicalNames.getExternalServiceJNDIName(EMSLogicalNames.SRV_IMS), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            cmsLogger.error(BizExceptionCode.CRI_011 + " : " + BizExceptionCode.GLB_002_MSG, e);
            throw new ServiceException(
                    BizExceptionCode.CRI_011,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_IMS.split(","));
        }
        return imsService;
    }

    private void createBusinessLog(BusinessLogAction logAction,
                                   BusinessLogEntity logEntityName,
                                   String logActor,
                                   String additionalData,
                                   Boolean exceptionFlag) throws BaseException {
        BusinessLogTO businessLogTO = new BusinessLogTO();
        businessLogTO.setEntityID(" ");
        businessLogTO.setAction(logAction);
        businessLogTO.setEntityName(logEntityName);
        businessLogTO.setActor(logActor);
        businessLogTO.setAdditionalData(additionalData);
        businessLogTO.setDate(new Timestamp(new Date().getTime()));
        if (exceptionFlag) {
            businessLogTO.setActionAttitude(BusinessLogActionAttitude.F);
        } else {
            businessLogTO.setActionAttitude(BusinessLogActionAttitude.T);
        }
        getBusinessLogService().insertLog(businessLogTO);

    }

    private List<CardRequestTO> loadCardRequestListWithAllAttributes(List<CardRequestTO> cardRequestTOList) throws BaseException {
//		TODO : Correct the type of fetching on future
        for (CardRequestTO cardRequestTO : cardRequestTOList) {
            getCitizenDAO().findAllAttributesByRequestId(cardRequestTO.getId());
        }
        return cardRequestTOList;
    }

    /**
     * The method updateCardRequestTOState is used to update an instance of type {@link CardRequestTO}
     */
    private CardRequestTO updateCardRequestTOState(CardRequestTO cardRequestTO, CardRequestState cardRequestState) throws BaseException {
        cardRequestTO.setState(cardRequestState);
        getCardRequestDAO().update(cardRequestTO);
        return cardRequestTO;
    }

    /**
     * The method updateCardRequestHistory is used to update an instance of type {@link CardRequestHistoryTO}
     */
    private CardRequestHistoryTO updateCardRequestHistory(CardRequestTO cardRequestTO,
                                                          String stringRequestId,
                                                          String resultValue,
                                                          CardRequestHistoryAction action) throws BaseException {

        logger.info("\nThe method updateCardRequestHistory has been started with these parameters : .");
        cmsLogger.info("\nThe method updateCardRequestHistory has been started with these parameters : .");
        logger.info("\ncardRequestTo with id : " + cardRequestTO.getId());
        cmsLogger.info("\ncardRequestTo with id : " + cardRequestTO.getId());
        logger.info("\nstringRequestId : '" + stringRequestId + "'");
        cmsLogger.info("\nstringRequestId : '" + stringRequestId + "'");
        logger.info("\nresult : '" + resultValue + "'");
        cmsLogger.info("\nresult : '" + resultValue + "'");
        CardRequestHistoryTO cardRequestHistoryTO = new CardRequestHistoryTO();
        cardRequestHistoryTO.setCardRequest(cardRequestTO);
        cardRequestHistoryTO.setDate(new Date());
        cardRequestHistoryTO.setRequestID(stringRequestId);
        cardRequestHistoryTO.setSystemID(SystemId.CMS);

        if (EmsUtil.checkString(resultValue)) {
            if (resultValue.length() >= 250) {
                resultValue = StringUtils.substring(resultValue, 0, 250);
            }
            cardRequestHistoryTO.setResult(resultValue);
        }
        if (action != null) {
            cardRequestHistoryTO.setCardRequestHistoryAction(action);
        }
        getCardRequestHistoryDAO().create(cardRequestHistoryTO);
        logger.info("\nThe method updateCardRequestHistory has been finished .");
        cmsLogger.info("\nThe method updateCardRequestHistory has been finished .");
        return cardRequestHistoryTO;
    }

    /**
     * The method checkCardRequestHistoryToRetry is used to check whether the retry request is possible or not
     *
     * @param cardRequestTO an instance of type {@link CardRequestTO}
     */
    private void checkCardRequestHistoryToRetry(CardRequestTO cardRequestTO) throws BaseException {
        if (!getCardRequestHistoryDAO().checkAcceptabilityOfRetryRequest(cardRequestTO, SystemId.CMS)) {
            updateCardRequestTOState(cardRequestTO, CardRequestState.CMS_ERROR);
        }
    }

    /**
     * The method getLatestFromCardVTOList is used to find the latest cardInfoVTO between the list
     *
     * @param cardInfoVTOList is a list of type {@link com.gam.nocr.ems.data.domain.vol.CardInfoVTO}
     * @return an object of type {@link com.gam.nocr.ems.data.domain.vol.CardInfoVTO}
     */
    private CardInfoVTO getLatestFromCardVTOList(List<CardInfoVTO> cardInfoVTOList) throws BaseException {
        Date date = cardInfoVTOList.get(0).getIssuanceDate();
        int latestDate = 0;
        for (int i = 1; i < cardInfoVTOList.size(); i++) {
            if (date.before(cardInfoVTOList.get(i).getIssuanceDate())) {
                date = cardInfoVTOList.get(i).getIssuanceDate();
                latestDate = i;
            }
        }
        return cardInfoVTOList.get(latestDate);
    }

    /**
     * The method getMessageRequestIdCount is used to get the digit count of the request id from config
     *
     * @return an instance of type {@link Integer}
     * @throws BaseException
     */
    private Integer getMessageRequestIdCount() throws BaseException {
        Integer DEFAULT_COUNT = 16;
        Integer requestIdDigitCount = null;
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            requestIdDigitCount = Integer.parseInt((String) pm.getProfile(ProfileKeyName.KEY_CMS_REQUEST_ID_DIGIT_COUNT, true, null, null));
        } catch (Exception e) {
            logger.warn(BizExceptionCode.CID_003_MSG);
            cmsLogger.warn(BizExceptionCode.CID_003_MSG);
        }
        if (requestIdDigitCount == null) {
            requestIdDigitCount = DEFAULT_COUNT;
        }
        return requestIdDigitCount;
    }

    /**
     * The method getReduplicateCount is used to calculate the reduplicate field
     *
     * @param nationalId is an instance of type {@link String}, which indicates an appropriate nationalId
     * @return an instance of type {@link Long}, which represents the calculated reduplicate field
     * @throws BaseException
     */
    private Long getReduplicateCount(String nationalId) throws BaseException {
        try {
            return getCardRequestDAO().getReplicaCardRequestsCount(nationalId) + 1;
        } catch (BaseException e) {
            logger.error(e.getExceptionCode() + " : " + e.getMessage(), e);
            cmsLogger.error(e.getExceptionCode() + " : " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(BizExceptionCode.CRI_013 + " : " + BizExceptionCode.GLB_008_MSG, e);
            cmsLogger.error(BizExceptionCode.CRI_013 + " : " + BizExceptionCode.GLB_008_MSG, e);
            throw new ServiceException(BizExceptionCode.CRI_013, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * The method getProductIdFromProfile is used to get the productId by using {@link ProfileManager}
     *
     * @return an instance of type {@link String}, which represents the value of product Id
     * @throws BaseException
     */
    private String getProductIdFromProfile() throws BaseException {
        String productId = null;
        String DEFAULT_PRODUCT_ID = "200";
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            productId = (String) pm.getProfile(ProfileKeyName.KEY_CMS_ISSUE_CARD_PRODUCT_ID, true, null, null);
        } catch (Exception e) {
            logger.warn(BizExceptionCode.CRI_010, BizExceptionCode.GLB_009_MSG, e);
            cmsLogger.warn(BizExceptionCode.CRI_010, BizExceptionCode.GLB_009_MSG, e);
        }

        if (productId == null || productId.isEmpty()) {
            productId = DEFAULT_PRODUCT_ID;
        }
        return productId;
    }

    /**
     * The method sendIssuanceRequest is used to send the issue card request to the sub system 'CMS'
     */
//	@BizLoggable(logAction = "ISSUE", logEntityName = "CARD", logActor = "System")
    @javax.ejb.TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private String sendIssuanceRequest(CardRequestTO cardRequestTO, CertificateTO certificateTO) throws BaseException {
        String returnValue = "";
        String imsException = "";
        String cmsException = "";

//		TODO : Omit the extra error codes which were written in the catch clause

        /*
        Calculating reduplicate count
        */
        CitizenTO citizenTO = cardRequestTO.getCitizen();
        citizenTO.setReduplicate(getReduplicateCount(citizenTO.getNationalID()));

        CardRequestHistoryDAO cardRequestHistoryDAO = getCardRequestHistoryDAO();

        /*
         Generating requestId for the xml message, which belongs to an appropriate request of card issuance
         */

        String requestIdForMessage = cardRequestTO.getCmsRequestId();
        if (requestIdForMessage == null) {
            Long requestId = cardRequestHistoryDAO.getRequestIdFromSequence(SequenceName.SEQ_EMS_REQUEST_ID_CMS);
            Integer requestIdDigitCount = getMessageRequestIdCount();
            requestIdForMessage = EmsUtil.createRequestIdForMessage(requestId.toString(), requestIdDigitCount, "0", "0");
            cardRequestTO.setCmsRequestId(requestIdForMessage);
        } else {
            if (getCardRequestHistoryDAO().existInHistoryByCmsReqId(requestIdForMessage, cardRequestTO.getId())) {
                Long requestId = cardRequestHistoryDAO.getRequestIdFromSequence(SequenceName.SEQ_EMS_REQUEST_ID_CMS);
                Integer requestIdDigitCount = getMessageRequestIdCount();
                requestIdForMessage = EmsUtil.createRequestIdForMessage(requestId.toString(), requestIdDigitCount, "0", "0");
                cardRequestTO.setCmsRequestId(requestIdForMessage);
            }
        }

        /*
        Getting productId
         */
        String productId = getProductIdFromProfile();

        CardRequestType oldType = null;
        try {
            CardRequestType cardRequestType = cardRequestTO.getType();
            CardInfoVTO latestCardInfoVTO;
            switch (cardRequestType) {
                case FIRST_CARD:
                    getCMSService().issueCard(cardRequestTO, requestIdForMessage, certificateTO);
                    break;

                case UNSUCCESSFUL_DELIVERY_FOR_FIRST_CARD:
//					Set this type to be able to call CMS Service
                    oldType = CardRequestType.UNSUCCESSFUL_DELIVERY_FOR_FIRST_CARD;
                    cardRequestTO.setType(CardRequestType.UNSUCCESSFUL_DELIVERY);
                    getCMSService().issueCard(cardRequestTO, requestIdForMessage, certificateTO);
                    cardRequestTO.setType(CardRequestType.UNSUCCESSFUL_DELIVERY_FOR_FIRST_CARD);
                    break;

                case REPLICA:
                    latestCardInfoVTO = getCMSService().getCurrentCitizenCardByProduct(cardRequestTO.getCitizen().getNationalID(), productId);
                    if (CMS_CARD_STATE_SUSPEND != latestCardInfoVTO.getStatus() &&
                            CMS_CARD_STATE_REVOKED != latestCardInfoVTO.getStatus() &&
                            CMS_CARD_STATE_DESTROYED != latestCardInfoVTO.getStatus()) {
                        getCMSService().suspendCard(latestCardInfoVTO.getCrn(), cardRequestTO.getReason());
                    }
                    getCMSService().issueCard(cardRequestTO, requestIdForMessage, certificateTO);
                    break;

                case UNSUCCESSFUL_DELIVERY_FOR_REPLICA:
//					Set this type to be able to call CMS Service
                    oldType = CardRequestType.UNSUCCESSFUL_DELIVERY_FOR_REPLICA;
                    cardRequestTO.setType(CardRequestType.UNSUCCESSFUL_DELIVERY);
                    getCMSService().issueCard(cardRequestTO, requestIdForMessage, certificateTO);
                    cardRequestTO.setType(CardRequestType.UNSUCCESSFUL_DELIVERY_FOR_REPLICA);
                    break;


                case REPLACE:
                    getCMSService().issueCard(cardRequestTO, requestIdForMessage, certificateTO);
                    break;

                case UNSUCCESSFUL_DELIVERY_FOR_REPLACE:
//					Set this type to be able to call CMS Service
                    oldType = CardRequestType.UNSUCCESSFUL_DELIVERY_FOR_REPLACE;
                    cardRequestTO.setType(CardRequestType.UNSUCCESSFUL_DELIVERY);
                    getCMSService().issueCard(cardRequestTO, requestIdForMessage, certificateTO);
                    cardRequestTO.setType(CardRequestType.UNSUCCESSFUL_DELIVERY_FOR_REPLACE);
                    break;


                case EXTEND:
                    getCMSService().issueCard(cardRequestTO, requestIdForMessage, certificateTO);
                    break;

                case UNSUCCESSFUL_DELIVERY_FOR_EXTEND:
//					Set this type to be able to call CMS Service
                    oldType = CardRequestType.UNSUCCESSFUL_DELIVERY_FOR_EXTEND;
                    cardRequestTO.setType(CardRequestType.UNSUCCESSFUL_DELIVERY);
                    getCMSService().issueCard(cardRequestTO, requestIdForMessage, certificateTO);
                    cardRequestTO.setType(CardRequestType.UNSUCCESSFUL_DELIVERY_FOR_EXTEND);
                    break;

                default:
                    logger.error(BizExceptionCode.CRI_016, BizExceptionCode.CRI_016_MSG);
                    cmsLogger.error(BizExceptionCode.CRI_016, BizExceptionCode.CRI_016_MSG);
                    cmsException += BizExceptionCode.CRI_016 + ":" + BizExceptionCode.CRI_016_MSG;
                    return cmsException;
            }

            updateCardRequestTOState(cardRequestTO, CardRequestState.PENDING_ISSUANCE);
            updateCardRequestHistory(cardRequestTO, requestIdForMessage, String.valueOf(CardRequestState.PENDING_ISSUANCE), CardRequestHistoryAction.PENDING_ISSUANCE);

        } catch (ServiceException e) {
            cmsException += e.getMessage();
            /**
             * ===========================================================================================================================
             * Attitudes in order to handle the thrown exceptions:
             * 	1. For reduplicate requests: requestState = 'PENDING_ISSUANCE', requestHistoryResult = 'PENDING_ISSUANCE'
             *
             * 	2. For those requests which have a limited retry, after each try, the history is updated by 'GAM_ERROR_WITH_LIMITED_RETRY'
             * 	   , but after crossing the try counter value of config, the state of request is converted to 'CMS_ERROR'
             *
             * 	3. For those requests which have an unlimited retry, just the history is updated by 'GAM_ERROR_WITH_UNLIMITED_RETRY'
             *
             * 	4. For those requests which have no second opportunity: requestState = 'CMS_ERROR',
             * 															requestHistoryResult = 'GAM_ERROR_WITH_NO_RETRY'
             * 	===========================================================================================================================
             */

//          XML Mapper Exceptions
            if (BizExceptionCode.CSI_151.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_152.equals(e.getExceptionCode())) {
                logger.error(e.getMessage(), e);
                cmsLogger.error(e.getMessage(), e);
                String result = (SystemId.CMS.name() + ":" + ConstValues.GAM_ERROR_WITH_UNLIMITED_RETRY + e.getMessage());
                updateCardRequestHistory(cardRequestTO, requestIdForMessage, result, CardRequestHistoryAction.CMS_XSD_ERROR);
            } else if (DataExceptionCode.CRC_002.equals(e.getExceptionCode())
                    || DataExceptionCode.CRC_003.equals(e.getExceptionCode())
                    || DataExceptionCode.CRC_004.equals(e.getExceptionCode())
                    || DataExceptionCode.CRC_005.equals(e.getExceptionCode())
                    || DataExceptionCode.CRC_009.equals(e.getExceptionCode())) {
                logger.error(e.getMessage(), e);
                cmsLogger.error(e.getMessage(), e);
                String result = (SystemId.CMS.name() + ":" + ConstValues.GAM_ERROR_WITH_UNLIMITED_RETRY + e.getMessage());
                updateCardRequestHistory(cardRequestTO, requestIdForMessage, result, CardRequestHistoryAction.ISSUE_CARD_ERROR_WITH_UNLIMITED_RETRY);

            }
//			CSI_001 : reduplicate
            else if (BizExceptionCode.CSI_001.equals(e.getExceptionCode())) {
                logger.debug(e.getMessage(), e);
                cmsLogger.debug(e.getMessage(), e);
                updateCardRequestTOState(cardRequestTO, CardRequestState.PENDING_ISSUANCE);
                updateCardRequestHistory(cardRequestTO, requestIdForMessage, String.valueOf(CardRequestState.PENDING_ISSUANCE), CardRequestHistoryAction.PENDING_ISSUANCE);

            } else if (BizExceptionCode.CSI_002.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_003.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_005.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_006.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_007.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_008.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_009.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_012.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_013.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_136.equals(e.getExceptionCode())

                    || BizExceptionCode.CSI_054.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_055.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_056.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_057.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_058.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_059.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_060.equals(e.getExceptionCode())

                    || BizExceptionCode.CSI_079.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_080.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_139.equals(e.getExceptionCode())

                    || BizExceptionCode.CSI_081.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_082.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_083.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_128.equals(e.getExceptionCode())
                    ) {
//                logger.error(e.getMessage(), e);
//                String result = (SystemId.CMS.name() + ":" + ConstValues.GAM_ERROR_WITH_LIMITED_RETRY + e.getMessage());
//                cardRequestHistoryTO = updateCardRequestHistory(cardRequestTO, requestIdForMessage, result);
//                checkCardRequestHistoryToRetry(cardRequestHistoryTO.getCardRequest());
                logger.error(e.getMessage(), e);
                cmsLogger.error(e.getMessage(), e);
                String result = (SystemId.CMS.name() + ":" + ConstValues.GAM_ERROR_WITH_UNLIMITED_RETRY + e.getMessage());
                updateCardRequestHistory(cardRequestTO, requestIdForMessage, result, CardRequestHistoryAction.ISSUE_CARD_ERROR_WITH_UNLIMITED_RETRY);

//				CSI_004 : XML is not valid
            } else if (BizExceptionCode.CSI_004.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_011.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_145.equals(e.getExceptionCode())) {
                logger.error(e.getMessage(), e);
                cmsLogger.error(e.getMessage(), e);
                updateCardRequestTOState(cardRequestTO, CardRequestState.CMS_ERROR);
                String result = (SystemId.CMS.name() + ":" + ConstValues.GAM_ERROR_WITH_NO_RETRY + e.getMessage());
                updateCardRequestHistory(cardRequestTO, requestIdForMessage, result, CardRequestHistoryAction.CMS_ERROR);

            } else if (BizExceptionCode.CSI_010.equals(e.getExceptionCode())) {
                try {
                    String oldReqIdForMessage = e.getMessage().split("'")[5];
                    if (!EmsUtil.checkString(oldReqIdForMessage)) {
                        throw new ServiceException(BizExceptionCode.CRI_019, BizExceptionCode.CRI_019_MSG + e.getMessage());
                    }
                    updateCardRequestTOState(cardRequestTO, CardRequestState.PENDING_ISSUANCE);
                    updateCardRequestHistory(cardRequestTO, oldReqIdForMessage, String.valueOf(CardRequestState.PENDING_ISSUANCE), CardRequestHistoryAction.PENDING_ISSUANCE);
                    logger.info("\nDue to the returned exception message of '" + e.getMessage() + "', the pending message request id of '" + oldReqIdForMessage + "', has been added to the history of this request.\n");
                    cmsLogger.info("\nDue to the returned exception message of '" + e.getMessage() + "', the pending message request id of '" + oldReqIdForMessage + "', has been added to the history of this request.\n");
                } catch (Exception ex) {
                    logger.error("\n" + ex.getMessage() + "\n", ex);
                    cmsLogger.error("\n" + ex.getMessage() + "\n", ex);
                    updateCardRequestTOState(cardRequestTO, CardRequestState.CMS_ERROR);
                    String result = (SystemId.CMS.name() + ":" + ConstValues.GAM_ERROR_WITH_NO_RETRY + e.getMessage());
                    updateCardRequestHistory(cardRequestTO, requestIdForMessage, result, CardRequestHistoryAction.CMS_ERROR);
                }
            } else if (BizExceptionCode.CSI_071.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_114.equals(e.getExceptionCode())
                    || BizExceptionCode.CSI_146.equals(e.getExceptionCode())) {
                logger.error(e.getMessage(), e);
                cmsLogger.error(e.getMessage(), e);
                String result = (SystemId.CMS.name() + ":" + ConstValues.GAM_ERROR_WITH_UNLIMITED_RETRY + e.getMessage());
                updateCardRequestHistory(cardRequestTO, requestIdForMessage, result, CardRequestHistoryAction.ISSUE_CARD_ERROR_WITH_UNLIMITED_RETRY);
            } else {
                logger.error(e.getMessage(), e);
                cmsLogger.error(e.getMessage(), e);
                String result = (SystemId.CMS.name() + ":" + ConstValues.GAM_ERROR_WITH_UNLIMITED_RETRY + e.getMessage());
                updateCardRequestHistory(cardRequestTO, requestIdForMessage, result, CardRequestHistoryAction.ISSUE_CARD_ERROR_WITH_UNLIMITED_RETRY);
            }
        } catch (Exception e) {
            cmsException = e.getMessage();
            logger.error(e.getMessage(), e);
            cmsLogger.error(e.getMessage(), e);
            String result = (SystemId.CMS.name() + ":" + ConstValues.GAM_ERROR_WITH_UNLIMITED_RETRY + e.getMessage());
            updateCardRequestHistory(cardRequestTO, requestIdForMessage, result, CardRequestHistoryAction.ISSUE_CARD_ERROR_WITH_UNLIMITED_RETRY);
        } finally {
            if (oldType != null) {
                cardRequestTO.setType(oldType);
            }
        }

        if (EmsUtil.checkString(imsException)) {
            returnValue += IMS_EXCEPTION + ":" + imsException;
        }
        if (EmsUtil.checkString(cmsException)) {
            returnValue += CMS_EXCEPTION + ":" + cmsException;
        }

        return returnValue;
    }

    /**
     * The method findCertificateByUsage is used to find a certificate due to its usage
     */
    @Override
    public CertificateTO findCertificateByUsage(CertificateUsage certificateUsage) throws BaseException {
        return getCertificateDAO().findCertificateByUsage(certificateUsage);
    }

    /**
     * The method prepareCitizensInfoForSendingRequest is used to gathering necessary information of the citizens,
     * whenever the requests is ready to send , by calling the issue card service of the sub system 'CMS'.
     *
     * @return true if there are data to prepare, otherwise false
     * @throws BaseException
     */
    @Override
    public boolean prepareCitizensInfoForSendingRequest() throws BaseException {
        boolean returnFlag = true;

        /**
         * Used in logging process
         */
        boolean exceptionFlag = false;
        String exceptionMessage = "";
        String additionalData = null;
        String exceptedAdditionalData = "";
        List<Long> inputIdsForLog = new ArrayList<Long>();
        List<Long> cardRequestIdsForLog = new ArrayList<Long>();

        try {
            List<CardRequestTO> cardRequestTOs = null;

            int DEFAULT_VALUE_FOR_COUNT = 10;
            try {
                cardRequestTOs = getCardRequestDAO().findLimitedByCardRequestState(
                        CardRequestState.APPROVED_BY_AFIS,
                        ProfileKeyName.KEY_CARD_REQUESTS_COUNT_FOR_SENDING_TO_CMS,
                        DEFAULT_VALUE_FOR_COUNT);

                if (cardRequestTOs == null || cardRequestTOs.isEmpty()) {
                    returnFlag = false;
                }

                if (returnFlag) {
                    List<CardRequestTO> cardRequestTOList = loadCardRequestListWithAllAttributes(cardRequestTOs);
                    CertificateTO certificateTO = findCertificateByUsage(CertificateUsage.CMS_SIGN);
                    String logForEachRequest = "";

                    for (CardRequestTO cardRequestTO : cardRequestTOList) {
                        inputIdsForLog.add(cardRequestTO.getId());
                    }
                    additionalData = "{input:" + EmsUtil.toJSON(inputIdsForLog) + "}";

                    for (CardRequestTO cardRequestTO : cardRequestTOList) {
                        logForEachRequest = sendIssuanceRequest(cardRequestTO, certificateTO);
                        if (EmsUtil.checkString(logForEachRequest)) {
                            exceptedAdditionalData += ",{" + EmsUtil.toJSON("id:" + cardRequestTO.getId() + "," + logForEachRequest) + "}";
                        } else {
                            cardRequestIdsForLog.add(cardRequestTO.getId());
                        }
                    }
                    additionalData +=
                            "output:{" + "{" + "successIds:" + cardRequestIdsForLog + "}" +
                                    "," + "{" + "failureIds:" +
                                    (EmsUtil.checkString(exceptedAdditionalData) ? exceptedAdditionalData.substring(1) : exceptedAdditionalData) + "}" +
                                    "}";
                }
            } catch (DataException e) {
                returnFlag = false;
                exceptionFlag = true;
                exceptionMessage = e.getMessage();
                logger.error(e.getExceptionCode(), e.getMessage(), e);
                cmsLogger.error(e.getExceptionCode(), e.getMessage(), e);
            }
        } catch (BaseException e) {
            exceptionFlag = true;
            exceptionMessage = e.getMessage();
            logger.error(e.getExceptionCode(), e.getMessage(), e);
            cmsLogger.error(e.getExceptionCode(), e.getMessage(), e);
        } catch (Exception e) {
            exceptionFlag = true;
            exceptionMessage = e.getMessage();
            logger.error(BizExceptionCode.CRI_009, BizExceptionCode.GLB_008_MSG, e);
            cmsLogger.error(BizExceptionCode.CRI_009, BizExceptionCode.GLB_008_MSG, e);
        }
        if (exceptionFlag) {
            additionalData += EmsUtil.toJSON(exceptionMessage);
        }
        if (EmsUtil.checkString(additionalData)) {
            createBusinessLog(BusinessLogAction.ISSUE, BusinessLogEntity.REQUEST, "System", additionalData, exceptionFlag);
        }

        return returnFlag;
    }

    @Override
    public Long getRequestsCountForIssue() throws BaseException {
        return getCardRequestDAO().getRequestsCountForIssue();
    }

    @Override
    public List<Long> getRequestIdsForIssue(Integer fetchLimit) throws BaseException {
        return getCardRequestDAO().getRequestIdsForIssue(fetchLimit);
    }

    @Override
    public void prepareDataAndSendIssuanceRequest(Integer from,
                                                  CertificateTO certificateTO) throws BaseException {
        String additionalData = "";
        boolean exceptionFlag = false;
        try {
            CardRequestTO cardRequestTO = getCardRequestDAO().findRequestForIssueCard(from);

            additionalData = "{input:" + EmsUtil.toJSON(cardRequestTO.getId()) + "}";
            String returnedException = sendIssuanceRequest(cardRequestTO, certificateTO);
            if (EmsUtil.checkString(returnedException)) {
                exceptionFlag = true;
                additionalData += ",{output:{" + returnedException + "}}";
            } else {
                additionalData += ",{output:{ Successful }}";
            }
            createBusinessLog(BusinessLogAction.ISSUE, BusinessLogEntity.REQUEST, "System", additionalData, exceptionFlag);

        } catch (Exception e) {
            exceptionFlag = true;
            try {
                additionalData += e.getMessage();
                createBusinessLog(BusinessLogAction.ISSUE, BusinessLogEntity.REQUEST, "System", additionalData, exceptionFlag);
            } catch (BaseException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new ServiceException(BizExceptionCode.CRI_018, BizExceptionCode.GLB_008_MSG, e);
            }
        }
    }

    @Override
    public void prepareDataAndSendIssuanceRequestById(Long requestId, CertificateTO certificateTO) throws BaseException {
        String additionalData = "";
        boolean exceptionFlag = false;
        try {
            CardRequestTO cardRequestTO = getCardRequestDAO().find(CardRequestTO.class, requestId);

            additionalData = "{input:" + EmsUtil.toJSON(cardRequestTO.getId()) + "}";
            String returnedException = sendIssuanceRequest(cardRequestTO, certificateTO);
            if (EmsUtil.checkString(returnedException)) {
                exceptionFlag = true;
                additionalData += ",{output:{" + returnedException + "}}";
            } else {
                additionalData += ",{output:{ Successful }}";
            }
            createBusinessLog(BusinessLogAction.ISSUE, BusinessLogEntity.REQUEST, "System", additionalData, exceptionFlag);

        } catch (Exception e) {
            exceptionFlag = true;
            try {
                additionalData += e.getMessage();
                createBusinessLog(BusinessLogAction.ISSUE, BusinessLogEntity.REQUEST, "System", additionalData, exceptionFlag);
            } catch (BaseException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new ServiceException(BizExceptionCode.CRI_018, BizExceptionCode.GLB_008_MSG, e);
            }
        }
    }


    @Override
    @Asynchronous
    public Future<String> prepareDataAndSendIssuanceRequestByIdAsync(Long requestId, CertificateTO certificateTO) throws BaseException {
        String additionalData = "";
        boolean exceptionFlag = false;
        try {
            CardRequestTO cardRequestTO = getCardRequestDAO().find(CardRequestTO.class, requestId);

            additionalData = "{input:" + EmsUtil.toJSON(cardRequestTO.getId()) + "}";
            String returnedException = sendIssuanceRequest(cardRequestTO, certificateTO);
            if (EmsUtil.checkString(returnedException)) {
                exceptionFlag = true;
                additionalData += ",{output:{" + returnedException + "}}";
            } else {
                additionalData += ",{output:{ Successful }}";
            }
            createBusinessLog(BusinessLogAction.ISSUE, BusinessLogEntity.REQUEST, "System", additionalData, exceptionFlag);

        } catch (Exception e) {
            exceptionFlag = true;
            try {
                additionalData += e.getMessage();
                createBusinessLog(BusinessLogAction.ISSUE, BusinessLogEntity.REQUEST, "System", additionalData, exceptionFlag);
            } catch (BaseException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new ServiceException(BizExceptionCode.CRI_018, BizExceptionCode.GLB_008_MSG, e);
            }
        }
        return new AsyncResult<String>("");
    }
}
