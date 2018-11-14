package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.BusinessLogService;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.biz.service.external.client.emks.lds.*;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.BusinessLogTO;
import com.gam.nocr.ems.data.domain.ws.EMKSCardMoCKeysWTO;
import com.gam.nocr.ems.data.domain.ws.EMKSDataResultWTO;
import com.gam.nocr.ems.data.domain.ws.EMKSDataWTO;
import com.gam.nocr.ems.data.enums.BusinessLogAction;
import com.gam.nocr.ems.data.enums.BusinessLogEntity;
import com.gam.nocr.ems.util.EmsUtil;
import com.sun.xml.ws.client.BindingProviderProperties;
import org.slf4j.Logger;
import servicePortUtil.ServicePorts;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import java.lang.Exception;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@Stateless(name = "EmksService")
@Local(EmksServiceLocal.class)
@Remote(EmksServiceRemote.class)
public class EmksServiceImpl extends EMSAbstractService implements
        EmksServiceLocal, EmksServiceRemote {
    private static final Logger emksLogger = BaseLog.getLogger("EmksLogger");
    private static final String DEFAULT_WSDL_URL = "http://10.202.1.2/EMKS_WCFService.EMKS.svc?singleWsdl";
    private static final String DEFAULT_NAMESPACE = "http://tempuri.org/";
    private static final Logger logger = BaseLog.getLogger(EmksServiceImpl.class);
    private static final Logger threadLocalLogger = BaseLog.getLogger("threadLocal");

    /**
     * Default sync web service timeout configurations in millisecond
     */
    private static final String DEFAULT_EMKS_WEBSERVICE_TIMEOUT = "300000";
    private static final String EMKS_0001 = "EMKS-0001";
    private static final String EMKS_0002 = "EMKS-0002";
    private static final String EMKS_0003 = "EMKS-0003";
    private static final String EMKS_0004 = "EMKS-0004";
    private static final String EMKS_0005 = "EMKS-0005";
    private static final String EMKS_0099 = "EMKS-0099";

    private IServiceEMKS getEMKSService() throws BaseException {
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();

            String wsdlUrl = (String) pm.getProfile(
                    ProfileKeyName.KEY_EMKS_ENDPOINT, true, null, null);
            String namespace = (String) pm.getProfile(
                    ProfileKeyName.KEY_EMKS_NAMESPACE, true, null, null);
            // String wsdlUrl =
            // "http://10.202.1.2/EMKS_WCFService.EMKS.svc?singleWsdl";
            // String namespace = "http://tempuri.org/";
            if (wsdlUrl == null)
                wsdlUrl = DEFAULT_WSDL_URL;
            if (namespace == null)
                namespace = DEFAULT_NAMESPACE;
            String serviceName = "EMKS";

            //Commented for ThraedLocal
            //IServiceEMKS port = new EMKS((new URL(wsdlUrl)), new QName(namespace, serviceName)).getBasicHttpBindingIServiceEMKS();
            IServiceEMKS port = ServicePorts.getEmksPort();
            if (port == null) {
                threadLocalLogger.debug("**************************** new IServiceEMKS in EMKS getEMKSService()");
                port = new EMKS((new URL(wsdlUrl)), new QName(namespace, serviceName)).getBasicHttpBindingIServiceEMKS();
                ServicePorts.setEmksPort(port);
            } else {
                threadLocalLogger.debug("***************************** using IServiceEMKS from ThradLocal");
            }
            EmsUtil.setJAXWSWebserviceProperties(port, wsdlUrl);
            try {
                Integer webserviceTimeout = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_EMKS_WEBSERVICE_TIMEOUT, DEFAULT_EMKS_WEBSERVICE_TIMEOUT));
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
            throw new ServiceException(BizExceptionCode.ESI_009,
                    e.getMessage(), e);
        }
    }

    private BusinessLogService getBusinessLogService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider
                .getServiceFactory();
        BusinessLogService businessLogService;
        try {
            businessLogService = serviceFactory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_BUSINESS_LOG), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            emksLogger.error(BizExceptionCode.ESI_029 + " : "
                    + BizExceptionCode.GLB_002_MSG, e);
            throw new ServiceException(BizExceptionCode.ESI_029,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_BUSINESS_LOG.split(","));
        }
        return businessLogService;
    }


    @Override
    public EMKSCardMoCKeysWTO getNIDCardMoCKeys(EMKSDataWTO emksDataWTO,
                                                Long requestID) throws BaseException {
        String str = "";
        if (emksDataWTO == null)
            throw new ServiceException(BizExceptionCode.ESI_007,
                    BizExceptionCode.ESI_007_MSG);
        checkValidationOnemksDataNIDCardMoCKeys(emksDataWTO);
        str = createCardInfoXml(emksDataWTO);
        emksLogger.info(str);
        IServiceEMKS emksService = getEMKSService();
        try {
            CardMoCKeys nidCardMoCKeys = emksService.getNIDCardMoCKeys(str);
            EMKSCardMoCKeysWTO emksDataResultWTO = new EMKSCardMoCKeysWTO();
            emksDataResultWTO.setMoc_enc(nidCardMoCKeys.getMoCENC().getValue().toString());
            emksDataResultWTO.setMoc_mac(nidCardMoCKeys.getMoCMAC().getValue().toString());
            insertBusinessActionLog(requestID, str, BusinessLogAction.GET_MOC_KEYS);
            return emksDataResultWTO;
        } catch (IServiceEMKSGetNIDCardMoCKeysEMKSExceptionFaultFaultMessage e) {
            handleEmksException(e);
            return null;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.ESI_008,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }


    @Override
    public EMKSDataResultWTO getNIDCardPINs(EMKSDataWTO emksDataWTO,
                                            Long requestID) throws BaseException {

        String str = "";

        if (emksDataWTO == null)
            throw new ServiceException(BizExceptionCode.ESI_007,
                    BizExceptionCode.ESI_007_MSG);

        checkValidationOnemksData(emksDataWTO);
        str = createCardInfoXml(emksDataWTO);

        logger.info(str);
        emksLogger.info(str);
        IServiceEMKS emksService = getEMKSService();
        try {
            CardKeysAndPINs nidCardPINs = emksService.getNIDCardKeysAndPINs(str);
            EMKSDataResultWTO emksDataResultWTO = new EMKSDataResultWTO();
            emksDataResultWTO.setId(nidCardPINs.getID() != null ? nidCardPINs.getID().getValue().toString() : "");
            emksDataResultWTO.setSign(nidCardPINs.getSign() != null ? nidCardPINs.getSign().getValue().toString() : "");
            emksDataResultWTO.setSmPin(nidCardPINs.getPINSM() != null ? nidCardPINs.getPINSM().getValue().toString() : "");
            emksDataResultWTO.setSmd(nidCardPINs.getSMd() != null ? nidCardPINs.getSMd().getValue().toString() : "");
            emksDataResultWTO.setAsd(nidCardPINs.getASd() != null ? nidCardPINs.getASd().getValue().toString() : "");
            emksDataResultWTO.setMac(nidCardPINs.getMAC() != null ? nidCardPINs.getMAC().getValue().toString() : "");
            emksDataResultWTO.setEnc(nidCardPINs.getENC() != null ? nidCardPINs.getENC().getValue().toString() : "");
            if (emksDataWTO.getMocAvailable().equals("0")) {
                if (nidCardPINs.getNMoC() == null)
                    throw new BaseException("101",
                            "MocAvailable is '0' but NMoC equals null");

                emksDataResultWTO.setNmoc(nidCardPINs.getNMoC().getValue()
                        .toString());
            } else if (emksDataWTO.getMocAvailable().equals("1")) {
                if (nidCardPINs.getNMoC() != null)
                    throw new BaseException("101",
                            "MocAvailable is '0' but NMoC must be null");
            }
            logger.info("id : " + nidCardPINs.getID().getValue().toString()
                    + "\n");
            emksLogger.info("id : " + nidCardPINs.getID().getValue().toString()
                    + "\n");
            logger.info("sign : " + nidCardPINs.getSign().getValue().toString()
                    + "\n");
            emksLogger.info("sign : "
                    + nidCardPINs.getSign().getValue().toString() + "\n");
            logger.info("N_MOC : "
                    + (nidCardPINs.getNMoC() == null ? "" : nidCardPINs
                    .getNMoC().getValue().toString()) + "\n");
            emksLogger.info("N_MOC : "
                    + (nidCardPINs.getNMoC() == null ? "" : nidCardPINs
                    .getNMoC().getValue().toString()) + "\n");


            insertBusinessActionLog(requestID, str, BusinessLogAction.GET_PINS);
            return emksDataResultWTO;
        } catch (IServiceEMKSGetNIDCardKeysAndPINsEMKSExceptionFaultFaultMessage e) {
            handleEmksException(e);
            return null;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.ESI_008,
                    BizExceptionCode.GLB_008_MSG, e);
        }
    }

    private void insertBusinessActionLog(Long requestID, String str, BusinessLogAction action) throws BaseException {
        BusinessLogTO businessLogTO = new BusinessLogTO();
        businessLogTO.setEntityID(requestID.toString());
        businessLogTO.setAction(action);
        businessLogTO.setEntityName(BusinessLogEntity.REQUEST);
        businessLogTO.setActor("ccos");
        businessLogTO.setAdditionalData("depI:" + userProfileTO.getDepID() + ";username:" + userProfileTO.getUserName() + ";" + str);
        businessLogTO.setDate(new Timestamp(new Date().getTime()));
        getBusinessLogService().insertLog(businessLogTO);
    }

    private void handleEmksException(Exception e) throws ServiceException {
        EMKSException faultInfo;
        if (e instanceof IServiceEMKSGetNIDCardKeysAndPINsEMKSExceptionFaultFaultMessage) {
            faultInfo = ((IServiceEMKSGetNIDCardKeysAndPINsEMKSExceptionFaultFaultMessage) e).getFaultInfo();
        } else {
            faultInfo = ((IServiceEMKSGetNIDCardMoCKeysEMKSExceptionFaultFaultMessage) e).getFaultInfo();
        }
        logger.error(faultInfo.getErrorCode().getValue(), e.getMessage(), e);
        emksLogger.error(faultInfo.getErrorCode().getValue(),
                e.getMessage(), e);

        String errorMessage = e.getMessage();
        String errorCode = faultInfo.getErrorCode().getValue();
        if (EMKS_0001.equals(errorCode))
            throw new ServiceException(BizExceptionCode.ESI_001,
                    BizExceptionCode.ESI_001_MSG);
        else if (EMKS_0002.equals(errorCode))
            throw new ServiceException(BizExceptionCode.ESI_002,
                    BizExceptionCode.ESI_002_MSG);
        else if (EMKS_0003.equals(errorCode))
            throw new ServiceException(BizExceptionCode.ESI_003,
                    BizExceptionCode.ESI_003_MSG);
        else if (EMKS_0004.equals(errorCode))
            throw new ServiceException(BizExceptionCode.ESI_004,
                    BizExceptionCode.ESI_004_MSG);
        else if (EMKS_0099.equals(errorCode))
            throw new ServiceException(BizExceptionCode.ESI_005,
                    BizExceptionCode.ESI_005_MSG);
        else
            throw new ServiceException(BizExceptionCode.ESI_006,
                    BizExceptionCode.ESI_006_MSG);
    }

    private String createCardInfoXml(EMKSDataWTO emksDataWTO) {
        String str;
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>")
                .append("\n");
        strBuilder.append("<CardInfo>").append("\n");
        strBuilder.append("<CSN>").append(emksDataWTO.getCsn())
                .append("</CSN>").append("\n");
        strBuilder.append("<CRN>").append(emksDataWTO.getCrn())
                .append("</CRN>").append("\n");
        strBuilder.append("<CardProvider>").append(emksDataWTO.getCardProvider())
                .append("</CardProvider>").append("\n");
        strBuilder.append("<MoC_Available>")
                .append(emksDataWTO.getMocAvailable())
                .append("</MoC_Available>").append("\n");
        strBuilder.append("<SM_KeyVersion>")
                .append(emksDataWTO.getSmKeyVersion())
                .append("</SM_KeyVersion>").append("\n");
        strBuilder.append("<PIN_KeyVersion>")
                .append(emksDataWTO.getPinKeyVersion())
                .append("</PIN_KeyVersion>").append("\n");
        strBuilder.append("<SM_AlgorithmVersion>")
                .append(emksDataWTO.getSmAlgorithmVersion())
                .append("</SM_AlgorithmVersion>").append("\n");
        strBuilder.append("<PIN_AlgorithmVersion>")
                .append(emksDataWTO.getPinAlgorithmVersion())
                .append("</PIN_AlgorithmVersion>").append("\n");
        strBuilder.append("<MoC_AlgorithmVersion>")
                .append(emksDataWTO.getMocAlgorithmVersion())
                .append("</MoC_AlgorithmVersion>").append("\n");
        strBuilder.append("<SOD_KeyVersion>")
                .append(emksDataWTO.getSodKeyVersion())
                .append("</SOD_KeyVersion>").append("\n");
        strBuilder.append("<LDS_Version>").append(emksDataWTO.getLdsVersion())
                .append("</LDS_Version>").append("\n");
        strBuilder.append("<DP_Version>").append(emksDataWTO.getDpVersion())
                .append("</DP_Version>").append("\n");
        strBuilder.append("<MoC_KeyVersion>").append(emksDataWTO.getMocKeyVersion())
                .append("</MoC_KeyVersion>").append("\n");
        strBuilder.append("<BluePart>").append(emksDataWTO.getBluePart())
                .append("</BluePart>").append("\n");
        strBuilder.append("<DG8>").append(emksDataWTO.getDg8())
                .append("</DG8>").append("\n");
        strBuilder.append("<YellowPart>").append(emksDataWTO.getYellowPart())
                .append("</YellowPart>").append("\n");
        strBuilder.append("<YellowPart_Signature>")
                .append(emksDataWTO.getYellowPartSignature())
                .append("</YellowPart_Signature>").append("\n");
        if (emksDataWTO.getMocAvailable().equals("1")) {
            strBuilder.append("<MoC_HashData>")
                    .append(emksDataWTO.getMocHashData())
                    .append("</MoC_HashData>").append("\n");
            strBuilder.append("<AntiYes_PublicKey>")
                    .append(emksDataWTO.getAntiYesPublicKey())
                    .append("</AntiYes_PublicKey>").append("\n");
            strBuilder.append("<MoC_Signature>")
                    .append(emksDataWTO.getMocSignature())
                    .append("</MoC_Signature>").append("\n");
        }
        strBuilder.append("</CardInfo>");
        str = strBuilder.toString();
        return str;
    }

    private void checkValidationOnemksData(EMKSDataWTO emksDataWTO)
            throws BaseException {
        if (!EmsUtil.checkString(emksDataWTO.getCardProvider())) {
            throw new ServiceException(BizExceptionCode.ESI_033,
                    BizExceptionCode.ESI_033_MSG);
        }

        if (!EmsUtil.checkString(emksDataWTO.getSmAlgorithmVersion())) {
            throw new ServiceException(BizExceptionCode.ESI_036,
                    BizExceptionCode.ESI_036_MSG);
        }

        if (!EmsUtil.checkString(emksDataWTO.getSmKeyVersion())) {
            throw new ServiceException(BizExceptionCode.ESI_037,
                    BizExceptionCode.ESI_037_MSG);
        }

        if (!EmsUtil.checkString(emksDataWTO.getMocAvailable())) {
            throw new ServiceException(BizExceptionCode.ESI_010,
                    BizExceptionCode.ESI_010_MSG);
        }

        if (!emksDataWTO.getMocAvailable().equals("0")
                && !emksDataWTO.getMocAvailable().equals("1")) {
            logger.info("MocAvailable : " + emksDataWTO.getMocAvailable());
            emksLogger.info("MocAvailable : " + emksDataWTO.getMocAvailable());
            throw new ServiceException(BizExceptionCode.ESI_011,
                    BizExceptionCode.ESI_011_MSG);
        }
        if (!EmsUtil.checkString(emksDataWTO.getCsn())) {
            throw new ServiceException(BizExceptionCode.ESI_012,
                    BizExceptionCode.ESI_012_MSG);
        }
        if (!EmsUtil.checkString(emksDataWTO.getCrn())) {
            throw new ServiceException(BizExceptionCode.ESI_013,
                    BizExceptionCode.ESI_013_MSG);
        }
        if (!EmsUtil.checkString(emksDataWTO.getPinKeyVersion())) {
            throw new ServiceException(BizExceptionCode.ESI_014,
                    BizExceptionCode.ESI_014_MSG);
        }
        if (!EmsUtil.checkString(emksDataWTO.getPinAlgorithmVersion())) {
            throw new ServiceException(BizExceptionCode.ESI_015,
                    BizExceptionCode.ESI_015_MSG);
        }
        if (!EmsUtil.checkString(emksDataWTO.getSodKeyVersion())) {
            throw new ServiceException(BizExceptionCode.ESI_016,
                    BizExceptionCode.ESI_016_MSG);
        }
        if (!EmsUtil.checkString(emksDataWTO.getLdsVersion())) {
            throw new ServiceException(BizExceptionCode.ESI_017,
                    BizExceptionCode.ESI_017_MSG);
        }
        if (!EmsUtil.checkString(emksDataWTO.getDpVersion())) {
            throw new ServiceException(BizExceptionCode.ESI_018,
                    BizExceptionCode.ESI_018_MSG);
        }
        if (!EmsUtil.checkString(emksDataWTO.getBluePart())) {
            throw new ServiceException(BizExceptionCode.ESI_019,
                    BizExceptionCode.ESI_019_MSG);
        }
        if (!EmsUtil.checkString(emksDataWTO.getDg8())) {
            throw new ServiceException(BizExceptionCode.ESI_020,
                    BizExceptionCode.ESI_020_MSG);
        }
        if (!EmsUtil.checkString(emksDataWTO.getYellowPart())) {
            throw new ServiceException(BizExceptionCode.ESI_021,
                    BizExceptionCode.ESI_021_MSG);
        }
        if (!EmsUtil.checkString(emksDataWTO.getYellowPartSignature())) {
            throw new ServiceException(BizExceptionCode.ESI_022,
                    BizExceptionCode.ESI_022_MSG);
        }
        if (emksDataWTO.getMocAvailable().equals("1")) {
            if (!EmsUtil.checkString(emksDataWTO.getAntiYesPublicKey()))
                throw new ServiceException(BizExceptionCode.ESI_023,
                        BizExceptionCode.ESI_023_MSG);
            if (!EmsUtil.checkString(emksDataWTO.getMocHashData()))
                throw new ServiceException(BizExceptionCode.ESI_024,
                        BizExceptionCode.ESI_024_MSG);
            if (!EmsUtil.checkString(emksDataWTO.getMocSignature()))
                throw new ServiceException(BizExceptionCode.ESI_025,
                        BizExceptionCode.ESI_025_MSG);

        } else if (emksDataWTO.getMocAvailable().equals("0")) {

            if (EmsUtil.checkString(emksDataWTO.getAntiYesPublicKey()))
                throw new ServiceException(BizExceptionCode.ESI_026,
                        BizExceptionCode.ESI_026_MSG);
            if (EmsUtil.checkString(emksDataWTO.getMocHashData()))
                throw new ServiceException(BizExceptionCode.ESI_027,
                        BizExceptionCode.ESI_027_MSG);
            if (EmsUtil.checkString(emksDataWTO.getMocSignature()))
                throw new ServiceException(BizExceptionCode.ESI_028,
                        BizExceptionCode.ESI_028_MSG);
        }
    }

    private void checkValidationOnemksDataNIDCardMoCKeys(EMKSDataWTO emksDataWTO) throws BaseException {

        if (!EmsUtil.checkString(emksDataWTO.getCsn())) {
            throw new ServiceException(BizExceptionCode.ESI_012,
                    BizExceptionCode.ESI_012_MSG);
        }
        if (!EmsUtil.checkString(emksDataWTO.getCrn())) {
            throw new ServiceException(BizExceptionCode.ESI_013,
                    BizExceptionCode.ESI_013_MSG);
        }

        if (!EmsUtil.checkString(emksDataWTO.getCardProvider())) {
            throw new ServiceException(BizExceptionCode.ESI_033,
                    BizExceptionCode.ESI_033_MSG);
        }

        if (!EmsUtil.checkString(emksDataWTO.getMocAvailable())) {
            throw new ServiceException(BizExceptionCode.ESI_010,
                    BizExceptionCode.ESI_010_MSG);
        }

        if (!emksDataWTO.getMocAvailable().equals("0")
                && !emksDataWTO.getMocAvailable().equals("1")) {
            logger.info("MocAvailable : " + emksDataWTO.getMocAvailable());
            emksLogger.info("MocAvailable : " + emksDataWTO.getMocAvailable());
            throw new ServiceException(BizExceptionCode.ESI_011,
                    BizExceptionCode.ESI_011_MSG);
        }

        if (!EmsUtil.checkString(emksDataWTO.getMocAlgorithmVersion())) {
            throw new ServiceException(BizExceptionCode.ESI_034,
                    BizExceptionCode.ESI_034_MSG);
        }

        if (!EmsUtil.checkString(emksDataWTO.getLdsVersion())) {
            throw new ServiceException(BizExceptionCode.ESI_017,
                    BizExceptionCode.ESI_017_MSG);
        }

        if (!EmsUtil.checkString(emksDataWTO.getDpVersion())) {
            throw new ServiceException(BizExceptionCode.ESI_018,
                    BizExceptionCode.ESI_018_MSG);
        }

        if (!EmsUtil.checkString(emksDataWTO.getMocKeyVersion())) {
            throw new ServiceException(BizExceptionCode.ESI_035,
                    BizExceptionCode.ESI_035_MSG);
        }

        if (emksDataWTO.getMocAvailable().equals("1")) {
            if (!EmsUtil.checkString(emksDataWTO.getAntiYesPublicKey()))
                throw new ServiceException(BizExceptionCode.ESI_023,
                        BizExceptionCode.ESI_023_MSG);
            if (!EmsUtil.checkString(emksDataWTO.getMocHashData()))
                throw new ServiceException(BizExceptionCode.ESI_024,
                        BizExceptionCode.ESI_024_MSG);
            if (!EmsUtil.checkString(emksDataWTO.getMocSignature()))
                throw new ServiceException(BizExceptionCode.ESI_025,
                        BizExceptionCode.ESI_025_MSG);

        } else if (emksDataWTO.getMocAvailable().equals("0")) {

            if (EmsUtil.checkString(emksDataWTO.getAntiYesPublicKey()))
                throw new ServiceException(BizExceptionCode.ESI_026,
                        BizExceptionCode.ESI_026_MSG);
            if (EmsUtil.checkString(emksDataWTO.getMocHashData()))
                throw new ServiceException(BizExceptionCode.ESI_027,
                        BizExceptionCode.ESI_027_MSG);
            if (EmsUtil.checkString(emksDataWTO.getMocSignature()))
                throw new ServiceException(BizExceptionCode.ESI_028,
                        BizExceptionCode.ESI_028_MSG);
        }
    }

    @Override
    public String getSigniture(String str) throws BaseException {
        String signature = "";
        try {
            signature = getEMKSService().getSignature(str);
        } catch (IServiceEMKSGetSignatureEMKSExceptionFaultFaultMessage e) {
            EMKSException faultInfo = e.getFaultInfo();
            String errorCode = faultInfo.getErrorCode().getValue();
            if (EMKS_0005.equals(errorCode)) {
                throw new ServiceException(BizExceptionCode.ESI_032,
                        BizExceptionCode.ESI_031_MSG);
            } else if (EMKS_0099.equals(errorCode)) {
                throw new ServiceException(BizExceptionCode.ESI_005,
                        BizExceptionCode.ESI_005_MSG);
            } else {
                throw new ServiceException(BizExceptionCode.ESI_030,
                        BizExceptionCode.ESI_031_MSG);
            }
        } catch (BaseException e) {
            throw new ServiceException(BizExceptionCode.ESI_031,
                    BizExceptionCode.ESI_031_MSG);
        }
        return signature;

    }


}
