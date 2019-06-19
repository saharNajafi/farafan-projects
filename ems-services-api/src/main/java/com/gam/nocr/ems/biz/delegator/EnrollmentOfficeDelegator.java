package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.SearchResult;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.EnrollmentOfficeService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.EnrollmentOfficeDAO;
import com.gam.nocr.ems.data.dao.RatingInfoDAO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.OfficeCapacityTO;
import com.gam.nocr.ems.data.domain.vol.EnrollmentOfficeVTO;
import com.gam.nocr.ems.data.domain.ws.HealthStatusWTO;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeDeliverStatus;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeType;
import com.gam.nocr.ems.util.EmsUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class EnrollmentOfficeDelegator implements Delegator {

    private EnrollmentOfficeService getService(UserProfileTO userProfileTO) throws BaseException {
        EnrollmentOfficeService enrollmentOfficeService;
        try {
            enrollmentOfficeService = ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_ENROLLMENT_OFFICE), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.ODL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_ENROLLMENT_OFFICE.split(","));
        }
        enrollmentOfficeService.setUserProfileTO(userProfileTO);
        return enrollmentOfficeService;
    }

    private EnrollmentOfficeDAO getEnrollmentOfficeDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_ENROLLMENT_OFFICE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.ODL_002,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_ENROLLMENT_OFFICE.split(","));
        }
    }

    private RatingInfoDAO getRatingInfoDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_RATING_INFO));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RMS_006, BizExceptionCode.GLB_001_MSG, e, EMSLogicalNames.DAO_RATING_INFO.split(","));
        }
    }

    public EnrollmentOfficeTO find(Long officeId) throws BaseException {
        return getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class, officeId);
    }

    public EnrollmentOfficeVTO load(UserProfileTO userProfileTO, long officeId) throws BaseException {
        return getService(userProfileTO).load(officeId);
    }

    public long save(UserProfileTO userProfileTO, EnrollmentOfficeVTO vto) throws BaseException {
        Long eofId = getService(userProfileTO).save(vto);
        if (eofId != null) {
            checkOfficeDelivery(vto, "NEW", null, vto.getOfficeDeliver());
        }
        return eofId;

    }

    private void checkOfficeDelivery(EnrollmentOfficeVTO vto, String mode, EnrollmentOfficeDeliverStatus oldDeliveryState, String newDeliveryState) throws BaseException {

        if (vto.getOfficeType().equals(EnrollmentOfficeType.OFFICE.name())) {
            if (oldDeliveryState == EnrollmentOfficeDeliverStatus.ENABLED && "0".equals(newDeliveryState))
                getService(null).disableOfficeDeliveryFeature(vto.getId(), vto.getSuperiorOfficeId(), mode);
//        	else if(oldDeliveryState == EnrollmentOfficeDeliverStatus.DISABLED && "1".equals(newDeliveryState))
//        		getService(null).enableOfficeDeliveryFeature(vto.getId(),vto.getSuperiorOfficeId(),mode);
        }

    }

//    public long update(UserProfileTO userProfileTO, EnrollmentOfficeVTO to) throws BaseException {
//         Long eofId = getService(userProfileTO).update(to);
//         if(eofId != null){
//	         PortalManagementDelegator portalManagementDelegator = new PortalManagementDelegator();
//	         RatingInfoTO ratingInfoTO = getRatingInfoDAO().find(RatingInfoTO.class, to.getRateId());
//	         if(ratingInfoTO != null)
//	        	 portalManagementDelegator.syncResevationFreeTimeByNewRating(eofId, ratingInfoTO);
//         }
//         return eofId;
//    }


    public long update(UserProfileTO userProfileTO, EnrollmentOfficeVTO vto) throws BaseException {
        EnrollmentOfficeTO eofOld = getEnrollmentOfficeDAO().find(
                EnrollmentOfficeTO.class, vto.getId());
//        Long ratingIdOld = eofOld.getRatingInfo().getId();
        EnrollmentOfficeDeliverStatus eofOldDelivery = eofOld.getDeliver();
        String eofNewDelivery = vto.getOfficeDeliver();
        Long eofId = getService(userProfileTO).update(vto);
//		boolean equalsCalender = vto.getCalenderType().equals(
//				OfficeCalenderType.toLong(eofOld.getCalenderType()).toString());
//		if (eofId != null || !equalsCalender) {
        if (eofId != null) {
            checkOfficeDelivery(vto, "EDIT", eofOldDelivery, eofNewDelivery);
//			PortalManagementDelegator portalManagementDelegator = new PortalManagementDelegator();
//            RatingInfoTO ratingInfoTO = getRatingInfoDAO().find(
//                    RatingInfoTO.class, vto.getRateId());
			/*if (ratingInfoTO != null || !equalsCalender) {

                if (ratingInfoTO != null) {
                    RatingInfoTO newRating = null;
                    String newCalender = null;
                    if (!ratingIdOld.equals(ratingInfoTO.getId()))
                        newRating = ratingInfoTO;
//				if (!equalsCalender)
//					newCalender = vto.getCalenderType();

//				portalManagementDelegator.syncResevationFreeTimeByNewRating(
//						eofId, newRating,newCalender);
                }
            }*/
//
        }
        return eofId;
    }

