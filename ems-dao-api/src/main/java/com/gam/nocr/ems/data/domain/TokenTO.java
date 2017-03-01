package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.TokenReIssuanceReason;
import com.gam.nocr.ems.data.enums.TokenState;
import com.gam.nocr.ems.util.EmsUtil;

import java.util.Date;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class TokenTO extends ExtEntityTO {

    private String requestID;
    private String AKI;
    private String certificateSerialNumber;
    private TokenState state;
    private Date requestDate;
    private Date issuanceDate;
    private TokenReIssuanceReason reason;

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getAKI() {
        return AKI;
    }

    public void setAKI(String AKI) {
        this.AKI = AKI;
    }

    public String getCertificateSerialNumber() {
        return certificateSerialNumber;
    }

    public void setCertificateSerialNumber(String certificateSerialNumber) {
        this.certificateSerialNumber = certificateSerialNumber;
    }

    public TokenState getState() {
        return state;
    }

    public void setState(TokenState state) {
        this.state = state;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(Date issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public TokenReIssuanceReason getReason() {
        return reason;
    }

    public void setReason(TokenReIssuanceReason reason) {
        this.reason = reason;
    }

    /**
     * Returns a string representation of the object. In general, the <code>toString</code> method returns a string that
     * "textually represents" this object. The result should be a concise but informative representation that is easy for a
     * person to read. It is recommended that all subclasses override this method.
     * <p/>
     * The <code>toString</code> method for class <code>Object</code> returns a string consisting of the name of the class
     * of which the object is an instance, the at-sign character `<code>@</code>', and the unsigned hexadecimal
     * representation of the hash code of the object. In other words, this method returns a string equal to the value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
