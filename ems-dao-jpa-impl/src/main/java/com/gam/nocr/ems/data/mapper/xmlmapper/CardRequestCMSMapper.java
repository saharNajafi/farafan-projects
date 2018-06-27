/**
 *
 */
package com.gam.nocr.ems.data.mapper.xmlmapper;

import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_CARD_REQUEST;
import static com.gam.nocr.ems.config.EMSLogicalNames.getDaoJNDIName;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.ConstValues;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.CardRequestDAO;
import com.gam.nocr.ems.data.dao.EnrollmentOfficeDAO;
import com.gam.nocr.ems.data.domain.BiometricTO;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CitizenInfoTO;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.enums.AFISState;
import com.gam.nocr.ems.data.enums.BiometricType;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeDeliverStatus;
import com.gam.nocr.ems.data.enums.Gender;
import gampooya.tools.date.DateUtil;
import gampooya.tools.util.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author Sina Golesorkhi
 */
public class CardRequestCMSMapper implements XMLMapper {

	private static final Logger logger = BaseLog
			.getLogger(CardRequestCMSMapper.class);

	// ====================================================================================================================
	/**
	 * Refactored by Saeed
	 */
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat marriageDateFormatter = new SimpleDateFormat(
			"yyyy/MM/dd");

	/**
	 * The method prepareMOCIndex is used to prepare MOC index
	 */
	private String prepareMOCIndex(String fingerIndex) {
		String mocIndex = null;
		switch (fingerIndex.charAt(0)) {
		case '1':
			mocIndex = "05";
			break;

		case '2':
			mocIndex = "09";
			break;

		case '3':
			mocIndex = "0D";
			break;

		case '4':
			mocIndex = "11";
			break;

		case '5':
			mocIndex = "15";
			break;

		case '6':
			mocIndex = "06";
			break;

		case '7':
			mocIndex = "0A";
			break;

		case '8':
			mocIndex = "0E";
			break;

		case '9':
			mocIndex = "12";
			break;
		}

		if (fingerIndex.equals("10")) {
			mocIndex = "16";
		}
		return mocIndex;
	}

