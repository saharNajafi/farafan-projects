package com.gam.nocr.ems.data.mapper.xmlmapper;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.domain.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calls the right writeXML and readXML corresponding to the type of the
 * ExtEntityTO passed to it.
 *
 * @author Sina Golesorkhi
 */
public class XMLMapperProvider implements XMLMapper {

	public byte[] writeXML(ExtEntityTO to, Map<String, String> attributesMap) throws BaseException {
		byte[] result = null;
		if (to instanceof CardRequestTO) {
			CardRequestCMSMapper cardRequestCMSMapper = new CardRequestCMSMapper();
			result = cardRequestCMSMapper.writeXML(to, attributesMap);

		} else if (to instanceof PersonTO) {
			PersonPKIMapper personPKIMapper = new PersonPKIMapper();
			result = personPKIMapper.writeXML(to, attributesMap);

		} else if (to instanceof EnrollmentOfficeTO) {
			EnrollmentPKIMapper enrollmentPKIMapper = new EnrollmentPKIMapper();
			result = enrollmentPKIMapper.writeXML(to, attributesMap);

		} else if (to instanceof TokenTO) {
			TokenPKIMapper tokenPKIMapper = new TokenPKIMapper();
			result = tokenPKIMapper.writeXML(to, attributesMap);

		} else if (to instanceof CitizenTO) {
			IMSUpdateCitizenInfoMapper imsUpdateCitizenInfoMapper = new IMSUpdateCitizenInfoMapper();
			result = imsUpdateCitizenInfoMapper.writeXML(to, attributesMap);

		} else if (to instanceof IMSUpdateTO) {
			GamIMSMapper gamIMSMapper = new GamIMSMapper();
			result = gamIMSMapper.writeXML(to, attributesMap);
		}

		return result;
	}

	//	TODO : Moving this method to the interface 'XMLMapper' on future
	public byte[] writeXML(List<CardRequestTO> toList, Map<String, String> attributesMap) throws BaseException {
		byte[] result;
		IMSUpdateCitizenInfoMapper imsUpdateCitizenInfoMapper = new IMSUpdateCitizenInfoMapper();
		result = imsUpdateCitizenInfoMapper.writeXML(toList, attributesMap);
		return result;
	}

	public ExtEntityTO readXML(String xmlData, ExtEntityTO to) throws BaseException {
		ExtEntityTO retrievedFromXMLTO = null;

//		if (to instanceof CardRequestTO) {
//			CardRequestCMSMapper cardRequestCMSMapper = new CardRequestCMSMapper();
//			retrievedFromXMLTO = (ExtEntityTO) cardRequestCMSMapper.readXML(xmlData,to);
//		}
//		if (to instanceof PersonTO) {
//			PersonPKIMapper personPKIMapper = new PersonPKIMapper();
//			retrievedFromXMLTO = (ExtEntityTO) personPKIMapper.readXML(xmlData,to);
//		}
//		if (to instanceof EnrollmentOfficeTO) {
//			EnrollmentPKIMapper enrollmentPKIMapper = new EnrollmentPKIMapper();
//			retrievedFromXMLTO = (ExtEntityTO) enrollmentPKIMapper.readXML(xmlData,to);
//		}

		//TODO revocationResponse
		if (to instanceof TokenTO) {
			TokenPKIMapper tokenPKIMapper = new TokenPKIMapper();
			retrievedFromXMLTO = (ExtEntityTO) tokenPKIMapper.readXML(xmlData, to);

		} else if (to instanceof CitizenTO) {
			IMSUpdateCitizenInfoMapper imsFetchUpdateCitizenInfoMapper = new IMSUpdateCitizenInfoMapper();
			retrievedFromXMLTO = imsFetchUpdateCitizenInfoMapper.readXML(xmlData, to);

		} else if (to instanceof IMSUpdateTO) {
			GamIMSMapper gamIMSMapper = new GamIMSMapper();
			retrievedFromXMLTO = gamIMSMapper.readXML(xmlData, to);
		}


		return retrievedFromXMLTO;
	}

	public HashMap<String, String> readXML(String xmlData) throws BaseException {
		IMSUpdateResultMapper imsUpdateResultMapper = new IMSUpdateResultMapper();
		return imsUpdateResultMapper.readXML(xmlData);
	}
	
	// Anbari fetch image and requestId
	public HashMap<String, String> readXMLNew(String xmlData)
			throws BaseException {
		IMSUpdateResultMapper imsUpdateResultMapper = new IMSUpdateResultMapper();
		return imsUpdateResultMapper.readXMLNew(xmlData);
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
		ExtEntityTO retrievedFromXMLTO = null;

		if (to instanceof CitizenTO) {
			IMSUpdateCitizenInfoMapper imsFetchUpdateCitizenInfoMapper = new IMSUpdateCitizenInfoMapper();
			retrievedFromXMLTO = imsFetchUpdateCitizenInfoMapper.readXML(xmlData, to);
		}

		return retrievedFromXMLTO;
	}
}
