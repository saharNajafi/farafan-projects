package com.gam.nocr.ems.biz.service.external.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.external.client.portal.BasicInfoWS;
import com.gam.nocr.ems.biz.service.external.client.portal.BasicInfoWS_Service;
import com.gam.nocr.ems.biz.service.external.client.portal.EnrollmentOfficeWTO;
import com.gam.nocr.ems.biz.service.external.client.portal.ExternalInterfaceException_Exception;
import com.gam.nocr.ems.biz.service.external.client.portal.HolidayWTO;
import com.gam.nocr.ems.biz.service.external.client.portal.LocationWTO;
import com.gam.nocr.ems.biz.service.external.client.portal.RatingInfoWTO;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.HolidayTO;
import com.gam.nocr.ems.data.domain.LocationTO;
import com.gam.nocr.ems.data.domain.RatingInfoTO;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeStatus;
import com.gam.nocr.ems.data.enums.OfficeCalenderType;
import com.gam.nocr.ems.data.mapper.tomapper.EnrollmentOfficeMapper;
import com.gam.nocr.ems.data.mapper.tomapper.HolidayMapper;
import com.gam.nocr.ems.data.mapper.tomapper.ProvinceMapper;
import com.gam.nocr.ems.data.mapper.tomapper.RatingInfoMapper;
import com.gam.nocr.ems.util.EmsUtil;
import com.sun.xml.ws.client.BindingProviderProperties;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "PortalBaseInfoService")
@Local(PortalBaseInfoServiceLocal.class)
@Remote(PortalBaseInfoServiceRemote.class)
public class PortalBaseInfoServiceImpl extends EMSAbstractService implements
		PortalBaseInfoServiceLocal, PortalBaseInfoServiceRemote {

	/**
	 * Portal Exception Codes
	 */
	private static final String PORTAL_ERROR_BIW_001 = "EMS_W_BIW_001";
	private static final String PORTAL_ERROR_BIW_002 = "EMS_W_BIW_002";

	private static final String DEFAULT_WSDL_URL = "http://localhost:7001/portal-web/services/BasicInfoWS?wsdl";
	private static final String DEFAULT_NAMESPACE = "http://portalws.ws.web.portal.nocr.gam.com/";

	private static final Logger logger = BaseLog
			.getLogger(CMSServiceImpl.class);
	private static final Logger portalLogger = BaseLog
			.getLogger("PortalLogger");

	BasicInfoWS service = null;

	/**
	 * The method getService is used to get WebServices from Portal sub system
	 * 
	 * @return an instance of type {@link BasicInfoWS}
	 * @throws com.gam.commons.core.BaseException
	 *             if cannot get the service
	 */
	private BasicInfoWS getService() throws BaseException {
		try {
			ProfileManager pm = ProfileHelper.getProfileManager();

			String wsdlUrl = (String) pm.getProfile(
					ProfileKeyName.KEY_PORTAL_BASIC_INFO_ENDPOINT, true, null,
					null);
			String namespace = (String) pm.getProfile(
					ProfileKeyName.KEY_PORTAL_NAMESPACE, true, null, null);
			if (wsdlUrl == null)
				wsdlUrl = DEFAULT_WSDL_URL;
			if (namespace == null)
				namespace = DEFAULT_NAMESPACE;
			String serviceName = "BasicInfoWS";
			logger.debug("Portal Basic Info wsdl url: " + wsdlUrl);
			portalLogger.debug("Portal Basic Info wsdl url: " + wsdlUrl);

			BasicInfoWS port = new BasicInfoWS_Service(new URL(wsdlUrl),
					new QName(namespace, serviceName)).getBasicInfoWSPort();
			// BasicInfoWS port = new
			// BasicInfoWS_Service().getBasicInfoWSPort();
			EmsUtil.setJAXWSWebserviceProperties(port, wsdlUrl);
			return port;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.PBS_001,
					e.getMessage(), e);
		}
	}

	private static final String DEFAULT_FREETIME_WEBSERVICE_TIMEOUT = "300000";
	private BasicInfoWS getServiceForReservationFreeTime() throws BaseException {
		try {
			ProfileManager pm = ProfileHelper.getProfileManager();

			String wsdlUrl = (String) pm.getProfile(
					ProfileKeyName.KEY_PORTAL_BASIC_INFO_ENDPOINT, true, null,
					null);
			String namespace = (String) pm.getProfile(
					ProfileKeyName.KEY_PORTAL_NAMESPACE, true, null, null);
			if (wsdlUrl == null)
				wsdlUrl = DEFAULT_WSDL_URL;
			if (namespace == null)
				namespace = DEFAULT_NAMESPACE;
			String serviceName = "BasicInfoWS";
			logger.debug("Portal Basic Info wsdl url: " + wsdlUrl);
			portalLogger.debug("Portal Basic Info wsdl url: " + wsdlUrl);

			BasicInfoWS port = new BasicInfoWS_Service(new URL(wsdlUrl),
					new QName(namespace, serviceName)).getBasicInfoWSPort();
			 try {
	                String endPoint = wsdlUrl.split("\\?")[0];
	                Integer webserviceTimeout = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_FREETIME_RESERVATION_WEBSERVICE_TIMEOUT, DEFAULT_FREETIME_WEBSERVICE_TIMEOUT));
	                BindingProvider bindingProvider = (BindingProvider) port;
	                Map<String, Object> context = bindingProvider.getRequestContext();
	                context.put(BindingProviderProperties.CONNECT_TIMEOUT, webserviceTimeout);
	                context.put(BindingProviderProperties.REQUEST_TIMEOUT, webserviceTimeout);
	            } catch (WebServiceException e) {
	                throw new BaseException("EmsUtil Exception. An exception has happened in setting webservice timeout properties.", e);
	            } catch (Exception e) {
	                throw new BaseException("EmsUtil Exception. An exception has happened in setting webservice timeout properties.", e);
	            }
			return port;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.PBS_001,
					e.getMessage(), e);
		}
	}

	
	
	private List<EnrollmentOfficeWTO> convert(
			List<EnrollmentOfficeTO> enrollmentOfficeTOList)
			throws BaseException {
		try {
			List<EnrollmentOfficeWTO> enrollmentOfficeWTOList = new ArrayList<EnrollmentOfficeWTO>();

			for (EnrollmentOfficeTO enrollmentOfficeTO : enrollmentOfficeTOList) {
				EnrollmentOfficeWTO enrollmentOfficeWTO = new EnrollmentOfficeWTO();

				enrollmentOfficeWTO.setId(enrollmentOfficeTO.getId());
				enrollmentOfficeWTO.setEnrollmentOfficeName(enrollmentOfficeTO
						.getName());
				enrollmentOfficeWTO.setEnrollmentOfficeCode(enrollmentOfficeTO
						.getCode());
				if (enrollmentOfficeTO.getLocation() != null
						&& enrollmentOfficeTO.getLocation().getId() != null)
					enrollmentOfficeWTO.setLocationId(enrollmentOfficeTO
							.getLocation().getId());
				if (enrollmentOfficeTO.getRatingInfo() != null
						&& enrollmentOfficeTO.getRatingInfo().getId() != null)
					enrollmentOfficeWTO.setRatingInfoId(enrollmentOfficeTO
							.getRatingInfo().getId());
				enrollmentOfficeWTO
						.setEnrollmentOfficeAddress(enrollmentOfficeTO
								.getAddress());
				enrollmentOfficeWTO.setEnrollmentOfficeArea(enrollmentOfficeTO
						.getArea());
				enrollmentOfficeWTO.setEnrollmentOfficeFax(enrollmentOfficeTO
						.getFax());
				enrollmentOfficeWTO.setEnrollmentOfficePhone(enrollmentOfficeTO
						.getPhone());
				enrollmentOfficeWTO
						.setEnrollmentOfficePostalCode(enrollmentOfficeTO
								.getPostalCode());
				if (enrollmentOfficeTO.getStatus() != null) {
					if (EnrollmentOfficeStatus.ENABLED
							.equals(enrollmentOfficeTO.getStatus()))
						enrollmentOfficeWTO.setEnrollmentOfficeStatus(true);
					if (EnrollmentOfficeStatus.DISABLED
							.equals(enrollmentOfficeTO.getStatus()))
						enrollmentOfficeWTO.setEnrollmentOfficeStatus(false);
				}
				if (enrollmentOfficeTO.getType() != null)
					enrollmentOfficeWTO
							.setEnrollmentOfficeType(enrollmentOfficeTO
									.getType().name());
				enrollmentOfficeWTO
						.setEnrollmentOfficeWorkingHourFrom(enrollmentOfficeTO
								.getWorkingHoursFrom());
				enrollmentOfficeWTO
						.setEnrollmentOfficeWorkingHourTo(enrollmentOfficeTO
								.getWorkingHoursTo());
				
				enrollmentOfficeWTO.setKhosusiType(enrollmentOfficeTO.getKhosusiType().toString());
				//Calender
				enrollmentOfficeWTO.setCalenderType(OfficeCalenderType.toLong(enrollmentOfficeTO.getCalenderType()).toString());

				enrollmentOfficeWTOList.add(enrollmentOfficeWTO);
			}

			return enrollmentOfficeWTOList;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.PBS_006,
					BizExceptionCode.GLB_008_MSG, e);
		}
	}

	/**
	 * The method updateEnrollmentOffices is used to notify the sub system
	 * 'Portal' about creating or updating of enrollmentOffices
	 * 
	 * @param enrollmentOfficeTOList
	 *            a list of type
	 *            {@link com.gam.nocr.ems.data.domain.EnrollmentOfficeTO}
	 * @throws com.gam.commons.core.BaseException
	 */
	@Override
	public void updateEnrollmentOffices(
			List<EnrollmentOfficeTO> enrollmentOfficeTOList)
			throws BaseException {
		List<EnrollmentOfficeWTO> enrollmentOfficeWTOList = convert(enrollmentOfficeTOList);
		if (enrollmentOfficeWTOList != null
				&& !enrollmentOfficeTOList.isEmpty()) {
			try {
				getService().updateEnrollmentOffice(enrollmentOfficeWTOList);
			} catch (ExternalInterfaceException_Exception e) {
				String errorMessage = e.getFaultInfo().getMessage();
				String errorCode = e.getFaultInfo().getErrorCode();
				if (PORTAL_ERROR_BIW_001.equals(errorCode)) {
					ServiceException serviceException = new ServiceException(
							BizExceptionCode.PBS_002, errorMessage, e,
							EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
					logger.error(BizExceptionCode.GLB_003_MSG,
							serviceException,
							EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
					portalLogger.error(BizExceptionCode.GLB_003_MSG,
							serviceException,
							EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
					throw serviceException;
				}

				ServiceException serviceException = new ServiceException(
						BizExceptionCode.PBS_003, errorMessage, e,
						EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
				logger.error(BizExceptionCode.GLB_003_MSG, serviceException,
						EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
				portalLogger.error(BizExceptionCode.GLB_003_MSG,
						serviceException,
						EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
				throw serviceException;
			}
		}
	}

	/**
	 * The method removeEnrollmentOffices is used to notify the sub system
	 * 'Portal' about removing some of the enrollmentOffices
	 * 
	 * @param enrolmentOfficeIdList
	 *            a list of type {@link Long}
	 * @throws com.gam.commons.core.BaseException
	 */
	@Override
	public void removeEnrollmentOffices(List<Long> enrolmentOfficeIdList)
			throws BaseException {
		if (enrolmentOfficeIdList != null && !enrolmentOfficeIdList.isEmpty()) {
			try {
				getService().removeEnrollmentOffice(enrolmentOfficeIdList);
			} catch (ExternalInterfaceException_Exception e) {
				String errorMessage = e.getFaultInfo().getMessage();
				String errorCode = e.getFaultInfo().getErrorCode();
				if (PORTAL_ERROR_BIW_002.equals(errorCode)) {
					ServiceException serviceException = new ServiceException(
							BizExceptionCode.PBS_004, errorMessage, e,
							EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
					logger.error(BizExceptionCode.GLB_003_MSG,
							serviceException,
							EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
					portalLogger.error(BizExceptionCode.GLB_003_MSG,
							serviceException,
							EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
					throw serviceException;
				}

				ServiceException serviceException = new ServiceException(
						BizExceptionCode.PBS_005, errorMessage, e,
						EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
				logger.error(BizExceptionCode.GLB_003_MSG, serviceException,
						EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
				portalLogger.error(BizExceptionCode.GLB_003_MSG,
						serviceException,
						EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
				throw serviceException;
			}
		}
	}

	/**
	 * The method updateRatingInfo is used to notify the subsystem 'Portal'
	 * about updating the rating info
	 * 
	 * @param ratingInfoTOList
	 *            a list of type
	 *            {@link com.gam.nocr.ems.data.domain.RatingInfoTO} which
	 *            consists of the new or the modified instances
	 * @throws com.gam.commons.core.BaseException
	 */
	@Override
	public void updateRatingInfo(List<RatingInfoTO> ratingInfoTOList)
			throws BaseException {
		List<RatingInfoWTO> ratingInfoWTOList = RatingInfoMapper
				.convert(ratingInfoTOList);
		if (ratingInfoWTOList != null && !ratingInfoWTOList.isEmpty()) {
			try {
				getService().updateRatingInfo(ratingInfoWTOList);
			} catch (ExternalInterfaceException_Exception e) {
				String errorMessage = e.getFaultInfo().getMessage();
				ServiceException serviceException = new ServiceException(
						BizExceptionCode.PBS_007, errorMessage, e,
						EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
				logger.error(BizExceptionCode.GLB_003_MSG, serviceException,
						EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
				portalLogger.error(BizExceptionCode.GLB_003_MSG,
						serviceException,
						EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
				throw serviceException;
			}
		}
	}

	/**
	 * The method updateLocations is used to notify the subsystem 'Portal' about
	 * updating the locations
	 * 
	 * @param locationTOList
	 *            a list of type {@link com.gam.nocr.ems.data.domain.LocationTO}
	 * @throws com.gam.commons.core.BaseException
	 */
	@Override
	public List<Long> updateLocations(List<LocationTO> locationTOList)
			throws BaseException {
		List<LocationWTO> locationWTOList = ProvinceMapper
				.convert(locationTOList);

		List<Long> updatedProvinceIdList = new ArrayList<Long>();
		if (locationWTOList != null && !locationWTOList.isEmpty()) {
			try {
				updatedProvinceIdList = getService().updateLocations(
						locationWTOList);
			} catch (ExternalInterfaceException_Exception e) {
				String errorMessage = e.getFaultInfo().getMessage();
				ServiceException serviceException = new ServiceException(
						BizExceptionCode.PBS_008, errorMessage, e,
						EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
				logger.error(BizExceptionCode.GLB_003_MSG, serviceException,
						EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
				portalLogger.error(BizExceptionCode.GLB_003_MSG,
						serviceException,
						EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
				throw serviceException;
			}
		}

		return updatedProvinceIdList;
	}

	@Override
	public void updateHoliday(List<HolidayTO> holidayTOList)
			throws BaseException {

		List<HolidayWTO> holidayWTOList = HolidayMapper.convert(holidayTOList);
		if (holidayWTOList != null && !holidayWTOList.isEmpty()) {
			try {
				getService().updateHoliday(holidayWTOList);
			} catch (ExternalInterfaceException_Exception e) {
				String errorMessage = e.getFaultInfo().getMessage();
				ServiceException serviceException = new ServiceException(
						BizExceptionCode.PBS_009, errorMessage, e,
						EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
				logger.error(BizExceptionCode.GLB_003_MSG, serviceException,
						EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
				portalLogger.error(BizExceptionCode.GLB_003_MSG,
						serviceException,
						EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
				throw serviceException;
			}
		}
	}
	
	
//	Anbari
	@Override
	public void notifyPortalRezervationFreeTime(List<Long> eofIds,Long date) throws BaseException {
		try {
			getServiceForReservationFreeTime().notifyPortalRezervationFreeTime(eofIds, date);
		} catch (ExternalInterfaceException_Exception e) {
			String errorMessage = e.getFaultInfo().getMessage();
			ServiceException serviceException = new ServiceException(
					BizExceptionCode.PBS_010, errorMessage, e,
					EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
			logger.error(BizExceptionCode.GLB_003_MSG, serviceException,
					EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
			portalLogger.error(BizExceptionCode.GLB_003_MSG,
					serviceException,
					EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
			throw serviceException;
		}
		
	}
	
	//Anbari
	@Override
	public int deleteReservationDateFromOfficeRSVFreeTime(Long dateForDelete)
			throws BaseException {
		try {
			return getServiceForReservationFreeTime().deleteReservationDateFromOfficeRSVFreeTime(dateForDelete);
		} catch (ExternalInterfaceException_Exception e) {
			String errorMessage = e.getFaultInfo().getMessage();
			ServiceException serviceException = new ServiceException(
					BizExceptionCode.PBS_012, errorMessage, e,
					EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
			logger.error(BizExceptionCode.GLB_003_MSG, serviceException,
					EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
			portalLogger.error(BizExceptionCode.GLB_003_MSG,
					serviceException,
					EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
			throw serviceException;
		}
	}


	//Adldoost
	@Override
	public void checkEnrollmentOfficeDeletePossibilityAndPerform(
			long id) throws BaseException {
		
		try {
			getService().checkEnrollmentOfficeDeletePossibilityAndPerform(id);
		} catch (ExternalInterfaceException_Exception e) {
			String errorMessage = e.getFaultInfo().getMessage();
			ServiceException serviceException = new ServiceException(
					BizExceptionCode.PBS_010, errorMessage, e,
					EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
			logger.error(BizExceptionCode.GLB_003_MSG, serviceException,
					EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
			portalLogger.error(BizExceptionCode.GLB_003_MSG,
					serviceException,
					EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
			throw serviceException;
		}
		
	}

	//Anbari
	@Override
	public void syncResevationFreeTimeByNewRating(Long eofId,
			RatingInfoTO ratingInfo,String newCalender) throws BaseException {
		
		try {
			RatingInfoWTO ratingInfoWTO = null;
			if(ratingInfo!=null){
				ratingInfoWTO = new RatingInfoWTO();
			ratingInfoWTO.setId(ratingInfo.getId());
			ratingInfoWTO.setSize(ratingInfo.getSize());
			ratingInfoWTO.setClazz(ratingInfo.getClazz());
			}
			getService().syncResevationFreeTimeByNewRating(eofId,ratingInfoWTO,newCalender);
		} catch (ExternalInterfaceException_Exception e) {
			String errorMessage = e.getFaultInfo().getMessage();
			ServiceException serviceException = new ServiceException(
					BizExceptionCode.PBS_010, errorMessage, e,
					EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
			logger.error(BizExceptionCode.GLB_003_MSG, serviceException,
					EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
			portalLogger.error(BizExceptionCode.GLB_003_MSG,
					serviceException,
					EMSLogicalNames.SRV_PORTAL_BASE_INFO.split(","));
			throw serviceException;
		}
		
	}
	
	
	


}
