package com.gam.nocr.ems.web.listreader.processor;

import java.util.ArrayList;
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
import com.gam.nocr.ems.biz.delegator.PersonDelegator;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.DepartmentTO;
import com.gam.nocr.ems.data.domain.vol.BatchDispatchInfoVTO;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * 
 * @author ganjyar
 * 
 */
public class BatchProcessor extends EMSVLPListProcessor {

	private static final Logger logger = BaseLog
			.getLogger(BatchProcessor.class);
	private static final String DEFAULT_CARD_LOST_CONFIRM = "false";

	@Override
	public ListResult fetchList(ParameterProvider paramProvider)
			throws ListReaderException {

		try {
			GeneralCriteria criteria = getCriteria(paramProvider);
			List<BatchDispatchInfoVTO> page = null;
			// true: need to manager confirm
			if (Boolean.valueOf(EmsUtil.getProfileValue(
					ProfileKeyName.KEY_LOST_CARD_CONFIRM,
					DEFAULT_CARD_LOST_CONFIRM))) {

				page = new CardRequestDelegator()
						.fetchBatchLostTempList(criteria);
			} else {
				page = new ArrayList<BatchDispatchInfoVTO>();
			}
			Integer count = page.size();
			if (criteria.getPageSize() == count) {
				count = new CardRequestDelegator().countBatchLostTemp(criteria);
			} else if (criteria.getPageNo() > 0) {
				count += criteria.getPageNo() * criteria.getPageSize();
			}
			logger.info("fetch lost batch. list name is: "
					+ paramProvider.getListName());
			return new ListResult(paramProvider.getListName(), count, page);

		} catch (BaseException e) {
			throw new ListReaderException(e.getMessage(), e.getCause());
		}

	}

	private GeneralCriteria getCriteria(ParameterProvider paramProvider)
			throws ListReaderException, BaseException {
		UserProfileTO uto = paramProvider.getUserProfileTO();
		Long personID = getPersonService().findPersonIdByUsername(uto.getUserName());

		StringBuilder parts = new StringBuilder("");
		HashMap parameters = new HashMap();
		String isConfirm=paramProvider.getFilter("isConfirm");
		String fromLostDate = paramProvider.getFilter("fromSentDate");
		String toLostDate = paramProvider.getFilter("toSendDate");
		String cmsIDStr = paramProvider.getFilter("cmsID");
		// If user's department is not MARKAZ (which its id is 1), limit his
				// access to those records that are
				// registered in his department or its sub-departments
				DepartmentTO departmentTO = null;
				try {
					departmentTO = new PersonDelegator().loadDepartmentByPersonId(uto, personID);
				} catch (BaseException e) {
					logger.error(WebExceptionCode.GLB_ERR_MSG, e);
					throw new ListReaderException(
							"Unable to load user department in order to limit the lost batch list result to his department scope",
							e);
				}
				
				

				if (departmentTO != null) {
					if (departmentTO.getId() != 1) {
						parameters.put("checkSecurity", true);
						String personId = "" + personID;
						parameters.put("perid", personId);
					}
				} else {
					logger.error(WebExceptionCode.GLB_ERR_MSG);
					throw new ListReaderException(
							"The user department loaded from database is null");
				}
				
		if (cmsIDStr != null && cmsIDStr.trim().length() != 0) {
			parameters.put("cmsID", "%" + cmsIDStr + "%");
		}
		if (fromLostDate != null && fromLostDate.trim().length() != 0) {
			parameters.put("fromLostDate", fromLostDate);
		}
		if (toLostDate != null && toLostDate.trim().length() != 0) {
			parameters.put("toLostDate", toLostDate);
		}
		if (isConfirm != null && isConfirm.trim().length() != 0) {
		if(isConfirm.equals("1"))
			parameters.put("confirmed", isConfirm );
		else
			parameters.put("waitingToConfirmed", isConfirm );
		}
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
		return new GeneralCriteria(parameters, orderBy, uto, pageSize, pageNo);
	}
	

	// Anbari
	private PersonManagementService getPersonService() throws BaseException {
		PersonManagementService personManagementService;
		try {
			personManagementService = (PersonManagementService) ServiceFactoryProvider
					.getServiceFactory()
					.getService(
							EMSLogicalNames
									.getServiceJNDIName(EMSLogicalNames.SRV_PERSON),
							null);
		} catch (ServiceFactoryException e) {
			throw new DelegatorException(BizExceptionCode.PDL_001,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_PERSON.split(","));
		}
		return personManagementService;
	}
}
