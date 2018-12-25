package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.commons.core.data.domain.SearchResult;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/13/18.
 */
public interface FeatureExtractCCService extends Service {
    SearchResult FeatureExtractCCList(String searchString, int from, int to, String orderBy) throws BaseException;

}
