package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.commons.stateprovider.StateProviderTO;

import java.util.List;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public interface StateProviderService extends Service {

    public List<StateProviderTO> getState(String moduleName, List<String> stateIds) throws BaseException;

    public void setState(String moduleName, List<StateProviderTO> records) throws BaseException;
}
