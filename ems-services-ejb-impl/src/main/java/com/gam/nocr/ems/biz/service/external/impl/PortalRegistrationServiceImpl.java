package com.gam.nocr.ems.biz.service.external.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import org.slf4j.Logger;

import servicePortUtil.ServicePorts;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.AbstractService;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.external.client.portal.BasicInfoWS;
import com.gam.nocr.ems.biz.service.external.client.portal.BasicInfoWS_Service;
import com.gam.nocr.ems.biz.service.external.client.portal.CardRequestWTO;
import com.gam.nocr.ems.biz.service.external.client.portal.CitizenVTO;
import com.gam.nocr.ems.biz.service.external.client.portal.CitizenWTO;
import com.gam.nocr.ems.biz.service.external.client.portal.ExternalInterfaceException_Exception;
import com.gam.nocr.ems.biz.service.external.client.portal.ItemWTO;
import com.gam.nocr.ems.biz.service.external.client.portal.RegistrationWS;
import com.gam.nocr.ems.biz.service.external.client.portal.RegistrationWS_Service;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.ws.SyncCardRequestWTO;
import com.gam.nocr.ems.data.mapper.tomapper.CardRequestMapper;
import com.gam.nocr.ems.util.EmsUtil;
import com.sun.xml.ws.client.BindingProviderProperties;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "PortalRegistrationService")
@Local(PortalRegistrationServiceLocal.class)
@Remote(PortalRegistrationServiceRemote.class)
public class PortalRegistrationServiceImpl extends AbstractService implements PortalRegistrationServiceLocal, PortalRegistrationServiceRemote {

    /**
     * Portal Exception Codes
     */
    private static final String PORTAL_ERROR_REW_003 = "EMS_W_REW_003";
    private static final String PORTAL_ERROR_REW_004 = "EMS_W_REW_004";

    private static final String PORTAL_ERROR_REW_005 = "EMS_W_REW_005";
//	private static final String PORTAL_ERROR_PRT_S_RSI_141 = "PRT_S_RSI_141";
//	private static final String PORTAL_ERROR_PRT_S_RSI_142 = "PRT_S_RSI_142";
//	private static final String PORTAL_ERROR_PRT_S_RSI_143 = "PRT_S_RSI_143";
//	private static final String PORTAL_ERROR_PRT_S_RSI_144 = "PRT_S_RSI_144";
//	private static final String PORTAL_ERROR_PRT_S_RSI_145 = "PRT_S_RSI_145";
//	private static final String PORTAL_ERROR_PRT_S_RSI_146 = "PRT_S_RSI_146";
//	private static final String PORTAL_ERROR_PRT_S_RSI_147 = "PRT_S_RSI_147";
//	private static final String PORTAL_ERROR_PRT_S_RSI_148 = "PRT_S_RSI_148";
//	private static final String PORTAL_ERROR_PRT_S_RSI_149 = "PRT_S_RSI_149";
//	private static final String PORTAL_ERROR_PRT_S_RSI_150 = "PRT_S_RSI_150";
//	private static final String PORTAL_ERROR_PRT_S_RSI_151 = "PRT_S_RSI_151";
//	private static final String PORTAL_ERROR_PRT_S_RSI_152 = "PRT_S_RSI_152";
//	private static final String PORTAL_ERROR_PRT_S_RSI_153 = "PRT_S_RSI_153";
//	private static final String PORTAL_ERROR_PRT_S_RSI_154 = "PRT_S_RSI_154";
//	private static final String PORTAL_ERROR_PRT_S_RSI_155 = "PRT_S_RSI_155";
//	private static final String PORTAL_ERROR_PRT_S_RSI_156 = "PRT_S_RSI_156";
//	private static final String PORTAL_ERROR_PRT_S_RSI_157 = "PRT_S_RSI_157";
//	private static final String PORTAL_ERROR_PRT_S_RSI_158 = "PRT_S_RSI_158";
//	private static final String PORTAL_ERROR_PRT_S_RSI_159 = "PRT_S_RSI_159";
//	private static final String PORTAL_ERROR_PRT_S_RSI_160 = "PRT_S_RSI_160";
//	private static final String PORTAL_ERROR_PRT_S_RSI_161 = "PRT_S_RSI_161";
//	private static final String PORTAL_ERROR_PRT_S_RSI_162 = "PRT_S_RSI_162";
//	private static final String PORTAL_ERROR_PRT_S_RSI_163 = "PRT_S_RSI_163";
//	private static final String PORTAL_ERROR_PRT_S_RSI_164 = "PRT_S_RSI_164";
//	private static final String PORTAL_ERROR_PRT_S_RSI_165 = "PRT_S_RSI_165";
//	private static final String PORTAL_ERROR_PRT_S_RSI_166 = "PRT_S_RSI_166";
//	private static final String PORTAL_ERROR_PRT_S_RSI_167 = "PRT_S_RSI_167";
//	private static final String PORTAL_ERROR_PRT_S_RSI_168 = "PRT_S_RSI_168";
//	private static final String PORTAL_ERROR_PRT_S_RSI_169 = "PRT_S_RSI_169";
//	private static final String PORTAL_ERROR_PRT_S_RSI_170 = "PRT_S_RSI_170";
//	private static final String PORTAL_ERROR_PRT_S_RSI_171 = "PRT_S_RSI_171";
//	private static final String PORTAL_ERROR_PRT_S_RSI_172 = "PRT_S_RSI_172";
//	private static final String PORTAL_ERROR_PRT_S_RSI_173 = "PRT_S_RSI_173";
//	private static final String PORTAL_ERROR_PRT_S_RSI_174 = "PRT_S_RSI_174";
//	private static final String PORTAL_ERROR_PRT_S_RSI_175 = "PRT_S_RSI_175";
//	private static final String PORTAL_ERROR_PRT_S_RSI_176 = "PRT_S_RSI_176";
//	private static final String PORTAL_ERROR_PRT_S_RSI_177 = "PRT_S_RSI_177";
//	private static final String PORTAL_ERROR_PRT_S_RSI_178 = "PRT_S_RSI_178";
//	private static final String PORTAL_ERROR_PRT_S_RSI_179 = "PRT_S_RSI_179";
//	private static final String PORTAL_ERROR_PRT_S_RSI_180 = "PRT_S_RSI_180";

