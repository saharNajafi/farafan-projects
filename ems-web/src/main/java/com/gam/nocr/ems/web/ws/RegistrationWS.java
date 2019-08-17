package com.gam.nocr.ems.web.ws;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.Internal;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.profile.ProfileException;
import com.gam.commons.profile.ProfileManager;
import com.gam.commons.security.SecurityUtil;
import com.gam.nocr.ems.biz.delegator.CompleteRegistrationDelegator;
import com.gam.nocr.ems.biz.delegator.RegistrationDelegator;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.ConstValues;
import com.gam.nocr.ems.config.NOCRPKEParamProviderImpl;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.WebExceptionCode;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.ws.*;
import com.gam.nocr.ems.data.enums.*;
import com.gam.nocr.ems.data.mapper.tomapper.BiometricMapper;
import com.gam.nocr.ems.data.mapper.tomapper.CardRequestMapper;
import com.gam.nocr.ems.data.mapper.tomapper.DocumentMapper;
import com.gam.nocr.ems.data.mapper.tomapper.DocumentTypeMapper;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.nocr.ems.util.NationalIDUtil;
import gampooya.tools.date.DateFormatException;
import gampooya.tools.date.DateUtil;
import gampooya.tools.util.Base64;
import org.slf4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebFault;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Exposes all operations related to the registration process in enrollment offices
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@WebFault(faultBean = "com.gam.nocr.ems.web.ws.InternalException")
@WebService(serviceName = "RegistrationWS", portName = "RegistrationWSPort", targetNamespace = "http://ws.web.ems.nocr.gam.com/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Internal
public class RegistrationWS extends EMSWS {

    static final Logger logger = BaseLog.getLogger(RegistrationWS.class);

    private RegistrationDelegator registrationDelegator = new RegistrationDelegator();

    /**
     * Returns a public key to be used by CCOS to encrypt fingerprints before sending to EMS. It fetches the certificate
     * object with CCOS_FINGER_CER_PUBLIC usage from EMST_CERT table in database
     *
     * @param securityContextWTO The login and session information of the user
     * @return a public key to be used by CCOS to encrypt fingerprints before sending to EMS
     * @throws InternalException
     */
    @WebMethod
    public String getCredentials(SecurityContextWTO securityContextWTO) throws InternalException {
        super.validateRequest(securityContextWTO);
        CertificateTO certificateTO;
        try {
            certificateTO = registrationDelegator.getCertificateByUsage(CertificateUsage.CCOS_FINGER_CER_PUBLIC);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_075, new EMSWebServiceFault(WebExceptionCode.RSW_075), e);
        }
        return convertToBase64(certificateTO.getCertificate());
    }

    /**
     * Converts given parameter to its Base64 string representation
     *
     * @param input Input data to be converted to Base64
     * @return Base64 representation of given input
     * @throws InternalException
     */
    private String convertToBase64(byte[] input) throws InternalException {
        String encoded;
        try {
            encoded = new String(Base64.encode(input), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InternalException(WebExceptionCode.RSW_076, new EMSWebServiceFault(WebExceptionCode.RSW_076), e);
        }
        return encoded;
    }

    /**
     * Saves a registration request in EMS database. This method is called by CCOS for new registration in enrollment
     * offices
     *
     * @param securityContextWTO The login and session information of the user
     * @param citizenWTO         Registration information
     * @return Identifier of the {@link com.gam.nocr.ems.data.domain.CardRequestTO} created for given citizen
     * @throws InternalException
     */
    @WebMethod
    public long saveRegistrationRequest(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                        @WebParam(name = "citizenWTO") CitizenWTO citizenWTO) throws InternalException {

        UserProfileTO up = super.validateRequest(securityContextWTO);
        CardRequestTO cardRequestTO;
        try {
            cardRequestTO = CardRequestMapper.convert(citizenWTO);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_001_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_001), e);
        }

        if (citizenWTO.getRequestId() != null) {
            throw new InternalException(WebExceptionCode.RSW_003_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_003));
        }
        cardRequestTO.setEnrolledDate(new Date());
        cardRequestTO.setState(CardRequestState.REFERRED_TO_CCOS);
        cardRequestTO.setEnrollmentOffice(new EnrollmentOfficeTO(up.getDepID()));
        cardRequestTO.setEstelam2Flag(Estelam2FlagType.V);
        cardRequestTO.setRequestedSmsStatus(1);

        try {
            return registrationDelegator.save(up, cardRequestTO);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_004_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_004), e);
        }
    }

    /**
     * Updates a {@link com.gam.nocr.ems.data.domain.CardRequestTO} in EMS. The CCOS calls this service when a citizen
     * refers to the enrollment office to complete its registration or in any cases that a registration request has to
     * be updated (e.g. when user tries to re-edit the request and update its contact info or postal address again)
     *
     * @param securityContextWTO The login and session information of the user
     * @param citizenWTO         Registration information
     * @throws InternalException
     */
    @WebMethod
    public void updateRegistrationRequest(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                          @WebParam(name = "citizenWTO") CitizenWTO citizenWTO) throws InternalException {

        UserProfileTO up = super.validateRequest(securityContextWTO);
        CardRequestTO cardRequestTO;
        try {
            cardRequestTO = CardRequestMapper.convert(citizenWTO);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_006_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_006), e);
        }
        if (citizenWTO.getRequestId() == null) {
            throw new InternalException(WebExceptionCode.RSW_005_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_005));
        }
        cardRequestTO.setEnrolledDate(new Date());
        cardRequestTO.setState(CardRequestState.REFERRED_TO_CCOS);
        cardRequestTO.setEnrollmentOffice(new EnrollmentOfficeTO(up.getDepID()));
        try {
            registrationDelegator.update(up, cardRequestTO);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_008_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_008), e);
        }
    }

    /**
     * Given an identifier of {@link com.gam.nocr.ems.data.domain.CardRequestTO} object and returns its detailed
     * information to be displayed in CCOS. The CCOS calls this service in scenarios like "manager approve" or "view
     * registration details" in order to show them to the user
     *
     * @param securityContextWTO The login and session information of the user
     * @param requestID          Identifier of the {@link com.gam.nocr.ems.data.domain.CardRequestTO} to fetch its
     *                           details
     * @return Detailed information about given request to be displayed to the user
     * @throws InternalException
     */
    @WebMethod
    public CitizenWTO getRegistrationRequest(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                             @WebParam(name = "requestID") long requestID) throws InternalException {

        UserProfileTO up = super.validateRequest(securityContextWTO);
        CardRequestTO crq;
        CitizenTO ctz;

        try {
            crq = registrationDelegator.load(up, requestID);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_033_MSG + requestID, new EMSWebServiceFault(WebExceptionCode.RSW_033), e);
        }
        if (crq == null) {
            throw new InternalException(WebExceptionCode.RSW_010_MSG + requestID, new EMSWebServiceFault(WebExceptionCode.RSW_010));
        }
        try {
            ctz = registrationDelegator.loadCitizen(up, requestID);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_039_MSG + requestID, new EMSWebServiceFault(WebExceptionCode.RSW_039), e);
        }

        if (ctz == null) {
            throw new InternalException(WebExceptionCode.RSW_058_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_058));
        }

        CitizenWTO wto;
        try {
            wto = CardRequestMapper.convert(crq, ctz);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_009_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_009), e);
        }
        return wto;
    }

    /**
     * Adds fingerprint information of the citizen to his/her records. Fingerprint information are in encrypted format
     * and should be decrypted first
     *
     * @param securityContextWTO The login and session information of the user
     * @param fingerInfoWTO      Identifier of the {@link com.gam.nocr.ems.data.domain.CardRequestTO} that given
     *                           fingerprint information belong to
     *                           Collection of fingerprint information (FING_ALL, FING_MIN_1, FING_MIN_2 and
     *                           FING_CANDIDATE)
     * @throws InternalException
     */
    @WebMethod
    public void saveFingerInfo(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                               @WebParam(name = "fingerInfoWTO") FingerInfoWTO fingerInfoWTO) throws InternalException {

        UserProfileTO up = super.validateRequest(securityContextWTO);
        if (fingerInfoWTO.getBiometricWTOs() == null) {
            throw new InternalException(WebExceptionCode.RSW_030_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_030));
        }
        if (fingerInfoWTO.getBiometricWTOs().length == 0) {
            throw new InternalException(WebExceptionCode.RSW_038_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_038));
        }

        if (!EmsUtil.checkString(fingerInfoWTO.getFeatureExtractorIdNormal())) {
            throw new InternalException(WebExceptionCode.RSW_086_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_086));
        }

        if (!EmsUtil.checkString(fingerInfoWTO.getFeatureExtractorIdCC())) {
            throw new InternalException(WebExceptionCode.RSW_087_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_087));
        }

        BiometricTO bio;
        ArrayList<BiometricTO> biometrics = new ArrayList<BiometricTO>();
        for (BiometricWTO biometricWTO : fingerInfoWTO.getBiometricWTOs()) {
            try {
                decryptFingerData(biometricWTO);
                bio = BiometricMapper.convert(biometricWTO);
            } catch (BaseException e) {
                throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
            } catch (Exception e) {
                throw new InternalException(WebExceptionCode.RSW_012_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_012));
            }

            if (bio.getType() == null) {
                throw new InternalException(WebExceptionCode.RSW_031_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_031));
            }
            if (bio.getType() == BiometricType.FACE_CHIP || bio.getType() == BiometricType.FACE_IMS || bio.getType() == BiometricType.FACE_LASER || bio.getType() == BiometricType.FACE_MLI) {
                throw new InternalException(WebExceptionCode.RSW_032_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_032));
            }
            biometrics.add(bio);
        }

        try {
//          registrationDelegator.addFingerData(up, requestID, biometrics);
            registrationDelegator.addFingerData(up, fingerInfoWTO.getRequestId(), biometrics
                    , fingerInfoWTO.getFeatureExtractorIdNormal(), fingerInfoWTO.getFeatureExtractorIdCC());
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_013_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_013), e);
        }
    }

    /**
     * Given a fingerprint data, decrypts it and put the final result back into the same object
     *
     * @param biometricWTO Encrypted biometric data (the final result will be put back in it too)
     * @throws BaseException
     * @throws ProfileException
     */
    private void decryptFingerData(BiometricWTO biometricWTO) throws BaseException, ProfileException {
        /**
         * @author Sina Golesorkhi
         */
        CertificateTO certificateTO = registrationDelegator.getCertificateByUsage(CertificateUsage.CCOS_FINGER_CER_PRIVATE);
        biometricWTO.setSymmetricKey(SecurityUtil.asymmetricDecryption(certificateTO.getCertificate(), biometricWTO.getSymmetricKey(), certificateTO.getCode()));
        biometricWTO.setInitialVector(SecurityUtil.asymmetricDecryption(certificateTO.getCertificate(), biometricWTO.getInitialVector(), certificateTO.getCode()));

        ProfileManager profileManager = ProfileHelper.getProfileManager();
        NOCRPKEParamProviderImpl nocrpkeParamProviderImpl = new NOCRPKEParamProviderImpl(profileManager);
        nocrpkeParamProviderImpl.setSymmetricDecryptionTransformation("AES/CBC/X9.23PADDING");
        nocrpkeParamProviderImpl.setSymmetricDecSecretKeyAlgorithm("AES");
        biometricWTO.setData(SecurityUtil.symmetricDecryption(biometricWTO.getSymmetricKey(), biometricWTO.getInitialVector(), biometricWTO.getData(), nocrpkeParamProviderImpl));

        /**
         * @author Sina Golesorkhi
         */
    }

    /**
     * Removes finger information of given request id from database
     *
     * @param securityContextWTO The login and session information of the user
     * @param requestID          Identifier of the {@link com.gam.nocr.ems.data.domain.CardRequestTO} that its finger information
     *                           should be deleted
     * @return Always true (not used)
     * @throws InternalException
     */
    @WebMethod
    public boolean removeFingerInfo(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                    @WebParam(name = "requestID") long requestID) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        //for (BiometricType type : BiometricType.values()) {
        // {
        try {
            registrationDelegator.removeFingerAllTypeData(up, requestID);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_017_MSG /*+ type.name()*/, new EMSWebServiceFault(WebExceptionCode.RSW_017), e);
        }
        //}
        //}
        return true;
    }

    /**
     * Adds face information of given request to EMS database.
     *
     * @param securityContextWTO The login and session information of the user
     * @param faceInfoWTO        Identifier of the {@link com.gam.nocr.ems.data.domain.CardRequestTO} to add face
     *                           information to it
     *                           Collection of face information (FACE_IMS, FACE_CHIP, FACE_MLI and FACE_LASER)
     * @throws InternalException
     */
    @WebMethod
    public void saveFaceInfo(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                             @WebParam(name = "faceInfoWTO") FaceInfoWTO faceInfoWTO) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        if (faceInfoWTO.getBiometricWTOs() == null) {
            throw new InternalException(WebExceptionCode.RSW_040_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_040));
        }
        if (faceInfoWTO.getBiometricWTOs().length == 0) {
            throw new InternalException(WebExceptionCode.RSW_035_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_035));
        }
        if (faceInfoWTO.getBiometricWTOs().length < 4) {
            throw new InternalException(WebExceptionCode.RSW_080_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_080));
        }

        BiometricTO bio;
        ArrayList<BiometricTO> biometrics = new ArrayList<BiometricTO>();
        for (BiometricWTO biometricWTO : faceInfoWTO.getBiometricWTOs()) {
            try {
                bio = BiometricMapper.convert(biometricWTO);
            } catch (BaseException e) {
                throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
            } catch (Exception e) {
                throw new InternalException(WebExceptionCode.RSW_018_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_018), e);
            }
            if (bio.getType() == null) {
                throw new InternalException(WebExceptionCode.RSW_037_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_037));
            }
            if (bio.getType() == BiometricType.FING_ALL || bio.getType() == BiometricType.FING_CANDIDATE || bio.getType() == BiometricType.FING_MIN_1 || bio.getType() == BiometricType.FING_MIN_2
                    || bio.getType() == BiometricType.FING_NORMAL_1 || bio.getType() == BiometricType.FING_NORMAL_2) {
                throw new InternalException(WebExceptionCode.RSW_034_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_034));
            }
            biometrics.add(bio);
        }

        try {
            registrationDelegator.addFaceData(up, faceInfoWTO.getRequestId(), biometrics, faceInfoWTO.getFaceDisabilityStatus());
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_019_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_019), e);
        }
    }

    /**
     * Returns the face information of given {@link com.gam.nocr.ems.data.domain.CardRequestTO} to be displayed in CCOS.
     * It's mainly used to display the citizen picture in "manager approve" or "view request detail" or even
     * "fingerprint scan" processes.
     *
     * @param securityContextWTO The login and session information of the user
     * @param requestID          Identifier of the {@link com.gam.nocr.ems.data.domain.CardRequestTO} to fetch its face
     *                           information.
     * @return The picture of given citizen
     * @throws InternalException
     */
    @WebMethod
    public BiometricWTO getFaceInfo(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                    @WebParam(name = "requestID") long requestID, @WebParam(name = "faceType") String faceType) throws InternalException {

        UserProfileTO up = super.validateRequest(securityContextWTO);

        List<BiometricTO> biometrics;
        try {

            BiometricType type = BiometricType.FACE_LASER;
            if (faceType != null)
                type = BiometricType.toType(faceType);
            biometrics = registrationDelegator.getFaceData(up, requestID, type);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()));
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_056_MSG + requestID, new EMSWebServiceFault(WebExceptionCode.RSW_056), e);
        }
        if ((biometrics == null) || (biometrics.size() == 0)) {
            return null;
        }
        if (biometrics.size() > 1) {
            throw new InternalException(WebExceptionCode.RSW_021_MSG + requestID, new EMSWebServiceFault(WebExceptionCode.RSW_021));
        }
        BiometricWTO wto;
        try {
            wto = BiometricMapper.convert(biometrics.get(0));
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_022_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_022), e);
        }
        return wto;
    }

    /**
     * Removes face information for given {@link com.gam.nocr.ems.data.domain.CardRequestTO}. It's mainly used when the
     * enrollment office manager disapproves the registration process and the picture of given citizen should be
     * captured again
     *
     * @param securityContextWTO The login and session information of the user
     * @param requestID          Identifier of the {@link com.gam.nocr.ems.data.domain.CardRequestTO} to remove his/her
     *                           face information
     * @return Always true (not used)
     * @throws InternalException
     */
    //Edited By Adldoost
    @WebMethod
    public boolean removeFaceInfo(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                  @WebParam(name = "requestID") long requestID) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
