package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import gampooya.tools.security.SecurityContextService;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;
import org.slf4j.Logger;

import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.util.EmsUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * The list reader processor class for card dispatch grid
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class CardDispatchProcessor extends EMSVLPListProcessor {

    private static final Logger logger = BaseLog.getLogger(CardDispatchProcessor.class);
    private static final String DEFAULT_CARD_LOST_CONFIRM = "false";

    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        String perId = null;
        Long personID = getPersoId(paramProvider.getUserProfileTO());
        if (paramProvider.getUserProfileTO() != null && personID != null && personID > 0)
            perId = "" + personID;
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
        StringBuilder part = new StringBuilder();
        String batchId = paramProvider.getParameter("batchId");
        String containerId = paramProvider.getParameter("containerId");
        if (containerId != null && containerId.trim().length() > 0 && !containerId.trim().equals("0"))
            batchId = containerId;
        String batchCmsId = paramProvider.getParameter("batchCmsId");
        String firstNameFA = paramProvider.getParameter("firstNameFA");
        String sureNameFA = paramProvider.getParameter("sureNameFA");
        String nationalId = paramProvider.getParameter("nationalId");
        String crdCrn = paramProvider.getParameter("cardSerialNum");
        if (batchId != null && batchId.trim().length() != 0) {
            part.append(",ccosBatchContent,batch");
            parameters.put("batchid", batchId);
        } else {
            part.append(",card");
        }
        if (batchCmsId != null && batchCmsId.trim().length() != 0) {
            part.append(",batchCmsId");
            parameters.put("batchCmsId", "%" + batchCmsId + "%");
        }
        if (firstNameFA != null && firstNameFA.trim().length() != 0) {
            part.append(",firstName");
            parameters.put("firstName", "%" + firstNameFA + "%");
        }
        if (sureNameFA != null && sureNameFA.trim().length() != 0) {
            part.append(",lastName");
            parameters.put("lastName", "%" + sureNameFA + "%");
        }
        if (nationalId != null && nationalId.trim().length() != 0) {
            part.append(",nationalId");
            parameters.put("nationalId", nationalId);
        }
        if (crdCrn != null && crdCrn.trim().length() != 0) {
            part.append(",crdCrn");
            parameters.put("crdCrn", "%" + crdCrn + "%");
        }
    	// true: need to manager confirm
        if (Boolean.valueOf(EmsUtil.getProfileValue(
				ProfileKeyName.KEY_LOST_CARD_CONFIRM,
				DEFAULT_CARD_LOST_CONFIRM))) {
        		part.append(",lostConfirmed");
		}
        //  Retrieve a VLP implementation of the underlying project
        ValueListProvider vlp = getValueListProvider();
        //  Determine requested ordering of the result
        String orderBy = getOrderBy(paramProvider);
        //  Create an instance of VLH base on given parameters
        ValueListHandler vlh = null;
        try {
            vlh = vlp.loadList("cardDispatchList",
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
    
    //Anbari
	Long getPersoId(UserProfileTO userProfileTO) {
		Long personID = null;
		if (userProfileTO != null && EmsUtil.checkString( userProfileTO.getUserName())) {
			String username = userProfileTO.getUserName();
			try {
				personID = getPersonService().findPersonIdByUsername(username);
			} catch (BaseException e) {
				e.printStackTrace();
			}
		}
		return personID;

	}
}
