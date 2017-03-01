package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ParameterProvider;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.enums.PersonRequestStatus;
import com.gam.nocr.ems.util.EmsUtil;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.HashMap;

/**
 * The list reader processor class for persons grid
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class PersonProcessor extends EMSVLPListProcessor {
    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        HashMap parameters = new HashMap();
        StringBuilder parts = new StringBuilder();
        //  Retrieve a VLP implementation of the underlying project
        ValueListProvider vlp = getValueListProvider();
        //  Determe requested ordering of the result
        String orderBy = getOrderBy(paramProvider);
        //  Create an instance of VLH base on given parameters
        ValueListHandler vlh = null;

        String firstName = paramProvider.getFilter("firstName");
        String lastName = paramProvider.getFilter("lastName");
        String nid = paramProvider.getFilter("nid");
        String userName = paramProvider.getFilter("userName");
        String departmentName = paramProvider.getFilter("departmentName");
        String fromDate = paramProvider.getFilter("fromDate");
        String toDate = paramProvider.getFilter("toDate");
        String status = paramProvider.getFilter("lStatus");
        String requestStatus = paramProvider.getParameter("requestStatus");
        String provinceName = paramProvider.getFilter("provinceName");
//        String departmentId = paramProvider.getParameter("departmentId");
        if ("requestedUserList".equalsIgnoreCase(paramProvider.getListName())) {
            requestStatus = "'" + PersonRequestStatus.REJECTED.name() + "'" + "," + "'" + PersonRequestStatus.REQUESTED.name() + "'";
        }
        if (EmsUtil.checkString(requestStatus)) {
            parameters.put("requestStatus", requestStatus.split(","));
            parts.append(",requestStatus");
        } else {
            parameters.put("requestStatus", "APPROVED");
            parts.append(",requestStatus");
        }

        if (EmsUtil.checkString(firstName)) {
            parameters.put("firstName", "%" + firstName + "%");
            parts.append(",firstName");
        }
        if (EmsUtil.checkString(lastName)) {
            parameters.put("lastName", "%" + lastName + "%");
            parts.append(",lastName");
        }
        if (EmsUtil.checkString(nid)) {
            parameters.put("nid", "%" + nid + "%");
            parts.append(",nid");
        }
        if (EmsUtil.checkString(userName)) {
            parameters.put("userName", "%" + userName + "%");
            parts.append(",userName");
        }
        if (EmsUtil.checkString(departmentName)) {
            parameters.put("departmentName", "%" + departmentName + "%");
            parts.append(",departmentName");
        }
        if (EmsUtil.checkFromAndToDate(fromDate, toDate)) {
            parameters.put("fromDate", EmsUtil.completeFromDate(fromDate));
            parameters.put("toDate", EmsUtil.completeToDate(toDate));
            parts.append(",lastLogin");
        }
        if (EmsUtil.checkString(status)) {
            parameters.put("status", status);
            parts.append(",status");
        }
        if (EmsUtil.checkString(provinceName)) {
            parameters.put("province", "%" + provinceName + "%");
            parts.append(",province");
        }
        UserProfileTO uto = paramProvider.getUserProfileTO();
        String perid = "0";
        Long personID = null;
        if (uto != null){
        	try {
				personID = getPersonService().findPersonIdByUsername(
						uto.getUserName());
			} catch (BaseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            perid = "" + personID;
        }
        parameters.put("perid", perid);

        try {
            vlh = vlp.loadList("userList",
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
