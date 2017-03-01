package com.gam.nocr.ems.biz.delegator;

import java.util.List;
import java.util.Map;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.LocationService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.ws.ProvinceWTO;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class LocationDelegator {

    private LocationService getService(UserProfileTO userProfileTO) throws BaseException {
        LocationService locationService = null;
        try {
            locationService = (LocationService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_LOCATION), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.LDL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_LOCATION.split(","));
        }
        locationService.setUserProfileTO(userProfileTO);
        return locationService;
    }

    public SearchResult fetchLocations(UserProfileTO userProfileTO, String searchString, int from, int to, String orderBy, Map additionalParams) throws BaseException {
        return getService(userProfileTO).fetchLocations(searchString, from, to, orderBy, additionalParams);
    }

    public SearchResult getAllProvinces(UserProfileTO userProfileTO, String searchString, int from, int to, String orderBy, Map additionalParams) throws BaseException {
        return getService(userProfileTO).getAllProvinces(searchString, from, to, orderBy, additionalParams);
    }
    /**
     * @author ganjyar
     * @param generalCriteria
     * @return
     * @throws BaseException
     */
    public List<ProvinceWTO> fetchLocationLivingList(GeneralCriteria generalCriteria) throws BaseException{

		return getService(generalCriteria.getUserProfileTO()).fetchLocationLivingList(generalCriteria);
		
	}
}