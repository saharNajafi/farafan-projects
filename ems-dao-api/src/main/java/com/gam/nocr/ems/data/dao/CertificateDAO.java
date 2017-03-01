package com.gam.nocr.ems.data.dao;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.CertificateTO;
import com.gam.nocr.ems.data.enums.CertificateUsage;

/**
 * The interface CertificateDAO is used to handle all the DAO activities which are depended on Certificates
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface CertificateDAO extends EmsBaseDAO<CertificateTO> {

    /**
     * The method findCertificateByUsage is used to find a certificate by a specified usage of type {@link
     * CertificateUsage}
     *
     * @param certificateUsage represents a specified type of {@link CertificateUsage}
     * @return an instance of type {@link CertificateTO}
     */
    public CertificateTO findCertificateByUsage(CertificateUsage certificateUsage) throws BaseException;

}
