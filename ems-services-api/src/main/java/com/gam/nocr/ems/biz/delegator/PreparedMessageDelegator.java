package com.gam.nocr.ems.biz.delegator;

/**
 * hossein
 */

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.PreparedMessageService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.PreparedMessageTO;
import com.gam.nocr.ems.util.EmsUtil;

public class PreparedMessageDelegator implements Delegator {

	private PreparedMessageService getService(UserProfileTO userProfile)
			throws BaseException {
		PreparedMessageService messageService = null;
		try {
			messageService = (PreparedMessageService) ServiceFactoryProvider
					.getServiceFactory()
					.getService(
							EMSLogicalNames
									.getServiceJNDIName(EMSLogicalNames.SRV_PREPARED_MESSAGE), EmsUtil.getUserInfo(userProfile));
		} catch (ServiceFactoryException e) {
			throw new DelegatorException(BizExceptionCode.MSD_001,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_MESSAGE.split(","));
		}
		messageService.setUserProfileTO(userProfile);
		return messageService;
	}


	public void save(PreparedMessageTO to) throws BaseException{
		getService(null).save(to);
	}


	public void delete(UserProfileTO userProfile, String id) throws BaseException {
		getService(userProfile).delete(id);
	}


	public PreparedMessageTO load(UserProfileTO userProfile, String id) throws BaseException {
		return getService(userProfile).loadById(id);
	}


	public PreparedMessageTO downloadAttachedFile(UserProfileTO userProfile,
			String id) throws BaseException {
		return getService(userProfile).downloadById(id);
	}
	
}