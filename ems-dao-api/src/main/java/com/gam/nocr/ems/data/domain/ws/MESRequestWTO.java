package com.gam.nocr.ems.data.domain.ws;

import java.io.Serializable;

public class MESRequestWTO implements Serializable {

    private CitizenWTO citizenWTO;
    private BiometricWTO[] fingersWTO;
    private BiometricWTO[] facesWTO;
    private String featureExtractorIdNormal;
    private String featureExtractorIdCC;
    private DocumentWTO[] documentsWTO;
    private byte[] signature;
    private Integer faceDisabilityStatus;

    public CitizenWTO getCitizenWTO() {
        return citizenWTO;
    }

    public void setCitizenWTO(CitizenWTO citizenWTO) {
        this.citizenWTO = citizenWTO;
    }

    public BiometricWTO[] getFingersWTO() {
        return fingersWTO;
    }

    public void setFingersWTO(BiometricWTO[] fingersWTO) {
        this.fingersWTO = fingersWTO;
    }

    public BiometricWTO[] getFacesWTO() {
        return facesWTO;
    }

    public void setFacesWTO(BiometricWTO[] facesWTO) {
        this.facesWTO = facesWTO;
    }

    public String getFeatureExtractorIdNormal() {
        return featureExtractorIdNormal;
    }

    public void setFeatureExtractorIdNormal(String featureExtractorIdNormal) {
        this.featureExtractorIdNormal = featureExtractorIdNormal;
    }

    public String getFeatureExtractorIdCC() {
        return featureExtractorIdCC;
    }

    public void setFeatureExtractorIdCC(String featureExtractorIdCC) {
        this.featureExtractorIdCC = featureExtractorIdCC;
    }

    public DocumentWTO[] getDocumentsWTO() {
        return documentsWTO;
    }

    public void setDocumentsWTO(DocumentWTO[] documentsWTO) {
        this.documentsWTO = documentsWTO;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public Integer getFaceDisabilityStatus() {
        return faceDisabilityStatus;
    }

    public void setFaceDisabilityStatus(Integer faceDisabilityStatus) {
        this.faceDisabilityStatus = faceDisabilityStatus;
    }
}
