package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.*;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.*;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.vol.BatchDispatchInfoVTO;
import com.gam.nocr.ems.data.domain.vol.CardDispatchInfoVTO;
import com.gam.nocr.ems.data.domain.vol.CardInfoVTO;
import com.gam.nocr.ems.data.domain.vol.EmsCardDeliverInfo;
import com.gam.nocr.ems.data.domain.ws.ImsCitizenInfoRequestWTO;
import com.gam.nocr.ems.data.domain.ws.ImsCitizenInfoResponseWTO;
import com.gam.nocr.ems.data.enums.*;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.NistParser;
import com.gam.nocr.ems.util.NistResult;
import gampooya.tools.date.DateUtil;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Future;

import static com.gam.nocr.ems.config.EMSLogicalNames.*;
import static com.gam.nocr.ems.data.enums.CMSCardState.*;
import static com.gam.nocr.ems.data.enums.CardRequestType.*;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "CardManagementService")
@Local(CardManagementServiceLocal.class)
@Remote(CardManagementServiceRemote.class)
public class CardManagementServiceImpl extends EMSAbstractService implements CardManagementServiceLocal, CardManagementServiceRemote {

    private static final Logger logger = BaseLog.getLogger(CardManagementServiceImpl.class);
    private static final Logger handoutJobLogger = BaseLog.getLogger("cardHandingOutNotification");
    private static final Logger unsuccessfulDeliveryLogger = BaseLog.getLogger("unsuccessfulDeliveryRequest");
    private static final Logger imsVerificationInDelivery = BaseLog.getLogger("imsVerificationInDelivery");

    private static final String DEFAULT_PRODUCT_ID = "200";
    private static final String DEFAULT_DELIVER_MESSAGE = "\u0627\u06CC\u0646\u062C\u0627\u0646\u0628 {firstName} {lastName} \u0628\u0647 \u0634\u0645\u0627\u0631\u0647 \u0645\u0644\u06CC {NID} \u06A9\u0627\u0631\u062A \u0647\u0648\u0634\u0645\u0646\u062F \u0645\u0644\u06CC \u062E\u0648\u062F \u0631\u0627 \u062F\u0631 \u062A\u0627\u0631\u06CC\u062E {currentDate} \u062A\u062D\u0648\u06CC\u0644 \u06AF\u0631\u0641\u062A\u0645.";
    private static final String DEFAULT_CARD_LOST_CONFIRM = "false";

    @Resource
    SessionContext sessionContext;
    /**
     * Card states at CMS sub system
     */
    //private static final int CMS_STATE_MISSING = 6;
    //private static final int CMS_STATE_HANDED_OUT = 5;
    //private static final int CMS_STATE_EXPIRED = 7;
    //private static final int CMS_STATE_SUSPENDED = 8;
    //private static final int CMS_STATE_REVOKED = 9;
    //private static final int CMS_STATE_DESTROYED = 10;

    /**
     * Reasons of UnsuccessfulDelivery
     */
    private static final String UNSUCCESSFUL_DELIVERY_IDENTITY_CHANGE = "UNSUCCESSFUL_DELIVERY_IDENTITY_CHANGE";
    private static final String UNSUCCESSFUL_DELIVERY_CARD_DAMAGE = "UNSUCCESSFUL_DELIVERY_CARD_DAMAGE";
    private static final String UNSUCCESSFUL_DELIVERY_BIOMETRIC_CHANGE = "UNSUCCESSFUL_DELIVERY_BIOMETRIC_CHANGE";
    private static final String FINGER_CUT = "finher_cut";
    private static final String DEFAULT_DURATION_OF_VALID_YEAR_FOR_EXTEND = "7";
    private static final String DEFAULT_KEY_PARSE_NIST_ALLWAYS = "true";


    /**
     * Private methods
     */

    /**
     * ===============
     * Getter for DAOs
     * ===============
     */

