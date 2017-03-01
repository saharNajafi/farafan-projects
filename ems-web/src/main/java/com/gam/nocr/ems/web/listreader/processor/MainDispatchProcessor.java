package com.gam.nocr.ems.web.listreader.processor;

import gampooya.tools.security.SecurityContextService;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.gam.nocr.ems.biz.delegator.DispatchingDelegator;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.ws.DispatchInfoWTO;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * The list reader processor class for dispatch grid
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class MainDispatchProcessor extends EMSVLPListProcessor {
	
	public static final String DEFAULT_DISPATCH_KEY_CARTABLE_QUERY_METHOD = "1";

    private static final Logger logger = BaseLog.getLogger(MainDispatchProcessor.class);

    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        String perId = null;
        Long personID = null;
        UserProfileTO userProfileTO = paramProvider.getUserProfileTO();
        if (userProfileTO != null)
        {

			try {
				personID = getPersonService().findPersonIdByUsername(
						userProfileTO.getUserName());
			} catch (BaseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(personID > 0)				
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

        String cmsId = paramProvider.getFilter("cmsId");
        if (!checkString(cmsId)) {
            cmsId = paramProvider.getParameter("cmsId");
        }
        String itemCount = paramProvider.getFilter("itemCount");
        String fromDispatchSentDate = paramProvider.getParameter("fromDispatchSentDate");
        String toDispatchSentDate = paramProvider.getParameter("toDispatchSentDate");
        String nextReceiverName = paramProvider.getFilter("nextReceiverName");
        String fromSentDate = paramProvider.getFilter("fromSentDate");
        String toSendDate = paramProvider.getFilter("toSendDate");
        String fromReceiveDate = paramProvider.getFilter("fromReceiveDate");
        String toReceiveDate = paramProvider.getFilter("toReceiveDate");

        String status = paramProvider.getFilter("status");

        StringBuilder part = new StringBuilder();
        if (cmsId != null) {
            parameters.put("cmsId", "%" + cmsId + "%");
            part.append(",cmsId");
        }
        if (itemCount != null) {
            parameters.put("itemCount", itemCount);
            part.append(",itemCount");
        }
        if (EmsUtil.checkFromAndToDate(fromDispatchSentDate, toDispatchSentDate)) {
            parameters.put("fromDispatchSentDate", EmsUtil.completeFromDate(fromDispatchSentDate));
            parameters.put("toDispatchSentDate", EmsUtil.completeToDate(toDispatchSentDate));
            part.append(",sendDate");
        }
        if (EmsUtil.checkString(nextReceiverName)) {
            parameters.put("nextReceiverName", "%" + nextReceiverName + "%");
            part.append(",receiver");
        }
        if (EmsUtil.checkFromAndToDate(fromSentDate, toSendDate)) {
            parameters.put("fromSentDate", fromSentDate);
            parameters.put("toSendDate", toSendDate);
            part.append(",dispatchSentDate");
        }
        if (EmsUtil.checkFromAndToDate(fromReceiveDate, toReceiveDate)) {
            parameters.put("fromReceiveDate", EmsUtil.completeFromDate(fromReceiveDate));
            parameters.put("toReceiveDate", EmsUtil.completeToDate(toReceiveDate));
            part.append(",dispatchReceiveDate");
        }
        if (EmsUtil.checkString(status)) {
            parameters.put("statusId", status);
            part.append(",status");
        }
        //  Retrieve a VLP implementation of the underlying project
        ValueListProvider vlp = getValueListProvider();
        //  Determe requested ordering of the result
        String orderBy = getOrderBy(paramProvider);
        //  Create an instance of VLH base on given parameters
        ValueListHandler vlh;
        try {
            vlh = vlp.loadList(paramProvider.getListName(),
                    ("main" + part).split(","),
                    ("count" + part).split(","),
                    parameters,
                    orderBy,
                    null);
        } catch (ListException e) {
            throw new ListReaderException("Unable to prepare a VLH to fetch list named '" + paramProvider.getListName() + "'", e);
        }
        return vlh;
    }

    private boolean checkString(String param) {
        return param != null && param.trim().length() != 0;
    }
    
    
    
    @Override
    public ListResult fetchList(ParameterProvider paramProvider)
    		throws ListReaderException {
    	
		String method = EmsUtil.getProfileValue(
				ProfileKeyName.KEY_DISPATCH_CARTABLE_QUERY_METHOD,
				DEFAULT_DISPATCH_KEY_CARTABLE_QUERY_METHOD);
		String cartableTypeStr = paramProvider.getListName();
		if (cartableTypeStr == null)
			throw new ListReaderException("No cartableType specified.");

		else if (cartableTypeStr.equals("batchDispatchList") && "ICT".equals(method.trim().toUpperCase())) {
			return fetchBatchDispatchList(paramProvider);
		} else if ("GAM".equals(method.trim().toUpperCase())){
			return super.fetchList(paramProvider);
		}else {
			return super.fetchList(paramProvider);
		}

    }

	private ListResult fetchBatchDispatchList(ParameterProvider paramProvider) throws ListReaderException {
		
		
		HashMap<String,Object> parameters = new HashMap();
		parameters.put("cmsId", paramProvider.getParameter("cmsId"));
		parameters.put("fromDispatchSentDate", paramProvider.getParameter("fromDispatchSentDate"));
		parameters.put("toDispatchSentDate", paramProvider.getParameter("toDispatchSentDate"));
		
		int pageSize = paramProvider.getTo() - paramProvider.getFrom();
		try {
			GeneralCriteria criteria = new GeneralCriteria(parameters, getOrderBy(paramProvider), paramProvider.getUserProfileTO(),pageSize , paramProvider.getFrom());
			List<DispatchInfoWTO> dispatchInfoWTOs = new DispatchingDelegator().fetchBatchDispatchList(criteria, paramProvider.getUserProfileTO());
			
			
			Integer count = dispatchInfoWTOs.size();
			int pageNo = (paramProvider.getTo() / pageSize) - 1; // zero index
			if (pageSize == count) {
				count = new DispatchingDelegator()
				.countBatchDispatchList(parameters, paramProvider.getUserProfileTO());
			} else if (pageNo > 0) {
				count += pageNo * pageSize;
			}
			
			return new ListResult(paramProvider.getListName(), count, dispatchInfoWTOs);
		} catch (BaseException exception) {
			throw new ListReaderException(exception.getMessage(),
					exception.getCause());
		}
			
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