//    public boolean remove(UserProfileTO userProfileTO, String officeIds) throws BaseException {
//        return getService(userProfileTO).remove(officeIds);
//    }

    public boolean checkForActiveRequest(UserProfileTO userProfileTO, long enrollmentOfficeId) throws BaseException {
        return getService(userProfileTO).checkForActiveRequest(enrollmentOfficeId);
    }

//    public void revokeAndSubstitute(UserProfileTO userProfileTO,
//                                    Long enrollmentOfficeId,
//                                    Long superiorEnrollmentOfficeId,
//                                    ReplicaReason reason,
//                                    String comment) throws BaseException {
//        getService(userProfileTO).revokeAndSubstitute(enrollmentOfficeId, superiorEnrollmentOfficeId, reason, comment);
//    }

    public void substituteAndDelete(UserProfileTO userProfileTO,
                                    Long enrollmentOfficeId,
                                    Long superiorEnrollmentOfficeId) throws BaseException {
        getService(userProfileTO).substituteAndDelete(enrollmentOfficeId, superiorEnrollmentOfficeId);
    }

    public SearchResult fetchEnrollments(UserProfileTO userProfileTO, String searchString, int from, int to, String orderBy, Map additionalParams) throws BaseException {
        return getService(userProfileTO).fetchEnrollments(searchString, from, to, orderBy, additionalParams);
    }

    public SearchResult fetchNOCRList(UserProfileTO userProfileTO, String searchString, int from, int to, String orderBy, Map additionalParams) throws BaseException {
        return getService(userProfileTO).fetchNOCRs(searchString, from, to, orderBy, additionalParams);
    }

    public SearchResult fetchPersonEnrollments(UserProfileTO userProfileTO, String searchString, int from, int to, String orderBy, Map additionalParams) throws BaseException {
        return getService(userProfileTO).fetchPersonEnrollments(searchString, from, to, orderBy, additionalParams);
    }

    public Map<String, List<EnrollmentOfficeTO>> fetchModifiedEnrollmentOffices() throws BaseException {
        return getService(null).fetchModifiedEnrollmentOffices();
    }

    /**
     * The method notifySubSystemsAboutNewEnrollmentOffices is used to notify the sub systems 'CMS' and 'Portal' about
     * adding some new enrollment offices. This method is triggered via a specified job.
     *
     * @throws BaseException
     */
    public Long notifySubSystemsAboutEnrollmentOffices(EnrollmentOfficeTO enrollmentOfficeTO, String mode) throws BaseException {
        return getService(null).notifySubSystemsAboutEnrollmentOffices(enrollmentOfficeTO, mode);
    }

    public void updateSyncDateByCurrentDate(Map<String, List<Long>> updatedEnrollmentOffice) throws BaseException {
        getService(null).updateSyncDateByCurrentDate(updatedEnrollmentOffice);
    }

