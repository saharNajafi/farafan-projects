package com.gam.nocr.ems.biz.service.external.impl.ims;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.AbstractService;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.FileTransferPort;
import com.gam.nocr.FileTransferPortPortClient;
import com.gam.nocr.ems.biz.service.BusinessLogService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.enums.BusinessLogActionAttitude;
import com.gam.nocr.ems.data.domain.BusinessLogTO;
import com.gam.nocr.ems.data.domain.vol.TransferInfoVTO;
import com.gam.nocr.ems.data.enums.BusinessLogAction;
import com.gam.nocr.ems.data.enums.BusinessLogEntity;
import com.gam.nocr.ems.util.EmsUtil;
import org.slf4j.Logger;
import web.info.TransferInfo;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@Stateless(name = "NOCRIMSBatchService")
@Local(NOCRIMSBatchServiceLocal.class)
@Remote(NOCRIMSBatchServiceRemote.class)
public class NOCRIMSBatchServiceImpl extends AbstractService implements NOCRIMSBatchServiceLocal, NOCRIMSBatchServiceRemote {

    private static final Logger logger = BaseLog.getLogger(NOCRIMSBatchServiceImpl.class);
    private static final Logger ImsLogger = BaseLog.getLogger("ImsLogger");

    public static final String DEFAULT_OFFLINE_USERNAME = "gam-sh-web";
    public static final String DEFAULT_OFFLINE_PASSWORD = "SH-ELW910308";
    public static final String DEFAULT_OFFLINE_ENDPOINT = "http://batchservice.nocrservices.org:80/nocrnin/FileTransfer?wsdl";

    /**
     * ===================
     * Getter for Services
     * ===================
     */

    /**
     * getService
     *
     * @return an instance of type {@link com.gam.nocr.FileTransferPort}
     * @throws BaseException if cannot get the service successfully
     */
    private FileTransferPort getService() throws BaseException {
        try {
            String wsdlUrl = EmsUtil.getProfileValue(ProfileKeyName.KEY_IMS_OFFLINE_ENDPOINT, DEFAULT_OFFLINE_ENDPOINT);
            FileTransferPortPortClient fileTransferPortPortClient = new FileTransferPortPortClient();
            FileTransferPort port = fileTransferPortPortClient.getPort();
            EmsUtil.setJAXRPCWebserviceProperties(port, wsdlUrl);
            return port;
        } catch (Exception e) {
            ImsLogger.error(BizExceptionCode.NIB_001 + " : " + e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.NIB_001, e.getMessage(), e);
        }

    }

