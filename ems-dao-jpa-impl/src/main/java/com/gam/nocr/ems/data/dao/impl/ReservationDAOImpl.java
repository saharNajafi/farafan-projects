package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.ReservationTO;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "ReservationDAO")
@Local(ReservationDAOLocal.class)
public class ReservationDAOImpl extends EmsBaseDAOImpl<ReservationTO> implements ReservationDAOLocal, ReservationDAORemote {

    private static final String UNIQUE_KEY_PORTAL_RESERVATION_ID = "AK_PORTAL_RESERVATION_ID";
    private static final String FOREIGN_KEY_RESERV_ENR_OFC_ID = "FK_RESERV_ENR_OFC_ID";
    private static final String FOREIGN_KEY_RESERV_REQ_ID = "FK_RESERV_REQ_ID";

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public ReservationTO create(ReservationTO reservationTO) throws BaseException {
        try {
            ReservationTO rTO = super.create(reservationTO);
            em.flush();
            return rTO;
        } catch (Exception e) {
            String exceptionMessage = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null) {
                    exceptionMessage = e.getCause().getCause().getMessage();

                } else {
                    exceptionMessage = e.getCause().getMessage();
                }
            }

            if (exceptionMessage.contains(UNIQUE_KEY_PORTAL_RESERVATION_ID)) {
                throw new DAOException(DataExceptionCode.RSI_002, DataExceptionCode.RSI_002_MSG, e);
            }
            if (exceptionMessage.contains(FOREIGN_KEY_RESERV_ENR_OFC_ID)) {
                throw new DAOException(DataExceptionCode.RSI_009, DataExceptionCode.RSI_009_MSG, e);
            }
            throw new DAOException(DataExceptionCode.RSI_001, DataExceptionCode.GLB_004_MSG, e);
        }
    }

    @Override
    public ReservationTO find(Class type, Object id) throws BaseException {
        ReservationTO rTO = null;
        try {
            rTO = super.find(type, id);
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RSI_005, DataExceptionCode.GLB_003_MSG, e);
        }
        return rTO;
    }

    @Override
    public ReservationTO update(ReservationTO reservationTO) throws BaseException {
        try {
            ReservationTO rTO = super.update(reservationTO);
            em.flush();
            return rTO;
        } catch (Exception e) {
            String exceptionMessage = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null) {
                    exceptionMessage = e.getCause().getCause().getMessage();

                } else {
                    exceptionMessage = e.getCause().getMessage();
                }
            }

            if (exceptionMessage.contains(UNIQUE_KEY_PORTAL_RESERVATION_ID)) {
                throw new DAOException(DataExceptionCode.RSI_003, DataExceptionCode.RSI_002_MSG, e);
            }
            if (exceptionMessage.contains(FOREIGN_KEY_RESERV_ENR_OFC_ID)) {
                throw new DAOException(DataExceptionCode.RSI_010, DataExceptionCode.RSI_009_MSG, e);
            }
            throw new DAOException(DataExceptionCode.RSI_004, DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public void delete(ReservationTO reservationTO) throws BaseException {
        try {
            super.delete(reservationTO);
            em.flush();
        } catch (Exception e) {
            String exceptionMessage = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null) {
                    exceptionMessage = e.getCause().getCause().getMessage();

                } else {
                    exceptionMessage = e.getCause().getMessage();
                }
            }

            if (exceptionMessage.contains(FOREIGN_KEY_RESERV_ENR_OFC_ID)) {
                throw new DAOException(DataExceptionCode.RSI_006, DataExceptionCode.RSI_006_MSG, e);

            } else if (exceptionMessage.contains(FOREIGN_KEY_RESERV_REQ_ID)) {
                throw new DAOException(DataExceptionCode.RSI_007, DataExceptionCode.RSI_007_MSG, e);
            }
            throw new DAOException(DataExceptionCode.RSI_008, DataExceptionCode.GLB_007_MSG, e);

        }
    }

    @Override
    public ReservationTO fetchReservationByPortalReservationId(Long portalReservationId) throws BaseException {
        List<ReservationTO> reservationTOList;
        try {
            reservationTOList = em.createQuery(" select res from ReservationTO res " +
                    " where res.portalReservationId = :portalReservationId order by res.id asc", ReservationTO.class)
                    .setParameter("portalReservationId", portalReservationId)
                    .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.RSI_011, DataExceptionCode.GLB_005_MSG, e);
        }

        if (!reservationTOList.isEmpty())
            return reservationTOList.get(0);
        else
            return null;
    }

    @Override
    public Boolean deleteByCardRequest(Long cardRequestId) throws BaseException {
        int result;
        try {
            result = em.createQuery("delete from ReservationTO rs " +
                    "where rs.cardRequest.id = :cardRequestId")
                    .setParameter("cardRequestId", cardRequestId)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.RSI_012, DataExceptionCode.GLB_007_MSG, e, new Long[]{cardRequestId});
        }
        return result == 1;
    }

    @Override
    public ReservationTO findReservationByCrqId(Long carqId) throws DataException {
        List<ReservationTO> reservationTOList;
        try {
            reservationTOList =
                    em.createNamedQuery("ReservationTO.findReservationByCrqId")
                            .setParameter("carqId", carqId)
                            .getResultList();
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.RSI_002, DataExceptionCode.GLB_005_MSG, e);
        }
        return !reservationTOList.isEmpty() ? reservationTOList.get(0) : null;
    }


}
