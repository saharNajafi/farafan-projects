package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;
import com.gam.nocr.ems.data.enums.ShiftEnum;

import java.util.List;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 5/26/18.
 */
public interface OfficeCapacityDAO extends EmsBaseDAO<OfficeCapacityTO> {

    List<OfficeCapacityTO> findByEnrollmentOfficeIdAndShiftNo(Long enrollmentOfficeId, ShiftEnum shiftNo) throws BaseException;

    boolean removeOfficeCapacities(String officaCapacityId) throws BaseException;

    OfficeCapacityTO findByEnrollmentOfficeId(Long enrollmentOfficeId) throws BaseException;

    List<OfficeCapacityTO> listOfficeCapacityByDate(int startDate, int endDate) throws DAOException;

    void removeByEnrollmentOfficeId(Long enrollmentOfficeId) throws BaseException;

    OfficeCapacityTO findByEnrollmentOfficeIdAndDateAndWorkingHour(Long enrollmentOfficeId, int date, float hour) throws DAOException;
}
