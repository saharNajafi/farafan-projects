package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.WorkstationInfoTO;
import com.gam.nocr.ems.data.domain.WorkstationTO;
import com.gam.nocr.ems.data.domain.vol.ClientHardWareSpecVTO;
import com.gam.nocr.ems.data.domain.vol.ClientNetworkConfigsVTO;
import com.gam.nocr.ems.data.domain.vol.ClientSoftWareSpecVTO;
import com.gam.nocr.ems.data.domain.vol.PluginInfoVTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/18/17.
 */
public interface WorkstationInfoDAO extends EmsBaseDAO<WorkstationInfoTO>{

    public WorkstationInfoTO isReliableVerInquiryRequired(Long workstationId) throws BaseException;

}
