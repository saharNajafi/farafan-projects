package com.gam.nocr.ems.web.listreader.processor;

import gampooya.tools.security.SecurityContextService;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.sql.Timestamp;
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

public class TokenRenewalRequestProcessor extends EMSVLPListProcessor {

	private static final Logger logger = BaseLog
			.getLogger(TokenRenewalRequestProcessor.class);

	protected ValueListHandler prepareVLH(ParameterProvider paramProvider)
			throws ListReaderException {
		
		 	Integer perId;
		 	Long personID = null;

	        UserProfileTO uto = paramProvider.getUserProfileTO();
	        if (uto != null)
	        {
	        	try {
					personID = getPersonService().findPersonIdByUsername(uto.getUserName());
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

		StringBuilder parts = new StringBuilder();
		HashMap parameters = new HashMap();
		parameters.put("perid", perId);
		
		String tokenType = paramProvider.getFilter("tokenType");
		String tokenState = paramProvider.getFilter("tokenState");
		String adminName = paramProvider.getFilter("adminName");
		String departmentName = paramProvider.getFilter("departmentName");
		String fromDate = paramProvider.getFilter("fromDate");
		String toDate = paramProvider.getFilter("toDate");

		if (EmsUtil.checkString(tokenType)) {
			parameters.put("tokenType", "%" + tokenType + "%");
			parts.append(",tokenType");
		}
		if (EmsUtil.checkString(tokenState)) {
			parameters.put("tokenState", tokenState);
			parts.append(",tokenState");
		}
		if (EmsUtil.checkString(adminName)) {
			parameters.put("adminName", "%" + adminName + "%");
			parts.append(",adminName");
		}
		if (EmsUtil.checkString(departmentName)) {
			parameters.put("departmentName", "%" + departmentName + "%");
			parts.append(",departmentName");
		}

		if (EmsUtil.checkFromAndToDate(fromDate, toDate)) {

			StringBuilder temp1 = new StringBuilder(fromDate);
			temp1.deleteCharAt(0);
			temp1.deleteCharAt(temp1.length() - 1);
			StringBuilder temp2 = new StringBuilder(toDate);
			temp2.deleteCharAt(0);
			temp2.deleteCharAt(temp2.length() - 1);
			String[] fromDates = temp1.toString().split(",");
			String[] toDates = temp2.toString().split(",");
			if (fromDates[1] != null && !fromDates[1].isEmpty()
					&& fromDates[1].length() >= 10) {

				parameters.put("fromDateRequestDate",
						EmsUtil.completeFromDate(fromDates[1]));
				parameters.put("toDateRequestDate",
						EmsUtil.completeToDate(toDates[1]));
				parts.append(",requestDate");
			}
			if (fromDates[0] != null && !fromDates[0].isEmpty()
					&& fromDates[0].length() >= 10) {

				parameters.put("fromDateIssuanceDate",
						EmsUtil.completeFromDate(fromDates[0]));
				parameters.put("toDateIssuanceDate",
						EmsUtil.completeToDate(toDates[0]));
				parts.append(",issuanceDate");
			}
			if (fromDates[2] != null && !fromDates[2].isEmpty()
					&& fromDates[2].length() >= 10) {

				parameters.put("fromDateDeliverDate",
						EmsUtil.completeFromDate(fromDates[2]));
				parameters.put("toDateDeliverDate",
						EmsUtil.completeToDate(toDates[2]));
				parts.append(",deliverDate");
			}
		}

		// String perId = paramProvider.getParameter("perId");
		// if (perId == null || perId.trim().length() == 0)
		// {
		// //perId = "0";
		// if (paramProvider.getUserProfileTO() != null
		// && paramProvider.getUserProfileTO().getPersonID() > 0)
		// perId = "" + paramProvider.getUserProfileTO().getPersonID();
		// else if (paramProvider.getRequest() != null) {
		// HttpServletRequest request = paramProvider.getRequest();
		// SecurityContextService scs = new SecurityContextService(request);
		// try {
		// perId = "" + scs.getCurrentPersonId();
		// } catch (Exception ex) {
		// // logger.error(ex.getMessage(), ex);
		// perId = "0";
		// }
		// }
		// }

		// parameters.put("perid", perId);
		//
		// if (EmsUtil.checkString(tokenType)) {
		// parameters.put("tokenType", tokenType);
		// parts.append(",tokenType");
		// }
		// if (EmsUtil.checkString(tokenState)) {
		// parameters.put("tokenState", tokenState);
		// parts.append(",tokenState");
		// }

		// Retrieve a VLP implementation of the underlying project
		ValueListProvider vlp = getValueListProvider();
		// Determe requested ordering of the result
		String orderBy = getOrderBy(paramProvider);
		// Create an instance of VLH base on given parameters
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
