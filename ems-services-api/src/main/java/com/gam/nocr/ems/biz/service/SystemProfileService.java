package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.vol.SystemProfileVTO;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public interface SystemProfileService extends Service {
    public void save(SystemProfileVTO systemProfileVTO) throws BaseException;

    public void remove(String keyName) throws BaseException;

    public void reload() throws BaseException;
}