    /**
     * getBusinessLogService
     *
     * @return an instance of type {@link BusinessLogService}
     * @throws BaseException if cannot get the service successfully
     */
    private BusinessLogService getBusinessLogService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider.getServiceFactory();
        BusinessLogService businessLogService;
        try {
            businessLogService = serviceFactory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_BUSINESS_LOG), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            ImsLogger.error(BizExceptionCode.NIB_003 + " : " + BizExceptionCode.GLB_002_MSG, e);
            throw new ServiceException(
                    BizExceptionCode.NIB_003,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_BUSINESS_LOG.split(","));
        }
        return businessLogService;
    }

    /**
     * @param logAction
     * @param logEntityName
     * @param logActor
     * @param additionalData
     * @param exceptionFlag
     * @throws BaseException
     */
    private void createBusinessLog(BusinessLogAction logAction,
                                   BusinessLogEntity logEntityName,
                                   String logActor,
                                   String additionalData,
                                   Boolean exceptionFlag) throws BaseException {
        BusinessLogTO businessLogTO = new BusinessLogTO();
        businessLogTO.setEntityID(" ");
        businessLogTO.setAction(logAction);
        businessLogTO.setEntityName(logEntityName);
        businessLogTO.setActor(logActor);
        businessLogTO.setAdditionalData(additionalData);
        businessLogTO.setDate(new Timestamp(new Date().getTime()));
        if (exceptionFlag) {
            businessLogTO.setActionAttitude(BusinessLogActionAttitude.F);
        } else {
            businessLogTO.setActionAttitude(BusinessLogActionAttitude.T);
        }
        getBusinessLogService().insertLog(businessLogTO);

    }

    /**
     * The method convertToTransferInfo is used to convert an instance of type {@link TransferInfoVTO} to an instance
     * of type {@link TransferInfo}
     *
     * @param transferInfoVTO is an instance of type {@link TransferInfoVTO}
     * @return an instance of type {@link TransferInfo}
     * @throws BaseException
     */
    private TransferInfo convertToTransferInfo(TransferInfoVTO transferInfoVTO) throws BaseException {
        try {
            TransferInfo transferInfo = new TransferInfo();
            transferInfo.setFilename(transferInfoVTO.getRequestId());
            transferInfo.setData(transferInfoVTO.getData());
            transferInfo.setIndex(0);
            return transferInfo;
        } catch (Exception e) {
            ImsLogger.error(BizExceptionCode.NIB_004 + " : " + BizExceptionCode.GLB_017_MSG, e);
            throw new ServiceException(BizExceptionCode.NIB_004, BizExceptionCode.GLB_017_MSG, e, new String[]{"TransferInfoVTO", "TransferInfo"});
        }
    }

    /**
     * The method convertToTransferInfoVTO is used to convert an instance of type {@link TransferInfo} to an instance
     * of type {@link TransferInfoVTO}
     *
     * @param transferInfo is an instance of type {@link TransferInfo}
     * @return an instance of type {@link TransferInfoVTO}
     * @throws BaseException
     */
    private TransferInfoVTO convertToTransferInfoVTO(TransferInfo transferInfo) throws BaseException {
        try {
            return new TransferInfoVTO(
                    transferInfo.getFilename(),
                    transferInfo.getIndex(),
                    transferInfo.getErrMsg(),
                    transferInfo.getData());
        } catch (Exception e) {
            ImsLogger.error(BizExceptionCode.NIB_011 + " : " + BizExceptionCode.GLB_017_MSG, e);
            throw new ServiceException(BizExceptionCode.NIB_011, BizExceptionCode.GLB_017_MSG, e, new String[]{"TransferInfo", "TransferInfoVTO"});
        }
    }

    /**
     * The method getOfflineParametersByProfileManager is used to prepare the parameters which are fetched from config
     * for offline services
     *
     * @return an array of type {@link String} which carries the required parameters
     */
    private String[] getOfflineParametersByProfileManager(String methodName) {
        String username = null;
        String password = null;
        ProfileManager pm = null;
        try {
            pm = ProfileHelper.getProfileManager();
            username = (String) pm.getProfile(ProfileKeyName.KEY_IMS_OFFLINE_USERNAME, true, null, null);
            password = (String) pm.getProfile(ProfileKeyName.KEY_IMS_OFFLINE_PASSWORD, true, null, null);
        } catch (Exception e) {
            if (pm == null) {
                logger.warn(BizExceptionCode.NIB_005, BizExceptionCode.GLB_011_MSG);
                ImsLogger.warn(BizExceptionCode.NIB_005, BizExceptionCode.GLB_011_MSG);
            }
        }

        if (username == null) {
            logger.warn(BizExceptionCode.NIB_006, BizExceptionCode.GLB_016_MSG, new String[]{"username", methodName, DEFAULT_OFFLINE_USERNAME});
            ImsLogger.warn(BizExceptionCode.NIB_006, BizExceptionCode.GLB_016_MSG, new String[]{"username", methodName, DEFAULT_OFFLINE_USERNAME});
            username = DEFAULT_OFFLINE_USERNAME;
        }

        if (password == null) {
            logger.warn(BizExceptionCode.NIB_007, BizExceptionCode.GLB_016_MSG, new String[]{"password", methodName, DEFAULT_OFFLINE_PASSWORD});
            ImsLogger.warn(BizExceptionCode.NIB_007, BizExceptionCode.GLB_016_MSG, new String[]{"password", methodName, DEFAULT_OFFLINE_PASSWORD});
            password = DEFAULT_OFFLINE_PASSWORD;
        }

        return new String[]{username, password};
    }

    /**
     * The method sendBatchEnquiryRequest is used to send a request toward IMS for fulfilling batch enquiry
     *
     * @param transferInfoVTO is an instance of type {@link com.gam.nocr.ems.data.domain.vol.TransferInfoVTO}, which carries the required information for batch enquiry
     * @return an instance of type {@link String}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public String sendBatchEnquiryRequest(TransferInfoVTO transferInfoVTO) throws BaseException {
        String returnValue;
        String username;
        String password;
        TransferInfo transferInfo;

        logger.info("\n============================== IMS BATCH ENQUIRY REQUEST ================================\n");
        ImsLogger.info("\n============================== IMS BATCH ENQUIRY REQUEST ================================\n");
        logger.info("\nIMS BATCH ENQUIRY REQUEST FILE_NAME : " + transferInfoVTO.getRequestId());
        ImsLogger.info("\nIMS BATCH ENQUIRY REQUEST FILE_NAME : " + transferInfoVTO.getRequestId());
        String additionalData;
        try {
            additionalData = new String(transferInfoVTO.getData(), "UTF-8");
            logger.info("\nIMS BATCH ENQUIRY REQUEST DATA : \n" + additionalData + "\n");
            ImsLogger.info("\nIMS BATCH ENQUIRY REQUEST DATA : \n" + additionalData + "\n");
        } catch (UnsupportedEncodingException e) {
            additionalData = e.getMessage();
            logger.error("\n" + BizExceptionCode.NIB_002, e.getMessage(), e);
            ImsLogger.error("\n" + BizExceptionCode.NIB_002, e.getMessage(), e);
        }
        createBusinessLog(
                BusinessLogAction.SEND_REQUEST_FOR_OFFLINE_ENQUIRY,
                BusinessLogEntity.IMS,
                "System",
                additionalData,
                true);
        logger.info("\n=========================================================================================\n");
        ImsLogger.info("\n=========================================================================================\n");


        transferInfo = convertToTransferInfo(transferInfoVTO);

        String[] strParameters;
        try {
            strParameters = getOfflineParametersByProfileManager(NOCRIMSBatchServiceImpl.class.getMethod("sendBatchEnquiryRequest", TransferInfoVTO.class).toString());
        } catch (Exception e) {
            ImsLogger.error(BizExceptionCode.NIB_008 + " : " + e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.NIB_008, e.getMessage(), e, EMSLogicalNames.SRV_NIB.split(","));
        }
        username = strParameters[0];
        password = strParameters[1];
        try {
            returnValue = getService().upload(username, password, transferInfo);
            if (EmsUtil.checkString(transferInfo.getErrMsg())) {
                throw new ServiceException(BizExceptionCode.NIB_015, transferInfo.getErrMsg());
            }
        } catch (BaseException e) {
            throw e;
        } catch (RemoteException e) {
            ServiceException serviceException = new ServiceException(BizExceptionCode.NIB_009, e.getMessage(), e, EMSLogicalNames.SRV_NIB.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_NIB.split(","));
            ImsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_NIB.split(","));
            throw serviceException;
        } catch (Exception e) {
            ServiceException serviceException = new ServiceException(BizExceptionCode.NIB_010, e.getMessage(), e, EMSLogicalNames.SRV_NIB.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_NIB.split(","));
            ImsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_NIB.split(","));
            throw serviceException;
        }
        return returnValue;
    }

    /**
     * The method getBatchEnquiryResponse is used to receive the response of the batch enquiring request from IMS sub
     * system.
     *
     * @return an object of type {@link com.gam.nocr.ems.data.domain.vol.TransferInfoVTO} which carries the result of batch enquiry
     */
    @Override
    public TransferInfoVTO getBatchEnquiryResponse() throws BaseException {
        TransferInfoVTO transferInfoVTO = null;
        String[] strParameters;
        try {
            strParameters = getOfflineParametersByProfileManager(NOCRIMSBatchServiceImpl.class.getMethod("getBatchEnquiryResponse").toString());
        } catch (Exception e) {
            ImsLogger.error(BizExceptionCode.NIB_012 + " : " + e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.NIB_012, e.getMessage(), e, EMSLogicalNames.SRV_NIB.split(","));
        }
        String username = strParameters[0];
        String password = strParameters[1];

        String additionalData = null;
        try {
            logger.info("\nIdentifierLog just before calling the method 'download'.");
            ImsLogger.info("\nIdentifierLog just before calling the method 'download'.");
            TransferInfo transferInfo = getService().download(username, password);
            logger.info("\nIdentifierLog just after calling the method 'download'.");
            ImsLogger.info("\nIdentifierLog just after calling the method 'download'.");
            if (transferInfo != null) {
                logger.info("\n============================== IMS BATCH ENQUIRY RESULT =================================");
                ImsLogger.info("\n============================== IMS BATCH ENQUIRY RESULT =================================");
                logger.info("\nIMS BATCH ENQUIRY RESULT FILE_NAME : " + transferInfo.getFilename());
                ImsLogger.info("\nIMS BATCH ENQUIRY RESULT FILE_NAME : " + transferInfo.getFilename());
                logger.info("\nIMS BATCH ENQUIRY RESULT ERROR_MESSAGE : " + transferInfo.getErrMsg());
                ImsLogger.info("\nIMS BATCH ENQUIRY RESULT ERROR_MESSAGE : " + transferInfo.getErrMsg());
                String strData;
                byte[] data = transferInfo.getData();
                if (data == null) {
                    strData = "The byte array data received from IMS sub system was null";
                } else if (data.length == 0) {
                    strData = "The byte array data received from IMS sub system was an array with zero length";
                } else {
                    strData = new String(data, "UTF-8");
                    transferInfoVTO = convertToTransferInfoVTO(transferInfo);
                }

                logger.info("\nIMS BATCH ENQUIRY RESULT DATA : " + strData);
                ImsLogger.info("\nIMS BATCH ENQUIRY RESULT DATA : " + strData);

                additionalData = strData;
                if (EmsUtil.checkString(transferInfo.getErrMsg())) {
                    additionalData += " " + transferInfo.getErrMsg();
                }
                logger.info("\n====================================================================================");
                ImsLogger.info("\n====================================================================================");
            }

        } catch (BaseException e) {
            additionalData = e.getMessage();
            logger.error(e.getExceptionCode(), e.getMessage(), e);
            ImsLogger.error(e.getExceptionCode(), e.getMessage(), e);
//            throw e;

        } catch (RemoteException e) {
            additionalData = e.getMessage();
            ServiceException serviceException = new ServiceException(BizExceptionCode.NIB_013, e.getMessage(), e, EMSLogicalNames.SRV_NIB.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_NIB.split(","));
            ImsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_NIB.split(","));
//            throw serviceException;

        } catch (Exception e) {
            additionalData = e.getMessage();
            ServiceException serviceException = new ServiceException(BizExceptionCode.NIB_014, e.getMessage(), e, EMSLogicalNames.SRV_NIB.split(","));
            logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_NIB.split(","));
            ImsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_NIB.split(","));
//            throw serviceException;
        }

        createBusinessLog(
                BusinessLogAction.RECEIVE_REQUEST_FROM_OFFLINE_ENQUIRY,
                BusinessLogEntity.IMS,
                "System",
                additionalData,
                true);


        return transferInfoVTO;
    }
}
