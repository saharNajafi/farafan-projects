package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.nocr.ems.data.domain.ws.ProvinceWTO;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;

import java.util.List;
import java.util.Map;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public interface LocationService extends Service {
    public SearchResult fetchLocations(String searchString, int from, int to, String orderBy, Map additionalParams) throws BaseException;

	public SearchResult getAllProvinces(String searchString, int from, int to,
			String orderBy, Map additionalParams) throws BaseException;
/**
 * @author ganjyar
 * @param generalCriteria
 * @return
 * @throws BaseException
 */
	public List<ProvinceWTO> fetchLocationLivingList(GeneralCriteria generalCriteria) throws BaseException;

}
