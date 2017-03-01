package com.gam.nocr.ems.web.listreader.processor;

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
import gampooya.tools.security.SecurityContextService;
import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * The list reader processor class for workstations grid (in 3S)
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class WorkStationProcessor extends EMSVLPListProcessor {

    private static final Logger logger = BaseLog.getLogger(WorkStationProcessor.class);

    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        StringBuilder part = new StringBuilder();
        //  Retrieve a list of parameters sent by client
        Map parameters = getQueryParrameters(paramProvider);

        String officeName = null;
        String officeId = null;
        String officeCode = null;
        String managerId = null;
        String code = null;
        String activationCode = null;
        String state = null;
        String provinceName = null;
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

        parameters.put("perid", perId);

        if (paramProvider.containsFilter("enrollmentOfficeName"))
            officeName = paramProvider.getFilter("enrollmentOfficeName");
        if (paramProvider.containsFilter("enrollmentOfficeId"))
            officeId = paramProvider.getFilter("enrollmentOfficeId");
        if (paramProvider.containsFilter("enrollmentOfficeCode"))
            officeCode = paramProvider.getFilter("enrollmentOfficeCode");
        if (paramProvider.containsFilter("code"))
            code = paramProvider.getFilter("code");
        if (paramProvider.containsFilter("activationCode"))
            activationCode = paramProvider.getFilter("activationCode");
        if (paramProvider.containsFilter("lState"))
            state = paramProvider.getFilter("lState");
        if (paramProvider.containsFilter("managerId"))
            managerId = paramProvider.getFilter("managerId");
        if (paramProvider.containsFilter("state"))
            state = paramProvider.getFilter("state");
        if (paramProvider.containsFilter("provinceName"))
            provinceName = paramProvider.getFilter("provinceName");

        if (EmsUtil.checkString(officeName)) {
            parameters.put("officeName", "%" + officeName + "%");
            part.append(",officeName");
        }
        if (EmsUtil.checkString(officeId)) {
            parameters.put("officeId", officeId);
            part.append(",officeId");
        }
        if (EmsUtil.checkString(officeCode)) {
            parameters.put("officeCode", officeCode);
            part.append(",officeCode");
        }
        if (EmsUtil.checkString(managerId)) {
            parameters.put("managerId", managerId);
            part.append(",managerId");
        }
        if (EmsUtil.checkString(code)) {
            parameters.put("code", "%" + code + "%");
            part.append(",workstationCode");
        }
        if (EmsUtil.checkString(activationCode)) {
            parameters.put("activationCode", "%" + activationCode + "%");
            part.append(",workstationActivationCode");
        }
        if (EmsUtil.checkString(state)) {
            parameters.put("lState", "%" + state + "%");
            part.append(",state");
        }
        if (EmsUtil.checkString(provinceName)) {
            parameters.put("province", "%" + provinceName + "%");
            part.append(",province");
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
