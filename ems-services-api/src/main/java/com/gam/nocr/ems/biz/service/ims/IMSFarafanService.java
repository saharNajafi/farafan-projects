package com.gam.nocr.ems.biz.service.ims;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.vol.EmsCardDeliverInfo;
import com.gam.nocr.ems.data.domain.vol.IMSUpdateResultVTO;

import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface IMSFarafanService {

    /**
     * The method updateCitizensInfo is used to send the request for updating the citizen information.
     *
     * @param cardRequestTOList a list of type {CardRequestTO}
     * @param imsRequestId      is an instance of type {@link String}, which represents the requestId of the update
     *                          request message
     */
    void updateCitizensInfo(List<CardRequestTO> cardRequestTOList,
                            String imsRequestId) throws BaseException;

    /**
     * The method getUpdatedCitizensResult is used to receive the response of updating the citizen information
     * request from IMS sub system.
     *
     * @param imsRequestId is an instance of type {@link String}, which represents the requestId of the update
     *                     request message
     * @return a list of type {@link com.gam.nocr.ems.data.domain.vol.IMSUpdateResultVTO}
     */
    List<IMSUpdateResultVTO> getUpdatedCitizensResult(String imsRequestId) throws BaseException;


    /**
     * The method setCitizenCardDelivered is used to notify the IMS about the delivering the card by citizen
     *
     * @param nationalId is an instance of type {@link String}, which represents the nationalId of a specified citizen
     * @return true or false (to show whether the alerting operation has done successfully or not)
     */
    boolean setCitizenCardDelivered(EmsCardDeliverInfo cardDeliverInfo) throws BaseException;

    /**
     * The method fetchCitizenInfo if used to receive all the information about a specified citizen, from the sub system
     * 'IMS'.
     *
     * @param nationalId is an instance of type {@link String}, which represents the nationalId of a specified citizen
     * @return an instance of type {@link com.gam.nocr.ems.data.domain.CitizenTO}
     * @throws BaseException
     */
    CitizenTO fetchCitizenInfo(String nationalId) throws BaseException;
    
  //Anbari:IMS
    IMSUpdateResultVTO getUpdatedCitizensResultNew(String nationalId) throws BaseException;
    
    

}
