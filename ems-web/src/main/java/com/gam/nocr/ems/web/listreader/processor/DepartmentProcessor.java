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
 * The list reader processor class for departments grid
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class DepartmentProcessor extends EMSVLPListProcessor {
    private static final Logger logger = BaseLog.getLogger(DepartmentProcessor.class);

    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        HashMap parameters = new HashMap();
        StringBuilder parts = new StringBuilder();
        ValueListProvider vlp = getValueListProvider();
        String orderBy = getOrderBy(paramProvider);

        //check grid filters and add related parts and parameters
        String address = paramProvider.getFilter("address");
        String code = paramProvider.getFilter("code");
        String name = paramProvider.getFilter("name");
        String sendType = paramProvider.getFilter("sendType");
        String dn = paramProvider.getFilter("dn");
        String zip = paramProvider.getFilter("postalCode");
        String parentName = paramProvider.getFilter("parentName");
        String locName = paramProvider.getFilter("locName");
        String provinceName = paramProvider.getFilter("provinceName");
        Integer perId;

        UserProfileTO uto = paramProvider.getUserProfileTO();
        Long personID = null;
		try {
			personID = getPersonService().findPersonIdByUsername(
					uto.getUserName());
		} catch (BaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        if (uto != null)
            perId = Integer.valueOf(("" + personID));
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
        if (EmsUtil.checkString(provinceName)) {
            parameters.put("province", "%" + provinceName + "%");
            parts.append(",province");
        }
        if (EmsUtil.checkString(address)) {
            parameters.put("address", "%" + address + "%");
            parts.append(",address");
        }
        if (EmsUtil.checkString(code)) {
            parameters.put("code", "%" + code + "%");
            parts.append(",code");
        }
        if (EmsUtil.checkString(name)) {
            parameters.put("name", "%" + name + "%");
            parts.append(",name");
        }
        if (EmsUtil.checkString(sendType)) {
            parameters.put("sendType", "%" + sendType + "%");
            parts.append(",sendType");
        }
        if (EmsUtil.checkString(dn)) {
            parameters.put("dn", "%" + dn + "%");
            parts.append(",dn");
        }
        if (EmsUtil.checkString(zip)) {
            parameters.put("zip", "%" + zip + "%");
            parts.append(",zip");
        }
        if (EmsUtil.checkString(parentName)) {
            parameters.put("parentName", "%" + parentName + "%");
            parts.append(",parentName");
        }
        if (EmsUtil.checkString(locName)) {
            parameters.put("locName", "%" + locName + "%");
            parts.append(",locName");
        }

        ValueListHandler vlh = null;
        try {
            vlh = vlp.loadList("departmentList",
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
