package com.gam.nocr.ems.biz.delegator;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.OutgoingSMSService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.util.EmsUtil;

public class OutgoingSMSDelegator implements Delegator {

	private OutgoingSMSService getService(UserProfileTO userProfileTO)
			throws BaseException {
		OutgoingSMSService outgoingSMSService = null;
		try {
			outgoingSMSService = (OutgoingSMSService) ServiceFactoryProvider
					.getServiceFactory()
					.getService(
							EMSLogicalNames
									.getServiceJNDIName(EMSLogicalNames.SRV_OUTGOING_SMS),
							EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new DelegatorException(BizExceptionCode.BLD_001,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_OUTGOING_SMS.split(","));
		}
		outgoingSMSService.setUserProfileTO(userProfileTO);
		return outgoingSMSService;
	}

	public List<Long> fetchMessagesId(Integer sendSmsType, Integer fetchLimit) throws BaseException {
		return getService(null).fetchMessagesId(sendSmsType, fetchLimit);
	}

	public void processSmsToSend(Long id) throws BaseException {
		getService(null).processSmsToSend(id);
		
	}
	
	
	
	
	
	

}
