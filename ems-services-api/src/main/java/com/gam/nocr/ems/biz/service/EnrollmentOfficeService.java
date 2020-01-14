package com.gam.nocr.ems.biz.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.vol.EnrollmentOfficeSingleStageVTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;
import com.gam.nocr.ems.data.domain.vol.EnrollmentOfficeVTO;
import com.gam.nocr.ems.data.domain.ws.HealthStatusWTO;
import com.gam.nocr.ems.data.domain.ws.OfficeAppointmentWTO;
import com.gam.nocr.ems.data.enums.enrollmentOfficeDeletableStates;
import com.gam.nocr.ems.data.enums.ShiftEnum;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public interface EnrollmentOfficeService extends Service /* ,TokenService Commented By Adldoost */ {

    public Long save(EnrollmentOfficeVTO to) throws BaseException;

    public Long update(EnrollmentOfficeVTO to) throws BaseException;

    public EnrollmentOfficeVTO load(Long officeId) throws BaseException;

    //public boolean remove(String officeIds) throws BaseException;

    public boolean checkForActiveRequest(long enrollmentOfficeId) throws BaseException;

    /**
     * The method revokeAndSubstitute is used to replace a specified enrollmentOffice with another one
     *
     * @param enrollmentOfficeId         represents the id of the old enrollmentOffice
     * @param superiorEnrollmentOfficeId represents the id of the superior enrollmentOffice
     * @param reason
     * @param comment
     * @throws BaseException
     */
//    public void revokeAndSubstitute(Long enrollmentOfficeId,
//                                    Long superiorEnrollmentOfficeId
//                                    /*ReplicaReason reason,
//                                    String comment*/) throws BaseException;

    public SearchResult fetchEnrollments(String searchString, int from, int to, String orderBy, Map additionalParams) throws BaseException;
    
    public SearchResult fetchPersonEnrollments(String searchString, int from, int to, String orderBy, Map additionalParams) throws BaseException;
    
    SearchResult fetchNOCRs(String searchString, int from, int to, String orderBy, Map additionalParams) throws BaseException ;

    Map<String, List<EnrollmentOfficeTO>> fetchModifiedEnrollmentOffices() throws BaseException;

    /**
     * The method notifySubSystemsAboutNewEnrollmentOffices is used to notify the sub systems 'CMS' and 'Portal' about
     * adding some new enrollment offices.
     *
     * @throws BaseException
     */
    Long notifySubSystemsAboutEnrollmentOffices(EnrollmentOfficeTO enrollmentOfficeTO, String mode) throws BaseException;

    void updateSyncDateByCurrentDate(Map<String, List<Long>> enrollmentOfficeIds) throws BaseException;

    /**
     * The method deleteEnrollmentOfficeToken is used to delete the token of a specified EnrollmentOffice
     *
     * @param EnrollmentOfficeId is an instance of type {@link Long}, which represents a specified instance of type {@link com.gam.nocr.ems.data.domain.EnrollmentOfficeTO}
     * @throws BaseException
     */
//Commented By Adldoost
    //void deleteEnrollmentOfficeToken(Long EnrollmentOfficeId) throws BaseException;

    /**
     * The method checkInProgressRequests is used to check the requests that are in progress
     *
     * @param enrollmentOfficeId is an instance of type {@link Long}, which represents a specified instance of type {@link com.gam.nocr.ems.data.domain.EnrollmentOfficeTO}
     * @return true or false
     * @throws BaseException
     */
    enrollmentOfficeDeletableStates checkInProgressRequests(Long enrollmentOfficeId) throws BaseException;
    
    
    //Anbari
    List<Long> getEnrollmentOfficeListIds() throws BaseException;
    
    //Anbari
    Future disableOfficeDeliveryFeature(Long eofId, Long supperiorOfficeId,String mode) throws BaseException;

	//Anbari
	//Long  enableOfficeDeliveryFeature(Long eofId, Long superiorOfficeId,String mode)throws BaseException;
	

	boolean substituteAndDelete(Long enrollmentOfficeId,Long superiorEnrollmentOfficeId) throws BaseException;

	//hossein messaging
	SearchResult fetchEnrollmentsByProvince(String searchString,
			int from, int to, String orderBy, Map additionalParams) throws BaseException;
	
	/**
	 * @author Madanipour
	 * @param enrollmentId
	 * @param officeSettingType
	 * @throws BaseException
	 */
	public void changeOfficeSetting(long enrollmentId,
			String officeSettingType) throws BaseException;

	public Boolean getAccessViewAndChangeOfficeSetting(UserProfileTO userProfile)throws BaseException;

	 public SearchResult fetchOfficesAutoComplete(UserProfileTO userProfileTO,String searchString, int from, int to, String orderBy, Map additionalParams) throws BaseException;

    void checkEnrollmentOfficeEligibleForSingleStageEnrollment(
            String nationalId, HealthStatusWTO healthStatusWTO, Long enrollmentOfficeId) throws BaseException;

    void updateActiveShift(CardRequestTO emsCardRequest, Long id, Integer activeDate, ShiftEnum shiftNo) throws BaseException;

    void editEnrollmentOfficeAppointment(CardRequestTO cardRequestTO, OfficeAppointmentWTO officeAppointment) throws BaseException;

    List<EnrollmentOfficeTO> getEnrollmentOfficeList() throws BaseException;

    List<OfficeCapacityTO> listOfficeCapacityByDate(int startDate, int endDate) throws BaseException;

    void updateActiveShiftForEnrollmentOfficeAndDate(EnrollmentOfficeTO enrollmentOfficeTO, Date fromDate, Map<Long, List<OfficeCapacityTO>> officeCapacityMap) throws BaseException;

    EnrollmentOfficeSingleStageVTO findEnrollmentOfficeSingleStageById(Long enrollmentOfficeId) throws BaseException;

    Boolean getHasAccessToViewAndChangeOfficeOnCardSetting(UserProfileTO userProfile) throws BaseException;;
}