	// TODO : Move this method to util package
	private String convertToBase64(byte[] input) {
		String encoded = null;
		try {
			encoded = new String(Base64.encode(input), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(DataExceptionCode.GLB_ERR_MSG, e);
		}
		return encoded;
	}

	/*
	 * The method getDashDateFormatter is used to convert the format of database
	 * date by using the format "yyyy-MM-dd"
	 */
	public SimpleDateFormat getDashDateFormatter() {
		return dateFormatter;
	}

	/*
	 * The method getSlashDateFormatter is used to convert the format of
	 * database date by using the format "yyyy/MM/dd"
	 */
	public SimpleDateFormat getSlashDateFormatter() {
		return marriageDateFormatter;
	}

	// ====================================================================================================================

	@Override
	public byte[] writeXML(ExtEntityTO to, Map<String, String> attributesMap)
			throws BaseException {

		CardRequestTO cardRequestTO = (CardRequestTO) to;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Request");
			doc.appendChild(rootElement);

			// // set attribute to Request element
			Attr typeAttr = doc.createAttribute("type");
			typeAttr.setValue("issuance");
			rootElement.setAttributeNode(typeAttr);

			if (attributesMap != null && attributesMap.get("id") != null) {
				Attr identifierAttr = doc.createAttribute("identifier");
				identifierAttr.setValue(attributesMap.get("id"));
				rootElement.setAttributeNode(identifierAttr);
			} else {
				Object[] args = { "identifier" };
				throw new DataException(DataExceptionCode.CRC_001,
						DataExceptionCode.GLB_001_MSG, args);
			}

			// CitizenInfo
			CitizenTO citizenTO = cardRequestTO.getCitizen();
			CitizenInfoTO citizenInfoTO = null;
			if (citizenTO != null) {
				citizenInfoTO = citizenTO.getCitizenInfo();
			}

			int mocCount = 0;
			boolean mohFlag = false;
			byte[] fingerMinOne = null;
			String fingerMinOneMetaDate = null;
			byte[] fingerMinTwo = null;
			String fingerMinTwoMetaDate = null;
			byte[] fingerCandidate = null;
			String fingerCandidateMetaDate = null;
			for (BiometricTO biometricTO : citizenInfoTO.getBiometrics()) {
				BiometricType biometricType = biometricTO.getType();
				if (BiometricType.FING_MIN_1.equals(biometricType)) {
					fingerMinOne = biometricTO.getData();
					fingerMinOneMetaDate = biometricTO.getMetaData();
					mocCount += 1;

				} else if (BiometricType.FING_MIN_2.equals(biometricType)) {
					fingerMinTwo = biometricTO.getData();
					fingerMinTwoMetaDate = biometricTO.getMetaData();
					mocCount += 1;

				} else if (BiometricType.FING_CANDIDATE.equals(biometricType)) {
					fingerCandidate = biometricTO.getData();
					fingerCandidateMetaDate = biometricTO.getMetaData();
					mohFlag = true;
				}
			}

			Attr mocAttr = doc.createAttribute("MOC");
			if (mocCount > 0) {
				mocAttr.setValue("true");
			} else {
				mocAttr.setValue("false");
			}
			rootElement.setAttributeNode(mocAttr);

			Attr mohAttr = doc.createAttribute("MOH");
			if (mohFlag) {
				mohAttr.setValue("true");
			} else {
				mohAttr.setValue("false");
			}
			rootElement.setAttributeNode(mohAttr);

			// EnrollmentInfo elements
			Element enrollmentInfo = doc.createElement("EnrollmentInfo");
			rootElement.appendChild(enrollmentInfo);

			Element enrollDate = doc.createElement("EnrollDate");
			if (cardRequestTO.getEnrolledDate() != null) {

				// Refactored by Saeed
				enrollDate.appendChild(doc.createTextNode(String
						.valueOf(getDashDateFormatter().format(
								cardRequestTO.getEnrolledDate()))));
			}
			enrollmentInfo.appendChild(enrollDate);

			Element userSiteID = doc.createElement("UserSiteID");
			//Anbari
			if (cardRequestTO.getEnrollmentOffice() != null) {
				//load lazy
				EnrollmentOfficeTO dbEnrollmentOffice = getEnrollmentOfficeDAO().loadLazyChildren(cardRequestTO.getEnrollmentOffice());
				if (dbEnrollmentOffice.getSuperiorOffice() == null) {
					userSiteID.appendChild(doc.createTextNode(String
							.valueOf(dbEnrollmentOffice
									.getId())));
				}else
				{
					if(EnrollmentOfficeDeliverStatus.ENABLED.equals(dbEnrollmentOffice.getDeliver()))
						userSiteID.appendChild(doc.createTextNode(String
								.valueOf(dbEnrollmentOffice.getId())));
					else
						userSiteID.appendChild(doc.createTextNode(String
							.valueOf(dbEnrollmentOffice.getSuperiorOffice()
									.getId())));
					
				}
			}
			enrollmentInfo.appendChild(userSiteID);

			Element citizenInfoElement = doc.createElement("CitizenInfo");
			rootElement.appendChild(citizenInfoElement);

			Element identification = doc.createElement("Identification");
			citizenInfoElement.appendChild(identification);

			Element firstNamePersian = doc.createElement("FirstNamePersian");
			if (citizenTO != null && citizenTO.getFirstNamePersian() != null) {
				firstNamePersian.appendChild(doc.createTextNode(citizenTO
						.getFirstNamePersian()));
			}
			identification.appendChild(firstNamePersian);

			Element firstNameEnglish = doc.createElement("FirstNameEnglish");
			if (citizenInfoTO != null
					&& citizenInfoTO.getFirstNameEnglish() != null) {
				firstNameEnglish.appendChild(doc.createTextNode(citizenInfoTO
						.getFirstNameEnglish()));
			}
			identification.appendChild(firstNameEnglish);

			Element surnamePersian = doc.createElement("SurnamePersian");
			if (citizenTO != null && citizenTO.getSurnamePersian() != null) {
				surnamePersian.appendChild(doc.createTextNode(citizenTO
						.getSurnamePersian()));
			}
			identification.appendChild(surnamePersian);

			Element surnameEnglish = doc.createElement("SurnameEnglish");
			if (citizenInfoTO != null
					&& citizenInfoTO.getSurnameEnglish() != null) {
				surnameEnglish.appendChild(doc.createTextNode(citizenInfoTO
						.getSurnameEnglish()));
			}
			identification.appendChild(surnameEnglish);

			Element birthDateJalali = doc.createElement("BirthDateJalali");
			if (citizenInfoTO != null
					&& citizenInfoTO.getBirthDateSolar() != null) {
				birthDateJalali.appendChild(doc.createTextNode(citizenInfoTO
						.getBirthDateSolar()));
			}
			identification.appendChild(birthDateJalali);

			Element birthDateHijri = doc.createElement("BirthDateHijri");
			if (citizenInfoTO != null
					&& citizenInfoTO.getBirthDateLunar() != null) {
				birthDateHijri.appendChild(doc.createTextNode(citizenInfoTO
						.getBirthDateLunar()));
			}
			identification.appendChild(birthDateHijri);

			Element birthDateGregorian = doc
					.createElement("BirthDateGregorian");
			if (citizenInfoTO != null
					&& citizenInfoTO.getBirthDateGregorian() != null) {
				birthDateGregorian.appendChild(doc.createTextNode(String
						.valueOf(getSlashDateFormatter().format(
								citizenInfoTO.getBirthDateGregorian()))));
			}
			identification.appendChild(birthDateGregorian);

			Element fatherFirstNamePersian = doc
					.createElement("FatherFirstNamePersian");
			if (citizenInfoTO != null
					&& citizenInfoTO.getFatherFirstNamePersian() != null) {
				fatherFirstNamePersian.appendChild(doc
						.createTextNode(citizenInfoTO
								.getFatherFirstNamePersian()));
			}
			identification.appendChild(fatherFirstNamePersian);

			Element fatherFirstNameEnglish = doc
					.createElement("FatherFirstNameEnglish");
			/**
			 * Modified to set default value for this field
			 */
			// if (citizenInfoTO != null &&
			// citizenInfoTO.getFatherFirstNameEnglish() != null) {
			// fatherFirstNameEnglish.appendChild(doc.createTextNode(citizenInfoTO.getFatherFirstNameEnglish()));
			// }
			fatherFirstNameEnglish.appendChild(doc
					.createTextNode(ConstValues.DEFAULT_NAMES_EN));
			identification.appendChild(fatherFirstNameEnglish);

			Element fatherBirthDateJalali = doc
					.createElement("FatherBirthDateJalali");
			/**
			 * Modified to set default value for this field
			 */
			// if (citizenInfoTO != null &&
			// citizenInfoTO.getFatherBirthDateSolar() != null) {
			// fatherBirthDateJalali.appendChild(doc.createTextNode(DateUtil.convert(citizenInfoTO.getFatherBirthDateSolar(),
			// DateUtil.JALALI)));
			// }
			Date fatherBirthDate = DateUtil.convert(ConstValues.DEFAULT_DATE,
					DateUtil.JALALI);
			fatherBirthDateJalali.appendChild(doc.createTextNode(DateUtil
					.convert(fatherBirthDate, DateUtil.JALALI)));
			identification.appendChild(fatherBirthDateJalali);

			Element fatherNationalID = doc.createElement("FatherNID");
			/**
			 * Modified to set default value for this field
			 */
			// if (citizenInfoTO != null && citizenInfoTO.getFatherNationalID()
			// != null) {
			// fatherNationalID.appendChild(doc.createTextNode(citizenInfoTO.getFatherNationalID()));
			// }
			fatherNationalID.appendChild(doc
					.createTextNode(ConstValues.DEFAULT_NID));
			identification.appendChild(fatherNationalID);

			Element nationalID = doc.createElement("NationalID");
			if (citizenTO != null && citizenTO.getNationalID() != null) {
				nationalID.appendChild(doc.createTextNode(citizenTO
						.getNationalID()));
			}
			identification.appendChild(nationalID);

			Element reduplicate = doc.createElement("Reduplicate");
			if (citizenTO != null) {
				if (citizenTO.getReduplicate() == null) {
					reduplicate.appendChild(doc.createTextNode("1"));
				} else {
					reduplicate.appendChild(doc.createTextNode(String
							.valueOf(citizenTO.getReduplicate())));
				}
			}
			identification.appendChild(reduplicate);

			Element sex = doc.createElement("Sex");
			if (citizenInfoTO != null && citizenInfoTO.getGender() != null) {
				sex.appendChild(doc.createTextNode(Gender
						.convertToString(citizenInfoTO.getGender())));
			}
			identification.appendChild(sex);

			Element birthCertificateIssuancePlacePersian = doc
					.createElement("BirthCertificateIssuancePlacePersian");
			if (citizenInfoTO != null
					&& citizenInfoTO.getBirthCertificateIssuancePlace() != null) {
				birthCertificateIssuancePlacePersian.appendChild(doc
						.createTextNode(citizenInfoTO
								.getBirthCertificateIssuancePlace()));
			}
			identification.appendChild(birthCertificateIssuancePlacePersian);

			Element motherFirstNamePersian = doc
					.createElement("MotherFirstNamePersian");
			/**
			 * Modified to set default value for this field
			 */
			// if (citizenInfoTO != null &&
			// citizenInfoTO.getMotherFirstNamePersian() != null) {
			// motherFirstNamePersian.appendChild(doc.createTextNode(citizenInfoTO
			// .getMotherFirstNamePersian()));
			// }
			motherFirstNamePersian.appendChild(doc
					.createTextNode(ConstValues.DEFAULT_NAMES_FA));
			identification.appendChild(motherFirstNamePersian);

			Element motherBirthDateJalali = doc
					.createElement("MotherBirthDateJalali");
			/**
			 * Modified to set default value for this field
			 */
			// if (citizenInfoTO != null &&
			// citizenInfoTO.getMotherBirthDateSolar() != null) {
			// motherBirthDateJalali.appendChild(doc.createTextNode(DateUtil.convert(citizenInfoTO.getMotherBirthDateSolar(),
			// DateUtil.JALALI)));
			// }
			Date motherBirthDate = DateUtil.convert(ConstValues.DEFAULT_DATE,
					DateUtil.JALALI);
			motherBirthDateJalali.appendChild(doc.createTextNode(DateUtil
					.convert(motherBirthDate, DateUtil.JALALI)));
			identification.appendChild(motherBirthDateJalali);

			Element motherNationalID = doc.createElement("MotherNID");
			/**
			 * Modified to set default value for this field
			 */
			// if (citizenInfoTO != null && citizenInfoTO.getMotherNationalID()
			// != null) {
			// motherNationalID.appendChild(doc.createTextNode(citizenInfoTO.getMotherNationalID()));
			// }
			motherNationalID.appendChild(doc
					.createTextNode(ConstValues.DEFAULT_NID));
			identification.appendChild(motherNationalID);

			Element identityChange = doc.createElement("IdentityChange");
			// TODO : Changing it into hex binary on future
			if (citizenInfoTO != null) {
				String strIdentityChange = citizenInfoTO.getIdentityChanged()
						.toString();
				strIdentityChange = StringUtils.leftPad(strIdentityChange, 4,
						"0");

				identityChange.appendChild(doc
						.createTextNode(strIdentityChange));
			}
			identification.appendChild(identityChange);

			Element email = doc.createElement("Email");
			email.appendChild(doc.createTextNode("ir"
					+ citizenTO.getNationalID() + "@iran.ir"));
			identification.appendChild(email);

			Element religion = doc.createElement("Religion");
			if (citizenInfoTO != null && citizenInfoTO.getReligion() != null
					&& citizenInfoTO.getReligion().getName() != null) {
				religion.appendChild(doc.createTextNode(citizenInfoTO
						.getReligion().getName()));
			}
			identification.appendChild(religion);

			Element afisValidate = doc.createElement("AFISValidate");
			afisValidate.appendChild(doc.createTextNode(AFISState
					.convertToString(citizenInfoTO.getAfisState())));
			identification.appendChild(afisValidate);

			Element moCFingersCount = doc.createElement("MoCFingersCount");
			moCFingersCount.appendChild(doc.createTextNode(String
					.valueOf(mocCount)));
			identification.appendChild(moCFingersCount);

			Element address = doc.createElement("Address");
			citizenInfoElement.appendChild(address);

			Element postalCode = doc.createElement("PostalCode");
			if (citizenInfoTO != null && citizenInfoTO.getPostcode() != null) {
				postalCode.appendChild(doc.createTextNode(citizenInfoTO
						.getPostcode()));
			} else if (citizenInfoTO.getPostcode() == null) {// TODO the else
				// part is dummy
				postalCode.appendChild(doc.createTextNode("0000000000"));
			}
			{

			}
			address.appendChild(postalCode);

			Element family = doc.createElement("Family");
			citizenInfoElement.appendChild(family);

			Element currentSpousesCount = doc
					.createElement("CurrentSpousesCount");
			/**
			 * Modified to omit spouse from the xml request
			 */
			// if (attributesMap != null &&
			// attributesMap.get("currentSpousesCount") != null) {
			// currentSpousesCount.appendChild(doc.createTextNode(String.valueOf(attributesMap.get("currentSpousesCount"))));
			// } else {
			// currentSpousesCount.appendChild(doc.createTextNode("0"));
			// }
			currentSpousesCount.appendChild(doc.createTextNode("0"));
			family.appendChild(currentSpousesCount);

			Element currentChildrenCount = doc
					.createElement("CurrentChildrenCount");
			/**
			 * Modified to omit child from the xml request
			 */
			// if (attributesMap != null &&
			// attributesMap.get("currentChildrenCount") != null) {
			// currentChildrenCount.appendChild(doc.createTextNode(String.valueOf(attributesMap.get("currentChildrenCount"))));
			// } else {
			// currentSpousesCount.appendChild(doc.createTextNode("0"));
			// }
			currentChildrenCount.appendChild(doc.createTextNode("0"));
			family.appendChild(currentChildrenCount);

			Element totalSpousesCount = doc.createElement("TotalSpousesCount");
			/**
			 * Modified to omit spouse from the xml request
			 */
			// if (citizenInfoTO != null && citizenInfoTO.getSpouses() != null)
			// {
			// totalSpousesCount.appendChild(doc.createTextNode(String.valueOf(citizenInfoTO.getSpouses().size())));
			// } else {
			// totalSpousesCount.appendChild(doc.createTextNode("0"));
			// }
			totalSpousesCount.appendChild(doc.createTextNode("0"));
			family.appendChild(totalSpousesCount);

			Element totalChildrenCount = doc
					.createElement("TotalChildrenCount");
			/**
			 * Modified to omit child from the xml request
			 */
			// if (citizenInfoTO != null && citizenInfoTO.getChildren() != null)
			// {
			// totalChildrenCount.appendChild(doc.createTextNode(String.valueOf(citizenInfoTO.getChildren().size())));
			// } else {
			// totalChildrenCount.appendChild(doc.createTextNode("0"));
			// }
			totalChildrenCount.appendChild(doc.createTextNode("0"));
			family.appendChild(totalChildrenCount);

			Element recordedSpousesCount = doc
					.createElement("SpousesRecordsCount");
			/**
			 * Modified to omit spouse from the xml request
			 */
			// if (attributesMap != null &&
			// attributesMap.get("recordedSpousesCount") != null) {
			// recordedSpousesCount.appendChild(doc.createTextNode(String.valueOf(attributesMap.get("recordedSpousesCount"))));
			// } else {
			// recordedSpousesCount.appendChild(doc.createTextNode("0"));
			// }
			recordedSpousesCount.appendChild(doc.createTextNode("0"));
			family.appendChild(recordedSpousesCount);

			Element recordedChildrenCount = doc
					.createElement("ChildrenRecordsCount");
			/**
			 * Modified to omit child from the xml request
			 */
			// if (attributesMap != null &&
			// attributesMap.get("recordedChildrenCount") != null) {
			// recordedChildrenCount.appendChild(doc.createTextNode(String.valueOf(attributesMap.get("recordedChildrenCount"))));
			// } else {
			// recordedChildrenCount.appendChild(doc.createTextNode("0"));
			// }
			recordedChildrenCount.appendChild(doc.createTextNode("0"));
			family.appendChild(recordedChildrenCount);

			/**
			 * 1==0 was added to omit spouse from the xml request
			 */
			if (1 == 0 && citizenInfoTO != null
					&& citizenInfoTO.getSpouses() != null) {
				Element spousesElement = doc.createElement("Spouses");
				family.appendChild(spousesElement);

				for (int i = 0; i < citizenInfoTO.getSpouses().size(); i++) {

					Element spouseElement = doc.createElement("Spouse");
					spousesElement.appendChild(spouseElement);

					Element spouseFirstNamePersianElement = doc
							.createElement("FirstNamePersian");
					if (citizenInfoTO.getSpouses().get(i)
							.getSpouseFirstNamePersian() != null) {
						spouseFirstNamePersianElement.appendChild(doc
								.createTextNode(citizenInfoTO.getSpouses()
										.get(i).getSpouseFirstNamePersian()));
					}
					spouseElement.appendChild(spouseFirstNamePersianElement);

					Element spouseSurnamePersianElement = doc
							.createElement("SurnamePersian");
					if (citizenInfoTO.getSpouses().get(i)
							.getSpouseSurnamePersian() != null) {
						spouseSurnamePersianElement.appendChild(doc
								.createTextNode(citizenInfoTO.getSpouses()
										.get(i).getSpouseSurnamePersian()));
					}
					spouseElement.appendChild(spouseSurnamePersianElement);

					Element nidElement = doc.createElement("NID");
					if (citizenInfoTO.getSpouses().get(i).getSpouseNationalID() != null) {
						nidElement.appendChild(doc.createTextNode(citizenInfoTO
								.getSpouses().get(i).getSpouseNationalID()));
					}
					spouseElement.appendChild(nidElement);

					Element marriageDateJalaliElement = doc
							.createElement("MarriageDateJalali");
					if (citizenInfoTO.getSpouses().get(i)
							.getSpouseMarriageDate() != null) {
						// Refactored by Saeed
						marriageDateJalaliElement.appendChild(doc
								.createTextNode(DateUtil.convert(citizenInfoTO
										.getSpouses().get(i)
										.getSpouseMarriageDate(),
										DateUtil.JALALI)));
					}
					spouseElement.appendChild(marriageDateJalaliElement);

					Element marriageStatusElement = doc
							.createElement("MarriageStatus");
					marriageStatusElement.appendChild(doc.createTextNode(String
							.valueOf(citizenInfoTO.getSpouses().get(i)
									.getMaritalStatus().getId())));
					spouseElement.appendChild(marriageStatusElement);
				}
			}

			/**
			 * 1==0 was added to omit child from the xml request
			 */
			if (1 == 0 && citizenInfoTO != null
					&& citizenInfoTO.getChildren() != null) {
				Element childrenElement = doc.createElement("Children");
				family.appendChild(childrenElement);
				int childrenCount = citizenInfoTO.getChildren().size();
				if (childrenCount > 20) {
					childrenCount = 20;
				}
				for (int i = 0; i < childrenCount; i++) {

					Element childElement = doc.createElement("Child");
					childrenElement.appendChild(childElement);

					Element childFirstNamePersianElement = doc
							.createElement("FirstNamePersian");
					if (citizenInfoTO.getChildren().get(i)
							.getChildFirstNamePersian() != null) {
						childFirstNamePersianElement.appendChild(doc
								.createTextNode(citizenInfoTO.getChildren()
										.get(i).getChildFirstNamePersian()));
					}
					childElement.appendChild(childFirstNamePersianElement);

					Element childBirthDateJalaliElement = doc
							.createElement("BirthDateJalali");
					if (citizenInfoTO.getChildren().get(i)
							.getChildBirthDateSolar() != null) {
						childBirthDateJalaliElement.appendChild(doc
								.createTextNode(DateUtil.convert(citizenInfoTO
										.getChildren().get(i)
										.getChildBirthDateSolar(),
										DateUtil.JALALI)));
					}
					childElement.appendChild(childBirthDateJalaliElement);

					Element childDeathDateJalaliElement = doc
							.createElement("DeathDateJalali");
					if (citizenInfoTO.getChildren().get(i)
							.getChildDeathDateSolar() != null) {
						childDeathDateJalaliElement.appendChild(doc
								.createTextNode(DateUtil.convert(citizenInfoTO
										.getChildren().get(i)
										.getChildDeathDateSolar(),
										DateUtil.JALALI)));
						childElement.appendChild(childDeathDateJalaliElement);
					}

					Element childGender = doc.createElement("Sex");
					if (citizenInfoTO.getChildren().get(i).getChildGender() != null) {
						childGender.appendChild(doc.createTextNode(Gender
								.convertToString(citizenInfoTO.getChildren()
										.get(i).getChildGender())));
					}
					childElement.appendChild(childGender);

					Element nidElement = doc.createElement("NID");
					if (citizenInfoTO.getChildren().get(i).getChildNationalID() != null) {
						nidElement.appendChild(doc.createTextNode(citizenInfoTO
								.getChildren().get(i).getChildNationalID()));
						childElement.appendChild(nidElement);
					}
				}
			}

			Element biometricIndfoElement = doc.createElement("BiometricInfo");
			rootElement.appendChild(biometricIndfoElement);

			Element faceImages = doc.createElement("FaceImages");
			biometricIndfoElement.appendChild(faceImages);

			Element chipElement = doc.createElement("Chip");
			faceImages.appendChild(chipElement);

			Element compressionFormat = doc.createElement("CompressionFormat");
			compressionFormat.appendChild(doc.createTextNode("JPEG"));
			chipElement.appendChild(compressionFormat);

			Element compressionRatioElement = doc
					.createElement("CompressionRatio");
			compressionRatioElement.appendChild(doc.createTextNode("50:1"));
			chipElement.appendChild(compressionRatioElement);

			Element heightElement = doc.createElement("Height");
			heightElement.appendChild(doc.createTextNode("536"));
			chipElement.appendChild(heightElement);

			Element widthElement = doc.createElement("Width");
			widthElement.appendChild(doc.createTextNode("416"));
			chipElement.appendChild(widthElement);

			Element resolutionElement = doc.createElement("Resolution");
			resolutionElement.appendChild(doc.createTextNode("300"));
			chipElement.appendChild(resolutionElement);

			Element dataElement = doc.createElement("Data");
			for (BiometricTO biometricTO : citizenInfoTO.getBiometrics()) {
				if (biometricTO.getType().equals(BiometricType.FACE_CHIP)) {
					dataElement.appendChild(doc
							.createTextNode(convertToBase64(biometricTO
									.getData())));
				}
			}
			chipElement.appendChild(dataElement);

			Element laserElement = doc.createElement("Laser");
			faceImages.appendChild(laserElement);

			Element compressionFormatForLaserElement = doc
					.createElement("CompressionFormat");

			// Refactored by Saeed
			compressionFormatForLaserElement.appendChild(doc
					.createTextNode("tif"));
			laserElement.appendChild(compressionFormatForLaserElement);

			Element compressionRatioForLaserElement = doc
					.createElement("CompressionRatio");
			compressionRatioForLaserElement.appendChild(doc
					.createTextNode("10:1"));
			laserElement.appendChild(compressionRatioForLaserElement);

			Element heightForLaserElement = doc.createElement("Height");
			heightForLaserElement.appendChild(doc.createTextNode("472"));
			laserElement.appendChild(heightForLaserElement);

			Element widthForLaserElement = doc.createElement("Width");
			widthForLaserElement.appendChild(doc.createTextNode("362"));
			laserElement.appendChild(widthForLaserElement);

			Element resolutionForLaserElement = doc.createElement("Resolution");
			resolutionForLaserElement.appendChild(doc.createTextNode("400"));
			laserElement.appendChild(resolutionForLaserElement);

			Element dataForLasElement = doc.createElement("Data");
			for (BiometricTO biometricTO : citizenInfoTO.getBiometrics()) {
				if (biometricTO.getType().equals(BiometricType.FACE_LASER)) {
					dataForLasElement.appendChild(doc
							.createTextNode(convertToBase64(biometricTO
									.getData())));
				}
			}
			laserElement.appendChild(dataForLasElement);

			Element mliElement = doc.createElement("MLI");
			faceImages.appendChild(mliElement);

			Element compressionFormatForMLIElement = doc
					.createElement("CompressionFormat");

			// Refactored by Saeed
			compressionFormatForMLIElement.appendChild(doc
					.createTextNode("tif"));
			mliElement.appendChild(compressionFormatForMLIElement);

			Element compressionRatioForMLIElement = doc
					.createElement("CompressionRatio");
			compressionRatioForMLIElement.appendChild(doc
					.createTextNode("10:1"));
			mliElement.appendChild(compressionRatioForMLIElement);

			Element heightFormliElement = doc.createElement("Height");
			heightFormliElement.appendChild(doc.createTextNode("94"));
			mliElement.appendChild(heightFormliElement);

			Element widthFormliElement = doc.createElement("Width");
			widthFormliElement.appendChild(doc.createTextNode("73"));
			mliElement.appendChild(widthFormliElement);

			Element resolutionFormliElement = doc.createElement("Resolution");
			resolutionFormliElement.appendChild(doc.createTextNode("300"));
			mliElement.appendChild(resolutionFormliElement);

			Element dataForMLIElement = doc.createElement("Data");
			for (BiometricTO biometricTO : citizenInfoTO.getBiometrics()) {
				if (biometricTO.getType().equals(BiometricType.FACE_MLI)) {
					dataForMLIElement.appendChild(doc
							.createTextNode(convertToBase64(biometricTO
									.getData())));
				}
			}
			mliElement.appendChild(dataForMLIElement);

			// ============================================================================================================
			if (mohFlag || mocCount > 0) {
				Element fingersElement = doc.createElement("Fingers");
				biometricIndfoElement.appendChild(fingersElement);
				Attr featureExtractorID = doc.createAttribute("FeatureExtractorID");
				featureExtractorID.setValue("0001");
				fingersElement.setAttributeNode(featureExtractorID);
				if (mohFlag) {
					Element imageElement = doc.createElement("Image");
					fingersElement.appendChild(imageElement);

					Element sensorModelElement = doc
							.createElement("SensorModel");
					sensorModelElement.appendChild(doc.createTextNode("0000"));
					imageElement.appendChild(sensorModelElement);

					Element positionForImageElement = doc
							.createElement("Position");
					// Value Ranges [00 - 0A] or [FF]
					Map<String, String> map = BiometricTO
							.getMetaData(fingerCandidateMetaDate);
					if (map == null) {
						Object[] args = { "FingerIndex" };
						throw new DataException(DataExceptionCode.CRC_006,
								DataExceptionCode.GLB_001_MSG, args);
					}
					String fingerIndex = map.get("FingerIndex");
					if (!fingerIndex.equals("10")) {
						fingerIndex = "0" + fingerIndex;
					} else {
						fingerIndex = "0A";
					}
					positionForImageElement.appendChild(doc
							.createTextNode(fingerIndex));
					imageElement.appendChild(positionForImageElement);

					Element compressionFormatForImageElement = doc
							.createElement("CompressionFormat");
					compressionFormatForImageElement.appendChild(doc
							.createTextNode("WSQ"));
					imageElement.appendChild(compressionFormatForImageElement);

					Element compressionRatioForImageElement = doc
							.createElement("CompressionRatio");
					compressionRatioForImageElement.appendChild(doc
							.createTextNode("15:1"));
					imageElement.appendChild(compressionRatioForImageElement);

					Element dataForImageElement = doc.createElement("Data");
					dataForImageElement.appendChild(doc
							.createTextNode(convertToBase64(fingerCandidate)));
					imageElement.appendChild(dataForImageElement);
				}

				if (mocCount > 0) {
					Map<String, String> map;
					Element templatesElement = doc.createElement("Templates");
					fingersElement.appendChild(templatesElement);

					if (fingerMinOne != null) {
						Element firstTemplateElement = doc
								.createElement("Template");
						templatesElement.appendChild(firstTemplateElement);

						Element dataForFirstTemplateElement = doc
								.createElement("Data");
						dataForFirstTemplateElement.appendChild(doc
								.createTextNode(convertToBase64(fingerMinOne)));

						firstTemplateElement
								.appendChild(dataForFirstTemplateElement);

						Element positionForFirstTemplateElement = doc
								.createElement("Position");
						// Value Ranges 00, 05, 09, 0D, 11, 15, 06, 0A, 0E, 12,
						// 16
						map = BiometricTO.getMetaData(fingerMinOneMetaDate);
						if (map == null) {
							Object[] args = { "FingerIndex" };
							throw new DataException(DataExceptionCode.CRC_007,
									DataExceptionCode.GLB_001_MSG, args);
						}
						String fingerMinOneIndex = map.get("FingerIndex");
						positionForFirstTemplateElement
								.appendChild(doc
										.createTextNode(prepareMOCIndex(fingerMinOneIndex)));
						firstTemplateElement
								.appendChild(positionForFirstTemplateElement);
					}

					if (fingerMinTwo != null) {
						Element secondTemplateElement = doc
								.createElement("Template");
						templatesElement.appendChild(secondTemplateElement);

						Element dataForSecondTemplateElement = doc
								.createElement("Data");
						dataForSecondTemplateElement.appendChild(doc
								.createTextNode(convertToBase64(fingerMinTwo)));

						secondTemplateElement
								.appendChild(dataForSecondTemplateElement);

						Element positionForSecondTemplateElement = doc
								.createElement("Position");
						// Value Ranges 00, 05, 09, 0D, 11, 15, 06, 0A, 0E, 12,
						// 16
						map = BiometricTO.getMetaData(fingerMinTwoMetaDate);
						if (map == null) {
							Object[] args = { "FingerIndex" };
							throw new DataException(DataExceptionCode.CRC_008,
									DataExceptionCode.GLB_001_MSG, args);
						}
						String fingerMinTwoIndex = map.get("FingerIndex");
						positionForSecondTemplateElement
								.appendChild(doc
										.createTextNode(prepareMOCIndex(fingerMinTwoIndex)));
						secondTemplateElement
								.appendChild(positionForSecondTemplateElement);
					}

				}
			}
			// ============================================================================================================

			/***
			 * Comment out following lines till the next comment block in order
			 * to write the output into an xml file.
			 */
			// write the content into xml file
			// TransformerFactory transformerFactory = TransformerFactory
			// .newInstance();
			// Transformer transformer = transformerFactory.newTransformer();
			// DOMSource source = new DOMSource(doc);
			// StreamResult result = new StreamResult(new File(
			// "/Users/Sina/Desktop/Generated by DOM.xml"));
			//
			// // Output to console for testing
			// // StreamResult result = new StreamResult(System.out);
			//
			// transformer.transform(source, result);
			//
			// logger.info("File saved!");
			// return null;

			/***
			 *
			 */

			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			logger.info("File saved!!!!!");
			return writer.toString().getBytes("UTF-8");

		} catch (ParserConfigurationException e) {
			throw new DataException(DataExceptionCode.CRC_002, e);
		} catch (TransformerConfigurationException e) {
			throw new DataException(DataExceptionCode.CRC_003, e);
		} catch (TransformerException e) {
			throw new DataException(DataExceptionCode.CRC_004, e);
		} catch (UnsupportedEncodingException e) {
			throw new DataException(DataExceptionCode.CRC_005, e);
		} catch (Exception e) {
			throw new DataException(DataExceptionCode.CRC_009, e);
		}
	}

