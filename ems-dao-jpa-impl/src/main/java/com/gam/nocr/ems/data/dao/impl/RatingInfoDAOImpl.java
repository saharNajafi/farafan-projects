package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.RatingInfoTO;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "RatingInfoDAO")
@Local(RatingInfoDAOLocal.class)
@Remote(RatingInfoDAORemote.class)
public class RatingInfoDAOImpl extends EmsBaseDAOImpl<RatingInfoTO> implements RatingInfoDAOLocal, RatingInfoDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public RatingInfoTO create(RatingInfoTO ratingInfoTO) throws BaseException {
        try {
            RatingInfoTO ratingInfo = super.create(ratingInfoTO);
            em.flush();
            return ratingInfo;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("AK_RATING_CLASS"))
                throw new DAOException(DataExceptionCode.RTI_009, DataExceptionCode.RTI_009_MSG, e);
            if (err.contains("AK_RATING_SIZE"))
                throw new DAOException(DataExceptionCode.RTI_010, DataExceptionCode.RTI_010_MSG, e);
            throw new DAOException(DataExceptionCode.RTI_008, DataExceptionCode.GLB_004_MSG, e);
        }
    }

    @Override
    public RatingInfoTO update(RatingInfoTO ratingInfoTO) throws BaseException {
        try {
            RatingInfoTO ratingInfo = super.update(ratingInfoTO);
            em.flush();
            return ratingInfo;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("AK_RATING_CLASS"))
                throw new DAOException(DataExceptionCode.RTI_012, DataExceptionCode.RTI_012_MSG, e);
            if (err.contains("AK_RATING_SIZE"))
                throw new DAOException(DataExceptionCode.RTI_013, DataExceptionCode.RTI_013_MSG, e);
            throw new DAOException(DataExceptionCode.RTI_011, DataExceptionCode.GLB_004_MSG, e);
        }
    }

    @Override
    public void delete(RatingInfoTO ratingInfoTO) throws BaseException {
        try {
            em.remove(ratingInfoTO);
            em.flush();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("FK_ENROLL_OFC_RAT_ID"))
                throw new DAOException(DataExceptionCode.RTI_004, DataExceptionCode.RTI_004_MSG, e, new Long[]{ratingInfoTO.getId()});
            throw new DAOException(DataExceptionCode.RTI_006, DataExceptionCode.RTI_006_MSG, e);
        }
    }

    @Override
    public boolean deleteRatingInfos(List<Long> ratingInfoIds) throws BaseException {
        try {
            Query q = null;
            if (ratingInfoIds == null) {
                q = em.createQuery("delete from RatingInfoTO rat");
            } else {
                q = em.createQuery("delete from RatingInfoTO rat " +
                        "where rat.id in (:ids)")
                        .setParameter("ids", ratingInfoIds);
            }
            int count = q.executeUpdate();
            em.flush();
            if (ratingInfoIds == null)
                return true;
            return count == ratingInfoIds.size();
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("FK_ENROLL_OFC_RAT_ID"))
                throw new DAOException(DataExceptionCode.RTI_005, DataExceptionCode.RTI_005_MSG, e);
            throw new DAOException(DataExceptionCode.RTI_002, DataExceptionCode.RTI_002_MSG, e);
        }
    }

    @Override
    public RatingInfoTO find(Class type, Object id) throws BaseException {
        try {
            return super.find(type, id);
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RTI_007, DataExceptionCode.RTI_007_MSG, e);
        }
    }

    @Override
    public List<RatingInfoTO> findAll() throws BaseException {
        try {
            return em.createQuery("select rat " +
                    "from RatingInfoTO rat", RatingInfoTO.class)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RTI_001, DataExceptionCode.RTI_001_MSG, e);
        }
    }

    /**
     * The method findModifiedRatingInfo is used to find the instances of type {@link
     * com.gam.nocr.ems.data.domain.RatingInfoTO} which are new or
     * modified.
     *
     * @return a list fo type {@link com.gam.nocr.ems.data.domain.RatingInfoTO}
     * @throws com.gam.commons.core.BaseException
     *
     */
    @Override
    public List<RatingInfoTO> findModifiedRatingInfo() throws BaseException {
        try {
            return em.createQuery("SELECT RAT " +
                    "FROM RatingInfoTO RAT " +
                    "WHERE RAT.modified = :MODIFIED_FLAG", RatingInfoTO.class)
                    .setParameter("MODIFIED_FLAG", true)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RTI_014, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    /**
     * The method updateModifiedFields is used to update the field 'modified' of a list of type {@link
     * com.gam.nocr.ems.data.domain.RatingInfoTO}
     *
     * @param idList is a list of type {@link Long}
     * @param flag   is an instance of type {@link Boolean}
     * @throws com.gam.commons.core.BaseException
     *
     */
    @Override
    public void updateModifiedFields(List<Long> idList,
                                     Boolean flag) throws BaseException {
        try {
            em.createQuery("UPDATE RatingInfoTO RAT " +
                    "SET RAT.modified = :MODIFIED_FLAG " +
                    "WHERE RAT.id IN (:ID_LIST)")
                    .setParameter("MODIFIED_FLAG", flag)
                    .setParameter("ID_LIST", idList)
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.RTI_015, DataExceptionCode.GLB_006_MSG, e);
        }
    }

    @Override
    public RatingInfoTO findByRatingInfoId(Long id)  {
        List<RatingInfoTO> ratingInfoTOList;
         ratingInfoTOList = em.createNamedQuery(
                "RatingInfoTO.findByRatingInfoId")
                .setParameter("id", id)
                .getResultList();

        return ratingInfoTOList != null ? ratingInfoTOList.get(0) : null;
    }
}
