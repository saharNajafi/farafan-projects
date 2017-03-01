package com.gam.nocr.ems.data.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.DocumentTO;
import com.gam.nocr.ems.data.domain.DocumentTypeTO;
import com.gam.nocr.ems.util.EmsUtil;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "DocumentDAO")
@Local(DocumentDAOLocal.class)
public class DocumentDAOImpl extends EmsBaseDAOImpl<DocumentTO> implements DocumentDAOLocal, DocumentDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public DocumentTO create(DocumentTO documentTO) throws BaseException {
        try {
            DocumentTO to = super.create(documentTO);
            em.flush();
            return to;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("FK_DOC_CITZN_INF_ID")) {
                throw new DAOException(DataExceptionCode.DCI_003, DataExceptionCode.DCI_003_MSG, e, new Long[]{documentTO.getCitizenInfo().getId()});
            }
            if (err.contains("FK_DOC_DOC_TYPE_ID")) {
                throw new DAOException(DataExceptionCode.DCI_004, DataExceptionCode.DCI_004_MSG, e, new Long[]{documentTO.getType().getId()});
            }
            if (err.contains("AK_DOC_TYPE_ID_CTZ_INFO_ID")) {
                throw new DAOException(DataExceptionCode.DCI_006, DataExceptionCode.DCI_006_MSG + documentTO.getCitizenInfo().getId(), e);
            }
            throw new DAOException(DataExceptionCode.DCI_001, DataExceptionCode.DCI_001_MSG, e);
        }
    }

    @Override
    public List<DocumentTO> findByRequestIdAndType(Long requestId, DocumentTypeTO docType) throws BaseException {
        try {
            Query q;
            if (docType == null) {
                q = em.createQuery("select doc " +
                        "from DocumentTO doc " +
                        "where doc.citizenInfo.id in (" +
                        "select czi.id from CitizenInfoTO czi, CitizenTO ctz, CardRequestTO crq " +
                        "where czi.id = ctz.citizenInfo.id " +
                        "and ctz.id = crq.citizen.id " +
                        "and crq.id = :requestId)", DocumentTO.class)
                        .setParameter("requestId", requestId);
            } else {
                q = em.createQuery("select doc " +
                        "from DocumentTO doc, CardRequestTO crq " +
                        "where doc.type = :docType " +
                        "and doc.citizenInfo.id in (" +
                        "select czi.id from CitizenInfoTO czi, CitizenTO ctz, CardRequestTO crq " +
                        "where czi.id = ctz.citizenInfo.id " +
                        "and ctz.id = crq.citizen.id " +
                        "and crq.id = :requestId)", DocumentTO.class)
                        .setParameter("requestId", requestId)
                        .setParameter("docType", docType);
            }

            return q.getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.DCI_005, DataExceptionCode.DCI_005_MSG, e, new Long[]{requestId});
        }
    }

    //Edited By Adldoost
    @Override
    public Integer removeByRequestIdAndType(Long requestId, List<DocumentTypeTO> documentTypeTOs) throws BaseException {
        Integer result = 0;
        try {
            if (!EmsUtil.checkListSize(documentTypeTOs)) {
                result = em.createQuery("delete from DocumentTO doc " +
                        "where doc.citizenInfo.id in " +
                        "(select czi.id from CitizenInfoTO czi, CitizenTO ctz, CardRequestTO crq " +
                        "where czi.id = ctz.citizenInfo.id " +
                        "and ctz.id = crq.citizen.id " +
                        "and crq.id = :requestId)")
                        .setParameter("requestId", requestId)
                        .executeUpdate();
//                if (d != 0)
//                    result++;
            } else {
            	List<Long> doctypeIds = new ArrayList<Long>();
                for (DocumentTypeTO documentType : documentTypeTOs) {
                	doctypeIds.add(documentType.getId());
                }
                
                    result = em.createQuery("delete from DocumentTO doc " +
                            "where doc.citizenInfo.id in " +
                            "(select czi.id from CitizenInfoTO czi, CitizenTO ctz, CardRequestTO crq " +
                            "where czi.id = ctz.citizenInfo.id " +
                            "and ctz.id = crq.citizen.id " +
                            "and crq.id = :requestId) " +
                            "and doc.type.id in (:docTypeId)")
                            .setParameter("requestId", requestId)
                            .setParameter("docTypeId", doctypeIds)
                            .executeUpdate();
//                    if (d != 0)
//                        result++;
                //}
            }

            em.flush();
            return result;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.DCI_002, DataExceptionCode.DCI_002_MSG, e, new Long[]{requestId});
        }
    }
}
