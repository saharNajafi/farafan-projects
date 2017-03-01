package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.data.domain.vol.SystemProfileVTO;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import org.slf4j.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */

@Stateless(name = "SystemProfileService")
@Local(SystemProfileServiceLocal.class)
@Remote(SystemProfileServiceRemote.class)
public class SystemProfileServiceImpl extends EMSAbstractService implements SystemProfileServiceLocal, SystemProfileServiceRemote {

    private static final Logger logger = BaseLog.getLogger(SystemProfileServiceImpl.class);

    @Permissions(value = "ems_saveSystemProfile")
    @BizLoggable(logAction = "INSERT", logEntityName = "SYSTEM_PROFILE")
    public void save(SystemProfileVTO systemProfileVTO) throws BaseException {
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            pm.setProfile(systemProfileVTO.getKeyName(), systemProfileVTO.getParentKeyName(), systemProfileVTO.getValue(), true, null, null);
        } catch (Exception ex) {
            throw new ServiceException(BizExceptionCode.SPI_001, BizExceptionCode.SPI_001_MSG, ex);
        }
    }

    @Permissions(value = "ems_removeSystemProfile")
    @BizLoggable(logAction = "REMOVE", logEntityName = "SYSTEM_PROFILE")
    public void remove(String keyName) throws BaseException {
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            pm.removeProfile(keyName, true, null, null);
            pm.removeProfileKey(keyName);
            NamedCache profileKeyList = CacheFactory.getCache("repl.unlimited.gam.infra.profileKeyList");
            if (profileKeyList.containsKey(keyName))
                profileKeyList.remove(keyName);
        } catch (Exception ex) {
            throw new ServiceException(BizExceptionCode.SPI_002, BizExceptionCode.SPI_002_MSG, ex);
        }
    }

    @Permissions(value = "ems_reloadSystemProfile")
    public void reload() throws BaseException {
        try {
            NamedCache profileKeyList = CacheFactory.getCache("repl.unlimited.gam.infra.profileKeyList");
            profileKeyList.clear();
            NamedCache keyIdNameMap = CacheFactory.getCache("repl.unlimited.gam.infra.keyIdNameMap");
            keyIdNameMap.clear();
        } catch (Exception ex) {
            throw new ServiceException(BizExceptionCode.SPI_003, BizExceptionCode.SPI_003_MSG, ex);
        }
    }
}
