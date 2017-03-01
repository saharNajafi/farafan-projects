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

import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.Map;

/**
 * The list reader processor class for persons list in a grid in CCOS
 *
 * @author <a href="mailto:saadat@gamelectronics.com.com">Alireza Saadat</a>
 */
public class CcosHelpProcessor extends EMSVLPListProcessor {
    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        //  Retrieve a list of parameters sent by client
        Map parameters = getQueryParrameters(paramProvider);

        UserProfileTO uto = paramProvider.getUserProfileTO();
        Long personID = null;
        String perid = "0";
        if (uto != null)
        {
        	try {
        		personID = getPersonService().findPersonIdByUsername(uto.getUserName());
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            perid = "" + personID;
        }
        parameters.put("perid", perid);

        //  Retrieve a VLP implementation of the underlying project
        ValueListProvider vlp = getValueListProvider();
        //  Determe requested ordering of the result
        String orderBy = getOrderBy(paramProvider);
        //  Create an instance of VLH base on given parameters
        ValueListHandler vlh;
        try {
            vlh = vlp.loadList(paramProvider.getListName(),
                    "main".split(","),
                    "count".split(","),
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
