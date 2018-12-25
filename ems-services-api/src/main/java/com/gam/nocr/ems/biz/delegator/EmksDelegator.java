package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.EmksService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.ws.EMKSCardMoCKeysWTO;
import com.gam.nocr.ems.data.domain.ws.EMKSDataResultWTO;
import com.gam.nocr.ems.data.domain.ws.EMKSDataWTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public class EmksDelegator implements Delegator {

    private EmksService getService(UserProfileTO userProfileTO)
            throws BaseException {
        EmksService emksService;
        try {
            emksService = (EmksService) ServiceFactoryProvider
                    .getServiceFactory()
                    .getService(
                            EMSLogicalNames
                                    .getServiceJNDIName(EMSLogicalNames.SRV_EMKS), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.EMD_001,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_EMKS.split(","));
        }
        emksService.setUserProfileTO(userProfileTO);
        return emksService;
    }

    public EMKSDataResultWTO getNIDCardPINs(UserProfileTO up,
                                            EMKSDataWTO emksDataWTO, Long requestID) throws BaseException {

        return getService(up).getNIDCardPINs(emksDataWTO, requestID);

    }

	public EMKSCardMoCKeysWTO getNIDCardMoCKeys(UserProfileTO up,
                                                EMKSDataWTO emksDataWTO, Long requestID) throws BaseException {
		return getService(up).getNIDCardMoCKeys(emksDataWTO,requestID);
	}


    public String getSigniture(UserProfileTO up,
                               String str) throws BaseException {

        return getService(up).getSigniture(str);

    }


}
