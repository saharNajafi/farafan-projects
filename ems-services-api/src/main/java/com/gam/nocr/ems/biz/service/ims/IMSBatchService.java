package com.gam.nocr.ems.biz.service.ims;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.vol.TransferInfoVTO;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface IMSBatchService {

    /**
     * The method sendBatchEnquiryRequest is used to send a request toward IMS for fulfilling batch enquiry
     *
     * @param transferInfoVTO is an instance of type {@link com.gam.nocr.ems.data.domain.vol.TransferInfoVTO}, which carries the required information for batch enquiry
     * @return an instance of type {@link String}
     * @throws com.gam.commons.core.BaseException
     */
    String sendBatchEnquiryRequest(TransferInfoVTO transferInfoVTO) throws BaseException;

    /**
     * The method getBatchEnquiryResponse is used to receive the response of the batch enquiring request from IMS sub
     * system.
     *
     * @return an object of type {@link TransferInfoVTO} which carries the result of batch enquiry
     */
    TransferInfoVTO getBatchEnquiryResponse() throws BaseException;
}
