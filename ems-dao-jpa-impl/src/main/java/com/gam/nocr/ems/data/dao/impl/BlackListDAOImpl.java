package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.BlackListTO;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "BlackListDAO")
@Local(BlackListDAOLocal.class)
public class BlackListDAOImpl extends EmsBaseDAOImpl<BlackListTO> implements BlackListDAOLocal, BlackListDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public BlackListTO find(Class type, Object id) throws BaseException {
        try {
            return super.find(type, id);
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.BLI_001, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public BlackListTO update(BlackListTO blackListTO) throws BaseException {
        try {
            BlackListTO blackListItem = super.update(blackListTO);
            em.flush();
            return blackListItem;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("AK_BLK_LST_NATIONAL_ID"))
                throw new DAOException(DataExceptionCode.BLI_007, DataExceptionCode.BLI_007_MSG, e);
            throw new DAOException(DataExceptionCode.BLI_008, DataExceptionCode.GLB_004_MSG, e);
        }
    }

    @Override
    public BlackListTO create(BlackListTO blackListTO) throws BaseException {
        try {
            BlackListTO blackListItem = super.create(blackListTO);
            em.flush();
            return blackListItem;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("AK_BLK_LST_NATIONAL_ID"))
                throw new DAOException(DataExceptionCode.BLI_006, DataExceptionCode.BLI_006_MSG, e);
            throw new DAOException(DataExceptionCode.BLI_003, DataExceptionCode.GLB_004_MSG, e);
        }
    }

    @Override
    public void delete(BlackListTO blackListTO) throws BaseException {
        try {
            super.delete(blackListTO);
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.BLI_002, DataExceptionCode.GLB_007_MSG, e);
        }
    }

    @Override
    public List<BlackListTO> findAll() throws BaseException {
        try {
            return em.createQuery("select blk " +
                    "from BlackListTO blk", BlackListTO.class)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.BLI_004, DataExceptionCode.BLI_004_MSG, e);
        }
    }

    @Override
    public List<BlackListTO> findByNid(String nid) throws BaseException {
        try {
            return em.createQuery("select blk " +
                    "from BlackListTO blk " +
                    "where blk.nationalId = :nid", BlackListTO.class)
                    .setParameter("nid", nid)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.BLI_009, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public boolean deleteBlackListItems(List<Long> blackListIds) throws BaseException {
        try {
            Query q = null;
            if (blackListIds == null) {
                q = em.createQuery("delete from BlackListTO blk");
            } else {
                q = em.createQuery("delete from BlackListTO blk " +
                        "where blk.id in (:ids)")
                        .setParameter("ids", blackListIds);
            }
            int count = q.executeUpdate();
            em.flush();
            if (blackListIds == null)
                return true;
            return count == blackListIds.size();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.BLI_005, DataExceptionCode.BLI_005_MSG, e);
        }
    }
}
