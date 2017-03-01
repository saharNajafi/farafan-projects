package com.gam.nocr.ems.web.listreader.processor;

import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.Map;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import com.gam.nocr.ems.biz.delegator.PersonDelegator;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.DepartmentTO;

/**
 * The list reader processor class for card request grid
 * 
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class Estelam2FalseProcessor extends EMSVLPListProcessor {

	private static final Logger logger = BaseLog
			.getLogger(Estelam2FalseProcessor.class);

	protected ValueListHandler prepareVLH(ParameterProvider paramProvider)
			throws ListReaderException {

		UserProfileTO userProfileTO = paramProvider.getUserProfileTO();
		String perid = "0";
		Long personID = null;
		if (userProfileTO != null){
			try {
				personID = getPersonService().findPersonIdByUsername(
						userProfileTO.getUserName());
			} catch (BaseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			perid = "" + personID;
		}

		StringBuilder part = new StringBuilder();

		Map parameters = getQueryParrameters(paramProvider);

		parameters.put("perid", perid);

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
				part.append(",allDepartmentWithSubDepartment");
			}
		} else {
			logger.error(WebExceptionCode.GLB_ERR_MSG);
			throw new ListReaderException(
					"The user department loaded from database is null");
		}

		String citizenNId = paramProvider.getFilter("citizenNId");

		if (citizenNId != null && citizenNId.trim().length() != 0) {
			part.append(",citizenNId");
			parameters.put("citizenNId", "%" + citizenNId + "%");
		}

		String cardRequestState = paramProvider.getFilter("cardRequestState");

		if (cardRequestState != null && cardRequestState.trim().length() != 0) {
			part.append(",cardRequestState");
			parameters.put("cardRequestState", cardRequestState);
		}

		String trackingId = paramProvider.getFilter("trackingId");

		if (trackingId != null && trackingId.trim().length() != 0) {
			part.append(",trackingId");
			parameters.put("trackingId", "%" + trackingId + "%");
		}

		ValueListProvider vlp = getValueListProvider();
		String orderBy = getOrderBy(paramProvider);
		ValueListHandler vlh;
		try {
			vlh = vlp.loadList("estelam2falseList", ("main" + part).split(","),
					("count" + part).split(","), parameters, orderBy, null);
		} catch (ListException e) {
			throw new ListReaderException(
					"Unable to prepare a VLH to fetch list named '"
							+ paramProvider.getListName() + "'", e);
		}
		return vlh;
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
