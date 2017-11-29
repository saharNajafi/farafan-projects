package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.WorkstationInfoTO;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/18/17.
 */
@Stateless(name = "WorkstationInfoDAO")
@Local(WorkstationInfoDAOLocal.class)
@Remote(WorkstationInfoDAORemote.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class WorkstationInfoDAOImpl extends EmsBaseDAOImpl<WorkstationInfoTO>
        implements WorkstationInfoDAOLocal, WorkstationInfoDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * The create method, handles all the save operations for all the classes which are extended from EntityTO
     *
     * @param workstationInfoTO - the object of type EntityTO to create
     * @return the object of type EntityTo
     */
    @Override
    public WorkstationInfoTO create(WorkstationInfoTO workstationInfoTO) throws BaseException {
        try {
            WorkstationInfoTO to = super.create(workstationInfoTO);
            em.flush();
            return to;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.WDI_014, DataExceptionCode.WDI_006_MSG, e);
        }
    }

    /**
     * The Update method, handles the update operations for all the classes which are extended from EntityTO.
     *
     * @param workstationInfoTO - the object of type EntityTO to create
     * @return the object which of type EntityTO, or null if the object is not found
     */
    @Override
    public WorkstationInfoTO update(WorkstationInfoTO workstationInfoTO) throws BaseException {
        try {
            WorkstationInfoTO to = super.update(workstationInfoTO);
            em.flush();
            return to;
        } catch (Exception e) {
                throw new DAOException(DataExceptionCode.WDI_014, DataExceptionCode.WDI_006_MSG, e);
        }
    }

    @Override
    public WorkstationInfoTO isReliableVerInquiryRequired(Long workstationId) throws BaseException {
        List<WorkstationInfoTO> workstationInfoTOs = null;
        try {
            workstationInfoTOs =
                    em.createNamedQuery("WorkstationInfoTO.findByWorkstationId")
                            .setParameter("workstationId", workstationId)
                            .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.WST_002, DataExceptionCode.WST_002_MSG, e);
        }
        if (EmsUtil.checkListSize(workstationInfoTOs))
            return workstationInfoTOs.get(0);
        else
            return null;
    }
}
