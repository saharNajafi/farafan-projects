package com.gam.nocr.ems.web.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.delegator.HelpDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.AboutTO;
import com.gam.nocr.ems.data.domain.HelpTO;
import com.gam.nocr.ems.data.domain.ws.AboutWTO;
import com.gam.nocr.ems.data.domain.ws.HelpWTO;
import com.gam.nocr.ems.data.domain.ws.SecurityContextWTO;

/**
 * Collection of services related to the help and about are implemented in this
 * web service
 * 
 */

@WebFault(faultBean = "com.gam.nocr.ems.web.ws.InternalException")
@WebService(serviceName = "HelpWS", portName = "HelpWSPort", targetNamespace = "http://ws.web.ems.nocr.gam.com/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Internal
public class HelpWS extends EMSWS {

	private static final Logger logger = BaseLog.getLogger(HelpWS.class);

	/**
	 * return the last about
	 * 
	 * @param securityContextWTO
	 *            The login and session information of the user
	 * @return the about content
	 * @throws InternalException
	 */
	@WebMethod
	public AboutWTO getAbout(
			@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO)
			throws InternalException {
		UserProfileTO up = super.validateRequest(securityContextWTO);

		HelpDelegator helpDelegator = new HelpDelegator();
		try {
			AboutTO aboutTO = helpDelegator.getAbout(up);
			AboutWTO aboutWTO = new AboutWTO();

			aboutWTO.setCreateDate(aboutTO.getCreateDate());
			aboutWTO.setContent(aboutTO.getContent());
			aboutWTO.setId(aboutTO.getId());

			return aboutWTO;
		} catch (BaseException e) {
			throw new InternalException(e.getMessage(), new EMSWebServiceFault(
					e.getExceptionCode()), e);
		} catch (Exception e) {
			throw new InternalException(WebExceptionCode.HEW_001,
					new EMSWebServiceFault(WebExceptionCode.HEW_001_MSG), e);
		}
	}

	@WebMethod
	public HelpWTO downloadHelpFile(
			@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
			@WebParam(name = "requestID") long fileId) throws InternalException {

		UserProfileTO up = super.validateRequest(securityContextWTO);

		try {
			HelpDelegator helpDelegator = new HelpDelegator();
			HelpTO helpTO = helpDelegator.downloadHelpFile(up, fileId);
			HelpWTO helpWTO = new HelpWTO();
			helpWTO.setHelpId(helpTO.getId());
			helpWTO.setHelpFile(helpTO.getHelpFile());
			helpWTO.setHelpExtention(helpTO.getContentType());
			return helpWTO;

		} catch (BaseException e) {
			throw new InternalException(e.getMessage(), new EMSWebServiceFault(
					e.getExceptionCode()));
		} catch (Exception e) {
			throw new InternalException(WebExceptionCode.HEW_002,
					new EMSWebServiceFault(WebExceptionCode.HEW_002_MSG), e);
		}

	}

}
