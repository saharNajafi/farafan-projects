package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.FeatureExtractIdsTO;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/16/18.
 */
public interface FeatureExtractIdsDAO extends EmsBaseDAO<FeatureExtractIdsTO> {

    FeatureExtractIdsTO findById(Long fenId) throws BaseException;
}
