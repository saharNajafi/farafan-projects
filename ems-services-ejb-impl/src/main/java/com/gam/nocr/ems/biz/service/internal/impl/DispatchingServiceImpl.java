package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactory;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.CMSService;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.*;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.vol.CardInfoVTO;
import com.gam.nocr.ems.data.domain.ws.DispatchInfoWTO;
import com.gam.nocr.ems.data.enums.*;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;
import com.gam.nocr.ems.util.EmsUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * TODO -- Explain this class
 * </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Stateless(name = "DispatchingService")
@Local(DispatchingServiceLocal.class)
@Remote(DispatchingServiceRemote.class)
public class DispatchingServiceImpl extends EMSAbstractService implements
        DispatchingServiceLocal, DispatchingServiceRemote {

    private static final Logger logger = BaseLog
            .getLogger(DispatchingServiceImpl.class);
    private static final Logger cmsDispatchLogger = BaseLog
            .getLogger("cmsdispatch");

    @Resource
    SessionContext sessionContext;
//
//	@Resource
//	UserTransaction utx;

    @Override
    @BizLoggable(logAction = "INSERT", logEntityName = "BATCH")
    public String batchProduction(String batchId, List<CardTO> cards)
            throws BaseException {
        cmsDispatchLogger
                .info("batchProduction service called with batchId: '{}' and cards: '{}'",
                        batchId, cards);
        try {
            if (EmsUtil.checkString(batchId)) {
                if (cards.size() > 0) {
                    BatchTO batchTO = new BatchTO();
                    batchTO.setCmsID(batchId);
                    batchTO.setState(BatchState.READY);
                    // By Adldoost
                    // batchTO.setTotalCardsCount((long) cards.size());
                    // batchTO.setReceivedCardsCount((long) 0);
                    // batchTO.setLostCardCount((long) 0);
                    // ////////////////////////////////
                    cmsDispatchLogger
                            .info("Inserting batch object in database - cmsID: {}, state: {}",
                                    new Object[]{batchTO.getCmsID(),
                                            batchTO.getState()});

                    try {
                        getDispatchDAO().create(batchTO);
                    } catch (BaseException e) {
                        if ("EMS_D_DSI_026".equals(e.getExceptionCode())) {
                            cmsDispatchLogger
                                    .error("There is already an existing batch with CMSID: {}",
                                            batchTO.getCmsID());
                            BatchTO loadedBatchTO = getDispatchDAO()
                                    .findBatchByCmsId(batchTO.getCmsID());
                            cmsDispatchLogger
                                    .error("The state of already existing batch with CMSID {} is {}",
                                            batchTO.getCmsID(),
                                            loadedBatchTO.getState());
                            if (BatchState.READY.equals(loadedBatchTO
                                    .getState())) {
                                throw new ServiceException(
                                        BizExceptionCode.DPI_021,
                                        BizExceptionCode.DPI_021_MSG,
                                        (batchTO.getCmsID() + "," + loadedBatchTO
                                                .getState()).split(","));
                            } else {
                                throw new ServiceException(
                                        BizExceptionCode.DPI_022,
                                        BizExceptionCode.DPI_022_MSG,
                                        (batchTO.getCmsID() + "," + "NEW," + loadedBatchTO
                                                .getState()).split(","));
                            }
                        }
                    }

                    CardDAO cardDAO = getCardDAO();
                    CardRequestDAO cardRequestDAO = getCardRequestDAO();
                    CardRequestHistoryDAO cardRequestHistoryDAO = getCardRequestHistoryDAO();

                    cmsDispatchLogger
                            .info("Starting to insert card information in database");

                    for (CardTO cardTO : cards) {
                        if (EmsUtil.checkString(cardTO.getCmsRequestId())) {
                            cmsDispatchLogger
                                    .info("Finding card request history by requestID {} and having 'PENDING_ISSUANCE' in their result field",
                                            cardTO.getCmsRequestId());
                            List<CardRequestHistoryTO> cardRequestHistoryList = cardRequestHistoryDAO
                                    .findByCmsRequestIdAndResult(cardTO
                                                    .getCmsRequestId(),
                                            CardRequestState.PENDING_ISSUANCE
                                                    .name());

                            if (cardRequestHistoryList.size() == 1) {
                                cmsDispatchLogger
                                        .info("Creating card record with crn {} and state 'READY' for batch {}",
                                                cardTO.getCrn(),
                                                batchTO.getCmsID());

                                cardTO.setBatch(batchTO);
                                cardDAO.create(cardTO);

                                cmsDispatchLogger
                                        .info("Updating card request state to 'ISSUED' and setting its cardID to {}",
                                                cardTO.getId());
                                cardRequestDAO.updateRequest(
                                        cardTO.getCmsRequestId(), cardTO);

                                cmsDispatchLogger
                                        .info("Updating card request history record by adding crn value of {}",
                                                cardTO.getCrn());
                                cardRequestHistoryDAO.updateRequestHistory(
                                        cardTO.getCmsRequestId(), cardTO);

                                cmsDispatchLogger
                                        .info("Inserting new record for 'BATCH_PRODUCTION' event in history for batch {}, crn {} and CMSRequestID {}",
                                                new Object[]{
                                                        batchId,
                                                        cardTO.getCrn(),
                                                        cardTO.getCmsRequestId()});
                                CardRequestTO cardRequestTO = cardRequestDAO
                                        .findByCmsRequestId(cardTO
                                                .getCmsRequestId());
                                getCardRequestHistoryDAO()
                                        .create(cardRequestTO,
                                                cardTO.getBatch().getId()
                                                        + " - "
                                                        + cardTO.getCrn(),
                                                SystemId.CMS,
                                                cardTO.getCmsRequestId(),
                                                CardRequestHistoryAction.BATCH_PRODUCTION,
                                                null);
                            } else {
                                if (cardRequestHistoryList.size() != 0) {
                                    cmsDispatchLogger
                                            .error("More than one record found in history by requestID {} and having 'PENDING_ISSUANCE' in result column",
                                                    cardTO.getCmsRequestId());
                                } else {
                                    cmsDispatchLogger
                                            .error("No record found in history by requestID {} and having 'PENDING_ISSUANCE' in result column",
                                                    cardTO.getCmsRequestId());
                                }
                                throw new ServiceException(
                                        BizExceptionCode.DPI_017,
                                        BizExceptionCode.DPI_017_MSG.replace(
                                                "{}", cardTO.getCrn()), cardTO
                                        .getCrn().split(","));
                            }
                        } else
                            throw new ServiceException(
                                    BizExceptionCode.DPI_016,
                                    BizExceptionCode.DPI_016_MSG,
                                    "requestId".split(","));
                    }
                } else
                    throw new ServiceException(BizExceptionCode.DPI_011,
                            BizExceptionCode.DPI_011_MSG);
            } else
                throw new ServiceException(BizExceptionCode.DPI_010,
                        BizExceptionCode.DPI_010_MSG);
        } catch (BaseException e) {
            try {
                sessionContext.setRollbackOnly();
            } catch (Exception ex) {
                logger.error(BizExceptionCode.GLB_ERR_MSG, ex);
                cmsDispatchLogger.error(BizExceptionCode.GLB_ERR_MSG, ex);
            }
            throw e;
        } catch (Exception e) {
            try {
                sessionContext.setRollbackOnly();
            } catch (Exception ex) {
                logger.error(BizExceptionCode.GLB_ERR_MSG, ex);
                cmsDispatchLogger.error(BizExceptionCode.GLB_ERR_MSG, ex);
            }
            throw new ServiceException(BizExceptionCode.DPI_018,
                    BizExceptionCode.GLB_008_MSG, e);
        }
        Map<Object, Object> businessLogMap = new HashMap<Object, Object>();
        businessLogMap.put("cmsBatchId", batchId);
        businessLogMap.put("cards", cards);
        return EmsUtil.convertStringToJSONFormat("batchProductionInfo",
                businessLogMap);
    }


    @Override
    @BizLoggable(logAction = "INSERT", logEntityName = "BATCH")
    public String batchProduction(String batchId, List<CardTO> cards, String postalTrackingCode)
            throws BaseException {

        cmsDispatchLogger
                .info("batchProduction service called with batchId: '{}' and cards: '{}'", new Object[]{batchId, cards, postalTrackingCode});
        try {
            if (EmsUtil.checkString(batchId)) {
                if (cards.size() > 0) {
                    BatchTO batchTO = new BatchTO();
                    batchTO.setCmsID(batchId);
                    batchTO.setState(BatchState.READY);
                    batchTO.setPostalTrackingCode(postalTrackingCode);
                    // By Adldoost
                    // batchTO.setTotalCardsCount((long) cards.size());
                    // batchTO.setReceivedCardsCount((long) 0);
                    // batchTO.setLostCardCount((long) 0);
                    // ////////////////////////////////
                    cmsDispatchLogger
                            .info("Inserting batch object in database - cmsID: {}, state: {}",
                                    new Object[]{batchTO.getCmsID(),
                                            batchTO.getState()});

                    try {
                        getDispatchDAO().create(batchTO);
                    } catch (BaseException e) {
                        if ("EMS_D_DSI_026".equals(e.getExceptionCode())) {
                            cmsDispatchLogger
                                    .error("There is already an existing batch with CMSID: {}",
                                            batchTO.getCmsID());
                            BatchTO loadedBatchTO = getDispatchDAO()
                                    .findBatchByCmsId(batchTO.getCmsID());
                            cmsDispatchLogger
                                    .error("The state of already existing batch with CMSID {} is {}",
                                            batchTO.getCmsID(),
                                            loadedBatchTO.getState());
                            if (BatchState.READY.equals(loadedBatchTO
                                    .getState())) {
                                throw new ServiceException(
                                        BizExceptionCode.DPI_021,
                                        BizExceptionCode.DPI_021_MSG,
                                        (batchTO.getCmsID() + "," + loadedBatchTO
                                                .getState()).split(","));
                            } else {
                                throw new ServiceException(
                                        BizExceptionCode.DPI_022,
                                        BizExceptionCode.DPI_022_MSG,
                                        (batchTO.getCmsID() + "," + "NEW," + loadedBatchTO
                                                .getState()).split(","));
                            }
                        }

                        if ("EMS_D_DSI_082".equals(e.getExceptionCode())) {
                            cmsDispatchLogger
                                    .error("There is already an existing batch with Postal Tracking Code: {}",
                                            postalTrackingCode);
                            throw new ServiceException(
                                    BizExceptionCode.DPI_064,
                                    BizExceptionCode.DPI_064_MSG,
                                    postalTrackingCode.split(","));
                        }
                    }

                    CardDAO cardDAO = getCardDAO();
                    CardRequestDAO cardRequestDAO = getCardRequestDAO();
                    CardRequestHistoryDAO cardRequestHistoryDAO = getCardRequestHistoryDAO();

                    cmsDispatchLogger
                            .info("Starting to insert card information in database");

                    for (CardTO cardTO : cards) {
                        if (EmsUtil.checkString(cardTO.getCmsRequestId())) {
                            cmsDispatchLogger
                                    .info("Finding card request history by requestID {} and having 'PENDING_ISSUANCE' in their result field",
                                            cardTO.getCmsRequestId());
                            List<CardRequestHistoryTO> cardRequestHistoryList = cardRequestHistoryDAO
                                    .findByCmsRequestIdAndResult(cardTO
                                                    .getCmsRequestId(),
                                            CardRequestState.PENDING_ISSUANCE
                                                    .name());

                            if (cardRequestHistoryList.size() == 1) {
                                cmsDispatchLogger
                                        .info("Creating card record with crn {} and state 'READY' for batch {}",
                                                cardTO.getCrn(),
                                                batchTO.getCmsID());

                                cardTO.setBatch(batchTO);
                                cardDAO.create(cardTO);

                                cmsDispatchLogger
                                        .info("Updating card request state to 'ISSUED' and setting its cardID to {}",
                                                cardTO.getId());
                                cardRequestDAO.updateRequest(
                                        cardTO.getCmsRequestId(), cardTO);

                                cmsDispatchLogger
                                        .info("Updating card request history record by adding crn value of {}",
                                                cardTO.getCrn());
                                cardRequestHistoryDAO.updateRequestHistory(
                                        cardTO.getCmsRequestId(), cardTO);

                                cmsDispatchLogger
                                        .info("Inserting new record for 'BATCH_PRODUCTION' event in history for batch {}, crn {} and CMSRequestID {}",
                                                new Object[]{
                                                        batchId,
                                                        cardTO.getCrn(),
                                                        cardTO.getCmsRequestId()});
                                CardRequestTO cardRequestTO = cardRequestDAO
                                        .findByCmsRequestId(cardTO
                                                .getCmsRequestId());
                                getCardRequestHistoryDAO()
                                        .create(cardRequestTO,
                                                cardTO.getBatch().getId()
                                                        + " - "
                                                        + cardTO.getCrn(),
                                                SystemId.CMS,
                                                cardTO.getCmsRequestId(),
                                                CardRequestHistoryAction.BATCH_PRODUCTION,
                                                null);
                            } else {
                                if (cardRequestHistoryList.size() != 0) {
                                    cmsDispatchLogger
                                            .error("More than one record found in history by requestID {} and having 'PENDING_ISSUANCE' in result column",
                                                    cardTO.getCmsRequestId());
                                } else {
                                    cmsDispatchLogger
                                            .error("No record found in history by requestID {} and having 'PENDING_ISSUANCE' in result column",
                                                    cardTO.getCmsRequestId());
                                }
                                throw new ServiceException(
                                        BizExceptionCode.DPI_017,
                                        BizExceptionCode.DPI_017_MSG.replace(
                                                "{}", cardTO.getCrn()), cardTO
                                        .getCrn().split(","));
                            }
                        } else
                            throw new ServiceException(
                                    BizExceptionCode.DPI_016,
                                    BizExceptionCode.DPI_016_MSG,
                                    "requestId".split(","));
                    }
                } else
                    throw new ServiceException(BizExceptionCode.DPI_011,
                            BizExceptionCode.DPI_011_MSG);
            } else
                throw new ServiceException(BizExceptionCode.DPI_010,
                        BizExceptionCode.DPI_010_MSG);
        } catch (BaseException e) {
            try {
                sessionContext.setRollbackOnly();
            } catch (Exception ex) {
                logger.error(BizExceptionCode.GLB_ERR_MSG, ex);
                cmsDispatchLogger.error(BizExceptionCode.GLB_ERR_MSG, ex);
            }
            throw e;
        } catch (Exception e) {
            try {
                sessionContext.setRollbackOnly();
            } catch (Exception ex) {
                logger.error(BizExceptionCode.GLB_ERR_MSG, ex);
                cmsDispatchLogger.error(BizExceptionCode.GLB_ERR_MSG, ex);
            }
            throw new ServiceException(BizExceptionCode.DPI_018,
                    BizExceptionCode.GLB_008_MSG, e);
        }
        Map<Object, Object> businessLogMap = new HashMap<Object, Object>();
        businessLogMap.put("cmsBatchId", batchId);
        businessLogMap.put("cards", cards);
        return EmsUtil.convertStringToJSONFormat("batchProductionInfo",
                businessLogMap);
    }

    @Override
    public void updateBatchPostalTrackingCode(String batchId, String postalTrackingCode) throws BaseException {
        cmsDispatchLogger
                .info("updateBatchPostalTrackingCode service called with batchId: '{}' and postalTrackingCode: '{}'",
                        batchId, postalTrackingCode);

        try {
            BatchTO loadedBatchTO = getDispatchDAO()
                    .findBatchByCmsId(batchId);

            if (loadedBatchTO == null) {
                throw new ServiceException(
                        BizExceptionCode.DPI_066,
                        BizExceptionCode.DPI_066_MSG);
            } else if (BatchState.RECEIVED.equals(loadedBatchTO
                    .getState())) {
                throw new ServiceException(
                        BizExceptionCode.DPI_065,
                        BizExceptionCode.DPI_065_MSG);
            }

        } catch (BaseException e) {
            if ("EMS_S_DSI_065".equals(e.getExceptionCode())) {
                cmsDispatchLogger
                        .error("Batch Recived Is Announced By Delivery Office");
                throw new ServiceException(
                        BizExceptionCode.DPI_065,
                        BizExceptionCode.DPI_065_MSG);
            }

            if ("EMS_S_DSI_066".equals(e.getExceptionCode())) {
                cmsDispatchLogger
                        .error("Can not find batch with CMSID: {}", batchId);
                throw new ServiceException(
                        BizExceptionCode.DPI_066,
                        BizExceptionCode.DPI_066_MSG);
            }
        }
        catch (Exception e){
            throw new ServiceException(BizExceptionCode.DPI_067,
                    BizExceptionCode.GLB_008_MSG, e);
        }
        try {
            getDispatchDAO().updateBatchPostalTrackingCode(batchId, postalTrackingCode);
        } catch (BaseException e) {
            if ("EMS_D_DSI_081".equals(e.getExceptionCode())) {
                cmsDispatchLogger
                        .error("There is already an existing batch with CMSID: {}",
                                batchId);
                throw new ServiceException(
                        BizExceptionCode.DPI_062,
                        BizExceptionCode.DPI_062_MSG);
            }
            if ("EMS_D_DSI_082".equals(e.getExceptionCode())) {
                cmsDispatchLogger
                        .error("There is already an existing batch with Postal Tracking Code: {}",
                                postalTrackingCode);
                throw new ServiceException(
                        BizExceptionCode.DPI_064,
                        BizExceptionCode.DPI_064_MSG,
                        postalTrackingCode.split(","));
            }
            if ("EMS_D_DSI_080".equals(e.getExceptionCode())) {
                cmsDispatchLogger
                        .error("Can not update batch with CMSID: {}",
                                batchId);
                throw new ServiceException(
                        BizExceptionCode.DPI_063,
                        BizExceptionCode.DPI_063_MSG);
            } else {
                throw new ServiceException(BizExceptionCode.DPI_068,
                        BizExceptionCode.GLB_008_MSG, e);
            }
        }
        catch (Exception e){
            throw new ServiceException(BizExceptionCode.DPI_069,
                    BizExceptionCode.GLB_008_MSG, e);
        }

    }


    @Override
    @BizLoggable(logAction = "INSERT", logEntityName = "BOX")
//	@TransactionAttribute(TransactionAttributeType.NEVER)
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String boxShipped(String boxId, List<String> batchIds)
            throws BaseException {
        cmsDispatchLogger.info("boxShipped called with cmsBoxId: {} and cmsBatchIds: {}", boxId, batchIds);

        try {
            if (EmsUtil.checkString(boxId)) {
                if (batchIds.size() > 0) {

                    /**
                     * Creating new box
                     */
                    BoxTO boxTO = null;
                    Exception originalEx = null;
                    try {

                        // By Adldoost
//						try {
//							utx.begin();
//						} catch (Exception ex) {
//							logger.error(ex.getMessage(), ex);
//							// Do nothing
//						}
                        boxTO = new BoxTO();
                        boxTO.setCmsID(boxId);
                        boxTO.setState(BoxState.READY);
                        // By Adldoost
                        // boxTO.setTotalBatchesCount((long) batchIds.size());
                        // boxTO.setReceivedBatchesCount((long) 0);
                        // boxTO.setLostBatchCount((long) 0);
                        // ///////////////

                        try {
                            if (getDispatchDAO().findBoxByCmsId(boxTO.getCmsID()) == null) {
                                cmsDispatchLogger.info("Creating a new box with id {} in 'READY' state started", boxId);
                                getDispatchDAO().create(boxTO);
                            } else {
                                cmsDispatchLogger.info("Box with cmsBoxId {} already existed in BoxTO", boxId);
                                boxTO = getDispatchDAO().findBoxByCmsId(boxTO.getCmsID());
                            }
                        } catch (BaseException e) {
                            if ("EMS_D_DSI_025".equals(e.getExceptionCode())) {
                                cmsDispatchLogger
                                        .error("There is already an existing box with CMSID: {}",
                                                boxId);
                                BoxTO to = getDispatchDAO().findBoxByCmsId(
                                        boxTO.getCmsID());
                                cmsDispatchLogger
                                        .error("The state of already existing box with CMSID {} is {}",
                                                boxId, to.getState());
                                if (!BoxState.READY.equals(to.getState())) {
                                    throw new ServiceException(
                                            BizExceptionCode.DPI_023,
                                            BizExceptionCode.DPI_023_MSG,
                                            (boxTO.getCmsID() + "," + BoxState.SHIPPED)
                                                    .split(","));
                                } /*
                                 * else { throw new
								 * ServiceException(BizExceptionCode.DPI_024,
								 * BizExceptionCode.DPI_024_MSG,
								 * (boxTO.getCmsID() + "," + "NEW," +
								 * to.getState()).split(",")); }
								 */


                            }
                        }

                        cmsDispatchLogger
                                .info("Starting to add already inserted batch items from batchTO to newly created box");
                        //Anbari : update batches which their states are ready and ignore other state, also ignore throw for retrying steps (dirty stored batches in sabt that their state are shipped )
                        List<String> batchIdtoUpdated = new ArrayList<String>();
                        for (String cmsBatchId : batchIds) {
                            BatchTO loadedBatchTo = getDispatchDAO().findBatchByCmsId(cmsBatchId);
                            if (loadedBatchTo == null) {
                                throw new ServiceException(
                                        BizExceptionCode.DPI_025,
                                        BizExceptionCode.DPI_025_MSG,
                                        (cmsBatchId).split(","));
                            } else {
                                //Commited By Anbari (for retrying situation)
//								if (!BatchState.READY.equals(loadedBatchTo
//										.getState())) {
//									throw new ServiceException(
//											BizExceptionCode.DPI_026,
//											BizExceptionCode.DPI_026_MSG,
//											(loadedBatchTo.getCmsID() + ","
//													+ BatchState.READY + "," + loadedBatchTo
//													.getState()).split(","));
//								}
                                //Anbari
                                if (BatchState.READY.equals(loadedBatchTo.getState())) {
                                    cmsDispatchLogger.info("Add Ready batch with cmsid {} to box with cmsid {}", cmsBatchId, boxId);
                                    batchIdtoUpdated.add(cmsBatchId);
                                }

                            }
                        }
                        //Anbari
                        if (batchIdtoUpdated != null && batchIdtoUpdated.size() > 0) {
                            getDispatchDAO().updateBatches(boxTO, batchIdtoUpdated);
                            //Anbari
                            cmsDispatchLogger
                                    .info("Updating state of cards to SHIPPED for those in these batches : {}",
                                            batchIdtoUpdated);
                            getCardDAO().updateCardsStateByCmsId(batchIdtoUpdated);
                        }

//						utx.commit();
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        //cmsDispatchLogger.error("Unable to commit the transaction. So trying to rollback it",e);
                        //


//								
//						try {
//							utx.rollback();
//						} catch (Exception e1) {
//							logger.error(e1.getMessage(), e1);
//							cmsDispatchLogger
//									.error("Unable to rollback the transaction that could not be committed",
//											e);
//							// Do nothing
//						}
                        throw e;
//						originalEx = e;
                    }

                    /**
                     * Tries to set the state of box to shipped
                     */

                    //commented by Anbari ( no trigger exist anymore so don't need to log )
//					cmsDispatchLogger
//							.info("Trying to change inserted box state to SHIPPED in order to trigger dispatch info database trigger to fill dispatch records for box {} with DB identifier {}",
//									boxId, (boxTO != null) ? boxTO.getId()
//											: boxTO);

                    try {
//						utx.begin();

                        // ====================================================================================================
                        // boxTo=findByBoxCmsId
                        // if boxTO==null or boxTO.getId==null {
                        // if originalEx!=null throw originalEx;
                        // throw new BaseException()
                        // }
                        // if (boxTO.getSatate=="SHIPPED")
                        // {
                        // if originalEx!=null throw originalEx;
                        // throw new ServiceException(Already exist)
                        // ====================================================================================================

                        cmsDispatchLogger
                                .info("Loading the box {} from database (db id : {})",
                                        boxId, (boxTO != null) ? boxTO.getId()
                                                : boxTO);
                        BoxTO foundBoxTO = getDispatchDAO().findBoxByCmsId(
                                boxId);
                        if (foundBoxTO == null || foundBoxTO.getId() == null) {
                            cmsDispatchLogger.error("Box {} not found", boxId);
//							if (originalEx != null) {
//								throw originalEx;
//							}
                            throw new ServiceException(
                                    BizExceptionCode.DPI_029,
                                    BizExceptionCode.DPI_029
                                            + BizExceptionCode.DPI_029_MSG);
                        }

                        if (BoxState.SHIPPED.equals(foundBoxTO.getState())) {
                            cmsDispatchLogger
                                    .error("Box {} found and its state is already SHIPPED",
                                            boxId);
//							if (originalEx != null) {
//								throw originalEx;
//							}
                            throw new ServiceException(
                                    BizExceptionCode.DPI_023,
                                    BizExceptionCode.DPI_023_MSG,
                                    (boxTO.getCmsID() + "," + BoxState.SHIPPED)
                                            .split(","));
                        }

                        //System.err.print(sessionContext);

                        foundBoxTO.setState(BoxState.SHIPPED);
                        getDispatchDAO().create(foundBoxTO);

                        cmsDispatchLogger
                                .info("State of box changed to SHIPPED");
                        // CardRequestTO cardRequestTO =
                        // getCardRequestDAO().findByContainerId(boxTO.getId(),
                        // DepartmentDispatchSendType.BOX);
                        // getCardRequestHistoryDAO().create(cardRequestTO,
                        // "BoxID : " + boxTO.getId(), SystemId.CMS, null,
                        // CardRequestHistoryAction.BOX_SHIPMENT, null);

//						utx.commit();
                    } catch (Exception e) {
                        cmsDispatchLogger
                                .error("An error occurred while committing updating box state. So trying to rollback");
//						try {
//							utx.rollback();
//						} catch (Exception e1) {
//							logger.error(e1.getMessage(), e1);
//							cmsDispatchLogger.error("Unable to rollback", e1);
//						}
                        if (e instanceof BaseException)
                            throw (BaseException) e;
                        else
                            throw new ServiceException(
                                    BizExceptionCode.DPI_030,
                                    BizExceptionCode.DPI_030
                                            + BizExceptionCode.DPI_029_MSG);
                    }

                } else
                    throw new ServiceException(BizExceptionCode.DPI_013,
                            BizExceptionCode.DPI_013_MSG);
            } else
                throw new ServiceException(BizExceptionCode.DPI_012,
                        BizExceptionCode.DPI_012_MSG);
        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (Exception e) {
            sessionContext.setRollbackOnly();
            throw new ServiceException(BizExceptionCode.DPI_019,
                    BizExceptionCode.GLB_008_MSG, e);
        }

        Map<Object, Object> businessLogMap = new HashMap<Object, Object>();
        businessLogMap.put("cmsBoxId", boxId);
        businessLogMap.put("cmsBatchIds", batchIds);
        return EmsUtil.convertStringToJSONFormat("boxShippedInfo",
                businessLogMap);
    }

    @Override
    @BizLoggable(logAction = "PRODUCTION_ERROR", logEntityName = "CARD")
    public String cardProductionError(String requestID, String errorCode,
                                      String description) throws BaseException {
        try {
            CardRequestDAO cardRequestDAO = getCardRequestDAO();
            CardRequestHistoryDAO cardRequestHistoryDAO = getCardRequestHistoryDAO();

            // CardRequestHistoryTO cardRequestHistoryTO =
            // cardRequestHistoryDAO.findByCmsRequestId(requestID);
            // if (cardRequestHistoryTO == null) {
            // throw new ServiceException(BizExceptionCode.DPI_027,
            // BizExceptionCode.DPI_027_MSG, (requestID).split(","));
            // }
            cmsDispatchLogger.info("Loading card request by cms request id {}",
                    requestID);
            CardRequestTO cardRequestTO = cardRequestDAO
                    .findByCmsRequestId(requestID);

            if (cardRequestTO != null) {
                if (!CardRequestState.PENDING_ISSUANCE.equals(cardRequestTO
                        .getState())) {
                    cmsDispatchLogger
                            .error("Card request {} is not in PENDING_ISSUANCE state. Its current state is {}",
                                    requestID, cardRequestTO.getState());
                    throw new ServiceException(
                            BizExceptionCode.DPI_028,
                            BizExceptionCode.DPI_028_MSG,
                            (requestID + ","
                                    + CardRequestState.PENDING_ISSUANCE + "," + cardRequestTO
                                    .getState()).split(","));
                }

                // List<String> cmsRequestIds = new ArrayList<String>();
                // cmsRequestIds.add(requestID);

                // cardRequestDAO.updateCardRequestsByRequestIdOfHistory(cmsRequestIds,
                // CardRequestState.CMS_PRODUCTION_ERROR, SystemId.CMS);
                cmsDispatchLogger
                        .info("Changing state of card request {} from PENDING_ISSUANCE to CMS_PRODUCTION_ERROR",
                                requestID);
                cardRequestTO.setState(CardRequestState.CMS_PRODUCTION_ERROR);
                cardRequestDAO.update(cardRequestTO);

                String result = ("FAILURE-"
                        + CardRequestState.CMS_PRODUCTION_ERROR + ":"
                        + errorCode + ":" + description.trim());
                if (result.length() >= 250) {
                    result = StringUtils.substring(result, 0, 250);
                    result = result.substring(0, 250);
                }
                // cardRequestHistoryDAO.bulkUpdateCardRequestHistoryBySubSystemRequestId(cmsRequestIds,
                // result);
                cmsDispatchLogger
                        .info("Adding a record in history of card request {} indicating production error",
                                requestID);
                cardRequestHistoryDAO.create(cardRequestTO, result,
                        SystemId.CMS, requestID,
                        CardRequestHistoryAction.PRODUCTION_ERROR, null);
            } else {
                throw new ServiceException(BizExceptionCode.DPI_056,
                        BizExceptionCode.DPI_056_MSG);
            }
        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (Exception e) {
            sessionContext.setRollbackOnly();
            throw new ServiceException(BizExceptionCode.DPI_020,
                    BizExceptionCode.GLB_008_MSG, e);
        }

        Map<Object, Object> businessLogMap = new HashMap<Object, Object>();
        businessLogMap.put("cmsRequestId", requestID);
        return EmsUtil.convertStringToJSONFormat("cardProductionErrorInfo",
                businessLogMap);
    }

    @Override
    //@Permissions(value = "ems_notifyItemLost")
    @Permissions(value = "ems_cardLost")
    @BizLoggable(logAction = "LOST", logEntityName = "DISPATCH")
    public void itemLost(String ids, String detailIds, String cardIds)
            throws BaseException {
        DispatchDAO dispatchDAO = getDispatchDAO();
        CardDAO cardDAO = getCardDAO();

        try {
            if ((ids != null && ids.trim().length() != 0)
                    || (detailIds != null && detailIds.trim().length() != 0)
                    || (cardIds != null && cardIds.trim().length() != 0)) {
                DispatchInfoTO dispatchInfoTO;
                CardTO cardTO;
                List<DispatchInfoTO> dispatchInfoTOs;
                List<CardTO> cardTOs;

                if (EmsUtil.checkString(detailIds)
                        && !EmsUtil.checkString(cardIds)) {
                    dispatchInfoTO = (DispatchInfoTO) dispatchDAO.find(
                            DispatchInfoTO.class, Long.valueOf(detailIds));
                    cardTOs = cardDAO.findByBatchId(dispatchInfoTO
                            .getContainerId());

                    if (dispatchInfoTO.getSendDate() != null
                            || dispatchInfoTO.getReceiveDate() != null
                            || dispatchInfoTO.getLostDate() != null
                            || dispatchInfoTO.getDetailReceiveDate() == null
                            || dispatchInfoTO.getDetailLostDate() != null) {
                        throw new ServiceException(BizExceptionCode.DPI_052,
                                BizExceptionCode.DPI_038_MSG);
                    } else {
                        for (CardTO to : cardTOs) {
                            if (to.getReceiveDate() != null
                                    && to.getReceiveDate() != null)
                                throw new ServiceException(
                                        BizExceptionCode.DPI_053,
                                        BizExceptionCode.DPI_039_MSG);
                        }
                    }
                } else if (!EmsUtil.checkString(detailIds)
                        && EmsUtil.checkString(cardIds)) {
                    // cardTO = cardDAO.find(CardTO.class,
                    // Long.valueOf(cardIds));
                    // dispatchInfoTOs =
                    // dispatchDAO.findByContainerId(cardTO.getBatch().getId());
                    // dispatchInfoTO = dispatchInfoTOs.get(0);
                    //
                    // if (cardTO.getReceiveDate() != null &&
                    // cardTO.getLostDate() != null)
                    // throw new ServiceException(BizExceptionCode.DPI_040,
                    // BizExceptionCode.DPI_039_MSG);
                    // else if (dispatchInfoTO.getSendDate() == null
                    // || !((dispatchInfoTO.getReceiveDate() == null &&
                    // dispatchInfoTO.getLostDate() != null)
                    // || (dispatchInfoTO.getReceiveDate() != null &&
                    // dispatchInfoTO.getLostDate() == null)
                    // || (dispatchInfoTO.getReceiveDate() == null &&
                    // dispatchInfoTO.getLostDate() == null))
                    // || dispatchInfoTO.getDetailReceiveDate() == null
                    // || dispatchInfoTO.getDetailLostDate() != null)
                    // throw new ServiceException(BizExceptionCode.DPI_041,
                    // BizExceptionCode.DPI_038_MSG);
                }
                Long personID = getPersonDAO().findPersonIdByUsername(userProfileTO.getUserName());
                getDispatchDAO().itemLost(personID, ids,
                        detailIds, cardIds);
            } else {
                throw new ServiceException(BizExceptionCode.DPI_003,
                        BizExceptionCode.DPI_014_MSG);
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DPI_031,
                    BizExceptionCode.GLB_008_MSG, e);
        }

    }

    @Override
    @Permissions(value = "ems_notifyItemFound")
    @BizLoggable(logAction = "FOUND", logEntityName = "DISPATCH")
    public void itemFound(String ids, String detailIds, String cardIds)
            throws BaseException {
        DispatchDAO dispatchDAO = getDispatchDAO();
        CardDAO cardDAO = getCardDAO();

        try {
            if ((ids != null && ids.trim().length() != 0)
                    || (detailIds != null && detailIds.trim().length() != 0)
                    || (cardIds != null && cardIds.trim().length() != 0)) {
                DispatchInfoTO dispatchInfoTO;
                CardTO cardTO;
                List<DispatchInfoTO> dispatchInfoTOs;
                List<CardTO> cardTOs;

                if (EmsUtil.checkString(detailIds)
                        && !EmsUtil.checkString(cardIds)) {
                    dispatchInfoTO = (DispatchInfoTO) dispatchDAO.find(
                            DispatchInfoTO.class, Long.valueOf(detailIds));
                    cardTOs = cardDAO.findByBatchId(dispatchInfoTO
                            .getContainerId());

                    if (dispatchInfoTO.getSendDate() != null
                            || dispatchInfoTO.getReceiveDate() != null
                            || dispatchInfoTO.getLostDate() != null
                            || dispatchInfoTO.getDetailReceiveDate() == null
                            || dispatchInfoTO.getDetailLostDate() == null) {
                        throw new ServiceException(BizExceptionCode.DPI_054,
                                BizExceptionCode.DPI_038_MSG);
                    } else {
                        for (CardTO to : cardTOs) {
                            if (to.getReceiveDate() != null
                                    && to.getReceiveDate() != null)
                                throw new ServiceException(
                                        BizExceptionCode.DPI_055,
                                        BizExceptionCode.DPI_039_MSG);
                        }
                    }
                } else if (!EmsUtil.checkString(detailIds)
                        && EmsUtil.checkString(cardIds)) {
                    // cardTO = cardDAO.find(CardTO.class,
                    // Long.valueOf(cardIds));
                    // dispatchInfoTOs =
                    // dispatchDAO.findByContainerId(cardTO.getBatch().getId());
                    // dispatchInfoTO = dispatchInfoTOs.get(0);
                    //
                    // if (cardTO.getReceiveDate() != null &&
                    // cardTO.getLostDate() != null)
                    // throw new ServiceException(BizExceptionCode.DPI_040,
                    // BizExceptionCode.DPI_039_MSG);
                    // else if (dispatchInfoTO.getSendDate() == null
                    // || !((dispatchInfoTO.getReceiveDate() == null &&
                    // dispatchInfoTO.getLostDate() != null)
                    // || (dispatchInfoTO.getReceiveDate() != null &&
                    // dispatchInfoTO.getLostDate() == null)
                    // || (dispatchInfoTO.getReceiveDate() == null &&
                    // dispatchInfoTO.getLostDate() == null))
                    // || dispatchInfoTO.getDetailReceiveDate() == null
                    // || dispatchInfoTO.getDetailLostDate() != null)
                    // throw new ServiceException(BizExceptionCode.DPI_041,
                    // BizExceptionCode.DPI_038_MSG);
                }
                Long personID = getPersonDAO().findPersonIdByUsername(userProfileTO.getUserName());
                getDispatchDAO().itemFound(personID, ids,
                        detailIds, cardIds);
            } else {
                throw new ServiceException(BizExceptionCode.DPI_004,
                        BizExceptionCode.DPI_014_MSG);
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DPI_032,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_notifyItemReceived")
    @BizLoggable(logAction = "RECEIVED", logEntityName = "DISPATCH")
    public void itemReceived(String ids, String detailIds, String cardIds)
            throws BaseException {
        DispatchDAO dispatchDAO = getDispatchDAO();
        CardDAO cardDAO = getCardDAO();

        try {
            if ((ids != null && ids.trim().length() != 0)
                    || (detailIds != null && detailIds.trim().length() != 0)
                    || (cardIds != null && cardIds.trim().length() != 0)) {

                DispatchInfoTO dispatchInfoTO;
                CardTO cardTO;
                List<DispatchInfoTO> dispatchInfoTOs;
                List<CardTO> cardTOs;

                if (EmsUtil.checkString(detailIds)
                        && !EmsUtil.checkString(cardIds)) {
                    dispatchInfoTO = (DispatchInfoTO) dispatchDAO.find(
                            DispatchInfoTO.class, Long.valueOf(detailIds));
                    cardTOs = cardDAO.findByBatchId(dispatchInfoTO
                            .getContainerId());

                    if (dispatchInfoTO.getSendDate() != null
                            || dispatchInfoTO.getReceiveDate() != null
                            || dispatchInfoTO.getLostDate() != null
                            || dispatchInfoTO.getDetailReceiveDate() != null
                            || dispatchInfoTO.getDetailLostDate() != null) {
                        throw new ServiceException(BizExceptionCode.DPI_038,
                                BizExceptionCode.DPI_038_MSG);
                    } else {
                        for (CardTO to : cardTOs) {
                            if (to.getReceiveDate() != null
                                    && to.getReceiveDate() != null)
                                throw new ServiceException(
                                        BizExceptionCode.DPI_039,
                                        BizExceptionCode.DPI_039_MSG);
                        }
                    }
                } else if (!EmsUtil.checkString(detailIds)
                        && EmsUtil.checkString(cardIds)) {
                    cardTO = cardDAO.find(CardTO.class, Long.valueOf(cardIds));
                    dispatchInfoTOs = dispatchDAO.findByContainerId(cardTO
                            .getBatch().getId());
                    dispatchInfoTO = dispatchInfoTOs.get(0);

                    if (cardTO.getReceiveDate() != null
                            && cardTO.getLostDate() != null)
                        throw new ServiceException(BizExceptionCode.DPI_040,
                                BizExceptionCode.DPI_039_MSG);
                    else if (dispatchInfoTO.getSendDate() == null
                            // || !((dispatchInfoTO.getReceiveDate() == null &&
                            // dispatchInfoTO.getLostDate() != null)
                            // || (dispatchInfoTO.getReceiveDate() != null &&
                            // dispatchInfoTO.getLostDate() == null)
                            // || (dispatchInfoTO.getReceiveDate() == null &&
                            // dispatchInfoTO.getLostDate() == null))
                            || (dispatchInfoTO.getReceiveDate() != null && dispatchInfoTO
                            .getLostDate() != null)
                            || dispatchInfoTO.getDetailReceiveDate() == null
                            || dispatchInfoTO.getDetailLostDate() != null)
                        throw new ServiceException(BizExceptionCode.DPI_041,
                                BizExceptionCode.DPI_038_MSG);
                }
                Long personID = getPersonDAO().findPersonIdByUsername(userProfileTO.getUserName());
                dispatchDAO.itemReceived(personID, ids,
                        detailIds, cardIds);

            } else {
                throw new ServiceException(BizExceptionCode.DPI_005,
                        BizExceptionCode.DPI_014_MSG);
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DPI_033,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    //@Permissions(value = "ems_notifyItemNotReceived")
    @Permissions(value = "ems_itemNotReceived")
    @BizLoggable(logAction = "NOT_RECEIVED", logEntityName = "DISPATCH")
    public void itemNotReceived(String ids, String detailIds, String cardIds)
            throws BaseException {
        DispatchDAO dispatchDAO = getDispatchDAO();
        CardDAO cardDAO = getCardDAO();

        try {
            if ((ids != null && ids.trim().length() != 0)
                    || (detailIds != null && detailIds.trim().length() != 0)
                    || (cardIds != null && cardIds.trim().length() != 0)) {
                DispatchInfoTO dispatchInfoTO;
                CardTO cardTO;
                List<DispatchInfoTO> dispatchInfoTOs;
                List<CardTO> cardTOs;

                if (EmsUtil.checkString(detailIds)
                        && !EmsUtil.checkString(cardIds)) {
                    dispatchInfoTO = (DispatchInfoTO) dispatchDAO.find(
                            DispatchInfoTO.class, Long.valueOf(detailIds));
                    cardTOs = cardDAO.findByBatchId(dispatchInfoTO
                            .getContainerId());
                    // Anbari
                    if (dispatchInfoTO.getSendDate() != null
                            || (dispatchInfoTO.getReceiveDate() != null && dispatchInfoTO
                            .getLostDate() != null)
                            || dispatchInfoTO.getLostDate() != null
                            || dispatchInfoTO.getDetailReceiveDate() != null
                            || dispatchInfoTO.getDetailLostDate() != null) {
                        throw new ServiceException(BizExceptionCode.DPI_048,
                                BizExceptionCode.DPI_038_MSG);
                    } else {
                        for (CardTO to : cardTOs) {
                            if (to.getReceiveDate() != null
                                    && to.getLostDate() != null)
                                throw new ServiceException(
                                        BizExceptionCode.DPI_049,
                                        BizExceptionCode.DPI_039_MSG);
                        }
                    }
                } else if (!EmsUtil.checkString(detailIds)
                        && EmsUtil.checkString(cardIds)) {
                    cardTO = cardDAO.find(CardTO.class, Long.valueOf(cardIds));
                    dispatchInfoTOs = dispatchDAO.findByContainerId(cardTO
                            .getBatch().getId());
                    dispatchInfoTO = dispatchInfoTOs.get(0);

                    if (cardTO.getReceiveDate() != null
                            && cardTO.getLostDate() != null)
                        throw new ServiceException(BizExceptionCode.DPI_050,
                                BizExceptionCode.DPI_039_MSG);
                    else if (dispatchInfoTO.getSendDate() == null
                            // || dispatchInfoTO.getReceiveDate() !=
                            // null//commented By Adldoost
                            || dispatchInfoTO.getLostDate() != null
                            || dispatchInfoTO.getDetailReceiveDate() == null
                            || dispatchInfoTO.getDetailLostDate() != null)
                        throw new ServiceException(BizExceptionCode.DPI_051,
                                BizExceptionCode.DPI_038_MSG);
                }
                Long personID = getPersonDAO().findPersonIdByUsername(userProfileTO.getUserName());
                getDispatchDAO().itemNotReceived(personID,
                        ids, detailIds, cardIds);
            } else {
                throw new ServiceException(BizExceptionCode.DPI_006,
                        BizExceptionCode.DPI_014_MSG);
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DPI_034,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_notifyItemSent")
    @BizLoggable(logAction = "SENT", logEntityName = "DISPATCH")
    public void itemSent(String ids, String detailIds) throws BaseException {
        DispatchDAO dispatchDAO = getDispatchDAO();
        CardDAO cardDAO = getCardDAO();

        try {
            if ((ids != null && ids.trim().length() != 0)
                    || (detailIds != null && detailIds.trim().length() != 0)) {
                DispatchInfoTO dispatchInfoTO;
                List<CardTO> cardTOs;

                dispatchInfoTO = (DispatchInfoTO) dispatchDAO.find(
                        DispatchInfoTO.class, Long.valueOf(detailIds));
                cardTOs = cardDAO
                        .findByBatchId(dispatchInfoTO.getContainerId());

                if (dispatchInfoTO.getSendDate() != null
                        || dispatchInfoTO.getReceiveDate() != null
                        || dispatchInfoTO.getLostDate() != null
                        || dispatchInfoTO.getDetailReceiveDate() == null
                        || dispatchInfoTO.getDetailLostDate() != null) {
                    throw new ServiceException(BizExceptionCode.DPI_046,
                            BizExceptionCode.DPI_038_MSG);
                } else {
                    for (CardTO to : cardTOs) {
                        if (to.getReceiveDate() != null
                                || to.getLostDate() != null)
                            throw new ServiceException(
                                    BizExceptionCode.DPI_047,
                                    BizExceptionCode.DPI_039_MSG);
                    }
                }

                Long personID = getPersonDAO().findPersonIdByUsername(userProfileTO.getUserName());
                getDispatchDAO().itemSent(personID, ids,
                        detailIds);
            } else {
                throw new ServiceException(BizExceptionCode.DPI_007,
                        BizExceptionCode.DPI_014_MSG);
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DPI_035,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_notifyUndoDispatch")
    @BizLoggable(logAction = "UNDO", logEntityName = "DISPATCH")
    public void undoSend(String ids, String detailIds) throws BaseException {
        DispatchDAO dispatchDAO = getDispatchDAO();
        CardDAO cardDAO = getCardDAO();

        try {
            DispatchInfoTO dispatchInfoTO;
            List<CardTO> cardTOs;

            if ((ids != null && ids.trim().length() != 0)
                    || (detailIds != null && detailIds.trim().length() != 0)) {
                dispatchInfoTO = (DispatchInfoTO) dispatchDAO.find(
                        DispatchInfoTO.class, Long.valueOf(detailIds));
                cardTOs = cardDAO
                        .findByBatchId(dispatchInfoTO.getContainerId());

                if (dispatchInfoTO.getSendDate() == null
                        || dispatchInfoTO.getReceiveDate() != null
                        || dispatchInfoTO.getLostDate() != null
                        || dispatchInfoTO.getDetailReceiveDate() == null
                        || dispatchInfoTO.getDetailLostDate() != null) {
                    throw new ServiceException(BizExceptionCode.DPI_046,
                            BizExceptionCode.DPI_038_MSG);
                } else {
                    for (CardTO to : cardTOs) {
                        // if (!((to.getReceiveDate() == null &&
                        // to.getLostDate() != null)
                        // || (to.getReceiveDate() != null && to.getLostDate()
                        // == null)
                        // || (to.getReceiveDate() == null && to.getLostDate()
                        // == null)))
                        if (to.getReceiveDate() != null
                                || to.getLostDate() != null)
                            throw new ServiceException(
                                    BizExceptionCode.DPI_047,
                                    BizExceptionCode.DPI_039_MSG);
                    }
                }

                Long personID = getPersonDAO().findPersonIdByUsername(userProfileTO.getUserName());
                getDispatchDAO().undoSend(personID, ids,
                        detailIds);
            } else {
                throw new ServiceException(BizExceptionCode.DPI_008,
                        BizExceptionCode.DPI_014_MSG);
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DPI_036,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    @Permissions(value = "ems_notifyUndoDispatch")
    @BizLoggable(logAction = "RESET", logEntityName = "DISPATCH")
    public void backToInitialState(String ids, String detailIds, String cardIds)
            throws BaseException {
        DispatchDAO dispatchDAO = getDispatchDAO();
        CardDAO cardDAO = getCardDAO();

        try {
            if ((ids != null && ids.trim().length() != 0)
                    || (detailIds != null && detailIds.trim().length() != 0)
                    || (cardIds != null && cardIds.trim().length() != 0)) {

                DispatchInfoTO dispatchInfoTO;
                CardTO cardTO;
                List<DispatchInfoTO> dispatchInfoTOs;
                List<CardTO> cardTOs;

                if (EmsUtil.checkString(detailIds)
                        && !EmsUtil.checkString(cardIds)) {
                    dispatchInfoTO = (DispatchInfoTO) dispatchDAO.find(
                            DispatchInfoTO.class, Long.valueOf(detailIds));
                    cardTOs = cardDAO.findByBatchId(dispatchInfoTO
                            .getContainerId());

                    if (dispatchInfoTO.getSendDate() != null
                            || dispatchInfoTO.getReceiveDate() != null
                            || dispatchInfoTO.getLostDate() != null
                            || (dispatchInfoTO.getDetailReceiveDate() != null && dispatchInfoTO
                            .getDetailLostDate() != null)) {
                        throw new ServiceException(BizExceptionCode.DPI_042,
                                BizExceptionCode.DPI_038_MSG);
                    } else {
                        for (CardTO to : cardTOs) {
                            if (to.getReceiveDate() != null
                                    || to.getLostDate() != null)
                                throw new ServiceException(
                                        BizExceptionCode.DPI_043,
                                        BizExceptionCode.DPI_039_MSG);
                        }
                    }
                } else if (!EmsUtil.checkString(detailIds)
                        && EmsUtil.checkString(cardIds)) {
                    cardTO = cardDAO.find(CardTO.class, Long.valueOf(cardIds));

                    /**
                     * Getting inquiry from CMS in order to check whether or not
                     * it can be possible to back to the previous state. If the
                     * state of card equals to one of the states of ISSUED,
                     * SHIPPED or ARRIVED, the undo operation will be allowed,
                     * Otherwise, this action cannot be done.
                     */
                    CardInfoVTO cardInfoVTO = getCMSService().getCardInfo(cardTO.getCrn());
                    if (cardInfoVTO != null) {
                        int currentCardState = cardInfoVTO.getStatus();
                        logger.info("\nCMS getCardInfo return value: CRN: "
                                + cardInfoVTO.getCrn() + ", Status: "
                                + cardInfoVTO.getStatus());
                        cmsDispatchLogger
                                .info("\nCMS getCardInfo return value: CRN: "
                                        + cardInfoVTO.getCrn() + ", Status: "
                                        + cardInfoVTO.getStatus());
                        if (CMSCardState.ISSUED.getCmsCardState() != currentCardState
                                && CMSCardState.SHIPPED.getCmsCardState() != currentCardState
                                && CMSCardState.ARRIVED.getCmsCardState() != currentCardState) {
                            throw new ServiceException(
                                    BizExceptionCode.DPI_058,
                                    BizExceptionCode.DPI_058_MSG);
                        }
                    }

                    dispatchInfoTOs = dispatchDAO.findByContainerId(cardTO
                            .getBatch().getId());
                    dispatchInfoTO = dispatchInfoTOs.get(0);

                    if (!((cardTO.getReceiveDate() == null && cardTO
                            .getLostDate() != null) || (cardTO.getReceiveDate() != null && cardTO
                            .getLostDate() == null)))
                        throw new ServiceException(BizExceptionCode.DPI_040,
                                BizExceptionCode.DPI_039_MSG);
                    else if (dispatchInfoTO.getSendDate() == null
                            // || !((dispatchInfoTO.getReceiveDate() == null &&
                            // dispatchInfoTO.getLostDate() != null)
                            // || (dispatchInfoTO.getReceiveDate() != null &&
                            // dispatchInfoTO.getLostDate() == null)
                            // || (dispatchInfoTO.getReceiveDate() == null &&
                            // dispatchInfoTO.getLostDate() == null))
                            || (dispatchInfoTO.getReceiveDate() != null && dispatchInfoTO
                            .getLostDate() != null)
                            || dispatchInfoTO.getDetailReceiveDate() == null
                            || dispatchInfoTO.getDetailLostDate() != null)
                        throw new ServiceException(BizExceptionCode.DPI_041,
                                BizExceptionCode.DPI_038_MSG);
                }

                Long personID = getPersonDAO().findPersonIdByUsername(userProfileTO.getUserName());

                getDispatchDAO().backToInitialState(
                        personID, ids, detailIds, cardIds);
            } else {
                throw new ServiceException(BizExceptionCode.DPI_009,
                        BizExceptionCode.DPI_014_MSG);
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.DPI_036,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    private CardDAO getCardDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CARD));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.DPI_002,
                    BizExceptionCode.GLB_001_MSG, e,
                    new String[]{EMSLogicalNames.DAO_CARD});
        }
    }

    private PersonDAO getPersonDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        PersonDAO personDAO = null;
        try {
            personDAO = (PersonDAO) factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_PERSON));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.TMS_003,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_PERSON.split(","));
        }
        return personDAO;
    }

    private CardRequestDAO getCardRequestDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.DPI_015,
                    BizExceptionCode.GLB_001_MSG, e,
                    new String[]{EMSLogicalNames.DAO_CARD_REQUEST});
        }
    }

    private CardRequestHistoryDAO getCardRequestHistoryDAO()
            throws BaseException {
        try {
            return DAOFactoryProvider
                    .getDAOFactory()
                    .getDAO(EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST_HISTORY));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.DPI_015,
                    BizExceptionCode.GLB_001_MSG, e,
                    new String[]{EMSLogicalNames.DAO_CARD_REQUEST_HISTORY});
        }
    }

    // ================== Accessor =================

    private DispatchDAO getDispatchDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_DISPATCHING));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.DPI_001,
                    BizExceptionCode.GLB_001_MSG, e,
                    new String[]{EMSLogicalNames.DAO_DISPATCHING});
        }
    }

    private CMSService getCMSService() throws BaseException {
        CMSService cmsService;
        try {
            cmsService = ServiceFactoryProvider
                    .getServiceFactory()
                    .getService(
                            EMSLogicalNames
                                    .getExternalServiceJNDIName(EMSLogicalNames.SRV_CMS), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.DPI_057,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_CMS.split(","));
        }
        cmsService.setUserProfileTO(getUserProfileTO());
        return cmsService;
    }


    @Override
    public Integer countBatchDispatchList(HashMap parameters,
                                          UserProfileTO userProfileTO) throws BaseException {
        return getDispatchDAO().countBatchDispatchList(parameters,
                userProfileTO);
    }

    @Override
    public List<DispatchInfoWTO> fetchBatchDispatchList(
            GeneralCriteria criteria, UserProfileTO userProfileTO)
            throws BaseException {
        return getDispatchDAO().fetchBatchDispatchList(criteria, userProfileTO);
    }
}
