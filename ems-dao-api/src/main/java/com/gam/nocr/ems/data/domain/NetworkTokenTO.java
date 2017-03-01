package com.gam.nocr.ems.data.domain;

import com.gam.nocr.ems.data.enums.TokenState;
import com.gam.nocr.ems.util.JSONable;
import flexjson.JSON;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_NETWORK_TOKEN")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_NETWORK_TOKEN", allocationSize = 1)
public class NetworkTokenTO extends TokenTO implements JSONable {

    private EnrollmentOfficeTO enrollmentOffice;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "NTK_ID")
    public Long getId() {
        return super.getId();
    }

    @Override
    @Column(name = "NTK_REQUEST_ID", length = 32)
    public String getRequestID() {
        return super.getRequestID();
    }

    @Override
    @Enumerated(EnumType.STRING)
    @Column(name = "NTK_STATE", nullable = false)
    public TokenState getState() {
        return super.getState();
    }

    @ManyToOne
    @JoinColumn(name = "NTK_ENROLL_OFFICE_ID", nullable = false)
    @JSON(include = false)
    public EnrollmentOfficeTO getEnrollmentOffice() {
        return enrollmentOffice;
    }

    public void setEnrollmentOffice(EnrollmentOfficeTO enrollmentOffice) {
        this.enrollmentOffice = enrollmentOffice;
    }

    @Override
    @Column(name = "NTK_AKI", length = 40, nullable = true)
    public String getAKI() {
        return super.getAKI();
    }

    @Override
    @Column(name = "NTK_CERT_SN", length = 40, nullable = true)
    public String getCertificateSerialNumber() {
        return super.getCertificateSerialNumber();
    }

    @Override
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NTK_REQUEST_DATE", nullable = false)
    public Date getRequestDate() {
        return super.getRequestDate();
    }

    @Override
    @Column(name = "NTK_ISSUANCE_DATE", nullable = true)
    public Date getIssuanceDate() {
        return super.getIssuanceDate();
    }

    @Override
    public String toString() {
        return toJSON();
    }

    /**
     * The method toJSON is used to convert an object to an instance of type {@link String}
     *
     * @return an instance of type {@link String}
     */
    @Override
    public String toJSON() {
        String jsonObject = super.toString();
        jsonObject = jsonObject.substring(0, jsonObject.length() - 1);
        if (enrollmentOffice == null) {
            jsonObject += ", enrollmentOfficeId:" + enrollmentOffice;
        } else {
            jsonObject += ", enrollmentOfficeId:" + enrollmentOffice.getId();
        }
        jsonObject += "}";
        return jsonObject;
    }
}
