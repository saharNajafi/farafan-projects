package com.gam.nocr.ems.biz.delegator;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.MessageService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.MessageDestinationTO;
import com.gam.nocr.ems.data.domain.ws.MessageWTO;
import com.gam.nocr.ems.util.EmsUtil;

public class MessageDelegator implements Delegator {

	private MessageService getService(UserProfileTO userProfileTO)
			throws BaseException {
		MessageService messageService = null;
		try {
			messageService = (MessageService) ServiceFactoryProvider
					.getServiceFactory()
					.getService(
							EMSLogicalNames
									.getServiceJNDIName(EMSLogicalNames.SRV_MESSAGE), EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new DelegatorException(BizExceptionCode.MSD_001,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_MESSAGE.split(","));
		}
		messageService.setUserProfileTO(userProfileTO);
		return messageService;
	}

	public List<Long> fetchReadyToProcessMessage() throws BaseException {
		return getService(null).fetchReadyToProcessMessage();
	}

	public void processMessage(Long id) throws BaseException {
		 getService(null).processMessage(id);
		
	}

	
	public List<MessageDestinationTO> getMessageByCriteria(UserProfileTO userProfileTO , MessageWTO wto) throws BaseException{
		return getService(userProfileTO).getMessageByCriteriagetMessageByCriteria(userProfileTO, wto);
	}


	public MessageDestinationTO getMessageDestinationById(UserProfileTO userProfileTO, Long msgId, Long personId) throws BaseException{
		return getService(userProfileTO).getMessageDestinationById(msgId, personId);
	}

	public void deleteMessageDestinationBypesonId(UserProfileTO userProfileTO, Long msgId, Long personId) throws BaseException{
		 getService(userProfileTO).deleteMessageDestinationByPesonId(msgId,personId);
		
	}
}