package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.AFISState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class IMSUpdateResultVTO extends ExtEntityTO {

    private String requestID;
    private Integer identityChanged;
    private AFISState afisState;
    private String errorCode;
    private String errorMessage;
    private String nationalId;
    private byte[] faceIMS; 
    private byte[] faceLASER;
    private byte[] faceCHIP ;
    private byte[] faceMLI ;
    private List<IMSErrorInfo> errorCodes= new ArrayList<IMSErrorInfo>();


    public byte[] getFaceIMS() {
		return faceIMS;
	}

	public void setFaceIMS(byte[] faceIMS) {
		this.faceIMS = faceIMS;
	}

	public byte[] getFaceLASER() {
		return faceLASER;
	}

	public void setFaceLASER(byte[] faceLASER) {
		this.faceLASER = faceLASER;
	}

	public byte[] getFaceCHIP() {
		return faceCHIP;
	}

	public void setFaceCHIP(byte[] faceCHIP) {
		this.faceCHIP = faceCHIP;
	}

	public byte[] getFaceMLI() {
		return faceMLI;
	}

	public void setFaceMLI(byte[] faceMLI) {
		this.faceMLI = faceMLI;
	}

	public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public Integer getIdentityChanged() {
        return identityChanged;
    }

    public void setIdentityChanged(Integer identityChanged) {
        this.identityChanged = identityChanged;
    }

    public AFISState getAfisState() {
        return afisState;
    }

    public void setAfisState(AFISState afisState) {
        this.afisState = afisState;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public List<IMSErrorInfo> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<IMSErrorInfo> errorCodes) {
        this.errorCodes = errorCodes;
    }
}
