package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;
import com.gam.nocr.ems.data.domain.vol.OfficeCapacityVTO;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 5/26/18.
 */
public interface OfficeCapacityService extends Service {

    public Long save(OfficeCapacityVTO to) throws BaseException;

    public Long update(OfficeCapacityVTO to) throws BaseException;

    public boolean remove(String officeCapacityIds) throws BaseException;

    public OfficeCapacityVTO load(Long officeCapacityId) throws BaseException;

    OfficeCapacityTO findByEnrollmentOfficeId(Long enrollmentOfficeId) throws BaseException;

    List<OfficeCapacityTO> listOfficeCapacityByDate(int startDate, int endDate) throws BaseException;
}
