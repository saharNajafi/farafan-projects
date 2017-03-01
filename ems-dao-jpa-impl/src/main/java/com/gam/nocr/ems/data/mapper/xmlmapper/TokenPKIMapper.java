/**
 *
 */
package com.gam.nocr.ems.data.mapper.xmlmapper;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.DepartmentTO;
//import com.gam.nocr.ems.data.domain.NetworkTokenTO;
import com.gam.nocr.ems.data.domain.PersonTokenTO;
import com.gam.nocr.ems.data.domain.TokenTO;
import com.gam.nocr.ems.data.enums.TokenReIssuanceReason;
import com.gam.nocr.ems.data.enums.TokenState;
import org.slf4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author Sina Golesorkhi
 */
public class TokenPKIMapper implements XMLMapper {

    private static final Logger logger = BaseLog.getLogger(TokenPKIMapper.class);

    @Override
    /**
     * creates either revocaitonReq or searchReq
     */
    public byte[] writeXML(ExtEntityTO to, Map<String, String> attributesMap) throws BaseException {

        TokenTO tokenTO = (TokenTO) to;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;

        try {
            docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            if (attributesMap.get("id").contains("revocationReq")) {
                return createRevocationReqXML(doc, tokenTO, attributesMap);
            }
            if (attributesMap.get("id").contains("searchReq")) {
                return createSearchReqXML(doc, tokenTO, attributesMap);
            }
            if (attributesMap.get("id").contains("enrollReq")) {
                return createEnrollReqXML(doc, tokenTO, attributesMap);

            }
            if (attributesMap.get("id").contains("renewalReq")) {
                return createRenewalReqXML(doc, tokenTO, attributesMap);

            }
            if (attributesMap.get("id").contains("activationReq")) {
                return createActivationReqXML(doc, tokenTO, attributesMap);

            }
        } catch (ParserConfigurationException e) {
            throw new DataException(DataExceptionCode.TPM_001, e);
        } catch (TransformerException e) {
            throw new DataException(DataExceptionCode.TPM_002, e);
        }
        return null;
    }

