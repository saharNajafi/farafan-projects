package com.gam.nocr.ems.data.mapper.xmlmapper;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.IMSUpdateTO;
import com.gam.nocr.ems.data.domain.vol.IMSUpdateResultVTO;
import com.gam.nocr.ems.data.enums.AFISState;
import com.gam.nocr.ems.util.EmsUtil;
import org.slf4j.Logger;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.Random;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class GamIMSMapper implements XMLMapper {

	private static final Logger logger = BaseLog.getLogger(GamIMSMapper.class);

	/**
	 * @param doc
	 * @param xpath
	 * @param xpathQuery
	 * @throws javax.xml.xpath.XPathExpressionException
	 *
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

	private String fetchNationalIds(Document doc,
									XPath xpath) throws XPathExpressionException {
		/**
		 * NationalIds
		 */
		String xpathQuery = "//UpdateRequest/Citizen/IdentityData";
		String nationalId = null;
		XPathExpression expr = xpath.compile(xpathQuery);
		NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			Element element = (Element) node;
			NodeList nationalIdElement = element.getElementsByTagName("NID");
			nationalId = nationalIdElement.item(0).getTextContent();
		}
		return nationalId;
	}

	@Override
	public byte[] writeXML(ExtEntityTO to,
						   Map<String, String> attributesMap) throws BaseException {
		IMSUpdateTO imsUpdateTO = (IMSUpdateTO) to;
		String[] nationalIdArray = null;
		try {
			nationalIdArray = imsUpdateTO.getNationalId().split(",");
		} catch (Exception e) {
            logger.error(e.getMessage(), e);
			nationalIdArray = new String[]{imsUpdateTO.getNationalId()};
		}
		String[] tempAttrArray = imsUpdateTO.getData().split(",");
		IMSUpdateResultVTO imsUpdateResultVTO = new IMSUpdateResultVTO();
		imsUpdateResultVTO.setNationalId(imsUpdateTO.getNationalId());
		imsUpdateResultVTO.setRequestID(tempAttrArray[0]);

		if (String.valueOf(AFISState.VALID).equals(tempAttrArray[1])) {
			imsUpdateResultVTO.setAfisState(AFISState.VALID);
		} else if (String.valueOf(AFISState.NOT_VALID).equals(tempAttrArray[1])) {
			imsUpdateResultVTO.setAfisState(AFISState.NOT_VALID);
		} else if (String.valueOf(AFISState.VIP).equals(tempAttrArray[1])) {
			imsUpdateResultVTO.setAfisState(AFISState.VIP);
		}

		imsUpdateResultVTO.setIdentityChanged(Integer.valueOf(tempAttrArray[2]));
		imsUpdateResultVTO.setErrorCode(tempAttrArray[3]);
		imsUpdateResultVTO.setErrorMessage(tempAttrArray[4]);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("CitInfoResult");
			doc.appendChild(rootElement);

			/**
			 * 	set identifierAttr for the root element
			 */
			Attr identifierAttr = doc.createAttribute("requestId");
			identifierAttr.setValue(imsUpdateTO.getRequestId());
			rootElement.setAttributeNode(identifierAttr);

			/**
			 * Citizens Element
			 */
//		TODO: The exception scenario has not been implemented till now
			for (String nationalId : nationalIdArray) {
				Element citizenElement = doc.createElement("Citizen");
				rootElement.appendChild(citizenElement);

				Attr nidAttr = doc.createAttribute("nid");
				nidAttr.setValue(nationalId);
				citizenElement.setAttributeNode(nidAttr);

				Attr afisValidateAttr = doc.createAttribute("afisvalidate");
				afisValidateAttr.setValue(imsUpdateResultVTO.getAfisState().name());
				citizenElement.setAttributeNode(afisValidateAttr);
			}

			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);

			logger.info("\n\nGAM IMS UPDATE CITIZEN INFO RESULT XML================\n");
			logger.info(writer.toString());
			logger.info("\n==========================================================\n\n");

			return writer.toString().getBytes("UTF-8");

		} catch (Exception e) {
            logger.error(DataExceptionCode.GLB_ERR_MSG, e);
		}
		return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public ExtEntityTO readXML(String xmlData,
							   ExtEntityTO to) throws BaseException {
		IMSUpdateTO imsUpdateTO = null;
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
			if (EmsUtil.checkString(requestId)) {
				IMSUpdateResultVTO imsUpdateResultVTO = new IMSUpdateResultVTO();
				imsUpdateResultVTO.setRequestID(requestId);
				String nationalId = fetchNationalIds(doc, xpath);
				/**
				 * Set Afis state by using random
				 */
				Random rn = new Random(System.nanoTime());
				int numberOfStates = 1;
				int i = Math.abs(rn.nextInt() % numberOfStates);
				int randomNum = i + 1;
				switch (randomNum) {
					case 1:
						imsUpdateResultVTO.setAfisState(AFISState.VALID);
						break;
					case 2:
						imsUpdateResultVTO.setAfisState(AFISState.NOT_VALID);
						break;
					case 3:
						imsUpdateResultVTO.setAfisState(AFISState.VIP);
						break;
				}

				imsUpdateResultVTO.setIdentityChanged(0000);
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(imsUpdateResultVTO.getRequestID());
				stringBuilder.append(",");
				stringBuilder.append(imsUpdateResultVTO.getAfisState());
				stringBuilder.append(",");
				stringBuilder.append(imsUpdateResultVTO.getIdentityChanged());
				stringBuilder.append(",");
				stringBuilder.append(imsUpdateResultVTO.getErrorCode());
				stringBuilder.append(",");
				stringBuilder.append(imsUpdateResultVTO.getErrorMessage());

				imsUpdateTO = new IMSUpdateTO(
						requestId,
						stringBuilder.toString(),
						xmlDATA,
						IMSUpdateTO.State.PENDING_TO_PROCESS,
						nationalId);
			}
		} catch (Exception e) {
            logger.error(DataExceptionCode.GLB_ERR_MSG, e);
		}

		return imsUpdateTO;
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
	public ExtEntityTO readXML(String xmlData, ExtEntityTO to, Map<String, String> attributesMap) throws BaseException {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
