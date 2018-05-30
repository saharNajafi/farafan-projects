package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 5/26/18.
 */
public interface OfficeCapacityDAO extends EmsBaseDAO<OfficeCapacityTO> {
    public List<OfficeCapacityTO> findByEnrollmentOfficeId(Long enrollmentOfficeId) throws BaseException;
}
