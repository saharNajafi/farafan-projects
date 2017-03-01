package com.gam.nocr.ems.web.listreader.processor;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import gampooya.tools.security.SecurityContextService;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

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

public class TokenProcessor extends EMSVLPListProcessor {

	private static final Logger logger = BaseLog
			.getLogger(CardRequestProcessor.class);
	
//	protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
//
//        StringBuilder parts = new StringBuilder();
//        HashMap parameters = new HashMap();
//
//        String tokenType = paramProvider.getFilter("tokenType");
//        String tokenState = paramProvider.getFilter("tokenState");
//
//        String perId = paramProvider.getParameter("perId");
//        if (perId == null || perId.trim().length() == 0)
//        {
//            //perId = "0";
//        	if (paramProvider.getUserProfileTO() != null
//    				&& paramProvider.getUserProfileTO().getPersonID() > 0)
//    			perId = "" + paramProvider.getUserProfileTO().getPersonID();
//    		else if (paramProvider.getRequest() != null) {
//    			HttpServletRequest request = paramProvider.getRequest();
//    			SecurityContextService scs = new SecurityContextService(request);
//    			try {
//    				perId = "" + scs.getCurrentPersonId();
//    			} catch (Exception ex) {
////    				logger.error(ex.getMessage(), ex);
//    				perId = "0";
//    			}
//    		}
//        }
//
//        parameters.put("perid", perId);
//
//        if (EmsUtil.checkString(tokenType)) {
//            parameters.put("tokenType", tokenType);
//            parts.append(",tokenType");
//        }
//        if (EmsUtil.checkString(tokenState)) {
//            parameters.put("tokenState", tokenState);
//            parts.append(",tokenState");
//        }
//
//        //  Retrieve a VLP implementation of the underlying project
//        ValueListProvider vlp = getValueListProvider();
//        //  Determe requested ordering of the result
//        String orderBy = getOrderBy(paramProvider);
//        //  Create an instance of VLH base on given parameters
//        ValueListHandler vlh = null;
//        try {
//            vlh = vlp.loadList(paramProvider.getListName(),
//                    ("main" + parts).split(","),
//                    ("count" + parts).split(","),
//                    parameters,
//                    orderBy,
//                    null);
//        } catch (ListException e) {
//            throw new ListReaderException("Unable to prepare a VLH to fetch list named '" + paramProvider.getListName() + "'", e);
//        }
//        return vlh;
//    }
	
	protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {

        StringBuilder parts = new StringBuilder();
        HashMap parameters = new HashMap();

        String tokenType = paramProvider.getFilter("tokenType");
        String tokenState = paramProvider.getFilter("tokenState");

//        String perId = paramProvider.getParameter("perId");
//        if (perId == null || perId.trim().length() == 0)
//            perId = "0";

      String perId = paramProvider.getParameter("perId");
      Long personID = null;
      if (perId == null || perId.trim().length() == 0)
      {
          //perId = "0";
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
//  				logger.error(ex.getMessage(), ex);
  				perId = "0";
  			}
  		}
      }

      parameters.put("perid", perId);

        if (EmsUtil.checkString(tokenType)) {
            parameters.put("tokenType", tokenType);
            parts.append(",tokenType");
        }
        if (EmsUtil.checkString(tokenState)) {
            parameters.put("tokenState", tokenState);
            parts.append(",tokenState");
        }

        //  Retrieve a VLP implementation of the underlying project
        ValueListProvider vlp = getValueListProvider();
        //  Determe requested ordering of the result
        String orderBy = getOrderBy(paramProvider);
        //  Create an instance of VLH base on given parameters
        ValueListHandler vlh = null;
        try {
            vlh = vlp.loadList(paramProvider.getListName(),
                    ("main" + parts).split(","),
                    ("count" + parts).split(","),
                    parameters,
                    orderBy,
                    null);
        } catch (ListException e) {
            throw new ListReaderException("Unable to prepare a VLH to fetch list named '" + paramProvider.getListName() + "'", e);
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
