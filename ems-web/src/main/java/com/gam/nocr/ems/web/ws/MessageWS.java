package com.gam.nocr.ems.web.ws;

import java.sql.Timestamp;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.MessageDelegator;
import com.gam.nocr.ems.biz.service.PersonManagementService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.MessageDestinationTO;
import com.gam.nocr.ems.data.domain.ws.MessageWTO;
import com.gam.nocr.ems.data.domain.ws.SecurityContextWTO;

@WebFault(faultBean = "com.gam.nocr.ems.web.ws.InternalException")
@WebService(serviceName = "MessageWS", portName = "MessageWSPort", targetNamespace = "http://ws.web.ems.nocr.gam.com/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Internal
public class MessageWS extends EMSWS {

	private static final Logger logger = BaseLog.getLogger(MessageWS.class);

	@WebMethod
	public MessageWTO downloadMessageFile(
			@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
			@WebParam(name = "msgID") Long msgId) throws InternalException {

		UserProfileTO up = super.validateRequest(securityContextWTO);

		try {
			MessageDelegator messageDelegator = new MessageDelegator();
			MessageDestinationTO messageDestinationTO = messageDelegator
					.getMessageDestinationById(up, msgId, null);
			MessageWTO messageWTO = new MessageWTO();
			if (messageDestinationTO != null) {

				if (messageDestinationTO.getMessage() == null)
					throw new InternalException(
							WebExceptionCode.HEW_005,
							new EMSWebServiceFault(WebExceptionCode.HEW_005_MSG));
				messageWTO.setAttachFile(messageDestinationTO.getMessage()
						.getAttachFile());
				messageWTO.setMsgExtention(messageDestinationTO.getMessage()
						.getFileType());
				messageWTO.setMsgDownload(messageDestinationTO.getMessage()
						.getFileName());
			}

			return messageWTO;

		} catch (BaseException e) {
			throw new InternalException(e.getMessage(), new EMSWebServiceFault(
					e.getExceptionCode()));
		} catch (Exception e) {
			throw new InternalException(WebExceptionCode.HEW_003,
					new EMSWebServiceFault(WebExceptionCode.HEW_003_MSG), e);
		}

	}

	@WebMethod
	public MessageWTO getMessageDetail(
			@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
			@WebParam(name = "msgID") long msgId) throws InternalException {

		UserProfileTO up = super.validateRequest(securityContextWTO);
		Long personID = null;
		
		try {
			personID = getPersonService().findPersonIdByUsername(up.getUserName());
		} catch (BaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			MessageDelegator messageDelegator = new MessageDelegator();
			MessageDestinationTO messageDestinationTO = messageDelegator
					.getMessageDestinationById(up, msgId, personID);
			MessageWTO messageWTO = new MessageWTO();
			if (messageDestinationTO != null) {

				if (messageDestinationTO.getMessage() == null)
					throw new InternalException(
							WebExceptionCode.HEW_005,
							new EMSWebServiceFault(WebExceptionCode.HEW_005_MSG));
				messageWTO.setMsgContent(messageDestinationTO.getMessage()
						.getContent());
				messageWTO.setMsgDate(new Timestamp(messageDestinationTO
						.getMessage().getCreateDate().getTime()));
				messageWTO.setMsgSubject(messageDestinationTO.getMessage()
						.getTitle());
				messageWTO.setSenderUsername(messageDestinationTO.getMessage()
						.getSenderUsername());
			}

			return messageWTO;

		} catch (BaseException e) {
			throw new InternalException(e.getMessage(), new EMSWebServiceFault(
					e.getExceptionCode()));
		} catch (Exception e) {
			throw new InternalException(WebExceptionCode.HEW_004,
					new EMSWebServiceFault(WebExceptionCode.HEW_004_MSG), e);
		}

	}

	@WebMethod
	public void deleteMessage(
			@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
			@WebParam(name = "msgID") long msgId) throws InternalException {
		
		Long personID = null;

		UserProfileTO up = super.validateRequest(securityContextWTO);
  		try {
			personID = getPersonService().findPersonIdByUsername(up.getUserName());
		} catch (BaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			MessageDelegator messageDelegator = new MessageDelegator();
			messageDelegator.deleteMessageDestinationBypesonId(up, msgId,personID);

		} catch (BaseException e) {
			throw new InternalException(e.getMessage(), new EMSWebServiceFault(
					e.getExceptionCode()));
		} catch (Exception e) {
			throw new InternalException(WebExceptionCode.HEW_006,
					new EMSWebServiceFault(WebExceptionCode.HEW_006_MSG), e);
		}

	}
	
	//Anbari
    private PersonManagementService getPersonService() throws BaseException {
        PersonManagementService personManagementService;
        try {
            personManagementService = (PersonManagementService) ServiceFactoryProvider.getServiceFactory().getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_PERSON),null);
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.PDL_001, BizExceptionCode.GLB_002_MSG, e, EMSLogicalNames.SRV_PERSON.split(","));
        }
        return personManagementService;
    }

}
