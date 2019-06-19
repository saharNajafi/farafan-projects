package com.gam.nocr.ems.biz.service.external.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.AbstractService;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.external.client.bpi.BpiException_Exception;
import com.gam.nocr.ems.biz.service.external.client.bpi.BpiInquiryWTO;
import com.gam.nocr.ems.biz.service.external.client.bpi.BpiService;
import com.gam.nocr.ems.biz.service.external.client.bpi.BpiService_Service;
import com.gam.nocr.ems.biz.service.external.client.pki.RAWS;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.RegistrationPaymentTO;
import com.gam.nocr.ems.util.EmsUtil;
import org.slf4j.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.xml.namespace.QName;
import java.net.URL;

@Stateless(name = "BpiInquiryService")
@Local(BpiInquiryServiceLocal.class)
@Remote(BpiInquiryServiceRemote.class)
public class BpiInquiryServiceImpl extends AbstractService
        implements BpiInquiryServiceLocal, BpiInquiryServiceRemote {

    private static final String DEFAULT_WSDL_URL = "http://10.7.17.109:7006/bpi-1.0/BpiService?WSDL";
    private static final String DEFAULT_NAMESPACE = "http://bpi.farafan.ir/";
    private static final Logger logger = BaseLog.getLogger(BpiInquiryServiceImpl.class);
    private static final Logger bpiLogger = BaseLog.getLogger("BpiLogger");
    private static final String BPI_ERROR_SADAD_001 = "SADAD_001";
    private static final String BPI_ERROR_SADAD_002 = "SADAD_002";
    private static final String BPI_ERROR_SADAD_003 = "SADAD_003";
    private static final String BPI_ERROR_SADAD_004 = "SADAD_004";
    private static final String BPI_ERROR_SADAD_005 = "SADAD_005";
    private static final String BPI_ERROR_SADAD_006 = "SADAD_006";


    @Override
    public BpiInquiryWTO bpiInquiry(RegistrationPaymentTO registrationPaymentTO) throws BaseException {

        BpiInquiryWTO bpiInquiryWTO = null;
        try {
            bpiInquiryWTO = getService().bpiInquiry(
                    registrationPaymentTO.getPaidBank().getCode()
                    , registrationPaymentTO.getPaymentCode()
                    , String.valueOf(registrationPaymentTO.getOrderId())
                   );
        } catch (BpiException_Exception e) {
            String errorMessage = e.getFaultInfo().getMessage();
            String errorCode = e.getFaultInfo().getExceptionCode();
            if (BPI_ERROR_SADAD_001.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.BPI_001, errorMessage, e,
                        EMSLogicalNames.SRV_BPI.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG,
                        serviceException,
                        EMSLogicalNames.SRV_BPI.split(","));
                bpiLogger.error(BizExceptionCode.GLB_003_MSG,
                        serviceException,
                        EMSLogicalNames.SRV_BPI.split(","));
                throw serviceException;
            }
            if (BPI_ERROR_SADAD_002.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.BPI_002, errorMessage, e,
                        EMSLogicalNames.SRV_BPI.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG,
                        serviceException,
                        EMSLogicalNames.SRV_BPI.split(","));
                bpiLogger.error(BizExceptionCode.GLB_003_MSG,
                        serviceException,
                        EMSLogicalNames.SRV_BPI.split(","));
                throw serviceException;
            }
            if (BPI_ERROR_SADAD_003.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.BPI_003, errorMessage, e,
                        EMSLogicalNames.SRV_BPI.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG,
                        serviceException,
                        EMSLogicalNames.SRV_BPI.split(","));
                bpiLogger.error(BizExceptionCode.GLB_003_MSG,
                        serviceException,
                        EMSLogicalNames.SRV_BPI.split(","));
                throw serviceException;
            }
            if (BPI_ERROR_SADAD_004.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.BPI_004, errorMessage, e,
                        EMSLogicalNames.SRV_BPI.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG,
                        serviceException,
                        EMSLogicalNames.SRV_BPI.split(","));
                bpiLogger.error(BizExceptionCode.GLB_003_MSG,
                        serviceException,
                        EMSLogicalNames.SRV_BPI.split(","));
                throw serviceException;
            }
            if (BPI_ERROR_SADAD_005.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.BPI_005, errorMessage, e,
                        EMSLogicalNames.SRV_BPI.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG,
                        serviceException,
                        EMSLogicalNames.SRV_BPI.split(","));
                bpiLogger.error(BizExceptionCode.GLB_003_MSG,
                        serviceException,
                        EMSLogicalNames.SRV_BPI.split(","));
                throw serviceException;
            }
            if (BPI_ERROR_SADAD_006.equals(errorCode)) {
                ServiceException serviceException = new ServiceException(
                        BizExceptionCode.BPI_006, errorMessage, e,
                        EMSLogicalNames.SRV_BPI.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG,
                        serviceException,
                        EMSLogicalNames.SRV_BPI.split(","));
                bpiLogger.error(BizExceptionCode.GLB_003_MSG,
                        serviceException,
                        EMSLogicalNames.SRV_BPI.split(","));
                throw serviceException;
            }

            ServiceException serviceException = new ServiceException(
                    BizExceptionCode.BPI_007, errorMessage, e,
                    EMSLogicalNames.SRV_BPI.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException,
                    EMSLogicalNames.SRV_BPI.split(","));
            bpiLogger.error(BizExceptionCode.GLB_003_MSG,
                    serviceException,
                    EMSLogicalNames.SRV_BPI.split(","));
            throw serviceException;
        }
        return bpiInquiryWTO;
    }


    /**
     * The method getService is used to get WebServices from BPI sub system
     *
     * @return an instance of type {@link RAWS}
     * @throws BaseException if cannot get the service
     */
    private BpiService getService() throws BaseException {
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            String wsdlUrl = (String) pm.getProfile(ProfileKeyName.KEY_BPI_ENDPOINT, true, null, null);
            String namespace = (String) pm.getProfile(ProfileKeyName.KEY_BPI_NAMESPACE, true, null, null);
            if (wsdlUrl == null)
                wsdlUrl = DEFAULT_WSDL_URL;
            if (namespace == null)
                namespace = DEFAULT_NAMESPACE;
            String serviceName = "BpiService";
            logger.debug("Bpi wsdl url: " + wsdlUrl);
            bpiLogger.debug("Bpi wsdl url: " + wsdlUrl);
            BpiService port = new BpiService_Service(new URL(wsdlUrl), new QName(namespace, serviceName)).getBpiServicePort();
            EmsUtil.setJAXWSWebserviceProperties(port, wsdlUrl);
            return port;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.BPI_008, e.getMessage(), e);
        }
    }

}
