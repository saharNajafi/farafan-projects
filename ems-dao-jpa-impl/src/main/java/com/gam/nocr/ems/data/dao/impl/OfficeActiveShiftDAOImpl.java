package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.OfficeActiveShiftTO;
import com.gam.nocr.ems.data.enums.ShiftEnum;
import org.slf4j.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 1/8/18.
 */
@Stateless(name = "OfficeActiveShiftDAO")
@Local(OfficeActiveShiftDAOLocal.class)
@Remote(OfficeActiveShiftDAORemote.class)
public class OfficeActiveShiftDAOImpl extends EmsBaseDAOImpl<OfficeActiveShiftTO>
        implements OfficeActiveShiftDAOLocal, OfficeActiveShiftDAORemote{
    private static final Logger logger = BaseLog
            .getLogger(OfficeActiveShiftDAOImpl.class);

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public OfficeActiveShiftTO findByEnrollmentOfficeId(Long eofId, int activeDate, ShiftEnum shiftNo) throws BaseException {

        List<OfficeActiveShiftTO> activeShiftTOList;
        try {
            activeShiftTOList = em.createNamedQuery("OfficeActiveShiftTO.findByEnrollmentOfficeId")
                    .setParameter("eofId", eofId)
                    .setParameter("activeDate", activeDate)
                    .setParameter("shiftNo", shiftNo)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.ENI_016,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return activeShiftTOList.size() != 0 ? activeShiftTOList.get(0) : null;
    }

    public void editActiveShiftRemainCapacity(Long activeShiftId, int remainCapacity) throws BaseException {
        try {
            em.createQuery("update OfficeActiveShiftTO set remainCapacity = :remainCapacity where id = :acsId")
                    .setParameter("remainCapacity", remainCapacity).setParameter("acsId", activeShiftId)
                    .setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.ASH_006,
                    DataExceptionCode.GLB_005_MSG, e);
        }
    }

    public OfficeActiveShiftTO OfficeActiveShiftByOfficeIdAndRsvDate(Long officeId, ShiftEnum shiftEnum, int myDate) throws BaseException {

        List<OfficeActiveShiftTO> activeShiftTOList;
        try {
            activeShiftTOList = em.createQuery(
                    "select oas from OfficeActiveShiftTO oas " +
                            " where oas.activeDate = :activeDate " +
                            " and oas.enrollmentOffice.id = :officeId" +
                            " and oas.shiftNo=:shiftno",
                    OfficeActiveShiftTO.class)
                    .setParameter("activeDate", myDate)
                    .setParameter("officeId", officeId)
                    .setParameter("shiftno", shiftEnum)
                    .getResultList();
        } catch (Exception e) {
            logger.error("Error occured for fetching active shift(eof,activeDate,shifNo) : ("
                    + officeId + "," + myDate + "," + shiftEnum.toString() + ")", e);
            throw new DAOException(DataExceptionCode.ASH_003,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return activeShiftTOList.size() != 0 ? activeShiftTOList.get(0) : null;
    }
}
