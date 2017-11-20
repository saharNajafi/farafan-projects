package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.WorkstationInfoTO;
import com.gam.nocr.ems.data.domain.vol.ClientHardWareSpecVTO;
import com.gam.nocr.ems.data.domain.vol.ClientNetworkConfigsVTO;
import com.gam.nocr.ems.data.domain.vol.ClientSoftWareSpecVTO;
import com.gam.nocr.ems.data.domain.vol.PluginInfoVTO;
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

    @Override
    public WorkstationInfoTO isReliableVerInquiryRequired(String workstationId) throws BaseException {
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

    @Override
    public String getReliableVerByPlatform(
            String workStationId, ClientHardWareSpecVTO clientHardWareSpec,
            ClientNetworkConfigsVTO clientNetworkConfig,
            ClientSoftWareSpecVTO clientSoftWareSpec) throws BaseException {
        return null;
    }

    @Override
    public String getReliableVerByPlugin(String workStationId,
                                         List<PluginInfoVTO> pluginInfoList) throws BaseException {
        return null;
    }

}
