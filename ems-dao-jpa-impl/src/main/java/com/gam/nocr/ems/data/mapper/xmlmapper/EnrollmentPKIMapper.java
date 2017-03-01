/**
 *
 */
package com.gam.nocr.ems.data.mapper.xmlmapper;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
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
public class EnrollmentPKIMapper implements XMLMapper {

    private static final Logger logger = BaseLog.getLogger(EnrollmentPKIMapper.class);

    @Override
    /**
     * creates networkReq.xml
     */
    public byte[] writeXML(ExtEntityTO to, Map<String, String> attributesMap) throws BaseException {

        EnrollmentOfficeTO enrollmentOfficeTO = (EnrollmentOfficeTO) to;
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
                throw new DataException(DataExceptionCode.EPM_001, DataExceptionCode.GLB_001_MSG,
                        args);
            }

            if (attributesMap.get("messageID") != null) {
                Attr messageID = doc.createAttribute("messageID");
                messageID.setValue(attributesMap.get("messageID"));
                rootElement.setAttributeNode(messageID);
            } else {
                Object[] args = {"messageID"};
                throw new DataException(DataExceptionCode.EPM_002, DataExceptionCode.GLB_001_MSG,
                        args);
            }

            if (attributesMap.get("offerID") != null) {
                Attr offerID = doc.createAttribute("offerID");
                offerID.setValue(attributesMap.get("offerID"));
                rootElement.setAttributeNode(offerID);
            } else {
                Object[] args = {"offerID"};
                throw new DataException(DataExceptionCode.EPM_003, DataExceptionCode.GLB_001_MSG,
                        args);
            }

            Element enrollmentRequestElement = doc.createElement("enrollmentRequest");
            rootElement.appendChild(enrollmentRequestElement);

            Element fqdnElement = doc.createElement("fqdn");
            if (enrollmentOfficeTO != null && enrollmentOfficeTO.returnFQDN() != null) {
                fqdnElement.appendChild(doc.createTextNode(enrollmentOfficeTO.returnFQDN()));
            }
            enrollmentRequestElement.appendChild(fqdnElement);

            Element siteIdElement = doc.createElement("siteId");
            if (enrollmentOfficeTO != null) {
                siteIdElement.appendChild(doc.createTextNode(String.valueOf(enrollmentOfficeTO
                        .getId())));
            }
            enrollmentRequestElement.appendChild(siteIdElement);

            Element managerUidElement = doc.createElement("managerUid");
            if (enrollmentOfficeTO != null && enrollmentOfficeTO.getManager() != null) {
                managerUidElement.appendChild(doc.createTextNode(String.valueOf(enrollmentOfficeTO
                        .getManager().getUserId())));
            }
            enrollmentRequestElement.appendChild(managerUidElement);

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
            throw new DataException(DataExceptionCode.EPM_004, e);
        } catch (TransformerConfigurationException e) {
            throw new DataException(DataExceptionCode.EPM_005, e);
        } catch (TransformerException e) {
            throw new DataException(DataExceptionCode.EPM_006, e);
        }
    }

    @Override
    public ExtEntityTO readXML(String xmlData, ExtEntityTO to) throws BaseException {
        //
        // try {
        //
        // /**
        // * DOM
        // */
        // DocumentBuilderFactory dbFactory =
        // DocumentBuilderFactory.newInstance();
        // dbFactory.setNamespaceAware(true);
        // DocumentBuilder dBuilder;
        // dBuilder = dbFactory.newDocumentBuilder();
        // Document doc = dBuilder.parse(xmlData);
        //
        // /**
        // * XPath
        // */
        // // TODO identifier
        // XPathFactory factory = XPathFactory.newInstance();
        // XPath xpath = factory.newXPath();
        // PersonTO personTO = (PersonTO) to;
        //
        // String xpathQuery =
        // "//enrollmentResponses/enrollmentResponse/error/message/text()";
        // String errorMessage = (String) nodeExtractor(doc, xpath, xpathQuery);
        // if(errorMessage!=null) {
        //
        // }
        // else {
        // xpathQuery =
        // "//enrollmentResponses/enrollmentResponse/error/code/text()";
        // String errorCode = (String) nodeExtractor(doc, xpath, xpathQuery);
        // throw new DataException(errorCode,errorMessage);//TODO
        // }
        //
        // } catch (ParserConfigurationException e) {
        // // TODO Auto-generated catch block
        // logger.error(DataExceptionCode.GLB_ERR_MSG, e);
        // } catch (SAXException e) {
        // // TODO Auto-generated catch block
        // logger.error(DataExceptionCode.GLB_ERR_MSG, e);
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // logger.error(DataExceptionCode.GLB_ERR_MSG, e);
        // } catch (XPathExpressionException e) {
        // // TODO Auto-generated catch block
        // logger.error(DataExceptionCode.GLB_ERR_MSG, e);
        // }

        return null;
    }

    // /**
    // * @param doc
    // * @param xpath
    // * @param xpathQuery
    // * @throws XPathExpressionException
    // */
    // private Object nodeExtractor(Document doc, XPath xpath, String
    // xpathQuery)
    // throws XPathExpressionException {
    // XPathExpression expr = xpath.compile(xpathQuery);
    // Object result = expr.evaluate(doc, XPathConstants.NODESET);
    //
    // NodeList nodes = (NodeList) result;
    // return nodes.item(0).getNodeValue();
    // }


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
