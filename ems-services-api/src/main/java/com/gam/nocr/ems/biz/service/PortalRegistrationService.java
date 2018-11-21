package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
//import com.gam.nocr.ems.biz.service.external.client.portal.CardRequestWTO;
//import com.gam.nocr.ems.biz.service.external.client.portal.CitizenWTO;
//import com.gam.nocr.ems.biz.service.external.client.portal.ItemWTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.ws.SyncCardRequestWTO;

import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface PortalRegistrationService extends Service {

    /**
     * The method updateCardRequestStates is used to alert the sub system 'Portal' about the current state of the request.
     *
     * @param syncCardRequestWTOList a list of type {@link ItemWTO} which carries a number of
     *                               portalRequestId with the specified state for each id
     * @return an object of type {@Boolean} which represents whether the batch update is done correctly or not
     * @throws BaseException
     */
//    List<ItemWTO> updateCardRequestsState(List<SyncCardRequestWTO> syncCardRequestWTOList) throws BaseException;

    /**
     * The method transferCardRequests is used to get a number of card request from the sub system 'Portal'
     *
     * @return a list of type {@link CardRequestTO}
     * @throws BaseException
     */
//    List<CardRequestTO> transferCardRequests(List<Long> portalCardRequestIds) throws BaseException;

//    List<Long> fetchPortalCardRequestIdsForTransfer() throws BaseException;

//    Long updateCcosCardRequests(CardRequestWTO cardRequestWTO) throws BaseException;

    /**
     * The method updateNotVerifiedMESRequest is used to update the request, which has not been verified by
     * IMS and have origins of type 'MES'.
     *
     * @param citizenWTO is an instance of type {@link CitizenWTO}, which carries the necessary information to update the
     *                   portal data base
     * @return an instance of type {@link Long}
     * @throws com.gam.commons.core.BaseException
     *
     */
//    Long updateNotVerifiedMESRequest(CitizenWTO citizenWTO) throws BaseException;
}
