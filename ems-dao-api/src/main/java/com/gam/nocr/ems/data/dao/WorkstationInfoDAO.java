package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.WorkstationInfoTO;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/18/17.
 */
public interface WorkstationInfoDAO extends EmsBaseDAO<WorkstationInfoTO>{

    public WorkstationInfoTO findByWorkstationId(Long workstationId) throws BaseException;

}
