package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.ServiceDocumentTypeTO;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author <a href="mailto:haeri@gamelectronics.com.com">Nooshin Haeri</a>
 */
@Stateless(name = "ServiceDocumentTypeDAO")
@Local(ServiceDocumentTypeDAOLocal.class)
public class ServiceDocumentTypeDAOImpl extends EmsBaseDAOImpl<ServiceDocumentTypeTO> implements ServiceDocumentTypeDAOLocal, ServiceDocumentTypeDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public ServiceDocumentTypeTO create(ServiceDocumentTypeTO serviceDocumentTypeTO) throws BaseException {
        try {
            ServiceDocumentTypeTO to = super.create(serviceDocumentTypeTO);
            em.flush();
            return to;
        } catch (BaseException e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("FK_SERV_DOC_TYPE_DOC_ID"))
                throw new DAOException(DataExceptionCode.STI_002, DataExceptionCode.STI_002_MSG, e, new Long[]{serviceDocumentTypeTO.getDocumentType().getId()});
            throw new DAOException(DataExceptionCode.STI_003, DataExceptionCode.GLB_004_MSG, e);
        }
    }

    @Override
    public void deleteByDocType(List<Long> docTypeIds) throws BaseException {
        if (docTypeIds == null || docTypeIds.size() == 0)
            throw new DAOException(DataExceptionCode.STI_004, DataExceptionCode.STI_004_MSG);
        try {
            em.createQuery("delete from ServiceDocumentTypeTO sdt " +
                    "where sdt.documentType.id in (:docTypeIds)")
                    .setParameter("docTypeIds", docTypeIds)
                    .executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.STI_005, DataExceptionCode.GLB_007_MSG);
        }
    }

    @Override
    public List<ServiceDocumentTypeTO> findByServiceId(Integer serviceId) throws BaseException {
        try {
            return em.createQuery("select std from ServiceDocumentTypeTO std " +
                    "where std.serviceId = :serviceId", ServiceDocumentTypeTO.class)
                    .setParameter("serviceId", serviceId)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.STI_001, DataExceptionCode.STI_001_MSG, e, new Integer[]{serviceId});
        }
    }

    @Override
    public List<ServiceDocumentTypeTO> findByDocumentTypeId(Long docTypeId) throws BaseException {
        if (docTypeId == null)
            throw new DAOException(DataExceptionCode.STI_006, DataExceptionCode.STI_006_MSG);
        try {
            return em.createQuery("select std from ServiceDocumentTypeTO  std " +
                    "where std.documentType.id in (:docTypeId)", ServiceDocumentTypeTO.class)
                    .setParameter("docTypeId", docTypeId)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.STI_007, DataExceptionCode.STI_007_MSG, e, new Long[]{docTypeId});
        }
    }
}
