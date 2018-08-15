package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.ValidationException;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactory;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.IMSService;
import com.gam.nocr.ems.config.*;
import com.gam.nocr.ems.data.dao.*;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.vol.*;
import com.gam.nocr.ems.data.domain.ws.ImsCitizenInfoRequestWTO;
import com.gam.nocr.ems.data.domain.ws.ImsCitizenInfoResponseWTO;
import com.gam.nocr.ems.data.enums.*;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.TranslateUtil;
import gampooya.tools.date.DateFormatException;
import gampooya.tools.date.DateUtil;
import gampooya.tools.vlp.ValueListHandler;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.*;
import java.util.*;
import java.util.concurrent.Future;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "IMSManagementService")
@Local(IMSManagementServiceLocal.class)
@Remote(IMSManagementServiceRemote.class)
public class IMSManagementServiceImpl extends EMSAbstractService implements
        IMSManagementServiceLocal, IMSManagementServiceRemote {

    @Resource
    SessionContext sessionContext;

    static int asyncCounter;

    private static final Logger logger = BaseLog
            .getLogger(IMSManagementServiceImpl.class);
    private static final String IMS_ENQUIRY_VTO_LIST = "imsEnquiryVTOList";
    private static final Logger imsLogger = BaseLog.getLogger("ImsLogger");
    private static final Logger imsVerificationInDelivery = BaseLog.getLogger("imsVerificationInDelivery");

    private static final Logger estelam2Logger = BaseLog
            .getLogger("Estelam2Logger");

    /**
     * IMS Error codes
     */
    private static final String IMS_UPDT_000004 = "UPDT-000004";
    private static final String IMS_UPDT_000009 = "UPDT-000009";
    private static final String IMS_UPDT_000010 = "UPDT-000010";
    private static final String IMS_UPDT_000016 = "UPDT-000016";
    private static final String IMS_UPDT_000017 = "UPDT-000017";
    private static final String IMS_UPDT_000018 = "UPDT-000018";
    private static final String IMS_UPDT_000019 = "UPDT-000019";
    private static final String IMS_UPDT_000020 = "UPDT-000020";
    private static final String IMS_UPDT_000021 = "UPDT-000021";
    private static final String IMS_UPDT_000022 = "UPDT-000022";

    private static final String DEFAULT_IMS_SMS_ENABLE = "1";

    /**
     * IMSProvider types
     */
    private static final int OFFLINE_ENQUIRY = 1;
    private static final int ONLINE_ENQUIRY = 2;
    private static final int UPDATE_CITIZENS_INFO = 3;
    private static final int FETCH_CITIZEN_INFO = 4;
    public static final int SET_CITIZEN_CARD_REQUESTED = 5;
    public static final int SET_CITIZEN_CARD_DELIVERED = 6;

    /**
     * =============== Getter for DAOs ===============
     */

    /**
     * getCardRequestDAO
     *
     * @return an instance of type CardRequestDAO
     */
    private CardRequestDAO getCardRequestDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        CardRequestDAO cardRequestDAO = null;
        try {
            cardRequestDAO = factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.MMS_001,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_CARD_REQUEST.split(","));
        }
        return cardRequestDAO;
    }

    private Estelam2FailureLogDAO getEstelam2LogDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        Estelam2FailureLogDAO estelam2LogDAO = null;
        try {
            estelam2LogDAO = factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_ESTELAM2_FAILURE_LOG));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.MMS_057,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_ESTELAM2_FAILURE_LOG.split(","));
        }
        return estelam2LogDAO;
    }

    /**
     * getCardRequestHistoryDAO
     */
    private CardRequestHistoryDAO getCardRequestHistoryDAO()
            throws BaseException {
        DAOFactory daoFactory = DAOFactoryProvider.getDAOFactory();
        CardRequestHistoryDAO cardRequestHistoryDAO;
        try {
            cardRequestHistoryDAO = daoFactory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST_HISTORY));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.MMS_002,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_CARD_REQUEST_HISTORY.split(","));
        }
        return cardRequestHistoryDAO;
    }

    private SpouseDAO getSpousDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        SpouseDAO spouseDAO = null;
        try {
            spouseDAO = factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_SPOUSE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.MMS_027,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_SPOUSE.split(","));
        }
        return spouseDAO;
    }


    private ChildDAO getChildDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        ChildDAO childDAO = null;
        try {
            childDAO = factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_CHILD));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.MMS_027,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_CHILD.split(","));
        }
        return childDAO;
    }

    /**
     * getCitizenDAO
     *
     * @return an instance of type CitizenDAO
     */
    private CitizenDAO getCitizenDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        CitizenDAO citizenDAO = null;
        try {
            citizenDAO = factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_CITIZEN));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.MMS_027,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_CITIZEN.split(","));
        }
        return citizenDAO;
    }

    /**
     * getCitizenInfoDAO
     *
     * @return an instance of type CitizenInfoDAO
     */
    private CitizenInfoDAO getCitizenInfoDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        CitizenInfoDAO citizenInfoDAO;
        try {
            citizenInfoDAO = factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_CITIZEN_INFO));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.MMS_004,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_CITIZEN_INFO.split(","));
        }
        return citizenInfoDAO;
    }

    // Anbari:IMS
    private BiometricDAO getBiometricDAO() throws BaseException {

        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        BiometricDAO biometricDAO;
        try {
            biometricDAO = factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_BIOMETRIC));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_004,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_BIOMETRIC.split(","));
        }
        return biometricDAO;

    }

    // Anbari:IMS
    private DocumentDAO getDocumentDAO() throws BaseException {

        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        DocumentDAO documentDAO;
        try {
            documentDAO = factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_DOCUMENT));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RSI_005,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_DOCUMENT.split(","));
        }
        return documentDAO;

    }

    /**
     * =================== Getter for Services ===================
     */

    /**
     * getIMSService
     *
     * @return an instance of type {@link IMSService}
     */
    private IMSService getIMSService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        IMSService imsService = null;
        try {
            imsService = serviceFactory.getService(EMSLogicalNames
                    .getExternalServiceJNDIName(EMSLogicalNames.SRV_IMS), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.MMS_003,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_IMS.split(","));
        }
        imsService.setUserProfileTO(getUserProfileTO());
        return imsService;
    }

    /**
     * The method createNewCardRequestHistory is used to create a new instance
     * of type {@link CardRequestHistoryTO} and save it
     *
     * @param cardRequestTO is an instance of type
     *                      {@link com.gam.nocr.ems.data.domain.CardRequestTO}
     * @param reqId         is an instance of type {@link String}
     * @param result        is an instance of type {@link String}
     * @param action        The action that has to be used for new history record
     *                      {@link CardRequestHistoryAction}
     * @return an instance of type {@link CardRequestHistoryTO}
     */
    private CardRequestHistoryTO createNewCardRequestHistory(
            CardRequestTO cardRequestTO, String reqId, String result,
            CardRequestHistoryAction action) throws BaseException {
        try {
            CardRequestHistoryTO cardRequestHistoryTO = new CardRequestHistoryTO();

            cardRequestHistoryTO.setRequestID(reqId);
            cardRequestHistoryTO.setCardRequest(cardRequestTO);
            cardRequestHistoryTO.setSystemID(SystemId.IMS);
            cardRequestHistoryTO.setDate(new Date());
            if (EmsUtil.checkString(result)) {
                cardRequestHistoryTO.setResult(result.length() <= 250 ? result
                        : result.substring(0, 250));
            }
            cardRequestHistoryTO.setCardRequestHistoryAction(action);
            getCardRequestHistoryDAO().create(cardRequestHistoryTO);
            return cardRequestHistoryTO;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.MMS_005,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * The method getBatchEnquiryListSizeByProfileManager is used to get the
     * batch enquiry list size from config
     *
     * @return an instance of type {@link Integer} which represents the size of
     * list
     * @throws BaseException
     */
    private Integer getBatchEnquiryListSizeByProfileManager()
            throws BaseException {
        Integer listSize = null;
        Integer DEFAULT_LIST_SIZE = 50;
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            listSize = Integer.parseInt((String) pm.getProfile(
                    ProfileKeyName.KEY_IMS_OFFLINE_ENQUIRY_SIZE, true, null,
                    null));
        } catch (Exception e) {
            logger.warn(BizExceptionCode.MMS_009, BizExceptionCode.MMS_009_MSG);
            imsLogger.warn(BizExceptionCode.MMS_009,
                    BizExceptionCode.MMS_009_MSG);
        }
        if (listSize == null) {
            listSize = DEFAULT_LIST_SIZE;
        }
        return listSize;
    }

    /**
     * The method getBatchRequestIdFromSequence is used to get the requestId for
     * the request of type batch from sequence
     *
     * @return an instance of type {@link String} which represents the request
     * id
     * @throws BaseException
     */
    private String getBatchRequestIdFromSequence() throws BaseException {
        Long requestId = getCardRequestHistoryDAO().getRequestIdFromSequence(
                SequenceName.SEQ_EMS_REQUEST_ID_IMS_BATCH_ENQUIRY);
        return requestId.toString();
    }

    /**
     * The method
     * getBatchEnquiryParametersForIMSPendingRequestsFromProfileManager is used
     * to get the required batch enquiry parameters for IMS_PENDING requests
     *
     * @return an array of type {@link String} which represents the required
     * parameters
     * @throws BaseException
     */
    @Override
    public String[] getBatchEnquiryParametersForIMSPendingRequestsFromProfileManager()
            throws BaseException {
        /**
         * The type of time intervals can be one of the markers bellow: Day
         * marker : DAY Hour marker : HOUR Minute marker: MINUTE
         */
        String DEFAULT_INTERVAL_TYPE = "DAY";
        String DEFAULT_INTERVAL = "2";
        String[] parameters = new String[2];

        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            parameters[0] = (String) pm
                    .getProfile(
                            ProfileKeyName.KEY_BATCH_ENQUIRY_INTERVAL_FOR_IMS_PENDING_REQUESTS,
                            true, null, null);
            parameters[1] = (String) pm
                    .getProfile(
                            ProfileKeyName.KEY_BATCH_ENQUIRY_INTERVAL_TYPE_FOR_IMS_PENDING_REQUESTS,
                            true, null, null);
        } catch (Exception e) {
            logger.warn(BizExceptionCode.MMS_025, BizExceptionCode.MMS_025_MSG);
            imsLogger.warn(BizExceptionCode.MMS_025,
                    BizExceptionCode.MMS_025_MSG);
        }
        if (parameters[0] == null) {
            parameters[0] = DEFAULT_INTERVAL;
        }
        if (parameters[1] == null) {
            parameters[1] = DEFAULT_INTERVAL_TYPE;
        }
        return parameters;
    }

    /**
     * The method updateCardRequestsByState is used to update the state of
     * CardRequests in spite of the input parameters
     *
     * @param cardRequestIds   a list of type {@link Long} which represents the ids that
     *                         belong to the instances of type {@link CardRequestTO}
     * @param cardRequestState an instance of type {@link CardRequestState}
     * @throws BaseException
     */
    private void updateCardRequestsByState(List<Long> cardRequestIds,
                                           CardRequestState cardRequestState) throws BaseException {
        if (cardRequestIds != null && !cardRequestIds.isEmpty()) {
            getCardRequestDAO().updateCardRequestsState(cardRequestIds,
                    cardRequestState);
        }
    }

    /**
     * The method createCardRequestHistory is used to create instances of type
     * {@link CardRequestHistoryTO} in spite of the input parameters
     *
     * @param cardRequestIds   a list of type {@link Long} which represents the ids that
     *                         belong to the instances of type {@link CardRequestTO}
     * @param messageRequestId an instance of type {@link String} which represents the
     *                         requestId of the request
     * @param result           an instance of type {@link String}
     * @throws BaseException
     */
    private void createCardRequestHistory(List<Long> cardRequestIds,
                                          String messageRequestId, String result,
                                          CardRequestHistoryAction action) throws BaseException {
        if (cardRequestIds != null && !cardRequestIds.isEmpty()) {
            for (Long cardRequestId : cardRequestIds) {
                CardRequestHistoryTO cardRequestHistoryTO = new CardRequestHistoryTO();
                CardRequestTO cardRequestTO = new CardRequestTO();
                cardRequestTO.setId(cardRequestId);
                cardRequestHistoryTO.setDate(new Date());
                cardRequestHistoryTO.setCardRequest(cardRequestTO);
                cardRequestHistoryTO.setRequestID(messageRequestId);
                if (EmsUtil.checkString(result)) {
                    cardRequestHistoryTO
                            .setResult(result.length() > 250 ? result
                                    .substring(0, 250) : result);
                }
                cardRequestHistoryTO.setSystemID(SystemId.IMS);
                cardRequestHistoryTO.setCardRequestHistoryAction(action);
                getCardRequestHistoryDAO().create(cardRequestHistoryTO);
                logger.info(
                        "\nThe new request history has been created. details : (cardRequestId: {}, messageRequestId : {}, setCardRequestHistoryAction :{} )",
                        new Object[]{cardRequestId, messageRequestId, action});
                imsLogger
                        .info("\nThe new request history has been created. details : (cardRequestId: {}, messageRequestId : {}, setCardRequestHistoryAction :{} )",
                                new Object[]{cardRequestId, messageRequestId,
                                        action});
            }
        }
    }

    private void createCardRequestHistory(Long requestId, String result,
                                          CardRequestHistoryAction action) throws BaseException {
        CardRequestHistoryTO cardRequestHistoryTO = new CardRequestHistoryTO();
        CardRequestTO cardRequestTO = new CardRequestTO();
        cardRequestTO.setId((Long) requestId);
        cardRequestHistoryTO.setDate(new Date());
        cardRequestHistoryTO.setCardRequest(cardRequestTO);
        if (EmsUtil.checkString(result)) {
            cardRequestHistoryTO.setResult(result.length() > 250 ? result
                    .substring(0, 250) : result);
        }
        cardRequestHistoryTO.setSystemID(SystemId.IMS);
        cardRequestHistoryTO.setCardRequestHistoryAction(action);
        getCardRequestHistoryDAO().create(cardRequestHistoryTO);
    }

    /**
     * The method updateCardRequestsAfterGettingBatchEnquiryResult is used to
     * update the state and metadata of the cardRequests which belong to the
     * result of IMSBatchEnquiry
     *
     * @param resultMap an instance of type {@link HashMap<Long, HashMap<String,
     *                  String>>}, which represents the result of IMS Batch enquiry by
     *                  using the pattern HashMap<[CardRequestTO id],
     *                  HashMap<[citizen, or father, or mother, or spouse, or child],
     *                  the comma separator string >>
     * @throws BaseException
     */
    private void updateCardRequestsAfterGettingBatchEnquiryResult(
            HashMap<Long, HashMap<String, String>> resultMap)
            throws BaseException {
        if (resultMap != null && !resultMap.isEmpty()) {
            Object[] requestIds = resultMap.keySet().toArray();
            if (requestIds != null && requestIds.length != 0) {
                Object[] innerMapKeys;
                for (Object requestId : requestIds) {
                    StringBuilder allMetaData = new StringBuilder();
                    HashMap<String, String> citizenInfoMap = resultMap
                            .get(requestId);

                    if (citizenInfoMap != null && !citizenInfoMap.isEmpty()) {

                        innerMapKeys = citizenInfoMap.keySet().toArray();
                        if (innerMapKeys != null && innerMapKeys.length != 0) {
                            for (Object innerMapKey : innerMapKeys) {

                                logger.info("\nThe citizen info for '"
                                        + innerMapKey + "' is : "
                                        + citizenInfoMap.get(innerMapKey));
                                imsLogger.info("\nThe citizen info for '"
                                        + innerMapKey + "' is : "
                                        + citizenInfoMap.get(innerMapKey));
                                String metaData = prepareMetaDataForRequest(
                                        (Long) requestId, innerMapKey,
                                        citizenInfoMap.get(innerMapKey));
                                logger.info("\nThe metadata for '"
                                        + innerMapKey + "' is : " + metaData);
                                imsLogger.info("\nThe metadata for '"
                                        + innerMapKey + "' is : " + metaData);
                                if (metaData != null) {
                                    allMetaData.append(metaData);
                                }
                            }
                        }
                    }

                    logger.info("\nThe all metadata for the requestId '"
                            + requestId + "' is : " + allMetaData);
                    imsLogger.info("\nThe all metadata for the requestId '"
                            + requestId + "' is : " + allMetaData);
                    int updateResult;
                    if (!checkIMSVerificationByMetadata((Long) requestId,
                            allMetaData.toString())) {
                        CardRequestTO cardRequestTO = new CardRequestTO();
                        cardRequestTO.setId((Long) requestId);
                        cardRequestTO.setMetadata(allMetaData.toString());
                        cardRequestTO
                                .setState(CardRequestState.NOT_VERIFIED_BY_IMS);

                        updateResult = getCardRequestDAO().updateRequest(
                                cardRequestTO);
                        if (updateResult != 0) {
                            createCardRequestHistory(
                                    (Long) requestId,
                                    CardRequestState.NOT_VERIFIED_BY_IMS.name(),
                                    CardRequestHistoryAction.NOT_VERIFIED_BY_IMS);
                        }
                    } else {
                        Set<String> remainedCitizenInfoSet = omitNotVerifiedMembersFromCitizenInfo(
                                citizenInfoMap.keySet(), allMetaData.toString());
                        String onlineEnquiryMetadata = fetchIMSDataForBatchEnquiry(remainedCitizenInfoSet
                                .toArray());
                        logger.info("\nThe onlineEnquiryMetadata for the requestId '"
                                + requestId + "' is : " + onlineEnquiryMetadata);
                        imsLogger
                                .info("\nThe onlineEnquiryMetadata for the requestId '"
                                        + requestId
                                        + "' is : "
                                        + onlineEnquiryMetadata);

                        if (!checkIMSVerificationByMetadata((Long) requestId,
                                onlineEnquiryMetadata)) {
                            CardRequestTO cardRequestTO = new CardRequestTO();
                            cardRequestTO.setId((Long) requestId);
                            cardRequestTO.setMetadata(allMetaData
                                    + onlineEnquiryMetadata);
                            cardRequestTO
                                    .setState(CardRequestState.NOT_VERIFIED_BY_IMS);

                            updateResult = getCardRequestDAO().updateRequest(
                                    cardRequestTO);
                            if (updateResult != 0) {
                                createCardRequestHistory(
                                        (Long) requestId,
                                        CardRequestState.NOT_VERIFIED_BY_IMS
                                                .name(),
                                        CardRequestHistoryAction.NOT_VERIFIED_BY_IMS);
                            }

                        } else {// Means that request has been verified by IMS
                            /*
                             * Fetch card request by id to update verified
							 * request
							 */
                            CardRequestTO cardRequestTO = getCardRequestDAO()
                                    .find(CardRequestTO.class, (Long) requestId);
                            if (cardRequestTO == null) {
                                throw new ServiceException(
                                        BizExceptionCode.MMS_052,
                                        BizExceptionCode.MMS_052_MSG);
                            }

                            cardRequestTO.setMetadata(allMetaData
                                    + onlineEnquiryMetadata);

							/*
                             * Check whether or not the verified request is
							 * MES-based request
							 */
                            if (CardRequestOrigin.M.equals(cardRequestTO
                                    .getOrigin())) {
								/*
								 * if request was MES-based, it is compulsory to
								 * check biometric flag in table of
								 * emst_card_request to be confident about
								 * updating request to an adequate state
								 */
                                Integer biometricFlag = getCardRequestDAO()
                                        .fetchBiometricFlag(
                                                cardRequestTO.getId());
								/*
								 * if flag equals to 7, means biometric info of
								 * request has been set completely, so the state
								 * can be changed to APPROVED; otherwise,
								 * request state should be backed to
								 * DOCUMENT_AUTHENTICATED in order to be ready
								 * for preparing biometric info
								 */
                                if (biometricFlag != null && biometricFlag == 7) {
                                    cardRequestTO
                                            .setState(CardRequestState.APPROVED);
                                } else {
                                    cardRequestTO
                                            .setState(CardRequestState.DOCUMENT_AUTHENTICATED);
                                }
                            } else if (CardRequestOrigin.P.equals(cardRequestTO
                                    .getOrigin())) {
                                cardRequestTO
                                        .setState(CardRequestState.VERIFIED_IMS);
                            }
                            createCardRequestHistory((Long) requestId,
                                    cardRequestTO.getState().name(),
                                    CardRequestHistoryAction.VERIFIED_IMS);
                        }
                    }

                }
            }
        }
    }

    /**
     * The method checkIMSVerificationByMetadata, is used to understand whether
     * the request, verified by AFIS or not.
     *
     * @param requestId is an instance of type {@link Long}, which represents the id
     *                  of a specified request
     * @param metadata  is an instance of type {@link String}
     * @return true or false
     * @throws BaseException
     */
    private boolean checkIMSVerificationByMetadata(Long requestId,
                                                   String metadata) throws BaseException {
        Boolean returnValue = null;
        if (EmsUtil.checkString(metadata)) {
            if (metadata.contains("deny")
                    || metadata.equals(requestId.toString())) {
                returnValue = false;
            } else {
                String[] items = metadata.split("-");
                for (String item : items) {
                    if (item.equals(requestId.toString())) {
                        returnValue = false;
                        break;
                    }
                }
            }
        }
        if (returnValue == null) {
            returnValue = true;
        }
        return returnValue;
    }

    private Set<String> omitNotVerifiedMembersFromCitizenInfo(
            Set<String> citizenInfoSet, String metadata) throws BaseException {
        String[] items;
        try {
            items = metadata.split("-");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            imsLogger.error(e.getMessage(), e);
            items = new String[1];
        }

        for (String item : items) {
            if (citizenInfoSet.contains(item)) {
                citizenInfoSet.remove(item);
            }
        }
        return citizenInfoSet;
    }

    /**
     * The method fetchIMSDataForBatchEnquiry is used to fetch the correct
     * values of citizen information from 'IMS'
     *
     * @param keys is an array of type {@link Object}, which represents the keys
     *             for getting the data from 'IMS'
     * @return an instance of type {@link String}, which represents the not
     * verified members on the eyes of IMS
     * @throws BaseException
     */

    private String fetchIMSDataForBatchEnquiry(Object[] keys)
            throws BaseException {
        StringBuilder metadata = new StringBuilder();
        List<PersonEnquiryVTO> personEnquiryVTOs = new ArrayList<PersonEnquiryVTO>();
        CitizenTO ctz;
        Long reqId = null;
        boolean fatherFlag = false;
        boolean motherFlag = false;
        boolean spouseFlag = false;
        boolean childrenFlag = false;
        for (Object key : keys) {
            if (key.toString().contains("f")) {
                fatherFlag = true;
            } else if (key.toString().contains("m")) {
                motherFlag = true;
            } else if (key.toString().contains("s")) {
                spouseFlag = true;
            } else if (key.toString().contains("c")) {
                childrenFlag = true;
            }
            try {
                reqId = Long.parseLong(key.toString());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                imsLogger.error(e.getMessage(), e);
                // Do nothing
            }
        }

        if (reqId != null) {
            ctz = getCitizenDAO().findByRequestId(reqId);
            CitizenInfoTO czi = ctz.getCitizenInfo();

            // Enquiry for CITIZEN
            personEnquiryVTOs.add(new PersonEnquiryVTO(ctz.getNationalID(), // national
                    // id
                    ctz.getFirstNamePersian(), // first name
                    ctz.getSurnamePersian(), // surname
                    czi.getFatherFirstNamePersian(), // father first name
                    null, // birth certificate series
                    ConstValues.DEFAULT_CERT_SERIAL, // birth certificate serial
                    czi.getBirthCertificateId(), // birth certificate id
                    czi.getBirthDateSolar(), // birth date
                    czi.getGender())); // gender

            // Enquiry for citizen FATHER
            if (fatherFlag) {
                personEnquiryVTOs.add(new PersonEnquiryVTO(czi
                        .getFatherNationalID(), // national id
                        czi.getFatherFirstNamePersian(), // first name
                        czi.getFatherSurname(), // surname
                        czi.getFatherFatherName(), // father first name
                        null, // birth certificate seri
                        ConstValues.DEFAULT_CERT_SERIAL, // birth certificate
                        // serial
                        czi.getFatherBirthCertificateId(), // birth certificate
                        // id
                        DateUtil.convert(czi.getFatherBirthDateSolar(),
                                DateUtil.JALALI), Gender.M)); // birth date
            }

            // Enquiry for citizen MOTHER
            if (motherFlag) {
                personEnquiryVTOs.add(new PersonEnquiryVTO(czi
                        .getMotherNationalID(), // national id
                        czi.getMotherFirstNamePersian(), // first name
                        czi.getMotherSurname(), // surname
                        czi.getMotherFatherName(), // father first name
                        null, // birth certificate seri
                        ConstValues.DEFAULT_CERT_SERIAL, // birth certificate
                        // serial
                        czi.getMotherBirthCertificateId(), // birth certificate
                        // id
                        DateUtil.convert(czi.getMotherBirthDateSolar(),
                                DateUtil.JALALI), Gender.F)); // birth date
            }

            if (spouseFlag) {
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
                    personEnquiryVTOs.add(new PersonEnquiryVTO(sps
                            .getSpouseNationalID(), // national
                            // id
                            sps.getSpouseFirstNamePersian(), // first name
                            sps.getSpouseSurnamePersian(), // surname
                            sps.getSpouseFatherName(), // father first name
                            null, // birth certificate seri
                            ConstValues.DEFAULT_CERT_SERIAL, // birth
                            // certificate
                            // serial
                            sps.getSpouseBirthCertificateId(), // birth
                            // certificate
                            // id
                            DateUtil.convert(sps.getSpouseBirthDate(),
                                    DateUtil.JALALI), // birth date
                            spouseGender)); // Gender
                }
            }

            if (childrenFlag) {
                for (ChildTO chi : czi.getChildren()) {
                    // Enquiry for citizen CHILD
                    personEnquiryVTOs.add(new PersonEnquiryVTO(chi
                            .getChildNationalID(), // national
                            // id
                            chi.getChildFirstNamePersian(), // first name
                            null, // surname
                            // needs to be added later,
                            // currently no surname is
                            // provided for child
                            chi.getChildFatherName(), // father first name
                            null, // birth certificate seri
                            ConstValues.DEFAULT_CERT_SERIAL, // birth
                            // certificate
                            // serial
                            chi.getChildBirthCertificateId(), // birth
                            // certificate
                            // id
                            DateUtil.convert(chi.getChildBirthDateSolar(),
                                    DateUtil.JALALI), // birth date
                            chi.getChildGender())); // Gender
                }
            }
            Map<String, PersonEnquiryVTO> returnMap = getIMSService()
                    .fetchDataByOnlineEnquiry(
                            personEnquiryVTOs.toArray(new PersonEnquiryVTO[personEnquiryVTOs
                                    .size()]));
            logger.info("\n Updating citizen data with nationalId ' "
                    + ctz.getNationalID()
                    + "' in fetchIMSDataForBatchEnquiry method ---------------------------------\n");
            imsLogger
                    .info("\n Updating citizen data with nationalId ' "
                            + ctz.getNationalID()
                            + "' in fetchIMSDataForBatchEnquiry method ---------------------------------\n");
            PersonEnquiryVTO personEnquiryVTO = null;
            for (Object key : keys) {
                String strKey = key.toString();
                String nId = null;
                if (strKey.contains("m")) {
                    nId = strKey.split("m")[1];
                    personEnquiryVTO = returnMap.get(nId);
                    if (EmsUtil.checkString(personEnquiryVTO.getMetadata())) {
                        metadata.append(strKey).append("-");

                    } else {
                        czi.setMotherFirstNamePersian(personEnquiryVTO
                                .getFirstName());
                        czi.setMotherSurname(personEnquiryVTO.getLastName());
                        czi.setMotherBirthCertificateId(personEnquiryVTO
                                .getBirthCertificateId());
                        czi.setMotherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                        try {
                            czi.setMotherBirthDateSolar(DateUtil.convert(
                                    personEnquiryVTO.getSolarBirthDate(),
                                    DateUtil.JALALI));
                        } catch (DateFormatException e) {
                            throw new ServiceException(
                                    BizExceptionCode.MMS_029,
                                    BizExceptionCode.MMS_029_MSG, e);
                        }
                        czi.setMotherFatherName(personEnquiryVTO
                                .getFatherName());

                        logger.info("\nMother info with national id : "
                                + czi.getMotherNationalID() + "\n");
                        imsLogger.info("\nMother info with national id : "
                                + czi.getMotherNationalID() + "\n");
                        logger.info("\nMother FirstName : "
                                + czi.getMotherFirstNamePersian() + "\n");
                        imsLogger.info("\nMother FirstName : "
                                + czi.getMotherFirstNamePersian() + "\n");
                        logger.info("\nMother LastName : "
                                + czi.getMotherSurname() + "\n");
                        imsLogger.info("\nMother LastName : "
                                + czi.getMotherSurname() + "\n");
                        logger.info("\nMother BirthCertId : "
                                + czi.getMotherBirthCertificateId() + "\n");
                        imsLogger.info("\nMother BirthCertId : "
                                + czi.getMotherBirthCertificateId() + "\n");
                        logger.info("\nMother BirthCertSerial : "
                                + czi.getMotherBirthCertificateSeries() + "\n");
                        imsLogger.info("\nMother BirthCertSerial : "
                                + czi.getMotherBirthCertificateSeries() + "\n");
                        logger.info("\nMother BirthDateSolar : "
                                + czi.getMotherBirthDateSolar() + "\n");
                        imsLogger.info("\nMother BirthDateSolar : "
                                + czi.getMotherBirthDateSolar() + "\n");
                    }

                } else if (strKey.contains("f")) {
                    nId = strKey.split("f")[1];
                    personEnquiryVTO = returnMap.get(nId);
                    if (EmsUtil.checkString(personEnquiryVTO.getMetadata())) {
                        metadata.append(strKey).append("-");
                    } else {
                        czi.setFatherSurname(personEnquiryVTO.getLastName());
                        czi.setFatherBirthCertificateId(personEnquiryVTO
                                .getBirthCertificateId());
                        czi.setFatherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                        try {
                            czi.setFatherBirthDateSolar(DateUtil.convert(
                                    personEnquiryVTO.getSolarBirthDate(),
                                    DateUtil.JALALI));
                        } catch (DateFormatException e) {
                            throw new ServiceException(
                                    BizExceptionCode.MMS_029,
                                    BizExceptionCode.MMS_029_MSG, e);
                        }
                        czi.setFatherFatherName(personEnquiryVTO
                                .getFatherName());

                        logger.info("\nFather info with national id : "
                                + czi.getFatherNationalID() + "\n");
                        imsLogger.info("\nFather info with national id : "
                                + czi.getFatherNationalID() + "\n");
                        logger.info("\nFather FirstName : "
                                + czi.getFatherFirstNamePersian() + "\n");
                        imsLogger.info("\nFather FirstName : "
                                + czi.getFatherFirstNamePersian() + "\n");
                        logger.info("\nFather LastName : "
                                + czi.getFatherSurname() + "\n");
                        imsLogger.info("\nFather LastName : "
                                + czi.getFatherSurname() + "\n");
                        logger.info("\nFather BirthCertId : "
                                + czi.getFatherBirthCertificateId() + "\n");
                        imsLogger.info("\nFather BirthCertId : "
                                + czi.getFatherBirthCertificateId() + "\n");
                        logger.info("\nFather BirthCertSerial : "
                                + czi.getFatherBirthCertificateSeries() + "\n");
                        imsLogger.info("\nFather BirthCertSerial : "
                                + czi.getFatherBirthCertificateSeries() + "\n");
                        logger.info("\nFather BirthDateSolar : "
                                + czi.getFatherBirthDateSolar() + "\n");
                        imsLogger.info("\nFather BirthDateSolar : "
                                + czi.getFatherBirthDateSolar() + "\n");
                    }
                } else if (strKey.contains("s")) {
                    nId = strKey.split("s")[1];
                    personEnquiryVTO = returnMap.get(nId);
                    if (EmsUtil.checkString(personEnquiryVTO.getMetadata())) {
                        metadata.append(strKey).append("-");
                    } else {
                        for (SpouseTO spouseTO : czi.getSpouses()) {
                            if (spouseTO.getSpouseNationalID().equals(nId)) {

                                spouseTO.setSpouseFirstNamePersian(personEnquiryVTO
                                        .getFirstName());
                                spouseTO.setSpouseSurnamePersian(personEnquiryVTO
                                        .getLastName());
                                spouseTO.setSpouseBirthCertificateId(personEnquiryVTO
                                        .getBirthCertificateId());
                                // spouseTO.setSpouseBirthCertificateSeries(personEnquiryVTO.getBirthCertificateSerial());
                                spouseTO.setSpouseBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                                spouseTO.setSpouseFatherName(personEnquiryVTO
                                        .getFatherName());
                                try {
                                    spouseTO.setSpouseBirthDate(DateUtil
                                            .convert(personEnquiryVTO
                                                            .getSolarBirthDate(),
                                                    DateUtil.JALALI));
                                } catch (DateFormatException e) {
                                    throw new ServiceException(
                                            BizExceptionCode.MMS_030,
                                            BizExceptionCode.MMS_029_MSG, e);
                                }

                                logger.info("\nSpouse info with national id : "
                                        + nId + "\n");
                                imsLogger
                                        .info("\nSpouse info with national id : "
                                                + nId + "\n");
                                logger.info("\nSpouse FirstName is : "
                                        + spouseTO.getSpouseFirstNamePersian()
                                        + "\n");
                                imsLogger.info("\nSpouse FirstName is : "
                                        + spouseTO.getSpouseFirstNamePersian()
                                        + "\n");
                                logger.info("\nSpouse FatherName is : "
                                        + spouseTO.getSpouseFatherName() + "\n");
                                imsLogger
                                        .info("\nSpouse FatherName is : "
                                                + spouseTO
                                                .getSpouseFatherName()
                                                + "\n");
                                logger.info("\nSpouse BirthCertId is : "
                                        + spouseTO
                                        .getSpouseBirthCertificateId()
                                        + "\n");
                                imsLogger.info("\nSpouse BirthCertId is : "
                                        + spouseTO
                                        .getSpouseBirthCertificateId()
                                        + "\n");
                                logger.info("\nSpouse BirthCertSerial is : "
                                        + spouseTO
                                        .getSpouseBirthCertificateSeries()
                                        + "\n");
                                imsLogger
                                        .info("\nSpouse BirthCertSerial is : "
                                                + spouseTO
                                                .getSpouseBirthCertificateSeries()
                                                + "\n");
                                logger.info("\nSpouse BirthDate is : "
                                        + spouseTO.getSpouseBirthDate() + "\n");
                                imsLogger.info("\nSpouse BirthDate is : "
                                        + spouseTO.getSpouseBirthDate() + "\n");
                            }
                        }
                    }

                } else if (strKey.contains("c")) {
                    nId = strKey.split("c")[1];
                    personEnquiryVTO = returnMap.get(nId);
                    if (EmsUtil.checkString(personEnquiryVTO.getMetadata())) {
                        metadata.append(strKey).append("-");
                    } else {
                        for (ChildTO childTO : czi.getChildren()) {
                            if (childTO.getChildNationalID().equals(nId)) {

                                childTO.setChildFirstNamePersian(personEnquiryVTO
                                        .getFirstName());
                                childTO.setChildBirthCertificateId(personEnquiryVTO
                                        .getBirthCertificateId());
                                // childTO.setChildBirthCertificateSeries(personEnquiryVTO.getBirthCertificateSerial());
                                childTO.setChildBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                                childTO.setChildFatherName(personEnquiryVTO
                                        .getFatherName());
                                childTO.setChildGender(personEnquiryVTO
                                        .getGender());
                                try {
                                    childTO.setChildBirthDateSolar(DateUtil
                                            .convert(personEnquiryVTO
                                                            .getSolarBirthDate(),
                                                    DateUtil.JALALI));
                                } catch (DateFormatException e) {
                                    throw new ServiceException(
                                            BizExceptionCode.MMS_031,
                                            BizExceptionCode.MMS_029_MSG, e);
                                }
                                logger.info("\nChild info with national id : "
                                        + nId + "\n");
                                imsLogger
                                        .info("\nChild info with national id : "
                                                + nId + "\n");
                                logger.info("\nChild FirstName is : "
                                        + childTO.getChildFirstNamePersian()
                                        + "\n");
                                imsLogger.info("\nChild FirstName is : "
                                        + childTO.getChildFirstNamePersian()
                                        + "\n");
                                logger.info("\nChild FatherName is : "
                                        + childTO.getChildFatherName() + "\n");
                                imsLogger.info("\nChild FatherName is : "
                                        + childTO.getChildFatherName() + "\n");
                                logger.info("\nChild BirthCertId is : "
                                        + childTO.getChildBirthCertificateId()
                                        + "\n");
                                imsLogger.info("\nChild BirthCertId is : "
                                        + childTO.getChildBirthCertificateId()
                                        + "\n");
                                logger.info("\nChild BirthCertSerial is : "
                                        + childTO
                                        .getChildBirthCertificateSeries()
                                        + "\n");
                                imsLogger
                                        .info("\nChild BirthCertSerial is : "
                                                + childTO
                                                .getChildBirthCertificateSeries()
                                                + "\n");
                                logger.info("\nChild BirthDate is : "
                                        + childTO.getChildBirthDateSolar()
                                        + "\n");
                                imsLogger.info("\nChild BirthDate is : "
                                        + childTO.getChildBirthDateSolar()
                                        + "\n");
                                logger.info("\nChild Gender is : "
                                        + childTO.getChildGender().name()
                                        + "\n");
                                imsLogger.info("\nChild Gender is : "
                                        + childTO.getChildGender().name()
                                        + "\n");
                            }
                        }
                    }

                } else if (strKey.contains(reqId.toString())) {
                    personEnquiryVTO = returnMap.get(ctz.getNationalID());
                    if (EmsUtil.checkString(personEnquiryVTO.getMetadata())) {
                        metadata.append(strKey).append("-");
                    } else {
                        ctz.setFirstNamePersian(personEnquiryVTO.getFirstName());
                        ctz.setSurnamePersian(personEnquiryVTO.getLastName());
                        czi.setBirthCertificateId(personEnquiryVTO
                                .getBirthCertificateId());
                        // czi.setBirthCertificateSeries(personEnquiryVTO.getBirthCertificateSerial());
                        czi.setBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                        czi.setFatherFirstNamePersian(personEnquiryVTO
                                .getFatherName());
                        if (!czi.getGender().equals(
                                personEnquiryVTO.getGender())) {
                            metadata.append(strKey).append("-");
                        } else {
                            czi.setGender(personEnquiryVTO.getGender());
                        }
                        logger.info("\nCitizen FirstName : "
                                + ctz.getFirstNamePersian() + "\n");
                        imsLogger.info("\nCitizen FirstName : "
                                + ctz.getFirstNamePersian() + "\n");
                        logger.info("\nCitizen LastName : "
                                + ctz.getSurnamePersian() + "\n");
                        imsLogger.info("\nCitizen LastName : "
                                + ctz.getSurnamePersian() + "\n");
                        logger.info("\nCitizen BirthCertId : "
                                + czi.getBirthCertificateId() + "\n");
                        imsLogger.info("\nCitizen BirthCertId : "
                                + czi.getBirthCertificateId() + "\n");
                        logger.info("\nCitizen BirthCertSerial : "
                                + czi.getBirthCertificateSeries() + "\n");
                        imsLogger.info("\nCitizen BirthCertSerial : "
                                + czi.getBirthCertificateSeries() + "\n");
                        logger.info("\nCitizen BirthDateSolar : "
                                + czi.getBirthDateSolar() + "\n");
                        imsLogger.info("\nCitizen BirthDateSolar : "
                                + czi.getBirthDateSolar() + "\n");
                    }
                }
            }
        }
        return metadata.toString();
    }

    /**
     * The method prepareMetaDataForRequest is used to create an instance of
     * String which represents the metadata Condition 1 : if citizenInfo is
     * null, the validationFlag will be false then the metadata will be created
     * Condition 2 : if citizenInfo is 'UniqueId,,,,,,,' .The validationFlag
     * will be false then the metadata will be created Condition 3 : if one of
     * the commas in the citizenInfo['UniqueId,,,,,,,'] has had a value. The
     * validationFlag will be false then the metadata will be created
     *
     * @param citizenInfo a comma separator String
     * @return an instance of type {@link String} which represents a part of the
     * metadata
     * @throws BaseException
     */
    private String prepareMetaDataForRequest(Long requestId,
                                             Object citizenInfoKey, String citizenInfo) throws BaseException {
        boolean validationFlag = false;
        boolean nullFlag = false;
        boolean denyFlag = false;
        String okValue = "OK";
        String notOkValue = "Not Ok";
        String denyValue = "deny";
        StringBuilder stringBuilder = new StringBuilder();

        // Condition 1
        if (citizenInfoKey == null && citizenInfo == null) {
            logger.info("\n########### prepareMetadata method, citizenInfo is null(condition 1)");
            imsLogger
                    .info("\n########### prepareMetadata method, citizenInfo is null(condition 1)");
            nullFlag = true;

        }

        // Condition 2
        else if (citizenInfoKey != null && citizenInfo == null) {
            logger.info("\n########### prepareMetadata method, condition 2");
            imsLogger.info("\n########### prepareMetadata method, condition 2");
        }

        // Condition 3
        else if (citizenInfoKey != null && citizenInfo != null) {
            logger.info("\n########### prepareMetadata method, condition 3, our notOkValue is : '"
                    + notOkValue
                    + "' with the length '"
                    + notOkValue.length()
                    + "' and IMS validation value is : '"
                    + citizenInfo
                    + "'with the length : '" + citizenInfo.length() + "'");
            imsLogger
                    .info("\n########### prepareMetadata method, condition 3, our notOkValue is : '"
                            + notOkValue
                            + "' with the length '"
                            + notOkValue.length()
                            + "' and IMS validation value is : '"
                            + citizenInfo
                            + "'with the length : '"
                            + citizenInfo.length()
                            + "'");

            if (citizenInfoKey.equals(requestId.toString())
                    && denyValue.equalsIgnoreCase(citizenInfo.trim())) {
                logger.info("\n########### In accordance to the IMS announcement, the citizen with the requestId of '"
                        + requestId
                        + "' is not eligible to apply for the Intelligent National Card. Details :[requestId: "
                        + requestId
                        + ", IMSEnquiryResult: "
                        + citizenInfo.trim() + "]\n");
                imsLogger
                        .info("\n########### In accordance to the IMS announcement, the citizen with the requestId of '"
                                + requestId
                                + "' is not eligible to apply for the Intelligent National Card. Details :[requestId: "
                                + requestId
                                + ", IMSEnquiryResult: "
                                + citizenInfo.trim() + "]\n");
                denyFlag = true;
            } else if (okValue.equalsIgnoreCase(citizenInfo.trim())) {
                validationFlag = true;
            }
        }

        logger.info("\n########## prepareMetadata method, validationFlag is : '"
                + validationFlag + "'");
        imsLogger
                .info("\n########## prepareMetadata method, validationFlag is : '"
                        + validationFlag + "'");
        if (!validationFlag) {
            if (nullFlag) {
                stringBuilder.append(requestId);
            } else if (denyFlag) {
                stringBuilder.append(requestId).append(denyValue);
            } else {
                stringBuilder.append(citizenInfoKey);
            }
            stringBuilder.append("-");
        }
        return stringBuilder.toString();
    }

    @javax.ejb.TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void innerUpdateCitizensInfo(List<CardRequestTO> cardRequestTOList,
                                         String requestId)
            throws BaseException {
        getIMSService().updateCitizensInfo(cardRequestTOList, requestId);
    }

    /**
     * The method getUpdatedCitizensInfoResult is used to call the external
     * service of loadUpdatedCitizensInfoResult
     *
     * @param imsProxyProvider is an instance of type {@link IMSProxyProvider}, which is used
     *                         to represent the current IMSProxyProvider, as regards of the
     *                         configurations that set in profileManager
     * @param imsRequestId     an instance of type {@link String} which represents the
     *                         message request id which belongs to the sub system 'IMS'
     * @return a list of type {@link IMSUpdateResultVTO}
     * @throws BaseException
     */
    @javax.ejb.TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private List<IMSUpdateResultVTO> getUpdatedCitizensInfoResult(String imsRequestId)
            throws BaseException {
        return getIMSService().getUpdatedCitizensResult(imsRequestId);
    }

    private List<IMSEnquiryVTO> fetchListForBatchEnquiryReq(Integer from,
                                                            Integer batchSize, CardRequestState cardRequestState)
            throws BaseException {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("requestState", cardRequestState.name());
        String queryName;

        /**
         * For the requests which are sent for the first time : If the request
         * is from MES originally, the state field is set by
         * 'CardRequestState.APPROVED_BY_MES' and the origin field is set by
         * 'CardRequestOrigin.M', Otherwise only the state field of request is
         * set by 'CardRequestState.RECEIVED_BY_EMS'.
         **/
        if (CardRequestState.RECEIVED_BY_EMS.equals(cardRequestState)) {
            queryName = "imsBatchEnquiryList";
            param.put("mesRequestState",
                    CardRequestState.APPROVED_BY_MES.name());
            param.put("mesOrigin", CardRequestOrigin.M.name());
        }

        /**
         * For the requests which are sent for the first time, the state is
         * filled with the state 'CardRequestState.PENDING_IMS'
         **/
        else if (CardRequestState.PENDING_IMS.equals(cardRequestState)) {
            queryName = "imsBatchEnquiryListForPendingIMSRequests";
            String[] parameters = getBatchEnquiryParametersForIMSPendingRequestsFromProfileManager();
            param.put("interval", parameters[0]);
            param.put("intervalType", parameters[1]);

        } else {
            throw new ServiceException(BizExceptionCode.MMS_024,
                    BizExceptionCode.MMS_024_MSG);
        }

        try {
            ValueListHandler vlh = EMSValueListProvider.getProvider().loadList(
                    queryName, "main".split(","), "count".split(","), param,
                    null, null);
            // Integer listSize = getBatchEnquiryListSizeByProfileManager();
            return vlh.getList(from, from + batchSize, true);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.MMS_045,
                    e.getMessage(), e);
        }
    }

