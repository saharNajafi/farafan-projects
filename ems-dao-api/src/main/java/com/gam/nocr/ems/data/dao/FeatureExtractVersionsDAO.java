package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.FeatureExtractIdsTO;
import com.gam.nocr.ems.data.domain.FeatureExtractVersionsTO;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/16/18.
 */
public interface FeatureExtractVersionsDAO extends EmsBaseDAO<FeatureExtractVersionsTO>{

    FeatureExtractVersionsTO findById(Long fevId) throws BaseException;
}
