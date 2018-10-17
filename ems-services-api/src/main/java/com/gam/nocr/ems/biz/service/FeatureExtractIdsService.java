package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.nocr.ems.data.domain.FeatureExtractIdsTO;
import com.gam.nocr.ems.data.domain.OfficeSettingTO;
import com.gam.nocr.ems.data.domain.vol.OfficeSettingVTO;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/13/18.
 */
public interface FeatureExtractIdsService extends Service {

    SearchResult fetchFeatureExtractIdList(String searchString, int from, int to, String orderBy) throws BaseException;

}
