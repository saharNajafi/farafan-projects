package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.PortalManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.RatingInfoTO;
import com.gam.nocr.ems.data.enums.LocationType;
import com.gam.nocr.ems.util.EmsUtil;

import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class PortalManagementDelegator implements Delegator {

    private PortalManagementService getService(UserProfileTO userProfileTO) throws BaseException {
        ServiceFactory factory = ServiceFactoryProvider.getServiceFactory();
        PortalManagementService portalManagementService;
        try {
            portalManagementService = factory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_PORTAL_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.PMD_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_PORTAL_MANAGEMENT.split(","));
        }
        portalManagementService.setUserProfileTO(userProfileTO);
        return portalManagementService;
    }

    /**
     * The method updateRequestStates is used to notify the sub system 'Portal' about the update of the state of the
     * requests. This method is done via a specified job.
     *
     * @throws BaseException
     */
    public Boolean updateRequestStates(List<Long> batchIds) throws BaseException {
        return getService(null).doActivityForUpdateState(batchIds);
    }

    public List<Long> fetchReservationIds() throws BaseException {
        return getService(null).fetchReservationIds();
    }

    /**
     * The method doActivityForReservations is used to fetch and save the reservations which were done on the sub
     * system 'Portal'. This method is done via a specified job
     *
     * @throws BaseException
     */
    public Boolean doActivityForReservations(List<Long> reservationIds) throws BaseException {
        return getService(null).doActivityForReservations(reservationIds);
    }
    
    /**
     * new method for transfer reservations. this method is used to fetch and save the reservations which were done on
     * 'Portal'. called by <code>PortalReservationsJob</code>
     * @param longList
     */
    public Boolean transferReservationsToEMSAndDoEstelam2(List<Long> longList) throws BaseException {
		return getService(null).transferReservationsToEMSAndDoEstelam2(longList);
	}

    public void updateCardRequestFromCCOSAndMES(int from, int to) throws BaseException {
        getService(null).doActivityForUpdateCcosAndMESCardRequest(from, to);
    }

    /**
     * The method notifyPortalAboutModifiedProvinces is used to notify the subsystem 'Portal' about any modification on
     * the instances of type {@link com.gam.nocr.ems.data.domain.LocationTO}. This method is triggered via a specified
     * job.
     *
     * @param locationType
     * @param from
     * @param to
     * @throws BaseException
     */
    public void notifyPortalAboutModifiedProvinces(LocationType locationType, Integer from, Integer to) throws BaseException {
        getService(null).notifyPortalAboutModifiedProvinces(locationType, from, to);
    }

    /**
     * The method transferNotVerifiedMESRequestsToPortal is used to transfer the requests that have not been verified by
     * IMS and have origins of type 'MES'. This method is called via a specified Job
     *
     * @return an instance of type {@link Boolean}, which is filled by true or false. If there are other data to send,
     *         then the return value will be true, otherwise it will be valued by false
     * @throws BaseException
     */
    public Boolean transferNotVerifiedMESRequestsToPortal() throws BaseException {
        return getService(null).transferNotVerifiedMESRequestsToPortal();
    }

    public List<Long> fetchRequestedSmsIds() throws BaseException {
        return getService(null).fetchRequestedSmsIds();
    }

    public void addRequestedSms(Long portalCardRequestId) throws BaseException {
        getService(null).addRequestedSms(portalCardRequestId);
    }

	public Integer fetchReferToCCOSProcessSms() throws BaseException {
		return getService(null).fetchReferToCCOSProcessSms();
	}
    
    public Integer fetchReadyToProcessSms() throws BaseException {
        return getService(null).fetchReadyToProcessSms();
    }

    public Boolean processSms(Integer from, Integer to) throws BaseException {
        return getService(null).processSms(from, to);
    }
    
    public Boolean processReferToCCOSSms(Integer from, Integer to) throws BaseException {
		return getService(null).processReferToCCOSSms(from, to);
	}

    public Long fetchModifiedLocationCount(LocationType locationType) throws BaseException {
        return getService(null).fetchModifiedLocationCount(locationType);
    }

    public Long getCcosAndVerifiedMESRequestsCount() throws BaseException{
        return getService(null).getCcosAndVerifiedMESRequestsCount();
    }

    //Anbari
  	public int deleteReservationDateFromOfficeRSVFreeTime(Long dateForDelete) throws BaseException{
  		 return getService(null).deleteReservationDateFromOfficeRSVFreeTime(dateForDelete);
  	}
    
    //Anbari
	public void notifyPortalRezervationFreeTime(List<Long> eofIds,Long date) throws BaseException{
		 getService(null).notifyPortalRezervationFreeTime(eofIds,date);
	}

    //Anbari
	public void syncResevationFreeTimeByNewRating(Long eofId, RatingInfoTO ratingInfo,String newCalender) throws BaseException{
		 getService(null).syncResevationFreeTimeByNewRating(eofId, ratingInfo,newCalender);

	}

	//Madanipour
	public Integer fetchSmsCount(int smsType) throws BaseException {
		return getService(null).fetchSmsCount(smsType);
	}

	//Madanipour
	public Boolean processSmsToSend(Integer from, int to, int smsType) throws BaseException {
		return getService(null).processSmsToSend(from, to, smsType);
	}

	//Madanipour
	public void deleteOldRecordsFromMsgt(Integer timeInterval, Integer smsType) throws BaseException{
		getService(null).deleteOldRecordsFromMsgt(timeInterval, smsType);
		
	}

}
