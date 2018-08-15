package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.nocr.ems.biz.service.CardRequestService;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.*;

/**
 * Created by Saeid Rastak (saeid.rastak@gmail.com) on 10/22/2017.
 */
@Stateless(name = "InternalServiceChecker")
@Local(InternalServiceCheckerLocal.class)
@Remote(InternalServiceCheckerRemote.class)
public class InternalServiceCheckerImpl extends EMSAbstractService
implements InternalServiceCheckerLocal, InternalServiceCheckerRemote{

    /**
     * fetch card request based on crs pre registration request
     *
     * @param nationalId
     * @return
     * @throws BaseException
     */
    public CardRequestTO inquiryHasCardRequest(String nationalId) throws BaseException {

        if (nationalId == null) {
            throw new ServiceException(BizExceptionCode.ISC_001, BizExceptionCode.ISC_010_MSG);
        }
        CardRequestTO cardRequestTO = null;
        try {
            cardRequestTO = getCardRequestService().findLastRequestByNationalId(nationalId);
            if (cardRequestTO == null) {
                throw new ServiceException(BizExceptionCode.ISC_002, BizExceptionCode.ISC_002_MSG, new Object[]{nationalId});
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.ISC_002, BizExceptionCode.ISC_002_MSG, new Object[]{nationalId});
        }
        return cardRequestTO;
    }

    public void checkEditPersonalInformationEnabled(CardRequestTO cardRequestTO) throws BaseException {
        if (!checkEnrollmentInProgress(cardRequestTO)) {
            throw new ServiceException(BizExceptionCode.ISC_006, BizExceptionCode.ISC_006_MSG, new Object[]{cardRequestTO.getId()});
        }
    }

    public boolean checkEnrollmentInProgress(CardRequestTO cardRequest){
        return cardRequest.getState().equals(CardRequestState.REFERRED_TO_CCOS) ||
                cardRequest.getState().equals(CardRequestState.DOCUMENT_AUTHENTICATED) ||
                cardRequest.getState().equals(CardRequestState.RESERVED);
    }

    private CardRequestService getCardRequestService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        CardRequestService cardRequestService;
        try {
            cardRequestService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_CARD_REQUEST), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(BizExceptionCode.PTL_005,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_CARD_REQUEST.split(","));
        }
        cardRequestService.setUserProfileTO(getUserProfileTO());
        return cardRequestService;
    }

}
