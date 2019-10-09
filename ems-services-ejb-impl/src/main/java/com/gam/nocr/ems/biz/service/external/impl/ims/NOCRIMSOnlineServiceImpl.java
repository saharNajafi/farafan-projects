package com.gam.nocr.ems.biz.service.external.impl.ims;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.AbstractService;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.dao.factory.DAOFactory;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.util.SecureString;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.biz.service.BusinessLogService;
import com.gam.nocr.ems.biz.service.IMSManagementService;
import com.gam.nocr.ems.config.*;
import com.gam.nocr.ems.data.dao.CitizenDAO;
import com.gam.nocr.ems.data.dao.ExceptionCodeDAO;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.vol.PersonEnquiryVTO;
import com.gam.nocr.ems.data.domain.ws.PersonEnquiryWTO;
import com.gam.nocr.ems.data.enums.*;
import com.gam.nocr.ems.util.EmsUtil;
import com.jayway.jsonpath.JsonPath;
import est.*;
import gampooya.tools.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import servicePortUtil.ServicePorts;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.xml.namespace.QName;
import java.lang.Exception;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@Stateless(name = "NOCRIMSOnlineService")
@Local(NOCRIMSOnlineServiceLocal.class)
@Remote(NOCRIMSOnlineServiceRemote.class)
public class NOCRIMSOnlineServiceImpl extends AbstractService implements NOCRIMSOnlineServiceLocal, NOCRIMSOnlineServiceRemote {


    @Resource
    SessionContext sessionContext;

    private static final Logger logger = BaseLog.getLogger(NOCRIMSOnlineServiceImpl.class);
    private static final Logger ImsLogger = BaseLog.getLogger("ImsLogger");
    private static final Logger threadLocalLogger = BaseLog.getLogger("threadLocal");
    private static final Logger estelam2Logger = BaseLog
            .getLogger("Estelam2Logger");

    private static final String DEFAULT_ONLINE_WSDL_URL = "http://webservice-idc.nocrservices.org/iwsd/Estelam?wsdl";
    private static final String DEFAULT_ONLINE_NAMESPACE = "http://est";
    private static final String DEFAULT_ONLINE_USERNAME = "gmet";
    private static final String DEFAULT_ONLINE_PASSWORD = "gmet-pass";
    private static final String DEFAULT_ONLINE_KEYHAN_USERNAME = "56570";
    private static final String DEFAULT_ONLINE_KEYHAN_SERIAL = "GAM05E657MAG";

    // Online enquiry service exceptions
    private static final String IMS_ONLINE_ENQUIRY_NO_RECORD_FOUND_EXCEPTION = "err.record.not.found";
    private static final String IMS_ONLINE_ENQUIRY_REVIEW_RECORD_EXCEPTION = "result.rec.review";

    private static final String CITIZEN_KEY = "ctz";
    private static final String FATHER_KEY = "f";
    private static final String MOTHER_KEY = "m";
    private static final String SPOUSE_KEY = "s";
    private static final String CHILD_KEY = "c";

    /**
     * ===================
     * Getter for Services
     * ===================
     */

    private BusinessLogService getBusinessLogService() throws BaseException {
        ServiceFactory serviceFactory = ServiceFactoryProvider.getServiceFactory();
        BusinessLogService businessLogService;
        try {
            businessLogService = serviceFactory.getService(EMSLogicalNames.getServiceJNDIName(EMSLogicalNames.SRV_BUSINESS_LOG), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            ImsLogger.error(BizExceptionCode.NIO_001 + " : " + BizExceptionCode.GLB_002_MSG, e);
            throw new ServiceException(
                    BizExceptionCode.NIO_001,
                    BizExceptionCode.GLB_002_MSG,
                    e,
                    EMSLogicalNames.SRV_BUSINESS_LOG.split(","));
        }
        return businessLogService;
    }

    /**
     * getService
     *
     * @return an instance of type {@link est.EstelamPort}
     * @throws BaseException if cannot get the service
     */
    private EstelamPort getService() throws BaseException {
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();

            String wsdlUrl = (String) pm.getProfile(ProfileKeyName.KEY_IMS_ONLINE_ENDPOINT, true, null, null);
            String namespace = (String) pm.getProfile(ProfileKeyName.KEY_IMS_ONLINE_NAMESPACE, true, null, null);
            if (wsdlUrl == null)
                wsdlUrl = DEFAULT_ONLINE_WSDL_URL;
            if (namespace == null)
                namespace = DEFAULT_ONLINE_NAMESPACE;
            String serviceName = "Estelam";
            logger.debug("=======================================================================================");
            ImsLogger.debug("=======================================================================================");
            logger.debug("=================================== IMS_ONLINE_WSDL ===================================");
            ImsLogger.debug("=================================== IMS_ONLINE_WSDL ===================================");
            logger.debug("IMS online wsdl url: " + wsdlUrl);
            ImsLogger.debug("IMS online wsdl url: " + wsdlUrl);
            logger.debug("=======================================================================================");
            ImsLogger.debug("=======================================================================================");
            logger.debug("=======================================================================================");
            ImsLogger.debug("=======================================================================================");

            //Commented for ThreadLocal
            //EstelamPort port = new Estelam(new URL(wsdlUrl), new QName(namespace, serviceName)).getEstelamPort();
            EstelamPort port = ServicePorts.getEstelamPort();
            if (port == null) {
                threadLocalLogger.debug("**************************** new EstelamPort in Estelam getService()");
                port = new Estelam(new URL(wsdlUrl), new QName(namespace, serviceName)).getEstelamPort();
                ServicePorts.setEstelamPort(port);
            } else {
                threadLocalLogger.debug("***************************** using EstelamPort from ThradLocal");
            }
            EmsUtil.setJAXWSWebserviceProperties(port, wsdlUrl);
            return port;
        } catch (Exception e) {
            ImsLogger.error(BizExceptionCode.NIO_002 + " : " + BizExceptionCode.GLB_002_MSG, e);
            throw new ServiceException(BizExceptionCode.NIO_002, BizExceptionCode.GLB_002_MSG, e);
        }
    }


