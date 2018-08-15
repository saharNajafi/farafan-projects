package com.gam.nocr.ems.data.mapper.xmlmapper;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.factory.DAOFactory;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.commons.core.util.SecureString;
import com.gam.nocr.ems.config.*;
import com.gam.nocr.ems.data.dao.*;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.enums.CardRequestHistoryAction;
import com.gam.nocr.ems.data.enums.CardRequestOrigin;
import com.gam.nocr.ems.data.enums.CardRequestType;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.NistParser;
import gampooya.tools.date.DateFormatException;
import gampooya.tools.date.DateUtil;
import gampooya.tools.util.Base64;
import org.slf4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.*;

import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_BIOMETRIC_INFO;
import static com.gam.nocr.ems.config.EMSLogicalNames.getDaoJNDIName;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class IMSUpdateCitizenInfoMapper implements XMLMapper {

    private static final Logger logger = BaseLog.getLogger(IMSUpdateCitizenInfoMapper.class);
    private static final Logger imsLogger = BaseLog.getLogger("ImsLogger");
    private static final String[] FACE_FINGER_ARRAY = new String[]{"faceData", "fingerData"};
    private static final String[] FACE_ARRAY = new String[]{"faceData"};
    private static final String[] FINGER_ARRAY = new String[]{"fingerData"};
    private static final String CLASS_NAME = IMSUpdateCitizenInfoMapper.class.getName();
    private static final byte[] DEFAULT_BYTE_ARRAY = new byte[0];

    HashMap issueTypeHashMap = new HashMap();

    /**
     * Card Issuance Types
     */
    private static final Integer ISSUE_TYPE_FIRST = 1;
    private static final Integer ISSUE_TYPE_EXTEND = 2;
    private static final Integer ISSUE_TYPE_REPLICA = 3;
    private static final Integer ISSUE_TYPE_REPLACE = 4;
    private static final Integer ISSUE_TYPE_UNSUCCESSFUL_DELIVERY = 5;
    private static final String DEFAULT_KEY_PARSE_NIST_ALLWAYS = "true";

    /**
     * ===============
     * Getter for DAOs
     * ===============
     */

    /**
     * getReligionDAO
     *
     * @return an instance of type ReligionDAO
     */
    private ReligionDAO getReligionDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        ReligionDAO religionDAO = null;
        try {
            religionDAO = (ReligionDAO) factory.getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_RELIGION));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    DataExceptionCode.IUC_032,
                    DataExceptionCode.GLB_009_MSG,
                    e,
                    EMSLogicalNames.DAO_RELIGION.split(","));
        }
        return religionDAO;
    }

    private NistHeaderDAO getNistHeaderDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        NistHeaderDAO nistHeaderDAO = null;
        try {
            nistHeaderDAO = (NistHeaderDAO) factory.getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_NIST_HEADER));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    DataExceptionCode.IUC_034,
                    DataExceptionCode.GLB_009_MSG,
                    e,
                    EMSLogicalNames.DAO_NIST_HEADER.split(","));
        }
        return nistHeaderDAO;
    }

    private EnrollmentOfficeDAO getEnrollmentOfficeDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        EnrollmentOfficeDAO enrollmentOfficeDAO = null;
        try {
            enrollmentOfficeDAO = (EnrollmentOfficeDAO) factory.getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_ENROLLMENT_OFFICE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    DataExceptionCode.IUC_032,
                    DataExceptionCode.GLB_009_MSG,
                    e,
                    EMSLogicalNames.DAO_RELIGION.split(","));
        }
        return enrollmentOfficeDAO;
    }

    private CardRequestHistoryDAO getCardRequestHistoryDAO() throws BaseException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        CardRequestHistoryDAO cardRequestHistoryDAO = null;
        try {
            cardRequestHistoryDAO = (CardRequestHistoryDAO) factory.getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CARD_REQUEST_HISTORY));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    DataExceptionCode.IUC_032,
                    DataExceptionCode.GLB_009_MSG,
                    e,
                    EMSLogicalNames.DAO_RELIGION.split(","));
        }
        return cardRequestHistoryDAO;
    }

    //	TODO : Move this method to util package
    private String convertToBase64(byte[] input) {
        String encoded = null;
        try {
            encoded = new String(Base64.encode(input), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(DataExceptionCode.GLB_ERR_MSG, e);
        }
        return encoded;
    }

    /**
     * @param doc
     * @param xpath
     * @param xpathQuery
     * @throws XPathExpressionException
     */
    private Object extractNode(Document doc, XPath xpath, String xpathQuery)
            throws XPathExpressionException {
        XPathExpression expr = xpath.compile(xpathQuery);
        Object result = expr.evaluate(doc, XPathConstants.NODESET);

        NodeList nodes = (NodeList) result;
        if (nodes != null && nodes.getLength() > 0) {
            if (nodes.item(0).hasChildNodes()) {
                return nodes.item(0).getFirstChild().getNodeValue();
            } else {
                return nodes.item(0).getNodeValue();
            }
        }
        return null;
    }

    private Object extractNodes(Document doc, XPath xpath, String xpathQuery) throws XPathExpressionException {
        XPathExpression expr = xpath.compile(xpathQuery);
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        List<NamedNodeMap> namedNodeMapList = new ArrayList<NamedNodeMap>();
        for (int i = 0; i < nodes.getLength(); i++) {
            namedNodeMapList.add(nodes.item(i).getAttributes());
        }
        return namedNodeMapList;
    }

    private ExtEntityTO readCitizenTO(Document doc, XPath xpath, ExtEntityTO to)
            throws XPathExpressionException, BaseException {
        CitizenTO citizenTO = (CitizenTO) to;
        CitizenInfoTO citizenInfoTO = new CitizenInfoTO();
        String xpathQuery;

        /**
         * Citizen NationalId
         */
        xpathQuery = "//Citizen/IdentityData/NID/text()";
        String nationalId = (String) extractNode(doc, xpath, xpathQuery);
        logger.info("\n\nXML MAPPER : THE NATIONAL ID AFTER READING THE XML RESULT WHICH BELONGS TO THE FETCH SERVICE IS : '" + nationalId + "'\n\n");
        if (nationalId != null) {
            citizenTO.setNationalID(nationalId);
        } else {
            Object[] args = {"citizenNationalId(NID)"};
            throw new DataException(DataExceptionCode.IUC_013, DataExceptionCode.GLB_001_MSG, args);
        }

        /**
         * Citizen Religion
         */
        xpathQuery = "//Citizen/IdentityData/Religion/text()";
        String religion = (String) extractNode(doc, xpath, xpathQuery);
        religion = SecureString.getSecureString(religion.trim());
        logger.info("\n\nXML MAPPER : THE RELIGION AFTER READING THE XML RESULT WHICH BELONGS TO THE FETCH SERVICE IS : '" + religion + "'\n\n");
        if (EmsUtil.checkString(religion)) {
            ReligionTO religionTO = getReligionDAO().findByName(religion);
            citizenInfoTO.setReligion(religionTO);
        } else {
            Object[] args = {"religion"};
            throw new DataException(DataExceptionCode.IUC_031, DataExceptionCode.GLB_001_MSG, args);
        }

        /**
         * Citizen Lunar Birthdate
         */
        xpathQuery = "//Citizen/IdentityData/BirthDateLunar/text()";
        String lunarBirthDate = (String) extractNode(doc, xpath, xpathQuery);
        logger.info("\n\nXML MAPPER : THE BirthDateLunar AFTER READING THE XML RESULT WHICH BELONGS TO THE FETCH SERVICE IS : '" + lunarBirthDate + "'\n\n");
        if (EmsUtil.checkString(lunarBirthDate)) {
            citizenInfoTO.setBirthDateLunar(lunarBirthDate);
        } else {
            Object[] args = {"BirthDateLunar"};
            throw new DataException(DataExceptionCode.IUC_033, DataExceptionCode.GLB_001_MSG, args);
        }


        /**
         * Address
         */
//        xpathQuery = "//Citizen/Address/ZipCode/text()";
//        String zipCode = (String) extractNode(doc, xpath, xpathQuery);
//
//        if (zipCode != null) {
//            citizenInfoTO.setPostcode(zipCode);
//        } else {
//            Object[] args = {"zipCode"};
//            throw new DataException(DataExceptionCode.IUC_014, DataExceptionCode.GLB_001_MSG, args);
//        }
//
//        xpathQuery = "//Citizen/Address/AddressDescription/text()";
//        String addressDescription = (String) extractNode(doc, xpath, xpathQuery);
//        if (addressDescription != null) {
//            citizenInfoTO.setAddress(addressDescription);
//        } else {
//            Object[] args = {"addressDescription"};
//            throw new DataException(DataExceptionCode.IUC_014, DataExceptionCode.GLB_001_MSG, args);
//        }

        /**
         * Family
         */

        //Father
        xpathQuery = "//Citizen/Family/Parents/Father/NID/text()";
        String fatherNationalId = (String) extractNode(doc, xpath, xpathQuery);
        if (EmsUtil.checkString(fatherNationalId)) {
            citizenInfoTO.setFatherNationalID(fatherNationalId);
        } else {
            Object[] args = {"fatherNationalId"};
            throw new DataException(DataExceptionCode.IUC_015, DataExceptionCode.GLB_001_MSG, args);
        }

        xpathQuery = "//Citizen/Family/Parents/Father/ShenasnamehNo/text()";
        String fatherBirthCertificateId = (String) extractNode(doc, xpath, xpathQuery);
        if (EmsUtil.checkString(fatherBirthCertificateId)) {
            citizenInfoTO.setFatherBirthCertificateId(fatherBirthCertificateId);
        } else {
            Object[] args = {"fatherBirthCertificateId"};
            throw new DataException(DataExceptionCode.IUC_016, DataExceptionCode.GLB_001_MSG, args);
        }

        xpathQuery = "//Citizen/Family/Parents/Father/FirstName/text()";
        String fatherFirstName = (String) extractNode(doc, xpath, xpathQuery);
        if (EmsUtil.checkString(fatherFirstName)) {
            citizenInfoTO.setFatherFirstNamePersian(fatherFirstName);
        } else {
            Object[] args = {"fatherFirstNamePersian"};
            throw new DataException(DataExceptionCode.IUC_027, DataExceptionCode.GLB_001_MSG, args);
        }

        //Mother
        xpathQuery = "//Citizen/Family/Parents/Mother/NID/text()";
        String motherNationalId = (String) extractNode(doc, xpath, xpathQuery);
        if (EmsUtil.checkString(motherNationalId)) {
            citizenInfoTO.setMotherNationalID(motherNationalId);
        } else {
            Object[] args = {"motherNationalId"};
            throw new DataException(DataExceptionCode.IUC_017, DataExceptionCode.GLB_001_MSG, args);
        }

        xpathQuery = "//Citizen/Family/Parents/Mother/ShenasnamehNo/text()";
        String motherBirthCertificateId = (String) extractNode(doc, xpath, xpathQuery);
        if (EmsUtil.checkString(motherBirthCertificateId)) {
            citizenInfoTO.setMotherBirthCertificateId(motherBirthCertificateId);
        } else {
            Object[] args = {"motherBirthCertificateId"};
            throw new DataException(DataExceptionCode.IUC_018, DataExceptionCode.GLB_001_MSG, args);
        }

        xpathQuery = "//Citizen/Family/Parents/Mother/FirstName/text()";
        String motherFirstName = (String) extractNode(doc, xpath, xpathQuery);
        if (EmsUtil.checkString(motherFirstName)) {
            citizenInfoTO.setMotherFirstNamePersian(motherFirstName);
        } else {
            Object[] args = {"motherFirstNamePersian"};
            throw new DataException(DataExceptionCode.IUC_028, DataExceptionCode.GLB_001_MSG, args);
        }

        NodeList listOfSpouses = doc.getElementsByTagName("Spouse");
        int totalSpouses = listOfSpouses.getLength();
        logger.info("Total number of spouses : " + totalSpouses);
        for (int i = 0; i < listOfSpouses.getLength(); i++) {
            SpouseTO spouseTO = new SpouseTO();

            Node firstSpouseNode = listOfSpouses.item(i);
            if (firstSpouseNode.getNodeType() == Node.ELEMENT_NODE) {
                Element firstSpouseElement = (Element) firstSpouseNode;
                //-------
                NodeList nationalIdList = firstSpouseElement.getElementsByTagName("NID");
                Element nationalIdElement = (Element) nationalIdList.item(0);
                NodeList textNIDList = nationalIdElement.getChildNodes();
                String spouseNationalId = ((Node) textNIDList.item(0)).getNodeValue().trim();
                logger.info("SpouseNationalId : " + ((Node) textNIDList.item(0)).getNodeValue().trim());
                spouseTO.setSpouseNationalID(spouseNationalId);

                NodeList marriageDateList = firstSpouseElement.getElementsByTagName("MarriageDate");
                Element marriageElement = (Element) marriageDateList.item(0);
                NodeList textMarriageDateList = marriageElement.getChildNodes();
                String spouseMarriageDate = ((Node) textMarriageDateList.item(0)).getNodeValue().trim();
                logger.info("MarriageDate : " + ((Node) textMarriageDateList.item(0)).getNodeValue().trim());
                try {
                    spouseTO.setSpouseMarriageDate(DateUtil.convert(spouseMarriageDate, DateUtil.JALALI));
                } catch (DateFormatException e) {
                    throw new DataException(DataExceptionCode.IUC_029, e.getMessage(), e);
                }

                NodeList divorceDateList = firstSpouseElement.getElementsByTagName("DivorceDate");
                if (divorceDateList != null && divorceDateList.getLength() != 0) {
                    Element divorceElement = (Element) divorceDateList.item(0);
                    NodeList textDivorceDateList = divorceElement.getChildNodes();
                    if (textDivorceDateList.item(0) != null && EmsUtil.checkString(((Node) textDivorceDateList.item(0)).getNodeValue())) {
                        String spouseDivorceDate = ((Node) textDivorceDateList.item(0)).getNodeValue().trim();
                        logger.info("DivorceDate : " + ((Node) textDivorceDateList.item(0)).getNodeValue().trim());
                        try {
                            if (EmsUtil.checkString(spouseDivorceDate)) {
                                spouseTO.setSpouseDeathOrDivorceDate(DateUtil.convert(spouseDivorceDate, DateUtil.JALALI));
                            }
                        } catch (DateFormatException e) {
                            throw new DataException(DataExceptionCode.IUC_030, e.getMessage(), e);
                        }
                    }
                }

                NodeList marriageStatusList = firstSpouseElement.getElementsByTagName("MarriageStatus");
                Element marriageStatusElement = (Element) marriageStatusList.item(0);
                NodeList textMarriageStatusList = marriageStatusElement.getChildNodes();
                String spouseMarriageStatus = ((Node) textMarriageStatusList.item(0)).getNodeValue().trim();
                logger.info("SpouseMarriageStatus : " + ((Node) textMarriageStatusList.item(0)).getNodeValue().trim());
                spouseTO.setMaritalStatus(new MaritalStatusTO(Long.parseLong(spouseMarriageStatus)));

                citizenInfoTO.getSpouses().add(spouseTO);
            }
        }

        NodeList listOfChildren = doc.getElementsByTagName("Child");
        int totalChildren = listOfChildren.getLength();
        logger.info("Total number of Children : " + totalChildren);
        for (int i = 0; i < listOfChildren.getLength(); i++) {
            Node firstChildNode = listOfChildren.item(i);
            if (firstChildNode.getNodeType() == Node.ELEMENT_NODE) {
                Element firstChildElement = (Element) firstChildNode;
                //-------
                NodeList nationalIdList = firstChildElement.getElementsByTagName("NID");
                Element nationalIdElement = (Element) nationalIdList.item(0);

                NodeList textNIDList = nationalIdElement.getChildNodes();
                String childNationalId = ((Node) textNIDList.item(0)).getNodeValue().trim();
                logger.info("ChildNationalId : " + ((Node) textNIDList.item(0)).getNodeValue().trim());
                ChildTO childTO = new ChildTO();
                childTO.setChildNationalID(childNationalId);
                citizenInfoTO.getChildren().add(childTO);
            }
        }
        citizenTO.setCitizenInfo(citizenInfoTO);
        return citizenTO;
    }

    public IMSUpdateCitizenInfoMapper() {

        issueTypeHashMap.put(CardRequestType.FIRST_CARD, ISSUE_TYPE_FIRST);
        issueTypeHashMap.put(CardRequestType.EXTEND, ISSUE_TYPE_EXTEND);
        issueTypeHashMap.put(CardRequestType.REPLICA, ISSUE_TYPE_REPLICA);
        issueTypeHashMap.put(CardRequestType.REPLACE, ISSUE_TYPE_REPLACE);
        issueTypeHashMap.put(CardRequestType.UNSUCCESSFUL_DELIVERY, ISSUE_TYPE_UNSUCCESSFUL_DELIVERY);
        issueTypeHashMap.put(CardRequestType.UNSUCCESSFUL_DELIVERY_FOR_FIRST_CARD, ISSUE_TYPE_UNSUCCESSFUL_DELIVERY);
        issueTypeHashMap.put(CardRequestType.UNSUCCESSFUL_DELIVERY_FOR_EXTEND, ISSUE_TYPE_UNSUCCESSFUL_DELIVERY);
        issueTypeHashMap.put(CardRequestType.UNSUCCESSFUL_DELIVERY_FOR_REPLACE, ISSUE_TYPE_UNSUCCESSFUL_DELIVERY);
        issueTypeHashMap.put(CardRequestType.UNSUCCESSFUL_DELIVERY_FOR_REPLICA, ISSUE_TYPE_UNSUCCESSFUL_DELIVERY);
    }

    public byte[] writeXML(List<CardRequestTO> toList,
                           Map<String, String> attributesMap) throws BaseException {
        if (toList == null || toList.isEmpty()) {
            throw new DataException(
                    DataExceptionCode.IUC_012,
                    DataExceptionCode.IUC_003_MSG,
                    new String[]{CLASS_NAME, ExtEntityTO.class.getName()});
        }

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;

        try {
            docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("UpdateRequest");
            doc.appendChild(rootElement);

            if (attributesMap != null && attributesMap.get("requestId") != null) {
                Attr identifierAttr = doc.createAttribute("requestId");
                identifierAttr.setValue(attributesMap.get("requestId"));
                rootElement.setAttributeNode(identifierAttr);

            } else {
                Object[] args = {"requestId"};
                throw new DataException(DataExceptionCode.IUC_001, DataExceptionCode.GLB_001_MSG, args);
            }
            for (ExtEntityTO extEntityTO : toList) {
                CardRequestTO cardRequestTO = (CardRequestTO) extEntityTO;
                CitizenTO citizenTO = cardRequestTO.getCitizen();
                if (citizenTO == null) {
                    throw new DataException(
                            DataExceptionCode.IUC_003,
                            DataExceptionCode.IUC_003_MSG,
                            new String[]{CLASS_NAME, CitizenTO.class.getName()});
                }

                CitizenInfoTO citizenInfoTO = citizenTO.getCitizenInfo();
                if (citizenInfoTO == null) {
                    throw new DataException(
                            DataExceptionCode.IUC_004,
                            DataExceptionCode.IUC_003_MSG,
                            new String[]{CLASS_NAME, CitizenInfoTO.class.getName()});
                }

                //Citizen Element
                Element citizenElement = doc.createElement("Citizen");
                rootElement.appendChild(citizenElement);

                //RequestInfo Element
                Element requestInfo = doc.createElement("RequestInfo");
                rootElement.appendChild(requestInfo);


                Attr updateType = doc.createAttribute("updateType");
                updateType.setValue(issueTypeHashMap.get(cardRequestTO.getType()).toString());
                citizenElement.setAttributeNode(updateType);

                //IdentityData Element
                Element identityDataElement = doc.createElement("IdentityData");
                citizenElement.appendChild(identityDataElement);

                Element nationalIdElement = doc.createElement("NID");
                if (citizenTO.getNationalID() != null) {
                    nationalIdElement.appendChild(doc.createTextNode(citizenTO.getNationalID()));
                }
                identityDataElement.appendChild(nationalIdElement);

                Element religionElement = doc.createElement("Religion");
                if (citizenInfoTO.getReligion() != null) {
                    religionElement.appendChild(doc.createTextNode(citizenInfoTO.getReligion().getId().toString()));
                }
                identityDataElement.appendChild(religionElement);


                Element birthDateLunarElement = doc.createElement("BirthDateLunar");
                if (citizenInfoTO.getBirthDateLunar() != null) {
                    birthDateLunarElement.appendChild(doc.createTextNode(citizenInfoTO.getBirthDateLunar()));
                }
                identityDataElement.appendChild(birthDateLunarElement);

//				Address Element
                Element addressElement = doc.createElement("Address");
                citizenElement.appendChild(addressElement);

                Element zipCodeElement = doc.createElement("ZipCode");
                if (citizenInfoTO.getPostcode() != null) {
                    zipCodeElement.appendChild(doc.createTextNode(citizenInfoTO.getPostcode()));
                }
                addressElement.appendChild(zipCodeElement);

                Element addressDescriptionElement = doc.createElement("AddressDescription");
                if (citizenInfoTO.getAddress() != null) {
                    addressDescriptionElement.appendChild(doc.createTextNode(citizenInfoTO.getAddress()));
                }
                addressElement.appendChild(addressDescriptionElement);

                Element mobilePhoneElement = doc.createElement("MobilePhone");
                if (citizenInfoTO.getMobile() != null) {
                    mobilePhoneElement.appendChild(doc.createTextNode(citizenInfoTO.getMobile()));
                }
                addressElement.appendChild(mobilePhoneElement);

                Element phoneElement = doc.createElement("Phone");
                if (citizenInfoTO.getPhone() != null) {
                    phoneElement.appendChild(doc.createTextNode(citizenInfoTO.getPhone()));
                }
                addressElement.appendChild(phoneElement);

                Element geoSerialElement = doc.createElement("geo_serial");
                if (citizenInfoTO.getLivingCity() != null) {
                    geoSerialElement.appendChild(doc.createTextNode(citizenInfoTO.getLivingCity().getId().toString()));
                }
                addressElement.appendChild(geoSerialElement);

//				Family Element
                Element familyElement = doc.createElement("Family");
                citizenElement.appendChild(familyElement);

                Element parentsElement = doc.createElement("Parents");
                familyElement.appendChild(parentsElement);

                Element fatherElement = doc.createElement("Father");
                parentsElement.appendChild(fatherElement);


                Element fatherNationalIdElement = doc.createElement("NID");
                if (citizenInfoTO.getFatherNationalID() != null && !citizenInfoTO.getFatherNationalID().equals(ConstValues.DEFAULT_NID)) {
                    fatherNationalIdElement.appendChild(doc.createTextNode(citizenInfoTO.getFatherNationalID()));
                }
                fatherElement.appendChild(fatherNationalIdElement);

                Element fatherCertificateIdElement = doc.createElement("ShenasnamehNo");
                if (citizenInfoTO.getFatherBirthCertificateId() != null && !citizenInfoTO.getFatherBirthCertificateId().equals(ConstValues.DEFAULT_NUMBER)) {
                    fatherCertificateIdElement.appendChild(doc.createTextNode(citizenInfoTO.getFatherBirthCertificateId()));
                }
                fatherElement.appendChild(fatherCertificateIdElement);

                Element fatherFirstNameElement = doc.createElement("FirstName");
                if (citizenInfoTO.getFatherFirstNamePersian() != null) {
                    fatherFirstNameElement.appendChild(doc.createTextNode(citizenInfoTO.getFatherFirstNamePersian()));
                }
                if (citizenInfoTO.getFatherFirstNamePersian() == null ||
                        citizenInfoTO.getFatherFirstNamePersian().isEmpty()) {
                    throw new DataException(
                            DataExceptionCode.IUC_004,
                            MessageFormat.format(
                                    DataExceptionCode.IUC_004_MSG, "Father's FirstName"
                            ),
                            new String[]{CLASS_NAME, "Father's FirstName"});
                }
                fatherElement.appendChild(fatherFirstNameElement);
                Element fatherBirthDateElement = doc.createElement("BirthDate");
                if (citizenInfoTO.getFatherBirthDateSolar() != null
                        &&
                        !DateUtil.convert(citizenInfoTO.getFatherBirthDateSolar(), DateUtil.JALALI).equals(ConstValues.DEFAULT_DATE)) {
                    fatherBirthDateElement
                            .appendChild(doc.createTextNode(DateUtil.convert(citizenInfoTO.getFatherBirthDateSolar(), DateUtil.JALALI)));
                }
                fatherElement.appendChild(fatherBirthDateElement);


                Element motherElement = doc.createElement("Mother");
                parentsElement.appendChild(motherElement);
                Element motherNationalIdElement = doc.createElement("NID");
                if (citizenInfoTO.getMotherNationalID() != null && !citizenInfoTO.getMotherNationalID().equals(ConstValues.DEFAULT_NID)) {
                    motherNationalIdElement.appendChild(doc.createTextNode(citizenInfoTO.getMotherNationalID()));
                }
                motherElement.appendChild(motherNationalIdElement);
                Element motherCertificateIdElement = doc.createElement("ShenasnamehNo");
                if (citizenInfoTO.getMotherBirthCertificateId() != null && !citizenInfoTO.getMotherBirthCertificateId().equals(ConstValues.DEFAULT_NUMBER)) {
                    motherCertificateIdElement.appendChild(doc.createTextNode(citizenInfoTO.getMotherBirthCertificateId()));
                }
                motherElement.appendChild(motherCertificateIdElement);

                Element motherFirstNameElement = doc.createElement("FirstName");
                if (citizenInfoTO.getMotherFirstNamePersian() != null) {
                    motherFirstNameElement.appendChild(doc.createTextNode(citizenInfoTO.getMotherFirstNamePersian()));
                }
                if (citizenInfoTO.getMotherFirstNamePersian() == null ||
                        citizenInfoTO.getMotherFirstNamePersian().isEmpty()) {
                    throw new DataException(
                            DataExceptionCode.IUC_004,
                            MessageFormat.format(
                                    DataExceptionCode.IUC_004_MSG, "Mother's FirstName"
                            ),
                            new String[]{CLASS_NAME, "Mother's FirstName"});
                }
                motherElement.appendChild(motherFirstNameElement);
                Element motherBirthDateElement = doc.createElement("BirthDate");
                if (citizenInfoTO.getMotherBirthDateSolar() != null
                        &&
                        !DateUtil.convert(citizenInfoTO.getMotherBirthDateSolar(), DateUtil.JALALI).equals(ConstValues.DEFAULT_DATE)) {
                    motherBirthDateElement
                            .appendChild(doc.createTextNode(DateUtil.convert(citizenInfoTO.getFatherBirthDateSolar(), DateUtil.JALALI)));
                }
                motherElement.appendChild(motherBirthDateElement);


                Element spousesElement = doc.createElement("Spouses");
                familyElement.appendChild(spousesElement);

                if (citizenInfoTO.getSpouses() != null) {
                    for (int i = 0; i < citizenInfoTO.getSpouses().size(); i++) {
                        Element spouseElement = doc.createElement("Spouse");
                        spousesElement.appendChild(spouseElement);

                        Element spouseNationalIdElement = doc.createElement("NID");
                        if (citizenInfoTO.getSpouses().get(i).getSpouseNationalID() != null) {
                            spouseNationalIdElement.appendChild(doc.createTextNode(citizenInfoTO.getSpouses().get(i).getSpouseNationalID()));
                        }
                        spouseElement.appendChild(spouseNationalIdElement);

                        Element marriageDateJalaliElement = doc.createElement("MarriageDate");
                        if (citizenInfoTO.getSpouses().get(i).getSpouseMarriageDate() != null) {
                            marriageDateJalaliElement
                                    .appendChild(doc.createTextNode(DateUtil.convert(citizenInfoTO.getSpouses().get(i).getSpouseMarriageDate(), DateUtil.JALALI)));
                        }
                        spouseElement.appendChild(marriageDateJalaliElement);

                        Element divorceDateJalaliElement = doc.createElement("DivorceDate");


                        if (citizenInfoTO.getSpouses().get(i).getSpouseDeathOrDivorceDate() != null) {
                            divorceDateJalaliElement
                                    .appendChild(doc.createTextNode(DateUtil.convert(citizenInfoTO.getSpouses().get(i).getSpouseDeathOrDivorceDate(), DateUtil.JALALI)));
                        }
                        spouseElement.appendChild(divorceDateJalaliElement);

                        Element marriageStatusElement = doc.createElement("MarriageStatus");
                        marriageStatusElement
                                .appendChild(doc.createTextNode(String.valueOf(citizenInfoTO.getSpouses().get(i).getMaritalStatus().getId())));
                        spouseElement.appendChild(marriageStatusElement);
                    }
                }

                Element childrenElement = doc.createElement("Children");
                familyElement.appendChild(childrenElement);
                if (citizenInfoTO != null && citizenInfoTO.getChildren() != null) {
                    int childrenCount = citizenInfoTO.getChildren().size();
                    if (childrenCount > 20) {
                        childrenCount = 20;
                    }

                    for (int i = 0; i < childrenCount; i++) {

                        Element childElement = doc.createElement("Child");
                        childrenElement.appendChild(childElement);

                        Element childNationalIdElement = doc.createElement("NID");
                        if (citizenInfoTO.getChildren().get(i).getChildNationalID() != null) {
                            childNationalIdElement.appendChild(doc.createTextNode(citizenInfoTO.getChildren().get(i).getChildNationalID()));
                            childElement.appendChild(childNationalIdElement);
                        }
                    }
                }
                String faceChipData = null;
                String faceImsData = null;
                String faceLaserData = null;
                String faceMliData = null;
                byte[] fingerData = null;
                String featureExtractorIDValue = null;

                for (BiometricTO biometricTO : citizenInfoTO.getBiometrics()) {

                    switch (biometricTO.getType()) {
                        case FACE_CHIP:
                            faceChipData = convertToBase64(biometricTO.getData());
                            break;
                        case FACE_IMS:
                            faceImsData = convertToBase64(biometricTO.getData());
                            break;
                        case FACE_LASER:
                            faceLaserData = convertToBase64(biometricTO.getData());
                            break;
                        case FACE_MLI:
                            faceMliData = convertToBase64(biometricTO.getData());
                            break;
                        case FING_ALL:
                            fingerData = biometricTO.getData();
                            BiometricInfoTO biometricInfoTO = getBiometricInfoDAO().findByNid(cardRequestTO.getCitizen().getNationalID());
                            featureExtractorIDValue = biometricInfoTO.getFeatureExtractorID();
                            break;

                        default:
                            break;
                    }

                }

                Element biometricInfoElement = doc.createElement("BiometricInfo");
                citizenElement.appendChild(biometricInfoElement);

                Element faceImagesElement = doc.createElement("FaceImages");
                biometricInfoElement.appendChild(faceImagesElement);


                Element faceImsElement = doc.createElement("FACE_IMS");
                faceImagesElement.appendChild(faceImsElement);

                Element faceImsDataElement = doc.createElement("Data");
                faceImsDataElement.appendChild(doc.createTextNode(faceImsData));
                faceImsElement.appendChild(faceImsDataElement);

                Element faceChipElement = doc.createElement("FACE_CHIP");
                faceImagesElement.appendChild(faceChipElement);

                Element faceChipDataElement = doc.createElement("Data");
                faceChipDataElement.appendChild(doc.createTextNode(faceChipData));
                faceChipElement.appendChild(faceChipDataElement);

                Element faceMliElement = doc.createElement("FACE_MLI");
                faceImagesElement.appendChild(faceMliElement);

                Element faceMliDataElement = doc.createElement("Data");
                faceMliDataElement.appendChild(doc.createTextNode(faceMliData));
                faceMliElement.appendChild(faceMliDataElement);

                Element faceLaserElement = doc.createElement("FACE_LASER");
                faceImagesElement.appendChild(faceLaserElement);


                Element faceLaserDataElement = doc.createElement("Data");
                faceLaserDataElement.appendChild(doc.createTextNode(faceLaserData));
                faceLaserElement.appendChild(faceLaserDataElement);

                Element sourceImageElement = doc.createElement("SourceImage");
                sourceImageElement.appendChild(doc.createTextNode(""));
                faceImagesElement.appendChild(sourceImageElement);

                Element fingersElement = doc.createElement("Fingers");
                biometricInfoElement.appendChild(fingersElement);

                Attr featureExtractorID = doc.createAttribute("FeatureExtractorID");
                if (featureExtractorIDValue == null || Integer.valueOf(featureExtractorIDValue) < 0) {
                    Object[] args = {"FeatureExtractorID"};
                    throw new DataException(DataExceptionCode.CRC_010,
                            DataExceptionCode.GLB_001_MSG, args);
                }
                featureExtractorID.setValue(featureExtractorIDValue);
                fingersElement.setAttributeNode(featureExtractorID);

                Element nistElement = doc.createElement("NIST");
                nistElement.appendChild(doc.createTextNode(convertToBase64(fingerData)));
                fingersElement.appendChild(nistElement);

                Element fingerDataElement = doc.createElement("MetaData");
                byte[] nistHeader = NistParser.parseNistData(fingerData).getNistHeader();
                //save nist header
                Boolean parseNistAllways = Boolean.valueOf(EmsUtil
                        .getProfileValue(ProfileKeyName.KEY_PARSE_NIST_ALLWAYS,
                                DEFAULT_KEY_PARSE_NIST_ALLWAYS));
                if (!parseNistAllways) {
                    NistHeaderTO nistHeaderTO = new NistHeaderTO(cardRequestTO, nistHeader, new Date());
                    getNistHeaderDAO().create(nistHeaderTO);
                }
                //
                fingerDataElement.appendChild(doc.createTextNode(convertToBase64(nistHeader)));
                fingersElement.appendChild(fingerDataElement);

                Element ksvElement = doc.createElement("KSV");
                ksvElement.appendChild(doc.createTextNode(""));
                fingersElement.appendChild(ksvElement);

                Element keyElement = doc.createElement("Key");
                keyElement.appendChild(doc.createTextNode(""));
                fingersElement.appendChild(keyElement);

//              Documents
                List<DocumentTO> documentTOs = citizenInfoTO.getDocuments();

                Element documentsElement = doc.createElement("Documents");
                citizenElement.appendChild(documentsElement);

                if (EmsUtil.checkListSize(documentTOs)) {
                    for (DocumentTO document : documentTOs) {
                        Element docElement = doc.createElement("Document");
                        documentsElement.appendChild(docElement);

                        Element typeElement = doc.createElement("Type");
                        typeElement.appendChild(doc.createTextNode(document.getType().getId().toString()));
                        docElement.appendChild(typeElement);

                        Element dataElement = doc.createElement("Data");
                        dataElement.appendChild(doc.createTextNode(convertToBase64(document.getData())));
                        docElement.appendChild(dataElement);
                    }
                }

                Element enrolledDateElement = doc.createElement("EnrolledDate");
                if (cardRequestTO.getEnrolledDate() != null) {
                    enrolledDateElement.appendChild(doc.createTextNode(DateUtil.convert(cardRequestTO.getEnrolledDate(), DateUtil.JALALI)));
                }
                requestInfo.appendChild(enrolledDateElement);

                Element enrollmentOfficeElement = doc.createElement("EnrollmentOffice");
                if (cardRequestTO.getEnrollmentOffice() != null) {
                    enrollmentOfficeElement.appendChild(doc.createTextNode(cardRequestTO.getEnrollmentOffice().getCode()));
                }
                requestInfo.appendChild(enrollmentOfficeElement);

                Element priority = doc.createElement("Priority");
                if (cardRequestTO.getPriority() != null) {
                    priority.appendChild(doc.createTextNode(cardRequestTO.getPriority().toString()));
                }
                requestInfo.appendChild(priority);

                Element originElement = doc.createElement("Origin");
                if (cardRequestTO.getOrigin() == null) {
                    throw new DataException(
                            DataExceptionCode.IUC_004,
                            MessageFormat.format(
                                    DataExceptionCode.IUC_004_MSG, "CardRequest's Origin"
                            ),
                            new String[]{CLASS_NAME, "CardRequest's Origin"});
                }
                originElement.appendChild(doc.createTextNode(cardRequestTO.getOrigin().toString()));
                requestInfo.appendChild(originElement);

                Element portalEnrolledDateElement = doc.createElement("PortalEnrolledDate");
                if (cardRequestTO.getPortalEnrolledDate() != null) {
                    try {
                        portalEnrolledDateElement.appendChild(doc.createTextNode(DateUtil.convert(cardRequestTO.getPortalEnrolledDate(), DateUtil.JALALI)));
                    } catch (Exception ex) {
                        logger.error("Error In Converting PortalEnrolledDate : " + cardRequestTO.getPortalEnrolledDate() + " for Card Request :" + cardRequestTO.getId(), ex);
                        throw ex;
                    }
                }
                requestInfo.appendChild(portalEnrolledDateElement);

                Element originalCardRequestOfficeElement = doc.createElement("OriginalCardRequestOffice");
                if (cardRequestTO.getOriginalCardRequestOfficeId() != null) {
                    EnrollmentOfficeTO enrollmentOfficeTO = getEnrollmentOfficeDAO().find(EnrollmentOfficeTO.class, cardRequestTO.getOriginalCardRequestOfficeId());
                    originalCardRequestOfficeElement.appendChild(doc.createTextNode(enrollmentOfficeTO.getCode()));
                }
                requestInfo.appendChild(originalCardRequestOfficeElement);

                Element reservationDateElement = doc.createElement("ReservationDate");
                if (cardRequestTO.getReservationDate() != null)
                    reservationDateElement.appendChild(doc.createTextNode(DateUtil.convert(cardRequestTO.getReservationDate(), DateUtil.JALALI)));
                else
                    reservationDateElement.appendChild(doc.createTextNode(DateUtil.convert(cardRequestTO.getEnrolledDate(), DateUtil.JALALI)));
                requestInfo.appendChild(reservationDateElement);

                Element reduplicateElement = doc.createElement("Reduplicate");
                reduplicateElement.appendChild(doc.createTextNode(""));
                requestInfo.appendChild(reduplicateElement);

                Element imsIdElement = doc.createElement("ImsId");
                imsIdElement.appendChild(doc.createTextNode("0"));
                requestInfo.appendChild(imsIdElement);

                Element managerUsernameElement = doc.createElement("ManagerUsername");
                if (cardRequestTO.getEnrollmentOffice().getManager() != null) {
                    managerUsernameElement.appendChild(doc.createTextNode(cardRequestTO.getEnrollmentOffice().getManager().getUserName()));
                }
                if (cardRequestTO.getEnrollmentOffice().getManager() == null) {
                    throw new DataException(
                            DataExceptionCode.IUC_004,
                            MessageFormat.format(
                                    DataExceptionCode.IUC_004_MSG, "ManagerUsername"
                            ),
                            new String[]{CLASS_NAME, "ManagerUsername"});

                } else if (cardRequestTO.getEnrollmentOffice().getManager() != null) {
                    if (cardRequestTO.getEnrollmentOffice().getManager().getUserName() == null ||
                            cardRequestTO.getEnrollmentOffice().getManager().getUserName().isEmpty()) {
                        throw new DataException(
                                DataExceptionCode.IUC_004,
                                MessageFormat.format(
                                        DataExceptionCode.IUC_004_MSG, "ManagerUsername"
                                ),
                                new String[]{CLASS_NAME, "ManagerUsername"});
                    }
                }
                requestInfo.appendChild(managerUsernameElement);
                CardRequestHistoryTO cardRequestHistoryTO = null;
                if (cardRequestTO.getOrigin() == CardRequestOrigin.V)
                    cardRequestHistoryTO = getCardRequestHistoryDAO()
                            .findHistoryByRequestIdAndAction(
                                    cardRequestTO.getId(),
                                    CardRequestHistoryAction.FINGER_SCAN);
                else
                    cardRequestHistoryTO = getCardRequestHistoryDAO()
                            .findHistoryByRequestIdAndAction(
                                    cardRequestTO.getId(),
                                    CardRequestHistoryAction.AUTHENTICATE_DOCUMENT);

                Element authenticatedByElement = doc.createElement("AuthenticatedBy");
                if (cardRequestHistoryTO != null && cardRequestHistoryTO.getActor() != null) {
                    authenticatedByElement.appendChild(doc.createTextNode(cardRequestHistoryTO.getActor()));
                }
                if (cardRequestHistoryTO == null) {
                    throw new DataException(
                            DataExceptionCode.IUC_004,
                            MessageFormat.format(
                                    DataExceptionCode.IUC_004_MSG, "AuthenticatedBy"
                            ),
                            new String[]{CLASS_NAME, "AuthenticatedBy"});
                } else if (cardRequestHistoryTO != null) {
                    if (cardRequestHistoryTO.getActor() == null ||
                            cardRequestHistoryTO.getActor().isEmpty()) {
                        throw new DataException(
                                DataExceptionCode.IUC_004,
                                MessageFormat.format(
                                        DataExceptionCode.IUC_004_MSG, "AuthenticatedBy"
                                ),
                                new String[]{CLASS_NAME, "AuthenticatedBy"});
                    }
                }
                requestInfo.appendChild(authenticatedByElement);
            }


            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "html");
            transformer.transform(domSource, result);

            logger.info("=================================IMS UPDATE CITIZEN INFO MAPPER================================");
            imsLogger.info("=================================IMS UPDATE CITIZEN INFO MAPPER================================");
            logger.info("The xml request for calling the updateCitizenInfo service was produced by the sub system 'EMS'.");
            imsLogger.info("requestId:" + toList.get(0).getId());
            logger.info("requestId:" + toList.get(0).getId());
            imsLogger.info("requestId:" + toList.get(0).getId());
//            logger.info(writer.toString());
//            imsLogger.info(writer.toString());
            logger.info("===============================================================================================");
            imsLogger.info("===============================================================================================");


            return writer.toString().getBytes("UTF-8");

        } catch (BaseException e) {
            throw e;
        } catch (ParserConfigurationException e) {
            throw new DataException(DataExceptionCode.IUC_008, e);
        } catch (TransformerConfigurationException e) {
            throw new DataException(DataExceptionCode.IUC_009, e);
        } catch (TransformerException e) {
            throw new DataException(DataExceptionCode.IUC_010, e);
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.IUC_011, e);
        }
    }

    @Override
    public byte[] writeXML(ExtEntityTO to, Map<String, String> attributesMap) throws BaseException {
        return DEFAULT_BYTE_ARRAY;
    }

    @Override
    public ExtEntityTO readXML(String xmlData,
                               ExtEntityTO to) throws BaseException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setIgnoringElementContentWhitespace(true);
            dbFactory.setNamespaceAware(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            byte[] xmlDATA = xmlData.getBytes("UTF-8");
            ByteArrayInputStream in = new ByteArrayInputStream(xmlDATA);
            Document doc = dBuilder.parse(in);

            /**
             * XPath
             */
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();

			/*
            This attribute has not been needed til now, but it can be useful in future
			 */
            String xpathQuery = "//@requestId";
            extractNode(doc, xpath, xpathQuery);

            if (to instanceof CitizenTO) {
                return readCitizenTO(doc, xpath, to);
            }

        } catch (BaseException e) {
            throw e;
        } catch (ParserConfigurationException e) {
            throw new DataException(DataExceptionCode.IUC_020, e);
        } catch (SAXException e) {
            throw new DataException(DataExceptionCode.IUC_021, e);
        } catch (IOException e) {
            throw new DataException(DataExceptionCode.IUC_022, e);
        } catch (XPathExpressionException e) {
            throw new DataException(DataExceptionCode.IUC_023, e);
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.IUC_024, e);
        }
        return null;
    }

    /**
     * The method readXML is used to read the xml data
     *
     * @param xmlData       is an instance of type {@link String} which represents the xml String that has wanted to read
     * @param to            is an instance of type {@link com.gam.commons.core.data.domain.ExtEntityTO}
     * @param attributesMap is a map of type {@link java.util.Map <String, String>}
     * @return an instance of type {@link com.gam.commons.core.data.domain.ExtEntityTO}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public ExtEntityTO readXML(String xmlData, ExtEntityTO to, Map<String, String> attributesMap) throws BaseException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private BiometricInfoDAO getBiometricInfoDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(
                    getDaoJNDIName(DAO_BIOMETRIC_INFO));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.CMS_084,
                    BizExceptionCode.GLB_001_MSG, e,
                    DAO_BIOMETRIC_INFO.split(","));
        }
    }
}
