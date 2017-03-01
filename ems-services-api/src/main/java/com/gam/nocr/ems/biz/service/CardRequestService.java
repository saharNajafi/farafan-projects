package com.gam.nocr.ems.biz.service;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.vol.AccessProductionVTO;
import com.gam.nocr.ems.data.domain.vol.CCOSCriteria;
import com.gam.nocr.ems.data.domain.vol.CardRequestVTO;
import com.gam.nocr.ems.data.domain.ws.CitizenWTO;
import com.gam.nocr.ems.data.domain.ws.SyncCardRequestWTO;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.CardRequestedAction;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;

/**
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public interface CardRequestService extends Service {

	public Long findRequestCountByAction(CardRequestedAction cardRequestedAction)
			throws BaseException;

	public List<Long> repealCardRequest(Integer from) throws BaseException;

	public void doCardRequestRepealAction(Long cardRequestId,
			CardRequestedAction cardRequestedAction, SystemId systemId)
			throws BaseException;

	public void transferCardRequestToNocr(Long cardRequestId,
			CardRequestedAction cardRequestedAction) throws BaseException;

	public List<CardRequestVTO> fetchCardRequests(GeneralCriteria criteria)
			throws BaseException;

	public Integer countCardRequests(GeneralCriteria criteria)
			throws BaseException;

	public List<CitizenWTO> fetchCCOSRegistrationCartableCardRequests(
			CCOSCriteria criteria) throws BaseException;

	public Integer countCCOSRegistrationCartableCardRequests(
			CCOSCriteria criteria) throws BaseException;

	public List<CitizenWTO> fetchCCOSDeliverCartableCardRequests(
			CCOSCriteria criteria) throws BaseException;

	public Integer countCCOSDeliverCartableCardRequests(CCOSCriteria criteria)
			throws BaseException;

	public List<CitizenWTO> fetchCCOSReadyToDeliverCartableCardRequests(
			CCOSCriteria criteria) throws BaseException;

	public Integer countCCOSReadyToDeliverCartableCardRequests(
			CCOSCriteria criteria) throws BaseException;

	public List<Long> getRequestIdsForUpdateState(Integer fetchLimit) throws BaseException;

	public List<SyncCardRequestWTO> getRequestListForUpdateState(
			List<Long> requestIds) throws BaseException;
    
  //Anbari    
    public void doImageDeleteAction(List<Long> cardRequestIds,Long citizenId, CardRequestState state) throws BaseException;
    public void doCMSRetryAction(List<Long> cardRequestIds,CardRequestState state) throws BaseException;
    public void doRepealCardAction(List<Long> cardRequestIds) throws BaseException;
    public void updateCardRequestForDocumentAuthenticateState(Long cardRequestId,CardRequestState state) throws BaseException;
	public void doRepealCardRequest(CardRequestTO cardRequestTO) throws BaseException;
    //ganjyar
	//Addoost
//    public void doEstelam2(Long cardRequestId) throws BaseException;

	public AccessProductionVTO getAccessProduction() throws BaseException;
	//

	public void increamentPriorityForSyncJob(Long requestId) throws BaseException;

	public CardRequestVTO findCardRequestById(String cardRequestId) throws BaseException;

	public void updateCardRequestPriority(String cardRequestId, String priority) throws BaseException;

	public boolean hasChangePriorityAccess() throws BaseException;


	// Hossein 8 feature start
	public CardRequestVTO viewCardRequestInfo(Long cardRequestId) throws BaseException;
	//hossein 8 feature end

	//Madanipour
	public List<Long> fetchReservedRequest(Integer numberOfRequestToFetch,Integer dayInterval)throws BaseException;

	//Madanipour
	public void addRequestedSmsForReservedReq(Long cardRequestId) throws BaseException;

	public void repealCardRequestInDelivery(CardRequestTO cardRequestTO) throws BaseException;

}
