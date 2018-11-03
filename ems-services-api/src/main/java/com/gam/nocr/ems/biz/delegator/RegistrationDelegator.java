package com.gam.nocr.ems.biz.delegator;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.delegator.Delegator;
import com.gam.commons.core.biz.delegator.DelegatorException;
import com.gam.commons.core.biz.service.factory.ServiceFactory;
import com.gam.commons.core.biz.service.factory.ServiceFactoryException;
import com.gam.commons.core.biz.service.factory.ServiceFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.biz.service.RegistrationService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.domain.*;
import com.gam.nocr.ems.data.domain.ws.CitizenBirthDateAndGenderWTO;
import com.gam.nocr.ems.data.domain.ws.DocumentTypeWTO;
import com.gam.nocr.ems.data.domain.ws.EstelamImsImageWTO;
import com.gam.nocr.ems.data.domain.ws.TrackingNumberWTO;
import com.gam.nocr.ems.data.domain.ws.PhotoVipWTO;
import com.gam.nocr.ems.data.enums.BiometricType;
import com.gam.nocr.ems.data.enums.CertificateUsage;
import com.gam.nocr.ems.util.EmsUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class RegistrationDelegator implements Delegator {

    private RegistrationService getService(UserProfileTO userProfileTO) throws BaseException {
        ServiceFactory factory = ServiceFactoryProvider.getServiceFactory();
        RegistrationService registrationService = null;
        try {
            registrationService = (RegistrationService) factory.getService(EMSLogicalNames
                    .getServiceJNDIName(EMSLogicalNames.SRV_REGISTRATION), EmsUtil.getUserInfo(userProfileTO));
        } catch (ServiceFactoryException e) {
            throw new DelegatorException(BizExceptionCode.RDL_001, BizExceptionCode.GLB_002_MSG, e,
                    EMSLogicalNames.SRV_REGISTRATION.split(","));
        }
        registrationService.setUserProfileTO(userProfileTO);
        return registrationService;
    }

    public long save(UserProfileTO userProfileTO, CardRequestTO requestTO) throws BaseException {
        return getService(userProfileTO).saveFromCcos(requestTO);
    }

    public long update(UserProfileTO userProfileTO, CardRequestTO requestTO) throws BaseException {
        return getService(userProfileTO).updateFromCcos(requestTO);
    }

    public CardRequestTO load(UserProfileTO userProfileTO, long requestId) throws BaseException {
        return getService(userProfileTO).load(requestId);
    }

    public CitizenTO loadCitizen(UserProfileTO userProfileTO, long requestId) throws BaseException {
        return getService(userProfileTO).loadCitizen(requestId);
    }

    public CitizenTO loadCitizen(UserProfileTO userProfileTO, String firstName, String lastName,
                                 String nationalId) throws BaseException {
        return getService(userProfileTO).loadCitizen(firstName, lastName, nationalId);
    }

    public boolean remove(UserProfileTO userProfileTO, long requestId) throws BaseException {
        return getService(userProfileTO).remove(requestId);
    }

    public void addFingerData(UserProfileTO userProfileTO, long requestId,
                              ArrayList<BiometricTO> biometricDatas , String featureExtractorIdNormal,String featureExtractorIdCC) throws BaseException {
        getService(userProfileTO).addFingerData(requestId, biometricDatas, featureExtractorIdNormal,featureExtractorIdCC);
//      getService(userProfileTO).addFingerData(requestId, biometricDatas);
    }

    public void addFaceData(UserProfileTO userProfileTO, long requestId,
                            ArrayList<BiometricTO> biometricDatas) throws BaseException {
        getService(userProfileTO).addFaceData(requestId, biometricDatas);
    }

    public void addDocument(UserProfileTO userProfileTO, long requestId,
                            ArrayList<DocumentTO> documents) throws BaseException {
        getService(userProfileTO).addDocument(requestId, documents);
    }

    public boolean removeFingerData(UserProfileTO userProfileTO, long requestId,
                                    BiometricType bioType) throws BaseException {
        return getService(userProfileTO).removeFingerData(requestId, bioType);
    }
    
    //Adldoost
    public boolean removeFingerAllTypeData(UserProfileTO userProfileTO, long requestId) throws BaseException {
    	return getService(userProfileTO).removeFingerAllTypeData(requestId);
	}

    public boolean removeFaceData(UserProfileTO userProfileTO, long requestId,
                                  BiometricType bioType) throws BaseException {
        return getService(userProfileTO).removeFaceData(requestId, bioType);
    }
    //Adldoost
    public boolean removeFaceAllTypeData(UserProfileTO userProfileTO, long requestId) throws BaseException {
    	return getService(userProfileTO).removeFaceAllTypeData(requestId);
	}

    public boolean removeDocument(UserProfileTO userProfileTO, long requestId,
                                  List<DocumentTypeWTO> documentTypeWTOs) throws BaseException {
        return getService(userProfileTO).removeDocument(requestId, documentTypeWTOs);
    }

    /**
     * This method return biometric data for specific request id.
     *
     * @param requestId
     * @param bioType   : specify the type of biometric data. If null passed all of
     *                  the biometric data for specified request id will be retrieved.
     * @return
     */
    public List<BiometricTO> getFingerData(UserProfileTO userProfileTO, long requestId,
                                           BiometricType bioType) throws BaseException {
        return getService(userProfileTO).getFingerData(requestId, bioType);
    }

    public List<BiometricTO> getFaceData(UserProfileTO userProfileTO, long requestId,
                                         BiometricType bioType) throws BaseException {
        return getService(userProfileTO).getFaceData(requestId, bioType);
    }

    public List<DocumentTO> getDocument(UserProfileTO userProfileTO, long requestId,
                                        DocumentTypeTO docType) throws BaseException {
        return getService(userProfileTO).getDocument(requestId, docType);
    }

    public List<DocumentTypeTO> getServiceDocuments(UserProfileTO userProfileTO, Integer serviceType, Long cardRequestID)
            throws BaseException {
        return getService(userProfileTO).getServiceDocuments(serviceType, cardRequestID);
    }

    public void authenticateDocument(UserProfileTO userProfileTO, long requestId)
            throws BaseException {
        getService(userProfileTO).authenticateDocument(requestId);
    }

    public void approveRequest(UserProfileTO userProfileTO, long requestId, byte[] envelope)
            throws BaseException {
        getService(userProfileTO).approveRequest(requestId, envelope , userProfileTO);
    }

    public void saveEncryptedCardRequest(UserProfileTO userProfileTO, EncryptedCardRequestTO to) throws BaseException {
        getService(userProfileTO).saveEncryptedCardRequest(to);
    }

    /**
     * The method fetchPortalCardRequestsToSave is used to fetch the requests
     * from the sub system 'Portal' and save them. This method is called by a
     * specified job
     *
     * @throws BaseException
     */
    public Boolean fetchPortalCardRequestsToSave(List<Long> portalCardRequestIds) throws BaseException {
        return getService(null).fetchPortalCardRequestsToSave(portalCardRequestIds);
    }

    public List<Long> fetchPortalCardRequestIdsForTransfer() throws BaseException {
        return getService(null).fetchPortalCardRequestIdsForTransfer();
    }

    /**
     * @param ccosCer
     * @return
     * @throws BaseException
     * @author Sina Golesorkhi
     */
    public CertificateTO getCertificateByUsage(CertificateUsage ccosCer) throws BaseException {
        return getService(null).getCertificateByUsage(ccosCer);
    }

    /**
     * @param userProfileTO
     * @param ids
     * @return
     * @throws BaseException
     * @author Sina Golesorkhi
     */
    public EncryptedCardRequestTO getEncryptedCardRequest(UserProfileTO userProfileTO, String ids) throws BaseException {
        return getService(userProfileTO).getEncryptedCardRequestTO(ids);
    }

    public void IdleR2DCardRequest() throws BaseException {
        getService(null).idleR2DCardRequest();
    }

    public TrackingNumberWTO retrieveTrackingNumber(UserProfileTO userProfileTO, String nationalId, Date birthDate) throws BaseException {
        return getService(userProfileTO).retrieveTrackingNumber(nationalId, birthDate);
    }

	public List<DocumentTypeTO> getServiceVipDocuments(UserProfileTO up,
			Integer serviceType) throws BaseException {
        return getService(up).getServiceVipDocuments(serviceType);
	}
	
	public void checkPreviousCardRequestNotStopped(UserProfileTO up, CitizenTO citizenTo)
			throws BaseException{
		getService(up).checkPreviousCardRequestNotStopped(citizenTo);
	}
	
	public Boolean registerVip(UserProfileTO userProfileTO,
			CardRequestTO requestTO, ArrayList<BiometricTO> fingers,
			ArrayList<BiometricTO> faces, ArrayList<DocumentTO> documents, String featureExtractorIdNormal,String featureExtractorIdCC)
			throws BaseException {
		/*return getService(userProfileTO).saveFromVip(requestTO, fingers, faces,
				documents);*/
        return getService(userProfileTO).saveFromVip(requestTO, fingers, faces,
                documents, featureExtractorIdNormal,featureExtractorIdCC);
    }

	public PhotoVipWTO getPhotoVip(UserProfileTO up, Long cardRquestId) throws BaseException{
		return getService(up).getPhotoVip(cardRquestId);
	}

	public Long updateVipRegistrationRequest(UserProfileTO up,
			CardRequestTO cardRequestTO) throws BaseException{
		return getService(up).updateVipRegistrationRequestFromCccos(cardRequestTO);		
	}

	
	// Anbari
	public String saveVIPpreRegistrationAndDoEstelam(UserProfileTO userProfileTO,
			CardRequestTO cardRequestTO) throws BaseException {
		return getService(userProfileTO).saveVIPpreRegistrationAndDoEstelam(
				cardRequestTO);
	}

	// Anbari
	public List<CitizenTO> findByNID(UserProfileTO userProfileTO, String nid)
			throws BaseException {
		return getService(userProfileTO).findByNID(nid);
	}
	
	//Anbari:Estelam
	public EstelamImsImageWTO getImsEstelamImage(UserProfileTO userProfileTO,String nationalID) throws BaseException {
		return getService(userProfileTO).getImsEstelamImage(nationalID);
	}

	//Anbari : get BirthDate and Gender
	public CitizenBirthDateAndGenderWTO getBirthDateAndGenderByRequestId(UserProfileTO userProfileTO,Long requestId) throws BaseException {
		return getService(userProfileTO).getBirthDateAndGenderByRequestId(requestId);
	}


}