    /**
     * getDispatchDAO
     *
     * @return an instance of type DispatchDAO
     * @throws {@link BaseException}
     */
    private DispatchDAO getDispatchDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_DISPATCHING));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.CMS_008,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_DISPATCHING.split(","));
        }
    }

    private NistHeaderDAO getNistHeaderDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_NIST_HEADER));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.CMS_087,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_NIST_HEADER.split(","));
        }
    }

    /**
     * getCardDAO
     *
     * @return an instance of type CardDAO
     * @throws {@link BaseException}
     */
    private CardDAO getCardDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CARD));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.CMS_009,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_CARD.split(","));
        }
    }

    /**
     * getEnrollmentOfficeDAO
     *
     * @return an instance of type CardDAO
     * @throws {@link BaseException}
     */
    private EnrollmentOfficeDAO getEnrollmentOfficeDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_ENROLLMENT_OFFICE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.CMS_009,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_CARD.split(","));
        }
    }

    private CardRequestService getCardRequestService(UserProfileTO userProfileTO)
			throws BaseException {
		CardRequestService cardRequestService;
		try {
			cardRequestService = ServiceFactoryProvider
					.getServiceFactory()
					.getService(
							EMSLogicalNames
									.getServiceJNDIName(EMSLogicalNames.SRV_CARD_REQUEST), EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new DelegatorException(BizExceptionCode.CRD_001,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_CARD_REQUEST.split(","));
		}
		cardRequestService.setUserProfileTO(userProfileTO);
		return cardRequestService;
	}

    /**
     * @author ganjyar
     * @return an instance of type batchDAO
     * @throws {@link BaseException}
     */
    private BatchDAO getBatchDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_BATCH));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.CMS_008,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_BATCH.split(","));
        }
    }

    /**
     * getCardRequestDAO
     *
     * @return an instance of type CardRequestDAO
     * @throws {@link BaseException}
     */
    private CardRequestDAO getCardRequestDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.CMS_010,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_CARD_REQUEST.split(","));
        }
    }

    /**
     * getCardRequestHistoryDAO
     */
    private CardRequestHistoryDAO getCardRequestHistoryDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory()
                    .getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST_HISTORY));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.CMS_048,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_CARD_REQUEST_HISTORY.split(","));
        }
    }

    private CitizenDAO getCitizenDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory()
                    .getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CITIZEN));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.CMS_054,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_CITIZEN.split(","));
        }
    }

    private BiometricDAO getBiometricDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_BIOMETRIC));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.CMS_049,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    DAO_BIOMETRIC.split(","));
        }
    }

    private BiometricInfoDAO getBiometricInfoDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					getDaoJNDIName(DAO_BIOMETRIC_INFO));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.CMS_084,
					BizExceptionCode.GLB_001_MSG, e,
					DAO_BIOMETRIC_INFO.split(","));
		}
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


    /**
     * ===================
     * Getter for Services
     * ===================
     */

    /**
     * getCMSService
     *
     * @return an instance of type {@link CMSService}
     * @throws {@link BaseException}
     */
    private CMSService getCMSService() throws BaseException {
        CMSService cmsService;
        try {
            cmsService = ServiceFactoryProvider.getServiceFactory()
                    .getService(EMSLogicalNames.getExternalServiceJNDIName(EMSLogicalNames.SRV_CMS), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.CMS_001,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_CMS.split(","));
        }
        cmsService.setUserProfileTO(getUserProfileTO());
        return cmsService;
    }

    /**
     * getIMSManagementService
     *
     * @return an instance of type {@link IMSManagementService}
     * @throws {@link BaseException}
     */
    private IMSManagementService getIMSManagementService() throws BaseException {
        IMSManagementService imsManagementService;
        try {
            imsManagementService = ServiceFactoryProvider.getServiceFactory()
                    .getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_IMS_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.CMS_053,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_IMS_MANAGEMENT.split(","));
        }
        imsManagementService.setUserProfileTO(getUserProfileTO());
        return imsManagementService;
    }

    /**
     * getIMSService
     *
     * @return an instance of type {@link com.gam.nocr.ems.biz.service.IMSService}
     */
    private IMSService getIMSService() throws BaseException {
        IMSService imsService;
        try {
            imsService = ServiceFactoryProvider.getServiceFactory()
                    .getService(EMSLogicalNames.getExternalServiceJNDIName(EMSLogicalNames.SRV_IMS), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.CMS_050,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_IMS.split(","));
        }
        imsService.setUserProfileTO(getUserProfileTO());
        return imsService;
    }

    /**
     * getBusinessLogService
     *
     * @return an instance of type {@link BusinessLogService}
     */
    private BusinessLogService getBusinessLogService() throws BaseException {
        BusinessLogService businessLogService;
        try {
            businessLogService = ServiceFactoryProvider.getServiceFactory()
                    .getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_BUSINESS_LOG), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.CMS_052,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_BUSINESS_LOG.split(","));
        }
        return businessLogService;
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

    /**
     * The method verifyFirstIssuance is used to check whether the issuance request of type 'FIRST_CARD' is valid or
     * not
     *
     * @param allCardsList is a list of type {@link CardInfoVTO}
     * @throws {@link BaseException}
     */
    private void verifyFirstIssuance(List<CardInfoVTO> allCardsList) throws BaseException {
        if (allCardsList != null
                && !allCardsList.isEmpty()) {
            CardInfoVTO latestCardInfoVTO = getLatestFromCardVTOList(allCardsList);
            if (MISSING.getCmsCardState() != latestCardInfoVTO.getStatus()) {
                throw new ServiceException(BizExceptionCode.CMS_002, BizExceptionCode.CMS_002_MSG);
            }
        }
    }

    /**
     * The method verifyReplicaIssuance is used to check whether the issuance request of type 'REPLICA' is valid or not
     *
     * @param allCardsList is a list of type {@link CardInfoVTO}
     * @throws {@link BaseException}
     */
    private void verifyReplicaIssuance(List<CardInfoVTO> allCardsList) throws BaseException {
        if (allCardsList != null
                && allCardsList.size() != 0) {
            CardInfoVTO latestCardInfoVTO = getLatestFromCardVTOList(allCardsList);
            if (HANDED_OUT.getCmsCardState() != latestCardInfoVTO.getStatus()
                    && SUSPENDED.getCmsCardState() != latestCardInfoVTO.getStatus()
                    && REVOKED.getCmsCardState() != latestCardInfoVTO.getStatus()
                    && DESTROYED.getCmsCardState() != latestCardInfoVTO.getStatus()) {
                throw new ServiceException(BizExceptionCode.CMS_003, BizExceptionCode.CMS_003_MSG);
            }

        } else {
            throw new ServiceException(BizExceptionCode.CMS_004, BizExceptionCode.CMS_003_MSG);
        }
    }


    /**
     * The method verifyReplaceIssuance is used to check whether the issuance request of type 'REPLACE' is valid or not
     *
     * @param cardInfoVTO is an instance of type {@link CardInfoVTO}
     * @throws {@link BaseException}
     */
    private void verifyReplaceIssuance(CardInfoVTO cardInfoVTO) throws BaseException {
        if (cardInfoVTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_005, BizExceptionCode.CMS_005_MSG);

        } else if (HANDED_OUT.getCmsCardState() != cardInfoVTO.getStatus()) {
        	if (SUSPENDED.getCmsCardState() == cardInfoVTO.getStatus()){
        		throw new ServiceException(BizExceptionCode.CMS_062, BizExceptionCode.CMS_005_MSG);
        	}
            throw new ServiceException(BizExceptionCode.CMS_044, BizExceptionCode.CMS_005_MSG);
        }
    }

    /**
     * The method verifyExtendIssuance is used to check whether the issuance request of type 'EXTEND' is valid or not
     *
     * @param allCardsList is a list of type {@link CardInfoVTO}
     * @throws {@link BaseException}
     */
    private void verifyExtendIssuance(List<CardInfoVTO> allCardsList) throws BaseException {
        try {
            if (allCardsList != null
                    && allCardsList.size() != 0) {
                CardInfoVTO latestCardInfoVTO = getLatestFromCardVTOList(allCardsList);
                if (HANDED_OUT.getCmsCardState() != latestCardInfoVTO.getStatus()
                        && EXPIRED.getCmsCardState() != latestCardInfoVTO.getStatus()) {
                    throw new ServiceException(BizExceptionCode.CMS_006, BizExceptionCode.CMS_006_MSG);
                }

                //Anbari: check issuanceDate of lastCard
                Integer extenVvalidYear = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_DURATION_OF_VALID_YEAR_FOR_EXTEND,DEFAULT_DURATION_OF_VALID_YEAR_FOR_EXTEND));
                Date issuanceDate = latestCardInfoVTO.getIssuanceDate();
                if(issuanceDate != null){
	                Date validDate = EmsUtil.getDateAtMidnight(EmsUtil.differYear(issuanceDate, extenVvalidYear));
	                Date currenetDate = EmsUtil.getDateAtMidnight(new Date());
	                if(validDate.after(currenetDate))
	                	 throw new ServiceException(BizExceptionCode.CMS_085, BizExceptionCode.CMS_075_MSG);
                }

            } else {
                throw new ServiceException(BizExceptionCode.CMS_007, BizExceptionCode.CMS_006_MSG);
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CMS_042, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * The method getLatestFromCardVTOList is used to find the latest cardInfoVTO between the list
     *
     * @param cardInfoVTOList is a list of type {@link CardInfoVTO}
     * @return an object of type {@link CardInfoVTO}
     * @throws {@link BaseException}
     */
    private CardInfoVTO getLatestFromCardVTOList(List<CardInfoVTO> cardInfoVTOList) throws BaseException {
        try {
            Date date = cardInfoVTOList.get(0).getIssuanceDate();
            int latestDate = 0;
            for (int i = 1; i < cardInfoVTOList.size(); i++) {
                if (date.before(cardInfoVTOList.get(i).getIssuanceDate())) {
                    date = cardInfoVTOList.get(i).getIssuanceDate();
                    latestDate = i;
                }
            }
            return cardInfoVTOList.get(latestDate);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CMS_041, BizExceptionCode.CMS_006_MSG, e);
        }
    }

    /**
     * The method findReceivedBatches is used to fetch the list of batches which were received
     *
     * @return a list of type {@link BatchTO}
     * @throws {@link BaseException}
     */
    private List<BatchTO> findReceivedBatches(Integer from, Integer to) throws BaseException {
        return getDispatchDAO().findReceivedBatches(from, to);
    }


    /**
     * The method findMissedBatches is used to fetch the list of batches which missed
     * In this method first checks profilekey for confirmation missed batches is true or false.
     * if it was true then just fetch batches which confirmed and if it was false just fetch batches which their lost
     * date is greater than interval missed time.
     * @return a list of type {@link BatchTO}
     * @throws {@link BaseException}
     */
	private List<BatchTO> findMissedBatches(Integer from, Integer to)
			throws BaseException {

		try {
			// true: need to manager confirm
			if (Boolean.valueOf(EmsUtil.getProfileValue(
					ProfileKeyName.KEY_LOST_CARD_CONFIRM,
					DEFAULT_CARD_LOST_CONFIRM))) {
				return getDispatchDAO().findMissedBatchesConfirmed(from, to);

			} else {

				return getDispatchDAO().findMissedBatches(from, to);

			}

		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.CMS_081,
					BizExceptionCode.GLB_008_MSG, e);
		}

	}

    /**
     * The method findMissedBoxes is used to fetch the list of boxes which were missed
     *
     * @return a list of type {@link BoxTO}
     * @throws {@link BaseException}
     */
    private List<BoxTO> findMissedBoxes(Integer from, Integer to) throws BaseException {
        return getDispatchDAO().findMissedBoxes(from, to);
    }

    /**
     * The method findMissedCards is used to fetch the list of cards which were missed
     * In this method first checks profilekey for confirmation missed card is true or false.
     * if it was true then just fetch cards which confirmed and if it was false just fetch cards which their lost
     * date is greater than interval missed time.
     * @return a list of type {@link CardTO}
     * @throws {@link BaseException}
     */
	private List<CardTO> findMissedCards(Integer from, Integer to)
			throws BaseException {

		try {
			// true: need to manager confirm
			if (Boolean.valueOf(EmsUtil.getProfileValue(
					ProfileKeyName.KEY_LOST_CARD_CONFIRM,
					DEFAULT_CARD_LOST_CONFIRM))) {
				return getDispatchDAO().findMissedCardsConfirmed(from, to);

			} else {

				return getDispatchDAO().findMissedCards(from, to);

			}

		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.CMS_061,
					BizExceptionCode.GLB_008_MSG, e);
		}

	}

    /**
     * The method findCardRequestsByState is used to fetch the list of cards which were missed
     *
     * @return a list of type {@link CardTO}
     * @throws {@link BaseException}
     */
    private List<CardRequestTO> findCardRequestsByState(CardRequestState cardRequestState) throws BaseException {
        return getCardRequestDAO().findByCardRequestState(cardRequestState);
    }

    /**
     * The method updateRequestState is used to update the state of a specified instance of type {@link CardRequestTO}
     * from
     * one state to another state
     *
     * @param cardRequestTO carries the required attributes which are needed to update
     * @param from          represents the primitive state that is wanted to be changed
     * @param to            represents the intended state
     * @throws {@link BaseException}
     */
    private void updateRequestState(CardRequestTO cardRequestTO,
                                    CardRequestState from,
                                    CardRequestState to) throws BaseException {
        getCardRequestDAO().updateRequestState(cardRequestTO, from, to);

    }

    /**
     * The method deliverFirstCard is used to handle the businesses which are needed in delivering the first card
     *
     * @param cardRequestIdListForDeliver
     * @param cardIdListForDeliver
     * @param cardRequestTO
     * @throws {@link BaseException}
     */
    private void deliverFirstCard(CardRequestTO cardRequestTO,
                                  List<Long> cardRequestIdListForDeliver,
                                  List<Long> cardIdListForDeliver) throws BaseException {
        if (cardRequestTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_027, BizExceptionCode.CMS_011_MSG);
        }
        CardTO cardTO = cardRequestTO.getCard();
        if (cardTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_028, BizExceptionCode.CMS_018_MSG, new Long[]{cardRequestTO.getId()});
        }

        if (cardTO.getCrn() != null) {
            getCMSService().cardHandedOut(cardTO.getCrn());
            addToList(cardRequestIdListForDeliver, cardRequestTO.getId());
            addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());
        }
    }

    /**
     * The method deliverReplicaCard is used to handle the businesses which are needed in delivering the replica card.
     * This method also handles in advance revoked cards
     *
     * @param cardIdListForRevoke
     * @param cardRequestIdListForDeliver
     * @param cardIdListForDeliver
     * @param cardRequestTO
     * @throws {@link BaseException}
     */
    private void deliverReplicaCard(CardRequestTO cardRequestTO,
                                    List<String> cardIdListForRevoke,
                                    List<Long> cardRequestIdListForDeliver,
                                    List<Long> cardIdListForDeliver) throws BaseException {
        if (cardRequestTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_025, BizExceptionCode.CMS_011_MSG);
        }
        CitizenTO citizenTO = cardRequestTO.getCitizen();
        if (citizenTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_026, BizExceptionCode.CMS_022_MSG, new Long[]{cardRequestTO.getId()});
        }

        String productId = null;
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            productId = (String) pm.getProfile(ProfileKeyName.KEY_CMS_ISSUE_CARD_PRODUCT_ID, true, null, null);
        } catch (Exception e) {
            logger.warn(BizExceptionCode.CMS_045, BizExceptionCode.GLB_009_MSG, e);
        }

        if (productId == null || productId.isEmpty()) {
            productId = DEFAULT_PRODUCT_ID;
        }

        CardInfoVTO oldCardInfoVTO = getCMSService().getCurrentCitizenCardByProduct(citizenTO.getNationalID(), productId);
        try {
        	if(!oldCardInfoVTO.getCrn().equals(cardRequestTO.getCard().getCrn()))
        	{
            getCMSService().revokeCard(oldCardInfoVTO.getCrn(), oldCardInfoVTO.getReason());
            addToList(cardIdListForRevoke, oldCardInfoVTO.getCrn());
        	}
            getCMSService().cardHandedOut(cardRequestTO.getCard().getCrn());
            addToList(cardRequestIdListForDeliver, cardRequestTO.getId());
            addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
			if (BizExceptionCode.CSI_071.equals(e.getExceptionCode())) {
				if (!oldCardInfoVTO.getCrn().equals(
						cardRequestTO.getCard().getCrn()))
					addToList(cardIdListForRevoke, oldCardInfoVTO.getCrn());
				getCMSService().cardHandedOut(cardRequestTO.getCard().getCrn());
				addToList(cardRequestIdListForDeliver, cardRequestTO.getId());
				addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());
			} else {
                throw e;
            }
        }


    }

    /**
     * The method deliverReplaceCard is used to handle the businesses which are needed in delivering the replace card, and
     * also handles in advance revoked cards(ExceptionCode : CSI_071)
     *
     * @param cardRequestTO
     * @param cardIdListForRevoke
     * @param cardRequestIdListForDeliver
     * @param cardIdListForDeliver
     * @throws {@link BaseException}
     */
    private void deliverReplaceCard(CardRequestTO cardRequestTO,
                                    List<String> cardIdListForRevoke,
                                    List<Long> cardRequestIdListForDeliver,
                                    List<Long> cardIdListForDeliver) throws BaseException {
        if (cardRequestTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_023, BizExceptionCode.CMS_011_MSG);
        }
        CitizenTO citizenTO = cardRequestTO.getCitizen();
        if (citizenTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_024, BizExceptionCode.CMS_022_MSG, new Long[]{cardRequestTO.getId()});
        }

        String productId = null;
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            productId = (String) pm.getProfile(ProfileKeyName.KEY_CMS_ISSUE_CARD_PRODUCT_ID, true, null, null);
        } catch (Exception e) {
            logger.warn(BizExceptionCode.CMS_046, BizExceptionCode.GLB_009_MSG, e);
        }

        if (productId == null || productId.isEmpty()) {
            productId = DEFAULT_PRODUCT_ID;
        }

        CardInfoVTO oldCardInfoVTO = getCMSService().getCurrentCitizenCardByProduct(citizenTO.getNationalID(), productId);
		try {
			if (!oldCardInfoVTO.getCrn().equals(
					cardRequestTO.getCard().getCrn())) {
				getCMSService().revokeCard(oldCardInfoVTO.getCrn(),
						oldCardInfoVTO.getReason());
				addToList(cardIdListForRevoke, oldCardInfoVTO.getCrn());
			}
			getCMSService().cardHandedOut(cardRequestTO.getCard().getCrn());
			addToList(cardRequestIdListForDeliver, cardRequestTO.getId());
			addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());
		} catch (BaseException e) {
            logger.error(e.getMessage(), e);
			if (BizExceptionCode.CSI_071.equals(e.getExceptionCode())) {
				if (!oldCardInfoVTO.getCrn().equals(
						cardRequestTO.getCard().getCrn())) {
					addToList(cardIdListForRevoke, oldCardInfoVTO.getCrn());
				}
				getCMSService().cardHandedOut(cardRequestTO.getCard().getCrn());
				addToList(cardRequestIdListForDeliver, cardRequestTO.getId());
				addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());
			} else {
                throw e;
            }
        }

    }

    /**
     * The method deliverExtendCard is used to handle the businesses which are needed in delivering the extend card, and
     * also handles in advance expired cards(ExceptionCode : CSI_050)
     *
     * @param cardIdListForExpire
     * @param cardRequestIdListForDeliver
     * @param cardIdListForDeliver
     * @param cardRequestTO
     * @throws {@link BaseException}
     */
    private void deliverExtendCard(CardRequestTO cardRequestTO,
                                   List<String> cardIdListForExpire,
                                   List<Long> cardRequestIdListForDeliver,
                                   List<Long> cardIdListForDeliver) throws BaseException {
        if (cardRequestTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_021, BizExceptionCode.CMS_011_MSG);
        }
        CitizenTO citizenTO = cardRequestTO.getCitizen();
        if (citizenTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_022, BizExceptionCode.CMS_022_MSG, new Long[]{cardRequestTO.getId()});
        }

        String productId = null;
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            productId = (String) pm.getProfile(ProfileKeyName.KEY_CMS_ISSUE_CARD_PRODUCT_ID, true, null, null);
        } catch (Exception e) {
            logger.warn(BizExceptionCode.CMS_047, BizExceptionCode.GLB_009_MSG, e);
        }

        if (productId == null || productId.isEmpty()) {
            productId = DEFAULT_PRODUCT_ID;
        }

        CardInfoVTO oldCardInfoVTO = getCMSService().getCurrentCitizenCardByProduct(citizenTO.getNationalID(), productId);
        try {
            getCMSService().expireCard(oldCardInfoVTO.getCrn(), oldCardInfoVTO.getReason());
            addToList(cardIdListForExpire, oldCardInfoVTO.getCrn());
            getCMSService().cardHandedOut(cardRequestTO.getCard().getCrn());
            addToList(cardRequestIdListForDeliver, cardRequestTO.getId());
            addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            if (BizExceptionCode.CSI_050.equals(e.getExceptionCode())) {
                addToList(cardIdListForExpire, oldCardInfoVTO.getCrn());
                getCMSService().cardHandedOut(cardRequestTO.getCard().getCrn());
                addToList(cardRequestIdListForDeliver, cardRequestTO.getId());
                addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());
            } else {
                throw e;
            }
        }
    }

    /**
     * The method deliverUnsuccessfulDeliveryCard is used to handle the businesses which are needed in delivering the
     * unsuccessful delivery card
     *
     * @param cardRequestIdListForDeliver
     * @param cardIdListForDeliver
     * @param cardRequestTO
     * @throws {@link BaseException}
     */
    private void deliverUnsuccessfulDeliveryCard(CardRequestTO cardRequestTO,
                                                 List<Long> cardRequestIdListForDeliver,
                                                 List<Long> cardIdListForDeliver) throws BaseException {
        if (cardRequestTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_019, BizExceptionCode.CMS_011_MSG);
        }
        CardTO cardTO = cardRequestTO.getCard();
        if (cardTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_020, BizExceptionCode.CMS_018_MSG, new Long[]{cardRequestTO.getId()});
        }
        getCMSService().cardHandedOut(cardTO.getCrn());
        addToList(cardRequestIdListForDeliver, cardRequestTO.getId());
        addToList(cardIdListForDeliver, cardTO.getId());
    }

    private void addToList(List<Long> longList, long longId) {
        if (longList == null) {
            longList = new ArrayList<Long>();
        }
        longList.add(longId);
    }

    private void addToList(List<String> stringList, String stringId) {
        if (stringList == null) {
            stringList = new ArrayList<String>();
        }
        stringList.add(stringId);
    }

    /**
     * The method notifyUnsuccessfulDeliveryForIdentityChange is used to handle all the activities which are needed in
     * unsuccessful Delivery by reason of 'IdentityChange'
     *
     * @param requestID an object of type {@link Long} which represents the id of an specified object of type {@link
     *                  CardRequestTO}
     * @param reason    an enumeration instance of type {@link UnSuccessfulDeliveryRequestReason}
     * @throws {@link BaseException}
     */
    private void notifyUnsuccessfulDeliveryForIdentityChange(Long requestID, UnSuccessfulDeliveryRequestReason reason) throws BaseException {
        CardRequestTO cardRequestTO = getCardRequestDAO().find(CardRequestTO.class, requestID);
        if (cardRequestTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_011, BizExceptionCode.CMS_011_MSG);
        }

        try {
            revokeCard(cardRequestTO, reason.name());
            cardRequestTO.setState(CardRequestState.STOPPED);
            getCardRequestHistoryDAO().create(
                    new CardRequestTO(cardRequestTO.getId()),
                    null,
                    SystemId.CMS,
                    null,
                    CardRequestHistoryAction.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_IDENTITY_CHANGE,
                    getUserProfileTO().getUserName());
        } catch (BaseException e) {
            if (BizExceptionCode.CSI_071.equals(e.getExceptionCode())) {
                cardRequestTO.setState(CardRequestState.STOPPED);
                getCardRequestHistoryDAO().create(
                        new CardRequestTO(cardRequestTO.getId()),
                        null,
                        SystemId.CMS,
                        null,
                        CardRequestHistoryAction.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_IDENTITY_CHANGE,
                        getUserProfileTO().getUserName());
            } else {
                throw e;
            }
        }

    }

    /**
     * The method notifyUnsuccessfulDeliveryForDamagedCard is used to handle all the activities which are needed in
     * unsuccessful Delivery by reason of 'CardDamage'
     *
     * @param requestID an object of type {@link Long} which represents the id of an specified object of type {@link
     *                  CardRequestTO}
     * @param reason    an enumeration instance of type {@link UnSuccessfulDeliveryRequestReason}
     * @throws {@link BaseException}
     */
    private void notifyUnsuccessfulDeliveryForDamagedCard(Long requestID, UnSuccessfulDeliveryRequestReason reason) throws BaseException {
        CardRequestTO cardRequestTO = getCardRequestDAO().find(CardRequestTO.class, requestID);
        if (cardRequestTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_012, BizExceptionCode.CMS_011_MSG);
        }
        if (CardRequestState.READY_TO_DELIVER.equals(cardRequestTO.getState())) {
            cardRequestTO.setState(CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE);
            getCardRequestHistoryDAO().create(
                    new CardRequestTO(cardRequestTO.getId()),
                    null,
                    SystemId.CMS,
                    null,
                    CardRequestHistoryAction.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE,
                    getUserProfileTO().getUserName());
        } else {
            throw new ServiceException(BizExceptionCode.CMS_029, BizExceptionCode.CMS_029_MSG);

        }
    }

    /**
     * The method notifyUnsuccessfulDeliveryForBiometricChange is used to handle all the activities which are needed in
     * unsuccessful Delivery by reason of 'Biometric Change'
     *
     * @param requestID an object of type {@link Long} which represents the id of an specified object of type {@link
     *                  CardRequestTO}
     * @param reason    an enumeration instance of type {@link UnSuccessfulDeliveryRequestReason}
     * @throws {@link BaseException}
     */
    private void notifyUnsuccessfulDeliveryForBiometricChange(Long requestID, UnSuccessfulDeliveryRequestReason reason) throws BaseException {
        CardRequestTO cardRequestTO = getCardRequestDAO().find(CardRequestTO.class, requestID);
        if (cardRequestTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_013, BizExceptionCode.CMS_011_MSG);
        }
        if (CardRequestState.READY_TO_DELIVER.equals(cardRequestTO.getState())) {
            cardRequestTO.setState(CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC);
            getCardRequestHistoryDAO().create(
                    new CardRequestTO(cardRequestTO.getId()),
                    null,
                    SystemId.CMS,
                    null,
                    CardRequestHistoryAction.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC,
                    getUserProfileTO().getUserName());
        } else {
            throw new ServiceException(BizExceptionCode.CMS_030, BizExceptionCode.CMS_030_MSG);
        }
    }

    /**
     * method revoke is used to handle all the work which should be done at the revocation time
     *
     * @param cardRequestTO is an instance of type {@link CardRequestTO} which carries the required attributes for this
     *                      operation
     * @param reason        is a String value which represents the reason for doing this operation
     * @throws {@link BaseException}
     */
    private void revokeCard(CardRequestTO cardRequestTO,
                            String reason) throws BaseException {
        if (cardRequestTO.getCard() == null) {
            throw new ServiceException(BizExceptionCode.CMS_018, BizExceptionCode.CMS_018_MSG, new Long[]{cardRequestTO.getId()});
        }
        getCMSService().revokeCard(cardRequestTO.getCard().getCrn(), reason);
        List<Long> idList = new ArrayList<Long>();
        idList.add(cardRequestTO.getCard().getId());
        getCardDAO().updateCardsState(idList, CardState.REVOKED);
    }

    /**
     * The method branchUnsuccessfulDeliveryType is used to change the simple CardRequestTypes to the more complicated
     * ones.
     */
    private CardRequestTO branchUnsuccessfulDeliveryType(CardRequestTO cardRequestTO) {

        switch (cardRequestTO.getType()) {
            case FIRST_CARD:
                cardRequestTO.setType(UNSUCCESSFUL_DELIVERY_FOR_FIRST_CARD);
                break;

            case REPLICA:
                cardRequestTO.setType(UNSUCCESSFUL_DELIVERY_FOR_REPLICA);
                break;

            case REPLACE:
                cardRequestTO.setType(UNSUCCESSFUL_DELIVERY_FOR_REPLACE);
                break;

            case EXTEND:
                cardRequestTO.setType(UNSUCCESSFUL_DELIVERY_FOR_EXTEND);
                break;
        }
        return cardRequestTO;
    }

    /**
     * The method wrapCardRequestType is used to wrap unsuccessful delivery cards type to the similar ones
     *
     * @param cardRequestTO is an instance of type {@link CardRequestTO}
     * @return an instance of type {@link CardRequestType}
     */
    private CardRequestType wrapCardRequestType(CardRequestTO cardRequestTO) {
        CardRequestType cardRequestType = cardRequestTO.getType();
        switch (cardRequestType) {
            case UNSUCCESSFUL_DELIVERY_FOR_FIRST_CARD:
                cardRequestType = CardRequestType.FIRST_CARD;
                break;

            case UNSUCCESSFUL_DELIVERY_FOR_REPLICA:
                cardRequestType = CardRequestType.REPLICA;
                break;

            case UNSUCCESSFUL_DELIVERY_FOR_REPLACE:
                cardRequestType = CardRequestType.REPLACE;
                break;

            case UNSUCCESSFUL_DELIVERY_FOR_EXTEND:
                cardRequestType = CardRequestType.EXTEND;
                break;
        }
        return cardRequestType;
    }

    /**
     * The method updateCardRequestHistory is used to update an instance of type {@link CardRequestHistoryTO}
     */
    private CardRequestHistoryTO updateCardRequestHistory(CardRequestTO cardRequestTO,
                                                          String stringRequestId,
                                                          String resultValue) throws BaseException {
        CardRequestHistoryTO cardRequestHistoryTO = new CardRequestHistoryTO();
        cardRequestHistoryTO.setCardRequest(cardRequestTO);
        cardRequestHistoryTO.setDate(new Date());
        cardRequestHistoryTO.setRequestID(stringRequestId);

        cardRequestHistoryTO.setSystemID(SystemId.CMS);
        cardRequestHistoryTO.setResult(resultValue);
        getCardRequestHistoryDAO().create(cardRequestHistoryTO);
        return cardRequestHistoryTO;
    }

    /**
     * @see com.gam.nocr.ems.biz.service.CardManagementService#checkRequestValidation(String, com.gam.nocr.ems.data.enums.CardRequestType)
     */
    @Override
    public void checkRequestValidation(String citizenNationalId, CardRequestType cardRequestType) throws BaseException {

        try {


            List<CardInfoVTO> allCardsList = new ArrayList<CardInfoVTO>();

            String productId = null;
            try {
                ProfileManager pm = ProfileHelper.getProfileManager();
                productId = (String) pm.getProfile(ProfileKeyName.KEY_CMS_ISSUE_CARD_PRODUCT_ID, true, null, null);
            } catch (Exception e) {
                logger.warn(BizExceptionCode.CMS_043, BizExceptionCode.GLB_009_MSG, e);
            }

            if (productId == null || productId.isEmpty()) {
                productId = DEFAULT_PRODUCT_ID;
            }

            switch (cardRequestType) {
                case FIRST_CARD:
                    try {
                        allCardsList = getCMSService().getCitizenCardsByProduct(citizenNationalId, productId);
                    } catch (ServiceException e) {
                        if (BizExceptionCode.CSI_082.equals(e.getExceptionCode())) {
                            verifyFirstIssuance(allCardsList);
                            return;
                        }
                        throw e;
                    } catch (Exception e) {
                        throw new ServiceException(BizExceptionCode.CMS_014, BizExceptionCode.CMS_014_MSG, e, new Object[]{citizenNationalId});
                    }
                    verifyFirstIssuance(allCardsList);
                    break;

                case REPLICA:
                    allCardsList = getCMSService().getCitizenCardsByProduct(citizenNationalId, productId);
                    verifyReplicaIssuance(allCardsList);
                    break;

                case REPLACE:
                    CardInfoVTO currentCardVto = getCMSService().getCurrentCitizenCardByProduct(citizenNationalId, productId);
                    verifyReplaceIssuance(currentCardVto);
                    break;

                case EXTEND:
                    allCardsList = getCMSService().getCitizenCardsByProduct(citizenNationalId, productId);
                    verifyExtendIssuance(allCardsList);
                    break;
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CMS_033, BizExceptionCode.GLB_008_MSG, e);
        }

    }

    /**
     * The method notifyReceivedBatches is used to be sent the report of receiving specified batches by EMS
     *
     * @throws {@link BaseException}
     */
    @Override
    @BizLoggable(logAction = "NOTIFY_RECEIVED", logEntityName = "BATCH", logActor = "System")
    public String notifyReceivedBatches(Integer from, Integer to) throws BaseException {
        try {
            List<BatchTO> batchTOList = findReceivedBatches(from, to);
            List<Long> batchIdList = new ArrayList<Long>();
            for (BatchTO batchTO : batchTOList) {
				try {
					getCMSService().batchReceipt(batchTO.getCmsID());
					batchIdList.add(batchTO.getId());
				} catch (ServiceException e) {
					if (BizExceptionCode.CSI_014.equals(e.getExceptionCode())
							|| BizExceptionCode.CSI_015.equals(e
									.getExceptionCode())
							|| BizExceptionCode.CSI_016.equals(e
									.getExceptionCode())) {
						logger.error(e.getMessage(), e);
						throw e;

					} else if (BizExceptionCode.CSI_017.equals(e
							.getExceptionCode())) {
						batchIdList.add(batchTO.getId());

					} else if (BizExceptionCode.CSI_114.equals(e
							.getExceptionCode())) {
						logger.error(e.getMessage(), e);
						throw e;
					}
				}
            }
            if (batchIdList.size() > 0) {
                getDispatchDAO().updateBatchesState(batchIdList, BatchState.RECEIVED);

				for (Long batchId : batchIdList) {
					CardRequestTO cardRequestTO = getCardRequestDAO()
							.findByContainerId(batchId,
									DepartmentDispatchSendType.BATCH);
					getCardRequestHistoryDAO().create(cardRequestTO, null,
							SystemId.CMS, null,
							CardRequestHistoryAction.BATCH_RECEIVED, null);

					// add outgoingSMS
					if (getDispatchDAO().isValidToSendDeliverySms(batchId) != null) {
						List<Long> cardRequestIds = getCardRequestDAO()
								.getCardRequestsByBatchID(batchId);
						for (Long cardRequestId : cardRequestIds) {
							getCardRequestDAO()
									.addRequestedSmsForReadyToDeliverReq(
											cardRequestId);
						}
					}
				}
            }
            /**
             * For BusinessLog
             */
            return EmsUtil.toJSON("receivedBatchIds:" + batchIdList);
        } catch (BaseException e) {
			sessionContext.setRollbackOnly();
            throw e;
        } catch (Exception e) {
			sessionContext.setRollbackOnly();
            throw new ServiceException(BizExceptionCode.CMS_034, BizExceptionCode.GLB_008_MSG, e);
        }
    }

	/**
     * The method notifyMissedBatches is used to be sent the report of missing for the specified batches
     *
     * @throws {@link BaseException}
     */
    @Override
    @BizLoggable(logAction = "NOTIFY_MISSED", logEntityName = "BATCH", logActor = "System")
    public String notifyMissedBatches(Integer from, Integer to) throws BaseException {
        try {
            List<BatchTO> batchTOList = findMissedBatches(from, to);
            List<Long> batchIdList = new ArrayList<Long>();
            for (BatchTO batchTO : batchTOList) {
            	 batchIdList.add(batchTO.getId());
                try {
                    getCMSService().batchMissed(batchTO.getCmsID(), "");
                    batchIdList.add(batchTO.getId());
                } catch (ServiceException e) {
                    if (BizExceptionCode.CSI_018.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_019.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_021.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_023.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_024.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_025.equals(e.getExceptionCode())) {
                        logger.error(e.getMessage(), e);
                        throw e;

                    } else if (BizExceptionCode.CSI_020.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_022.equals(e.getExceptionCode())) {
                        batchIdList.add(batchTO.getId());

                    } else if (BizExceptionCode.CSI_114.equals(e.getExceptionCode())) {
                        logger.error(e.getMessage(), e);
                        throw e;
                    }
                }
            }
            if (batchIdList.size() > 0) {
                getDispatchDAO().updateBatchesState(batchIdList, BatchState.MISSED);
            }
            return EmsUtil.toJSON("missedBatchIds:" + batchIdList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CMS_035, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * The method notifyMissedBoxes is used to be sent the report of missing for the specified boxes
     *
     * @throws {@link BaseException}
     */
    @Override
    @BizLoggable(logAction = "NOTIFY_MISSED", logEntityName = "BOX", logActor = "System")
    public String notifyMissedBoxes(Integer from, Integer to) throws BaseException {
        try {
            List<BoxTO> boxTOList = findMissedBoxes(from, to);
            List<Long> boxIdList = new ArrayList<Long>();
            for (BoxTO boxTO : boxTOList) {
            	 boxIdList.add(boxTO.getId());
                try {
                    getCMSService().boxMissed(boxTO.getCmsID(), "");
                    boxIdList.add(boxTO.getId());
                } catch (ServiceException e) {
                    if (BizExceptionCode.CSI_026.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_027.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_028.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_030.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_031.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_032.equals(e.getExceptionCode())) {
                        logger.error(e.getMessage(), e);
                        throw e;

                    } else if (BizExceptionCode.CSI_029.equals(e.getExceptionCode())) {
                        boxIdList.add(boxTO.getId());

                    } else if (BizExceptionCode.CSI_114.equals(e.getExceptionCode())) {
                        logger.error(e.getMessage(), e);
                        throw e;
                    }
                }
            }
            if (boxIdList.size() > 0) {
                getDispatchDAO().updateBoxesState(boxIdList, BoxState.MISSED);
            }
            return EmsUtil.toJSON("missedBoxIds:" + boxIdList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CMS_036, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * The method notifyMissedCards is used to be sent the report of missing for the specified cards
     *
     * @throws {@link BaseException}
     */
    @Override
    @BizLoggable(logAction = "NOTIFY_MISSED", logEntityName = "CARD", logActor = "System")
    public String notifyMissedCards(Integer from, Integer to) throws BaseException {
        try {

        	String description=null;
        	// true: need to manager confirm
        	if (Boolean.valueOf(EmsUtil.getProfileValue(
					ProfileKeyName.KEY_LOST_CARD_CONFIRM,
					DEFAULT_CARD_LOST_CONFIRM))) {

        		description="lostCardConfirm is true";
			}
        	List<CardTO> cardTOList = findMissedCards(from, to);
            List<Long> cardIdList = new ArrayList<Long>();
            for (CardTO cardTO : cardTOList) {
//            	cardIdList.add(cardTO.getId());            	
                try {
                    getCMSService().cardMissed(cardTO.getCrn(), "");
                    cardIdList.add(cardTO.getId());
                } catch (ServiceException e) {
                    if (BizExceptionCode.CSI_033.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_034.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_035.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_037.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_038.equals(e.getExceptionCode())
                            || BizExceptionCode.CSI_039.equals(e.getExceptionCode())) {
                        logger.debug(e.getMessage(), e);
                        throw e;

                    } else if (BizExceptionCode.CSI_036.equals(e.getExceptionCode())) {
                        cardIdList.add(cardTO.getId());

                    } else if (BizExceptionCode.CSI_114.equals(e.getExceptionCode())) {
                        logger.error(e.getMessage(), e);
                        throw e;
                    }
                }
            }
            if (cardIdList.size() > 0) {
                getCardDAO().updateCardsState(cardIdList, CardState.MISSED);

                for (Long cardId : cardIdList) {
                    CardRequestTO cardRequestTO = getCardRequestDAO().findByContainerId(cardId, DepartmentDispatchSendType.CARD);
                    getCardRequestHistoryDAO().create(cardRequestTO, description, SystemId.CMS, null,
                            CardRequestHistoryAction.NOTIFY_CARD_MISSED, null);
                }
            }
            return EmsUtil.toJSON("missedCardIds:" + cardIdList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CMS_037, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * The method notifyCardsHandedOut is used to be sent the report of handing out a specified card by EMS
     *
     * @param from is an instance of type {@link Integer}, which represents the first record to be processed
     * @param to   is an instance of type {@link Integer}, which represents the last record to be processed
     * @return an instance of type {@link Boolean}, which specifies by the value of true or false, whether the process
     * of calling the method will be replicated or not
     * @throws {@link BaseException}
     */
    @Override
    public Boolean notifyCardsHandedOut(Long cardRequestId) throws BaseException {
        try {
           // List<CardRequestTO> cardRequestTOList = getCardRequestDAO().findRequestsByBoundaryLimitAndState(from, to,CardRequestState.PENDING_TO_DELIVER_BY_CMS);

            CardRequestTO cardRequestTO = getCardRequestDAO().find(CardRequestTO.class,cardRequestId);
            //handoutJobLogger.info("The list of card requests which are ready to be delivered contains {} records", cardRequestTOList.size());
            //if (EmsUtil.checkListSize(cardRequestTO)) {
            if (cardRequestTO != null) {
                List<Long> cardRequestIdListForDeliver = new ArrayList<Long>();

                List<Long> cardIdListForDeliver = new ArrayList<Long>();
                List<String> cardCRNListForRevoke = new ArrayList<String>();
                List<String> cardIdListForExpire = new ArrayList<String>();

           //     for (CardRequestTO cardRequestTO : cardRequestTOList) {

                    /**
                     * Wrapping UnsuccessfulDelivery types on to the similar ones. By using this method all the unsuccessful
                     * delivery type for the next cards are wrap to there type.
                     * For instance :
                     * UNSUCCESSFUL_DELIVERY_FOR_FIRST_CARD is converted to FIRST_CARD and this scenario is repeated for the other types.
                     */
                    CardRequestType cardRequestType = wrapCardRequestType(cardRequestTO);

                    handoutJobLogger.info("Delivering card request {} of type {} with crn {}", new Object[]{cardRequestTO.getId(), cardRequestTO.getType(), cardRequestTO.getCard().getCrn()});
                    switch (cardRequestType) {
                        case FIRST_CARD:
                            try {
                                deliverFirstCard(cardRequestTO, cardRequestIdListForDeliver, cardIdListForDeliver);
                            } catch (ServiceException e) {
                                handoutJobLogger.error("Unable to deliver card request " + cardRequestTO.getId() + " of type " + cardRequestTO.getType() + " with crn " + cardRequestTO.getCard().getCrn(), e);
                                if (BizExceptionCode.CSI_043.equals(e.getExceptionCode())) {
                                    addToList(cardRequestIdListForDeliver, cardRequestTO.getId());
                                    addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());

                                } else if (BizExceptionCode.CSI_112.equals(e.getExceptionCode())) {
                                    List<Long> requestIdList = new ArrayList<Long>();
                                    requestIdList.add(cardRequestTO.getId());
                                    getCardRequestDAO().updateCardRequestsState(requestIdList, CardRequestState.CMS_ERROR);
                                    updateCardRequestHistory(cardRequestTO, "", "FAILURE_VALUE" + "-" + e.getMessage());

                                } else {
                                    throw e;
                                }
                            }
                            break;

                        case REPLICA:
                            try {
                                /**
                                 * The method deliverReplicaCard is used to handle the businesses which are needed in delivering the replica card,
                                 * and also handles in advance revoked cards(ExceptionCode : CSI_071)
                                 */
                                deliverReplicaCard(cardRequestTO, cardCRNListForRevoke, cardRequestIdListForDeliver, cardIdListForDeliver);
                            } catch (ServiceException e) {
                                handoutJobLogger.error("Unable to deliver card request " + cardRequestTO.getId() + " of type " + cardRequestTO.getType() + " with crn " + cardRequestTO.getCard().getCrn(), e);
                                if (BizExceptionCode.CSI_112.equals(e.getExceptionCode())) {
                                    List<Long> requestIdList = new ArrayList<Long>();
                                    requestIdList.add(cardRequestTO.getId());
                                    getCardRequestDAO().updateCardRequestsState(requestIdList, CardRequestState.CMS_ERROR);
                                    updateCardRequestHistory(cardRequestTO, "", "FAILURE_VALUE" + "-" + e.getMessage());

                                } else if (BizExceptionCode.CSI_043.equals(e.getExceptionCode())) {
                                    addToList(cardRequestIdListForDeliver, cardRequestTO.getId());
                                    addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());

                                } else {
                                    throw e;
                                }
                            }
                            break;

                        case REPLACE:
                            try {
                                /**
                                 * The method deliverReplaceCard is used to handle the businesses which are needed in delivering the replace card,
                                 * and also handles in advance revoked cards(ExceptionCode : CSI_071)
                                 */
                                deliverReplaceCard(cardRequestTO, cardCRNListForRevoke, cardRequestIdListForDeliver, cardIdListForDeliver);
                            } catch (ServiceException e) {
                                handoutJobLogger.error("Unable to deliver card request " + cardRequestTO.getId() + " of type " + cardRequestTO.getType() + " with crn " + cardRequestTO.getCard().getCrn(), e);
//							getCurrentCitizenCardByProduct exceptions
                                if (BizExceptionCode.CSI_112.equals(e.getExceptionCode())) {
                                    List<Long> requestIdList = new ArrayList<Long>();
                                    requestIdList.add(cardRequestTO.getId());
                                    getCardRequestDAO().updateCardRequestsState(requestIdList, CardRequestState.CMS_ERROR);
                                    updateCardRequestHistory(cardRequestTO, "", "FAILURE_VALUE" + "-" + e.getMessage());

                                } else if (BizExceptionCode.CSI_043.equals(e.getExceptionCode())) {
                                    cardRequestIdListForDeliver.add(cardRequestTO.getId());
                                    addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());

                                } else {
                                    throw e;
                                }
                            }
                            break;

                        case EXTEND:
                            try {
                                /**
                                 * The method deliverExtendCard is used to handle the businesses which are needed in delivering the extend card,
                                 * and also handles in advance expired cards(ExceptionCode : CSI_050)
                                 */
                                deliverExtendCard(cardRequestTO, cardIdListForExpire, cardRequestIdListForDeliver, cardIdListForDeliver);
                            } catch (ServiceException e) {
                                handoutJobLogger.error("Unable to deliver card request " + cardRequestTO.getId() + " of type " + cardRequestTO.getType() + " with crn " + cardRequestTO.getCard().getCrn(), e);
//							getCurrentCitizenCardByProduct exceptions
                                if (BizExceptionCode.CSI_112.equals(e.getExceptionCode())) {
                                    List<Long> requestIdList = new ArrayList<Long>();
                                    requestIdList.add(cardRequestTO.getId());
                                    getCardRequestDAO().updateCardRequestsState(requestIdList, CardRequestState.CMS_ERROR);
                                    updateCardRequestHistory(cardRequestTO, "", "FAILURE_VALUE" + "-" + e.getMessage());

                                } else if (BizExceptionCode.CSI_043.equals(e.getExceptionCode())) {
                                    addToList(cardRequestIdListForDeliver, cardRequestTO.getId());
                                    addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());

                                } else {
                                    throw e;
                                }
                                break;
                            }

                        case UNSUCCESSFUL_DELIVERY:
                            try {
                                /**
                                 * The method deliverUnsuccessfulDeliveryCard is used to handle the businesses which are needed in delivering the unsuccessfulDelivered card,
                                 */
                                deliverUnsuccessfulDeliveryCard(cardRequestTO, cardRequestIdListForDeliver, cardIdListForDeliver);
                            } catch (ServiceException e) {
                                handoutJobLogger.error("Unable to deliver card request " + cardRequestTO.getId() + " of type " + cardRequestTO.getType() + " with crn " + cardRequestTO.getCard().getCrn(), e);
                                if (BizExceptionCode.CSI_112.equals(e.getExceptionCode())) {
                                    List<Long> requestIdList = new ArrayList<Long>();
                                    requestIdList.add(cardRequestTO.getId());
                                    getCardRequestDAO().updateCardRequestsState(requestIdList, CardRequestState.CMS_ERROR);
                                    updateCardRequestHistory(cardRequestTO, "", "FAILURE_VALUE" + "-" + e.getMessage());

                                } else if (BizExceptionCode.CSI_043.equals(e.getExceptionCode())) {
                                    addToList(cardRequestIdListForDeliver, cardRequestTO.getId());
                                    addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());

                                } else {
                                    throw e;
                                }
                            }
                            break;

                    }

                    // IMS setCitizenCardDelivered Service =================================================
                    try {
                    	CardTO card = cardRequestTO.getCard();
                    	Long batchId = card.getBatch().getId();
                    	EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class, cardRequestTO.getDeliveredOfficeId());
                    	EmsCardDeliverInfo cardDeliverInfo = new EmsCardDeliverInfo();
                        cardDeliverInfo.setCardbatchId(batchId);
                        cardDeliverInfo.setCardDeliveredDate(card.getDeliverDate());
                        cardDeliverInfo.setCardlostDate(card.getLostDate());
                        cardDeliverInfo.setCardreceivedDate(card.getReceiveDate());
                        cardDeliverInfo.setCardshipmentDate(card.getShipmentDate());
                        cardDeliverInfo.setCardState(card.getState());
                        cardDeliverInfo.setEstelamId(0);
                        cardDeliverInfo.setOfficeCode(Integer.parseInt(enrollmentOfficeTO.getCode()));
                        cardDeliverInfo.setPersonNin(Long.parseLong(cardRequestTO.getCitizen().getNationalID()));
                        cardDeliverInfo.setCrn(card.getCrn());
                        cardDeliverInfo.setCardIssuanceDate(card.getIssuanceDate());
                        cardDeliverInfo.setCardRequestId(cardRequestId);
                        boolean imsDeliverFlag = getIMSService().setCitizenCardDelivered(cardDeliverInfo);

                        handoutJobLogger.info("The return value of the IMS webservice of setCitizenCardDelivered is: " + imsDeliverFlag);
                        if (!imsDeliverFlag) {
                            cardRequestIdListForDeliver.remove(cardRequestTO.getId());
                            cardIdListForDeliver.remove(cardRequestTO.getCard().getId());
                            handoutJobLogger.warn("setCitizenCardDelivered of IMS returned false for request {} (CRN={}). So this request will be included in next round of CardHandoutNotification job", cardRequestTO.getId(), cardRequestTO.getCard().getCrn());
                        }
                    } catch (Exception e) {
                        handoutJobLogger.error("An exception happened while calling setCitizenCardDelivered of IMS.", e);
                        cardRequestIdListForDeliver.remove(cardRequestTO.getId());
                        cardIdListForDeliver.remove(cardRequestTO.getCard().getId());
                        handoutJobLogger.info("State of card request {} (CRN={}) will not be changed to DELIVERED. So this request will be included in next round of CardHandoutNotification job", cardRequestTO.getId(), cardRequestTO.getCard().getCrn());
                        if (e instanceof BaseException) {
                            logger.error(((BaseException) e).getExceptionCode(), e.getMessage(), e);
                        } else {
                            logger.error(BizExceptionCode.CMS_051, BizExceptionCode.GLB_008_MSG, e);
                        }
                    }
                    // =====================================================================================

            //    }//End for

                handoutJobLogger.info("Updating state of card requests to REVOKED for these CRNs :[{}] ", cardCRNListForRevoke);

                if (EmsUtil.checkListSize(cardCRNListForRevoke)) {
                    getCardDAO().updateCardsStateByCRN(cardCRNListForRevoke, CardState.REVOKED);
                }

                if (EmsUtil.checkListSize(cardRequestIdListForDeliver)) {
                    getCardRequestDAO().updateCardRequestsState(cardRequestIdListForDeliver, CardRequestState.DELIVERED);
                    handoutJobLogger.info("Updating state of card requests to DELIVERED for these card request IDs :[{}] ", cardRequestIdListForDeliver);

                    handoutJobLogger.info("Updating history of card requests about delivering notification for these card request IDs :[{}] ", cardRequestIdListForDeliver);
                //    for (Long cardRequestId : cardRequestIdListForDeliver) {
                        getCardRequestHistoryDAO().create(new CardRequestTO(cardRequestId), null, SystemId.CMS, null,
                                CardRequestHistoryAction.NOTIFY_CARD_DELIVER, null);
                 //   }
                }

                if (EmsUtil.checkListSize(cardIdListForDeliver)) {
                    getCardDAO().updateCardsState(cardIdListForDeliver, CardState.DELIVERED);
                    handoutJobLogger.info("Updating state of cards to DELIVERED for these card IDs :[{}] ", cardIdListForDeliver);
                }

                /**
                 * BusinessLog
                 */
                Map<Object, Object> businessLogMap = new HashMap<Object, Object>();
                businessLogMap.put("deliveredCardRequestIds", cardRequestIdListForDeliver);
                businessLogMap.put("deliveredCardIds", cardIdListForDeliver);
                createBusinessLog(BusinessLogAction.NOTIFY_HANDOUT, BusinessLogEntity.CARD, "system",
                        EmsUtil.convertStringToJSONFormat("cardHandedOutInfo", businessLogMap), true);
            }
            //return loopFlag;
            return null;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CMS_038, BizExceptionCode.GLB_008_MSG, e);
        }
    }



    @Override
    @Asynchronous
    public Future<String> notifyCardsHandedOutAsync(Long cardRequestId) throws BaseException {
        try {
           // List<CardRequestTO> cardRequestTOList = getCardRequestDAO().findRequestsByBoundaryLimitAndState(from, to,CardRequestState.PENDING_TO_DELIVER_BY_CMS);

            CardRequestTO cardRequestTO = getCardRequestDAO().find(CardRequestTO.class,cardRequestId);
            //handoutJobLogger.info("The list of card requests which are ready to be delivered contains {} records", cardRequestTOList.size());
            //if (EmsUtil.checkListSize(cardRequestTO)) {
            if (cardRequestTO != null) {
                List<Long> cardRequestIdListForDeliver = new ArrayList<Long>();
                 List<Long> cardIdListForDeliver = new ArrayList<Long>();
                 List<String> cardCRNListForRevoke = new ArrayList<String>();
                 List<String> cardIdListForExpire = new ArrayList<String>();

            //     for (CardRequestTO cardRequestTO : cardRequestTOList) {

                     /**
                      * Wrapping UnsuccessfulDelivery types on to the similar ones. By using this method all the unsuccessful
                      * delivery type for the next cards are wrap to there type.
                      * For instance :
                      * UNSUCCESSFUL_DELIVERY_FOR_FIRST_CARD is converted to FIRST_CARD and this scenario is repeated for the other types.
                      */
                     CardRequestType cardRequestType = wrapCardRequestType(cardRequestTO);

                     handoutJobLogger.info("Delivering card request {} of type {} with crn {}", new Object[]{cardRequestTO.getId(), cardRequestTO.getType(), cardRequestTO.getCard().getCrn()});
                     switch (cardRequestType) {
                         case FIRST_CARD:
                             try {
                                 deliverFirstCard(cardRequestTO, cardRequestIdListForDeliver, cardIdListForDeliver);
                             } catch (ServiceException e) {
                                 handoutJobLogger.error("Unable to deliver card request " + cardRequestTO.getId() + " of type " + cardRequestTO.getType() + " with crn " + cardRequestTO.getCard().getCrn(), e);
                                 if (BizExceptionCode.CSI_043.equals(e.getExceptionCode())) {
                                     addToList(cardRequestIdListForDeliver, cardRequestTO.getId());
                                     addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());

                                 } else if (BizExceptionCode.CSI_112.equals(e.getExceptionCode())) {
                                     List<Long> requestIdList = new ArrayList<Long>();
                                     requestIdList.add(cardRequestTO.getId());
                                     getCardRequestDAO().updateCardRequestsState(requestIdList, CardRequestState.CMS_ERROR);
                                     updateCardRequestHistory(cardRequestTO, "", "FAILURE_VALUE" + "-" + e.getMessage());

                                 } else {
                                     throw e;
                                 }
                             }
                             break;

                         case REPLICA:
                             try {
                                 /**
                                  * The method deliverReplicaCard is used to handle the businesses which are needed in delivering the replica card,
                                  * and also handles in advance revoked cards(ExceptionCode : CSI_071)
                                  */
                                 deliverReplicaCard(cardRequestTO, cardCRNListForRevoke, cardRequestIdListForDeliver, cardIdListForDeliver);
                             } catch (ServiceException e) {
                                 handoutJobLogger.error("Unable to deliver card request " + cardRequestTO.getId() + " of type " + cardRequestTO.getType() + " with crn " + cardRequestTO.getCard().getCrn(), e);
                                 if (BizExceptionCode.CSI_112.equals(e.getExceptionCode())) {
                                     List<Long> requestIdList = new ArrayList<Long>();
                                     requestIdList.add(cardRequestTO.getId());
                                     getCardRequestDAO().updateCardRequestsState(requestIdList, CardRequestState.CMS_ERROR);
                                     updateCardRequestHistory(cardRequestTO, "", "FAILURE_VALUE" + "-" + e.getMessage());

                                 } else if (BizExceptionCode.CSI_043.equals(e.getExceptionCode())) {
                                     addToList(cardRequestIdListForDeliver, cardRequestTO.getId());
                                     addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());

                                 } else {
                                     throw e;
                                 }
                             }
                             break;

                         case REPLACE:
                             try {
                                 /**
                                  * The method deliverReplaceCard is used to handle the businesses which are needed in delivering the replace card,
                                  * and also handles in advance revoked cards(ExceptionCode : CSI_071)
                                  */
                                 deliverReplaceCard(cardRequestTO, cardCRNListForRevoke, cardRequestIdListForDeliver, cardIdListForDeliver);
                             } catch (ServiceException e) {
                                 handoutJobLogger.error("Unable to deliver card request " + cardRequestTO.getId() + " of type " + cardRequestTO.getType() + " with crn " + cardRequestTO.getCard().getCrn(), e);
// 							getCurrentCitizenCardByProduct exceptions
                                 if (BizExceptionCode.CSI_112.equals(e.getExceptionCode())) {
                                     List<Long> requestIdList = new ArrayList<Long>();
                                     requestIdList.add(cardRequestTO.getId());
                                     getCardRequestDAO().updateCardRequestsState(requestIdList, CardRequestState.CMS_ERROR);
                                     updateCardRequestHistory(cardRequestTO, "", "FAILURE_VALUE" + "-" + e.getMessage());

                                 } else if (BizExceptionCode.CSI_043.equals(e.getExceptionCode())) {
                                     cardRequestIdListForDeliver.add(cardRequestTO.getId());
                                     addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());

                                 } else {
                                     throw e;
                                 }
                             }
                             break;

                         case EXTEND:
                             try {
                                 /**
                                  * The method deliverExtendCard is used to handle the businesses which are needed in delivering the extend card,
                                  * and also handles in advance expired cards(ExceptionCode : CSI_050)
                                  */
                                 deliverExtendCard(cardRequestTO, cardIdListForExpire, cardRequestIdListForDeliver, cardIdListForDeliver);
                             } catch (ServiceException e) {
                                 handoutJobLogger.error("Unable to deliver card request " + cardRequestTO.getId() + " of type " + cardRequestTO.getType() + " with crn " + cardRequestTO.getCard().getCrn(), e);
// 							getCurrentCitizenCardByProduct exceptions
                                 if (BizExceptionCode.CSI_112.equals(e.getExceptionCode())) {
                                     List<Long> requestIdList = new ArrayList<Long>();
                                     requestIdList.add(cardRequestTO.getId());
                                     getCardRequestDAO().updateCardRequestsState(requestIdList, CardRequestState.CMS_ERROR);
                                     updateCardRequestHistory(cardRequestTO, "", "FAILURE_VALUE" + "-" + e.getMessage());

                                 } else if (BizExceptionCode.CSI_043.equals(e.getExceptionCode())) {
                                     addToList(cardRequestIdListForDeliver, cardRequestTO.getId());
                                     addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());

                                 } else {
                                     throw e;
                                 }
                                 break;
                             }

                         case UNSUCCESSFUL_DELIVERY:
                             try {
                                 /**
                                  * The method deliverUnsuccessfulDeliveryCard is used to handle the businesses which are needed in delivering the unsuccessfulDelivered card,
                                  */
                                 deliverUnsuccessfulDeliveryCard(cardRequestTO, cardRequestIdListForDeliver, cardIdListForDeliver);
                             } catch (ServiceException e) {
                                 handoutJobLogger.error("Unable to deliver card request " + cardRequestTO.getId() + " of type " + cardRequestTO.getType() + " with crn " + cardRequestTO.getCard().getCrn(), e);
                                 if (BizExceptionCode.CSI_112.equals(e.getExceptionCode())) {
                                     List<Long> requestIdList = new ArrayList<Long>();
                                     requestIdList.add(cardRequestTO.getId());
                                     getCardRequestDAO().updateCardRequestsState(requestIdList, CardRequestState.CMS_ERROR);
                                     updateCardRequestHistory(cardRequestTO, "", "FAILURE_VALUE" + "-" + e.getMessage());

                                 } else if (BizExceptionCode.CSI_043.equals(e.getExceptionCode())) {
                                     addToList(cardRequestIdListForDeliver, cardRequestTO.getId());
                                     addToList(cardIdListForDeliver, cardRequestTO.getCard().getId());

                                 } else {
                                     throw e;
                                 }
                             }
                             break;

                     }

                     // IMS setCitizenCardDelivered Service =================================================
                     try {
                     	CardTO card = cardRequestTO.getCard();
                     	Long batchId = card.getBatch().getId();
                     	EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class, cardRequestTO.getDeliveredOfficeId());
                     	EmsCardDeliverInfo cardDeliverInfo = new EmsCardDeliverInfo();
                         cardDeliverInfo.setCardbatchId(batchId);
                         cardDeliverInfo.setCardDeliveredDate(card.getDeliverDate());
                         cardDeliverInfo.setCardlostDate(card.getLostDate());
                         cardDeliverInfo.setCardreceivedDate(card.getReceiveDate());
                         cardDeliverInfo.setCardshipmentDate(card.getShipmentDate());
                         cardDeliverInfo.setCardState(card.getState());
                         cardDeliverInfo.setEstelamId(0);
                         cardDeliverInfo.setOfficeCode(Integer.parseInt(enrollmentOfficeTO.getCode()));
                         cardDeliverInfo.setPersonNin(Long.parseLong(cardRequestTO.getCitizen().getNationalID()));
                         cardDeliverInfo.setCrn(card.getCrn());
                         cardDeliverInfo.setCardIssuanceDate(card.getIssuanceDate());
                         cardDeliverInfo.setCardRequestId(cardRequestId);
                         boolean imsDeliverFlag = getIMSService().setCitizenCardDelivered(cardDeliverInfo);

                         handoutJobLogger.info("The return value of the IMS webservice of setCitizenCardDelivered is: " + imsDeliverFlag);
                         if (!imsDeliverFlag) {
                             cardRequestIdListForDeliver.remove(cardRequestTO.getId());
                             cardIdListForDeliver.remove(cardRequestTO.getCard().getId());
                             handoutJobLogger.warn("setCitizenCardDelivered of IMS returned false for request {} (CRN={}). So this request will be included in next round of CardHandoutNotification job", cardRequestTO.getId(), cardRequestTO.getCard().getCrn());
                         }
                     } catch (Exception e) {
                         handoutJobLogger.error("An exception happened while calling setCitizenCardDelivered of IMS.", e);
                         cardRequestIdListForDeliver.remove(cardRequestTO.getId());
                         cardIdListForDeliver.remove(cardRequestTO.getCard().getId());
                         handoutJobLogger.info("State of card request {} (CRN={}) will not be changed to DELIVERED. So this request will be included in next round of CardHandoutNotification job", cardRequestTO.getId(), cardRequestTO.getCard().getCrn());
                         if (e instanceof BaseException) {
                             logger.error(((BaseException) e).getExceptionCode(), e.getMessage(), e);
                         } else {
                             logger.error(BizExceptionCode.CMS_051, BizExceptionCode.GLB_008_MSG, e);
                         }
                     }
                     // =====================================================================================

             //    }//End for

                 handoutJobLogger.info("Updating state of card requests to REVOKED for these CRNs :[{}] ", cardCRNListForRevoke);

                 if (EmsUtil.checkListSize(cardCRNListForRevoke)) {
                     getCardDAO().updateCardsStateByCRN(cardCRNListForRevoke, CardState.REVOKED);
                 }

                 if (EmsUtil.checkListSize(cardRequestIdListForDeliver)) {
                     getCardRequestDAO().updateCardRequestsState(cardRequestIdListForDeliver, CardRequestState.DELIVERED);
                     handoutJobLogger.info("Updating state of card requests to DELIVERED for these card request IDs :[{}] ", cardRequestIdListForDeliver);

                     handoutJobLogger.info("Updating history of card requests about delivering notification for these card request IDs :[{}] ", cardRequestIdListForDeliver);
                 //    for (Long cardRequestId : cardRequestIdListForDeliver) {
                         getCardRequestHistoryDAO().create(new CardRequestTO(cardRequestId), null, SystemId.CMS, null,
                                 CardRequestHistoryAction.NOTIFY_CARD_DELIVER, null);
                  //   }
                 }

                 if (EmsUtil.checkListSize(cardIdListForDeliver)) {
                     getCardDAO().updateCardsState(cardIdListForDeliver, CardState.DELIVERED);
                     handoutJobLogger.info("Updating state of cards to DELIVERED for these card IDs :[{}] ", cardIdListForDeliver);
                 }

                 /**
                  * BusinessLog
                  */
                 Map<Object, Object> businessLogMap = new HashMap<Object, Object>();
                 businessLogMap.put("deliveredCardRequestIds", cardRequestIdListForDeliver);
                 businessLogMap.put("deliveredCardIds", cardIdListForDeliver);
                 createBusinessLog(BusinessLogAction.NOTIFY_HANDOUT, BusinessLogEntity.CARD, "system",
                         EmsUtil.convertStringToJSONFormat("cardHandedOutInfo", businessLogMap), true);
             }
             //return loopFlag;
             return new AsyncResult<String>("");
         } catch (BaseException e) {
             throw e;
         } catch (Exception e) {
             throw new ServiceException(BizExceptionCode.CMS_038, BizExceptionCode.GLB_008_MSG, e);
         }
    }


    /**
     * The method deliver is used to set some updates which are depend on the delivery service
     *
     * @param requestId     is an object of type {@link Long} which represents a specified id of the object of type {@link
     *                      com.gam.nocr.ems.data.domain.CardRequestTO}
     * @param receipt       is a String value which represents the plane text that is used to create a receipt
     * @param signedReceipt is a String value which represents a signed message
     * @throws {@link BaseException}
     */
    @Override
    @Permissions(value = "ems_deliverRequest")
    @BizLoggable(logAction = "DELIVER", logEntityName = "REQUEST")
    public String deliver(Long requestId,
                          String receipt,
                          byte[] signedReceipt) throws BaseException {
        try {
            if (!getCardRequestDAO().existsCardRequest(requestId)) {
                throw new ServiceException(BizExceptionCode.CMS_015, BizExceptionCode.CMS_015_MSG, new Long[]{requestId});
            }
            if (receipt == null || receipt.trim().equals("")) {
                throw new ServiceException(BizExceptionCode.CMS_016, BizExceptionCode.CMS_016_MSG);
            }
            if (signedReceipt == null) {
                throw new ServiceException(BizExceptionCode.CMS_017, BizExceptionCode.CMS_017_MSG);
            }
            CardRequestTO tempCardRequestTO = new CardRequestTO();
            tempCardRequestTO.setId(requestId);


            //****************** Anbari: Separate insertion of CardRequest Blobs
            tempCardRequestTO.setReceiptText(receipt);
            tempCardRequestTO.setSignedReceipt(signedReceipt);
//            

           // CardRequestBlobsTO findcardRequestBlobsTO = getCardRequestBlobs().findByCardRequestId(requestId);
//            CardRequestBlobsTO findcardRequestBlobsTO = getCardRequestBlobs().find(CardRequestBlobsTO.class, requestId);
//            if(findcardRequestBlobsTO != null)
//            {
//            	findcardRequestBlobsTO.setReceiptText(receipt);
//            	findcardRequestBlobsTO.setSignedReceipt(signedReceipt);
//            	getCardRequestBlobs().update(findcardRequestBlobsTO);
//            }
//            else
//            {
//            	CardRequestBlobsTO cardRequestBlobsTO = new CardRequestBlobsTO();
//            	CardRequestTO cardRequest = getCardRequestDAO().find(CardRequestTO.class,requestId);
//            	cardRequestBlobsTO.setCardRequest(cardRequest);
//            	cardRequestBlobsTO.setReceiptText(receipt);
//            	cardRequestBlobsTO.setSignedReceipt(signedReceipt);
//            	getCardRequestBlobs().create(cardRequestBlobsTO);
//            	
//            }

            //******************



            updateRequestState(tempCardRequestTO, CardRequestState.READY_TO_DELIVER, CardRequestState.PENDING_TO_DELIVER_BY_CMS);
            getCardDAO().updateDeliverDate(requestId,new Date());

            getCardRequestHistoryDAO().create(new CardRequestTO(tempCardRequestTO.getId()), null, SystemId.CCOS,
                    null, CardRequestHistoryAction.DELIVERED_TO_CITIZEN, getUserProfileTO().getUserName());

            return EmsUtil.toJSON("ccosDeliveredCardWithRequestId:" + requestId);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CMS_039, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_deliverRequest")
    public String retrieveDeliverMessage(Long cardRequestId) throws BaseException {
        String deliverMessage = EmsUtil.getProfileValue(ProfileKeyName.KEY_DELIVER_MESSAGE, DEFAULT_DELIVER_MESSAGE);

        CitizenTO citizenTO = getCitizenDAO().findRequestId(cardRequestId);

        deliverMessage = deliverMessage.replace("{firstName}", citizenTO.getFirstNamePersian());
        deliverMessage = deliverMessage.replace("{lastName}", citizenTO.getSurnamePersian());
        deliverMessage = deliverMessage.replace("{NID}", citizenTO.getNationalID());
        deliverMessage = deliverMessage.replace("{currentDate}", DateUtil.convert(new Date(), DateUtil.JALALI));

        return deliverMessage;
    }

    /**
     * The method notifyUnsuccessfulDelivery is used to handle all the businesses which are needed for the unsuccessful
     * delivery requests
     *
     * @param requestID is an object of type {@link Long} which represents a specified id of the object of type {@link
     *                  com.gam.nocr.ems.data.domain.CardRequestTO}
     * @param reason    is an enumeration instance of type {@link UnSuccessfulDeliveryRequestReason} which represents the
     *                  reason of activities that should be done
     * @throws {@link BaseException}
     */
    @Override
    //@Permissions(value = "ems_deliverRequest")
    @Permissions(value = "ems_unsuccessfulDeliverRequest")
    @BizLoggable(logAction = "UNSUCCESSFUL_DELIVER", logEntityName = "REQUEST")
    public String notifyUnsuccessfulDelivery(Long requestID,
                                             UnSuccessfulDeliveryRequestReason reason) throws BaseException {
        Map<Object, Object> jsonMap = new HashMap<Object, Object>();
        jsonMap.put("requestID", requestID.toString());
        jsonMap.put("reason", reason);
        try {
            switch (reason) {
//                case IDENTITY_CHANGE:
//                    notifyUnsuccessfulDeliveryForIdentityChange(requestID, reason);
//                    break;
            	case BIOMETRIC_FINGER_ERROR:
            		notifyUnsuccessfulDeliveryForCutFingersChanged(requestID, reason);
            		break;

                case CARD_DAMAGE:
                    notifyUnsuccessfulDeliveryForDamagedCard(requestID, reason);
                    break;

                case BIOMETRIC_CHANGE:
                    notifyUnsuccessfulDeliveryForBiometricChange(requestID, reason);
                    break;

                case FACE_IMAGE_UNMATCHED:
                    notifyUnsuccessfulDeliveryForFaceImageUnmatched(requestID, reason);
                    break;

                case BIOMETRIC_FACE_ERROR:
                    notifyUnsuccessfulDeliveryForbioAndFaceError(requestID, reason);
                    break;

                case BIOMETRIC_14_TRY_FINGER_ERROR:
                    notifyUnsuccessfulDeliveryForBioFourteenFingerTry(requestID, reason);
                    break;

            }
            return EmsUtil.convertStringToJSONFormat("ccosUnsuccessfulDeliveryInfo:", jsonMap);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CMS_040, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * The method processOnDamagedCards is used to handle the operations bellow : 1. find a list of type {@link
     * CardRequestTO} which are in the state CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE 2. Revoke the card
     * which assigned to the appropriate request in the loop 3. Updating the request
     *
     * @throws {@link BaseException}
     */
    @BizLoggable(logAction = "PROCESS_UNSUCCESSFUL_DELIVERY", logEntityName = "REQUEST", logActor = "System")
    public String processOnDamagedCards(Integer from) throws BaseException {
        try {
            List<CardRequestTO> RequestTOListForDamagedCard = getCardRequestDAO()
                    .findByCardRequestState(CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_DAMAGE, from, 1);
            Map<Object, Object> businessLogMap = new HashMap<Object, Object>();
            List<String> cardRequestIdsForLog = new ArrayList<String>();
            List<String> cardIdsForLog = new ArrayList<String>();
            for (CardRequestTO cardRequestTO : RequestTOListForDamagedCard) {
                cardRequestIdsForLog.add(cardRequestTO.getId().toString());
                cardIdsForLog.add(cardRequestTO.getCard().getId().toString());
                try {
                    revokeCard(cardRequestTO, "");
                    branchUnsuccessfulDeliveryType(cardRequestTO);
                    cardRequestTO.setState(CardRequestState.APPROVED_BY_AFIS);
                } catch (BaseException e) {
                    if (BizExceptionCode.CSI_071.equals(e.getExceptionCode())) {
                        branchUnsuccessfulDeliveryType(cardRequestTO);
                        cardRequestTO.setState(CardRequestState.APPROVED_BY_AFIS);

                    } else {
                        throw e;
                    }
                }
            }
            businessLogMap.put("cardRequestIds", cardRequestIdsForLog.toString());
            businessLogMap.put("cardIds", cardIdsForLog.toString());
            return EmsUtil.convertStringToJSONFormat("requestsWithBiometricProblem", businessLogMap);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CMS_055, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * The method processOnRequestsWithBiometricProblem is used to handle the operations bellow :
     * <p/>
     * 1. find a list of type {@link CardRequestTO} which are in the state CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC
     * <p/>
     * 2. Revoke the card which assigned to the appropriate request in the loop
     * <p/>
     * 3. Updating the request
     *
     * @throws {@link BaseException}
     */
    @BizLoggable(logAction = "PROCESS_UNSUCCESSFUL_DELIVERY", logEntityName = "REQUEST", logActor = "System")
    public String processOnRequestsWithBiometricProblem(Integer from) throws BaseException {
        try {
            List<CardRequestTO> RequestTOListForBiometricProblem = getCardRequestDAO()
                    .findByCardRequestState(CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC, from, 1);
            Map<Object, Object> businessLogMap = new HashMap<Object, Object>();
            List<String> cardRequestIdsForLog = new ArrayList<String>();
            List<String> cardIdsForLog = new ArrayList<String>();
            for (CardRequestTO cardRequestTO : RequestTOListForBiometricProblem) {
                cardRequestIdsForLog.add(cardRequestTO.getId().toString());
                cardIdsForLog.add(cardRequestTO.getCard().getId().toString());
                try {
                	unsuccessfulDeliveryLogger.info("\n-------------------------------------------------------------------------------------------");
		            unsuccessfulDeliveryLogger.info("\n-------------------------------- revoke card ----------------------------------------------");
		            unsuccessfulDeliveryLogger.info("\n-------------------------------------------------------------------------------------------");
		            unsuccessfulDeliveryLogger.info("\nrequest id is:"+cardRequestTO.getId());
                    revokeCard(cardRequestTO, "");
                    //Some extensions on the type of request
                    branchUnsuccessfulDeliveryType(cardRequestTO);

                    List<CardRequestHistoryTO> allHistorys = getCardRequestHistoryDAO().fetchAllHistoryByRequestId(cardRequestTO.getId());
                    for (CardRequestHistoryTO cardRequestHistoryTO : allHistorys) {
                        boolean stop = false;

                        switch (cardRequestHistoryTO
                                .getCardRequestHistoryAction()) {
                            case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC:
                            case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_FINGER:
                            case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_CFINGER: {
                                // Removing the biometric data which belong to
                                // Finger
                                unsuccessfulDeliveryLogger.info("\nremove finger biometric and finger info");
                                unsuccessfulDeliveryLogger.info("\ncard request history action is:" + cardRequestHistoryTO
                                        .getCardRequestHistoryAction());
                                unsuccessfulDeliveryLogger.info("\ncard request history id is:" + cardRequestHistoryTO
                                        .getId());
                                getBiometricDAO().removeFingersInfoByCitizenId(
                                        cardRequestTO.getCitizen().getId());
                                getBiometricInfoDAO().removeBiometricInfo(
                                        cardRequestTO.getCitizen().getId());
                                stop = true;
                            }
                            break;
                            case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_FINGER_IMAGE: {
                                unsuccessfulDeliveryLogger.info("\nremove finger biometric and finger info and biometric face");
                                unsuccessfulDeliveryLogger.info("\ncard request history action is:" + cardRequestHistoryTO
                                        .getCardRequestHistoryAction());
                                unsuccessfulDeliveryLogger.info("\ncard request history id is:" + cardRequestHistoryTO
                                        .getId());
                                getBiometricDAO().removeFingersInfoByCitizenId(
                                        cardRequestTO.getCitizen().getId());
                                getBiometricInfoDAO().removeBiometricInfo(
                                        cardRequestTO.getCitizen().getId());
                                getBiometricDAO().removeFaceInfoByCitizenId(cardRequestTO.getCitizen().getId());
                                stop = true;
                            }
                            break;
                            case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_IMAGE: {
                                unsuccessfulDeliveryLogger.info("\nremove biometric face");
                                unsuccessfulDeliveryLogger.info("\ncard request history action is:" + cardRequestHistoryTO
                                        .getCardRequestHistoryAction());
                                unsuccessfulDeliveryLogger.info("\ncard request history id is:" + cardRequestHistoryTO
                                        .getId());
                                getBiometricDAO().removeFaceInfoByCitizenId(cardRequestTO.getCitizen().getId());
                                stop = true;
                            }
                            break;

						default:
							break;
						}
						if(stop)
							{

								cardRequestTO.setState(CardRequestState.DOCUMENT_AUTHENTICATED);
								cardRequestTO.setReEnrolledDate(new Date());
								break;
							}
						}




                } catch (BaseException e) {
                	unsuccessfulDeliveryLogger.error(e.getExceptionCode(), e.getMessage(), e);
                    if (BizExceptionCode.CSI_071.equals(e.getExceptionCode())) {
//					Some extensions on the type of request
                        branchUnsuccessfulDeliveryType(cardRequestTO);

                        List<CardRequestHistoryTO> allHistorys = getCardRequestHistoryDAO().fetchAllHistoryByRequestId(cardRequestTO.getId());
                        for (CardRequestHistoryTO cardRequestHistoryTO : allHistorys) {
    						boolean stop=false;

    						switch (cardRequestHistoryTO
    								.getCardRequestHistoryAction()) {
    						case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC:
    						case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_CFINGER: {
    							// Removing the biometric data which belong to
    							// Finger
    							unsuccessfulDeliveryLogger.info("\nremove finger biometric and finger info");
    							unsuccessfulDeliveryLogger.info("\ncard request history action is:"+cardRequestHistoryTO
    									.getCardRequestHistoryAction());
    							unsuccessfulDeliveryLogger.info("\ncard request history id is:"+cardRequestHistoryTO
    									.getId());
    							getBiometricDAO().removeFingersInfoByCitizenId(
    									cardRequestTO.getCitizen().getId());
    							getBiometricInfoDAO().removeBiometricInfo(
    									cardRequestTO.getCitizen().getId());
    							stop=true;
    						}
    							break;
    						case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_FINGER_IMAGE:
    						{
    							unsuccessfulDeliveryLogger.info("\nremove finger biometric and finger info and biometric face");
    							unsuccessfulDeliveryLogger.info("\ncard request history action is:"+cardRequestHistoryTO
    									.getCardRequestHistoryAction());
    							unsuccessfulDeliveryLogger.info("\ncard request history id is:"+cardRequestHistoryTO
    									.getId());
    							getBiometricDAO().removeFingersInfoByCitizenId(
    									cardRequestTO.getCitizen().getId());
    							getBiometricInfoDAO().removeBiometricInfo(
    									cardRequestTO.getCitizen().getId());
    							getBiometricDAO().removeFaceInfoByCitizenId(cardRequestTO.getCitizen().getId());
    							stop=true;
    						}
    							break;
    						case UNSUCCESSFUL_DELIVERY_BECAUSE_OF_IMAGE:
    						{
    							unsuccessfulDeliveryLogger.info("\nremove biometric face");
    							unsuccessfulDeliveryLogger.info("\ncard request history action is:"+cardRequestHistoryTO
    									.getCardRequestHistoryAction());
    							unsuccessfulDeliveryLogger.info("\ncard request history id is:"+cardRequestHistoryTO
    									.getId());
    							getBiometricDAO().removeFaceInfoByCitizenId(cardRequestTO.getCitizen().getId());
    							stop=true;
    						}
    							break;

    						default:
    							break;
    						}
    						if(stop)
    							{

    								cardRequestTO.setState(CardRequestState.DOCUMENT_AUTHENTICATED);
    								cardRequestTO.setReEnrolledDate(new Date());
    								break;
    							}
    						}

                    } else {
                        throw e;
                    }
                }
            }
            businessLogMap.put("cardRequestIds", cardRequestIdsForLog.toString());
            businessLogMap.put("cardIds", cardIdsForLog.toString());
            return EmsUtil.convertStringToJSONFormat("requestsWithBiometricProblem", businessLogMap);
        } catch (BaseException e) {
        	unsuccessfulDeliveryLogger.error(e.getExceptionCode(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
        	unsuccessfulDeliveryLogger.error("unhandle exception occurred:", e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CMS_056, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    public Long findRequestsCountByState(CardRequestState cardRequestState) throws BaseException {
        return getCardRequestDAO().findRequestsCountByState(cardRequestState);
    }

    @Override
    public List<Long> findRequestsIdByState(CardRequestState cardRequestState,Integer fetchLimit) throws BaseException {
        return getCardRequestDAO().findRequestsIdByState(cardRequestState,fetchLimit);
    }

    @Override
    public Long findReceivedBatchesCount() throws BaseException {
        return getDispatchDAO().findReceivedBatchesCount();
    }

    /**
     * in this method first is checked that the profilekey for confirm lost card is true or false
     * if it was true then it means in business it must check that has been done confirm for batches or not.
     * if it was false then it means in business it dont need to check confirm. instead of it we check the interval time.
     * @author ganjyar
     */
    @Override
    public Long findMissedBatchesCount() throws BaseException {

		try {
			// true: need to manager confirm
			if (Boolean.valueOf(EmsUtil.getProfileValue(
					ProfileKeyName.KEY_LOST_CARD_CONFIRM,
					DEFAULT_CARD_LOST_CONFIRM))) {
				return getDispatchDAO().findMissedBatchesCountConfirmed();

			} else {

				return getDispatchDAO().findMissedBatchesCount();

			}

		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.CMS_080,
					BizExceptionCode.GLB_008_MSG, e);
		}


    }

    @Override
    public Long findMissedBoxesCount() throws BaseException {
        return getDispatchDAO().findMissedBoxesCount();
    }


    /**
     * in this method first is checked that the profilekey for confirm lost card is true or false
     * if it was true then it means in business it must check that has been done confirm for cards or not.
     * if it was false then it means in business it dont need to check confirm. instead of it we check the interval time.
     * @author ganjyar
     */
    @Override
	public Long findMissedCardsCount() throws BaseException {
		try {
			// true: need to manager confirm
			if (Boolean.valueOf(EmsUtil.getProfileValue(
					ProfileKeyName.KEY_LOST_CARD_CONFIRM,
					DEFAULT_CARD_LOST_CONFIRM))) {
				return getDispatchDAO().findMissedCardsCountConfirmed();

			} else {

				return getDispatchDAO().findMissedCardsCount();

			}

		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.CMS_078,
					BizExceptionCode.GLB_008_MSG, e);
		}
	}

    /**
     * @author Saeed Jalilian (jalilian@gamelectronics.com)
     * @see com.gam.nocr.ems.biz.service.CardManagementService#checkCRNValidation(Long, String)
     */
    @Override
    public Boolean checkCRNValidation(Long requestID, String crn) throws BaseException {
        try {
            if (requestID == null) {
                throw new ServiceException(BizExceptionCode.CMS_057, BizExceptionCode.GLB_026_MSG);
            }
            if (!EmsUtil.checkString(crn)) {
                throw new ServiceException(BizExceptionCode.CMS_058, BizExceptionCode.CMS_058_MSG);
            }
            String savedCRN = getCardDAO().findCRNByRequestId(requestID);
            if (!EmsUtil.checkString(savedCRN)) {
                throw new ServiceException(BizExceptionCode.CMS_059, BizExceptionCode.CMS_059_MSG);
            }
            if (!savedCRN.equals(crn)) {
                throw new ServiceException(BizExceptionCode.CMS_060, BizExceptionCode.CMS_060_MSG);
            }
            return true;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CMS_061, BizExceptionCode.GLB_008_MSG, e);
        }
    }
	/**
	 * this method is used to confirm lost card in 3s
	 * @author ganjyar
	 */
	@Override
	@Permissions(value = "ems_viewCardLost")
	public void doConfirmLostCard(Long cardId)
			throws BaseException {
		try {
			if (cardId == null) {
				throw new ServiceException(BizExceptionCode.CMS_063,
						BizExceptionCode.CMS_063_MSG);
			}
			CardTO card = getCardDAO().find(CardTO.class, cardId);
			if (card == null) {
				throw new ServiceException(BizExceptionCode.CMS_065,
						BizExceptionCode.CMS_065_MSG);
			}
			if (card.getState() != CardState.SHIPPED) {
				throw new ServiceException(BizExceptionCode.CMS_066,
						BizExceptionCode.CMS_066_MSG);

			}
			if (card.getLostDate() == null) {
				throw new ServiceException(BizExceptionCode.CMS_067,
						BizExceptionCode.CMS_067_MSG);
			}
			logger.info("confirming lost card with id :"+cardId);
			card.setIsLostCardConfirmed(true);
			return;
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.CMS_064,
					BizExceptionCode.GLB_008_MSG, e);
		}

	}

    /**
	 * this method is used to unconfirm lost card in 3s
	 * @author Namjoofar
	 */
	@Override
	@Permissions(value = "ems_viewCardLost")
	public void doUnconfirmLostCard(Long cardId)
			throws BaseException {
		try {
            if (cardId == null) {
                throw new ServiceException(BizExceptionCode.CMS_107,
                        BizExceptionCode.CMS_107_MSG);
            }
            CardTO card = getCardDAO().find(CardTO.class, cardId);
            if (card == null) {
                throw new ServiceException(BizExceptionCode.CMS_108,
                        BizExceptionCode.CMS_108_MSG);
            }
            if (card.getState() != CardState.SHIPPED) {
                throw new ServiceException(BizExceptionCode.CMS_109,
                        BizExceptionCode.CMS_109_MSG);

            }
            if (card.getLostDate() == null) {
                throw new ServiceException(BizExceptionCode.CMS_110,
                        BizExceptionCode.CMS_110_MSG);
            }
			logger.info("unconfirming lost card with id :" + cardId);
            card.setLostDate(null);
			return;
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.CMS_111,
					BizExceptionCode.GLB_008_MSG, e);
		}

	}

	/**
	 * This method fetch lost cards which are confirmed by manager in 3s
	 * @author ganjyar
	 *
	 */
	@Override
	public List<CardDispatchInfoVTO> fetchCardLostTempList(
			GeneralCriteria criteria) throws BaseException {
		try {
			return getCardDAO().fetchCardLostTempList(criteria);

		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.CMS_068,
					BizExceptionCode.GLB_008_MSG, e);
		}

	}

    /**
     * This method count lost cards which are confirmed by manager in 3s
     *
     * @author ganjyar
     */
    @Override
    public Integer countCardLostTemp(GeneralCriteria criteria)
            throws BaseException {

        try {
            return getCardDAO().countCardLostTemp(criteria);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CMS_069,
                    BizExceptionCode.GLB_008_MSG, e);
        }

    }

    /**
     * this method is used to confirm lost batch which are missed in ccos.
     * a job will do confirmation and notify cms
     *
     * @author ganjyar
     */
    @Override
    @Permissions(value = "ems_viewCardLost")
    public void doConfirmLostBatch(Long batchId) throws BaseException {

        try {
            if (batchId == null) {
                throw new ServiceException(BizExceptionCode.CMS_070,
                        BizExceptionCode.CMS_070_MSG);
            }
            BatchTO batch = getBatchDAO().find(BatchTO.class, batchId);
            if (batch == null) {
                throw new ServiceException(BizExceptionCode.CMS_071,
                        BizExceptionCode.CMS_071_MSG);
            }
            if (batch.getState() != BatchState.SHIPPED) {
                throw new ServiceException(BizExceptionCode.CMS_072,
                        BizExceptionCode.CMS_072_MSG);

            }
            List<DispatchInfoTO> dispatchInfo = getDispatchDAO().findByContainerId(batchId);

            if (dispatchInfo.get(0).getLostDate() == null) {
                throw new ServiceException(BizExceptionCode.CMS_073,
                        BizExceptionCode.CMS_073_MSG);
            }
            logger.info("confirming lost batch with id :" + batchId);
            batch.setIsLostBatchConfirmed(true);
            getCardDAO().updateLostConfirmBytBatchConfirm(batchId);
            return;
        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (Exception e) {
            sessionContext.setRollbackOnly();
            throw new ServiceException(BizExceptionCode.CMS_074,
                    BizExceptionCode.GLB_008_MSG, e);
        }


    }

    /**
     * this method is used to unconfirm lost batch which are missed in ccos.
     * a job will do confirmation and notify cms
     *
     * @author Namjoofar
     */
    @Override
    @Permissions(value = "ems_viewCardLost")
    public void doUnconfirmLostBatch(Long batchId) throws BaseException {
        try {
            if (batchId == null) {
                throw new ServiceException(BizExceptionCode.CMS_112,
                        BizExceptionCode.CMS_112_MSG);
            }
            BatchTO batch = getBatchDAO().find(BatchTO.class, batchId);
            if (batch == null) {
                throw new ServiceException(BizExceptionCode.CMS_113,
                        BizExceptionCode.CMS_113_MSG);
            }
            if (batch.getState() != BatchState.SHIPPED) {
                throw new ServiceException(BizExceptionCode.CMS_114,
                        BizExceptionCode.CMS_114_MSG);

            }
            List<DispatchInfoTO> dispatchInfo = getDispatchDAO().findByContainerId(batchId);

            if (dispatchInfo.get(0).getLostDate() == null) {
                throw new ServiceException(BizExceptionCode.CMS_115,
                        BizExceptionCode.CMS_115_MSG);
            }
            logger.info("unconfirming lost batch with id :" + batchId);
            getCardDAO().unconfirmAllCardsOfBatch(batchId);
            return;
        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (Exception e) {
            sessionContext.setRollbackOnly();
            throw new ServiceException(BizExceptionCode.CMS_116,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * This method count lost batches which are confirmed by manager in 3s
     *
     * @author ganjyar
     */
    @Override
    public Integer countBatchLostTemp(GeneralCriteria criteria)
            throws BaseException {

        try {

            return getBatchDAO().countBatchLostTemp(criteria);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CMS_076,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * This method fetch lost batches which are confirmed by manager in 3s
     *
     * @author ganjyar
     */
    @Override
    public List<BatchDispatchInfoVTO> fetchBatchLostTempList(
            GeneralCriteria criteria) throws BaseException {
        try {
            return getBatchDAO().fetchBatchLostTempList(criteria);

        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CMS_082,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    /**
     * this method is called in card delivering process and use getEstelam3 to verification citizen
     * in this method we can figure out the citizen is dead or forbidden or has not equal info
     *
     * @param imsCitizenInfoWTO  citizen info e.g nid, name
     * @param securityContextWTO The login and session information of the user
     * @author ganjyar
     */
    @Override
    public ImsCitizenInfoResponseWTO doImsVerificationInDelivery(
            ImsCitizenInfoRequestWTO imsCitizenInfoRequest)
            throws BaseException {
        try {
            Long reqId = imsCitizenInfoRequest.getRequestId();
            if (reqId != null) {
                ImsCitizenInfoResponseWTO response = getIMSManagementService()
                        .verificationInDelivery(imsCitizenInfoRequest);

                // set nist header
                Boolean parseNistAllways = Boolean.valueOf(EmsUtil
                        .getProfileValue(ProfileKeyName.KEY_PARSE_NIST_ALLWAYS,
                                DEFAULT_KEY_PARSE_NIST_ALLWAYS));

                if (parseNistAllways) {

                    List<BiometricTO> fingers = getBiometricDAO()
                            .findByRequestIdAndType(
                                    imsCitizenInfoRequest.getRequestId(),
                                    BiometricType.FING_ALL);
                    if (EmsUtil.checkListSize(fingers)) {
                        NistResult parseNistData = NistParser
                                .parseNistData(fingers.get(0).getData());
                        response.setNistHeader(parseNistData.getNistHeader());
                    } else
                        throw new ServiceException(BizExceptionCode.CMS_093,
                                BizExceptionCode.CMS_085_MSG);

                } else {
                    NistHeaderTO nistHeader = getNistHeaderDAO()
                            .findByRequestId(reqId);
                    if (nistHeader == null || nistHeader.getData() == null) {
                        List<BiometricTO> fingers = getBiometricDAO()
                                .findByRequestIdAndType(
                                        imsCitizenInfoRequest.getRequestId(),
                                        BiometricType.FING_ALL);
                        if (EmsUtil.checkListSize(fingers)) {
                            NistResult parseNistData = NistParser
                                    .parseNistData(fingers.get(0).getData());
                            response.setNistHeader(parseNistData
                                    .getNistHeader());
                        } else
                            throw new ServiceException(
                                    BizExceptionCode.CMS_093,
                                    BizExceptionCode.CMS_085_MSG);
                    } else
                        response.setNistHeader(nistHeader.getData());
                }
                //
                if (response.getImsIsDown())
                    throw new ServiceException(BizExceptionCode.CMS_083,
                            BizExceptionCode.CMS_083_MSG);

                if (response.getEstelamIsFalse()) {
                    getCardRequestHistoryDAO().create(
                            new CardRequestTO(imsCitizenInfoRequest.getRequestId()),
                            "estelam in delivery", SystemId.CCOS, null,
                            CardRequestHistoryAction.NOT_VERIFIED_BY_IMS, getUserProfileTO().getUserName());
                    if (response.getLogInfo().contains("err.record.not.found"))
                        throw new ServiceException(BizExceptionCode.CMS_100,
                                BizExceptionCode.CMS_085_MSG);
                    else
                        /*throw new ServiceException(response.getLogInfo(),
                                BizExceptionCode.CMS_084_MSG);*/
                    throw new ServiceException(BizExceptionCode.CMS_101,
                            BizExceptionCode.CMS_084_MSG);
                }

                CardRequestHistoryAction historyAction = null;

                StringBuilder details = new StringBuilder();
                if (response.getImsIsForbidden())

                    historyAction = CardRequestHistoryAction.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_IS_FORBIDDEN;
                else if (response.getImsIsDead())

                    historyAction = CardRequestHistoryAction.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_IS_DIED;
                else if (response.getImsIsInfoError()) {
                    if (response.getImsIsNameError())
                        details.append("name,");
                    if (response.getImsIsLastNameError())
                        details.append("lastName,");
                    if (response.getImsIsFatherNameError())
                        details.append("fatherName,");
                    if (response.getImsIsBrithDateError())
                        details.append("birthDay");

                    historyAction = CardRequestHistoryAction.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_IDENTITY_CHANGE;
                }
                if (historyAction != null) {
                    CardRequestTO cardRequest = getCardRequestDAO().find(
                            CardRequestTO.class,
                            imsCitizenInfoRequest.getRequestId());
                    cardRequest.setState(CardRequestState.STOPPED);
                    getCardRequestHistoryDAO().create(
                            new CardRequestTO(cardRequest.getId()),
                            details.toString(), SystemId.CCOS, null,
                            historyAction, getUserProfileTO().getUserName());
                }
                return response;
            } else
                throw new ServiceException(BizExceptionCode.CMS_088,
                        BizExceptionCode.CMS_088_MSG);

        } catch (BaseException e) {
            imsVerificationInDelivery.error("An exception occured calling doImsVerificationInDelivery.", e);
            logger.error("An exception occured calling doImsVerificationInDelivery.", e);
            sessionContext.setRollbackOnly();
            throw e;

        } catch (Exception e) {
            imsVerificationInDelivery.error("An unhandle exception occured calling doImsVerificationInDelivery.", e);
            logger.error("An unhandle occured calling doImsVerificationInDelivery.", e);
            sessionContext.setRollbackOnly();
            throw new ServiceException(BizExceptionCode.CMS_086,
                    BizExceptionCode.GLB_008_MSG, e);
        }

    }

    /**
     * this method is called when cut fingers has been saved in complete registration
     * are not matched with cut fingers in delivery card
     *
     * @param requestID
     * @param reason
     * @throws BaseException
     * @author ganjyar
     */
    private void notifyUnsuccessfulDeliveryForCutFingersChanged(Long requestID,
                                                                UnSuccessfulDeliveryRequestReason reason) throws BaseException {
        CardRequestTO cardRequestTO = getCardRequestDAO().find(
                CardRequestTO.class, requestID);
        if (cardRequestTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_013,
                    BizExceptionCode.CMS_011_MSG);
        }
        if (CardRequestState.READY_TO_DELIVER.equals(cardRequestTO.getState())) {
            cardRequestTO
                    .setState(CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC);
            getCardRequestHistoryDAO()
                    .create(new CardRequestTO(cardRequestTO.getId()),
                            null,
                            SystemId.CCOS,
                            null,
                            CardRequestHistoryAction.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_CFINGER,
                            getUserProfileTO().getUserName());
        } else {
            throw new ServiceException(BizExceptionCode.CMS_030,
                    BizExceptionCode.CMS_030_MSG);
        }
    }


    @Override
    public String processOnRequestsWithIdentifyChanged(Integer from)
            throws BaseException {
        try {
            List<CardRequestTO> RequestTOListForIdentifyProblem = getCardRequestDAO()
                    .findByCardRequestState(CardRequestState.STOPPED, from, 1);
            Map<Object, Object> businessLogMap = new HashMap<Object, Object>();
            List<String> cardRequestIdsForLog = new ArrayList<String>();
            List<String> cardIdsForLog = new ArrayList<String>();
            for (CardRequestTO cardRequestTO : RequestTOListForIdentifyProblem) {
                cardRequestIdsForLog.add(cardRequestTO.getId().toString());
                cardIdsForLog.add(cardRequestTO.getCard().getId().toString());
                try {
                    unsuccessfulDeliveryLogger.info("\n-------------------------------------------------------------------------------------------");
                    unsuccessfulDeliveryLogger.info("\n-------------------------------- revoke and repeal request --------------------------------");
                    unsuccessfulDeliveryLogger.info("\n-------------------------------------------------------------------------------------------");
                    unsuccessfulDeliveryLogger.info("\nrequest id is:" + cardRequestTO.getId());
                    revokeCard(cardRequestTO, "");
                    getCardRequestService(userProfileTO).repealCardRequestInDelivery(cardRequestTO);
                    unsuccessfulDeliveryLogger.info("\nrequest repealed!");
                    branchUnsuccessfulDeliveryType(cardRequestTO);
                } catch (BaseException e) {
                    if (BizExceptionCode.CSI_071.equals(e.getExceptionCode())) {
                        getCardRequestService(userProfileTO)
                                .repealCardRequestInDelivery(cardRequestTO);
                        unsuccessfulDeliveryLogger.info("\nrequest repealed!");
                        branchUnsuccessfulDeliveryType(cardRequestTO);
                        unsuccessfulDeliveryLogger.error("exception in revoke and repeal card request: " + e.getExceptionCode(), e.getMessage(), e);
                    } else {
                        throw e;
                    }
                }
            }
            businessLogMap.put("cardRequestIds",
                    cardRequestIdsForLog.toString());
            businessLogMap.put("cardIds", cardIdsForLog.toString());
            return EmsUtil.convertStringToJSONFormat(
                    "requestsWithBiometricProblem", businessLogMap);
        } catch (BaseException e) {
            unsuccessfulDeliveryLogger.error(e.getExceptionCode(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            unsuccessfulDeliveryLogger.error("unhandle exception occurred:", e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.CMS_056,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    public String findCmsBatchByRequestId(Long requestId) throws BaseException {
        try {
            String cmsId = "";
            if (requestId == null) {
                throw new ServiceException(BizExceptionCode.CMS_104,
                        BizExceptionCode.CMS_104_MSG);
            }
            cmsId = getBatchDAO().findCmsIdByRequestId(requestId);
            if (cmsId == null) {
                throw new ServiceException(BizExceptionCode.CMS_102,
                        BizExceptionCode.CMS_102_MSG);
            }
            return cmsId;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.CMS_103,
                    BizExceptionCode.CMS_103_MSG, e);
        }
    }

    /**
     * The method notifyUnsuccessfulDeliveryForFaceImageUnmatched is used to handle all the activities which are needed in
     * unsuccessful Delivery by reason of 'face image unmatched'
     * this method is called when card photo is not matched with citizen face
     *
     * @param requestID an object of type {@link Long} which represents the id of an specified object of type {@link
     *                  CardRequestTO}
     * @param reason    an enumeration instance of type {@link UnSuccessfulDeliveryRequestReason}
     * @throws {@link BaseException}
     * @author ganjyar
     */
    private void notifyUnsuccessfulDeliveryForFaceImageUnmatched(Long requestID, UnSuccessfulDeliveryRequestReason reason) throws BaseException {
        CardRequestTO cardRequestTO = getCardRequestDAO().find(CardRequestTO.class, requestID);
        if (cardRequestTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_092, BizExceptionCode.CMS_011_MSG);
        }
        if (CardRequestState.READY_TO_DELIVER.equals(cardRequestTO.getState())) {
            cardRequestTO.setState(CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC);
            getCardRequestHistoryDAO().create(
                    new CardRequestTO(cardRequestTO.getId()),
                    null,
                    SystemId.CMS,
                    null,
                    CardRequestHistoryAction.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_IMAGE,
                    getUserProfileTO().getUserName());
        } else {
            throw new ServiceException(BizExceptionCode.CMS_091, BizExceptionCode.CMS_030_MSG);
        }
    }

    /**
     * The method notifyUnsuccessfulDeliveryForbioAndFace is used to handle all the activities which are needed in
     * unsuccessful Delivery by reason of 'bio and face unmatched'
     * this method is called when card photo is not matched with citizen face and cut fingers is not matched with
     * current cut fingers(both of them check in one form in ccos)
     *
     * @param requestID an object of type {@link Long} which represents the id of an specified object of type {@link
     *                  CardRequestTO}
     * @param reason    an enumeration instance of type {@link UnSuccessfulDeliveryRequestReason}
     * @throws {@link BaseException}
     * @author ganjyar
     */
    private void notifyUnsuccessfulDeliveryForbioAndFaceError(Long requestID, UnSuccessfulDeliveryRequestReason reason) throws BaseException {
        CardRequestTO cardRequestTO = getCardRequestDAO().find(CardRequestTO.class, requestID);
        if (cardRequestTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_089, BizExceptionCode.CMS_011_MSG);
        }
        if (CardRequestState.READY_TO_DELIVER.equals(cardRequestTO.getState())) {
            cardRequestTO.setState(CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC);
            getCardRequestHistoryDAO().create(
                    new CardRequestTO(cardRequestTO.getId()),
                    null,
                    SystemId.CMS,
                    null,
                    CardRequestHistoryAction.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_FINGER_IMAGE,
                    getUserProfileTO().getUserName());
        } else {
            throw new ServiceException(BizExceptionCode.CMS_090, BizExceptionCode.CMS_030_MSG);
        }
    }


    /**
     * The method notifyUnsuccessfulDeliveryForBioFourteenFingerTry is used to handle all the activities which are needed in
     * unsuccessful Delivery by reason of '14 times tried and finger unmatched'
     * this method is called when 14 times tried and finger unmatched with current cut fingers
     *
     * @param requestID an object of type {@link Long} which represents the id of an specified object of type {@link
     *                  CardRequestTO}
     * @param reason    an enumeration instance of type {@link UnSuccessfulDeliveryRequestReason}
     * @throws {@link BaseException}
     * @author amiri
     */
    private void notifyUnsuccessfulDeliveryForBioFourteenFingerTry(Long requestID, UnSuccessfulDeliveryRequestReason reason) throws BaseException {
        CardRequestTO cardRequestTO = getCardRequestDAO().find(
                CardRequestTO.class, requestID);
        if (cardRequestTO == null) {
            throw new ServiceException(BizExceptionCode.CMS_106,
                    BizExceptionCode.CMS_011_MSG);
        }
        if (CardRequestState.READY_TO_DELIVER.equals(cardRequestTO.getState())) {
            cardRequestTO
                    .setState(CardRequestState.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_BIOMETRIC);
            getCardRequestHistoryDAO()
                    .create(new CardRequestTO(cardRequestTO.getId()),
                            null,
                            SystemId.EMS,
                            null,
                            CardRequestHistoryAction.UNSUCCESSFUL_DELIVERY_BECAUSE_OF_FINGER,
                            getUserProfileTO().getUserName());
        } else {
            throw new ServiceException(BizExceptionCode.CMS_105,
                    BizExceptionCode.CMS_030_MSG);
        }
    }

}
