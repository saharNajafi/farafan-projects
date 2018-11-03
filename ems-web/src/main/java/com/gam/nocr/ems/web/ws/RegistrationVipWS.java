package com.gam.nocr.ems.web.ws;

import gampooya.tools.date.DateFormatException;
import gampooya.tools.date.DateUtil;
import gampooya.tools.util.Base64;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;

import org.slf4j.Logger;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.profile.ProfileException;
import com.gam.commons.profile.ProfileManager;
import com.gam.commons.security.SecurityUtil;
import com.gam.nocr.ems.biz.delegator.RegistrationDelegator;
import com.gam.nocr.ems.config.ConstValues;
import com.gam.nocr.ems.config.NOCRPKEParamProviderImpl;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.BiometricTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CertificateTO;
import com.gam.nocr.ems.data.domain.DocumentTO;
import com.gam.nocr.ems.data.domain.DocumentTypeTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.PhotoVipTO;
import com.gam.nocr.ems.data.domain.ws.BiometricWTO;
import com.gam.nocr.ems.data.domain.ws.ChildrenWTO;
import com.gam.nocr.ems.data.domain.ws.CitizenWTO;
import com.gam.nocr.ems.data.domain.ws.DocumentTypeWTO;
import com.gam.nocr.ems.data.domain.ws.DocumentWTO;
import com.gam.nocr.ems.data.domain.ws.SecurityContextWTO;
import com.gam.nocr.ems.data.domain.ws.SpouseWTO;
import com.gam.nocr.ems.data.enums.BiometricType;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.CertificateUsage;
import com.gam.nocr.ems.data.enums.Gender;
import com.gam.nocr.ems.data.mapper.tomapper.BiometricMapper;
import com.gam.nocr.ems.data.mapper.tomapper.CardRequestMapper;
import com.gam.nocr.ems.data.mapper.tomapper.DocumentMapper;
import com.gam.nocr.ems.data.mapper.tomapper.DocumentTypeMapper;
import com.gam.nocr.ems.util.EmsUtil;