    private IMSManagementService getImsManagementervice()
            throws BaseException {
        ServiceFactory factory = ServiceFactoryProvider.getServiceFactory();
        IMSManagementService imsManagementService = null;
        try {
            imsManagementService = (IMSManagementService) factory
                    .getService(EMSLogicalNames
                            .getServiceJNDIName(EMSLogicalNames.SRV_IMS_MANAGEMENT), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.IMD_001,
                    BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_IMS_MANAGEMENT.split(","));
        }
        return imsManagementService;
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
     * The method convertToPersonInfo is used to convert an instance of type {@link PersonEnquiryVTO} to an instance of
     * type {@link PersonInfo}
     *
     * @param personEnquiryVTO is an instance of type {@link PersonEnquiryVTO}
     * @return an instance of type {@link PersonInfo}
     * @throws BaseException
     */
    private PersonInfo convertToPersonInfo(PersonEnquiryVTO personEnquiryVTO) throws BaseException {
        try {
            PersonInfo personInfo = new PersonInfo();

            personInfo.setNin(Long.parseLong(personEnquiryVTO.getNationalId()));
            StringBuilder imsEnquiryFieldsMessage = new StringBuilder().append("\n IMS_ONLINE_ENQUIRY_FIELDS : NATIONAL_ID ' ").append(personEnquiryVTO.getNationalId()).append("', ");

            if (personEnquiryVTO.getSolarBirthDate() != null) {
                String[] solarDate = personEnquiryVTO.getSolarBirthDate().split("/");
                String stringSolarDate = solarDate[0] + solarDate[1] + solarDate[2];
                personInfo.setBirthDate(Integer.parseInt(stringSolarDate));
                imsEnquiryFieldsMessage.append("SOLAR_BIRTH_DATE '").append(stringSolarDate).append("'");
            } else if (personEnquiryVTO.getBirthCertificateId() != null) {
                personInfo.setShenasnameNo(Integer.parseInt(personEnquiryVTO.getBirthCertificateId()));
                imsEnquiryFieldsMessage.append("BIRTH_CERTIFICATE_ID '").append(personEnquiryVTO.getBirthCertificateId()).append("'");
            }

            imsEnquiryFieldsMessage.append(" \n");
            logger.info(imsEnquiryFieldsMessage.toString());
            ImsLogger.info(imsEnquiryFieldsMessage.toString());

            return personInfo;
        } catch (Exception e) {
            ImsLogger.error(BizExceptionCode.NIO_003 + " : " + e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.NIO_003, e.getMessage(), e);
        }
    }

    /**
     * The method getOnlineParametersByProfileManager is used to prepare the parameters which are fetched from config
     * for
     * online services
     *
     * @return an array of type {@link String} which carries the required parameters
     */
    private String[] getOnlineParametersByProfileManager() throws BaseException {
        String username = null;
        String password = null;
        String keyhanUsername = null;
        String keyhanSerial = null;
        ProfileManager pm = null;
        String[] strArray;
        try {
            pm = ProfileHelper.getProfileManager();
            username = (String) pm.getProfile(ProfileKeyName.KEY_IMS_ONLINE_USERNAME, true, null, null);
            password = (String) pm.getProfile(ProfileKeyName.KEY_IMS_ONLINE_PASSWORD, true, null, null);
            keyhanUsername = (String) pm.getProfile(ProfileKeyName.KEY_IMS_ONLINE_KEYHAN_USERNAME, true, null, null);
            keyhanSerial = (String) pm.getProfile(ProfileKeyName.KEY_IMS_ONLINE_KEYHAN_SERIAL, true, null, null);
        } catch (Exception e) {
            if (pm == null) {
                logger.warn(BizExceptionCode.NIO_004, BizExceptionCode.GLB_011_MSG);
            }
        }

        if (username == null) {
            strArray = new String[]{"username", "onlineEnquiry", DEFAULT_ONLINE_USERNAME};
            logger.warn(BizExceptionCode.NIO_005, BizExceptionCode.GLB_016_MSG, strArray);
            ImsLogger.warn(BizExceptionCode.NIO_005, BizExceptionCode.GLB_016_MSG, strArray);
            username = DEFAULT_ONLINE_USERNAME;
        }

        if (password == null) {
            strArray = new String[]{"password", "onlineEnquiry", DEFAULT_ONLINE_PASSWORD};
            logger.warn(BizExceptionCode.NIO_006, BizExceptionCode.GLB_016_MSG, strArray);
            ImsLogger.warn(BizExceptionCode.NIO_006, BizExceptionCode.GLB_016_MSG, strArray);
            password = DEFAULT_ONLINE_PASSWORD;
        }

        if (keyhanUsername == null) {
            strArray = new String[]{"keyhanUsername", "onlineEnquiry", DEFAULT_ONLINE_KEYHAN_USERNAME};
            logger.warn(BizExceptionCode.NIO_007, BizExceptionCode.GLB_016_MSG, strArray);
            ImsLogger.warn(BizExceptionCode.NIO_007, BizExceptionCode.GLB_016_MSG, strArray);
            keyhanUsername = DEFAULT_ONLINE_KEYHAN_USERNAME;
        }

        if (keyhanSerial == null) {
            strArray = new String[]{"keyhanSerial", "onlineEnquiry", DEFAULT_ONLINE_KEYHAN_SERIAL};
            logger.warn(BizExceptionCode.NIO_008, BizExceptionCode.GLB_016_MSG, strArray);
            ImsLogger.warn(BizExceptionCode.NIO_008, BizExceptionCode.GLB_016_MSG, strArray);
            keyhanSerial = DEFAULT_ONLINE_KEYHAN_SERIAL;
        }

        return new String[]{username, password, keyhanUsername, keyhanSerial};
    }

    /**
     * The method setCitizenData is used to set the attributes of an instance of type {@link CitizenTO} with values, which are fetched from the IMS subsystem
     *
     * @param personInfoMap is a map of type {@link HashMap<String, String>}, which is used to keep the track of citizen info [It means what sorts of citizen (father, mother, ... or the exact citizen)]
     * @param citizenTO     is an instance of type {@link CitizenTO}
     * @param estelamResult is an instance of {@link EstelamResult}, which carries citizen's attributes that were valued by IMS
     */
    private void setCitizenData(HashMap<String, String> personInfoMap,
                                CitizenTO citizenTO,
                                EstelamResult estelamResult) throws BaseException {
        try {
            String nationalId = StringUtils.leftPad(String.valueOf(estelamResult.getNin()), 10, "0");
            CitizenInfoTO citizenInfoTO = citizenTO.getCitizenInfo();
            if (CITIZEN_KEY.equals(personInfoMap.get(nationalId))) {
                citizenTO.setNationalID(nationalId);
                citizenTO.setFirstNamePersian(new String(estelamResult.getName(), "UTF-8"));
                citizenTO.setSurnamePersian(new String(estelamResult.getFamily(), "UTF-8"));
                citizenInfoTO.setGender(Gender.convertFromIMSEstelamResultGender(estelamResult.getGender()));
                citizenInfoTO.setFatherFirstNamePersian(new String(estelamResult.getFatherName(), "UTF-8"));
                citizenInfoTO.setBirthCertificateId(String.valueOf(estelamResult.getShenasnameNo()));
//                citizenInfoTO.setBirthCertificateSeries(String.valueOf(estelamResult.getShenasnameSerial()));
                citizenInfoTO.setBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                String citizenBirthDate = String.valueOf(estelamResult.getBirthDate());
                citizenInfoTO.setBirthDateSolar(citizenBirthDate.substring(0, 4) + "/" + citizenBirthDate.substring(4, 6) + "/" + citizenBirthDate.substring(6, 8));
                citizenInfoTO.setBirthDateGregorian(DateUtil.convert(citizenInfoTO.getBirthDateSolar(), DateUtil.JALALI));
                citizenInfoTO.setBirthDateLunar(DateUtil.convert(citizenInfoTO.getBirthDateGregorian(), DateUtil.HIJRI));
                logger.info("\n\nCitizen : " +
                        "\nCitizenName : " + citizenTO.getFirstNamePersian() + " " + citizenTO.getSurnamePersian() + "" +
                        "\n CitizenNationalId : " + citizenTO.getNationalID() + "\n");
                ImsLogger.info("\n\nCitizen : " +
                        "\nCitizenName : " + citizenTO.getFirstNamePersian() + " " + citizenTO.getSurnamePersian() + "" +
                        "\n CitizenNationalId : " + citizenTO.getNationalID() + "\n");


            } else if (FATHER_KEY.equals(personInfoMap.get(nationalId))) {
                citizenInfoTO.setFatherNationalID(nationalId);
//                citizenInfoTO.setFatherFirstNamePersian(new String(estelamResult.getName(), "UTF-8"));
                citizenInfoTO.setFatherFirstNameEnglish(ConstValues.DEFAULT_NAMES_EN);
                citizenInfoTO.setFatherSurname(ConstValues.DEFAULT_NAMES_FA);
                citizenInfoTO.setFatherFatherName(ConstValues.DEFAULT_NAMES_FA);
                citizenInfoTO.setFatherBirthCertificateId(String.valueOf(estelamResult.getShenasnameNo()));
//                citizenInfoTO.setFatherBirthCertificateSeries(String.valueOf(estelamResult.getShenasnameSerial()));
                citizenInfoTO.setFatherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                String fBirthDate = String.valueOf(estelamResult.getBirthDate());
                citizenInfoTO.setFatherBirthDateSolar(DateUtil.convert(fBirthDate.substring(0, 4) + "/" + fBirthDate.substring(4, 6) + "/" + fBirthDate.substring(6, 8), DateUtil.JALALI));
                logger.info("\n\nFATHER : " +
                        "\nFatherName : " + citizenInfoTO.getFatherFirstNamePersian() + " " + citizenInfoTO.getFatherSurname() + "" +
                        "\n FatherNationalId : " + citizenInfoTO.getFatherNationalID() + "\n");
                ImsLogger.info("\n\nFATHER : " +
                        "\nFatherName : " + citizenInfoTO.getFatherFirstNamePersian() + " " + citizenInfoTO.getFatherSurname() + "" +
                        "\n FatherNationalId : " + citizenInfoTO.getFatherNationalID() + "\n");


            } else if (MOTHER_KEY.equals(personInfoMap.get(nationalId))) {
                citizenInfoTO.setMotherNationalID(nationalId);
                citizenInfoTO.setMotherFirstNamePersian(new String(estelamResult.getName(), "UTF-8"));
                citizenInfoTO.setMotherSurname(ConstValues.DEFAULT_NAMES_FA);
                citizenInfoTO.setMotherFatherName(ConstValues.DEFAULT_NAMES_FA);
                citizenInfoTO.setMotherBirthCertificateId(String.valueOf(estelamResult.getShenasnameNo()));
//                citizenInfoTO.setMotherBirthCertificateSeries(String.valueOf(estelamResult.getShenasnameSerial()));
                citizenInfoTO.setMotherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                String mBirthDate = String.valueOf(estelamResult.getBirthDate());
                citizenInfoTO.setMotherBirthDateSolar(DateUtil.convert(mBirthDate.substring(0, 4) + "/" + mBirthDate.substring(4, 6) + "/" + mBirthDate.substring(6, 8), DateUtil.JALALI));
                logger.info("\n\nMOTHER : " +
                        "\nMotherName : " + citizenInfoTO.getMotherFirstNamePersian() + " " + citizenInfoTO.getMotherSurname() + "" +
                        "\n MotherNationalId : " + citizenInfoTO.getMotherNationalID() + "\n");
                ImsLogger.info("\n\nMOTHER : " +
                        "\nMotherName : " + citizenInfoTO.getMotherFirstNamePersian() + " " + citizenInfoTO.getMotherSurname() + "" +
                        "\n MotherNationalId : " + citizenInfoTO.getMotherNationalID() + "\n");


            } else if (SPOUSE_KEY.equals(personInfoMap.get(nationalId))) {
                List<SpouseTO> spouseTOs = citizenInfoTO.getSpouses();
                for (SpouseTO spouseTO : spouseTOs) {
                    if (nationalId.equals(spouseTO.getSpouseNationalID())) {
                        spouseTO.setSpouseNationalID(nationalId);
                        spouseTO.setSpouseFirstNamePersian(new String(estelamResult.getName(), "UTF-8"));
                        spouseTO.setSpouseSurnamePersian(new String(estelamResult.getFamily(), "UTF-8"));
                        spouseTO.setSpouseFatherName(ConstValues.DEFAULT_NAMES_FA);
                        spouseTO.setSpouseBirthCertificateId(String.valueOf(estelamResult.getShenasnameNo()));
//                        spouseTO.setSpouseBirthCertificateSeries(String.valueOf(estelamResult.getShenasnameSerial()));
                        spouseTO.setSpouseBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                        String sBirthDate = String.valueOf(estelamResult.getBirthDate());
                        logger.info("\n\n SPOUSE BIRTHDATE BEFORE CONVERT : " + sBirthDate + "\n");
                        ImsLogger.info("\n\n SPOUSE BIRTHDATE BEFORE CONVERT : " + sBirthDate + "\n");
                        spouseTO.setSpouseBirthDate(DateUtil.convert(sBirthDate.substring(0, 4) + "/" + sBirthDate.substring(4, 6) + "/" + sBirthDate.substring(6, 8), DateUtil.JALALI));
                        logger.info("\n\n SPOUSE BIRTHDATE AFTER CONVERT : " + DateUtil.convert(spouseTO.getSpouseBirthDate(), DateUtil.JALALI) + "\n");
                        ImsLogger.info("\n\n SPOUSE BIRTHDATE AFTER CONVERT : " + DateUtil.convert(spouseTO.getSpouseBirthDate(), DateUtil.JALALI) + "\n");
                        logger.info("\n\nSPOUSE : " +
                                "\nSpouseName : " + spouseTO.getSpouseFirstNamePersian() + " " + spouseTO.getSpouseSurnamePersian() + "" +
                                "\n SpouseNationalId : " + spouseTO.getSpouseNationalID() + "\n");
                        ImsLogger.info("\n\nSPOUSE : " +
                                "\nSpouseName : " + spouseTO.getSpouseFirstNamePersian() + " " + spouseTO.getSpouseSurnamePersian() + "" +
                                "\n SpouseNationalId : " + spouseTO.getSpouseNationalID() + "\n");
                    }
                }


            } else if (CHILD_KEY.equals(personInfoMap.get(nationalId))) {
                List<ChildTO> childTOs = citizenInfoTO.getChildren();
                for (ChildTO childTO : childTOs) {
                    if (nationalId.equals(childTO.getChildNationalID())) {
                        childTO.setChildNationalID(nationalId);
                        childTO.setChildFirstNamePersian(new String(estelamResult.getName(), "UTF-8"));
                        childTO.setChildFatherName(ConstValues.DEFAULT_NAMES_FA);
                        childTO.setChildBirthCertificateId(String.valueOf(estelamResult.getShenasnameNo()));
//                        childTO.setChildBirthCertificateSeries(String.valueOf(estelamResult.getShenasnameSerial()));
                        childTO.setChildBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                        childTO.setChildGender(Gender.convertFromIMSEstelamResultGender(estelamResult.getGender()));
                        String cBirthDate = String.valueOf(estelamResult.getBirthDate());
                        childTO.setChildBirthDateSolar(DateUtil.convert(cBirthDate.substring(0, 4) + "/" + cBirthDate.substring(4, 6) + "/" + cBirthDate.substring(6, 8), DateUtil.JALALI));
                        logger.info("\n\nCHILD : " +
                                "\nChildName : " + childTO.getChildFirstNamePersian() + "" +
                                "\n ChildNationalId : " + childTO.getChildNationalID() + "\n");
                        ImsLogger.info("\n\nCHILD : " +
                                "\nChildName : " + childTO.getChildFirstNamePersian() + "" +
                                "\n ChildNationalId : " + childTO.getChildNationalID() + "\n");
                    }
                }


            }
            citizenTO.setCitizenInfo(citizenInfoTO);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.NIO_020, BizExceptionCode.NIO_020_MSG + e.getMessage(), e);
        }
    }


