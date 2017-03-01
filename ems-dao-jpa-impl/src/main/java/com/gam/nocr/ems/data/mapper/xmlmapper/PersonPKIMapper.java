/**
 *
 */
package com.gam.nocr.ems.data.mapper.xmlmapper;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.PersonTO;
import org.slf4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * @author Sina Golesorkhi
 */
public class PersonPKIMapper implements XMLMapper {

    private static final Logger logger = BaseLog.getLogger(PersonPKIMapper.class);

    /**
     * Creates enrollmentReq.xml
     */
    public byte[] writeXML(ExtEntityTO to, Map<String, String> attributesMap) throws BaseException {
        PersonTO personTO = (PersonTO) to;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;

        try {
            docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("enrollmentRequests");
            doc.appendChild(rootElement);

            if (attributesMap.get("protocolVersion") != null) {
                Attr protocolVersionAttr = doc.createAttribute("protocolVersion");
                protocolVersionAttr.setValue(attributesMap.get("protocolVersion"));
                rootElement.setAttributeNode(protocolVersionAttr);
            } else {
                Object[] args = {"protocolVersion"};
                throw new DataException(DataExceptionCode.PPM_001, DataExceptionCode.GLB_001_MSG,
                        args);
            }

            if (attributesMap.get("messageID") != null) {
                Attr messageID = doc.createAttribute("messageID");
                messageID.setValue(attributesMap.get("messageID"));
                rootElement.setAttributeNode(messageID);
            } else {
                Object[] args = {"messageID"};
                throw new DataException(DataExceptionCode.PPM_002, DataExceptionCode.GLB_001_MSG,
                        args);
            }

            if (attributesMap.get("offerID") != null) {
                Attr offerID = doc.createAttribute("offerID");
                offerID.setValue(attributesMap.get("offerID"));
                rootElement.setAttributeNode(offerID);
            } else {
                Object[] args = {"offerID"};
                throw new DataException(DataExceptionCode.PPM_003, DataExceptionCode.GLB_001_MSG,
                        args);
            }

            Element enrollmentRequestElement = doc.createElement("enrollmentRequest");
            rootElement.appendChild(enrollmentRequestElement);

            Element firstNameElement = doc.createElement("firstName");
            if (personTO != null && personTO.getFatherName() != null) {
                firstNameElement.appendChild(doc.createTextNode(personTO.getFirstName()));
            }
            enrollmentRequestElement.appendChild(firstNameElement);

            Element lastNameElement = doc.createElement("lastName");
            if (personTO != null && personTO.getLastName() != null) {
                lastNameElement.appendChild(doc.createTextNode(personTO.getLastName()));
            }
            enrollmentRequestElement.appendChild(lastNameElement);
            Element emailElement = doc.createElement("email");
            if (personTO != null && personTO.getEmail() != null) {
                emailElement.appendChild(doc.createTextNode(personTO.getEmail()));
            }
            enrollmentRequestElement.appendChild(emailElement);

            Element uidElement = doc.createElement("uid");
            // TODO what if there is not uid available?
            uidElement.appendChild(doc.createTextNode(String.valueOf(personTO.getUserId())));
            enrollmentRequestElement.appendChild(uidElement);

            Element organizationElement = doc.createElement("organization");
            if (personTO != null && personTO.getDepartment() != null
                    && personTO.getDepartment().returnFQDN() != null) {
                organizationElement.appendChild(doc.createTextNode(personTO.getDepartment()
                        .returnFQDN()));
            }
            enrollmentRequestElement.appendChild(organizationElement);

            Element roleElement = doc.createElement("role");
            if (personTO != null && personTO.getDepartment() != null
                    && personTO.getDepartment().getParentDN() != null) {
                roleElement.appendChild(doc.createTextNode(personTO.getDepartment().getParentDN()));
            }
            enrollmentRequestElement.appendChild(roleElement);

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

        } catch (ParserConfigurationException e) {
            throw new DataException(DataExceptionCode.PPM_005, e);
        } catch (TransformerConfigurationException e) {
            throw new DataException(DataExceptionCode.PPM_006, e);
        } catch (TransformerException e) {
            throw new DataException(DataExceptionCode.PPM_007, e);
        }
    }

    public ExtEntityTO readXML(String xmlData, ExtEntityTO to) throws BaseException {
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
}
