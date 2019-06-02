package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;
import com.gam.nocr.ems.data.enums.ShiftEnum;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 1/8/18.
 */
@Stateless(name = "OfficeCapacityDAO")
@Local(OfficeCapacityDAOLocal.class)
@Remote(OfficeCapacityRemote.class)
public class OfficeCapacityDAOImpl extends EmsBaseDAOImpl<OfficeCapacityTO> implements OfficeCapacityDAOLocal, OfficeCapacityRemote {

    private static final String FOREIGN_KEY_BOX_NXT_REC_DEP_ID = "FK_BOX_NXT_REC_DEP_ID";
    private static final String FOREIGN_KEY_BATCH_NXT_REC_DEP_ID = "FK_BATCH_NXT_REC_DEP_ID";
    private static final String FOREIGN_KEY_DEP_PROFILE_DEP_ID = "FK_DEP_PROFILE_DEP_ID";
    private static final String FOREIGN_KEY_PER_DEP_ID = "FK_PER_DEP_ID";
    private static final String FOREIGN_KEY_WORKST_ENROLL_OFC_ID = "FK_WORKST_ENROLL_OFC_ID";
    private static final String FOREIGN_KEY_NET_TKN_ENR_OFC_ID = "FK_NET_TKN_ENR_OFC_ID";
    private static final String FOREIGN_KEY_RESERV_ENR_OFC_ID = "FK_RESERV_ENR_OFC_ID";
    private static final String FOREIGN_KEY_CARD_REQ_ENROLL_OFC_ID = "FK_CARD_REQ_ENROLL_OFC_ID";
    private static final String FOREIGN_KEY_EOF_SUPERIOR_OFFICE = "FK_EOF_SUPERIOR_OFFICE";
    private static final String FOREIGN_KEY_CRQ_ORG_ENROLL_OFFICE_ID = "FK_CRQ_ORG_ENROLL_OFFICE_ID";

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<OfficeCapacityTO> findByEnrollmentOfficeIdAndShiftNo(Long enrollmentOfficeId, ShiftEnum shiftNo) throws BaseException {
        List<OfficeCapacityTO> officeCapacityList;
        try {
            officeCapacityList = em.createNamedQuery("officeCapacityTO.findByEnrollmentOfficeIdAndShiftNo")
                    .setParameter("enrollmentOfficeId", enrollmentOfficeId)
                    .setParameter("shiftNo", shiftNo)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.OCD_002, DataExceptionCode.OCD_002_MSG, e);
        }
        return officeCapacityList.size() != 0 ? officeCapacityList : null;
    }

    @Override
    public OfficeCapacityTO create(OfficeCapacityTO officeCapacityTO) throws BaseException {
        try {
            OfficeCapacityTO officeCapacity = super.create(officeCapacityTO);
            em.flush();
            return officeCapacity;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("AK_OFFICE_CAPACITY_CLASS"))
                throw new DAOException(DataExceptionCode.OCD_001, DataExceptionCode.OCD_001_MSG, e);
            if (err.contains("AK_OFFICE_CAPACITY_SIZE"))
                throw new DAOException(DataExceptionCode.OCD_003, DataExceptionCode.OCD_003_MSG, e);
            throw new DAOException(DataExceptionCode.OCD_004, DataExceptionCode.GLB_004_MSG, e);
        }
    }

    @Override
    public OfficeCapacityTO update(OfficeCapacityTO officeCapacityTO) throws BaseException {
        try {
            OfficeCapacityTO officeCapacity = super.update(officeCapacityTO);
            em.flush();
            return officeCapacity;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("AK_OFFICE_CAPACITY_CLASS"))
                throw new DAOException(DataExceptionCode.OCD_006, DataExceptionCode.OCD_001_MSG, e);
            if (err.contains("AK_OFFICE_CAPACITY_SIZE"))
                throw new DAOException(DataExceptionCode.OCD_007, DataExceptionCode.OCD_003_MSG, e);
            throw new DAOException(DataExceptionCode.OCD_005, DataExceptionCode.GLB_004_MSG, e);
        }
    }

    @Override
    public boolean removeOfficeCapacities(String officeCapacityId) throws BaseException {
        boolean result = false;
        try {
            OfficeCapacityTO officeCapacityTO = em.find(OfficeCapacityTO.class, officeCapacityId);
            em.remove(officeCapacityId);
            result = true;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains(FOREIGN_KEY_BOX_NXT_REC_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_036, DataExceptionCode.DDI_036_MSG, e);
            if (err.contains(FOREIGN_KEY_BATCH_NXT_REC_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_037, DataExceptionCode.DDI_037_MSG, e);
            if (err.contains(FOREIGN_KEY_DEP_PROFILE_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_038, DataExceptionCode.DDI_038_MSG, e);
            if (err.contains(FOREIGN_KEY_PER_DEP_ID))
                throw new DAOException(DataExceptionCode.DDI_039, DataExceptionCode.DDI_039_MSG, e);
            if (err.contains(FOREIGN_KEY_WORKST_ENROLL_OFC_ID))
                throw new DAOException(DataExceptionCode.DDI_041, DataExceptionCode.DDI_041_MSG, e);
            if (err.contains(FOREIGN_KEY_NET_TKN_ENR_OFC_ID))
                throw new DAOException(DataExceptionCode.DDI_042, DataExceptionCode.DDI_042_MSG, e);
            if (err.contains(FOREIGN_KEY_RESERV_ENR_OFC_ID))
                throw new DAOException(DataExceptionCode.DDI_043, DataExceptionCode.DDI_043_MSG, e);
            if (err.contains(FOREIGN_KEY_CARD_REQ_ENROLL_OFC_ID))
                throw new DAOException(DataExceptionCode.DDI_044, DataExceptionCode.DDI_044_MSG, e);
            if (err.contains(FOREIGN_KEY_EOF_SUPERIOR_OFFICE))
                throw new DAOException(DataExceptionCode.DDI_052, DataExceptionCode.DDI_052_MSG, e);
            if (err.contains(FOREIGN_KEY_CRQ_ORG_ENROLL_OFFICE_ID))
                throw new DAOException(DataExceptionCode.DDI_053, DataExceptionCode.DDI_053_MSG, e);
            if (err.contains("integrity constraint"))
                throw new DAOException(DataExceptionCode.DDI_045, DataExceptionCode.DDI_045_MSG, e);
            throw new DAOException(DataExceptionCode.DDI_046, DataExceptionCode.DDI_046_MSG, e);
        }

        return result;
    }

    @Override
    public OfficeCapacityTO findByEnrollmentOfficeId(Long eofId)
            throws BaseException {
        List<OfficeCapacityTO> officeCapacityTOList;
        try {
            officeCapacityTOList = em.createNamedQuery("OfficeCapacityTO.findByEnrollmentOfficeId")
                    .setParameter("eofId", eofId)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.OCD_008,
                    DataExceptionCode.OCD_008_MSG, e);
        }
        return officeCapacityTOList.size() != 0 ? officeCapacityTOList.get(0) : null;
    }

    @Override
    public List<OfficeCapacityTO> listOfficeCapacityByDate(int startDate, int endDate) throws DAOException {
        List<OfficeCapacityTO> officeCapacityTOList;
        try {
            officeCapacityTOList = em.createNamedQuery("OfficeCapacityTO.listOfficeCapacityByDate")
                    .setParameter("endDate", endDate)
                    .setParameter("startDate", startDate)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.OFC_003,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return officeCapacityTOList.size() != 0 ? officeCapacityTOList : null;
    }

    @Override
    public OfficeCapacityTO findByEnrollmentOfficeIdAndDateAndWorkingHour(Long eofId, int date, float hour) throws DAOException {
        List<OfficeCapacityTO> officeCapacityTOList;
        try {
            officeCapacityTOList = em.createNamedQuery("OfficeCapacityTO.findByEnrollmentOfficeIdAndDateAndWorkingHour")
                    .setParameter("eofId", eofId)
                    .setParameter("date", date)
                    .setParameter("hour", hour)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.OFC_003,
                    DataExceptionCode.GLB_005_MSG, e);
        }
        return officeCapacityTOList.size() != 0 ? officeCapacityTOList.get(0) : null;
    }


    @Override
    public void removeByEnrollmentOfficeId(Long enrollmentOfficeId) throws BaseException {
        try {
            em.createQuery("delete OfficeCapacityTO capacity " +
                    "where capacity.enrollmentOffice.id=:enrollmentOfficeId")
                    .setParameter("enrollmentOfficeId", enrollmentOfficeId)
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException(
                    DataExceptionCode.OFC_004,
                    DataExceptionCode.GLB_012_MSG,
                    e);
        }
    }

    @Override
    public List<OfficeCapacityTO> findByEnrollmentOfficeIdAndShiftNoAndOcId(
            Long enrollmentOfficeId, ShiftEnum shiftNo, Long id) throws DAOException {
        List<OfficeCapacityTO> officeCapacityList;
        try {
            officeCapacityList =
                    em.createNamedQuery("officeCapacityTO.findByEnrollmentOfficeIdAndShiftNoAndOcId")
                            .setParameter("enrollmentOfficeId", enrollmentOfficeId)
                            .setParameter("shiftNo", shiftNo)
                            .setParameter("id", id)
                            .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.OCD_009, DataExceptionCode.OCD_009_MSG, e);
        }
        return officeCapacityList.size() != 0 ? officeCapacityList : null;
    }
}