    private ExceptionCodeDAO getExceptionCodeDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory()
                    .getDAO(EMSLogicalNames
                            .getDaoJNDIName(EMSLogicalNames.DAO_ExceptionCodeDAO));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.EXC_001,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_ExceptionCodeDAO.split(","));
        }
    }

    private CitizenDAO getCitizenDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        CitizenDAO citizenDAO = null;
        try {
            citizenDAO = factory.getDAO(EMSLogicalNames
                    .getDaoJNDIName(EMSLogicalNames.DAO_CITIZEN));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.MMS_027,
                    BizExceptionCode.GLB_001_MSG, e,
                    EMSLogicalNames.DAO_CITIZEN.split(","));
        }
        return citizenDAO;
    }


    private CitizenTO setCitizenDataNew(HashMap<String, String> personInfoMap, CitizenTO ctz, EstelamResultC estelam3sc)
            throws Exception {
        try {


            CitizenTO citizenTO = getCitizenDAO().findByNationalID(ctz.getNationalID());
            CitizenInfoTO citizenInfoTO = citizenTO.getCitizenInfo();

            EstelamResult3 estelamResult3 = estelam3sc.getEstelamResult3().get(0);
            String nationalId = StringUtils.leftPad(String.valueOf(estelamResult3.getNin()), 10, "0");
            if (CITIZEN_KEY.equals(personInfoMap.get(nationalId))) {
                citizenTO.setNationalID(nationalId);


                if (estelamResult3.getShenasnameNo() >= 0) {
                    if (estelamResult3.getShenasnameNo() == 0) {
                        citizenInfoTO.setBirthCertificateId(nationalId);
                    } else {
                        citizenInfoTO.setBirthCertificateId(String.valueOf(estelamResult3.getShenasnameNo()));
                    }

                } else {
                    throw new ServiceException(BizExceptionCode.NIO_021, BizExceptionCode.NIO_033_MSG);

                }


                if (estelamResult3.getBirthDate() != 0) {
                    String strDate = String.valueOf(estelamResult3.getBirthDate());
                    //validate of birthDate
                    if (checkValidateDate(strDate)) {

                        citizenInfoTO.setBirthDateSolar(strDate.substring(0, 4)
                                + "/"
                                + strDate.substring(4, 6)
                                + "/"
                                + strDate.substring(6, 8));
                        citizenInfoTO.setBirthDateGregorian(DateUtil.convert(citizenInfoTO.getBirthDateSolar(), DateUtil.JALALI));
                        citizenInfoTO.setBirthDateLunar(DateUtil.convert(citizenInfoTO.getBirthDateGregorian(), DateUtil.HIJRI));

                    } else {
                        throw new ServiceException(BizExceptionCode.NIO_022, BizExceptionCode.NIO_037_MSG);

                    }
                } else {
                    throw new ServiceException(BizExceptionCode.NIO_022, BizExceptionCode.NIO_037_MSG);

                }

                //Shenasname Serial
                String serialShenasname = String.valueOf(estelamResult3.getShenasnameSerial());
                if (serialShenasname.length() == 6 && !serialShenasname.equals("000000")) {
                    citizenInfoTO.setBirthCertificateSeries(String.valueOf(estelamResult3.getShenasnameSerial()));
                } else {
                    throw new ServiceException(BizExceptionCode.NIO_023, BizExceptionCode.NIO_036_MSG);

                }


                String firstName = new String(estelamResult3.getName());
                if (EmsUtil.checkString(firstName) && !firstName.equals(ConstValues.DEFAULT_NAMES_FA)) {

                    citizenTO.setFirstNamePersian(new String(estelamResult3.getName(), "UTF-8"));
                } else {

                    throw new ServiceException(BizExceptionCode.NIO_024, BizExceptionCode.NIO_024_MSG);
                }

                String familyName = new String(estelamResult3.getFamily());
                if (EmsUtil.checkString(familyName) && !familyName.equals(ConstValues.DEFAULT_NAMES_FA)) {
                    citizenTO.setSurnamePersian(new String(estelamResult3.getFamily(), "UTF-8"));
                } else {

                    throw new ServiceException(BizExceptionCode.NIO_025, BizExceptionCode.NIO_025_MSG);
                }

                String fatherName = new String(estelamResult3.getFatherName());
                if (EmsUtil.checkString(fatherName) && !fatherName.equals(ConstValues.DEFAULT_NAMES_FA)) {
                    citizenInfoTO.setFatherFirstNamePersian(new String(estelamResult3.getFatherName(), "UTF-8"));
                } else {
                    throw new ServiceException(BizExceptionCode.NIO_026, BizExceptionCode.NIO_038_MSG);
                }

                if (estelamResult3.getGender() == 0 || estelamResult3.getGender() == 1) {

                    citizenInfoTO.setGender(Gender.convertFromIMSEstelamResultGender(estelamResult3.getGender()));

                } else {
                    throw new ServiceException(BizExceptionCode.NIO_027, BizExceptionCode.NIO_039_MSG);
                }

                logger.info("\n\nCitizen : " + "\nCitizenName : "
                        + citizenTO.getFirstNamePersian() + " "
                        + citizenTO.getSurnamePersian() + ""
                        + "\n CitizenNationalId : " + citizenTO.getNationalID()
                        + "\n");
                ImsLogger.info("\n\nCitizen : " + "\nCitizenName : "
                        + citizenTO.getFirstNamePersian() + " "
                        + citizenTO.getSurnamePersian() + ""
                        + "\n CitizenNationalId : " + citizenTO.getNationalID()
                        + "\n");


                //Anbari: Save image just for requested citizen
                if (estelam3sc.getImageResult() != null) {

                    if (!checkIMSMessageReturn(estelam3sc.getImageResult().getMessage())) {
                        ImageResult imageResult = estelam3sc.getImageResult();
                        try {
                            byte[] nid_image = imageResult.getImage();
                            getImsManagementervice().saveImsEstelamImage(nationalId, citizenTO, ImsEstelamImageType.IMS_NID_IMAGE, nid_image);
                            //sessionContext.getBusinessObject(IMSManagementServiceLocal.class).saveImsEstelamImage(nationalId,citizenTO,ImsEstelamImageType.IMS_NID_IMAGE,nid_image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

            } else if (FATHER_KEY.equals(personInfoMap.get(nationalId))) {
                citizenInfoTO.setFatherNationalID(nationalId);
                // citizenInfoTO.setFatherFirstNamePersian(new
                // String(estelamResult.getName(), "UTF-8"));
                citizenInfoTO.setFatherFirstNameEnglish(ConstValues.DEFAULT_NAMES_EN);
                citizenInfoTO.setFatherSurname(ConstValues.DEFAULT_NAMES_FA);
                citizenInfoTO.setFatherFatherName(ConstValues.DEFAULT_NAMES_FA);
                citizenInfoTO.setFatherBirthCertificateId(String.valueOf(estelamResult3.getShenasnameNo()));
                // citizenInfoTO.setFatherBirthCertificateSeries(String.valueOf(estelamResult.getShenasnameSerial()));
                citizenInfoTO.setFatherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                String fBirthDate = String.valueOf(estelamResult3.getBirthDate());
                citizenInfoTO.setFatherBirthDateSolar(DateUtil.convert(
                        fBirthDate.substring(0, 4) + "/"
                                + fBirthDate.substring(4, 6) + "/"
                                + fBirthDate.substring(6, 8), DateUtil.JALALI));
                logger.info("\n\nFATHER : " + "\nFatherName : "
                        + citizenInfoTO.getFatherFirstNamePersian() + " "
                        + citizenInfoTO.getFatherSurname() + ""
                        + "\n FatherNationalId : "
                        + citizenInfoTO.getFatherNationalID() + "\n");
                ImsLogger.info("\n\nFATHER : " + "\nFatherName : "
                        + citizenInfoTO.getFatherFirstNamePersian() + " "
                        + citizenInfoTO.getFatherSurname() + ""
                        + "\n FatherNationalId : "
                        + citizenInfoTO.getFatherNationalID() + "\n");

            } else if (MOTHER_KEY.equals(personInfoMap.get(nationalId))) {
                citizenInfoTO.setMotherNationalID(nationalId);
                citizenInfoTO.setMotherFirstNamePersian(new String(estelamResult3.getName(), "UTF-8"));
                citizenInfoTO.setMotherSurname(ConstValues.DEFAULT_NAMES_FA);
                citizenInfoTO.setMotherFatherName(ConstValues.DEFAULT_NAMES_FA);
                citizenInfoTO.setMotherBirthCertificateId(String.valueOf(estelamResult3.getShenasnameNo()));
                // citizenInfoTO.setMotherBirthCertificateSeries(String.valueOf(estelamResult.getShenasnameSerial()));
                citizenInfoTO.setMotherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                String mBirthDate = String.valueOf(estelamResult3.getBirthDate());
                citizenInfoTO.setMotherBirthDateSolar(DateUtil.convert(
                        mBirthDate.substring(0, 4) + "/"
                                + mBirthDate.substring(4, 6) + "/"
                                + mBirthDate.substring(6, 8), DateUtil.JALALI));
                logger.info("\n\nMOTHER : " + "\nMotherName : "
                        + citizenInfoTO.getMotherFirstNamePersian() + " "
                        + citizenInfoTO.getMotherSurname() + ""
                        + "\n MotherNationalId : "
                        + citizenInfoTO.getMotherNationalID() + "\n");
                ImsLogger.info("\n\nMOTHER : " + "\nMotherName : "
                        + citizenInfoTO.getMotherFirstNamePersian() + " "
                        + citizenInfoTO.getMotherSurname() + ""
                        + "\n MotherNationalId : "
                        + citizenInfoTO.getMotherNationalID() + "\n");

            } else if (SPOUSE_KEY.equals(personInfoMap.get(nationalId))) {
                List<SpouseTO> spouseTOs = citizenInfoTO.getSpouses();
                for (SpouseTO spouseTO : spouseTOs) {
                    if (nationalId.equals(spouseTO.getSpouseNationalID())) {
                        spouseTO.setSpouseNationalID(nationalId);
                        spouseTO.setSpouseFirstNamePersian(new String(
                                estelamResult3.getName(), "UTF-8"));
                        spouseTO.setSpouseSurnamePersian(new String(
                                estelamResult3.getFamily(), "UTF-8"));
                        spouseTO.setSpouseFatherName(ConstValues.DEFAULT_NAMES_FA);
                        spouseTO.setSpouseBirthCertificateId(String
                                .valueOf(estelamResult3.getShenasnameNo()));
                        // spouseTO.setSpouseBirthCertificateSeries(String.valueOf(estelamResult.getShenasnameSerial()));
                        spouseTO.setSpouseBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                        String sBirthDate = String.valueOf(estelamResult3
                                .getBirthDate());
                        logger.info("\n\n SPOUSE BIRTHDATE BEFORE CONVERT : "
                                + sBirthDate + "\n");
                        ImsLogger
                                .info("\n\n SPOUSE BIRTHDATE BEFORE CONVERT : "
                                        + sBirthDate + "\n");
                        spouseTO.setSpouseBirthDate(DateUtil.convert(
                                sBirthDate.substring(0, 4) + "/"
                                        + sBirthDate.substring(4, 6) + "/"
                                        + sBirthDate.substring(6, 8),
                                DateUtil.JALALI));
                        logger.info("\n\n SPOUSE BIRTHDATE AFTER CONVERT : "
                                + DateUtil.convert(
                                spouseTO.getSpouseBirthDate(),
                                DateUtil.JALALI) + "\n");
                        ImsLogger.info("\n\n SPOUSE BIRTHDATE AFTER CONVERT : "
                                + DateUtil.convert(
                                spouseTO.getSpouseBirthDate(),
                                DateUtil.JALALI) + "\n");
                        logger.info("\n\nSPOUSE : " + "\nSpouseName : "
                                + spouseTO.getSpouseFirstNamePersian() + " "
                                + spouseTO.getSpouseSurnamePersian() + ""
                                + "\n SpouseNationalId : "
                                + spouseTO.getSpouseNationalID() + "\n");
                        ImsLogger.info("\n\nSPOUSE : " + "\nSpouseName : "
                                + spouseTO.getSpouseFirstNamePersian() + " "
                                + spouseTO.getSpouseSurnamePersian() + ""
                                + "\n SpouseNationalId : "
                                + spouseTO.getSpouseNationalID() + "\n");
                    }
                }

            } else if (CHILD_KEY.equals(personInfoMap.get(nationalId))) {
                List<ChildTO> childTOs = citizenInfoTO.getChildren();
                for (ChildTO childTO : childTOs) {
                    if (nationalId.equals(childTO.getChildNationalID())) {
                        childTO.setChildNationalID(nationalId);
                        childTO.setChildFirstNamePersian(new String(
                                estelamResult3.getName(), "UTF-8"));
                        childTO.setChildFatherName(ConstValues.DEFAULT_NAMES_FA);
                        childTO.setChildBirthCertificateId(String
                                .valueOf(estelamResult3.getShenasnameNo()));
                        // childTO.setChildBirthCertificateSeries(String.valueOf(estelamResult.getShenasnameSerial()));
                        childTO.setChildBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                        childTO.setChildGender(Gender
                                .convertFromIMSEstelamResultGender(estelamResult3
                                        .getGender()));
                        String cBirthDate = String.valueOf(estelamResult3
                                .getBirthDate());
                        childTO.setChildBirthDateSolar(DateUtil.convert(
                                cBirthDate.substring(0, 4) + "/"
                                        + cBirthDate.substring(4, 6) + "/"
                                        + cBirthDate.substring(6, 8),
                                DateUtil.JALALI));
                        logger.info("\n\nCHILD : " + "\nChildName : "
                                + childTO.getChildFirstNamePersian() + ""
                                + "\n ChildNationalId : "
                                + childTO.getChildNationalID() + "\n");
                        ImsLogger.info("\n\nCHILD : " + "\nChildName : "
                                + childTO.getChildFirstNamePersian() + ""
                                + "\n ChildNationalId : "
                                + childTO.getChildNationalID() + "\n");
                    }
                }

            }
            citizenTO.setCitizenInfo(citizenInfoTO);
            return citizenTO;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * The method getOnlineEnquiry is used to get the online enquiry from the sub system 'IMS'
     *
     * @param personEnquiryVTOs is an array of type {@link com.gam.nocr.ems.data.domain.vol.PersonEnquiryVTO} which carries the necessary attributes for the process of online enquiry
     * @return a hashmap of {@link java.util.HashMap <String, Boolean>} which carries nationalId and the result of the online enquiry(true or false)
     */
    @Override
    public HashMap<String, Boolean> getOnlineEnquiry(PersonEnquiryVTO[] personEnquiryVTOs) throws BaseException {
        StringBuilder additionalData = new StringBuilder("{");
        String[] strParameters = getOnlineParametersByProfileManager();
        String username = strParameters[0];
        String password = strParameters[1];
        String keyhanUsername = strParameters[2];
        String keyhanSerial = strParameters[3];

        HashMap<String, Boolean> returnMap = new HashMap<String, Boolean>();
        for (PersonEnquiryVTO personEnquiryVTO : personEnquiryVTOs) {
            Map<Object, Object> businessLogMap = new HashMap<Object, Object>();
            businessLogMap.put("nId", personEnquiryVTO.getNationalId());

            /**
             * The enquiryFlag is used to show the result of the enquiry
             */
            boolean enquiryFlag = true;

            PersonInfo personInfo;

            try {
                personInfo = convertToPersonInfo(personEnquiryVTO);
                if (personInfo != null) {
                    String errorMessage = null;
                    List<EstelamResult> estelamResults = getService().getEstelam2SC(username, password, keyhanUsername, keyhanSerial, personInfo);

                    if (!EmsUtil.checkListSize(estelamResults)) {
                        enquiryFlag = false;

                    } else if (EmsUtil.checkListSize(estelamResults.get(0).getMessage())) {
                        for (String errMSG : estelamResults.get(0).getMessage()) {
                            errorMessage = "\n" + errMSG;
                            enquiryFlag = false;
                        }

                        logger.error("\n=== ONLINE ENQUIRY RESULT ERROR MESSAGE ===\n");
                        logger.error(BizExceptionCode.NIO_010, BizExceptionCode.NIO_001_MSG.replace("{0}", personEnquiryVTO.getNationalId()) + ":" + errorMessage);
                        businessLogMap.put("errorMessage", errorMessage);
                    }

                } else {
                    enquiryFlag = false;
                }


            } catch (BaseException e) {
                logger.error(e.getExceptionCode(), e.getMessage(), e);
                businessLogMap.put("errorMessage", e.getMessage());
                enquiryFlag = false;
            } catch (Exception e) {
                logger.error(BizExceptionCode.NIO_011, e.getMessage(), e);
                businessLogMap.put("errorMessage", e.getMessage());
                enquiryFlag = false;
            }

            businessLogMap.put("enquiryFlag", enquiryFlag);
            additionalData.append(EmsUtil.toJSON(businessLogMap));

            if (enquiryFlag) {
                logger.info("\nThe online enquiry result for the personEnquiryVTO with nationalId '" + personEnquiryVTO.getNationalId() + "' is valid.");
            }
            returnMap.put(personEnquiryVTO.getNationalId(), enquiryFlag);

        }
        additionalData.append("}");
        createBusinessLog(BusinessLogAction.SEND_REQUEST_FOR_ONLINE_ENQUIRY, BusinessLogEntity.IMS, "System", additionalData.toString(), true);
        return returnMap;
    }

    /**
     * The method fetchDataByOnlineEnquiry is used to fetch the citizen info from the IMS sub system
     *
     * @param personEnquiryVTOs is an array of type {@link com.gam.nocr.ems.data.domain.vol.PersonEnquiryVTO} which carries the necessary attributes for fetching data from IMS
     * @return a hashmap of {@link java.util.HashMap <String, PersonEnquiryVTO>} which carries nationalId and an instance
     * of type {@link com.gam.nocr.ems.data.domain.vol.PersonEnquiryVTO}, which was valued By means of the IMS database
     */
    @Override
    public HashMap<String, PersonEnquiryVTO> fetchDataByOnlineEnquiry(PersonEnquiryVTO[] personEnquiryVTOs) throws BaseException {
        String[] strParameters = getOnlineParametersByProfileManager();
        String username = strParameters[0];
        String password = strParameters[1];
        String keyhanUsername = strParameters[2];
        String keyhanSerial = strParameters[3];


        HashMap<String, PersonEnquiryVTO> returnMap = new HashMap<String, PersonEnquiryVTO>();

        for (PersonEnquiryVTO personEnquiryVTO : personEnquiryVTOs) {
            Map<Object, Object> businessLogMap = new HashMap<Object, Object>();
            businessLogMap.put("nationalId", personEnquiryVTO.getNationalId());

            PersonInfo personInfo = new PersonInfo();
            personInfo.setNin(Long.parseLong(personEnquiryVTO.getNationalId()));
            boolean metadataFlag = false;

            try {
                List<EstelamResult> estelamResults = getService().getEstelam2SC(username, password, keyhanUsername, keyhanSerial, personInfo);
                if (EmsUtil.checkListSize(estelamResults)) {
                    if ((estelamResults.get(0).getMessage() != null) && (estelamResults.get(0).getMessage().size() > 0)) {
                        int i = 0;
                        for (String errMSG : estelamResults.get(0).getMessage()) {
                            if (errMSG != null && !errMSG.isEmpty() && !metadataFlag) {
                                businessLogMap.put("exception" + i, errMSG);
                                i++;
                                personEnquiryVTO.setMetadata(personEnquiryVTO.getNationalId());
                                metadataFlag = true;
                            }

                        }

                    } else {
                        businessLogMap.put("birthCertificateId", estelamResults.get(0).getShenasnameNo());
                        businessLogMap.put("birthDate", estelamResults.get(0).getBirthDate());
                        businessLogMap.put("birthCertificateSerial", estelamResults.get(0).getShenasnameSerial());
                        businessLogMap.put("firstName", new String(estelamResults.get(0).getName(), "UTF-8"));
                        businessLogMap.put("surName", new String(estelamResults.get(0).getFamily(), "UTF-8"));
                        businessLogMap.put("fatherName", new String(estelamResults.get(0).getFatherName(), "UTF-8"));
                        businessLogMap.put("gender", estelamResults.get(0).getGender());

                        if (estelamResults.get(0).getShenasnameNo() >= 0) {
                            if (estelamResults.get(0).getShenasnameNo() == 0) {
                                personEnquiryVTO.setBirthCertificateId(personEnquiryVTO.getNationalId());
                            } else {
                                personEnquiryVTO.setBirthCertificateId(String.valueOf(estelamResults.get(0).getShenasnameNo()));
                            }

                        } else if (!metadataFlag) {
                            personEnquiryVTO.setMetadata(personEnquiryVTO.getNationalId());
                            metadataFlag = true;
                        }

                        if (estelamResults.get(0).getBirthDate() != 0) {
                            String strDate = String.valueOf(estelamResults.get(0).getBirthDate());
                            if (strDate.length() == 8) {
                                personEnquiryVTO.setSolarBirthDate(strDate.substring(0, 4) + "/" + strDate.substring(4, 6) + "/" + strDate.substring(6));
                            } else {
                                personEnquiryVTO.setMetadata(personEnquiryVTO.getNationalId());
                                metadataFlag = true;
                            }
                        } else if (!metadataFlag) {
                            personEnquiryVTO.setMetadata(personEnquiryVTO.getNationalId());
                            metadataFlag = true;
                        }

//                        if (estelamResults.get(0).getShenasnameSerial() != 0) {
//                            personEnquiryVTO.setBirthCertificateSerial(ConstValues.DEFAULT_CERT_SERIAL);
//                        } else if (!metadataFlag) {
//                            personEnquiryVTO.setMetadata(personEnquiryVTO.getNationalId());
//                            metadataFlag = true;
//                        }
                        personEnquiryVTO.setBirthCertificateSerial(ConstValues.DEFAULT_CERT_SERIAL);

                        if (EmsUtil.checkString(new String(estelamResults.get(0).getName()))) {
                            personEnquiryVTO.setFirstName(SecureString.getSecureString(new String(estelamResults.get(0).getName(), "UTF-8").trim()));
                        } else if (!metadataFlag) {
                            personEnquiryVTO.setMetadata(personEnquiryVTO.getNationalId());
                            metadataFlag = true;
                        }

                        if (EmsUtil.checkString(new String(estelamResults.get(0).getFamily()))) {
                            personEnquiryVTO.setLastName(SecureString.getSecureString(new String(estelamResults.get(0).getFamily(), "UTF-8").trim()));
                        } else if (!metadataFlag) {
                            personEnquiryVTO.setMetadata(personEnquiryVTO.getNationalId());
                            metadataFlag = true;
                        }

                        if (EmsUtil.checkString(new String(estelamResults.get(0).getFatherName()))) {
                            personEnquiryVTO.setFatherName(SecureString.getSecureString(new String(estelamResults.get(0).getFatherName(), "UTF-8").trim()));
                        } else if (!metadataFlag) {
                            personEnquiryVTO.setMetadata(personEnquiryVTO.getNationalId());
                            metadataFlag = true;
                        }

                        if (estelamResults.get(0).getGender() == 0 ||
                                estelamResults.get(0).getGender() == 1) {
                            logger.info("\nThe IMS Gender is : " + estelamResults.get(0).getGender() + "\n");
                            personEnquiryVTO.setGender((Gender.convertFromIMSEstelamResultGender(estelamResults.get(0).getGender())));
                            logger.info("\nThe personEnquiryVTO Gender is : " + personEnquiryVTO.getGender() + "\n");
                        } else if (!metadataFlag) {
                            personEnquiryVTO.setMetadata(personEnquiryVTO.getNationalId());
                        }

                    }
                    returnMap.put(personEnquiryVTO.getNationalId(), personEnquiryVTO);

                } else {
                    throw new ServiceException(BizExceptionCode.NIO_019, BizExceptionCode.NIO_019_MSG);
                }

            } catch (Exception e) {
                if (e instanceof BaseException) {
                    businessLogMap.put("exception", ((BaseException) e).getExceptionCode() + ":" + e.getMessage());
                    logger.error(((BaseException) e).getExceptionCode(), e.getMessage(), e);
                    ImsLogger.error(((BaseException) e).getExceptionCode(), e.getMessage(), e);
                } else {
                    businessLogMap.put("exception", BizExceptionCode.NIO_012 + ":" + e.getMessage());
                    logger.error(BizExceptionCode.NIO_012, e.getMessage(), e);
                    ImsLogger.error(BizExceptionCode.NIO_012, e.getMessage(), e);
                }
                personEnquiryVTO.setMetadata(personEnquiryVTO.getNationalId());
                returnMap.put(personEnquiryVTO.getNationalId(), personEnquiryVTO);
            }

            String additionalData = EmsUtil.convertStringToJSONFormat("imsFetchInfo", businessLogMap);
            createBusinessLog(BusinessLogAction.FETCH_CITIZEN_INFO_FROM_IMS, BusinessLogEntity.IMS, "System", additionalData, true);
        }

        return returnMap;

    }

    /**
     * The method fetchCitizenInfoByOnlineEnquiry is used to fetch the citizen information, which is signed as valid data on half of IMS sub system
     *
     * @param citizenTO is an instance of type {@link com.gam.nocr.ems.data.domain.CitizenTO}
     * @return an instance of type {@link com.gam.nocr.ems.data.domain.CitizenTO}
     * @throws com.gam.commons.core.BaseException
     */

    //Anbari : Modified for new IMS Estelam mechanism
    @Override
    public CitizenTO fetchCitizenInfoByOnlineEnquiry(CitizenTO citizenTO) throws BaseException {

        HashMap<String, String> personInfoMap = new HashMap<String, String>();
        try {
            List<PersonInfo> personInfoList = new ArrayList<PersonInfo>();
            PersonInfo citizenPersonInfo = new PersonInfo();
            citizenPersonInfo.setNin(Long.parseLong(citizenTO.getNationalID()));
            personInfoMap.put(citizenTO.getNationalID(), CITIZEN_KEY);
            personInfoList.add(citizenPersonInfo);

//            if (!ConstValues.DEFAULT_NID.equals(citizenTO.getCitizenInfo().getFatherNationalID())) {
//                PersonInfo fatherPersonInfo = new PersonInfo();
//                fatherPersonInfo.setNin(Long.parseLong(citizenTO.getCitizenInfo().getFatherNationalID()));
//                personInfoMap.put(citizenTO.getCitizenInfo().getFatherNationalID(), FATHER_KEY);
//                personInfoList.add(fatherPersonInfo);
//            }
//
//            if (!ConstValues.DEFAULT_NID.equals(citizenTO.getCitizenInfo().getMotherNationalID())) {
//                PersonInfo motherPersonInfo = new PersonInfo();
//                motherPersonInfo.setNin(Long.parseLong(citizenTO.getCitizenInfo().getMotherNationalID()));
//                personInfoMap.put(citizenTO.getCitizenInfo().getMotherNationalID(), MOTHER_KEY);
//                personInfoList.add(motherPersonInfo);
//            }
//
//            List<SpouseTO> spouseTOList = citizenTO.getCitizenInfo().getSpouses();
//            if (spouseTOList != null) {
//                for (SpouseTO spouseTO : spouseTOList) {
//                    if (!ConstValues.DEFAULT_NID.equals(spouseTO.getSpouseNationalID())) {
//                        PersonInfo spousePersonInfo = new PersonInfo();
//                        spousePersonInfo.setNin(Long.parseLong(spouseTO.getSpouseNationalID()));
//                        personInfoMap.put(spouseTO.getSpouseNationalID(), SPOUSE_KEY);
//                        personInfoList.add(spousePersonInfo);
//                    }
//                }
//            }
//
//            List<ChildTO> childTOList = citizenTO.getCitizenInfo().getChildren();
//            if (childTOList != null) {
//                for (ChildTO childTO : citizenTO.getCitizenInfo().getChildren()) {
//                    if (!ConstValues.DEFAULT_NID.equals(childTO.getChildNationalID())) {
//                        PersonInfo childPersonInfo = new PersonInfo();
//                        childPersonInfo.setNin(Long.parseLong(childTO.getChildNationalID()));
//                        personInfoMap.put(childTO.getChildNationalID(), CHILD_KEY);
//                        personInfoList.add(childPersonInfo);
//                    }
//                }
//            }

            String[] strParameters = getOnlineParametersByProfileManager();
            String username = strParameters[0];
            String password = strParameters[1];
            String keyhanUsername = strParameters[2];
            String keyhanSerial = strParameters[3];

            for (PersonInfo personInfo : personInfoList) {
                String nin = EmsUtil.convertLongNidToString(personInfo.getNin());
                String key = personInfoMap.get(nin);

                ///Anbari: Estelam3SC
                EstelamPort service = getService();
                EstelamResultC estelam3sc = service.getEstelam3SC(username, password, keyhanUsername, keyhanSerial, personInfo);

                if (CITIZEN_KEY.equals(key)) {

                    if (estelam3sc == null) {
                        throw new BaseException(BizExceptionCode.NOCR_IMS_01, BizExceptionCode.NOCR_IMS_01_MSG);
                    }

                    if (estelam3sc.getEstelamResult3() == null || estelam3sc.getEstelamResult3().size() == 0) {
                        throw new BaseException(BizExceptionCode.NOCR_IMS_03, BizExceptionCode.NOCR_IMS_03_MSG);
                    }
                    List<String> estelam3scMessage = estelam3sc.getMessage();
                    if (checkIMSMessageReturn(estelam3scMessage)) {
                        if (estelam3scMessage.contains("IS_EXCEPTED")) {
                            String exceptionMessage = estelam3sc.getEstelamResult3().get(0).getExceptionMessage();
                            if (checkIMSExceptionMessageReturn(exceptionMessage)) {

                                List<String> allCode = getExceptionCodeDAO().getAll();
                                List<String> matchedCode = JsonPath.read(exceptionMessage, "$..code");
                                Boolean isExcepted = false;
                                for (String code : matchedCode) {
                                    if (allCode.contains(code)) {
                                        isExcepted = true;
                                        break;
                                    }
                                }
                                if (isExcepted)
                                    throw new BaseException(BizExceptionCode.NOCR_IMS_05, estelam3sc.getEstelamResult3().get(0).getExceptionMessage());

                            }
                        }
                    }
                    if (estelam3scMessage.contains("Nin.Not.Valid") || estelam3scMessage.contains("result.rec.invisible") || estelam3scMessage.contains("result.rec.review") || estelam3scMessage.contains("result.rec.return")) {
                        throw new BaseException(BizExceptionCode.NOCR_IMS_05, estelam3sc.getEstelamResult3().get(0).getExceptionMessage());

                    }

//    				if(checkIMSMessageReturn(estelam3sc.getEstelamResult3().get(0).getMessage()))
//    				{
//    					if(checkIMSExceptionMessageReturn(estelam3sc.getEstelamResult3().get(0).getExceptionMessage()) || estelam3sc.getEstelamResult3().get(0).getMessage().contains("IS_EXCEPTED"))
//    							throw new BaseException(BizExceptionCode.NOCR_IMS_05,estelam3sc.getEstelamResult3().get(0).getExceptionMessage());											
//    					
//    				}				

                    EstelamResult3 estelamResult3 = estelam3sc.getEstelamResult3().get(0);

                    if (estelamResult3 == null) {
                        throw new ServiceException(BizExceptionCode.NOCR_IMS_02, BizExceptionCode.NIO_032_MSG);
                    }

                    if (estelamResult3.getDeathStatus() == 1) // is Dead
                    {
                        throw new BaseException(BizExceptionCode.NOCR_IMS_06, BizExceptionCode.NOCR_IMS_05_MSG);
                    }
                    //Save Citizen Date
                    citizenTO = setCitizenDataNew(personInfoMap, citizenTO, estelam3sc);
                }
//            	else if (FATHER_KEY.equals(key) || MOTHER_KEY.equals(key) || SPOUSE_KEY.equals(key) || CHILD_KEY.equals(key)){ //relatives
//            		boolean foundException = checkCitizenInfoIMSValidatipn(estelam3sc);            		
//                    if (foundException){
//                    	logger.error("\n=================going to remove invalid data before return to ccos==========================\n");
//                    	removeInvalidCitizenData(citizenTO, key, nin);
//                    }
//                    else
//                    {
//                    	setCitizenDataNew(personInfoMap, citizenTO, estelam3sc);
//                    }
//                    
//            	}
                else {
                    String errormessage = " THIS IS A BUG - unknown key: " + key;
                    logger.error("\n========================= " + errormessage + " ======================================\n");
                    throw new ServiceException(BizExceptionCode.NIO_030, BizExceptionCode.NIO_030_MSG + errormessage);
                }

            }

        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.NIO_018, BizExceptionCode.NIO_018_MSG, e);
        }
        return citizenTO;
    }

    private Boolean checkCitizenInfoIMSValidatipn(EstelamResultC estelam3sc) throws BaseException, ServiceException {
        if (estelam3sc == null) {
            return false;
        }
        List<String> estelam3scMessage = estelam3sc.getMessage();
        if (checkIMSMessageReturn(estelam3scMessage)) {
            return false;
        }
        if (estelam3sc.getEstelamResult3() == null || estelam3sc.getEstelamResult3().size() == 0) {
            return false;
        }

        EstelamResult3 estelamResult3 = estelam3sc.getEstelamResult3().get(0);

        if (estelamResult3 == null) {
            return false;
        }

        return true;

    }

    private void removeInvalidCitizenData(CitizenTO citizenTO, String key,
                                          String nationalId) throws BaseException {
        try {
            CitizenInfoTO citizenInfoTO = citizenTO.getCitizenInfo();
            if (FATHER_KEY.equals(key)) {
                citizenInfoTO.setFatherNationalID(ConstValues.DEFAULT_NID);
//				citizenInfoTO
//						.setFatherFirstNamePersian(ConstValues.DEFAULT_NAMES_FA);
                citizenInfoTO
                        .setFatherFirstNameEnglish(ConstValues.DEFAULT_NAMES_EN);
                citizenInfoTO.setFatherSurname(ConstValues.DEFAULT_NAMES_FA);
                citizenInfoTO.setFatherFatherName(ConstValues.DEFAULT_NAMES_FA);
                citizenInfoTO
                        .setFatherBirthCertificateId(ConstValues.DEFAULT_NUMBER);
                citizenInfoTO
                        .setFatherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                citizenInfoTO.setFatherBirthDateSolar(DateUtil.convert(
                        ConstValues.DEFAULT_DATE, DateUtil.JALALI));
            } else if (MOTHER_KEY.equals(key)) {
                citizenInfoTO.setMotherNationalID(ConstValues.DEFAULT_NID);
//				citizenInfoTO
//						.setMotherFirstNamePersian(ConstValues.DEFAULT_NAMES_FA);
                citizenInfoTO.setMotherSurname(ConstValues.DEFAULT_NAMES_FA);
                citizenInfoTO.setMotherFatherName(ConstValues.DEFAULT_NAMES_FA);
                citizenInfoTO
                        .setMotherBirthCertificateId(ConstValues.DEFAULT_NUMBER);
                citizenInfoTO
                        .setMotherBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
                citizenInfoTO.setMotherBirthDateSolar(DateUtil.convert(
                        ConstValues.DEFAULT_DATE, DateUtil.JALALI));
            } else if (CHILD_KEY.equals(key)) {
                List<ChildTO> childTOs = citizenInfoTO.getChildren();
                List<ChildTO> toBeRemoved = new ArrayList<ChildTO>();
                for (ChildTO childTO : childTOs) {
                    if (nationalId.equals(childTO.getChildNationalID())) {
                        toBeRemoved.add(childTO);
//						childTO.setChildNationalID(ConstValues.DEFAULT_NID);
//						childTO.setChildFirstNamePersian(ConstValues.DEFAULT_NAMES_FA);
//						childTO.setChildFatherName(ConstValues.DEFAULT_NAMES_FA);
//						childTO.setChildBirthCertificateId(ConstValues.DEFAULT_NUMBER);
//						childTO.setChildBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
//						childTO.setChildGender(Gender.valueOf(ConstValues.DEFAULT_GENDER));
//						childTO.setChildBirthDateSolar(DateUtil.convert(
//								ConstValues.DEFAULT_DATE, DateUtil.JALALI));
                    }
                }
                for (ChildTO childTO : toBeRemoved) {
                    childTOs.remove(childTO);
                }
                if (childTOs.isEmpty()) {
                    citizenInfoTO.setChildren(null);
                } else {
                    citizenInfoTO.setChildren(childTOs);
                }
            } else if (SPOUSE_KEY.equals(key)) {
                List<SpouseTO> spouseTOs = citizenInfoTO.getSpouses();
                List<SpouseTO> toBeRemoved = new ArrayList<SpouseTO>();
                for (SpouseTO spouseTO : spouseTOs) {
                    if (nationalId.equals(spouseTO.getSpouseNationalID())) {
                        toBeRemoved.add(spouseTO);
//						spouseTO.setSpouseNationalID(ConstValues.DEFAULT_NID);
//						spouseTO.setSpouseFirstNamePersian(ConstValues.DEFAULT_NAMES_FA);
//						spouseTO.setSpouseSurnamePersian(ConstValues.DEFAULT_NAMES_FA);
//						spouseTO.setSpouseFatherName(ConstValues.DEFAULT_NAMES_FA);
//						spouseTO.setSpouseBirthCertificateId(ConstValues.DEFAULT_NUMBER);
//						spouseTO.setSpouseBirthCertificateSeries(ConstValues.DEFAULT_CERT_SERIAL);
//						spouseTO.setSpouseDeathOrDivorceDate(DateUtil.convert(
//								ConstValues.DEFAULT_DATE, DateUtil.JALALI));
//						spouseTO.setSpouseMarriageDate(DateUtil.convert(
//								ConstValues.DEFAULT_DATE, DateUtil.JALALI));
//						spouseTO.setSpouseBirthDate(DateUtil.convert(
//								ConstValues.DEFAULT_DATE, DateUtil.JALALI));
                    }
                }
                for (SpouseTO spouseTO : toBeRemoved) {
                    spouseTOs.remove(spouseTO);
                }
                if (spouseTOs.isEmpty()) {
                    citizenInfoTO.setSpouses(null);
                } else {
                    citizenInfoTO.setSpouses(spouseTOs);
                }
            }
            citizenTO.setCitizenInfo(citizenInfoTO);
        } catch (Exception e) {
            throw new ServiceException(BizExceptionCode.NIO_031, BizExceptionCode.NIO_031_MSG + e.getMessage(), e);
        }
    }


    //Anbari: Estelam3
    @Override
    public PersonEnquiryVTO fetchDataByOnlineEnquiryByEstelam3(
            PersonEnquiryVTO personEnquiryVTOInput, String citizenNID) throws BaseException {

        String[] strParameters = getOnlineParametersByProfileManager();
        String username = strParameters[0];
        String password = strParameters[1];
        String keyhanUsername = strParameters[2];
        String keyhanSerial = strParameters[3];
        PersonInfo personInfo = new PersonInfo();
        PersonEnquiryVTO personEnquiryVTOResult = new PersonEnquiryVTO();
        personInfo.setNin(Long.parseLong(personEnquiryVTOInput.getNationalId()));


        boolean metadataFlag = false;
        boolean isAllowableExcepted = false;

        try {
            EstelamPort service = getService();
            EstelamResultC estelam3sc = service.getEstelam3SC(username, password, keyhanUsername, keyhanSerial, personInfo);
            if (estelam3sc == null) {

                personEnquiryVTOResult.setIsEstelamCorrupt(true);
                personEnquiryVTOResult.setDescription(BizExceptionCode.NOCR_IMS_01_MSG);
                return personEnquiryVTOResult;
                //throw new BaseException(BizExceptionCode.NOCR_IMS_01,BizExceptionCode.NOCR_IMS_01_MSG);
            }

            List<String> estelam3scMessage = estelam3sc.getMessage();
            if (checkIMSMessageReturn(estelam3scMessage)) {
                //personEnquiryVTOResult.setIsEstelamCorrupt(true);
                personEnquiryVTOResult.setDescription(BizExceptionCode.NOCR_IMS_02_MSG + ":" + estelam3scMessage.get(0));
                personEnquiryVTOResult.setMetadata(personEnquiryVTOInput.getNationalId());//set MetaData

//				if (estelam3sc.getEstelamResult3() != null	&& estelam3sc.getEstelamResult3().size() > 0)
//					{
//					if (checkIMSMessageReturn(estelam3sc.getEstelamResult3().get(0).getMessage()))
//						{
//							if (estelam3sc.getEstelamResult3().get(0).getMessage().contains("IS_EXCEPTED")) {

                if (estelam3scMessage.contains("IS_EXCEPTED")) {
                    if (personEnquiryVTOInput.getNationalId().equals(citizenNID)) {
                        String exceptionMessage = estelam3sc.getEstelamResult3().get(0).getExceptionMessage();
                        if (checkIMSExceptionMessageReturn(exceptionMessage)) {

                            List<String> allCode = getExceptionCodeDAO().getAll();
                            List<String> matchedCode = JsonPath.read(exceptionMessage, "$..code");
                            Boolean isExcepted = false;
                            for (String code : matchedCode) {
                                if (allCode.contains(code)) {
                                    isExcepted = true;
                                    break;
                                }
                            }
                            if (isExcepted) {
                                //personEnquiryVTOResult.setIsEstelamCorrupt(true);
                                personEnquiryVTOResult.setIsExceptionMessage(true);
                                personEnquiryVTOResult.setDescription(exceptionMessage);
                                return personEnquiryVTOResult;
                            } else {
                                isAllowableExcepted = true;
                                personEnquiryVTOResult.setMetadata("");
                            }
                        }
                    } else {
                        personEnquiryVTOResult = setPersonEnquiryLogInfo(
                                personEnquiryVTOInput, estelam3sc
                                        .getEstelamResult3().get(0)
                                        .getMessage());
                        // returnMap.put(personEnquiryVTO.getNationalId(),personEnquiryVTO);
                    }
                }//

                if (estelam3scMessage.contains("Nin.Not.Valid") || estelam3scMessage.contains("result.rec.invisible") || estelam3scMessage.contains("result.rec.review") || estelam3scMessage.contains("result.rec.return")) {
                    personEnquiryVTOResult.setIsExceptionMessage(true);
                    personEnquiryVTOResult.setDescription(estelam3scMessage.get(0));
                    return personEnquiryVTOResult;
                }

                if (estelam3scMessage.contains("err.record.not.found")) {
                    personEnquiryVTOResult.setIsRecordNotFound(true);
                    personEnquiryVTOResult.setDescription(estelam3scMessage.get(0));
                    return personEnquiryVTOResult;
                }


//							} else {
//								personEnquiryVTOResult.setIsEstelamCorrupt(true);
//								//personEnquiryVTOResult.setLogInfo(BizExceptionCode.NOCR_IMS_04_MSG);
//								//personEnquiryVTOResult.setDescription(BizExceptionCode.NOCR_IMS_04_MSG);
//								return personEnquiryVTOResult;
//								// throw new
//								// BaseException(BizExceptionCode.NOCR_IMS_04,
//								// BizExceptionCode.NOCR_IMS_04_MSG);
//							}

//						}
//					}				
                if (!isAllowableExcepted)
                    return personEnquiryVTOResult;
                //throw new BaseException(BizExceptionCode.NOCR_IMS_02,estelam3scMessage.get(0));
            }


            if (estelam3sc.getEstelamResult3() == null || estelam3sc.getEstelamResult3().size() == 0) {
                personEnquiryVTOResult.setIsEstelamCorrupt(true);
                personEnquiryVTOResult.setDescription(BizExceptionCode.NOCR_IMS_03_MSG);
                return personEnquiryVTOResult;
                //throw new BaseException(BizExceptionCode.NOCR_IMS_03, BizExceptionCode.NOCR_IMS_03_MSG);
            }


            EstelamResult3 estelamResult3 = estelam3sc.getEstelamResult3().get(0);
            if (estelamResult3.getDeathStatus() == 1 && personEnquiryVTOInput.getNationalId().equals(citizenNID)) // is Dead
            {
                //personEnquiryVTOResult.setIsEstelamCorrupt(true);
                personEnquiryVTOResult.setIsDead(estelamResult3.getDeathStatus());// store deathStatus
                personEnquiryVTOResult.setDescription(BizExceptionCode.NOCR_IMS_05_MSG);
                return personEnquiryVTOResult;
            }

            //Save image just for requested citizen
            if (estelam3sc.getImageResult() != null && personEnquiryVTOInput.getNationalId().equals(citizenNID)) {

                if (checkIMSMessageReturn(estelam3sc.getImageResult().getMessage()))

                    //personEnquiryVTOResult = setPersonEnquiryLogInfoImage(personEnquiryVTOInput,estelam3sc.getImageResult().getMessage());
                    personEnquiryVTOResult.setImageDescription(estelam3sc.getImageResult().getMessage().get(0));
                else {
                    ImageResult imageResult = estelam3sc.getImageResult();
                    if (imageResult.getImage() == null)
                        personEnquiryVTOResult.setImageDescription(BizExceptionCode.NOCR_IMS_07_MSG);
                    personEnquiryVTOResult.setNidImage(imageResult.getImage());
                }

            }

            if (estelamResult3.getShenasnameNo() >= 0) {
                if (estelamResult3.getShenasnameNo() == 0) {
                    personEnquiryVTOResult.setBirthCertificateId(personEnquiryVTOInput
                            .getNationalId());
                } else {
                    personEnquiryVTOResult.setBirthCertificateId(String
                            .valueOf(estelamResult3.getShenasnameNo()));
                }

            } else if (!metadataFlag) {
                personEnquiryVTOResult.setDescription("The IMS ShenasnameNo is :"
                        + estelamResult3.getShenasnameNo());
                personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_021);
                personEnquiryVTOResult.setMetadata(personEnquiryVTOInput
                        .getNationalId());
                metadataFlag = true;
            }

            if (estelamResult3.getBirthDate() != 0) {
                String strDate = String.valueOf(estelamResult3.getBirthDate());
                //validate of birthDate
                if (checkValidateDate(strDate)) {

                    personEnquiryVTOResult.setSolarBirthDate(strDate.substring(
                            0, 4)
                            + "/"
                            + strDate.substring(4, 6)
                            + "/"
                            + strDate.substring(6));

                } else {
                    personEnquiryVTOResult
                            .setDescription("The IMS BirthDate is :"
                                    + estelamResult3.getBirthDate());
                    personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_022);
                    personEnquiryVTOResult.setMetadata(personEnquiryVTOInput
                            .getNationalId());
                    metadataFlag = true;
                }
            } else if (!metadataFlag) {
                personEnquiryVTOResult.setDescription("The IMS BirthDate is :"
                        + estelamResult3.getBirthDate());
                personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_022);
                personEnquiryVTOResult.setMetadata(personEnquiryVTOInput
                        .getNationalId());
                metadataFlag = true;
            }

            //Shenasname Serial
            //Validate ShenasnameSerial
            String serialShenasname = String.valueOf(estelamResult3.getShenasnameSerial());
            if (serialShenasname.length() == 6 && !serialShenasname.equals("000000")) {
                personEnquiryVTOResult.setBirthCertificateSerial(String
                        .valueOf(estelamResult3.getShenasnameSerial()));
            } else if (!metadataFlag) {
                personEnquiryVTOResult
                        .setDescription("The IMS ShenasnameSerial is :"
                                + estelamResult3.getShenasnameSerial());
                personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_023);
                personEnquiryVTOResult.setMetadata(personEnquiryVTOInput
                        .getNationalId());
                metadataFlag = true;
            }

            //Anbari: for gettting Shenasname seri from IMS
//				if (estelamResult3.getShenasnameSerial() != 0) {
//					personEnquiryVTOResult.setBirthCertificateSeries(String
//							.valueOf(estelamResult3.getShenasnameSeri()));
//				} else if (!metadataFlag) {
//					personEnquiryVTOResult
//							.setDescription("The IMS ShenasnameSeri is :"
//									+ estelamResult3.getShenasnameSeri());
//					personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_033);
//					personEnquiryVTOResult.setMetadata(personEnquiryVTOInput.getNationalId());
//					metadataFlag = true;
//				}

            String firstName = new String(estelamResult3.getName());
            if (EmsUtil.checkString(firstName) && !firstName.equals(ConstValues.DEFAULT_NAMES_FA)) {

                personEnquiryVTOResult.setFirstName(new String(estelamResult3
                        .getName(), "UTF-8").trim());
            } else if (!metadataFlag) {
                personEnquiryVTOResult.setMetadata(personEnquiryVTOInput
                        .getNationalId());
                personEnquiryVTOResult.setDescription("The IMS Name is :"
                        + new String(estelamResult3.getName()));
                personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_024);
                personEnquiryVTOResult.setMetadata(personEnquiryVTOInput
                        .getNationalId());
                metadataFlag = true;
//					throw new ServiceException(BizExceptionCode.NIO_024,
//							BizExceptionCode.NIO_024_MSG);
            }

            String familyName = new String(estelamResult3.getFamily());
            if (EmsUtil.checkString(familyName) && !familyName.equals(ConstValues.DEFAULT_NAMES_FA)) {

                personEnquiryVTOResult.setLastName(new String(estelamResult3
                        .getFamily(), "UTF-8").trim());
            } else if (!metadataFlag) {
                personEnquiryVTOResult.setMetadata(personEnquiryVTOInput
                        .getNationalId());
                personEnquiryVTOResult.setDescription("The IMS Family is :"
                        + new String(estelamResult3.getFamily()));
                personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_025);
                personEnquiryVTOResult.setMetadata(personEnquiryVTOInput
                        .getNationalId());
                metadataFlag = true;

//					throw new ServiceException(BizExceptionCode.NIO_025,
//							BizExceptionCode.NIO_025_MSG);
            }

            String fatherName = new String(estelamResult3.getFatherName());
            if (EmsUtil.checkString(fatherName) && !fatherName.equals(ConstValues.DEFAULT_NAMES_FA)) {
                personEnquiryVTOResult.setFatherName(new String(estelamResult3
                        .getFatherName(), "UTF-8").trim());
            } else if (!metadataFlag) {
                personEnquiryVTOResult.setDescription("The IMS FatherName is: "
                        + new String(estelamResult3.getFatherName()));
                personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_026);
                personEnquiryVTOResult.setMetadata(personEnquiryVTOInput
                        .getNationalId());
                metadataFlag = true;
            }

            if (estelamResult3.getGender() == 0
                    || estelamResult3.getGender() == 1) {
                logger.info("\nThe IMS Gender is : "
                        + estelamResult3.getGender() + "\n");
                estelam2Logger.info("\nThe IMS Gender is : "
                        + estelamResult3.getGender() + "\n");
                personEnquiryVTOResult.setGender((Gender
                        .convertFromIMSEstelamResultGender(estelamResult3
                                .getGender())));
                logger.info("\nThe personEnquiryVTO Gender is : "
                        + personEnquiryVTOInput.getGender() + "\n");
                estelam2Logger.info("\nThe personEnquiryVTO Gender is : "
                        + personEnquiryVTOInput.getGender() + "\n");
            } else if (!metadataFlag) {
                personEnquiryVTOResult.setDescription("IMS Gender is : "
                        + estelamResult3.getGender()
                        + ";\n The person Gender is : "
                        + personEnquiryVTOInput.getGender());
                personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_027);
                personEnquiryVTOResult.setMetadata(personEnquiryVTOInput
                        .getNationalId());
                metadataFlag = true;
            }

            //returnMap.put(personEnquiryVTO.getNationalId(),	personEnquiryVTO);
        }
        //Anbari: throw NOCR_IMS_05
        catch (Exception e) {

            logger.error(BizExceptionCode.NIO_012, e.getMessage(), e);
            estelam2Logger.error(BizExceptionCode.NIO_012, e.getMessage(), e);
            personEnquiryVTOResult.setIsServiceDown(true);
            personEnquiryVTOResult.setLogInfo(e.getMessage());
            personEnquiryVTOResult.setDescription(e.getMessage());


//				if(e instanceof BaseException)
//				{
//					String exceptionCode = ((BaseException) e).getExceptionCode();
//					
//					if( exceptionCode != null && (BizExceptionCode.NOCR_IMS_05.equals(exceptionCode)) ||  (BizExceptionCode.NOCR_IMS_02.equals(exceptionCode)) ) 
//							throw (BaseException) e;
//					else
//					{
//						businessLogMap.put("exception",((BaseException) e).getExceptionCode() + ":"	+ e.getMessage());
//						logger.error(((BaseException) e).getExceptionCode(),e.getMessage(), e);
//						estelam2Logger.error(((BaseException) e).getExceptionCode(),e.getMessage(), e);
//						personEnquiryVTOResult.setLogInfo(((BaseException) e).getExceptionCode());
//						
//					}
//				}
//				else
//				{
//					businessLogMap.put("exception", BizExceptionCode.NIO_012 + ":" + e.getMessage());
//					logger.error(BizExceptionCode.NIO_012, e.getMessage(), e);
//					estelam2Logger.error(BizExceptionCode.NIO_012,e.getMessage(), e);				
//				}				
        }

        // Anbari bussLog commited
        //String additionalData = EmsUtil.convertStringToJSONFormat("imsFetchInfo", businessLogMap);
        //createBusinessLog(BusinessLogAction.FETCH_CITIZEN_INFO_FROM_IMS,BusinessLogEntity.IMS, "System", additionalData, true);

