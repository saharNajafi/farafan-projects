package com.gam.nocr.ems.web.listreader.processor;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.listreader.ListReaderException;
import com.gam.commons.listreader.ListResult;
import com.gam.commons.listreader.ParameterProvider;
import com.gam.nocr.ems.biz.delegator.LocationDelegator;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.ws.ProvinceWTO;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;
import com.gam.nocr.ems.util.EmsUtil;

import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;
import gampooya.tools.vlp.ValueListProvider;

import java.util.HashMap;
import java.util.List;

/**
 * The list reader processor class for province grid
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class ProvinceProcessor extends EMSVLPListProcessor {

    protected ValueListHandler prepareVLH(ParameterProvider paramProvider) throws ListReaderException {
        HashMap parameters = new HashMap();
        StringBuilder parts = new StringBuilder();
        //  Retrieve a VLP implementation of the underlying project
        ValueListProvider vlp = getValueListProvider();
        //  Determe requested ordering of the result
        String orderBy = getOrderBy(paramProvider);
        //  Create an instance of VLH base on given parameters
        ValueListHandler vlh;

        String parentId = paramProvider.getParameter("parentId");

        if (EmsUtil.checkString(parentId)) {
            if (!"-1".equals(parentId)) {
                parameters.put("parentId", parentId);
                parts.append(",locations,city");
            }
        } else {
            parts.append(",locations,state");
        }

        try {
            vlh = vlp.loadList("provinceList",
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
    /**
     * this method fetchs all locations to show in ccos and in complete registration 
     * @author ganjyar
     * 
     */
    @Override
    public ListResult fetchList(ParameterProvider paramProvider)
			throws ListReaderException {

		if (paramProvider.getListName().equals("provinceList"))

			return super.fetchList(paramProvider);

		else if (paramProvider.getListName().equals("locationLivingList")) {
			try {
				UserProfileTO uto = paramProvider.getUserProfileTO();
				String perid = "0";
				Long personID = null;
				
				if (uto != null)
				{
					try {
						personID = getPersonService().findPersonIdByUsername(uto.getUserName());
					} catch (BaseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					perid = "" + personID;
				}

				HashMap parameters = new HashMap();
				StringBuilder parts = new StringBuilder();
				String orderBy = getOrderBy(paramProvider);

				String parentId = paramProvider.getParameter("parentId");
				String livingType = paramProvider.getParameter("userCityType");

				if (EmsUtil.checkString(parentId)) {
					if (!"-1".equals(parentId)) {
						parameters.put("parentId", parentId);
					}
				} 
				if (EmsUtil.checkString(livingType)) {
						parameters.put("livingType", livingType);
				}
				LocationDelegator locationDelegator = new LocationDelegator();
				List<ProvinceWTO> fetchLocationLivingList = locationDelegator.fetchLocationLivingList(new GeneralCriteria(
						parameters, orderBy, uto, null, null));
				return new ListResult("locationLivingList", fetchLocationLivingList.size(), fetchLocationLivingList);
				
			} catch (BaseException e) {
				
				throw new ListReaderException("Unable to prepare a VLH to fetch list named '" + paramProvider.getListName() + "'", e);
				
			}
			 catch (Exception e) {
				 throw new ListReaderException("Unable to prepare a VLH to fetch list named '" + paramProvider.getListName() + "'", e);
			 
			 }
				
		}
		return null;
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
