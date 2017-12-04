package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.DispatchingService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.CardTO;
import com.gam.nocr.ems.data.domain.ws.DispatchInfoWTO;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;
import com.gam.nocr.ems.util.EmsUtil;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class DispatchingDelegator implements Delegator {

    private DispatchingService getService(UserProfileTO userProfileTO) throws BaseException {
        DispatchingService dispatchingService = null;
        try {
            dispatchingService = (DispatchingService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_DISPATCHING), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.IDL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_DISPATCHING.split(","));
        }
        dispatchingService.setUserProfileTO(userProfileTO);
        return dispatchingService;
    }

    public void batchProduction(UserProfileTO userProfileTO, String batchId, List<CardTO> cards) throws BaseException {
        getService(userProfileTO).batchProduction(batchId, cards);
    }

    public void batchProduction(UserProfileTO userProfileTO, String batchId, List<CardTO> cards,String postalTrackingCode) throws BaseException {
        getService(userProfileTO).batchProduction(batchId, cards, postalTrackingCode);
    }

    public void boxShipped(UserProfileTO userProfileTO, String boxId, List<String> batchIds) throws BaseException {
        getService(userProfileTO).boxShipped(boxId, batchIds);
    }

    public void cardProductionError(UserProfileTO userProfileTO, String requestID, String errorCode, String description) throws BaseException {
        getService(userProfileTO).cardProductionError(requestID, errorCode, description);
    }

    public void itemLost(UserProfileTO userProfileTO, String ids, String detailIds, String cardIds) throws BaseException {
        getService(userProfileTO).itemLost(ids, detailIds, cardIds);
    }

    public void itemFound(UserProfileTO userProfileTO, String ids, String detailIds, String cardIds) throws BaseException {
        getService(userProfileTO).itemFound(ids, detailIds, cardIds);
    }

    public void itemReceived(UserProfileTO userProfileTO, String ids, String detailIds, String cardIds) throws BaseException {
        getService(userProfileTO).itemReceived(ids, detailIds, cardIds);
    }

    public void itemNotReceived(UserProfileTO userProfileTO, String ids, String detailIds, String cardIds) throws BaseException {
        getService(userProfileTO).itemNotReceived(ids, detailIds, cardIds);
    }

    public void itemSent(UserProfileTO userProfileTO, String ids, String detailIds) throws BaseException {
        getService(userProfileTO).itemSent(ids, detailIds);
    }

    public void undoSend(UserProfileTO userProfileTO, String ids, String detailIds) throws BaseException {
        getService(userProfileTO).undoSend(ids, detailIds);
    }

    public void backToInitialState(UserProfileTO userProfileTO, String ids, String detailIds, String cardIds) throws BaseException {
        getService(userProfileTO).backToInitialState(ids, detailIds, cardIds);
    }

	public List<DispatchInfoWTO> fetchBatchDispatchList(GeneralCriteria criteria,
			UserProfileTO userProfileTO)throws BaseException {
		return getService(userProfileTO).fetchBatchDispatchList(criteria, userProfileTO);
		
	}

	public Integer countBatchDispatchList(HashMap parameters, UserProfileTO userProfileTO) throws BaseException {
		return getService(userProfileTO).countBatchDispatchList(parameters, userProfileTO);
	}

    public void updateBatchPostalTrackingCode(UserProfileTO userProfileTO,String batchId,String postalTrackingCode) throws BaseException{
         getService(userProfileTO).updateBatchPostalTrackingCode(batchId, postalTrackingCode);
    }
    
}
