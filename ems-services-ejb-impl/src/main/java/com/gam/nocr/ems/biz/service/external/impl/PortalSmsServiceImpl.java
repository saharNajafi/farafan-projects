package com.gam.nocr.ems.biz.service.external.impl;

import gampooya.tools.vlp.ListException;
import gampooya.tools.vlp.ValueListHandler;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import org.displaytag.exception.ListHandlerException;
import org.slf4j.Logger;

import servicePortUtil.ServicePorts;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.external.client.nocrSms.SmsDelegate;
import com.gam.nocr.ems.biz.service.external.client.nocrSms.SmsService;
import com.gam.nocr.ems.biz.service.external.client.portal.ExternalInterfaceException_Exception;
import com.gam.nocr.ems.biz.service.external.client.portal.SmsWS;
import com.gam.nocr.ems.biz.service.external.client.portal.SmsWS_Service;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.EMSValueListProvider;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.CardRequestDAO;
import com.gam.nocr.ems.data.dao.SmsDAO;
import com.gam.nocr.ems.data.domain.OutgoingSMSTO;
import com.gam.nocr.ems.data.enums.SmsMessages;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Stateless(name = "PortalSmsService")
@Local(PortalSmsServiceLocal.class)
@Remote(PortalSmsServiceRemote.class)
public class PortalSmsServiceImpl extends EMSAbstractService implements PortalSmsServiceLocal, PortalSmsServiceRemote {

    private static final Logger logger = BaseLog.getLogger(PortalSmsServiceImpl.class);

    private static final Logger LOGGER = BaseLog.getLogger(PortalSmsServiceImpl.class);
    private static final Logger portalLogger = BaseLog.getLogger("PortalLogger");
    private static final Logger threadLocalLogger = BaseLog.getLogger("threadLocal");

    private static final String DEFAULT_PORTAL_SMS_ENDPOINT = "http://localhost:7001/services/SmsWS?wsdl";
    private static final String DEFAULT_NOCR_SMS_ENDPOINT = "http://sms.sabteahval.ir:8001/SmsProject/SmsPort?wsdl";
    private static final String DEFAULT_PORTAL_NAMESPACE = "http://portalws.ws.web.portal.nocr.gam.com/";
    private static final String DEFAULT_NOCR_SMS_NAMESPACE = "http://ws.sms.com/";
    private static final String DEFAULT_NOCR_SMS_SERVICE_USERNAME = "SMARTCARD";
    private static final String DEFAULT_NOCR_SMS_SERVICE_PASSWORD = "sm#asrcb*92";

    private static final String DEFAULT_SMS_RETRY_DURATION = "10";
    private static final String DEFAULT_SMS_MAX_RETRY_COUNT = "0";

