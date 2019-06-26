package com.gam.nocr.ems.data.domain.ws;

import java.io.Serializable;

public class FingerInfoWTO implements Serializable {

    private Long requestId;
    private String featureExtractorIdNormal;
    private String featureExtractorIdCC;
    private BiometricWTO[] biometricWTOs;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
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

    public BiometricWTO[] getBiometricWTOs() {
        return biometricWTOs;
    }

    public void setBiometricWTOs(BiometricWTO[] biometricWTOs) {
        this.biometricWTOs = biometricWTOs;
    }
}
