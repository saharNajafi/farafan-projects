package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.security.SecurityUtil;
import com.gam.nocr.ems.biz.delegator.UserDelegator;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.CertificateTO;
import com.gam.nocr.ems.data.domain.vol.UserVTO;
import com.gam.nocr.ems.data.domain.ws.SecurityContextWTO;
import com.gam.nocr.ems.data.enums.CertificateUsage;
import gampooya.tools.util.Base64;
import org.slf4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Collection of services related to handling user related requests (e.g. changing password)
 *
 * @author Haeri (haeri@gamelectronics.com)
 */
@WebFault(faultBean = "com.gam.nocr.ems.web.ws.InternalException")
@WebService(serviceName = "UserManagementWS", portName = "UserManagementWSPort", targetNamespace = "http://ws.web.ems.nocr.gam.com/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Internal
public class UserManagementWS extends EMSWS {

    static final Logger logger = BaseLog.getLogger(UserManagementWS.class);

    /**
     * Converts given parameter to its Base64 string representation
     *
     * @param input Input data to be converted to Base64
     * @return Base64 representation of given input
     * @throws InternalException
     */
    private String convertToBase64(byte[] input) throws InternalException {
        String encoded = null;
        try {
            encoded = new String(Base64.encode(input), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InternalException(WebExceptionCode.UMW_002, new EMSWebServiceFault(WebExceptionCode.UMW_002_MSG), e);
        }
        return encoded;
    }

    /**
     * Returns a public key to be used by CCOS to encrypt password before sending to EMS. It fetches the certificate
     * object with CCOS_PASSWORD_CER_PUBLIC usage from EMST_CERT table in database
     *
     * @param securityContextWTO The login and session information of the user
     * @return a public key to be used by CCOS to encrypt password before sending to EMS
     * @throws InternalException
     */
    @WebMethod
    public String getUserCredentials(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        UserDelegator userDelegator = new UserDelegator();
        CertificateTO certificateTO = null;
        try {
            certificateTO = userDelegator.getCertificateByUsage(up, CertificateUsage.CCOS_PASSWORD_CER_PUBLIC);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.UMW_001, new EMSWebServiceFault(WebExceptionCode.UMW_001_MSG), e);
        }
        return convertToBase64(certificateTO.getCertificate());
    }

    /**
     * Updates user's passwords
     *
     * @param securityContextWTO The login and session information of the user
     * @param oldPassword        Old password of user
     * @param newPassword        New password of user
     * @param confirmNewPassword Confirmation of new password of user
     * @throws InternalException
     */
    @WebMethod
    public void changeUserPassword(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                   @WebParam(name = "oldPassword") byte[] oldPassword,
                                   @WebParam(name = "newPassword") byte[] newPassword,
                                   @WebParam(name = "confirmNewPassword") byte[] confirmNewPassword) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);

        UserDelegator userDelegator = new UserDelegator();
        UserVTO vto = new UserVTO();
        try {
            CertificateTO certificate = userDelegator.getCertificateByUsage(up, CertificateUsage.CCOS_PASSWORD_CER_PRIVATE);
            vto.setOldPassword(new String(SecurityUtil.asymmetricDecryption(certificate.getCertificate(), oldPassword, certificate.getCode()), Charset.forName("UTF-8")));
            vto.setNewPassword(new String(SecurityUtil.asymmetricDecryption(certificate.getCertificate(), newPassword, certificate.getCode()), Charset.forName("UTF-8")));
            vto.setConfirmNewPassword(new String(SecurityUtil.asymmetricDecryption(certificate.getCertificate(), confirmNewPassword, certificate.getCode()), Charset.forName("UTF-8")));
            userDelegator.changePassword(up, vto);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.UMW_003, new EMSWebServiceFault(WebExceptionCode.UMW_003_MSG), e);
        }
    }

    /**
	 * Return user's permissions
	 * 
	 * @param securityContextWTO
	 *            The login and session information of the user
	 * @throws InternalException
	 * @author ganjyar
	 */
	@WebMethod
	public List<String> getUserAccess(
			@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO)
			throws InternalException {
		UserProfileTO up = super.validateRequest(securityContextWTO);

		UserDelegator userDelegator = new UserDelegator();
		try {
			return userDelegator.getUserAccess(up);
		} catch (BaseException e) {
			throw new InternalException(e.getMessage(), new EMSWebServiceFault(
					e.getExceptionCode()), e);
		} catch (Exception e) {
			throw new InternalException(WebExceptionCode.UMW_004,
					new EMSWebServiceFault(WebExceptionCode.UMW_004_MSG), e);
		}
	}
}