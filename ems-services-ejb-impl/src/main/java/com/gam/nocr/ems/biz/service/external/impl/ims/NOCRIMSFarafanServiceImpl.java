package com.gam.nocr.ems.biz.service.external.impl.ims;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.ValidationException;
import com.gam.commons.core.biz.service.AbstractService;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactory;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.RegistrationService;
import com.gam.nocr.ems.biz.service.ims.IMSOnlineService;
import com.gam.nocr.ems.config.*;
import com.gam.nocr.ems.data.dao.CardRequestHistoryDAO;
import com.gam.nocr.ems.data.dao.XmlAfisDAO;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.vol.*;
import com.gam.nocr.ems.data.enums.AFISState;
import com.gam.nocr.ems.data.enums.CardRequestHistoryAction;
import com.gam.nocr.ems.data.enums.CardState;
import com.gam.nocr.ems.data.enums.SystemId;
import com.gam.nocr.ems.data.mapper.xmlmapper.XMLMapperProvider;
import com.gam.nocr.ems.util.EmsUtil;
import est.*;
import gampooya.tools.date.DateFormatException;
import gampooya.tools.date.DateUtil;
import gampooya.tools.util.Base64;
import org.slf4j.Logger;
import servicePortUtil.ServicePorts;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttributeType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.Exception;
import java.net.URL;
import java.util.*;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "NOCRIMSFarafanService")
@Local(NOCRIMSFarafanServiceLocal.class)
@Remote(NOCRIMSFarafanServiceRemote.class)
public class NOCRIMSFarafanServiceImpl extends AbstractService implements NOCRIMSFarafanServiceLocal, NOCRIMSFarafanServiceRemote {

    private static final Logger logger = BaseLog.getLogger(NOCRIMSFarafanServiceImpl.class);
    private static final Logger ImsLogger = BaseLog.getLogger("ImsLogger");
    private static final Logger threadLocalLogger = BaseLog.getLogger("threadLocal");

    private static final String DEFAULT_USERNAME = "demo";
    private static final String DEFAULT_PASSWORD = "demo";

    //    TODO : correct the endpoint and namespace default values
    private static final String DEFAULT_ENDPOINT = "http://webservice.nocrservices.org:80/ims-service/services/ImsService/?wsdl";
    private static final String DEFAULT_NAMESPACE = "http://ws.matiran.gam.com/";
    private static final String DEFAULT_SAVE_XMLAFIS = "false";

    /**
     * ===================
     * Getter for Services
     * ===================
     */

