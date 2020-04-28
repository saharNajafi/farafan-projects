package com.gam.nocr.ems.data.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.EnrollmentOfficeDAO;
import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.BatchTO;
import com.gam.nocr.ems.data.domain.BoxTO;
import com.gam.nocr.ems.data.domain.CardContainerTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CardTO;
import com.gam.nocr.ems.data.domain.DispatchInfoTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.ws.CitizenWTO;
import com.gam.nocr.ems.data.domain.ws.DispatchInfoWTO;
import com.gam.nocr.ems.data.enums.BatchState;
import com.gam.nocr.ems.data.enums.BoxState;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.CardState;
import com.gam.nocr.ems.data.enums.DepartmentDispatchSendType;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeDeliverStatus;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeType;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * <p>
 * TODO -- Explain this class
 * </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Stateless(name = "DispatchDAO")
@Local(DispatchDAOLocal.class)
@Remote(DispatchDAORemote.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class DispatchDAOImpl extends EmsBaseDAOImpl<CardContainerTO> implements
        DispatchDAOLocal, DispatchDAORemote {

    private static final String UNIQUE_KEY_BOX_CMS_ID = "AK_BOX_CMS_ID";
    private static final String UNIQUE_KEY_BATCH_CMS_ID = "AK_BATCH_CMS_ID";
    private static final String UNIQUE_KEY_CARD_CRN = "AK_CARD_CRN";
    private static final String UNIQUE_KEY_DPI_CONTNER_ID_TYP_SNDER = "AK_DPI_CONTNER_ID_TYP_SNDER";
    private static final String UNIQUE_KEY_POSTAL_TRACKING_CODE = "AK_BATCH_POSTAL_TRACKING";

    private static final Logger logger = BaseLog.getLogger(DispatchDAOImpl.class);
    private static final Logger cmsDispatchLogger = BaseLog.getLogger("cmsdispatch");

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * The create method, handles all the save operations for all the classes
     * which are extended from EntityTO
     *
     * @param cardContainerTO - the object of type EntityTO to create
     * @return the object of type EntityTo
     */
    @Override
    public CardContainerTO create(CardContainerTO cardContainerTO)
            throws BaseException {


        try {
            CardContainerTO to = super.create(cardContainerTO);
            em.flush();
            //By Adldoost
            if (cardContainerTO instanceof BoxTO) {
                if (((BoxTO) cardContainerTO).getState() == BoxState.SHIPPED) {
                    List<Long> ids = new ArrayList<Long>();
                    ids.add(cardContainerTO.getId());
                    notifyBoxShipped(ids);
                }
                if (((BoxTO) cardContainerTO).getState() == BoxState.MISSED) {
                    List<Long> ids = new ArrayList<Long>();
                    ids.add(cardContainerTO.getId());
                    notifyBoxMissed(ids);
                }
            }
            ////////////
            return to;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(UNIQUE_KEY_BOX_CMS_ID))
                throw new DAOException(DataExceptionCode.DSI_025,
                        DataExceptionCode.DSI_025_MSG, e);
            if (err.contains(UNIQUE_KEY_BATCH_CMS_ID))
                throw new DAOException(DataExceptionCode.DSI_026,
                        DataExceptionCode.DSI_026_MSG, e);
            if (err.contains(UNIQUE_KEY_POSTAL_TRACKING_CODE))
                throw new DAOException(DataExceptionCode.DSI_082,
                        DataExceptionCode.DSI_082_MSG, e);
            else
                throw new DAOException(DataExceptionCode.DSI_028,
                        DataExceptionCode.DSI_028_MSG, e);
        }
    }

    public void updateBatches(BoxTO boxTO, List<String> batchIds)
            throws BaseException {
        logger.info("updateBatches for box : '{}' and batchIds : '{}'", boxTO, batchIds);
        try {
            em.createQuery(
                    "update BatchTO bat "
                            + "set bat.box = :box, bat.state = :state"
                            + " where bat.cmsID in (:ids)")
                    .setParameter("box", boxTO)
                    .setParameter("state", BatchState.SHIPPED)
                    .setParameter("ids", batchIds).executeUpdate();
            em.flush();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(UNIQUE_KEY_BOX_CMS_ID))
                throw new DAOException(DataExceptionCode.DSI_029,
                        DataExceptionCode.DSI_025_MSG, e);
            if (err.contains(UNIQUE_KEY_BATCH_CMS_ID))
                throw new DAOException(DataExceptionCode.DSI_030,
                        DataExceptionCode.DSI_026_MSG, e);
            else
                throw new DAOException(DataExceptionCode.DSI_032,
                        DataExceptionCode.DSI_032_MSG, e);
        }
    }

    @Override
    public void updateBatchPostalTrackingCode(String batchId, String postalTrackingCode) throws BaseException {
        logger.info("updateBatchPostalTrackingCode for batchId : '{}' and postalTrackingCode : '{}'", batchId, postalTrackingCode);
        try {
            int affectedRecords = em.createQuery(
                    "update BatchTO bat "
                            + "set bat.postalTrackingCode = :postalTrackingCode"
                            + " where bat.cmsID = :batchId")
                    .setParameter("postalTrackingCode", postalTrackingCode)
                    .setParameter("batchId", batchId).executeUpdate();
            em.flush();

            if (affectedRecords == 0) {
                throw new DAOException(DataExceptionCode.DSI_080,
                        DataExceptionCode.DSI_080_MSG);
            }
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(UNIQUE_KEY_BATCH_CMS_ID))
                throw new DAOException(DataExceptionCode.DSI_081,
                        DataExceptionCode.DSI_081_MSG, e);
            if (err.contains(UNIQUE_KEY_POSTAL_TRACKING_CODE))
                throw new DAOException(DataExceptionCode.DSI_082,
                        DataExceptionCode.DSI_082_MSG, e);
            else
                throw new DAOException(DataExceptionCode.DSI_080,
                        DataExceptionCode.DSI_080_MSG, e);
        }
    }

    @Override
    public void itemLost(Long perId, String ids, String detailIds,
                         String cardIds) throws BaseException {
        logger.info("itemLost for ids : '{}' and cardIds : '{}'", ids, cardIds);
        if (ids == null || ids.trim().length() == 0)
            ids = null;
        String mids = (ids == null) ? detailIds : ids;
        if (mids != null) {
            List<Long> lids = new ArrayList<Long>();
            for (String id : mids.split(","))
                lids.add(Long.parseLong(id.trim()));

            try {
                em.createQuery(
                        "update DispatchInfoTO dpi "
                                + ((ids != null) ? "set dpi.lostDate = current_date "
                                : "set dpi.detailLostDate = current_date ")
                                + " where dpi.id in (:ids)")
                        .setParameter("ids", lids).executeUpdate();
                em.flush();
                // By Adldoost
                notifyCardContainerLost(lids, true);//ids are dispatch table records ids
            } catch (Exception e) {
                String err = e.getMessage();
                if (e.getCause() != null) {
                    if (e.getCause().getCause() != null)
                        err = e.getCause().getCause().getMessage();
                    else
                        err = e.getCause().getMessage();
                }
                if (err.contains(UNIQUE_KEY_DPI_CONTNER_ID_TYP_SNDER))
                    throw new DAOException(DataExceptionCode.DSI_001,
                            DataExceptionCode.DSI_001_MSG, e);
                else
                    throw new DAOException(DataExceptionCode.DSI_014,
                            DataExceptionCode.DSI_003_MSG, e);
            }
        }
        if (cardIds != null) {
            List<Long> lids = new ArrayList<Long>();
            for (String id : cardIds.split(","))
                lids.add(Long.parseLong(id.trim()));

            try {
                em.createQuery(
                        "update CardTO crd "
                                + "set crd.lostDate = current_date "
                                + " where crd.id in (:ids)")
                        .setParameter("ids", lids).executeUpdate();
                em.flush();
                undoNotifyCardsReceived(lids);
                notifyCardLost(lids);
            } catch (Exception e) {
                String err = e.getMessage();
                if (e.getCause() != null) {
                    if (e.getCause().getCause() != null)
                        err = e.getCause().getCause().getMessage();
                    else
                        err = e.getCause().getMessage();
                }
                if (err.contains(UNIQUE_KEY_CARD_CRN))
                    throw new DAOException(DataExceptionCode.DSI_002,
                            DataExceptionCode.DSI_002_MSG, e);
                else
                    throw new DAOException(DataExceptionCode.DSI_015,
                            DataExceptionCode.DSI_004_MSG, e);
            }
        }
    }

    @Override
    public void itemFound(Long perId, String ids, String detailIds,
                          String cardIds) throws BaseException {
        logger.info("itemFound for ids : '{}' and cardIds : '{}'", ids, cardIds);
        if (ids == null || ids.trim().length() == 0)
            ids = null;
        String mids = (ids == null) ? detailIds : ids;
        if (mids != null) {
            List<Long> lids = new ArrayList<Long>();
            for (String id : mids.split(","))
                lids.add(Long.parseLong(id.trim()));

            try {
                em.createQuery(
                        "update DispatchInfoTO dpi "
                                + ((ids != null) ? "set dpi.lostDate = null "
                                : "set dpi.detailLostDate = null ")
                                + " where dpi.id in (:ids)")
                        .setParameter("ids", lids).executeUpdate();
                em.flush();
                // By Adldoost
                notifyCardContainerFound(lids, true);
            } catch (Exception e) {
                String err = e.getMessage();
                if (e.getCause() != null) {
                    if (e.getCause().getCause() != null)
                        err = e.getCause().getCause().getMessage();
                    else
                        err = e.getCause().getMessage();
                }
                if (err.contains(UNIQUE_KEY_DPI_CONTNER_ID_TYP_SNDER))
                    throw new DAOException(DataExceptionCode.DSI_005,
                            DataExceptionCode.DSI_001_MSG, e);
                else
                    throw new DAOException(DataExceptionCode.DSI_016,
                            DataExceptionCode.DSI_003_MSG, e);
            }
        }
        if (cardIds != null) {
            List<Long> lids = new ArrayList<Long>();
            for (String id : cardIds.split(","))
                lids.add(Long.parseLong(id.trim()));

            try {
                em.createQuery(
                        "update CardTO crd " + "set crd.lostDate =  null "
                                + " where crd.id in (:ids)")
                        .setParameter("ids", lids).executeUpdate();
                em.flush();
                notifyCardFound(lids);
            } catch (Exception e) {
                String err = e.getMessage();
                if (e.getCause() != null) {
                    if (e.getCause().getCause() != null)
                        err = e.getCause().getCause().getMessage();
                    else
                        err = e.getCause().getMessage();
                }
                if (err.contains(UNIQUE_KEY_CARD_CRN))
                    throw new DAOException(DataExceptionCode.DSI_006,
                            DataExceptionCode.DSI_002_MSG, e);
                else
                    throw new DAOException(DataExceptionCode.DSI_017,
                            DataExceptionCode.DSI_004_MSG, e);
            }
        }
    }

    @Override
    public void itemReceived(Long perId, String ids, String detailIds,
                             String cardIds) throws BaseException {
        logger.info("itemReceived for ids : '{}' and cardIds : '{}'", ids, cardIds);
        if (ids == null || ids.trim().length() == 0)
            ids = null;
        String mids = (ids == null) ? detailIds : ids;
        if (mids != null) {
            List<Long> lids = new ArrayList<Long>();
            for (String id : mids.split(","))
                lids.add(Long.parseLong(id.trim()));

            try {
                em.createQuery(
                        "update DispatchInfoTO dpi "
                                + ((ids != null) ? "set dpi.receiveDate = current_date "
                                : "set dpi.detailReceiveDate = current_date ")
                                + " where dpi.id in (:ids)")
                        .setParameter("ids", lids).executeUpdate();
                em.flush();
                notifyCardContainerReceived(lids);
            } catch (Exception e) {
                String err = e.getMessage();
                if (e.getCause() != null) {
                    if (e.getCause().getCause() != null)
                        err = e.getCause().getCause().getMessage();
                    else
                        err = e.getCause().getMessage();
                }
                if (err.contains(UNIQUE_KEY_DPI_CONTNER_ID_TYP_SNDER))
                    throw new DAOException(DataExceptionCode.DSI_007,
                            DataExceptionCode.DSI_001_MSG, e);
                else
                    throw new DAOException(DataExceptionCode.DSI_018,
                            DataExceptionCode.DSI_003_MSG, e);
            }
        }
        if (cardIds != null) {
            List<Long> lids = new ArrayList<Long>();
            for (String id : cardIds.split(","))
                lids.add(Long.parseLong(id.trim()));

            try {
                em.createQuery(
                        "update CardTO crd "
                                + "set crd.receiveDate = current_date "
                                + " where crd.id in (:ids)")
                        .setParameter("ids", lids).executeUpdate();
                em.flush();

                em.createQuery(
                        " update CardRequestTO crt "
                                + " set crt.state = :state "
                                + " where crt.card.id = :cardId")
                        .setParameter("state",
                                CardRequestState.READY_TO_DELIVER)
                        .setParameter("cardId", lids).executeUpdate();
                em.flush();
                notifyCardsReceived(lids);
            } catch (Exception e) {
                String err = e.getMessage();
                if (e.getCause() != null) {
                    if (e.getCause().getCause() != null)
                        err = e.getCause().getCause().getMessage();
                    else
                        err = e.getCause().getMessage();
                }
                if (err.contains(UNIQUE_KEY_CARD_CRN))
                    throw new DAOException(DataExceptionCode.DSI_008,
                            DataExceptionCode.DSI_002_MSG, e);
                else
                    throw new DAOException(DataExceptionCode.DSI_019,
                            DataExceptionCode.DSI_004_MSG, e);
            }
        }
    }

    @Override
    public void itemNotReceived(Long perId, String ids, String detailIds,
                                String cardIds) throws BaseException {
        logger.info("itemNotReceived for ids : '{}' and cardIds : '{}'", ids, cardIds);
        if (ids == null || ids.trim().length() == 0)
            ids = null;
        String mids = (ids == null) ? detailIds : ids;
        if (mids != null) {
            List<Long> lids = new ArrayList<Long>();
            for (String id : mids.split(","))
                lids.add(Long.parseLong(id.trim()));

            try {
                em.createQuery(
                        "update DispatchInfoTO dpi "
                                + ((ids != null) ? "set dpi.receiveDate = null, dpi.lostDate = current_date "
                                : "set dpi.detailReceiveDate = null, dpi.detailLostDate = current_date ")
                                + " where dpi.id in (:ids)")
                        .setParameter("ids", lids).executeUpdate();
                em.flush();
                //By Adldoost
                undoNotifyCardContainerReceived(lids);
            } catch (Exception e) {
                String err = e.getMessage();
                if (e.getCause() != null) {
                    if (e.getCause().getCause() != null)
                        err = e.getCause().getCause().getMessage();
                    else
                        err = e.getCause().getMessage();
                }
                if (err.contains(UNIQUE_KEY_DPI_CONTNER_ID_TYP_SNDER))
                    throw new DAOException(DataExceptionCode.DSI_009,
                            DataExceptionCode.DSI_001_MSG, e);
                else
                    throw new DAOException(DataExceptionCode.DSI_020,
                            DataExceptionCode.DSI_003_MSG, e);
            }
        }
        if (cardIds != null) {
            List<Long> lids = new ArrayList<Long>();
            for (String id : cardIds.split(","))
                lids.add(Long.parseLong(id.trim()));

            try {
                em.createQuery(
                        "update CardTO crd "
                                + "set crd.receiveDate = null,crd.lostDate = current_date "
                                + " where crd.id in (:ids)")
                        .setParameter("ids", lids).executeUpdate();
                em.flush();
                notifyCardLost(lids);
            } catch (Exception e) {
                String err = e.getMessage();
                if (e.getCause() != null) {
                    if (e.getCause().getCause() != null)
                        err = e.getCause().getCause().getMessage();
                    else
                        err = e.getCause().getMessage();
                }
                if (err.contains(UNIQUE_KEY_CARD_CRN))
                    throw new DAOException(DataExceptionCode.DSI_010,
                            DataExceptionCode.DSI_002_MSG, e);
                else
                    throw new DAOException(DataExceptionCode.DSI_021,
                            DataExceptionCode.DSI_004_MSG, e);
            }
        }

    }

    @Override
    public void itemSent(Long perId, String ids, String detailIds)
            throws BaseException {
        logger.info("itemSent for ids : '{}' ", ids);
        if (ids == null || ids.trim().length() == 0)
            ids = null;
        String mids = (ids == null) ? detailIds : ids;
        if (mids != null) {
            List<Long> lids = new ArrayList<Long>();
            for (String id : mids.split(","))
                lids.add(Long.parseLong(id.trim()));

            try {
                em.createQuery(
                        "update DispatchInfoTO dpi "
                                + "set dpi.sendDate = current_date "
                                + ((ids != null) ? " where dpi.dispatchParentId.id in (:ids) "
                                : " where dpi.id in (:ids) "))
                        .setParameter("ids", lids).executeUpdate();
                em.flush();
            } catch (Exception e) {
                String err = e.getMessage();
                if (e.getCause() != null) {
                    if (e.getCause().getCause() != null)
                        err = e.getCause().getCause().getMessage();
                    else
                        err = e.getCause().getMessage();
                }
                if (err.contains(UNIQUE_KEY_DPI_CONTNER_ID_TYP_SNDER))
                    throw new DAOException(DataExceptionCode.DSI_011,
                            DataExceptionCode.DSI_001_MSG, e);
                else
                    throw new DAOException(DataExceptionCode.DSI_022,
                            DataExceptionCode.DSI_003_MSG, e);
            }
        }
    }

    @Override
    public void undoSend(Long perId, String ids, String detailIds)
            throws BaseException {
        logger.info("undoSend for ids : '{}'", ids);
        if (ids == null || ids.trim().length() == 0)
            ids = null;
        String mids = (ids == null) ? detailIds : ids;
        if (mids != null) {
            List<Long> lids = new ArrayList<Long>();
            for (String id : mids.split(","))
                lids.add(Long.parseLong(id.trim()));

            try {
                em.createQuery(
                        "update DispatchInfoTO dpi "
                                + "set dpi.sendDate = "
                                + null
                                + ((ids != null) ? " where dpi.dispatchParentId.id in (:ids) "
                                : " where dpi.id in (:ids) "))
                        .setParameter("ids", lids).executeUpdate();
                em.flush();
            } catch (Exception e) {
                String err = e.getMessage();
                if (e.getCause() != null) {
                    if (e.getCause().getCause() != null)
                        err = e.getCause().getCause().getMessage();
                    else
                        err = e.getCause().getMessage();
                }
                if (err.contains(UNIQUE_KEY_DPI_CONTNER_ID_TYP_SNDER))
                    throw new DAOException(DataExceptionCode.DSI_012,
                            DataExceptionCode.DSI_001_MSG, e);
                else
                    throw new DAOException(DataExceptionCode.DSI_023,
                            DataExceptionCode.DSI_003_MSG, e);
            }
        }
    }

    @Override
    public void backToInitialState(Long perId, String ids, String detailIds,
                                   String cardIds) throws BaseException {
        logger.info("backToInitialState for ids : '{}'", ids);
        if (ids == null || ids.trim().length() == 0)
            ids = null;
        String mids = (ids == null) ? detailIds : ids;
        if (mids != null) {
            List<Long> lids = new ArrayList<Long>();
            for (String id : mids.split(","))
                lids.add(Long.parseLong(id.trim()));

            try {
                //By Adldoost
                //first we need to recognize how was card before it

                List<DispatchInfoTO> dispatches = em
                        .createQuery(
                                "select dispatch from DispatchInfoTO dispatch where dispatch.id in (:ID_LIST)",
                                DispatchInfoTO.class).setParameter("ID_LIST", lids)
                        .getResultList();

                List<Long> receivedDispatchesList = new ArrayList<Long>();
                List<Long> notReceivedDispatchesList = new ArrayList<Long>();
                for (DispatchInfoTO dis : dispatches) {
                    if (dis.getReceiveDate() != null)
                        receivedDispatchesList.add(dis.getId());
                    else if (dis.getLostDate() != null)
                        notReceivedDispatchesList.add(dis.getId());
                    else {
                        //TODO
                    }
                }

                ///////////////End of Adldoost code//////////////////////////////////

                em.createQuery(
                        "update DispatchInfoTO dpi "
                                + ((ids != null) ? "set dpi.receiveDate = null, dpi.lostDate = null "
                                : "set dpi.detailReceiveDate = null, dpi.detailLostDate = null ")
                                + " where dpi.id in (:ids)")
                        .setParameter("ids", lids).executeUpdate();
                em.flush();

                //By Adldoost
                undoNotifyCardContainerReceived(receivedDispatchesList);
                notifyCardContainerFound(notReceivedDispatchesList, true);
                /////
            } catch (Exception e) {
                String err = e.getMessage();
                if (e.getCause() != null) {
                    if (e.getCause().getCause() != null)
                        err = e.getCause().getCause().getMessage();
                    else
                        err = e.getCause().getMessage();
                }
                if (err.contains(UNIQUE_KEY_DPI_CONTNER_ID_TYP_SNDER))
                    throw new DAOException(DataExceptionCode.DSI_013,
                            DataExceptionCode.DSI_001_MSG, e);
                else
                    throw new DAOException(DataExceptionCode.DSI_024,
                            DataExceptionCode.DSI_003_MSG, e);
            }
        }
        if (cardIds != null) {
            List<Long> lids = new ArrayList<Long>();
            for (String id : cardIds.split(","))
                lids.add(Long.parseLong(id.trim()));


            try {
                //By Adldoost
                //first we need to recognize how was card before it

                List<CardTO> cards = em
                        .createQuery(
                                "select crd from CardTO crd where crd.id in (:ID_LIST)",
                                CardTO.class).setParameter("ID_LIST", lids)
                        .getResultList();

                List<Long> receviedCardsList = new ArrayList<Long>();
                List<Long> notReceivedCardsList = new ArrayList<Long>();
                for (CardTO card : cards) {
                    if (card.getReceiveDate() != null)
                        receviedCardsList.add(card.getId());
                    else if (card.getLostDate() != null)
                        notReceivedCardsList.add(card.getId());
                    else {
                        //This situation should not be happened!
                        throw new DAOException(DataExceptionCode.DSI_073, DataExceptionCode.DSI_073_MSG);
                    }
                }

                ///////////////End of Adldoost code//////////////////////////////////

                em.createQuery(
                        "update CardTO crd "
                                + "set crd.receiveDate = null, crd.lostDate = null "
                                + " where crd.id in (:ids)")
                        .setParameter("ids", lids).executeUpdate();
                em.flush();

                em.createQuery(
                        " update CardRequestTO crt "
                                + " set crt.state = :state "
                                + " where crt.card.id = :cardId")
                        .setParameter("state", CardRequestState.ISSUED)
                        .setParameter("cardId", lids).executeUpdate();
                em.flush();

                //By Adldoost
                undoNotifyCardsReceived(receviedCardsList);
                notifyCardFound(notReceivedCardsList);
                /////

            } catch (Exception e) {
                throw new DAOException(DataExceptionCode.DSI_042,
                        DataExceptionCode.DSI_004_MSG, e);
            }
        }
    }

    /**
     * The method updateBatchesState is use to update the state of the batches
     * in spite of their ids
     *
     * @param batchIdList a list of type Long which represents the ids for the specified
     *                    batches
     * @param batchState  an enum value of type
     *                    {@link com.gam.nocr.ems.data.enums.BatchState}
     */
    @Override
    public void updateBatchesState(List<Long> batchIdList, BatchState batchState)
            throws BaseException {
        logger.info("updateBatchesState for batchIds : '{}' and batchState : '{}'", batchIdList, batchState);
        try {
            em.createQuery(
                    "UPDATE BatchTO bat " + "SET bat.state = :BATCH_STATE "
                            + "WHERE bat.id in (:ID_LIST)")
                    .setParameter("BATCH_STATE", batchState)
                    .setParameter("ID_LIST", batchIdList).executeUpdate();
            em.flush();
            // By Adldoost
            if (batchState == BatchState.MISSED) {
                notifyBatchMissed(batchIdList);
            }
            /////
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(UNIQUE_KEY_BATCH_CMS_ID)) {
                throw new DAOException(DataExceptionCode.DSI_038,
                        DataExceptionCode.DSI_026_MSG, e);
            } else {
                throw new DataException(DataExceptionCode.DSI_034,
                        DataExceptionCode.GLB_006_MSG, e);
            }
        }
    }

    /**
     * The method updateBoxesState is use to update the state of the batches in
     * spite of their ids
     *
     * @param boxIdList a list of type Long which represents the ids for the specified
     *                  boxes
     * @param boxState  an enum value of type
     *                  {@link com.gam.nocr.ems.data.enums.BoxState}
     */
    @Override
    public void updateBoxesState(List<Long> boxIdList, BoxState boxState)
            throws BaseException {
        logger.info("updateBoxesState for boxIds : '{}' and boxState : '{}'", boxIdList, boxState);
        try {
            em.createQuery(
                    "update BoxTO bxt " + "set bxt.state = :BOX_STATE"
                            + " where bxt.id in (:ID_LIST)")
                    .setParameter("BOX_STATE", boxState)
                    .setParameter("ID_LIST", boxIdList).executeUpdate();
            em.flush();
            // By : Adldoost
            if (boxState == BoxState.MISSED) {
                notifyBoxMissed(boxIdList);
            } else if (boxState == BoxState.SHIPPED) {
                notifyBoxShipped(boxIdList);
            }
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("AK_BOX_CMS_ID")) {
                throw new DAOException(DataExceptionCode.DSI_039,
                        DataExceptionCode.DSI_026_MSG, e);
            } else {
                throw new DataException(DataExceptionCode.DSI_036,
                        DataExceptionCode.GLB_006_MSG, e);
            }
        }
    }

    /**
     * The method findReceivedBatches is used to find a specified instances of
     * type {@link com.gam.nocr.ems.data.domain.BatchTO} in order to understand
     * which one of them were received
     *
     * @return a list of type {@link com.gam.nocr.ems.data.domain.BatchTO}
     */
    @Override
    public List<BatchTO> findReceivedBatches(Integer from, Integer to)
            throws BaseException {
        String TIME_INTERVAL_DEFAULT_VALUE = "0";
        String timeInterval = EmsUtil.getProfileValue(
                ProfileKeyName.KEY_TIME_INTERVAL_FOR_RECEIVED_BATCHES,
                TIME_INTERVAL_DEFAULT_VALUE);

        List<BatchTO> batchTOList;
        try {
            batchTOList = em
                    .createQuery(
                            "SELECT btc FROM DispatchInfoTO dpi, BatchTO btc "
                                    + "WHERE dpi.containerId = btc.id AND "
                                    + "dpi.containerType = :CONTAINER_TYPE AND "
                                    + "btc.state = :BATCH_STATE AND "
                                    + "(dpi.receiveDate <= :TIME_INTERVAL AND "
                                    + "dpi.detailReceiveDate <= :TIME_INTERVAL) AND "
                                    + "("
                                    + "(SELECT COUNT(c1.id) FROM CardTO c1 WHERE c1.batch.id = btc.id AND (c1.receiveDate IS NOT NULL OR c1.lostDate IS NOT NULL))"
                                    + " = (SELECT COUNT(c2.id) FROM CardTO c2 WHERE c2.batch.id = btc.id)"
                                    + ") AND "
                                    + "dpi.receiverDepartmentId IN (SELECT EOF.id FROM EnrollmentOfficeTO EOF)",
                            BatchTO.class)
                    .setParameter("CONTAINER_TYPE",
                            DepartmentDispatchSendType.BATCH)
                    .setParameter("BATCH_STATE", BatchState.SHIPPED)
                    .setParameter(
                            "TIME_INTERVAL",
                            EmsUtil.differHour(new Date(),
                                    0 - Integer.valueOf(timeInterval)))
                    .setFirstResult(from).setMaxResults(to).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.DSI_051,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        return batchTOList;
    }

    @Override
    public Long findReceivedBatchesCount() throws BaseException {
        String TIME_INTERVAL_DEFAULT_VALUE = "0";
        String timeInterval = EmsUtil.getProfileValue(
                ProfileKeyName.KEY_TIME_INTERVAL_FOR_RECEIVED_BATCHES,
                TIME_INTERVAL_DEFAULT_VALUE);

        try {
            List<Long> batchCountList = em
                    .createQuery(
                            "SELECT COUNT(btc.id) FROM DispatchInfoTO dpi, BatchTO btc "
                                    + "WHERE dpi.containerId = btc.id AND "
                                    + "dpi.containerType = :CONTAINER_TYPE AND "
                                    + "btc.state = :BATCH_STATE AND "
                                    + "(dpi.receiveDate <= :TIME_INTERVAL AND "
                                    + "dpi.detailReceiveDate <= :TIME_INTERVAL) AND "
                                    + "("
                                    + "(SELECT COUNT(c1.id) FROM CardTO c1 WHERE c1.batch.id = btc.id AND (c1.receiveDate IS NOT NULL OR c1.lostDate IS NOT NULL))"
                                    + " = (SELECT COUNT(c2.id) FROM CardTO c2 WHERE c2.batch.id = btc.id)"
                                    + ") AND "
                                    + "dpi.receiverDepartmentId IN (SELECT EOF.id FROM EnrollmentOfficeTO EOF)",
                            Long.class)
                    .setParameter("CONTAINER_TYPE",
                            DepartmentDispatchSendType.BATCH)
                    .setParameter("BATCH_STATE", BatchState.SHIPPED)
                    .setParameter(
                            "TIME_INTERVAL",
                            EmsUtil.differHour(new Date(),
                                    0 - Integer.valueOf(timeInterval)))
                    .getResultList();
            if (EmsUtil.checkListSize(batchCountList)) {
                return batchCountList.get(0);
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.DSI_048,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        return null;
    }

    /**
     * The method findMissedBatches is used to find a specified instances of
     * type {@link com.gam.nocr.ems.data.domain.BatchTO} in order to understand
     * which one of them were missed
     *
     * @return a list of type {@link com.gam.nocr.ems.data.domain.BatchTO}
     */
    @Override
    public List<BatchTO> findMissedBatches(Integer from, Integer to)
            throws BaseException {
        String TIME_INTERVAL_DEFAULT_VALUE = "0";
        String timeInterval = EmsUtil.getProfileValue(
                ProfileKeyName.KEY_TIME_INTERVAL_FOR_MISSED_BATCHES,
                TIME_INTERVAL_DEFAULT_VALUE);

        List<BatchTO> batchTOList;
        try {
            batchTOList = em
                    .createQuery(
                            "SELECT bat FROM DispatchInfoTO dpi, BatchTO bat "
                                    + "WHERE dpi.containerId = bat.id AND "
                                    + "dpi.containerType = :CONTAINER_TYPE AND "
                                    + "bat.state = :BATCH_STATE AND "
                                    + "(dpi.lostDate <= :TIME_INTERVAL OR "
                                    + "dpi.detailLostDate <= :TIME_INTERVAL)",
                            BatchTO.class)
                    .setParameter("CONTAINER_TYPE",
                            DepartmentDispatchSendType.BATCH)
                    .setParameter("BATCH_STATE", BatchState.SHIPPED)
                    .setParameter(
                            "TIME_INTERVAL",
                            EmsUtil.differHour(new Date(),
                                    0 - Integer.valueOf(timeInterval)))
                    .setFirstResult(from).setMaxResults(to).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.DSI_033,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        return batchTOList;
    }

    @Override
    public Long findMissedBatchesCount() throws BaseException {
        String TIME_INTERVAL_DEFAULT_VALUE = "0";
        String timeInterval = EmsUtil.getProfileValue(
                ProfileKeyName.KEY_TIME_INTERVAL_FOR_MISSED_BATCHES,
                TIME_INTERVAL_DEFAULT_VALUE);

        try {
            List<Long> batchCountList = em
                    .createQuery(
                            "SELECT COUNT(bat.id) FROM DispatchInfoTO dpi, BatchTO bat "
                                    + "WHERE dpi.containerId = bat.id AND "
                                    + "dpi.containerType = :CONTAINER_TYPE AND "
                                    + "bat.state = :BATCH_STATE AND "
                                    + "(dpi.lostDate <= :TIME_INTERVAL OR "
                                    + "dpi.detailLostDate <= :TIME_INTERVAL)",
                            Long.class)
                    .setParameter("CONTAINER_TYPE",
                            DepartmentDispatchSendType.BATCH)
                    .setParameter("BATCH_STATE", BatchState.SHIPPED)
                    .setParameter(
                            "TIME_INTERVAL",
                            EmsUtil.differHour(new Date(),
                                    0 - Integer.valueOf(timeInterval)))
                    .getResultList();
            if (EmsUtil.checkListSize(batchCountList)) {
                return batchCountList.get(0);
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.DSI_049,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        return null;
    }

    /**
     * The method findMissedBoxes is used to find a specified instances of type
     * {@link com.gam.nocr.ems.data.domain.BoxTO} in order to understand which
     * one of them were missed
     *
     * @return a list of type {@link com.gam.nocr.ems.data.domain.BoxTO}
     */
    @Override
    public List<BoxTO> findMissedBoxes(Integer from, Integer to)
            throws BaseException {
        String TIME_INTERVAL_DEFAULT_VALUE = "0";
        String timeInterval = EmsUtil.getProfileValue(
                ProfileKeyName.KEY_TIME_INTERVAL_FOR_MISSED_BOXES,
                TIME_INTERVAL_DEFAULT_VALUE);

        List<BoxTO> boxTOList;
        try {
            boxTOList = em
                    .createQuery(
                            "SELECT box FROM DispatchInfoTO dpi, BoxTO box "
                                    + "WHERE dpi.containerId = box.id AND "
                                    + "dpi.containerType = :CONTAINER_TYPE AND "
                                    + "box.state = :BOX_STATE AND "
                                    + "(dpi.lostDate <= :TIME_INTERVAL OR "
                                    + "dpi.detailLostDate <= :TIME_INTERVAL)",
                            BoxTO.class)
                    .setParameter("CONTAINER_TYPE",
                            DepartmentDispatchSendType.BOX)
                    .setParameter("BOX_STATE", BoxState.SHIPPED)
                    .setParameter(
                            "TIME_INTERVAL",
                            EmsUtil.differHour(new Date(),
                                    0 - Integer.valueOf(timeInterval)))
                    .setFirstResult(from).setMaxResults(to).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.DSI_035,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        return boxTOList;
    }

    @Override
    public Long findMissedBoxesCount() throws BaseException {
        String TIME_INTERVAL_DEFAULT_VALUE = "0";
        String timeInterval = EmsUtil.getProfileValue(
                ProfileKeyName.KEY_TIME_INTERVAL_FOR_MISSED_BOXES,
                TIME_INTERVAL_DEFAULT_VALUE);

        try {
            List<Long> boxCountList = em
                    .createQuery(
                            "SELECT count(box.id) FROM DispatchInfoTO dpi, BoxTO box "
                                    + "WHERE dpi.containerId = box.id AND "
                                    + "dpi.containerType = :CONTAINER_TYPE AND "
                                    + "box.state = :BOX_STATE AND "
                                    + "(dpi.lostDate <= :TIME_INTERVAL OR "
                                    + "dpi.detailLostDate <= :TIME_INTERVAL)",
                            Long.class)
                    .setParameter("CONTAINER_TYPE",
                            DepartmentDispatchSendType.BOX)
                    .setParameter("BOX_STATE", BoxState.SHIPPED)
                    .setParameter(
                            "TIME_INTERVAL",
                            EmsUtil.differHour(new Date(),
                                    0 - Integer.valueOf(timeInterval)))
                    .getResultList();
            if (EmsUtil.checkListSize(boxCountList)) {
                return boxCountList.get(0);
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.DSI_050,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        return null;
    }

    /**
     * The method findMissedCards is used to find a specified instances of type
     * {@link com.gam.nocr.ems.data.domain.CardTO} in order to understand which
     * one of them were missed
     *
     * @return a list of type {@link com.gam.nocr.ems.data.domain.CardTO}
     */
    @Override
    public List<CardTO> findMissedCards(Integer from, Integer to)
            throws BaseException {
        String TIME_INTERVAL_DEFAULT_VALUE = "0";
        String timeInterval = EmsUtil.getProfileValue(
                ProfileKeyName.KEY_TIME_INTERVAL_FOR_MISSED_CARDS,
                TIME_INTERVAL_DEFAULT_VALUE);

        List<CardTO> cardTOList;
        try {
            cardTOList = em
                    .createQuery(
                            "SELECT crd FROM CardTO crd "
                                    + "WHERE crd.state = :CARD_STATE AND "
                                    + "crd.lostDate <= :TIME_INTERVAL",
                            CardTO.class)
                    .setParameter("CARD_STATE", CardState.SHIPPED)
                    .setParameter(
                            "TIME_INTERVAL",
                            EmsUtil.differHour(new Date(),
                                    0 - Integer.valueOf(timeInterval)))
                    .setFirstResult(from).setMaxResults(to).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.DSI_037,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        return cardTOList;
    }

    @Override
    public Long findMissedCardsCount() throws BaseException {
        String TIME_INTERVAL_DEFAULT_VALUE = "0";
        String timeInterval = EmsUtil.getProfileValue(
                ProfileKeyName.KEY_TIME_INTERVAL_FOR_MISSED_CARDS,
                TIME_INTERVAL_DEFAULT_VALUE);

        try {
            List<Long> cardCountList = em
                    .createQuery(
                            "SELECT COUNT(crd.id) FROM CardTO crd "
                                    + "WHERE crd.state = :CARD_STATE AND "
                                    + "crd.lostDate <= :TIME_INTERVAL",
                            Long.class)
                    .setParameter("CARD_STATE", CardState.SHIPPED)
                    .setParameter(
                            "TIME_INTERVAL",
                            EmsUtil.differHour(new Date(),
                                    0 - Integer.valueOf(timeInterval)))
                    .getResultList();
            if (EmsUtil.checkListSize(cardCountList)) {
                return cardCountList.get(0);
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.DSI_052,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        return null;
    }

    @Override
    public BatchTO findBatchByCmsId(String cmsBatchId) throws BaseException {
        List<BatchTO> batchTOList;
        try {
            batchTOList = em
                    .createQuery(
                            " select bat from BatchTO bat "
                                    + " where bat.cmsID = :cmsBatchId ",
                            BatchTO.class)
                    .setParameter("cmsBatchId", cmsBatchId).getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.DSI_040,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        if (EmsUtil.checkListSize(batchTOList))
            return batchTOList.get(0);
        else
            return null;
    }

    /**
     * The method findBoxByCmsId is used to find an instance of type
     * {@link com.gam.nocr.ems.data.domain.BoxTO} by using CMS box id
     *
     * @param cmsBoxId is a string value which represents the CMS box Id
     * @return a instance of type {@link BoxTO} or null
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public BoxTO findBoxByCmsId(String cmsBoxId) throws BaseException {
        BoxTO boxTO = null;
        try {
            List<BoxTO> boxTOList = em
                    .createQuery(
                            "SELECT box FROM BoxTO box "
                                    + "WHERE box.cmsID = :BOX_CMS_ID ",
                            BoxTO.class).setParameter("BOX_CMS_ID", cmsBoxId)
                    .getResultList();

            if (boxTOList.size() > 0) {
                boxTO = boxTOList.get(0);
            }
            return boxTO;

        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.DSI_041,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public void replaceDispatchInfoReceiverDepId(Long enrollmentOfficeId,
                                                 Long superiorEnrollmentOfficeId) throws BaseException {
        logger.info("replaceDispatchInfoReceiverDepId for enrollmentOfficeId : '{}' and superiorEnrollmentOfficeId : '{}'", enrollmentOfficeId, superiorEnrollmentOfficeId);
        try {
            em.createQuery(
                    "update DispatchInfoTO dis "
                            + "set dis.receiverDepartmentId = :superiorEnrollmentOfficeId"
                            + " where dis.receiverDepartmentId = :enrollmentOfficeId")
                    .setParameter("enrollmentOfficeId", enrollmentOfficeId)
                    .setParameter("superiorEnrollmentOfficeId",
                            superiorEnrollmentOfficeId).executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.DSI_047,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public List<DispatchInfoTO> findByContainerId(Long containerId)
            throws BaseException {
        try {

            return em
                    .createQuery(
                            " select dis from DispatchInfoTO dis "
                                    + " where dis.containerId = :containerId ",
                            DispatchInfoTO.class)
                    .setParameter("containerId", containerId).getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.DSI_053,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    // Implementing triggers
    // /////**********************************************************

    public Long getReceivedCardsCountForBatchId(long batchId) {
        return em.createQuery("select count(crd.id) from CardTO crd where crd.receiveDate is not null and crd.batch.id = :batchId", Long.class).setParameter("batchId", batchId).getSingleResult();
    }

    public Long getReceivedBatchCountForBoxId(long boxId) {
        List<Long> batchList = em.createQuery("select bat.id from BatchTO bat where bat.box.id = :boxId", Long.class).setParameter("boxId", boxId).getResultList();
        return em.createQuery("select count(dis.id) from DispatchInfoTO dis where dis.receiveDate is not null and dis.containerId in (:batchList) and dis.containerType = :type", Long.class).setParameter("batchList", batchList).setParameter("type", DepartmentDispatchSendType.BATCH).getSingleResult();
    }

    public Long getLostCardsCountForBatchId(long batchId) {
        return em.createQuery("select count(crd.id) from CardTO crd where crd.lostDate is not null and crd.batch.id = :batchId", Long.class).setParameter("batchId", batchId).getSingleResult();
    }

    public Long getLostBatchCountForBoxId(long boxId) {
        List<Long> batchList = em.createQuery("select bat.id from BatchTO bat where bat.box.id = :boxId", Long.class).setParameter("boxId", boxId).getResultList();
        return em.createQuery("select count(dis.id) from DispatchInfoTO dis where dis.lostDate is not null or dis.detailLostDate is not null and dis.containerId in (:batchList) and dis.containerType = :type", Long.class).setParameter("batchList", batchList).setParameter("type", DepartmentDispatchSendType.BATCH).getSingleResult();
    }


    // By : Adldoost
    // First part of AIU_Card_Rcv
    // Should be run after everywhere after card received date changes to not
    // null

    private void notifyCardsReceived(List<Long> cardIdList)
            throws BaseException {

        logger.info("AIU_Card_Rcv : notify cards received for card ids : '{}'", cardIdList);

        try {
            List<BatchTO> batches = em
                    .createQuery(
                            "select crd.batch from CardTO crd where crd.id in (:ID_LIST)",
                            BatchTO.class).setParameter("ID_LIST", cardIdList)
                    .getResultList();
            for (BatchTO batch : batches) {

                Long receivedCount = getReceivedCardsCountForBatchId(batch.getId());

                if (receivedCount == 1) {
                    em.createQuery(
                            "update DispatchInfoTO dpi set dpi.receiveDate = current_date"
                                    + " where dpi.containerId = :batchId and dpi.containerType = :type ")
                            .setParameter("type",
                                    DepartmentDispatchSendType.BATCH)
                            .setParameter("batchId", batch.getId())
                            .executeUpdate();
                    em.flush();
                }
            }
            em.flush();

        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.DSI_055, e);
        }

    }

    // By : Adldoost
    // second part of AIU_Card_Rcv
    // Should be run after everywhere after card received date changes to null
    private void undoNotifyCardsReceived(List<Long> cardIdList)
            throws BaseException {

        if (cardIdList == null || cardIdList.isEmpty())
            return;
        logger.info("AIU_Card_Rcv : undo notify cards received for card ids : '{}'", cardIdList);

        try {
            List<BatchTO> batches = em
                    .createQuery(
                            "select crd.batch from CardTO crd where crd.id in (:ID_LIST)",
                            BatchTO.class).setParameter("ID_LIST", cardIdList)
                    .getResultList();

            em.flush();

            for (BatchTO batch : batches) {
                List<Date> minReceiveDate = em
                        .createQuery(
                                "select min(card.receiveDate) from CardTO card where card.batch = :batch",
                                Date.class).setParameter("batch", batch)
                        .getResultList();
                if (minReceiveDate != null && !minReceiveDate.isEmpty()) {
                    em.createQuery(
                            "update DispatchInfoTO dpi set dpi.receiveDate = :date"
                                    + " where dpi.containerId = :batchId and dpi.containerType = :type")
                            .setParameter("type",
                                    DepartmentDispatchSendType.BATCH)
                            .setParameter("batchId", batch.getId())
                            .setParameter("date", minReceiveDate.get(0))
                            .executeUpdate();
                    em.flush();
                } else {
                    em.createQuery(
                            "update DispatchInfoTO dpi set dpi.receiveDate = :date"
                                    + " where dpi.containerId = :batchId and dpi.containerType = :type")
                            .setParameter("type",
                                    DepartmentDispatchSendType.BATCH)
                            .setParameter("batchId", batch.getId())
                            .setParameter("date", null).executeUpdate();
                    em.flush();
                }
            }

        } catch (Exception exp) {
            throw new DAOException(DataExceptionCode.DSI_057, exp);
        }

    }

    // By : Adldoost
    // should be run everywhere after update card container(batch) lostDate
    // to not null
    // second part of trigger : AIU_Dis_Info_Lost
    private void notifyCardContainerLost(List<Long> ids, boolean isDispatchIdList) throws BaseException {

        logger.info("AIU_Dis_Info_Lost : notify cardContainer lost for ids : '{}'", ids);
        if (ids == null || ids.isEmpty())
            return;
        try {

            List<Long> batchDispatchesIds = null;
            if (isDispatchIdList) {
                batchDispatchesIds = em
                        .createQuery(
                                "select dispatch.containerId from DispatchInfoTO dispatch where dispatch.id in (:ID_LIST) and dispatch.containerType = :type",
                                Long.class).setParameter("ID_LIST", ids)
                        .setParameter("type", DepartmentDispatchSendType.BATCH)
                        .getResultList();
            } else
                batchDispatchesIds = ids;

            List<BatchTO> batches = em
                    .createQuery(
                            "select batch from BatchTO batch where batch.id in (:ID_LIST)",
                            BatchTO.class)
                    .setParameter("ID_LIST", batchDispatchesIds)
                    .getResultList();

            List<Long> lostBoxesIds = new ArrayList<Long>();
            for (BatchTO batch : batches) {
                long boxBatchesCount = em.createQuery("select count(bat.id) from BatchTO bat where bat.box.id = :boxid", Long.class).setParameter("boxid", batch.getBox().getId()).getSingleResult();

//				if(batch.getBox().getTotalBatchesCount() == getLostBatchCountForBoxId(batch.getBox().getId()))
                if (boxBatchesCount == getLostBatchCountForBoxId(batch.getBox().getId())) {
                    lostBoxesIds.add(batch.getBox().getId());
                }
            }
            if (lostBoxesIds == null || lostBoxesIds.isEmpty())
                return;
            em.createQuery(
                    "update DispatchInfoTO dispatch set dispatch.lostDate = current_date where dispatch.containerId in (:lostIds)")
                    .setParameter("lostIds", lostBoxesIds).executeUpdate();
            em.flush();
        } catch (Exception exp) {
            throw new DAOException(DataExceptionCode.DSI_058, exp);
        }

    }


    // By Adldoost
    // Should be run every where card container(batch) lost date changes to
    // null
    // first part of trigger : AIU_Dis_Inf_Lost
    private void notifyCardContainerFound(List<Long> ids, boolean isDispatchIdList) throws BaseException {

        logger.info("AIU_Dis_Info_Lost : notify cardContainer foung for ids : '{}'", ids);
        if (ids == null || ids.isEmpty())
            return;
        try {
            List<Long> batchDispatchesIds = null;
            if (isDispatchIdList) {
                batchDispatchesIds = em
                        .createQuery(
                                "select dispatch.containerId from DispatchInfoTO dispatch where dispatch.id in (:ID_LIST) and dispatch.containerType = :type",
                                Long.class).setParameter("ID_LIST", ids)
                        .setParameter("type", DepartmentDispatchSendType.BATCH)
                        .getResultList();
            } else
                batchDispatchesIds = ids;

            List<BatchTO> batches = em
                    .createQuery(
                            "select batch from BatchTO batch where batch.id in (:ID_LIST)",
                            BatchTO.class)
                    .setParameter("ID_LIST", batchDispatchesIds)
                    .getResultList();

            List<Long> unLostBoxesIds = new ArrayList<Long>();
            for (BatchTO batch : batches) {
                unLostBoxesIds.add(batch.getBox().getId());
            }
            em.createQuery(
                    "update DispatchInfoTO dispatch set dispatch.lostDate = :nullDate where dispatch.containerId in (:lostIds)")
                    .setParameter("lostIds", unLostBoxesIds)
                    .setParameter("nullDate", null).executeUpdate();
            em.flush();
        } catch (Exception exp) {
            throw new DAOException(DataExceptionCode.DSI_059, exp);
        }
    }

    // By : Adldoost
    // should be run everywhere after update card container(batch/box)
    // receivedDate to not null
    // first part of trigger : AIU_Dis_Info_Rcv
    private void notifyCardContainerReceived(List<Long> ids) throws BaseException {

        logger.info("AIU_Dis_Info_Rcv : notify cardContainer received foung for ids : '{}'", ids);
        if (ids == null || ids.isEmpty())
            return;
        try {
            List<Long> batchDispatchesIds = em
                    .createQuery(
                            "select dispatch.containerId from DispatchInfoTO dispatch where dispatch.id in (:ID_LIST) and dispatch.containerType = :type",
                            Long.class).setParameter("ID_LIST", ids)
                    .setParameter("type", DepartmentDispatchSendType.BATCH)
                    .getResultList();

            List<BatchTO> batches = em
                    .createQuery(
                            "select batch from BatchTO batch where batch.id in (:ID_LIST)",
                            BatchTO.class)
                    .setParameter("ID_LIST", batchDispatchesIds)
                    .getResultList();

            List<Long> receivedBoxesIds = new ArrayList<Long>();
            for (BatchTO batch : batches) {
                receivedBoxesIds.add(batch.getBox().getId());
            }
            em.createQuery(
                    "update DispatchInfoTO dispatch set dispatch.receiveDate = current_date where dispatch.containerId in (:recIds)")
                    .setParameter("recIds", receivedBoxesIds).executeUpdate();
            em.flush();
        } catch (Exception exp) {
            throw new DAOException(DataExceptionCode.DSI_060, exp);
        }
    }

    // By Adldoost
    // should be run everywhere after card container(batch/box) received date
    // changes to null
    // second part of trigger AIU_Dis_Info_Rcv
    private void undoNotifyCardContainerReceived(List<Long> ids) throws BaseException {

        logger.info("AIU_Dis_Info_Rcv : undo notify cardContainer received foung for ids : '{}'", ids);
        if (ids == null || ids.isEmpty())
            return;
        try {
            List<Long> batchDispatchesIds = em
                    .createQuery(
                            "select dispatch.containerId from DispatchInfoTO dispatch where dispatch.id in (:ID_LIST) and dispatch.containerType = :type",
                            Long.class).setParameter("ID_LIST", ids)
                    .setParameter("type", DepartmentDispatchSendType.BATCH)
                    .getResultList();

            List<BatchTO> batches = em
                    .createQuery(
                            "select batch from BatchTO batch where batch.id in (:ID_LIST)",
                            BatchTO.class)
                    .setParameter("ID_LIST", batchDispatchesIds)
                    .getResultList();

            List<Long> unReceivedBoxesIds = new ArrayList<Long>();
            for (BatchTO batch : batches) {
                if (getReceivedBatchCountForBoxId(batch.getBox().getId()) == 0)
                    unReceivedBoxesIds.add(batch.getBox().getId());

            }
            em.createQuery(
                    "update DispatchInfoTO dispatch set dispatch.receiveDate = :nullDate where dispatch.containerId in (:Ids)")
                    .setParameter("Ids", unReceivedBoxesIds)
                    .setParameter("nullDate", null).executeUpdate();
            em.flush();
        } catch (Exception exp) {
            throw new DAOException(DataExceptionCode.DSI_061, exp);
        }
    }

    // By : Adldoost
    // first part of trigger : AUI_Card_Lost
    // Should be run everywhere after card lost date changes to not null
    private void notifyCardLost(List<Long> cardIdList) throws BaseException {

        logger.info("AUI_Card_Lost : notify card Lost for cards ids : '{}'", cardIdList);

        try {

            if (cardIdList == null || cardIdList.isEmpty())
                return;

            List<CardTO> cards = em
                    .createQuery(
                            "select crd from CardTO crd where crd.id in (:ID_LIST)",
                            CardTO.class).setParameter("ID_LIST", cardIdList)
                    .getResultList();
            List<BatchTO> batches = em
                    .createQuery(
                            "select crd.batch from CardTO crd where crd.id in (:ID_LIST)",
                            BatchTO.class).setParameter("ID_LIST", cardIdList)
                    .getResultList();

            for (CardTO card : cards) {

                long batchCardsCount = em.createQuery("select count(crd.id) from CardTO crd where crd.batch.id = :batchid", Long.class).setParameter("batchid", card.getBatch().getId()).getSingleResult();
//				if (getLostCardsCountForBatchId(card.getBatch().getId()) > card.getBatch()
//						.getTotalCardsCount()) {
//					throw new DAOException(DataExceptionCode.DSI_062, new RuntimeException("Lost cards count should not be larger than total count"));
//				}
                if (getLostCardsCountForBatchId(card.getBatch().getId()) > batchCardsCount) {
                    throw new DAOException(DataExceptionCode.DSI_062, new RuntimeException("Lost cards count should not be larger than total count"));
                }
            }
            em.flush();
            List<Long> lostBatchList = new ArrayList<Long>();
            for (BatchTO batch : batches) {
                long batchCardsCount = em.createQuery("select count(crd.id) from CardTO crd where crd.batch.id = :batchid", Long.class).setParameter("batchid", batch.getId()).getSingleResult();
//				if (batch.getTotalCardsCount() == getLostCardsCountForBatchId(batch.getId())) {
                if (batchCardsCount == getLostCardsCountForBatchId(batch.getId())) {
                    List<Date> minDate = em.createQuery(
                            "select max(crd.lostDate) from CardTO crd",
                            Date.class).getResultList();

                    if (minDate == null || minDate.isEmpty()) {
                        throw new DAOException(DataExceptionCode.DSI_063, DataExceptionCode.DSI_063_MSG);
                    }

                    em.createQuery(
                            "update DispatchInfoTO dpi set dpi.lostDate = :lostDate"
                                    + " where dpi.containerId = :batchId and dpi.containerType = :type ")
                            .setParameter("lostDate", minDate.get(0))
                            .setParameter("type",
                                    DepartmentDispatchSendType.BATCH)
                            .setParameter("batchId", batch.getId())
                            .executeUpdate();

                    lostBatchList.add(batch.getId());
                }
            }

            em.flush();
            notifyCardContainerLost(lostBatchList, false);// ids are batch table recoreds ids
        } catch (Exception exp) {
            throw new DAOException(DataExceptionCode.DSI_064, exp);
        }

    }

    // By : Adldoost
    // second part of trigger : AUI_Card_Lost
    // Should be run everywhere after Card LostDate changes to null
    private void notifyCardFound(List<Long> cardIdList) throws BaseException {

        logger.info("AUI_Card_Lost : notify card found for cards ids : '{}'", cardIdList);

        try {
            if (cardIdList == null || cardIdList.isEmpty())
                return;
            List<CardTO> cards = em
                    .createQuery(
                            "select crd from CardTO crd where crd.id in (:ID_LIST)",
                            CardTO.class).setParameter("ID_LIST", cardIdList)
                    .getResultList();
            List<Long> batchIds = new ArrayList<Long>();
            for (CardTO card : cards) {
                batchIds.add(card.getBatch().getId());
            }

            for (CardTO card : cards) {
                notifyCardsReceived(cardIdList);
                if (getLostCardsCountForBatchId(card.getBatch().getId()) < 0) {
                    throw new DAOException(DataExceptionCode.DSI_072, DataExceptionCode.DSI_072_MSG);
                }
                em.createQuery(
                        "update DispatchInfoTO dpi set dpi.lostDate = :lostDate"
                                + " where dpi.containerId = :batchId and dpi.containerType = :type ")
                        .setParameter("lostDate", null)
                        .setParameter("type", DepartmentDispatchSendType.BATCH)
                        .setParameter("batchId", card.getBatch().getId())
                        .executeUpdate();
            }
            em.flush();
            notifyCardContainerFound(batchIds, false);
        } catch (Exception exp) {
            throw new DAOException(DataExceptionCode.DSI_065, exp);
        }
    }

    // By : Adldoost
    // should be run everywhere box state changes to Shipped
    // first part of trigger : AIU_Box
    private void notifyBoxShipped(List<Long> boxList) throws BaseException {

        cmsDispatchLogger.info("AIU_Box : notify Box Shipped for box ids : '{}'", boxList);

        try {
            List<BoxTO> boxes = em
                    .createQuery(
                            "select box from BoxTO box where box.id in (:ID_LIST)",
                            BoxTO.class).setParameter("ID_LIST", boxList)
                    .getResultList();
            BatchTO selectedBatch = null;
            CardTO selectedCard = null;
            CardRequestTO selectedRequest = null;
            EnrollmentOfficeTO selectedOffice = null;
            for (BoxTO box : boxes) {
                List<BatchTO> batchList = em
                        .createQuery(
                                "select batch from BatchTO batch where batch.box.id in (:ID_LIST)",
                                BatchTO.class).setParameter("ID_LIST", boxList)
                        .getResultList();
                if (batchList != null && !batchList.isEmpty()) {
                    selectedBatch = batchList.get(0);
                    List<CardTO> cardList = em
                            .createQuery(
                                    "select card from CardTO card where card.batch in :batch",
                                    CardTO.class)
                            .setParameter("batch", selectedBatch)
                            .setMaxResults(1).getResultList();
                    if (cardList != null && !cardList.isEmpty()) {
                        selectedCard = cardList.get(0);
                        List<CardRequestTO> requestList = em
                                .createQuery(
                                        "select req from CardRequestTO req where req.card = :card",
                                        CardRequestTO.class)
                                .setParameter("card", selectedCard)
                                .setMaxResults(1).getResultList();
                        if (requestList != null && !requestList.isEmpty()) {
                            selectedRequest = requestList.get(0);
                            if (selectedRequest.getSelectDeliveryOfficeId() != null) {
                                selectedOffice = getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class, selectedRequest.getSelectDeliveryOfficeId());
                            } else {
                                selectedOffice = selectedRequest.getEnrollmentOffice();
                            }
                            DispatchInfoTO dispatchInfo = new DispatchInfoTO();
                            dispatchInfo.setContainerId(box.getId());
                            dispatchInfo.setContainerType(DepartmentDispatchSendType.BOX);
                            dispatchInfo.setDispatchParentId(null);
                            dispatchInfo.setReceiveDate(new Timestamp(new Date()
                                    .getTime()));
                            if (selectedOffice.getType() == EnrollmentOfficeType.OFFICE) {
                                dispatchInfo.setReceiverDepartmentId(selectedOffice.getSuperiorOffice().getParentDepartment().getId());
                            } else
                                dispatchInfo.setReceiverDepartmentId(selectedOffice.getParentDepartment().getId());

                            dispatchInfo.setSendDate(new Timestamp(new Date()
                                    .getTime()));
                            dispatchInfo.setSenderDepartmentId((long) 1);


//							create(dispatchInfo);
                            List<DispatchInfoTO> repeatitiveDispatchInfoListBox = em
                                    .createQuery(
                                            "select dip from DispatchInfoTO dip where dip.containerId = :containerId " +
                                                    "and dip.containerType = :containerType " +
                                                    "and dip.senderDepartmentId = :senderDepartmentId ",
                                            DispatchInfoTO.class)
                                    .setParameter("containerType", DepartmentDispatchSendType.BOX)
                                    .setParameter("containerId", box.getId())
                                    .setParameter("senderDepartmentId", dispatchInfo.getSenderDepartmentId())
                                    .getResultList();
                            if (repeatitiveDispatchInfoListBox == null || repeatitiveDispatchInfoListBox.isEmpty()) {
//								em.persist(dispatchInfo);	
                                cmsDispatchLogger.info("box '{}' is starting to add in dispatchinfoTO", box);
                                create(dispatchInfo);
                                em.flush();
                                //em.refresh(dispatch);
                            } else {
                                cmsDispatchLogger.info("box '{}' is already existing in dispatchinfoTO so skipp to insert it twice and starting to add its batches to dispatchinfo.", box);
                                dispatchInfo = repeatitiveDispatchInfoListBox.get(0);
                            }

                            cmsDispatchLogger.info("start to adding batches in dispatchinfoTO ...");
                            for (BatchTO bat : batchList) {
                                DispatchInfoTO dis = new DispatchInfoTO();
                                dis.setContainerId(bat.getId());
                                dis.setContainerType(DepartmentDispatchSendType.BATCH);
                                dis.setDispatchParentId(dispatchInfo);
                                dis.setDetailReceiveDate(new Timestamp(new Date().getTime()));
                                //dis.setReceiveDate(new Timestamp(new Date().getTime()));
                                dis.setReceiveDate(null);
                                //Anbari
                                if (selectedOffice.getType() == EnrollmentOfficeType.OFFICE) {
                                    if (EnrollmentOfficeDeliverStatus.ENABLED.equals(selectedOffice.getDeliver()))
                                        dis.setReceiverDepartmentId(selectedOffice.getId());
                                    else
                                        dis.setReceiverDepartmentId(selectedOffice.getSuperiorOffice().getId());
                                } else
                                    dis.setReceiverDepartmentId(selectedOffice.getId());
                                dis.setSendDate(new Timestamp(new Date().getTime()));
                                dis.setSenderDepartmentId(dispatchInfo.getReceiverDepartmentId());

                                // In Previous Dispatch ( with trigger )  we have some situation that the Box is ready and some or all of it's batch are inserted in dispatchInfo in this situation we don't throw the constraint exception
                                List<DispatchInfoTO> repeatitiveDispatchInfoList = em
                                        .createQuery(
                                                "select dip from DispatchInfoTO dip where dip.containerId = :containerId " +
                                                        "and dip.containerType = :containerType " +
                                                        "and dip.senderDepartmentId = :senderDepartmentId ",
                                                DispatchInfoTO.class)
                                        .setParameter("containerType", DepartmentDispatchSendType.BATCH)
                                        .setParameter("containerId", bat.getId())
                                        .setParameter("senderDepartmentId", dispatchInfo.getReceiverDepartmentId())
                                        .getResultList();
                                if (repeatitiveDispatchInfoList != null && repeatitiveDispatchInfoList.size() > 0) {
                                    cmsDispatchLogger.info("batch with cmsid '{}' is already existing in dispatchinfoTO so skipp to insert it twice and go to next batch.", bat.getCmsID());
                                    continue;
                                }
                                create(dis);
//								em.persist(dis);
                                em.flush();
                            }
                        }
                    }
                }
            }
        } catch (Exception exp) {
            throw new DAOException(DataExceptionCode.DSI_066, exp);
        }
    }

    // By Adldoost
    // should be run everywhere after box state changes to missed
    // second part of trigger : AIU_Box
    private void notifyBoxMissed(List<Long> boxList) throws BaseException {

        logger.info("AIU_Box : notify Box missed for box ids : '{}'", boxList);
        try {
            List<BatchTO> batchList = em
                    .createQuery(
                            "select batch from BatchTO batch where batch.box.id in (:ID_LIST)",
                            BatchTO.class).setParameter("ID_LIST", boxList)
                    .getResultList();
            for (BatchTO batch : batchList) {
                batch.setState(BatchState.MISSED);
                em.merge(batch);
            }
            em.flush();
            List<Long> batchIds = new ArrayList<Long>();
            for (BatchTO batch : batchList) {
                batchIds.add(batch.getId());
            }
            notifyBatchMissed(batchIds);
        } catch (Exception exp) {
            throw new DAOException(DataExceptionCode.DSI_067, exp);
        }
    }

    // By Adldoost
    // should be run everywhere after batch state changes to missed
    // implementation of trigger : AIU_Batch
    private void notifyBatchMissed(List<Long> batchList) throws BaseException {
        logger.info("AIU_Batch : notify Batch missed for batches ids : '{}'", batchList);
        try {
            List<CardTO> cards = em
                    .createQuery(
                            "select crd from CardTO crd where crd.batch.id in (:ID_LIST) and crd.state <> :state",
                            CardTO.class).setParameter("state", CardState.MISSED).setParameter("ID_LIST", batchList)
                    .getResultList();

            for (CardTO card : cards) {
                if (card.getState() != CardState.MISSED)
                    card.setState(CardState.MISSED);
            }
            em.flush();
            notifyCardMissed(cards);

        } catch (Exception exp) {
            throw new DAOException(DataExceptionCode.DSI_068, exp);
        }
    }

    // By Adldoost
    // should be run everywhere after card state changes to missed
    // implementation of trigger : AIU_Card
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @Override
    public void notifyCardMissed(List<CardTO> cards) throws BaseException {
        logger.info("AIU_Card : notify Card Missed for cards : '{}'", cards);
        try {
            List<CardRequestTO> requests = em
                    .createQuery(
                            "select req from CardRequestTO req where req.card in (:ID_LIST)",
                            CardRequestTO.class).setParameter("ID_LIST", cards)
                    .getResultList();

            for (CardRequestTO request : requests) {
                request.setState(CardRequestState.APPROVED_BY_AFIS);
            }
            em.flush();
        } catch (Exception exp) {
            throw new DAOException(DataExceptionCode.DSI_069, exp);
        }
    }

    //Anbari
    @Override
    public void replaceDispatchInfoReceiverIdBySuperriorOfficeId(Long eofId, Long supperiorOfficeId) throws DAOException {
        try {
            em.createQuery(
                    "update DispatchInfoTO dpi set dpi.receiverDepartmentId = :supperiorOfficeId"
                            + " where dpi.containerType = :type and dpi.receiverDepartmentId = :eofId")
                    .setParameter("supperiorOfficeId", supperiorOfficeId)
                    .setParameter("type", DepartmentDispatchSendType.BATCH)
                    .setParameter("eofId", eofId).executeUpdate();
        } catch (Exception exp) {
            throw new DAOException(DataExceptionCode.DSI_074, exp);
        }

    }

    /**
     * This method fetches cards which have missed date and has been confirmed by manager.
     *
     * @param from
     * @param to
     * @return
     * @throws BaseException
     * @author ganjyar
     */
    @Override
    public List<BatchTO> findMissedBatchesConfirmed(Integer from, Integer to)
            throws BaseException {
        List<BatchTO> batchTOList;
        try {
            batchTOList = em
                    .createQuery(
                            "SELECT bat FROM DispatchInfoTO dpi, BatchTO bat "
                                    + "WHERE dpi.containerId = bat.id AND "
                                    + "dpi.containerType = :CONTAINER_TYPE AND "
                                    + "bat.state = :BATCH_STATE AND bat.isLostBatchConfirmed is true AND "
                                    + "(dpi.lostDate is not null OR "
                                    + "dpi.detailLostDate is not null)"
                            , BatchTO.class)
                    .setParameter("CONTAINER_TYPE",
                            DepartmentDispatchSendType.BATCH)
                    .setParameter("BATCH_STATE", BatchState.SHIPPED)
                    .setFirstResult(from).setMaxResults(to).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.DSI_077,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        return batchTOList;
    }

    /**
     * this method find count missed batches which has been confirmed by manager in 3s.
     *
     * @author ganjyar
     */
    @Override
    public Long findMissedBatchesCountConfirmed() throws BaseException {
        try {
            List<Long> batchCountList = em
                    .createQuery(
                            "SELECT COUNT(bat.id) FROM DispatchInfoTO dpi, BatchTO bat "
                                    + "WHERE dpi.containerId = bat.id AND "
                                    + "dpi.containerType = :CONTAINER_TYPE AND "
                                    + "bat.state = :BATCH_STATE AND bat.isLostBatchConfirmed is true AND "
                                    + "(dpi.lostDate is not null OR "
                                    + "dpi.detailLostDate is not null)"
                            , Long.class)
                    .setParameter("CONTAINER_TYPE",
                            DepartmentDispatchSendType.BATCH)
                    .setParameter("BATCH_STATE", BatchState.SHIPPED)
                    .getResultList();
            if (EmsUtil.checkListSize(batchCountList)) {
                return batchCountList.get(0);
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.DSI_076,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        return null;
    }

    /**
     * This method fetches cards which have missed date and has been confirmed by manager.
     *
     * @author ganjyar
     */
    @Override
    public List<CardTO> findMissedCardsConfirmed(Integer from, Integer to)
            throws BaseException {

        List<CardTO> cardTOList;
        try {
            cardTOList = em
                    .createQuery(
                            "SELECT crd FROM CardTO crd "
                                    + "WHERE crd.state = :CARD_STATE AND "
                                    + "crd.lostDate is not null AND "
                                    + "crd.isLostCardConfirmed is true",
                            CardTO.class)
                    .setParameter("CARD_STATE", CardState.SHIPPED)
                    .setFirstResult(from).setMaxResults(to).getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.DSI_075,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        return cardTOList;

    }

    /**
     * this method fetch count of lost cards which manager has done their confirmation.
     *
     * @author ganjyar
     */
    @Override
    public Long findMissedCardsCountConfirmed() throws BaseException {

        try {
            List<Long> cardCountList = em
                    .createQuery(
                            "SELECT COUNT(crd.id) FROM CardTO crd "
                                    + "WHERE crd.state = :CARD_STATE AND "
                                    + "crd.lostDate is not null AND "
                                    + "crd.isLostCardConfirmed is true",
                            Long.class)
                    .setParameter("CARD_STATE", CardState.SHIPPED)
                    .getResultList();
            if (EmsUtil.checkListSize(cardCountList)) {
                return cardCountList.get(0);
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.DSI_052,
                    DataExceptionCode.GLB_005_MSG, e);
        }

        return null;

    }

    @Override
    public Long isValidToSendDeliverySms(Long batchId) throws BaseException {

        try {
            List resultList = em
                    .createNativeQuery(
                            "SELECT dpi.DPI_CONTAINER_ID FROM EMST_DISPATCH_INFO dpi, EMST_DEPARTMENT dep "
                                    + "where dpi.DPI_RECEIVER_DEP_ID = dep.DEP_ID "
                                    + "and dpi.DPI_CONTAINER_ID = :BATCH_ID "
                                    + "and dpi.DPI_CONTAINER_TYPE = :BATCH_TYPE  "
                                    + "and dep.DEP_LOCATION_ID not in "
                                    + "(SELECT bll.BLL_ID FROM EMST_BLACK_LIST_LOCATIONS bll) ")
                    .setParameter("BATCH_TYPE", "BATCH")
                    .setParameter("BATCH_ID", batchId).getResultList();
            if (resultList != null && !resultList.isEmpty()) {
                return new Long(((BigDecimal) resultList.get(0)).intValue());
            } else
                return null;

        } catch (Exception e) {
            throw new DataException(DataExceptionCode.DSI_052,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public List<DispatchInfoWTO> fetchBatchDispatchList(GeneralCriteria criteria,
                                                        UserProfileTO userProfileTO) throws BaseException {

        List<DispatchInfoWTO> result = new ArrayList<DispatchInfoWTO>();
        StringBuffer queryBuffer = new StringBuffer();

        try {
            queryBuffer.append("SELECT bat.BAT_CMS_ID cmsId, dpi.DPI_SEND_DATE dispatchSentDate, dpi.dpi_id id, bat.bat_id containerId, ");
            queryBuffer.append("(case ");
            queryBuffer.append("when (select count(*) from emst_dispatch_info idi where idi.dpi_id = dpi.dpi_id and (idi.dpi_receive_date is null and idi.dpi_lost_date is null)) > 0 then '0' ");
            queryBuffer.append("when (select count(*) from emst_dispatch_info idi where idi.dpi_id = dpi.dpi_id and (idi.dpi_receive_date is not null and idi.dpi_lost_date is null)) > 0 then '1' ");
            queryBuffer.append("when (select count(*) from emst_dispatch_info idi where idi.dpi_id = dpi.dpi_id and (idi.dpi_receive_date is null and idi.dpi_lost_date is not null)) > 0 then '2' ");
            queryBuffer.append("when (select count(*) from emst_dispatch_info idi where idi.dpi_id = dpi.dpi_id and (idi.dpi_receive_date is not null and idi.dpi_lost_date is not null)) > 0 then '4' ");
            queryBuffer.append("else '-1' end) status, count(crd.CRD_ID) itemCount ");
            queryBuffer.append("FROM EMST_BATCH bat, EMST_DISPATCH_INFO dpi, EMST_CARD crd where bat.BAT_ID = dpi.DPI_CONTAINER_ID and bat.BAT_ID = crd.CRD_BATCH_ID and dpi.DPI_RECEIVER_DEP_ID = :depId ");

            if (criteria.getParameters().get("cmsId") != null) {

                queryBuffer.append("and bat.BAT_CMS_ID like concat(concat('%',:cmsId),'%') ");
            }

            if (criteria.getParameters().get("fromDispatchSentDate") != null && criteria.getParameters().get("toDispatchSentDate") != null) {

                queryBuffer.append("and dpi.DPI_SEND_DATE BETWEEN to_date(:fromDispatchSentDate, 'YYYY/MM/DD HH24:MI')  and to_date(:toDispatchSentDate, 'YYYY/MM/DD HH24:MI') ");

            } else if (criteria.getParameters().get("fromDispatchSentDate") != null) {

                queryBuffer.append("and dpi.DPI_SEND_DATE >= to_date(:fromDispatchSentDate, 'YYYY/MM/DD HH24:MI') ");

            } else if (criteria.getParameters().get("toDispatchSentDate") != null) {

                queryBuffer.append("and dpi.DPI_SEND_DATE <= to_date(:toDispatchSentDate, 'YYYY/MM/DD HH24:MI') ");

            }

            queryBuffer.append("group by dpi.DPI_ID, bat.BAT_CMS_ID , dpi.DPI_SEND_DATE, bat.bat_id ");

            if (criteria.getOrderBy() != null
                    && !criteria.getOrderBy().equals("")) {
                queryBuffer.append(" order by " + criteria.getOrderBy().toString());

            } else {
                queryBuffer.append(" order by dpi.DPI_ID asc ");

            }


            Query query = em.createNativeQuery(queryBuffer.toString());
            query.setParameter("depId", userProfileTO.getDepID());

            if (criteria.getParameters() != null) {
                Set<String> keySet = criteria.getParameters().keySet();
                if (keySet != null) {
                    for (String key : keySet) {
                        if (queryBuffer.toString().contains(":" + key)) {
                            query.setParameter(key, criteria.getParameters().get(key));
                        }
                    }
                }
            }


            query.setMaxResults(criteria.getPageSize()).setFirstResult(criteria.getPageNo());

            List resultList = query.getResultList();
            if (resultList != null) {
                for (Object record : resultList) {
                    Object[] data = (Object[]) record;
                    DispatchInfoWTO obj = new DispatchInfoWTO();
                    obj.setCmsId((String) data[0]);
                    obj.setDispatchSentDate((Timestamp) data[1]);
                    obj.setId(((BigDecimal) data[2]).longValue());
                    obj.setContainerId(((BigDecimal) data[3]).longValue());
                    obj.setStatus((String) data[4]);
                    obj.setItemCount(((BigDecimal) data[5]).intValue());

                    result.add(obj);
                }

                return result;
            }

        } catch (Exception e) {

            throw new DataException(DataExceptionCode.DSI_079,
                    DataExceptionCode.GLB_005_MSG, e);

        }
        return null;

    }

    @Override
    public Integer countBatchDispatchList(HashMap parameters,
                                          UserProfileTO userProfileTO) throws BaseException {

        StringBuffer queryBuffer = new StringBuffer();

        try {
            queryBuffer.append("SELECT count(dpi.dpi_id) ");
            queryBuffer.append("FROM EMST_BATCH bat, EMST_DISPATCH_INFO dpi where bat.BAT_ID = dpi.DPI_CONTAINER_ID and dpi.DPI_RECEIVER_DEP_ID = :depId ");

            if (parameters.get("cmsId") != null) {

                queryBuffer.append("and bat.BAT_CMS_ID like concat(concat('%',:cmsId),'%') ");
            }

            if (parameters.get("fromDispatchSentDate") != null && parameters.get("toDispatchSentDate") != null) {

                queryBuffer.append("and dpi.DPI_SEND_DATE BETWEEN to_date(:fromDispatchSentDate, 'YYYY/MM/DD HH24:MI') and to_date(:toDispatchSentDate, 'YYYY/MM/DD HH24:MI') ");

            } else if (parameters.get("fromDispatchSentDate") != null) {

                queryBuffer.append("and dpi.DPI_SEND_DATE >= to_date(:fromDispatchSentDate, 'YYYY/MM/DD HH24:MI') ");

            } else if (parameters.get("toDispatchSentDate") != null) {

                queryBuffer.append("and dpi.DPI_SEND_DATE <= to_date(:toDispatchSentDate, 'YYYY/MM/DD HH24:MI') ");

            }

            Query query = em.createNativeQuery(queryBuffer.toString());
            query.setParameter("depId", userProfileTO.getDepID());
            if (parameters != null) {
                Set<String> keySet = parameters.keySet();
                if (keySet != null) {
                    for (String key : keySet) {
                        if (queryBuffer.toString().contains(":" + key)) {
                            query.setParameter(key, parameters.get(key));
                        }
                    }
                }
            }

            Number number = (Number) query.getSingleResult();
            if (number != null) {
                return number.intValue();
            }


        } catch (Exception e) {
            throw new DataException(DataExceptionCode.DSI_078,
                    DataExceptionCode.GLB_005_MSG, e);

        }

        return null;
    }

    /**
     * getEnrollmentOfficeDAO
     *
     * @return an instance of type EnrollmentOfficeDAO
     * @throws {@link BaseException}
     */
    private EnrollmentOfficeDAO getEnrollmentOfficeDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_ENROLLMENT_OFFICE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    DataExceptionCode.DSI_083,
                    DataExceptionCode.GLB_009_MSG,
                    e,
                    EMSLogicalNames.DAO_CARD.split(","));
        }
    }


}
