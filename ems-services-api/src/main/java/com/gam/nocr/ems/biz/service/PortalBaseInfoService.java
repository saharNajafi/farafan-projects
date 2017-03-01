package com.gam.nocr.ems.biz.service;

import java.util.List;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.nocr.ems.biz.service.external.client.portal.EnrollmentOfficeWTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.HolidayTO;
import com.gam.nocr.ems.data.domain.LocationTO;
import com.gam.nocr.ems.data.domain.RatingInfoTO;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface PortalBaseInfoService extends Service {

    /**
     * The method updateEnrollmentOffices is used to notify the sub system 'Portal' about creating or updating of
     * enrollmentOffices
     *
     * @param enrollmentOfficeTOList a list of type {@link EnrollmentOfficeTO}
     * @throws BaseException
     */
    void updateEnrollmentOffices(List<EnrollmentOfficeTO> enrollmentOfficeTOList) throws BaseException;

    /**
     * The method removeEnrollmentOffices is used to notify the sub system 'Portal' about removing some of the
     * enrollmentOffices
     *
     * @param enrolmentOfficeIdList a list of type {@link Long}
     * @throws BaseException
     */
    void removeEnrollmentOffices(List<Long> enrolmentOfficeIdList) throws BaseException;

    /**
     * The method updateRatingInfo is used to notify the subsystem 'Portal' about updating the rating info
     *
     * @param ratingInfoTOList a list of type {@link RatingInfoTO} which consists of the new or the modified instances
     * @throws BaseException
     */
    void updateRatingInfo(List<RatingInfoTO> ratingInfoTOList) throws BaseException;

    /**
     * The method updateLocations is used to notify the subsystem 'Portal' about updating the locations
     *
     * @param locationTOList a list of type {@link LocationTO}
     * @throws BaseException
     */
    List<Long> updateLocations(List<LocationTO> locationTOList) throws BaseException;
    
    
    void updateHoliday(List<HolidayTO> holidayTOList) throws BaseException;

    ////Anbari
	void notifyPortalRezervationFreeTime(List<Long> eofIds,Long date) throws BaseException;
	
	//Anbari
	int deleteReservationDateFromOfficeRSVFreeTime(Long dateForDelete) throws BaseException;
	
	void checkEnrollmentOfficeDeletePossibilityAndPerform(long id) throws BaseException;

	//Anbari
	void syncResevationFreeTimeByNewRating(Long eofId, RatingInfoTO ratingInfo,String newCalender) throws BaseException;
	
	
	
	
}
