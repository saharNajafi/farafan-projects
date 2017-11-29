package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.WorkstationInfoTO;
import com.gam.nocr.ems.data.domain.WorkstationPluginsTO;
import com.gam.nocr.ems.util.EmsUtil;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/21/17.
 */
@Stateless(name = "WorkstationPluginsDAO")
@Local(WorkstationPluginsDAOLocal.class)
@Remote(WorkstationPluginsDAORemote.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class WorkstationPluginsDAOImpl extends EmsBaseDAOImpl<WorkstationPluginsTO>
        implements WorkstationPluginsDAOLocal, WorkstationPluginsDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * The create method, handles all the save operations for all the classes which are extended from EntityTO
     *
     * @param workstationPluginsTO - the object of type EntityTO to create
     * @return the object of type EntityTo
     */
    @Override
    public WorkstationPluginsTO create(WorkstationPluginsTO workstationPluginsTO) throws BaseException {
        try {
            WorkstationPluginsTO to = super.create(workstationPluginsTO);
            em.flush();
            return to;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.WDI_015, DataExceptionCode.WDI_007_MSG, e);
        }
    }

    /**
     * The Update method, handles the update operations for all the classes which are extended from EntityTO.
     *
     * @param workstationPluginsTO - the object of type EntityTO to create
     * @return the object which of type EntityTO, or null if the object is not found
     */
    @Override
    public WorkstationPluginsTO update(WorkstationPluginsTO workstationPluginsTO) throws BaseException {
        try {
            WorkstationPluginsTO to = super.update(workstationPluginsTO);
            em.flush();
            return to;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.WDI_015, DataExceptionCode.WDI_006_MSG, e);
        }
    }

    @Override
    public WorkstationPluginsTO findByWorkstationById(Long workstationId){
        List<WorkstationPluginsTO> workstationPluginsTOs = null;
        try {
            workstationPluginsTOs =
                    em.createNamedQuery("WorkstationPluginsTO.findByWorkstationById")
                            .setParameter("workstationId", workstationId)
                            .getResultList();
        } catch (Exception e) {
            try {
                throw new DAOException(DataExceptionCode.WST_002, DataExceptionCode.WST_002_MSG, e);
            } catch (DAOException e1) {
                e1.printStackTrace();
            }
        }
        if (EmsUtil.checkListSize(workstationPluginsTOs))
            return workstationPluginsTOs.get(0);
        else
            return null;
    }
}
