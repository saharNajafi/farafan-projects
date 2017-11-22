package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
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

    @Override
    public WorkstationPluginsTO findByWorkstationId(String workstationId){
        List<WorkstationPluginsTO> workstationPluginsTOs = null;
        try {
            workstationPluginsTOs =
                    em.createNamedQuery("WorkstationPluginsTO.findByWorkstationId")
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