//			if (personEnquiryVTOInput.getNationalId().equals(citizenNID)) {
//				// PersonEnquiryVTO person = returnMap.get(personEnquiryVTO
//				// .getNationalId());
//				if (personEnquiryVTOInput.getMetadata() != null)
//					break;
//			}		
        //return returnMap;
        return personEnquiryVTOResult;

    }


    // Anbari:IMS
    private boolean checkValidateDate(String strDate) {

        if (strDate.length() == 8) {

            if (strDate.equals("10000000"))
                return false;

            String year = strDate.substring(0, 4);
            String mounth = strDate.substring(4, 6);
            String day = strDate.substring(6, 8);

            if (year.equals("1000") || mounth.equals("00") || day.equals("00"))
                return false;
            else
                return true;

        }

        return false;
    }

    private boolean checkIMSExceptionMessageReturn(String exceptionMessage) {
        return exceptionMessage != null && !"".equals(exceptionMessage) && !" ".equals(exceptionMessage);
    }

    private boolean checkIMSMessageReturn(List<String> message) {
        return message != null && message.size() > 0 && !message.get(0).equals(" ") && !message.get(0).equals("");
    }

    //Anbari
    PersonEnquiryVTO setPersonEnquiryLogInfo(PersonEnquiryVTO personEnquiryVTO, List<String> messageList) {
        int i = 0;
        String messages = "";
        Map<Object, Object> businessLogMap = new HashMap<Object, Object>();
        for (String errMSG : messageList) {
            if (errMSG != null && !errMSG.isEmpty()) {
                businessLogMap.put("exception" + i, errMSG);
                i++;
                messages += "exception" + i + ":" + errMSG + ";";
                personEnquiryVTO.setLogInfo(BizExceptionCode.NIO_028);
                personEnquiryVTO.setMetadata(personEnquiryVTO.getNationalId());
            }

        }
        personEnquiryVTO.setDescription(messages);
        return personEnquiryVTO;
    }

    //Anbari
