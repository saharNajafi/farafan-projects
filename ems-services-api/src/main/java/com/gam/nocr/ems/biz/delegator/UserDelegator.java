package com.gam.nocr.ems.biz.delegator;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.UserManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.CertificateTO;
import com.gam.nocr.ems.data.domain.vol.UserVTO;
import com.gam.nocr.ems.data.enums.CertificateUsage;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public class UserDelegator implements Delegator {

    private UserManagementService getService(UserProfileTO userProfileTO) throws BaseException {
        UserManagementService userManagementService;
        try {
            userManagementService = (UserManagementService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_USER), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.UDL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_USER.split(","));
        }
        userManagementService.setUserProfileTO(userProfileTO);
        return userManagementService;
    }

    public void changePassword(UserProfileTO userProfileTO, UserVTO userVTO) throws BaseException {
        getService(userProfileTO).changePassword(userVTO);
    }

    public String fetchUserInfo(UserProfileTO userProfileTO) throws BaseException {
        return getService(userProfileTO).fetchUserInfo();
    }

    public CertificateTO getCertificateByUsage(UserProfileTO userProfileTO, CertificateUsage usage) throws BaseException {
        return getService(userProfileTO).getCertificateByUsage(usage);
    }
	public List<String> getUserAccess(UserProfileTO up) throws BaseException{
		return getService(up).getUserAccess(up);
	}
	
	//	Anbari - userPerm-commented
	/*public void updatePermissionCache(Long personID) throws BaseException {
		getService(null).updatePermissionCache(personID);
		
	}*/
}
