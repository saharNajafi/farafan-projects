package com.gam.nocr.ems.biz.service.external.impl;

import static com.gam.nocr.ems.config.EMSLogicalNames.SRV_REGISTRATION;
import static com.gam.nocr.ems.config.EMSLogicalNames.getServiceJNDIName;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.AbstractService;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactory;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.RegistrationService;
import com.gam.nocr.ems.biz.service.external.impl.ims.NOCRIMSBatchService;
import com.gam.nocr.ems.biz.service.external.impl.ims.NOCRIMSFarafanService;
import com.gam.nocr.ems.biz.service.external.impl.ims.NOCRIMSOnlineService;
import com.gam.nocr.ems.biz.service.ims.IMSProxy;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.IMSUpdateDAO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.vol.EmsCardDeliverInfo;
import com.gam.nocr.ems.data.domain.vol.IMSUpdateResultVTO;
import com.gam.nocr.ems.data.domain.vol.PersonEnquiryVTO;
import com.gam.nocr.ems.data.domain.vol.TransferInfoVTO;
import com.gam.nocr.ems.util.EmsUtil;

import org.slf4j.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "IMSService")
@Local(IMSServiceLocal.class)
@Remote(IMSServiceRemote.class)
public class IMSServiceImpl extends AbstractService implements IMSServiceLocal, IMSServiceRemote {

	private static final Logger logger = BaseLog.getLogger(IMSServiceImpl.class);
	private static final Logger imsLogger = BaseLog.getLogger("ImsLogger");

	ProfileManager pm = null;

	/**
	 * IMS ErrorMessages
	 */
	// Online enquiry service exceptions
	private static final String IMS_ONLINE_ENQUIRY_NO_RECORD_FOUND_EXCEPTION = "err.record.not.found";
	private static final String IMS_ONLINE_ENQUIRY_REVIEW_RECORD_EXCEPTION = "result.rec.review";

	/**
	 * ===============
	 * Getter for DAOs
	 * ===============
	 */

	/**
	 * getIMSUpdateDAO
	 */
	private IMSUpdateDAO getIMSUpdateDAO() throws BaseException {
		DAOFactory daoFactory = DAOFactoryProvider.getDAOFactory();
		IMSUpdateDAO cardRequestDAO = null;
		try {
			cardRequestDAO = daoFactory.getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_IMS_UPDATE));
		} catch (DAOFactoryException e) {
			throw new ServiceException(
					BizExceptionCode.IMS_001,
					BizExceptionCode.GLB_001_MSG,
					e,
					EMSLogicalNames.DAO_IMS_UPDATE.split(","));
		}
		return cardRequestDAO;
	}
	
	
	//Anbari:Estalm3
