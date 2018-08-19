package com.gam.nocr.ems.biz.service.ims;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.CitizenInfoTO;
import com.gam.nocr.ems.data.domain.vol.BirthCertIssPlaceVTO;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 8/15/18.
 */
public interface CitizenInfoService extends Service {

    void checkCitizenInfoValid(CitizenInfoTO czi) throws BaseException;

    List<BirthCertIssPlaceVTO> fetchBirthCertIssPlace(String nationalID) throws BaseException;
}