//    public Long issueToken(UserProfileTO userProfileTO, Long enrollmentId, TokenType type) throws BaseException {
//        return getService(userProfileTO).issueToken(enrollmentId, type);
//    }
//
//    public Long replicateToken(UserProfileTO userProfileTO, Long enrollmentId, TokenType type, ReplicaReason reason) throws BaseException {
//        return getService(userProfileTO).replicateToken(enrollmentId, type, reason);
//    }
//
//    public Long reissueToken(UserProfileTO userProfileTO, Long enrollmentOfficeId) throws BaseException {
//        return getService(userProfileTO).reissueToken(enrollmentOfficeId);
//    }
//
//    public void deliverToken(UserProfileTO userProfileTO, Long enrollmentOfficeId) throws BaseException {
//        getService(userProfileTO).deliverToken(enrollmentOfficeId);
//    }
//
//    public void revokeToken(UserProfileTO userProfileTO, Long enrollmentOfficeId, ReplicaReason reason, String comment) throws BaseException {
//        getService(userProfileTO).revokeToken(enrollmentOfficeId, reason, comment);
//    }

    /**
     * The method deleteToken is used to delete an instance of type {@link com.gam.nocr.ems.data.domain.NetworkTokenTO}
     *
     * @param userProfileTO is an instance of type {@link UserProfileTO}
     * @param enrollmentId  Identifier of an {@link com.gam.nocr.ems.data.domain.EnrollmentOfficeTO} to delete its
     *                      ready to send token request
     * @throws BaseException
     */
//    public void deleteToken(UserProfileTO userProfileTO,
//                            Long enrollmentId) throws BaseException {
//        getService(userProfileTO).deleteEnrollmentOfficeToken(enrollmentId);
//    }
    public Boolean checkInProgressRequests(UserProfileTO userProfileTO,
                                           Long enrollmentId) throws BaseException {
        return getService(userProfileTO).checkInProgressRequests(enrollmentId);
    }

    // Anbari
    public List<Long> getEnrollmentOfficeListIds() throws BaseException {
        return getService(null).getEnrollmentOfficeListIds();

    }

    public List<EnrollmentOfficeTO> getEnrollmentOfficeList() throws BaseException {
        return getService(null).getEnrollmentOfficeList();

    }

    // hossein message
    public SearchResult fetchEnrollmentsByProvince(UserProfileTO userProfile,
                                                   String searchString, int from, int to, String orderBy,
                                                   Map additionalParams) throws BaseException {
        return getService(userProfile).fetchEnrollmentsByProvince(searchString,
                from, to, orderBy, additionalParams);
    }

    /**
     * @param userProfile
     * @param enrollmentId
     * @param officeSettingType
     * @throws BaseException
     * @author Madanipour
     */
    public void chageOfficeSetting(UserProfileTO userProfile,
                                   long enrollmentId, String officeSettingType)
            throws BaseException {
        getService(userProfile).changeOfficeSetting(enrollmentId,
                officeSettingType);

    }

    /**
     * @param userProfile
     * @return
     * @throws BaseException
     * @author Madanipour
     */
    public Boolean getAccessViewAndChangeOfficeSetting(UserProfileTO userProfile)
            throws BaseException {
        return getService(userProfile).getAccessViewAndChangeOfficeSetting(userProfile);
    }


    public SearchResult fetchOfficesAutoComplete(UserProfileTO userProfileTO, String searchString, int from, int to, String orderBy, Map additionalParams) throws BaseException {
        return getService(userProfileTO).fetchOfficesAutoComplete(userProfileTO, searchString, from, to, orderBy, additionalParams);
    }


    public void checkEnrollmentOfficeEligibleForSingleStageEnrollment(
            UserProfileTO userProfileTO, String nationalId, HealthStatusWTO healthStatusWTO, Long enrollmentOfficeId) throws BaseException {
        getService(userProfileTO).checkEnrollmentOfficeEligibleForSingleStageEnrollment(nationalId, healthStatusWTO, enrollmentOfficeId);
    }

    public List<OfficeCapacityTO> listOfficeCapacityByDate(int startDate, int endDate) throws BaseException {
        return getService(null).listOfficeCapacityByDate(startDate, endDate);
    }

    public void updateActiveShiftForEnrollmentOfficeAndDate(EnrollmentOfficeTO enrollmentOfficeTO, Date fromDate,
                                                            Map<Long, List<OfficeCapacityTO>> officeCapacityMap) throws BaseException {
        getService(null).updateActiveShiftForEnrollmentOfficeAndDate(enrollmentOfficeTO, fromDate, officeCapacityMap);
    }
}