 //Edited By Adldoost
    private byte[] createEnrollReqXML(Document doc, TokenTO tokenTO, Map<String, String> idMap) throws BaseException {
        Element rootElement = doc.createElement("enrollmentRequests");
        doc.appendChild(rootElement);

        if (idMap.get("protocolVersion") != null) {
            Attr protocolVersionAttr = doc.createAttribute("protocolVersion");
            protocolVersionAttr.setValue(idMap.get("protocolVersion"));
            rootElement.setAttributeNode(protocolVersionAttr);
        } else {
            Object[] args = {"protocolVersion"};
            throw new DataException(DataExceptionCode.TPM_003, DataExceptionCode.GLB_001_MSG, args);
        }

        if (idMap.get("messageID") != null) {
            Attr messageID = doc.createAttribute("messageID");
            messageID.setValue(idMap.get("messageID"));
            rootElement.setAttributeNode(messageID);
        } else {
            Object[] args = {"messageID"};
            throw new DataException(DataExceptionCode.TPM_004, DataExceptionCode.GLB_001_MSG, args);
        }

        if (idMap.get("offerID") != null) {
            Attr offerID = doc.createAttribute("offerID");
            offerID.setValue(idMap.get("offerID"));
            rootElement.setAttributeNode(offerID);
        } else {
            Object[] args = {"offerID"};
            // TODO codes
            throw new DataException(DataExceptionCode.TPM_004, DataExceptionCode.GLB_001_MSG, args);
        }

        if (idMap.get("id") == null) {
            Object[] args = {"id"};
            throw new DataException(DataExceptionCode.TPM_020, DataExceptionCode.GLB_001_MSG, args);
        }

//        if (idMap.get("offerID").contains("SERVERMSDC")
//                || idMap.get("offerID").contains("SERVERAUTH")) {// Network
//            Element enrollmentRequest = doc.createElement("enrollmentRequest");
//            enrollmentRequest.setAttribute("id", idMap.get("id"));
//            rootElement.appendChild(enrollmentRequest);
//
//            Element fqdnPropertyElement = doc.createElement("property");
//            Attr fqdnNameAttr = doc.createAttribute("name");
//            fqdnNameAttr.setValue("fqdn");
//            fqdnPropertyElement.setAttributeNode(fqdnNameAttr);
//
//            Attr fqdnValueAttr = doc.createAttribute("value");
//            NetworkTokenTO networkTokenTO = (NetworkTokenTO) tokenTO;
//            if (networkTokenTO.getEnrollmentOffice() != null
//                    && networkTokenTO.getEnrollmentOffice().returnFQDN() != null) {
//                fqdnValueAttr.setValue(networkTokenTO.getEnrollmentOffice().returnFQDN());
//            }
//            fqdnPropertyElement.setAttributeNode(fqdnValueAttr);
//            enrollmentRequest.appendChild(fqdnPropertyElement);
//
//			/*
//             *
//			 */
//            Element siteIDPropertyElement = doc.createElement("property");
//            Attr siteNameAttr = doc.createAttribute("name");
//            siteNameAttr.setValue("siteId");
//            siteIDPropertyElement.setAttributeNode(siteNameAttr);
//
//            Attr siteValueAttr = doc.createAttribute("value");
//            if (networkTokenTO.getEnrollmentOffice() != null) {
//                siteValueAttr
//                        .setValue(String.valueOf(networkTokenTO.getEnrollmentOffice().getId()));
//            }
//            siteIDPropertyElement.setAttributeNode(siteValueAttr);
//            enrollmentRequest.appendChild(siteIDPropertyElement);
//
//			/*
//			 * 
//			 */
//            Element managerUidPropertyElement = doc.createElement("property");
//            Attr managerUidAttr = doc.createAttribute("name");
//            managerUidAttr.setValue("managerUid");
//            managerUidPropertyElement.setAttributeNode(managerUidAttr);
//
//            Attr managerUidValueAttr = doc.createAttribute("value");
//            if (networkTokenTO.getEnrollmentOffice() != null
//                    && networkTokenTO.getEnrollmentOffice().getManager() != null) {
//                managerUidValueAttr.setValue(String.valueOf(networkTokenTO.getEnrollmentOffice()
//                        .getManager().getUserId()));
//            }
//            managerUidPropertyElement.setAttributeNode(managerUidValueAttr);
//            enrollmentRequest.appendChild(managerUidPropertyElement);

        /*} else{*/ // PersonTokenTo
            Element enrollmentRequest = doc.createElement("enrollmentRequest");
            rootElement.appendChild(enrollmentRequest);
            enrollmentRequest.setAttribute("id", idMap.get("id"));

            Element firstNamePropertyElement = doc.createElement("property");
            Attr firstNameNameAttr = doc.createAttribute("name");
            firstNameNameAttr.setValue("firstName");
            firstNamePropertyElement.setAttributeNode(firstNameNameAttr);

            Attr firstNameValueAttr = doc.createAttribute("value");
            PersonTokenTO personTokenTO = (PersonTokenTO) tokenTO;
            if (personTokenTO != null && personTokenTO.getPerson() != null) {
                ////Edited By Adldoost
                //firstNameValueAttr.setValue(personTokenTO.getPerson().getFirstName());
            	firstNameValueAttr.setValue(personTokenTO.getPerson().getFirstName() + "-" + personTokenTO.getPerson().getLastName());
            }
            firstNamePropertyElement.setAttributeNode(firstNameValueAttr);
            enrollmentRequest.appendChild(firstNamePropertyElement);

			/*
			 * 
			 */

            Element lastNamePropertyElement = doc.createElement("property");
            Attr lastNameNameAttr = doc.createAttribute("name");
            lastNameNameAttr.setValue("lastName");
            lastNamePropertyElement.setAttributeNode(lastNameNameAttr);

            Attr lastNameValueAttr = doc.createAttribute("value");
            if (personTokenTO != null && personTokenTO.getPerson() != null) {
                ////Edited By Adldoost
                //lastNameValueAttr.setValue(personTokenTO.getPerson().getLastName());
            	//Anbari : Edited - change from township to county
            	DepartmentTO dep = personTokenTO.getPerson().getDepartment();
            	lastNameValueAttr.setValue(dep.getLocation().getProvince().getName() + "-" +
            			dep.getLocation().getCounty().getName() + "-" +
            			dep.getCode());
            }
            lastNamePropertyElement.setAttributeNode(lastNameValueAttr);
            enrollmentRequest.appendChild(lastNamePropertyElement);
			/*
			 * 
			 */

            Element emailPropertyElement = doc.createElement("property");
            Attr emailNameAttr = doc.createAttribute("name");
            emailNameAttr.setValue("email");
            emailPropertyElement.setAttributeNode(emailNameAttr);

            Attr emailValueAttr = doc.createAttribute("value");
            if (personTokenTO != null && personTokenTO.getPerson() != null) {
                emailValueAttr.setValue(personTokenTO.getPerson().getEmail());
            }
            emailPropertyElement.setAttributeNode(emailValueAttr);
            enrollmentRequest.appendChild(emailPropertyElement);
			/*
			 * 
			 */
            Element uidPropertyElement = doc.createElement("property");
            Attr uidNameAttr = doc.createAttribute("name");
            uidNameAttr.setValue("uid");
            uidPropertyElement.setAttributeNode(uidNameAttr);

            Attr uidValueAttr = doc.createAttribute("value");
            if (personTokenTO != null && personTokenTO.getPerson() != null) {
                uidValueAttr.setValue(String.valueOf(personTokenTO.getPerson().getUserId()));
            }
            uidPropertyElement.setAttributeNode(uidValueAttr);
            enrollmentRequest.appendChild(uidPropertyElement);

			/*
			 * optional
			 */

            // Element organizationPropertyElement =
            // doc.createElement("property");
            // Attr organizationNameAttr = doc.createAttribute("name");
            // organizationNameAttr.setValue("organization");
            // organizationPropertyElement.setAttributeNode(organizationNameAttr);
            //
            // Attr organizationValueAttr = doc.createAttribute("value");
            // if (personTokenTO != null && personTokenTO.getPerson() != null) {
            // organizationValueAttr.setValue(personTokenTO.getPerson().getDepartmentName());
            // }
            // organizationPropertyElement.setAttributeNode(organizationValueAttr);
            // enrollmentRequest.appendChild(organizationPropertyElement);

			/*
			 * optional
			 */

            // Element rolePropertyElement = doc.createElement("property");
            // Attr roleNameAttr = doc.createAttribute("name");
            // roleNameAttr.setValue("organization");
            // rolePropertyElement.setAttributeNode(roleNameAttr);
            //
            // Attr roleValueAttr = doc.createAttribute("value");
            // roleValueAttr.setValue("");
            // rolePropertyElement.setAttributeNode(roleValueAttr);
            // enrollmentRequest.appendChild(rolePropertyElement);

        //}
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            // // write the content into xml file
            // TransformerFactory transformerFactory =
            // TransformerFactory.newInstance();
            // Transformer transformer = transformerFactory.newTransformer();
            // DOMSource source = new DOMSource(doc);
            // StreamResult result = new StreamResult(new File(
            // "/Users/Sina/Desktop/Generated by DOM.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            // transformer.transform(source, result);
            //
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            Result result = new StreamResult(out);
            transformer.transform(source, result);
            logger.info("File saved!");
        } catch (TransformerConfigurationException e) {
            throw new DataException(DataExceptionCode.TPM_016, e);
        } catch (TransformerException e) {
            throw new DataException(DataExceptionCode.TPM_017, e);
        }
        return out.toByteArray();
    }

    private byte[] createSearchReqXML(Document doc, TokenTO tokenTO, Map<String, String> idMap) throws BaseException {
        Element rootElement = doc.createElement("searchRequests");
        doc.appendChild(rootElement);

        if (idMap.get("protocolVersion") != null) {
            Attr protocolVersionAttr = doc.createAttribute("protocolVersion");
            protocolVersionAttr.setValue(idMap.get("protocolVersion"));
            rootElement.setAttributeNode(protocolVersionAttr);
        } else {
            Object[] args = {"protocolVersion"};
            throw new DataException(DataExceptionCode.TPM_003, DataExceptionCode.GLB_001_MSG, args);
        }

        if (idMap.get("messageID") != null) {
            Attr messageID = doc.createAttribute("messageID");
            messageID.setValue(idMap.get("messageID"));
            rootElement.setAttributeNode(messageID);
        } else {
            Object[] args = {"messageID"};
            throw new DataException(DataExceptionCode.TPM_004, DataExceptionCode.GLB_001_MSG, args);
        }

        if (idMap.get("offerID") != null) {
            Attr offerID = doc.createAttribute("offerID");
            offerID.setValue(idMap.get("offerID"));
            rootElement.setAttributeNode(offerID);
        } else {
            Object[] args = {"offerID"};
            throw new DataException(DataExceptionCode.TPM_005, DataExceptionCode.GLB_001_MSG, args);
        }

        if (idMap.get("id") == null) {
            Object[] args = {"id"};
            throw new DataException(DataExceptionCode.TPM_018, DataExceptionCode.GLB_001_MSG, args);
        }

        Element searchRequestElement = doc.createElement("searchRequest");
        searchRequestElement.setAttribute("id", idMap.get("id"));
        rootElement.appendChild(searchRequestElement);

        Element requestIdElement = doc.createElement("requestId");
        if (tokenTO == null || tokenTO.getRequestID() == null) {
            Object[] args = {"requestId"};
            throw new DataException(DataExceptionCode.TPM_023, DataExceptionCode.GLB_001_MSG, args);
        } else {
            requestIdElement.appendChild(doc.createTextNode(tokenTO.getRequestID()));
        }
        searchRequestElement.appendChild(requestIdElement);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            Result result = new StreamResult(out);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            throw new DataException(DataExceptionCode.TPM_021, e);
        } catch (TransformerException e) {
            throw new DataException(DataExceptionCode.TPM_022, e);
        }

        // // write the content into xml file
        // TransformerFactory transformerFactory =
        // TransformerFactory.newInstance();
        // Transformer transformer = transformerFactory.newTransformer();
        // DOMSource source = new DOMSource(doc);
        // StreamResult result = new StreamResult(new
        // File("/Users/Sina/Desktop/Generated by DOM.xml"));

        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);

        // transformer.transform(source, result);
        //
        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);

        logger.info("File saved!");
        return out.toByteArray();
        // return null;
    }

    private byte[] createRevocationReqXML(Document doc, TokenTO tokenTO, Map<String, String> idMap)
            throws TransformerException, BaseException {
        Element rootElement = doc.createElement("revocationRequests");
        doc.appendChild(rootElement);

        if (idMap.get("protocolVersion") != null) {
            Attr protocolVersionAttr = doc.createAttribute("protocolVersion");
            protocolVersionAttr.setValue(idMap.get("protocolVersion"));
            rootElement.setAttributeNode(protocolVersionAttr);
        } else {
            Object[] args = {"protocolVersion"};
            throw new DataException(DataExceptionCode.TPM_006, DataExceptionCode.GLB_001_MSG, args);
        }

        if (idMap.get("messageID") != null) {
            Attr messageID = doc.createAttribute("messageID");
            messageID.setValue(idMap.get("messageID"));
            rootElement.setAttributeNode(messageID);
        } else {
            Object[] args = {"messageID"};
            throw new DataException(DataExceptionCode.TPM_007, DataExceptionCode.GLB_001_MSG, args);
        }

        if (idMap.get("offerID") != null) {
            Attr offerID = doc.createAttribute("offerID");
            offerID.setValue(idMap.get("offerID"));
            rootElement.setAttributeNode(offerID);
        } else {
            Object[] args = {"offerID"};
            throw new DataException(DataExceptionCode.TPM_008, DataExceptionCode.GLB_001_MSG, args);
        }

        if (idMap.get("id") == null) {
            Object[] args = {"id"};
            throw new DataException(DataExceptionCode.TPM_019, DataExceptionCode.GLB_001_MSG, args);
        }

        Element revocationRequestElement = doc.createElement("revocationRequest");
        revocationRequestElement.setAttribute("id", idMap.get("id"));
        rootElement.appendChild(revocationRequestElement);

        Element akiElement = doc.createElement("AKI");
        if (tokenTO != null && tokenTO.getAKI() != null) {
            akiElement.appendChild(doc.createTextNode(tokenTO.getAKI()));
        }
        revocationRequestElement.appendChild(akiElement);

        Element certificateSerialNumber = doc.createElement("certificateSerialNumber");
        if (tokenTO != null && tokenTO.getCertificateSerialNumber() != null) {
            certificateSerialNumber.appendChild(doc.createTextNode(tokenTO
                    .getCertificateSerialNumber()));
        }
        revocationRequestElement.appendChild(certificateSerialNumber);

        // TODO this tag element is optional and will be provided later id it's
        // needed
        Element reasonElement = doc.createElement("reason");
        reasonElement.appendChild(doc.createTextNode(String.valueOf(tokenTO.getReason())));
        revocationRequestElement.appendChild(reasonElement);

        Element commentElement = doc.createElement("comment");
        String comment = idMap.get("comment");
        if (comment != null) {
            commentElement.appendChild(doc.createTextNode(comment));
        } else {
            commentElement.appendChild(doc.createTextNode("Not Specified."));
        }
        revocationRequestElement.appendChild(commentElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Result result = new StreamResult(out);

        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);

        transformer.transform(source, result);

        logger.info("File saved!");
        return out.toByteArray();
    }
    
    //created by Adldoost
    private byte[] createRenewalReqXML(Document doc, TokenTO tokenTO, Map<String, String> idMap)
            throws TransformerException, BaseException {
        Element rootElement = doc.createElement("renewalRequests");
        doc.appendChild(rootElement);

        if (idMap.get("protocolVersion") != null) {
            Attr protocolVersionAttr = doc.createAttribute("protocolVersion");
            protocolVersionAttr.setValue(idMap.get("protocolVersion"));
            rootElement.setAttributeNode(protocolVersionAttr);
        } else {
            Object[] args = {"protocolVersion"};
            throw new DataException(DataExceptionCode.TPM_006, DataExceptionCode.GLB_001_MSG, args);
        }

        if (idMap.get("messageID") != null) {
            Attr messageID = doc.createAttribute("messageID");
            messageID.setValue(idMap.get("messageID"));
            rootElement.setAttributeNode(messageID);
        } else {
            Object[] args = {"messageID"};
            throw new DataException(DataExceptionCode.TPM_007, DataExceptionCode.GLB_001_MSG, args);
        }

        if (idMap.get("offerID") != null) {
            Attr offerID = doc.createAttribute("offerID");
            offerID.setValue(idMap.get("offerID"));
            rootElement.setAttributeNode(offerID);
        } else {
            Object[] args = {"offerID"};
            throw new DataException(DataExceptionCode.TPM_008, DataExceptionCode.GLB_001_MSG, args);
        }

        if (idMap.get("id") == null) {
            Object[] args = {"id"};
            throw new DataException(DataExceptionCode.TPM_019, DataExceptionCode.GLB_001_MSG, args);
        }

        Element renewalRequestElement = doc.createElement("renewalRequest");
        renewalRequestElement.setAttribute("id", idMap.get("id"));
        rootElement.appendChild(renewalRequestElement);

        Element akiElement = doc.createElement("AKI");
        if (tokenTO != null && tokenTO.getAKI() != null) {
            akiElement.appendChild(doc.createTextNode(tokenTO.getAKI()));
        }
        renewalRequestElement.appendChild(akiElement);

        Element certificateSerialNumber = doc.createElement("certificateSerialNumber");
        if (tokenTO != null && tokenTO.getCertificateSerialNumber() != null) {
            certificateSerialNumber.appendChild(doc.createTextNode(tokenTO
                    .getCertificateSerialNumber()));
        }
        renewalRequestElement.appendChild(certificateSerialNumber);

//        // TODO this tag element is optional and will be provided later id it's
//        // needed
//        Element reasonElement = doc.createElement("reason");
//        reasonElement.appendChild(doc.createTextNode(String.valueOf(tokenTO.getReason())));
//        revocationRequestElement.appendChild(reasonElement);
//
//        Element commentElement = doc.createElement("comment");
//        String comment = idMap.get("comment");
//        if (comment != null) {
//            commentElement.appendChild(doc.createTextNode(comment));
//        } else {
//            commentElement.appendChild(doc.createTextNode("Not Specified."));
//        }
//        revocationRequestElement.appendChild(commentElement);
        
        Element nameElement = doc.createElement("property");
        Attr firstNameNameAttr = doc.createAttribute("name");
        firstNameNameAttr.setValue("");
        nameElement.setAttributeNode(firstNameNameAttr);
        Attr firstNameValueAttr = doc.createAttribute("value");
        firstNameValueAttr.setValue("");
        nameElement.setAttributeNode(firstNameValueAttr);
        

//        Element propertyElement = doc.createElement("property");
//        renewalRequestElement.setAttribute("value", "");
	    renewalRequestElement.appendChild(nameElement);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Result result = new StreamResult(out);

        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);

        transformer.transform(source, result);

        logger.info("File saved!");
        return out.toByteArray();
    }
    
    //created by Adldoost
    private byte[] createActivationReqXML(Document doc, TokenTO tokenTO, Map<String, String> idMap)
            throws TransformerException, BaseException {
        Element rootElement = doc.createElement("revocationRequests");
        doc.appendChild(rootElement);

        if (idMap.get("protocolVersion") != null) {
            Attr protocolVersionAttr = doc.createAttribute("protocolVersion");
            protocolVersionAttr.setValue(idMap.get("protocolVersion"));
            rootElement.setAttributeNode(protocolVersionAttr);
        } else {
            Object[] args = {"protocolVersion"};
            throw new DataException(DataExceptionCode.TPM_006, DataExceptionCode.GLB_001_MSG, args);
        }

        if (idMap.get("messageID") != null) {
            Attr messageID = doc.createAttribute("messageID");
            messageID.setValue(idMap.get("messageID"));
            rootElement.setAttributeNode(messageID);
        } else {
            Object[] args = {"messageID"};
            throw new DataException(DataExceptionCode.TPM_007, DataExceptionCode.GLB_001_MSG, args);
        }

        if (idMap.get("offerID") != null) {
            Attr offerID = doc.createAttribute("offerID");
            offerID.setValue(idMap.get("offerID"));
            rootElement.setAttributeNode(offerID);
        } else {
            Object[] args = {"offerID"};
            throw new DataException(DataExceptionCode.TPM_008, DataExceptionCode.GLB_001_MSG, args);
        }

        if (idMap.get("id") == null) {
            Object[] args = {"id"};
            throw new DataException(DataExceptionCode.TPM_019, DataExceptionCode.GLB_001_MSG, args);
        }

        Element activationRequestElement = doc.createElement("revocationRequest");
        activationRequestElement.setAttribute("id", idMap.get("id"));
        rootElement.appendChild(activationRequestElement);

        Element akiElement = doc.createElement("AKI");
        if (tokenTO != null && tokenTO.getAKI() != null) {
            akiElement.appendChild(doc.createTextNode(tokenTO.getAKI()));
        }
        activationRequestElement.appendChild(akiElement);

        Element certificateSerialNumber = doc.createElement("certificateSerialNumber");
        if (tokenTO != null && tokenTO.getCertificateSerialNumber() != null) {
            certificateSerialNumber.appendChild(doc.createTextNode(tokenTO
                    .getCertificateSerialNumber()));
        }
        activationRequestElement.appendChild(certificateSerialNumber);

//        // TODO this tag element is optional and will be provided later id it's
        // needed
        Element reasonElement = doc.createElement("reason");
        tokenTO.setReason(TokenReIssuanceReason.removeFromCRL);
        reasonElement.appendChild(doc.createTextNode(String.valueOf(tokenTO.getReason())));
        activationRequestElement.appendChild(reasonElement);
//
        Element commentElement = doc.createElement("comment");
        String comment = idMap.get("comment");
        if (comment != null) {
            commentElement.appendChild(doc.createTextNode(comment));
        } else {
            commentElement.appendChild(doc.createTextNode("Card Retrieve"));
        }
        activationRequestElement.appendChild(commentElement);

//        Element propertyElement = doc.createElement("property");
//        activationRequestElement.setAttribute("value", "");
//	    activationRequestElement.appendChild(propertyElement);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Result result = new StreamResult(out);

        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);

        transformer.transform(source, result);

        logger.info("File saved!");
        return out.toByteArray();
    }

    @Override
    public ExtEntityTO readXML(String xmlData, ExtEntityTO to) throws BaseException {

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setIgnoringElementContentWhitespace(true);
            dbFactory.setNamespaceAware(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            byte[] xmlDATA = xmlData.getBytes();
            ByteArrayInputStream in = new ByteArrayInputStream(xmlDATA);
            Document doc = dBuilder.parse(in);

            /**
             * XPath
             */
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            searchForErrorInResponseXML(doc, xpath);

            String xpathQuery = "//*/*/@id";
            String requestId = (String) nodeExtractor(doc, xpath, xpathQuery);

            // enrollReq
            if (requestId.contains("enrollReq")) {
                if (to instanceof PersonTokenTO) {
                    return readPersonTokenTO(doc, xpath, to);
                }
//Commented By Adldoost
//                if (to instanceof NetworkTokenTO) {
//                    return readNetworkTokenTO(doc, xpath, to);
//                }
            }

            // searchReq
            if (requestId.contains("searchReq")) {
                return readSearchResultAsTokenTO(doc, xpath, to);
            }

            // revocationReq
            if (requestId.contains("revocationReq")) {
                return new TokenTO();
            }
            
            //Adldoost
            //renewalReq
            if (requestId.contains("renewalReq")) {
            	return readPersonTokenTOFromRenewalResponse(doc, xpath, to);
            }

        } catch (ParserConfigurationException e) {
            throw new DataException(DataExceptionCode.TPM_011, e);
        } catch (SAXException e) {
            throw new DataException(DataExceptionCode.TPM_012, e);
        } catch (IOException e) {
            throw new DataException(DataExceptionCode.TPM_013, e);
        } catch (XPathExpressionException e) {
            throw new DataException(DataExceptionCode.TPM_014, e);
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
     *
     */
    @Override
    public ExtEntityTO readXML(String xmlData,
                               ExtEntityTO to,
                               Map<String, String> attributesMap) throws BaseException {
        return null;
    }

    private ExtEntityTO readSearchResultAsTokenTO(Document doc, XPath xpath, ExtEntityTO to) throws BaseException {

        TokenTO tokenTO = (TokenTO) to;

        String xpathQuery = "//searchRequestsResponses/searchRequestsResponse/request/@id";
        String requestId;
        try {
            requestId = (String) nodeExtractor(doc, xpath, xpathQuery);
            if (requestId != null) {
                tokenTO.setRequestID(requestId);
            } else {
                Object[] args = {"requestId"};
                throw new DataException(DataExceptionCode.TPM_009, DataExceptionCode.GLB_001_MSG,
                        args);
            }
            xpathQuery = "//searchRequestsResponses/searchRequestsResponse/request/@state";
            String state = (String) nodeExtractor(doc, xpath, xpathQuery);
            if (state != null) {
                if (state.contains("PROCESSED")) {
                    tokenTO.setState(TokenState.PROCESSED);
                }
            } else {
                Object[] args = {"state"};
                throw new DataException(DataExceptionCode.TPM_024, DataExceptionCode.GLB_001_MSG,// TODO
                        args);
            }

            xpathQuery = "//searchRequestsResponses/searchRequestsResponse/request/certificate/AKI/text()";
            tokenTO.setAKI((String) nodeExtractor(doc, xpath, xpathQuery));

            xpathQuery = "//searchRequestsResponses/searchRequestsResponse/request/certificate/certificateSerialNumber/text()";
            tokenTO.setCertificateSerialNumber((String) nodeExtractor(doc, xpath, xpathQuery));
        } catch (XPathExpressionException e) {
            throw new DataException(DataExceptionCode.TPM_015, e);
        }

        return tokenTO;
    }

//Commented by Adldoost
//    private ExtEntityTO readNetworkTokenTO(Document doc, XPath xpath, ExtEntityTO to)
//            throws XPathExpressionException, BaseException {
//        NetworkTokenTO networkTokenTO = (NetworkTokenTO) to;
//
//        String xpathQuery = "//enrollmentResponses/enrollmentResponse/requestId/text()";
//        String requestId = (String) nodeExtractor(doc, xpath, xpathQuery);
//        if (requestId != null) {
//            networkTokenTO.setRequestID(requestId);
//        } else {
//            Object[] args = {"requestId"};
//            throw new DataException(DataExceptionCode.TPM_025, DataExceptionCode.GLB_001_MSG, args);
//        }
//        return networkTokenTO;
//    }

    /**
     * Checks whether the response XML contains an error with its corresponding
     * tag or not
     *
     * @param doc
     * @param xpath
     * @throws XPathExpressionException
     */
    private void searchForErrorInResponseXML(Document doc, XPath xpath)
            throws XPathExpressionException, BaseException {
        String xpathQuery = "//enrollmentResponses/enrollmentResponse/error/message";
        String message = (String) nodeExtractor(doc, xpath, xpathQuery);

        xpathQuery = "//enrollmentResponses/enrollmentResponse/error/code";
        String code = (String) nodeExtractor(doc, xpath, xpathQuery);
        if (message != null) {
            throw new DataException(code, message);
        } else {
            xpathQuery = "//errorResponse/error/message";
            message = (String) nodeExtractor(doc, xpath, xpathQuery);
            xpathQuery = "//errorResponse/error/code";
            code = (String) nodeExtractor(doc, xpath, xpathQuery);
            if (message != null) {
                throw new DataException(code, message);
            } else {
                xpathQuery = "//revocationResponses/revocationResponse/error/message";
                message = (String) nodeExtractor(doc, xpath, xpathQuery);
                xpathQuery = "//revocationResponses/revocationResponse/error/code";
                code = (String) nodeExtractor(doc, xpath, xpathQuery);
                if (message != null) {
                    throw new DataException(code, message);
                }
            }
        }
    }

    private ExtEntityTO readPersonTokenTO(Document doc, XPath xpath, ExtEntityTO to)
            throws XPathExpressionException, BaseException {
        PersonTokenTO personTokenTO = (PersonTokenTO) to;

        String xpathQuery = "//enrollmentResponses/enrollmentResponse/requestId/text()";
        String requestId = (String) nodeExtractor(doc, xpath, xpathQuery);

        if (requestId != null) {
            personTokenTO.setRequestID(requestId);
        } else {
            Object[] args = {"requestId"};
            throw new DataException(DataExceptionCode.TPM_010, DataExceptionCode.GLB_001_MSG, args);
        }
        return personTokenTO;
    }
    
    //Adldoost
    private ExtEntityTO readPersonTokenTOFromRenewalResponse(Document doc, XPath xpath, ExtEntityTO to)
            throws XPathExpressionException, BaseException {
        PersonTokenTO personTokenTO = (PersonTokenTO) to;

        String xpathQuery = "//renewalResponses/renewalResponse/requestId/text()";
        String requestId = (String) nodeExtractor(doc, xpath, xpathQuery);

        if (requestId != null) {
            personTokenTO.setRequestID(requestId);
        } else {
            Object[] args = {"requestId"};
            throw new DataException(DataExceptionCode.TPM_026, DataExceptionCode.GLB_001_MSG, args);
        }
        return personTokenTO;
    }

    /**
     * @param doc
     * @param xpath
     * @param xpathQuery
     * @throws XPathExpressionException
     */
    private Object nodeExtractor(Document doc, XPath xpath, String xpathQuery)
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
        // String[] elements = xpathQuery.split("/");
        // String element = elements[elements.length-2];
        // Object[] args = {element};
        // System.out.println(element);
        // throw new
        // DataException(DataExceptionCode.TPM_015,DataExceptionCode.GLB_001_MSG,args);
        return null;
    }
}
