package com.gam.nocr.ems.web.listreader.processor;

import gampooya.tools.security.SecurityContextService;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.HashMap;

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
import com.gam.nocr.ems.util.EmsUtil;

/**
 * The list reader processor class for card dispatch grid
 * 
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class PreparedMessageProcessor extends EMSVLPListProcessor {

	private static final Logger logger = BaseLog
			.getLogger(PreparedMessageProcessor.class);

	@SuppressWarnings("unchecked")
	protected ValueListHandler prepareVLH(ParameterProvider paramProvider)
			throws ListReaderException {
		String perId = null;
		Long personID = null;		
		UserProfileTO userProfileTO = paramProvider.getUserProfileTO();
		if (userProfileTO != null)
		{
			try {
				personID = getPersonService().findPersonIdByUsername(userProfileTO.getUserName());
			} catch (BaseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			perId = "" + personID;
		}
		else if (paramProvider.getRequest() != null) {
			HttpServletRequest request = paramProvider.getRequest();
			SecurityContextService scs = new SecurityContextService(request);
			try {
				perId = "" + scs.getCurrentPersonId();
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				perId = "0";
			}
		} else
			perId = "0";
		HashMap parameters = new HashMap();
		parameters.put("perid", perId);
		StringBuilder parts = new StringBuilder();

		String senderUsername = null;
		String subject = null;
		String msgFromDate = null;
		String msgToDate = null;
		String preparedState = null;

		// Filter
		if (paramProvider.containsFilter("fromDate"))
			msgFromDate = paramProvider.getFilter("fromDate");
		if (paramProvider.containsFilter("toDate"))
			msgToDate = paramProvider.getFilter("toDate");

		if (paramProvider.containsFilter("title"))
			subject = paramProvider.getFilter("title");

		if (EmsUtil.checkString(subject)) {
			parameters.put("subject", "%" + subject + "%");
			parts.append(",subject");
		}

		if (paramProvider.containsFilter("preparedState"))
			preparedState= paramProvider.getFilter("preparedState");
		
		if (EmsUtil.checkString(preparedState)) {
			parameters.put("state", "%" + preparedState + "%");
			parts.append(",state");
		}
		
		if (paramProvider.containsFilter("senderUsername"))
			senderUsername = paramProvider.getFilter("senderUsername");

		if (EmsUtil.checkString(senderUsername)) {
			parameters.put("senderUsername", "%" + senderUsername + "%");
			parts.append(",senderUsername");
		}

		if (EmsUtil.checkFromAndToDate(msgFromDate, msgToDate)) {
			parameters.put("fromDate", EmsUtil.completeFromDate(msgFromDate));
			parameters.put("toDate", EmsUtil.completeToDate(msgToDate));
			parts.append(",createDate");
		}

		ValueListProvider vlp = getValueListProvider();
		String orderBy = getOrderBy(paramProvider);
		ValueListHandler vlh = null;
		try {
			vlh = vlp.loadList(paramProvider.getListName(),
					("main" + parts).split(","), ("count" + parts).split(","),
					parameters, orderBy, null);
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
