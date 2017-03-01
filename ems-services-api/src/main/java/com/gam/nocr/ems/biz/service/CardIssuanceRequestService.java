package com.gam.nocr.ems.biz.service;

import java.util.List;
import java.util.concurrent.Future;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.CertificateTO;
import com.gam.nocr.ems.data.enums.CertificateUsage;

/**
 * The interface CardIssuanceRequestService is used to prepare the necessary data in calling the issue card service of
 * the sub system 'CMS'.
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface CardIssuanceRequestService extends Service {

    /**
     * The method prepareCitizensInfoForSendingRequest is used to gathering necessary information of the citizens,
     * whenever
     * the requests is ready to send , by calling the issue card service of the sub system 'CMS'.
     *
     * @return true if there are data to prepare, otherwise false
     * @throws BaseException
     */
    public boolean prepareCitizensInfoForSendingRequest() throws BaseException;

    Long getRequestsCountForIssue() throws BaseException;

    /**
     * The method findCertificateByUsage is used to find a certificate due to its usage
     */
    CertificateTO findCertificateByUsage(CertificateUsage certificateUsage) throws BaseException;

    void prepareDataAndSendIssuanceRequest(Integer from,
                                           CertificateTO certificateTO) throws BaseException;

	List<Long> getRequestIdsForIssue(Integer fetchLimit) throws BaseException;
	
	void prepareDataAndSendIssuanceRequestById(Long requestId,CertificateTO certificateTO) throws BaseException;

	Future<String> prepareDataAndSendIssuanceRequestByIdAsync(Long requestId,CertificateTO certificateTO) throws BaseException;

}
