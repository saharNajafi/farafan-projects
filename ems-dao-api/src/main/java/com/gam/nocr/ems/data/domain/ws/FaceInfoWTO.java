package com.gam.nocr.ems.data.domain.ws;

import java.io.Serializable;

public class FaceInfoWTO implements Serializable {

    private Long requestId;
    private Integer faceDisabilityStatus;
    private  BiometricWTO[] biometricWTOs;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Integer getFaceDisabilityStatus() {
        return faceDisabilityStatus;
    }

    public void setFaceDisabilityStatus(Integer faceDisabilityStatus) {
        this.faceDisabilityStatus = faceDisabilityStatus;
    }

    public BiometricWTO[] getBiometricWTOs() {
        return biometricWTOs;
    }

    public void setBiometricWTOs(BiometricWTO[] biometricWTOs) {
        this.biometricWTOs = biometricWTOs;
    }
}