//        for (BiometricType type : BiometricType.values()) {
//            if (type == BiometricType.FACE_IMS || type == BiometricType.FACE_CHIP || type == BiometricType.FACE_MLI || type == BiometricType.FACE_LASER) {
        try {
            registrationDelegator.removeFaceAllTypeData(up, requestID);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_023_MSG /*+ type.name()*/, new EMSWebServiceFault(WebExceptionCode.RSW_023), e);
        }
//            }
//        }
        return true;
    }

    /**
     * Returns a collection of documents that should be scanned by CCOS user in registration process. The CCOS calls
     * this service when user tries to scan registration documents in order to detect how many documents and in what
     * types should be scanned. The document scan form will be generated dynamically base on the results of this service
     *
     * @param securityContextWTO The login and session information of the user
     * @param serviceType        The type of registration process that its document types are needed (e.g. Replica,
     *                           Replace, etc.)
     * @param requestID          Identifier of the card request that its documents are going to be scanned
     * @return List of document types that should be scanned in CCOS
     * @throws InternalException
     */
    @WebMethod
    public DocumentTypeWTO[] getServiceDocuments(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                                 @WebParam(name = "serviceType") Integer serviceType,
                                                 @WebParam(name = "requestID") Long requestID)
            throws InternalException {

        UserProfileTO up = super.validateRequest(securityContextWTO);
        List<DocumentTypeTO> docTypes;
        try {
            docTypes = registrationDelegator.getServiceDocuments(up, serviceType, requestID);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_057_MSG + serviceType, new EMSWebServiceFault(WebExceptionCode.RSW_057), e);
        }
        DocumentTypeWTO[] wtos = new DocumentTypeWTO[docTypes.size()];
        for (int i = 0; i < docTypes.size(); i++) {
            try {
                wtos[i] = DocumentTypeMapper.convert(docTypes.get(i));
            } catch (BaseException e) {
                throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
            } catch (Exception e) {
                throw new InternalException(WebExceptionCode.RSW_026_MSG + docTypes.get(i).getId(), new EMSWebServiceFault(WebExceptionCode.RSW_026), e);
            }
        }
        return wtos;
    }

    /**
     * Adds scanned documents to given {@link com.gam.nocr.ems.data.domain.CardRequestTO} object
     *
     * @param securityContextWTO  The login and session information of the user
     * @param requestDocumentsWTO Identifier of the {@link com.gam.nocr.ems.data.domain.CardRequestTO} to add given
     *                            documents to
     *                            List of scanned documents to save
     * @throws InternalException
     */
    @WebMethod
    public void saveRequestDocuments(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                     @WebParam(name = "requestDocumentsWTO") RequestDocumentsWTO requestDocumentsWTO) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        ArrayList<DocumentTO> docs = new ArrayList<DocumentTO>();
        DocumentTO doc;
        for (DocumentWTO wto : requestDocumentsWTO.getDocumentWTO()) {
            try {
                doc = DocumentMapper.convert(wto);
            } catch (BaseException e) {
                throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
            } catch (Exception e) {
                throw new InternalException(WebExceptionCode.RSW_024_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_024), e);
            }
            docs.add(doc);
        }
        try {
            registrationDelegator.addDocument(up, requestDocumentsWTO.getRequestId(), docs);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_024_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_025), e);
        }
    }

    /**
     * Returns list of scanned documents for given {@link com.gam.nocr.ems.data.domain.CardRequestTO}. It would be
     * called by CCOS to fetch the list of scanned document to be displayed in manager approval form
     *
     * @param securityContextWTO The login and session information of the user
     * @param requestID          Identifier of the {@link com.gam.nocr.ems.data.domain.CardRequestTO} to fetch its
     *                           scanned documents
     * @return List of document scanned for given {@link com.gam.nocr.ems.data.domain.CardRequestTO}
     * @throws InternalException
     */
    @WebMethod
    public DocumentWTO[] getRequestDocuments(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                             @WebParam(name = "requestID") long requestID) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        List<DocumentTO> documents;
        try {
            documents = registrationDelegator.getDocument(up, requestID, null);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_054_MSG + requestID, new EMSWebServiceFault(WebExceptionCode.RSW_054), e);
        }
        DocumentWTO[] wtos = new DocumentWTO[documents.size()];
        for (int i = 0; i < documents.size(); i++) {
            try {
                wtos[i] = DocumentMapper.convert(documents.get(i));
            } catch (BaseException e) {
                throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
            } catch (Exception e) {
                throw new InternalException(WebExceptionCode.RSW_036_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_036), e);
            }
        }
        return wtos.length == 0 ? null : wtos;
    }

    /**
     * Registers the authentication of citizen's birth certificate and other identity documents. The CCOS calls this
     * service when user selects corresponding action for a {@link com.gam.nocr.ems.data.domain.CardRequestTO}
     *
     * @param securityContextWTO The login and session information of the user
     * @param requestID          Identifier of the {@link com.gam.nocr.ems.data.domain.CardRequestTO} to register its
     *                           document authentication
     * @throws InternalException
     */
    @WebMethod
    public void authenticateDocuments(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                      @WebParam(name = "requestID") long requestID) throws InternalException {

        UserProfileTO up = super.validateRequest(securityContextWTO);
        try {
            registrationDelegator.authenticateDocument(up, requestID);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_027_MSG + requestID, new EMSWebServiceFault(WebExceptionCode.RSW_027), e);
        }
    }

    /**
     * Removes scanned documents for given {@link com.gam.nocr.ems.data.domain.CardRequestTO}. It's mainly used when the
     * enrollment office manager disapproves the registration process and documents of given citizen should be scanned
     * again
     *
     * @param securityContextWTO The login and session information of the user
     * @param requestID          Identifier of the card request that its documents are going to be removed
     * @param documentTypeWTOs   Type of documents that should be removed. As the manager may just disapproves some
     *                           items (not all of them) this third parameter will be used to specify just those ones
     * @return true, if documents removed successfully and false in other cases
     * @throws InternalException
     */
    @WebMethod
    public boolean removeDocument(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                  @WebParam(name = "requestID") Long requestID,
                                  @WebParam(name = "documentTypeWTOs") List<DocumentTypeWTO> documentTypeWTOs) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        try {
            return registrationDelegator.removeDocument(up, requestID, documentTypeWTOs);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_028_MSG + requestID, new EMSWebServiceFault(WebExceptionCode.RSW_028), e);
        }
    }

    /**
     * Registers approval of given card request by enrollment office manager
     *
     * @param securityContextWTO The login and session information of the user
     * @param requestID          Identifier of the {@link com.gam.nocr.ems.data.domain.CardRequestTO} that manager
     *                           approves
     * @param digest             As manger signs the request with his/her token, the digest result has to be saved in
     *                           database. This parameters represents the digest of the registration
     * @throws InternalException
     */
    @WebMethod
    public void setManagerApproval(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                   @WebParam(name = "requestID") long requestID,
                                   @WebParam(name = "digest") byte[] digest)
            throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        try {
            registrationDelegator.approveRequest(up, requestID, digest);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_029_MSG + requestID, new EMSWebServiceFault(WebExceptionCode.RSW_029), e);
        }
    }

    /**
     * The CCOS calls this service by citizen's firstName, lastName and NID in order to check its existence in EMS. This
     * service is called in all issuance processes (except first card) by CCOS in order to fetch citizen's information
     * and fill the registration form (in read-only mode)
     *
     * @param securityContextWTO The login and session information of the user
     * @param itemWTOs           Collection of search items (firstName, lastName and NID) and their corresponding values
     * @return Registration information for given user (if any found in system)
     * @throws InternalException
     */
    @WebMethod
    public CitizenWTO findRegistrationRequest(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                              @WebParam(name = "itemWTOs") ItemWTO[] itemWTOs) throws InternalException {

        UserProfileTO up = super.validateRequest(securityContextWTO);

        String firstName = null, lastName = null, nationalId = null;

        for (ItemWTO wto : itemWTOs) {
            if (wto == null) {
                throw new InternalException(WebExceptionCode.RSW_058_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_058));
            }
            if (wto.getKey() == null) {
                throw new InternalException(WebExceptionCode.RSW_059_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_059));
            }
            if (wto.getKey().equals("firstNameFA")) {
                firstName = wto.getValue();
                continue;
            }
            if (wto.getKey().equals("sureNameFA")) {
                lastName = wto.getValue();
                continue;
            }
            if (wto.getKey().equals("nationalId")) {
                nationalId = wto.getValue();
                continue;
            }
            throw new InternalException(WebExceptionCode.RSW_060_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_060));
        }

        CitizenTO ctz;
        try {
            ctz = registrationDelegator.loadCitizen(up, firstName, lastName, nationalId);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_061_MSG + nationalId, new EMSWebServiceFault(WebExceptionCode.RSW_061), e);
        }

        if (ctz == null) {
            throw new InternalException(WebExceptionCode.RSW_062_MSG + firstName + ", " + lastName + ", " + nationalId, new EMSWebServiceFault(WebExceptionCode.RSW_062));
        }


        try {
            registrationDelegator.checkPreviousCardRequestNotStopped(up, ctz);
            CitizenWTO citizenWTO = CardRequestMapper.convert(null, ctz);

            citizenWTO.setId(null);
            return citizenWTO;
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_063_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_063), e);
        }
    }

    /**
     * The CCOS calls this service by citizen's cardRequestType, crn and nationalId and birthDate in order to check its existence in EMS. This
     * service is called in all issuance processes (except first card) by CCOS in order to fetch citizen's information
     * and fill the registration form (in read-only mode)
     *
     * @param securityContextWTO  The login and session information of the user
     * @param fetchCitizenInfoWTO
     * @return Registration information for given user (if any found in system)
     * @throws InternalException
     */
    @WebMethod
    public CitizenWTO fetchCitizenInfo(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "fetchCitizenInfoWTO") FetchCitizenInfoWTO fetchCitizenInfoWTO) throws InternalException {

        UserProfileTO up = super.validateRequest(securityContextWTO);
        CitizenWTO citizenWTO = null;
        CitizenTO citizenTO = null;
        Date birthDate = null;
        Date ctzBirthDate = null;
        CardRequestType type = fetchCitizenInfoWTO.getType();

        if (!NationalIDUtil.checkValidNinStr(fetchCitizenInfoWTO.getNationalId())) {
            throw new InternalException(
                    WebExceptionCode.RSW_088_MSG + fetchCitizenInfoWTO.getNationalId(),
                    new EMSWebServiceFault(WebExceptionCode.RSW_088));
        }
        if (fetchCitizenInfoWTO.getBirthDate() == null || fetchCitizenInfoWTO.getBirthDate().isEmpty()) {
            throw new InternalException(WebExceptionCode.RSW_092_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_092));
        }
        if (fetchCitizenInfoWTO.getType() == null) {
            throw new InternalException(WebExceptionCode.RSW_094_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_094));
        }
        try {
            registrationDelegator.checkPreviousCardStateValid(up, fetchCitizenInfoWTO.getNationalId());
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_096_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_096), e);
        }
        if (type.equals(CardRequestType.EXTEND) || type.equals(CardRequestType.REPLACE)) {
            try {
                registrationDelegator.checkCRN(up, fetchCitizenInfoWTO.getNationalId(), fetchCitizenInfoWTO.getCrn());
            } catch (BaseException e) {
                throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
            } catch (Exception e) {
                throw new InternalException(WebExceptionCode.RSW_097_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_097), e);
            }
        }

        try {
            citizenTO = registrationDelegator.loadCitizen(up, "", "", fetchCitizenInfoWTO.getNationalId());
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_061_MSG + fetchCitizenInfoWTO.getNationalId(),
                    new EMSWebServiceFault(WebExceptionCode.RSW_061), e);
        }

        if (citizenTO == null) {
            throw new InternalException(WebExceptionCode.RSW_062_MSG + fetchCitizenInfoWTO.getNationalId(),
                    new EMSWebServiceFault(WebExceptionCode.RSW_093));
        }

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            birthDate = simpleDateFormat.parse(citizenTO.getCitizenInfo().getBirthDateSolar());
            ctzBirthDate = simpleDateFormat.parse(fetchCitizenInfoWTO.getBirthDate());
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_098_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_098), e);
        }
        if (!(ctzBirthDate.equals(birthDate))) {
            throw new InternalException(WebExceptionCode.RSW_090_MSG + fetchCitizenInfoWTO.getBirthDate(),
                    new EMSWebServiceFault(WebExceptionCode.RSW_090));
        }
        try {
            citizenWTO = CardRequestMapper.convert(null, citizenTO);
            if (type.equals(CardRequestType.EXTEND) || type.equals(CardRequestType.REPLACE) || type.equals(CardRequestType.REPLICA)) {
                citizenWTO.setLivingPrvId(0L);
                citizenWTO.setLivingCityId(0L);
                citizenWTO.setLivingStateId(0L);
                citizenWTO.setLivingSectorId(0L);
                citizenWTO.setLivingVillageId(0L);
                citizenWTO.setAddress("");
                citizenWTO.setPhone("");
                citizenWTO.setPostCode("");
                citizenWTO.setUserCityType("-1");
            }
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_091_MSG,
                    new EMSWebServiceFault(WebExceptionCode.RSW_091), e);
        }

        citizenWTO.setId(null);
        return citizenWTO;
    }

    /**
     * Transfers MES registration to EMS db
     *
     * @param securityContextWTO The login and session information of the user
     * @param mesRequestWTO      Basic information of citizen
     *                           Collection of encrypted fingerprint data
     *                           Collection of face information
     *                           Collection of scanned documents
     *                           The signature of data that is generated using the sign token of the enrollment office
     *                           manager before sending to EMS
     * @throws InternalException
     */
    @WebMethod
    public void submitMESRequest(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                 @WebParam(name = "mesRequestWTO") MESRequestWTO mesRequestWTO) throws InternalException {
        UserProfileTO up = super.validateRequest(securityContextWTO);
        CompleteRegistrationDelegator delegator = new CompleteRegistrationDelegator();

        CardRequestTO cardRequestTO = convertToCardRequestTO(up, mesRequestWTO.getCitizenWTO());
        ArrayList<BiometricTO> fingers = convertToFingersTO(mesRequestWTO.getFingersWTO());
        ArrayList<BiometricTO> faces = convertToFacesTO(mesRequestWTO.getFacesWTO());
        ArrayList<DocumentTO> documents = convertToDocumentsTO(mesRequestWTO.getDocumentsWTO());

        try {
//            delegator.register(up, cardRequestTO, fingers, faces, documents, signature);
            delegator.register(up, cardRequestTO, fingers, faces, documents,
                    mesRequestWTO.getSignature(), mesRequestWTO.getFeatureExtractorIdNormal(),
                    mesRequestWTO.getFeatureExtractorIdCC(), mesRequestWTO.getFaceDisabilityStatus());
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode(), e.getArgs()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_074_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_074), e);
        }
    }

    /**
     * Given a {@link com.gam.nocr.ems.data.domain.ws.CitizenWTO} object and copies its properties to an instance of
     * {@link com.gam.nocr.ems.data.domain.CardRequestTO}. Its main use is in transfer process for information
     * registered by MES
     *
     * @param up         The user profile for current user
     * @param citizenWTO Source object that should be converted
     * @return Converted data
     * @throws InternalException
     */

    private CardRequestTO convertToCardRequestTO(UserProfileTO up, CitizenWTO citizenWTO) throws InternalException {
        CardRequestTO cardRequestTO;

        //  Replacing some empty input values with their corresponding default values in order to make the convert process
        //  possible
        initCitizenDefaultValues(citizenWTO);

        try {
            cardRequestTO = CardRequestMapper.convert(citizenWTO);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_065_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_065), e);
        }

        if (citizenWTO.getRequestId() != null) {
            throw new InternalException(WebExceptionCode.RSW_066_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_066));
        }
        if (cardRequestTO.getEnrolledDate() == null) {
            cardRequestTO.setEnrolledDate(new Date());
        }
        cardRequestTO.setState(CardRequestState.APPROVED_BY_MES);
        cardRequestTO.setEnrollmentOffice(new EnrollmentOfficeTO(up.getDepID()));
        return cardRequestTO;
    }

    /**
     * Given an instance of {@link CitizenWTO} and initializes its non mandatory fields to their corresponding default
     * values. This would be used when transferring MES registered requests to EMS. They may not have valid values for
     * some empty input fields
     *
     * @param citizenWTO An object to initialize its properties
     * @throws InternalException
     */
    private void initCitizenDefaultValues(CitizenWTO citizenWTO) throws InternalException {
        //  Citizen
        citizenWTO.setFirstNameFA(ConstValues.DEFAULT_NAMES_FA);
        citizenWTO.setFirstNameEN(ConstValues.DEFAULT_NAMES_EN);
        citizenWTO.setSureNameFA(ConstValues.DEFAULT_NAMES_FA);
        citizenWTO.setSureNameEN(ConstValues.DEFAULT_NAMES_EN);
        citizenWTO.setBirthCertId(ConstValues.DEFAULT_NUMBER);
        citizenWTO.setBirthCertSerial(ConstValues.DEFAULT_CERT_SERIAL);

        //  Father
        citizenWTO.setFatherFirstNameEN(ConstValues.DEFAULT_NAMES_EN);
        citizenWTO.setFatherFatherName(ConstValues.DEFAULT_NAMES_FA);
        citizenWTO.setFatherSureName(ConstValues.DEFAULT_NAMES_FA);
        citizenWTO.setFatherBirthCertSeries(ConstValues.DEFAULT_CERT_SERIAL);

        try {
            citizenWTO.setFatherBirthDate(new Timestamp(DateUtil.convert(ConstValues.DEFAULT_DATE, DateUtil.JALALI).getTime()));
        } catch (DateFormatException e) {
            throw new InternalException(WebExceptionCode.RSW_065_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_078), e);
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

        //  Mother
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
            citizenWTO.setMotherBirthDate(new Timestamp(DateUtil.convert(ConstValues.DEFAULT_DATE, DateUtil.JALALI).getTime()));
        } catch (DateFormatException e) {
            throw new InternalException(WebExceptionCode.RSW_065_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_079), e);
        }

        //  Spouses
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

        //  Children
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

    /**
     * Given a collection of encrypted fingerprint data, decrypt them and convert them to a collection of
     * {@link com.gam.nocr.ems.data.domain.BiometricTO} object to be saved in database. Its main use is in transferring
     * MES registration
     *
     * @param fingersWTO Collection of encrypted fingerprint data
     * @return Collection of decrypted fingerprint data, converted to an appropriate objects to be saved
     * @throws InternalException
     */
    private ArrayList<BiometricTO> convertToFingersTO(BiometricWTO[] fingersWTO) throws InternalException {
        ArrayList<BiometricTO> fingers = new ArrayList<BiometricTO>();
        if (fingersWTO != null) {
            BiometricTO bio;
            for (BiometricWTO biometricWTO : fingersWTO) {
                try {
                    decryptFingerData(biometricWTO);
                    bio = BiometricMapper.convert(biometricWTO);
                } catch (BaseException e) {
                    throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
                } catch (Exception e) {
                    throw new InternalException(WebExceptionCode.RSW_067_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_067));
                }

                if (bio.getType() == null) {
                    throw new InternalException(WebExceptionCode.RSW_068_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_068));
                }
                if (bio.getType() == BiometricType.FACE_CHIP || bio.getType() == BiometricType.FACE_IMS || bio.getType() == BiometricType.FACE_LASER || bio.getType() == BiometricType.FACE_MLI) {
                    throw new InternalException(WebExceptionCode.RSW_069_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_069));
                }
                fingers.add(bio);
            }
        }
        return fingers;
    }

    /**
     * Given a collection face data and convert them to a collection of {@link com.gam.nocr.ems.data.domain.BiometricTO}
     * object to be saved in database. Its main use is in transferring MES registration
     *
     * @param facesWTO Collection of face data
     * @return Collection of face data, converted to an appropriate objects to be saved
     * @throws InternalException
     */
    private ArrayList<BiometricTO> convertToFacesTO(BiometricWTO[] facesWTO) throws InternalException {
        ArrayList<BiometricTO> faces = new ArrayList<BiometricTO>();
        if (facesWTO != null) {
            BiometricTO bio;
            for (BiometricWTO biometricWTO : facesWTO) {
                try {
                    bio = BiometricMapper.convert(biometricWTO);
                } catch (BaseException e) {
                    throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
                } catch (Exception e) {
                    throw new InternalException(WebExceptionCode.RSW_070_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_070), e);
                }
                if (bio.getType() == null) {
                    throw new InternalException(WebExceptionCode.RSW_071_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_071));
                }
                if (bio.getType() == BiometricType.FING_ALL || bio.getType() == BiometricType.FING_CANDIDATE || bio.getType() == BiometricType.FING_MIN_1 || bio.getType() == BiometricType.FING_MIN_2
                        || bio.getType() == BiometricType.FING_NORMAL_1 || bio.getType() == BiometricType.FING_NORMAL_2) {
                    throw new InternalException(WebExceptionCode.RSW_072_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_072));
                }
                faces.add(bio);
            }
        }
        return faces;
    }

    /**
     * Given a collection scanned documents data and convert them to a collection of
     * {@link com.gam.nocr.ems.data.domain.DocumentTO} object to be saved in database. Its main use is in transferring
     * MES registration
     *
     * @param documentsWTO Collection of scanned documents
     * @return Collection of scanned documents, converted to an appropriate objects to be saved
     * @throws InternalException
     */
    private ArrayList<DocumentTO> convertToDocumentsTO(DocumentWTO[] documentsWTO) throws InternalException {
        ArrayList<DocumentTO> documents = new ArrayList<DocumentTO>();
        if (documentsWTO != null) {
            DocumentTO doc;
            for (DocumentWTO wto : documentsWTO) {
                try {
                    doc = DocumentMapper.convert(wto);
                } catch (BaseException e) {
                    throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
                } catch (Exception e) {
                    throw new InternalException(WebExceptionCode.RSW_073_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_073), e);
                }
                documents.add(doc);
            }
        }
        return documents;
    }

    @WebMethod
    public PhotoVipWTO getPhotoVip(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "cardRquestId") Long cardRquestId)
            throws InternalException {

        UserProfileTO up = super.validateRequest(securityContextWTO);
        PhotoVipWTO photoVipWTO = null;

        try {
            photoVipWTO = registrationDelegator.getPhotoVip(up,
                    cardRquestId);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(
                    e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RVW_017_MSG, new EMSWebServiceFault(
                    WebExceptionCode.RVW_017), e);
        }

        return photoVipWTO;
    }

    @WebMethod
    public void updateVipRegistrationRequest(@WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
                                             @WebParam(name = "citizenWTO") CitizenWTO citizenWTO) throws InternalException {

        UserProfileTO up = super.validateRequest(securityContextWTO);
        CardRequestTO cardRequestTO;
        try {
            cardRequestTO = CardRequestMapper.convert(citizenWTO);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_006_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_081), e);
        }
        if (citizenWTO.getRequestId() == null) {
            throw new InternalException(WebExceptionCode.RSW_005_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_082));
        }
        try {
            registrationDelegator.updateVipRegistrationRequest(up, cardRequestTO);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_008_MSG, new EMSWebServiceFault(WebExceptionCode.RSW_083), e);
        }
    }


    //Anbari
    @WebMethod
    public String saveOfficeVIPRegistrationRequest(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "citizenWTO") CitizenWTO citizenWTO)
            throws InternalException {

        UserProfileTO up = super.validateRequest(securityContextWTO);
        String savedTrackingId = null;

        CardRequestTO cardRequestTO;

        // convert wto to CardRequestTO
        try {
            cardRequestTO = CardRequestMapper.convertVIPRequest(citizenWTO);

        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(
                    e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_001_MSG,
                    new EMSWebServiceFault(WebExceptionCode.RSW_085), e);
        }

        try {
            List<CitizenTO> citizenList = registrationDelegator.findByNID(up, citizenWTO.getNationalId());
            if (!EmsUtil.checkListSize(citizenList)) {
                savedTrackingId = registrationDelegator.saveVIPpreRegistrationAndDoEstelam(up, cardRequestTO);
            } else
                throw new ServiceException(BizExceptionCode.MMS_063,
                        BizExceptionCode.MMS_063_MSG);
            return savedTrackingId;
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(
                    e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RSW_004_MSG,
                    new EMSWebServiceFault(WebExceptionCode.RSW_084), e);
        }
    }

    //Anbari : Method to fetch IMS Estelam Image
    @WebMethod
    public EstelamImsImageWTO getImsEstelamImage(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "nationalID") String nationalID)
            throws InternalException {

        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        EstelamImsImageWTO imsEstelamImageWTO = null;
        try {
            imsEstelamImageWTO = registrationDelegator.getImsEstelamImage(userProfileTO, nationalID);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(
                    e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RVW_017_MSG, new EMSWebServiceFault(
                    WebExceptionCode.RVW_017), e);
        }

        return imsEstelamImageWTO;
    }


    //Anbari : Method to fetch citizen birth date to calculate elderly mode
    @WebMethod
    public CitizenBirthDateAndGenderWTO getBirthDateAndGenderByRequestId(
            @WebParam(name = "securityContextWTO") SecurityContextWTO securityContextWTO,
            @WebParam(name = "requestId") Long requestId)
            throws InternalException {

        UserProfileTO userProfileTO = super.validateRequest(securityContextWTO);
        CitizenBirthDateAndGenderWTO birthDateAndGender;
        try {
            birthDateAndGender = registrationDelegator.getBirthDateAndGenderByRequestId(userProfileTO, requestId);
        } catch (BaseException e) {
            throw new InternalException(e.getMessage(), new EMSWebServiceFault(
                    e.getExceptionCode()), e);
        } catch (Exception e) {
            throw new InternalException(WebExceptionCode.RVW_022_MSG, new EMSWebServiceFault(
                    WebExceptionCode.RVW_023), e);
        }

        return birthDateAndGender;
    }


}
