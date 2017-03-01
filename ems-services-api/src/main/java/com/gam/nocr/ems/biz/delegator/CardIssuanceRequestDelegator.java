package com.gam.nocr.ems.biz.delegator;

import java.util.List;
import java.util.concurrent.Future;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.nocr.ems.biz.service.CardIssuanceRequestService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.CertificateTO;
import com.gam.nocr.ems.data.enums.CertificateUsage;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class CardIssuanceRequestDelegator implements Delegator {

    private CardIssuanceRequestService getService() throws BaseException {
        ServiceFactory factory = ServiceFactoryProvider.getServiceFactory();
        CardIssuanceRequestService cardIssuanceRequestService = null;
        try {
            cardIssuanceRequestService = (CardIssuanceRequestService) factory.getService(EMSLogicalNames.getExternalServiceJNDIName(EMSLogicalNames.SRV_CARD_ISSUANCE_REQUEST), null);
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(
                    BizExceptionCode.CID_001,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_CARD_ISSUANCE_REQUEST.split(","));
        }
        return cardIssuanceRequestService;
    }

    /**
     * The method prepareCitizensInfoForSendingRequest is used to fetch the citizens' data to send them to CMS
     *
     * @return true if there are data to prepare, otherwise false
     * @throws BaseException
     */
    public boolean prepareCitizensInfoForSendingRequest() throws BaseException {
        return getService().prepareCitizensInfoForSendingRequest();

    }

    public Long getRequestsCountForIssue() throws BaseException {
        return getService().getRequestsCountForIssue();
    }
    
    public List<Long> getRequestIdsForIssue(Integer fetchLimit) throws BaseException {
        return getService().getRequestIdsForIssue(fetchLimit);
    }

    public CertificateTO findCertificateByUsage(CertificateUsage certificateUsage) throws BaseException {
        return getService().findCertificateByUsage(certificateUsage);
    }

    public void prepareDataAndSendIssuanceRequest(Integer from,
                                                  CertificateTO certificateTO) throws BaseException {
        getService().prepareDataAndSendIssuanceRequest(from, certificateTO);
    }
    
    public void prepareDataAndSendIssuanceRequestById(Long requestId,CertificateTO certificateTO) throws BaseException {
		getService().prepareDataAndSendIssuanceRequestById(requestId, certificateTO);
	}

   	public Future<String> prepareDataAndSendIssuanceRequestByIdAsync(Long requestId,	CertificateTO certificateTO) throws BaseException {
   		return getService().prepareDataAndSendIssuanceRequestByIdAsync(requestId, certificateTO);
   	}

}
