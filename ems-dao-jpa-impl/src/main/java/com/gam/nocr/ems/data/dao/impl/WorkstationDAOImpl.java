package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.WorkstationTO;
import com.gam.nocr.ems.data.enums.WorkstationState;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Stateless(name = "WorkstationDAO")
@Local(WorkstationDAOLocal.class)
@Remote(WorkstationDAORemote.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class WorkstationDAOImpl extends EmsBaseDAOImpl<WorkstationTO> implements WorkstationDAOLocal, WorkstationDAORemote {

    private static final String UNIQUE_KEY_WORKST_ACTIV_ID = "AK_WORKST_ACTIV_ID";
    private static final String UNIQUE_KEY_WORKST_CODE = "AK_WORKST_CODE";
    private static final String UNIQUE_KEY_WORKST_ACTIVATION_CODE = "WST_ACTIVATION_CODE_UNIQUE";

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * The create method, handles all the save operations for all the classes which are extended from EntityTO
     *
     * @param workstationTO - the object of type EntityTO to create
     * @return the object of type EntityTo
     */
    @Override
    public WorkstationTO create(WorkstationTO workstationTO) throws BaseException {
        try {
            WorkstationTO to = super.create(workstationTO);
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
            if (err.contains(UNIQUE_KEY_WORKST_ACTIV_ID))
                throw new DAOException(DataExceptionCode.WDI_002, DataExceptionCode.WDI_002_MSG, e);
            if (err.contains(UNIQUE_KEY_WORKST_CODE))
                throw new DAOException(DataExceptionCode.WDI_003, DataExceptionCode.WDI_003_MSG, e);
            if (err.contains(UNIQUE_KEY_WORKST_ACTIVATION_CODE))
            	throw new DAOException(DataExceptionCode.WDI_010,DataExceptionCode.WDI_010_MSG , e);
            else
                throw new DAOException(DataExceptionCode.WDI_004, DataExceptionCode.WDI_004_MSG, e);
        }
    }

    /**
     * The Update method, handles the update operations for all the classes which are extended from EntityTO.
     *
     * @param workstationTO - the object of type EntityTO to create
     * @return the object which of type EntityTO, or null if the object is not found
     */
    @Override
    public WorkstationTO update(WorkstationTO workstationTO) throws BaseException {
        try {
            WorkstationTO to = super.update(workstationTO);
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
            if (err.contains(UNIQUE_KEY_WORKST_ACTIV_ID))
                throw new DAOException(DataExceptionCode.WDI_010, DataExceptionCode.WDI_002_MSG, e);
            if (err.contains(UNIQUE_KEY_WORKST_CODE))
                throw new DAOException(DataExceptionCode.WDI_011, DataExceptionCode.WDI_003_MSG, e);
            else
                throw new DAOException(DataExceptionCode.WDI_012, DataExceptionCode.WDI_004_MSG, e);
        }
    }

    /**
     * The find method, handles the find operation on the classes which are extended from EntityTO.
     *
     * @param type - the class type of an instance of the class
     * @param id   - the id number of an instance of the class
     * @return the object which of type EntityTO, or null if the object is not found
     */
    @Override
    public WorkstationTO find(Class type, Object id) throws BaseException {
        WorkstationTO workstation = null;
        try {
            workstation = super.find(type, id);
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.WDI_001, DataExceptionCode.GLB_003_MSG, e);
        }
        return workstation;
    }

    @Override
    public boolean removeWorkstations(String workstationIds) throws BaseException {
        int workstationToDeleteCount = workstationIds.split(",").length;

        List<Long> ids = new ArrayList<Long>();
        for (String id : workstationIds.split(","))
            ids.add(Long.parseLong(id.trim()));

        int workstationDeletedCount = em.createQuery(" DELETE FROM WorkstationTO EW " +
                " where EW.id in (:ids) ")
                .setParameter("ids", ids)
                .executeUpdate();
        return workstationToDeleteCount == workstationDeletedCount;
    }

    @Override
    public WorkstationTO findByActivationCode(String workstationActivationCode) throws BaseException {
        List<WorkstationTO> workstationTOs;
        try {
            workstationTOs = em.createQuery(" select EW from WorkstationTO EW " +
                    "where EW.activationCode = :activationCode and " +
                    "EW.state = :state", WorkstationTO.class)
                    .setParameter("activationCode", workstationActivationCode)
                    .setParameter("state", WorkstationState.A)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.WDI_006, DataExceptionCode.GLB_005_MSG, e);
        }

        if (workstationTOs.size() != 0)
            return workstationTOs.get(0);
        else
            return null;
    }

    @Override
    public void approveWorkstation(String workstationIds) throws BaseException {
        List<Long> lids = new ArrayList<Long>();
        for (String id : workstationIds.split(","))
            lids.add(Long.parseLong(id.trim()));

        try {
            em.createQuery("update WorkstationTO ew " +
                    " set ew.state = '" + WorkstationState.A + "'" +
                    " where ew.id in (:ids)")
                    .setParameter("ids", lids)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.WDI_008, DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public void rejectWorkstation(String workstationIds) throws BaseException {
        List<Long> lids = new ArrayList<Long>();
        for (String id : workstationIds.split(","))
            lids.add(Long.parseLong(id.trim()));

        try {
            em.createQuery("update WorkstationTO ew " +
                    " set ew.state = '" + WorkstationState.R + "'" +
                    " where ew.id in (:ids)")
                    .setParameter("ids", lids)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.WDI_009, DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public WorkstationTO findByDepartmentIdAndActivationCode(Long departmentId,
                                                             String workstationActivationCode) throws BaseException {
        try {
            List<WorkstationTO> workstationTOs = em.createQuery("" +
                    "SELECT WST FROM WorkstationTO WST " +
                    "WHERE WST.enrollmentOffice.id = :DEPARTMENT_ID " +
                    "AND WST.activationCode = :ACTIVATION_CODE", WorkstationTO.class)
                    .setParameter("DEPARTMENT_ID", departmentId)
                    .setParameter("ACTIVATION_CODE", workstationActivationCode)
                    .getResultList();
            if (EmsUtil.checkListSize(workstationTOs)) {
                return workstationTOs.get(0);
            }
            return null;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.WDI_013, DataExceptionCode.GLB_005_MSG, e);
        }
    }
}