    private SmsWS getPortalSmsService() throws BaseException {
        try {
            String wsdlUrl = EmsUtil.getProfileValue(ProfileKeyName.KEY_PORTAL_SMS_ENDPOINT, DEFAULT_PORTAL_SMS_ENDPOINT);
            String namespace = EmsUtil.getProfileValue(ProfileKeyName.KEY_PORTAL_NAMESPACE, DEFAULT_PORTAL_NAMESPACE);
            String serviceName = "SmsWS";
            SmsWS port = new SmsWS_Service(new URL(wsdlUrl), new QName(namespace, serviceName)).getSmsWSPort();
//            SmsWS port = new SmsWS_Service().getSmsWSPort();
            EmsUtil.setJAXWSWebserviceProperties(port, wsdlUrl);
            return port;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSS_001, e.getMessage(), e);
        }
    }

    private SmsDelegate getNocrSmsService() throws BaseException {
        try {
            String wsdlUrl = EmsUtil.getProfileValue(ProfileKeyName.KEY_NOCR_SMS_ENDPOINT, DEFAULT_NOCR_SMS_ENDPOINT);
            String namespace = EmsUtil.getProfileValue(ProfileKeyName.KEY_NOCR_SMS_NAMESPACE, DEFAULT_NOCR_SMS_NAMESPACE);
            String serviceName = "SmsService";
            //Commented  for ThreadLocal
            //SmsDelegate smsDelegate = new SmsService(new URL(wsdlUrl), new QName(namespace, serviceName)).getSmsPort();
            SmsDelegate smsDelegate = ServicePorts.getSMSPort();
			if (smsDelegate == null) {
				threadLocalLogger.debug("**************************** new SmsDelegate in SMS getEMKSService()");
				smsDelegate = new SmsService(new URL(wsdlUrl), new QName(namespace, serviceName)).getSmsPort();
				ServicePorts.setSMSPort(smsDelegate);
			} else {
				threadLocalLogger.debug("***************************** using SmsDelegate from ThradLocal");
			}
            setNocrSmsUserCredential(smsDelegate);
            EmsUtil.setJAXWSWebserviceProperties(smsDelegate, wsdlUrl);
            return smsDelegate;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSS_003, BizExceptionCode.PSS_003_MSG, e);
        }
    }

    private CardRequestDAO getCardRequestDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.PSS_004,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_CARD_REQUEST.split(","));
        }
    }

    private SmsDAO getSmsDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_SMS));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.PSS_009,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_SMS.split(","));
        }
    }

    @Override
    public List<Long> retrieveRequestedSms() throws BaseException {
        try {
            return getPortalSmsService().retrieveRequestedSms();
        } catch (ExternalInterfaceException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();

            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.PSS_002,
                    errorMessage,
                    e,
                    EMSLogicalNames.SRV_PORTAL_SMS.split(","));
            LOGGER.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_SMS.split(","));
            portalLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_PORTAL_SMS.split(","));
            throw serviceException;
        }
    }

    @Override
    public void addRequestedSms(Long portalCardRequestId) throws BaseException {
        getCardRequestDAO().addRequestedSms(portalCardRequestId);
    }

    @Override
    public Integer fetchReadyToProcessSms() throws BaseException {
        try {
            String retryDuration = EmsUtil.getProfileValue(ProfileKeyName.KEY_SMS_RETRY_DURATION, DEFAULT_SMS_RETRY_DURATION);

            String maxRetryCount = EmsUtil.getProfileValue(ProfileKeyName.KEY_SMS_MAX_RETRY_COUNT, DEFAULT_SMS_MAX_RETRY_COUNT);

            HashMap parameters = new HashMap();
            parameters.put("retryDuration", Integer.valueOf(retryDuration));
            parameters.put("maxRetryCount", Integer.valueOf(maxRetryCount));

            ValueListHandler vlh = EMSValueListProvider.getProvider()
                    .loadList("availableSmsList", "main".split(","), "count".split(","), parameters, null, null);

            return vlh.executeCountQuery();
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.PSS_005, BizExceptionCode.PSS_005_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.PSS_006, BizExceptionCode.PSS_006_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSS_007, BizExceptionCode.GLB_008_MSG, e);
        }
    }
    
    //Hossein
    @Override
    public Integer fetchReferToCCOSProcessSms() throws BaseException {
        try {
            String retryDuration = EmsUtil.getProfileValue(ProfileKeyName.KEY_SMS_RETRY_DURATION, DEFAULT_SMS_RETRY_DURATION);

            String maxRetryCount = EmsUtil.getProfileValue(ProfileKeyName.KEY_SMS_MAX_RETRY_COUNT, DEFAULT_SMS_MAX_RETRY_COUNT);

            HashMap parameters = new HashMap();
            parameters.put("retryDuration", Integer.valueOf(retryDuration));
            parameters.put("maxRetryCount", Integer.valueOf(maxRetryCount));

            ValueListHandler vlh = EMSValueListProvider.getProvider()
                    .loadList("availableReferToCCOSSmsList", "main".split(","), "count".split(","), parameters, null, null);

            return vlh.executeCountQuery();
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.PSS_005, BizExceptionCode.PSS_005_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.PSS_006, BizExceptionCode.PSS_006_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSS_007, BizExceptionCode.GLB_008_MSG, e);
        }
    }

    @Override
    public Boolean processSms(Integer from, Integer to) throws BaseException {
        List<OutgoingSMSTO> outgoingSMSTOs;
        try {
            String retryDuration = EmsUtil.getProfileValue(ProfileKeyName.KEY_SMS_RETRY_DURATION, DEFAULT_SMS_RETRY_DURATION);

            String maxRetryCount = EmsUtil.getProfileValue(ProfileKeyName.KEY_SMS_MAX_RETRY_COUNT, DEFAULT_SMS_MAX_RETRY_COUNT);

            HashMap parameters = new HashMap();
            parameters.put("retryDuration", Integer.valueOf(retryDuration));
            parameters.put("maxRetryCount", Integer.valueOf(maxRetryCount));

            ValueListHandler vlh = EMSValueListProvider.getProvider()
                    .loadList("availableSmsList", "main".split(","), "count".split(","), parameters, null, null);

            outgoingSMSTOs = vlh.getList(from, to, true);
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.PSS_005, BizExceptionCode.PSS_005_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.PSS_006, BizExceptionCode.PSS_006_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSS_007, BizExceptionCode.GLB_008_MSG, e);
        }

        if (EmsUtil.checkListSize(outgoingSMSTOs)) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            java.util.Date utilDate = cal.getTime();
            Timestamp currentTime = new Timestamp(utilDate.getTime());

            SmsDAO smsDAO = getSmsDAO();
            OutgoingSMSTO outgoingSMSTO = outgoingSMSTOs.get(0);
            String result = null;

            try {
                result = getNocrSmsService().send(outgoingSMSTO.getCellNo(),
                        new String(outgoingSMSTO.getMessageBody().getBytes("UTF-8"), "UTF-8"));

                logger.info("incoming result from NOCR SMS service is : " + result);

                if (SmsMessages.INVALID_USER.name().equals(result))
                    throw new ServiceException(BizExceptionCode.PSS_010, BizExceptionCode.PSS_010_MSG);
                else if (SmsMessages.INVALID_IP_ADDRESS.name().equals(result))
                    throw new ServiceException(BizExceptionCode.PSS_011, BizExceptionCode.PSS_011_MSG);
                else if (SmsMessages.SEND_ERROR.name().equals(result))
                    throw new ServiceException(BizExceptionCode.PSS_012, BizExceptionCode.PSS_012_MSG);
                else if (SmsMessages.SMS_VERY_LONG.name().equals(result))
                    throw new ServiceException(BizExceptionCode.PSS_013, BizExceptionCode.PSS_013_MSG);

                outgoingSMSTO.setSentDate(currentTime);
                outgoingSMSTO.setResult(result);
                smsDAO.update(outgoingSMSTO);

                return true;
            } catch (Exception e) {
                if (e instanceof BaseException) {
                    if (BizExceptionCode.PSS_003.equals(((BaseException) e).getExceptionCode())
                            || BizExceptionCode.PSS_010.equals(((BaseException) e).getExceptionCode())
                            || BizExceptionCode.PSS_011.equals(((BaseException) e).getExceptionCode()))
                        throw (BaseException) e;
                    logger.error(e.getMessage(), e);
                } else {
                    logger.error(BizExceptionCode.PSS_008 + BizExceptionCode.PSS_008_MSG + e.getMessage(), e);
                }
                outgoingSMSTO.setLastTryDate(currentTime);
                outgoingSMSTO.setRetryCount(outgoingSMSTO.getRetryCount() + 1);
                outgoingSMSTO.setResult(result);
                smsDAO.update(outgoingSMSTO);

                return false;
            }
        } else {
            return false;
        }
    }
    
    //Hossein
    @Override
    public Boolean processReferToCCOSSms(Integer from, Integer to) throws BaseException {
        List<OutgoingSMSTO> outgoingSMSTOs;
        try {
            String retryDuration = EmsUtil.getProfileValue(ProfileKeyName.KEY_SMS_RETRY_DURATION, DEFAULT_SMS_RETRY_DURATION);

            String maxRetryCount = EmsUtil.getProfileValue(ProfileKeyName.KEY_SMS_MAX_RETRY_COUNT, DEFAULT_SMS_MAX_RETRY_COUNT);

            HashMap parameters = new HashMap();
            parameters.put("retryDuration", Integer.valueOf(retryDuration));
            parameters.put("maxRetryCount", Integer.valueOf(maxRetryCount));

            ValueListHandler vlh = EMSValueListProvider.getProvider()
                    .loadList("availableReferToCCOSSmsList", "main".split(","), "count".split(","), parameters, null, null);

            outgoingSMSTOs = vlh.getList(from, to, true);
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.PSS_005, BizExceptionCode.PSS_005_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.PSS_006, BizExceptionCode.PSS_006_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSS_007, BizExceptionCode.GLB_008_MSG, e);
        }

        if (EmsUtil.checkListSize(outgoingSMSTOs)) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            java.util.Date utilDate = cal.getTime();
            Timestamp currentTime = new Timestamp(utilDate.getTime());

            SmsDAO smsDAO = getSmsDAO();
            OutgoingSMSTO outgoingSMSTO = outgoingSMSTOs.get(0);
            String result = null;

            try {
                result = getNocrSmsService().send(outgoingSMSTO.getCellNo(),
                        new String(outgoingSMSTO.getMessageBody().getBytes("UTF-8"), "UTF-8"));

                logger.info("incoming result from NOCR SMS service is : " + result);

                if (SmsMessages.INVALID_USER.name().equals(result))
                    throw new ServiceException(BizExceptionCode.PSS_010, BizExceptionCode.PSS_010_MSG);
                else if (SmsMessages.INVALID_IP_ADDRESS.name().equals(result))
                    throw new ServiceException(BizExceptionCode.PSS_011, BizExceptionCode.PSS_011_MSG);
                else if (SmsMessages.SEND_ERROR.name().equals(result))
                    throw new ServiceException(BizExceptionCode.PSS_012, BizExceptionCode.PSS_012_MSG);
                else if (SmsMessages.SMS_VERY_LONG.name().equals(result))
                    throw new ServiceException(BizExceptionCode.PSS_013, BizExceptionCode.PSS_013_MSG);

                outgoingSMSTO.setSentDate(currentTime);
                outgoingSMSTO.setResult(result);
                smsDAO.update(outgoingSMSTO);

                return true;
            } catch (Exception e) {
                if (e instanceof BaseException) {
                    if (BizExceptionCode.PSS_003.equals(((BaseException) e).getExceptionCode())
                            || BizExceptionCode.PSS_010.equals(((BaseException) e).getExceptionCode())
                            || BizExceptionCode.PSS_011.equals(((BaseException) e).getExceptionCode()))
                        throw (BaseException) e;
                    logger.error(e.getMessage(), e);
                } else {
                    logger.error(BizExceptionCode.PSS_008 + BizExceptionCode.PSS_008_MSG + e.getMessage(), e);
                }
                outgoingSMSTO.setLastTryDate(currentTime);
                outgoingSMSTO.setRetryCount(outgoingSMSTO.getRetryCount() + 1);
                outgoingSMSTO.setResult(result);
                smsDAO.update(outgoingSMSTO);

                return false;
            }
        } else {
            return false;
        }
    }

    private void setNocrSmsUserCredential(SmsDelegate smsDelegate) {
        String username = EmsUtil.getProfileValue(ProfileKeyName.KEY_NOCR_SMS_SERVICE_USERNAME, DEFAULT_NOCR_SMS_SERVICE_USERNAME);
        String password = EmsUtil.getProfileValue(ProfileKeyName.KEY_NOCR_SMS_SERVICE_PASSWORD, DEFAULT_NOCR_SMS_SERVICE_PASSWORD);

        /*******************UserName & Password ******************************/
        //***		this method use when authenticate SOAP request header and send to server
        Map<String, Object> req_ctx = ((BindingProvider) smsDelegate).getRequestContext();
        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("Username", Collections.singletonList(username));
        headers.put("Password", Collections.singletonList(password));
        req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
    }

    //Madanipour
	@Override
	public Integer fetchSmsCount(int smsType) throws BaseException {
        try {
            String retryDuration = EmsUtil.getProfileValue(ProfileKeyName.KEY_SMS_RETRY_DURATION, DEFAULT_SMS_RETRY_DURATION);

            String maxRetryCount = EmsUtil.getProfileValue(ProfileKeyName.KEY_SMS_MAX_RETRY_COUNT, DEFAULT_SMS_MAX_RETRY_COUNT);

            HashMap parameters = new HashMap();
            parameters.put("retryDuration", Integer.valueOf(retryDuration));
            parameters.put("maxRetryCount", Integer.valueOf(maxRetryCount));
            parameters.put("smsType", Integer.valueOf(smsType));

            ValueListHandler vlh = EMSValueListProvider.getProvider()
                    .loadList("availableSms", "main".split(","), "count".split(","), parameters, null, null);

            return vlh.executeCountQuery();
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.PSS_014, BizExceptionCode.PSS_005_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.PSS_015, BizExceptionCode.PSS_006_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSS_016, BizExceptionCode.GLB_008_MSG, e);
        }
	}

	@Override
	public Boolean processSmsToSend(Integer from, int to,int smsType)
			throws BaseException {
		List<OutgoingSMSTO> outgoingSMSTOs;
        try {
            String retryDuration = EmsUtil.getProfileValue(ProfileKeyName.KEY_SMS_RETRY_DURATION, DEFAULT_SMS_RETRY_DURATION);

            String maxRetryCount = EmsUtil.getProfileValue(ProfileKeyName.KEY_SMS_MAX_RETRY_COUNT, DEFAULT_SMS_MAX_RETRY_COUNT);

            HashMap parameters = new HashMap();
            parameters.put("retryDuration", Integer.valueOf(retryDuration));
            parameters.put("maxRetryCount", Integer.valueOf(maxRetryCount));
            parameters.put("smsType", Integer.valueOf(smsType));
            

            ValueListHandler vlh = EMSValueListProvider.getProvider()
                    .loadList("availableSms", "main".split(","), "count".split(","), parameters, null, null);

            outgoingSMSTOs = vlh.getList(from, to, true);
        } catch (ListException e) {
            throw new ServiceException(BizExceptionCode.PSS_017, BizExceptionCode.PSS_005_MSG, e);
        } catch (ListHandlerException e) {
            throw new ServiceException(BizExceptionCode.PSS_018, BizExceptionCode.PSS_006_MSG, e);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.PSS_019, BizExceptionCode.GLB_008_MSG, e);
        }

        if (EmsUtil.checkListSize(outgoingSMSTOs)) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            java.util.Date utilDate = cal.getTime();
            Timestamp currentTime = new Timestamp(utilDate.getTime());

            SmsDAO smsDAO = getSmsDAO();
            OutgoingSMSTO outgoingSMSTO = outgoingSMSTOs.get(0);
            String result = null;

            try {
                result = getNocrSmsService().send(outgoingSMSTO.getCellNo(),
                        new String(outgoingSMSTO.getMessageBody().getBytes("UTF-8"), "UTF-8"));

                logger.info("incoming result from NOCR SMS service is : " + result);

                if (SmsMessages.INVALID_USER.name().equals(result))
                    throw new ServiceException(BizExceptionCode.PSS_020, BizExceptionCode.PSS_010_MSG);
                else if (SmsMessages.INVALID_IP_ADDRESS.name().equals(result))
                    throw new ServiceException(BizExceptionCode.PSS_021, BizExceptionCode.PSS_011_MSG);
                else if (SmsMessages.SEND_ERROR.name().equals(result))
                    throw new ServiceException(BizExceptionCode.PSS_022, BizExceptionCode.PSS_012_MSG);
                else if (SmsMessages.SMS_VERY_LONG.name().equals(result))
                    throw new ServiceException(BizExceptionCode.PSS_023, BizExceptionCode.PSS_013_MSG);

                outgoingSMSTO.setSentDate(currentTime);
                outgoingSMSTO.setResult(result);
                smsDAO.update(outgoingSMSTO);

                return true;
            } catch (Exception e) {
                if (e instanceof BaseException) {
                    if (BizExceptionCode.PSS_003.equals(((BaseException) e).getExceptionCode())
                            || BizExceptionCode.PSS_020.equals(((BaseException) e).getExceptionCode())
                            || BizExceptionCode.PSS_021.equals(((BaseException) e).getExceptionCode()))
                        throw (BaseException) e;
                    logger.error(e.getMessage(), e);
                } else {
                    logger.error(BizExceptionCode.PSS_024 + BizExceptionCode.PSS_008_MSG + e.getMessage(), e);
                }
                outgoingSMSTO.setLastTryDate(currentTime);
                outgoingSMSTO.setRetryCount(outgoingSMSTO.getRetryCount() + 1);
                outgoingSMSTO.setResult(result);
                smsDAO.update(outgoingSMSTO);

                return false;
            }
        } else {
            return false;
        }

	}

	@Override
	public void deleteOldRecordsFromMsgt(Integer timeInterval, Integer smsType)
			throws BaseException {
		
		SmsDAO smsDAO = getSmsDAO();
		smsDAO.deleteOldRecordsFromMsgt(timeInterval, smsType);
		
	}
}
