package com.gam.nocr.ems.biz.service;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.Service;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.vol.BirthCertIssPlaceVTO;
import com.gam.nocr.ems.data.domain.vol.RegistrationVIPVTO;
import com.gam.nocr.ems.data.domain.ws.CitizenBirthDateAndGenderWTO;
import com.gam.nocr.ems.data.domain.ws.DocumentTypeWTO;
import com.gam.nocr.ems.data.domain.ws.EstelamImsImageWTO;
import com.gam.nocr.ems.data.domain.ws.TrackingNumberWTO;
import com.gam.nocr.ems.data.domain.ws.PhotoVipWTO;
import com.gam.nocr.ems.data.enums.BiometricType;
import com.gam.nocr.ems.data.enums.CardRequestState;
import com.gam.nocr.ems.data.enums.CertificateUsage;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author : Soheil Toodeh Fallah (fallah@gamelectronics.com)
 * @author Haeri (haeri@gamelectronics.com)
 */
public interface RegistrationService extends Service {

    public Long saveFromCcos(CardRequestTO requestTO) throws BaseException;

    public Long saveFromMES(CardRequestTO requestTO) throws BaseException;

    public Long updateFromCcos(CardRequestTO requestTO) throws BaseException;

    public CardRequestTO load(Long requestId) throws BaseException;

    public CitizenTO loadCitizen(long requestId) throws BaseException;

    public CitizenTO loadCitizen(String firstName, String lastName, String nationalId) throws BaseException;

    public boolean remove(long requestId) throws BaseException;

    public void addFingerData(long requestId, ArrayList<BiometricTO> biometricDatas) throws BaseException;

    public void addFingerDataFromMES(long requestId, ArrayList<BiometricTO> biometricDatas) throws BaseException;

    public void addFaceData(long requestId, ArrayList<BiometricTO> biometricDatas) throws BaseException;

    public void addFaceDataFromMES(long requestId, ArrayList<BiometricTO> biometricDatas) throws BaseException;

    public void addDocument(long requestId, ArrayList<DocumentTO> documents) throws BaseException;

    public void addDocumentFromMES(long requestId, ArrayList<DocumentTO> documents) throws BaseException;

    public boolean removeFingerData(long requestId, BiometricType bioType) throws BaseException;
    
    //Adldoost
    public boolean removeFingerAllTypeData(long requestId) throws BaseException;

    public void removeFingerDataBySystem(Long citizenId) throws BaseException;

    public boolean removeFaceData(long requestId, BiometricType bioType) throws BaseException;
    
    //Adldoost
    public boolean removeFaceAllTypeData(long requestId) throws BaseException;

    public void removeFaceDataBySystem(Long citizenId) throws BaseException;

    public boolean removeDocument(Long requestId, List<DocumentTypeWTO> documentTypeWTOs) throws BaseException;

    public boolean removeDocumentBySystem(Long requestId, List<DocumentTypeTO> documentTypeTOs) throws BaseException;

    public List<BiometricTO> getFingerData(long requestId, BiometricType bioType) throws BaseException;

    public List<BiometricTO> getFaceData(long requestId, BiometricType bioType) throws BaseException;

    public List<DocumentTO> getDocument(long requestId, DocumentTypeTO docType) throws BaseException;

    public List<DocumentTypeTO> getServiceDocuments(Integer serviceType, Long cardRequestID) throws BaseException;

    public void authenticateDocument(long requestId) throws BaseException;

    public void authenticateDocumentFromMES(long requestId) throws BaseException;

    public void approveRequest(long requestId, byte[] envelope ,UserProfileTO userProfileTO) throws BaseException;

    public void approveRequestFromMES(long requestId, byte[] digest ,UserProfileTO userProfileTO) throws BaseException;

    /**
     * The method fetchPortalCardRequestsToSave is used to fetch the requests from the sub system 'Portal' and save them
     *
     * @throws BaseException
     */
    public Boolean fetchPortalCardRequestsToSave(List<Long> portalCardRequestIds) throws BaseException;

    public List<Long> fetchPortalCardRequestIdsForTransfer() throws BaseException;

    public CertificateTO getCertificateByUsage(CertificateUsage ccosCer) throws BaseException;

    // Methods for EncryptedCardRequest
    public Long saveEncryptedCardRequest(EncryptedCardRequestTO to) throws BaseException;

    /**
     * @param ids
     * @return
     * @throws BaseException
     * @author Sina Golesorkhi
     */
    public EncryptedCardRequestTO getEncryptedCardRequestTO(String ids) throws BaseException;

    public HashMap<Enum<CardRequestState>, List<CardRequestState>> registrationStatePolicyForUpdate
            (CardRequestState cardRequestState) throws BaseException;

    public void idleR2DCardRequest() throws BaseException;

    public TrackingNumberWTO retrieveTrackingNumber(String nationalId, Date birthDate) throws BaseException;

    /**
     * The method fetchBirthCertIssPlace is used to fetch the birth city that associated to a specified nationalId
     *
     * @param nationalId is an instance of type {@link String}, which represents the nationalId
     * @return
     * @throws BaseException
     */
    List<BirthCertIssPlaceVTO> fetchBirthCertIssPlace(String nationalId) throws BaseException;

	public Long saveReservationCardRequest(
			CardRequestTO portalCardRequest) throws BaseException;

	public List<DocumentTypeTO> getServiceVipDocuments(Integer serviceType) throws BaseException ;

	void checkPreviousCardRequestNotStopped(CitizenTO citizenTo)
			throws BaseException;

	public Boolean saveFromVip(CardRequestTO requestTO,
			ArrayList<BiometricTO> fingers, ArrayList<BiometricTO> faces,
			ArrayList<DocumentTO> documents) throws BaseException;

	public PhotoVipWTO getPhotoVip(Long cardRquestId) throws BaseException;

	public Long updateVipRegistrationRequestFromCccos(
			CardRequestTO cardRequestTO) throws BaseException;

	
	//Anbari
	String saveVIPpreRegistrationAndDoEstelam(CardRequestTO cardRequestTO) throws BaseException;
		
	//Anbari
	RegistrationVIPVTO savePreRegistrationVIP(CardRequestTO cardRequestTO , UserProfileTO userProfile) throws BaseException;
		
	//Anbari
	List<CitizenTO> findByNID(String nid) throws BaseException;
	
	//Anbari:Estelam
	EstelamImsImageWTO getImsEstelamImage(String nationalID) throws BaseException;

	//Anbari : get BirthDate for elderly mode calculation
	CitizenBirthDateAndGenderWTO getBirthDateAndGenderByRequestId(Long requestId) throws BaseException;


}
