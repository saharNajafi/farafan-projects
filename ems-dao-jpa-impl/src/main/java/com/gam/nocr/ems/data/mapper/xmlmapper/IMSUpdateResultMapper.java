package com.gam.nocr.ems.data.mapper.xmlmapper;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.config.DataExceptionCode;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class IMSUpdateResultMapper implements XMLMapper {

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

    /**
     * The method readCitizenInfoResult is used to get the citizen attributes from the xml bellow
     *
     * @param doc
     * @param xpath
     * @return an instance of type {@link HashMap<String, String>} which carries the names and values of the attributes
     * @throws XPathExpressionException
     * @throws BaseException
     */
    private HashMap<String, String> readCitizenInfoResult(Document doc,
                                                          XPath xpath) throws XPathExpressionException, BaseException {

        /**
         * NationalIds & AfisValidates
         */
        String xpathQuery = "//CitInfoResult/Citizen";
        HashMap<String, String> attributeMap = new HashMap<String, String>();
        XPathExpression expr = xpath.compile(xpathQuery);
        Object result = expr.evaluate(doc, XPathConstants.NODESET);

        NodeList nodes = (NodeList) result;
        for (int i = 0; i < nodes.getLength(); i++) {
            Attr nationalIdAttr = (Attr) nodes.item(i).getAttributes().getNamedItem("nid");
            if (nationalIdAttr == null) {
                throw new DataException(DataExceptionCode.IUR_003, DataExceptionCode.GLB_001_MSG, new String[]{"nationalId"});
            }

            Attr afisValidateAttr = (Attr) nodes.item(i).getAttributes().getNamedItem("afisvalidate");
            if (afisValidateAttr != null) {
                attributeMap.put(nationalIdAttr.getValue(), afisValidateAttr.getValue());

            } else {
                Node node = nodes.item(i);
                Element element = (Element) node;

                NodeList errorElement = element.getElementsByTagName("Error");
                if (errorElement == null) {
                    throw new DataException(DataExceptionCode.IUR_004, DataExceptionCode.GLB_001_MSG, new String[]{"errorElement"});
                }

                NodeList codeElement = element.getElementsByTagName("Code");
                if (codeElement == null) {
                    throw new DataException(DataExceptionCode.IUR_007, DataExceptionCode.GLB_001_MSG, new String[]{"codeElement"});
                }

                String errorCode = codeElement.item(0).getTextContent();
                if (errorCode == null || errorCode.trim().isEmpty()) {
                    throw new DataException(DataExceptionCode.IUR_005, DataExceptionCode.GLB_001_MSG, new String[]{"ErrorCode"});
                }

                NodeList messageElement = element.getElementsByTagName("Message");
                String errorMessage = messageElement.item(0).getTextContent();
                if (errorMessage == null || errorMessage.trim().isEmpty()) {
                    throw new DataException(DataExceptionCode.IUR_006, DataExceptionCode.GLB_001_MSG, new String[]{"ErrorMessage"});
                }

                attributeMap.put(nationalIdAttr.getValue(), "EXCEPTION:" + errorCode + "-" + errorMessage);

            }
        }

        return attributeMap;
    }

    public HashMap<String, String> readXML(String xmlData) throws BaseException {
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

            String xpathQuery = "//@requestId";
            String requestId = (String) extractNode(doc, xpath, xpathQuery);
            if (requestId == null || requestId.isEmpty()) {
                throw new DataException(DataExceptionCode.IUR_001, DataExceptionCode.GLB_001_MSG, new String[]{"requestId"});
            }
            HashMap<String, String> returnMap = readCitizenInfoResult(doc, xpath);
            returnMap.put("REQUEST_ID", requestId);
            return returnMap;

        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.IUR_002, e.getMessage(), e);
        }
    }
    
    
 // Anbari fetch Images
 	public HashMap<String, String> readXMLNew(String xmlData)
 			throws BaseException {
 		try {
 			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
 					.newInstance();
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
 			
 			HashMap<String, String> returnMap = readCitizenInfoResultNew(doc,xpath);
 			return returnMap;

 		} catch (BaseException e) {
 			throw e;
 		} catch (Exception e) {
 			throw new DataException(DataExceptionCode.IUR_002, e.getMessage(),
 					e);
 		}
 	}
 	
 	private HashMap<String, String> readCitizenInfoResultNew(Document doc,
			XPath xpath) throws XPathExpressionException, BaseException {
		
		String xpathQuery = "//Citizen/FaceImages";
		HashMap<String, String> attributeMap = new HashMap<String, String>();
		XPathExpression expr = xpath.compile(xpathQuery);
		Object result = expr.evaluate(doc, XPathConstants.NODESET);

		NodeList nodes = (NodeList) result;
		for (int i = 0; i < nodes.getLength(); i++) {		
				Node node = nodes.item(i);
				Element element = (Element) node;				

				NodeList faceIMS = element.getElementsByTagName("FACE_IMS");
				if (faceIMS == null) {
					throw new DataException(DataExceptionCode.IUR_008,
							DataExceptionCode.GLB_010_MSG,
							new String[] { "fACE_IMS" });
				}

				NodeList faceCHIP = element.getElementsByTagName("FACE_CHIP");
				if (faceCHIP == null) {
					throw new DataException(DataExceptionCode.IUR_009,
							DataExceptionCode.GLB_010_MSG,
							new String[] { "FACE_CHIP" });
				}
				
				NodeList faceMLI = element.getElementsByTagName("FACE_MLI");
				if (faceMLI == null) {
					throw new DataException(DataExceptionCode.IUR_010,
							DataExceptionCode.GLB_010_MSG,
							new String[] { "FACE_MLI" });
				}
				
				NodeList faceLASER = element.getElementsByTagName("FACE_LASER");
				if (faceLASER == null) {
					throw new DataException(DataExceptionCode.IUR_011,
							DataExceptionCode.GLB_010_MSG,
							new String[] { "FACE_LASER" });
				}
				
				Node faceIMSNode = faceIMS.item(0);
				Node faceCHIPNode = faceCHIP.item(0);
				Node faceMLINode = faceMLI.item(0);
				Node faceLASERNode = faceLASER.item(0);
				

				if (faceIMSNode.getNodeName()== null || faceIMSNode.getFirstChild() == null) {
					throw new DataException(DataExceptionCode.IUR_012,
							DataExceptionCode.GLB_010_MSG,
							new String[] { "FACE_IMS Element" });
				}
				
				if (faceLASERNode.getNodeName()== null || faceLASERNode.getFirstChild() == null ) {
					throw new DataException(DataExceptionCode.IUR_013,
							DataExceptionCode.GLB_010_MSG,
							new String[] { "FACE_LASER Element" });
				}
				
				if (faceCHIPNode.getNodeName()== null || faceCHIPNode.getFirstChild() == null ) {
					throw new DataException(DataExceptionCode.IUR_014,
							DataExceptionCode.GLB_010_MSG,
							new String[] { "FACE_CHIP Element" });
				}
				
				if (faceMLINode.getNodeName() == null || faceMLINode.getFirstChild() == null) {
					throw new DataException(DataExceptionCode.IUR_015,
							DataExceptionCode.GLB_010_MSG,
							new String[] { "FACE_MLI Element" });
				}	
				
				String faceIMSNodeContent =  faceIMSNode.getFirstChild().getTextContent();
				String faceMLINodeContent = faceMLINode.getFirstChild().getTextContent();
				String faceCHIPNodeContent = faceCHIPNode.getFirstChild().getTextContent();
				String faceLASERNodeContent = faceLASERNode.getFirstChild().getTextContent();
				
				if (faceIMSNodeContent == null || faceIMSNodeContent.isEmpty()) {
					throw new DataException(DataExceptionCode.IUR_016,
							DataExceptionCode.GLB_010_MSG,
							new String[] { "FACE_IMS Content" });
				}
				
				
				if (faceMLINodeContent == null || faceMLINodeContent.isEmpty()) {
					throw new DataException(DataExceptionCode.IUR_017,
							DataExceptionCode.GLB_010_MSG,
							new String[] { "FACE_MLI Content" });
				}
				
				
				if (faceCHIPNodeContent == null || faceCHIPNodeContent.isEmpty()) {
					throw new DataException(DataExceptionCode.IUR_018,
							DataExceptionCode.GLB_010_MSG,
							new String[] { "FACE_CHIP Content" });
				}
				
				
				if (faceLASERNodeContent == null || faceLASERNodeContent.isEmpty()) {
					throw new DataException(DataExceptionCode.IUR_019,
							DataExceptionCode.GLB_010_MSG,
							new String[] { "FACE_LASER Content" });
				}				
				
				attributeMap.put(faceIMSNode.getNodeName(),faceIMSNodeContent);
				attributeMap.put(faceCHIPNode.getNodeName(),faceCHIPNodeContent);
				attributeMap.put(faceMLINode.getNodeName(),faceMLINodeContent);
				attributeMap.put(faceLASERNode.getNodeName(),faceLASERNodeContent);				

			}
		

		return attributeMap;
	}


    @Override
    public byte[] writeXML(ExtEntityTO to, Map<String, String> attributesMap) throws BaseException {
        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
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