//		PersonEnquiryVTO setPersonEnquiryLogInfoImage(PersonEnquiryVTO personEnquiryVTO, List<String> messageList)
//		{
//			int i = 0;
//			String messages="";
//			Map<Object, Object> businessLogMap = new HashMap<Object, Object>();
//			for (String errMSG : messageList) {
//				if (errMSG != null && !errMSG.isEmpty()) {
//					businessLogMap.put("exception" + i, errMSG);
//					i++;
//					messages+="exception" + i+":"+errMSG+";";
//					personEnquiryVTO.setLogInfo(BizExceptionCode.NIO_032);
//				}
//
//			}
//			personEnquiryVTO.setDescription(messages);
//			return personEnquiryVTO;		
//		}

    public PersonEnquiryWTO fetchDataByOnlineEnquiryAndCheck(String nationalId, boolean check) throws BaseException {
        try {
            PersonEnquiryWTO personEnquiryVTOResult = fetchDataByOnlineEnquiry(nationalId);
            if (check) {
                if (personEnquiryVTOResult.getIsServiceDown() || personEnquiryVTOResult.getIsEstelamCorrupt()
                        || personEnquiryVTOResult.getIsExceptionMessage() || personEnquiryVTOResult.getIsDead() == 1) {
                    if (personEnquiryVTOResult.getIsExceptionMessage()) {
                        throw new ServiceException(BizExceptionCode.NIO_034, personEnquiryVTOResult.getDescription());
                    }
                    if (personEnquiryVTOResult.getIsDead() == 1) {
                        throw new ServiceException(BizExceptionCode.NOCR_IMS_05, personEnquiryVTOResult.getDescription());
                    }
                    if (personEnquiryVTOResult.getIsServiceDown() || personEnquiryVTOResult.getIsEstelamCorrupt()) {
                        throw new ServiceException(BizExceptionCode.NIO_002, personEnquiryVTOResult.getDescription());
                    }
                    throw new ServiceException(BizExceptionCode.NIO_035, BizExceptionCode.NIO_001_MSG, new Object[]{nationalId});
                }
            }
            if(personEnquiryVTOResult.getDescription().contains("login.invalid.user")){
                throw new ServiceException(BizExceptionCode.NIO_040, BizExceptionCode.NIO_040_MSG, new Object[]{nationalId});
            }
            return personEnquiryVTOResult;

        } catch (BaseException e) {
            ImsLogger.error(e.getMessage(), e);
            throw e;

        } catch (Exception e) {
            ImsLogger.error(e.getMessage(), e);
            throw new ServiceException(BizExceptionCode.NIO_033, BizExceptionCode.NIO_001_MSG, e, new Object[]{nationalId});
        }
    }

    public PersonEnquiryWTO fetchDataByOnlineEnquiry(String nationalId) throws BaseException {
        String[] strParameters = getOnlineParametersByProfileManager();
        String username = strParameters[0];
        String password = strParameters[1];
        String keyhanUsername = strParameters[2];
        String keyhanSerial = strParameters[3];
        PersonInfo personInfo = new PersonInfo();
        PersonEnquiryWTO personEnquiryVTOResult = new PersonEnquiryWTO();
        personEnquiryVTOResult.setNationalId(nationalId);
        personInfo.setNin(Long.parseLong(nationalId));

        boolean metadataFlag = false;
        boolean isAllowableExcepted = false;
        try {
            EstelamPort service = getService();
            EstelamResultC estelam3sc = service.getEstelam3SC(username, password, keyhanUsername, keyhanSerial, personInfo);
            if (estelam3sc == null) {

                personEnquiryVTOResult.setIsEstelamCorrupt(true);
                personEnquiryVTOResult.setDescription(BizExceptionCode.NOCR_IMS_01_MSG);
                return personEnquiryVTOResult;
                //throw new BaseException(BizExceptionCode.NOCR_IMS_01,BizExceptionCode.NOCR_IMS_01_MSG);
            }

            List<String> estelam3scMessage = estelam3sc.getMessage();
            if (checkIMSMessageReturn(estelam3scMessage)) {
                //personEnquiryVTOResult.setIsEstelamCorrupt(true);
                personEnquiryVTOResult.setDescription(BizExceptionCode.NOCR_IMS_02_MSG + ":" + estelam3scMessage.get(0));
                personEnquiryVTOResult.setMetadata(nationalId);//set MetaData

//				if (estelam3sc.getEstelamResult3() != null	&& estelam3sc.getEstelamResult3().size() > 0)
//					{
//					if (checkIMSMessageReturn(estelam3sc.getEstelamResult3().get(0).getMessage()))
//						{
//							if (estelam3sc.getEstelamResult3().get(0).getMessage().contains("IS_EXCEPTED")) {

                if (estelam3scMessage.contains("IS_EXCEPTED")) {
                    String exceptionMessage = estelam3sc.getEstelamResult3().get(0).getExceptionMessage();
                    if (checkIMSExceptionMessageReturn(exceptionMessage)) {

                        List<String> allCode = getExceptionCodeDAO().getAll();
                        List<String> matchedCode = JsonPath.read(exceptionMessage, "$..code");
                        Boolean isExcepted = false;
                        for (String code : matchedCode) {
                            if (allCode.contains(code)) {
                                isExcepted = true;
                                break;
                            }
                        }
                        if (isExcepted) {
                            //personEnquiryVTOResult.setIsEstelamCorrupt(true);
                            personEnquiryVTOResult.setIsExceptionMessage(true);
                            personEnquiryVTOResult.setDescription(exceptionMessage);
                            return personEnquiryVTOResult;
                        } else {
                            isAllowableExcepted = true;
                            personEnquiryVTOResult.setMetadata("");
                        }
                    }
                }//

                if (estelam3scMessage.contains("Nin.Not.Valid") || estelam3scMessage.contains("result.rec.invisible") || estelam3scMessage.contains("result.rec.review") || estelam3scMessage.contains("result.rec.return")) {
                    personEnquiryVTOResult.setIsExceptionMessage(true);
                    personEnquiryVTOResult.setDescription(estelam3scMessage.get(0));
                    return personEnquiryVTOResult;
                }

                if (estelam3scMessage.contains("err.record.not.found")) {
                    personEnquiryVTOResult.setIsRecordNotFound(true);
                    personEnquiryVTOResult.setDescription(estelam3scMessage.get(0));
                    return personEnquiryVTOResult;
                }

                if (estelam3scMessage.contains("login.invalid.user")) {
                    personEnquiryVTOResult.setIsRecordNotFound(true);
                    personEnquiryVTOResult.setDescription(estelam3scMessage.get(0));
                    return personEnquiryVTOResult;
                }


//							} else {
//								personEnquiryVTOResult.setIsEstelamCorrupt(true);
//								//personEnquiryVTOResult.setLogInfo(BizExceptionCode.NOCR_IMS_04_MSG);
//								//personEnquiryVTOResult.setDescription(BizExceptionCode.NOCR_IMS_04_MSG);
//								return personEnquiryVTOResult;
//								// throw new
//								// BaseException(BizExceptionCode.NOCR_IMS_04,
//								// BizExceptionCode.NOCR_IMS_04_MSG);
//							}

//						}
//					}
                if (!isAllowableExcepted)
                    return personEnquiryVTOResult;
                //throw new BaseException(BizExceptionCode.NOCR_IMS_02,estelam3scMessage.get(0));
            }


            if (estelam3sc.getEstelamResult3() == null || estelam3sc.getEstelamResult3().size() == 0) {
                personEnquiryVTOResult.setIsEstelamCorrupt(true);
                personEnquiryVTOResult.setDescription(BizExceptionCode.NOCR_IMS_03_MSG);
                return personEnquiryVTOResult;
                //throw new BaseException(BizExceptionCode.NOCR_IMS_03, BizExceptionCode.NOCR_IMS_03_MSG);
            }


            EstelamResult3 estelamResult3 = estelam3sc.getEstelamResult3().get(0);
            if (estelamResult3.getDeathStatus() == 1) // is Dead
            {
                //personEnquiryVTOResult.setIsEstelamCorrupt(true);
                personEnquiryVTOResult.setIsDead(estelamResult3.getDeathStatus());// store deathStatus
                personEnquiryVTOResult.setDescription(BizExceptionCode.NOCR_IMS_05_MSG);
                return personEnquiryVTOResult;
            }

            //Save image just for requested citizen
            if (estelam3sc.getImageResult() != null) {

                if (checkIMSMessageReturn(estelam3sc.getImageResult().getMessage()))

                    //personEnquiryVTOResult = setPersonEnquiryLogInfoImage(personEnquiryVTOInput,estelam3sc.getImageResult().getMessage());
                    personEnquiryVTOResult.setImageDescription(estelam3sc.getImageResult().getMessage().get(0));
                else {
                    ImageResult imageResult = estelam3sc.getImageResult();
                    if (imageResult.getImage() == null)
                        personEnquiryVTOResult.setImageDescription(BizExceptionCode.NOCR_IMS_07_MSG);
                    personEnquiryVTOResult.setNidImage(imageResult.getImage());
                }

            }

            if (estelamResult3.getShenasnameNo() >= 0) {
                if (estelamResult3.getShenasnameNo() == 0) {
                    personEnquiryVTOResult.setBirthCertificateId(nationalId);
                } else {
                    personEnquiryVTOResult.setBirthCertificateId(String
                            .valueOf(estelamResult3.getShenasnameNo()));
                }

            } else if (!metadataFlag) {
                personEnquiryVTOResult.setDescription("The IMS ShenasnameNo is :"
                        + estelamResult3.getShenasnameNo());
                personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_021);
                personEnquiryVTOResult.setMetadata(nationalId);
                metadataFlag = true;
            }

            if (estelamResult3.getBirthDate() != 0) {
                String strDate = String.valueOf(estelamResult3.getBirthDate());
                //validate of birthDate
                if (checkValidateDate(strDate)) {

                    personEnquiryVTOResult.setSolarBirthDate(strDate.substring(
                            0, 4)
                            + "/"
                            + strDate.substring(4, 6)
                            + "/"
                            + strDate.substring(6));

                } else {
                    personEnquiryVTOResult
                            .setDescription("The IMS BirthDate is :"
                                    + estelamResult3.getBirthDate());
                    personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_022);
                    personEnquiryVTOResult.setMetadata(nationalId);
                    metadataFlag = true;
                }
            } else if (!metadataFlag) {
                personEnquiryVTOResult.setDescription("The IMS BirthDate is :"
                        + estelamResult3.getBirthDate());
                personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_022);
                personEnquiryVTOResult.setMetadata(nationalId);
                metadataFlag = true;
            }

            //Shenasname Serial
            //Validate ShenasnameSerial
            String serialShenasname = String.valueOf(estelamResult3.getShenasnameSerial());
            if (serialShenasname.length() == 6 && !serialShenasname.equals("000000")) {
                personEnquiryVTOResult.setBirthCertificateSerial(String
                        .valueOf(estelamResult3.getShenasnameSerial()));
            } else if (!metadataFlag) {
                personEnquiryVTOResult
                        .setDescription("The IMS ShenasnameSerial is :"
                                + estelamResult3.getShenasnameSerial());
                personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_023);
                personEnquiryVTOResult.setMetadata(nationalId);
                metadataFlag = true;
            }

            //Anbari: for gettting Shenasname seri from IMS
