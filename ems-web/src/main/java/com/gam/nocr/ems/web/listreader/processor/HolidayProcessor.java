package com.gam.nocr.ems.web.listreader.processor;

import gampooya.tools.security.SecurityContextService;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;

public class HolidayProcessor extends EMSVLPListProcessor {

	private static final Logger logger = BaseLog
			.getLogger(HolidayProcessor.class);

	@SuppressWarnings("unchecked")
	protected ValueListHandler prepareVLH(ParameterProvider paramProvider)
			throws ListReaderException {
		StringBuilder part = new StringBuilder();
		// Retrieve a list of parameters sent by client
		Map parameters = getQueryParrameters(paramProvider);

		Integer perId;
		Long personID = null;

		UserProfileTO uto = paramProvider.getUserProfileTO();
		if (uto != null)
		{
			try {
				personID = getPersonService().findPersonIdByUsername(
						uto.getUserName());
			} catch (BaseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			perId = Integer.valueOf(("" + personID));
		}
		else {
			HttpServletRequest request = paramProvider.getRequest();
			SecurityContextService scs = new SecurityContextService(request);
			try {
				perId = scs.getCurrentPersonId();
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				perId = 0;
			}
		}


		parameters.put("perid", perId);

		ValueListProvider vlp = getValueListProvider();
		String orderBy = getOrderBy(paramProvider);
		ValueListHandler vlh;
		try {
			vlh = vlp.loadList("hosseinHolidayAC", ("main" + part).split(","),
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
