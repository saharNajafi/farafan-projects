package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.AfterDeliveryService;
import com.gam.nocr.ems.biz.service.CardManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.ws.ImsCitizenInfoRequestWTO;
import com.gam.nocr.ems.data.domain.ws.ImsCitizenInfoResponseWTO;
import com.gam.nocr.ems.data.enums.AfterDeliveryRequestType;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class CardDelegator implements Delegator {
	
	private CardManagementService getCardManagementService(UserProfileTO userProfileTO) throws BaseException {
	        ServiceFactory factory = ServiceFactoryProvider.getServiceFactory();
	        CardManagementService cardManagementService = null;
	        try {
	        	cardManagementService = (CardManagementService) factory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_CARD_MANAGEMENT),userProfileTO.getUserName());
	        } catch (ServiceFactoryException e) {
	            throw new DelegatorException(BizExceptionCode.CDL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_AFTER_DELIVERY.split(","));
	        }
	        cardManagementService.setUserProfileTO(userProfileTO);
	        return cardManagementService;
	    }
    private AfterDeliveryService getService(UserProfileTO userProfileTO) throws BaseException {
        ServiceFactory factory = ServiceFactoryProvider.getServiceFactory();
        AfterDeliveryService afterDeliveryService = null;
        try {
            afterDeliveryService = (AfterDeliveryService) factory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_AFTER_DELIVERY), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.CDL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_AFTER_DELIVERY.split(","));
        }
        afterDeliveryService.setUserProfileTO(userProfileTO);
        return afterDeliveryService;
    }

    public void doAfterDelivery(UserProfileTO userProfileTO, long requestId, AfterDeliveryRequestType requestType, String complementaryData) throws BaseException {
        getService(userProfileTO).doAfterDelivery(requestId, requestType, complementaryData);
    }

    /**
     * @param certificate
     * @param userProfileTO
     * @throws BaseException
     * @author Sina Golesorkhi
     */
    public void doOCSPVerification(byte[] certificate, UserProfileTO userProfileTO) throws BaseException {
        getService(userProfileTO).doOCSPVerification(certificate);
    }
    /**
     * @author ganjyar
     */
	public ImsCitizenInfoResponseWTO doImsVerificationInDelivery(
			UserProfileTO userProfileTO,
			ImsCitizenInfoRequestWTO imsCitizenInfoRequestWTO) throws BaseException{
		return	getCardManagementService(userProfileTO).doImsVerificationInDelivery(imsCitizenInfoRequestWTO);
	}
}