    /**
     * getService
     *
     * @return an instance of type {@link com.gam.nocr.ems.biz.service.external.client.ims.ImsServiceServicePort}
     * @throws BaseException if cannot get the service
     */
//    private ImsServiceServicePort getService() throws BaseException {
//        try {
//            ProfileManager pm = ProfileHelper.getProfileManager();
//            String wsdlUrl = (String) pm.getProfile(ProfileKeyName.KEY_IMS_NEW_SERVICES_ENDPOINT, true, null, null);
//            String namespace = (String) pm.getProfile(ProfileKeyName.KEY_IMS_NEW_SERVICES_NAMESPACE, true, null, null);
//            if (wsdlUrl == null) {
//                wsdlUrl = DEFAULT_ENDPOINT;
//            }
//            if (namespace == null) {
//                namespace = DEFAULT_NAMESPACE;
//            }
//            logger.debug("=======================================================================================");
//            ImsLogger.debug("=======================================================================================");
//            logger.debug("=============================== IMS_NEW_SERVICES_WSDL =================================");
//            ImsLogger.debug("=============================== IMS_NEW_SERVICES_WSDL =================================");
//            logger.debug("IMS new services wsdl url: " + wsdlUrl);
//            ImsLogger.debug("IMS new services wsdl url: " + wsdlUrl);
//            logger.debug("=======================================================================================");
//            logger.debug("=======================================================================================");
//            ImsLogger.debug("=======================================================================================");
//            ImsLogger.debug("=======================================================================================");
//            ImsServiceServicePort port = new ImsServiceService_Impl(wsdlUrl).getImsServiceServicePort();
////            ImsServiceServicePort port = new ImsServiceService_Impl().getImsServiceServicePort();
//            EmsUtil.setJAXRPCWebserviceProperties(port, wsdlUrl);
//            return port;
//        } catch (Exception e) {
//            ImsLogger.error(BizExceptionCode.NIF_001 + " : " + e.getMessage(), e);
//            throw new ServiceException(BizExceptionCode.NIF_001, e.getMessage(), e);
//        }
//    }
    private XmlAfisDAO getXmlAfisDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_XML_AFIS));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.RMS_006, BizExceptionCode.GLB_001_MSG, e);
        }
    }


    private ImsService getService() throws BaseException {
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            String wsdlUrl = (String) pm.getProfile(ProfileKeyName.KEY_IMS_NEW_SERVICES_ENDPOINT, true, null, null);
            String namespace = (String) pm.getProfile(ProfileKeyName.KEY_IMS_NEW_SERVICES_NAMESPACE, true, null, null);
            if (wsdlUrl == null) {
                wsdlUrl = DEFAULT_ENDPOINT;
            }
            if (namespace == null) {
                namespace = DEFAULT_NAMESPACE;
            }
            String serviceName = "ImsServiceService";
            logger.debug("=======================================================================================");
            ImsLogger.debug("=======================================================================================");
            logger.debug("=============================== IMS_NEW_SERVICES_WSDL =================================");
            ImsLogger.debug("=============================== IMS_NEW_SERVICES_WSDL =================================");
            logger.debug("IMS new services wsdl url: " + wsdlUrl);
            ImsLogger.debug("IMS new services wsdl url: " + wsdlUrl);
            logger.debug("=======================================================================================");
            logger.debug("=======================================================================================");
            ImsLogger.debug("=======================================================================================");
            ImsLogger.debug("=======================================================================================");
            //Commented for ThraedLocal
            //ImsService port = new ImsServiceService(new URL(wsdlUrl), new QName(namespace, serviceName)).getImsServiceServicePort();
            ImsService port = ServicePorts.getImsPort();
            if (port == null) {
                threadLocalLogger.debug("**************************** new webServicePort in AFIS getService()");
                port = new ImsServiceService(new URL(wsdlUrl), new QName(
                        namespace, serviceName)).getImsServiceServicePort();
                ServicePorts.setImsPort(port);
            } else {
                threadLocalLogger.debug("***************************** using webServicePort(AFIS) from ThradLocal");
            }
            EmsUtil.setJAXWSWebserviceProperties(port, wsdlUrl);
            return port;
        } catch (Exception e) {
            ImsLogger.error(BizExceptionCode.NIF_001 + " : " + e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.NIF_001, e.getMessage(), e);
        }
    }


    /**
     * getIMSOnlineService
     *
     * @return an instance of type {@link IMSOnlineService}
     * @throws BaseException if cannot get the service successfully
     */
    private NOCRIMSOnlineService getIMSOnlineService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider.getServiceFactory();
        NOCRIMSOnlineService nocrImsOnlineService;
        try {
            nocrImsOnlineService = serviceFactory.getService(EMSLogicalNames.getExternalIMSServiceJNDIName(EMSLogicalNames.SRV_NIO), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            ImsLogger.error(BizExceptionCode.NIF_016 + " : " + BizExceptionCode.GLB_002_MSG, e);
            throw new ServiceException(
                    BizExceptionCode.NIF_016,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_NIO.split(","));
        }
        return nocrImsOnlineService;
    }

    private CardRequestHistoryDAO getCardRequestHistoryDAO()
            throws BaseException {
        DAOFactory daoFactory = DAOFactoryProvider.getDAOFactory();
        CardRequestHistoryDAO cardRequestHistoryDAO;
        try {
            cardRequestHistoryDAO = daoFactory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST_HISTORY));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.MMS_002,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_CARD_REQUEST_HISTORY.split(","));
        }
        return cardRequestHistoryDAO;
    }

    /**
     * getRegistrationService
     *
     * @return an instance of type {@link RegistrationService}
     * @throws BaseException if cannot get the service successfully
     */
    private RegistrationService getRegistrationService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider.getServiceFactory();
        RegistrationService registrationService;
        try {
            registrationService = serviceFactory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_REGISTRATION), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            ImsLogger.error(BizExceptionCode.NIF_017 + " : " + BizExceptionCode.GLB_002_MSG, e);
            throw new ServiceException(
                    BizExceptionCode.NIF_017,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_REGISTRATION.split(","));
        }
        return registrationService;
    }

    /**
     * The method convertToTransferInfoVTO is used to convert an instance of type {@link server.business.TransferInfo} to an instance of type {@link TransferInfoVTO}
     *
     * @param transferInfo is an instance of type {@link server.business.TransferInfo}
     * @return an instance of type {@link TransferInfoVTO}
     * @throws BaseException
     */
    private TransferInfoVTO convertToTransferInfoVTO(TransferInfo transferInfo) throws BaseException {
        try {
            return new TransferInfoVTO(
                    transferInfo.getErrMessage(),
                    transferInfo.getData());
        } catch (Exception e) {
            ImsLogger.error(BizExceptionCode.NIF_002 + " : " + BizExceptionCode.NIF_002_MSG, e);
            throw new ServiceException(BizExceptionCode.NIF_002, BizExceptionCode.NIF_002_MSG, e, new String[]{"TransferInfo", "TransferInfoVTO"});
        }
    }

    /**
     * The method convertToTransferInfo is used to convert an instance of type  {@link TransferInfoVTO}to an instance of type {@link server.business.TransferInfo}
     *
     * @param transferInfoVTO is an instance of type {@link TransferInfoVTO}
     * @return an instance of type {@link server.business.TransferInfo}
     * @throws BaseException
     */
    private TransferInfo convertToTransferInfo(TransferInfoVTO transferInfoVTO) throws BaseException {
        try {
            TransferInfo transferInfo = new TransferInfo();

            transferInfo.setData(transferInfoVTO.getData());

            return transferInfo;
        } catch (Exception e) {
            ImsLogger.error(BizExceptionCode.NIF_003 + " : " + BizExceptionCode.NIF_002_MSG, e);
            throw new ServiceException(BizExceptionCode.NIF_003, BizExceptionCode.NIF_002_MSG, e, new String[]{"TransferInfoVTO", "TransferInfo"});
        }
    }

    /**
     * The method getParametersByProfileManager is used to prepare the parameters which are going to be fetched from config
     *
     * @return an array of type {@link String} which carries the required parameters
     */
    private String[] getParametersByProfileManager() throws BaseException {
        String username = null;
        String password = null;
        ProfileManager pm = null;
        String[] strArray;
        try {
            pm = ProfileHelper.getProfileManager();
            username = (String) pm.getProfile(ProfileKeyName.KEY_IMS_NEW_SERVICES_USERNAME, true, null, null);
            password = (String) pm.getProfile(ProfileKeyName.KEY_IMS_NEW_SERVICES_PASSWORD, true, null, null);
        } catch (Exception e) {
            if (pm == null) {
                logger.warn(BizExceptionCode.NIF_004, BizExceptionCode.GLB_011_MSG);
                ImsLogger.warn(BizExceptionCode.NIF_004, BizExceptionCode.GLB_011_MSG);
            }
        }

        if (username == null) {
            strArray = new String[]{"username", "updateCitizensInfo", DEFAULT_USERNAME};
            logger.warn(BizExceptionCode.NIF_005, BizExceptionCode.GLB_016_MSG, strArray);
            ImsLogger.warn(BizExceptionCode.NIF_005, BizExceptionCode.GLB_016_MSG, strArray);
            username = DEFAULT_USERNAME;
        }

        if (password == null) {
            strArray = new String[]{"password", "updateCitizensInfo", DEFAULT_PASSWORD};
            logger.warn(BizExceptionCode.NIF_006, BizExceptionCode.GLB_016_MSG, strArray);
            ImsLogger.warn(BizExceptionCode.NIF_006, BizExceptionCode.GLB_016_MSG, strArray);
            password = DEFAULT_PASSWORD;
        }
        return new String[]{username, password};
    }

    /**
     * The method setDefaultValues is used to set the default values for necessary fields
     *
     * @param citizenTO is an instance of type {@link CitizenTO}
     * @throws BaseException
     */
    private void setDefaultValues(CitizenTO citizenTO) throws BaseException {
        try {
            CitizenInfoTO czi = citizenTO.getCitizenInfo();
            logger.info("\nThe default values for the citizen with the nationalId of '" + citizenTO.getNationalID() + "' is : ");

            /**
             * Default values for citizen
             */
            czi.setFirstNameEnglish(ConstValues.DEFAULT_NAMES_EN);
            logger.info("\nFirstNameEN : " + czi.getFirstNameEnglish());
            ImsLogger.info("\nFirstNameEN : " + czi.getFirstNameEnglish());

            czi.setSurnameEnglish(ConstValues.DEFAULT_NAMES_EN);
            logger.info("\nSurNameEN : " + czi.getSurnameEnglish());
            ImsLogger.info("\nSurNameEN : " + czi.getSurnameEnglish());

            czi.setBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
            logger.info("\nBirthCertificateSeries : " + czi.getBirthCertificateSeries());
            ImsLogger.info("\nBirthCertificateSeries : " + czi.getBirthCertificateSeries());

            if (czi.getBirthDateGregorian() == null) {
                czi.setBirthDateGregorian(DateUtil.convert(ConstValues.DEFAULT_DATE, DateUtil.GREGORIAN));
                logger.info("\nGregorianBirthDate : " + czi.getBirthDateGregorian());
                ImsLogger.info("\nGregorianBirthDate : " + czi.getBirthDateGregorian());
            }

            if (czi.getBirthDateSolar() == null) {
                czi.setBirthDateSolar(DateUtil.convert(czi.getBirthDateGregorian(), DateUtil.JALALI));
                logger.info("\nSolarBirthDate : " + czi.getBirthDateLunar());
                ImsLogger.info("\nSolarBirthDate : " + czi.getBirthDateLunar());
            }

            if (czi.getBirthDateLunar() == null) {
                czi.setBirthDateLunar(DateUtil.convert(czi.getBirthDateGregorian(), DateUtil.HIJRI));
                logger.info("\nLunarBirthDate : " + czi.getBirthDateLunar());
                ImsLogger.info("\nLunarBirthDate : " + czi.getBirthDateLunar());
            }

            if (czi.getBirthCertificateIssuancePlace() == null) {
                List<BirthCertIssPlaceVTO> birthCertIssPlaceVTOs = null;
                try {
                    birthCertIssPlaceVTOs = getRegistrationService().fetchBirthCertIssPlace(citizenTO.getNationalID());
                } catch (Exception e) {
                    logger.error(BizExceptionCode.NIF_018, BizExceptionCode.NIF_018_MSG);
                    ImsLogger.error(BizExceptionCode.NIF_018, BizExceptionCode.NIF_018_MSG);
                    throw new ServiceException(BizExceptionCode.NIF_018, BizExceptionCode.NIF_018_MSG);
                }
                if (EmsUtil.checkListSize(birthCertIssPlaceVTOs)) {
                    czi.setBirthCertificateIssuancePlace(birthCertIssPlaceVTOs.get(0).getDepName());
                    logger.info("\nBirthCertificateIssuancePlace : " + czi.getBirthCertificateIssuancePlace());
                    ImsLogger.info("\nBirthCertificateIssuancePlace : " + czi.getBirthCertificateIssuancePlace());
                }
            }

            /**
             * Default values for father
             */
            if (!EmsUtil.checkString(czi.getFatherNationalID())) {
                czi.setFatherNationalID(ConstValues.DEFAULT_NID);
                logger.info("\nFatherNationalId : " + czi.getFatherNationalID());
                ImsLogger.info("\nFatherNationalId : " + czi.getFatherNationalID());
            }

            if (!EmsUtil.checkString(czi.getFatherFirstNamePersian())) {
                czi.setFatherFirstNamePersian(ConstValues.DEFAULT_NAMES_FA);
                logger.info("\nFatherFirstName : " + czi.getFatherFirstNamePersian());
                ImsLogger.info("\nFatherFirstName : " + czi.getFatherFirstNamePersian());
            }

            czi.setFatherFirstNameEnglish(ConstValues.DEFAULT_NAMES_EN);
            logger.info("\nFatherFirstNameEN : " + czi.getFatherFirstNameEnglish());
            ImsLogger.info("\nFatherFirstNameEN : " + czi.getFatherFirstNameEnglish());

            if (!EmsUtil.checkString(czi.getFatherSurname())) {
                czi.setFatherSurname(ConstValues.DEFAULT_NAMES_FA);
                logger.info("\nFatherSurName : " + czi.getFatherSurname());
                ImsLogger.info("\nFatherSurName : " + czi.getFatherSurname());
            }

            if (!EmsUtil.checkString(czi.getFatherBirthCertificateId())) {
                czi.setFatherBirthCertificateId(ConstValues.DEFAULT_NUMBER);
                logger.info("\nFatherBirthCertId : " + czi.getFatherBirthCertificateId());
                ImsLogger.info("\nFatherBirthCertId : " + czi.getFatherBirthCertificateId());
            }

//            if (!EmsUtil.checkString(czi.getFatherBirthCertificateSeries())) {
            czi.setFatherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
            logger.info("\nFatherBirthCertificateSeries : " + czi.getFatherBirthCertificateSeries());
            ImsLogger.info("\nFatherBirthCertificateSeries : " + czi.getFatherBirthCertificateSeries());
//            }

            if (!EmsUtil.checkString(czi.getFatherFatherName())) {
                czi.setFatherFatherName(ConstValues.DEFAULT_NAMES_FA);
                logger.info("\nFatherFatherName : " + czi.getFatherFatherName());
                ImsLogger.info("\nFatherFatherName : " + czi.getFatherFatherName());
            }

            if (czi.getFatherBirthDateSolar() == null) {
                czi.setFatherBirthDateSolar(DateUtil.convert(ConstValues.DEFAULT_DATE, DateUtil.GREGORIAN));
                logger.info("\nFatherBirthDateSolar : " + czi.getFatherBirthDateSolar());
                ImsLogger.info("\nFatherBirthDateSolar : " + czi.getFatherBirthDateSolar());

            }

            /**
             * Default values for mother
             */
            if (!EmsUtil.checkString(czi.getMotherNationalID())) {
                czi.setMotherNationalID(ConstValues.DEFAULT_NID);
                logger.info("\nMotherNationalId : " + czi.getMotherNationalID());
                ImsLogger.info("\nMotherNationalId : " + czi.getMotherNationalID());
            }

            if (!EmsUtil.checkString(czi.getMotherFirstNamePersian())) {
                czi.setMotherFirstNamePersian(ConstValues.DEFAULT_NAMES_FA);
                logger.info("\nMotherFirstName : " + czi.getMotherFirstNamePersian());
                ImsLogger.info("\nMotherFirstName : " + czi.getMotherFirstNamePersian());
            }

            if (!EmsUtil.checkString(czi.getMotherSurname())) {
                czi.setMotherSurname(ConstValues.DEFAULT_NAMES_FA);
                logger.info("\nMotherSurName : " + czi.getMotherSurname());
                ImsLogger.info("\nMotherSurName : " + czi.getMotherSurname());
            }

            if (!EmsUtil.checkString(czi.getMotherBirthCertificateId())) {
                czi.setMotherBirthCertificateId(ConstValues.DEFAULT_NUMBER);
                logger.info("\nMotherBirthCertId : " + czi.getMotherBirthCertificateId());
                ImsLogger.info("\nMotherBirthCertId : " + czi.getMotherBirthCertificateId());
            }

//            if (!EmsUtil.checkString(czi.getMotherBirthCertificateSeries())) {
            czi.setMotherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
            logger.info("\nMotherBirthCertificateSeries : " + czi.getMotherBirthCertificateSeries());
            ImsLogger.info("\nMotherBirthCertificateSeries : " + czi.getMotherBirthCertificateSeries());
//            }

            if (!EmsUtil.checkString(czi.getMotherFatherName())) {
                czi.setMotherFatherName(ConstValues.DEFAULT_NAMES_FA);
                logger.info("\nMotherFatherName : " + czi.getMotherFatherName());
                ImsLogger.info("\nMotherFatherName : " + czi.getMotherFatherName());
            }

            if (czi.getMotherBirthDateSolar() == null) {
                czi.setMotherBirthDateSolar(DateUtil.convert(ConstValues.DEFAULT_DATE, DateUtil.GREGORIAN));
                logger.info("\nMotherBirthDateSolar : " + czi.getMotherBirthDateSolar());
                ImsLogger.info("\nMotherBirthDateSolar : " + czi.getMotherBirthDateSolar());
            }

            /**
             * Default values for spouses
             */
            List<SpouseTO> spouseTOs = czi.getSpouses();
            if (EmsUtil.checkListSize(spouseTOs)) {
                int i = 0;
                for (SpouseTO spouseTO : spouseTOs) {
                    i++;
                    if (ConstValues.DEFAULT_NAMES_FA.equals(spouseTO.getSpouseFirstNamePersian())) {
                        logger.info("\nDefault values for the spouse[ " + i + " ]");
                        ImsLogger.info("\nSpouseNationalId : " + spouseTO.getSpouseNationalID());
//                    if (!EmsUtil.checkString(spouseTO.getSpouseNationalID())) {
                        spouseTO.setSpouseNationalID(ConstValues.DEFAULT_NID);
                        logger.info("\nSpouseNationalId : " + spouseTO.getSpouseNationalID());
                        ImsLogger.info("\nSpouseNationalId : " + spouseTO.getSpouseNationalID());
//                    }
//                    if (!EmsUtil.checkString(spouseTO.getSpouseFirstNamePersian())) {
                        spouseTO.setSpouseFirstNamePersian(ConstValues.DEFAULT_NAMES_FA);
                        logger.info("\nSpouseFirstNamePersian : " + spouseTO.getSpouseFirstNamePersian());
                        ImsLogger.info("\nSpouseFirstNamePersian : " + spouseTO.getSpouseFirstNamePersian());
//                    }
//                    if (!EmsUtil.checkString(spouseTO.getSpouseSurnamePersian())) {
                        spouseTO.setSpouseSurnamePersian(ConstValues.DEFAULT_NAMES_FA);
                        logger.info("\nSpouseSurnamePersian : " + spouseTO.getSpouseSurnamePersian());
                        ImsLogger.info("\nSpouseSurnamePersian : " + spouseTO.getSpouseSurnamePersian());
//                    }
//                    if (spouseTO.getSpouseBirthCertificateId() == null) {
                        spouseTO.setSpouseBirthCertificateId(ConstValues.DEFAULT_NUMBER);
                        logger.info("\nSpouseBirthCertificateId : " + spouseTO.getSpouseBirthCertificateId());
                        ImsLogger.info("\nSpouseBirthCertificateId : " + spouseTO.getSpouseBirthCertificateId());
//                    }
//                    if (spouseTO.getSpouseBirthCertificateSeries() == null) {
                        spouseTO.setSpouseBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                        logger.info("\nSpouseBirthCertificateSeries : " + spouseTO.getSpouseBirthCertificateSeries());
                        ImsLogger.info("\nSpouseBirthCertificateSeries : " + spouseTO.getSpouseBirthCertificateSeries());
//                    }
//                    if (!EmsUtil.checkString(spouseTO.getSpouseFatherName())) {
                        spouseTO.setSpouseFatherName(ConstValues.DEFAULT_NAMES_FA);
                        logger.info("\nSpouseFatherName : " + spouseTO.getSpouseFatherName());
                        ImsLogger.info("\nSpouseFatherName : " + spouseTO.getSpouseFatherName());
//                    }
//                    if (spouseTO.getSpouseBirthDate() == null) {
                        spouseTO.setSpouseBirthDate(DateUtil.convert(ConstValues.DEFAULT_DATE, DateUtil.GREGORIAN));
                        logger.info("\nSpouseBirthDate : " + spouseTO.getSpouseBirthDate());
                        ImsLogger.info("\nSpouseBirthDate : " + spouseTO.getSpouseBirthDate());
//                    }
                    }
                }
            }

            /**
             * Default values for children
             */
            List<ChildTO> children = czi.getChildren();
            if (EmsUtil.checkListSize(children)) {
                int i = 0;
                for (ChildTO child : children) {
                    i++;
                    if (ConstValues.DEFAULT_NAMES_FA.equals(child.getChildFirstNamePersian())) {
                        logger.info("\nDefault values for the spouse[ " + i + " ]");
                        ImsLogger.info("\nChildNationalId : " + child.getChildNationalID());
//                    if (!EmsUtil.checkString(child.getChildNationalID())) {
                        child.setChildNationalID(ConstValues.DEFAULT_NID);
                        logger.info("\nChildNationalId : " + child.getChildNationalID());
                        ImsLogger.info("\nChildNationalId : " + child.getChildNationalID());
//                    }
//                    if (!EmsUtil.checkString(child.getChildFirstNamePersian())) {
                        child.setChildFirstNamePersian(ConstValues.DEFAULT_NAMES_FA);
                        logger.info("\nChildFirstNamePersian : " + child.getChildFirstNamePersian());
                        ImsLogger.info("\nChildFirstNamePersian : " + child.getChildFirstNamePersian());
//                    }

//                    if (child.getChildBirthCertificateId() == null) {
                        child.setChildBirthCertificateId(ConstValues.DEFAULT_NUMBER);
                        logger.info("\nChildBirthCertificateId : " + child.getChildBirthCertificateId());
                        ImsLogger.info("\nChildBirthCertificateId : " + child.getChildBirthCertificateId());
//                    }
//                    if (child.getChildBirthCertificateSeries() == null) {
                        child.setChildBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                        logger.info("\nChildBirthCertificateSeries : " + child.getChildBirthCertificateSeries());
                        ImsLogger.info("\nChildBirthCertificateSeries : " + child.getChildBirthCertificateSeries());
//                    }
//                    if (!EmsUtil.checkString(child.getChildFatherName())) {
                        child.setChildFatherName(ConstValues.DEFAULT_NAMES_FA);
                        logger.info("\nChildFatherName : " + child.getChildFatherName());
                        ImsLogger.info("\nChildFatherName : " + child.getChildFatherName());
//                    }
//                    if (child.getChildBirthDateSolar() == null) {
                        child.setChildBirthDateSolar(DateUtil.convert(ConstValues.DEFAULT_DATE, DateUtil.GREGORIAN));
                        logger.info("\nChildBirthDate : " + child.getChildBirthDateSolar());
                        ImsLogger.info("\nChildBirthDate : " + child.getChildBirthDateSolar());
//                    }
                    }
                }
            }
        } catch (DateFormatException e) {
            throw new ServiceException(BizExceptionCode.NIF_019, BizExceptionCode.GLB_024_MSG);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.NIF_020, BizExceptionCode.GLB_024_MSG);
        }
    }

    /**
     * The method updateCitizensInfo is used to send the request for updating the citizen information.
     *
     * @param cardRequestTOList a list of type {CardRequestTO}
     * @param imsRequestId      is an instance of type {@link String}, which represents the requestId of the update
     *                          request message
     */
    @Override
    public void updateCitizensInfo(List<CardRequestTO> cardRequestTOList, String imsRequestId) throws BaseException {
        XMLMapperProvider xmlMapperProvider = new XMLMapperProvider();
        HashMap map = new HashMap();
        map.put("requestId", imsRequestId);

        Long writeDate = new Date().getTime();
        byte[] byteRequest = null;
        try {
            byteRequest = xmlMapperProvider.writeXML(cardRequestTOList, map);
        } catch (BaseException ex) {
            logger.error("Exception in WriteXML for Card Request: " + cardRequestTOList.get(0).getId(), ex);
            ImsLogger.error("Exception in WriteXML for Card Request: " + cardRequestTOList.get(0).getId(), ex);
            throw ex;
        } catch (RuntimeException ex) {
            logger.error("Exception in WriteXML for Card Request: " + cardRequestTOList.get(0).getId(), ex);
            ImsLogger.error("Exception in WriteXML for Card Request: " + cardRequestTOList.get(0).getId(), ex);
            throw ex;
        }
        String validateXml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>" + new String(byteRequest);
        try {
            xmlMapperProvider.validateAgainstXSD(new ByteArrayInputStream(validateXml.getBytes("UTF-8")),
                    getClass().getClassLoader().getResourceAsStream("com/gam/nocr/ims/xsd/IMSUpdateRequest.xsd"));
        } catch (UnsupportedEncodingException e) {
            logger.error("Invalid XML Encoding for sending to EQC (IMS request Id: " + imsRequestId + ")");
        } catch (ValidationException ve) {
            ImsLogger.error("Error in XSD Validation for sending to EQC (IMS request Id: " + imsRequestId + ") and card requests: " + cardRequestTOList.get(0), ve);
            throw ve;
        }
        ImsLogger.info("\n################## Preparing Xml lasts: " + ((new Date().getTime()) - writeDate) + " ##################");
        logger.info("\n################## Preparing Xml lasts: " + ((new Date().getTime()) - writeDate) + " ##################");

        if (byteRequest != null) {
            TransferInfoVTO transferInfoVTO = new TransferInfoVTO(imsRequestId, 0, byteRequest);
            TransferInfo transferInfo = convertToTransferInfo(transferInfoVTO);
            String strResult;
            // save xml afis
            Boolean saveXmlAfis = Boolean.valueOf(EmsUtil
                    .getProfileValue(ProfileKeyName.KEY_SAVE_XMLAFIS,
                            DEFAULT_SAVE_XMLAFIS));
            try {
                String[] strParameters = getParametersByProfileManager();
                String username = strParameters[0];
                String password = strParameters[1];

                ImsService imsOnlineService = getService();

                ImsLogger.info("IMS Farafan service stub created. Now calling updateCitizenInfo");
                Long startDate = new Date().getTime();

                TransferInfo trInfo = imsOnlineService.updateCitizenInfo(username, password, transferInfo);

                ImsLogger.info("\n################## Calling AFIS Webservice lasts: " + ((new Date().getTime()) - startDate) + " ##################");
                logger.info("\n################## Calling AFIS Webservice lasts: " + ((new Date().getTime()) - startDate) + " ##################");
                ImsLogger.info("Calling updateCitizenInfo finished");

                if (trInfo == null) {
                    ImsLogger.error(BizExceptionCode.NIF_007 + " : " + BizExceptionCode.NIF_007_MSG);
                    throw new ServiceException(BizExceptionCode.NIF_007, BizExceptionCode.NIF_007_MSG, new String[]{"transferInfo"});
                }
                logger.info("\n===========UPDATE CITIZENS INFO REQUEST BEFORE CONVERT===============");
                ImsLogger.info("\n===========UPDATE CITIZENS INFO REQUEST BEFORE CONVERT===============");
                //Anbari
                //logger.info("\nTransferInfo Filename : ", trInfo.getFileName());
                //ImsLogger.info("\nTransferInfo Filename : ", trInfo.getFileName());
                logger.info("\nTransferInfo ErrorMessage : ", trInfo.getErrMessage());
                ImsLogger.info("\nTransferInfo ErrorMessage : ", trInfo.getErrMessage());
                logger.info("\nTransferInfo Data : ", trInfo.getData());
                ImsLogger.info("\nTransferInfo Data : ", trInfo.getData());
                logger.info("\n====================================================================");
                ImsLogger.info("\n====================================================================");

                TransferInfoVTO transferInfoVTOResult = convertToTransferInfoVTO(trInfo);

                logger.info("\n============UPDATE CITIZENS INFO REQUEST AFTER CONVERT===============");
                ImsLogger.info("\n============UPDATE CITIZENS INFO REQUEST AFTER CONVERT===============");
                logger.info("\nTransferInfoVTO RequestId : ", transferInfoVTOResult.getRequestId());
                ImsLogger.info("\nTransferInfoVTO RequestId : ", transferInfoVTOResult.getRequestId());
                logger.info("\nTransferInfoVTO ErrorMessage : ", transferInfoVTOResult.getErrMessage());
                ImsLogger.info("\nTransferInfoVTO ErrorMessage : ", transferInfoVTOResult.getErrMessage());
                logger.info("\nTransferInfoVTO Data : ", transferInfoVTOResult.getData());
                ImsLogger.info("\nTransferInfoVTO Data : ", transferInfoVTOResult.getData());
                logger.info("\n====================================================================");
                ImsLogger.info("\n====================================================================");
                strResult = transferInfoVTOResult.getErrMessage();
            } catch (BaseException e) {
                ImsLogger.error(e.getExceptionCode() + " : " + e.getMessage(), e);
                if (saveXmlAfis)
                    saveXmlAfis(new XmlAfisTO(cardRequestTOList.get(0).getId(), new String(byteRequest), new Date(), e.getMessage()));
                throw e;
            } catch (Exception e) {
                ServiceException serviceException = new ServiceException(BizExceptionCode.NIF_008, e.getMessage(), e, EMSLogicalNames.SRV_NIF.split(","));
                logger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_NIF.split(","));
                ImsLogger.error(BizExceptionCode.GLB_003_MSG, serviceException, EMSLogicalNames.SRV_NIF.split(","));
                if (saveXmlAfis)
                    saveXmlAfis(new XmlAfisTO(cardRequestTOList.get(0).getId(), new String(byteRequest), new Date(), e.getMessage()));
                throw serviceException;
            }
            if (strResult != null) {
                if (saveXmlAfis)
                    saveXmlAfis(new XmlAfisTO(cardRequestTOList.get(0).getId(), new String(byteRequest), new Date(), "strResult is not null:" + strResult));
                ImsLogger.error(BizExceptionCode.NIF_009 + " : " + strResult);
                throw new ServiceException(BizExceptionCode.NIF_009, strResult, EMSLogicalNames.SRV_NIF.split(","));
            }
//            else
//            {
//            	if(saveXmlAfis)
//            	saveXmlAfis(new XmlAfisTO(cardRequestTOList.get(0).getId(),new String(byteRequest), new Date(),"successfull"));
//            }
        }
    }

    /**
     * @author ganjyar
     * The method saves xml sending AFIS
     */
    @javax.ejb.TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void saveXmlAfis(XmlAfisTO xmlAfisTO) {

        try {
            XmlAfisTO create = getXmlAfisDAO().create(xmlAfisTO);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            ImsLogger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ImsLogger.error(e.getMessage(), e);
        }

    }


    /**
     * The method getUpdatedCitizensResult is used to receive the response of updating the citizen information
     * request from IMS sub system.
     *
     * @param imsRequestId is an instance of type {@link String}, which represents the requestId of the update
     *                     request message
     * @return a list of type {@link com.gam.nocr.ems.data.domain.vol.IMSUpdateResultVTO}
     */
    @Override
    public List<IMSUpdateResultVTO> getUpdatedCitizensResult(String imsRequestId) throws BaseException {
        List<IMSUpdateResultVTO> imsUpdateResultVTOList = new ArrayList<IMSUpdateResultVTO>();

        try {
            String[] strParameters = getParametersByProfileManager();
            String username = strParameters[0];
            String password = strParameters[1];
            //Anbari
            //server.business.TransferInfo transferInfo = getService().getUpdatedCitizensResult(username, password, imsRequestId);
            // Set NIN instead of ims imsRequestId 
            TransferInfo transferInfo = getService().getUpdatedCitizensResult(username, password, 0);
            if (transferInfo == null) {
                ImsLogger.error(BizExceptionCode.NIF_010 + " : " + BizExceptionCode.NIF_010_MSG);
                throw new ServiceException(BizExceptionCode.NIF_010, BizExceptionCode.NIF_010_MSG, new String[]{"TransferInfo", "getUpdatedCitizensResult"});
            }

            if (transferInfo.getData() == null) {
                ImsLogger.error(BizExceptionCode.NIF_011 + " : " + BizExceptionCode.NIF_011_MSG);
                throw new ServiceException(BizExceptionCode.NIF_011, BizExceptionCode.NIF_011_MSG, new String[]{"getUpdatedCitizensResult"});
            }

            XMLMapperProvider xmlMapperProvider = new XMLMapperProvider();
            String xmlResult = EmsUtil.convertByteArrayToXMLString(transferInfo.getData());

            logger.info("\n\n The xml result from the service 'getUpdatedCitizensResult' : \n" + xmlResult + "\n\n");
            ImsLogger.info("\n\n The xml result from the service 'getUpdatedCitizensResult' : \n" + xmlResult + "\n\n");
            HashMap<String, String> resultMap = xmlMapperProvider.readXML(xmlResult);

            //----------------------------------------------------------------------------------------------------------------------
            /**
             * The exception NIF_021 will be thrown to handle the incorrect XML file, which has been returned from AFIS
             *
             * Incorrect XML file sample:
             *
             * <xml version="1.0" encoding="UTF-8"?>
             * <CitInfoResult requestId="00000000000000000000000000000000">
             * <Citizen afisvalidate="0" nid="0000000000"/>
             * </CitInfoResult>
             */
            String errorMSG = "";
            if (resultMap.containsValue("00000000000000000000000000000000")) {
                errorMSG += "Invalid message request id. The returned message request id from AFIS is '00000000000000000000000000000000'; however the expected one has been '" + imsRequestId + "'.\n";
            }
            if (resultMap.containsValue("0000000000")) {
                errorMSG += "Invalid nationalId. The returned nationalId from AFIS is '0000000000'.\n";
            }
            if (EmsUtil.checkString(errorMSG)) {
                throw new ServiceException(BizExceptionCode.NIF_021, errorMSG);
            }
            //----------------------------------------------------------------------------------------------------------------------

            String requestId = resultMap.get("REQUEST_ID");
            resultMap.remove("REQUEST_ID");
            Object[] mapKeys = resultMap.keySet().toArray();
            for (Object mapKey : mapKeys) {
                IMSUpdateResultVTO imsUpdateResultVTO = new IMSUpdateResultVTO();
                imsUpdateResultVTO.setRequestID(requestId);

                imsUpdateResultVTO.setNationalId(mapKey.toString());
                if (resultMap.get(mapKey.toString()).contains("EXCEPTION:")) {
                    String error = resultMap.get(mapKey.toString()).split("EXCEPTION:")[1];
                    String[] exceptionArray = error.split("-");
                    imsUpdateResultVTO.setErrorCode(exceptionArray[0]);
                    imsUpdateResultVTO.setErrorMessage(exceptionArray[1]);
                } else {
                    imsUpdateResultVTO.setAfisState(AFISState.convertToAFISState(resultMap.get(mapKey.toString())));
                }

//					TODO : Calling IMS_IdentityChangeService on future, but now set this attribute with a dummy value
                imsUpdateResultVTO.setIdentityChanged(0000);

                imsUpdateResultVTOList.add(imsUpdateResultVTO);
            }

        } catch (BaseException e) {
            ImsLogger.error(e.getExceptionCode() + " : " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(BizExceptionCode.NIF_012 + " : " + BizExceptionCode.NIF_012_MSG, e);
            ImsLogger.error(BizExceptionCode.NIF_012 + " : " + BizExceptionCode.NIF_012_MSG, e);
            throw new ServiceException(BizExceptionCode.NIF_012, BizExceptionCode.NIF_012_MSG, e);
        }

        return imsUpdateResultVTOList;
    }


    /**
     * The method setCitizenCardDelivered is used to notify the IMS about the delivering the card by citizen
     *
     * @param nationalId is an instance of type {@link String}, which represents the nationalId of a specified citizen
     * @return true or false (to show whether the alerting operation has done successfully or not)
     */
    @Override
    public boolean setCitizenCardDelivered(EmsCardDeliverInfo emsInfo) throws BaseException {
        String returnFlag = "";
        try {
            String[] strParameters = getParametersByProfileManager();
            String username = strParameters[0];
            String password = strParameters[1];

            CardDeliverInfo cardDeliverInfo = new CardDeliverInfo();

            cardDeliverInfo.setCardbatchId(emsInfo.getCardbatchId());
            cardDeliverInfo.setCarddeliveredDate(DateUtil.convert(emsInfo.getCardDeliveredDate(), DateUtil.JALALI));
            if (emsInfo.getCardlostDate() != null)
                cardDeliverInfo.setCardlostDate(DateUtil.convert(emsInfo.getCardlostDate(), DateUtil.JALALI));
            cardDeliverInfo.setCardreceivedDate(DateUtil.convert(emsInfo.getCardreceivedDate(), DateUtil.JALALI));
            cardDeliverInfo.setCardshipmentDate(DateUtil.convert(emsInfo.getCardshipmentDate(), DateUtil.JALALI));
            cardDeliverInfo.setCardState(Byte.valueOf(CardState.toLong(emsInfo.getCardState()).toString()));
            cardDeliverInfo.setEstelamId(0);
            cardDeliverInfo.setOfficeCode(emsInfo.getOfficeCode());
            cardDeliverInfo.setPersonNin(emsInfo.getPersonNin());
            cardDeliverInfo.setSmartCardIssuanceDate(DateUtil.convert(emsInfo.getCardIssuanceDate(), DateUtil.JALALI));
            cardDeliverInfo.setSmartCardSeriSerial(emsInfo.getCrn());

            returnFlag = getService().setCitizenCardDelivered(username, password, cardDeliverInfo);
        } catch (BaseException e) {
            logger.error("EXCEPTION IN IMS setCitizenCardDelivered ", e);
            ImsLogger.error("EXCEPTION IN IMS setCitizenCardDelivered ", e);
            throw new ServiceException(BizExceptionCode.NIF_015, BizExceptionCode.NIF_015_MSG, e);
        } catch (Exception e) {
            logger.error("EXCEPTION IN IMS setCitizenCardDelivered ", e);
            ImsLogger.error("EXCEPTION IN IMS setCitizenCardDelivered ", e);
        }
        if (returnFlag != null && "OK".equals(returnFlag))
            return true;
        else {
            Long cardRequestId = emsInfo.getCardRequestId();
            getCardRequestHistoryDAO().create(new CardRequestTO(cardRequestId), returnFlag, SystemId.IMS, null, CardRequestHistoryAction.AFIS_DELIVERED_ERROR, null);

        }
        return false;
    }

    private XMLGregorianCalendar convertDateToXMLGregorianCalendar(
            Date carddeliveredDate) throws BaseException {

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(carddeliveredDate);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            throw new ServiceException(BizExceptionCode.NIF_015, BizExceptionCode.NIF_015_MSG, e);
        }
    }


    /**
     * The method fetchCitizenInfo if used to receive all the information about a specified citizen, from the sub system
     * 'IMS'.
     *
     * @param nationalId is an instance of type {@link String}, which represents the nationalId of a specified citizen
     * @return an instance of type {@link com.gam.nocr.ems.data.domain.CitizenTO}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public CitizenTO fetchCitizenInfo(String nationalId) throws BaseException {

        //   CitizenTO citizenTO;
        CitizenTO citizenTO = new CitizenTO();

        try {
//            String[] strParameters = getParametersByProfileManager();
//            String username = strParameters[0];
//            String password = strParameters[1];
//            TransferInfo transferInfo = getService().fetchCitizenInfo(username, password, Long.valueOf(nationalId));
//            if (transferInfo == null) {
//                ImsLogger.error(BizExceptionCode.NIF_013 + " : " + BizExceptionCode.NIF_010_MSG);
//                throw new ServiceException(BizExceptionCode.NIF_013, BizExceptionCode.NIF_010_MSG, new String[]{"TransferInfo", "fetchCitizenInfo"});
//            }
//
//            TransferInfoVTO transferInfoVTO = convertToTransferInfoVTO(transferInfo);
//            XMLMapperProvider xmlMapperProvider = new XMLMapperProvider();
//            if (transferInfoVTO.getData() == null) {
//                ImsLogger.error(BizExceptionCode.NIF_014 + " : " + BizExceptionCode.NIF_011_MSG);
//                throw new ServiceException(BizExceptionCode.NIF_014, BizExceptionCode.NIF_011_MSG);
//            }
//            String xmlResponse = EmsUtil.convertByteArrayToXMLString(transferInfoVTO.getData());
//            logger.info("\n\n The xml fetch result : \n" + xmlResponse + "\n\n");
//            ImsLogger.info("\n\n The xml fetch result : \n" + xmlResponse + "\n\n");
//            CitizenTO ctz = new CitizenTO();
//            citizenTO = (CitizenTO) xmlMapperProvider.readXML(xmlResponse, ctz);
//
//            setDefaultValues(citizenTO);

            citizenTO.setNationalID(nationalId);
            citizenTO = getIMSOnlineService().fetchCitizenInfoByOnlineEnquiry(citizenTO);


        } catch (BaseException e) {
            logger.error(e.getExceptionCode() + " : " + e.getMessage(), e);
            ImsLogger.error(e.getExceptionCode() + " : " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(BizExceptionCode.NIF_015 + " : " + BizExceptionCode.NIF_015_MSG, e);
            ImsLogger.error(BizExceptionCode.NIF_015 + " : " + BizExceptionCode.NIF_015_MSG, e);
            throw new ServiceException(BizExceptionCode.NIF_015, BizExceptionCode.NIF_015_MSG, e);
        }

        return citizenTO;
    }


    //Anbari getUpdatedCitizensResultNew to call IMS newServices and pars exception and errorCode and get Image
    @Override
    public IMSUpdateResultVTO getUpdatedCitizensResultNew(String nationalId) throws BaseException {
        IMSUpdateResultVTO imsUpdateResultVTO = new IMSUpdateResultVTO();

        try {
            String[] strParameters = getParametersByProfileManager();
            String username = strParameters[0];
            String password = strParameters[1];
            //Anbari
            //server.business.TransferInfo transferInfo = getService().getUpdatedCitizensResult(username, password, imsRequestId);
            // Set NIN instead of ims imsRequestId 
            TransferInfo transferInfo = getService().getUpdatedCitizensResult(username, password, Long.valueOf(nationalId));
            if (transferInfo == null) {
                ImsLogger.error(BizExceptionCode.NIF_010 + " : " + BizExceptionCode.NIF_010_MSG);
                throw new ServiceException(BizExceptionCode.NIF_010, BizExceptionCode.NIF_010_MSG, new String[]{"TransferInfo", "getUpdatedCitizensResult"});
            }
            //َAnbari : get ErrorrMessage
            if (transferInfo.getErrMessage() == null || transferInfo.getErrMessage().isEmpty()) {
                ImsLogger.error(BizExceptionCode.NIF_022 + " : " + BizExceptionCode.NIF_010_MSG);
                throw new ServiceException(BizExceptionCode.NIF_022, BizExceptionCode.NIF_010_MSG, new String[]{"ErrorMessage", "getUpdatedCitizensResult"});
            }

            //***************************** Anbari :  extract IMS requestID form XML inorder to find incorrect xml format or NID
            XMLMapperProvider xmlMapperProvider = new XMLMapperProvider();
            String xmlResult = EmsUtil.convertByteArrayToXMLString(transferInfo.getData());

            //logger.info("\n\n The xml result from the service 'getUpdatedCitizensResult' : \n" + xmlResult + "\n\n");
            ImsLogger.info("\n\n The xml result from the service 'getUpdatedCitizensResult' : \n" + xmlResult + "\n\n");
            HashMap<String, String> resultMap = xmlMapperProvider.readXML(xmlResult);

            //********************************

            //Anbari : do business in various ErrorCode in outer method
            imsUpdateResultVTO.setErrorMessage(transferInfo.getErrMessage());

            //Anbari : Invalid message requestID
            String errorMSG = "";
            if (resultMap.containsValue("00000000000000000000000000000000")) {
                errorMSG += "Invalid message request id for nationalID " + nationalId + ". The returned message request id from AFIS is '00000000000000000000000000000000'; however the expected one has been .\n";
            }
            if (resultMap.containsValue("0000000000")) {
                errorMSG += "Invalid nationalId. The returned nationalId from AFIS is '0000000000'.\n";
            }
            if (EmsUtil.checkString(errorMSG)) {
                throw new ServiceException(BizExceptionCode.NIF_021, errorMSG);
            }
            //  ----------------------------------------------------------------------------------------------------------------------

            String requestId = resultMap.get("REQUEST_ID");
            resultMap.remove("REQUEST_ID");
            Object[] mapKeys = resultMap.keySet().toArray();
            for (Object mapKey : mapKeys) {
                imsUpdateResultVTO.setRequestID(requestId);

                imsUpdateResultVTO.setNationalId(mapKey.toString());
                if (resultMap.get(mapKey.toString()).contains("EXCEPTION:")) {
                    String error = resultMap.get(mapKey.toString()).split("EXCEPTION:")[1];
                    String[] exceptionArray = error.split("-");
                    imsUpdateResultVTO.setErrorCode(exceptionArray[0]);
                    imsUpdateResultVTO.setErrorMessage(exceptionArray[1]);
                } else {
                    imsUpdateResultVTO.setAfisState(AFISState.convertToAFISState(resultMap.get(mapKey.toString())));
                }

                //		TODO : Calling IMS_IdentityChangeService on future, but now set this attribute with a dummy value
                imsUpdateResultVTO.setIdentityChanged(0000);
            }

          /*  List<ErrorInfo> errCodes = transferInfo.getErrCodes();
            if (errCodes != null && !errCodes.isEmpty()) {
                List<IMSErrorInfo> imsErrCodes = new ArrayList<IMSErrorInfo>();
                for (ErrorInfo errCode : errCodes) {
                    imsErrCodes.add(new IMSErrorInfo(errCode.getCode(), errCode.getDesc()));
                }
                imsUpdateResultVTO.setErrorCodes(imsErrCodes);
            }*/
            if (transferInfo.getErrMessage().contains("UPDT-000018") && transferInfo.getData() != null) //OK with Image : so parse the XML IMS_UPDT_000018
            {
                if (transferInfo.getData() == null) {
                    ImsLogger.error(BizExceptionCode.NIF_011 + " : " + BizExceptionCode.NIF_011_MSG);
                    throw new ServiceException(BizExceptionCode.NIF_011, BizExceptionCode.NIF_011_MSG, new String[]{"getUpdatedCitizensResult"});
                }

                //logger.info("\n\n The xml result from the service 'getUpdatedCitizensResult' : \n" + xmlResult + "\n\n");
                ImsLogger.info("\n\n The xml result from the service 'getUpdatedCitizensResult' : \n" + xmlResult + "\n\n");
                resultMap.clear();
                resultMap = xmlMapperProvider.readXMLNew(xmlResult);
                String faceIMSStr = resultMap.get("FACE_IMS");
                String faceLASERstr = resultMap.get("FACE_LASER");
                String faceCHIPstr = resultMap.get("FACE_CHIP");
                String faceMLIstr = resultMap.get("FACE_MLI");

                if (faceIMSStr == null || faceLASERstr == null || faceCHIPstr == null || faceMLIstr == null)
                    throw new ServiceException(BizExceptionCode.NIF_023, BizExceptionCode.NIF_019_MSG, new String[]{"getUpdatedCitizensResult"});

                imsUpdateResultVTO.setFaceIMS(Base64.decodeToBytes(faceIMSStr));
                imsUpdateResultVTO.setFaceLASER(Base64.decodeToBytes(faceLASERstr));
                imsUpdateResultVTO.setFaceCHIP(Base64.decodeToBytes(faceCHIPstr));
                imsUpdateResultVTO.setFaceMLI(Base64.decodeToBytes(faceMLIstr));


//                 imsUpdateResultVTO.setFaceIMS(org.apache.commons.codec.binary.Base64.decodeBase64(faceIMSStr));
//                 imsUpdateResultVTO.setFaceLASER(org.apache.commons.codec.binary.Base64.decodeBase64(faceLASERstr));                 
//                 imsUpdateResultVTO.setFaceCHIP(org.apache.commons.codec.binary.Base64.decodeBase64(faceCHIPstr));
//                 imsUpdateResultVTO.setFaceMLI(org.apache.commons.codec.binary.Base64.decodeBase64(faceMLIstr));  
            }


        } catch (BaseException e) {
            ImsLogger.error(e.getExceptionCode() + " : " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(BizExceptionCode.NIF_012 + " : " + BizExceptionCode.NIF_012_MSG, e);
            ImsLogger.error(BizExceptionCode.NIF_012 + " : " + BizExceptionCode.NIF_012_MSG, e);
            throw new ServiceException(BizExceptionCode.NIF_012, BizExceptionCode.NIF_012_MSG, e);
        }
        return imsUpdateResultVTO;
    }

}