@WebFault(faultBean = "com.gam.nocr.ems.web.ws.InternalException")
@WebService(serviceName = "RegistrationVipWS", portName = "RegistrationVipWSPort", targetNamespace = "http://ws.web.ems.nocr.gam.com/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Internal
public class RegistrationVipWS extends EMSWS {

	static final Logger logger = BaseLog.getLogger(RegistrationVipWS.class);
	private static final Logger vipLogger = BaseLog
			.getLogger("VipLogger");

	private RegistrationDelegator registrationDelegator = new RegistrationDelegator();

	@WebMethod
	public String getCredentials(SecurityContextWTO securityContextWTO)
			throws InternalException {
		super.validateRequest(securityContextWTO);
		CertificateTO certificateTO;
		try {
			certificateTO = registrationDelegator
					.getCertificateByUsage(CertificateUsage.CCOS_FINGER_CER_PUBLIC);
		} catch (BaseException e) {
			throw new InternalException(e.getMessage(), new EMSWebServiceFault(
					e.getExceptionCode()), e);
		} catch (Exception e) {
			throw new InternalException(WebExceptionCode.RVW_016,
					new EMSWebServiceFault(WebExceptionCode.RVW_016), e);
		}
		return convertToBase64(certificateTO.getCertificate());
	}

	@WebMethod
	public Boolean saveVipRegistrationRequest(
			@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
			@WebParam(name = "citizenWTO") CitizenWTO citizenWTO,
			@WebParam(name = "fingers") BiometricWTO[] fingersWTO,
			@WebParam(name = "faces") BiometricWTO[] facesWTO,
			@WebParam(name = "featureExtractorIdNormal") String featureExtractorIdNormal,
			@WebParam(name = "featureExtractorIdCC") String featureExtractorIdCC,
			@WebParam(name = "documents") DocumentWTO[] documentsWTO)
			throws InternalException {

		try {
			vipLogger.info("==================================================================================================");
			vipLogger.info("========================== transfer data from vip client to ems started ==========================");
			vipLogger.info("==================================================================================================");
			logger.info("==================================================================================================");
			logger.info("========================== transfer data from vip client to ems started ==========================");
			logger.info("==================================================================================================");
			UserProfileTO up = super.validateRequest(securityContextWTO);
			if (!EmsUtil.checkArraySize(fingersWTO))
				throw new InternalException(WebExceptionCode.RVW_018_MSG,
						new EMSWebServiceFault(WebExceptionCode.RVW_018));

			CardRequestTO cardRequestTO = convertToCardRequestTO(up, citizenWTO);
			ArrayList<BiometricTO> fingers = convertToFingersTO(fingersWTO);
			ArrayList<BiometricTO> faces = convertToFacesTO(facesWTO);
			ArrayList<DocumentTO> documents = convertToDocumentsTO(documentsWTO);
			RegistrationDelegator delegator = new RegistrationDelegator();
			/*delegator.registerVip(up, cardRequestTO, fingers, faces,
					documents);*/
			delegator.registerVip(up, cardRequestTO, fingers, faces,
					documents, featureExtractorIdNormal,featureExtractorIdCC);
			return true;
		} catch (BaseException e) {

			throw new InternalException(e.getMessage(), new EMSWebServiceFault(
					e.getExceptionCode()), e);
		} catch (Exception e) {
			if (e instanceof InternalException) {
				InternalException ex = (InternalException) e;
				String code = ex.getFaultInfo().getCode();
				String message = ex.getMessage();
					throw new InternalException(message,
							new EMSWebServiceFault(code));

			} else {
				throw new InternalException(WebExceptionCode.RVW_001_MSG,
						new EMSWebServiceFault(WebExceptionCode.RVW_001), e);
			}
		}
	}

	@WebMethod
	public DocumentTypeWTO[] getServiceVipDocuments(
			@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
			@WebParam(name = "serviceType") Integer serviceType)
			throws InternalException {

		UserProfileTO up = super.validateRequest(securityContextWTO);
		List<DocumentTypeTO> docTypes;
		try {
			docTypes = registrationDelegator.getServiceVipDocuments(up,
					serviceType);
		} catch (BaseException e) {
			throw new InternalException(e.getMessage(), new EMSWebServiceFault(
					e.getExceptionCode()), e);
		} catch (Exception e) {
			throw new InternalException(WebExceptionCode.RVW_002_MSG
					+ serviceType, new EMSWebServiceFault(
					WebExceptionCode.RVW_002), e);
		}
		DocumentTypeWTO[] wtos = new DocumentTypeWTO[docTypes.size()];
		for (int i = 0; i < docTypes.size(); i++) {
			try {
				wtos[i] = DocumentTypeMapper.convert(docTypes.get(i));
			} catch (BaseException e) {
				throw new InternalException(e.getMessage(),
						new EMSWebServiceFault(e.getExceptionCode()), e);
			} catch (Exception e) {
				throw new InternalException(WebExceptionCode.RVW_003_MSG
						+ docTypes.get(i).getId(), new EMSWebServiceFault(
						WebExceptionCode.RVW_003), e);
			}
		}
		return wtos;
	}

	private String convertToBase64(byte[] input) throws InternalException {
		String encoded;
		try {
			encoded = new String(Base64.encode(input), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new InternalException(WebExceptionCode.RVW_004_MSG,
					new EMSWebServiceFault(WebExceptionCode.RVW_004), e);
		}
		return encoded;
	}

	private CardRequestTO convertToCardRequestTO(UserProfileTO up,
			CitizenWTO citizenWTO) throws InternalException {
		CardRequestTO cardRequestTO;

		if (citizenWTO.getNationalId() == null
				|| citizenWTO.getGender() == null
				|| citizenWTO.getBirthDate() == null
				|| citizenWTO.getBirthDateHijri() == null
				|| citizenWTO.getReligionId() == null
				|| citizenWTO.getMotherFirstNameFA() == null
				|| citizenWTO.getMobile() == null
				) {

			throw new InternalException(WebExceptionCode.RVW_019_MSG,
					new EMSWebServiceFault(WebExceptionCode.RVW_019));

		}

		// Replacing some empty input values with their corresponding default
		// values in order to make the convert process
		// possible
		citizenWTO.setType(1L);
		initCitizenDefaultValues(citizenWTO);

		try {
			cardRequestTO = CardRequestMapper.convert(citizenWTO);
		} catch (BaseException e) {
			throw new InternalException(e.getMessage(), new EMSWebServiceFault(
					e.getExceptionCode()), e);
		} catch (Exception e) {
			throw new InternalException(WebExceptionCode.RVW_005_MSG,
					new EMSWebServiceFault(WebExceptionCode.RVW_005), e);
		}

		cardRequestTO.setEnrolledDate(new Date());
		cardRequestTO.setId(null);	
		return cardRequestTO;
	}

	private ArrayList<BiometricTO> convertToFingersTO(BiometricWTO[] fingersWTO)
			throws InternalException {
		ArrayList<BiometricTO> fingers = new ArrayList<BiometricTO>();
		if (fingersWTO != null) {
			BiometricTO bio;
			for (BiometricWTO biometricWTO : fingersWTO) {
				try {
					decryptFingerData(biometricWTO);
					bio = BiometricMapper.convert(biometricWTO);
				} catch (BaseException e) {
					throw new InternalException(e.getMessage(),
							new EMSWebServiceFault(e.getExceptionCode()), e);
				} catch (Exception e) {
					throw new InternalException(WebExceptionCode.RVW_007_MSG,
							new EMSWebServiceFault(WebExceptionCode.RVW_007));
				}

				if (bio.getType() == null) {
					throw new InternalException(WebExceptionCode.RVW_008_MSG,
							new EMSWebServiceFault(WebExceptionCode.RVW_008));
				}
				if (bio.getType() == BiometricType.FACE_CHIP
						|| bio.getType() == BiometricType.FACE_IMS
						|| bio.getType() == BiometricType.FACE_LASER
						|| bio.getType() == BiometricType.FACE_MLI) {
					throw new InternalException(WebExceptionCode.RVW_009_MSG,
							new EMSWebServiceFault(WebExceptionCode.RVW_009));
				}
				fingers.add(bio);
			}
		}
		return fingers;
	}

	private ArrayList<DocumentTO> convertToDocumentsTO(
			DocumentWTO[] documentsWTO) throws InternalException {
		ArrayList<DocumentTO> documents = new ArrayList<DocumentTO>();
		if (documentsWTO != null) {
			DocumentTO doc;
			for (DocumentWTO wto : documentsWTO) {
				try {
					doc = DocumentMapper.convert(wto);
				} catch (BaseException e) {
					throw new InternalException(e.getMessage(),
							new EMSWebServiceFault(e.getExceptionCode()), e);
				} catch (Exception e) {
					throw new InternalException(WebExceptionCode.RVW_013_MSG,
							new EMSWebServiceFault(WebExceptionCode.RVW_013), e);
				}
				documents.add(doc);
			}
		}
		return documents;
	}

	private void decryptFingerData(BiometricWTO biometricWTO)
			throws BaseException, ProfileException {

		CertificateTO certificateTO = registrationDelegator
				.getCertificateByUsage(CertificateUsage.CCOS_FINGER_CER_PRIVATE);
		biometricWTO.setSymmetricKey(SecurityUtil.asymmetricDecryption(
				certificateTO.getCertificate(), biometricWTO.getSymmetricKey(),
				certificateTO.getCode()));
		biometricWTO.setInitialVector(SecurityUtil.asymmetricDecryption(
				certificateTO.getCertificate(),
				biometricWTO.getInitialVector(), certificateTO.getCode()));

		ProfileManager profileManager = ProfileHelper.getProfileManager();
		NOCRPKEParamProviderImpl nocrpkeParamProviderImpl = new NOCRPKEParamProviderImpl(
				profileManager);
		nocrpkeParamProviderImpl
				.setSymmetricDecryptionTransformation("AES/CBC/X9.23PADDING");
		nocrpkeParamProviderImpl.setSymmetricDecSecretKeyAlgorithm("AES");
		biometricWTO.setData(SecurityUtil.symmetricDecryption(
				biometricWTO.getSymmetricKey(),
				biometricWTO.getInitialVector(), biometricWTO.getData(),
				nocrpkeParamProviderImpl));

	}

	private void initCitizenDefaultValues(CitizenWTO citizenWTO)
			throws InternalException {
		// Citizen
		citizenWTO.setFirstNameFA(ConstValues.DEFAULT_NAMES_FA);
		citizenWTO.setFirstNameEN(ConstValues.DEFAULT_NAMES_EN);
		citizenWTO.setSureNameFA(ConstValues.DEFAULT_NAMES_FA);
		citizenWTO.setSureNameEN(ConstValues.DEFAULT_NAMES_EN);
		citizenWTO.setBirthCertId(ConstValues.DEFAULT_NUMBER);
		citizenWTO.setBirthCertSerial(ConstValues.DEFAULT_CERT_SERIAL);

		// Father
		citizenWTO.setFatherFirstNameEN(ConstValues.DEFAULT_NAMES_EN);
		citizenWTO.setFatherFatherName(ConstValues.DEFAULT_NAMES_FA);
		citizenWTO.setFatherSureName(ConstValues.DEFAULT_NAMES_FA);
		citizenWTO.setFatherBirthCertSeries(ConstValues.DEFAULT_CERT_SERIAL);

		try {
			citizenWTO.setFatherBirthDate(new Timestamp(DateUtil.convert(
					ConstValues.DEFAULT_DATE, DateUtil.JALALI).getTime()));
		} catch (DateFormatException e) {
			throw new InternalException(WebExceptionCode.RVW_014_MSG,
					new EMSWebServiceFault(WebExceptionCode.RVW_014), e);
		}

		if (!EmsUtil.checkString(citizenWTO.getFatherFirstNameFA())) {
			citizenWTO.setFatherFirstNameFA(ConstValues.DEFAULT_NAMES_FA);
		}
		if (!EmsUtil.checkString(citizenWTO.getFatherNationalId())) {
			citizenWTO.setFatherNationalId(ConstValues.DEFAULT_NID);
		}
		if (!EmsUtil.checkString(citizenWTO.getFatherBirthCertId())) {
			citizenWTO.setFatherBirthCertId(ConstValues.DEFAULT_NUMBER);
		}

		// Mother
		citizenWTO.setMotherSureName(ConstValues.DEFAULT_NAMES_FA);
		citizenWTO.setMotherBirthCertSeries(ConstValues.DEFAULT_CERT_SERIAL);

		if (!EmsUtil.checkString(citizenWTO.getMotherFirstNameFA())) {
			citizenWTO.setMotherFirstNameFA(ConstValues.DEFAULT_NAMES_FA);
		}
		if (!EmsUtil.checkString(citizenWTO.getMotherNationalId())) {
			citizenWTO.setMotherNationalId(ConstValues.DEFAULT_NID);
		}
		if (!EmsUtil.checkString(citizenWTO.getMotherBirthCertId())) {
			citizenWTO.setMotherBirthCertId(ConstValues.DEFAULT_NUMBER);
		}

		try {
			citizenWTO.setMotherBirthDate(new Timestamp(DateUtil.convert(
					ConstValues.DEFAULT_DATE, DateUtil.JALALI).getTime()));
		} catch (DateFormatException e) {
			throw new InternalException(WebExceptionCode.RVW_015_MSG,
					new EMSWebServiceFault(WebExceptionCode.RVW_015), e);
		}

		// Spouses
		SpouseWTO[] spouses = citizenWTO.getSpouses();
		if (EmsUtil.checkArraySize(spouses)) {
			for (SpouseWTO spouse : spouses) {
				spouse.setFirstNameFA(ConstValues.DEFAULT_NAMES_FA);
				spouse.setSureNameFA(ConstValues.DEFAULT_NAMES_FA);
				spouse.setFatherName(ConstValues.DEFAULT_NAMES_FA);
				spouse.setBirthCerId(ConstValues.DEFAULT_NUMBER);
				spouse.setBirthCertSeries(ConstValues.DEFAULT_CERT_SERIAL);
			}
		}

		// Children
		ChildrenWTO[] children = citizenWTO.getChildren();
		if (EmsUtil.checkArraySize(children)) {
			for (ChildrenWTO child : children) {
				child.setFistNameFA(ConstValues.DEFAULT_NAMES_FA);
				child.setFatherName(ConstValues.DEFAULT_NAMES_FA);
				child.setBirthCertId(ConstValues.DEFAULT_NUMBER);
				child.setBirthCertSeries(ConstValues.DEFAULT_CERT_SERIAL);
				child.setGender(Gender.UNDEFINED.toString());
			}
		}
	}

	@WebMethod
	public String testConnection() {
		return "helloHide";
	}
	private ArrayList<BiometricTO> convertToFacesTO(BiometricWTO[] facesWTOs) throws InternalException {
        ArrayList<BiometricTO> faces = new ArrayList<BiometricTO>();
        if(EmsUtil.checkArraySize(facesWTOs))
        {
        if(facesWTOs.length !=5)
        	throw new InternalException(WebExceptionCode.RVW_021_MSG, new EMSWebServiceFault(WebExceptionCode.RVW_021));      
            BiometricTO bio;
            for (BiometricWTO biometricWTO : facesWTOs) {
                try {
                    bio = BiometricMapper.convert(biometricWTO);
                } catch (BaseException e) {
                    throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
                } catch (Exception e) {
                    throw new InternalException(WebExceptionCode.RSW_070_MSG, new EMSWebServiceFault(WebExceptionCode.RVW_022), e);
                }
                if (bio.getType() == null) {
                    throw new InternalException(WebExceptionCode.RSW_071_MSG, new EMSWebServiceFault(WebExceptionCode.RVW_023));
                }
                faces.add(bio);
            }
	}
        return faces;
    }

}