//	private List<IMSEnquiryVTO> fetchListForBatchEnquiryReqAndReservedState(
//			Integer from, Long requestId) throws BaseException {
//		HashMap<String, String> param = new HashMap<String, String>();
//		param.put("requestState", CardRequestState.RESERVED.name());
//		param.put("mesOrigin", CardRequestOrigin.P.name());
//		param.put("estelam2Flag", BooleanType.F.name());
//		param.put("requestId", requestId.toString());
//
//		try {
//			ValueListHandler vlh = EMSValueListProvider.getProvider().loadList(
//					"imsFaildEnquiryList", "main".split(","),
//					"count".split(","), param, null, null);
//			// Integer listSize = getBatchEnquiryListSizeByProfileManager();
//			return vlh.getList(from, from + 1, true);
//		} catch (Exception e) {
//			throw new ServiceException(BizExceptionCode.MMS_045,
//					e.getMessage(), e);
//		}
//	}

    private List<IMSEnquiryVTO> fetchListForEnquiryReqByRequestId(
            Long cardRequestId) throws BaseException {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("requestId", cardRequestId.toString());
        param.put("mesOrigin", CardRequestOrigin.P.name());
        param.put("requestState", CardRequestState.RESERVED.name());
        param.put("notVerifiedFlag", Estelam2FlagType.N.toString());
        param.put("readyFlag", Estelam2FlagType.R.toString());


        try {
            ValueListHandler vlh = EMSValueListProvider.getProvider().loadList(
                    "imsEnquiryListByRequestId", "main".split(","),
                    "count".split(","), param, null, null);
            // Integer listSize = getBatchEnquiryListSizeByProfileManager();
            return vlh.getList(true);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.MMS_045,
                    e.getMessage(), e);
        }
    }

    private HashMap<String, Object> prepareDataForBatchEnquiryReq(Integer from,
                                                                  Integer batchSize, CardRequestState cardRequestState)
            throws BaseException {
        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        List<IMSEnquiryVTO> imsEnquiryVTOList = fetchListForBatchEnquiryReq(
                from, batchSize, cardRequestState);
        StringBuilder strBuilder = new StringBuilder();
        TransferInfoVTO transferInfoVTO;
        if (imsEnquiryVTOList != null && !imsEnquiryVTOList.isEmpty()) {
            returnMap.put("imsEnquiryVTOList", imsEnquiryVTOList);
            for (IMSEnquiryVTO imsEnquiryVTO : imsEnquiryVTOList) {
                strBuilder.append(imsEnquiryVTO.getCitizenInfo()).append("\n");
            }

            String strData = strBuilder.toString();
            if (EmsUtil.checkString(strData)) {
                // todo: refactor
                try {
                    // Generating requestId
                    String batchRequestId = "IMS_BATCH_ENQUIRY_"
                            + getBatchRequestIdFromSequence();
                    transferInfoVTO = new TransferInfoVTO(batchRequestId, 0,
                            strBuilder.toString().getBytes("UTF-8"));
                    returnMap.put("transferInfoVTO", transferInfoVTO);
                } catch (BaseException e) {
                    logger.error(e.getExceptionCode(), e.getMessage(), e);
                    imsLogger.error(e.getExceptionCode(), e.getMessage(), e);
                } catch (Exception e) {
                    logger.error(BizExceptionCode.MMS_048, e.getMessage(), e);
                    imsLogger
                            .error(BizExceptionCode.MMS_048, e.getMessage(), e);
                }
            }
        }
        return returnMap;
    }

    /**
     * The method selectIMSProvider is used to check whether the IMS Service
     * will be done or not, in spite of the return value from config.
     *
     * @param type
     *            an integer value, which consists of the values:
     *            (OFFLINE_ENQUIRY, ONLINE_ENQUIRY, UPDATE_CITIZENS_INFO,
     *            FETCH_CITIZEN_INFO, SET_CITIZEN_CARD_REQUESTED,
     *            SET_CITIZEN_CARD_DELIVERED)
     * @return an instance of type {@link IMSProxyProvider}
     * @throws BaseException
     */