    private EnrollmentOfficeDAO getEnrollmentOfficeDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_ENROLLMENT_OFFICE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(
                    BizExceptionCode.EOS_001,
                    BizExceptionCode.GLB_001_MSG,
                    e,
                    EMSLogicalNames.DAO_ENROLLMENT_OFFICE.split(","));
        }
    }


	public ExtEntityTO readXML(String xmlData, ExtEntityTO to)
			throws BaseException {
		// try {
		//
		// CardRequestTO cardRequestTO = (CardRequestTO) to;
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
		//
		// String xpathQuery = "//EnrollmentInfo/EnrollDate/text()";
		// cardRequestTO.setEnrolledDate((Date) nodeExtractor(doc, xpath,
		// xpathQuery));
		//
		// xpathQuery = "//EnrollmentInfo/UserSiteID/text()";
		// cardRequestTO.getEnrollmentOffice().setId((Long) nodeExtractor(doc,
		// xpath, xpathQuery));
		//
		// xpathQuery = "//CitizenInfo/Identification/FirstNamePersian/text()";
		// cardRequestTO.getCitizen().setFirstNamePersian((String)
		// nodeExtractor(doc, xpath, xpathQuery));
		//
		// xpathQuery = "//CitizenInfo/Identification/FirstNameEnglish/text()";
		// CitizenInfoTO citizenInfoTO
		// =cardRequestTO.getCitizen().getCitizenInfo();
		// citizenInfoTO.setFirstNameEnglish((String) nodeExtractor(doc, xpath,
		// xpathQuery));
		//
		// xpathQuery = "//CitizenInfo/Identification/SurnamePersian/text()";
		// cardRequestTO.getCitizen().setSurnamePersian((String)
		// nodeExtractor(doc, xpath, xpathQuery));
		//
		// xpathQuery = "//CitizenInfo/Identification/SurnameEnglish/text()";
		// citizenInfoTO.setSurnameEnglish((String) nodeExtractor(doc, xpath,
		// xpathQuery));
		//
		// xpathQuery = "//CitizenInfo/Identification/BirthDateHijri/text()";
		// citizenInfoTO.setBirthDateLunar((String) nodeExtractor(doc, xpath,
		// xpathQuery));
		//
		// xpathQuery = "//CitizenInfo/Identification/BirthDateJalali/text()";
		// citizenInfoTO.setBirthDateSolar((String) nodeExtractor(doc, xpath,
		// xpathQuery));
		//
		// xpathQuery =
		// "//CitizenInfo/Identification/FatherFirstNamePersian/text()";
		// citizenInfoTO.setFatherFirstNamePersian((String) nodeExtractor(doc,
		// xpath, xpathQuery));
		//
		// xpathQuery = "//CitizenInfo/Identification/NationalID/text()";
		// cardRequestTO.getCitizen().setNationalID((String) nodeExtractor(doc,
		// xpath, xpathQuery));
		//
		// xpathQuery = "//CitizenInfo/Identification/Reduplicate/text()";
		// cardRequestTO.getCitizen().setReduplicate((Integer)
		// nodeExtractor(doc, xpath, xpathQuery));
		//
		// xpathQuery = "//CitizenInfo/Identification/Sex/text()";
		// citizenInfoTO.setGender((Gender) nodeExtractor(doc, xpath,
		// xpathQuery));
		//
		// xpathQuery =
		// "//CitizenInfo/Identification/MotherFirstNamePersian/text()";
		// citizenInfoTO.setMotherFirstNamePersian((String) nodeExtractor(doc,
		// xpath, xpathQuery));
		//
		// xpathQuery = "//CitizenInfo/Identification/IdentityChange/text()";
		// citizenInfoTO.setIdentityChange((Integer) nodeExtractor(doc, xpath,
		// xpathQuery));
		//
		// xpathQuery = "//CitizenInfo/Identification/Email/text()";
		// citizenInfoTO.setEmail((String) nodeExtractor(doc, xpath,
		// xpathQuery));
		//
		// xpathQuery = "//CitizenInfo/Identification/Religion/text()";
		// citizenInfoTO.getReligion().setName((String) nodeExtractor(doc,
		// xpath, xpathQuery));
		//
		// xpathQuery = "//CitizenInfo/Identification/AFISValidate/text()";
		// //TODO extract AFIS
		//
		// xpathQuery = "//CitizenInfo/Identification/MoCFingersCount/text()";
		// //TODO extract Moc
		//
		// xpathQuery = "//CitizenInfo/Address/PostalCode/text()";
		// citizenInfoTO.setPostcode((String) nodeExtractor(doc, xpath,
		// xpathQuery));
		//
		// xpathQuery = "//CitizenInfo/Family/SpousesCount/text()";
		// citizenInfoTO.setS
		//
		//
		//
		// } catch (ParserConfigurationException e) {
		// logger.error(DataExceptionCode.GLB_ERR_MSG, e);
		// } catch (SAXException e) {
		// logger.error(DataExceptionCode.GLB_ERR_MSG, e);
		// } catch (IOException e) {
		// logger.error(DataExceptionCode.GLB_ERR_MSG, e);
		// } catch (XPathExpressionException e) {
		// logger.error(DataExceptionCode.GLB_ERR_MSG, e);
		// }
		//
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
	 * @param xmlData
	 *            is an instance of type {@link String} which represents the xml
	 *            String that has wanted to read
	 * @param to
	 *            is an instance of type
	 *            {@link com.gam.commons.core.data.domain.ExtEntityTO}
	 * @param attributesMap
	 *            is a map of type {@link java.util.Map <String, String>}
	 * @return an instance of type
	 *         {@link com.gam.commons.core.data.domain.ExtEntityTO}
	 * @throws com.gam.commons.core.BaseException
	 * 
	 */
	@Override
	public ExtEntityTO readXML(String xmlData, ExtEntityTO to,
			Map<String, String> attributesMap) throws BaseException {
		return null;
	}
}