    private static final String DEFAULT_WSDL_URL = "http://localhost:7001/portal-web/services/RegistrationWS?wsdl";
    private static final String DEFAULT_NAMESPACE = "http://portalws.ws.web.portal.nocr.gam.com/";

    private static final Logger logger = BaseLog.getLogger(CMSServiceImpl.class);
    private static final Logger portalLogger = BaseLog.getLogger("PortalLogger");
    private static final Logger jobLogger = BaseLog.getLogger("portalFetchCardRequest");
    private static final Logger threadLocalLogger = BaseLog.getLogger("threadLocal");

    RegistrationWS service = null;

    /**
     * The method getService is used to get WebServices from Portal sub system
     *
     * @return an instance of type {@link RegistrationWS}
     * @throws com.gam.commons.core.BaseException
     *          if cannot get the service
     */
    private RegistrationWS getService() throws BaseException {
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();

            String wsdlUrl = (String) pm.getProfile(ProfileKeyName.KEY_PORTAL_REGISTRATION_ENDPOINT, true, null, null);
            String namespace = (String) pm.getProfile(ProfileKeyName.KEY_PORTAL_NAMESPACE, true, null, null);
            if (wsdlUrl == null)
                wsdlUrl = DEFAULT_WSDL_URL;
            if (namespace == null)
                namespace = DEFAULT_NAMESPACE;
            String serviceName = "RegistrationWS";
            logger.debug("Portal Registration wsdl url: " + wsdlUrl);
            portalLogger.debug("Portal Registration wsdl url: " + wsdlUrl);
            //Commented for ThraedLocal
           // RegistrationWS port = new RegistrationWS_Service(new URL(wsdlUrl), new QName(namespace, serviceName)).getRegistrationWSPort();
            RegistrationWS port = ServicePorts.getPortalRegistrationPort();
			if (port == null) {
				threadLocalLogger.debug("**************************** new PortalRegistartion in Portal getService()");
				port = new RegistrationWS_Service(new URL(wsdlUrl), new QName(namespace, serviceName)).getRegistrationWSPort();
				ServicePorts.setPortalRegistrationPort(port);
			} else {
				threadLocalLogger.debug("***************************** using PortalRegistartion from ThradLocal");
			}
            EmsUtil.setJAXWSWebserviceProperties(port, wsdlUrl);
            return port;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PRS_001, e.getMessage(), e);
        }
    }
    
    
    /**
     * Default sync web service timeout configurations in millisecond
     */
    private static final String DEFAULT_SYNC_WEBSERVICE_TIMEOUT = "300000";
    
    private RegistrationWS getServiceForSyncRequests() throws BaseException {
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();

            String wsdlUrl = (String) pm.getProfile(ProfileKeyName.KEY_PORTAL_REGISTRATION_ENDPOINT, true, null, null);
            String namespace = (String) pm.getProfile(ProfileKeyName.KEY_PORTAL_NAMESPACE, true, null, null);
            if (wsdlUrl == null)
                wsdlUrl = DEFAULT_WSDL_URL;
            if (namespace == null)
                namespace = DEFAULT_NAMESPACE;
            String serviceName = "RegistrationWS";
            logger.debug("Portal Registration wsdl url: " + wsdlUrl);
            portalLogger.debug("Portal Registration wsdl url: " + wsdlUrl);
            RegistrationWS port = new RegistrationWS_Service(new URL(wsdlUrl), new QName(namespace, serviceName)).getRegistrationWSPort();
            try {
                String endPoint = wsdlUrl.split("\\?")[0];
                Integer webserviceTimeout = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_SYNC_WEBSERVICE_TIMEOUT, DEFAULT_SYNC_WEBSERVICE_TIMEOUT));
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
            throw new ServiceException(BizExceptionCode.PRS_001, e.getMessage(), e);
        }
    }

    /**
     * The method convertToPortalItemWTOList is used to convert a list of type {@link SyncCardRequestWTO} to a list of type {@link
     * com.gam.nocr.ems.biz.service.external.client.portal.SyncCardRequestWTO}
     *
     * @param syncCardRequestWTOList {@link SyncCardRequestWTO}
     * @return a list of type {@link com.gam.nocr.ems.biz.service.external.client.portal.SyncCardRequestWTO}
     */
    private List<com.gam.nocr.ems.biz.service.external.client.portal.SyncCardRequestWTO> convertToPortalSyncCardRequestWTOList(
            List<SyncCardRequestWTO> syncCardRequestWTOList) {
        List<com.gam.nocr.ems.biz.service.external.client.portal.SyncCardRequestWTO> syncCardRequestWTOs =
                new ArrayList<com.gam.nocr.ems.biz.service.external.client.portal.SyncCardRequestWTO>();

        for (SyncCardRequestWTO syncCardRequestWTO : syncCardRequestWTOList) {
            com.gam.nocr.ems.biz.service.external.client.portal.SyncCardRequestWTO cardRequestWTO =
                    new com.gam.nocr.ems.biz.service.external.client.portal.SyncCardRequestWTO();
            cardRequestWTO.setId(syncCardRequestWTO.getId());
            cardRequestWTO.setCardRequestState(syncCardRequestWTO.getCardRequestState());
            cardRequestWTO.setCardEnrollmentOfficeId(syncCardRequestWTO.getCardEnrollmentOfficeId());
            cardRequestWTO.setCardRequestMetadata(syncCardRequestWTO.getCardRequestMetadata());
            cardRequestWTO.setOriginalCardRequestOfficeId(syncCardRequestWTO.getOriginalCardRequestOfficeId());
            syncCardRequestWTOs.add(cardRequestWTO);
        }

        return syncCardRequestWTOs;
    }

    /**
     * The method convertToCardRequestToList is used to convert a list of type {@link CitizenVTO} to a list of {@link
     * CardRequestTO}
     *
     * @param portalCitizenVTOList a list of type {@link CitizenVTO}
     * @return a list of type {@link CardRequestTO}
     */
    private List<CardRequestTO> convertToCardRequestToList(List<CitizenVTO> portalCitizenVTOList) throws BaseException {
        List<CardRequestTO> cardRequestTOList = new ArrayList<CardRequestTO>();
        for (CitizenVTO portalCitizenVTO : portalCitizenVTOList) {
            CardRequestTO cardRequestTO = CardRequestMapper.convert(portalCitizenVTO);
            if (cardRequestTO != null) {
                cardRequestTOList.add(cardRequestTO);
            }
        }
        return cardRequestTOList;
    }

    /**
     * The method transferCardRequests is used to get a number of card request from the sub system 'Portal'
     *
     * @return a list of type {@link com.gam.nocr.ems.data.domain.CardRequestTO}
     * @throws com.gam.commons.core.BaseException
     *
     */
    @Override
    public List<CardRequestTO> transferCardRequests(List<Long> portalCardRequestIds) throws BaseException {
        List<CardRequestTO> cardRequestTOList = new ArrayList<CardRequestTO>();

        try {
            jobLogger.info("Fetching detailed data for portal card requests : {}", portalCardRequestIds);

            List<CitizenVTO> portalCardRequestTOList = getService().transferCardRequests(portalCardRequestIds);
            if (portalCardRequestTOList != null && !portalCardRequestTOList.isEmpty()) {
                jobLogger.info("List of portal card requests fetched successfully. trying to convert them to an appropriate format to be used in EMS");
                cardRequestTOList = convertToCardRequestToList(portalCardRequestTOList);
            } else {
                jobLogger.warn("List of portal card requests fetched from portal is null or empty");
            }
        } catch (ExternalInterfaceException_Exception e) {
//			TODO : Exception handling
            String errorMessage = e.getFaultInfo().getMessage();

            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.PRS_005,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
            portalLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
            jobLogger.error("Failed to load details of portal card requests : "  + portalCardRequestIds, serviceException);
            throw serviceException;
        }
        return cardRequestTOList;
    }

    @Override
    public List<Long> fetchPortalCardRequestIdsForTransfer() throws BaseException {
        try {
            jobLogger.info("Retrieving list of card requests identifiers from portal to fetch their data in next steps");
            portalLogger.debug("Calling fetchPortalCardRequestIdsForTransfer from portal");
            List<Long> result = getService().fetchPortalCardRequestIdsForTransfer();
            jobLogger.info("List of portal request identifiers fetched from portal : {}", result);
            portalLogger.debug("Results of calling fetchPortalCardRequestIdsForTransfer from portal is :" + result);
            return result;
        } catch (ExternalInterfaceException_Exception e) {
//			TODO : Exception handling
            String errorMessage = e.getFaultInfo().getMessage();

            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.PRS_011,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));

            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
            portalLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
            jobLogger.error("Unable to fetch the list of portal request identifiers in order to retrieve their data from portal", serviceException);
            throw serviceException;
        }
    }

    /**
     * The method updateCardRequestStates is used to alert the sub system 'Portal' about the current state of the
     * request.
     *
     * @param syncCardRequestWTOList a list of type {@link ItemWTO} which carries a number of portalRequestId with the
     *                               specified state
     *                               for each id
     * @return an object of type {@Boolean} which represents whether the batch update is done correctly or not
     * @throws com.gam.commons.core.BaseException
     *
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<ItemWTO> updateCardRequestsState(List<SyncCardRequestWTO> syncCardRequestWTOList) throws BaseException {
        List<com.gam.nocr.ems.biz.service.external.client.portal.SyncCardRequestWTO> syncCardRequestWTOs =
                convertToPortalSyncCardRequestWTOList(syncCardRequestWTOList);

        try {
            portalLogger.debug("Calling updateCardRequestState of Portal for : " + syncCardRequestWTOList);
            return getServiceForSyncRequests().updateCardRequestState(syncCardRequestWTOs);
        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();

            if (PORTAL_ERROR_REW_003.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.PRS_002,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
                portalLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
                throw serviceException;

            } else if (PORTAL_ERROR_REW_004.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.PRS_003,
                        errorMessage,
                        e,
                        EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
                portalLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
                throw serviceException;
            }

            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.PRS_004,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
            portalLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
            throw serviceException;
        }
    }

    @Override
    public Long updateCcosCardRequests(CardRequestWTO cardRequestWTO) throws BaseException {
        if (cardRequestWTO == null)
            throw new ServiceException(BizExceptionCode.PRS_006, BizExceptionCode.PRS_006_MSG);

        try {
            return getService().updateCcosCardRequest(cardRequestWTO);
        } catch (ExternalInterfaceException_Exception e) {
            //			TODO : Exception handling
            String errorMessage = e.getFaultInfo().getMessage();

            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.PRS_007,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
            portalLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
            throw serviceException;
        }
    }

    /**
     * The method updateNotVerifiedMESRequest is used to update the request, which has not been verified by
     * IMS and have origins of type 'MES'.
     *
     * @param citizenWTO is an instance of type {@link CitizenWTO}, which carries the necessary information to update
     *                   the portal data base
     * @return an instance of type {@link Long}
     * @throws com.gam.commons.core.BaseException
     *
     */
    @Override
    public Long updateNotVerifiedMESRequest(CitizenWTO citizenWTO) throws BaseException {
        if (citizenWTO == null) {
            throw new ServiceException(BizExceptionCode.PRS_008, BizExceptionCode.PRS_008_MSG);
        }

        try {
            return getService().updateNotVerifiedMESRequest(citizenWTO);
        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getErrorCode();

            if (PORTAL_ERROR_REW_005.equals(errorCode)) {
                throw new ServiceException(BizExceptionCode.PRS_009, errorMessage, e, EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(","));
            }

            throw new ServiceException(
                    BizExceptionCode.PRS_010,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_PORTAL_REGISTRATION.split(",")
            );
        }
    }
}
