package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.vol.FeatureExtractIdsVTO;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/15/18.
 */
public interface OfficeSettingService extends Service {

    Long update(FeatureExtractIdsVTO to) throws BaseException;

    Long save(FeatureExtractIdsVTO to) throws BaseException;
}
