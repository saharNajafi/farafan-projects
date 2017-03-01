package com.gam.nocr.ems.web.listreader.processor;

import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ListResult;
import com.gam.commons.listreader.ParameterProvider;
import com.gam.nocr.ems.biz.delegator.CardRequestDelegator;
import com.gam.nocr.ems.biz.delegator.EnrollmentOfficeDelegator;
import com.gam.nocr.ems.biz.delegator.PersonDelegator;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.DepartmentTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.vol.CCOSCriteria;
import com.gam.nocr.ems.data.domain.ws.CitizenWTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * The list reader processor class for card request grid
 * 
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class CardRequestProcessor extends EMSVLPListProcessor {
	private static final String DEFAULT_CCOS_RESERVATION_RANGE_TO_SHOW = "2";

	public static final String DEFAULT_KEY_CARTABLE_QUERY_METHOD = "1";

	private static final Logger logger = BaseLog
			.getLogger(CardRequestProcessor.class);

	protected ValueListHandler prepareVLH(ParameterProvider paramProvider)
			throws ListReaderException {

		UserProfileTO uto = paramProvider.getUserProfileTO();
		Long personID = null;
		try {
			personID = getPersonService().findPersonIdByUsername(uto.getUserName());
		} catch (BaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String perid = "0";
		if (uto != null)
			perid = "" + personID;
		String cartableTypeStr = paramProvider.getParameter("cartableType");
		String repealFilter = paramProvider.getParameter("repealFilter");
		String transferFilter = paramProvider.getParameter("transferFilter");
		String firstName = paramProvider.getParameter("firstNameFA");
		String lastName = paramProvider.getParameter("sureNameFA");
		String nationalId = paramProvider.getParameter("nationalId");
		String trackingId = paramProvider.getParameter("trackingId");
		String fromEnrollmentDate = paramProvider
				.getParameter("fromEnrolledDate");
		String toEnrollmentDate = paramProvider.getParameter("toEnrolledDate");
		String birthDateGreg = paramProvider.getParameter("birthDate");
		if (cartableTypeStr == null)
			throw new ListReaderException("No cartableType specified.");
		int cartableType;
		try {
			cartableType = Integer.parseInt(cartableTypeStr);
			if (cartableType <= 0)
				throw new Exception(
						"Cartable Type can not defined smaller than one.");
		} catch (Exception ex) {
			throw new ListReaderException("Invalid cartable type specified.",
					ex);
		}
		StringBuilder parts = new StringBuilder(",cartable");
		HashMap parameters = new HashMap();
		parameters.put("perid", perid);
		if (firstName != null && firstName.trim().length() != 0) {
			parameters.put("firstName", "%" + firstName + "%");
			parts.append(",firstName");
		}
		if (lastName != null && lastName.trim().length() != 0) {
			parameters.put("lastName", "%" + lastName + "%");
			parts.append(",lastName");
		}
		if (nationalId != null && nationalId.trim().length() != 0) {
			parameters.put("nationalId", nationalId);
			parts.append(",nationalId");
		}
		if (trackingId != null && trackingId.trim().length() != 0) {
			parameters.put("trackingId", trackingId);
			parts.append(",trackingId");
		}
		if (EmsUtil.checkFromAndToDate(fromEnrollmentDate, toEnrollmentDate)) {
			parameters.put("fromEnrollmentDate",
					EmsUtil.completeFromDate(fromEnrollmentDate));
			parameters.put("toEnrollmentDate",
					EmsUtil.completeToDate(toEnrollmentDate));
			parts.append(",enrollmentDate");
		}
		if (EmsUtil.checkString(birthDateGreg)) {
			parameters.put("birthDate", birthDateGreg);
			parts.append(",birthDate");
		}

		String reservationRange = EmsUtil.getProfileValue(
				ProfileKeyName.KEY_CCOS_RESERVATION_RANGE_TO_SHOW,
				DEFAULT_CCOS_RESERVATION_RANGE_TO_SHOW);

		if ((cartableType & 1) == 1) {
			parts.append(",office,reserve,todayReservation,states,notMES");
			parameters.put("reservationRange", reservationRange);
		}
		if ((cartableType & 2) == 2)
			parts.append(",other,office,last30Day,reEnrolledAfter30Day,states,notMES,readyToScan");
		if ((cartableType & 4) == 4)
			parts.append(",other,office,last30Day,reEnrolledAfter30Day,states,notMES,readyToFing");
		if ((cartableType & 8) == 8)
			parts.append(",other,office,last30Day,reEnrolledAfter30Day,states,notMES,readyToFace");
		if ((cartableType & 16) == 16)
			parts.append(",other,office,last30Day,reEnrolledAfter30Day,states,notMES,readyToAuth");
		if ((cartableType & 64) == 64)
			parts.append(",other,office,last30Day,reEnrolledAfter30Day,states,notMES,readyToApprove");
		if ((cartableType & 128) == 128)
			parts.append(",other,office,states,notMES,underIssuance");
		if ((cartableType & 256) == 256)
			parts.append(",other,office,states,readyToDeliver");
		if ((cartableType & 512) == 512)
			parts.append(",other,states,delivered");
		if ((cartableType & 1024) == 1024) {
			parts.append(",other,office,reserve,pastReservation,states,notMES");
			parameters.put("reservationRange", reservationRange);
		}
		if ((cartableType & 2048) == 2048) {
			parts.append(",other,office,reserve,futureReservation,states,notMES");
			parameters.put("reservationRange", reservationRange);
		}

		if (EmsUtil.checkString(repealFilter)
				|| EmsUtil.checkString(transferFilter)) {
			parts.append(",cartableFilter");
		} else {
			parts.append(",noneCartableFilter");
		}
		if (EmsUtil.checkString(repealFilter)) {
			if ("repealed".trim().equals(repealFilter))
				parts.append(",repealed");
			else if ("notRepealed".trim().equals(repealFilter))
				parts.append(",notRepealed");
			else if ("notRepealed,repealed".trim().equals(repealFilter)
					|| "repealed,notRepealed".trim().equals(repealFilter))
				parts.append(",notRepealed,repealed");

		}
		if (EmsUtil.checkString(transferFilter)) {
			if ("transferred".trim().equals(transferFilter))
				parts.append(",transferred");
			else if ("notTransferred".trim().equals(transferFilter))
				parts.append(",notTransferred");
			else if ("notTransferred,transferred".trim().equals(transferFilter)
					|| "transferred,notTransferred".trim().equals(
							transferFilter))
				parts.append(",notTransferred,transferred");
		}

		// Retrieve a VLP implementation of the underlying project
		ValueListProvider vlp = getValueListProvider();
		// Determine requested ordering of the result
		String orderBy = getOrderBy(paramProvider);
		// Create an instance of VLH base on given parameters
		ValueListHandler vlh;
		try {
			vlh = vlp.loadList("cardRequestList", ("main" + parts).split(","),
					("count" + parts).split(","), parameters, orderBy, null);
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
		String method = EmsUtil.getProfileValue(
				ProfileKeyName.KEY_CARTABLE_QUERY_METHOD,
				DEFAULT_KEY_CARTABLE_QUERY_METHOD);
		if (method == null || "GAM".equals(method.trim().toUpperCase())) {
			return super.fetchList(paramProvider);
		}

		else if ("MIX".equals(method.trim().toUpperCase())) {
			String cartableTypeStr = paramProvider.getParameter("cartableType");
			if (cartableTypeStr == null)
				throw new ListReaderException("No cartableType specified.");
			int cartableType;
			try {
				cartableType = Integer.parseInt(cartableTypeStr);
				if (cartableType <= 0)
					throw new Exception(
							"Cartable Type can not defined smaller than one.");
			} catch (Exception ex) {
				throw new ListReaderException(
						"Invalid cartable type specified.", ex);
			}
			if ((cartableType & 256) == 256 || (cartableType & 512) == 512) {
				return super.fetchList(paramProvider);
			} else {
				return newApproach(paramProvider);
			}
		}

		else if ("ICT".equals(method.trim().toUpperCase())) {
			return newApproach(paramProvider);
		} else {
			return super.fetchList(paramProvider);
		}
	}

	private ListResult newApproach(ParameterProvider paramProvider)
			throws ListReaderException {

		try {
			CCOSCriteria criteria = getCriteria(paramProvider);

			if (criteria.getParts().contains("noneCartableFilter")) {
				if (criteria.getParts().contains("readyToDeliver")) {
					return readyToDeliverCartable(paramProvider, criteria);
				} else if (criteria.getParts().contains("delivered")) {
					return deliverCartable(paramProvider, criteria);
				}
			} else if (criteria.getParts().contains("cartableFilter")) {
				return registrationCartable(paramProvider, criteria);
			}
		} catch (BaseException e) {
			throw new ListReaderException(e.getMessage(), e.getCause());
		}

		return null;
	}

	private ListResult registrationCartable(ParameterProvider paramProvider,
			CCOSCriteria criteria) throws BaseException {

		List<CitizenWTO> page = new CardRequestDelegator()
				.fetchCCOSRegistrationCartableCardRequests(criteria);

		Integer count = page.size();
		if (criteria.getPageSize() == count) {
			count = new CardRequestDelegator()
					.countCCOSRegistrationCartableCardRequests(criteria);
		} else if (criteria.getPageNo() > 0) {
			count += criteria.getPageNo() * criteria.getPageSize();
		}

		return new ListResult(paramProvider.getListName(), count, page);
	}

	private ListResult deliverCartable(ParameterProvider paramProvider,
			CCOSCriteria criteria) throws BaseException {
		List<CitizenWTO> page = new CardRequestDelegator()
				.fetchCCOSDeliverCartableCardRequests(criteria);

		Integer count = page.size();
		if (criteria.getPageSize() == count) {
			count = new CardRequestDelegator()
					.countCCOSDeliverCartableCardRequests(criteria);
		} else if (criteria.getPageNo() > 0) {
			count += criteria.getPageNo() * criteria.getPageSize();
		}

		return new ListResult(paramProvider.getListName(), count, page);
	}

	private ListResult readyToDeliverCartable(ParameterProvider paramProvider,
			CCOSCriteria criteria) throws BaseException {
		List<CitizenWTO> page = new CardRequestDelegator()
				.fetchCCOSReadyToDeliverCartableCardRequests(criteria);

		Integer count = page.size();
		if (criteria.getPageSize() == count) {
			count = new CardRequestDelegator()
					.countCCOSReadyToDeliverCartableCardRequests(criteria);
		} else if (criteria.getPageNo() > 0) {
			count += criteria.getPageNo() * criteria.getPageSize();
		}

		return new ListResult(paramProvider.getListName(), count, page);
	}

	private CCOSCriteria getCriteria(ParameterProvider paramProvider)
			throws ListReaderException {
		UserProfileTO uto = paramProvider.getUserProfileTO();
		Long personID = null;
		try {
			personID = getPersonService().findPersonIdByUsername(uto.getUserName());
		} catch (BaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String perid = "0";
		if (uto != null)
			perid = "" + personID;
		String cartableTypeStr = paramProvider.getParameter("cartableType");
		String repealFilter = paramProvider.getParameter("repealFilter");
		String transferFilter = paramProvider.getParameter("transferFilter");
		String firstName = paramProvider.getParameter("firstNameFA");
		String lastName = paramProvider.getParameter("sureNameFA");
		String nationalId = paramProvider.getParameter("nationalId");
		String trackingId = paramProvider.getParameter("trackingId");
		String fromEnrollmentDate = paramProvider
				.getParameter("fromEnrolledDate");
		String toEnrollmentDate = paramProvider.getParameter("toEnrolledDate");
		String birthDateGreg = paramProvider.getParameter("birthDate");
		if (cartableTypeStr == null)
			throw new ListReaderException("No cartableType specified.");
		int cartableType;
		try {
			cartableType = Integer.parseInt(cartableTypeStr);
			if (cartableType <= 0)
				throw new Exception(
						"Cartable Type can not defined smaller than one.");
		} catch (Exception ex) {
			throw new ListReaderException("Invalid cartable type specified.",
					ex);
		}

		DepartmentTO departmentTO = null;
		try {
			departmentTO = new PersonDelegator().loadDepartmentByPersonId(uto,personID);
		} catch (BaseException e) {
			logger.error(WebExceptionCode.GLB_ERR_MSG, e);
			throw new ListReaderException("Unable to load user department ", e);
		}
		if (departmentTO == null) {
			logger.error(WebExceptionCode.GLB_ERR_MSG);
			throw new ListReaderException(
					"The user department loaded from database is null");
		}

		EnrollmentOfficeTO enrollmentOfficeTO;
		try {
			enrollmentOfficeTO = new EnrollmentOfficeDelegator()
					.find(departmentTO.getId());
		} catch (BaseException e) {
			logger.error(WebExceptionCode.GLB_ERR_MSG, e);
			throw new ListReaderException("Unable to load user eof ", e);
		}
		if (enrollmentOfficeTO == null) {
			logger.error(WebExceptionCode.GLB_ERR_MSG);
			throw new ListReaderException(
					"The user eof loaded from database is null");
		}

		StringBuilder parts = new StringBuilder(",cartable");
		HashMap parameters = new HashMap();

		// parameters.put("perid", perid);
		if ((cartableType & 512) != 512){
			parameters.put("departmentId", departmentTO.getId());
		}

		if (firstName != null && firstName.trim().length() != 0) {
			parameters.put("firstName", firstName + "%");
			parts.append(",firstName");
		}
		if (lastName != null && lastName.trim().length() != 0) {
			parameters.put("lastName", lastName + "%");
			parts.append(",lastName");
		}
		if (nationalId != null && nationalId.trim().length() != 0) {
			parameters.put("nationalId", nationalId);
			parts.append(",nationalId");
		}
		if (trackingId != null && trackingId.trim().length() != 0) {
			parameters.put("trackingId", trackingId);
			parts.append(",trackingId");
		}
		if (EmsUtil.checkFromAndToDate(fromEnrollmentDate, toEnrollmentDate)) {
			parameters.put("fromEnrollmentDate",
					EmsUtil.completeFromDate(fromEnrollmentDate));
			parameters.put("toEnrollmentDate",
					EmsUtil.completeToDate(toEnrollmentDate));
			parts.append(",enrollmentDate");
		}
		if (EmsUtil.checkString(birthDateGreg)) {
			parameters.put("birthDate", birthDateGreg);
			parts.append(",birthDate");
		}

		String reservationRange = EmsUtil.getProfileValue(
				ProfileKeyName.KEY_CCOS_RESERVATION_RANGE_TO_SHOW,
				DEFAULT_CCOS_RESERVATION_RANGE_TO_SHOW);

		if ((cartableType & 1) == 1) {
			parts.append(",office,reserve,todayReservation,states,notMES");
			parameters.put("reservationRange", reservationRange);
		}
		if ((cartableType & 2) == 2)
			parts.append(",other,office,last30Day,reEnrolledAfter30Day,states,notMES,readyToScan");
		if ((cartableType & 4) == 4)
			parts.append(",other,office,last30Day,reEnrolledAfter30Day,states,notMES,readyToFing");
		if ((cartableType & 8) == 8)
			parts.append(",other,office,last30Day,reEnrolledAfter30Day,states,notMES,readyToFace");
		if ((cartableType & 16) == 16)
			parts.append(",other,office,last30Day,reEnrolledAfter30Day,states,notMES,readyToAuth");
		if ((cartableType & 64) == 64)
			parts.append(",other,office,last30Day,reEnrolledAfter30Day,states,notMES,readyToApprove");
		if ((cartableType & 128) == 128)
			parts.append(",other,office,states,notMES,underIssuance");
		if ((cartableType & 256) == 256)
			parts.append(",other,office,states,readyToDeliver");
		if ((cartableType & 512) == 512)
			parts.append(",other,states,delivered");
		if ((cartableType & 1024) == 1024) {
			parts.append(",other,office,reserve,pastReservation,states,notMES");
			parameters.put("reservationRange", reservationRange);
		}
		if ((cartableType & 2048) == 2048) {
			parts.append(",other,office,reserve,futureReservation,states,notMES");
			parameters.put("reservationRange", reservationRange);
		}

		if (EmsUtil.checkString(repealFilter)
				|| EmsUtil.checkString(transferFilter)) {
			parts.append(",cartableFilter");
		} else {
			parts.append(",noneCartableFilter");
		}
		if (EmsUtil.checkString(repealFilter)) {
			if ("repealed".trim().equals(repealFilter))
				parts.append(",repealed");
			else if ("notRepealed".trim().equals(repealFilter))
				parts.append(",notRepealed");
			else if ("notRepealed,repealed".trim().equals(repealFilter)
					|| "repealed,notRepealed".trim().equals(repealFilter))
				parts.append(",notRepealed,repealed");

		}
		if (EmsUtil.checkString(transferFilter)) {
			if ("transferred".trim().equals(transferFilter))
				parts.append(",transferred");
			else if ("notTransferred".trim().equals(transferFilter))
				parts.append(",notTransferred");
			else if ("notTransferred,transferred".trim().equals(transferFilter)
					|| "transferred,notTransferred".trim().equals(
							transferFilter))
				parts.append(",notTransferred,transferred");
		}

		// Determine requested ordering of the result
		String orderBy = getOrderBy(paramProvider);

		int pageSize = paramProvider.getTo() - paramProvider.getFrom();
		int pageNo = (paramProvider.getTo() / pageSize) - 1; // zero index

		Set<String> partSet = new HashSet<String>();
		StringTokenizer tok = new StringTokenizer(parts.toString(), ",");
		int cnt = tok.countTokens();
		for (int i = 0; i < cnt; i++) {
			String token = tok.nextToken();
			partSet.add(token.trim());
		}

		return new CCOSCriteria(parameters, orderBy, uto, pageSize, pageNo,
				cartableType, partSet, enrollmentOfficeTO);
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
