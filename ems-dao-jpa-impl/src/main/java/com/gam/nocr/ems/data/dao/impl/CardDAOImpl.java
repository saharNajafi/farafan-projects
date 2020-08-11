package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.CardTO;
import com.gam.nocr.ems.data.domain.vol.CardDispatchInfoVTO;
import com.gam.nocr.ems.data.enums.CardState;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Stateless(name = "CardDAO")
@Local(CardDAOLocal.class)
@Remote(CardDAORemote.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CardDAOImpl extends EmsBaseDAOImpl<CardTO> implements CardDAOLocal, CardDAORemote {

    private static final String UNIQUE_KEY_CARD_CRN = "AK_CARD_CRN";
    @EJB
    DispatchDAOLocal dispatchDAO;

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * The create method, handles all the save operations for all the classes which are extended from EntityTO
     *
     * @param cardTO - the object of type EntityTO to create
     * @return the object of type EntityTo
     */
    @Override
    public CardTO create(CardTO cardTO) throws BaseException {
        try {
            CardTO to = super.create(cardTO);
            em.flush();
            return to;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(UNIQUE_KEY_CARD_CRN))
                throw new DAOException(DataExceptionCode.CAI_002, DataExceptionCode.CAI_002_MSG, e);
            else
                throw new DAOException(DataExceptionCode.CAI_001, DataExceptionCode.GLB_004_MSG, e);
        }
    }


    /**
     * The method updateCardsState is use to update the state of the cards in spite of their ids
     *
     * @param cardIdList a list of type Long which represents the ids for the specified cards
     * @param cardState  an enum value of type {@link com.gam.nocr.ems.data.enums.CardState}
     */
    @Override
    public void updateCardsState(List<Long> cardIdList, CardState cardState) throws BaseException {
        try {
            List<CardTO> cardList = em.createQuery("select crd from CardTO crd where crd.id in(:idList) and crd.state <> :state", CardTO.class).setParameter("idList", cardIdList).setParameter("state", CardState.MISSED).getResultList();
            em.createQuery("update CardTO crt " +
                    "set crt.state = :CARD_STATE" +
                    " where crt.id in (:ID_LIST)")
                    .setParameter("CARD_STATE", cardState)
                    .setParameter("ID_LIST", cardIdList)
                    .executeUpdate();
            em.flush();
            //Adldoost
            if (cardState == CardState.MISSED)
                dispatchDAO.notifyCardMissed(cardList);
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(UNIQUE_KEY_CARD_CRN)) {
                throw new DAOException(DataExceptionCode.CAI_005, DataExceptionCode.CAI_005_MSG, e);
            } else {
                throw new DataException(DataExceptionCode.CAI_003, DataExceptionCode.GLB_006_MSG, e);
            }
        }
    }


    /**
     * The method updateCardsStateByCRN is use to update the state of the cards in spite of the crn number of their cards
     *
     * @param crnList   a list of type String which represents the crn number for the specified cards
     * @param cardState an enum value of type {@link com.gam.nocr.ems.data.enums.CardState}
     */
    @Override
    public void updateCardsStateByCRN(List<String> crnList, CardState cardState) throws BaseException {
        try {
            em.createQuery("update CardTO crt " +
                    "set crt.state = :CARD_STATE" +
                    " where crt.crn in (:CRN_LIST)")
                    .setParameter("CARD_STATE", cardState)
                    .setParameter("CRN_LIST", crnList)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(UNIQUE_KEY_CARD_CRN)) {
                throw new DAOException(DataExceptionCode.CAI_006, DataExceptionCode.CAI_002_MSG, e);
            } else {
                throw new DataException(DataExceptionCode.CAI_004, DataExceptionCode.GLB_006_MSG, e);
            }
        }
    }

    @Override
    public void updateCardsStateByCmsId(List<String> batchIds) throws BaseException {
        try {
            em.createQuery("update CardTO crd " +
                    "set crd.state = :state" +
                    " where crd.batch.id in (select bat.id from BatchTO bat where bat.cmsID in (:ids))")
                    .setParameter("state", CardState.SHIPPED)
                    .setParameter("ids", batchIds)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(UNIQUE_KEY_CARD_CRN)) {
                throw new DAOException(DataExceptionCode.CAI_007, DataExceptionCode.CAI_002_MSG, e);
            } else {
                throw new DataException(DataExceptionCode.CAI_008, DataExceptionCode.GLB_006_MSG, e);
            }
        }
    }

    /**
     * The method findCRNByRequestId is used to find a desired crn in spite of the given requestId.
     *
     * @param requestId a number of type {@link Long} which represents the a specified request in {@link
     *                  com.gam.nocr.ems.data.domain.CardRequestTO}
     * @return an object of type {@link String} which represents the crn or null
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public String findCRNByRequestId(Long requestId) throws BaseException {
        List<String> crnList;
        try {
            crnList = em.createQuery("SELECT CRD.crn " +
                    "FROM CardTO CRD, CardRequestTO CRT " +
                    "WHERE CRD.id = CRT.card.id AND " +
                    "CRT.id = :REQUEST_ID", String.class)
                    .setParameter("REQUEST_ID", requestId)
                    .getResultList();
            if (!crnList.isEmpty()) {
                return crnList.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CAI_009, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public List<CardTO> findByBatchId(Long batchId) throws BaseException {
        try {
            return em.createQuery("SELECT CRD " +
                    "FROM CardTO CRD " +
                    "WHERE CRD.batch.id = :BATCH_ID", CardTO.class)
                    .setParameter("BATCH_ID", batchId)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CAI_010, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public void updateDeliverDate(Long requestId, Date date)
            throws BaseException {
        try {
            em.createQuery("update CardTO crt " +
                    "set crt.deliverDate = :DELIVER_DATE" +
                    " where crt.id = (SELECT CRQ.card.id FROM CardRequestTO CRQ where CRQ.id = :requestId)")
                    .setParameter("DELIVER_DATE", date)
                    .setParameter("requestId", requestId)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CAI_011, DataExceptionCode.GLB_006_MSG, e);
        }
    }

    /**
     * this method fetches cards which are lost
     *
     * @author ganjyar
     */
    @Override
    public List<CardDispatchInfoVTO> fetchCardLostTempList(
            GeneralCriteria criteria) throws BaseException {

        try {
            StringBuffer stringBuffer = new StringBuffer(
                    "select distinct crd_id,crd_batch_id, crd_crn, crd_lost_date, crd_lostconfirm, ci.CTZ_FIRST_NAME_FA fname,ci.CTZ_SURNAME_FA lname, ci.CTZ_NATIONAL_ID nid "
                            + "from emst_card crd ,emst_card_request cr,emst_dispatch_info dis, EMST_CITIZEN ci, emst_batch bc"
                            + " where ci.CTZ_ID=cr.CRQ_CITIZEN_ID "
                            + " and  dis.dpi_container_id=crd.crd_batch_id"
                            + " and  crd.crd_batch_id=bc.bat_id"
                            + " and cr.crq_card_id=crd.crd_id"
                            + " and (( dis.dpi_lost_date is null and crd_lost_date is not null and"
                            + " crd.crd_state = :cardState) or (dis.dpi_lost_date is not null "
                            + " and bc.bat_state= :batchState"
                            + " and crd_lost_date is not null"
                            + " and crd.crd_state = :cardState)) ");

            if (criteria.getParameters() != null) {
                Set<String> keySet = criteria.getParameters().keySet();
                if (keySet != null) {
                    if (keySet.contains("checkSecurity")) {
                        stringBuffer
                                .append(" and cr.crq_enroll_office_id in" +
                                        " (select dp.dep_id from emst_department dp connect by prior dp.dep_id=dp.dep_parent_dep_id start " +
                                        "with dp.dep_id in (select pr.per_dep_id from emst_person pr where pr.per_id=" + criteria.getParameters().get("perid") +
                                        " union select e.eof_id from emst_enrollment_office e where e.eof_is_deleted = 0 connect by " +
                                        "prior e.eof_id=e.eof_superior_office start with" +
                                        " e.eof_id in (select p.per_dep_id from emst_person p where p.per_id=" + criteria.getParameters().get("perid") + " ))) ");
                    }
                    for (String key : keySet) {
                        if ("crn".equals(key)) {
                            stringBuffer.append(" and crd.crd_crn like '"
                                    + criteria.getParameters().get(key) + "'");
                        }
                        if ("fromLostDate".equals(key)) {
                            stringBuffer
                                    .append(" and crd.crd_lost_date > to_date('"
                                            + criteria.getParameters().get(key)
                                            + "', 'YYYY/MM/DD HH24:MI')");
                        }
                        if ("toLostDate".equals(key)) {
                            stringBuffer
                                    .append(" and crd.crd_lost_date < to_date('"
                                            + criteria.getParameters().get(key)
                                            + "', 'YYYY/MM/DD HH24:MI')");
                        }
                        if ("waitingToConfirmed".equals(key)) {
                            stringBuffer
                                    .append(" and (crd.CRD_LOSTCONFIRM =" + criteria.getParameters().get(key)
                                            + " or crd.CRD_LOSTCONFIRM is null)");
                        }
                        if ("confirmed".equals(key)) {
                            stringBuffer
                                    .append(" and crd.CRD_LOSTCONFIRM =" + criteria.getParameters().get(key));
                        }
                        if ("fname".equals(key)) {
                            stringBuffer.append(" and ci.CTZ_FIRST_NAME_FA like '"
                                    + criteria.getParameters().get(key) + "'");
                        }
                        if ("lname".equals(key)) {
                            stringBuffer.append(" and ci.CTZ_SURNAME_FA like '"
                                    + criteria.getParameters().get(key) + "'");
                        }
                        if ("nationalId".equals(key)) {
                            stringBuffer.append(" and ci.CTZ_NATIONAL_ID like '"
                                    + criteria.getParameters().get(key) + "'");
                        }
                    }

                }
            }

            if (EmsUtil.checkString(criteria.getOrderBy())) {
                String orderBy = criteria.getOrderBy();
                String sortKey = "crd.crd_id";
                String dir = "asc";
                String[] split = orderBy.split(" ");
                if (split.length >= 2) {
                    sortKey = split[0].trim();
                    dir = split[1].trim();
                }

                if ("crn".equals(sortKey)) {
                    sortKey = "crd.crd_crn";
                } else if ("batchId".equals(sortKey)) {
                    sortKey = "crd.crd_batch_id";
                } else if ("isConfirm".equals(sortKey)) {
                    sortKey = "crd.crd_lostconfirm";
                } else if ("cardLostDate".equals(sortKey)) {
                    sortKey = "crd.crd_lost_date";
                }

                stringBuffer.append(" order by ").append(sortKey).append(" ")
                        .append(dir);
            } else {
                stringBuffer.append(" order by crd.crd_id asc ");
            }

            Query query = em.createNativeQuery(stringBuffer.toString());
            query.setParameter("batchState", CardState.RECEIVED.toString());
            query.setParameter("cardState", CardState.SHIPPED.toString());
            query.setMaxResults(criteria.getPageSize()).setFirstResult(
                    criteria.getPageNo() * criteria.getPageSize());

            List resultList = query.getResultList();
            List<CardDispatchInfoVTO> result = new ArrayList<CardDispatchInfoVTO>();
            if (resultList != null) {
                for (Object record : resultList) {
                    Object[] data = (Object[]) record;
                    CardDispatchInfoVTO obj = new CardDispatchInfoVTO();
                    obj.setId(((BigDecimal) data[0]).longValue());
                    obj.setBatchId(((BigDecimal) data[1]).toString());
                    obj.setCrn((String) data[2]);
                    obj.setCardLostDate((Timestamp) data[3]);
                    if (data[4] == null)
                        obj.setIsConfirm("0");
                    else
                        obj.setIsConfirm(((BigDecimal) data[4]).toString());
                    obj.setFname((String) data[5]);
                    obj.setLname((String) data[6]);
                    obj.setNationalId((String) data[7]);
                    result.add(obj);
                }
            }
            return result;

        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CAI_017,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    /**
     * this method fetches cards which are lost
     *
     * @author ganjyar
     */
    @Override
    public Integer countCardLostTemp(GeneralCriteria criteria)
            throws BaseException {

        try {
            StringBuffer stringBuffer = new StringBuffer("select count(*) "
                    + "from emst_card crd ,emst_card_request cr,emst_dispatch_info dis, EMST_CITIZEN ci, emst_batch bc"
                    + " where ci.CTZ_ID=cr.CRQ_CITIZEN_ID "
                    + " and  dis.dpi_container_id=crd.crd_batch_id"
                    + " and  crd.crd_batch_id=bc.bat_id"
                    + " and cr.crq_card_id=crd.crd_id"
                    + " and ((dis.dpi_lost_date is null and crd_lost_date is not null and"
                    + " crd.crd_state = :cardState) or (dis.dpi_lost_date is not null "
                    + " and bc.bat_state= :batchState"
                    + " and crd_lost_date is not null"
                    + " and crd.crd_state = :cardState))");

            if (criteria.getParameters() != null) {
                Set<String> keySet = criteria.getParameters().keySet();
                if (keySet != null) {
                    if (keySet.contains("checkSecurity")) {
                        stringBuffer
                                .append(" and cr.crq_enroll_office_id in" +
                                        " (select dp.dep_id from emst_department dp connect by prior dp.dep_id=dp.dep_parent_dep_id start " +
                                        "with dp.dep_id in (select pr.per_dep_id from emst_person pr where pr.per_id=" + criteria.getParameters().get("perid") +
                                        " union select e.eof_id from emst_enrollment_office e where e.eof_is_deleted = 0 connect by " +
                                        "prior e.eof_id=e.eof_superior_office start with" +
                                        " e.eof_id in (select p.per_dep_id from emst_person p where p.per_id=" + criteria.getParameters().get("perid") + " ))) ");
                    }
                    for (String key : keySet) {
                        if ("crn".equals(key)) {
                            stringBuffer.append(" and crd.crd_crn like '"
                                    + criteria.getParameters().get(key) + "'");
                        }
                        if ("fromLostDate".equals(key)) {
                            stringBuffer
                                    .append(" and crd.crd_lost_date > to_date('"
                                            + criteria.getParameters().get(key)
                                            + "', 'YYYY/MM/DD HH24:MI')");
                        }
                        if ("toLostDate".equals(key)) {
                            stringBuffer
                                    .append(" and crd.crd_lost_date < to_date('"
                                            + criteria.getParameters().get(key)
                                            + "', 'YYYY/MM/DD HH24:MI')");
                        }
                        if ("waitingToConfirmed".equals(key)) {
                            stringBuffer
                                    .append(" and (crd.CRD_LOSTCONFIRM =" + criteria.getParameters().get(key)
                                            + " or crd.CRD_LOSTCONFIRM is null)");
                        }
                        if ("confirmed".equals(key)) {
                            stringBuffer
                                    .append(" and crd.CRD_LOSTCONFIRM =" + criteria.getParameters().get(key));
                        }
                    }

                }
            }

            Query query = em.createNativeQuery(stringBuffer.toString());
            query.setParameter("batchState", CardState.RECEIVED.toString());
            query.setParameter("cardState", CardState.SHIPPED.toString());

            Number number = (Number) query.getSingleResult();

            if (number != null) {
                return number.intValue();
            }

        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CAI_016,
                    DataExceptionCode.GLB_006_MSG, e);
        }

        return null;
    }

    /**
     * this method used to update all cards of a batch when a lost batch is confirmed by 3s.
     * then all cards of the batch must confirm.
     *
     * @author ganjyar
     */
    @Override
    public void updateLostConfirmBytBatchConfirm(Long batchId)
            throws BaseException {

        try {
            em.createQuery(
                    "update CardTO crt "
                            + "set crt.isLostCardConfirmed = :confirm"
                            + " where crt.batch.id = :batchId")
                    .setParameter("batchId", batchId)
                    .setParameter("confirm", true).executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CAI_013,
                    DataExceptionCode.GLB_006_MSG, e);
        }

    }

    /**
     * this method used to update all cards of a batch when a lost batch is unconfirmed by 3s.
     * then all cards lost-date will be null.
     *
     * @author ٔNamjoofar
     */
    @Override
    public void unconfirmAllCardsOfBatch(Long batchId) throws BaseException {
        try {
            em.createQuery(
                    "update CardTO crt "
                            + "set crt.lostDate = :lostDate"
                            + " where crt.batch.id = :batchId")
                    .setParameter("batchId", batchId)
                    .setParameter("lostDate", null).executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CAI_018,
                    DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public Long countCardLostDate(Long batchId) throws BaseException {
        List<Long> countCardLostInBatchList;
        try {
            countCardLostInBatchList = em.createNamedQuery("CardTO.countCardLostInBatch")
                    .setParameter("batchId", batchId)
                    .getResultList();
            if (EmsUtil.checkListSize(countCardLostInBatchList)) {
                return countCardLostInBatchList.get(0);
            }
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CAI_019,
                    DataExceptionCode.GLB_011_MSG, e);
        }
        return null;
    }
}
