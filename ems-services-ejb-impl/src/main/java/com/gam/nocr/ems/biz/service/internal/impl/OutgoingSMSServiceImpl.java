package com.gam.nocr.ems.biz.service.internal.impl;

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

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.external.client.nocrSms.SmsDelegate;
import com.gam.nocr.ems.biz.service.external.client.nocrSms.SmsService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.dao.SmsDAO;
import com.gam.nocr.ems.data.domain.OutgoingSMSTO;
import com.gam.nocr.ems.data.enums.SmsMessages;
import com.gam.nocr.ems.util.EmsUtil;

@Stateless(name = "OutgoingSMSService")
@Local(OutgoingSMSServiceLocal.class)
@Remote(OutgoingSMSServiceRemote.class)
public class OutgoingSMSServiceImpl extends EMSAbstractService implements
		OutgoingSMSServiceLocal, OutgoingSMSServiceRemote {

	private static final String DEFAULT_PORTAL_SMS_ENDPOINT = "http://localhost:7001/services/SmsWS?wsdl";
	private static final String DEFAULT_NOCR_SMS_ENDPOINT = "http://sms.sabteahval.ir:8001/SmsProject/SmsPort?wsdl";
	private static final String DEFAULT_PORTAL_NAMESPACE = "http://portalws.ws.web.portal.nocr.gam.com/";
	private static final String DEFAULT_NOCR_SMS_NAMESPACE = "http://ws.sms.com/";
	private static final String DEFAULT_NOCR_SMS_SERVICE_USERNAME = "SMARTCARD";
	private static final String DEFAULT_NOCR_SMS_SERVICE_PASSWORD = "sm#asrcb*92";

	private static final String DEFAULT_SMS_MAX_RETRY_COUNT = "0";

	private static final Logger logger = BaseLog
			.getLogger(OutgoingSMSServiceImpl.class);

	private SmsDAO getSmsDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_SMS));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.OSS_001,
					BizExceptionCode.GLB_001_MSG, e,
					EMSLogicalNames.DAO_SMS.split(","));
		}
	}

	private SmsDelegate getNocrSmsService() throws BaseException {
		try {
			String wsdlUrl = EmsUtil.getProfileValue(
					ProfileKeyName.KEY_NOCR_SMS_ENDPOINT,
					DEFAULT_NOCR_SMS_ENDPOINT);
			String namespace = EmsUtil.getProfileValue(
					ProfileKeyName.KEY_NOCR_SMS_NAMESPACE,
					DEFAULT_NOCR_SMS_NAMESPACE);
			String serviceName = "SmsService";

			SmsDelegate smsDelegate = new SmsService(new URL(wsdlUrl),
					new QName(namespace, serviceName)).getSmsPort();
			// SmsDelegate smsDelegate = new SmsService().getSmsPort();
			setNocrSmsUserCredential(smsDelegate);
			EmsUtil.setJAXWSWebserviceProperties(smsDelegate, wsdlUrl);
			return smsDelegate;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.OSS_002,
					BizExceptionCode.PSS_003_MSG, e);
		}
	}

	private void setNocrSmsUserCredential(SmsDelegate smsDelegate) {
		String username = EmsUtil.getProfileValue(
				ProfileKeyName.KEY_NOCR_SMS_SERVICE_USERNAME,
				DEFAULT_NOCR_SMS_SERVICE_USERNAME);
		String password = EmsUtil.getProfileValue(
				ProfileKeyName.KEY_NOCR_SMS_SERVICE_PASSWORD,
				DEFAULT_NOCR_SMS_SERVICE_PASSWORD);

		/******************* UserName & Password ******************************/
		// *** this method use when authenticate SOAP request header and send to
		// server
		Map<String, Object> req_ctx = ((BindingProvider) smsDelegate)
				.getRequestContext();
		Map<String, List<String>> headers = new HashMap<String, List<String>>();
		headers.put("Username", Collections.singletonList(username));
		headers.put("Password", Collections.singletonList(password));
		req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
	}

	@Override
	public void processSmsToSend(Long id) throws BaseException {

		OutgoingSMSTO outgoingSMSTO = new OutgoingSMSTO();
		SmsDAO smsDAO = getSmsDAO();
		try {

			outgoingSMSTO = smsDAO.find(OutgoingSMSTO.class, id);

		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.OSS_008,
					BizExceptionCode.GLB_008_MSG);
		}
		
		String maxRetryCount = EmsUtil.getProfileValue(ProfileKeyName.KEY_SMS_MAX_RETRY_COUNT,DEFAULT_SMS_MAX_RETRY_COUNT);
		if (outgoingSMSTO != null) {
			
			if(outgoingSMSTO.getRetryLimit() == null ){
				
				outgoingSMSTO.setRetryLimit(Integer.parseInt(maxRetryCount));
			}
			
			java.util.Calendar cal = java.util.Calendar.getInstance();
			java.util.Date utilDate = cal.getTime();
			Timestamp currentTime = new Timestamp(utilDate.getTime());

			String result = null;
		
			try {

				result = getNocrSmsService().send(
						outgoingSMSTO.getCellNo(),
						new String(outgoingSMSTO.getMessageBody().getBytes(
								"UTF-8"), "UTF-8"));

				logger.info("incoming result from NOCR SMS service is : "
						+ result);

				if (SmsMessages.INVALID_USER.name().equals(result))
					throw new ServiceException(BizExceptionCode.OSS_003,
							BizExceptionCode.PSS_010_MSG);
				else if (SmsMessages.INVALID_IP_ADDRESS.name().equals(result))
					throw new ServiceException(BizExceptionCode.OSS_004,
							BizExceptionCode.PSS_011_MSG);
				else if (SmsMessages.SEND_ERROR.name().equals(result))
					throw new ServiceException(BizExceptionCode.OSS_005,
							BizExceptionCode.PSS_012_MSG);
				else if (SmsMessages.SMS_VERY_LONG.name().equals(result))
					throw new ServiceException(BizExceptionCode.OSS_006,
							BizExceptionCode.PSS_013_MSG);

				outgoingSMSTO.setSentDate(currentTime);
				outgoingSMSTO.setResult(result);
				smsDAO.update(outgoingSMSTO);

			} catch (Exception e) {
				if (e instanceof BaseException) {
					if (BizExceptionCode.OSS_002.equals(((BaseException) e)
							.getExceptionCode())
							|| BizExceptionCode.OSS_003
									.equals(((BaseException) e)
											.getExceptionCode())
							|| BizExceptionCode.OSS_004
									.equals(((BaseException) e)
											.getExceptionCode()))
						throw (BaseException) e;
					logger.error(e.getMessage(), e);
				} else {
					logger.error(BizExceptionCode.OSS_007
							+ BizExceptionCode.PSS_008_MSG + e.getMessage(), e);
				}
				outgoingSMSTO.setLastTryDate(currentTime);
				outgoingSMSTO.setRetryCount(outgoingSMSTO.getRetryCount() + 1);
				outgoingSMSTO.setResult(result);
				smsDAO.update(outgoingSMSTO);

			}

		}

	}

	@Override
	public List<Long> fetchMessagesId(Integer sendSmsType, Integer fetchLimit)
			throws BaseException {
		try {
			List<Long> fetchMessagesId = getSmsDAO().fetchMessagesId(
					sendSmsType, fetchLimit);
			return fetchMessagesId;
		} catch (Exception e) {
			throw new ServiceException(BizExceptionCode.OSS_009,
					BizExceptionCode.GLB_008_MSG, e);

		}
	}

}
