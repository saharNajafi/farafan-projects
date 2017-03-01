package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.DataException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.gam.nocr.ems.data.domain.CertificateTO;
import com.gam.nocr.ems.data.domain.ws.CertificateWTO;
import com.gam.nocr.ems.data.enums.CertificateUsage;
import com.gam.nocr.ems.util.EmsUtil;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Cacheable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Stateless(name = "CertificateDAO")
@Local(CertificateDAOLocal.class)
public class CertificateDAOImpl extends EmsBaseDAOImpl<CertificateTO> implements CertificateDAOLocal {
	
	

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public CertificateTO create(CertificateTO certificateTO) throws BaseException {
        CertificateTO certTO = null;
        try {
            certTO = super.create(certificateTO);
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CRT_001, DataExceptionCode.GLB_004_MSG, e);
        }
        return certTO;
    }

    @Override
    public CertificateTO find(Class type, Object id) throws BaseException {
        CertificateTO certTO = null;
        try {
            certTO = super.find(type, id);
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CRT_002, DataExceptionCode.GLB_005_MSG, e);
        }
        return certTO;
    }

    @Override
    public CertificateTO update(CertificateTO certificateTO) throws BaseException {
        CertificateTO certTO = null;
        try {
            certTO = super.update(certificateTO);
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CRT_003, DataExceptionCode.GLB_006_MSG, e);
        }
        return certTO;
    }

    @Override
    public void delete(CertificateTO certificateTO) throws BaseException {
        try {
            super.delete(certificateTO);
        } catch (Exception e) {
            throw new DataException(DataExceptionCode.CRT_004, DataExceptionCode.GLB_007_MSG, e);
        }
    }

    /**
     * The method findCertificateByUsage is used to find a certificate by a specified usage of type {@link com.gam.nocr.ems.data.enums.CertificateUsage}
     *
     * @param certificateUsage represents a specified type of {@link com.gam.nocr.ems.data.enums.CertificateUsage}
     * @return an instance of type {@link com.gam.nocr.ems.data.domain.CertificateTO}
     */
    
    private static String TICKET_CACHE_NAME = "ticketCert";
    private NamedCache ticketCert = CacheFactory.getCache(TICKET_CACHE_NAME);
    private static final String DEFAULT_CERT_CACHE_ENABLE = "1";

    
	@Override
	// Anbari : Caching Certificates
	public CertificateTO findCertificateByUsage(CertificateUsage certificateUsage) throws BaseException {

		Integer isCertCacheEnable = Integer
				.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_IS_CERT_CACHE_ENABLE, DEFAULT_CERT_CACHE_ENABLE));
		CertificateTO certificateTO = null;

		if (isCertCacheEnable.intValue() == 1) {
			CertificateWTO certificateWTO = (CertificateWTO) ticketCert.get(certificateUsage);
			if (certificateWTO != null) {
				certificateTO = convertCertWTOtoTO(certificateWTO);
			} else {
				try {
					certificateTO = (CertificateTO) em
							.createQuery(
									"SELECT CRT FROM CertificateTO CRT WHERE CRT.usage =:CERT_USAGE AND CRT.id = (SELECT MAX(CRT.id) FROM CertificateTO CRT WHERE CRT.usage =:CERT_USAGE)")
							.setParameter("CERT_USAGE", certificateUsage).getSingleResult();
					certificateWTO = convertCertTOtoWTO(certificateTO);
					ticketCert.put(certificateUsage, certificateWTO);
				} catch (Exception e) {
					throw new DataException(DataExceptionCode.CRT_006, DataExceptionCode.GLB_005_MSG, e);
				}
			}
		} else {
			try {
				certificateTO = (CertificateTO) em
						.createQuery(
								"SELECT CRT FROM CertificateTO CRT WHERE CRT.usage =:CERT_USAGE AND CRT.id = (SELECT MAX(CRT.id) FROM CertificateTO CRT WHERE CRT.usage =:CERT_USAGE)")
						.setParameter("CERT_USAGE", certificateUsage).getSingleResult();
			} catch (Exception e) {
				throw new DataException(DataExceptionCode.CRT_006, DataExceptionCode.GLB_005_MSG, e);
			}
		}
		return certificateTO;
	}

	private CertificateWTO convertCertTOtoWTO(CertificateTO certificateTO) {
		CertificateWTO certificateWTO = new CertificateWTO();
		certificateWTO.setId(certificateTO.getId());
		certificateWTO.setCertificate(certificateTO.getCertificate());
		certificateWTO.setCode(certificateTO.getCode());
		certificateWTO.setUsage(certificateTO.getUsage());
		return certificateWTO;
	}
	
	private CertificateTO convertCertWTOtoTO(CertificateWTO certificateWTO) {
		CertificateTO certificateTO = new CertificateTO();
		certificateTO.setId(certificateWTO.getId());
		certificateTO.setCertificate(certificateWTO.getCertificate());
		certificateTO.setCode(certificateWTO.getCode());
		certificateTO.setUsage(certificateWTO.getUsage());
		return certificateTO;
	}
    
    
   
   

}