//				if (estelamResult3.getShenasnameSerial() != 0) {
//					personEnquiryVTOResult.setBirthCertificateSeries(String
//							.valueOf(estelamResult3.getShenasnameSeri()));
//				} else if (!metadataFlag) {
//					personEnquiryVTOResult
//							.setDescription("The IMS ShenasnameSeri is :"
//									+ estelamResult3.getShenasnameSeri());
//					personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_033);
//					personEnquiryVTOResult.setMetadata(personEnquiryVTOInput.getNationalId());
//					metadataFlag = true;
//				}

            String firstName = new String(estelamResult3.getName());
            if (EmsUtil.checkString(firstName) && !firstName.equals(ConstValues.DEFAULT_NAMES_FA)) {

                personEnquiryVTOResult.setFirstName(new String(estelamResult3
                        .getName(), "UTF-8").trim());
            } else if (!metadataFlag) {
                personEnquiryVTOResult.setMetadata(nationalId);
                personEnquiryVTOResult.setDescription("The IMS Name is :"
                        + new String(estelamResult3.getName()));
                personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_024);
                personEnquiryVTOResult.setMetadata(nationalId);
                metadataFlag = true;
//					throw new ServiceException(BizExceptionCode.NIO_024,
//							BizExceptionCode.NIO_024_MSG);
            }

            String familyName = new String(estelamResult3.getFamily());
            if (EmsUtil.checkString(familyName) && !familyName.equals(ConstValues.DEFAULT_NAMES_FA)) {

                personEnquiryVTOResult.setLastName(new String(estelamResult3
                        .getFamily(), "UTF-8").trim());
            } else if (!metadataFlag) {
                personEnquiryVTOResult.setMetadata(nationalId);
                personEnquiryVTOResult.setDescription("The IMS Family is :"
                        + new String(estelamResult3.getFamily()));
                personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_025);
                personEnquiryVTOResult.setMetadata(nationalId);
                metadataFlag = true;