//	public IMSProxyProvider selectIMSProvider(int type) throws BaseException {
//		Integer configValue = null;
//		switch (type) {
//		case OFFLINE_ENQUIRY:
//			try {
//				ProfileManager pm = ProfileHelper.getProfileManager();
//				configValue = Integer.parseInt((String) pm.getProfile(
//						ProfileKeyName.KEY_IMS_OFFLINE_ENQUIRY_FLAG, true,
//						null, null));
//			} catch (Exception e) {
//				logger.warn(BizExceptionCode.MMS_017,
//						BizExceptionCode.MMS_017_MSG,
//						new String[] { "BatchEnquiry" });
//				imsLogger.warn(BizExceptionCode.MMS_017,
//						BizExceptionCode.MMS_017_MSG,
//						new String[] { "BatchEnquiry" });
//			}
//			break;
//		case ONLINE_ENQUIRY:
//			try {
//				ProfileManager pm = ProfileHelper.getProfileManager();
//				configValue = Integer.parseInt((String) pm.getProfile(
//						ProfileKeyName.KEY_IMS_ONLINE_ENQUIRY_FLAG, true, null,
//						null));
//			} catch (Exception e) {
//				logger.warn(BizExceptionCode.MMS_018,
//						BizExceptionCode.MMS_017_MSG,
//						new String[] { "OnlineEnquiry" });
//				imsLogger.warn(BizExceptionCode.MMS_018,
//						BizExceptionCode.MMS_017_MSG,
//						new String[] { "OnlineEnquiry" });
//			}
//			break;
//		case UPDATE_CITIZENS_INFO:
//			try {
//				ProfileManager pm = ProfileHelper.getProfileManager();
//				configValue = Integer.parseInt((String) pm.getProfile(
//						ProfileKeyName.KEY_IMS_UPDATE_CITIZENS_INFO_FLAG, true,
//						null, null));
//			} catch (Exception e) {
//				logger.warn(BizExceptionCode.MMS_019,
//						BizExceptionCode.MMS_017_MSG,
//						new String[] { "updateCitizensInfo" });
//				imsLogger.warn(BizExceptionCode.MMS_019,
//						BizExceptionCode.MMS_017_MSG,
//						new String[] { "updateCitizensInfo" });
//			}
//			break;
//
//		case FETCH_CITIZEN_INFO:
//			try {
//				ProfileManager pm = ProfileHelper.getProfileManager();
//				configValue = Integer.parseInt((String) pm.getProfile(
//						ProfileKeyName.KEY_IMS_FETCH_CITIZEN_INFO_FLAG, true,
//						null, null));
//			} catch (Exception e) {
//				logger.warn(BizExceptionCode.MMS_026,
//						BizExceptionCode.MMS_017_MSG,
//						new String[] { "fetchCitizenInfo" });
//				imsLogger.warn(BizExceptionCode.MMS_026,
//						BizExceptionCode.MMS_017_MSG,
//						new String[] { "fetchCitizenInfo" });
//			}
//			break;
//
//		case SET_CITIZEN_CARD_REQUESTED:
//			try {
//				ProfileManager pm = ProfileHelper.getProfileManager();
//				configValue = Integer.parseInt((String) pm.getProfile(
//						ProfileKeyName.KEY_IMS_SET_CITIZEN_CARD_REQUESTED_FLAG,
//						true, null, null));
//			} catch (Exception e) {
//				logger.warn(BizExceptionCode.MMS_038,
//						BizExceptionCode.MMS_017_MSG,
//						new String[] { "setCitizenCardRequested" });
//				imsLogger.warn(BizExceptionCode.MMS_038,
//						BizExceptionCode.MMS_017_MSG,
//						new String[] { "setCitizenCardRequested" });
//			}
//			break;
//
//		case SET_CITIZEN_CARD_DELIVERED:
//			try {
//				ProfileManager pm = ProfileHelper.getProfileManager();
//				configValue = Integer.parseInt((String) pm.getProfile(
//						ProfileKeyName.KEY_IMS_SET_CITIZEN_CARD_DELIVERED_FLAG,
//						true, null, null));
//			} catch (Exception e) {
//				logger.warn(BizExceptionCode.MMS_039,
//						BizExceptionCode.MMS_017_MSG,
//						new String[] { "setCitizenCardDelivered" });
//				imsLogger.warn(BizExceptionCode.MMS_039,
//						BizExceptionCode.MMS_017_MSG,
//						new String[] { "setCitizenCardDelivered" });
//			}
//			break;
//
//		}
//
//		IMSProxyProvider imsProxyProvider = null;
//		if (configValue == null || configValue == 0) {
//			imsProxyProvider = IMSProxyProvider.GAM;
//		} else if (configValue == 1) {
//			imsProxyProvider = IMSProxyProvider.NOCR;
//		} else if (configValue == 2) {
//			imsProxyProvider = IMSProxyProvider.MATIRAN;
//		}
//
//		return imsProxyProvider;
//	}

    /**
     * <pre>
     * The method loadUpdatedCitizensInfoResult is used to handle the activities which has been mentioned bellow :
     * 1. Calling IMS service to get the result of update
     * 2. Looping on CardRequestHistoryTo
     * 3. Updating CardRequestHistoryTo
     * 4. Updating CardRequestTO
     * 5. Updating CitizenInfoTO
     * </pre>
     */
    @Override
    @BizLoggable(logAction = "LOAD_UPDATED_CITIZENS", logEntityName = "IMS", logActor = "System")
    public void loadUpdatedCitizensInfoResult() throws BaseException {
        try {

            List<String> imsRequestIdList = getCardRequestHistoryDAO()
                    .findSubSystemRequestIdsByRequestStateAndSystemId(
                            CardRequestState.SENT_TO_AFIS, SystemId.IMS);
            if (imsRequestIdList != null && !imsRequestIdList.isEmpty()) {

                for (String imsRequestId : imsRequestIdList) {
                    List<IMSUpdateResultVTO> imsUpdateResultVTOList = null;
                    try {

                        imsUpdateResultVTOList = getUpdatedCitizensInfoResult(imsRequestId);
                    } catch (BaseException e) {
                        logger.error("\n*************************************************************************************");
                        imsLogger
                                .error("\n*************************************************************************************");
                        logger.error("\n************* ERROR IN UPDATE CITIZEN RESULT(Whole request was rejected) ************");
                        imsLogger
                                .error("\n************* ERROR IN UPDATE CITIZEN RESULT(Whole request was rejected) ************");
                        logger.error("\n*************************************************************************************");
                        imsLogger
                                .error("\n*************************************************************************************");
                        logger.error(e.getExceptionCode(), e.getMessage(), e);
                        imsLogger
                                .error(e.getExceptionCode(), e.getMessage(), e);

                        String result = (SystemId.IMS.name() + ":"
                                + ConstValues.GAM_ERROR_WITH_LIMITED_RETRY
                                + e.getExceptionCode() + ":" + e.getMessage());
                        if (result.length() > 240) {
                            result = result.substring(0, 240);
                        }
                        logger.info("\nBEFORE FINDING REQUEST_IDS\n");
                        imsLogger.info("\nBEFORE FINDING REQUEST_IDS\n");
                        List<Long> cardRequestIdList = getCardRequestHistoryDAO()
                                .findCardRequestIdsBySubSystemRequestId(
                                        imsRequestId);
                        if (cardRequestIdList != null
                                && !cardRequestIdList.isEmpty()) {
                            logger.info("\nAFTER FINDING REQUEST_IDS ---- THE ID LIST COUNT IS : ' "
                                    + cardRequestIdList.size() + " '\n");
                            imsLogger
                                    .info("\nAFTER FINDING REQUEST_IDS ---- THE ID LIST COUNT IS : ' "
                                            + cardRequestIdList.size() + " '\n");
                        } else {
                            logger.info("\nTHE REQUEST IDS IS NULL\n");
                            imsLogger.info("\nTHE REQUEST IDS IS NULL\n");
                        }
                        if (EmsUtil.checkListSize(cardRequestIdList)) {
                            createCardRequestHistory(cardRequestIdList,
                                    imsRequestId, result,
                                    CardRequestHistoryAction.AFIS_RECEIVE_ERROR);
                            List<Long> requestIdListForIMSError = new ArrayList<Long>();
                            for (Long cardRequestId : cardRequestIdList) {
                                CardRequestTO cardRequestTO = new CardRequestTO();
                                cardRequestTO.setId(cardRequestId);
                                if (!getCardRequestHistoryDAO()
                                        .checkAcceptabilityOfRetryRequest(
                                                cardRequestTO, SystemId.IMS)) {
                                    requestIdListForIMSError.add(cardRequestId);
                                }
                            }
                            if (EmsUtil.checkListSize(requestIdListForIMSError)) {
                                getCardRequestDAO().updateCardRequestsState(
                                        requestIdListForIMSError,
                                        CardRequestState.IMS_ERROR);
                            }
                        }

                    }
                    if (EmsUtil.checkListSize(imsUpdateResultVTOList)) {
                        List<Long> cardRequestIdListForSuccessfulUpdate = new ArrayList<Long>();
                        List<Long> cardRequestIdListForFailureUpdate = new ArrayList<Long>();
                        HashMap<CitizenInfoTO, Long> citizenInfoHashMap = new HashMap<CitizenInfoTO, Long>();
                        for (IMSUpdateResultVTO imsUpdateResultVTO : imsUpdateResultVTOList) {
                            List<CardRequestHistoryTO> cardRequestHistoryTOs = getCardRequestHistoryDAO()
                                    .findUniqueByNationalId(
                                            imsUpdateResultVTO.getNationalId());
                            if (!EmsUtil.checkListSize(cardRequestHistoryTOs)) {
                                // throw new
                                // ServiceException(BizExceptionCode.MMS_023,
                                // BizExceptionCode.MMS_023_MSG, new
                                // String[]{imsUpdateResultVTO.getNationalId()});
                                logger.warn(BizExceptionCode.MMS_023,
                                        BizExceptionCode.MMS_023_MSG,
                                        new String[]{imsUpdateResultVTO
                                                .getNationalId()});
                                imsLogger.warn(BizExceptionCode.MMS_023,
                                        BizExceptionCode.MMS_023_MSG,
                                        new String[]{imsUpdateResultVTO
                                                .getNationalId()});
                            } else {
                                CardRequestHistoryTO cardRequestHistoryTO = cardRequestHistoryTOs
                                        .get(0);
                                if (imsUpdateResultVTO.getErrorCode() == null
                                        || imsUpdateResultVTO.getErrorCode()
                                        .equalsIgnoreCase("null")) {
                                    cardRequestHistoryTO
                                            .setResult(CardRequestState.APPROVED_BY_AFIS
                                                    .name());

                                    cardRequestIdListForSuccessfulUpdate
                                            .add(cardRequestHistoryTO
                                                    .getCardRequest().getId());

                                    CitizenInfoTO tempCitizenInfoTO = new CitizenInfoTO();
                                    tempCitizenInfoTO
                                            .setAfisState(imsUpdateResultVTO
                                                    .getAfisState());
                                    tempCitizenInfoTO
                                            .setIdentityChanged(imsUpdateResultVTO
                                                    .getIdentityChanged());
                                    citizenInfoHashMap.put(tempCitizenInfoTO,
                                            cardRequestHistoryTO.getId());
                                } else {
                                    logger.error(imsUpdateResultVTO
                                            .getErrorCode(), imsUpdateResultVTO
                                            .getErrorMessage());
                                    imsLogger.error(imsUpdateResultVTO
                                            .getErrorCode(), imsUpdateResultVTO
                                            .getErrorMessage());
                                    String result = "";
                                    if (IMS_UPDT_000004
                                            .equals(imsUpdateResultVTO
                                                    .getErrorCode())) {
                                        result = (SystemId.IMS.name()
                                                + ":"
                                                + ConstValues.GAM_ERROR_WITH_NO_RETRY
                                                + imsUpdateResultVTO
                                                .getErrorCode() + ":" + imsUpdateResultVTO
                                                .getErrorMessage()).substring(
                                                0, 255);
                                        cardRequestHistoryTO.setResult(result);
                                        cardRequestIdListForFailureUpdate
                                                .add(cardRequestHistoryTO
                                                        .getCardRequest()
                                                        .getId());
                                    } else {
                                        // TODO : This part is temporary assumed
                                        // as GAM_ERROR_WITH_NO_RETRY
                                        result = (SystemId.IMS.name()
                                                + ":"
                                                + ConstValues.GAM_ERROR_WITH_NO_RETRY
                                                + imsUpdateResultVTO
                                                .getErrorCode() + ":" + imsUpdateResultVTO
                                                .getErrorMessage()).substring(
                                                0, 255);
                                        cardRequestHistoryTO.setResult(result);
                                        cardRequestIdListForFailureUpdate
                                                .add(cardRequestHistoryTO
                                                        .getCardRequest()
                                                        .getId());
                                    }
                                }
                            }
                        }
                        if (!cardRequestIdListForSuccessfulUpdate.isEmpty()) {
                            getCardRequestDAO().updateCardRequestsState(
                                    cardRequestIdListForSuccessfulUpdate,
                                    CardRequestState.APPROVED_BY_AFIS);
                            getCitizenInfoDAO()
                                    .updateCitizenInfoByRequestHistory(
                                            citizenInfoHashMap);
                        }

                        if (!cardRequestIdListForFailureUpdate.isEmpty()) {
                            getCardRequestDAO().updateCardRequestsState(
                                    cardRequestIdListForFailureUpdate,
                                    CardRequestState.IMS_ERROR);
                        }

                    }

                }

            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.MMS_007,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * The method getOnlineEnquiry is used to enquiry a citizen via an online
     * request
     *
     * @param personEnquiryVTOs is an array of type
     *                          {@link com.gam.nocr.ems.data.domain.vol.PersonEnquiryVTO}
     *                          which carries the necessary attributes to enquiry
     * @return a hashmap of {@link java.util.HashMap <String, Boolean>} which
     * consists of values of true or false to represent the response of
     * enquiry in spite of nationalIds
     * @throws BaseException
     */
    @Override
    public HashMap<String, Boolean> getOnlineEnquiry(
            PersonEnquiryVTO[] personEnquiryVTOs) throws BaseException {
        return getIMSService().getOnlineEnquiry(personEnquiryVTOs);
    }

    /**
     * The method fetchCitizenInfo is used to fetch the information of a
     * specified citizen
     *
     * @param nationalId is an string value which represents the nationalId of a
     *                   specified citizen
     * @return an instance of type
     * {@link com.gam.nocr.ems.data.domain.CitizenTO}
     * @throws BaseException
     */
    @Override
    public CitizenTO fetchCitizenInfo(String nationalId) throws BaseException {
        return getIMSService().fetchCitizenInfo(nationalId);
    }

    /**
     * The method updatePersonInfoByOnlineEnquiry is used to update the citizen
     * information of a request, base on IMS citizen data
     *
     * @param cardRequestTO     is an instance of type {@link CardRequestTO}, which carries
     *                          the necessary information belongs to a specified citizen
     * @param personEnquiryVTOs is an instance of type {@link PersonEnquiryVTO}
     * @throws BaseException
     */
    @Override
    public void updatePersonInfoByOnlineEnquiry(CardRequestTO cardRequestTO,
                                                PersonEnquiryVTO[] personEnquiryVTOs) throws BaseException {
        Map<String, PersonEnquiryVTO> fetchedMap = getIMSService()
                .fetchDataByOnlineEnquiry(personEnquiryVTOs);
        if (fetchedMap != null && !fetchedMap.isEmpty()) {
            CitizenTO citizenTO = cardRequestTO.getCitizen();
            CitizenInfoTO citizenInfoTO = citizenTO.getCitizenInfo();
            for (PersonEnquiryVTO inputPersonEnquiryVTO : personEnquiryVTOs) {
                String nationalId = inputPersonEnquiryVTO.getNationalId();
                PersonEnquiryVTO mapPersonEnquiryVTO = fetchedMap
                        .get(nationalId);
                if (nationalId.equals(citizenTO.getNationalID())) {
                    citizenTO.setFirstNamePersian(mapPersonEnquiryVTO
                            .getFirstName());
                    citizenTO.setSurnamePersian(mapPersonEnquiryVTO
                            .getLastName());
                    citizenInfoTO.setGender(mapPersonEnquiryVTO.getGender());
                    // citizenInfoTO.setBirthCertificateSeries(mapPersonEnquiryVTO.getBirthCertificateSerial());
                    citizenInfoTO
                            .setBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                    citizenInfoTO.setFatherFirstNamePersian(mapPersonEnquiryVTO
                            .getFatherName());

                } else if (nationalId.equals(citizenInfoTO
                        .getFatherNationalID())) {
                    citizenInfoTO.setFatherSurname(mapPersonEnquiryVTO
                            .getLastName());
                    citizenInfoTO
                            .setFatherBirthCertificateId(mapPersonEnquiryVTO
                                    .getBirthCertificateId());
                    // citizenInfoTO.setFatherBirthCertificateSeries(mapPersonEnquiryVTO.getBirthCertificateSerial());
                    citizenInfoTO
                            .setFatherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                    citizenInfoTO.setFatherFatherName(mapPersonEnquiryVTO
                            .getFatherName());
                    try {
                        citizenInfoTO.setFatherBirthDateSolar(DateUtil.convert(
                                mapPersonEnquiryVTO.getSolarBirthDate(),
                                DateUtil.JALALI));
                    } catch (DateFormatException e) {
                        throw new ServiceException(BizExceptionCode.MMS_033,
                                BizExceptionCode.MMS_029_MSG, e);
                    }

                } else if (nationalId.equals(citizenInfoTO
                        .getMotherNationalID())) {
                    citizenInfoTO.setMotherFirstNamePersian(mapPersonEnquiryVTO
                            .getFirstName());
                    citizenInfoTO.setMotherSurname(mapPersonEnquiryVTO
                            .getLastName());
                    citizenInfoTO
                            .setMotherBirthCertificateId(mapPersonEnquiryVTO
                                    .getBirthCertificateId());
                    // citizenInfoTO.setMotherBirthCertificateSeries(mapPersonEnquiryVTO.getBirthCertificateSerial());
                    citizenInfoTO
                            .setMotherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                    citizenInfoTO.setMotherFatherName(mapPersonEnquiryVTO
                            .getFatherName());
                    try {
                        citizenInfoTO.setMotherBirthDateSolar(DateUtil.convert(
                                mapPersonEnquiryVTO.getSolarBirthDate(),
                                DateUtil.JALALI));
                    } catch (DateFormatException e) {
                        throw new ServiceException(BizExceptionCode.MMS_034,
                                BizExceptionCode.MMS_029_MSG, e);
                    }
                } else {
                    boolean selectorFlag = false;
                    for (SpouseTO spouseTO : citizenInfoTO.getSpouses()) {
                        if (nationalId.equals(spouseTO.getSpouseNationalID())) {
                            selectorFlag = true;
                            spouseTO.setSpouseFirstNamePersian(mapPersonEnquiryVTO
                                    .getFirstName());
                            spouseTO.setSpouseSurnamePersian(mapPersonEnquiryVTO
                                    .getLastName());
                            spouseTO.setSpouseBirthCertificateId(mapPersonEnquiryVTO
                                    .getBirthCertificateId());
                            // spouseTO.setSpouseBirthCertificateSeries(mapPersonEnquiryVTO.getBirthCertificateSerial());
                            spouseTO.setSpouseBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                            spouseTO.setSpouseFatherName(mapPersonEnquiryVTO
                                    .getFatherName());
                            try {
                                spouseTO.setSpouseBirthDate(DateUtil.convert(
                                        mapPersonEnquiryVTO.getSolarBirthDate(),
                                        DateUtil.JALALI));
                            } catch (DateFormatException e) {
                                throw new ServiceException(
                                        BizExceptionCode.MMS_035,
                                        BizExceptionCode.MMS_029_MSG, e);
                            }
                            break;
                        }
                    }

                    if (!selectorFlag) {
                        for (ChildTO childTO : citizenInfoTO.getChildren()) {
                            if (nationalId.equals(childTO.getChildNationalID())) {
                                childTO.setChildFirstNamePersian(mapPersonEnquiryVTO
                                        .getFirstName());
                                childTO.setChildBirthCertificateId(mapPersonEnquiryVTO
                                        .getBirthCertificateId());
                                // childTO.setChildBirthCertificateSeries(mapPersonEnquiryVTO.getBirthCertificateSerial());
                                childTO.setChildBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                                childTO.setChildFatherName(mapPersonEnquiryVTO
                                        .getFatherName());
                                childTO.setChildGender(mapPersonEnquiryVTO
                                        .getGender());
                                try {
                                    childTO.setChildBirthDateSolar(DateUtil
                                            .convert(mapPersonEnquiryVTO
                                                            .getSolarBirthDate(),
                                                    DateUtil.JALALI));
                                } catch (DateFormatException e) {
                                    throw new ServiceException(
                                            BizExceptionCode.MMS_036,
                                            BizExceptionCode.MMS_029_MSG, e);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * The method getRequestsCountForSendToAFIS is used to get the count of
     * requests, which are ready to send to AFIS
     *
     * @return an instance of type {@link Long}, which represents the number of
     * ready requests to send
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public Long getRequestsCountForSendToAFIS() throws BaseException {
        return getCardRequestDAO().getRequestsCountForSendToAFIS();
    }

    //Anbari
    @Override
    public List<Long> getRequestIdsForSendToAFIS(Integer fetchLimit) throws BaseException {
        return getCardRequestDAO().getRequestIdsForSendToAFIS(fetchLimit);
    }

    /**
     * The method 'updateCitizenInfo' is used to call the AFIS service of update
     * citizen info to send stored information that associated to a specified
     * citizen.
     *
     * @param from is an instance of type {@link Integer}, which represents the
     *             index of data that is supposed to be fetched from database to
     *             send
     * @throws BaseException
     */
    @Override
    @BizLoggable(logAction = "UPDATE_CITIZENS_INFO", logEntityName = "IMS", logActor = "System")
    public void updateCitizenInfo(Integer from) throws BaseException {

        CardRequestTO cardRequestTO = getCardRequestDAO()
                .findRequestForSendToAFIS(from);
        if (cardRequestTO == null) {
            throw new ServiceException(BizExceptionCode.MMS_043,
                    BizExceptionCode.MMS_043_MSG);
        }
        String reqId = null;
        try {
            Long uniqueRequestId = getCardRequestHistoryDAO()
                    .getRequestIdFromSequence(
                            SequenceName.SEQ_EMS_REQUEST_ID_IMS_UPDATE_CITIZEN_INFO);
            reqId = EmsUtil.createRequestIdForMessage(
                    uniqueRequestId.toString(), 32, "", "0");
            List<CardRequestTO> cardRequestTOs = new ArrayList<CardRequestTO>();
            cardRequestTOs.add(cardRequestTO);
            innerUpdateCitizensInfo(cardRequestTOs, reqId);
            cardRequestTO.setState(CardRequestState.SENT_TO_AFIS);
            createNewCardRequestHistory(cardRequestTO, reqId,
                    CardRequestState.SENT_TO_AFIS.name(),
                    CardRequestHistoryAction.SENT_TO_AFIS);
        } catch (Exception e) {
            try {
                if (reqId != null) {
                    createNewCardRequestHistory(
                            cardRequestTO,
                            reqId,
                            ConstValues.GAM_ERROR_WITH_UNLIMITED_RETRY
                                    + e.getMessage(),
                            CardRequestHistoryAction.AFIS_SEND_ERROR);
                }
            } catch (BaseException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new ServiceException(BizExceptionCode.MMS_041,
                        BizExceptionCode.GLB_008_MSG, ex);
            }

            if (e instanceof BaseException) {
                throw (BaseException) e;
            } else {
                throw new ServiceException(BizExceptionCode.MMS_042,
                        BizExceptionCode.GLB_008_MSG, e);
            }

        }
    }

    //Anbari
    @Override
    public void updateCitizenInfo(Long requestId) throws BaseException {

        CardRequestTO cardRequestTO = getCardRequestDAO().find(CardRequestTO.class, requestId);
        if (cardRequestTO == null) {
            throw new ServiceException(BizExceptionCode.MMS_043,
                    BizExceptionCode.MMS_043_MSG);
        }
        String reqId = null;
        try {
            Long uniqueRequestId = getCardRequestHistoryDAO()
                    .getRequestIdFromSequence(
                            SequenceName.SEQ_EMS_REQUEST_ID_IMS_UPDATE_CITIZEN_INFO);
            reqId = EmsUtil.createRequestIdForMessage(
                    uniqueRequestId.toString(), 32, "", "0");
            List<CardRequestTO> cardRequestTOs = new ArrayList<CardRequestTO>();
            cardRequestTOs.add(cardRequestTO);
            innerUpdateCitizensInfo(cardRequestTOs, reqId);
            cardRequestTO.setState(CardRequestState.SENT_TO_AFIS);
            createNewCardRequestHistory(cardRequestTO, reqId,
                    CardRequestState.SENT_TO_AFIS.name(),
                    CardRequestHistoryAction.SENT_TO_AFIS);
        } catch (Exception e) {
            try {
                if (reqId != null) {
                    createNewCardRequestHistory(
                            cardRequestTO,
                            reqId,
                            ConstValues.GAM_ERROR_WITH_UNLIMITED_RETRY
                                    + e.getMessage(),
                            CardRequestHistoryAction.AFIS_SEND_ERROR);
                }
            } catch (BaseException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new ServiceException(BizExceptionCode.MMS_064,
                        BizExceptionCode.GLB_008_MSG, ex);
            }

            if (e instanceof BaseException) {
                throw (BaseException) e;
            } else {
                throw new ServiceException(BizExceptionCode.MMS_065,
                        BizExceptionCode.GLB_008_MSG, e);
            }

        }

    }


    public Integer getRequestsCountForBatchEnquiry(String queryName,
                                                   HashMap<String, String> param) throws BaseException {
        try {
            return EMSValueListProvider
                    .getProvider()
                    .loadList(queryName, "main".split(","), "count".split(","),
                            param, null, null).executeCountQuery();
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.MMS_044,
                    e.getMessage(), e);
        }
    }

    public List<Long> getRequestsIdsForEnquiry(Integer limit) throws BaseException {
        try {
            return getCardRequestDAO().getRequestsIdsForEnquiry(limit);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.MMS_058,
                    e.getMessage(), e);
        }
    }

    @Override
    @BizLoggable(logAction = "BATCH_ENQUIRY_REQUEST", logEntityName = "IMS", logActor = "System")
    public void sendBatchEnquiryReq(Integer from, Integer batchSize,
                                    CardRequestState cardRequestState) throws BaseException {
        HashMap<String, Object> preparedDataMap = prepareDataForBatchEnquiryReq(
                from, batchSize, cardRequestState);
        if (preparedDataMap != null && !preparedDataMap.isEmpty()) {
            List<IMSEnquiryVTO> imsEnquiryVTOList = (List<IMSEnquiryVTO>) preparedDataMap
                    .get("imsEnquiryVTOList");
            TransferInfoVTO transferInfoVTO = (TransferInfoVTO) preparedDataMap
                    .get("transferInfoVTO");
            if (EmsUtil.checkListSize(imsEnquiryVTOList)
                    && transferInfoVTO != null) {
                List<Long> cardRequestIds = new ArrayList<Long>();
                try {
                    getIMSService().sendBatchEnquiryRequest(transferInfoVTO);
                    for (IMSEnquiryVTO imsEnquiryVTO : imsEnquiryVTOList) {
                        cardRequestIds.add(imsEnquiryVTO.getRequestId());
                    }
                    if (!cardRequestIds.isEmpty()) {
                        updateCardRequestsByState(cardRequestIds,
                                CardRequestState.PENDING_IMS);
                        createCardRequestHistory(cardRequestIds,
                                transferInfoVTO.getRequestId(),
                                CardRequestState.PENDING_IMS.name(),
                                CardRequestHistoryAction.PENDING_IMS);
                    }
                } catch (Exception e) {
                    try {
                        if (EmsUtil.checkListSize(cardRequestIds)) {
                            createCardRequestHistory(cardRequestIds,
                                    transferInfoVTO.getRequestId(),
                                    e.getMessage(),
                                    CardRequestHistoryAction.IMS_SEND_ERROR);
                        }
                    } catch (BaseException ex) {
                        throw ex;
                    } catch (Exception ex) {
                        throw new ServiceException(BizExceptionCode.MMS_046,
                                BizExceptionCode.GLB_008_MSG, ex);
                    }
                    if (e instanceof BaseException) {
                        throw (BaseException) e;
                    } else {
                        throw new ServiceException(BizExceptionCode.MMS_047,
                                BizExceptionCode.GLB_008_MSG, e);
                    }
                }
            }
        }
    }

    @Override
    public TransferInfoVTO getBatchEnquiryResult() throws BaseException {
        return getIMSService().getBatchEnquiryResponse();
    }

    /**
     * The method updateRequestsByBatchEnquiryResult is used to update requests
     * due to the result of batch enquiry from the sub system 'IMS'
     *
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    @BizLoggable(logAction = "BATCH_ENQUIRY_RESPONSE", logEntityName = "IMS", logActor = "System")
    public void updateRequestsByBatchEnquiryResult(
            TransferInfoVTO transferInfoVTO) throws BaseException {
        HashMap<Long, HashMap<String, String>> resultMap = new HashMap<Long, HashMap<String, String>>();
        if (transferInfoVTO != null) {
            String strData = EmsUtil.convertByteArrayToString(transferInfoVTO
                    .getData());
            String[] strDataSplitByEnter = strData.split("\n");

            List<Long> requestIds = new ArrayList<Long>();

            /**
             * Creates a list of type long which carries the requestIds
             */
            for (String strInEachRow : strDataSplitByEnter) {
                String[] strSplitByComma = strInEachRow.split(",");
                try {
                    Long requestId = Long.parseLong(strSplitByComma[0]);
                    logger.info("\n The returned requestId from IMS is : '"
                            + requestId + "'");
                    imsLogger.info("\n The returned requestId from IMS is : '"
                            + requestId + "'");
                    HashMap<String, String> citizenFamilyInfoMap = new HashMap<String, String>();
                    requestIds.add(requestId);
                    citizenFamilyInfoMap.put(strSplitByComma[0],
                            strSplitByComma[1]);
                    resultMap.put(requestId, citizenFamilyInfoMap);
                } catch (Exception e) {
                    logger.info(e.getMessage(), e);
                    imsLogger.info(e.getMessage(), e);
                    // Do nothing
                }
            }

            /**
             * Gains the family information belongs to the requestIds which have
             * been achieved in the loop above
             */
            if (!requestIds.isEmpty()) {

                for (String strInEachRow : strDataSplitByEnter) {
                    boolean checkFlag = true;

                    String[] strSplitByComma = strInEachRow.split(",");
                    String[] strSplitByMother = strInEachRow.split("m");
                    if (strSplitByMother != null
                            && strSplitByMother.length == 2) {
                        Long motherRequestId = Long
                                .parseLong(strSplitByMother[0]);
                        if (requestIds.contains(motherRequestId)) {
                            resultMap.get(motherRequestId).put(
                                    strSplitByComma[0], strSplitByComma[1]);
                        }
                        checkFlag = false;
                    }

                    if (checkFlag) {
                        String[] strSplitByFather = strInEachRow.split("f");
                        if (strSplitByFather != null
                                && strSplitByFather.length == 2) {
                            Long fatherRequestId = Long
                                    .parseLong(strSplitByFather[0]);
                            if (requestIds.contains(fatherRequestId)) {
                                resultMap.get(fatherRequestId).put(
                                        strSplitByComma[0], strSplitByComma[1]);
                            }
                            checkFlag = false;
                        }
                    }

                    if (checkFlag) {
                        String[] strSplitByChild = strInEachRow.split("c");
                        if (strSplitByChild != null
                                && strSplitByChild.length == 2) {
                            Long childRequestId = Long
                                    .parseLong(strSplitByChild[0]);
                            if (requestIds.contains(childRequestId)) {
                                resultMap.get(childRequestId).put(
                                        strSplitByComma[0], strSplitByComma[1]);
                            }
                            checkFlag = false;
                        }
                    }

                    if (checkFlag) {
                        String[] strSplitBySpouse = strInEachRow.split("s");
                        if (strSplitBySpouse != null
                                && strSplitBySpouse.length == 2) {
                            Long spouseRequestId = Long
                                    .parseLong(strSplitBySpouse[0]);
                            if (requestIds.contains(spouseRequestId)) {
                                resultMap.get(spouseRequestId).put(
                                        strSplitByComma[0], strSplitByComma[1]);
                            }
                        }
                    }
                }

                updateCardRequestsAfterGettingBatchEnquiryResult(resultMap);

            } else {
                logger.info(BizExceptionCode.MMS_016,
                        BizExceptionCode.MMS_014_MSG);
                imsLogger.info(BizExceptionCode.MMS_016,
                        BizExceptionCode.MMS_014_MSG);
            }
        }
    }

    @Override
    public Integer getRequestCountToFetchFromAFIS() throws BaseException {
        return getCardRequestHistoryDAO().getRequestCountToFetchFromAFIS();
    }

    @Override
    public void getUpdatedCitizenResult(Integer from) throws BaseException {
        logger.info("\nThe method 'getUpdatedCitizenResult has been started...'");
        imsLogger
                .info("\nThe method 'getUpdatedCitizenResult has been started...'");
        List<CardRequestTO> cardRequestTOs = getCardRequestDAO()
                .findByCardRequestStateByPriority(CardRequestState.SENT_TO_AFIS, from, 1);
        if (!EmsUtil.checkListSize(cardRequestTOs)) {
            logger.error(BizExceptionCode.MMS_049, BizExceptionCode.GLB_021_MSG);
            imsLogger.error(BizExceptionCode.MMS_049,
                    BizExceptionCode.GLB_021_MSG);
            throw new ServiceException(BizExceptionCode.MMS_049,
                    BizExceptionCode.GLB_021_MSG);
        }
        CardRequestTO crq = cardRequestTOs.get(0);
        String messageRequestId = getCardRequestHistoryDAO()
                .findSentToAFISMessageRequestId(crq.getId());
        logger.info("\nMethod : getUpdatedCitizenResult, Field : {name : messageRequestId; value : "
                + messageRequestId
                + "; dependantCardRequestId : "
                + crq.getId() + "}");
        imsLogger
                .info("\nMethod : getUpdatedCitizenResult, Field : {name : messageRequestId; value : "
                        + messageRequestId
                        + "; dependantCardRequestId : "
                        + crq.getId() + "}");
        if (EmsUtil.checkString(messageRequestId)) {
            CardRequestState oldState = crq.getState();
            List<Long> crqIds = new ArrayList<Long>();
            crqIds.add(crq.getId());
            List<IMSUpdateResultVTO> imsUpdateResultVTOList = null;
            boolean exceptionFlag = false;
            try {
                imsUpdateResultVTOList = getUpdatedCitizensInfoResult(messageRequestId);
            } catch (BaseException e) {
                exceptionFlag = true;
                logger.error(
                        "\nException has happened after calling the method of 'getUpdatedCitizensInfoResult'."
                                + e.getExceptionCode(), e.getMessage(), e);
                imsLogger
                        .error("\nException has happened after calling the method of 'getUpdatedCitizensInfoResult'."
                                + e.getExceptionCode(), e.getMessage(), e);

                // Creating new request history by the action of
                // 'AFIS_RECEIVE_ERROR'
                String result = (SystemId.IMS.name() + ":"
                        + ConstValues.GAM_ERROR_WITH_UNLIMITED_RETRY
                        + e.getExceptionCode() + ":" + e.getMessage());
                createCardRequestHistory(crqIds, messageRequestId, result,
                        CardRequestHistoryAction.AFIS_RECEIVE_ERROR);

                // Checking acceptability of the retry request
                // List<Long> requestIdListForIMSError = new ArrayList<Long>();
                // if
                // (!getCardRequestHistoryDAO().checkAcceptabilityOfRetryRequest(crq,
                // SystemId.IMS)) {
                // logger.info("\nThe acceptability of the retry request for getUpdatedCitizensInfoResult has been checked. The return value is false");
                // imsLogger.info("\nThe acceptability of the retry request for getUpdatedCitizensInfoResult has been checked. The return value is false");
                // requestIdListForIMSError.add(crq.getId());
                // }
                // if (EmsUtil.checkListSize(requestIdListForIMSError)) {
                // crq.setState(CardRequestState.IMS_ERROR);
                // logger.info("\nThe card request has been updated. Details : {crqId : "
                // + crq.getId() + "; old_state : " + oldState.name() +
                // " ; new_state : " + crq.getState().name() + "}");
                // imsLogger.info("\nThe card request has been updated. Details : {crqId : "
                // + crq.getId() + "; old_state : " + oldState.name() +
                // " ; new_state : " + crq.getState().name() + "}");
                // }
            }

            if (!exceptionFlag && EmsUtil.checkListSize(imsUpdateResultVTOList)) {
                for (IMSUpdateResultVTO imsUpdateResultVTO : imsUpdateResultVTOList) {

                    if (!EmsUtil.checkString(imsUpdateResultVTO.getErrorCode())) {

                        // Updating cardRequestState to 'APPROVED_BY_AFIS'
                        crq.setState(CardRequestState.APPROVED_BY_AFIS);
                        logger.info("\nThe card request has been updated. Details : {crqId : "
                                + crq.getId()
                                + "; old_state : "
                                + oldState.name()
                                + " ; new_state : "
                                + crq.getState().name() + "}");
                        imsLogger
                                .info("\nThe card request has been updated. Details : {crqId : "
                                        + crq.getId()
                                        + "; old_state : "
                                        + oldState.name()
                                        + " ; new_state : "
                                        + crq.getState().name() + "}");
                        // Creating new request history by the action of
                        // 'APPROVED_BY_AFIS'
                        createCardRequestHistory(crqIds,
                                imsUpdateResultVTO.getRequestID(),
                                CardRequestHistoryAction.APPROVED_BY_AFIS
                                        .name(),
                                CardRequestHistoryAction.APPROVED_BY_AFIS);

                        setIdentityChangeAndAfisState(crq, imsUpdateResultVTO);
                    } else {
                        logger.error(imsUpdateResultVTO.getErrorCode(),
                                imsUpdateResultVTO.getErrorMessage());
                        imsLogger.error(imsUpdateResultVTO.getErrorCode(),
                                imsUpdateResultVTO.getErrorMessage());
                        String result;
                        if (IMS_UPDT_000004.equals(imsUpdateResultVTO
                                .getErrorCode())) {

                            // Updating cardRequestState to 'IMS_ERROR'

                            crq.setState(CardRequestState.IMS_ERROR);
                            logger.info("\nThe card request has been updated. Details : {crqId : "
                                    + crq.getId()
                                    + "; old_state : "
                                    + oldState.name()
                                    + " ; new_state : "
                                    + crq.getState().name() + "}");
                            imsLogger
                                    .info("\nThe card request has been updated. Details : {crqId : "
                                            + crq.getId()
                                            + "; old_state : "
                                            + oldState.name()
                                            + " ; new_state : "
                                            + crq.getState().name() + "}");

                            // Creating new request history by the action of
                            // 'AFIS_RECEIVE_ERROR'
                            result = (SystemId.IMS.name() + ":"
                                    + ConstValues.GAM_ERROR_WITH_NO_RETRY
                                    + imsUpdateResultVTO.getErrorCode() + ":" + imsUpdateResultVTO
                                    .getErrorMessage());
                            createCardRequestHistory(crqIds,
                                    imsUpdateResultVTO.getRequestID(), result,
                                    CardRequestHistoryAction.AFIS_RECEIVE_ERROR);

                        } else {

                            // Updating cardRequestState to 'IMS_ERROR'
                            crq.setState(CardRequestState.IMS_ERROR);
                            logger.info("\nThe card request has been updated. Details : {crqId : "
                                    + crq.getId()
                                    + "; old_state : "
                                    + oldState.name()
                                    + " ; new_state : "
                                    + crq.getState().name() + "}");
                            imsLogger
                                    .info("\nThe card request has been updated. Details : {crqId : "
                                            + crq.getId()
                                            + "; old_state : "
                                            + oldState.name()
                                            + " ; new_state : "
                                            + crq.getState().name() + "}");

                            // Updating request history by the action of
                            // 'AFIS_RECEIVE_ERROR'
                            // TODO : This part is temporary assumed as
                            // GAM_ERROR_WITH_NO_RETRY
                            result = (SystemId.IMS.name() + ":"
                                    + ConstValues.GAM_ERROR_WITH_NO_RETRY
                                    + imsUpdateResultVTO.getErrorCode() + ":" + imsUpdateResultVTO
                                    .getErrorMessage());
                            createCardRequestHistory(crqIds,
                                    imsUpdateResultVTO.getRequestID(), result,
                                    CardRequestHistoryAction.AFIS_RECEIVE_ERROR);
                        }
                    }
                }
            }
        } else {
            throw new ServiceException(BizExceptionCode.MMS_051,
                    BizExceptionCode.MMS_051_MSG);
        }
    }

    @Override
    public Long findRequestsCountByState(CardRequestState requestState)
            throws BaseException {
        return getCardRequestDAO().findRequestsCountByState(requestState);
    }

    //Anbari:IMS
    @Override
    public List<Long> findAfisResultRequestsCountByState(CardRequestState sentToAfis, Integer fetchLimit)
            throws BaseException {
        return getCardRequestDAO().findAfisResultRequestsCountByState(sentToAfis, fetchLimit);
    }

    /**
     * Author ganjyar
     * This method is called by the system JOB to retry those requests which have not verified by IMS yet
     */
//	@Override
//	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//	public void updateBatchOfCitizenInfoByEstelam2(List<Long> requestIds) throws BaseException {
//		for (Long cardRequestId : requestIds) {
//			try {
//				sessionContext.getBusinessObject(IMSManagementServiceLocal.class).doEstelam2(cardRequestId);
//			} catch (Exception e) {
//				logger.error(BizExceptionCode.MMS_061 + " : " + BizExceptionCode.MMS_061_MSG + cardRequestId, e);
//				estelam2Logger.error(BizExceptionCode.MMS_061 + " : " + BizExceptionCode.MMS_061_MSG + cardRequestId, e);
//			}
//		}
//	}


//	@Override
//	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//	public void updateBatchOfCitizenInfoByEstelam2(List<Long> requestIds) throws BaseException {
//		for (Long cardRequestId : requestIds) {
//			try {
//				sessionContext.getBusinessObject(IMSManagementServiceLocal.class).doEstelam2(cardRequestId);
//			} catch (Exception e) {
//				logger.error(BizExceptionCode.MMS_061 + " : " + BizExceptionCode.MMS_061_MSG + cardRequestId, e);
//				estelam2Logger.error(BizExceptionCode.MMS_061 + " : " + BizExceptionCode.MMS_061_MSG + cardRequestId, e);
//			}
//		}
//	}


    //Anbari:Estelam3
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void updateBatchOfCitizenInfoByEstelam3(List<Long> requestIds)
            throws BaseException {
        for (Long cardRequestId : requestIds) {
            try {
                sessionContext.getBusinessObject(
                        IMSManagementServiceLocal.class).doEstelam3(
                        cardRequestId, true);// filed true because of
                // persistance of getestelam3sc
                // image
            } catch (Exception e) {
                logger.error(BizExceptionCode.MMS_061 + " : "
                        + BizExceptionCode.MMS_061_MSG + cardRequestId, e);
                estelam2Logger.error(BizExceptionCode.MMS_061 + " : "
                        + BizExceptionCode.MMS_061_MSG + cardRequestId, e);
            }
        }
    }


    /**
     * Author ganjyar
     * This method is called just after reservations move to the EMS for doing enquire from IMS
     */
//	@Override
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
//	public void doEstelam2(Long cardRequestId) throws BaseException {
//		if (cardRequestId != null) {
//			try {
//				sessionContext.getBusinessObject(IMSManagementServiceLocal.class).updateInfoByGetEstelam2(cardRequestId);
//			} catch (Exception e) {
//				logger.error(BizExceptionCode.MMS_062 + "	 : " + BizExceptionCode.MMS_062_MSG + cardRequestId, e);
//				estelam2Logger.error(BizExceptionCode.MMS_062 + " : " + BizExceptionCode.MMS_062_MSG + cardRequestId, e);
//				sessionContext.setRollbackOnly();
//				throw new BaseException(BizExceptionCode.MMS_062, BizExceptionCode.MMS_062_MSG + cardRequestId);
//			}
//		}
//	}


    //Anbari:Estelam3
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PersonEnquiryVTO doEstelam3(Long cardRequestId,
                                       Boolean isImageRequested) throws BaseException {
        PersonEnquiryVTO personEnquiryVTO = new PersonEnquiryVTO();
        if (cardRequestId != null) {
            try {
                sessionContext.getBusinessObject(IMSManagementServiceLocal.class).updateInfoByGetEstelam3(cardRequestId, isImageRequested);
            } catch (Exception e) {

                logger.error(BizExceptionCode.MMS_066 + " : "
                        + BizExceptionCode.MMS_064_MSG + cardRequestId, e);
                estelam2Logger.error(BizExceptionCode.MMS_066 + " : "
                        + BizExceptionCode.MMS_064_MSG + cardRequestId, e);
                getCardRequestHistoryDAO().create(new CardRequestTO(cardRequestId), BizExceptionCode.MMS_066 + ":" + BizExceptionCode.MMS_062_MSG, SystemId.IMS, null, CardRequestHistoryAction.IMS_ESTELAM_RETRY, null);
                //sessionContext.setRollbackOnly();


//				if (e instanceof BaseException) {
//					try {
//						String exceptionCode = ((BaseException) e)
//								.getExceptionCode();
//						CardRequestTO cardRequestTO = getCardRequestDAO()
//								.fetchCardRequest(cardRequestId);
//						getCardRequestHistoryDAO().create(cardRequestTO,
//								e.getMessage(),
//								SystemId.IMS, null,
//								CardRequestHistoryAction.NOT_VERIFIED_BY_IMS, null);
//
//						if (exceptionCode != null
//								&& BizExceptionCode.NOCR_IMS_05
//										.equals(exceptionCode)) {
//
//							// sessionContext.getBusinessObject(CardRequestServiceLocal.class).doRepealCardRequest(cardRequestTO);
//							if(cardRequestTO.getState()!=CardRequestState.READY_TO_DELIVER)
//							{
//							cardRequestTO
//									.setRequestedAction(CardRequestedAction.REPEAL_ACCEPTED);
//							getCardRequestDAO().update(cardRequestTO);
//							// personEnquiryVTO.setIsExceptionMessage(true);// for
//							// call revoke card in CMS
//							}
//						} else if (exceptionCode != null
//								&& BizExceptionCode.NOCR_IMS_02
//										.equals(exceptionCode)) {
//							cardRequestTO.setEstelam2Flag(Estelam2FlagType.N);
//							getCardRequestDAO().update(cardRequestTO);
//
//						}
//					} catch (Exception e1) {
//						sessionContext.setRollbackOnly();
//					}
//
//				} else {
//					logger.error(BizExceptionCode.MMS_062 + " : "
//							+ BizExceptionCode.MMS_062_MSG + cardRequestId, e);
//					estelam2Logger.error(BizExceptionCode.MMS_062 + " : "
//							+ BizExceptionCode.MMS_062_MSG + cardRequestId, e);
//					sessionContext.setRollbackOnly();
//				}

            }
        }
        return personEnquiryVTO;
    }


    // Anbari:Estelam3 Without citizen update
    //In progress exception
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PersonEnquiryVTO doEstelam3WithoutCitizenInfoUpdate(
            Long cardRequestId) throws BaseException {
        PersonEnquiryVTO personEnquiryVTO = new PersonEnquiryVTO();
        if (cardRequestId != null) {
            try {
                personEnquiryVTO = sessionContext.getBusinessObject(
                        IMSManagementServiceLocal.class)
                        .updateInfoByGetEstelam3WithoutCitizenInfoUpdate(
                                cardRequestId);
            } catch (Exception e) {

                logger.error(BizExceptionCode.MMS_062 + " : "
                        + BizExceptionCode.MMS_062_MSG + cardRequestId, e);
                estelam2Logger.error(BizExceptionCode.MMS_062 + " : "
                        + BizExceptionCode.MMS_062_MSG + cardRequestId, e);
                getCardRequestHistoryDAO().create(new CardRequestTO(cardRequestId), BizExceptionCode.MMS_062 + ":" + BizExceptionCode.MMS_062_MSG, SystemId.IMS, null, CardRequestHistoryAction.IMS_ESTELAM_RETRY, null);
                //sessionContext.setRollbackOnly();

            }

        }

        return personEnquiryVTO;
    }


//	/**
//	 * Author ganjyar
//	 * This main method for doing enquire from IMS
//	 */
//	@Override
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
//	public void updateInfoByGetEstelam2(Long emsCardRequestId)
//			throws BaseException {
//
//		List<IMSEnquiryVTO> imsEnquiryVTOList = fetchListForEnquiryReqByRequestId(emsCardRequestId);
//		if (!EmsUtil.checkListSize(imsEnquiryVTOList)) {
//			return ;//TODO log exception
//		}
//
//		IMSEnquiryVTO imsEnquiryVTO = imsEnquiryVTOList.get(0);
//
//		String citizenInfo = imsEnquiryVTO.getCitizenInfo();
//		Object[] keys = citizenInfo.split("\n");
//
//		// Object[] keys = imsEnquiryVTOList.toArray();
//		StringBuilder metadata = new StringBuilder();
////		try {
////			asyncCounter++;
////			System.err.println(asyncCounter);
////			Thread.sleep(30000);
////			asyncCounter--;
////		} catch (InterruptedException e1) {
////			// TODO Auto-generated catch block
////			e1.printStackTrace();
////		}
//		List<PersonEnquiryVTO> personEnquiryVTOs = new ArrayList<PersonEnquiryVTO>();
//		CitizenTO ctz = null;
//		Long reqId = null;
//		boolean fatherFlag = false;
//		boolean motherFlag = false;
//		boolean spouseFlag = false;
//		boolean childrenFlag = false;
//		for (Object key : keys) {
//			if (key.toString().contains("f")) {
//				fatherFlag = true;
//			} else if (key.toString().contains("m")) {
//				motherFlag = true;
//			} else if (key.toString().contains("s")) {
//				spouseFlag = true;
//			} else if (key.toString().contains("c")) {
//				childrenFlag = true;
//			}
////			 try {
////			 reqId = Long.parseLong(key.toString());
////			 } catch (Exception e) {
////			 logger.error(e.getMessage(), e);
////			 imsLogger.error(e.getMessage(), e);
////			 // Do nothing
////			 }
//		}
//
//		reqId = imsEnquiryVTO.getRequestId();
//		if (reqId != null) {
//			ctz = getCitizenDAO().findByRequestId(reqId);
//
//			// TODO :write a query for fetch requests which have state
//			// "reserved"
//			// and estelam state "false"
//			// ctz = getCitizenDAO().findByNID("4647689484").get(0);
//
//		CitizenInfoTO czi = ctz.getCitizenInfo();
//
//		// Enquiry for CITIZEN
//		personEnquiryVTOs.add(new PersonEnquiryVTO(ctz.getNationalID(), // national
//																		// id
//				ctz.getFirstNamePersian(), // first name
//				ctz.getSurnamePersian(), // surname
//				czi.getFatherFirstNamePersian(), // father first name
//				null, // birth certificate series
//				ConstValues.DEFAULT_CERT_SERIAL, // birth certificate serial
//				czi.getBirthCertificateId(), // birth certificate id
//				czi.getBirthDateSolar(), // birth date
//				czi.getGender())); // gender
//
//		// Enquiry for citizen FATHER
//		if (fatherFlag) {
//			personEnquiryVTOs.add(new PersonEnquiryVTO(czi
//					.getFatherNationalID(), // national id
//					czi.getFatherFirstNamePersian(), // first name
//					czi.getFatherSurname(), // surname
//					czi.getFatherFatherName(), // father first name
//					null, // birth certificate seri
//					ConstValues.DEFAULT_CERT_SERIAL, // birth certificate
//					// serial
//					czi.getFatherBirthCertificateId(), // birth certificate
//														// id
//					DateUtil.convert(czi.getFatherBirthDateSolar(),
//							DateUtil.JALALI), Gender.M)); // birth date
//		}
//
//		// Enquiry for citizen MOTHER
//		if (motherFlag) {
//			personEnquiryVTOs.add(new PersonEnquiryVTO(czi
//					.getMotherNationalID(), // national id
//					czi.getMotherFirstNamePersian(), // first name
//					czi.getMotherSurname(), // surname
//					czi.getMotherFatherName(), // father first name
//					null, // birth certificate seri
//					ConstValues.DEFAULT_CERT_SERIAL, // birth certificate
//					// serial
//					czi.getMotherBirthCertificateId(), // birth certificate
//														// id
//					DateUtil.convert(czi.getMotherBirthDateSolar(),
//							DateUtil.JALALI), Gender.F)); // birth date
//		}
//
//		if (spouseFlag) {
//			for (SpouseTO sps : czi.getSpouses()) {
//				// Spouse Gender
//				Gender spouseGender = Gender.UNDEFINED;
//				switch (czi.getGender()) {
//				case M:
//					spouseGender = Gender.F;
//					break;
//
//				case F:
//					spouseGender = Gender.M;
//					break;
//				}
//				// Enquiry for citizen SPOUSE
//				personEnquiryVTOs.add(new PersonEnquiryVTO(sps
//						.getSpouseNationalID(), // national
//						// id
//						sps.getSpouseFirstNamePersian(), // first name
//						sps.getSpouseSurnamePersian(), // surname
//						sps.getSpouseFatherName(), // father first name
//						null, // birth certificate seri
//						ConstValues.DEFAULT_CERT_SERIAL, // birth
//															// certificate
//						// serial
//						sps.getSpouseBirthCertificateId(), // birth
//															// certificate
//						// id
//						DateUtil.convert(sps.getSpouseBirthDate(),
//								DateUtil.JALALI), // birth date
//						spouseGender)); // Gender
//			}
//		}
//
//		if (childrenFlag) {
//			for (ChildTO chi : czi.getChildren()) {
//				// Enquiry for citizen CHILD
//				personEnquiryVTOs.add(new PersonEnquiryVTO(chi
//						.getChildNationalID(), // national
//						// id
//						chi.getChildFirstNamePersian(), // first name
//						null, // surname
//						// needs to be added later,
//						// currently no surname is
//						// provided for child
//						chi.getChildFatherName(), // father first name
//						null, // birth certificate seri
//						ConstValues.DEFAULT_CERT_SERIAL, // birth
//															// certificate
//						// serial
//						chi.getChildBirthCertificateId(), // birth
//															// certificate
//						// id
//						DateUtil.convert(chi.getChildBirthDateSolar(),
//								DateUtil.JALALI), // birth date
//						chi.getChildGender())); // Gender
//			}
//		}
//		IMSProxyProvider imsProxyProvider = selectIMSProvider(ONLINE_ENQUIRY);
//
//		IMSService imsService = getIMSService();
//		Map<String, PersonEnquiryVTO> returnMap = imsService
//				.fetchDataByOnlineEnquiryByEstelam2(
//						personEnquiryVTOs
//				                .toArray(new PersonEnquiryVTO[personEnquiryVTOs
//				        	  			.size()]), ctz.getNationalID(),
//						imsProxyProvider);
//		logger.info("\n Updating citizen data with nationalId ' "
//				+ ctz.getNationalID()
//				+ "' in fetchIMSDataForBatchEnquiry method ---------------------------------\n");
//		estelam2Logger
//				.info("\n Updating citizen data with nationalId ' "
//						+ ctz.getNationalID()
//						+ "' in fetchIMSDataForBatchEnquiry method ---------------------------------\n");
//		PersonEnquiryVTO personEnquiryVTO = null;
//		for (Object key : keys) {
//			String strKey = key.toString();
//			String nId = null;
//			if (strKey.contains("m")) {
//				String nIdStr = strKey.split("m")[1];
//				nId = nIdStr.split(",")[0];
//				personEnquiryVTO = returnMap.get(nId);
//				if (EmsUtil.checkString(personEnquiryVTO.getMetadata())) {
//					metadata.append(strKey).append("-");
//					logEstelamFailed("Mother", reqId, ctz.getNationalID(), nId);
//					if(EmsUtil.checkString(personEnquiryVTO.getLogInfo()))
//						{
//
//							if(personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_001) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_002))
//							{
//								logServiceDown();
////								sessionContext.setRollbackOnly();
//								throw new RuntimeException();
//							}
//							else if(personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_008) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_029))
//							{
//								logPersonNotFound("Mother");
//							}
//						}
//					czi.setMotherSurname(ConstValues.DEFAULT_NAMES_FA);
//					czi.setMotherBirthCertificateId(ConstValues
//							.DEFAULT_NUMBER);
//					czi.setMotherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
//					czi.setMotherNationalID(ConstValues.DEFAULT_NID);
//					czi.setMotherFatherName(ConstValues.DEFAULT_NAMES_FA);
//					 try {
//			                 czi.setMotherBirthDateSolar(DateUtil.convert(ConstValues.DEFAULT_DATE, DateUtil.JALALI));
//			            } catch (DateFormatException e) {
//			                throw new BaseException(BizExceptionCode.MMS_059, BizExceptionCode.MMS_059_MSG, e);
//			            }
//				} else {
//					czi.setMotherFirstNamePersian(personEnquiryVTO
//							.getFirstName());
//					czi.setMotherSurname(personEnquiryVTO.getLastName());
//					czi.setMotherBirthCertificateId(personEnquiryVTO
//							.getBirthCertificateId());
//					czi.setMotherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
//					try {
//						czi.setMotherBirthDateSolar(DateUtil.convert(
//								personEnquiryVTO.getSolarBirthDate(),
//								DateUtil.JALALI));
//					} catch (DateFormatException e) {
//						throw new ServiceException(BizExceptionCode.MMS_053,
//								BizExceptionCode.MMS_029_MSG, e);
//					}
//					czi.setMotherFatherName(personEnquiryVTO.getFatherName());
//
//					logger.info("\nMother info with national id : "
//							+ czi.getMotherNationalID() + "\n");
//					estelam2Logger.info("\nMother info with national id : "
//							+ czi.getMotherNationalID() + "\n");
//					logger.info("\nMother FirstName : "
//							+ czi.getMotherFirstNamePersian() + "\n");
//					estelam2Logger.info("\nMother FirstName : "
//						    + czi.getMotherFirstNamePersian() + "\n");
//					logger.info("\nMother LastName : " + czi.getMotherSurname()
//							+ "\n");
//					estelam2Logger.info("\nMother LastName : "
//							+ czi.getMotherSurname() + "\n");
//					logger.info("\nMother BirthCertId : "
//							+ czi.getMotherBirthCertificateId() + "\n");
//					estelam2Logger.info("\nMother BirthCertId : "
//							+ czi.getMotherBirthCertificateId() + "\n");
//					logger.info("\nMother BirthCertSerial : "
//							+ czi.getMotherBirthCertificateSeries() + "\n");
//					estelam2Logger.info("\nMother BirthCertSerial : "
//							+ czi.getMotherBirthCertificateSeries() + "\n");
//					logger.info("\nMother BirthDateSolar : "
//							+ czi.getMotherBirthDateSolar() + "\n");
//					estelam2Logger.info("\nMother BirthDateSolar : "
//							+ czi.getMotherBirthDateSolar() + "\n");
//				}
//
//			} else if (strKey.contains("f")) {
//				String nIdStr = strKey.split("f")[1];
//				nId = nIdStr.split(",")[0];
//				personEnquiryVTO = returnMap.get(nId);
//				if (EmsUtil.checkString(personEnquiryVTO.getMetadata())) {
//					metadata.append(strKey).append("-");
//
//					logEstelamFailed("Father", reqId, ctz.getNationalID(), nId);
//
//					if(EmsUtil.checkString(personEnquiryVTO.getLogInfo()))
//					{
//
//						if(personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_001) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_002))
//						{
//							logServiceDown();
//							throw new RuntimeException();
////							sessionContext.setRollbackOnly();
//						}
//						else if(personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_008) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_029))
//						{
//							logPersonNotFound("Father");
//						}
//
//					}
//					czi.setFatherFatherName(ConstValues.DEFAULT_NAMES_FA);
//					czi.setFatherSurname(ConstValues.DEFAULT_NAMES_FA);
//					czi.setFatherBirthCertificateId(ConstValues.DEFAULT_NUMBER);
//					czi.setFatherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
//					czi.setFatherNationalID(ConstValues.DEFAULT_NID);
//					try {
//		                 czi.setFatherBirthDateSolar(DateUtil.convert(ConstValues.DEFAULT_DATE, DateUtil.JALALI));
//		            } catch (DateFormatException e) {
//		                throw new BaseException(BizExceptionCode.MMS_060, BizExceptionCode.MMS_060_MSG, e);
//		            }
//
//				} else {
//					czi.setFatherSurname(personEnquiryVTO.getLastName());
//					czi.setFatherBirthCertificateId(personEnquiryVTO
//							.getBirthCertificateId());
//					czi.setFatherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
//					try {
//						czi.setFatherBirthDateSolar(DateUtil.convert(
//								personEnquiryVTO.getSolarBirthDate(),
//								DateUtil.JALALI));
//					} catch (DateFormatException e) {
//						throw new ServiceException(BizExceptionCode.MMS_054,
//								BizExceptionCode.MMS_029_MSG, e);
//					}
//					czi.setFatherFatherName(personEnquiryVTO.getFatherName());
//
//					logger.info("\nFather info with national id : "
//							+ czi.getFatherNationalID() + "\n");
//					estelam2Logger.info("\nFather info with national id : "
//							+ czi.getFatherNationalID() + "\n");
//					logger.info("\nFather FirstName : "
//							+ czi.getFatherFirstNamePersian() + "\n");
//					estelam2Logger.info("\nFather FirstName : "
//							+ czi.getFatherFirstNamePersian() + "\n");
//					logger.info("\nFather LastName : " + czi.getFatherSurname()
//							+ "\n");
//					estelam2Logger.info("\nFather LastName : "
//							+ czi.getFatherSurname() + "\n");
//					logger.info("\nFather BirthCertId : "
//							+ czi.getFatherBirthCertificateId() + "\n");
//					estelam2Logger.info("\nFather BirthCertId : "
//							+ czi.getFatherBirthCertificateId() + "\n");
//					logger.info("\nFather BirthCertSerial : "
//							+ czi.getFatherBirthCertificateSeries() + "\n");
//					estelam2Logger.info("\nFather BirthCertSerial : "
//							+ czi.getFatherBirthCertificateSeries() + "\n");
//					logger.info("\nFather BirthDateSolar : "
//							+ czi.getFatherBirthDateSolar() + "\n");
//					estelam2Logger.info("\nFather BirthDateSolar : "
//							+ czi.getFatherBirthDateSolar() + "\n");
//				}
//			} else if (strKey.contains("s")) {
//				String nIdStr = strKey.split("s")[1];
//				nId = nIdStr.split(",")[0];
//				personEnquiryVTO = returnMap.get(nId);
//				if (EmsUtil.checkString(personEnquiryVTO.getMetadata())) {
//					metadata.append(strKey).append("-");
//
//					logEstelamFailed("Spouse", reqId, ctz.getNationalID(), nId);
//					if(EmsUtil.checkString(personEnquiryVTO.getLogInfo()))
//						{
//						if (personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_001) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_002))
//						{
//							logServiceDown();
////							sessionContext.setRollbackOnly();
//							throw new RuntimeException();
//						}
//						else if(personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_008) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_029))
//						{
//							logPersonNotFound("Spouse");
//
//						}
//						}
//						SpouseDAO spousDAO = getSpousDAO();
//						for (SpouseTO spouseTO : czi.getSpouses()) {
//								if (spouseTO.getSpouseNationalID().equals(nId)) {
//									czi.getSpouses().remove(spouseTO);
//									spousDAO.delete(spouseTO);
//									break;
//								}
//							}
//
//
//
//				} else {
//					for (SpouseTO spouseTO : czi.getSpouses()) {
//						if (spouseTO.getSpouseNationalID().equals(nId)) {
//
//							spouseTO.setSpouseFirstNamePersian(personEnquiryVTO
//									.getFirstName());
//							spouseTO.setSpouseSurnamePersian(personEnquiryVTO
//									.getLastName());
//							spouseTO.setSpouseBirthCertificateId(personEnquiryVTO
//									.getBirthCertificateId());
//							//
////							spouseTO.setSpouseBirthCertificateSeries(personEnquiryVTO
////								 	.getBirthCertificateSerial());
//							spouseTO.setSpouseBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
//							spouseTO.setSpouseFatherName(personEnquiryVTO
//									.getFatherName());
//							try {
//								spouseTO.setSpouseBirthDate(DateUtil.convert(
//										personEnquiryVTO.getSolarBirthDate(),
//										DateUtil.JALALI));
//							} catch (DateFormatException e) {
//								throw new ServiceException(
//										BizExceptionCode.MMS_055,
//										BizExceptionCode.MMS_029_MSG, e);
//							}
//
//							logger.info("\nSpouse info with national id : "
//									+ nId + "\n");
//							estelam2Logger
//									.info("\nSpouse info with national id : "
//											+ nId + "\n");
//							logger.info("\nSpouse FirstName is : "
//									+ spouseTO.getSpouseFirstNamePersian()
//									+ "\n");
//							estelam2Logger.info("\nSpouse FirstName is : "
//									+ spouseTO.getSpouseFirstNamePersian()
//									+ "\n");
//							logger.info("\nSpouse FatherName is : "
//									+ spouseTO.getSpouseFatherName() + "\n");
//							estelam2Logger.info("\nSpouse FatherName is : "
//									+ spouseTO.getSpouseFatherName() + "\n");
//							logger.info("\nSpouse BirthCertId is : "
//									+ spouseTO.getSpouseBirthCertificateId()
//									+ "\n");
//							estelam2Logger.info("\nSpouse BirthCertId is : "
//									+ spouseTO.getSpouseBirthCertificateId()
//									+ "\n");
//							logger.info("\nSpouse BirthCertSerial is : "
//									+ spouseTO
//											.getSpouseBirthCertificateSeries()
//									+ "\n");
//							estelam2Logger
//									.info("\nSpouse BirthCertSerial is : "
//											+ spouseTO
//													.getSpouseBirthCertificateSeries()
//											+ "\n");
//							logger.info("\nSpouse BirthDate is : "
//									+ spouseTO.getSpouseBirthDate() + "\n");
//							estelam2Logger.info("\nSpouse BirthDate is : "
//									+ spouseTO.getSpouseBirthDate() + "\n");
//						}
//					}
//				}
//
//			} else if (strKey.contains("c")) {
//				String nIdStr = strKey.split("c")[1];
//				nId = nIdStr.split(",")[0];
//				personEnquiryVTO = returnMap.get(nId);
//				if (EmsUtil.checkString(personEnquiryVTO.getMetadata())) {
//					metadata.append(strKey).append("-");
//					logEstelamFailed("Child", reqId, ctz.getNationalID(), nId);
//					if(EmsUtil.checkString(personEnquiryVTO.getLogInfo()))
//					{
//						if(personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_001) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_002))
//							{
//						logServiceDown();
////						sessionContext.setRollbackOnly();
//						throw new RuntimeException();
//					}
//					else if(personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_008) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_029))
//					{
//						logPersonNotFound("child");
//
//					}
//
//					}
//					ChildDAO childDAO = getChildDAO();
//					for (ChildTO childTO : czi.getChildren()) {
//						if (childTO.getChildNationalID().equals(nId)) {
////								childTO.setCitizenInfo(null);
//								czi.getChildren().remove(childTO);
//								childDAO.delete(childTO);
//								break;
//							}
//						}
//
//
//				} else {
//					for (ChildTO childTO : czi.getChildren()) {
//						if (childTO.getChildNationalID().equals(nId)) {
//
//							childTO.setChildFirstNamePersian(personEnquiryVTO
//									.getFirstName());
//							childTO.setChildBirthCertificateId(personEnquiryVTO
//									.getBirthCertificateId());
//							//
////							childTO.setChildBirthCertificateSeries(personEnquiryVTO
////									.getBirthCertificateSerial());
//							childTO.setChildBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
//							childTO.setChildFatherName(personEnquiryVTO
//									.getFatherName());
//							childTO.setChildGender(personEnquiryVTO.getGender());
//							try {
//								childTO.setChildBirthDateSolar(DateUtil
//										.convert(personEnquiryVTO
//												.getSolarBirthDate(),
//												DateUtil.JALALI));
//							} catch (DateFormatException e) {
//								throw new ServiceException(
//										BizExceptionCode.MMS_056,
//										BizExceptionCode.MMS_029_MSG, e);
//							}
//							logger.info("\nChild info with national id : "
//									+ nId + "\n");
//							estelam2Logger
//									.info("\nChild info with national id : "
//											+ nId + "\n");
//							logger.info("\nChild FirstName is : "
//									+ childTO.getChildFirstNamePersian() + "\n");
//							estelam2Logger
//						    		.info("\nChild FirstName is : "
//										    + childTO
//													.getChildFirstNamePersian()
//											+ "\n");
//							logger.info("\nChild FatherName is : "
//									+ childTO.getChildFatherName() + "\n");
//							estelam2Logger.info("\nChild FatherName is : "
//									+ childTO.getChildFatherName() + "\n");
//							logger.info("\nChild BirthCertId is : "
//									+ childTO.getChildBirthCertificateId()
//									+ "\n");
//							estelam2Logger.info("\nChild BirthCertId is : "
//									+ childTO.getChildBirthCertificateId()
//									+ "\n");
//							logger.info("\nChild BirthCertSerial is : "
//									+ childTO.getChildBirthCertificateSeries()
//									+ "\n");
//							estelam2Logger.info("\nChild BirthCertSerial is : "
//									+ childTO.getChildBirthCertificateSeries()
//									+ "\n");
//							logger.info("\nChild BirthDate is : "
//									+ childTO.getChildBirthDateSolar() + "\n");
//							estelam2Logger.info("\nChild BirthDate is : "
//									+ childTO.getChildBirthDateSolar() + "\n");
//							logger.info("\nChild Gender is : "
//									+ childTO.getChildGender().name() + "\n");
//							estelam2Logger.info("\nChild Gender is : "
//									+ childTO.getChildGender().name() + "\n");
//						}
//					}
//				}
//
//			} else if (strKey.contains(reqId.toString())) {
//				personEnquiryVTO = returnMap.get(ctz.getNationalID());
//				if (EmsUtil.checkString(personEnquiryVTO.getMetadata())) {
//					metadata.append(strKey).append("-");
//					logEstelamFailed("Citizen", reqId, ctz.getNationalID(), null);
//					if(EmsUtil.checkString(personEnquiryVTO.getLogInfo()))
//					{
//						//service is down
//					if	(personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_001) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_002))
//					{
//						logServiceDown();
//						throw new RuntimeException();
////							break;
//					}
//					else
//					{
//						Estelam2FailureLogTO estelam2LogTO = new Estelam2FailureLogTO();
//						estelam2LogTO.setNationalID(ctz.getNationalID());
//						estelam2LogTO.setDescription(null);
//						estelam2LogTO.setCreateDate(new Date());
//						EstelamFailureType type=EstelamFailureType.NOT_FOUND;
//						estelam2LogTO.setDescription(personEnquiryVTO.getDescription());
//						if(personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_008) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_029))
//						{
//							logPersonNotFound("Citizen");
//							type=EstelamFailureType.NOT_FOUND;
//						}
//						else if(personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_010) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_021))
//						{
//							type=EstelamFailureType.INVAILD_CERTIFICATE_ID;
//						}
//						else if (personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_011) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_022))
//						{
//							type=EstelamFailureType.INVAILD_BIRTH_DATE;
//						}
//						else if (personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_012) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_023))
//						{
//							type=EstelamFailureType.INVAILD_CERTIFICATE_SERIAL;
//						}
//						else if (personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_013) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_024))
//						{
//							type=EstelamFailureType.INVAILD_NAME;
//						}
//						else if (personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_014) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_025))
//						{
//							type=EstelamFailureType.INVAILD_LAST_NAME;
//						}
//						else if (personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_015) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_026))
//						{
//							type=EstelamFailureType.INVAILD_FATHER_NAME;
//						}
//						else if (personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_016) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_027))
//						{
//							type=EstelamFailureType.INVAILD_GENDER;
//						}
//						else if (personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_017) || personEnquiryVTO.getLogInfo().equals(BizExceptionCode.NIO_028))
//						{
//							type=EstelamFailureType.UNKNOWN_IMS_MESSAGE;
//						}
//						estelam2LogTO.setType(type);
//						logInsertIntoEstelam2FailureTable(estelam2LogTO, null, null);
//						getEstelam2LogDAO().create(estelam2LogTO);
//						break;
//					}
//					}
//				} else {
//					ctz.setFirstNamePersian(personEnquiryVTO.getFirstName());
//					ctz.setSurnamePersian(personEnquiryVTO.getLastName());
//					czi.setBirthCertificateId(personEnquiryVTO
//							.getBirthCertificateId());
//					// TODO : check why it is sets to default
//					czi.setBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
//					czi.setFatherFirstNamePersian(personEnquiryVTO
//							.getFatherName());
////					if (!czi.getGender().equals(personEnquiryVTO.getGender())) {
////						metadata.append(strKey).append("-");
////						Estelam2FailureLogTO estelam2LogTO = new Estelam2FailureLogTO();
////						estelam2LogTO.setNationalID(ctz.getNationalID());
////						estelam2LogTO.setDescription(null);
////						estelam2LogTO.setCreateDate(new Date());
////						estelam2LogTO.setType(EstelamFailureType.NOT_GENDER_EQUAL);
////						estelam2LogTO.setDescription("IMS Gender :"+personEnquiryVTO.getGender()+";Citizen Gender :"+czi.getGender());
////
////						logInsertIntoEstelam2FailureTable(estelam2LogTO,personEnquiryVTO.getGender(),czi.getGender());
////						getEstelam2LogDAO().create(estelam2LogTO);
////					} else {
//						czi.setGender(personEnquiryVTO.getGender());
//						//Anbari: for overwrite birthdate in vip direct
//						czi.setBirthDateSolar(personEnquiryVTO.getSolarBirthDate());
////					}
//
//					logger.info("\nCitizen FirstName : "
//							+ ctz.getFirstNamePersian() + "\n");
//					estelam2Logger.info("\nCitizen FirstName : "
//							+ ctz.getFirstNamePersian() + "\n");
//					logger.info("\nCitizen LastName : "
//							+ ctz.getSurnamePersian() + "\n");
//					estelam2Logger.info("\nCitizen LastName : "
//							+ ctz.getSurnamePersian() + "\n");
//					logger.info("\nCitizen BirthCertId : "
//							+ czi.getBirthCertificateId() + "\n");
//					estelam2Logger.info("\nCitizen BirthCertId : "
//							+ czi.getBirthCertificateId() + "\n");
//					logger.info("\nCitizen BirthCertSerial : "
//							+ czi.getBirthCertificateSeries() + "\n");
//					estelam2Logger.info("\nCitizen BirthCertSerial : "
//							+ czi.getBirthCertificateSeries() + "\n");
//					logger.info("\nCitizen BirthDateSolar : "
//							+ czi.getBirthDateSolar() + "\n");
//					estelam2Logger.info("\nCitizen BirthDateSolar : "
//							+ czi.getBirthDateSolar() + "\n");
//				}
//			}
//			// }
//		}
//
//		CardRequestTO cardRequestTO = getCardRequestDAO().find(
//				CardRequestTO.class, reqId);
//
//		CardRequestHistoryAction historyAction = CardRequestHistoryAction.NOT_VERIFIED_BY_IMS;
//
//		if (EmsUtil.checkString(metadata.toString())) {
//				Boolean returnValue = false;
//				String[] items = metadata.toString().split("-");
//				for (String item : items) {
//
//					if (item.contains(ctz.getNationalID())) {
//						returnValue = true;
//						break;
//					}
//				}
//				if (returnValue)
//				{
//					historyAction = CardRequestHistoryAction.NOT_VERIFIED_BY_IMS;
////					cardRequestTO.setEstelam2Flag(BooleanType.F);
//					cardRequestTO.setEstelam2Flag(Estelam2FlagType.N);
//				}
//				else
//				{	historyAction = CardRequestHistoryAction.VERIFIED_IMS;
////					cardRequestTO.setEstelam2Flag(BooleanType.T);
//					cardRequestTO.setEstelam2Flag(Estelam2FlagType.V);
//				}
//		} else {
//
//			historyAction = CardRequestHistoryAction.VERIFIED_IMS;
////			cardRequestTO.setEstelam2Flag(BooleanType.T);
//			cardRequestTO.setEstelam2Flag(Estelam2FlagType.V);
//
//		}
//
//
//		String description="";
//		if(EmsUtil.checkString(personEnquiryVTO.getDescription()))
//		{
//		if(EmsUtil.checkMaxFieldLength(description, 255))
//			description=personEnquiryVTO.getDescription();
//		else
//			description=personEnquiryVTO.getDescription().substring(0, 254);
//		}
//		getCardRequestHistoryDAO().create(new CardRequestTO(reqId), description, SystemId.EMS, null,
//               historyAction, "");
//
//		cardRequestTO.setMetadata(metadata.toString());
//	}
//	}//

    private void logEstelamFailed(String person, Long reqId,
                                  String citizenNationalID, String nId) {
        logger.info("\n======================== Estelam Failed For " + person + "========================\n");
        estelam2Logger.info("\n======================== Estelam Failed For " + person + " ========================\n");

        logger.info("\nRequest Id : " + reqId + "\n");
        estelam2Logger.info("\nRequest Id : " + reqId + "\n");
        logger.info("\nCitizen NID : " + citizenNationalID + "\n");
        estelam2Logger.info("\nCitizen NID : " + citizenNationalID + "\n");
        if (!person.equals("Citizen")) {
            logger.info("\nCitizen " + person + " NID : " + nId + "\n");
            estelam2Logger.info("\nCitizen " + person + " NID : " + nId + "\n");
        }
    }

    private void logServiceDown() {
        logger.info("\n=====================================================================================\n");
        estelam2Logger.info("\n=====================================================================================\n");
        logger.info("\n======================== IMS Online Service Was Down ========================\n");
        estelam2Logger.info("\n======================== IMS Online Service Was Down ========================\n");
        estelam2Logger.info("\n=====================================================================================\n");
        logger.info("\n=====================================================================================\n");
    }

    private void logPersonNotFound(String person) {
        logger.info("\n=====================================================================================\n");
        estelam2Logger.info("\n=====================================================================================\n");
        logger.info("\n======================== " + person + " Not Found In Ims ========================\n");
        estelam2Logger.info("\n======================== " + person + " Not Found In Ims ========================\n");
        estelam2Logger.info("\n=====================================================================================\n");
        logger.info("\n=====================================================================================\n");

    }

    private void logInsertIntoEstelam2FailureTable(Estelam2FailureLogTO estelam2Log, Gender imsGender, Gender citizenGender) {

        logger.info("\n======================== Insert Into Estelam 2 Failure Table ========================\n");
        estelam2Logger.info("\n======================== Insert Into Estelam 2 Failure Table ========================\n");

        if (citizenGender != null && imsGender != null) {
            logger.info("\n Citizen Gender : " + citizenGender + "\n");
            estelam2Logger.info("\n Citizen Gender : " + citizenGender + "\n");
            logger.info("\n Ims Gender : " + imsGender + "\n");
            estelam2Logger.info("\n Ims Gender : " + imsGender + "\n");
        }
        logger.info("\nestelam2Log NID : " + estelam2Log.getNationalID() + "\n");
        estelam2Logger.info("\nestelam2Log NID : " + estelam2Log.getNationalID() + "\n");
        logger.info("\nestelam2Log createDate : " + estelam2Log.getCreateDate() + "\n");
        estelam2Logger.info("\nestelam2Log createDate : " + estelam2Log.getCreateDate() + "\n");
        logger.info("\nestelam2Log type : " + estelam2Log.getType() + "\n");
        estelam2Logger.info("\nestelam2Log type : " + estelam2Log.getType() + "\n");
        estelam2Logger.info("\n=====================================================================================\n");
        logger.info("\n=====================================================================================\n");
    }
//
//	@Override
//	public PersonEnquiryVTO getPersonalInfoByGetEstelam2(String nationalID)
//			throws BaseException {
//		try {
//
//		PersonEnquiryVTO personEnquiryVTO = new PersonEnquiryVTO();
//		personEnquiryVTO.setNationalId(nationalID);
//
//
//		PersonEnquiryVTO[] personEnquiryVTOs=new PersonEnquiryVTO[]{personEnquiryVTO};
//		IMSProxyProvider imsProxyProvider = selectIMSProvider(ONLINE_ENQUIRY);
//
//		IMSService imsService = getIMSService();
//
//		Map<String, PersonEnquiryVTO> returnMap = imsService
//				.fetchDataByOnlineEnquiryByEstelam2(personEnquiryVTOs, nationalID, imsProxyProvider);
//
//
//		PersonEnquiryVTO personEnquiry = returnMap.get(nationalID);
//
//		return personEnquiry;
//
//
//		}
//		catch(BaseException e)
//		{
//
//			throw e;
//
//		}
//
//
//		catch (Exception e) {
//
//			throw new ServiceException(BizExceptionCode.RSI_139, BizExceptionCode.RSI_139_MSG, e);
//
//		}
////		return null;
//
//
//	}


    //Anbari:Estelam3
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PersonEnquiryVTO updateInfoByGetEstelam3(Long emsCardRequestId, Boolean isImageRequested) throws BaseException, DateFormatException {
        boolean verified = false;
        String description = "";
        CardRequestHistoryAction historyAction = null;
        StringBuilder metadata = new StringBuilder();
        List<IMSEnquiryVTO> imsEnquiryVTOList = fetchListForEnquiryReqByRequestId(emsCardRequestId);
        if (!EmsUtil.checkListSize(imsEnquiryVTOList)) {
            throw new ServiceException(BizExceptionCode.MMS_064,
                    BizExceptionCode.MMS_063_MSG);
        }

        IMSEnquiryVTO imsEnquiryVTO = imsEnquiryVTOList.get(0);
        Long reqId = imsEnquiryVTO.getRequestId();
        if (reqId == null) {
            throw new ServiceException(BizExceptionCode.MMS_067,
                    BizExceptionCode.MMS_065_MSG);
        }
        // Anbari Fix me if reqId is null

        CardRequestTO cardRequestTO = getCardRequestDAO().find(CardRequestTO.class, reqId);

        CitizenTO ctz = getCitizenDAO().findByRequestId(reqId);
        CitizenInfoTO czi = ctz.getCitizenInfo();
        String requestedPersonNationaID = ctz.getNationalID();
        String citizenInfo = imsEnquiryVTO.getCitizenInfo();
        Object[] keys = citizenInfo.split("\n");
        PersonEnquiryVTO personEnquiryVTOResult = null;
        for (Object key : keys) {

            String nId = getNationalIdFromCitizenInfoKey(ctz, key);
            String strKey = key.toString();

            String imageDescription;
            // Anbari Add Try Catch
            try {
                PersonEnquiryVTO personEnquiryVTOinput = new PersonEnquiryVTO();
                personEnquiryVTOinput.setNationalId(nId);
                personEnquiryVTOResult = getIMSService().fetchDataByOnlineEnquiryByEstelam3(personEnquiryVTOinput, requestedPersonNationaID);
//				if (personEnquiryVTOResult.getIsServiceDown() || personEnquiryVTOResult.getIsEstelamCorrupt())
//					return personEnquiryVTOResult;
                //return personEnquiryVTOResult;


            } catch (Exception e) {
                if (e instanceof BaseException)
                    throw (BaseException) e;
            }
            // Anbari End

            // fix me if personEnquiryVTOResult is null


            if (strKey.contains("m")) {
                if (EmsUtil.checkString(personEnquiryVTOResult.getMetadata())) {
                    metadata.append(strKey).append("-");
                    failedSituationEstelamLog(reqId, requestedPersonNationaID, personEnquiryVTOResult, nId, "Mother");
                    setCitizenInfoByDefultValue(reqId, "m");
                } else {
                    updateMotherInfo(reqId, personEnquiryVTOResult);
                }

            } else if (strKey.contains("f")) {
                if (EmsUtil.checkString(personEnquiryVTOResult.getMetadata())) {
                    metadata.append(strKey).append("-");
                    failedSituationEstelamLog(reqId, requestedPersonNationaID,
                            personEnquiryVTOResult, nId, "Father");
                    setCitizenInfoByDefultValue(reqId, "f");

                } else {
                    updateFatherInfo(reqId, personEnquiryVTOResult);
                }
            } else if (strKey.contains("s")) {
                if (EmsUtil.checkString(personEnquiryVTOResult.getMetadata())) {
                    metadata.append(strKey).append("-");
                    failedSituationEstelamLog(reqId, requestedPersonNationaID, personEnquiryVTOResult, nId, "Spouse");
                    SpouseDAO spousDAO = getSpousDAO();
                    for (SpouseTO spouseTO : czi.getSpouses()) {
                        if (spouseTO.getSpouseNationalID().equals(nId)) {
                            czi.getSpouses().remove(spouseTO);
                            spousDAO.delete(spouseTO);
                            break;
                        }
                    }
                } else {
                    updateSpouseInfo(reqId, personEnquiryVTOResult, nId);
                }

            } else if (strKey.contains("c")) {
                if (EmsUtil.checkString(personEnquiryVTOResult.getMetadata())) {
                    metadata.append(strKey).append("-");
                    failedSituationEstelamLog(reqId, requestedPersonNationaID,
                            personEnquiryVTOResult, nId, "Child");
                    ChildDAO childDAO = getChildDAO();
                    for (ChildTO childTO : czi.getChildren()) {
                        if (childTO.getChildNationalID().equals(nId)) {
                            czi.getChildren().remove(childTO);
                            childDAO.delete(childTO);
                            break;
                        }
                    }
                } else {
                    updateChildInfo(reqId, personEnquiryVTOResult, nId);
                }

            } else if (strKey.contains(reqId.toString())) {


                if (personEnquiryVTOResult.getIsServiceDown() || personEnquiryVTOResult.getIsEstelamCorrupt() || personEnquiryVTOResult.getIsExceptionMessage() || personEnquiryVTOResult.getIsDead() == 1)

                {

                    if (personEnquiryVTOResult.getIsExceptionMessage()) {
                        cardRequestTO.setEstelam2Flag(Estelam2FlagType.N);
                        getCardRequestDAO().update(cardRequestTO);
                    }

                    if (personEnquiryVTOResult.getIsDead() == 1) {
                        cardRequestTO.setEstelam2Flag(Estelam2FlagType.N);
                        getCardRequestDAO().update(cardRequestTO);
                    }

                    description = personEnquiryVTOResult.getDescription();
                    if (EmsUtil.checkString(description)) {
                        if (!EmsUtil.checkMaxFieldLength(description, 600))
                            description = description.substring(0, 600);
                    }
                    getCardRequestHistoryDAO().create(cardRequestTO,
                            description, SystemId.EMS, null,
                            CardRequestHistoryAction.NOT_VERIFIED_BY_IMS, null);
                    return personEnquiryVTOResult;

                }


                if (EmsUtil.checkString(personEnquiryVTOResult.getMetadata())) {
                    metadata.append(strKey).append("-");
                    logEstelamFailed("Citizen", reqId, requestedPersonNationaID, null);
                    if (EmsUtil.checkString(personEnquiryVTOResult.getLogInfo())) {

                        Estelam2FailureLogTO estelam2LogTO = new Estelam2FailureLogTO();
                        estelam2LogTO.setNationalID(requestedPersonNationaID);
                        estelam2LogTO.setCreateDate(new Date());
                        EstelamFailureType type = EstelamFailureType.NOT_FOUND;
                        description = personEnquiryVTOResult.getDescription();
                        if (EmsUtil.checkString(description)) {
                            if (!EmsUtil.checkMaxFieldLength(description, 600))
                                description = description.substring(0, 600);
                        }
                        estelam2LogTO.setDescription(description);
                        type = checkImsErrorType(personEnquiryVTOResult, type);
                        estelam2LogTO.setType(type);
                        logInsertIntoEstelam2FailureTable(estelam2LogTO, null, null);
                        getEstelam2LogDAO().create(estelam2LogTO);
                        verified = false;
                        break;
                    }
                } else {
                    verified = true;
                    imageDescription = personEnquiryVTOResult.getImageDescription();
                    updateCitizen(reqId, personEnquiryVTOResult, isImageRequested);
                    if (EmsUtil.checkString(imageDescription)) {
                        if (!EmsUtil.checkMaxFieldLength(imageDescription, 600))
                            imageDescription = imageDescription.substring(0, 600);
                        getCardRequestHistoryDAO().create(cardRequestTO, imageDescription, SystemId.EMS, null, CardRequestHistoryAction.IMS_IMAGE_NOT_FOUND, null);
                    }
                }

            }

        }// End for key

        if (verified) {
            historyAction = CardRequestHistoryAction.VERIFIED_IMS;
            cardRequestTO.setEstelam2Flag(Estelam2FlagType.V);
        } else {
            historyAction = CardRequestHistoryAction.NOT_VERIFIED_BY_IMS;
            cardRequestTO.setEstelam2Flag(Estelam2FlagType.N);
        }
        description = personEnquiryVTOResult.getDescription();
        if (EmsUtil.checkString(description)) {
            if (!EmsUtil.checkMaxFieldLength(description, 600))
                description = description.substring(0, 600);
        }
        getCardRequestHistoryDAO().create(new CardRequestTO(reqId), description, SystemId.EMS, null, historyAction, "");
        cardRequestTO.setMetadata(metadata.toString());
        return personEnquiryVTOResult;

    }


    //Anbari:Estelam3 without citizenInfoUpdate
    @Override
    public PersonEnquiryVTO updateInfoByGetEstelam3WithoutCitizenInfoUpdate(
            Long emsCardRequestId) throws BaseException {

        CitizenTO ctz = getCitizenDAO().findByRequestId(emsCardRequestId);
        String requestedPersonNationaID = ctz.getNationalID();
        PersonEnquiryVTO personEnquiryVTOResult = null;
        String description = "";
        try {
            PersonEnquiryVTO personEnquiryVTOinput = new PersonEnquiryVTO();
            personEnquiryVTOinput.setNationalId(ctz.getNationalID());
            personEnquiryVTOResult = getIMSService().fetchDataByOnlineEnquiryByEstelam3(personEnquiryVTOinput, requestedPersonNationaID);
            if (EmsUtil.checkString(personEnquiryVTOResult.getMetadata())) {
                if (EmsUtil.checkString(personEnquiryVTOResult.getLogInfo())) {
                    Estelam2FailureLogTO estelam2LogTO = new Estelam2FailureLogTO();
                    estelam2LogTO.setNationalID(requestedPersonNationaID);
                    estelam2LogTO.setCreateDate(new Date());
                    EstelamFailureType type = EstelamFailureType.NOT_FOUND;
                    description = personEnquiryVTOResult.getDescription();
                    if (EmsUtil.checkString(description)) {
                        if (!EmsUtil.checkMaxFieldLength(description, 600))
                            description = description.substring(0, 600);
                    }
                    estelam2LogTO.setDescription(description);
                    type = checkImsErrorType(personEnquiryVTOResult, type);
                    estelam2LogTO.setType(type);
                    logInsertIntoEstelam2FailureTable(estelam2LogTO, null, null);
                    getEstelam2LogDAO().create(estelam2LogTO);
                    personEnquiryVTOResult.setIsEstelamCorrupt(true);
                }
            }


        } catch (Exception e) {
            if (e instanceof BaseException)
                throw (BaseException) e;
        }

        return personEnquiryVTOResult;
    }

    //Anbari:Estelam3
    private String getNationalIdFromCitizenInfoKey(CitizenTO ctz, Object key) {
        String nId = null;
        String strKey = key.toString();
        if (strKey.contains("m")) {
            String nIdStr = strKey.split("m")[1];
            nId = nIdStr.split(",")[0];
        } else if (strKey.contains("f")) {
            String nIdStr = strKey.split("f")[1];
            nId = nIdStr.split(",")[0];
        } else if (strKey.contains("s")) {
            String nIdStr = strKey.split("s")[1];
            nId = nIdStr.split(",")[0];
        } else if (strKey.contains("c")) {
            String nIdStr = strKey.split("c")[1];
            nId = nIdStr.split(",")[0];
        } else {
            nId = ctz.getNationalID();
        }
        return nId;
    }

    //Anbari:Estelam3
    private void failedSituationEstelamLog(Long reqId,
                                           String requestedPersonNationaID,
                                           PersonEnquiryVTO personEnquiryVTOResult, String nId,
                                           String familyType) {
        logEstelamFailed(familyType, reqId, requestedPersonNationaID, nId);
        if (EmsUtil.checkString(personEnquiryVTOResult.getLogInfo())) {

            if (personEnquiryVTOResult.getLogInfo().equals(
                    BizExceptionCode.GIO_001)
                    || personEnquiryVTOResult.getLogInfo().equals(
                    BizExceptionCode.NIO_002)) {
                logServiceDown();
                throw new RuntimeException();
            } else if (personEnquiryVTOResult.getLogInfo().equals(
                    BizExceptionCode.GIO_008)
                    || personEnquiryVTOResult.getLogInfo().equals(
                    BizExceptionCode.NIO_029)) {
                logPersonNotFound(familyType);
            }
        }
    }

    //Anbari:Estelam3
    private void setCitizenInfoByDefultValue(Long reqId, String familyType)
            throws BaseException {

        CitizenTO ctz = getCitizenDAO().findByRequestId(reqId);
        CitizenInfoTO czi = ctz.getCitizenInfo();

        if (familyType.equals("m")) {
            czi.setMotherSurname(ConstValues.DEFAULT_NAMES_FA);
            czi.setMotherBirthCertificateId(ConstValues.DEFAULT_NUMBER);
            czi.setMotherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
            czi.setMotherNationalID(ConstValues.DEFAULT_NID);
            czi.setMotherFatherName(ConstValues.DEFAULT_NAMES_FA);
            try {
                czi.setMotherBirthDateSolar(DateUtil.convert(
                        ConstValues.DEFAULT_DATE, DateUtil.JALALI));
            } catch (DateFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (familyType.equals("f")) {
            czi.setFatherFatherName(ConstValues.DEFAULT_NAMES_FA);
            czi.setFatherSurname(ConstValues.DEFAULT_NAMES_FA);
            czi.setFatherBirthCertificateId(ConstValues.DEFAULT_NUMBER);
            czi.setFatherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
            czi.setFatherNationalID(ConstValues.DEFAULT_NID);
            try {
                czi.setFatherBirthDateSolar(DateUtil.convert(
                        ConstValues.DEFAULT_DATE, DateUtil.JALALI));
            } catch (DateFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        getCitizenInfoDAO().update(czi);

    }

    //Anbari:Estelam3
    private void updateCitizen(Long reqId, PersonEnquiryVTO personEnquiryVTO,
                               Boolean isImageRequested) throws BaseException, DateFormatException {

        CitizenTO ctz = getCitizenDAO().findByRequestId(reqId);
        CitizenInfoTO czi = ctz.getCitizenInfo();

        // Anbari Khode Citizen
        ctz.setFirstNamePersian(personEnquiryVTO.getFirstName());
        ctz.setSurnamePersian(personEnquiryVTO.getLastName());
        czi.setBirthCertificateId(personEnquiryVTO.getBirthCertificateId());
        // Anbari: set Serial
        czi.setBirthCertificateSeries(personEnquiryVTO
                .getBirthCertificateSerial());
        // Anbari : set NIDImage to requested citizen nidImage
        // field
        if (isImageRequested && personEnquiryVTO.getNidImage() != null) {
            try {
                sessionContext.getBusinessObject(
                        IMSManagementServiceLocal.class).saveImsEstelamImage(
                        ctz.getNationalID(), ctz, ImsEstelamImageType.IMS_NID_IMAGE,
                        personEnquiryVTO.getNidImage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        czi.setFatherFirstNamePersian(personEnquiryVTO.getFatherName());
        czi.setGender(personEnquiryVTO.getGender());
        //Anbari: overwrite birthDate
        czi.setBirthDateSolar(personEnquiryVTO.getSolarBirthDate());
        czi.setBirthDateGregorian(DateUtil.convert(personEnquiryVTO.getSolarBirthDate(), DateUtil.JALALI));
        insertLogForCitizenInfo(ctz, czi);

        getCitizenDAO().update(ctz);
        getCitizenInfoDAO().update(czi);
    }


    //Anbari:Estelam3: check if child commit at the end or not
    private void updateChildInfo(Long reqId,
                                 PersonEnquiryVTO personEnquiryVTOResult, String nId)
            throws BaseException {

        CitizenTO ctz = getCitizenDAO().findByRequestId(reqId);
        CitizenInfoTO czi = ctz.getCitizenInfo();

        for (ChildTO childTO : czi.getChildren()) {
            if (childTO.getChildNationalID().equals(nId)) {

                childTO.setChildFirstNamePersian(personEnquiryVTOResult
                        .getFirstName());
                childTO.setChildBirthCertificateId(personEnquiryVTOResult
                        .getBirthCertificateId());
                // Anbari: set Serial of citizenInfoTO
                childTO.setChildBirthCertificateSeries(personEnquiryVTOResult
                        .getBirthCertificateSerial());
                childTO.setChildFatherName(personEnquiryVTOResult
                        .getFatherName());
                childTO.setChildGender(personEnquiryVTOResult.getGender());
                try {
                    childTO.setChildBirthDateSolar(DateUtil.convert(
                            personEnquiryVTOResult.getSolarBirthDate(),
                            DateUtil.JALALI));
                } catch (DateFormatException e) {
                    throw new ServiceException(BizExceptionCode.MMS_056,
                            BizExceptionCode.MMS_029_MSG, e);
                }
                insertLogForChildrenInfo(nId, childTO);
                break;
            }
        }
    }

    //Anbari:Estelam3: check if spouse commit at the end or not
    private void updateSpouseInfo(Long reqId,
                                  PersonEnquiryVTO personEnquiryVTOResult, String nId)
            throws BaseException {

        CitizenTO ctz = getCitizenDAO().findByRequestId(reqId);
        CitizenInfoTO czi = ctz.getCitizenInfo();

        for (SpouseTO spouseTO : czi.getSpouses()) {
            if (spouseTO.getSpouseNationalID().equals(nId)) {

                spouseTO.setSpouseFirstNamePersian(personEnquiryVTOResult
                        .getFirstName());
                spouseTO.setSpouseSurnamePersian(personEnquiryVTOResult
                        .getLastName());
                spouseTO.setSpouseBirthCertificateId(personEnquiryVTOResult
                        .getBirthCertificateId());
                // Anbari: set Serial of citizenInfoTO
                spouseTO.setSpouseBirthCertificateSeries(personEnquiryVTOResult
                        .getBirthCertificateSerial());
                spouseTO.setSpouseFatherName(personEnquiryVTOResult
                        .getFatherName());
                try {
                    spouseTO.setSpouseBirthDate(DateUtil.convert(
                            personEnquiryVTOResult.getSolarBirthDate(),
                            DateUtil.JALALI));
                } catch (DateFormatException e) {
                    throw new ServiceException(BizExceptionCode.MMS_055,
                            BizExceptionCode.MMS_029_MSG, e);
                }
                insertLogForSpouseInfo(nId, spouseTO);
                break;
            }
        }

    }

    //Anbari:Estelam3
    private void updateFatherInfo(Long reqId, PersonEnquiryVTO personEnquiryVTO)
            throws BaseException {

        CitizenTO ctz = getCitizenDAO().findByRequestId(reqId);
        CitizenInfoTO czi = ctz.getCitizenInfo();

        czi.setFatherSurname(personEnquiryVTO.getLastName());
        czi.setFatherBirthCertificateId(personEnquiryVTO
                .getBirthCertificateId());
        // Anbari: set Serial of citizenInfoTO
        czi.setFatherBirthCertificateSeries(personEnquiryVTO
                .getBirthCertificateSerial());
        try {
            czi.setFatherBirthDateSolar(DateUtil.convert(
                    personEnquiryVTO.getSolarBirthDate(), DateUtil.JALALI));
        } catch (DateFormatException e) {
            throw new ServiceException(BizExceptionCode.MMS_054,
                    BizExceptionCode.MMS_029_MSG, e);
        }
        czi.setFatherFatherName(personEnquiryVTO.getFatherName());

        insertLogForFatherInfo(czi);

        getCitizenDAO().update(ctz);
        getCitizenInfoDAO().update(czi);
    }

    //Anbari:Estelam3
    private void updateMotherInfo(Long reqId, PersonEnquiryVTO personEnquiryVTO)
            throws BaseException {

        CitizenTO ctz = getCitizenDAO().findByRequestId(reqId);
        CitizenInfoTO czi = ctz.getCitizenInfo();

        czi.setMotherFirstNamePersian(personEnquiryVTO.getFirstName());
        czi.setMotherSurname(personEnquiryVTO.getLastName());
        czi.setMotherBirthCertificateId(personEnquiryVTO
                .getBirthCertificateId());
        // Anbari: set Serial of citizenInfoTO
        czi.setMotherBirthCertificateSeries(personEnquiryVTO
                .getBirthCertificateSerial());
        try {
            czi.setMotherBirthDateSolar(DateUtil.convert(
                    personEnquiryVTO.getSolarBirthDate(), DateUtil.JALALI));
        } catch (DateFormatException e) {
            throw new ServiceException(BizExceptionCode.MMS_053,
                    BizExceptionCode.MMS_029_MSG, e);
        }
        czi.setMotherFatherName(personEnquiryVTO.getFatherName());

        insertLogForMotherInfo(czi);

        getCitizenDAO().update(ctz);
        getCitizenInfoDAO().update(czi);
    }

    //Anbari:IMS
    private EstelamFailureType checkImsErrorType(
            PersonEnquiryVTO personEnquiryVTO, EstelamFailureType type) {
        if (personEnquiryVTO.getLogInfo().equals(BizExceptionCode.GIO_008)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_029)) {
            logPersonNotFound("Citizen");
            type = EstelamFailureType.NOT_FOUND;
        } else if (personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.GIO_010)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_021)) {
            type = EstelamFailureType.INVAILD_CERTIFICATE_ID;
        } else if (personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.GIO_011)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_022)) {
            type = EstelamFailureType.INVAILD_BIRTH_DATE;
        } else if (personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.GIO_012)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_023)) {
            type = EstelamFailureType.INVAILD_CERTIFICATE_SERIAL;
        } else if (personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.GIO_013)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_024)) {
            type = EstelamFailureType.INVAILD_NAME;
        } else if (personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.GIO_014)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_025)) {
            type = EstelamFailureType.INVAILD_LAST_NAME;
        } else if (personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.GIO_015)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_026)) {
            type = EstelamFailureType.INVAILD_FATHER_NAME;
        } else if (personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.GIO_016)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_027)) {
            type = EstelamFailureType.INVAILD_GENDER;
        } else if (personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.GIO_017)
                || personEnquiryVTO.getLogInfo().equals(
                BizExceptionCode.NIO_028)) {
            type = EstelamFailureType.UNKNOWN_IMS_MESSAGE;
        }
        return type;
    }


    private void insertLogForMotherInfo(CitizenInfoTO czi) {
        logger.info("\nMother info with national id : "
                + czi.getMotherNationalID() + "\n");
        estelam2Logger.info("\nMother info with national id : "
                + czi.getMotherNationalID() + "\n");
        logger.info("\nMother FirstName : " + czi.getMotherFirstNamePersian()
                + "\n");
        estelam2Logger.info("\nMother FirstName : "
                + czi.getMotherFirstNamePersian() + "\n");
        logger.info("\nMother LastName : " + czi.getMotherSurname() + "\n");
        estelam2Logger.info("\nMother LastName : " + czi.getMotherSurname()
                + "\n");
        logger.info("\nMother BirthCertId : "
                + czi.getMotherBirthCertificateId() + "\n");
        estelam2Logger.info("\nMother BirthCertId : "
                + czi.getMotherBirthCertificateId() + "\n");
        logger.info("\nMother BirthCertSerial : "
                + czi.getMotherBirthCertificateSeries() + "\n");
        estelam2Logger.info("\nMother BirthCertSerial : "
                + czi.getMotherBirthCertificateSeries() + "\n");
        logger.info("\nMother BirthDateSolar : "
                + czi.getMotherBirthDateSolar() + "\n");
        estelam2Logger.info("\nMother BirthDateSolar : "
                + czi.getMotherBirthDateSolar() + "\n");
    }

    private void insertLogForChildrenInfo(String nId, ChildTO childTO) {
        logger.info("\nChild info with national id : " + nId + "\n");
        estelam2Logger.info("\nChild info with national id : " + nId + "\n");
        logger.info("\nChild FirstName is : "
                + childTO.getChildFirstNamePersian() + "\n");
        estelam2Logger.info("\nChild FirstName is : "
                + childTO.getChildFirstNamePersian() + "\n");
        logger.info("\nChild FatherName is : " + childTO.getChildFatherName()
                + "\n");
        estelam2Logger.info("\nChild FatherName is : "
                + childTO.getChildFatherName() + "\n");
        logger.info("\nChild BirthCertId is : "
                + childTO.getChildBirthCertificateId() + "\n");
        estelam2Logger.info("\nChild BirthCertId is : "
                + childTO.getChildBirthCertificateId() + "\n");
        logger.info("\nChild BirthCertSerial is : "
                + childTO.getChildBirthCertificateSeries() + "\n");
        estelam2Logger.info("\nChild BirthCertSerial is : "
                + childTO.getChildBirthCertificateSeries() + "\n");
        logger.info("\nChild BirthDate is : "
                + childTO.getChildBirthDateSolar() + "\n");
        estelam2Logger.info("\nChild BirthDate is : "
                + childTO.getChildBirthDateSolar() + "\n");
        logger.info("\nChild Gender is : " + childTO.getChildGender().name()
                + "\n");
        estelam2Logger.info("\nChild Gender is : "
                + childTO.getChildGender().name() + "\n");
    }

    private void insertLogForSpouseInfo(String nId, SpouseTO spouseTO) {
        logger.info("\nSpouse info with national id : " + nId + "\n");
        estelam2Logger.info("\nSpouse info with national id : " + nId + "\n");
        logger.info("\nSpouse FirstName is : "
                + spouseTO.getSpouseFirstNamePersian() + "\n");
        estelam2Logger.info("\nSpouse FirstName is : "
                + spouseTO.getSpouseFirstNamePersian() + "\n");
        logger.info("\nSpouse FatherName is : "
                + spouseTO.getSpouseFatherName() + "\n");
        estelam2Logger.info("\nSpouse FatherName is : "
                + spouseTO.getSpouseFatherName() + "\n");
        logger.info("\nSpouse BirthCertId is : "
                + spouseTO.getSpouseBirthCertificateId() + "\n");
        estelam2Logger.info("\nSpouse BirthCertId is : "
                + spouseTO.getSpouseBirthCertificateId() + "\n");
        logger.info("\nSpouse BirthCertSerial is : "
                + spouseTO.getSpouseBirthCertificateSeries() + "\n");
        estelam2Logger.info("\nSpouse BirthCertSerial is : "
                + spouseTO.getSpouseBirthCertificateSeries() + "\n");
        logger.info("\nSpouse BirthDate is : " + spouseTO.getSpouseBirthDate()
                + "\n");
        estelam2Logger.info("\nSpouse BirthDate is : "
                + spouseTO.getSpouseBirthDate() + "\n");
    }

    private void insertLogForFatherInfo(CitizenInfoTO czi) {
        logger.info("\nFather info with national id : "
                + czi.getFatherNationalID() + "\n");
        estelam2Logger.info("\nFather info with national id : "
                + czi.getFatherNationalID() + "\n");
        logger.info("\nFather FirstName : " + czi.getFatherFirstNamePersian()
                + "\n");
        estelam2Logger.info("\nFather FirstName : "
                + czi.getFatherFirstNamePersian() + "\n");
        logger.info("\nFather LastName : " + czi.getFatherSurname() + "\n");
        estelam2Logger.info("\nFather LastName : " + czi.getFatherSurname()
                + "\n");
        logger.info("\nFather BirthCertId : "
                + czi.getFatherBirthCertificateId() + "\n");
        estelam2Logger.info("\nFather BirthCertId : "
                + czi.getFatherBirthCertificateId() + "\n");
        logger.info("\nFather BirthCertSerial : "
                + czi.getFatherBirthCertificateSeries() + "\n");
        estelam2Logger.info("\nFather BirthCertSerial : "
                + czi.getFatherBirthCertificateSeries() + "\n");
        logger.info("\nFather BirthDateSolar : "
                + czi.getFatherBirthDateSolar() + "\n");
        estelam2Logger.info("\nFather BirthDateSolar : "
                + czi.getFatherBirthDateSolar() + "\n");
    }

    //Anbari:Eslelam3
    private void insertLogForCitizenInfo(CitizenTO ctz, CitizenInfoTO czi) {
        logger.info("\nCitizen FirstName : " + ctz.getFirstNamePersian() + "\n");
        estelam2Logger.info("\nCitizen FirstName : "
                + ctz.getFirstNamePersian() + "\n");
        logger.info("\nCitizen LastName : " + ctz.getSurnamePersian() + "\n");
        estelam2Logger.info("\nCitizen LastName : " + ctz.getSurnamePersian()
                + "\n");
        logger.info("\nCitizen BirthCertId : " + czi.getBirthCertificateId()
                + "\n");
        estelam2Logger.info("\nCitizen BirthCertId : "
                + czi.getBirthCertificateId() + "\n");
        logger.info("\nCitizen BirthCertSerial : "
                + czi.getBirthCertificateSeries() + "\n");
        estelam2Logger.info("\nCitizen BirthCertSerial : "
                + czi.getBirthCertificateSeries() + "\n");
        logger.info("\nCitizen BirthDateSolar : " + czi.getBirthDateSolar()
                + "\n");
        estelam2Logger.info("\nCitizen BirthDateSolar : "
                + czi.getBirthDateSolar() + "\n");
    }

    // Anbari:Estelam3
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveImsEstelamImage(String nationalId, CitizenTO citizen,
                                    ImsEstelamImageType imsImageType, byte[] data) throws BaseException {
        // List<ImsEstelamImageTO> imsEstelamImageTOList =
        // getImsEstelamImageDAO().findImsImageByNationalIdAndType(nationalId,
        // imsImageType);
        ImsEstelamImageTO imsImageTO = getImsEstelamImageDAO().find(ImsEstelamImageTO.class, citizen.getId());
        if (imsImageTO == null) {
            ImsEstelamImageTO imsEstelamImage = new ImsEstelamImageTO();
            if (ImsEstelamImageType.IMS_NID_IMAGE.equals(imsImageType))
                imsEstelamImage.setNationalIdImage(data);
            imsEstelamImage.setNationalID(nationalId);
            citizen = getCitizenDAO().find(CitizenTO.class, citizen.getId());
            imsEstelamImage.setCitizen(citizen);
            getImsEstelamImageDAO().create(imsEstelamImage);
        } else {
            if (ImsEstelamImageType.IMS_NID_IMAGE.equals(imsImageType))
                imsImageTO.setNationalIdImage(data);
            getImsEstelamImageDAO().update(imsImageTO);
        }

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveImsEstelamImage(String nationalId, CitizenTO citizen,
                                    ImsEstelamImageType imsImageType, byte[] data, boolean isNewCardRequest) throws BaseException {
        // List<ImsEstelamImageTO> imsEstelamImageTOList =
        // getImsEstelamImageDAO().findImsImageByNationalIdAndType(nationalId,
        // imsImageType);
        ImsEstelamImageTO imsImageTO = getImsEstelamImageDAO().find(ImsEstelamImageTO.class, citizen.getId());
        if (imsImageTO == null) {
            ImsEstelamImageTO imsEstelamImage = new ImsEstelamImageTO();
            if (ImsEstelamImageType.IMS_NID_IMAGE.equals(imsImageType))
                imsEstelamImage.setNationalIdImage(data);
            imsEstelamImage.setNationalID(nationalId);
            if (!isNewCardRequest) {
                citizen = getCitizenDAO().find(CitizenTO.class, citizen.getId());
            }
            imsEstelamImage.setCitizen(citizen);
            getImsEstelamImageDAO().create(imsEstelamImage);
        } else {
            if (ImsEstelamImageType.IMS_NID_IMAGE.equals(imsImageType))
                imsImageTO.setNationalIdImage(data);
            getImsEstelamImageDAO().update(imsImageTO);
        }

    }

    /**
     * this method does verification in delivery
     *
     * @author ganjyar
     */
    @Override
    public ImsCitizenInfoResponseWTO verificationInDelivery(
            ImsCitizenInfoRequestWTO imsCitizenInfoRequest)
            throws BaseException {

        ImsCitizenInfoResponseWTO response = new ImsCitizenInfoResponseWTO();
        try {

            imsVerificationInDelivery.info("\n==============================================================================================");
            imsVerificationInDelivery.info("\n=====================Estelam in delivey card just started...==================================");
            imsVerificationInDelivery.info("\n==============================================================================================");

            imsVerificationInDelivery.info("\nCard and citizen info on card encomposs of:");
            imsVerificationInDelivery.info("\nRequestId: " + imsCitizenInfoRequest.getRequestId());
            imsVerificationInDelivery.info("\nCitizen nid: " + imsCitizenInfoRequest.getCardNationalNumber());
            imsVerificationInDelivery.info("\nCitizen name: " + imsCitizenInfoRequest.getCardFirstname());
            imsVerificationInDelivery.info("\nCitizen lastName: " + imsCitizenInfoRequest.getCardLastname());
            imsVerificationInDelivery.info("\nCitizen fatherName: " + imsCitizenInfoRequest.getCardFatherFirstname());
            imsVerificationInDelivery.info("\nCitizen birthDate: " + imsCitizenInfoRequest.getCardBirthDate());
            imsVerificationInDelivery.info("\n==============================================================================================");

            int isdead = 0;
            boolean hasException = false;
            boolean imsIsDown = false;
            boolean estelamIsFault = false;
            Long requestId = imsCitizenInfoRequest.getRequestId();
            response.setRequestId(requestId);
            PersonEnquiryVTO estelamResult = doEstelam3WithoutCitizenInfoUpdate(requestId);

            imsVerificationInDelivery.info("\n====================================================================================================================");
            imsVerificationInDelivery.info("\n===================== citizen info fetching by doEstelam3WithoutCitizenInfoUpdate ==================================");
            imsVerificationInDelivery.info("\n====================================================================================================================");
            imsVerificationInDelivery.info("\nRequestId: " + imsCitizenInfoRequest.getRequestId());
            imsVerificationInDelivery.info("\nisdead: " + estelamResult.getIsDead());
            imsVerificationInDelivery.info("\nIsExceptionMessage: " + estelamResult.getIsExceptionMessage());
            imsVerificationInDelivery.info("\nimsIsDown: " + estelamResult.getIsServiceDown());
            imsVerificationInDelivery.info("\nestelamIsFault: " + estelamResult.getMetadata());
            imsVerificationInDelivery.info("\nfname: " + estelamResult.getFirstName());
            imsVerificationInDelivery.info("\nlname: " + estelamResult.getLastName());
            imsVerificationInDelivery.info("\nbirthDate: " + estelamResult.getSolarBirthDate());
            imsVerificationInDelivery.info("\nfatherName: " + estelamResult.getFatherName());
            imsVerificationInDelivery.info("\n==================================================================================================================");

            isdead = estelamResult.getIsDead();
            if (isdead != 0 && isdead != 1)
                throw new ServiceException(BizExceptionCode.MMS_068,
                        BizExceptionCode.MMS_068_MSG);
            if (estelamResult.getIsExceptionMessage() == null || estelamResult.getIsServiceDown() == null)
                throw new ServiceException(BizExceptionCode.MMS_069,
                        BizExceptionCode.MMS_069_MSG);
            hasException = estelamResult.getIsExceptionMessage();
            imsIsDown = estelamResult.getIsServiceDown();
            estelamIsFault = EmsUtil.checkString(estelamResult.getMetadata());
            String fname = estelamResult.getFirstName();
            String lname = estelamResult.getLastName();
            String birthDate = estelamResult.getSolarBirthDate();
            String fatherName = estelamResult.getFatherName();
            if (imsIsDown) {
                response.setImsIsDown(true);
                imsVerificationInDelivery.info("\nIms Is Down!");
                return response;
            }
            // 1)has exception
            if (hasException) {
                response.setImsIsForbidden(true);
                imsVerificationInDelivery.info("\nCitizen is forbidden!");
                return response;
            }

            if (estelamIsFault) {
                response.setEstelamIsFalse(true);
                response.setLogInfo(estelamResult.getLogInfo() == null ? estelamResult.getDescription() : estelamResult.getLogInfo());
                imsVerificationInDelivery.info("\nCitizen EstelamIsFalse!");
                return response;
            }

            // 2)is dead
            if (isdead == 1) {
                response.setImsIsDead(true);
                imsVerificationInDelivery.info("\nCitizen is dead!");
                return response;
            }
            // 3)verifying personal info
            if (!(EmsUtil.checkString(fname) || EmsUtil.checkString(lname) || EmsUtil.checkString(birthDate)
                    || EmsUtil.checkString(fatherName)))

                throw new ServiceException(BizExceptionCode.MMS_070,
                        BizExceptionCode.MMS_070_MSG);

            // fname
            if (!TranslateUtil.translate(fname).equals(TranslateUtil.translate(imsCitizenInfoRequest.getCardFirstname()))) {
                imsVerificationInDelivery.info("\nCitizen NameError!");
                response.setImsIsInfoError(true);
                response.setImsIsNameError(true);
            }
            // lname
            if (!TranslateUtil.translate(lname).equals(TranslateUtil.translate(imsCitizenInfoRequest.getCardLastname()))) {
                imsVerificationInDelivery.info("\nCitizen LastNameError!");
                response.setImsIsInfoError(true);
                response.setImsIsLastNameError(true);
            }
            // birth date
            String dateOnCard = "";
            String dateStr = imsCitizenInfoRequest.getCardBirthDate();
            String[] str = dateStr.split("");
            for (int i = 1; i < str.length; i++) {


                if (str[i].equals("/"))
                    dateOnCard += str[i];
                else {
                    Integer item = Integer.valueOf(str[i]);
                    dateOnCard += item;
                }
            }

            if (!birthDate.equals(dateOnCard)) {
                imsVerificationInDelivery.info("\nCitizen BrithDateError!");
                response.setImsIsInfoError(true);
                response.setImsIsBrithDateError(true);
            }
            // father name
            if (!TranslateUtil.translate(fatherName).equals(TranslateUtil.translate(imsCitizenInfoRequest
                    .getCardFatherFirstname()))) {
                imsVerificationInDelivery.info("\nCitizen FatherNameError!");
                response.setImsIsInfoError(true);
                response.setImsIsFatherNameError(true);
            }

            return response;
        } catch (BaseException e) {
            imsVerificationInDelivery.error("An exception happened while calling verificationInDelivery in delivery card.", e);
            logger.error("An exception happened while calling verificationInDelivery in delivery card.", e);
            throw e;
        } catch (Exception e) {
            imsVerificationInDelivery.error("An unhandle exception happened while calling verificationInDelivery in delivery card.", e);
            logger.error("An unhandle exception happened while calling verificationInDelivery in delivery card.", e);
            throw new ServiceException(BizExceptionCode.MMS_072,
                    BizExceptionCode.GLB_008_MSG);

        }
    }

    //Anbari:Estelam3
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


    // Fixed in VIP version
    @Override
    public PersonEnquiryVTO getPersonalInfoByGetEstelam3(String nationalID)
            throws BaseException {
        return null;

    }

    //Anbari:Estelam2
//		@Override
//		public PersonEnquiryVTO getPersonInfoByGetEstelam2(Long emsCardRequestId) {
//			boolean verified = false;
//			String description = "";
//			StringBuilder metadata = new StringBuilder();
//			PersonEnquiryVTO personEnquiryVTOReturn = null;
//			List<IMSEnquiryVTO> imsEnquiryVTOList = fetchListForEnquiryReqByRequestId(emsCardRequestId);
//			if (!EmsUtil.checkListSize(imsEnquiryVTOList)) {
//				throw new ServiceException(BizExceptionCode.MMS_064,
//						BizExceptionCode.MMS_063_MSG);
//			}
//
//			IMSEnquiryVTO imsEnquiryVTO = imsEnquiryVTOList.get(0);
//			Long reqId = imsEnquiryVTO.getRequestId();
//			// Anbari Fix me if reqId is null
//
//			CardRequestTO cardRequestTO = getCardRequestDAO().find(
//					CardRequestTO.class, reqId);
//
//			CitizenTO ctz = getCitizenDAO().findByRequestId(reqId);
//			CitizenInfoTO czi = ctz.getCitizenInfo();
//			String requestedPersonNationaID = ctz.getNationalID();
//			String citizenInfo = imsEnquiryVTO.getCitizenInfo();
//			Object[] keys = citizenInfo.split("\n");
//
//			for (Object key : keys) {
//
//				PersonEnquiryVTO personEnquiryVTOResult = null;
//				String nId = getNationalIdFromCitizenInfoKey(ctz, key); // get
//																		// nationalId
//																		// of person
//																		// or his
//																		// family to
//																		// call
//																		// estelam3
//
//				// Anbari Add Try Catch
//				try {
//					PersonEnquiryVTO personEnquiryVTOinput = new PersonEnquiryVTO();
//					personEnquiryVTOinput.setNationalId(nId);
//					personEnquiryVTOResult = getIMSService()
//							.fetchDataByOnlineEnquiryByEstelam3(
//									personEnquiryVTOinput,
//									requestedPersonNationaID);
//				} catch (Exception e) {
//					if (e instanceof BaseException)
//						throw (BaseException) e;
//				}
//				// Anbari End
//
//				// fix me if personEnquiryVTOResult is null
//
//				String strKey = key.toString();
//
//				if (strKey.contains("m")) {
//					if (EmsUtil.checkString(personEnquiryVTOResult.getMetadata())) {
//						metadata.append(strKey).append("-");
//						failedSituationEstelamLog(reqId, requestedPersonNationaID,
//								personEnquiryVTOResult, nId, "Mother");
//						setCitizenInfoByDefultValue(reqId, "m");
//					} else {
//						updateMotherInfo(reqId, personEnquiryVTOResult);
//					}
//
//				} else if (strKey.contains("f")) {
//					if (EmsUtil.checkString(personEnquiryVTOResult.getMetadata())) {
//						metadata.append(strKey).append("-");
//						failedSituationEstelamLog(reqId, requestedPersonNationaID,
//								personEnquiryVTOResult, nId, "Father");
//						setCitizenInfoByDefultValue(reqId, "f");
//
//					} else {
//						updateFatherInfo(reqId, personEnquiryVTOResult);
//					}
//				} else if (strKey.contains("s")) {
//					if (EmsUtil.checkString(personEnquiryVTOResult.getMetadata())) {
//						metadata.append(strKey).append("-");
//						failedSituationEstelamLog(reqId, requestedPersonNationaID,
//								personEnquiryVTOResult, nId, "Spouse");
//						SpouseDAO spousDAO = getSpousDAO();
//						for (SpouseTO spouseTO : czi.getSpouses()) {
//							if (spouseTO.getSpouseNationalID().equals(nId)) {
//								czi.getSpouses().remove(spouseTO);
//								spousDAO.delete(spouseTO);
//								break;
//							}
//						}
//					} else {
//						updateSpouseInfo(reqId, personEnquiryVTOResult, nId);
//					}
//
//				} else if (strKey.contains("c")) {
//					if (EmsUtil.checkString(personEnquiryVTOResult.getMetadata())) {
//						metadata.append(strKey).append("-");
//						failedSituationEstelamLog(reqId, requestedPersonNationaID,
//								personEnquiryVTOResult, nId, "Child");
//						ChildDAO childDAO = getChildDAO();
//						for (ChildTO childTO : czi.getChildren()) {
//							if (childTO.getChildNationalID().equals(nId)) {
//								czi.getChildren().remove(childTO);
//								childDAO.delete(childTO);
//								break;
//							}
//						}
//					} else {
//						updateChildInfo(reqId, personEnquiryVTOResult, nId);
//					}
//
//				} else if (strKey.contains(reqId.toString())) {
//					personEnquiryVTOReturn = personEnquiryVTOResult; // if
//																		// cardRequset
//																		// Person
//																		// then
//																		// retrun
//																		// estelam
//																		// obj for
//																		// repeal or
//																		// ...
//																		// action
//					if (EmsUtil.checkString(personEnquiryVTOResult.getMetadata())) {
//						metadata.append(strKey).append("-");
//						logEstelamFailed("Citizen", reqId,
//								requestedPersonNationaID, null);
//						if (EmsUtil
//								.checkString(personEnquiryVTOResult.getLogInfo())) {
//							// service is down
//							if (personEnquiryVTOResult.getLogInfo().equals(
//									BizExceptionCode.GIO_001)
//									|| personEnquiryVTOResult.getLogInfo().equals(
//											BizExceptionCode.NIO_002)) {
//								logServiceDown();
//								throw new RuntimeException();
//							} else {
//								Estelam2FailureLogTO estelam2LogTO = new Estelam2FailureLogTO();
//								estelam2LogTO
//										.setNationalID(requestedPersonNationaID);
//								estelam2LogTO.setDescription(null);
//								estelam2LogTO.setCreateDate(new Date());
//								EstelamFailureType type = EstelamFailureType.NOT_FOUND;
//								estelam2LogTO.setDescription(personEnquiryVTOResult
//										.getDescription());
//								type = checkImsErrorType(personEnquiryVTOResult,
//										type);
//								estelam2LogTO.setType(type);
//								logInsertIntoEstelam2FailureTable(estelam2LogTO,
//										null, null);
//								getEstelam2LogDAO().create(estelam2LogTO);
//								verified = false;
//								break;
//							}
//						}
//					} else {
//						verified = true;
//						personEnquiryVTOReturn = personEnquiryVTOResult;
//						updateCitizen(reqId, personEnquiryVTOResult,
//								isImageRequested);
//					}
//				}
//
//			}// End for key
//
//			CardRequestHistoryAction historyAction = CardRequestHistoryAction.NOT_VERIFIED_BY_IMS;
//			if (verified) {
//				historyAction = CardRequestHistoryAction.VERIFIED_IMS;
//				cardRequestTO.setEstelam2Flag(Estelam2FlagType.V);
//			} else {
//				historyAction = CardRequestHistoryAction.NOT_VERIFIED_BY_IMS;
//				cardRequestTO.setEstelam2Flag(Estelam2FlagType.N);
//			}
//			if (EmsUtil.checkString(description)) {
//				if (!EmsUtil.checkMaxFieldLength(description, 255))
//					description = description.substring(0, 254);
//			}
//			getCardRequestHistoryDAO().create(new CardRequestTO(reqId),
//					description, SystemId.EMS, null, historyAction, "");
//
//			cardRequestTO.setMetadata(metadata.toString());
//			return personEnquiryVTOReturn;
//		}

    // Anbari:IMS new getUpdatedCitizenResultNew for new ImsServiceSevices
    @Override
    public void getUpdatedCitizenResultNew(Long requestId) throws BaseException {

        //*************** Start ***********************
        logger.info("\nThe method 'getUpdatedCitizenResultNew has been started...'");
        imsLogger.info("\nThe method 'getUpdatedCitizenResultNew has been started...'");
        //***************

        CardRequestTO crq = getCardRequestDAO().find(CardRequestTO.class, requestId);
        if (crq == null) {
            logger.error(BizExceptionCode.MMS_049, BizExceptionCode.GLB_021_MSG);
            imsLogger.error(BizExceptionCode.MMS_049,
                    BizExceptionCode.GLB_021_MSG);
            throw new ServiceException(BizExceptionCode.MMS_049,
                    BizExceptionCode.GLB_021_MSG);
        }
        // Anbari:getNIN and change logger
        CitizenTO citizenTO = getCitizenDAO().findRequestId(crq.getId());
        logger.info("\nMethod : getUpdatedCitizenResult, Field : {name : nationalID; value : "
                + citizenTO.getNationalID()
                + "; dependantCardRequestId : "
                + crq.getId() + "}");
        imsLogger
                .info("\nMethod : getUpdatedCitizenResult, Field : {name : nationalID; value : "
                        + citizenTO.getNationalID()
                        + "; dependantCardRequestId : " + crq.getId() + "}");

        IMSUpdateResultVTO imsUpdateResultVTO = null;
        if (EmsUtil.checkString(citizenTO.getNationalID())) {
            boolean exceptionFlag = false;
            try {
                imsUpdateResultVTO = getUpdatedCitizensInfoResultNew(citizenTO.getNationalID());
            } catch (BaseException e) {
                exceptionFlag = true;
                logger.error(
                        "\nException has happened after calling the method of 'getUpdatedCitizensInfoResult'."
                                + e.getExceptionCode(), e.getMessage(), e);
                imsLogger
                        .error("\nException has happened after calling the method of 'getUpdatedCitizensInfoResult'."
                                + e.getExceptionCode(), e.getMessage(), e);

                // Creating new request history by the action of
                // 'AFIS_RECEIVE_ERROR'
                String result = (SystemId.IMS.name() + ":"
                        + ConstValues.GAM_ERROR_WITH_UNLIMITED_RETRY
                        + e.getExceptionCode() + ":" + e.getMessage());
                // Anbari Fix history : remove ims RequestId
                // increateCardRequestHistory method argument
                createCardRequestHistory(crq.getId(), result,
                        CardRequestHistoryAction.AFIS_RECEIVE_ERROR);
            }

            if (!exceptionFlag && imsUpdateResultVTO != null) {
                // Anbari : Do it need Session Set rollback?
                try {
                    processImsGetUpdateResult(crq, imsUpdateResultVTO);
                } catch (Exception e) {
                    logger.error(
                            "\nException has happened after calling the method of 'processImsGetUpdateResult'.",
                            e.getMessage(), e);
                    imsLogger
                            .error("\nException has happened after calling the method of 'processImsGetUpdateResult'.",
                                    e.getMessage(), e);
                }

            }
        } else {
            throw new ServiceException(BizExceptionCode.MMS_051,
                    BizExceptionCode.MMS_051_MSG);
        }
    }

    // Anbari:IMS
    @javax.ejb.TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private IMSUpdateResultVTO getUpdatedCitizensInfoResultNew(String nationalId)
            throws BaseException {
        return getIMSService().getUpdatedCitizensResultNew(nationalId);
    }

    //Anbari:IMS
    private void processImsGetUpdateResult(CardRequestTO crq,
                                           IMSUpdateResultVTO imsUpdateResultVTO) throws BaseException {

//			CardRequestTO cardRequestTO = getCardRequestDAO().fetchCardRequest(
//					crq.getId());

        if (imsUpdateResultVTO.getErrorMessage().contains("UPDT-000016")) // WAITING
        {

            getCardRequestHistoryDAO().create(crq,
                    imsUpdateResultVTO.getErrorMessage(), SystemId.IMS, null,
                    CardRequestHistoryAction.AFIS_WAITING, null);

        } else if (imsUpdateResultVTO.getErrorMessage().contains("UPDT-000017")) // OK
        {

            crq.setState(CardRequestState.APPROVED_BY_AFIS);
            getCardRequestDAO().update(crq);
            getCardRequestHistoryDAO().create(crq,
                    imsUpdateResultVTO.getErrorMessage(), SystemId.IMS, null,
                    CardRequestHistoryAction.APPROVED_BY_AFIS, null);

            setIdentityChangeAndAfisState(crq, imsUpdateResultVTO);


        } else if (imsUpdateResultVTO.getErrorMessage().contains("UPDT-000020")) // Delete
        // DOCUMENT
        {
            String errMsg = createImsReultErrorMessage(imsUpdateResultVTO);
            getCardRequestHistoryDAO().create(crq,
                    errMsg, SystemId.IMS, null,
                    CardRequestHistoryAction.AFIS_DOCUMENT_ERROR, null);

            doDeleteAction(crq, DoDeleteAction.DeleteDocument,
                    errMsg);

            crq.setReEnrolledDate(new Date());
            getCardRequestDAO().update(crq);

        } else if (imsUpdateResultVTO.getErrorMessage().contains("UPDT-000021")) // Delete
        // IMAGE
        {
            String errMsg = createImsReultErrorMessage(imsUpdateResultVTO);
            getCardRequestHistoryDAO().create(crq,
                    errMsg, SystemId.IMS, null,
                    CardRequestHistoryAction.AFIS_IMAGE_ERROR, null);

            doDeleteAction(crq, DoDeleteAction.DeleteImage,
                    errMsg);

            crq.setReEnrolledDate(new Date());
            getCardRequestDAO().update(crq);

        } else if (imsUpdateResultVTO.getErrorMessage().contains("UPDT-000022")) // Delete
        // Finger
        {
            getCardRequestHistoryDAO().create(crq,
                    imsUpdateResultVTO.getErrorMessage(), SystemId.IMS, null,
                    CardRequestHistoryAction.AFIS_FINGER_ERROR, null);


            doDeleteAction(crq, DoDeleteAction.DeleteFinger,
                    imsUpdateResultVTO.getErrorMessage());

            crq.setReEnrolledDate(new Date());
            getCardRequestDAO().update(crq);

        } else if (imsUpdateResultVTO.getErrorMessage().contains("UPDT-000018")) // OK with images
        {

            getCardRequestHistoryDAO().create(crq,
                    imsUpdateResultVTO.getErrorMessage(), SystemId.IMS, null,
                    CardRequestHistoryAction.AFIS_IMAGES_UPDATED, null);

            processIMSGetUpdateImage(crq, imsUpdateResultVTO);

            setIdentityChangeAndAfisState(crq, imsUpdateResultVTO);

        } else if (imsUpdateResultVTO.getErrorMessage().contains("UPDT-000019")) // reject
        {
            //Trigger should edit to accept change state from Sent_To_Afis to Refer_To_Ccos
				/*
				getBiometricDAO().removeAllBioDataByRequestID(crq.getId());
				getDocumentDAO().removeByRequestIdAndType(crq.getId(), null);


				crq.setState(CardRequestState.REFERRED_TO_CCOS);
				getCardRequestDAO().update(crq);
				getCardRequestHistoryDAO().create(crq,
						imsUpdateResultVTO.getErrorMessage(), SystemId.IMS, null,
						CardRequestHistoryAction.AFIS_REJECT, null);
						*/
            String errMsg = createImsReultErrorMessage(imsUpdateResultVTO);
            crq.setState(CardRequestState.IMS_ERROR);
            getCardRequestDAO().update(crq);
            getCardRequestHistoryDAO().create(crq,
                    errMsg, SystemId.IMS, null,
                    CardRequestHistoryAction.AFIS_REJECT, null);

        } else if (imsUpdateResultVTO.getErrorMessage().contains("UPDT-000004")) {

            String result;
            List<Long> crqIds = new ArrayList<Long>();
            crqIds.add(crq.getId());

            // Updating cardRequestState to 'IMS_ERROR'
            crq.setState(CardRequestState.IMS_ERROR);

            // Creating new request history by the action of
            // 'AFIS_RECEIVE_ERROR'
            result = (SystemId.IMS.name() + ":"
                    + ConstValues.GAM_ERROR_WITH_NO_RETRY
                    + imsUpdateResultVTO.getErrorCode() + ":" + imsUpdateResultVTO
                    .getErrorMessage());


            createCardRequestHistory(crqIds,
                    imsUpdateResultVTO.getRequestID(), result,
                    CardRequestHistoryAction.AFIS_RECEIVE_ERROR);

        } else {

            String result;
            List<Long> crqIds = new ArrayList<Long>();
            crqIds.add(crq.getId());
            // Updating cardRequestState to 'IMS_ERROR'
            crq.setState(CardRequestState.IMS_ERROR);

            // Updating request history by the action of
            // 'AFIS_RECEIVE_ERROR'
            // TODO : This part is temporary assumed as
            // GAM_ERROR_WITH_NO_RETRY
            result = (SystemId.IMS.name() + ":"
                    + ConstValues.GAM_ERROR_WITH_NO_RETRY
                    + imsUpdateResultVTO.getErrorCode() + ":" + imsUpdateResultVTO
                    .getErrorMessage());
            createCardRequestHistory(crqIds,
                    imsUpdateResultVTO.getRequestID(), result,
                    CardRequestHistoryAction.AFIS_RECEIVE_ERROR);
        }
    }

    private String createImsReultErrorMessage(IMSUpdateResultVTO imsUpdateResultVTO) {
        String errorMessage = imsUpdateResultVTO.getErrorMessage();
        List<IMSErrorInfo> errorCodes = imsUpdateResultVTO.getErrorCodes();
        if (errorCodes != null) {
            for (IMSErrorInfo errorCode : errorCodes) {
                errorMessage += errorCode.toString() + " ,";
            }
        }
        return errorMessage;
    }

    //Anbari:IMS
    private void setIdentityChangeAndAfisState(CardRequestTO crq, IMSUpdateResultVTO imsUpdateResultVTO) throws BaseException,
            ServiceException {
        CitizenInfoTO citizenInfoTO;
        try {
            citizenInfoTO = getCitizenInfoDAO().find(CitizenInfoTO.class, crq.getCitizen().getId());
            if (imsUpdateResultVTO.getAfisState() != null)
                citizenInfoTO.setAfisState(imsUpdateResultVTO.getAfisState());
            else
                citizenInfoTO.setAfisState(AFISState.NOT_VALID);
            if (imsUpdateResultVTO.getIdentityChanged() != null)
                citizenInfoTO.setIdentityChanged(imsUpdateResultVTO.getIdentityChanged());
            else
                citizenInfoTO.setIdentityChanged(0000);
            getCitizenInfoDAO().update(citizenInfoTO);
            logger.info("The citizen who associated to the card request id of '"
                    + crq.getId()
                    + "' has been updated. Details : {citizenId : "
                    + citizenInfoTO.getId()
                    + "; afisState : "
                    + citizenInfoTO.getAfisState()
                    + "; identityChanged : "
                    + citizenInfoTO.getIdentityChanged() + "}");
            imsLogger
                    .info("The citizen who associated to the card request id of '"
                            + crq.getId()
                            + "' has been updated. Details : {citizenId : "
                            + citizenInfoTO.getId()
                            + "; afisState : "
                            + citizenInfoTO.getAfisState()
                            + "; identityChanged : "
                            + citizenInfoTO
                            .getIdentityChanged() + "}");
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(
                    BizExceptionCode.MMS_050, e.getMessage(), e);
        }
    }

    //Anbari:IMS
    private void processIMSGetUpdateImage(CardRequestTO crq,
                                          IMSUpdateResultVTO imsUpdateResultVTO) throws BaseException {
        CardRequestTO cardRequestTO = getCardRequestDAO().fetchCardRequest(
                crq.getId());
        cardRequestTO.setState(CardRequestState.APPROVED_BY_AFIS);
        saveIMSModifiedFaceImage(cardRequestTO, BiometricType.FACE_CHIP,
                imsUpdateResultVTO.getFaceCHIP());
        saveIMSModifiedFaceImage(cardRequestTO, BiometricType.FACE_MLI,
                imsUpdateResultVTO.getFaceMLI());
        saveIMSModifiedFaceImage(cardRequestTO, BiometricType.FACE_IMS,
                imsUpdateResultVTO.getFaceIMS());
        saveIMSModifiedFaceImage(cardRequestTO, BiometricType.FACE_LASER,
                imsUpdateResultVTO.getFaceLASER());
        cardRequestTO.setState(CardRequestState.APPROVED_BY_AFIS);
        getCardRequestDAO().update(cardRequestTO);
        getCardRequestHistoryDAO().create(cardRequestTO,
                imsUpdateResultVTO.getErrorMessage(), SystemId.EMS, null,
                CardRequestHistoryAction.APPROVED_BY_AFIS, null);
    }

    // Anbari:IMS Do Delete Bio Data And Send SMS ( Fix RollBack)
    public void doDeleteAction(CardRequestTO crq, DoDeleteAction deleteAction,
                               String errorMrssage) throws BaseException {

        try {
            CardRequestTO cardRequestTO = getCardRequestDAO().fetchCardRequest(
                    crq.getId());
            cardRequestTO.setState(CardRequestState.DOCUMENT_AUTHENTICATED);


            Integer imsSMSEnable = Integer.valueOf(EmsUtil.getProfileValue(
                    ProfileKeyName.KEY_IMS_SMS_ENABLE, DEFAULT_IMS_SMS_ENABLE));

            // Do actions
            if (DoDeleteAction.DeleteDocument.equals(deleteAction)) {
                getDocumentDAO().removeByRequestIdAndType(
                        cardRequestTO.getId(), null);
            } else if (DoDeleteAction.DeleteImage.equals(deleteAction)) {
                getBiometricDAO().removeFaceInfoByRequestId(
                        cardRequestTO.getId());
            } else if (DoDeleteAction.DeleteFinger.equals(deleteAction)) {
                getBiometricDAO().removeFingersInfoByCitizenId(
                        cardRequestTO.getId());
            }

            //send SMS if sms sending for ims is enable
            if (imsSMSEnable == 1) {
                if (DoDeleteAction.DeleteDocument.equals(deleteAction)) {
                    getCardRequestDAO().addRequestedSmsForNotification(
                            cardRequestTO.getId(), SMSTypeState.IMS_DELETE_DOC);
                } else if (DoDeleteAction.DeleteImage.equals(deleteAction)) {
                    getCardRequestDAO()
                            .addRequestedSmsForNotification(
                                    cardRequestTO.getId(),
                                    SMSTypeState.IMS_DELETE_FACE);
                } else if (DoDeleteAction.DeleteFinger.equals(deleteAction)) {
                    getCardRequestDAO().addRequestedSmsForNotification(
                            cardRequestTO.getId(),
                            SMSTypeState.IMS_DELETE_FINGER);
                }
            }

            // update State and create hitory
            getCardRequestDAO().update(cardRequestTO);
            getCardRequestHistoryDAO().create(cardRequestTO, errorMrssage,
                    SystemId.EMS, null,
                    CardRequestHistoryAction.AUTHENTICATE_DOCUMENT, null);

        } catch (Exception e) {
            logger.error(BizExceptionCode.CRE_022, e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CRE_022,
                    BizExceptionCode.GLB_028_MSG, e);
        }

    }

    //Anbari:IMS
    //todo : change get(0) and replace it
    private void saveIMSModifiedFaceImage(CardRequestTO crq,
                                          BiometricType biometricType, byte[] faceImage) throws BaseException {
        List<BiometricTO> storedImage = getBiometricDAO()
                .findByRequestIdAndType(crq.getId(), biometricType);
        storedImage.get(0).setData(faceImage);
        getBiometricDAO().update(storedImage.get(0));
    }

    //Anbari:IMS Async
    @Override
    @Asynchronous
    public Future<String> updateCitizenInfoAsync(Long requestId)
            throws BaseException {
        CardRequestTO cardRequestTO = getCardRequestDAO().find(CardRequestTO.class, requestId);
        if (cardRequestTO == null) {
            throw new ServiceException(BizExceptionCode.MMS_043,
                    BizExceptionCode.MMS_043_MSG);
        }
        String reqId = null;
        try {
            Long uniqueRequestId = getCardRequestHistoryDAO()
                    .getRequestIdFromSequence(
                            SequenceName.SEQ_EMS_REQUEST_ID_IMS_UPDATE_CITIZEN_INFO);
            reqId = EmsUtil.createRequestIdForMessage(
                    uniqueRequestId.toString(), 32, "", "0");
            List<CardRequestTO> cardRequestTOs = new ArrayList<CardRequestTO>();
            cardRequestTOs.add(cardRequestTO);
            innerUpdateCitizensInfo(cardRequestTOs, reqId);
            cardRequestTO.setState(CardRequestState.SENT_TO_AFIS);
            createNewCardRequestHistory(cardRequestTO, reqId,
                    CardRequestState.SENT_TO_AFIS.name(),
                    CardRequestHistoryAction.SENT_TO_AFIS);
        } catch (Exception e) {
            try {
                if (reqId != null) {
                    String message;
                    if (e instanceof ValidationException) {
                        message = e.getCause().getMessage();
                    } else {
                        message = e.getMessage();
                    }
                    createNewCardRequestHistory(
                            cardRequestTO,
                            reqId,
                            ConstValues.GAM_ERROR_WITH_UNLIMITED_RETRY
                                    + message,
                            CardRequestHistoryAction.AFIS_SEND_ERROR);
                }
            } catch (BaseException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new ServiceException(BizExceptionCode.MMS_064,
                        BizExceptionCode.GLB_008_MSG, ex);
            }

            if (e instanceof BaseException) {
                throw (BaseException) e;
            } else {
                throw new ServiceException(BizExceptionCode.MMS_065,
                        BizExceptionCode.GLB_008_MSG, e);
            }

        }
        return new AsyncResult<String>("");


    }


}
