package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.DocumentTypeTO;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "DocTypeDAO")
@Local(DocTypeDAOLocal.class)
@Remote(DocTypeDAORemote.class)
public class DocTypeDAOImpl extends EmsBaseDAOImpl<DocumentTypeTO> implements DocTypeDAOLocal, DocTypeDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public DocumentTypeTO create(DocumentTypeTO documentTypeTO) throws BaseException {
        try {
            DocumentTypeTO docType = super.create(documentTypeTO);
            em.flush();
            return docType;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("AK_DOC_TYPE_NAME"))
                throw new DAOException(DataExceptionCode.DCT_010, DataExceptionCode.DCT_010_MSG, e);
            throw new DAOException(DataExceptionCode.DCT_009, DataExceptionCode.GLB_004_MSG, e);
        }
    }

    @Override
    public void delete(DocumentTypeTO documentTypeTO) throws BaseException {
        try {
            em.remove(documentTypeTO);
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("FK_DOC_DOC_TYPE_ID"))
                throw new DAOException(DataExceptionCode.DCT_001, DataExceptionCode.DCT_001_MSG, e, new Long[]{documentTypeTO.getId()});
            if (err.contains("FK_SERV_DOC_TYPE_DOC_ID"))
                throw new DAOException(DataExceptionCode.DCT_002, DataExceptionCode.DCT_002_MSG, e, new Long[]{documentTypeTO.getId()});

            throw new DAOException(DataExceptionCode.DCT_003, DataExceptionCode.DCT_003_MSG, e, new Long[]{documentTypeTO.getId()});
        }
    }

    @Override
    public DocumentTypeTO update(DocumentTypeTO documentTypeTO) throws BaseException {
        try {
            DocumentTypeTO docType = super.update(documentTypeTO);
            em.flush();
            return docType;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("AK_DOC_TYPE_NAME"))
                throw new DAOException(DataExceptionCode.DCT_012, DataExceptionCode.DCT_012_MSG, e);
            throw new DAOException(DataExceptionCode.DCT_011, DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public List<DocumentTypeTO> findAll() throws BaseException {
        try {
            return em.createQuery("select dct " +
                    "from DocumentTypeTO dct", DocumentTypeTO.class)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.DCT_004, DataExceptionCode.DCT_004_MSG, e);
        }
    }

    @Override
    public DocumentTypeTO find(Class type, Object id) throws BaseException {
        try {
            return super.find(type, id);
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.DCT_013, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public int deleteDocTypes(List<Long> documentTypeIds) throws BaseException {
        if (documentTypeIds == null || documentTypeIds.size() == 0)
            throw new DAOException(DataExceptionCode.DCT_014, DataExceptionCode.DCT_014_MSG);
        try {
            return em.createQuery("delete from DocumentTypeTO dct " +
                    "where dct.id in (:ids)")
                    .setParameter("ids", documentTypeIds)
                    .executeUpdate();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("FK_DOC_DOC_TYPE_ID"))
                throw new DAOException(DataExceptionCode.DCT_006, DataExceptionCode.DCT_006_MSG, e);
            if (err.contains("FK_SERV_DOC_TYPE_DOC_ID"))
                throw new DAOException(DataExceptionCode.DCT_007, DataExceptionCode.DCT_007_MSG, e);

            throw new DAOException(DataExceptionCode.DCT_008, DataExceptionCode.DCT_008_MSG, e);
        }
    }
}
