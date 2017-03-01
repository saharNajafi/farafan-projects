package com.gam.nocr.ems.web.listreader.processor;

import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ListResult;
import com.gam.commons.listreader.ParameterProvider;
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.biz.delegator.PersonDelegator;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.DepartmentTO;
import com.gam.nocr.ems.data.domain.vol.CardRequestVTO;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * The list reader processor class for card request grid in 3S
 * 
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public class EmsCardRequestProcessor extends EMSVLPListProcessor {

	private static final Logger logger = BaseLog
			.getLogger(EmsCardRequestProcessor.class);

	protected ValueListHandler prepareVLH(ParameterProvider paramProvider)
			throws ListReaderException {
		HashMap parameters = new HashMap();
		StringBuilder parts = new StringBuilder();
		// Retrieve a VLP implementation of the underlying project
		ValueListProvider vlp = getValueListProvider();
		// Determe requested ordering of the result
		String orderBy = getOrderBy(paramProvider);
		// Create an instance of VLH base on given parameters
		ValueListHandler vlh;

		UserProfileTO userProfileTO = paramProvider.getUserProfileTO();
		Long personID = null;
		try {
			personID = getPersonService().findPersonIdByUsername(
					userProfileTO.getUserName());
		} catch (BaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String personId;
		personId = "" + personID;
		parameters.put("perid", personId);

		// If user's department is not MARKAZ (which its id is 1), limit his
		// access to those records that are
		// registered in his department or its sub-departments
		DepartmentTO departmentTO = null;
		try {
			departmentTO = new PersonDelegator().loadDepartmentByPersonId(
					userProfileTO, personID);
		} catch (BaseException e) {
			logger.error(WebExceptionCode.GLB_ERR_MSG, e);
			throw new ListReaderException(
					"Unable to load user department in order to limit the request list result to his department scope",
					e);
		}

		if (departmentTO != null) {
			if (departmentTO.getId() != 1) {
				parts.append(",allDepartmentWithSubDepartment");
			}
		} else {
			logger.error(WebExceptionCode.GLB_ERR_MSG);
			throw new ListReaderException(
					"The user department loaded from database is null");
		}

		String citizenFirstName = paramProvider.getFilter("citizenFirstName");
		String citizenSurname = paramProvider.getFilter("citizenSurname");
		String citizenNId = paramProvider.getFilter("citizenNId");
		String enrollmentOfficeName = paramProvider
				.getFilter("enrollmentOfficeName");
		String fromEnrolledDate = paramProvider.getFilter("fromEnrolledDate");
		String toEnrolledDate = paramProvider.getFilter("toEnrolledDate");
		String fromPortalEnrolledDate = paramProvider
				.getFilter("fromPortalEnrolledDate");
		String toPortalEnrolledDate = paramProvider
				.getFilter("toPortalEnrolledDate");
		String cardRequestState = paramProvider.getFilter("cardRequestState");
		String cardType = paramProvider.getFilter("cardType");
		String cardState = paramProvider.getFilter("cardState");
		String requestedAction = paramProvider.getFilter("requestedAction");
		String trackingId = paramProvider.getFilter("trackingId");
		String deliveredOfficeId = paramProvider
				.getFilter("deliveredOfficeName");
	
		//hossein 8 feature merge start
		String reservationDateFrom = paramProvider.getFilter("reservationDateFrom");
        String reservationDateTo = paramProvider.getFilter("reservationDateTo");
        String documentFlag = paramProvider.getFilter("documentFlag");
        String faceFlag = paramProvider.getFilter("faceFlag");
        String fingerFlag = paramProvider.getFilter("fingerFlag");
		//hossein 8 feature merge end

		if (EmsUtil.checkString(citizenFirstName)) {
			parameters.put("citizenFirstName", "%" + citizenFirstName + "%");
			parts.append(",citizenFirstName");
		}
		if (EmsUtil.checkString(citizenSurname)) {
			parameters.put("citizenSurname", "%" + citizenSurname + "%");
			parts.append(",citizenSurname");
		}
		if (EmsUtil.checkString(citizenNId)) {
			parameters.put("citizenNId", "%" + citizenNId + "%");
			parts.append(",citizenNId");
		}
		if (EmsUtil.checkString(enrollmentOfficeName)) {
			parameters.put("enrollmentOfficeName", "%" + enrollmentOfficeName
					+ "%");
			parts.append(",enrollmentOfficeName");
		}
		if (EmsUtil.checkFromAndToDate(fromEnrolledDate, toEnrolledDate)) {
			parameters.put("fromEnrolledDate",
					EmsUtil.completeFromDate(fromEnrolledDate));
			parameters.put("toEnrolledDate",
					EmsUtil.completeToDate(toEnrolledDate));
			parts.append(",enrolledDate");
		}
		if (EmsUtil.checkFromAndToDate(fromPortalEnrolledDate,
				toPortalEnrolledDate)) {
			parameters.put("fromPortalEnrolledDate",
					EmsUtil.completeFromDate(fromPortalEnrolledDate));
			parameters.put("toPortalEnrolledDate",
					EmsUtil.completeToDate(toPortalEnrolledDate));
			parts.append(",portalEnrolledDate");
		}
		if (EmsUtil.checkString(cardRequestState)) {
			parameters.put("cardRequestState", cardRequestState);
			parts.append(",cardRequestState");
		}
		if (EmsUtil.checkString(cardType)) {
			parameters.put("cardType", cardType);
			parts.append(",cardType");
		}
		if (EmsUtil.checkString(cardState)) {
			parameters.put("cardState", cardState);
			parts.append(",cardState");
		}
		if (EmsUtil.checkString(requestedAction)) {
			parameters.put("requestedAction", requestedAction);
			parts.append(",requestedAction");
		}
		if (EmsUtil.checkString(trackingId)) {
			parameters.put("trackingId", "%" + trackingId + "%");
			parts.append(",trackingId");
		}
		if (EmsUtil.checkString(deliveredOfficeId)) {
			parameters.put("deliveredOfficeId",  deliveredOfficeId);
			parts.append(",deliveredOfficeId");
		}
		
		//hossein 8 feature merge start
		  if (EmsUtil.checkFromAndToDate(reservationDateFrom, reservationDateTo)) {
	            parameters.put("reservationDateFrom", EmsUtil.completeFromDate(reservationDateFrom));
	            parameters.put("reservationDateTo", EmsUtil.completeToDate(reservationDateTo));
	            parts.append(",reservationDate");
	        }
	        if (EmsUtil.checkString(documentFlag)) {
	            if (Boolean.toString(true).equals(documentFlag)) {
	                parts.append(",documentFlagTrue");
	            } else if (Boolean.toString(false).equals(documentFlag)) {
	                parts.append(",documentFlagFalse");
	            }
	        }
	        if (EmsUtil.checkString(faceFlag)) {
	            if (Boolean.toString(true).equals(faceFlag)) {
	                parts.append(",faceFlagTrue");
	            } else if (Boolean.toString(false).equals(faceFlag)) {
	                parts.append(",faceFlagFalse");
	            }
	        }
	        if (EmsUtil.checkString(fingerFlag)) {
	            if (Boolean.toString(true).equals(fingerFlag)) {
	                parts.append(",fingerFlagTrue");
	            } else if (Boolean.toString(false).equals(fingerFlag)) {
	                parts.append(",fingerFlagFalse");
	            }
	        }
			//hossein 8 feature merge end

		try {
			vlh = vlp.loadList("emsCardRequestList",
					("main" + parts).split(","), ("count" + parts).split(","),
					parameters, orderBy, null);
		} catch (ListException e) {
			throw new ListReaderException(
					"Unable to prepare a VLH to fetch list named '"
							+ paramProvider.getListName() + "'", e);
		}
		return vlh;
	}

	@Override
	public ListResult fetchList(ParameterProvider paramProvider)
			throws ListReaderException {
		try {

			GeneralCriteria criteria = getCriteria(paramProvider);
			
			CardRequestDelegator cardRequestDelegator = new CardRequestDelegator();			
			
			List<CardRequestVTO> page = cardRequestDelegator
					.fetchCardRequests(criteria);

			Integer count = page.size();
			if (criteria.getPageSize() == count) {
				count = cardRequestDelegator.countCardRequests(criteria);
			}
			else if (criteria.getPageNo() > 0){
				count += criteria.getPageNo() * criteria.getPageSize() ;
			}

			return new ListResult(paramProvider.getListName(), count, page);

		} catch (BaseException e) {
			if(e.getMessage().contains("CRP_001")){
				throw new ListReaderException(WebExceptionCode.CRP_001);
			}
			e.printStackTrace();
			throw new ListReaderException(e.getMessage(), e.getCause());
		}
	}

	private GeneralCriteria getCriteria(ParameterProvider paramProvider)
			throws ListReaderException, NumberFormatException, BaseException {

		HashMap parameters = new HashMap();
		String orderBy = getOrderBy(paramProvider);
		Long personID = null;		

		UserProfileTO userProfileTO = paramProvider.getUserProfileTO();
		personID = getPersonService().findPersonIdByUsername(userProfileTO.getUserName());

		// If user's department is not MARKAZ (which its id is 1), limit his
		// access to those records that are
		// registered in his department or its sub-departments
		DepartmentTO departmentTO = null;
		try {
			departmentTO = new PersonDelegator().loadDepartmentByPersonId(
					userProfileTO, personID);
		} catch (BaseException e) {
			logger.error(WebExceptionCode.GLB_ERR_MSG, e);
			throw new ListReaderException(
					"Unable to load user department in order to limit the request list result to his department scope",
					e);
		}

		if (departmentTO != null) {
			if (departmentTO.getId() != 1) {
				
				parameters.put("checkSecurity", true);
				//String personId = "" + userProfileTO.getPersonID();
				//parameters.put("perid", personId);			
				
				PersonDelegator personDelegator = new PersonDelegator();
				List<Long> departmantListIds = personDelegator.findListDepartmentsByPersonId(personID);
				//departmantListIds.add(-1L);
				parameters.put("depIds", departmantListIds);
			}
		} else {
			logger.error(WebExceptionCode.GLB_ERR_MSG);
			throw new ListReaderException(
					"The user department loaded from database is null");
		}

		String citizenFirstName = paramProvider.getFilter("citizenFirstName");
		String citizenSurname = paramProvider.getFilter("citizenSurname");
		String citizenNId = paramProvider.getFilter("citizenNId");
		String enrollmentOfficeId = paramProvider
				.getFilter("enrollmentOfficeName");
		String fromDeliveredDate = paramProvider.getFilter("fromDeliveredDate");
		String toDeliveredDate = paramProvider.getFilter("toDeliveredDate");
		String fromEnrolledDate = paramProvider.getFilter("fromEnrolledDate");
		String toEnrolledDate = paramProvider.getFilter("toEnrolledDate");
		String fromPortalEnrolledDate = paramProvider
				.getFilter("fromPortalEnrolledDate");
		String toPortalEnrolledDate = paramProvider
				.getFilter("toPortalEnrolledDate");
		String cardRequestState = paramProvider.getFilter("cardRequestState");
		String cardType = paramProvider.getFilter("cardType");
		String cardState = paramProvider.getFilter("cardState");
		String requestedAction = paramProvider.getFilter("requestedAction");
		String trackingId = paramProvider.getFilter("trackingId");
		String deliveredOfficeId = paramProvider
				.getFilter("deliveredOfficeName");

		   //hossein 8 feature start
		  String reservationDateFrom = paramProvider.getFilter("reservationDateFrom");
	        String reservationDateTo = paramProvider.getFilter("reservationDateTo");
	        String documentFlag = paramProvider.getFilter("documentFlag");
	        String faceFlag = paramProvider.getFilter("faceFlag");
	        String fingerFlag = paramProvider.getFilter("fingerFlag");
			//hossein 8 feature end

		if (EmsUtil.checkString(citizenFirstName)) {
			parameters.put("citizenFirstName", citizenFirstName + "%");
		}
		if (EmsUtil.checkString(citizenSurname)) {
			parameters.put("citizenSurname", citizenSurname + "%");
		}
		if (EmsUtil.checkString(citizenNId)) {
			if (citizenNId.length() == 8) {
				citizenNId = "00" + citizenNId;
			} else if (citizenNId.length() == 9) {
				citizenNId = "0" + citizenNId;
			}
			parameters.put("citizenNId", citizenNId);
		}
		if (EmsUtil.checkString(enrollmentOfficeId)) {
			parameters.put("enrollmentOfficeId", enrollmentOfficeId);
		}
		if (EmsUtil.checkFromAndToDate(fromEnrolledDate, toEnrolledDate)) {
			parameters.put("fromEnrolledDate",
					EmsUtil.completeFromDate(fromEnrolledDate));
			parameters.put("toEnrolledDate",
					EmsUtil.completeToDate(toEnrolledDate));
		}
		if (EmsUtil.checkFromAndToDate(fromDeliveredDate, toDeliveredDate)) {
			parameters.put("fromDeliveredDate",
					EmsUtil.completeFromDate(fromDeliveredDate));
			parameters.put("toDeliveredDate",
					EmsUtil.completeToDate(toDeliveredDate));
		}
		if (EmsUtil.checkFromAndToDate(fromPortalEnrolledDate,
				toPortalEnrolledDate)) {
			parameters.put("fromPortalEnrolledDate",
					EmsUtil.completeFromDate(fromPortalEnrolledDate));
			parameters.put("toPortalEnrolledDate",
					EmsUtil.completeToDate(toPortalEnrolledDate));
		}
		if (EmsUtil.checkString(cardRequestState)) {
			parameters.put("cardRequestState", cardRequestState);
		}
		if (EmsUtil.checkString(cardType)) {
			parameters.put("cardType", cardType);
		}
		if (EmsUtil.checkString(cardState)) {
			parameters.put("cardState", cardState);
		}
		if (EmsUtil.checkString(requestedAction)) {
			parameters.put("requestedAction", requestedAction);
		}
		if (EmsUtil.checkString(trackingId)) {
			parameters.put("trackingId", trackingId);
		}
		if (EmsUtil.checkString(deliveredOfficeId)) {
			parameters.put("deliveredOfficeId", deliveredOfficeId);
		}
		
		if(EmsUtil.checkString(reservationDateFrom)){
			parameters.put("reservationDateFrom", reservationDateFrom);
		}

		if(EmsUtil.checkString(reservationDateTo)){
			parameters.put("reservationDateTo", reservationDateTo);
		}
	
		if(EmsUtil.checkString(documentFlag)){
			parameters.put("documentFlag", documentFlag);
		}
		if(EmsUtil.checkString(faceFlag)){
			parameters.put("faceFlag", faceFlag);
		}
		if(EmsUtil.checkString(fingerFlag)){
			parameters.put("fingerFlag", fingerFlag);
		}

		int pageSize = paramProvider.getTo() - paramProvider.getFrom();
		int pageNo = (paramProvider.getTo() / pageSize) - 1; // zero index

		GeneralCriteria criteria = new GeneralCriteria(parameters, orderBy,
				userProfileTO, pageSize, pageNo);
		return criteria;
	}

	@Override
	public void prepareList(ParameterProvider paramProvider)
			throws ListReaderException {
		super.prepareList(paramProvider);
	}
	
	 //Anbari
    private PersonManagementService getPersonService() throws BaseException {
        PersonManagementService personManagementService;
        try {
            personManagementService = (PersonManagementService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_PERSON),null);
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.PDL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_PERSON.split(","));
        }
        return personManagementService;
    }
}
