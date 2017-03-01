package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.commons.core.data.dao.factory.DAOFactory;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.profile.ProfileException;
import com.gam.commons.profile.ProfileManager;
import com.gam.commons.security.SecurityUtil;
import com.gam.nocr.ems.config.*;
import com.gam.nocr.ems.data.dao.CertificateDAO;
import com.gam.nocr.ems.data.domain.BusinessLogTO;
import com.gam.nocr.ems.data.domain.CertificateTO;
import com.gam.nocr.ems.data.enums.CertificateUsage;
import gampooya.tools.date.DateUtil;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class BusinessLogDAOImpl
 */
@Stateless(name = "BusinessLogDAO")
@Local(BusinessLogDAOLocal.class)
@Remote(BusinessLogDAORemote.class)
public class BusinessLogDAOImpl extends EmsBaseDAOImpl<BusinessLogTO> implements BusinessLogDAORemote, BusinessLogDAOLocal {

    private CertificateDAO getCertificateDAO() throws ServiceException {
        DAOFactory factory = DAOFactoryProvider.getDAOFactory();
        CertificateDAO certificateDAO = null;
        try {
            certificateDAO = factory.getDAO(EMSLogicalNames.getDaoJNDIName(EMSLogicalNames.DAO_CERTIFICATE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.BZI_004, BizExceptionCode.GLB_001_MSG, e, EMSLogicalNames.DAO_CERTIFICATE.split(","));
        }

        return certificateDAO;
    }

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public void insertLog(BusinessLogTO to) throws BaseException {
        String originalData = to.getAction().name() + to.getEntityID() + to.getEntityName().name() + to.getActor() + to.getAdditionalData() + DateUtil.convert(to.getDate(), "mi");
        byte[] thumbprint = null;
        try {
            thumbprint = SecurityUtil.createThumbprint(originalData.getBytes(), "SHA-1");
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.BZI_003, DataExceptionCode.BZI_003_MSG, e);
        }
        CertificateTO certificateTO = getCertificateDAO().findCertificateByUsage(CertificateUsage.BIZLOG_CER_PRIVATE);
        ProfileManager profileManager = null;
        NOCRPKEParamProviderImpl nocrpkeParamProvider = null;
        try {
            profileManager = ProfileHelper.getProfileManager();
            nocrpkeParamProvider = new NOCRPKEParamProviderImpl(profileManager);

        } catch (ProfileException e) {
            throw new DAOException(DataExceptionCode.BZI_005, DataExceptionCode.BZI_005_MSG, e);
        }
        byte[] digest = null;
        try {
            digest = com.gam.commons.security.signature.Signature.sign(thumbprint, certificateTO.getCertificate(), nocrpkeParamProvider, certificateTO.getCode());
        } catch (SecurityException e) {
            throw new DAOException(DataExceptionCode.BZI_009, e);
        } catch (ProfileException e) {
            throw new DAOException(DataExceptionCode.BZI_010, e);
        }
        to.setDigest(digest);
        to.setCertificateTO(certificateTO);
        try {
            em.persist(to);
            em.flush();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("PK_BIZ_ID"))
                throw new DAOException(DataExceptionCode.BZI_001, DataExceptionCode.BZI_001_MSG, e);
            else
                throw new DAOException(DataExceptionCode.BZI_002, DataExceptionCode.BZI_002_MSG, e);
        }
    }

    @Override
    public boolean verify(String id) throws BaseException {
        BusinessLogTO businessLogTO = em.find(BusinessLogTO.class, Long.valueOf(id));
        String digest = businessLogTO.getAction().name() + businessLogTO.getEntityID() + businessLogTO.getEntityName().name() + businessLogTO.getActor() + businessLogTO.getAdditionalData()
                + DateUtil.convert(businessLogTO.getDate(), "mi");
        boolean verificationResult = false;
        try {
            byte[] thumbprint = SecurityUtil.createThumbprint(digest.getBytes(), "SHA-1");
            if (businessLogTO.getCertificateTO() != null) {
                verificationResult = com.gam.commons.security.signature.Signature.verifySignature(thumbprint, businessLogTO.getCertificateTO().getCertificate(), businessLogTO.getDigest(), businessLogTO
                        .getCertificateTO().getCode());
            } else {
                Object[] args = {businessLogTO.getId()};
                throw new DAOException(DataExceptionCode.BZI_011, DataExceptionCode.BZI_011_MSG, args);
            }
//			/**
//			 * Test code
//			 */
//			CertificateTO gmailCertificateTO = getCertificateDAO().findCertificateByUsage(CertificateUsage.VERIFY_TEST);
//			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
//			InputStream in = new ByteArrayInputStream(gmailCertificateTO.getCertificate());
//			X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);
//			ProfileManager profileManager = ProfileHelper.getProfileManager();
//			NOCRPKEParamProviderImpl nocrpkeParamProvider = new NOCRPKEParamProviderImpl(profileManager);
//			nocrpkeParamProvider.setValidationMethod("OCSP");
//			CertificateUtil.verifyCerificate(cert, nocrpkeParamProvider,nocrpkeParamProvider.getValidationMethod());
//			/**
//			 * Test code
//			 */
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.BZI_004, DataExceptionCode.BZI_004_MSG, e);
        }
        return verificationResult;
    }

}
