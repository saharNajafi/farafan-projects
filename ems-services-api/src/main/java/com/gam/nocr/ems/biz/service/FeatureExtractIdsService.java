package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.nocr.ems.data.domain.vol.FeatureExtractIdsVTO;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/13/18.
 */
public interface FeatureExtractIdsService extends Service {

    SearchResult fetchFeatureExtractIdsNormalList(String searchString, int from, int to, String orderBy) throws BaseException;

    SearchResult fetchFeatureExtractIdsCCList(String searchString, int from, int to, String orderBy) throws BaseException;

    List<FeatureExtractIdsVTO> load(Long id) throws BaseException;

    SearchResult fetchMOCEngineEnhancementAutoCompleteIdList(String searchString, int from, int to, String orderBy) throws BaseException;
}
