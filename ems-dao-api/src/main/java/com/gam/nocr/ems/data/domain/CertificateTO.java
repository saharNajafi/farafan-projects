package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.CertificateUsage;
import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

import javax.persistence.*;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Entity
@Table(name = "EMST_CERT")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_CERT", allocationSize = 1)
public class CertificateTO extends ExtEntityTO {

    private byte[] certificate;
    private CertificateUsage usage;
    private String code;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "CRT_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "CRT_CERT", nullable = false)
    @JSON(include = false)
    public byte[] getCertificate() {
        return certificate == null ? certificate : certificate.clone();
    }

    public void setCertificate(byte[] certificate) {
        this.certificate = certificate;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CRT_USAGE", nullable = false)
    public CertificateUsage getUsage() {
        return usage;
    }

    public void setUsage(CertificateUsage usage) {
        this.usage = usage;
    }

    @Column(name = "CRT_CODE", length = 20, nullable = false)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
