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
import java.util.HashMap;

/**
 * The list reader processor class for enrollment offices grid
 *
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class EnrollmentOfficeProcessor extends EMSVLPListProcessor {
    private static final Logger logger = BaseLog.getLogger(EnrollmentOfficeProcessor.class);

    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        HashMap parameters = new HashMap();
        StringBuilder parts = new StringBuilder();
        ValueListProvider vlp = getValueListProvider();
        String orderBy = getOrderBy(paramProvider);

        if (EmsUtil.checkString(orderBy) && orderBy.contains("code"))
            orderBy = orderBy.replace("code", "codeNum");

        //check grid filters and add related parts and parameters
        String name = paramProvider.getFilter("name");
        String managerName = paramProvider.getFilter("managerName");
        String parentName = paramProvider.getFilter("parentName");
        String tokenState = paramProvider.getFilter("tokenState");
        String code = paramProvider.getFilter("code");
        String address = paramProvider.getFilter("address");
        String phone = paramProvider.getFilter("phone");
//        String rate = paramProvider.getFilter("rate");
        String officeType = paramProvider.getFilter("officeType");
        String khosusiType = paramProvider.getFilter("khosusiType");
        //Anbari
        String officeDeliver = paramProvider.getFilter("officeDeliver");
        String locName = paramProvider.getFilter("locName");
        String workingHoursStart = paramProvider.getFilter("workingHoursStart");
        String workingHoursFinish = paramProvider.getFilter("workingHoursFinish");
        String superiorOfficeName = paramProvider.getFilter("superiorOfficeName");
        String fqdn = paramProvider.getFilter("fqdn");
        String provinceName = paramProvider.getFilter("provinceName");
        String depPhoneNumber = paramProvider.getFilter("depPhoneNumber");

        Integer perId;
        Long personID = null;

        UserProfileTO uto = paramProvider.getUserProfileTO();
        
        if (uto != null){
        	try {
        		personID = getPersonService().findPersonIdByUsername(uto.getUserName());
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

        if (name != null && name.trim().length() != 0) {
            parameters.put("name", "%" + name + "%");
            parts.append(",name");
        }
        if (managerName != null && managerName.trim().length() != 0) {
            parameters.put("managerName", "%" + managerName + "%");
            parts.append(",manager");
        }
        if (parentName != null && parentName.trim().length() != 0) {
            parameters.put("parentName", "%" + parentName + "%");
            parts.append(",parentName");
        }
        if (tokenState != null && tokenState.trim().length() != 0) {
            parameters.put("tokenState", tokenState);
            parts.append(",tokenState");
        }
        if (code != null && code.trim().length() != 0) {
            parameters.put("code", "%" + code + "%");
            parts.append(",code");
        }
        if (address != null && address.trim().length() != 0) {
            parameters.put("address", "%" + address + "%");
            parts.append(",address");
        }
        if (phone != null && phone.trim().length() != 0) {
            parameters.put("phone", "%" + phone + "%");
            parts.append(",phone");
        }
//        if (rate != null && rate.trim().length() != 0) {
//            parameters.put("rate", "%" + rate + "%");
//            parts.append(",rate");
//        }
        if (EmsUtil.checkString(officeType)) {
            parameters.put("officeType", officeType);
            parts.append(",officeType");
        } 
        if (EmsUtil.checkString(khosusiType)) {
            parameters.put("khosusiType", khosusiType);
            parts.append(",khosusiType");
        } 
        if (EmsUtil.checkString(officeDeliver)) {
            parameters.put("officeDeliver", officeDeliver);
            parts.append(",officeDeliver");
        }
        if (EmsUtil.checkString(locName)) {
            parameters.put("locName", locName);
            parts.append(",locName");
        }
        if (EmsUtil.checkString(workingHoursStart)) {
            parameters.put("workingHoursStart", workingHoursStart);
            parts.append(",workingHoursStart");
        }
        if (EmsUtil.checkString(workingHoursFinish)) {
            parameters.put("workingHoursFinish", workingHoursFinish);
            parts.append(",workingHoursFinish");
        }
        if (EmsUtil.checkString(superiorOfficeName)) {
            parameters.put("superiorOfficeName", superiorOfficeName);
            parts.append(",superiorOfficeName");
        }
        if (EmsUtil.checkString(fqdn)) {
            parameters.put("fqdn", "%" + fqdn + "%");
            parts.append(",fqdn");
        }
        if (EmsUtil.checkString(provinceName)) {
            parameters.put("province", "%" + provinceName + "%");
            parts.append(",province");
        }
        if (depPhoneNumber != null && depPhoneNumber.trim().length() != 0) {
            parameters.put("depPhoneNumber", "%" + depPhoneNumber + "%");
            parts.append(",depPhoneNumber");
        }

        ValueListHandler vlh;
        try {
            vlh = vlp.loadList("enrollmentList",
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
