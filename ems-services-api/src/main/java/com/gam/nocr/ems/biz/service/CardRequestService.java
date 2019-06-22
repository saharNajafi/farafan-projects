package com.gam.nocr.ems.biz.service;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.RegistrationPaymentTO;
import com.gam.nocr.ems.data.domain.vol.AccessProductionVTO;
import com.gam.nocr.ems.data.domain.vol.CCOSCriteria;
import com.gam.nocr.ems.data.domain.vol.CardRequestReceiptVTO;
import com.gam.nocr.ems.data.domain.vol.CardRequestVTO;
import com.gam.nocr.ems.data.domain.ws.CitizenWTO;
import com.gam.nocr.ems.data.domain.ws.PersonEnquiryWTO;
import com.gam.nocr.ems.data.domain.ws.SyncCardRequestWTO;
import com.gam.nocr.ems.data.enums.*;
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

	public boolean hasPrintRegistrationReceipt() throws BaseException;


	// Hossein 8 feature start
	public CardRequestVTO viewCardRequestInfo(Long cardRequestId) throws BaseException;
	//hossein 8 feature end

	//Madanipour
	public List<Long> fetchReservedRequest(Integer numberOfRequestToFetch,Integer dayInterval)throws BaseException;

	//Madanipour
	public void addRequestedSmsForReservedReq(Long cardRequestId) throws BaseException;

	public void repealCardRequestInDelivery(CardRequestTO cardRequestTO) throws BaseException;
	
	
	public List<Long> getCitizenIdsForPurgeBioAndDocs(Integer fetchLimit) throws BaseException;

	public void purgeBiometricsAndDocuments(Long citizenId,String savePurgeHistory) throws BaseException;
	
	public void savePurgeHistory(Long citizenId, PurgeState purgeState,String metaData) throws BaseException;

	public  String findCardRequestStateByTrackingId(
			String trackingId) throws  BaseException;

	public  String findCardRequestStateByNationalIdAndMobile(
			String nationalId, String mobile) throws  BaseException ;

	public  String findCardRequestStateByNationalIdAndBirthCertificateSeries(
			String nationalId, String birthCertificateSeries, String citizenBirthDate) throws  BaseException ;

	public void checkInsertSingleStageEnrollmentPossible(
			String nationalId, String birthDateSolar, String certSerialNo, GenderEnum gender) throws BaseException;

	public CardRequestTO findLastRequestByNationalId(String nationalId) throws BaseException ;

	PersonEnquiryWTO updateCitizenByEstelam(CardRequestTO cardRequest, boolean b, boolean b1) throws BaseException;

	CardRequestTO addCardRequest(CardRequestTO emsCardRequest) throws BaseException;

	CardRequestTO update(CardRequestTO cardRequestTO) throws BaseException;

	CardRequestTO findByCitizenId(CitizenTO citizenTO) throws BaseException;

	Long countCardRequestByNationalIdAndType(String nationalId, CardRequestType type) throws BaseException;

	CardRequestReceiptVTO printRegistrationReceipt(long cardRequestId) throws BaseException;

     void print(Long cardRequestId) throws BaseException;

}