//					throw new ServiceException(BizExceptionCode.NIO_025,
//							BizExceptionCode.NIO_025_MSG);
            }

            String fatherName = new String(estelamResult3.getFatherName());
            if (EmsUtil.checkString(fatherName) && !fatherName.equals(ConstValues.DEFAULT_NAMES_FA)) {
                personEnquiryVTOResult.setFatherName(new String(estelamResult3
                        .getFatherName(), "UTF-8").trim());
            } else if (!metadataFlag) {
                personEnquiryVTOResult.setDescription("The IMS FatherName is: "
                        + new String(estelamResult3.getFatherName()));
                personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_026);
                personEnquiryVTOResult.setMetadata(nationalId);
                metadataFlag = true;
            }

            if (estelamResult3.getGender() == 0
                    || estelamResult3.getGender() == 1) {
                logger.info("\nThe IMS Gender is : "
                        + estelamResult3.getGender() + "\n");
                personEnquiryVTOResult.setGender((Gender
                        .convertFromIMSEstelamResultGender(estelamResult3
                                .getGender())));
            } else if (!metadataFlag) {
                personEnquiryVTOResult.setDescription("IMS Gender is : "
                        + estelamResult3.getGender());
                personEnquiryVTOResult.setLogInfo(BizExceptionCode.NIO_027);
                personEnquiryVTOResult.setMetadata(nationalId);
                metadataFlag = true;
            }

            //returnMap.put(personEnquiryVTO.getNationalId(),	personEnquiryVTO);
        }
        //Anbari: throw NOCR_IMS_05
        catch (Exception e) {

            logger.error(BizExceptionCode.NIO_012, e.getMessage(), e);
            personEnquiryVTOResult.setIsServiceDown(true);
            personEnquiryVTOResult.setLogInfo(e.getMessage());
            personEnquiryVTOResult.setDescription(e.getMessage());


//				if(e instanceof BaseException)
//				{
//					String exceptionCode = ((BaseException) e).getExceptionCode();
//
//					if( exceptionCode != null && (BizExceptionCode.NOCR_IMS_05.equals(exceptionCode)) ||  (BizExceptionCode.NOCR_IMS_02.equals(exceptionCode)) )
//							throw (BaseException) e;
//					else
//					{
//						businessLogMap.put("exception",((BaseException) e).getExceptionCode() + ":"	+ e.getMessage());
//						logger.error(((BaseException) e).getExceptionCode(),e.getMessage(), e);
//						estelam2Logger.error(((BaseException) e).getExceptionCode(),e.getMessage(), e);
//						personEnquiryVTOResult.setLogInfo(((BaseException) e).getExceptionCode());
//
//					}
//				}
//				else
//				{
//					businessLogMap.put("exception", BizExceptionCode.NIO_012 + ":" + e.getMessage());
//					logger.error(BizExceptionCode.NIO_012, e.getMessage(), e);
//					estelam2Logger.error(BizExceptionCode.NIO_012,e.getMessage(), e);
//				}
        }
        return personEnquiryVTOResult;
    }
}
