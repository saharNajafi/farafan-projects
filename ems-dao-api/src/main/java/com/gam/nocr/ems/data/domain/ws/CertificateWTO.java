package com.gam.nocr.ems.data.domain.ws;

import java.io.Serializable;

import com.gam.nocr.ems.data.enums.CertificateUsage;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

public class CertificateWTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] certificate;
    private CertificateUsage usage;
    private String code;   
    private Long id;  

    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getCertificate() {
		return certificate;
	}

	public void setCertificate(byte[] certificate) {
        this.certificate = certificate;
    }

    public CertificateUsage getUsage() {
        return usage;
    }

    public void setUsage(CertificateUsage usage) {
        this.usage = usage;
    }

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
