package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 5/26/18.
 */
public interface OfficeCapacityService extends Service {
    public Long save(OfficeCapacityTO to) throws BaseException;

    public Long update(OfficeCapacityTO to) throws BaseException;

    public List<OfficeCapacityTO> fetchOfficeCapacityList(Long EnrollmentOfficeId) throws BaseException;

    public OfficeCapacityTO load(Long officeCapacityId) throws BaseException;
}