//	private NOCRIMSOnlineService getNOCRIMSOnlineService()
//			throws BaseException {
//		NOCRIMSOnlineService nocrIMSOnlineService;
//		try {
//			nocrIMSOnlineService = ServiceFactoryProvider
//					.getServiceFactory()
//					.getService(
//							EMSLogicalNames
//									.getServiceJNDIName(EMSLogicalNames.SRV_NIO), EmsUtil.getUserInfo(userProfileTO));
//		} catch (ServiceFactoryException e) {
//			throw new DelegatorException(BizExceptionCode.CRD_001,
//					BizExceptionCode.GLB_002_MSG, e,
//					EMSLogicalNames.SRV_CARD_REQUEST.split(","));
//		}
//		nocrIMSOnlineService.setUserProfileTO(userProfileTO);
//		return nocrIMSOnlineService;
//	}
	
	//Anbari:Estalm3
	private NOCRIMSOnlineService getIMSOnlineService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider.getServiceFactory();
        NOCRIMSOnlineService nocrImsOnlineService;
        try {
            nocrImsOnlineService = serviceFactory.getService(EMSLogicalNames.getExternalIMSServiceJNDIName(EMSLogicalNames.SRV_NIO), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.NIF_016,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_NIO.split(","));
        }
        return nocrImsOnlineService;
    }
	
	private NOCRIMSBatchService getNOCRIMSBatchService()
			throws BaseException {
		NOCRIMSBatchService nocrIMSBatchService;
		try {
			nocrIMSBatchService = ServiceFactoryProvider
					.getServiceFactory()
					.getService(
							EMSLogicalNames
									.getServiceJNDIName(EMSLogicalNames.SRV_IMS_BATCH), EmsUtil.getUserInfo(userProfileTO));
		} catch (ServiceFactoryException e) {
			throw new DelegatorException(BizExceptionCode.CRD_001,
					BizExceptionCode.GLB_002_MSG, e,
					EMSLogicalNames.SRV_CARD_REQUEST.split(","));
		}
		nocrIMSBatchService.setUserProfileTO(userProfileTO);
		return nocrIMSBatchService;
	}	
	
	//Anbari:IMS
		private NOCRIMSFarafanService getNOCRIMSFarafanService()
				throws BaseException {
			NOCRIMSFarafanService nocrIMSFarafanService;
			try {
				nocrIMSFarafanService = ServiceFactoryProvider
						.getServiceFactory()
						.getService(
								EMSLogicalNames
										.getExternalIMSServiceJNDIName(EMSLogicalNames.SRV_IMS_FARAFAN),EmsUtil.getUserInfo(userProfileTO));
			} catch (ServiceFactoryException e) {
				throw new DelegatorException(BizExceptionCode.CRD_001,
						BizExceptionCode.GLB_002_MSG, e,
						EMSLogicalNames.SRV_CARD_REQUEST.split(","));
			}
			nocrIMSFarafanService.setUserProfileTO(userProfileTO);
			return nocrIMSFarafanService;
		}
	

	/**
	 * The method sendBatchEnquiryRequestForFirstTime is used to send the batch enquiring request.
	 *
	 * @param transferInfoVTO  is carries the citizens' information
	 * @param imsProxyProvider is an instance of type {@link IMSProxyProvider}, which is used to represent the current
	 *                         IMSProxyProvider,
	 *                         as regards of the configurations that set in profileManager
	 * @return an instance of type {@link String}
	 */
	@Override
	public String sendBatchEnquiryRequest(TransferInfoVTO transferInfoVTO) throws BaseException {
		return getNOCRIMSBatchService().sendBatchEnquiryRequest(transferInfoVTO);
	}

	/**
	 * The method getBatchEnquiryResponse is used to receive the response of the batch enquiring request from IMS sub
	 * system.
	 *
	 * @param imsProxyProvider
	 * @return an object of type {@link TransferInfoVTO} which carries the citizen's information
	 */
	@Override
	public TransferInfoVTO getBatchEnquiryResponse() throws BaseException {
		return getNOCRIMSBatchService().getBatchEnquiryResponse();
	}

	/**
	 * The method updateCitizensInfo is used to send the request for updating the citizen information.
	 *
	 * @param cardRequestTOList a list of type {CardRequestTO}
	 * @param requestId         is an instance of type {@link String} which represents the requestId of the update
	 *                          request
	 * @param imsProxyProvider  is an instance of type {@link IMSProxyProvider}, which is used to represent the current
	 *                          IMSProxyProvider,
	 *                          as regards of the configurations that set in profileManager
	 */
	@Override
	public void updateCitizensInfo(List<CardRequestTO> cardRequestTOList,
								   String requestId) throws BaseException {
		getNOCRIMSFarafanService().updateCitizensInfo(cardRequestTOList, requestId);
	}

	/**
	 * The method getUpdatedCitizensResult is used to receive the response of updating the citizen information request
	 * from IMS sub system.
	 *
	 * @param imsProxyProvider is an instance of type {@link IMSProxyProvider}, which is used to represent the current
	 *                         IMSProxyProvider,
	 *                         as regards of the configurations that set in profileManager
	 * @param imsRequestId
	 * @return a list of type {@link IMSUpdateResultVTO}
	 */
	@Override
	public List<IMSUpdateResultVTO> getUpdatedCitizensResult(String imsRequestId) throws BaseException {

		return getNOCRIMSFarafanService().getUpdatedCitizensResult(imsRequestId);
	}

	/**
	 * The method setCitizenCardDelivered is used to notify the IMS about this issue which the card was delivered to
	 * citizen.
	 *
	 * @param nationalId       represents the nationalId of a specified citizen
	 * @param imsProxyProvider is an instance of type {@link IMSProxyProvider}, which is used to represent the current
	 *                         IMSProxyProvider,
	 *                         as regards of the configurations that set in profileManager
	 * @param nationalId       represents the nationalId of a specified citizen
	 * @return true or false (to show whether the alerting operation has done successfully or not)
	 */
	@Override
	public boolean setCitizenCardDelivered(EmsCardDeliverInfo cardDeliverInfo) throws BaseException {

		return getNOCRIMSFarafanService().setCitizenCardDelivered(cardDeliverInfo);
	}

	/**
	 * The method fetchCitizenInfo if used to receive all the information of a specified citizen from the sub system
	 * 'IMS'.
	 *
	 * @param nationalId       is an string value which represents the nationalId of a specified citizen
	 * @param imsProxyProvider is an instance of type {@link IMSProxyProvider}, which is used to represent the current
	 *                         IMSProxyProvider,
	 *                         as regards of the configurations that set in profileManager
	 * @return an instance of type {@link com.gam.nocr.ems.data.domain.CitizenTO}
	 * @throws com.gam.commons.core.BaseException
	 *
	 */
	@Override
	public CitizenTO fetchCitizenInfo(String nationalId) throws BaseException {
		return getNOCRIMSFarafanService().fetchCitizenInfo(nationalId);
	}


	/**
	 * The method getOnlineEnquiry is used to get the online enquiry from the sub system 'IMS'
	 *
	 * @param personEnquiryVTOs is an array of type {@link com.gam.nocr.ems.data.domain.vol.PersonEnquiryVTO} which
	 *                          carries the necessary attributes to enquiry
	 * @param imsProxyProvider  a variable of type boolean (true for doing enquiry and false for not doing enquiry)
	 * @return a hashmap of {@link java.util.HashMap <String, Boolean>} which carries nationalId and the result of
	 *         enquiry(true or false)
	 */
	@Override
	public HashMap<String, Boolean> getOnlineEnquiry(PersonEnquiryVTO[] personEnquiryVTOs) throws BaseException {
		return getIMSOnlineService().getOnlineEnquiry(personEnquiryVTOs);
	}

	/**
	 * The method getOnlineEnquiry is used to fetch the citizen info from the IMS sub system
	 *
	 * @param personEnquiryVTOs is an array of type {@link com.gam.nocr.ems.data.domain.vol.PersonEnquiryVTO} which
	 *                          carries the necessary attributes to enquiry
	 * @param imsProxyProvider  is an instance of type {@link IMSProxyProvider}, which is used to represent the current
	 *                          IMSProxyProvider, as regards of the configurations that set in profileManager
	 * @return a hashmap of {@link java.util.HashMap <String, PersonEnquiryVTO>} which carries nationalId and an
	 *         instance
	 *         of type {@link PersonEnquiryVTO}, which was valued By IMS database
	 */
	@Override
	public HashMap<String, PersonEnquiryVTO> fetchDataByOnlineEnquiry(PersonEnquiryVTO[] personEnquiryVTOs) throws BaseException {
		return getIMSOnlineService().fetchDataByOnlineEnquiry(personEnquiryVTOs);

	}

//	@Override
//	public HashMap<String, PersonEnquiryVTO> fetchDataByOnlineEnquiryByEstelam2(
//			PersonEnquiryVTO[] personEnquiryVTOs,String citizenNID,
//			IMSProxyProvider imsProxyProvider) throws BaseException {
//		return getIMSProxy(imsProxyProvider).fetchDataByOnlineEnquiryByEstelam2(personEnquiryVTOs,citizenNID);
//	}
	
	//Anbari:Estelam3
	@Override
	public PersonEnquiryVTO fetchDataByOnlineEnquiryByEstelam3(
			PersonEnquiryVTO personEnquiryVTO,String nid) throws BaseException {
		return getIMSOnlineService().fetchDataByOnlineEnquiryByEstelam3(personEnquiryVTO,nid);
	}

	//Anbari:IMS
	@Override
	public IMSUpdateResultVTO getUpdatedCitizensResultNew(String nationalId)
			throws BaseException {

		return getNOCRIMSFarafanService().getUpdatedCitizensResultNew(nationalId);

	}
	
	
	
	
	
	
}
