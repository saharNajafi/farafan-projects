package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.util.EmsUtil;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.security.cert.X509Certificate;

/**
 * Utility class used by CMSWS in order to validate caller of its services
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class WSSecurity {
    private static final Logger logger = BaseLog.getLogger(WSSecurity.class);

    private static final String DEFAULT_CMS_SUBJECT_DN = "CN=alireza-pc, OU=FOR TESTING ONLY, O=MyOrganization, L=MyTown, ST=MyState, C=US";
    private static final String DEFAULT_CMS_CHECK_SUBJECT_DN = "TRUE";

    /**
     * Validates web service caller to make sure it's the CMS that is calling us. The authentication process comprises
     * of comparing the subjectDN of caller's SSL certificate with the value specified in EMS configurations
     *
     * @param webServiceContext    The caller's request context to take required information about its SSL certificate
     * @throws BaseException
     */
    public void authenticate(WebServiceContext webServiceContext) throws BaseException {
        String check = EmsUtil.getProfileValue(ProfileKeyName.KEY_CMS_CHECK_SUBJECT_DN, DEFAULT_CMS_CHECK_SUBJECT_DN);
        if (Boolean.valueOf(check)) {
            MessageContext messageContext = webServiceContext.getMessageContext();
            HttpServletRequest request = (HttpServletRequest) messageContext.get(MessageContext.SERVLET_REQUEST);
            String cipherSuite = (String) request.getAttribute("javax.servlet.request.cipher_suite");
            X509Certificate certChain[];
            if (cipherSuite != null) {
                certChain = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
                if (certChain != null) {
                    for (int i = 0; i < certChain.length; i++) {
                        if (certChain[i] != null) {
                            logger.info("Client Certificate [" + i + "] = "
                                    + certChain[i].toString() + "*************"
                                    + certChain[i].getSubjectDN());
                        }
                    }
                }
            } else {
                throw new BaseException(WebExceptionCode.SCW_001, WebExceptionCode.SCW_001_MSG);
            }
            if (certChain != null && certChain[0] != null) {
                String partnerSubjectDN = EmsUtil.getProfileValue(ProfileKeyName.KEY_CMS_SUBJECT_DN, DEFAULT_CMS_SUBJECT_DN);

                logger.info("subjectDN=" + certChain[0].getSubjectDN());
                logger.info("partnerSubjectDN=" + partnerSubjectDN);

                if (certChain[0].getSubjectDN().getName().equals(partnerSubjectDN)) {
                    logger.info(certChain[0].getSubjectDN() + "==" + partnerSubjectDN);
                } else {
                    throw new BaseException(WebExceptionCode.SCW_002, WebExceptionCode.SCW_001_MSG);
                }
            } else {
                throw new BaseException(WebExceptionCode.SCW_002, WebExceptionCode.SCW_001_MSG);
            }
        }
    }
}
