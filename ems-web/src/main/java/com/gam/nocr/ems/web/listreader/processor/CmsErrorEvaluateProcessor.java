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
import com.gam.nocr.ems.util.EmsUtil;

/**
 * The list reader processor class for card request grid
 * 
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class CmsErrorEvaluateProcessor extends EMSVLPListProcessor {

	private static final Logger logger = BaseLog
			.getLogger(CmsErrorEvaluateProcessor.class);

	protected ValueListHandler prepareVLH(ParameterProvider paramProvider)
			throws ListReaderException {

		UserProfileTO userProfileTO = paramProvider.getUserProfileTO();
		Long personID = null;
		UserProfileTO uto = paramProvider.getUserProfileTO();
		try {
			personID = getPersonService().findPersonIdByUsername(
					uto.getUserName());
		} catch (BaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String perid = "0";
		if (userProfileTO != null)
			perid = "" + personID;

		StringBuilder parts = new StringBuilder();
		// Retrieve a list of parameters sent by client
		Map parameters = getQueryParrameters(paramProvider);

		parameters.put("perid", perid);

		
		String citizenFirstName = paramProvider.getFilter("citizenFirstName");
		String citizenSurname = paramProvider.getFilter("citizenSurname");
		String citizenNId = paramProvider.getFilter("citizenNId");
		String enrollmentOfficeName = paramProvider.getFilter("enrollmentOfficeName");
		String fromBackDate = paramProvider.getFilter("fromBackDate");
		String toBackDate= paramProvider.getFilter("toBackDate");
		
		
		
        DepartmentTO departmentTO = null;
        try {
            departmentTO = new PersonDelegator().loadDepartmentByPersonId(userProfileTO, personID);
        } catch (BaseException e) {
            logger.error(WebExceptionCode.GLB_ERR_MSG, e);
            throw new ListReaderException("Unable to load user department in order to limit the request list result to his department scope", e);
        }

        if (departmentTO != null) {
            if (departmentTO.getId() != 1){
                parts.append(",allDepartmentWithSubDepartment");
            }
        } else {
            logger.error(WebExceptionCode.GLB_ERR_MSG);
            throw new ListReaderException("The user department loaded from database is null");
        }

        if (EmsUtil.checkString(citizenFirstName)) {
			parameters.put("citizenFirstName", "%" + citizenFirstName + "%");
			parts.append(",citizenFirstName");
		}
		if (EmsUtil.checkString(citizenSurname)) {
			parameters.put("citizenSurname", "%" + citizenSurname + "%");
			parts.append(",citizenSurname");
		}
		if (EmsUtil.checkString(citizenNId)) {
			parameters.put("citizenNId", "%" + citizenNId + "%");
			parts.append(",citizenNId");
		}
		
		if (EmsUtil.checkString(enrollmentOfficeName)) {
			parameters.put("enrollmentOfficeName", "%" + enrollmentOfficeName + "%");
			parts.append(",enrollmentOfficeName");
		}
		
		if (EmsUtil.checkFromAndToDate(fromBackDate, toBackDate)) {
			parameters.put("fromBackDate",
					EmsUtil.completeFromDate(fromBackDate));
			parameters.put("toBackDate",
					EmsUtil.completeToDate(toBackDate));
			parts.append(",backDate");
		}
        
        
		ValueListProvider vlp = getValueListProvider();
		String orderBy = getOrderBy(paramProvider);
		ValueListHandler vlh;
		try {
			vlh = vlp.loadList("cmserrorevaluateList",
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
